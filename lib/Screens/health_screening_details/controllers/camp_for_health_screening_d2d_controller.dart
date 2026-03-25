import 'package:flutter/material.dart' show TextEditingController, VoidCallback;
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_d2d_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class CampForHealthScreeningD2DController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final RxList<CampDetailsonLabForDoorToDoorOutput> campList =
      <CampDetailsonLabForDoorToDoorOutput>[].obs;
  final RxBool isLoading = false.obs;
  final RxString selectedCampDate = ''.obs;

  int dESGID = 0;
  int cityCode = 0;
  int subOrgId = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;
  String district = '';
  bool isUserInteractionEnabled = true;

  CampDetailsonLabForDoorToDoorOutput? selectedCamp;

  final TextEditingController dateController = TextEditingController();

  @override
  void onClose() {
    dateController.dispose();
    super.onClose();
  }

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    dESGID = userData?.dESGID ?? 0;
    district = userData?.district ?? '';
    cityCode = userData?.cityCode ?? 0;
    subOrgId = userData?.subOrgId ?? 0;
    dISTLGDCODE = userData?.dISTLGDCODE ?? 0;
    empCode = userData?.empCode ?? 0;
    selectedCampDate.value = FormatterManager.formatDateToString(DateTime.now());
    dateController.text = selectedCampDate.value;

    isUserInteractionEnabled = !(dESGID == 29 ||
        dESGID == 141 ||
        dESGID == 108 ||
        dESGID == 169 ||
        dESGID == 139 ||
        dESGID == 35 ||
        dESGID == 92 ||
        dESGID == 136 ||
        dESGID == 146);

    fetchCamps();
  }

  Future<void> fetchCamps() async {
    isLoading.value = true;
    final response = await _repo.getCampDetailsForD2D(
      campDate: selectedCampDate.value,
      labCode: cityCode,
      subOrgId: subOrgId,
      distLgdCode: dISTLGDCODE,
      userId: empCode,
      dESGID: dESGID,
    );
    if (response != null) {
      campList.assignAll(response.output ?? []);
    } else {
      campList.clear();
      ToastManager.toast('Failed to load camps');
    }
    isLoading.value = false;
  }

  Future<void> onDateChanged(DateTime picked) async {
    selectedCampDate.value = FormatterManager.formatDateToString(picked);
    dateController.text = selectedCampDate.value;
    await fetchCamps();
  }

  Future<void> onCampSelected(
    CampDetailsonLabForDoorToDoorOutput camp,
    VoidCallback onNavigate,
  ) async {
    selectedCamp = camp;

    if (dESGID == 29 ||
        dESGID == 92 ||
        dESGID == 104 ||
        dESGID == 160 ||
        dESGID == 105 ||
        dESGID == 77 ||
        dESGID == 84 ||
        dESGID == 30 ||
        dESGID == 108 ||
        dESGID == 147 ||
        dESGID == 130 ||
        dESGID == 139 ||
        dESGID == 136 ||
        dESGID == 141) {
      onNavigate();
    } else {
      await _validateAndNavigate(camp, onNavigate);
    }
  }

  Future<void> _validateAndNavigate(
    CampDetailsonLabForDoorToDoorOutput camp,
    VoidCallback onNavigate,
  ) async {
    final response = await _repo.getUserCampMappingAndAttendanceStatusD2D(
      campDate: selectedCampDate.value,
      userId: empCode,
      distLgdCode: camp.dISTLGDCODE ?? 0,
      campType: camp.campType ?? 0,
      campId: camp.campId ?? 0,
    );

    if (response == null) {
      ToastManager.toast('Validation failed. Please try again.');
      return;
    }

    final obj = response.output?.first;
    if (obj == null) return;

    if (obj.isCampClosed == 1) {
      ToastManager.toast('This camp is closed');
      return;
    }
    if (obj.isReadinessFormFilled == 1) {
      ToastManager.toast('Please fill camp readiness form');
      return;
    }
    if (obj.attendanceFlag == 1) {
      ToastManager.toast('Please mark attendance first');
      return;
    }
    if (obj.campFlag == 1) {
      ToastManager.toast('This camp not mapped to you');
      return;
    }

    onNavigate();
  }
}