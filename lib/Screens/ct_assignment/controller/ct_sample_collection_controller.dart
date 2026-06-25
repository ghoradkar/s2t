// ignore_for_file: file_names

import 'dart:io';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/beneficiary_details_for_assign_teamid_details_response.dart';
import '../model/confirmatory_tests_screening_response.dart';
import '../model/confirmatory_tests_screening_tube_response.dart';
import '../repository/ct_assignment_repository.dart';

class CTSampleCollectionController extends GetxController {
  CTSampleCollectionController({
    required this.beneficiaryDetails,
    this.isAppointmentFlow = false,
  });

  final BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
  final bool isAppointmentFlow;

  final _repo = CTAssignmentRepository();
  int empCode = 0;

  ConfirmatoryTestsScreeningOutput? testDetailslist;
  List<ConfirmatoryTestsScreeningOutput> list = [];
  List<ConfirmatoryTestsScreeningTubeOutput> tubesDetailslist = [];
  List<AssignmentRemarksOutput> remarksList = [];
  AssignmentRemarksOutput? selectedRemark;

  int selectedVendor = 1;
  String? selectedMobileNumber;
  bool isSmsVendorExpanded = false;
  bool isOtpSent = false;
  bool isOtpVerified = false;
  String generatedOtp = '';

  bool shouldShowAppointmentPendingAlert = false;

  final TextEditingController otpController = TextEditingController();
  final TextEditingController barcodeController = TextEditingController();
  final TextEditingController sampleCountController = TextEditingController();
  final TextEditingController remarkController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController labController = TextEditingController();

  String? selectedLabName;
  List<LandingLabCampCreationOutput> labList = [];
  int? selectedLabCode;
  List<TextEditingController> tubeCountControllers = [];

  bool skipFaceDetection = false;
  File? patientPhotoFile;
  File? consentPhotoFile;

  bool get isAppointmentConfirmed =>
      (testDetailslist?.isAppointmentDone ?? 'N').toUpperCase() == 'Y';

  bool get isSampleCollected {
    final val =
        testDetailslist?.sampleCollection ??
        beneficiaryDetails?.sampleCollection ??
        'N';
    return val.toUpperCase() == 'Y';
  }

  String get activeMobile =>
      selectedMobileNumber ??
      testDetailslist?.workersMob ??
      testDetailslist?.mobileNo ??
      '';

  bool get isCollectionRemark =>
      selectedRemark == null || (selectedRemark?.arId ?? 1) == 1;

  @override
  void onInit() {
    super.onInit();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    loadInitialData();
  }

  @override
  void onClose() {
    otpController.dispose();
    barcodeController.dispose();
    sampleCountController.dispose();
    remarkController.dispose();
    mobileController.dispose();
    labController.dispose();
    for (final c in tubeCountControllers) c.dispose();
    super.onClose();
  }

  Future<void> loadInitialData() async {
    ToastManager.showLoader();
    final typeInfo = isAppointmentFlow ? '4' : '9';
    final typeCount = isAppointmentFlow ? '5' : '10';
    final regdno = beneficiaryDetails?.regdno ?? '';
    final t2tOrderId = (beneficiaryDetails?.t2tOrderId ?? 0).toString();

    final results = await Future.wait([
      _repo.fetchTestInfo({
        'USERID': empCode.toString(),
        'DISTLGDCODE': '0',
        'AREA': '0',
        'Type': typeInfo,
        'REDNO': regdno,
        'T2T_Order_Id': t2tOrderId,
        'FROMDATE': '2024/01/01',
        'TODATE': '2027/07/24',
      }),
      _repo.fetchTestInfoCount({
        'USERID': empCode.toString(),
        'DISTLGDCODE': '0',
        'AREA': '0',
        'Type': typeCount,
        'REDNO': regdno,
        'T2T_Order_Id': t2tOrderId,
        'FROMDATE': '2024/01/01',
        'TODATE': '2027/10/24',
      }),
      _repo.fetchAssignmentRemarks({
        'USERID': empCode.toString(),
        'Type': '4',
      }),
    ]);

    final testResp = results[0] as ConfirmatoryTestsScreeningResponse;
    testDetailslist = testResp.output?.first;
    list = testResp.output ?? [];

    final tubeResp = results[1] as ConfirmatoryTestsScreeningTubeResponse;
    tubesDetailslist = tubeResp.output ?? [];
    for (final c in tubeCountControllers) c.dispose();
    tubeCountControllers = tubesDetailslist
        .map((t) => TextEditingController(text: '${t.tubCount ?? 0}'))
        .toList();

    remarksList = (results[2] as dynamic).output ?? [];

    mobileController.text = activeMobile;

    final passedArId = beneficiaryDetails?.arId;
    if (selectedRemark == null && passedArId != null && remarksList.isNotEmpty) {
      final matches = remarksList.where((r) => r.arId == passedArId);
      if (matches.isNotEmpty) {
        selectedRemark = matches.first;
        remarkController.text = selectedRemark?.assignmentRemarks ?? '';
      }
    }

    ToastManager.hideLoader();

    if (isAppointmentFlow && !isAppointmentConfirmed && !isSampleCollected) {
      shouldShowAppointmentPendingAlert = true;
    }

    update();
  }

  void setRemark(AssignmentRemarksOutput remark) {
    selectedRemark = remark;
    remarkController.text = remark.assignmentRemarks ?? '';
    update();
  }

