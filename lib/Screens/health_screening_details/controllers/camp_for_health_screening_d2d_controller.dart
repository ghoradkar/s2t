import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
// import 'package:s2toperational/Screens/health_screening_details/models/camp_d2d_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

import '../models/camp_d2d_model.dart';

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
  final TextEditingController districtController = TextEditingController();

  @override
  void onClose() {
    dateController.dispose();
    districtController.dispose();
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
    districtController.text = district;

    isUserInteractionEnabled = !(dESGID == 29 ||
        dESGID == 141 ||
        dESGID == 108 ||
        dESGID == 169 ||
        dESGID == 139 ||
        dESGID == 92 ||
        dESGID == 136 ||
        dESGID == 146);

    fetchCamps();
  }

  Future<void> fetchDistrictList() async {
    if (!isUserInteractionEnabled) return;
    final ctx = Get.context;
    if (ctx == null) return;
    ToastManager.showLoader();
    final response = await _repo.getDistrictByUserID(userId: empCode);
    ToastManager.hideLoader();

    if (response == null) {
      ToastManager.toast('Failed to load districts');
      return;
    }

    final List<DistrictOutput> districts = response.output ?? [];
    if (districts.isEmpty) {
      ToastManager.toast('No districts found');
      return;
    }

    _showDistrictBottomSheet(ctx, districts);
  }

  void _showDistrictBottomSheet(
    BuildContext ctx,
    List<DistrictOutput> districts,
  ) {
    showModalBottomSheet(
      context: ctx,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (_) => Container(
        width: double.infinity,
        height: MediaQuery.of(ctx).size.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: DropDownListScreen(
          titleString: 'District',
          dropDownList: districts,
          dropDownMenu: DropDownTypeMenu.District,
          onApplyTap: (selected) {
            if (selected is DistrictOutput) {
              district = selected.dISTNAME ?? '';
              dISTLGDCODE = selected.dISTLGDCODE ?? 0;
              districtController.text = district;
              fetchCamps();
            }
          },
        ),
      ),
    );
  }

  Future<void> fetchCamps() async {
    isLoading.value = true;
    debugPrint('=== fetchCamps ===');
    debugPrint('campDate: ${selectedCampDate.value}');
    debugPrint('distLgdCode: $dISTLGDCODE');
    debugPrint('dESGID: $dESGID');
    debugPrint('empCode: $empCode');
    debugPrint('subOrgId: $subOrgId');
    final response = await _repo.getCampDetailsForD2D(
      campDate: selectedCampDate.value,
      labCode: cityCode,
      subOrgId: subOrgId,
      distLgdCode: dISTLGDCODE,
      userId: empCode,
      dESGID: dESGID,
    );
    debugPrint('response status: ${response?.status} message: ${response?.message} count: ${response?.output?.length}');
    if (response != null && response.status?.toLowerCase() == 'success') {
      campList.assignAll(response.output ?? []);
    } else {
      campList.clear();
      ToastManager.toast(response?.message ?? 'Failed to load camps');
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

    if (obj.isOldCampClosed == 0) {
      ToastManager.toast('मागील दिवसाचा कॅम्प अजूनही सुरू आहे. त्यामुळे नवीन patient registration करता येणार नाही.');
      return;
    }
    if (obj.isCampClosed == 1) {
      ToastManager.toast('This camp is closed');
      return;
    }
    if (obj.isReadinessFormFilled == 0) {
      ToastManager.toast('Please fill camp readiness form');
      return;
    }
    if (obj.campFlag == 0) {
      ToastManager.toast('This camp not mapped to you');
      return;
    }
    if (obj.attendanceFlag == 0) {
      ToastManager.toast('Please mark attendance first');
      return;
    }
    if (obj.teamMemberAttendance == 1) {
      ToastManager.toast('टीम मेंबरची attendance अजूनही pending आहे.');
      return;
    }

    onNavigate();
  }
}