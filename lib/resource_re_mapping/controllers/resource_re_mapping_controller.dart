// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/login/models/login_response_model.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../models/GetCampAssignUserResponse.dart';
import '../models/ResourceReMappingCampResponse.dart';
import '../repository/resource_re_mapping_repository.dart';
import '../screens/resource_re_mapping_update_screen.dart';

class ResourceReMappingController extends GetxController {
  final _repository = ResourceReMappingRepository();

  int dESGID = 0;
  int subOrgId = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;

  String selectedCampDate = '';
  List<ResourceReMappingCampOutput> campList = [];
  List<ResourceReMappingCampOutput> searchCampList = [];

  @override
  void onInit() {
    super.onInit();
    selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    final LoginOutput? user =
        DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    empCode = user?.empCode ?? 0;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    fetchCampList();
  }

  void filterSearch(String query) {
    searchCampList = campList.where((item) {
      final id = item.campId?.toString().toLowerCase() ?? '';
      return id.contains(query.toLowerCase());
    }).toList();
    update();
  }

  Future<void> selectDate() async {
    final DateTime? picked = await showDatePicker(
      context: Get.context!,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      selectedCampDate = FormatterManager.formatDateToString(picked);
      await fetchCampList();
    }
  }

  Future<void> fetchCampList() async {
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchApprovedCampList({
        'CampDATE': selectedCampDate,
        'SubOrgId': subOrgId.toString(),
        'Divison': '0',
        'DISTLGDCODE': '0',
        'USERID': empCode.toString(),
        'DesgId': dESGID.toString(),
      });
      campList = response.output ?? [];
      searchCampList = campList;
    } catch (e) {
      campList = [];
      searchCampList = [];
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  Future<void> onCampRowTap(ResourceReMappingCampOutput camp) async {
    final int createdBy = camp.createdBy ?? 0;
    final bool isDesigRestricted = dESGID == 29 ||
        dESGID == 103 ||
        dESGID == 84 ||
        dESGID == 100 ||
        dESGID == 34 ||
        dESGID == 104 ||
        dESGID == 107 ||
        dESGID == 113 ||
        dESGID == 92 ||
        dESGID == 162;

    if (isDesigRestricted) {
      if (empCode != createdBy) {
        ToastManager.toast(
          'This camp not created by you, please select created camp',
        );
        return;
      }
      if (camp.isRegdDone == 1) {
        ToastManager.showAlertDialog(
          Get.context!,
          'या कॅम्पमध्ये काही patient registration झालेले असल्यामुळे नवीन phlebotomist/doctor जोडण्यास किंवा हटवण्यास परवानगी नाही.',
          () {
            Get.back();
          },
          title: 'Alert',
        );
        return;
      }
      await _checkMappingStatusAndNavigate(camp);
    } else {
      await _checkAssignedUserAndNavigate(camp);
    }
  }

  Future<void> _checkMappingStatusAndNavigate(
    ResourceReMappingCampOutput camp,
  ) async {
    try {
      final response = await _repository.fetchCampMappingStatus({
        'CampDATE': selectedCampDate,
        'UserId': empCode.toString(),
        'DISTLGDCODE': dISTLGDCODE.toString(),
        'CampType': camp.campType?.toString() ?? '',
        'CampID': camp.campId?.toString() ?? '',
        'TestId': '1',
      });
      final obj = response.output?.first;
      if (obj != null) {
        if (obj.isCampClosed == 1) {
          ToastManager.toast('This camp is closed');
        } else {
          Get.to(
            () => ResourceReMappingUpdateScreen(reMappingCampOutput: camp),
          );
        }
      }
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  Future<void> _checkAssignedUserAndNavigate(
    ResourceReMappingCampOutput camp,
  ) async {
    try {
      final response = await _repository.fetchCampAssignUserList({
        'campid': camp.campId?.toString() ?? '',
        'ResourceUserId': empCode.toString(),
        'TestId': '0',
      });
      final list = response.output ?? [];
      bool allow = false;

      if (list.isEmpty) {
        ToastManager.toast('You are not authorise to Conduct this camp');
        return;
      }

      for (GetCampAssignUserOutput obj in list) {
        if (obj.resourceUserId == empCode && obj.statusRes == 1) {
          allow = true;
        }
      }

      if (allow) {
        Get.to(() => ResourceReMappingUpdateScreen(reMappingCampOutput: camp));
      } else {
        ToastManager.toast('You are not authorise to Conduct this camp');
      }
    } catch (e) {
      ToastManager.toast('You are not authorise to Conduct this camp');
    }
  }
}
