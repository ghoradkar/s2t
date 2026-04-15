// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_success_screen.dart';

class AbhaAddressCreationController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();

  // ── Route arguments (set before use) ───────────────────────────
  String accessToken = '';
  String txnId = '';
  String authToken = '';
  Map<String, dynamic> healthCard = {};
  bool isNew = true;
  String existingABHAAddress = '';
  String mobile = '';
  String campId = '';
  String siteId = '';
  String distLgdCode = '';
  String district = '';
  String campType = '';

  // ── State ───────────────────────────────────────────────────────
  final loading = true.obs;
  final suggestions = <String>[].obs;
  final errorMessage = ''.obs;
  /// true when the current address text passes format rules (or is a suggestion)
  final formatValid = false.obs;
  /// set to true when address was filled from a suggestion (skips length regex)
  bool _fromSuggestion = false;

  // ── Profile helpers (read from healthCard['ABHAProfile']) ────────
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

  /// Returns the existing ABHA address, checking both the top-level field
  /// and ABHAProfile.phrAddress (the ABDM API sometimes returns it there).
  String get resolvedExistingAddress {
    if (existingABHAAddress.isNotEmpty) return existingABHAAddress;
    final phr = _profile['phrAddress'];
    if (phr is List && phr.isNotEmpty) return phr.first.toString();
    if (phr is String && phr.isNotEmpty) return phr;
    return '';
  }

  // ── Text controller ─────────────────────────────────────────────
  final addressCtrl = TextEditingController();

  // ── Lifecycle ────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    _initSession();
  }

  @override
  void onClose() {
    addressCtrl.dispose();
    super.onClose();
  }

  // ── Session + suggestions ────────────────────────────────────────

  Future<void> _initSession() async {
    ToastManager.showLoader();
    // ignore: avoid_print
    print('[AbhaAddr] init: incoming accessToken.len=${accessToken.length}  authToken.len=${authToken.length}  txnId=$txnId');
    final token = await _repo.createAbhaSession();
    if (token != null) {
      accessToken = token;
      // ignore: avoid_print
      print('[AbhaAddr] session refreshed OK  new accessToken.len=${accessToken.length}');
    } else {
      // ignore: avoid_print
      print('[AbhaAddr] session refresh FAILED — using incoming accessToken');
    }
    await _loadSuggestions();
    loading.value = false;
    ToastManager.hideLoader();
  }

  Future<void> _loadSuggestions() async {
    if (accessToken.isEmpty) return;
    final result = await _repo.getAbhaAddressSuggestions(
      accessToken: accessToken,
      txnId: txnId,
    );
    suggestions.assignAll(result);
  }

  // ── Validation ───────────────────────────────────────────────────

  bool _isValidAbhaAddress(String address) {
    final reg = RegExp(r'^[a-zA-Z][a-zA-Z0-9._]{2,13}[a-zA-Z0-9]$');
    return reg.hasMatch(address);
  }

  // ── Actions ──────────────────────────────────────────────────────

  void selectSuggestion(String s) {
    _fromSuggestion = true;
    addressCtrl.text = s;
    errorMessage.value = '';
    formatValid.value = true; // API-provided suggestions are always valid
  }

  /// Called on every keystroke — real-time format validation.
  void onAddressChanged(String value) {
    _fromSuggestion = false;
    if (value.isEmpty) {
      errorMessage.value = '';
      formatValid.value = false;
      return;
    }
    // Must start with a letter
    if (!RegExp(r'^[a-zA-Z]').hasMatch(value)) {
      errorMessage.value = 'Enter valid ABHA address';
      formatValid.value = false;
      return;
    }
    // Full check once 4+ chars typed
    if (value.length >= 4) {
      if (_isValidAbhaAddress(value)) {
        errorMessage.value = '';
        formatValid.value = true;
      } else {
        errorMessage.value = 'Enter valid ABHA address';
        formatValid.value = false;
      }
      return;
    }
    // Still typing prefix (< 4 chars, starts with letter) — no message yet
    errorMessage.value = '';
    formatValid.value = false;
  }

  /// "Check Availability" button action.
  /// ABDM v3 has no separate checkAvailability endpoint — the create API
  /// itself validates and rejects taken addresses, so we call it directly.
  Future<void> onCheckAvailability() async {
    errorMessage.value = '';
    final address = addressCtrl.text.trim();
    if (address.isEmpty) {
      errorMessage.value = 'Please enter an ABHA address';
      return;
    }
    // Suggestions from ABDM may exceed the manual 14-char limit — skip regex.
    if (!_fromSuggestion && !_isValidAbhaAddress(address)) {
      errorMessage.value = 'Enter valid ABHA address';
      return;
    }

    ToastManager.showLoader();
    final result = await _repo.createAbhaAddress(
      accessToken: accessToken,
      txnId: txnId,
      address: address,
      authToken: authToken,
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (result != null && result['error'] == null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        if (Get.context == null) return;
        Navigator.pushReplacement(
          Get.context!,
          MaterialPageRoute(
            builder: (_) => AbhaSuccessScreen(
              abhaAddress: '$address@abdm',
              accessToken: accessToken,
              authToken: authToken,
              healthCard: healthCard,
            ),
          ),
        );
      });
    } else {
      final errBody = result?['error']?.toString() ?? '';
      // ignore: avoid_print
      print('[createAbhaAddress] error body: $errBody');
      if (errBody.toLowerCase().contains('already') ||
          errBody.contains('ABDM-1073') ||
          errBody.contains('409')) {
        errorMessage.value = 'ABHA address already taken. Please choose another.';
      } else if (errBody.contains('Invalid ABHA Address') ||
          errBody.contains('ABDM-1074')) {
        errorMessage.value = 'Invalid ABHA address';
      } else {
        errorMessage.value = 'Unable to create ABHA address. Please try again.';
      }
    }
  }

  void onContinueWithExisting() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (Get.context == null) return;
      Navigator.pushReplacement(
        Get.context!,
        MaterialPageRoute(
          builder: (_) => AbhaSuccessScreen(
            abhaAddress: resolvedExistingAddress.contains('@')
                ? resolvedExistingAddress
                : '$resolvedExistingAddress@abdm',
            accessToken: accessToken,
            authToken: authToken,
            healthCard: healthCard,
          ),
        ),
      );
    });
  }
}