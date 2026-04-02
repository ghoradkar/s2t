// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/repository/daily_work_dashboard_repository.dart';

class RejectedBeneficiaryListController extends GetxController {
  final DailyWorkDashboardRepository _repository = DailyWorkDashboardRepository();

  // ─── Nav params ───────────────────────────────────────────────────────────────

  String fromDate = "";
  String toDate = "";
  String oganizationId = "";
  String divisionId = "";
  String dISTLGDCODE = "";
  String tALLGDCODE = "";
  String landingLabId = "";
  String campType = "";
  String searchFilterId = "";

  // ─── User info ────────────────────────────────────────────────────────────────

  int dESGID = 0;
  int empCode = 0;
  int labCode = 0;
  int tALLGDCODEUser = 0;

  // ─── State ───────────────────────────────────────────────────────────────────

  List<RecollectionBeneficiaryStatusandDetailsCountV1Output> rejectedBeneficaryList = [];
  List<RecollectionBeneficiaryStatusandDetailsCountV1Output> searchRejectedBeneficaryList = [];

  RecollectionAssignmentRemarksOutput? selectedStatus;
  RecollectionAssignmentRemarksOutput? selectedSearchBy;

  List<RecollectionAssignmentRemarksOutput> searcyByList = [];
  final TextEditingController searchTextEditingController = TextEditingController();

  String get areaString => "[]";
  String get pinCode => "[]";

  // ─── Role helper ──────────────────────────────────────────────────────────────

  bool get isGroup1 => [86, 64, 35, 129, 146].contains(dESGID);

  // ─── Init with nav params ─────────────────────────────────────────────────────

  void initWithParams({
    required String fromDate,
    required String toDate,
    required String oganizationId,
    required String divisionId,
    required String dISTLGDCODE,
    required String tALLGDCODE,
    required String landingLabId,
    required String campType,
    required String searchFilterId,
  }) {
    final user = DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    empCode = user?.empCode ?? 0;
    labCode = user?.labCode ?? 0;
    tALLGDCODEUser = user?.tALLGDCODE ?? 0;

    this.fromDate = fromDate;
    this.toDate = toDate;
    this.oganizationId = oganizationId;
    this.divisionId = divisionId;
    this.dISTLGDCODE = dISTLGDCODE;
    this.tALLGDCODE = tALLGDCODE;
    this.landingLabId = landingLabId;
    this.campType = campType;
    this.searchFilterId = searchFilterId;

    searcyByList = [
      RecollectionAssignmentRemarksOutput(arId: 1, assignmentRemarks: "Beneficiary Name"),
      RecollectionAssignmentRemarksOutput(arId: 2, assignmentRemarks: "Pincode"),
      RecollectionAssignmentRemarksOutput(arId: 3, assignmentRemarks: "Taluka"),
      RecollectionAssignmentRemarksOutput(arId: 4, assignmentRemarks: "Area"),
      RecollectionAssignmentRemarksOutput(arId: 5, assignmentRemarks: "Beneficiary Number"),
    ];

    selectedStatus = isGroup1
        ? RecollectionAssignmentRemarksOutput(arId: 0, assignmentRemarks: "All")
        : RecollectionAssignmentRemarksOutput(arId: 2, assignmentRemarks: "Assignment Pending");

    callAPICall();
  }

  @override
  void onClose() {
    searchTextEditingController.dispose();
    super.onClose();
  }

  // ─── API call ─────────────────────────────────────────────────────────────────

