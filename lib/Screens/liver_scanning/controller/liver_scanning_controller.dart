// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/check_connectivity.dart';
import '../model/FibroScanningDistrictWiseModel.dart';
import '../model/LiverScanningCountModel.dart';
import '../model/LiverScanningTableData.dart';
import '../repository/liver_scanning_repository.dart';

class LiverScanningController extends GetxController {
  final LiverScanningRepository _repository = LiverScanningRepository();

  // ─── State ────────────────────────────────────────────────────────────────────

  bool hasInternet = true;
  bool isLiverScanningLoading = false;
  bool isTableLoading = false;

  String fromDate = '';
  String toDate = '';

  int empCode = 0;
  int desgId = 0;

  LiverScanningCountModel? liverScanningCountModel;
  LiverScanningTableData? fibroScanResponse;
  FibroScanningDistrictWiseModel? fibroScanDistirctResponse;

  // TextEditingControllers for date fields
  final TextEditingController fromDateController = TextEditingController();
  final TextEditingController toDateController = TextEditingController();

  // ScrollControllers for horizontal tables
  final ScrollController districtTableScrollController = ScrollController();
  final ScrollController patientTableScrollController = ScrollController();

  // ─── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    desgId = user?.dESGID ?? 0;
    checkInternetAndLoad();
  }

  @override
  void onClose() {
    fromDateController.dispose();
    toDateController.dispose();
    districtTableScrollController.dispose();
    patientTableScrollController.dispose();
    super.onClose();
  }

  // ─── Internet check + initial load ───────────────────────────────────────────

  Future<void> checkInternetAndLoad() async {
    isLiverScanningLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      if (hasInternet) {
        final now = DateTime.now();
        toDate = FormatterManager.formatDateToStringInDash(now);
        final tenDaysBefore = now.subtract(const Duration(days: 10));
        fromDate = FormatterManager.formatDateToStringInDash(tenDaysBefore);
        fromDateController.text = fromDate;
        toDateController.text = toDate;

        await _getLiverDashCount();
        await _getLiverTableData();
      }
    } finally {
      isLiverScanningLoading = false;
      update();
    }
  }

  // ─── Dashboard count ─────────────────────────────────────────────────────────

  Future<void> _getLiverDashCount() async {
    try {
      final response = await _repository.getLiverDashCount();
      if (response.statusCode == 200) {
        liverScanningCountModel = LiverScanningCountModel.fromJson(
          jsonDecode(response.body),
        );
      } else {
        ToastManager.toast('Failed getting liver dash count');
      }
    } catch (e) {
      debugPrint('getLiverDashCount error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  // ─── District-wise table data ─────────────────────────────────────────────────

  Future<void> refreshTableData() async {
    isTableLoading = true;
    update();
    await _getLiverTableData();
    isTableLoading = false;
    update();
  }

  Future<void> _getLiverTableData() async {
    final body = {
      'fromdate': fromDate,
      'todate': toDate,
      'userid': empCode.toString(),
      'desgid': desgId.toString(),
      'suborgid': '0',
      'distlgdcode': '0',
    };
    try {
      final response = await _repository.getLiverTableData(body);
      if (response.statusCode == 200) {
        final data = jsonDecode(await response.stream.bytesToString());
        if (data['Status'] == 'Success') {
          fibroScanResponse = LiverScanningTableData.fromJson(data);
        }
      } else {
        ToastManager.toast('Failed getting liver table data');
      }
    } catch (e) {
      debugPrint('getLiverTableData error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  // ─── Patient data district-wise ───────────────────────────────────────────────

  Future<void> loadDistrictWiseData(String distlgdcode) async {
    ToastManager.showLoader();
    final body = {
      'fromdate': fromDate,
      'todate': toDate,
      'distlgdcode': distlgdcode,
    };
    try {
      final response =
          await _repository.getLiverTableDataDistrictWise(body);
      if (response.statusCode == 200) {
        final data = jsonDecode(await response.stream.bytesToString());
        if (data['Status'] == 'Success') {
          fibroScanDistirctResponse =
              FibroScanningDistrictWiseModel.fromJson(data);
        }
      } else {
        ToastManager.toast('Failed getting district wise data');
      }
    } catch (e) {
      debugPrint('getLiverTableDataDistrictWise error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  // ─── Date pickers ─────────────────────────────────────────────────────────────

  Future<void> selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      fromDate = FormatterManager.formatDateToStringInDash(picked);
      toDate = '';
      fromDateController.text = fromDate;
      toDateController.text = '';

      if (fromDate.isNotEmpty && toDate.isNotEmpty) {
        await refreshTableData();
      }
    }
    update();
  }

  Future<void> selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      toDate = FormatterManager.formatDateToStringInDash(picked);
      toDateController.text = toDate;
      await refreshTableData();
    }
  }
}