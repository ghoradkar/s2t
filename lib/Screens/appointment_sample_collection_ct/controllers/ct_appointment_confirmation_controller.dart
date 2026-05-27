// ignore_for_file: use_build_context_synchronously

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_appointment_beneficiary_model.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/repository/ct_appointment_repository.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';

class CTAppointmentConfirmationController extends GetxController {
  final String beneficiaryName;
  final String regNo;
  final String mobile;
  final String status;

  CTAppointmentConfirmationController({
    required this.beneficiaryName,
    required this.regNo,
    required this.mobile,
    required this.status,
  });

  final CTAppointmentRepository _repo = CTAppointmentRepository();

  final TextEditingController districtController = TextEditingController();
  final TextEditingController areaController = TextEditingController();
  final TextEditingController addressController = TextEditingController();
  final TextEditingController remarkController = TextEditingController();
  final TextEditingController appointmentDateController =
      TextEditingController();

  List<AssignmentRemarksOutput> remarkList = [];
  AssignmentRemarksOutput? selectedRemark;

  List<CTAppointmentBeneficiaryOutput> dependentList = [];

  bool showAppointmentDate = true;
  bool isControlsDisabled = false;
  bool isLoading = true;
  bool _isFetching = false;
  bool _hasLoaded = false;

  final DateFormat _displayFormat = DateFormat('dd-MM-yyyy');

  @override
  void onReady() {
    super.onReady();
    if (status == '4') {
      isControlsDisabled = true;
      update();
    }
  }

  @override
  void onClose() {
    districtController.dispose();
    areaController.dispose();
    addressController.dispose();
    remarkController.dispose();
    appointmentDateController.dispose();
    super.onClose();
  }

  Future<void> reloadData() {
    _hasLoaded = false;
    return loadBeneficiaryDetails();
  }

  Future<void> loadBeneficiaryDetails() async {
    if (_isFetching || _hasLoaded) return;
    _isFetching = true;
    ToastManager.showLoader();
    final empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    final response = await _repo.getAppointmentDetails({
      'USERID': empCode.toString(),
      'DISTLGDCODE': '0',
      'AREA': '0',
      'Type': '3',
      'REDNO': regNo,
      'FROMDATE': '2024/01/01',
      'TODATE': '2027/07/24',
      'T2T_Order_Id': '0',
    });

    ToastManager.hideLoader();
    isLoading = false;
    _isFetching = false;
    _hasLoaded = true;

    if (response?.output != null && response!.output!.isNotEmpty) {
      dependentList = response.output!;
      final first = dependentList.first;
      districtController.text = first.distName ?? '';
      areaController.text = first.area ?? '';
      addressController.text = first.address ?? '';
      if ((first.appointmentDate ?? '').isNotEmpty) {
        appointmentDateController.text = first.appointmentDate!;
      }
    }
    update();
  }

  Future<void> fetchRemarks() async {
    ToastManager.showLoader();
    final response = await _repo.getAssignmentRemarks('3');
    ToastManager.hideLoader();

    if (response?.output != null) {
      remarkList = response!.output!;
      update();
      _showRemarkBottomSheet();
    } else {
      ToastManager.toast('Failed to load remarks');
    }
  }

  void _showRemarkBottomSheet() {
    showModalBottomSheet(
      context: Get.context!,
      isScrollControlled: true,
      builder: (context) {
        return StatefulBuilder(
          builder: (c, sheetState) {
            return SelectionBottomSheet<AssignmentRemarksOutput, int>(
              title: 'Remark',
              items: remarkList,
              selectedValue: selectedRemark?.arId,
              valueFor: (item) => item.arId ?? 0,
              labelFor: (item) => item.assignmentRemarks ?? 'N/A',
              height: 360,
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
              itemContainerPadding: const EdgeInsets.symmetric(
                vertical: 8.0,
                horizontal: 4.0,
              ),
              selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
              itemTextStyle: TextStyle(
                fontSize: 13,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.normal,
                color: kBlackColor,
              ),
              onItemTap: (item) {
                sheetState(() => selectedRemark = item);
                remarkController.text = item.assignmentRemarks ?? '';
                _handleRemarkSelection(item.arId ?? 0);
                Get.back();
              },
            );
          },
        );
      },
    );
  }

