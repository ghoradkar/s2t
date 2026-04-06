// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/LabDataResponse/LabDataResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryToTeamResponse/RecollectionBeneficiaryToTeamResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/repository/daily_work_dashboard_repository.dart';

import '../model/BindDistrictResponse.dart';
import '../model/BindDivisionResponse.dart';
import '../model/LabDataResponse.dart';
import '../model/LandingLabCampCreationResponse.dart';
import '../model/RecollectionBeneficiaryToTeamResponse.dart';
import '../model/SubOrganizationResponse.dart';

class DailyWorkDashboardController extends GetxController {
  final DailyWorkDashboardRepository _repository = DailyWorkDashboardRepository();

  // ─── User info ────────────────────────────────────────────────────────────────

  int dESGID = 0;
  int empCode = 0;

  // ─── Role helpers ─────────────────────────────────────────────────────────────

  bool get isGroup1 => [86, 64, 35, 129, 146].contains(dESGID);
  bool get isGroup2 => [92, 29, 160, 104, 162, 78, 77, 128, 30, 108, 84, 139, 136].contains(dESGID);
  bool get isGroup3 => [170, 171, 182, 183].contains(dESGID);
  bool get isAdminDesignation => [
        170, 171, 51, 26, 30, 128, 182, 183, 78,
        47, 83, 101, 102, 103, 168, 173, 196, 198, 60, 61, 69, 79,
      ].contains(dESGID);

  // ─── Loading / filter visibility ─────────────────────────────────────────────

  bool isLoading = true;
  bool isShowFilter = false;

  // ─── Camp type toggle ─────────────────────────────────────────────────────────

  bool regularCamp = true;
  bool doorToDoorCamp = false;

  // ─── Date range ───────────────────────────────────────────────────────────────

  String fromDate = "";
  String toDate = "";

  // ─── Filter selections ────────────────────────────────────────────────────────

  SubOrganizationOutput? selectedSubOrganization;
  BindDivisionOutput? selectedDivision;
  BindDistrictOutput? selectedDistrict;
  LandingLabCampCreationOutput? selectedLabVal;

  // ─── Dashboard counts ─────────────────────────────────────────────────────────

  int? rejectedBeneficiaryCount;
  int? screeningConfimPendingCount;
  int? assignmentPendingCount;
  int? interestedInScreeningCount;
  int? notinterestedInScreeningCount;
  int? wrongNumberCount;
  int? notAvailableForScreeningCount;
  int? sampleCollectedCount;
  int? reScreeningPendingCount;
  int? deniedScreeningCount;

  // ─── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    empCode = user?.empCode ?? 0;

    const adminDesignations = [
      170, 171, 51, 26, 30, 128, 182, 183, 78,
      47, 83, 101, 102, 103, 168, 173, 196, 198, 60, 61, 69, 79,
    ];
    final int daysBack = adminDesignations.contains(dESGID) ? 10 : 7;
    final DateTime firstDate = DateTime.now().subtract(Duration(days: daysBack));
    fromDate = FormatterManager.formatDateToString(firstDate);
    toDate = FormatterManager.formatDateToString(DateTime.now());

