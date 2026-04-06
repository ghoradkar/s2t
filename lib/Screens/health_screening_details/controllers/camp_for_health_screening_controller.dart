import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
// import 'package:s2toperational/Screens/health_screening_details/models/camp_regular_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

import '../models/camp_regular_model.dart';

class CampForHealthScreeningController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final RxList<ResourceReMappingCampOutput> campList =
      <ResourceReMappingCampOutput>[].obs;
  final RxList<ResourceReMappingCampOutput> searchList =
      <ResourceReMappingCampOutput>[].obs;
  final RxBool isLoading = false.obs;
  final RxString selectedCampDate = ''.obs;

  int subOrgId = 0;
  int empCode = 0;
  int dESGID = 0;

  final TextEditingController dateController = TextEditingController();
  final TextEditingController searchController = TextEditingController();

  @override
  void onClose() {
    dateController.dispose();
    searchController.dispose();
    super.onClose();
  }

  // Selected camp data
  int selectedDistLgdCode = 0;
  String selectedDistName = '';
  int selectedCampId = 0;
  int selectedCampType = 0;

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    subOrgId = userData?.subOrgId ?? 0;
    empCode = userData?.empCode ?? 0;
    dESGID = userData?.dESGID ?? 0;
    selectedCampDate.value = FormatterManager.formatDateToString(DateTime.now());
    dateController.text = selectedCampDate.value;
    fetchCamps();
  }

  Future<void> fetchCamps() async {
    isLoading.value = true;
    final response = await _repo.getApprovedCampList(
      campDate: selectedCampDate.value,
      subOrgId: subOrgId,
      userId: empCode,
      dESGID: dESGID,
    );
    if (response != null) {
      campList.assignAll(response.output ?? []);
      searchList.assignAll(campList);
    } else {
      campList.clear();
      searchList.clear();
      ToastManager.toast('Failed to load camps');
    }
    isLoading.value = false;
  }

  void filterBySearch(String query) {
    if (query.isEmpty) {
      searchList.assignAll(campList);
    } else {
      searchList.assignAll(
        campList.where((item) {
          final id = item.campId?.toString().toLowerCase() ?? '';
          return id.contains(query.toLowerCase());
        }).toList(),
      );
    }
  }

  Future<void> onDateChanged(DateTime picked) async {
    selectedCampDate.value = FormatterManager.formatDateToString(picked);
    dateController.text = selectedCampDate.value;
    searchController.clear();
    await fetchCamps();
  }

  Future<void> onCampSelected(
    ResourceReMappingCampOutput camp,
    VoidCallback onNavigate,
  ) async {
    selectedDistLgdCode = camp.dISTLGDCODE ?? 0;
    selectedDistName = camp.dISTNAME ?? '';
    selectedCampId = camp.campId ?? 0;
    selectedCampType = camp.campType ?? 0;

    if (dESGID == 29 ||
        dESGID == 92 ||
        dESGID == 104 ||
        dESGID == 160 ||
        dESGID == 105 ||
        dESGID == 77 ||
        dESGID == 84 ||
        dESGID == 30 ||
        dESGID == 108 ||
        dESGID == 147 ||
        dESGID == 130) {
      onNavigate();
    } else {
      if (DataProvider().getRegularCamp()) {
        await _validateRegular(onNavigate);
      } else {
        await _validateD2D(onNavigate);
      }
    }
  }

  Future<void> _validateRegular(VoidCallback onNavigate) async {
    final response = await _repo.getUserCampMappingAndAttendanceStatusRegular(
      campDate: selectedCampDate.value,
      userId: empCode,
      distLgdCode: selectedDistLgdCode,
      campType: selectedCampType,
      campId: selectedCampId,
    );

    if (response == null) {
      ToastManager.toast('Validation failed. Please try again.');
      return;
    }

    final obj = response.output?.first;
    if (obj == null) return;

    if (obj.isCampClosed == 1) {
      ToastManager.showAlertDialog(Get.context!, 'This camp is closed', () {
        Get.back();
      });
      return;
    }
    if (obj.campFlag == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'This camp not mapped to you',
        () => Get.back(),
      );
      return;
    }
    if (obj.isReadinessFormFilled == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Readiness form is not filled. Please contact camp coordinator',
        () => Get.back(),
      );
      return;
    }
    if (obj.attendanceFlag == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Attendance not marked. Please mark attendance first',
        () => Get.back(),
      );
      return;
    }

    onNavigate();
  }

  Future<void> _validateD2D(VoidCallback onNavigate) async {
    final response =
        await _repo.getUserCampMappingAndAttendanceStatusReadiness(
      campDate: selectedCampDate.value,
      userId: empCode,
      distLgdCode: selectedDistLgdCode,
      campType: selectedCampType,
      campId: selectedCampId,
    );

    if (response == null) {
      ToastManager.toast('Validation failed. Please try again.');
      return;
    }

    final obj = response.output?.first;
    if (obj == null) return;

    if (obj.isCampClosed == 1) {
      ToastManager.showAlertDialog(Get.context!, 'This camp is closed', () {
        Get.back();
      });
      return;
    }
    if (obj.isReadinessFormFilled == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Readiness form is not filled. Please contact camp coordinator',
        () => Get.back(),
      );
      return;
    }
    if (obj.campFlag == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'This camp not mapped to you',
        () => Get.back(),
      );
      return;
    }
    if (obj.attendanceFlag == 0) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Please mark attendance first',
        () => Get.back(),
      );
      return;
    }

    onNavigate();
  }
}