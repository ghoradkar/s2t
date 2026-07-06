// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import '../models/campId_list_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/camp_type_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/district_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Screens/camp_readiness_form/models/camp_readiness_form_list_response.dart';
import 'package:s2toperational/Screens/camp_readiness_form/models/camp_readiness_form_submitt_response.dart';
import 'package:s2toperational/Screens/camp_readiness_form/repository/camp_readiness_form_repository.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/team_number_by_campId_and_user_id_list_response.dart';

class CampReadinessFormController extends GetxController {
  final CampReadinessFormRepository _repo = CampReadinessFormRepository();

  // â”€â”€â”€ User data â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  String campDate = '';
  int dESGID = 0;
  int empCode = 0;
  int districtId = 0;

  // â”€â”€â”€ Selections â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  CampTypeOutput? selectedCampType;
  CampIdOutput? selectedCampID;
  DistrictOutput? selectedDistrict;
  String teamId = '0';
  String teamName = '';

  // â”€â”€â”€ Flags â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  bool showTeam = false;
  bool showSubmitButton = true;
  bool isFormSubmitted = false;

  // â”€â”€â”€ Form items list â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  List<CampReadinessFormOutput> campReadinessList = [];

  // â”€â”€â”€ Lifecycle â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @override
  void onInit() {
    super.onInit();
    campDate = FormatterManager.formatDateToString(DateTime.now());
    final user = DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    empCode = user?.empCode ?? 0;
    districtId = user?.dISTLGDCODE ?? 0;
  }

  // â”€â”€â”€ District â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<List<DistrictOutput>> fetchDistrict() async {
    ToastManager.showLoader();
    try {
      final resp = await _repo.getDistrict(empCode: empCode.toString());
      if (resp.statusCode == 200) {
        final parsed = DistrictResponse.fromJson(jsonDecode(resp.body));
        if (parsed.status == 'Success') return parsed.output ?? [];
        ToastManager.toast(parsed.message ?? '');
      }
    } catch (e) {
      debugPrint('fetchDistrict error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
    }
    return [];
  }

  void onDistrictSelected(DistrictOutput? district) {
    selectedDistrict = district;
    selectedCampID = null;
    teamId = '0';
    teamName = '';
    showTeam = false;
    campReadinessList = [];
    showSubmitButton = true;
    isFormSubmitted = false;
    update();
  }