  void setMobileNumber(String mob) {
    selectedMobileNumber = mob;
    mobileController.text = mob;
    update();
  }

  void toggleSmsVendorExpanded() {
    isSmsVendorExpanded = !isSmsVendorExpanded;
    update();
  }

  void setVendor(int v) {
    selectedVendor = v;
    update();
  }

  Future<void> sendOTP() async {
    final mobile = activeMobile;
    if (mobile.isEmpty) {
      ToastManager.toast('Please select a number first');
      return;
    }
    generatedOtp = (10000 + Random().nextInt(90000)).toString();
    final subOrgId =
        DataProvider().getParsedUserData()?.output?.first.subOrgId?.toString() ?? '0';
    final t2tOrderId =
        (testDetailslist?.t2tOrderId ?? beneficiaryDetails?.t2tOrderId ?? 0).toString();
    final params = {
      'MOBNO': mobile,
      'OTP': generatedOtp,
      'RegdId': t2tOrderId,
      'CreatedBy': empCode.toString(),
      'MsgID': '1',
      'SubOrgID': subOrgId,
      'Option': selectedVendor.toString(),
    };
    ToastManager.showLoader();
    final ok = await _repo.sendOTP(params);
    ToastManager.hideLoader();
    if (ok) {
      isOtpSent = true;
      ToastManager.toast('OTP sent to $mobile');
    } else {
      ToastManager.toast('Failed to send OTP');
    }
    update();
  }

  Future<void> verifyOTP() async {
    final entered = otpController.text.trim();
    if (entered.length < 4) {
      ToastManager.toast('Please enter valid OTP');
      return;
    }
    ToastManager.showLoader();
    final ok = await _repo.verifyOTP({'MobNo': activeMobile, 'Otp': entered});
    ToastManager.hideLoader();
    if (ok) {
      isOtpVerified = true;
      ToastManager.toast('OTP verified successfully');
    } else {
      ToastManager.toast('OTP verification failed');
    }
    update();
  }

  Future<List<LandingLabCampCreationOutput>> fetchLabList() async {
    final distCode =
        (testDetailslist?.dISTLGDCODE ?? beneficiaryDetails?.dISTLGDCODE ?? 0).toString();
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchLabList({'DISTLGDCODE': distCode});
      labList = response.output ?? [];
      return labList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void setLabSelection(LandingLabCampCreationOutput lab) {
    selectedLabName = lab.labName;
    selectedLabCode = lab.labCode;
    labController.text = lab.labName ?? '';
    update();
  }

  void toggleSkipFaceDetection(bool v) {
    skipFaceDetection = v;
    update();
  }

  void setPatientPhoto(File file) {
    patientPhotoFile = file;
    update();
  }

  void setConsentPhoto(File file) {
    consentPhotoFile = file;
    update();
  }

  Future<bool> submitSampleCollection() async {
    if (isAppointmentConfirmed && !isOtpVerified && isCollectionRemark) {
      ToastManager.toast('Please verify OTP first');
      return false;
    }

    final regNo = testDetailslist?.regdNo ?? beneficiaryDetails?.regdno ?? '';
    final t2tOrderId =
        (testDetailslist?.t2tOrderId ?? beneficiaryDetails?.t2tOrderId ?? 0).toString();
    final treatmentId = (testDetailslist?.treatmentID ?? 0).toString();

    if (isCollectionRemark) {
      if (selectedLabCode == null) { ToastManager.toast('Please select lab'); return false; }
      if (barcodeController.text.trim().isEmpty) { ToastManager.toast('Please enter barcode'); return false; }
      if (sampleCountController.text.trim().isEmpty) { ToastManager.toast('Please enter sample count'); return false; }

      final totalTubeCount = tubeCountControllers.fold<int>(
        0,
        (sum, c) => sum + (int.tryParse(c.text.trim()) ?? 0),
      );
      final enteredCount = int.tryParse(sampleCountController.text.trim()) ?? -1;
      if (enteredCount != totalTubeCount) {
        ToastManager.toast('Sample count ($enteredCount) must match total tube count ($totalTubeCount)');
        return false;
      }
      if (patientPhotoFile == null) { ToastManager.toast('Please capture beneficiary photo'); return false; }
      if (consentPhotoFile == null) { ToastManager.toast('Please capture consent form & photo ID'); return false; }

      final fields = <String, String>{
        'Regdno': regNo,
        'Barcode': barcodeController.text.trim(),
        'Labcode': (selectedLabCode ?? 0).toString(),
        'Samplecount': sampleCountController.text.trim(),
        'USERID': empCode.toString(),
        'ArId': (selectedRemark?.arId ?? 1).toString(),
        'T2T_Order_Id': t2tOrderId,
        'TreatmentID': treatmentId,
      };
      ToastManager.showLoader();
      final ok = await _repo.submitBarcodeCollectionWithConsent(
        fields,
        patientPhotoFile?.path,
        consentPhotoFile?.path,
      );
      ToastManager.hideLoader();
      return ok;
    } else {
      final params = <String, String>{
        'Regdno': regNo,
        'Barcode': '',
        'Labcode': '0',
        'Samplecount': '0',
        'USERID': empCode.toString(),
        'T2T_Order_Id': t2tOrderId,
        'TreatmentID': treatmentId,
        'ArId': (selectedRemark?.arId ?? 0).toString(),
      };
      ToastManager.showLoader();
      final ok = await _repo.submitBarcodeCollection(params);
      ToastManager.hideLoader();
      return ok;
    }
  }
}
