// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:geolocator/geolocator.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Modules/LocationManager/LocationManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/user_attendance/model/user_attandance_response.dart';
import 'package:s2toperational/Screens/user_attendance/repository/user_attendance_repository.dart';

class UserAttendanceController extends GetxController {
  final UserAttendanceRepository repository;

  UserAttendanceController({required this.repository});

  int year = 0;
  int month = 0;
  int dESGID = 0;
  String projectId = '0';
  int subOrgId = 0;
  int empCode = 0;
  bool isLoading = false;
  DateTime visibleMonth = DateTime.now();

  // Map from date → color: green = checked in (01), blue = checked out (02)
  Map<DateTime, Color> attendanceMap = {};

  // Tracks InOutFlag per day for check-in/checkout decisions
  final Map<DateTime, String> _inOutFlagMap = {};

  // "0" = all tests complete, "1" = some tests still incomplete
  String _testFlag = '0';

  // Map data
  double currentLat = 0.0;
  double currentLng = 0.0;
  double campLat = 0.0;
  double campLng = 0.0;
  bool isMapReady = false;

  static const Color _colorCheckIn = Color.fromRGBO(100, 167, 90, 1.0);
  static const Color _colorCheckOut = Color.fromRGBO(33, 150, 243, 1.0);

  @override
  void onInit() {
    super.onInit();
    final DateTime now = DateTime.now();
    year = now.year;
    month = now.month;
    visibleMonth = DateTime(now.year, now.month, 1);

    final LoginOutput? userDetails =
        DataProvider().getParsedUserData()?.output?.first;
    dESGID = userDetails?.dESGID ?? 0;
    projectId = userDetails?.projectId ?? '0';
    subOrgId = userDetails?.subOrgId ?? 0;
    empCode = userDetails?.empCode ?? 0;

    _loadInitialData();
  }

  Future<void> _loadInitialData() async {
    await Future.wait([
      fetchUserAttendance(),
      _fetchTestFlag(),
      _fetchMapData(),
    ]);
  }

  Future<void> _fetchMapData() async {
    // Get current location
    final bool allowed = await LocationManager.checkAndRequestLocation();
    if (!allowed) return;
    final Position? pos = await LocationManager.getCurrentLocation();
    if (pos == null) return;
    currentLat = pos.latitude;
    currentLng = pos.longitude;

    // Get camp location from API
    final camp = await repository.getCampLocation(userId: empCode);
    campLat = camp?.latitude ?? 0.0;
    campLng = camp?.longitude ?? 0.0;

    isMapReady = true;
    update();
  }

  double get distanceKm {
    if (currentLat == 0 || currentLng == 0) return 0;
    final d = Geolocator.distanceBetween(
      currentLat,
      currentLng,
      campLat,
      campLng,
    );
    return d / 1000;
  }

  /// True only if today has any attendance record
  bool get hasTodayAttendance {
    final today = DateTime(
      DateTime.now().year,
      DateTime.now().month,
      DateTime.now().day,
    );
    return _inOutFlagMap.containsKey(today);
  }

  // ─── Test Flag ─────────────────────────────────────────────────────────────

  Future<void> _fetchTestFlag() async {
    _testFlag = await repository.getTestCompleteFlag(userId: empCode);
    print('testFlag: $_testFlag');
  }

  // ─── Month navigation ───────────────────────────────────────────────────────

  void onMonthChanged(DateTime newMonth) {
    visibleMonth = DateTime(newMonth.year, newMonth.month, 1);
    year = newMonth.year;
    month = newMonth.month;
    update();
    fetchUserAttendance();
  }

  // ─── Fetch calendar ─────────────────────────────────────────────────────────

  Future<void> fetchUserAttendance() async {
    isLoading = true;
    update();

    final Map<String, String> params = {
      'Year': year.toString(),
      'Month': month.toString(),
      'ProjectId': projectId,
      'SubOrgId': subOrgId.toString(),
      'USERID': empCode.toString(),
    };

    try {
      final UserAttandanceResponse? response = await repository
          .getUserAttendance(params);
      attendanceMap = {};
      _inOutFlagMap.clear();

      for (final UserAttandanceOutput item in response?.output ?? []) {
        final date = DateTime(item.year ?? 0, item.month ?? 0, item.day ?? 0);
        final flag = item.inOutFlag ?? '';
        _inOutFlagMap[date] = flag;

        // Color: blue = fully checked out (2/02), green = checked in only (1/01 or other)
        attendanceMap[date] =
            (flag == '02' || flag == '2') ? _colorCheckOut : _colorCheckIn;
      }
    } catch (e) {
      attendanceMap = {};
      _inOutFlagMap.clear();
      ToastManager.toast(e.toString());
    }

    isLoading = false;
    update();
  }

  // ─── Date tap ───────────────────────────────────────────────────────────────