    callAPI();
  }

  // ─── Main data load ───────────────────────────────────────────────────────────

  Future<void> callAPI() async {
    isLoading = true;
    update();
    bool didCall = false;
    if (isGroup1) {
      didCall = true;
      final params = {
        "Fromdate": fromDate,
        "Todate": toDate,
        "Arid": "0",
        "BeneficiaryNumber": "0",
        "UserId": empCode.toString(),
        "Type": "1",
        "CampType": regularCamp ? "1" : "3",
      };
      final response = await _repository.getCountForPageloadForTeam(params);
      if (response != null) {
        showDateInUI(response.output ?? []);
      } else {
        resetUI();
        ToastManager.toast('Something went wrong');
      }
    } else if ([92, 29, 160, 104, 162, 78, 77, 128, 30, 108, 84, 139, 136, 51, 170, 171, 182, 183, 26]
        .contains(dESGID)) {
      didCall = true;
      final params = {
        "Fromdate": fromDate,
        "Todate": toDate,
        "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
        "DIVID": selectedDivision?.dIVID.toString() ?? "0",
        "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
        "TALLGDCODE": "0",
        "Labcode": selectedLabVal?.labCode.toString() ?? "0",
        "Arid": "0",
        "BeneficiaryNumber": "0",
        "UserId": empCode.toString(),
        "Type": "1",
        "DesgID": "$dESGID",
        "CampType": regularCamp ? "1" : "3",
      };
      final response = await _repository.getCountForPageload(params);
      if (response != null) {
        showDateInUI(response.output ?? []);
      } else {
        resetUI();
        ToastManager.toast('Something went wrong');
      }
    }
    if (!didCall) {
      isLoading = false;
    } else {
      isLoading = false;
    }
    update();
  }

  void showDateInUI(List<RecollectionBeneficiaryToTeamOutput> output) {
    resetUI();
    for (final item in output) {
      switch (item.sequenceNo) {
        case 0: rejectedBeneficiaryCount = item.patientCount ?? 0; break;
        case 1: screeningConfimPendingCount = item.patientCount ?? 0; break;
        case 2: assignmentPendingCount = item.patientCount ?? 0; break;
        case 3: interestedInScreeningCount = item.patientCount ?? 0; break;
        case 4: notinterestedInScreeningCount = item.patientCount ?? 0; break;
        case 5: wrongNumberCount = item.patientCount ?? 0; break;
        case 6: notAvailableForScreeningCount = item.patientCount ?? 0; break;
        case 7: sampleCollectedCount = item.patientCount ?? 0; break;
        case 8: reScreeningPendingCount = item.patientCount ?? 0; break;
        case 9: deniedScreeningCount = item.patientCount ?? 0; break;
      }
    }
  }

  void resetUI() {
    rejectedBeneficiaryCount = 0;
    screeningConfimPendingCount = 0;
    assignmentPendingCount = 0;
    interestedInScreeningCount = 0;
    notinterestedInScreeningCount = 0;
    wrongNumberCount = 0;
    notAvailableForScreeningCount = 0;
    sampleCollectedCount = 0;
    reScreeningPendingCount = 0;
    deniedScreeningCount = 0;
  }

  // ─── Date pickers ─────────────────────────────────────────────────────────────

  Future<void> selectFromDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      fromDate = FormatterManager.formatDateToString(picked);
      update();
    }
  }

  Future<void> selectToDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      toDate = FormatterManager.formatDateToString(picked);
      update();
      await callAPI();
    }
  }

  // ─── Camp toggle ─────────────────────────────────────────────────────────────

  Future<void> setRegularCamp() async {
    regularCamp = true;
    doorToDoorCamp = false;
    update();
    await callAPI();
  }

  Future<void> setDoorToDoorCamp() async {
    regularCamp = false;
    doorToDoorCamp = true;
    update();
    await callAPI();
  }

  // ─── Filter toggle ────────────────────────────────────────────────────────────

  void toggleFilter() {
    isShowFilter = !isShowFilter;
    update();
  }

  // ─── Filter dropdown fetchers (return data for screen to show bottom sheet) ──

  Future<List<SubOrganizationOutput>?> fetchSubOrganizationList() async {
    if (dESGID == 71) return null;
    ToastManager.showLoader();
    try {
      final params = {
        "UserID": empCode.toString(),
        "DESGID": dESGID.toString(),
      };
      final response = await _repository.getSubOrganization(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<BindDivisionOutput>?> fetchDivisionList() async {
    ToastManager.showLoader();
    try {
      final params = {
        "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
        "UserID": empCode.toString(),
        "DESGID": dESGID.toString(),
      };
      final response = await _repository.getBindDivision(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<BindDistrictOutput>?> fetchDistrictList() async {
    ToastManager.showLoader();
    try {
      final params = {
        "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
        "UserID": empCode.toString(),
        "DESGID": dESGID.toString(),
        "DIVID": selectedDivision?.dIVID.toString() ?? "0",
        "DISTLGDCODE": "0",
      };
      final response = await _repository.getBindDistrict(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<LandingLabCampCreationOutput>?> fetchLabList() async {
    ToastManager.showLoader();
    try {
      if (isGroup2) {
        // Group 2 (camp coordinators): use UserID-based API
        final params = {"UserID": empCode.toString()};
        final LabDataResponse? response =
            await _repository.getLabForD2DCampCoordinator(params);
        return response?.output
            ?.map((e) => LandingLabCampCreationOutput(
                  labCode: e.labCode,
                  labName: e.labName,
                ))
            .toList();
      } else {
        // Admin designations: use district-wise API
        final params = {
          "DISTLGDCODE": selectedDistrict?.dISTLGDCODE?.toString() ?? "0",
        };
        final response = await _repository.getLabDistrictWise(params);
        return response?.output;
      }
    } finally {
      ToastManager.hideLoader();
    }
  }

  // ─── Filter selection setters ─────────────────────────────────────────────────

  void setSubOrganization(SubOrganizationOutput? val) {
    selectedSubOrganization = val;
    selectedDivision = null;
    selectedDistrict = null;
    selectedLabVal = null;
    update();
  }

  void setDivision(BindDivisionOutput? val) {
    selectedDivision = val;
    selectedDistrict = null;
    selectedLabVal = null;
    update();
  }

  void setDistrict(BindDistrictOutput? val) {
    selectedDistrict = val;
    selectedLabVal = null;
    update();
  }

  void setLab(LandingLabCampCreationOutput? val) {
    selectedLabVal = val;
    update();
  }

  // ─── Resolved filter values for navigation ────────────────────────────────────

  String get resolvedOrgId => selectedSubOrganization?.subOrgId.toString() ?? "0";
  String get resolvedDivId => selectedDivision?.dIVID.toString() ?? "0";
  String get resolvedDistCode => selectedDistrict?.dISTLGDCODE.toString() ?? "0";
  String get resolvedLabCode => selectedLabVal?.labCode.toString() ?? "0";
  String get resolvedCampType => regularCamp ? "1" : "3";
}