  void _handleRemarkSelection(int arId) {
    if (arId == 4) {
      isControlsDisabled = false;
      showAppointmentDate = false;
      update();
      Future.delayed(const Duration(milliseconds: 200), () {
        ToastManager.showAlertDialog(
          Get.context!,
          'This beneficiary will not be available for CT screening.Please verify before proceeding with the submission.',
          () {
            Get.back();
          },
        );
        // showDialog(
        //   context: Get.context!,
        //   builder:
        //       (_) => AlertDialog(
        //         title: Text(
        //           'Alert',
        //           style: TextStyle(fontFamily: FontConstants.interFonts),
        //         ),
        //         content: Text(
        //           'Beneficiary will not be available for CT screening.',
        //           style: TextStyle(fontFamily: FontConstants.interFonts),
        //         ),
        //         actions: [
        //           TextButton(
        //             onPressed: () => Get.back(),
        //             child: const Text('OK'),
        //           ),
        //         ],
        //       ),
        // );
      });
    } else if (arId == 6) {
      isControlsDisabled = false;
      showAppointmentDate = false;
      update();
      Future.delayed(const Duration(milliseconds: 200), () {
        ToastManager.showAlertDialog(
          Get.context!,
          'Beneficiary can be re-attempted.',
              () {
            Get.back();
          },
        );

        // showDialog(
        //   context: Get.context!,
        //   builder:
        //       (_) => AlertDialog(
        //         title: Text(
        //           'Alert',
        //           style: TextStyle(fontFamily: FontConstants.interFonts),
        //         ),
        //         content: Text(
        //           'Beneficiary can be re-attempted.',
        //           style: TextStyle(fontFamily: FontConstants.interFonts),
        //         ),
        //         actions: [
        //           TextButton(
        //             onPressed: () => Get.back(),
        //             child: const Text('OK'),
        //           ),
        //         ],
        //       ),
        // );
      });
    } else if (arId == 3) {
      isControlsDisabled = false;
      showAppointmentDate = true;
      update();
    } else {
      isControlsDisabled = false;
      showAppointmentDate = false;
      update();
    }
  }

  Future<void> pickAppointmentDate(BuildContext context) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: now,
      lastDate: DateTime(2030),
      builder:
          (context, child) => Theme(
            data: Theme.of(
              context,
            ).copyWith(colorScheme: ColorScheme.light(primary: kPrimaryColor)),
            child: child!,
          ),
    );
    if (picked != null) {
      appointmentDateController.text = _displayFormat.format(picked);
      update();
    }
  }

  Future<void> submit(BuildContext context) async {
    if (selectedRemark == null) {
      ToastManager.toast('Please select a remark');
      return;
    }
    if ((selectedRemark?.arId ?? 0) == 3 &&
        appointmentDateController.text.trim().isEmpty) {
      ToastManager.toast('Please select appointment date');
      return;
    }
    if (dependentList.isEmpty) {
      ToastManager.toast('No beneficiary data to submit');
      return;
    }

    final appointmentDateDT = jsonEncode(
      dependentList
          .map(
            (e) => {
              'T2T_Order_Id': e.t2tOrderId ?? '0',
              'TreatmentID': e.treatmentID ?? '0',
              'Regdno': e.regdNo ?? '',
              'AppointmentDate': appointmentDateController.text.trim(),
            },
          )
          .toList(),
    );

    final data = {
      'AppointmentDateDT': appointmentDateDT,
      'ArId': (selectedRemark?.arId ?? 0).toString(),
    };

    ToastManager.showLoader();
    final response = await _repo.updateAppointmentDate(data);
    ToastManager.hideLoader();

    if (response != null) {
      ToastManager().showSuccessOkayDialog(
        context: Get.context!,
        title: 'Success',
        message: 'Appointment updated successfully',
        onTap: () {
          Get.back();
          Get.back(result: true);
        },
      );
      // showDialog(
      //   context: context,
      //   builder:
      //       (_) => AlertDialog(
      //         title: Text(
      //           'Success',
      //           style: TextStyle(fontFamily: FontConstants.interFonts),
      //         ),
      //         content: Text(
      //           'Appointment updated successfully',
      //           style: TextStyle(fontFamily: FontConstants.interFonts),
      //         ),
      //         actions: [
      //           TextButton(
      //             onPressed: () {
      //               Get.back();
      //               Get.back(result: true);
      //             },
      //             child: const Text('OK'),
      //           ),
      //         ],
      //       ),
      // );
    } else {
      ToastManager.toast('Submission failed');
    }
  }
}
