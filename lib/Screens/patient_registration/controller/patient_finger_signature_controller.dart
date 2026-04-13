// ignore_for_file: file_names

import 'dart:async';
import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/native/mantra_mfs100_channel.dart';
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
  final VoidCallback? onSuccess; // clears the parent registration form on success

  PatientFingerSignatureController({
    required this.campId,
    required this.siteId,
    required this.regNo,
    this.onSuccess,
  });

  final _repo = RegularPatientRegistrationRepository();

  // ── Patient info ─────────────────────────────────────────────────────────
  final isLoading = false.obs;
  final Rxn<PatientDetailsOnRegNoOutput> patientInfo = Rxn();

  // RegdId returned from the API — used in the submit call
  String regdId = '';

  // ── Screen 1: Thumb capture ──────────────────────────────────────────────
  /// When true the user checked "Finger Issue" — camera is used.
  /// When false and device is connected — hardware scanner is used.
  final isFingerPrintIssue = false.obs;

  final Rxn<File> thumbImageFile = Rxn();
  final isCapturing = false.obs; // spinner during hardware capture
  final isSubmitting = false.obs;

  // ── Hardware scanner state ───────────────────────────────────────────────
  final isDeviceConnected = false.obs;
  StreamSubscription<MantraDeviceEvent>? _deviceEventSub;

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

    _startDeviceListener();
    _fetchPatientInfo();
  }

  @override
  void onClose() {
    MantraMfs100Channel.stopListening();
    _deviceEventSub?.cancel();
    signatureController.dispose();
    super.onClose();
  }

  // ── Hardware scanner events ──────────────────────────────────────────────

  void _startDeviceListener() {
    MantraMfs100Channel.startListening();

    // Poll current connection state once
    MantraMfs100Channel.isDeviceConnected().then((v) {
      isDeviceConnected.value = v;
    });

    // Listen for attach/detach events
    _deviceEventSub = MantraMfs100Channel.deviceEvents.listen((event) {
      if (event.isReady) {
        isDeviceConnected.value = true;
        ToastManager.toast(
          'Mantra scanner ready — ${event.model ?? ''}  ${event.serial ?? ''}'
              .trim(),
        );
      } else if (event.isDetached) {
        isDeviceConnected.value = false;
        // If capture was in progress, stop it
        if (isCapturing.value) {
          isCapturing.value = false;
          MantraMfs100Channel.stopCapture();
        }
        ToastManager.toast('Mantra scanner disconnected');
      } else if (event.event == 'attached' && event.status == 'no_permission') {
        ToastManager.toast('USB permission denied for scanner');
      } else if (event.event == 'attached' && event.status == 'init_failed') {
        ToastManager.toast('Scanner init failed: ${event.error}');
      } else if (event.event == 'host_check_failed') {
        ToastManager.toast('Host check failed: ${event.error}');
      }
    });
  }

  // ── Fetch patient info ───────────────────────────────────────────────────

  Future<void> _fetchPatientInfo() async {
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

  /// Called when the user taps the fingerprint/thumb area.
  ///
  /// Routing logic:
  ///   • isFingerPrintIssue = true  → camera (always)
  ///   • isFingerPrintIssue = false + device connected → hardware scanner
  ///   • isFingerPrintIssue = false + no device        → camera fallback
  Future<void> captureThumbImage(BuildContext context) async {
    if (isCapturing.value) return;

    final useScanner = !isFingerPrintIssue.value && isDeviceConnected.value;

    if (useScanner) {
      await _captureViaScanner();
    } else {
      await _captureViaCamera();
    }
  }

  Future<void> _captureViaScanner() async {
    isCapturing.value = true;
    ToastManager.toast('Place your finger on the sensor…');
    try {
      final result = await MantraMfs100Channel.startCapture(timeoutMs: 10000);
      final file = result.file;
      if (file != null) {
        thumbImageFile.value = file;
        ToastManager.toast(
          'Captured — quality: ${result.quality}, NFIQ: ${result.nfiq}',
        );
      } else {
        ToastManager.toast('Capture complete but file not saved');
      }
    } on MantraCaptureException catch (e) {
      ToastManager.toast('Capture failed: ${e.message}');
    } finally {
      isCapturing.value = false;
    }
  }

  Future<void> _captureViaCamera() async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) {
      thumbImageFile.value = File(picked.path);
    }
  }

  void onFingerIssueToggled(bool val) {
    isFingerPrintIssue.value = val;
    thumbImageFile.value = null; // clear previously captured image
  }

  // ── Navigate to signature screen (Screen 1 "Next") ──────────────────────

  void onNextTapped(BuildContext context, Widget Function() routeBuilder) {
    if (thumbImageFile.value == null) {
      ToastManager.toast('Please capture thumb/fingerprint image first');
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
      isFingerPrintIssue: isFingerPrintIssue.value,
      empCode: empCode,
      thumbImageFile: thumbImageFile.value,
      signatureImageFile: signatureFile,
    );

    isSubmitting.value = false;

    if (result == null) {
      ToastManager.toast('Server not responding. Please try again.');
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
          onSuccess?.call(); // clear parent registration form
          Navigator.of(context)
            ..pop() // dialog
            ..pop() // signature screen
            ..pop(); // finger screen
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

  void _showSuccessDialog(BuildContext context, String message) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (dialogContext) => AlertDialog(
            title: const Text('Success'),
            content: Text(
              message.isNotEmpty ? message : 'Data uploaded successfully',
            ),
            actions: [
              TextButton(
                onPressed: () {
                  onSuccess?.call(); // clear parent registration form
                  Navigator.of(dialogContext)
                    ..pop() // dialog
                    ..pop() // signature screen
                    ..pop(); // finger screen
                },
                child: const Text('OK'),
              ),
            ],
          ),
    );
  }
}
