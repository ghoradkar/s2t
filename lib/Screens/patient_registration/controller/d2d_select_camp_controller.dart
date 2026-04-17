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
  int cityCode = 0;
  String userDistLgdCode = '0';
  String divisionId = '0';
  String navBeneficiaryNo = '';

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
    cityCode = user?.cityCode ?? 0;
    userDistLgdCode = user?.dISTLGDCODE?.toString() ?? '0';
    divisionId = user?.divid?.toString() ?? '0';
    // ignore: avoid_print
    print('[D2D-ctrl] _loadSession empCode=$empCode dESGID=$dESGID subOrgId=$subOrgId cityCode=$cityCode userDistLgdCode=$userDistLgdCode divisionId=$divisionId');
  }

  void _setDefaultDate() {
    selectedDate.value = FormatterManager.formatDateToString(DateTime.now());
  }

  Future<void> fetchDistrictList() async {
    isLoadingDist.value = true;
    try {
      // ignore: avoid_print
      print('[D2D-ctrl] fetchDistrictList empCode=$empCode userDistLgdCode=$userDistLgdCode');
      final result = await _repo.getDistrictList(
        empCode: empCode.toString(),
        subOrgId: subOrgId.toString(),
        desgId: dESGID.toString(),
      );
      districtList.value = result?.output ?? [];
      // ignore: avoid_print
      print('[D2D-ctrl] districtList count=${districtList.length}');
      if (districtList.isNotEmpty) {
        // Pre-select the district matching the user's own district code,
        // falling back to the first item if no match found.
        selectedDistrict.value = districtList.firstWhereOrNull(
              (d) => d.distLgdCode?.toString() == userDistLgdCode,
            ) ??
            districtList.first;
        // ignore: avoid_print
        print('[D2D-ctrl] selectedDistrict=${selectedDistrict.value?.distLgdCode}/${selectedDistrict.value?.distName}');
        await fetchCampList();
      }
    } finally {
      isLoadingDist.value = false;
    }
  }

  Future<void> fetchCampList() async {
    if (selectedDate.value.isEmpty || selectedDistrict.value == null) return;
    isLoadingCamps.value = true;
    // ignore: avoid_print
    print('[D2D-ctrl] fetchCampList date=${selectedDate.value} dist=${selectedDistrict.value?.distLgdCode} cityCode=$cityCode desgId=$dESGID subOrgId=$subOrgId');
    try {
      final result = await _repo.getCampList(
        campDate: selectedDate.value,
        subOrgId: subOrgId.toString(),
        distLgdCode: selectedDistrict.value?.distLgdCode ?? '0',
        userId: empCode.toString(),
        desgId: dESGID.toString(),
        labCode: cityCode.toString(),
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
      rc.navBeneficiaryNo = navBeneficiaryNo;
      if (navBeneficiaryNo.isNotEmpty) {
        rc.tecWorkerRegNo.text = navBeneficiaryNo;
        rc.onWorkerRegNoChanged(navBeneficiaryNo);
      }

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
