// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/repository/daily_work_dashboard_repository.dart';

class RejectedBeneficiaryTrackingController extends GetxController {
  final DailyWorkDashboardRepository _repository = DailyWorkDashboardRepository();

  // ─── Params from navigation ───────────────────────────────────────────────────

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

  // ─── State ───────────────────────────────────────────────────────────────────

  String fromDate = "";
  String toDate = "";
  List<RecollectionBeneficiaryDashboardForMobOutput> trackingList = [];

  // ─── Init with navigation params ─────────────────────────────────────────────

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

    this.oganizationId = oganizationId;
    this.divisionId = divisionId;
    this.dISTLGDCODE = dISTLGDCODE;
    this.tALLGDCODE = tALLGDCODE;
    this.landingLabId = landingLabId;
    this.campType = campType;
    this.searchFilterId = searchFilterId;

    if (fromDate.isNotEmpty) {
      this.fromDate = fromDate;
    } else {
      final firstDate = DateTime.now().subtract(const Duration(days: 10));
      this.fromDate = FormatterManager.formatDateToString(firstDate);
    }
    if (toDate.isNotEmpty) {
      this.toDate = toDate;
    } else {
      this.toDate = FormatterManager.formatDateToString(DateTime.now());
    }

    callAPI();
  }

  // ─── API call ─────────────────────────────────────────────────────────────────

  Future<void> callAPI() async {
    if (toDate.isEmpty) return;
    ToastManager.showLoader();
    try {
      final params = {
        "Fromdate": fromDate,
        "Todate": toDate,
        "SubOrgId": oganizationId,
        "DIVID": divisionId,
        "DISTLGDCODE": dISTLGDCODE,
        "Arid": "0",
        "UserId": empCode.toString(),
        "DESGID": dESGID.toString(),
        "Type": "1",
      };
      final response = await _repository.getRecollectionBeneficiaryDashboardForMob(params);
      if (response != null) {
        trackingList = response.output ?? [];
        if (trackingList.isEmpty) {
          ToastManager.toast("No Data Found");
        }
      } else {
        trackingList = [];
        ToastManager.toast('Something went wrong');
      }
    } finally {
      ToastManager.hideLoader();
    }
    update();
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
      toDate = "";
      update();
    }
  }

  Future<void> selectToDate(BuildContext context) async {
    if (fromDate.isEmpty) {
      ToastManager.toast("Please Select From Date");
      return;
    }
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      final selected = FormatterManager.formatDateToString(picked);
      if (FormatterManager.isAscendingOrder(fromDate, selected)) {
        toDate = selected;
        update();
        await callAPI();
      } else {
        ToastManager.toast("To Date cannot be before From Date");
      }
    }
  }
}