  // â”€â”€â”€ Camp Type â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<List<CampTypeOutput>> fetchCampType() async {
    ToastManager.showLoader();
    try {
      if (dESGID == 129 || dESGID == 146) {
        final resp = await _repo.getCampTypeFlexi();
        ToastManager.hideLoader();
        if (resp.statusCode == 200) {
          final parsed = CampTypeResponse.fromJson(jsonDecode(resp.body));
          if (parsed.status?.toLowerCase() == 'success') return parsed.output ?? [];
          ToastManager.toast(parsed.message ?? '');
        }
      } else if (dESGID == 138 ||
          dESGID == 137 ||
          dESGID == 169 ||
          dESGID == 177 ||
          dESGID == 31 ||
          dESGID == 176) {
        final resp = await _repo.getCampTypeMMU();
        ToastManager.hideLoader();
        if (resp.statusCode == 200) {
          final parsed = CampTypeResponse.fromJson(jsonDecode(resp.body));
          if (parsed.status?.toLowerCase() == 'success') return parsed.output ?? [];
          ToastManager.toast(parsed.message ?? '');
        }
      } else if (dESGID == 35 || dESGID == 86) {
        ToastManager.hideLoader();
        return [CampTypeOutput(cAMPTYPE: 3, campTypeDescription: 'DOOR TO DOOR')];
      } else {
        ToastManager.hideLoader();
        return [CampTypeOutput(cAMPTYPE: 1, campTypeDescription: 'REGULAR')];
      }
    } catch (e) {
      debugPrint('fetchCampType error: $e');
      ToastManager.hideLoader();
      ToastManager.toast('Something went wrong');
    }
    return [];
  }

  void onCampTypeSelected(CampTypeOutput? campType) {
    selectedCampType = campType;
    selectedCampID = null;
    teamId = '0';
    teamName = '';
    showTeam = false;
    campReadinessList = [];
    showSubmitButton = true;
    isFormSubmitted = false;
    update();
  }

  // â”€â”€â”€ Camp ID â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<List<CampIdOutput>> fetchCampList() async {
    if (selectedDistrict == null) {
      ToastManager.toast('Please select District');
      return [];
    }
    if (selectedCampType == null) {
      ToastManager.toast('Please select Camp Type');
      return [];
    }
    ToastManager.showLoader();
    try {
      final distCode =
          (selectedDistrict?.dISTLGDCODE ?? districtId).toString();
      final resp = await _repo.getCampList(
        campDate: campDate,
        empCode: empCode.toString(),
        distCode: distCode,
        campType: selectedCampType!.cAMPTYPE.toString(),
      );
      ToastManager.hideLoader();
      if (resp.statusCode == 200) {
        final parsed = CampIdListResponse.fromJson(jsonDecode(resp.body));
        if (parsed.status == 'Success') return parsed.output ?? [];
        ToastManager.toast(parsed.message ?? '');
      }
    } catch (e) {
      debugPrint('fetchCampList error: $e');
      ToastManager.hideLoader();
      ToastManager.toast('Something went wrong');
    }
    return [];
  }

  Future<void> onCampIdSelected(CampIdOutput? campId) async {
    selectedCampID = campId;
    update();
    final needsTeam = dESGID == 35 ||
        dESGID == 129 ||
        dESGID == 86 ||
        dESGID == 146 ||
        dESGID == 138 ||
        dESGID == 137 ||
        dESGID == 169 ||
        dESGID == 177 ||
        dESGID == 31 ||
        dESGID == 176;
    if (needsTeam) {
      await _fetchTeamId();
    } else {
      await _fetchFormItems();
    }
  }

  // â”€â”€â”€ Team ID â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> _fetchTeamId() async {
    try {
      final resp = await _repo.getTeamNumber(
        campId: selectedCampID!.campId.toString(),
        empCode: empCode.toString(),
      );
      if (resp.statusCode == 200) {
        final parsed = TeamNumberByCampIdAndUserIdListResponse.fromJson(
          jsonDecode(resp.body),
        );
        if (parsed.status == 'Success') {
          final first = parsed.output?.first;
          if (first != null) {
            teamId = first.teamNumber ?? '0';
            teamName = first.teamName ?? '';
            showTeam = true;
            update();
            await _fetchFormItems();
          } else {
            ToastManager.toast('Data not found');
          }
        } else {
          ToastManager.toast(parsed.message ?? '');
        }
      }
    } catch (e) {
      debugPrint('_fetchTeamId error: $e');
      ToastManager.toast('Something went wrong');
    }
  }

  // â”€â”€â”€ Form items â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> _fetchFormItems() async {
    ToastManager.showLoader();
    try {
      final resp = await _repo.getCampReadinessFormItems(
        campId: selectedCampID?.campId ?? 0,
        teamId: int.tryParse(teamId) ?? 0,
      );
      ToastManager.hideLoader();
      if (resp.statusCode == 200) {
        final parsed =
            CampReadinessFormListResponse.fromJson(jsonDecode(resp.body));
        if (parsed.status == 'Success') {
          campReadinessList = parsed.output ?? [];
          if (campReadinessList.isNotEmpty) {
            final first = campReadinessList.first;
            if (first.itemStatus != null) {
              showSubmitButton = false;
              isFormSubmitted = true;
            } else {
              showSubmitButton = true;
              isFormSubmitted = false;
            }
          }
        } else {
          campReadinessList = [];
          ToastManager.toast(parsed.message ?? '');
        }
      }
    } catch (e) {
      debugPrint('_fetchFormItems error: $e');
      ToastManager.hideLoader();
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  // â”€â”€â”€ Submit â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  void validations() {
    bool isSuccess = true;
    final List<Map<String, String>> items = [];

    for (final obj in campReadinessList) {
      items.add({
        'ItemId': obj.itemId.toString(),
        'ItemStatus': obj.itemStatus.toString(),
      });
      if (obj.itemStatus == 0 || obj.itemStatus == null) {
        isSuccess = false;
        break;
      }
    }

    if (isSuccess) {
      _submitData(_encodeJson(items));
    } else {
      ToastManager.toast('Please select all the items.');
    }
  }

  String _encodeJson(List<Map<String, dynamic>> items) {
    try {
      return jsonEncode(items);
    } catch (e) {
      print('Error encoding JSON: $e');
      return '';
    }
  }

  Future<void> _submitData(String formJson) async {
    ToastManager.showLoader();
    try {
      final resp = await _repo.insertCampReadinessFormDetails(
        campId: selectedCampID?.campId.toString() ?? '0',
        campType: selectedCampType?.cAMPTYPE.toString() ?? '0',
        createdBy: empCode.toString(),
        teamId: teamId,
        formJson: formJson,
      );
      if (resp.statusCode == 200) {
        final parsed =
            CampReadinessFormSubmittResponse.fromJson(jsonDecode(resp.body));
        if (parsed.status == 'Success') {
          await _fetchFormItems();
        } else {
          ToastManager.hideLoader();
          ToastManager.toast(parsed.message ?? '');
        }
      } else {
        ToastManager.hideLoader();
      }
    } catch (e) {
      debugPrint('_submitData error: $e');
      ToastManager.hideLoader();
      ToastManager.toast('Something went wrong');
    }
  }
}