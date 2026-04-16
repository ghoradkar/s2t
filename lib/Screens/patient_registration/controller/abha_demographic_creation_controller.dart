// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_success_screen.dart';

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

  // ── State LGD code map ───────────────────────────────────────────
  static const Map<String, String> kStateLgdCodes = {
    'Andaman and Nicobar Islands': '35',
    'Andhra Pradesh': '28',
    'Arunachal Pradesh': '12',
    'Assam': '18',
    'Bihar': '10',
    'Chandigarh': '4',
    'Chhattisgarh': '22',
    'Dadra and Nagar Haveli and Daman and Diu': '38',
    'Delhi': '7',
    'Goa': '30',
    'Gujarat': '24',
    'Haryana': '6',
    'Himachal Pradesh': '2',
    'Jammu and Kashmir': '1',
    'Jharkhand': '20',
    'Karnataka': '29',
    'Kerala': '32',
    'Ladakh': '37',
    'Lakshadweep': '31',
    'Madhya Pradesh': '23',
    'Maharashtra': '27',
    'Manipur': '14',
    'Meghalaya': '17',
    'Mizoram': '15',
    'Nagaland': '13',
    'Odisha': '21',
    'Puducherry': '34',
    'Punjab': '3',
    'Rajasthan': '8',
    'Sikkim': '11',
    'Tamil Nadu': '33',
    'Telangana': '36',
    'Tripura': '16',
    'Uttar Pradesh': '9',
    'Uttarakhand': '5',
    'West Bengal': '19',
  };

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

  // ── Dropdown selections ──────────────────────────────────────────
  final selectedGender = ''.obs; // 'M', 'F', 'O'
  final selectedState = ''.obs;
  final districtList = <DistrictOutput>[].obs;
  String selectedDistrictCode = ''; // LGD code of the selected district

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

  // ── Lifecycle ────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    initSession();
  }

  @override
  void onClose() {
    aadhaarCtrl.dispose();
    nameCtrl.dispose();
    dobCtrl.dispose();
    addressCtrl.dispose();
    districtCtrl.dispose();
    pincodeCtrl.dispose();
    mobileCtrl.dispose();
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

  // ── District list ────────────────────────────────────────────────

  Future<void> fetchDistrictList() async {
    if (districtList.isNotEmpty) return;
    final result = await _repo.getDistrictListForReg();
    if (!isClosed && result?.output != null) {
      districtList.assignAll(result!.output!);
    }
  }

  // ── Validate & submit ────────────────────────────────────────────

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
      ToastManager.toast('Please select district');
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

    // Convert DOB from yyyy/MM/dd → dd-MM-yyyy (matches native app API format)
    final parts = dobCtrl.text.trim().split('/');
    final dobForApi = parts.length == 3 ? '${parts[2]}-${parts[1]}-${parts[0]}' : dobCtrl.text.trim();

    final stateCode = kStateLgdCodes[selectedState.value] ?? '';
    if (stateCode.isEmpty) {
      ToastManager.toast('State code not found. Please re-select state.');
      return;
    }
    if (selectedDistrictCode.isEmpty) {
      ToastManager.toast('Please re-select district');
      return;
    }

    ToastManager.showLoader();
    final result = await _repo.enrolByDemographic(
      accessToken: accessToken!,
      publicKey: publicKey!,
      aadhaarNumber: aadhaarCtrl.text.trim(),
      name: nameCtrl.text.trim(),
      dob: dobForApi,
      gender: selectedGender.value,
      address: addressCtrl.text.trim(),
      stateCode: stateCode,
      districtCode: selectedDistrictCode,
      pinCode: pincodeCtrl.text.trim(),
      mobile: mobileCtrl.text.trim(),
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    // Demographic flow response has healthIdNumber at root level (no txnId at root).
    // txnId is embedded inside the JWT token claims.
    if (result != null && result['error'] == null && result['healthIdNumber'] != null) {
      authToken = result['token'] as String?;
      txnId = _txnIdFromJwt(authToken) ?? '';
      enrollResponse = Map<String, dynamic>.from(result);
      // Demographic flow: no OTP step, no server save — navigate directly
      await _navigateToAddressCreation();
    } else {
      final err = result?['error']?.toString() ?? '';
      ToastManager.toast(
        err.isNotEmpty ? err : 'Enrollment failed. Please check the details.',
      );
    }
  }

  // ── Save to server & navigate to address creation ────────────────

  // Native app navigates directly to ABHAHealthIDActivity (success/card screen)
  // after demographic enrollment — no address creation step needed.
  Future<void> _navigateToAddressCreation() async {
    if (enrollResponse == null) return;
    if (isClosed) return;
    final snapshot = Map<String, dynamic>.from(enrollResponse!);
    final abhaAddress = snapshot['healthId'] as String? ?? '';
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (Get.context == null) return;
      Navigator.pushReplacement(
        Get.context!,
        MaterialPageRoute(
          builder: (_) => AbhaSuccessScreen(
            abhaAddress: abhaAddress,
            accessToken: accessToken!,
            authToken: authToken ?? '',
            healthCard: snapshot,
          ),
        ),
      );
    });
  }

  // ── JWT helper ───────────────────────────────────────────────────

  /// Extracts `txnId` from the JWT payload (middle part, base64-decoded).
  String? _txnIdFromJwt(String? token) {
    if (token == null || token.isEmpty) return null;
    try {
      final parts = token.split('.');
      if (parts.length < 2) return null;
      final payload = base64.normalize(parts[1]);
      final claims = jsonDecode(utf8.decode(base64.decode(payload))) as Map<String, dynamic>;
      return claims['txnId'] as String?;
    } catch (_) {
      return null;
    }
  }

  // ── DOB picker ───────────────────────────────────────────────────

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