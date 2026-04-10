// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_patient_registration_controller.dart';
import 'package:s2toperational/Screens/patient_registration/model/attendance_status_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/select_camp_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/regular_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/d2d_patient_registration_screen.dart';

class SelectCampController extends GetxController {
  final _repo = RegularPatientRegistrationRepository();

  String navCampType = '1';

  int empCode = 0;
  int dESGID = 0;
  int subOrgId = 0;
  String distLgdCode = '0';
  String divisionId = '0';

  final selectedDate = ''.obs;
  final campList = <SelectCampOutput>[].obs;
  final filteredCampList = <SelectCampOutput>[].obs;
  final searchQuery = ''.obs;
  final isLoading = false.obs;
  final isCheckingAttendance = false.obs;
  final checkingCampId = ''.obs;

  @override
  void onInit() {
    super.onInit();
    _loadSession();
    _setDefaultDate();
    fetchCampList();
  }

  void _loadSession() {
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    dESGID = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    distLgdCode = user?.dISTLGDCODE?.toString() ?? '0';
    divisionId = user?.divid?.toString() ?? '0';
  }

  void _setDefaultDate() {
    selectedDate.value = FormatterManager.formatDateToString(DateTime.now());
  }

  Future<void> fetchCampList() async {
    if (selectedDate.value.isEmpty) return;
    isLoading.value = true;
    try {
      final result = await _repo.getCampList(
        campDate: selectedDate.value,
        subOrgId: subOrgId.toString(),
        empCode: empCode.toString(),
        desgId: dESGID.toString(),
      );
      campList.value = result?.output ?? [];
      filteredCampList.assignAll(campList);
      if (campList.isEmpty) {
        ToastManager.toast('No camps found for selected date');
      }
    } finally {
      isLoading.value = false;
    }
  }

  Future<void> onDateTapped(BuildContext context) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: now.subtract(const Duration(days: 30)),
      lastDate: now,
    );
    if (picked != null) {
      selectedDate.value = FormatterManager.formatDateToString(picked);
      await fetchCampList();
    }
  }

  void onSearchChanged(String query) {
    searchQuery.value = query;
    if (query.isEmpty) {
      filteredCampList.assignAll(campList);
      return;
    }
    filteredCampList.assignAll(
      campList.where(
        (c) => (c.campId ?? '').toLowerCase().contains(query.toLowerCase()),
      ),
    );
  }

  Future<void> onCampTapped(SelectCampOutput camp, BuildContext context) async {
    if (isCheckingAttendance.value) return;
    isCheckingAttendance.value = true;
    checkingCampId.value = camp.campId ?? '';
    try {
      final result = await _repo.checkAttendanceStatus(
        campDate: selectedDate.value,
        userId: empCode.toString(),
        distLgdCode: camp.distLgdCode ?? distLgdCode,
        campType: camp.campType ?? navCampType,
        campId: camp.campId ?? '',
      );

      final output = result?.output;
      if (output == null) {
        ToastManager.toast('Unable to check attendance status');
        return;
      }

      final msg = _getBlockMessage(output);
      if (msg.isNotEmpty) {
        ToastManager.toast(msg);
        return;
      }

      Get.delete<D2DPatientRegistrationController>(force: true);
      final rc = Get.put(D2DPatientRegistrationController());
      rc.navCampId = camp.campId ?? '';
      rc.navCampLocation = camp.distName ?? '';
      rc.navSiteId = camp.siteDetailId ?? '';
      rc.navDistLgd = camp.distLgdCode ?? '';
      rc.navType = '6';
      rc.navCampType = navCampType;

      Navigator.push(
        context,
        MaterialPageRoute(builder: (_) => const D2DPatientRegistrationScreen()),
      );
    } finally {
      isCheckingAttendance.value = false;
      checkingCampId.value = '';
    }
  }

  String _getBlockMessage(AttendanceStatusOutput output) {
    if (output.blockOldCamp) {
      return "मागील दिवसाचा कॅम्प अजूनही सुरू आहे. त्यामुळे नवीन patient registration करता येणार नाही.";
    }
    if (output.blockCampClosed) return 'This camp is closed';
    if (output.blockNotMapped) return 'Camp not mapped to you';
    if (output.blockReadiness) return 'Fill camp readiness form first';
    if (output.blockAttendance) return 'Please mark your attendance first';
    if (output.blockTest) return 'You are not mapped for patient registration';
    if ((output.teamMemberAttendance ?? '') == '1') {
      return "कॅम्प सुरू करण्यासाठी सर्व टीम सदस्यांनी उपस्थिती नोंदवणे आवश्यक आहे. मात्र काही टीम सदस्यांनी उपस्थिती नोंदवलेली नाही किंवा टीमचा फोटो अपलोड केलेला नाही.";
    }
    return '';
  }
}
