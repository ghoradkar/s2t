// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/patient_registration/model/beneficiary_details_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/regular_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/patient_finger_signature_screen.dart';

class RegularPatientRegistrationController extends GetxController {
  final _repo = RegularPatientRegistrationRepository();
  final _picker = ImagePicker();

  String navCampId = '';
  String navCampName = '';
  String navSiteId = '';
  String navDistrict = '';

  int empCode = 0;
  int subOrgId = 0;

  final tecWorkerRegNo = TextEditingController();
  final tecFullName = TextEditingController();
  final tecMobileNo = TextEditingController();
  final tecAadhaarNo = TextEditingController();
  final tecDob = TextEditingController();
  final tecAge = TextEditingController();
  final tecLocalAddr = TextEditingController();
  final tecPermAddr = TextEditingController();
  final tecPincode = TextEditingController();
  final tecRenewalDate = TextEditingController();
  final tecCardRegDate = TextEditingController();
  final tecCardExpiry = TextEditingController();

  final selectedTitle = ''.obs;
  final selectedGender = ''.obs;
  final isHCRenewal = false.obs;
  final showRenewal = false.obs;

  final patientPhotoPath = ''.obs;
  final healthCardPhotoPath = ''.obs;
  final renewalFormPath = ''.obs;

  final patientPhotoUrl = ''.obs;
  final healthCardPhotoUrl = ''.obs;
  final renewalPhotoUrl = ''.obs;

  bool isReregistration = false;
  String previousRegDate = '';

  final isLoadingBeneficiary = false.obs;
  final isSubmitting = false.obs;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    subOrgId = user?.subOrgId ?? 0;
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

