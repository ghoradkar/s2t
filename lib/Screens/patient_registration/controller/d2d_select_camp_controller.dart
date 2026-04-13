// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_patient_registration_controller.dart';
import 'package:s2toperational/Screens/patient_registration/model/attendance_status_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_camp_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/d2d_patient_registration_screen.dart';

class D2DSelectCampController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();

  int empCode = 0;
  int dESGID = 0;
  int subOrgId = 0;

  final selectedDate = ''.obs;
  final districtList = <DistrictOutput>[].obs;
  final selectedDistrict = Rxn<DistrictOutput>();
  final campList = <D2DCampOutput>[].obs;
  final isLoadingDist = false.obs;
  final isLoadingCamps = false.obs;
  final isCheckingAttendance = false.obs;
  final checkingCampId = ''.obs;

  @override
  void onInit() {
    super.onInit();
    _loadSession();
    _setDefaultDate();
    fetchDistrictList();
  }

  void _loadSession() {
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    dESGID = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
  }

  void _setDefaultDate() {
    selectedDate.value = FormatterManager.formatDateToString(DateTime.now());
  }

  Future<void> fetchDistrictList() async {
    isLoadingDist.value = true;
    try {
      final result = await _repo.getDistrictList(empCode: empCode.toString());
      districtList.value = result?.output ?? [];
      if (districtList.isNotEmpty) {
        selectedDistrict.value = districtList.first;
        await fetchCampList();
      }
    } finally {
      isLoadingDist.value = false;
    }
  }

  Future<void> fetchCampList() async {
    if (selectedDate.value.isEmpty || selectedDistrict.value == null) return;
    isLoadingCamps.value = true;
    try {
      final result = await _repo.getCampList(
        campDate: selectedDate.value,
        subOrgId: subOrgId.toString(),
        distLgdCode: selectedDistrict.value?.distLgdCode ?? '0',
        userId: empCode.toString(),
        desgId: dESGID.toString(),
      );
      campList.value = result?.output ?? [];
      if (campList.isEmpty) {
        // ToastManager.toast('No camps found for selected date');
        ToastManager.showAlertDialog(
          Get.context!,
          'No camps found for selected date',
              () {
            Get.back();
          },
        );
      }
    } finally {
      isLoadingCamps.value = false;
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

  void onDistrictSelected(DistrictOutput dist) {
    selectedDistrict.value = dist;
    fetchCampList();
  }

  Future<void> onCampTapped(D2DCampOutput camp, BuildContext context) async {
    if (isCheckingAttendance.value) return;
    isCheckingAttendance.value = true;
    checkingCampId.value = camp.campId ?? '';
    try {
      final result = await _repo.checkAttendanceStatus(
        campDate: selectedDate.value,
        userId: empCode.toString(),
        distLgdCode: camp.distLgdCode ?? '0',
        campId: camp.campId ?? '',
      );

      final output = result?.output;
      if (output == null) {
        ToastManager.toast('Unable to check attendance status');
        return;
      }
      final msg = _getBlockMessage(output);
      if (msg.isNotEmpty) {
        // ToastManager.toast(msg);
        ToastManager.showAlertDialog(
          Get.context!,
          msg,
              () {
            Get.back();
          },
        );
        return;
      }

      Get.delete<D2DPatientRegistrationController>(force: true);
      final rc = Get.put(D2DPatientRegistrationController());
      rc.navCampId = camp.campId ?? '';
      rc.navSiteId = camp.siteDetailId ?? '';
      rc.navDistLgd = camp.distLgdCode ?? '';
      rc.navCampLocation = camp.campLocation ?? '';
      rc.navType = '6';

      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (_) => const D2DPatientRegistrationScreen(),
        ),
      );
    } finally {
      isCheckingAttendance.value = false;
      checkingCampId.value = '';
    }
  }

  String _getBlockMessage(AttendanceStatusOutput output) {
    if (output.blockOldCamp) return "मागील दिवसाचा कॅम्प अजूनही  सुरू आहे. त्यामुळे नवीन patient registration करता येणार नाही";
    if (output.blockCampClosed) return 'This camp is closed';
    if (output.blockNotMapped) return 'Camp not mapped to you';
    if (output.blockReadiness) return 'Fill camp readiness form first';
    if (output.blockAttendance) return 'Please mark your attendance first';
    if (output.blockTest) return 'You are not mapped for patient registration';
    return '';
  }
}
