// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:geolocator/geolocator.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Screens/user_attendance/Model/user_attandance_response.dart';
import 'package:s2toperational/Modules/LocationManager/LocationManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/user_attendance/repository/user_attendance_repository.dart';

class UserAttendanceController extends GetxController {
  final UserAttendanceRepository repository;

  UserAttendanceController({required this.repository});

  int year = 0;
  int month = 0;
  int dESGID = 0;
  String projectId = "0";
  int subOrgId = 0;
  int empCode = 0;
  bool isLoading = false;
  DateTime visibleMonth = DateTime.now();
  Map<DateTime, Color> attendanceMap = {};

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
    projectId = userDetails?.projectId ?? "0";
    subOrgId = userDetails?.subOrgId ?? 0;
    empCode = userDetails?.empCode ?? 0;

    fetchUserAttendance();
  }

  void onMonthChanged(DateTime newMonth) {
    visibleMonth = DateTime(newMonth.year, newMonth.month, 1);
    year = newMonth.year;
    month = newMonth.month;
    update();
    fetchUserAttendance();
  }

  void onDateSelected(DateTime date) {
    if (attendanceMap.containsKey(date)) {
      ToastManager.toast("You have already marked your availability.");
      return;
    }

    final String currentDate =
        FormatterManager.formatDateToString(DateTime.now());
    final String selectedDate = FormatterManager.formatDateToString(date);
    print("Now $currentDate");
    print("selected $selectedDate");

    if (currentDate == selectedDate) {
      print(date);
      _onFetchData(selectedDate);
    } else {
      print("Date not found.");
    }
  }

  Future<void> fetchUserAttendance() async {
    isLoading = true;
    update();

    final Map<String, String> params = {
      "Year": year.toString(),
      "Month": month.toString(),
      "ProjectId": projectId,
      "SubOrgId": subOrgId.toString(),
      "USERID": empCode.toString(),
    };
    print(params);

    try {
      final UserAttandanceResponse? response =
          await repository.getUserAttendance(params);
      attendanceMap = {};
      final List<UserAttandanceOutput> list = response?.output ?? [];
      for (final UserAttandanceOutput item in list) {
        attendanceMap[DateTime(
          item.year ?? 0,
          item.month ?? 0,
          item.day ?? 0,
        )] = const Color.fromRGBO(100, 167, 90, 1.0);
      }
    } catch (e) {
      attendanceMap = {};
      ToastManager.toast(e.toString());
    }

    isLoading = false;
    update();
  }

  Future<void> _onFetchData(String date) async {
    EasyLoading.show(status: 'Fetching location...');
    final bool locationAllowed =
        await LocationManager.checkAndRequestLocation();
    if (!locationAllowed) {
      EasyLoading.dismiss();
      print("Location permission is required.");
      return;
    }
    await _insertUserAttendance(date);
  }

  Future<void> _insertUserAttendance(String attendanceDate) async {
    EasyLoading.show(status: 'Marking attendance...');
    final Position? location = await LocationManager.getCurrentLocation();
    final Map<String, String> params = {
      "LATITUDE": location?.latitude.toString() ?? "0",
      "LONGITUDE": location?.longitude.toString() ?? "0",
      "ATTENDANCEMARKBY": empCode.toString(),
      "ATTENDANCEDATE": attendanceDate,
      "Userid": empCode.toString(),
      "MACID": "0",
    };

    try {
      await repository.insertUserAttendance(params);
      EasyLoading.dismiss();
      ToastManager.toast("Attendance marked successfully");
      await fetchUserAttendance();
    } catch (e) {
      EasyLoading.dismiss();
      ToastManager.toast(e.toString());
    }
  }
}