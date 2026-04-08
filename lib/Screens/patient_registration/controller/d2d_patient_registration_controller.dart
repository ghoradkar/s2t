// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:geolocator/geolocator.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/patient_registration/model/beneficiary_details_response.dart';
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
  String maritalStatusId = '0';

  final registrationType = 'without_abha'.obs;
  final isDependent = false.obs;
  final workerMode = 'board'.obs;

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
  final isFaceDetection = true.obs;
  final selectedRelation = Rxn<RelationOutput>();
  final relationList = <RelationOutput>[].obs;

  final abhaVerified = false.obs;
  final abhaOtpSent = false.obs;
  final abhaOtpTimer = 120.obs;
  int abhaResendCount = 0;
  String _generatedAbhaOtp = '';
  String _abhaNameAtVerify = '';
  String _abhaGenderAtVerify = '';
  Timer? _abhaTimer;

  final mobileOtpSent = false.obs;
  final mobileOtpVerified = false.obs;
  String _generatedOtp = '';

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
    maritalStatusId = user?.maritialstatusId?.toString() ?? '0';
    _captureLocation();
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
      abhaOtpSent.value = false;
      _abhaNameAtVerify = '';
      _abhaGenderAtVerify = '';
    }
  }
  void onDependentToggled(bool val) {
    isDependent.value = val;
    if (val) {
      fetchRelationList(maritalStatusId, selectedGender.value);
    } else {
      selectedRelation.value = null;
      relationList.clear();
    }
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
    try {
      final bocwResult = await _repo.getBeneficiaryFromBocw(
        workerRegNo: regNo,
      );
      final internalResult = await _repo.checkInternalRegistration(
        workerRegNo: regNo,
      );

      final internalDate = internalResult?.output?.registrationDate;
      if (internalDate != null && internalDate.isNotEmpty) {
        final regDate = _tryParseDate(internalDate);
        if (regDate != null) {
          final diff = DateTime.now().difference(regDate).inDays;
          if (diff < 365) {
            ToastManager.toast('Cannot re-register within 365 days');
            tecWorkerRegNo.clear();
            return;
          }
        }
      }

      final data = internalResult?.output ?? bocwResult?.output;
      if (data == null) return;
      _applyBeneficiary(data);
    } finally {
      isLoadingBeneficiary.value = false;
    }
  }

  void _applyBeneficiary(BeneficiaryOutput data) {
    if (!isDependent.value) {
      tecFullName.text = data.name ?? '';
    }
    tecMobileNo.text = data.mobileNo ?? '';
    tecAadhaarNo.text = data.aadhaarNo ?? '';
    tecDob.text = _normalizeDate(data.dob ?? '');
    if (tecDob.text.isNotEmpty) onDobChanged(tecDob.text);
    tecAge.text = data.age ?? tecAge.text;
    tecPermAddr.text = data.address ?? '';
    tecLocalAddr.text = data.address ?? '';
    tecPincode.text = data.pinCode ?? '';

    final gender = (data.gender ?? '').toUpperCase();
    if (gender.startsWith('M')) {
      selectedGender.value = 'M';
    } else if (gender.startsWith('F')) {
      selectedGender.value = 'F';
    }
    selectedTitle.value = data.title ?? selectedTitle.value;

    if (data.expiryDate != null && data.expiryDate!.isNotEmpty) {
      tecCardExpiry.text = _normalizeDate(data.expiryDate!);
      onCardExpiryChanged(tecCardExpiry.text);
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

    final min = isDependent.value ? 10 : 18;
    final max = isDependent.value ? 99 : 60;
    if (age < min || age > max) {
      if (Get.context != null) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Age must be between $min and $max',
          () => Navigator.of(Get.context!, rootNavigator: true).pop(),
        );
      } else {
        ToastManager.toast('Age must be between $min and $max');
      }
    }
  }

  void onNamePartsChanged() {
    final parts = [
      tecFirstName.text.trim(),
      tecMiddleName.text.trim(),
      tecLastName.text.trim(),
    ].where((e) => e.isNotEmpty).toList();
    tecFullName.text = parts.join(' ');
  }

  Future<void> fetchRelationList(String maritalStatusId, String genderId) async {
    final g = genderId.isEmpty ? '0' : genderId;
    final result = await _repo.getRelationList(
      maritalStatusId: maritalStatusId,
      genderId: g,
    );
    relationList.value = result?.output ?? [];
  }

  Future<void> sendMobileOtp() async {
    final mob = tecMobileNo.text.trim();
    if (mob.length != 10) {
      ToastManager.toast('Enter valid mobile number');
      return;
    }
    _generatedOtp = FormatterManager.generateRandomDigits(5);
    final success = await _repo.sendOtp(
      mobileNo: mob,
      otp: _generatedOtp,
      regdId: '0',
      createdBy: empCode.toString(),
      subOrgId: subOrgId.toString(),
    );
    if (success) {
      mobileOtpSent.value = true;
      ToastManager.toast('OTP sent successfully');
    } else {
      ToastManager.toast('Failed to send OTP');
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
    if (tecAbhaNumber.text.trim().isEmpty &&
        tecAbhaAddress.text.trim().isEmpty) {
      ToastManager.toast('Enter ABHA number or address');
      return;
    }
    if (abhaResendCount >= 3) {
      ToastManager.toast('Max resends reached');
      return;
    }
    _generatedAbhaOtp = FormatterManager.generateRandomDigits(6);
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer();
    ToastManager.toast('ABHA OTP sent');
  }

  Future<void> verifyAbhaOtp() async {
    if (tecAbhaOtp.text.trim() == _generatedAbhaOtp) {
      abhaVerified.value = true;
      _abhaNameAtVerify = tecFullName.text.trim();
      _abhaGenderAtVerify = selectedGender.value;
      ToastManager.toast('ABHA verified');
    } else {
      ToastManager.toast('Invalid ABHA OTP');
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
      ToastManager.toast('Beneficiary Reg. No must be 12 digits');
      return false;
    }
    if (isDependent.value) {
      if (tecFirstName.text.trim().isEmpty || tecLastName.text.trim().isEmpty) {
        ToastManager.toast('First and Last name are required');
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
      final relId = selectedRelation.value?.relId ?? 0;
      final isChild = relId == 7 || relId == 8;
      final minAge = isChild ? 10 : 18;
      final maxAge = isChild ? 18 : 99;
      if (age < minAge || age > maxAge) {
        ToastManager.toast('Age must be between $minAge and $maxAge');
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
        !mobileOtpVerified.value) {
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

    final needsCardPhoto =
        !isDependent.value || registrationType.value == 'without_abha';
    if (needsCardPhoto && healthCardPhotoPath.value.isEmpty) {
      ToastManager.toast('Health/Identity card photo is required');
      return false;
    }

    if (isFaceDetection.value && patientPhotoPath.value.isEmpty) {
      ToastManager.toast('Patient photo is required');
      return false;
    }

    if (showRenewal.value && renewalFormPath.value.isEmpty) {
      ToastManager.toast('Renewal slip photo is required');
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
        'RegdNo': tecWorkerRegNo.text.trim(),
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
        'RenewalDate': tecRenewalDate.text.trim(),
        'IsDependent': isDependent.value ? '1' : '0',
        'Education': tecEducation.text.trim(),
        'ReleationID': selectedRelation.value?.relId?.toString() ?? '0',
        'DependREGID': '0',
        'IndentityId': '0',
        'CW_WorkerName': tecFullName.text.trim(),
        'next_renewal_date': tecRenewalDate.text.trim(),
        'residential_address_postOffice': tecPostOffice.text.trim(),
        'residential_address_taluka': tecTaluka.text.trim(),
        'residential_address_district': tecDistrict.text.trim(),
        'CurrentAddress': tecCurrentAddr.text.trim(),
        'LandMark': tecLandmark.text.trim(),
        'AlternateMobNo': tecAltMobileNo.text.trim(),
        'IsMobNoVerified': mobileOtpVerified.value ? '1' : '0',
        'IsSelfMobNo': selfMobNoMode.value,
        'MobNoOf': selfMobNoMode.value,
        'OptionMode': registrationType.value == 'with_abha' ? '2' : '1',
        'VersionNo': isFaceDetection.value ? '14' : '15',
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
        'WorkerGenderByPhlebo': selectedGender.value,
        'WorkerAgeByPhlebo': tecAge.text.trim(),
      };

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
                ),
          ),
        );
      } else {
        ToastManager.toast(result?.message ?? 'Registration failed');
      }
    } finally {
      isSubmitting.value = false;
      ToastManager.hideLoader();
    }
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
    super.onClose();
  }
}











