// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
// import 'package:s2toperational/Modules/Json_Class/BeneficiaryStatusAndDetailsResponse/BeneficiaryStatusAndDetailsResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
// import 'package:s2toperational/Modules/Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/repository/daily_work_dashboard_repository.dart';

import '../model/BeneficiaryStatusAndDetailsResponse.dart';
import '../model/RecollectionAssignmentRemarksResponse.dart';
import '../model/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import '../model/SelectedTeamsDataListResponse.dart';

class RejectedBeneficiaryInfoController extends GetxController {
  final DailyWorkDashboardRepository _repository = DailyWorkDashboardRepository();

  // ─── Nav params ───────────────────────────────────────────────────────────────

  late RecollectionBeneficiaryStatusandDetailsCountV1Output navObj;
  String fromDate = "";
  String toDate = "";
  String oganizationId = "";
  String divisionId = "";
  String dISTLGDCODE = "";
  String tALLGDCODE = "";
  String landingLabId = "";
  String campType = "";
  String statusType = "";

  // ─── User info ────────────────────────────────────────────────────────────────

  int dESGID = 0;
  int empCode = 0;

  // ─── Role helper ──────────────────────────────────────────────────────────────

  bool get isGroup1 => [86, 64, 35, 129, 146].contains(dESGID);

  // ─── UI state ─────────────────────────────────────────────────────────────────

  bool showTeam = true;
  bool showRemark = true;
  bool showAppoointmentDate = true;
  bool showAssignButton = true;
  bool showReRegisterButton = false;
  String buttonTitle = "";

  // ─── Beneficiary data ─────────────────────────────────────────────────────────

  BeneficiaryStatusAndDetailsOutput? beneficiaryObj;

  // ─── Team state ───────────────────────────────────────────────────────────────

  String teamActivae = "";
  int teamID = 0;
  String teamName = "";
  SelectedTeamsDataLisOutput? selectedTeam;

  // ─── Remark state ─────────────────────────────────────────────────────────────

  int? remarkId;
  String remark = "";
  RecollectionAssignmentRemarksOutput? selectedRemark;

  // ─── Appointment date ─────────────────────────────────────────────────────────

  String appoointmentDate = "";

  // ─── Init with nav params ─────────────────────────────────────────────────────

  void initWithParams({
    required RecollectionBeneficiaryStatusandDetailsCountV1Output obj,
    required String fromDate,
    required String toDate,
    required String oganizationId,
    required String divisionId,
    required String dISTLGDCODE,
    required String tALLGDCODE,
    required String landingLabId,
    required String campType,
    required String statusType,
  }) {
    final user = DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    empCode = user?.empCode ?? 0;

    navObj = obj;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.oganizationId = oganizationId;
    this.divisionId = divisionId;
    this.dISTLGDCODE = dISTLGDCODE;
    this.tALLGDCODE = tALLGDCODE;
    this.landingLabId = landingLabId;
    this.campType = campType;
    this.statusType = statusType;

    _setupUIForDesig();
    _callAPIGroup();
  }

  void _setupUIForDesig() {
    if (isGroup1) {
      showTeam = false;
      showRemark = true;
      showAppoointmentDate = true;
      buttonTitle = "Save";
      showAssignButton = true;
      showReRegisterButton = false;
    } else {
      buttonTitle = "Assign";
      showAssignButton = true;
      showReRegisterButton = false;
      showRemark = false;
      showAppoointmentDate = false;
    }
  }

  void _callAPIGroup() {
    ToastManager.showLoader();
    if (isGroup1) {
      _getBeneficiaryDataForTeam();
    } else {
      _getBeneficiaryData();
    }
  }

  // ─── Fetch beneficiary details (group1) ──────────────────────────────────────