  Future<void> callAPICall() async {
    ToastManager.showLoader();
    try {
      RecollectionBeneficiaryStatusandDetailsCountV1Response? response;
      if (isGroup1) {
        if (searchFilterId == "0") {
          response = await _repository.getCountForPageloadForTeamV1({
            "Fromdate": fromDate,
            "Todate": toDate,
            "SubOrgId": oganizationId,
            "DIVID": "0",
            "DISTLGDCODE": dISTLGDCODE,
            "TALLGDCODE": "0",
            "Labcode": "0",
            "Arid": selectedStatus?.arId.toString() ?? "0",
            "BeneficiaryNumber": "0",
            "UserId": empCode.toString(),
            "Type": "2",
          });
        } else {
          response = await _repository.getCountForTeam({
            "Fromdate": fromDate,
            "Todate": toDate,
            "SubOrgId": oganizationId,
            "DIVID": divisionId,
            "DISTLGDCODE": dISTLGDCODE,
            "TALLGDCODE": tALLGDCODE,
            "Labcode": landingLabId,
            "T_AreaofPincode": areaString,
            "T_PincodeofArea": pinCode,
            "Arid": selectedStatus?.arId.toString() ?? "0",
            "BeneficiaryNumber": "0",
            "UserId": empCode.toString(),
            "Type": "2",
          });
        }
      } else {
        if (searchFilterId == "0") {
          response = await _repository.getCountForPageloadVa({
            "Fromdate": fromDate,
            "Todate": toDate,
            "SubOrgId": oganizationId,
            "DIVID": "0",
            "DISTLGDCODE": dISTLGDCODE,
            "TALLGDCODE": tALLGDCODEUser.toString(),
            "Labcode": labCode.toString(),
            "Arid": selectedStatus?.arId.toString() ?? "0",
            "BeneficiaryNumber": "0",
            "UserId": empCode.toString(),
            "Type": "2",
            "DesgID": dESGID.toString(),
            "CampType": campType,
          });
        } else {
          response = await _repository.getCountV1({
            "Fromdate": fromDate,
            "Todate": toDate,
            "SubOrgId": "0",
            "DIVID": "0",
            "DISTLGDCODE": "0",
            "TALLGDCODE": "0",
            "Labcode": landingLabId,
            "Arid": "0",
            "BeneficiaryNumber": "0",
            "UserId": empCode.toString(),
            "Type": "1",
            "CampType": campType,
          });
        }
      }
      if (response != null) {
        rejectedBeneficaryList = response.output ?? [];
        searchRejectedBeneficaryList = List.from(rejectedBeneficaryList);
      } else {
        rejectedBeneficaryList = [];
        searchRejectedBeneficaryList = [];
        ToastManager.toast('Something went wrong');
      }
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  // ─── Status dropdown ──────────────────────────────────────────────────────────

  Future<List<RecollectionAssignmentRemarksOutput>?> fetchStatusList() async {
    ToastManager.showLoader();
    try {
      final type = isGroup1 ? "3" : "0";
      final response = await _repository.getRecollectionAssignmentRemarks({"Type": type});
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  void setStatus(RecollectionAssignmentRemarksOutput? val) {
    selectedStatus = val;
    update();
    callAPICall();
  }

  void setSearchBy(RecollectionAssignmentRemarksOutput? val) {
    selectedSearchBy = val;
    update();
  }

  // ─── Client-side search ───────────────────────────────────────────────────────

  void applySearch(String query) {
    searchRejectedBeneficaryList = searchByDescEn(query);
    update();
  }

  List<RecollectionBeneficiaryStatusandDetailsCountV1Output> searchByDescEn(String query) {
    if (query.isEmpty) return List.from(rejectedBeneficaryList);
    if (selectedSearchBy?.arId == 1) {
      return rejectedBeneficaryList.where((item) {
        return (item.beneficiaryName?.toLowerCase() ?? '').contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 2) {
      return rejectedBeneficaryList.where((item) {
        return (item.pincode?.toLowerCase() ?? '').contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 3) {
      return rejectedBeneficaryList.where((item) {
        return (item.taluka?.toLowerCase() ?? '').contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 4) {
      return rejectedBeneficaryList.where((item) {
        return (item.area?.toLowerCase() ?? '').contains(query.toLowerCase());
      }).toList();
    }
    return rejectedBeneficaryList.where((item) {
      return (item.beneficiaryNumber?.toLowerCase() ?? '').contains(query.toLowerCase());
    }).toList();
  }
}