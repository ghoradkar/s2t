// ignore_for_file: file_names

import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
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

  /// Ration card fields — only used for dependent registrations.
  /// When [dependentBocId] != '0', the ration card photo upload screen is
  /// shown after signature upload succeeds.
  final String dependentBocId;
  final String rationCardNumber;

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
    this.dependentBocId = '',
    this.rationCardNumber = '',
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

  // ── Ration card section (fingerprint screen, dependent only) ─────────────
  // 'manual' = 2 photos required, 'digital' = 1 photo required
  final rcType = 'manual'.obs;
  final rcPhotos = <File>[].obs;
  final isUploadingRc = false.obs;

  int get rcMaxPhotos => rcType.value == 'digital' ? 1 : 2;
  int get rcMinPhotos => rcMaxPhotos;

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

  // ── Ration card photo capture (fingerprint screen, dependent only) ───────

  void onRcTypeChanged(String type) {
    rcType.value = type;
    rcPhotos.clear(); // clear captured photos when type is switched
  }

  Future<void> captureRcPhoto(BuildContext context) async {
    if (rcPhotos.length >= rcMaxPhotos) {
      ToastManager.toast('Maximum $rcMaxPhotos photo(s) allowed');
      return;
    }
    final picker = ImagePicker();
    final picked = await picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) {
      rcPhotos.add(File(picked.path));
    }
  }

  void removeRcPhoto(int index) {
    if (index >= 0 && index < rcPhotos.length) {
      rcPhotos.removeAt(index);
    }
  }

  /// Validates ration card photos, uploads each one, then navigates to the
  /// signature screen on success.
  Future<void> uploadRcAndProceed(
    BuildContext context,
    Widget Function() signatureRouteBuilder,
  ) async {
    if (thumbImageFile.value == null) {
      ToastManager.showAlertDialog(
        context,
        'Please capture thumb/fingerprint image first',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }

    if (rcPhotos.length < rcMinPhotos) {
      ToastManager.showAlertDialog(
        context,
        'Please capture at least $rcMinPhotos photo(s) for '
        '${rcType.value == "digital" ? "Digital" : "Old"} ration card',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }

    if (regdId.isEmpty) {
      ToastManager.toast('Patient details not loaded. Please try again.');
      return;
    }

    final user = DataProvider().getParsedUserData()?.output?.first;
    final empCode = (user?.empCode ?? 0).toString();
    final rcNo =
        rationCardNumber.trim().isEmpty ? 'NA' : rationCardNumber.trim();

    isUploadingRc.value = true;
    try {
      for (final photo in List<File>.from(rcPhotos)) {
        final result = await _repo.insertRationCardDetails(
          regdId: regdId,
          empCode: empCode,
          bocwDependentId: dependentBocId,
          rationCardNo: rcNo,
          photoFile: photo,
        );

        if (result == null ||
            result['status']?.toString().toLowerCase() != 'success') {
          if (!context.mounted) return;
          ToastManager.showAlertDialog(
            context,
            result?['message']?.toString() ??
                'Failed to upload ration card photo. Please try again.',
            () => Navigator.of(context, rootNavigator: true).pop(),
          );
          return;
        }
      }

      if (!context.mounted) return;
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        'Ration card photos uploaded successfully',
        () {
          Navigator.of(context).pop(); // close success dialog
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => signatureRouteBuilder()),
          );
        },
      );
    } finally {
      isUploadingRc.value = false;
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
      // Ration card upload is handled in the fingerprint screen (before this).
      // For both dependent and non-dependent, show success and pop back.
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