  Future<void> _getBeneficiaryDataForTeam() async {
    try {
      final params = {
        "Fromdate": fromDate,
        "Todate": toDate,
        "SubOrgId": oganizationId,
        "DIVID": divisionId,
        "DISTLGDCODE": dISTLGDCODE,
        "TALLGDCODE": tALLGDCODE,
        "Labcode": landingLabId,
        "Arid": "0",
        "BeneficiaryNumber": navObj.rejRegdid.toString(),
        "UserId": empCode.toString(),
        "Type": "3",
        "CampType": campType,
      };
      final response = await _repository.getBeneficiaryStatusAndDetails(params);
      if (response != null) {
        beneficiaryObj = response.output?.first;
        _applyBeneficiaryDataGroup1();
      } else {
        ToastManager.toast('Something went wrong');
      }
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  void _applyBeneficiaryDataGroup1() {
    if (beneficiaryObj == null) return;
    teamActivae = beneficiaryObj!.isTeamActive == 1
        ? "${beneficiaryObj!.teamName ?? 'NA'} (Active)"
        : "${beneficiaryObj!.teamName ?? 'NA'} (InActive)";

    if (beneficiaryObj!.isAppointmentConfirm == 1) {
      showAssignButton = false;
      showAppoointmentDate = true;
      showReRegisterButton = true;
    } else {
      showReRegisterButton = false;
    }

    if (["1", "8", "0", "9"].contains(statusType)) {
      showAssignButton = false;
      showReRegisterButton = false;
      showRemark = true;
    }
    if (statusType == "1") {
      showReRegisterButton = false;
    }

    if (beneficiaryObj!.isTeamMapped == 1) {
      ToastManager.toast("Team assigned for this beneficiary");
    }

    teamID = beneficiaryObj!.teamID ?? 0;
    teamName = beneficiaryObj!.teamName ?? "";
    remarkId = beneficiaryObj!.arid;

    if (remarkId != null) {
      showAppoointmentDate = remarkId == 3;
    }
  }

  // ─── Fetch beneficiary details (non-group1) ──────────────────────────────────

  Future<void> _getBeneficiaryData() async {
    try {
      final params = {
        "Fromdate": fromDate,
        "Todate": toDate,
        "SubOrgId": oganizationId,
        "DIVID": divisionId,
        "DISTLGDCODE": dISTLGDCODE,
        "TALLGDCODE": "0",
        "Labcode": landingLabId,
        "Arid": "0",
        "BeneficiaryNumber": navObj.rejRegdid.toString(),
        "UserId": empCode.toString(),
        "Type": "3",
        "CampType": campType,
      };
      final response = await _repository.getBeneficiaryStatusAndDetails(params);
      if (response != null) {
        beneficiaryObj = response.output?.first;
        _applyBeneficiaryData();
      } else {
        ToastManager.toast('Something went wrong');
      }
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  void _applyBeneficiaryData() {
    if (beneficiaryObj == null) return;
    teamActivae = beneficiaryObj!.isTeamActive == 1
        ? "${beneficiaryObj!.teamName ?? 'NA'} (Active)"
        : "${beneficiaryObj!.teamName ?? 'NA'} (InActive)";

    if (beneficiaryObj!.isAppointmentConfirm == 1) {
      showAssignButton = false;
    }
    if (["1", "8", "0", "9"].contains(statusType)) {
      showAssignButton = false;
    }
    if (beneficiaryObj!.isTeamMapped == 1) {
      ToastManager.toast("Team assigned for this beneficiary");
    }

    teamID = beneficiaryObj!.teamID ?? 0;
    teamName = beneficiaryObj!.teamName ?? "";
  }

  // ─── Team list (for assign) ───────────────────────────────────────────────────

  Future<List<SelectedTeamsDataLisOutput>?> fetchTeamList() async {
    ToastManager.showLoader();
    try {
      final params = {
        "Pincode": beneficiaryObj?.pincode ?? "",
        "USERID": empCode.toString(),
      };
      final response = await _repository.getRecollectionTeamDetails(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  void selectTeam(SelectedTeamsDataLisOutput team) {
    selectedTeam = team;
    teamName = team.teamName ?? "";
    teamID = team.teamID ?? 0;
    teamActivae = "${team.teamName ?? 'NA'} (Active)";
    update();
  }

  // ─── Remark list ─────────────────────────────────────────────────────────────

  Future<List<RecollectionAssignmentRemarksOutput>?> fetchRemarkList() async {
    ToastManager.showLoader();
    try {
      final response =
          await _repository.getRecollectionAssignmentRemarks({"Type": "1"});
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  void selectRemark(RecollectionAssignmentRemarksOutput val) {
    selectedRemark = val;
    remarkId = val.arId;
    remark = val.assignmentRemarks ?? "";
    showAppoointmentDate = remarkId == 3;
    update();
  }

  // ─── Appointment date ─────────────────────────────────────────────────────────

  Future<void> selectAppointmentDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      appoointmentDate = FormatterManager.formatDateToString(picked);
      update();
    }
  }

  // ─── Submit ───────────────────────────────────────────────────────────────────

  Future<bool> submitGroup1() async {
    if (remark.isEmpty) {
      ToastManager.toast("Please Select remark");
      return false;
    }
    if (remarkId == 3 && appoointmentDate.isEmpty) {
      ToastManager.toast("Please Select appointment date");
      return false;
    }
    final jsonArray = [
      {
        "Regdid": beneficiaryObj?.rejRegdid.toString() ?? "",
        "Regdno": teamID.toString(),
        "AppointmentDate": appoointmentDate,
      },
    ];
    return await _insertAppointmentDetails(_jsonEncode(jsonArray));
  }

  Future<bool> submitGroup2() async {
    if (teamName.isEmpty || teamName == "NA") {
      ToastManager.toast("Please Select Team");
      return false;
    }
    final jsonArray = [
      {
        "USERID": selectedTeam?.memberUserID1.toString() ?? "0",
        "Teamid": selectedTeam?.teamID.toString() ?? "0",
      },
      {
        "USERID": selectedTeam?.memberUserID2.toString() ?? "0",
        "Teamid": selectedTeam?.teamID.toString() ?? "0",
      },
    ];
    return await _insertTeamMapping(_jsonEncode(jsonArray));
  }

  Future<bool> _insertAppointmentDetails(String jsonString) async {
    ToastManager.showLoader();
    try {
      final params = {
        "RecollectionAppointmentDateType": jsonString,
        "ArId": remarkId?.toString() ?? "0",
        "Userid": empCode.toString(),
      };
      final response = await _repository.insertAppointmentDetails(params);
      return response?.status?.toLowerCase() == 'success';
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<bool> _insertTeamMapping(String teamMappingJson) async {
    ToastManager.showLoader();
    try {
      final params = {
        "Regdid": beneficiaryObj?.rejRegdid.toString() ?? "0",
        "Campid": beneficiaryObj?.rejCampID.toString() ?? "0",
        "RecollectionTeamandBeneficiaryMapping": teamMappingJson,
        "AssignedBy": empCode.toString(),
      };
      final response =
          await _repository.insertRecollectionTeamAndBeneficiaryMapping(params);
      return response?.status?.toLowerCase() == 'success';
    } finally {
      ToastManager.hideLoader();
    }
  }

  String _jsonEncode(List<Map<String, String>> list) {
    try {
      return jsonEncode(list);
    } catch (_) {
      return "";
    }
  }
}
