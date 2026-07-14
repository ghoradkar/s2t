// ignore_for_file: file_names

import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/camp_details/model/other_reason_for_patient_rejection_response.dart';
import 'package:s2toperational/camp_details/model/test_list_for_reject_response.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../model/audio_screening_details_response.dart';
import '../model/beneficiary_worker_response.dart';
import '../model/lung_function_test_details_response.dart';
import '../model/patient_checkup_analysis_report_response.dart';
import '../model/vision_screening_details_response.dart';
import '../repository/camp_details_repository.dart';
import '../widget/multiple_alert_manager.dart';

class BeneficiaryVerificationController extends GetxController {
  BeneficiaryVerificationController({required this.obj});

  final BeneficiaryWorkerOutput obj;
  final _repo = CampDetailsRepository();
  final MultipleAlertManager alertManager = MultipleAlertManager();

  int dESGID = 0;
  int empCode = 0;

  bool isShowPhlebotomistName = true;
  bool isShowDeny = true;
  bool isShowApprove = true;
  bool isUserInteractionEnabled = true;
  bool showOtherTextField = false;

  int? testToRejectID;
  String testToRejectString = '';
  int? reasonId;
  String reasonDescription = '';

  PatientCheckupAnalysisReportOutput? patientCheckupAnalysisReportOutput;
  String rightRemark = '';
  String remark = '';
  VisionScreeningDetailsOutput? visionScreeningDetailsOutput;
  LungFunctionTestDetailsOutput? lungFunctionTestDetailsOutput;

  File? selectedBeneficiaryFile;
  File? selectedCardFile;

  final TextEditingController otherReasonTextField = TextEditingController();

  bool shouldShowServerError = false;
  bool shouldShowAlerts = false;

  @override
  void onInit() {
    super.onInit();
    _requestStoragePermission();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    _computeVisibilityFlags();
    _computeTestToRejectState();
    if ((obj.otherDescription ?? '').isNotEmpty) showOtherTextField = true;
    loadInitialData();
  }

  @override
  void onClose() {
    otherReasonTextField.dispose();
    super.onClose();
  }

  void _computeVisibilityFlags() {
    const phlebotomistDesgs = {35, 146, 129, 138, 137, 169, 31, 176, 177};
    isShowPhlebotomistName = !phlebotomistDesgs.contains(dESGID);

    final isApproved = obj.isApproved ?? 0;
    final photoSentForVerification = obj.photoSentForVerification ?? 0;

    if (isApproved == 1 || (isApproved == 3 && photoSentForVerification == 1) || isApproved == 2) {
      isShowDeny = false;
    }

    const supervisorDesgs = {92, 29, 104, 162, 78, 77, 128, 108, 139, 136};
    if (supervisorDesgs.contains(dESGID)) {
      if (isApproved == 1 || (isApproved == 3 && photoSentForVerification == 1) || isApproved == 2) {
        isShowApprove = false;
        isShowDeny = false;
      }
    }

    const hideApproveDesgs = {77, 84, 30, 35, 86, 64, 129, 146, 138, 169, 177, 137, 176, 31};
    if (hideApproveDesgs.contains(dESGID)) isShowApprove = false;

    const hideAllDesgs = {34, 147, 130, 141};
    if (hideAllDesgs.contains(dESGID)) {
      isShowApprove = false;
      isShowDeny = false;
    }
  }

  void _computeTestToRejectState() {
    final testId = obj.testId;
    if (testId == null) return;
    testToRejectID = testId;
    const testNames = {
      2: 'Basic Details',
      3: 'Physical Examination',
      4: 'Lung Functioin Test',
      5: 'Audio Screening Test',
      6: 'Vision Screening',
      7: 'Sample Collection',
      8: 'Random Sugar Test',
      9: 'Ackowledgement',
    };
    final name = testNames[testId];
    if (name != null) {
      isUserInteractionEnabled = false;
      testToRejectString = name;
    }
  }

  Future<void> _requestStoragePermission() async {
    if (!kIsWeb) {
      final status = await Permission.storage.status;
      if (!status.isGranted) await Permission.storage.request();
      final cameraStatus = await Permission.camera.status;
      if (!cameraStatus.isGranted) await Permission.camera.request();
    }
  }

