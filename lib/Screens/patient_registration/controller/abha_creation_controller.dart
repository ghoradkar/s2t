// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_address_creation_screen.dart';

enum AbhaPhase {
  input,       // Initial: show aadhaar + T&C + Get OTP
  aadhaarOtp,  // After Get OTP: show mobile + OTP pin + timer + Verify/Resend
  mobileInput, // Mobile not linked: let user confirm mobile, then send OTP
  mobileOtp,   // Mobile OTP sent: show OTP pin + timer + Verify/Resend
}

class AbhaCreationController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();

  // ── Consent strings (exact text from native strings.xml) ──────────
  static const consentAadhaar =
      'I am voluntarily sharing my Aadhaar Number / Virtual ID issued by the '
      'Unique Identification Authority of India ("UIDAI"), and my demographic '
      'information for the purpose of creating an Ayushman Bharat Health Account '
      'number ("ABHA number") and Ayushman Bharat Health Account address ("ABHA '
      'Address"). I authorize NHA to use my Aadhaar number / Virtual ID for '
      'performing Aadhaar based authentication with UIDAI as per the provisions '
      'of the Aadhaar Act, 2016 for the aforesaid purpose.';

  static const consentAbhaLink =
      'I consent to usage of my ABHA address and ABHA number for linking of my '
      'legacy (past) government health records and those which will be generated '
      'during this encounter.';

  static const consentHealthRecords =
      'I authorize the sharing of all my health records with healthcare '
      'provider(s) for the purpose of providing healthcare services to me during '
      'this encounter.';

  static const consentAnonymization =
      'I consent to the anonymization and subsequent use of my government health '
      'records for public health purposes.';

  static const consentAnonymization1 =
      'I confirm that I have duly informed and explained the beneficiary of the '
      'contents of consent for aforementioned purposes.';

  static const consentAnonymization2 =
      'I have been explained about the consent as stated above and hereby '
      'provide my consent for the aforementioned purposes.';
  // ──────────────────────────────────────────────────────────────────

  // ── Route arguments (set before use) ───────────────────────────
  String campId = '';
  String siteId = '';
  String distLgdCode = '';
  String district = '';
  String campType = '';
  int empCode = 0;
  String initialMobile = '';

  // ── Session state ───────────────────────────────────────────────
  final sessionLoading = true.obs;
  final sessionReady = false.obs;
  final sessionError = ''.obs;

  // ── Flow state ──────────────────────────────────────────────────
  final phase = AbhaPhase.input.obs;
  final otpInfoMsg = ''.obs;

  // ── API tokens / data ───────────────────────────────────────────
  String? accessToken;
  String? publicKey;
  String? txnId;
  String? authToken;
  Map<String, dynamic>? creationResponse;

  // ── Text controllers ────────────────────────────────────────────
  final aadhaarCtrl = TextEditingController();
  final mobileCtrl = TextEditingController();
  final otpCtrl = TextEditingController();

  // ── Consent checkboxes ──────────────────────────────────────────
  final cbAadhaar = false.obs;
  final cbAbhaLink = false.obs;
  final cbHealthRecord = false.obs;
  final cbAnon = false.obs;
  final cbAnon1 = false.obs;
  final cbAnon2 = false.obs;

  bool get allConsents =>
      cbAadhaar.value &&
      cbAbhaLink.value &&
      cbHealthRecord.value &&
      cbAnon.value &&
      cbAnon1.value &&
      cbAnon2.value;

  // ── Timer ────────────────────────────────────────────────────────
  Timer? _timer;
  final timerSeconds = 60.obs;
  final timerFinished = false.obs;
  int resendAadhaarCount = 0;
  int resendMobileCount = 0;



  // ── Lifecycle ────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    if (initialMobile.isNotEmpty) mobileCtrl.text = initialMobile;
    initSession();
  }

  @override
  void onClose() {
    _timer?.cancel();
    aadhaarCtrl.dispose();
    mobileCtrl.dispose();
    otpCtrl.dispose();
    super.onClose();
  }

  // ── Session ──────────────────────────────────────────────────────

  Future<void> initSession() async {
    sessionError.value = '';
    ToastManager.showLoader();
    accessToken = await _repo.createAbhaSession();
    if (!isClosed && accessToken != null) {
      publicKey = await _repo.getAbhaPublicCertificate(accessToken!);
    }
    ToastManager.hideLoader();
    if (isClosed) return;
    final ready = accessToken != null && publicKey != null;
    sessionReady.value = ready;
    sessionLoading.value = false;
    if (!ready) {
      sessionError.value = accessToken == null
          ? 'Session creation failed. Check network/credentials.'
          : 'Failed to fetch public certificate.';
    }
  }

  // ── Timer ────────────────────────────────────────────────────────

  void _startTimer() {
    _timer?.cancel();
    timerSeconds.value = 60;
    timerFinished.value = false;
    _timer = Timer.periodic(const Duration(seconds: 1), (t) {
      timerSeconds.value--;
      if (timerSeconds.value <= 0) {
        timerFinished.value = true;
        t.cancel();
      }
    });
  }

  // ── API actions ──────────────────────────────────────────────────

  Future<void> onGetOtp() async {
    final aadhaar = aadhaarCtrl.text.trim();
    if (aadhaar.length != 12) {
      ToastManager.toast('Please enter a valid 12-digit Aadhaar number');
      return;
    }
    if (!allConsents) {
      ToastManager.toast('Please accept all terms and conditions to proceed');
      return;
    }
    ToastManager.showLoader();
    final result = await _repo.generateAadhaarOtp(
      accessToken: accessToken!,
      publicKey: publicKey!,
      aadhaarNumber: aadhaar,
    );
    ToastManager.hideLoader();
    if (isClosed) return;
    if (result != null && result['error'] == null && result['txnId'] != null) {
      txnId = result['txnId'] as String;
      otpInfoMsg.value =
          result['message']?.toString() ?? 'OTP sent to registered mobile';
      resendAadhaarCount++;
      _startTimer();
      phase.value = AbhaPhase.aadhaarOtp;
    } else {
      ToastManager.toast('Unable to send OTP. Please check your Aadhaar number.');
    }
  }

  Future<void> onVerifyAadhaarOtp() async {
    final otp = otpCtrl.text.trim();
    if (otp.length != 6) {
      ToastManager.toast('Please enter the 6-digit OTP');
      return;
    }
    final mobile = mobileCtrl.text.trim();
    if (mobile.length != 10) {
      ToastManager.toast('Please enter a valid 10-digit mobile number');
      return;
    }
    ToastManager.showLoader();
    final result = await _repo.enrolByAadhaarOtp(
      accessToken: accessToken!,
      publicKey: publicKey!,
      txnId: txnId!,
      otpValue: otp,
      mobile: mobile,
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (result != null && result['error'] == null && result['txnId'] != null) {
      txnId = result['txnId'] as String;
      final tokens = result['tokens'] as Map<String, dynamic>?;
      authToken = tokens?['token'] as String?;
      creationResponse = Map<String, dynamic>.from(result);
      final profile = result['ABHAProfile'] as Map<String, dynamic>?;
      final profileMobile = profile?['mobile'] as String?;
      if (profileMobile == null ||
          profileMobile.isEmpty ||
          profileMobile != mobile) {
        _timer?.cancel();
        otpCtrl.clear();
        otpInfoMsg.value =
            'Entered mobile is not linked with Aadhaar. Please enter OTP to verify.';
        resendMobileCount = 0;
        phase.value = AbhaPhase.mobileInput;
        await onSendMobileOtp();
      } else {
        await _saveToServer();
      }
    } else {
      final err = result?['error']?.toString() ?? '';
      ToastManager.toast(
          err.contains('ABDM-1204') ? 'OTP validation failed' : 'Unable to verify OTP');
    }
  }

  Future<void> onSendMobileOtp() async {
    final mobile = mobileCtrl.text.trim();
    if (mobile.length != 10) {
      ToastManager.toast('Please enter a valid 10-digit mobile number');
      phase.value = AbhaPhase.mobileInput;
      return;
    }
    ToastManager.showLoader();
    final result = await _repo.sendMobileOtpForAbha(
      accessToken: accessToken!,
      publicKey: publicKey!,
      txnId: txnId!,
      mobile: mobile,
    );
    ToastManager.hideLoader();
    if (isClosed) return;
    if (result != null && result['error'] == null && result['txnId'] != null) {
      txnId = result['txnId'] as String;
      resendMobileCount++;
      _startTimer();
      otpInfoMsg.value =
          result['message']?.toString() ?? 'OTP sent to your mobile number';
      otpCtrl.clear();
      phase.value = AbhaPhase.mobileOtp;
    } else {
      ToastManager.toast('Unable to send OTP to mobile');
    }
  }

  Future<void> onVerifyMobileOtp() async {
    final otp = otpCtrl.text.trim();
    if (otp.length != 6) {
      ToastManager.toast('Please enter the 6-digit OTP');
      return;
    }
    ToastManager.showLoader();
    final result = await _repo.verifyMobileOtpForAbha(
      accessToken: accessToken!,
      publicKey: publicKey!,
      txnId: txnId!,
      otpValue: otp,
    );
    ToastManager.hideLoader();
    if (isClosed) return;
    if (result != null &&
        result['error'] == null &&
        result['txnId'] != null &&
        result['authResult']?.toString().toLowerCase() == 'success') {
      txnId = result['txnId'] as String;
      await _saveToServer();
    } else {
      ToastManager.toast('Unable to verify OTP: ${result?['message'] ?? ''}');
    }
  }

  Future<void> onResend() async {
    if (phase.value == AbhaPhase.aadhaarOtp) {
      if (resendAadhaarCount >= 3) {
        Get.dialog(
          AlertDialog(
            content: const Text('You have reached the OTP resend limit'),
            actions: [
              TextButton(
                onPressed: Get.back,
                child: const Text('OK'),
              ),
            ],
          ),
        );
        return;
      }
      await onGetOtp();
    } else {
      if (resendMobileCount >= 3) {
        Get.dialog(
          AlertDialog(
            content: const Text('You have reached the OTP resend limit'),
            actions: [
              TextButton(
                onPressed: Get.back,
                child: const Text('OK'),
              ),
            ],
          ),
        );
        return;
      }
      await onSendMobileOtp();
    }
  }

  Future<void> _saveToServer() async {
    if (creationResponse == null) return;
    ToastManager.showLoader();
    final payload = Map<String, dynamic>.from(creationResponse!);
    payload['createdBy'] = empCode;
    payload['campId'] = int.tryParse(campId) ?? 0;
    final ok = await _repo.insertAbhaRegistration(payload: payload);
    ToastManager.hideLoader();
    if (isClosed) return;
    if (ok) {
      _timer?.cancel();
      // Capture values before the postFrameCallback (controller may close).
      final snapshot = Map<String, dynamic>.from(creationResponse!);
      final mobile = mobileCtrl.text.trim();
      // Navigate on the next frame so the EasyLoading overlay finishes
      // dismissing before the route transition starts (avoids the
      // _dependents.isEmpty assertion from flutter_easyloading).
      WidgetsBinding.instance.addPostFrameCallback((_) {
        if (Get.context == null) return;
        Navigator.pushReplacement(
          Get.context!,
          MaterialPageRoute(
            builder: (_) => AbhaAddressCreationScreen(
              accessToken: accessToken!,
              txnId: txnId!,
              authToken: authToken ?? '',
              healthCard: snapshot,
              isNew: snapshot['isNew'] == false ||
                  snapshot['isNew']?.toString() == 'false'
                  ? false
                  : true,
              existingABHAAddress:
                  snapshot['existingABHAAddress'] as String? ?? '',
              mobile: mobile,
              campId: campId,
              siteId: siteId,
              distLgdCode: distLgdCode,
              district: district,
              campType: campType,
            ),
          ),
        );
      });
    } else {
      ToastManager.toast('Failed to save ABHA details. Please try again.');
    }
  }
}