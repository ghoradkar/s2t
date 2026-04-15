// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_address_creation_screen.dart';

enum AbhaDemoPhase { form, mobileOtp }

class AbhaDemographicCreationController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();

  // ── Consent strings (same as Aadhaar OTP flow) ──────────────────
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

  // ── Indian states list ───────────────────────────────────────────
  static const List<String> kIndianStates = [
    'Andaman and Nicobar Islands',
    'Andhra Pradesh',
    'Arunachal Pradesh',
    'Assam',
    'Bihar',
    'Chandigarh',
    'Chhattisgarh',
    'Dadra and Nagar Haveli and Daman and Diu',
    'Delhi',
    'Goa',
    'Gujarat',
    'Haryana',
    'Himachal Pradesh',
    'Jammu and Kashmir',
    'Jharkhand',
    'Karnataka',
    'Kerala',
    'Ladakh',
    'Lakshadweep',
    'Madhya Pradesh',
    'Maharashtra',
    'Manipur',
    'Meghalaya',
    'Mizoram',
    'Nagaland',
    'Odisha',
    'Puducherry',
    'Punjab',
    'Rajasthan',
    'Sikkim',
    'Tamil Nadu',
    'Telangana',
    'Tripura',
    'Uttar Pradesh',
    'Uttarakhand',
    'West Bengal',
  ];

  // ── Route arguments ──────────────────────────────────────────────
  String campId = '';
  String siteId = '';
  String distLgdCode = '';
  String district = '';
  String campType = '';
  int empCode = 0;

  // ── Session state ────────────────────────────────────────────────
  final sessionLoading = true.obs;
  final sessionReady = false.obs;
  final sessionError = ''.obs;

  // ── Flow state ───────────────────────────────────────────────────
  final phase = AbhaDemoPhase.form.obs;
  final otpInfoMsg = ''.obs;

  // ── API tokens / data ────────────────────────────────────────────
  String? accessToken;
  String? publicKey;
  String? txnId;
  String? authToken;
  Map<String, dynamic>? enrollResponse;

  // ── Form text controllers ────────────────────────────────────────
  final aadhaarCtrl = TextEditingController();
  final nameCtrl = TextEditingController();
  final dobCtrl = TextEditingController();
  final addressCtrl = TextEditingController();
  final districtCtrl = TextEditingController();
  final pincodeCtrl = TextEditingController();
  final mobileCtrl = TextEditingController();
  final otpCtrl = TextEditingController();

  // ── Dropdown selections ──────────────────────────────────────────
  final selectedGender = ''.obs; // 'M', 'F', 'O'
  final selectedState = ''.obs;
  final districtList = <DistrictOutput>[].obs;

  // ── Consent checkboxes ───────────────────────────────────────────
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
  int resendMobileCount = 0;

  // ── Lifecycle ────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    initSession();
  }

  @override
  void onClose() {
    _timer?.cancel();
    aadhaarCtrl.dispose();
    nameCtrl.dispose();
    dobCtrl.dispose();
    addressCtrl.dispose();
    districtCtrl.dispose();
    pincodeCtrl.dispose();
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

  // ── Accept All Consents ──────────────────────────────────────────

  void onAcceptAllChanged(bool value) {
    cbAadhaar.value = value;
    cbAbhaLink.value = value;
    cbHealthRecord.value = value;
    cbAnon.value = value;
    cbAnon1.value = value;
    cbAnon2.value = value;
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

  // ── District list ────────────────────────────────────────────────

  Future<void> fetchDistrictList() async {
    if (districtList.isNotEmpty) return; // already loaded
    final result = await _repo.getDistrictListForReg();
    if (!isClosed && result?.output != null) {
      districtList.assignAll(result!.output!);
    }
  }

  // ── Validate & submit demographic form ───────────────────────────

  Future<void> onCreateAbha() async {
    if (aadhaarCtrl.text.trim().length != 12) {
      ToastManager.toast('Please enter a valid 12-digit Aadhaar number');
      return;
    }
    if (nameCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter name');
      return;
    }
    if (dobCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter date of birth');
      return;
    }
    if (selectedGender.value.isEmpty) {
      ToastManager.toast('Please select gender');
      return;
    }
    if (addressCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter address');
      return;
    }
    if (selectedState.value.isEmpty) {
      ToastManager.toast('Please select state');
      return;
    }
    if (districtCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter district');
      return;
    }
    if (pincodeCtrl.text.trim().length != 6) {
      ToastManager.toast('Please enter a valid 6-digit pincode');
      return;
    }
    if (mobileCtrl.text.trim().length != 10) {
      ToastManager.toast('Please enter a valid 10-digit mobile number');
      return;
    }
    if (!allConsents) {
      ToastManager.toast('Please accept all terms and conditions to proceed');
      return;
    }

    // Convert DOB from yyyy/MM/dd → yyyy-MM-dd for ABDM API
    final dobDash = dobCtrl.text.trim().replaceAll('/', '-');

    ToastManager.showLoader();
    final result = await _repo.enrolByDemographic(
      accessToken: accessToken!,
      publicKey: publicKey!,
      aadhaarNumber: aadhaarCtrl.text.trim(),
      name: nameCtrl.text.trim(),
      dob: dobDash,
      gender: selectedGender.value,
      address: addressCtrl.text.trim(),
      state: selectedState.value,
      district: districtCtrl.text.trim(),
      pinCode: pincodeCtrl.text.trim(),
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (result != null && result['error'] == null && result['txnId'] != null) {
      txnId = result['txnId'] as String;
      final tokens = result['tokens'] as Map<String, dynamic>?;
      authToken = tokens?['token'] as String?;
      enrollResponse = Map<String, dynamic>.from(result);

      final profile = result['ABHAProfile'] as Map<String, dynamic>?;
      final profileMobile = profile?['mobile'] as String?;
      final enteredMobile = mobileCtrl.text.trim();

      if (profileMobile == null ||
          profileMobile.isEmpty ||
          profileMobile != enteredMobile) {
        otpCtrl.clear();
        otpInfoMsg.value =
            'Entered mobile is not linked with Aadhaar. Please enter OTP to verify.';
        resendMobileCount = 0;
        await _sendMobileOtp();
      } else {
        await _saveToServer();
      }
    } else {
      final err = result?['error']?.toString() ?? '';
      ToastManager.toast(
        err.isNotEmpty ? err : 'Enrollment failed. Please check the details.',
      );
    }
  }

  // ── Mobile OTP ───────────────────────────────────────────────────

  Future<void> _sendMobileOtp() async {
    final mobile = mobileCtrl.text.trim();
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
      phase.value = AbhaDemoPhase.mobileOtp;
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
      ToastManager.toast(
          'Unable to verify OTP: ${result?['message'] ?? ''}');
    }
  }

  Future<void> onResend() async {
    if (resendMobileCount >= 3) {
      Get.dialog(
        AlertDialog(
          content: const Text('You have reached the OTP resend limit'),
          actions: [
            TextButton(onPressed: Get.back, child: const Text('OK')),
          ],
        ),
      );
      return;
    }
    await _sendMobileOtp();
  }

  // ── Save to server & navigate ─────────────────────────────────────

  Future<void> _saveToServer() async {
    if (enrollResponse == null) return;
    ToastManager.showLoader();
    final payload = Map<String, dynamic>.from(enrollResponse!);
    payload['createdBy'] = empCode;
    payload['campId'] = int.tryParse(campId) ?? 0;
    final ok = await _repo.insertAbhaRegistration(payload: payload);
    ToastManager.hideLoader();
    if (isClosed) return;
    if (ok) {
      _timer?.cancel();
      final snapshot = Map<String, dynamic>.from(enrollResponse!);
      final mobile = mobileCtrl.text.trim();
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

  // ── DOB helper: picks date and formats as yyyy/MM/dd ─────────────

  Future<void> pickDob(BuildContext context) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(1900),
      lastDate: now,
    );
    if (picked != null) {
      dobCtrl.text = FormatterManager.formatDateToString(picked);
    }
  }
}