  Future<void> loadInitialData() async {
    alertManager.messagesList.clear();
    ToastManager.showLoader();
    final regdIdParam = {'RegdId': obj.regdId.toString()};
    final results = await Future.wait([
      _repo.fetchPatientCheckupSafe(regdIdParam),
      _repo.fetchAudioScreeningDetailsSafe(regdIdParam),
      _repo.fetchVisionScreeningDetailsSafe(regdIdParam),
      _repo.fetchLungFunctionDetailsSafe(regdIdParam),
    ]);
    ToastManager.hideLoader();

    final checkup = results[0] as PatientCheckupAnalysisReportResponse?;
    final audio = results[1] as AudioScreeningDetailsResponse?;
    final vision = results[2] as VisionScreeningDetailsResponse?;
    final lung = results[3] as LungFunctionTestDetailsResponse?;

    if (checkup == null || audio == null || vision == null || lung == null) {
      patientCheckupAnalysisReportOutput = null;
      rightRemark = '';
      remark = '';
      visionScreeningDetailsOutput = null;
      lungFunctionTestDetailsOutput = null;
      shouldShowServerError = true;
    } else {
      patientCheckupAnalysisReportOutput = checkup.output?.first;
      rightRemark = audio.output?.first.rightRemark ?? '';
      remark = audio.output?.first.remark ?? '';
      visionScreeningDetailsOutput = vision.output?.first;
      lungFunctionTestDetailsOutput = lung.output?.first;

      if (rightRemark.toLowerCase() == 'deafness') {
        alertManager.messagesList.add('लाभार्थी उजव्या कानाने मूकबधिर आहे. बरोबर असल्याची खात्री करा.');
      }
      if (remark.toLowerCase() == 'deafness') {
        alertManager.messagesList.add('लाभार्थी डाव्या कानाने मूकबधिर आहे. बरोबर असल्याची खात्री करा.');
      }
      if ((visionScreeningDetailsOutput?.rightRemark ?? '').toLowerCase() == 'right eye blind') {
        alertManager.messagesList.add('लाभार्थी उजव्या डोळ्याने अंध आहे. बरोबर असल्याची खात्री करा.');
      }
      if ((visionScreeningDetailsOutput?.leftRemark ?? '').toLowerCase() == 'left eye blind') {
        alertManager.messagesList.add('लाभार्थी डाव्या डोळ्याने अंध आहे. बरोबर असल्याची खात्री करा.');
      }
      if (alertManager.messagesList.isNotEmpty) shouldShowAlerts = true;
    }
    update();
  }

  Future<List<TestListForRejectOutput>> fetchTestsToReject() async {
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchTestsToReject({'RegdId': obj.regdId.toString()});
      ToastManager.hideLoader();
      return response.output ?? [];
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
      return [];
    }
  }

  Future<List<OtherReasonOutput>> fetchRejectionReasons() async {
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchRejectionReasons();
      ToastManager.hideLoader();
      return response.output ?? [];
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
      return [];
    }
  }

  void selectTestToReject(TestListForRejectOutput t) {
    testToRejectID = t.testId ?? 0;
    testToRejectString = t.testName ?? '';
    update();
  }

  void selectRejectionReason(OtherReasonOutput r) {
    reasonId = r.reasonId ?? 0;
    reasonDescription = r.reasonDescription ?? '';
    update();
  }

  Future<void> uploadPhoto(File imageFile, bool isBeneficiary) async {
    final regdId = obj.regdId ?? 0;
    final siteDetailId = obj.siteDetailId ?? 0;
    final currentIsType = isBeneficiary ? '1' : '2';
    final randomStr = FormatterManager.generateRandomDigits(5);
    final timeStamp = FormatterManager.getFileNameFromDateTime();
    final suffix = isBeneficiary ? 'PR' : 'HC';
    final fileName = '${randomStr}_${timeStamp}_$suffix.png';
    final params = {
      'RegdNo': regdId.toString(),
      'IsType': currentIsType,
      'CreatedBy': empCode.toString(),
      'SiteId': siteDetailId.toString(),
    };
    ToastManager.showLoader();
    final ok = await _repo.uploadVerificationPhoto(params, fileName, imageFile, currentIsType);
    if (ok) {
      selectedBeneficiaryFile = null;
      selectedCardFile = null;
      ToastManager.toast('Photo updated successfully');
      await loadInitialData();
    } else {
      ToastManager.hideLoader();
    }
    update();
  }

  bool approveValidation() {
    if ((obj.aLLTESTDONE ?? 0) == 0) {
      ToastManager.toast('Beneficiary test are pending');
      return false;
    }
    return true;
  }

  bool denyValidation() {
    if (testToRejectID == null) {
      ToastManager.toast('Please select Test To Reject');
      return false;
    }
    if (reasonId == null) {
      ToastManager.toast('Please select reason');
      return false;
    }
    if (showOtherTextField && otherReasonTextField.text.isEmpty) {
      ToastManager.toast('Please enter other description');
      return false;
    }
    return true;
  }

  Future<bool> submitApproveOrDeny({
    required String isApproved,
  }) async {
    final regdId = obj.regdId ?? 0;
    final campId = obj.campId ?? 0;
    ToastManager.showLoader();
    final ok = await _repo.submitApproveOrDeny({
      'RegdNo': regdId.toString(),
      'CampId': campId.toString(),
      'TestId': (testToRejectID ?? 0).toString(),
      'Reason': reasonDescription,
      'IsApproved': isApproved,
      'CreatedBy': empCode.toString(),
      'ReasonId': (reasonId ?? 0).toString(),
      'OtherDescription': otherReasonTextField.text,
    });
    ToastManager.hideLoader();
    return ok;
  }
}
