// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_patient_registration_controller.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';

class AbhaSuccessController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();

  // ── Route arguments ─────────────────────────────────────────────
  String abhaAddress = '';
  String accessToken = '';
  String authToken = '';
  Map<String, dynamic> healthCard = {};

  // ── State ────────────────────────────────────────────────────────
  final cardBytes = Rxn<Uint8List>(); // null = loading, set = ready
  final cardLoading = true.obs;
  final cardError = false.obs;
  final downloading = false.obs;

  // ── Profile helpers ──────────────────────────────────────────────
  Map<String, dynamic> get _profile =>
      (healthCard['ABHAProfile'] as Map<String, dynamic>?) ?? {};

  String get profileName {
    final p = _profile;
    final full = (p['name'] as String?)?.trim() ?? '';
    if (full.isNotEmpty) return full;
    final parts = [
      p['firstName'] as String? ?? '',
      p['middleName'] as String? ?? '',
      p['lastName'] as String? ?? '',
    ].where((s) => s.isNotEmpty).join(' ');
    return parts.isNotEmpty ? parts : '—';
  }

  String get profileAbhaNumber =>
      (_profile['ABHANumber'] as String?) ??
      (_profile['healthId'] as String?) ??
      '—';

  // ── Lifecycle ────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    _fetchCard();
  }

  // ── Auto-load card ────────────────────────────────────────────────

  Future<void> _fetchCard() async {
    cardLoading.value = true;
    cardError.value = false;
    final bytes = await _repo.downloadAbhaCard(
      accessToken: accessToken,
      authToken: authToken,
    );
    if (isClosed) return;
    if (bytes != null && bytes.isNotEmpty) {
      cardBytes.value = bytes;
      cardError.value = false;
    } else {
      cardError.value = true;
    }
    cardLoading.value = false;
  }

  Future<void> retryFetchCard() => _fetchCard();

  // ── Download ──────────────────────────────────────────────────────

  Future<void> onDownload() async {
    if (downloading.value) return;
    final bytes = cardBytes.value;
    if (bytes == null || bytes.isEmpty) {
      ToastManager.toast('Card not loaded yet. Please wait.');
      return;
    }

    // Request storage permission on Android
    if (Platform.isAndroid) {
      final status = await Permission.storage.request();
      if (!status.isGranted) {
        final manage = await Permission.manageExternalStorage.request();
        if (!manage.isGranted) {
          ToastManager.toast('Storage permission denied');
          return;
        }
      }
    }

    downloading.value = true;
    try {
      Directory saveDir;
      if (Platform.isAndroid) {
        saveDir = Directory('/storage/emulated/0/Download');
        if (!saveDir.existsSync()) {
          final ext = await getExternalStorageDirectory();
          saveDir = ext ?? await getApplicationDocumentsDirectory();
        }
      } else {
        saveDir = await getApplicationDocumentsDirectory();
      }
      final fileName =
          'ABHA_Card_${abhaAddress.replaceAll('@', '_').replaceAll('.', '_')}.png';
      final file = File('${saveDir.path}/$fileName');
      await file.writeAsBytes(bytes);
      ToastManager.toast('ABHA card saved to Downloads');
      await OpenFile.open(file.path);
    } catch (e) {
      ToastManager.toast('Unable to save file: $e');
    } finally {
      downloading.value = false;
    }
  }

  void onGoToRegistration() {
    String? mismatchMessage;

    try {
      final regCtrl = Get.find<D2DPatientRegistrationController>();
      final profile =
          (healthCard['ABHAProfile'] as Map<String, dynamic>?) ?? {};
      mismatchMessage = regCtrl.fillFromAbhaCreation(
        profile: profile,
        abhaAddress: abhaAddress,
      );
    } catch (_) {
      // Registration controller not found — navigate anyway
    }

    // Navigate back first (stack: Home → PatientReg → AbhaSuccess)
    final ctx = Get.context;
    if (ctx != null) {
      Navigator.pop(ctx);
    }

    // Show mismatch dialog on the registration screen after navigation settles
    if (mismatchMessage != null) {
      final msg = mismatchMessage;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        ToastManager.showAlertDialog(ctx!, msg, () {
          Get.back();
          try {
            Get.find<D2DPatientRegistrationController>().clearAbhaSearch();
          } catch (_) {}
        }, title: "Board and ABHA Details Mismatch");

        // Get.dialog(
        //   AlertDialog(
        //     title: const Text(
        //       'Board and ABHA Details Mismatch',
        //       style: TextStyle(fontWeight: FontWeight.w700, fontSize: 16),
        //     ),
        //     content: Text(msg),
        //     actions: [
        //       TextButton(
        //         onPressed: () {
        //           Get.back();
        //           try {
        //             Get.find<D2DPatientRegistrationController>()
        //                 .clearAbhaSearch();
        //           } catch (_) {}
        //         },
        //         child: const Text('OK'),
        //       ),
        //     ],
        //   ),
        //   barrierDismissible: false,
        // );
      });
    }
  }
}