      // 365-day check
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
        isReregistration = true;
        previousRegDate = internalDate;
      }

      final data = internalResult?.output ?? bocwResult?.output;
      if (data == null) return;

      _applyBeneficiary(data);
    } finally {
      isLoadingBeneficiary.value = false;
    }
  }

  void _applyBeneficiary(BeneficiaryOutput data) {
    tecFullName.text = data.name ?? '';
    tecMobileNo.text = data.mobileNo ?? '';
    tecAadhaarNo.text = data.aadhaarNo ?? '';
    tecDob.text = _normalizeDate(data.dob ?? '');
    if (tecDob.text.isNotEmpty) onDobChanged(tecDob.text);
    tecAge.text = data.age ?? tecAge.text;

    final gender = (data.gender ?? '').toUpperCase();
    if (gender.startsWith('M')) {
      selectedGender.value = 'M';
    } else if (gender.startsWith('F')) {
      selectedGender.value = 'F';
    }
    selectedTitle.value = data.title ?? selectedTitle.value;

    tecPermAddr.text = data.address ?? '';
    tecLocalAddr.text = data.address ?? '';
    tecPincode.text = data.pinCode ?? '';

    if (data.expiryDate != null && data.expiryDate!.isNotEmpty) {
      tecCardExpiry.text = _normalizeDate(data.expiryDate!);
      onCardExpiryChanged(tecCardExpiry.text);
    }

    if (isReregistration) {
      patientPhotoUrl.value = data.patientPhotoUrl ?? '';
      healthCardPhotoUrl.value = data.healthCardPhotoUrl ?? '';
      renewalPhotoUrl.value = data.renewalPhotoUrl ?? '';
    }
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

  void onCardRegDateChanged(String date) {
    // Reset expiry/renewal when registration date changes
    tecCardRegDate.text = date;
    tecCardExpiry.clear();
    tecRenewalDate.clear();
    showRenewal.value = false;
    isHCRenewal.value = false;
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

    if (age < 18 || age > 60) {
      if (Get.context != null) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Age must be between 18 and 60',
          () => Navigator.of(Get.context!, rootNavigator: true).pop(),
        );
      } else {
        ToastManager.toast('Age must be between 18 and 60');
      }
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

  Future<void> pickRenewalFormPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) renewalFormPath.value = picked.path;
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

  bool _validateForm() {
    final regNo = tecWorkerRegNo.text.trim();
    if (regNo.length != 12) {
      ToastManager.toast('Beneficiary Reg. No must be 12 digits');
      return false;
    }
    final name = tecFullName.text.trim();
    if (name.isEmpty || !RegExp(r'^[A-Za-z ]+$').hasMatch(name)) {
      ToastManager.toast('Please enter a valid English name');
      return false;
    }
    if (selectedTitle.value.isEmpty) {
      ToastManager.toast('Please select title');
      return false;
    }
    if (selectedGender.value.isEmpty) {
      ToastManager.toast('Please select gender');
      return false;
    }
    if (tecMobileNo.text.trim().length != 10) {
      ToastManager.toast('Mobile number must be 10 digits');
      return false;
    }
    if (tecAadhaarNo.text.trim().length != 12) {
      ToastManager.toast('Aadhaar number must be 12 digits');
      return false;
    }
    if (tecDob.text.trim().isEmpty) {
      ToastManager.toast('Please select DOB');
      return false;
    }
    final age = int.tryParse(tecAge.text.trim()) ?? 0;
    if (age < 18 || age > 60) {
      ToastManager.toast('Age must be between 18 and 60');
      return false;
    }
    if (tecLocalAddr.text.trim().isEmpty) {
      ToastManager.toast('Local address is required');
      return false;
    }
    if (tecPincode.text.trim().length != 6) {
      ToastManager.toast('Pin code must be 6 digits');
      return false;
    }
    if (tecCardRegDate.text.trim().isEmpty || tecCardExpiry.text.trim().isEmpty) {
      ToastManager.toast('Card registration and expiry dates are required');
      return false;
    }
    if (patientPhotoPath.value.isEmpty && patientPhotoUrl.value.isEmpty) {
      ToastManager.toast('Patient photo is required');
      return false;
    }
    if (healthCardPhotoPath.value.isEmpty && healthCardPhotoUrl.value.isEmpty) {
      ToastManager.toast('Health card photo is required');
      return false;
    }
    if (showRenewal.value &&
        renewalFormPath.value.isEmpty &&
        renewalPhotoUrl.value.isEmpty) {
      ToastManager.toast('Renewal slip photo is required');
      return false;
    }
    return true;
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
        'RegistrationDate': tecCardRegDate.text.trim(),
        'ExpirtyDate': tecCardExpiry.text.trim(),
      };

      final result = await _repo.saveRegistration(
        fields: fields,
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
      );

      if (result?.status?.toLowerCase() == 'success') {
        _syncBocw();
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

  Future<void> _syncBocw() async {
    final dob = _tryParseDate(tecDob.text.trim());
    final payload = {
      'RegdNo': tecWorkerRegNo.text.trim(),
      'RegistrationDate': _formatDateDash(tecCardRegDate.text.trim()),
      'EnglishName': tecFullName.text.trim(),
      'MobileNo': tecMobileNo.text.trim(),
      'Aadhar': tecAadhaarNo.text.trim(),
      'Dob': dob != null ? DateFormat('yyyy-MM-dd').format(dob) : '',
      'Age': tecAge.text.trim(),
      'Gender': selectedGender.value == 'M' ? 'Male' : 'Female',
      'DueRenewalDate': _formatDateDash(tecCardExpiry.text.trim()),
      'DISTNAME': navCampName,
      'Taluka': '',
      'ResidentialAddress': tecLocalAddr.text.trim(),
      'PermanentAddress': tecPermAddr.text.trim(),
    };
    await _repo.syncToBocw(payload: payload);
  }

  String _normalizeDate(String input) {
    if (input.isEmpty) return '';
    final dt = _tryParseDate(input);
    if (dt == null) return input;
    return FormatterManager.formatDateToString(dt);
  }

  String _formatDateDash(String input) {
    final dt = _tryParseDate(input);
    if (dt == null) return '';
    return DateFormat('yyyy-MM-dd').format(dt);
  }

  DateTime? _tryParseDate(String input) {
    if (input.isEmpty) return null;
    try {
      return DateTime.parse(input);
    } catch (_) {
      // Try common formats
    }
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
    tecWorkerRegNo.dispose();
    tecFullName.dispose();
    tecMobileNo.dispose();
    tecAadhaarNo.dispose();
    tecDob.dispose();
    tecAge.dispose();
    tecLocalAddr.dispose();
    tecPermAddr.dispose();
    tecPincode.dispose();
    tecRenewalDate.dispose();
    tecCardRegDate.dispose();
    tecCardExpiry.dispose();
    super.onClose();
  }
}


