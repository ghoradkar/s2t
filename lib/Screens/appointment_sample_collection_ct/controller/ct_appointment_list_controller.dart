// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/assignment_remarks_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/district_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_confirmatory_list_model.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/repository/ct_appointment_repository.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/selection_bottom_sheet.dart';

class CTAppointmentListController extends GetxController {
  final CTAppointmentRepository _repo = CTAppointmentRepository();

  final TextEditingController fromDateController = TextEditingController();
  final TextEditingController toDateController = TextEditingController();
  final TextEditingController searchController = TextEditingController();
  final TextEditingController districtController = TextEditingController();
  final TextEditingController remarkController = TextEditingController();

  List<CTConfirmatoryListOutput> fullList = [];
  List<CTConfirmatoryListOutput> filteredList = [];
  List<DistrictOutput> districtList = [];

  DistrictOutput? selectedDistrict;
  AssignmentRemarksOutput? selectedRemark;

  bool isLoading = false;

  int _empCode = 0;
  String _userDistrict = '';

  final DateFormat _displayFormat = DateFormat('dd-MM-yyyy');
  final DateFormat _apiFormat = DateFormat('yyyy/MM/dd');

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    _empCode = user?.empCode ?? 0;
    _userDistrict = user?.district ?? '';
    final distCode = user?.dISTLGDCODE ?? 0;

    selectedDistrict = DistrictOutput(dISTLGDCODE: distCode, dISTNAME: _userDistrict);
    districtController.text = _userDistrict;

    selectedRemark = AssignmentRemarksOutput(arId: 2, assignmentRemarks: 'Assigned');
    remarkController.text = 'Assigned';

    final now = DateTime.now();
    fromDateController.text = _displayFormat.format(now.subtract(const Duration(days: 7)));
    toDateController.text = _displayFormat.format(now);

    fetchList();
  }

  @override
  void onClose() {
    fromDateController.dispose();
    toDateController.dispose();
    searchController.dispose();
    districtController.dispose();
    remarkController.dispose();
    super.onClose();
  }

  Future<void> fetchList() async {
    isLoading = true;
    update();

    DateTime fromDate;
    DateTime toDate;
    try {
      fromDate = _displayFormat.parse(fromDateController.text);
      toDate = _displayFormat.parse(toDateController.text);
    } catch (_) {
      fromDate = DateTime.now().subtract(const Duration(days: 7));
      toDate = DateTime.now();
    }

    final result = await _repo.getConfirmatoryList({
      'USERID': _empCode.toString(),
      'DISTLGDCODE': (selectedDistrict?.dISTLGDCODE ?? 0).toString(),
      'AREA': '0',
      'Type': (selectedRemark?.arId ?? 2).toString(),
      'REDNO': '0',
      'FROMDATE': _apiFormat.format(fromDate),
      'TODATE': _apiFormat.format(toDate),
      'T2T_Order_Id': '0',
    });

    isLoading = false;
    if (result?.output != null) {
      fullList = result!.output!;
      filteredList = List.from(fullList);
      searchController.clear();
    } else {
      fullList = [];
      filteredList = [];
    }
    update();
  }

  void filterList(String query) {
    if (query.trim().isEmpty) {
      filteredList = List.from(fullList);
    } else {
      final q = query.toLowerCase();
      filteredList = fullList
          .where((e) =>
              (e.beneficiaryName ?? '').toLowerCase().startsWith(q) ||
              (e.regdNo ?? '').toLowerCase().startsWith(q))
          .toList();
    }
    update();
  }

  Future<void> showDistrictPicker() async {
    ToastManager.showLoader();
    final response = await _repo.getDistrictList(_empCode);
    ToastManager.hideLoader();

    if (response?.output == null) {
      ToastManager.toast('Failed to load districts');
      return;
    }

    districtList = response!.output!;
    final match = districtList.where((e) => e.dISTNAME == _userDistrict).toList();
    if (selectedDistrict == null && match.isNotEmpty) {
      selectedDistrict = match.first;
    }

    showModalBottomSheet(
      context: Get.context!,
      isScrollControlled: true,
      builder: (context) => StatefulBuilder(
        builder: (c, sheetState) => SelectionBottomSheet<DistrictOutput, int>(
          title: 'Select District',
          items: districtList,
          selectedValue: selectedDistrict?.dISTLGDCODE,
          valueFor: (item) => item.dISTLGDCODE ?? 0,
          labelFor: (item) => item.dISTNAME ?? 'N/A',
          height: responsiveHeight(400),
          padding: EdgeInsets.only(
            top: responsiveHeight(28),
            left: responsiveHeight(35),
            right: responsiveHeight(35),
            bottom: responsiveHeight(60),
          ),
          titleTextStyle: TextStyle(
            fontSize: responsiveFont(16),
            fontFamily: FontConstants.interFonts,
          ),
          titleBottomSpacing: responsiveHeight(20),
          itemContainerPadding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
          selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
          itemTextStyle: TextStyle(
            fontSize: 13,
            fontFamily: FontConstants.interFonts,
            color: kBlackColor,
          ),
          onItemTap: (item) {
            selectedDistrict = item;
            districtController.text = item.dISTNAME ?? '';
            update();
            Get.back();
          },
        ),
      ),
    );
  }

  Future<void> showRemarkPicker() async {
    ToastManager.showLoader();
    final response = await _repo.getAssignmentRemarks('2');
    ToastManager.hideLoader();

    if (response?.output == null) {
      ToastManager.toast('Failed to load status list');
      return;
    }

    final list = response!.output!;
    showModalBottomSheet(
      context: Get.context!,
      isScrollControlled: true,
      builder: (context) => StatefulBuilder(
        builder: (c, sheetState) => SelectionBottomSheet<AssignmentRemarksOutput, int>(
          title: 'Select Status',
          items: list,
          selectedValue: selectedRemark?.arId,
          valueFor: (item) => item.arId ?? 0,
          labelFor: (item) => item.assignmentRemarks ?? 'N/A',
          height: responsiveHeight(360),
          padding: EdgeInsets.only(
            top: responsiveHeight(28),
            left: responsiveHeight(35),
            right: responsiveHeight(35),
            bottom: responsiveHeight(60),
          ),
          titleTextStyle: TextStyle(
            fontSize: responsiveFont(16),
            fontFamily: FontConstants.interFonts,
          ),
          titleBottomSpacing: responsiveHeight(20),
          itemContainerPadding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
          selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
          itemTextStyle: TextStyle(
            fontSize: 13,
            fontFamily: FontConstants.interFonts,
            color: kBlackColor,
          ),
          onItemTap: (item) {
            selectedRemark = item;
            remarkController.text = item.assignmentRemarks ?? '';
            update();
            Get.back();
          },
        ),
      ),
    );
  }

  Future<void> pickDate(TextEditingController controller) async {
    DateTime initial;
    try {
      initial = _displayFormat.parse(controller.text);
    } catch (_) {
      initial = DateTime.now();
    }
    final picked = await showDatePicker(
      context: Get.context!,
      initialDate: initial,
      firstDate: DateTime(2020),
      lastDate: DateTime(2030),
      builder: (context, child) => Theme(
        data: Theme.of(context).copyWith(
          colorScheme: ColorScheme.light(primary: kPrimaryColor),
        ),
        child: child!,
      ),
    );
    if (picked != null) {
      controller.text = _displayFormat.format(picked);
      update();
    }
  }

  int get totalMemberCount =>
      filteredList.fold(0, (sum, e) => sum + (int.tryParse(e.memberCount ?? '0') ?? 0));
}