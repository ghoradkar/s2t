// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:geolocator/geolocator.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/document_type_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/worker_info_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/patient_finger_signature_screen.dart';

class D2DPatientRegistrationController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();
  final _picker = ImagePicker();

  String navCampId = '';
  String navCampLocation = '';
  String navSiteId = '';
  String navDistLgd = '';
  String navType = '6';
  String navCampType = '3'; // '1' = Regular Camp, '3' = D2D Camp

  int empCode = 0;
  int subOrgId = 0;
  String talLgd = '0';
  String maritalStatusId = '1';
  String _workerRegdId = '0';
  String _beneficiaryCount =
      '0'; // Count from GetBenificiaryRegisterOrNot — appended to RegdNo
  String _appVersion = '';

  final registrationType = 'without_abha'.obs;
  final isDependent = false.obs;
  final workerMode = 'board'.obs;

  /// Board/beneficiary name and gender stored when worker-info API data loads.
  /// Used to detect mismatch when ABHA data is filled back.
  String benefBoardName = '';
  String benefBoardGender = '';

  final tecWorkerRegNo = TextEditingController();
  final tecFullName = TextEditingController();
  final tecFirstName = TextEditingController();
  final tecMiddleName = TextEditingController();
  final tecLastName = TextEditingController();
  final tecMobileNo = TextEditingController();
  final tecAltMobileNo = TextEditingController();
  final tecAadhaarNo = TextEditingController();
  final tecDob = TextEditingController();
  final tecAge = TextEditingController();
  final tecEducation = TextEditingController();
  final tecPermAddr = TextEditingController();
  final tecLocalAddr = TextEditingController();
  final tecCurrentAddr = TextEditingController();
  final tecLandmark = TextEditingController();
  final tecTaluka = TextEditingController();
  final tecDistrict = TextEditingController();
  final tecPostOffice = TextEditingController();
  final tecPincode = TextEditingController();
  final tecRenewalDate = TextEditingController();
  final tecCardExpiry = TextEditingController();
  final tecAbhaNumber = TextEditingController();
  final tecAbhaAddress = TextEditingController();
  final tecAbhaOtp = TextEditingController();
  final tecMobileOtp = TextEditingController();

  final selectedTitle = ''.obs;
  final selectedGender = ''.obs;
  final selfMobNoMode = '1'.obs;
  final isNumberBgsChecked = false.obs;
  final whatsAppMode = '1'.obs;
  final isHCRenewal = false.obs;
  final showRenewal = false.obs;
  final isFaceDetection = false.obs;
  final selectedRelation = Rxn<RelationOutput>();
  final relationList = <RelationOutput>[].obs;

  /// true when the selected relation forces a specific gender (locks the gender toggle)
  final isGenderLockedByRelation = false.obs;

  final abhaVerified = false.obs;
  final abhaFormLocked = false.obs; // true after successful ABHA-creation fill
  final abhaOtpSent = false.obs;
  final abhaOtpTimer = 120.obs;
  int abhaResendCount = 0;
  String _generatedAbhaOtp = '';
  String _abhaNameAtVerify = '';
  String _abhaGenderAtVerify = '';
  Timer? _abhaTimer;

  // ── ABHA create / search sub-mode ────────────────────────────────────────
  /// 'demographic' or 'aadhaar_otp'
  final abhaCreateMode = 'aadhaar_otp'.obs;
  /// 'find' or 'verify'
  final abhaSearchMode = 'find'.obs;
  /// 'mobile' or 'aadhaar'
  final abhaValidateMode = 'mobile'.obs;
  final abhaOtpAttempts = 0.obs;
  final tecAbhaLinkedMobile = TextEditingController();
  final tecAbhaAadhaar = TextEditingController();

  final mobileOtpSent = false.obs;
  final mobileOtpVerified = false.obs;
  String _generatedOtp = '';

  // ── New state for 4-scenario form logic ────────────────────────────────────
  /// true after reg-no API returns data → drives field readOnly / visible states
  final hasApiData = false.obs;

  /// "This Number not belongs to beneficiary" checkbox
  final isNumberNotBelongsToBeneficiary = false.obs;

  /// Alternate mobile OTP flow
  final altMobileOtpSent = false.obs;
  final altMobileOtpVerified = false.obs;
  String _generatedAltOtp = '';

  /// Alternate mobile belongs to (1=Self, 2=Spouse, 3=Child)
  final altMobileBelongsTo = '1'.obs;

  /// Worker's gender selected by phlebo (for isDependent=Yes)
  final workerGenderByPhlebo = ''.obs;

  /// Worker marital status selection (drives relation list)
  final selectedWorkerMaritalStatusId = '0'.obs;
  final selectedWorkerMaritalStatusName = ''.obs;

  /// Worker info shown as read-only cards when isDependent=Yes + hasApiData=true
  final workerNameDisplay = ''.obs;
  final workerAgeDisplay = ''.obs;
  final workerGenderDisplay = ''.obs;

  /// Identity card selection (isDependent=Yes)
  final identityList = <DocumentTypeOutput>[].obs;
  final selectedIdentityId = '0'.obs;
  final selectedIdentityName = ''.obs;
  final isLoadingIdentity = false.obs;

  /// District / Taluka dropdowns (isDependent=No + hasApiData=true)
  final regDistrictList = <DistrictOutput>[].obs;
  final regTalukaList = <UserMappedTalukaOutput>[].obs;

  /// true when API returned a non-empty value → field stays readonly
  final isDistrictLocked = false.obs;
  final isTalukaLocked = false.obs;

  /// District LGD code used when fetching talukas
  String _regDistLgdCode = '';

  // New text controllers
  final tecAltMobileOtp = TextEditingController();

  final patientPhotoPath = ''.obs;
  final healthCardPhotoPath = ''.obs;
  final hivLetterPath = ''.obs;
  final renewalFormPath = ''.obs;

  final currentLat = '0.0'.obs;
  final currentLong = '0.0'.obs;

  final isLoadingBeneficiary = false.obs;
  final isSubmitting = false.obs;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    talLgd = user?.tALLGDCODE?.toString() ?? '0';
    final rawMsId = user?.maritialstatusId?.toString() ?? '';
    maritalStatusId = (int.tryParse(rawMsId) != null) ? rawMsId : '1';
    _captureLocation();
    _loadAppVersion();
  }

  Future<void> _loadAppVersion() async {
    final info = await PackageInfo.fromPlatform();
    _appVersion = info.version;
  }

  Future<void> _captureLocation() async {
    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) return;

      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) return;
      }
      if (permission == LocationPermission.deniedForever) return;

      final position = await Geolocator.getCurrentPosition(
        locationSettings: const LocationSettings(
          accuracy: LocationAccuracy.high,
        ),
      );
      currentLat.value = position.latitude.toStringAsFixed(6);
      currentLong.value = position.longitude.toStringAsFixed(6);
    } catch (_) {
      // GPS unavailable — values remain '0.0'
    }
  }

  void onRegistrationTypeChanged(String type) {
    registrationType.value = type;
    if (type == 'without_abha') {
      abhaVerified.value = false;
      abhaFormLocked.value = false;
      abhaOtpSent.value = false;
      abhaResendCount = 0;
      _abhaTimer?.cancel();
      abhaOtpTimer.value = 120;
      _abhaNameAtVerify = '';
      _abhaGenderAtVerify = '';
      tecAbhaNumber.clear();
      tecAbhaAddress.clear();
      tecAbhaOtp.clear();
      abhaCreateMode.value = 'aadhaar_otp';
      abhaSearchMode.value = 'find';
      abhaValidateMode.value = 'mobile';
      abhaOtpAttempts.value = 0;
      tecAbhaLinkedMobile.clear();
      tecAbhaAadhaar.clear();
    }
  }

  /// Called from AbhaSuccessScreen "Go-To Registration" to pre-fill the form.
  ///
  /// [profile]     = healthCard['ABHAProfile'] from the enrollment response.
  /// [abhaAddress] = the ABHA address just created (e.g. "john.doe@abdm").
  /// Returns a mismatch message string if board/ABHA data don't match,
  /// null if everything is fine and form was pre-filled successfully.
  String? fillFromAbhaCreation({
    required Map<String, dynamic> profile,
    required String abhaAddress,
  }) {
    // ── Resolve ABHA name & gender from profile ───────────────────────
    final firstName = ((profile['firstName'] as String?) ?? '').trim();
    final middleName = ((profile['middleName'] as String?) ?? '').trim();
    final lastName = ((profile['lastName'] as String?) ?? '').trim();
    final fullFromApi = ((profile['name'] as String?) ?? '').trim();
    final abhaFullName = fullFromApi.isNotEmpty
        ? fullFromApi
        : [firstName, middleName, lastName]
            .where((s) => s.isNotEmpty)
            .join(' ');

    final genderRaw =
        ((profile['gender'] as String?) ?? '').trim().toUpperCase();
    final abhaGender = (genderRaw == 'M' || genderRaw == 'MALE')
        ? 'Male'
        : (genderRaw == 'F' || genderRaw == 'FEMALE')
            ? 'Female'
            : genderRaw.isNotEmpty
                ? 'Other'
                : '';

    // ── Board vs ABHA mismatch check (mirrors native logic) ───────────
    final boardName = benefBoardName.trim();
    if (!abhaFullName.toLowerCase().contains(boardName.toLowerCase()) ||
        (boardName.isEmpty && abhaFullName.isNotEmpty)) {
      // Return mismatch message — caller must show dialog AFTER navigating back
      final boardGender = benefBoardGender.isNotEmpty ? benefBoardGender : '—';
      final abhaGenderDisplay = abhaGender.isNotEmpty ? abhaGender : '—';
      return 'ABHA and Board details does not match\n\n'
          'Details:\n'
          'Board Name: $boardName\n'
          'ABHA Name: $abhaFullName\n'
          'Board Gender: $boardGender\n'
          'ABHA Gender: $abhaGenderDisplay';
    }

    // ── Switch to "With ABHA" and mark as verified ──────────────────
    registrationType.value = 'with_abha';
    abhaVerified.value = true;

    // ── Name ─────────────────────────────────────────────────────────
    tecFirstName.text = firstName;
    tecMiddleName.text = middleName;
    tecLastName.text = lastName;
    if (fullFromApi.isNotEmpty) {
      tecFullName.text = fullFromApi;
    } else {
      onNamePartsChanged();
    }

    // ── Mobile ───────────────────────────────────────────────────────
    final mobile = ((profile['mobile'] as String?) ?? '').trim();
    if (mobile.isNotEmpty) tecMobileNo.text = mobile;

    // ── DOB & Age ────────────────────────────────────────────────────
    final dobRaw = ((profile['dob'] as String?) ??
                    (profile['dateOfBirth'] as String?) ?? '').trim();
    if (dobRaw.isNotEmpty) {
      tecDob.text = _normalizeDate(dobRaw);
      try {
        final parts = dobRaw.split(RegExp(r'[-/]'));
        if (parts.length == 3) {
          final year = int.parse(parts[0].length == 4 ? parts[0] : parts[2]);
          final age = DateTime.now().year - year;
          if (age > 0) tecAge.text = age.toString();
        }
      } catch (_) {}
    }

    // ── Gender ────────────────────────────────────────────────────────
    if (genderRaw == 'M' || genderRaw == 'MALE') {
      selectedGender.value = 'M';
    } else if (genderRaw == 'F' || genderRaw == 'FEMALE') {
      selectedGender.value = 'F';
    } else if (genderRaw.isNotEmpty) {
      selectedGender.value = 'O';
    }

    // ── ABHA Number ───────────────────────────────────────────────────
    final abhaNumber = ((profile['ABHANumber'] as String?) ??
                        (profile['healthId'] as String?) ?? '').trim();
    if (abhaNumber.isNotEmpty) tecAbhaNumber.text = abhaNumber;

    // ── ABHA Address ──────────────────────────────────────────────────
    tecAbhaAddress.text = abhaAddress;

    // ── Permanent Address (native: address + "," + pincode) ───────────
    final addr = ((profile['address'] as String?) ?? '').trim();
    final pin  = ((profile['pincode'] as String?) ?? '').trim();
    if (addr.isNotEmpty) {
      tecPermAddr.text = pin.isNotEmpty ? '$addr,$pin' : addr;
    }

    // ── Pincode ───────────────────────────────────────────────────────
    if (pin.isNotEmpty) tecPincode.text = pin;

    // ── District (native: set from districtName, then disabled) ───────
    final district = ((profile['districtName'] as String?) ?? '').trim();
    if (district.isNotEmpty) tecDistrict.text = district;

    // ── Current Address (native: explicitly cleared) ──────────────────
    tecCurrentAddr.clear();
    // Local Address — native does NOT set it from ABHA (left for user entry)

    // ── Lock internal name snapshot (used by submit validation) ───────
    _abhaNameAtVerify = tecFullName.text.trim();
    _abhaGenderAtVerify = selectedGender.value;

    // ── Lock form — mirrors native disableABHAFormAfterFill + disableIfFilled
    abhaFormLocked.value = true;
    return null;
  }

  void clearAbhaSearch() {
    abhaSearchMode.value = 'find';
    abhaValidateMode.value = 'mobile';
    tecAbhaNumber.clear();
    tecAbhaAddress.clear();
    tecAbhaLinkedMobile.clear();
    tecAbhaAadhaar.clear();
    tecAbhaOtp.clear();
    abhaOtpSent.value = false;
    abhaVerified.value = false;
    abhaFormLocked.value = false;
    abhaResendCount = 0;
    abhaOtpAttempts.value = 0;
    _abhaTimer?.cancel();
    abhaOtpTimer.value = 120;
  }

  /// Called by the Clear button shown in the verified banner after ABHA-creation
  /// fill. Mirrors native: clearABHA() + clearPatientDetails(3) + enableABHAForm.
  ///
  /// clearFlag=3 in native means: clear everything EXCEPT the name fields.
  void clearAfterAbhaFill() {
    // ── Reset ABHA section (mirrors clearABHA + enableABHAFormAfterFill) ──
    clearAbhaSearch();          // resets abhaFormLocked, abhaVerified, ABHA fields
    abhaCreateMode.value = 'aadhaar_otp';

    // ── Clear patient details — mirrors clearPatientDetails(3) ────────
    // Name fields (first/middle/last/full) are intentionally NOT cleared
    // (native clearPatientDetails with flag=3 skips edt_fname)

    tecMobileNo.clear();
    tecAadhaarNo.clear();
    tecDob.clear();
    tecAge.clear();
    tecPermAddr.clear();
    tecLocalAddr.clear();
    tecCurrentAddr.clear();
    tecPincode.clear();
    tecRenewalDate.clear();
    tecEducation.clear();

    // Reset gender so user can re-select
    selectedGender.value = '';
    isGenderLockedByRelation.value = false;

    // ── Reset OTP / verification state ───────────────────────────────
    mobileOtpSent.value = false;
    mobileOtpVerified.value = false;
    altMobileOtpSent.value = false;
    altMobileOtpVerified.value = false;
    _abhaNameAtVerify = '';
    _abhaGenderAtVerify = '';
  }

  void onDependentToggled(bool val) {
    isDependent.value = val;
    // Reset the API-data state and clear the form whenever toggled
    hasApiData.value = false;
    tecWorkerRegNo.clear();
    _clearForm();
    selectedRelation.value = null;
    relationList.clear();
    selectedWorkerMaritalStatusId.value = '0';
    selectedWorkerMaritalStatusName.value = '';
    workerNameDisplay.value = '';
    workerAgeDisplay.value = '';
    workerGenderDisplay.value = '';
    if (val) {
      // isDependent=Yes → fetch relations with default marital status
      fetchRelationList(
        selectedWorkerMaritalStatusId.value,
        selectedGender.value,
      );
    }
  }

  void onWorkerMaritalStatusChanged(String id, String name) {
    selectedWorkerMaritalStatusId.value = id;
    selectedWorkerMaritalStatusName.value = name;
    // Relation list depends on worker's gender, not the dependent's gender
    fetchRelationList(id, workerGenderByPhlebo.value);
  }

  void onWorkerModeChanged(String mode) {
    workerMode.value = mode;
  }

  void onWorkerRegNoChanged(String val) {
    if (val.length == 12) {
      _fetchBeneficiary(val);
    }
  }

  Future<void> _fetchBeneficiary(String regNo) async {
    isLoadingBeneficiary.value = true;
    hasApiData.value = false;
    try {
      final workerInfo = await _repo.getWorkerInfoWithMaritalStatus(
        regNo: regNo,
      );
      final regdInfo = await _repo.getWorkerRegdId(regdNo: regNo);
      _workerRegdId = regdInfo['regdId']!;
      _beneficiaryCount = regdInfo['count']!;
      final data =
          workerInfo?.output?.isNotEmpty == true
              ? workerInfo!.output!.first
              : null;
      if (data == null) {
        ToastManager.toast('Beneficiary not found');
        ToastManager.showAlertDialog(
          Get.context!,
          'Beneficiary not found',
          () {
            Get.back();
          },
        );
        return;
      }
      _applyWorkerInfo(data);
    } finally {
      isLoadingBeneficiary.value = false;
    }
  }

  // ── Alternate mobile OTP ─────────────────────────────────────────────────

  Future<void> sendAltMobileOtp() async {
    final mob = tecAltMobileNo.text.trim();
    if (mob.length != 10) {
      // ToastManager.toast('Enter valid 10-digit alternate mobile number');
      ToastManager.showAlertDialog(
        Get.context!,
        'Enter valid 10-digit alternate mobile number',
            () {
          Get.back();
        },
      );
      return;
    }
    _generatedAltOtp = FormatterManager.generateRandomDigits(5);
    final error = await _repo.sendOtp(
      mobileNo: mob,
      otp: _generatedAltOtp,
      regdId: '0',
      createdBy: empCode.toString(),
      subOrgId: subOrgId.toString(),
    );
    if (error == null) {
      altMobileOtpSent.value = true;
      ToastManager.toast('OTP sent to alternate number');

    } else {
      ToastManager.showAlertDialog(Get.context!, error, () {
        Get.back();
      });
      // ToastManager.toast(error);
    }
  }

  Future<void> verifyAltMobileOtp() async {
    final otp = tecAltMobileOtp.text.trim();
    if (otp.isEmpty) return;
    final success = await _repo.verifyOtp(
      mobileNo: tecAltMobileNo.text.trim(),
      otp: otp,
    );
    if (success) {
      altMobileOtpVerified.value = true;
      ToastManager.toast('Alternate number verified');
    } else {
      // ToastManager.toast('Invalid OTP');
      ToastManager.showAlertDialog(
        Get.context!,
        'Invalid OTP',
            () {
          Get.back();
        },
      );
    }
  }

  void _clearForm() {
    // Worker reg no and API-data gate
    tecWorkerRegNo.clear();
    hasApiData.value = false;

    // Name fields
    tecFullName.clear();
    tecFirstName.clear();
    tecMiddleName.clear();
    tecLastName.clear();

    // Contact
    tecMobileNo.clear();
    tecAltMobileNo.clear();
    tecAltMobileOtp.clear();
    tecAadhaarNo.clear();
    tecAbhaNumber.clear();
    tecAbhaAddress.clear();
    tecAbhaOtp.clear();
    tecMobileOtp.clear();

    // Dates / age
    tecDob.clear();
    tecAge.clear();
    tecCardExpiry.clear();
    tecRenewalDate.clear();

    // Address
    tecPermAddr.clear();
    tecLocalAddr.clear();
    tecCurrentAddr.clear();
    tecLandmark.clear();
    tecTaluka.clear();
    tecDistrict.clear();
    tecPostOffice.clear();
    tecPincode.clear();

    // Dropdowns / toggles
    selectedGender.value = '';
    selectedTitle.value = '';
    isGenderLockedByRelation.value = false;
    showRenewal.value = false;
    isHCRenewal.value = false;
    isNumberNotBelongsToBeneficiary.value = false;

    // OTP flows
    mobileOtpSent.value = false;
    mobileOtpVerified.value = false;
    altMobileOtpSent.value = false;
    altMobileOtpVerified.value = false;
    abhaVerified.value = false;
    abhaFormLocked.value = false;
    abhaOtpSent.value = false;
    abhaResendCount = 0;
    _abhaTimer?.cancel();
    abhaOtpTimer.value = 120;
    abhaCreateMode.value = 'aadhaar_otp';
    abhaSearchMode.value = 'find';
    abhaValidateMode.value = 'mobile';
    abhaOtpAttempts.value = 0;
    tecAbhaLinkedMobile.clear();
    tecAbhaAadhaar.clear();

    // Worker-info display (isDependent = Yes)
    workerNameDisplay.value = '';
    workerAgeDisplay.value = '';
    workerGenderDisplay.value = '';
    workerGenderByPhlebo.value = '';
    selectedWorkerMaritalStatusId.value = '0';
    selectedWorkerMaritalStatusName.value = '';
    selectedRelation.value = null;
    relationList.clear();

    // District / Taluka state
    isDistrictLocked.value = false;
    isTalukaLocked.value = false;
    regDistrictList.clear();
    regTalukaList.clear();
    _regDistLgdCode = '';

    // Internal IDs
    _workerRegdId = '0';
    _beneficiaryCount = '0';
    benefBoardName = '';
    benefBoardGender = '';

    // Photos
    patientPhotoPath.value = '';
    healthCardPhotoPath.value = '';
    renewalFormPath.value = '';
    hivLetterPath.value = '';
  }

  void _applyWorkerInfo(WorkerInfoOutput data) {
    hasApiData.value = true;

    final firstName = (data.firstNamePersonal ?? '').trim();
    final middleName = (data.middleNamePersonal ?? '').trim();
    final lastName = (data.lastNamePersonal ?? '').trim();
    final fullName = data.fullName;

    if (!isDependent.value) {
      // Scenario 2 (No + Data): all name parts pre-filled → UI disables them
      tecFirstName.text = firstName;
      tecMiddleName.text = middleName;
      tecLastName.text = lastName;
      tecFullName.text = fullName;
      // Store board name/gender for ABHA mismatch check
      benefBoardName = fullName;
    } else {
      // Scenario 4 (Yes + Data):
      // Worker display cards at top show full name, age, gender
      workerNameDisplay.value = fullName;
      workerAgeDisplay.value =
          (int.tryParse(
            (data.age ?? '').split('.').first.trim(),
          )?.toString()) ??
          '';
      workerGenderDisplay.value = data.gender ?? '';
      // Pre-select worker gender dropdown (native: benefBoardGender = gender)
      workerGenderByPhlebo.value =
          (data.gender ?? '').isNotEmpty ? data.gender! : '';
      // Last name from API (disabled), first+middle editable
      tecLastName.text = lastName;
      // Native: for male → first-name field shows firstName; for female → empty
      final genderLower = (data.gender ?? '').toLowerCase();
      tecMiddleName.text = genderLower.startsWith('f') ? '' : firstName;
      tecFirstName.clear();
      // Compose full name from available parts
      onNamePartsChanged();
    }

    // Mobile
    tecMobileNo.text = data.mobile ?? '';

    // Aadhaar, DOB, Gender — only pre-fill for the beneficiary themselves
    // (isDependent=No). When registering a dependent the phlebo enters these
    // fields manually for the dependent person.
    if (!isDependent.value) {
      // Aadhaar
      tecAadhaarNo.text = data.aadhaar ?? '';

      // Age + DOB (calculated from age; API has no DOB field)
      final ageInt =
          int.tryParse((data.age ?? '').split('.').first.trim()) ?? 0;
      tecAge.text = ageInt > 0 ? ageInt.toString() : '';
      if (ageInt > 0) {
        final now = DateTime.now();
        final approxDob = DateTime(now.year - ageInt, now.month, now.day);
        tecDob.text = _normalizeDate(
          '${approxDob.year.toString().padLeft(4, '0')}/${approxDob.month.toString().padLeft(2, '0')}/${approxDob.day.toString().padLeft(2, '0')}',
        );
      }

      // Gender
      final genderStr = (data.gender ?? '').toLowerCase();
      if (genderStr.startsWith('m')) {
        selectedGender.value = 'M';
        benefBoardGender = 'Male';
      } else if (genderStr.startsWith('f')) {
        selectedGender.value = 'F';
        benefBoardGender = 'Female';
      }
    }

    // Marital status (drives relation list for isDependent=Yes)
    final msId =
        (int.tryParse(data.maritalStatusID ?? '') != null)
            ? data.maritalStatusID!
            : '1';
    final msName = data.maritalStatus ?? 'Married';
    selectedWorkerMaritalStatusId.value = msId;
    selectedWorkerMaritalStatusName.value = msName;
    maritalStatusId =
        msId; // update from worker data (native: maritalStatus = maritalStatusId)
    if (isDependent.value) {
      fetchRelationList(msId, data.gender ?? '');
    }

    // LGD codes
    if (data.talLgdCode?.isNotEmpty == true) talLgd = data.talLgdCode!;
    _regDistLgdCode = data.distLgdCode ?? '';

    // District / Taluka lock state (both dependent and non-dependent)
    isDistrictLocked.value = (data.residentialDistrict ?? '').isNotEmpty;
    isTalukaLocked.value =
        (data.residentialTaluka ?? '').isNotEmpty &&
        (data.talLgdCode ?? '').isNotEmpty;

    // Addresses — native exact format
    final localAddr = data.localAddress;
    final permAddr = data.permanentAddressFormatted;

    tecPermAddr.text = permAddr;
    tecLocalAddr.text = localAddr;
    tecCurrentAddr.text = localAddr;

    // Separate address component fields
    tecTaluka.text = data.residentialTaluka ?? '';
    tecDistrict.text = data.residentialDistrict ?? '';
    // Landmark = permanent_address_area (native: edt_landMark.setText(permArea))
    tecLandmark.text = data.permanentArea ?? '';
    // Post office = permanent_address_postOffice (native: edt_postOffice.setText(postoffice))
    tecPostOffice.text = data.permanentPostOffice ?? '';
    // Pincode = residential pincode
    final pin = data.residentialPincode ?? '';
    tecPincode.text = (pin == '0') ? '' : pin;

    // Renewal date / card expiry — do NOT auto-trigger showRenewal here;
    // renewal section is user-controlled via the switch in the form.
    final renewal = (data.nextRenewalDate ?? '').trim();
    if (renewal.isNotEmpty) {
      tecRenewalDate.text = _normalizeDate(renewal);
      tecCardExpiry.text = _normalizeDate(renewal);
    }
  }

  void onDobChanged(String date) {
    final dob = _tryParseDate(date);
    if (dob == null) return;
    final now = DateTime.now();
    int age = now.year - dob.year;
    if (now.month < dob.month ||
        (now.month == dob.month && now.day < dob.day)) {
      age--;
    }
    tecAge.text = age.toString();

    if (isDependent.value) {
      final msg = _dependentAgeMessage(age);
      if (msg != null) {
        if (Get.context != null) {
          ToastManager.showAlertDialog(
            Get.context!,
            msg,
            () => Navigator.of(Get.context!, rootNavigator: true).pop(),
          );
        } else {
          ToastManager.toast(msg);
        }
      }
    } else {
      if (age < 18 || age > 60) {
        final msg = 'Age must be between 18 and 60';
        if (Get.context != null) {
          ToastManager.showAlertDialog(
            Get.context!,
            msg,
            () => Navigator.of(Get.context!, rootNavigator: true).pop(),
          );
        } else {
          ToastManager.toast(msg);
        }
      }
    }
  }

  /// Returns the exact native validation message if [age] is invalid for the
  /// currently selected relation, or null if the age is acceptable.
  ///
  /// Relation IDs (from native D2DPatientRegistration_Activity):
  ///   Children   : 5, 6, 7, 8  (Son / Daughter)
  ///   Parents/IL : 1, 2, 21, 22 (Father / Mother / Father-in-law / Mother-in-law)
  ///   Spouse/etc : 9, 10, 17, 18 (Husband / Wife and variants)
  String? _dependentAgeMessage(int age) {
    final rel = selectedRelation.value;
    if (rel == null) return null;

    final relId = rel.relId ?? 0;
    final workerAge = int.tryParse(workerAgeDisplay.value) ?? 0;

    // ── Children: Son / Daughter ──────────────────────────────────────────
    const _childIds = {5, 6, 7, 8};
    if (_childIds.contains(relId)) {
      final ageDiff = workerAge > 0 ? workerAge - age : 999;
      if (age < 10 || age > 17 || ageDiff < 15) {
        return '१. नोंदणीकृत बांधकाम कामगाराच्या मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त व '
            '१८ वर्षांपर्यंत असावे आणि\n'
            '२. मुलगा/मुलगी आणि नोंदणीकृत बांधकाम कामगार यांच्या वयातील फरक किमान '
            '१५ वर्ष असावा.';
      }
      return null;
    }

    // ── Parents / In-laws ────────────────────────────────────────────────
    const _parentIds = {1, 2, 21, 22};
    if (_parentIds.contains(relId)) {
      if (age < 18 || (workerAge > 0 && age <= workerAge)) {
        return 'आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम कामगारापेक्षा जास्त असावे.';
      }
      return null;
    }

    // ── Spouse / other adult relations ────────────────────────────────────
    if (age < 18 || age > 75) {
      return 'Dependent age should not be less than 18 years or more than 75 years';
    }
    return null;
  }

  void onNamePartsChanged() {
    final parts =
        [
          tecFirstName.text.trim(),
          tecMiddleName.text.trim(),
          tecLastName.text.trim(),
        ].where((e) => e.isNotEmpty).toList();
    tecFullName.text = parts.join(' ');
  }

  Future<void> fetchIdentityList() async {
    if (identityList.isNotEmpty) return; // already loaded
    isLoadingIdentity.value = true;
    try {
      final result = await _repo.getDocumentTypeList();
      identityList.value = result?.output ?? [];
    } finally {
      isLoadingIdentity.value = false;
    }
  }

  void onIdentitySelected(String id, String name) {
    selectedIdentityId.value = id;
    selectedIdentityName.value = name;
    // Clear the number field whenever the identity type changes
    tecAadhaarNo.clear();
  }

  /// True when no identity card selected OR the selected card is Aadhaar.
  bool get isAadhaarMode =>
      selectedIdentityId.value == '0' ||
      selectedIdentityName.value.toLowerCase().contains('aadh');

  /// Max character length for the identity number field (matches native).
  ///   Aadhaar / default : 12
  ///   PAN Card          : 10
  ///   Driving Licence   : 16
  ///   Passport          : 12
  int get identityMaxLength {
    final name = selectedIdentityName.value.toLowerCase();
    if (name.contains('pan')) return 10;
    if (name.contains('driving') ||
        name.contains('licence') ||
        name.contains('license'))
      return 16;
    return 12; // Aadhaar, Passport, and default
  }

  Future<void> fetchRegDistrictList() async {
    if (regDistrictList.isNotEmpty) return; // cached
    final result = await _repo.getDistrictListForReg();
    regDistrictList.value = result?.output ?? [];
  }

  Future<void> fetchRegTalukaList() async {
    regTalukaList.clear();
    final code = _regDistLgdCode.isNotEmpty ? _regDistLgdCode : '0';
    final result = await _repo.getTalukaListForReg(distLgdCode: code);
    regTalukaList.value = result?.output ?? [];
  }

  void selectRegDistrict(DistrictOutput district) {
    tecDistrict.text = district.distName ?? '';
    _regDistLgdCode = district.distLgdCode ?? '';
    // Clear taluka when district changes
    tecTaluka.clear();
    isTalukaLocked.value = false;
    regTalukaList.clear();
  }

  void selectRegTaluka(UserMappedTalukaOutput taluka) {
    tecTaluka.text = taluka.tALNAME ?? '';
    talLgd = taluka.tALLGDCODE?.toString() ?? '0';
  }

  Future<void> fetchRelationList(
    String maritalStatusId,
    String genderId,
  ) async {
    // Normalize to "Male"/"Female" — the API expects these exact strings (matches native)
    String g;
    final gl = genderId.toLowerCase();
    if (gl.startsWith('f')) {
      g = 'Female';
    } else if (gl.startsWith('m')) {
      g = 'Male';
    } else {
      g = '0';
    }
    final result = await _repo.getRelationList(
      maritalStatusId: maritalStatusId,
      genderId: g,
    );
    relationList.value = result?.output ?? [];
  }

  /// Called when user picks a relation from the bottom sheet.
  /// Mirrors native D2DPatientRegistration_Activity openNationalityListDialog switch block:
  ///   Male  relations (1,5,7,9,17,22)  → force gender M, lock female option
  ///   Female relations (2,6,8,10,18,21) → force gender F, lock male option
  ///   Default                           → clear gender lock, both options free
  /// In all cases: clear DOB + Age (native clears edt_dob / edt_age).
  /// For parent IDs (1,2,21,22): also clear middle name (native clears txt_beneficiary_Mname).
  void onRelationSelected(RelationOutput relation) {
    selectedRelation.value = relation;

    const _maleRelIds = {1, 5, 7, 9, 17, 22};
    const _femaleRelIds = {2, 6, 8, 10, 18, 21};
    const _parentRelIds = {1, 2, 21, 22};

    final id = relation.relId ?? 0;

    if (_maleRelIds.contains(id)) {
      selectedGender.value = 'M';
      isGenderLockedByRelation.value = true;
    } else if (_femaleRelIds.contains(id)) {
      selectedGender.value = 'F';
      isGenderLockedByRelation.value = true;
    } else {
      selectedGender.value = '';
      isGenderLockedByRelation.value = false;
    }

    // Clear DOB + Age on every relation change (matches native)
    tecDob.clear();
    tecAge.clear();

    // Clear middle name for parent / parent-in-law relations (matches native)
    if (_parentRelIds.contains(id)) {
      tecMiddleName.clear();
      onNamePartsChanged();
    }
  }

  Future<void> sendMobileOtp() async {
    final mob = tecMobileNo.text.trim();
    if (mob.length != 10) {
      // ToastManager.toast('Enter valid mobile number');
      ToastManager.showAlertDialog(
        Get.context!,
        'Enter valid mobile number',
            () {
          Get.back();
        },
      );
      return;
    }
    _generatedOtp = FormatterManager.generateRandomDigits(5);
    final error = await _repo.sendOtp(
      mobileNo: mob,
      otp: _generatedOtp,
      regdId: '0',
      createdBy: empCode.toString(),
      subOrgId: subOrgId.toString(),
    );
    if (error == null) {
      mobileOtpSent.value = true;
      ToastManager.toast('OTP sent successfully');
    } else {
      // ToastManager.toast(error);
      ToastManager.showAlertDialog(Get.context!, error, () {
        Get.back();
      });
    }
  }

  Future<void> verifyMobileOtp(String otp) async {
    if (otp.isEmpty) return;
    final success = await _repo.verifyOtp(
      mobileNo: tecMobileNo.text.trim(),
      otp: otp,
    );
    if (success) {
      mobileOtpVerified.value = true;
      ToastManager.toast('Mobile number verified');
    } else {
      ToastManager.toast('Invalid OTP');
    }
  }

  Future<void> sendAbhaOtp() async {
    final abhaNum = tecAbhaNumber.text.trim();
    final abhaAddr = tecAbhaAddress.text.trim();
    final mobile = tecAbhaLinkedMobile.text.trim();
    final aadhaar = tecAbhaAadhaar.text.trim();

    // Verify mode: need ABHA number or address
    if (abhaSearchMode.value == 'verify') {
      if (abhaNum.isEmpty && abhaAddr.isEmpty) {
        ToastManager.toast('Enter ABHA number or address');
        return;
      }
    }
    // Find mode: need mobile or aadhaar
    if (abhaValidateMode.value == 'mobile' && mobile.isEmpty) {
      ToastManager.toast('Enter ABHA linked mobile number');
      return;
    }
    if (abhaValidateMode.value == 'aadhaar' && aadhaar.isEmpty) {
      ToastManager.toast('Enter Aadhaar number');
      return;
    }
    if (abhaResendCount >= 3) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Max resends reached',
        () {
          Get.back();
        },
      );
      return;
    }
    _generatedAbhaOtp = FormatterManager.generateRandomDigits(6);
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer();
    ToastManager.toast('OTP sent');
  }

  Future<void> verifyAbhaOtp() async {
    abhaOtpAttempts.value++;
    if (tecAbhaOtp.text.trim() == _generatedAbhaOtp) {
      abhaVerified.value = true;
      _abhaNameAtVerify = tecFullName.text.trim();
      _abhaGenderAtVerify = selectedGender.value;
      ToastManager.toast('ABHA verified');
    } else {
      ToastManager.toast('Invalid OTP');
    }
  }

  void _startAbhaOtpTimer() {
    _abhaTimer?.cancel();
    abhaOtpTimer.value = 120;
    _abhaTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (abhaOtpTimer.value <= 0) {
        timer.cancel();
      } else {
        abhaOtpTimer.value = abhaOtpTimer.value - 1;
      }
    });
  }

  void onCardExpiryChanged(String date) {
    final expiry = _tryParseDate(date);
    if (expiry == null) return;
    final today = DateTime.now();
    if (expiry.isBefore(DateTime(today.year, today.month, today.day))) {
      showRenewal.value = true;
      isHCRenewal.value = true;
    } else {
      showRenewal.value = false;
      isHCRenewal.value = false;
      tecRenewalDate.clear();
    }
  }

  Future<void> pickPatientPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) patientPhotoPath.value = picked.path;
  }

  Future<void> pickHealthCardPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) healthCardPhotoPath.value = picked.path;
  }

  Future<void> pickHivLetterPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) hivLetterPath.value = picked.path;
  }

  Future<void> pickRenewalFormPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) renewalFormPath.value = picked.path;
  }

  bool _validateForm() {
    if (tecWorkerRegNo.text.trim().length != 12) {
      // ToastManager.toast('Beneficiary Reg. No must be 12 digits');
      ToastManager.showAlertDialog(
        Get.context!,
        'Beneficiary Reg. No must be 12 digits',
            () {
          Get.back();
        },
      );
      return false;
    }
    if (isDependent.value) {
      if (tecFirstName.text.trim().isEmpty ||
          tecMiddleName.text.trim().isEmpty ||
          tecLastName.text.trim().isEmpty) {
        // ToastManager.toast('First, Middle and Last name are required');
        ToastManager.showAlertDialog(
          Get.context!,
          'First, Middle and Last name are required',
              () {
            Get.back();
          },
        );
        return false;
      }
    } else if (tecFullName.text.trim().isEmpty) {
      ToastManager.toast('English name is required');
      return false;
    }
    if (selectedGender.value.isEmpty) {
      ToastManager.toast('Please select gender');
      return false;
    }

    final age = int.tryParse(tecAge.text.trim()) ?? 0;
    if (isDependent.value) {
      if (selectedRelation.value == null) {
        ToastManager.toast('Please select relation');
        return false;
      }
      final ageMsg = _dependentAgeMessage(age);
      if (ageMsg != null) {
        ToastManager.toast(ageMsg);
        return false;
      }
    } else if (age < 18 || age > 60) {
      ToastManager.toast('Age must be between 18 and 60');
      return false;
    }

    if (tecMobileNo.text.trim().length != 10) {
      ToastManager.toast('Mobile number must be 10 digits');
      return false;
    }

    if (registrationType.value == 'without_abha' &&
        !mobileOtpVerified.value &&
        !altMobileOtpVerified.value) {
      ToastManager.toast('Please verify mobile number');
      return false;
    }

    if (registrationType.value == 'without_abha' &&
        tecAadhaarNo.text.trim().length != 12) {
      ToastManager.toast('Aadhaar number must be 12 digits');
      return false;
    }

    if (tecDob.text.trim().isEmpty) {
      ToastManager.toast('Please select DOB');
      return false;
    }

    if (tecPermAddr.text.trim().isEmpty || tecLocalAddr.text.trim().isEmpty) {
      ToastManager.toast('Permanent and Local address are required');
      return false;
    }

    if (isDependent.value) {
      if (tecCurrentAddr.text.trim().length < 10) {
        ToastManager.toast('Current address must be at least 10 chars');
        return false;
      }
      if (tecLandmark.text.trim().isEmpty ||
          tecTaluka.text.trim().isEmpty ||
          tecDistrict.text.trim().isEmpty) {
        ToastManager.toast('Landmark, Taluka, District are required');
        return false;
      }
    }

    if (tecPincode.text.trim().length != 6) {
      ToastManager.toast('Pin code must be 6 digits');
      return false;
    }

    if (registrationType.value == 'with_abha') {
      if (tecAbhaNumber.text.trim().isEmpty &&
          tecAbhaAddress.text.trim().isEmpty) {
        ToastManager.toast('Enter ABHA number or address');
        return false;
      }
      if (!abhaVerified.value) {
        ToastManager.toast('ABHA must be verified');
        return false;
      }
      final nameNow = tecFullName.text.trim();
      final genderNow = selectedGender.value;
      if (_abhaNameAtVerify.isNotEmpty &&
          (nameNow != _abhaNameAtVerify || genderNow != _abhaGenderAtVerify)) {
        abhaVerified.value = false;
        ToastManager.toast(
          'Name/Gender changed after ABHA verification. Please re-verify.',
        );
        return false;
      }
    }

    // Patient photo is required when face detection is enabled
    if (isFaceDetection.value && patientPhotoPath.value.isEmpty) {
      ToastManager.toast(
        'Please capture patient photo (Face Detection is enabled)',
      );
      return false;
    }

    final needsCardPhoto =
        !isDependent.value || registrationType.value == 'without_abha';
    if (needsCardPhoto && healthCardPhotoPath.value.isEmpty) {
      ToastManager.toast('Health/Identity card photo is required');
      return false;
    }

    return true;
  }

  void showConfirmationDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Confirm'),
          content: const Text(
            "Please confirm the beneficiary's details before submitting",
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                submitRegistration(context);
              },
              child: const Text('Confirm'),
            ),
          ],
        );
      },
    );
  }

  Future<void> submitRegistration(BuildContext context) async {
    if (!_validateForm()) return;
    isSubmitting.value = true;
    ToastManager.showLoader();
    try {
      final fields = <String, String>{
        'SiteId': navSiteId,
        'CampId': navCampId,
        'RegdNo': '${tecWorkerRegNo.text.trim()}$_beneficiaryCount',
        'Title': selectedTitle.value,
        'EnglishName': tecFullName.text.trim(),
        'MobileNo': tecMobileNo.text.trim(),
        'UID': tecAadhaarNo.text.trim(),
        'DOB': tecDob.text.trim(),
        'Age': tecAge.text.trim(),
        'Gender': selectedGender.value,
        'PermanentAddress': tecPermAddr.text.trim(),
        'LocalAddress': tecLocalAddr.text.trim(),
        'PinCode': tecPincode.text.trim(),
        'CreatedBy': empCode.toString(),
        'IsHCRenewal': isHCRenewal.value ? '1' : '0',
        'RenewalDate':
            isHCRenewal.value ? _toApiDate(tecRenewalDate.text.trim()) : '',
        'IsDependent': isDependent.value ? '1' : '0',
        'Education': tecEducation.text.trim(),
        'ReleationID': selectedRelation.value?.relId?.toString() ?? '0',
        'DependREGID': _workerRegdId,
        'IndentityId': selectedIdentityId.value,
        'CW_WorkerName':
            isDependent.value
                ? workerNameDisplay.value
                : tecFullName.text.trim(),
        'next_renewal_date': _toApiDate(tecCardExpiry.text.trim()),
        'residential_address_postOffice': tecPostOffice.text.trim(),
        'residential_address_taluka': tecTaluka.text.trim(),
        'residential_address_district': tecDistrict.text.trim(),
        'CurrentAddress': tecCurrentAddr.text.trim(),
        'LandMark': tecLandmark.text.trim(),
        'AlternateMobNo': tecAltMobileNo.text.trim(),
        'IsMobNoVerified': mobileOtpVerified.value ? '1' : '0',
        'IsSelfMobNo': isNumberNotBelongsToBeneficiary.value ? '0' : '1',
        'MobNoOf': altMobileBelongsTo.value,
        'OptionMode': isDependent.value ? '2' : '1',
        'VersionNo':
            _appVersion.isNotEmpty
                ? _appVersion
                : (isFaceDetection.value ? '14' : '15'),
        'Isrecollection': navType == '5' ? '1' : '0',
        'Rej_Regdid': '0',
        'Rej_CampID': '0',
        'MaritalStatusID': maritalStatusId,
        'IsFaceDetectionEnabled': isFaceDetection.value ? '1' : '0',
        'TALLGDCODE': talLgd,
        'DISTLGDCODE': navDistLgd,
        'IsRegdByCall': navType == '7' ? '1' : '0',
        'ABHANumber': tecAbhaNumber.text.trim(),
        'ABHAAddress': tecAbhaAddress.text.trim(),
        'IsWhatsAppNo': whatsAppMode.value,
        'WorkerGenderByPhlebo':
            isDependent.value
                ? (workerGenderByPhlebo.value.toLowerCase().startsWith('f')
                    ? 'Female'
                    : 'Male')
                : (selectedGender.value == 'F' ? 'Female' : 'Male'),
        'WorkerAgeByPhlebo':
            isDependent.value
                ? (int.tryParse(
                      workerAgeDisplay.value.split('.').first.trim(),
                    )?.toString() ??
                    '0')
                : tecAge.text.trim(),
      };
      print("===== REQUEST FIELDS =====");
      print(jsonEncode(fields));
      final result = await _repo.saveD2DRegistration(
        fields: fields,
        isFaceDetectionEnabled: isFaceDetection.value,
        patientPhoto:
            patientPhotoPath.value.isNotEmpty
                ? File(patientPhotoPath.value)
                : null,
        healthCardPhoto:
            healthCardPhotoPath.value.isNotEmpty
                ? File(healthCardPhotoPath.value)
                : null,
        renewalSlipPhoto:
            renewalFormPath.value.isNotEmpty
                ? File(renewalFormPath.value)
                : null,
        hivLetterPhoto:
            hivLetterPath.value.isNotEmpty ? File(hivLetterPath.value) : null,
      );

      if (result?.status?.toLowerCase() == 'success') {
        ToastManager.toast(result?.message ?? 'Registration successful');
        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (_) => PatientFingerAndSignatureScreen(
                  campId: navCampId,
                  siteId: navSiteId,
                  regNo: tecWorkerRegNo.text.trim(),
                  onSuccess: _clearForm,
                ),
          ),
        );
      } else {
        print(result?.message ?? 'Registration failed');
        ToastManager.showAlertDialog(
          context,
          result?.message ?? 'Registration failed',
          () {
            Get.back();
          },
        );
      }
    } finally {
      isSubmitting.value = false;
      ToastManager.hideLoader();
    }
  }

  /// Formats a date string to yyyy-MM-dd (hyphen) as expected by the API
  /// for next_renewal_date and RenewalDate fields — matches native Utilities.dfDate.
  String _toApiDate(String input) {
    if (input.isEmpty) return '';
    final dt = _tryParseDate(input);
    if (dt == null) return input;
    return FormatterManager.formatDateToStringInDash(dt);
  }

  String _normalizeDate(String input) {
    if (input.isEmpty) return '';
    final dt = _tryParseDate(input);
    if (dt == null) return input;
    return FormatterManager.formatDateToString(dt);
  }

  DateTime? _tryParseDate(String input) {
    if (input.isEmpty) return null;
    try {
      return DateTime.parse(input);
    } catch (_) {}
    final formats = ['yyyy/MM/dd', 'dd/MM/yyyy', 'yyyy-MM-dd'];
    for (final f in formats) {
      try {
        return DateFormat(f).parseStrict(input);
      } catch (_) {}
    }
    return null;
  }

  @override
  void onClose() {
    _abhaTimer?.cancel();
    tecWorkerRegNo.dispose();
    tecFullName.dispose();
    tecFirstName.dispose();
    tecMiddleName.dispose();
    tecLastName.dispose();
    tecMobileNo.dispose();
    tecAltMobileNo.dispose();
    tecAadhaarNo.dispose();
    tecDob.dispose();
    tecAge.dispose();
    tecEducation.dispose();
    tecPermAddr.dispose();
    tecLocalAddr.dispose();
    tecCurrentAddr.dispose();
    tecLandmark.dispose();
    tecTaluka.dispose();
    tecDistrict.dispose();
    tecPostOffice.dispose();
    tecPincode.dispose();
    tecRenewalDate.dispose();
    tecCardExpiry.dispose();
    tecAbhaNumber.dispose();
    tecAbhaAddress.dispose();
    tecAbhaOtp.dispose();
    tecMobileOtp.dispose();
    tecAbhaLinkedMobile.dispose();
    tecAbhaAadhaar.dispose();
    super.onClose();
  }
}
