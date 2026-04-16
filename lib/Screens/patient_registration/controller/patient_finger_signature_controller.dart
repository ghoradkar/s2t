// ignore_for_file: file_names

import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/patient_registration/model/patient_details_on_reg_no_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/regular_patient_registration_repository.dart';
import 'package:signature/signature.dart';

class PatientFingerSignatureController extends GetxController {
  // ── Passed from registration screen ─────────────────────────────────────
  final String campId;
  final String siteId;
  final String regNo; // worker registration number
  final VoidCallback?
  onSuccess; // clears the parent registration form on success

  /// Optional pre-filled patient data (D2D registration passes these to avoid
  /// a redundant API call immediately after saving the patient).
  final String? prefillRegdId;
  final String? prefillRegdNo;
  final String? prefillName;
  final String? prefillGender;
  final String? prefillAge;
  final String? prefillDob;

  PatientFingerSignatureController({
    required this.campId,
    required this.siteId,
    required this.regNo,
    this.onSuccess,
    this.prefillRegdId,
    this.prefillRegdNo,
    this.prefillName,
    this.prefillGender,
    this.prefillAge,
    this.prefillDob,
  });

  final _repo = RegularPatientRegistrationRepository();

  // ── Patient info ─────────────────────────────────────────────────────────
  final isLoading = false.obs;
  final Rxn<PatientDetailsOnRegNoOutput> patientInfo = Rxn();

  // RegdId returned from the API — used in the submit call
  String regdId = '';

  // ── Screen 1: Thumb capture ──────────────────────────────────────────────
  final Rxn<File> thumbImageFile = Rxn();
  final isSubmitting = false.obs;

  // ── Screen 2: Signature ──────────────────────────────────────────────────
  final isSignatureApplicable = true.obs; // true = signature required
  late final SignatureController signatureController;
  final isSigned = false.obs;

  // ── Lifecycle ────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    signatureController = SignatureController(
      penStrokeWidth: 2,
      penColor: Colors.black,
      exportBackgroundColor: Colors.white,
    );
    signatureController.addListener(() {
      isSigned.value = signatureController.isNotEmpty;
    });

    _fetchPatientInfo();
  }

  @override
  void onClose() {
    signatureController.dispose();
    super.onClose();
  }

  // ── Fetch patient info ───────────────────────────────────────────────────

  Future<void> _fetchPatientInfo() async {
    // If pre-filled data was passed (e.g. from D2D registration after save),
    // use it directly and skip the API call.
    if (prefillRegdId != null && prefillRegdId!.isNotEmpty) {
      regdId = prefillRegdId!;
      patientInfo.value = PatientDetailsOnRegNoOutput.fromPrefill(
        regdId: prefillRegdId!,
        regdNo: prefillRegdNo ?? regNo,
        name: prefillName ?? '',
        gender: prefillGender ?? '',
        age: prefillAge ?? '',
        dob: prefillDob ?? '',
      );
      return;
    }

    isLoading.value = true;
    final result = await _repo.getPatientDetailsByRegNo(regNo: regNo);
    isLoading.value = false;

    if (result == null ||
        result.status?.toLowerCase() != 'success' ||
        (result.output?.isEmpty ?? true)) {
      ToastManager.toast(result?.message ?? 'Failed to fetch patient details');
      return;
    }

    final info = result.output!.last;
    patientInfo.value = info;
    regdId = info.regdId ?? '';
  }

  // ── Thumb image capture ──────────────────────────────────────────────────

  /// Called when the user taps the thumb/fingerprint capture area.
  Future<void> captureThumbImage(BuildContext context) async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) {
      thumbImageFile.value = File(picked.path);
    }
  }

  // ── Navigate to signature screen (Screen 1 "Next") ──────────────────────

  void onNextTapped(BuildContext context, Widget Function() routeBuilder) {
    if (thumbImageFile.value == null) {
      // ToastManager.toast('Please capture thumb/fingerprint image first');

      ToastManager.showAlertDialog(
        Get.context!,
        "Please capture thumb/fingerprint image first",
        () {
          Get.back();
        },
      );
      return;
    }
    Navigator.push(context, MaterialPageRoute(builder: (_) => routeBuilder()));
  }

  // ── Signature screen (Screen 2) ──────────────────────────────────────────

  void onSignatureApplicableToggled(bool val) {
    isSignatureApplicable.value = !val; // checkbox "Not Applicable" → invert
    if (!isSignatureApplicable.value) {
      signatureController.clear();
    }
  }

  void clearSignature() {
    signatureController.clear();
  }

  Future<void> submitSignatureAndThumb(BuildContext context) async {
    if (isSignatureApplicable.value && !isSigned.value) {
      ToastManager.toast('Please sign on the signature pad');
      return;
    }

    if (regdId.isEmpty) {
      ToastManager.toast('Patient details not loaded. Please try again.');
      return;
    }

    isSubmitting.value = true;

    // Export signature as PNG file
    File? signatureFile;
    if (isSigned.value) {
      final Uint8List? bytes = await signatureController.toPngBytes();
      if (bytes != null) {
        final dir = await getTemporaryDirectory();
        signatureFile = await File(
          '${dir.path}/${regdId}_0_${campId}_SG.png',
        ).writeAsBytes(bytes);
      }
    }

    final user = DataProvider().getParsedUserData()?.output?.first;
    final empCode = (user?.empCode ?? 0).toString();

    final result = await _repo.insertSignatureAndThumb(
      regdId: regdId,
      siteId: siteId,
      campId: campId,
      isSignatureApplicable: isSignatureApplicable.value,
      isFingerPrintIssue: false,
      empCode: empCode,
      thumbImageFile: thumbImageFile.value,
      signatureImageFile: signatureFile,
    );

    isSubmitting.value = false;

    if (result == null) {
      // ToastManager.toast('Server not responding. Please try again.');

      ToastManager.showAlertDialog(
        Get.context!,
        "Server not responding. Please try again.",
        () {
          Get.back();
        },
      );
      return;
    }

    final status = result['status']?.toString() ?? '';
    final message = result['message']?.toString() ?? '';

    if (!context.mounted) return;

    if (status.toLowerCase() == 'success') {
      // _showSuccessDialog(context, message);

      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        message.isNotEmpty ? message : "Data uploaded successfully",
        () {
          onSuccess?.call();
          Navigator.of(context)
            ..pop()
            ..pop()
            ..pop();
        },
      );
    } else {
      ToastManager.showAlertDialog(
        context,
        message,
        () => Navigator.of(context).pop(),
      );
    }
  }
}
