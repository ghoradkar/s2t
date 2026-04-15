// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_success_screen.dart';

enum _AvailStatus { notChecked, available, unavailable }

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
  final creating = false.obs;
  final suggestions = <String>[].obs;
  final errorMessage = ''.obs;
  // notChecked → available / unavailable
  final availabilityStatus = _AvailStatus.notChecked.obs;
  bool get isAvailable => availabilityStatus.value == _AvailStatus.available;
  bool get isUnavailable =>
      availabilityStatus.value == _AvailStatus.unavailable;

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
    final token = await _repo.createAbhaSession();
    if (token != null) accessToken = token;
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
    addressCtrl.text = s;
    errorMessage.value = '';
    // ABDM-provided suggestions are already valid & available — skip check.
    availabilityStatus.value = _AvailStatus.available;
  }

  void resetAvailability() {
    availabilityStatus.value = _AvailStatus.notChecked;
    errorMessage.value = '';
  }

  Future<void> onCheckAvailability() async {
    errorMessage.value = '';
    final address = addressCtrl.text.trim();
    if (address.isEmpty) {
      errorMessage.value = 'Please enter an ABHA address';
      return;
    }
    if (!_isValidAbhaAddress(address)) {
      errorMessage.value =
          'Invalid address: 4–14 characters, start with a letter, alphanumeric/dot/underscore only';
      return;
    }
    ToastManager.showLoader();
    final available = await _repo.checkAbhaAddressAvailability(
      accessToken: accessToken,
      address: address,
    );
    ToastManager.hideLoader();
    if (isClosed) return;
    if (available == true) {
      availabilityStatus.value = _AvailStatus.available;
      errorMessage.value = '';
    } else if (available == false) {
      availabilityStatus.value = _AvailStatus.unavailable;
      errorMessage.value =
          'ABHA address already taken. Please choose another.';
    } else {
      availabilityStatus.value = _AvailStatus.notChecked;
      errorMessage.value = 'Unable to check availability. Please try again.';
    }
  }

  Future<void> onCreateAddress() async {
    errorMessage.value = '';
    final address = addressCtrl.text.trim();
    if (address.isEmpty) {
      errorMessage.value = 'Please enter an ABHA address';
      return;
    }
    // Availability has already been confirmed (either via API check or
    // suggestion selection), so no further format validation is needed.
    if (availabilityStatus.value != _AvailStatus.available) {
      errorMessage.value = 'Please check availability first';
      return;
    }
    ToastManager.showLoader();
    final result = await _repo.createAbhaAddress(
      accessToken: accessToken,
      txnId: txnId,
      address: address,
    );
    ToastManager.hideLoader();
    creating.value = false;
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
      errorMessage.value = errBody.contains('Invalid ABHA Address')
          ? 'Invalid ABHA Address'
          : 'Unable to create ABHA address';
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