  void onDateSelected(DateTime date) {
    // Only allow today
    final String todayStr = FormatterManager.formatDateToString(DateTime.now());
    final String selectedStr = FormatterManager.formatDateToString(date);
    if (todayStr != selectedStr) return;

    final String? flag = _inOutFlagMap[date];

    if (flag == '02' || flag == '2') {
      // Already fully checked out
      _showAlertDialog('You have already marked your availability.');
      return;
    }

    if (flag == '01' || flag == '1') {
      // Checked in — attempt checkout
      _onCheckoutTap(selectedStr);
      return;
    }

    // Not yet marked — check in
    _onFetchData(selectedStr, isCheckout: false);
  }

  // ─── Check-in ───────────────────────────────────────────────────────────────

  Future<void> _onFetchData(String date, {required bool isCheckout}) async {
    EasyLoading.show(status: 'Fetching location...');
    final bool locationAllowed =
        await LocationManager.checkAndRequestLocation();
    if (!locationAllowed) {
      EasyLoading.dismiss();
      ToastManager.toast('Location permission is required.');
      return;
    }
    if (isCheckout) {
      await _checkout(date);
    } else {
      await _insertUserAttendance(date);
    }
  }

  Future<void> _insertUserAttendance(String attendanceDate) async {
    EasyLoading.show(status: 'Marking attendance...');
    final Position? location = await LocationManager.getCurrentLocation();

    final Map<String, String> params = {
      'LATITUDE': location?.latitude.toString() ?? '0',
      'LONGITUDE': location?.longitude.toString() ?? '0',
      'ATTENDANCEMARKBY': empCode.toString(),
      'ATTENDANCEDATE': attendanceDate,
      'Userid': empCode.toString(),
      'MACID': '0',
    };

    try {
      final response = await repository.insertUserAttendance(params);
      EasyLoading.dismiss();
      _showSuccessDialog(response?.message ?? 'Attendance marked successfully');
      await fetchUserAttendance();
    } catch (e) {
      EasyLoading.dismiss();
      ToastManager.toast(e.toString());
    }
  }

  // ─── Checkout ───────────────────────────────────────────────────────────────

  void _onCheckoutTap(String date) {
    if (_testFlag == '1') {
      _showAlertDialog(
        'काही beneficiary च्या कॅम्प test अजूनही अपूर्ण आहेत. तुम्ही CheckOut करू शकत नाही.',
      );
      return;
    }

    _showCheckoutConfirmDialog(date);
  }

  void _showCheckoutConfirmDialog(String date) {
    final ctx = Get.context;
    if (ctx == null) return;

    ToastManager().showConfirmationDialog(
      context: ctx,
      message: 'Are you sure you want to check out?',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Navigator.pop(ctx);
          _onFetchData(date, isCheckout: true);
        } else if (p1 == false) {
          Get.back();
        }
      },
    );
    // showDialog(
    //   context: ctx,
    //   barrierDismissible: false,
    //   builder: (ctx) => AlertDialog(
    //     title: const Text('Confirm'),
    //     content: const Text('Are you sure you want to check out?'),
    //     actions: [
    //       TextButton(
    //         onPressed: () => Navigator.pop(ctx),
    //         child: const Text('Cancel'),
    //       ),
    //       TextButton(
    //         onPressed: () {
    //           Navigator.pop(ctx);
    //           _onFetchData(date, isCheckout: true);
    //         },
    //         child: const Text('Yes'),
    //       ),
    //     ],
    //   ),
    // );
  }

  Future<void> _checkout(String attendanceDate) async {
    EasyLoading.show(status: 'Processing checkout...');
    final Position? location = await LocationManager.getCurrentLocation();

    final result = await repository.insertUserCheckout(
      userId: empCode,
      latitude: location?.latitude ?? 0,
      longitude: location?.longitude ?? 0,
      attendanceDate: attendanceDate,
    );

    EasyLoading.dismiss();

    if (result == null) {
      ToastManager.toast('Checkout failed. Please try again.');
      return;
    }

    final status = result['status']?.toString() ?? '';
    final message = result['message']?.toString() ?? '';

    if (status.toLowerCase() == 'success') {
      ToastManager.showSuccessPopup(
        Get.context!,
        icSuccessIcon,
        "User Assigned successfully",
        () {
          Get.back();
        },
      );

      // _showSuccessDialog(message.isNotEmpty ? message : 'Checkout successful');
      await fetchUserAttendance();
    } else {
      _showAlertDialog(message.isNotEmpty ? message : 'Checkout failed');
    }
  }

  // ─── Dialogs ─────────────────────────────────────────────────────────────────

  void _showAlertDialog(String message) {
    final ctx = Get.context;
    if (ctx == null) return;
    showDialog(
      context: ctx,
      builder:
          (ctx) => AlertDialog(
            title: const Text('Alert'),
            content: Text(message),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx),
                child: const Text('Okay'),
              ),
            ],
          ),
    );
  }

  void _showSuccessDialog(String message) {
    final ctx = Get.context;
    if (ctx == null) return;
    showDialog(
      context: ctx,
      barrierDismissible: false,
      builder:
          (ctx) => AlertDialog(
            title: const Text('Success'),
            content: Text(message),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(ctx),
                child: const Text('Ok'),
              ),
            ],
          ),
    );
  }
}
