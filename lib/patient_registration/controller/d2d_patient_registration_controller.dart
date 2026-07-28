// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:geolocator/geolocator.dart';
import 'package:http/http.dart' as http;
import 'package:s2toperational/constants/api_constants.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/medicine_delivery_menu/model/user_mapped_taluka_response.dart';
import 'package:s2toperational/common_widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/calling_modules/models/relation_model.dart';
import 'package:s2toperational/patient_registration/model/dependent_list_response.dart';
import 'package:s2toperational/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/patient_registration/model/document_type_response.dart';
import 'package:s2toperational/patient_registration/model/worker_info_response.dart';
import 'package:s2toperational/patient_registration/model/gp_item.dart';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/patient_registration/screen/abha_success_screen.dart';
import 'package:s2toperational/patient_registration/screen/patient_finger_signature_screen.dart';

class D2DPatientRegistrationController extends GetxController {
  final _repo = D2DPatientRegistrationRepository();
  final _picker = ImagePicker();
  final _api = APIManager();

  String navCampId = '';
  String navCampLocation = '';
  String navSiteId = '';
  String navDistLgd = '';
  String navType = '6';
  String navCampType = '3'; // '1' = Regular Camp, '3' = D2D Camp
  String navBeneficiaryNo = '';
  String navRelation = '';
  String navRegId = '0';
  String navRejCampId = '0';
  String navBeneficiaryName = '';

  final reRegistrationLocked = false.obs;

  int empCode = 0;
  int subOrgId = 0;
  String talLgd = '0';
  String maritalStatusId = '1';
  String _workerRegdId = '0';
  String _beneficiaryCount =
      '0'; // Count from GetBenificiaryRegisterOrNot — appended to RegdNo
  final registrationType = 'without_abha'.obs;
  final isDependent = false.obs;
  final workerMode = 'board'.obs;

  /// Board/beneficiary name and gender stored when worker-info API data loads.
  /// Used to detect mismatch when ABHA data is filled back.
  String benefBoardName = '';
  String benefBoardGender = '';

  // Hardcoded marital status list — IDs match the GetMaritalMaster API response
  // (native fetches dynamically; Flutter hardcodes these verified values)
  // API returns: 1=Married, 2=Unmarried, 3=Divorced, 4=Widowed
  var kMaritalStatus = [
    ('1', 'Married'),
    ('2', 'Unmarried'),
    ('3', 'Divorced'),
    ('4', 'Widowed'),
  ];

  final tecWorkerRegNo = TextEditingController();
  final tecFullName = TextEditingController();
  final tecFirstName = TextEditingController();
  final tecMiddleName = TextEditingController();
  final tecLastName = TextEditingController();
  final tecMobileNo = TextEditingController();
  // final tecMobileNo = TextEditingController(text: '9673974373');
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

  /// true = skip face detection (same as native switch ON); false = face detection required (default)
  final skipFaceDetection = false.obs;

  /// true = show "Skip Face Detection" toggle (server returns IsFaceDetetctionEnabled == "0")
  /// false = toggle hidden — face detection is mandatory, cannot skip
  final showFaceDetectionToggle = false.obs;

  /// Mirrors native isBoardDataCompalsory — set from ActiveRegFlag in GetFaceDetectionFlag.
  /// true = must validate worker active status via MAHABOCW API on reg-no entry.
  bool _isBoardDataCompulsory = false;
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

  // ── Find ABHA Using Mobile — real ABDM API state ──────────────────────────
  String _findAbhaTxnId = '';
  String _findAbhaSelectedIndex = '';
  String _findAbhaAccessToken = '';
  String _findAbhaPublicKey = '';
  String _findAbhaAuthToken = '';
  Map<String, dynamic> _findAbhaHealthCard = {};
  String _findAbhaAddress = '';
  final abhaCardAvailable = false.obs;

  // ── ABHA create / search sub-mode ────────────────────────────────────────
  /// 'demographic' or 'aadhaar_otp'
  final abhaCreateMode = 'aadhaar_otp'.obs;

  /// True once the user taps either create-mode radio (equivalent of native
  /// clearABHA() → enableABHAFormAfterFill()). Unlocks the full ABHA section
  /// even before the beneficiary reg no is entered.
  final abhaFormEnabled = false.obs;

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
  bool isAlternateMessageShown = false;

  /// Alternate mobile belongs to (1=Self, 2=Spouse, 3=Child)
  final altMobileBelongsTo = '1'.obs;

  /// Worker's gender selected by phlebo (for isDependent=Yes)
  final workerGenderByPhlebo = ''.obs;

  /// Inline validation error for Beneficiary Reg. No field (shown while typing)
  final workerRegNoError = ''.obs;

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

  /// true when worker-info API loaded for a dependent → identity locked to Aadhaar
  final isIdentityLockedByData = false.obs;

  /// Dependent list — fetched on-demand when "Select Dependent" is tapped
  final dependentList = <DependentOutput>[].obs;
  final selectedDependent = Rxn<DependentOutput>();
  final isLoadingDependents = false.obs;

  /// API error message from the last fetchDependentList call (e.g. screening-pending alert)
  String dependentListErrorMessage = '';

  /// Dependent's BOCW ID set when user selects from the list
  String bocwIdDepend = '';

  /// District / Taluka dropdowns (isDependent=No + hasApiData=true)
  final regDistrictList = <DistrictOutput>[].obs;
  final regTalukaList = <UserMappedTalukaOutput>[].obs;

  /// true when API returned a non-empty value → field stays readonly
  final isDistrictLocked = false.obs;
  final isTalukaLocked = false.obs;
  final isPincodeLocked = false.obs;

  /// District LGD code used when fetching talukas
  String _regDistLgdCode = '';

  // New text controllers
  final tecAltMobileOtp = TextEditingController();
  final tecRationCardNo = TextEditingController();

  final patientPhotoPath = ''.obs;
  final healthCardPhotoPath = ''.obs;
  final isCellularPhone = false.obs;
  final consentPhotoPath = ''.obs;
  final hivLetterPath = ''.obs;
  final renewalFormPath = ''.obs;

  final currentLat = '0.0'.obs;
  final currentLong = '0.0'.obs;
  final currentAddress = ''.obs;
  final isCapturingLocation = false.obs;
  Timer? _locationTimer;

  // ── Aadhaar masking ───────────────────────────────────────────────────────
  String originalAadhaar = '';
  bool _isAadhaarUpdating = false;
  final isAadhaarVisible = false.obs;

  // true after ABHA verification when Aadhaar was provided — reveals masked field
  final aadhaarSetForAbha = false.obs;

  // Inline validation error shown below the Aadhaar field while typing
  final aadhaarError = ''.obs;

  // Inline validation error for the ABHA-flow Aadhaar input (tecAbhaAadhaar)
  final abhaAadhaarError = ''.obs;

  static final _aadhaarRegex = RegExp(r'^[2-9][0-9]{11}$');

  /// Full Aadhaar validation — mirrors native Utilities.isaadharNumberValidate:
  ///   1. Pattern  ^[2-9][0-9]{11}$  (12 digits, first digit 2–9)
  ///   2. Verhoeff checksum (same tables as native VerhoeffAlgorithm class)
  static bool _isValidAadhaar(String aadhar) {
    if (!_aadhaarRegex.hasMatch(aadhar)) return false;
    return _verhoeffValidate(aadhar);
  }

  static bool _verhoeffValidate(String num) {
    const d = [
      [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
      [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
      [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
      [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
      [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
      [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
      [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
      [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
      [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
      [9, 8, 7, 6, 5, 4, 3, 2, 1, 0],
    ];
    const p = [
      [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
      [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
      [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
      [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
      [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
      [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
      [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
      [7, 0, 4, 6, 9, 1, 3, 2, 5, 8],
    ];
    final digits = num.split('').reversed.map((c) => int.parse(c)).toList();
    int c = 0;
    for (int i = 0; i < digits.length; i++) {
      c = d[c][p[i % 8][digits[i]]];
    }
    return c == 0;
  }

  // ── Gram Panchayat ────────────────────────────────────────────────────────
  final isRural = true.obs;
  final gpList = <GpItem>[].obs;
  final selectedGpName = ''.obs;
  final selectedGpCode = ''.obs;
  final isLoadingGp = false.obs;

  final isLoadingBeneficiary = false.obs;
  final isSubmitting = false.obs;

  @override
  String _teamId = '0';

  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    talLgd = user?.tALLGDCODE?.toString() ?? '0';
    final rawMsId = user?.maritialstatusId?.toString() ?? '';
    maritalStatusId = (int.tryParse(rawMsId) != null) ? rawMsId : '1';
    // tecMobileNo.text = '9673974373';
    _startAutoLocationUpdates();
    _fetchFaceDetectionFlag();
  }

  @override
  void onReady() {
    super.onReady();
    // Only D2D camps need the screening-completion gate (mirrors native isHllUser check)
    if (navCampType == '3') {
      _getTeamIdForValidation();
    }
  }

  void _getTeamIdForValidation() {
    _api.getTeamNumberByCampIdAndUSerIdAPI(
      {'campid': navCampId, 'UserID': empCode.toString()},
      (response, error, success) {
        if (success && response != null) {
          _teamId = response.output?.first.teamNumber ?? '0';
        }
        _checkPatientRegistrationAllowed();
      },
    );
  }

  Future<void> _checkPatientRegistrationAllowed() async {
    await _api.getPatientAndTestValidationCountAPI(
      {'CAMPID': navCampId, 'Teamid': _teamId, 'Userid': empCode.toString()},
      (allTestDone, error, success) {
        if (success && allTestDone == '0') {
          _showScreeningIncompleteAlert();
        }
      },
    );
  }

  void _showScreeningIncompleteAlert() {
    final context = Get.context;
    if (context == null) return;
    ToastManager.showAlertDialog(
      context,
      "You cannot register new patients until registered beneficiaries screening is completed",
      () {
        Get.back();
        Get.back();
      },
    );
    // showDialog(
    //   context: context,
    //   barrierDismissible: false,
    //   builder:
    //       (_) => AlertDialog(
    //         title: const Text('Alert'),
    //         content: const Text(
    //           'You cannot register new patients until registered beneficiaries screening is completed',
    //         ),
    //         actions: [
    //           TextButton(
    //             onPressed: () {
    //               Navigator.of(context).pop(); // dismiss dialog
    //               Navigator.of(context).pop(); // go back to Select Camp
    //             },
    //             child: const Text('OK'),
    //           ),
    //         ],
    //       ),
    // );
  }

  /// Mirrors native getFaceDetectionFlag() — calls GetFaceDetectionFlag API.
  /// If IsFaceDetetctionEnabled == "0"  → show skip toggle (optional).
  /// If IsFaceDetetctionEnabled == "1"  → hide skip toggle (mandatory, cannot skip).
  void _fetchFaceDetectionFlag() {
    _api.getFaceDetectionFlagAPI(empCode.toString(), (
      response,
      error,
      success,
    ) {
      if (success && response != null) {
        final output = response['output'] as List? ?? [];
        if (output.isNotEmpty) {
          final compulsory =
              output.first['IsFaceDetetctionEnabled']?.toString() ?? '1';
          // Show toggle only when server says it is NOT compulsory ("0")
          showFaceDetectionToggle.value = compulsory == '0';

          // Mirrors native isBoardDataCompalsory = o.getActiveRegFlag()
          final activeRegFlag =
              output.first['ActiveRegFlag']?.toString() ?? '0';
          _isBoardDataCompulsory = activeRegFlag == '1';
        }
      }
      // On failure: keep defaults — face detection required, board check off.
      // Matches native behaviour on API failure.
    });
  }

  /// Mirrors native getWorkerInfoForFlag() — called after reg-no is entered and
  /// after a dependent is selected, when ActiveRegFlag == 1.
  /// If worker is inactive (empty array from MAHABOCW), clears the form and
  /// shows the same Marathi alert as native.
  Future<void> _checkWorkerActiveStatus() async {
    if (!_isBoardDataCompulsory) return;
    final regNo = tecWorkerRegNo.text.trim();
    if (regNo.isEmpty) return;

    final isActive = await _repo.checkWorkerActiveStatus(regNo);
    if (!isActive) {
      _clearForm();
      final ctx = Get.context;
      if (ctx != null) {
        ToastManager.showAlertDialog(
          ctx,
          'लाभार्थी सध्या निष्क्रिय आहे किंवा उपलब्ध नाही. कृपया नंतर पुन्हा प्रयत्न करा.',
          () => Get.back(),
        );
      }
    }
  }

  /// Starts on init and repeats every 5 s — mirrors native locationRequest interval.
  void _startAutoLocationUpdates() {
    _captureLocation();
    _locationTimer?.cancel();
    _locationTimer = Timer.periodic(const Duration(seconds: 5), (_) {
      if (!isCapturingLocation.value) _captureLocation();
    });
  }

  Future<void> _captureLocation() async {
    isCapturingLocation.value = true;
    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        currentLat.value = '0.0';
        currentLong.value = '0.0';
        return;
      }

      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          currentLat.value = '0.0';
          currentLong.value = '0.0';
          return;
        }
      }
      if (permission == LocationPermission.deniedForever) {
        currentLat.value = '0.0';
        currentLong.value = '0.0';
        return;
      }

      // ── Step 1: last known location (instant, mirrors native getLastKnownLocation) ──
      // Show cached coordinates immediately so the field is never empty on open.
      Position? position;
      final lastKnown = await Geolocator.getLastKnownPosition();
      if (lastKnown != null) {
        position = lastKnown;
        currentLat.value = position.latitude.toStringAsFixed(6);
        currentLong.value = position.longitude.toStringAsFixed(6);
        // Show coords as address placeholder while reverse-geocode runs
        if (currentAddress.value.isEmpty) {
          currentAddress.value =
              '${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)}';
        }
      }

      // ── Step 2: fresh high-accuracy fix (background update) ─────────────────
      try {
        final fresh = await Geolocator.getCurrentPosition(
          locationSettings: const LocationSettings(
            accuracy: LocationAccuracy.high,
          ),
        ).timeout(const Duration(seconds: 15));
        position = fresh;
        currentLat.value = position.latitude.toStringAsFixed(6);
        currentLong.value = position.longitude.toStringAsFixed(6);
      } catch (_) {
        // Fresh fix timed out or failed — keep lastKnown values already set
      }

      if (position == null) {
        currentLat.value = '0.0';
        currentLong.value = '0.0';
        return;
      }

      // Reverse-geocode via Google Maps API — same provider as native Geocoder
      try {
        const apiKey = 'AIzaSyDbtPLpwrcS571PfdJw9ednQAemxBiNhUA';
        final url = Uri.parse(
          'https://maps.googleapis.com/maps/api/geocode/json'
          '?latlng=${position.latitude},${position.longitude}&key=$apiKey',
        );
        final response = await http
            .get(url)
            .timeout(const Duration(seconds: 8));
        if (response.statusCode == 200) {
          final json = jsonDecode(response.body) as Map<String, dynamic>;
          final results = json['results'] as List?;
          if (results != null && results.isNotEmpty) {
            currentAddress.value =
                (results.first as Map<String, dynamic>)['formatted_address']
                    as String? ??
                '${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)}';
          } else {
            currentAddress.value =
                '${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)}';
          }
        } else {
          currentAddress.value =
              '${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)}';
        }
      } catch (_) {
        currentAddress.value =
            '${position.latitude.toStringAsFixed(4)}, ${position.longitude.toStringAsFixed(4)}';
      }
    } catch (_) {
      currentLat.value = '0.0';
      currentLong.value = '0.0';
    } finally {
      isCapturingLocation.value = false;
    }
  }

  Future<void> refreshLocation() => _captureLocation();

  // ── Gram Panchayat helpers ────────────────────────────────────────────────

  void setRural(bool rural) {
    isRural.value = rural;
    if (!rural) {
      selectedGpName.value = '';
      selectedGpCode.value = '0';
    } else {
      // mirrors native radioRural click: edtGp.setText("") + gpCode = ""
      selectedGpName.value = '';
      selectedGpCode.value = '';
    }
  }

  /// Fetches GP list then invokes [onSuccess] with the loaded items so the
  /// screen can show the picker dialog. Mirrors native getGramPanchayat().
  Future<void> fetchAndShowGpPicker({
    required void Function(List<GpItem>) onSuccess,
  }) async {
    if (tecWorkerRegNo.text.trim().isEmpty) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Please search worker number first',
        () => Get.back(),
      );
      return;
    }
    if (talLgd == '0' || talLgd.isEmpty) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Please select taluka',
        () => Get.back(),
      );
      return;
    }
    isLoadingGp.value = true;
    final list = await _repo.getGramPanchayatList(talLgd: talLgd);
    isLoadingGp.value = false;
    if (list.isEmpty) {
      ToastManager.toast('No GP list found for this taluka');
      return;
    }
    gpList.assignAll(list);
    onSuccess(list);
  }

  void selectGp(GpItem item) {
    selectedGpName.value = item.gpName;
    selectedGpCode.value = item.gpLgdCode;
  }

  // ── Aadhaar masking helpers ───────────────────────────────────────────────

  String _maskAadhaar(String aadhaar) {
    if (aadhaar.isEmpty) return '';
    final sb = StringBuffer();
    for (int i = 0; i < aadhaar.length; i++) {
      sb.write(i < 8 ? '•' : aadhaar[i]);
    }
    return sb.toString();
  }

  void onAadhaarChanged(String value) {
    if (_isAadhaarUpdating) return;
    _isAadhaarUpdating = true;

    final prevLength = originalAadhaar.length;
    final newLength = value.length;

    if (newLength > prevLength) {
      // User typed a character — grab the last char of the displayed value
      final newChar = value[value.length - 1];
      if (RegExp(r'\d').hasMatch(newChar) && originalAadhaar.length < 12) {
        originalAadhaar += newChar;
      }
    } else if (newLength < prevLength) {
      final removed = prevLength - newLength;
      originalAadhaar =
          originalAadhaar.length >= removed
              ? originalAadhaar.substring(0, originalAadhaar.length - removed)
              : '';
    }

    // Inline validation: show error only once all 12 digits are entered.
    // Uses full Verhoeff check (mirrors native isaadharNumberValidate).
    if (originalAadhaar.length == 12) {
      aadhaarError.value =
          _isValidAadhaar(originalAadhaar)
              ? ''
              : 'Please enter valid Aadhar Card No.';
    } else {
      aadhaarError.value = '';
    }

    final display =
        isAadhaarVisible.value
            ? originalAadhaar
            : _maskAadhaar(originalAadhaar);
    tecAadhaarNo.value = TextEditingValue(
      text: display,
      selection: TextSelection.collapsed(offset: display.length),
    );

    _isAadhaarUpdating = false;
  }

  void toggleAadhaarVisibility() {
    isAadhaarVisible.value = !isAadhaarVisible.value;
    _isAadhaarUpdating = true;
    final text =
        isAadhaarVisible.value
            ? originalAadhaar
            : _maskAadhaar(originalAadhaar);
    tecAadhaarNo.value = TextEditingValue(
      text: text,
      selection: TextSelection.collapsed(offset: text.length),
    );
    _isAadhaarUpdating = false;
  }

  /// Inline validation for the ABHA-flow Aadhaar input field.
  void onAbhaAadhaarChanged(String val) {
    if (val.length == 12) {
      abhaAadhaarError.value =
          _isValidAadhaar(val) ? '' : 'Please enter valid Aadhar Card No.';
    } else {
      abhaAadhaarError.value = '';
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
      abhaAadhaarError.value = '';
      abhaCardAvailable.value = false;
      abhaFormEnabled.value = false;
      _findAbhaTxnId = '';
      _findAbhaSelectedIndex = '';
      _findAbhaAccessToken = '';
      _findAbhaPublicKey = '';
      _findAbhaAuthToken = '';
      _findAbhaHealthCard = {};
      _findAbhaAddress = '';
    }
  }

  /// Mirrors native rgCreateABHAOption.onCheckedChanged → clearABHA() →
  /// enableABHAFormAfterFill(): selecting a create mode unlocks the full ABHA
  /// section even before the beneficiary reg no is entered.
  void onAbhaCreateModeSelected(String mode) {
    abhaCreateMode.value = mode;
    abhaFormEnabled.value = true;
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
    final abhaFullName =
        fullFromApi.isNotEmpty
            ? fullFromApi
            : [
              firstName,
              middleName,
              lastName,
            ].where((s) => s.isNotEmpty).join(' ');

    final genderRaw =
        ((profile['gender'] as String?) ?? '').trim().toUpperCase();
    final abhaGender =
        (genderRaw == 'M' || genderRaw == 'MALE')
            ? 'Male'
            : (genderRaw == 'F' || genderRaw == 'FEMALE')
            ? 'Female'
            : genderRaw.isNotEmpty
            ? 'Other'
            : '';

    // ── Board vs ABHA mismatch check + name fill ─────────────────────
    if (isDependent.value) {
      // ── Dependent: last-name-only check (mirrors native line 9773) ──
      // Native takes the last word of the ABHA name and the last word of
      // txt_beneficiary_name (worker display name) and compares them.
      final abhaWords = abhaFullName.trim().split(RegExp(r'\s+'));
      final abhaLastWord = abhaWords.isNotEmpty ? abhaWords.last : '';

      // Use worker display name first; fall back to pre-filled last-name field.
      final workerDisplay = workerNameDisplay.value.trim();
      final boardWords =
          workerDisplay.isNotEmpty
              ? workerDisplay.split(RegExp(r'\s+'))
              : tecLastName.text.trim().split(RegExp(r'\s+'));
      final boardLastWord = boardWords.isNotEmpty ? boardWords.last : '';

      if (boardLastWord.isNotEmpty &&
          abhaLastWord.toLowerCase() != boardLastWord.toLowerCase()) {
        return 'ABHA and Board details does not match\n\n'
            'Details:\n'
            'Worker Board Last Name: $boardLastWord\n'
            'ABHA Last Name: $abhaLastWord';
      }

      // ── Match: fill dependent name from ABHA parts ───────────────
      // Native (lines 9792–9814): splits ABHA full name into
      // first / middle / last and sets txt_beneficiary_Fname/Mname/Lname.
      final nameParts = abhaFullName.trim().split(RegExp(r'\s+'));
      final depFirst = nameParts.isNotEmpty ? nameParts[0] : firstName;
      final depMiddle = nameParts.length > 2 ? nameParts[1] : '';
      final depLast =
          nameParts.length > 1 ? nameParts[nameParts.length - 1] : lastName;

      tecFirstName.text = depFirst;
      tecMiddleName.text = depMiddle;
      tecLastName.text = depLast;
      tecFullName.text = abhaFullName.isNotEmpty ? abhaFullName : depFirst;
    } else {
      // ── Non-dependent: full-name check ───────────────────────────
      // Use benefBoardName if set; fall back to the full-name field value.
      final boardName =
          benefBoardName.trim().isNotEmpty
              ? benefBoardName.trim()
              : tecFullName.text.trim();
      // Skip check if we have no board name to compare against.
      if (boardName.isNotEmpty &&
          !abhaFullName.toLowerCase().contains(boardName.toLowerCase())) {
        final boardGender =
            benefBoardGender.isNotEmpty ? benefBoardGender : '—';
        final abhaGenderDisplay = abhaGender.isNotEmpty ? abhaGender : '—';
        return 'ABHA and Board details does not match\n\n'
            'Details:\n'
            'Board Name: $boardName\n'
            'ABHA Name: $abhaFullName\n'
            'Board Gender: $boardGender\n'
            'ABHA Gender: $abhaGenderDisplay';
      }

      // ── Match: fill name fields ───────────────────────────────────
      tecFirstName.text = firstName;
      tecMiddleName.text = middleName;
      tecLastName.text = lastName;
      if (fullFromApi.isNotEmpty) {
        tecFullName.text = fullFromApi;
      } else {
        onNamePartsChanged();
      }
    }

    // ── Switch to "With ABHA" and mark as verified ──────────────────
    registrationType.value = 'with_abha';
    abhaVerified.value = true;

    // ── Mobile ───────────────────────────────────────────────────────
    final mobile = ((profile['mobile'] as String?) ?? '').trim();
    if (mobile.isNotEmpty) tecMobileNo.text = mobile;

    // ── DOB & Age ────────────────────────────────────────────────────
    final dobRaw =
        ((profile['dob'] as String?) ??
                (profile['dateOfBirth'] as String?) ??
                '')
            .trim();
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
    final abhaNumber =
        ((profile['ABHANumber'] as String?) ??
                (profile['healthId'] as String?) ??
                '')
            .trim();
    if (abhaNumber.isNotEmpty) tecAbhaNumber.text = abhaNumber;

    // ── ABHA Address ──────────────────────────────────────────────────
    tecAbhaAddress.text = abhaAddress;

    // ── Permanent Address (native: address + "," + pincode) ───────────
    final addr = ((profile['address'] as String?) ?? '').trim();
    final pin = ((profile['pincode'] as String?) ?? '').trim();
    if (addr.isNotEmpty) {
      tecPermAddr.text = pin.isNotEmpty ? '$addr,$pin' : addr;
    }

    // ── Pincode ───────────────────────────────────────────────────────
    if (pin.isNotEmpty) {
      tecPincode.text = pin;
      isPincodeLocked.value = true;
    }

    // ── District (native: set from districtName, then disabled) ───────
    final district = ((profile['districtName'] as String?) ?? '').trim();
    if (district.isNotEmpty) tecDistrict.text = district;

    // ── Current Address (native: explicitly cleared) ──────────────────
    tecCurrentAddr.clear();
    // Local Address — native does NOT set it from ABHA (left for user entry)

    // ── Aadhaar from ABHA verification (mirrors native lines 11357–11363) ──
    // After ABHA OTP verification, native copies edtABHAAadhaar into
    // originalAadhaar and shows edt_aadhaarno with the masked value.
    final abhaAadhaarRaw = tecAbhaAadhaar.text.trim().replaceAll('-', '');
    if (abhaAadhaarRaw.isNotEmpty) {
      originalAadhaar = abhaAadhaarRaw;
      tecAadhaarNo.text = _maskAadhaar(originalAadhaar);
      aadhaarSetForAbha.value = true;
    }

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
    // Reset Find ABHA state
    _findAbhaTxnId = '';
    _findAbhaSelectedIndex = '';
    _findAbhaAccessToken = '';
    _findAbhaPublicKey = '';
    _findAbhaAuthToken = '';
    _findAbhaHealthCard = {};
    _findAbhaAddress = '';
    abhaCardAvailable.value = false;
    aadhaarSetForAbha.value = false;
  }

  /// Mirrors native abhaTokenReceiver: pre-fills the D2D registration form from
  /// a patient queue selection. Called when "Go To Registration" is tapped in
  /// ViewQueuePatientScreen after selecting a patient from the queue.
  ///
  /// [responseJson] — the full response string from QueueOutput.response.
  /// [authToken]    — the authtoken from the selected queue item.
  void fillFromQueueSelection(String responseJson, String authToken) {
    try {
      final responseObj = jsonDecode(responseJson) as Map<String, dynamic>;
      final profileObj = responseObj['profile'] as Map<String, dynamic>;
      final patientObj = profileObj['patient'] as Map<String, dynamic>;

      // ABHA number + address (mirrors edtABHANumber1 / edtABHAAddress1)
      final abhaNum = patientObj['abhaNumber']?.toString() ?? '';
      final abhaAddr = patientObj['abhaAddress']?.toString() ?? '';
      tecAbhaNumber.text = abhaNum;
      tecAbhaAddress.text = abhaAddr;

      // Full name (mirrors edtFname)
      final name = patientObj['name']?.toString() ?? '';
      if (name.isNotEmpty) tecFullName.text = name;

      // DOB + Age
      final year = patientObj['yearOfBirth']?.toString() ?? '';
      final month = patientObj['monthOfBirth']?.toString() ?? '';
      final day = patientObj['dayOfBirth']?.toString() ?? '';
      if (year.isNotEmpty && month.isNotEmpty && day.isNotEmpty) {
        tecDob.text = _normalizeDate('$year/$month/$day');
        try {
          final yInt = int.parse(year);
          final mInt = int.parse(month);
          final dInt = int.parse(day);
          final now = DateTime.now();
          int age = now.year - yInt;
          if (now.month < mInt || (now.month == mInt && now.day < dInt)) age--;
          tecAge.text = age.toString();
        } catch (_) {}
      }

      // Gender — lock after setting (mirrors rgGender.setEnabled(false))
      final gender = (patientObj['gender']?.toString() ?? '').toUpperCase();
      if (gender == 'M') {
        selectedGender.value = 'M';
        isGenderLockedByRelation.value = true;
      } else if (gender == 'F') {
        selectedGender.value = 'F';
        isGenderLockedByRelation.value = true;
      }

      // Mobile
      final phone = patientObj['phoneNumber']?.toString() ?? '';
      if (phone.isNotEmpty) tecMobileNo.text = phone;

      // Address + pincode
      final addressObj = (patientObj['address'] as Map<String, dynamic>?) ?? {};
      final line = addressObj['line']?.toString() ?? '';
      if (line.isNotEmpty) tecPermAddr.text = line;
      final pincode = addressObj['pincode']?.toString() ?? '';
      if (pincode.isNotEmpty && pincode != 'null') {
        tecPincode.text = pincode;
        isPincodeLocked.value = true;
      }

      // Mark ABHA verified → locks ABHA number/address fields + shows banner
      abhaVerified.value = true;
      abhaFormEnabled.value = true;
    } catch (e) {
      // ignore: avoid_print
      print('[fillFromQueueSelection] error=$e');
    }
  }

  /// Called by the Clear button shown in the verified banner after ABHA-creation
  /// fill. Mirrors native: clearABHA() + clearPatientDetails(3) + enableABHAForm.
  ///
  /// clearFlag=3 in native means: clear everything EXCEPT the name fields.
  void clearAfterAbhaFill() {
    // ── Reset ABHA section (mirrors clearABHA + enableABHAFormAfterFill) ──
    clearAbhaSearch(); // resets abhaFormLocked, abhaVerified, ABHA fields
    abhaCreateMode.value = 'aadhaar_otp';
    abhaFormEnabled.value = false;

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

  /// Called by D2DSelectCampController after setting all navType="5" params.
  void initForReRegistration() {
    reRegistrationLocked.value = true;
    registrationType.value = 'without_abha';
    if (navRelation == 'Self') {
      isDependent.value = false;
      tecWorkerRegNo.text = navBeneficiaryNo;
      onWorkerRegNoChanged(navBeneficiaryNo);
    } else {
      isDependent.value = true;
      tecWorkerRegNo.text = navBeneficiaryNo;
      onWorkerRegNoChanged(navBeneficiaryNo);
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
    if (val.isEmpty) {
      workerRegNoError.value = '';
    } else if (val.length < 12) {
      workerRegNoError.value =
          'Enter valid 12 digit worker registration number';
    } else {
      workerRegNoError.value = '';
      if (!isLoadingBeneficiary.value) _fetchBeneficiary(val);
    }
  }

  Future<void> _fetchBeneficiary(String regNo) async {
    if (isLoadingBeneficiary.value) return;
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
        tecWorkerRegNo.clear();
        _clearForm();
        ToastManager.showAlertDialog(
          Get.context!,
          'बांधकाम कामगार मंडळाकडून लाभार्थ्याची अद्ययावत माहिती प्राप्त झालेली नाही. \nत्यामुळे सध्या या लाभार्थ्याची नोंदणी करता येणार नाही याची नोंद घ्यावी',
          () {
            Get.back();
          },
        );
        return;
      }

      // ── 365-day re-registration check (mirrors native GetWorkerInfroRe_Registration)
      // Only applies when registering the worker themselves (not a dependent)
      if (!isDependent.value) {
        final regDate = await _repo.getReRegistrationDate(workerRegNo: regNo);
        if (regDate != null && regDate.isNotEmpty) {
          final parsed = _tryParseDate(regDate);
          if (parsed != null &&
              DateTime.now().difference(parsed).inDays < 365) {
            tecWorkerRegNo.clear();
            _clearForm();
            final ctx = Get.context;
            if (ctx != null) {
              ToastManager.showAlertDialog(
                ctx,
                'You have done registration on $regDate',
                () => Get.back(),
                title: 'Cannot re-register the Labour',
              );
            }
            return;
          }
        }
      }

      _applyWorkerInfo(data);
      if (reRegistrationLocked.value && isDependent.value) {
        await fetchRelationList(
          selectedWorkerMaritalStatusId.value,
          workerGenderByPhlebo.value,
        );
        await _fetchDependentRescreeningData();
      }
      await _checkWorkerActiveStatus();
    } finally {
      isLoadingBeneficiary.value = false;
    }
  }

  /// Called when user taps "Select Dependent" and guards have passed.
  /// Fetches list from GetDependentDetailsFromBoardData with "MH" + regNo.
  /// On API failure stores the error message; caller checks [dependentListErrorMessage].
  Future<void> fetchDependentList() async {
    isLoadingDependents.value = true;
    dependentList.clear();
    selectedDependent.value = null;
    dependentListErrorMessage = '';
    try {
      final regNo = tecWorkerRegNo.text.trim();
      final result = await _repo.getDependentList(
        regdNo: 'MH$regNo',
        workerAge: workerAgeDisplay.value,
        workerGender: workerGenderByPhlebo.value,
        workerMaritalStatus: maritalStatusId,
      );
      if (result == null) {
        dependentListErrorMessage = 'Server not responding';
        return;
      }
      if (result.status?.toLowerCase() == 'success') {
        dependentList.value = result.output ?? [];
        // ignore: avoid_print
        print('[fetchDependentList] count=${dependentList.length}');
      } else {
        dependentListErrorMessage =
            result.message?.isNotEmpty == true
                ? result.message!
                : 'Failed to load dependent list';
      }
    } finally {
      isLoadingDependents.value = false;
    }
  }

  /// Mirrors native getDependentRescreeningData() — called for navType="5" dependent
  /// re-registration after worker info loads. Populates name, relation, gender, DOB.
  Future<void> _fetchDependentRescreeningData() async {
    final result = await _repo.getDependentRescreeningData(regdId: navRegId);
    final data =
        result?.output?.isNotEmpty == true ? result!.output!.first : null;
    if (data == null) {
      // ignore: avoid_print
      print('[_fetchDependentRescreeningData] no data returned');
      return;
    }

    // Name: split EnglishName into first / middle / last (mirrors native)
    final englishName = (data.englishName ?? '').trim();
    final parts = englishName.split(RegExp(r'\s+'));
    tecFirstName.text = parts.isNotEmpty ? parts[0] : '';
    tecMiddleName.text = parts.length > 2 ? parts[1] : '';
    tecLastName.text =
        parts.length > 1
            ? parts.sublist(parts.length > 2 ? 2 : 1).join(' ')
            : '';
    onNamePartsChanged();

    // BOCW ID
    bocwIdDepend = data.bocwIdDepend ?? '';

    // Relation: match RelId in loaded relation list
    final relIdStr = data.relId ?? '';
    final relIdInt = int.tryParse(relIdStr);
    final matched =
        relIdInt != null
            ? relationList.firstWhereOrNull((r) => r.relId == relIdInt)
            : null;
    if (matched != null) {
      selectedRelation.value = matched;
    } else if (relIdInt != null) {
      selectedRelation.value = RelationOutput(
        relId: relIdInt,
        relName:
            data.relName?.isNotEmpty == true ? data.relName : data.relation,
      );
    }

    // Gender: derived from RelId (mirrors native switch, DOB NOT cleared here)
    const _maleRelIds = {'1', '5', '7', '9', '17', '22'};
    const _femaleRelIds = {'2', '6', '8', '10', '18', '21'};
    if (_maleRelIds.contains(relIdStr)) {
      selectedGender.value = 'M';
      isGenderLockedByRelation.value = true;
    } else if (_femaleRelIds.contains(relIdStr)) {
      selectedGender.value = 'F';
      isGenderLockedByRelation.value = true;
    }

    // DOB: "dd/MM/yyyy" → "yyyy/MM/dd" then calculate age
    final rawDob = (data.dob ?? '').trim();
    if (rawDob.isNotEmpty) {
      try {
        final dobParts = rawDob.split('/');
        if (dobParts.length == 3) {
          // dd/MM/yyyy → yyyy/MM/dd
          tecDob.text = '${dobParts[2]}/${dobParts[1]}/${dobParts[0]}';
          onDobChanged(tecDob.text);
        }
      } catch (_) {}
    }

    // ignore: avoid_print
    print(
      '[_fetchDependentRescreeningData] relId=$relIdStr name=${tecFirstName.text} ${tecMiddleName.text} ${tecLastName.text} gender=${selectedGender.value} dob=${tecDob.text}',
    );
  }

  Future<void> onDependentSelected(DependentOutput dep) async {
    selectedDependent.value = dep;
    bocwIdDepend = dep.bocwIdDepend ?? '';

    // ── Name: split full_name into first / middle / last ────────────────────
    final nameParts = dep.displayName.trim().split(RegExp(r'\s+'));
    final first = nameParts.isNotEmpty ? nameParts[0] : '';
    final middle = nameParts.length > 2 ? nameParts[1] : '';
    final last = nameParts.length > 1 ? nameParts.last : '';

    tecFirstName.text = first;
    tecMiddleName.text = middle;
    tecLastName.text = last;

    // Clear middle name for spouse / sibling relations (mirrors native switch)
    const _clearMiddleRelIds = {'1', '2', '21', '22'};
    if (_clearMiddleRelIds.contains(dep.relId)) {
      tecMiddleName.text = '';
    }

    onNamePartsChanged();

    // ── Relation: auto-fill from API field + find in relation list ───────────
    final relIdInt = int.tryParse(dep.relId ?? '');
    final matched =
        relIdInt != null
            ? relationList.firstWhereOrNull((r) => r.relId == relIdInt)
            : null;
    if (matched != null) {
      selectedRelation.value = matched;
    } else if (dep.relation?.isNotEmpty == true) {
      // Create a temporary RelationOutput when list not yet loaded
      selectedRelation.value = RelationOutput(
        relId: relIdInt,
        relName: dep.relation,
      );
    }

    // ── Gender: determined by RelId (mirrors native switch-case) ────────────
    const _maleRelIds = {'1', '5', '7', '9', '17', '22'};
    const _femaleRelIds = {'2', '6', '8', '10', '18', '21'};
    tecDob.clear();
    tecAge.clear();
    if (_maleRelIds.contains(dep.relId)) {
      selectedGender.value = 'M';
      isGenderLockedByRelation.value = true;
    } else if (_femaleRelIds.contains(dep.relId)) {
      selectedGender.value = 'F';
      isGenderLockedByRelation.value = true;
    } else {
      selectedGender.value = '';
      isGenderLockedByRelation.value = false;
    }

    // ── DOB: convert "dd-MM-yyyy" → "yyyy/MM/dd" ────────────────────────────
    final rawDob = dep.dob ?? '';
    if (rawDob.isNotEmpty) {
      try {
        final parts = rawDob.split('-');
        if (parts.length == 3) {
          // dd-MM-yyyy → yyyy/MM/dd
          tecDob.text = '${parts[2]}/${parts[1]}/${parts[0]}';
          onDobChanged(tecDob.text);
        }
      } catch (_) {}
    }

    // Mirrors native: after dependent gender is set, validate worker active
    // status if ActiveRegFlag == 1.
    await _checkWorkerActiveStatus();
  }

  /// Calls CheckDependentRegistrationStatus. Returns null on success, error message on failure.
  Future<String?> checkDependentRegistrationStatus(DependentOutput dep) async {
    final regNo = tecWorkerRegNo.text.trim();
    return _repo.checkDependentRegistrationStatus(
      regdNo: 'MH$regNo',
      dependentName: dep.displayName,
    );
  }

  /// Calls GetRelationWiseDependantCountwithMaritalStatus.
  /// Returns true if the relation slot is available, false if blocked (Column1 == 0).
  /// RegdNo is sent without MH prefix; Gender is the worker's gender.
  Future<bool> checkRelationWiseCount(DependentOutput dep) {
    return _repo.checkRelationWiseCount(
      regdNo: tecWorkerRegNo.text.trim(),
      relId: dep.relId ?? '',
      gender: workerGenderByPhlebo.value,
      maritalStatusId: maritalStatusId,
    );
  }

  /// Mirrors native clearDependentData() + clearPatientDetails() called on registration-status failure.
  void clearDependentSelection() {
    selectedDependent.value = null;
    bocwIdDepend = '';
    tecFirstName.clear();
    tecMiddleName.clear();
    tecLastName.clear();
    tecDob.clear();
    tecAge.clear();
    selectedGender.value = '';
    isGenderLockedByRelation.value = false;
    selectedRelation.value = null;
    originalAadhaar = '';
    isAadhaarVisible.value = false;
    aadhaarSetForAbha.value = false;
    tecAadhaarNo.clear();
    onNamePartsChanged();
    selectedGpName.value = '';
    selectedGpCode.value = '';
  }

  // ── Alternate mobile OTP ─────────────────────────────────────────────────

  void onAltMobileChanged(String value) {
    if (!isAlternateMessageShown && value.length == 1) {
      isAlternateMessageShown = true;
      if (Get.context != null) {
        ToastManager.showAlertDialog(
          Get.context!,
          'नोंदणीकृत मोबाईल क्रमांक कार्यरत नसल्यास किंवा त्यावर OTP प्राप्त होत नसल्यास पर्यायी क्रमांक शेअर करावा. मात्र, स्क्रीनिंग प्रक्रियेसाठी पर्यायी मोबाईल क्रमांकाचा वापर केला जाणार नाही, याची नोंद घ्यावी.',
          () => Navigator.of(Get.context!, rootNavigator: true).pop(),
          title: 'सूचना',
        );
      }
    }
  }

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
      bocwRegNo: 'MH${tecWorkerRegNo.text.trim()}',
      beneficiaryName: tecFullName.text.trim(),
      relationId: selectedRelation.value?.relId?.toString() ?? '20',
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
      ToastManager.showAlertDialog(Get.context!, 'Invalid OTP', () {
        Get.back();
      });
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
    // tecMobileNo.text = '9673974373'; // TEST OVERRIDE
    tecAltMobileNo.clear();
    tecAltMobileOtp.clear();
    originalAadhaar = '';
    isAadhaarVisible.value = false;
    aadhaarError.value = '';
    aadhaarSetForAbha.value = false;
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
    skipFaceDetection.value =
        false; // reset to face detection ON (matches native isfaceDetection = "1")

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
    abhaCardAvailable.value = false;
    abhaFormEnabled.value = false;
    _findAbhaTxnId = '';
    _findAbhaSelectedIndex = '';
    _findAbhaAccessToken = '';
    _findAbhaPublicKey = '';
    _findAbhaAuthToken = '';
    _findAbhaHealthCard = {};
    _findAbhaAddress = '';

    // Worker-info display (isDependent = Yes)
    workerNameDisplay.value = '';
    workerAgeDisplay.value = '';
    workerGenderDisplay.value = '';
    workerGenderByPhlebo.value = '';
    selectedWorkerMaritalStatusId.value = '0';

    // Identity card
    selectedIdentityId.value = '0';
    selectedIdentityName.value = '';
    isIdentityLockedByData.value = false;
    selectedWorkerMaritalStatusName.value = '';
    selectedRelation.value = null;
    relationList.clear();
    dependentList.clear();
    selectedDependent.value = null;
    dependentListErrorMessage = '';
    bocwIdDepend = '';

    // District / Taluka / Pincode state
    isDistrictLocked.value = false;
    isTalukaLocked.value = false;
    isPincodeLocked.value = false;
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
    isCellularPhone.value = false;
    consentPhotoPath.value = '';

    // Ration card
    tecRationCardNo.clear();

    // GP
    isRural.value = true;
    selectedGpName.value = '';
    selectedGpCode.value = '';
    gpList.clear();
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
      // Pre-fill worker gender correction dropdown (req 7) — normalize to
      // 'Male'/'Female' so dropdown items match and API receives correct value.
      final wGStr = (data.gender ?? '').toLowerCase();
      workerGenderByPhlebo.value =
          wGStr.startsWith('f')
              ? 'Female'
              : wGStr.startsWith('m')
              ? 'Male'
              : '';
    } else {
      // Scenario 4 (Yes + Data):
      // Worker display cards at top show full name, age, gender
      workerNameDisplay.value = fullName;
      // Auto-select Aadhaar Card and lock identity field (mirrors native identityId="1")
      selectedIdentityId.value = '1';
      selectedIdentityName.value = 'Adhar Card';
      isIdentityLockedByData.value = true;
      workerAgeDisplay.value =
          (int.tryParse(
            (data.age ?? '').split('.').first.trim(),
          )?.toString()) ??
          '';
      workerGenderDisplay.value = data.gender ?? '';
      // Pre-select worker gender for API (normalize to 'Male'/'Female')
      final dGStr = (data.gender ?? '').toLowerCase();
      workerGenderByPhlebo.value =
          dGStr.startsWith('f')
              ? 'Female'
              : dGStr.startsWith('m')
              ? 'Male'
              : '';
      // Skip name fields in re-registration mode — rescreening API will populate them
      if (!reRegistrationLocked.value) {
        tecLastName.text = lastName;
        final genderLower = (data.gender ?? '').toLowerCase();
        tecMiddleName.text = genderLower.startsWith('f') ? '' : firstName;
        tecFirstName.clear();
        onNamePartsChanged();
      }
    }

    // Mobile
    final apiMobile = (data.mobile ?? '').trim();
    if (apiMobile.isNotEmpty) tecMobileNo.text = apiMobile;
    // tecMobileNo.text = '9673974373'; // TEST OVERRIDE

    // Aadhaar, DOB, Gender — only pre-fill for the beneficiary themselves
    // (isDependent=No). When registering a dependent the phlebo enters these
    // fields manually for the dependent person.
    if (!isDependent.value) {
      // Aadhaar — store real value, display masked (mirrors native originalAadhaar pattern)
      originalAadhaar = data.aadhaar ?? '';
      isAadhaarVisible.value = false;
      tecAadhaarNo.text = _maskAadhaar(originalAadhaar);

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
    // Only pre-select when API returns a real numeric ID (matches native behavior:
    // native sets empty string when API returns "null", leaving field unselected).
    final rawMsId = data.maritalStatusID ?? '';
    final rawMsName = data.maritalStatus ?? '';
    if (int.tryParse(rawMsId) != null) {
      maritalStatusId = rawMsId;
      selectedWorkerMaritalStatusId.value = rawMsId;
      selectedWorkerMaritalStatusName.value =
          rawMsName.isNotEmpty ? rawMsName : 'Married';
    } else {
      // API returned null/empty → leave field unselected
      selectedWorkerMaritalStatusId.value = '0';
      selectedWorkerMaritalStatusName.value = '';
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
    // Lock when API returned a real value — mirrors native setEnabled(false)
    isPincodeLocked.value = pin.isNotEmpty && pin != '0';

    // Renewal date / card expiry — do NOT auto-trigger showRenewal here;
    // renewal section is user-controlled via the switch in the form.
    final renewal = (data.nextRenewalDate ?? '').trim();
    if (renewal.isNotEmpty) {
      tecRenewalDate.text = _normalizeDate(renewal);
      tecCardExpiry.text = _normalizeDate(renewal);
    }

    // GP auto-populate — mirrors native IsUrban/GPName/GPLGDCODE handling
    final isUrbanVal = data.isUrban ?? '';
    if (isUrbanVal == '0') {
      isRural.value = true;
      selectedGpName.value = data.gpName ?? '';
      selectedGpCode.value = data.gpLgdCode ?? '';
    } else if (isUrbanVal.isNotEmpty) {
      isRural.value = false;
      selectedGpName.value = '';
      selectedGpCode.value = '0';
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
        clearDependentSelection();
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
      selectedIdentityId.value == '1' || // ID 1 = Adhar Card
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
      bocwRegNo: 'MH${tecWorkerRegNo.text.trim()}',
      beneficiaryName: tecFullName.text.trim(),
      relationId: selectedRelation.value?.relId?.toString() ?? '20',
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

    // ── Verify mode (ABHA number/address): real ABDM API ─────────────────────
    if (abhaSearchMode.value == 'verify') {
      if (abhaNum.isEmpty && abhaAddr.isEmpty) {
        ToastManager.toast('Enter ABHA number or address');
        return;
      }
      if (abhaResendCount >= 3) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Max resends reached',
          () => Get.back(),
        );
        return;
      }
      if (abhaValidateMode.value == 'mobile') {
        await _sendAbhaVerifyMobileOtp(abhaNum, abhaAddr);
      } else {
        await _sendAbhaVerifyAadhaarOtp(abhaNum, abhaAddr);
      }
      return;
    }

    // ── Find mode, Using Aadhaar: real ABDM API ──────────────────────────────
    if (abhaValidateMode.value == 'aadhaar') {
      if (!_isValidAadhaar(aadhaar)) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Please enter valid Aadhar Card No.',
          () => Get.back(),
        );
        return;
      }
      if (abhaResendCount >= 3) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Max resends reached',
          () => Get.back(),
        );
        return;
      }
      await _sendAbhaAadhaarOtp(aadhaar);
      return;
    }

    // ── Find mode, Using Mobile: real ABDM API ───────────────────────────────
    if (mobile.length != 10) {
      ToastManager.toast('Enter valid 10-digit mobile number');
      return;
    }
    if (abhaResendCount >= 3) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Max resends reached',
        () => Get.back(),
      );
      return;
    }

    // Resend: skip search, re-send OTP to same selected index
    if (abhaOtpSent.value &&
        _findAbhaTxnId.isNotEmpty &&
        _findAbhaSelectedIndex.isNotEmpty) {
      await _sendAbhaMobileOtpToSelectedIndex();
      return;
    }

    // First time: search ABHA accounts linked to this mobile
    await _searchAbhaByMobile(mobile);
  }

  /// Returns true if the ABDM API result indicates an expired/invalid token
  /// (HTTP 401 / code 900901). Used to trigger a one-shot token refresh + retry.
  bool _isAbdmAuthError(Map<String, dynamic>? result) {
    if (result == null) return false;
    final err = result['error']?.toString() ?? '';
    return err.contains('900901') || err.contains('Invalid Credentials');
  }

  /// Forces a fresh ABDM session token + public certificate.
  /// Returns true on success, false if either step fails.
  Future<bool> _refreshAbhaSession() async {
    _findAbhaAccessToken = await _repo.createAbhaSession() ?? '';
    if (_findAbhaAccessToken.isEmpty) return false;
    _findAbhaPublicKey =
        await _repo.getAbhaPublicCertificate(_findAbhaAccessToken) ?? '';
    return _findAbhaPublicKey.isNotEmpty;
  }

  /// Parses an ABDM error body (which may be a JSON string like
  /// `{"message":"..."}` or a plain string) into a human-readable message.
  String _extractAbdmErrorMessage(String? raw) {
    if (raw == null || raw.isEmpty)
      return 'Failed to send OTP. Please try again.';
    try {
      final decoded = jsonDecode(raw) as Map<String, dynamic>;
      // ABDM wraps errors as {"error":{"message":"..."}} or {"message":"..."}
      final inner = decoded['error'];
      if (inner is Map) {
        return inner['message']?.toString() ?? raw;
      }
      return decoded['message']?.toString() ?? raw;
    } catch (_) {
      return raw;
    }
  }

  Future<void> _searchAbhaByMobile(String mobile) async {
    ToastManager.showLoader();
    if (_findAbhaAccessToken.isEmpty || _findAbhaPublicKey.isEmpty) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
    }

    var result = await _repo.findAbhaByMobile(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      mobile: mobile,
    );

    if (_isAbdmAuthError(result)) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
      result = await _repo.findAbhaByMobile(
        accessToken: _findAbhaAccessToken,
        publicKey: _findAbhaPublicKey,
        mobile: mobile,
      );
    }

    ToastManager.hideLoader();
    if (isClosed) return;

    if (result == null || result['error'] != null) {
      final raw = result?['error']?.toString() ?? '';
      // Native (line 12386): ABDM-1114 "User not found." → show custom message
      final errMsg =
          raw.contains('User not found')
              ? 'The mobile number you have entered does not match with any of the records.Please enter a different number'
              : _extractAbdmErrorMessage(raw.isNotEmpty ? raw : null);
      ToastManager.showAlertDialog(Get.context!, errMsg, () => Get.back());
      return;
    }

    _findAbhaTxnId = result['txnId'] as String? ?? '';
    // Response field is 'ABHA' (native uses same key)
    final rawAddresses = result['ABHA'] ?? result['ABHAAddresses'];
    final addresses =
        (rawAddresses is List)
            ? rawAddresses.whereType<Map<String, dynamic>>().toList()
            : <Map<String, dynamic>>[];

    if (addresses.isEmpty) {
      ToastManager.toast('No ABHA accounts found for this mobile number.');
      return;
    }

    final ctx = Get.context;
    if (ctx == null) return;
    _showAbhaSelectionDialog(ctx, addresses);
  }

  void _showAbhaSelectionDialog(
    BuildContext context,
    List<Map<String, dynamic>> results,
  ) {
    showDialog(
      context: context,
      barrierDismissible: true,
      builder:
          (_) => AlertDialog(
            title: CommonText(
              text: "Select ABHA Account",
              fontSize: 18.sp,
              fontWeight: FontWeight.w700,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),

            // const Text(
            //   'Select ABHA Account',
            //   style: TextStyle(fontWeight: FontWeight.w700, fontSize: 16),
            // ),
            contentPadding: const EdgeInsets.symmetric(vertical: 8),
            content: SizedBox(
              width: double.maxFinite,
              child: ListView.separated(
                shrinkWrap: true,
                itemCount: results.length,
                separatorBuilder: (_, __) => const Divider(height: 1),
                itemBuilder: (ctx, i) {
                  final item = results[i];
                  final name =
                      (item['name'] as String?) ??
                      (item['fullName'] as String?) ??
                      '—';
                  final abhaNum =
                      (item['ABHANumber'] as String?) ??
                      (item['abhaNumber'] as String?) ??
                      '—';
                  final gender = (item['gender'] as String?) ?? '—';
                  return ListTile(
                    title: CommonText(
                      text: name,
                      fontSize: 16.sp,
                      fontWeight: FontWeight.w600,
                      textColor: kBlackColor,
                      textAlign: TextAlign.start,
                    ),

                    // Text(
                    //   name,
                    //   style: const TextStyle(
                    //     fontWeight: FontWeight.w600,
                    //     fontSize: 14,
                    //   ),
                    // ),
                    subtitle: CommonText(
                      text: 'ABHA: $abhaNum  |  Gender: $gender',
                      fontSize: 14.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kTextColor,
                      textAlign: TextAlign.start,
                    ),

                    // Text(
                    //   'ABHA: $abhaNum  |  Gender: $gender',
                    //   style: const TextStyle(fontSize: 12),
                    // ),
                    onTap: () {
                      Navigator.of(context, rootNavigator: true).pop();
                      // Use the 'index' from the response entry (1-based, matches native)
                      _findAbhaSelectedIndex =
                          (item['index'] ?? i + 1).toString();
                      _sendAbhaMobileOtpToSelectedIndex();
                    },
                  );
                },
              ),
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Get.back();
                },
                child: CommonText(
                  text: 'Cancel',
                  fontSize: 18.sp,
                  fontWeight: FontWeight.bold,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.start,
                ),
              ),
            ],
          ),
    );
  }

  Future<void> _sendAbhaMobileOtpToSelectedIndex() async {
    if (_findAbhaTxnId.isEmpty || _findAbhaSelectedIndex.isEmpty) return;
    ToastManager.showLoader();
    final result = await _repo.sendAbhaMobileOtpByIndex(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      txnId: _findAbhaTxnId,
      index: _findAbhaSelectedIndex,
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (result == null || result['error'] != null) {
      final errMsg = result?['error']?.toString() ?? '';
      ToastManager.toast(
        errMsg.isNotEmpty ? errMsg : 'Failed to send OTP. Please try again.',
      );
      return;
    }

    // API may return an updated txnId
    if (result['txnId'] != null) _findAbhaTxnId = result['txnId'] as String;
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer(60);
    ToastManager.toast('OTP sent to registered mobile');
  }

  Future<void> _sendAbhaAadhaarOtp(String aadhaar) async {
    ToastManager.showLoader();
    if (_findAbhaAccessToken.isEmpty || _findAbhaPublicKey.isEmpty) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
    }

    var result = await _repo.sendAbhaAadhaarLoginOtp(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      aadhaarNumber: aadhaar,
      txnId: _findAbhaTxnId,
    );

    if (_isAbdmAuthError(result)) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
      result = await _repo.sendAbhaAadhaarLoginOtp(
        accessToken: _findAbhaAccessToken,
        publicKey: _findAbhaPublicKey,
        aadhaarNumber: aadhaar,
        txnId: _findAbhaTxnId,
      );
    }

    ToastManager.hideLoader();
    if (isClosed) return;

    if (result == null || result['error'] != null) {
      final errMsg = _extractAbdmErrorMessage(result?['error']?.toString());
      ToastManager.showAlertDialog(Get.context!, errMsg, () => Get.back());
      return;
    }

    if (result['txnId'] != null) _findAbhaTxnId = result['txnId'] as String;
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer(60);
    ToastManager.toast('OTP sent to Aadhaar-linked mobile');
  }

  Future<void> _verifyFindAbhaAadhaarOtp() async {
    final otpVal = tecAbhaOtp.text.trim();
    if (otpVal.length != 6) {
      ToastManager.toast('Enter valid 6-digit OTP');
      return;
    }
    if (_findAbhaTxnId.isEmpty) {
      ToastManager.toast('Session expired. Please search again.');
      clearAbhaSearch();
      return;
    }

    ToastManager.showLoader();
    final verifyResult = await _repo.verifyAbhaAadhaarLoginOtp(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      txnId: _findAbhaTxnId,
      otpValue: otpVal,
    );
    if (isClosed) {
      ToastManager.hideLoader();
      return;
    }

    if (verifyResult == null || verifyResult['error'] != null) {
      ToastManager.hideLoader();
      final errMsg = verifyResult?['error']?.toString() ?? '';
      ToastManager.toast(
        errMsg.isNotEmpty
            ? errMsg
            : 'OTP verification failed. Please try again.',
      );
      return;
    }

    // Check authResult field (native checks authResult == "success")
    final authResult = (verifyResult['authResult'] as String?) ?? '';
    if (authResult.isNotEmpty && authResult.toLowerCase() != 'success') {
      ToastManager.hideLoader();
      final msg =
          (verifyResult['message'] as String?) ?? 'OTP verification failed';
      ToastManager.toast(msg);
      return;
    }

    // Extract auth token
    String authToken = (verifyResult['token'] as String?) ?? '';
    if (authToken.isEmpty) {
      final tokens = verifyResult['tokens'] as Map<String, dynamic>?;
      authToken = (tokens?['token'] as String?) ?? '';
    }

    if (authToken.isEmpty) {
      ToastManager.hideLoader();
      ToastManager.toast('Verification failed. Please try again.');
      return;
    }
    _findAbhaAuthToken = authToken;

    // Fetch account profile
    final profile = await _repo.getAbhaAccountProfile(
      accessToken: _findAbhaAccessToken,
      authToken: _findAbhaAuthToken,
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (profile == null || profile['error'] != null) {
      ToastManager.toast('Failed to fetch ABHA profile. Please try again.');
      return;
    }

    _findAbhaHealthCard = Map<String, dynamic>.from(profile);
    // profile/account returns 'preferredAbhaAddress' as the ABHA address
    _findAbhaAddress =
        (profile['preferredAbhaAddress'] as String?) ??
        (profile['healthId'] as String?) ??
        (profile['ABHAAddress'] as String?) ??
        '';

    // profile/account splits DOB into yearOfBirth/monthOfBirth/dayOfBirth.
    // Synthesise a 'dob' key so fillFromAbhaCreation can parse it.
    if (_findAbhaHealthCard['dob'] == null) {
      final y = _findAbhaHealthCard['yearOfBirth'] as String?;
      final m = _findAbhaHealthCard['monthOfBirth'] as String?;
      final d = _findAbhaHealthCard['dayOfBirth'] as String?;
      if (y != null && m != null && d != null) {
        _findAbhaHealthCard['dob'] =
            '$y-${m.padLeft(2, '0')}-${d.padLeft(2, '0')}';
      }
    }

    // Always make card available so user can view it
    abhaCardAvailable.value = true;

    // Fill the registration form; show mismatch dialog if names don't match
    final mismatch = fillFromAbhaCreation(
      profile: profile,
      abhaAddress: _findAbhaAddress,
    );

    if (mismatch != null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        final ctx = Get.context;
        if (ctx == null) return;
        ToastManager.showAlertDialog(ctx, mismatch, () {
          Get.back();
          clearAbhaSearch();
        }, title: 'Board and ABHA Details Mismatch');
      });
    } else {
      ToastManager.toast('ABHA verified successfully');
    }
  }

  Future<void> _sendAbhaVerifyMobileOtp(
    String abhaNumber,
    String abhaAddress,
  ) async {
    ToastManager.showLoader();
    // Ensure session + cert are available
    if (_findAbhaAccessToken.isEmpty || _findAbhaPublicKey.isEmpty) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
    }

    var result = await _repo.sendAbhaVerifyMobileOtp(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      abhaAddress: abhaAddress,
      abhaNumber: abhaNumber,
      txnId: _findAbhaTxnId,
    );

    // Token may have expired — refresh once and retry
    if (_isAbdmAuthError(result)) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
      result = await _repo.sendAbhaVerifyMobileOtp(
        accessToken: _findAbhaAccessToken,
        publicKey: _findAbhaPublicKey,
        abhaAddress: abhaAddress,
        abhaNumber: abhaNumber,
        txnId: _findAbhaTxnId,
      );
    }

    ToastManager.hideLoader();
    if (isClosed) return;

    if (result == null || result['error'] != null) {
      final errMsg = _extractAbdmErrorMessage(result?['error']?.toString());
      ToastManager.showAlertDialog(Get.context!, errMsg, () => Get.back());
      return;
    }

    if (result['txnId'] != null) _findAbhaTxnId = result['txnId'] as String;
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer(60);
    ToastManager.toast('OTP sent to ABHA-linked mobile');
  }

  Future<void> _sendAbhaVerifyAadhaarOtp(
    String abhaNumber,
    String abhaAddress,
  ) async {
    ToastManager.showLoader();
    if (_findAbhaAccessToken.isEmpty || _findAbhaPublicKey.isEmpty) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
    }

    var result = await _repo.sendAbhaVerifyAadhaarOtp(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      abhaAddress: abhaAddress,
      abhaNumber: abhaNumber,
      txnId: _findAbhaTxnId,
    );

    // Token may have expired — refresh once and retry
    if (_isAbdmAuthError(result)) {
      final ok = await _refreshAbhaSession();
      if (!ok) {
        ToastManager.hideLoader();
        ToastManager.toast('Session creation failed. Please retry.');
        return;
      }
      result = await _repo.sendAbhaVerifyAadhaarOtp(
        accessToken: _findAbhaAccessToken,
        publicKey: _findAbhaPublicKey,
        abhaAddress: abhaAddress,
        abhaNumber: abhaNumber,
        txnId: _findAbhaTxnId,
      );
    }

    ToastManager.hideLoader();
    if (isClosed) return;

    if (result == null || result['error'] != null) {
      final errMsg = _extractAbdmErrorMessage(result?['error']?.toString());
      ToastManager.showAlertDialog(Get.context!, errMsg, () => Get.back());
      return;
    }

    if (result['txnId'] != null) _findAbhaTxnId = result['txnId'] as String;
    abhaOtpSent.value = true;
    abhaVerified.value = false;
    abhaResendCount++;
    _startAbhaOtpTimer(60);
    ToastManager.toast('OTP sent to ABHA-linked Aadhaar');
  }

  Future<void> verifyAbhaOtp() async {
    abhaOtpAttempts.value++;

    // ── Find mode, Using Mobile: real ABDM verify ────────────────────────────
    if (abhaSearchMode.value == 'find' && abhaValidateMode.value == 'mobile') {
      await _verifyFindAbhaMobileOtp();
      return;
    }

    // ── Find mode, Using Aadhaar: real ABDM verify ───────────────────────────
    if (abhaSearchMode.value == 'find' && abhaValidateMode.value == 'aadhaar') {
      await _verifyFindAbhaAadhaarOtp();
      return;
    }

    // ── Verify mode, Using Mobile: real ABDM verify ──────────────────────────
    if (abhaSearchMode.value == 'verify' &&
        abhaValidateMode.value == 'mobile') {
      await _verifyFindAbhaMobileOtp();
      return;
    }

    // ── Verify mode, Using Aadhaar: real ABDM verify ──────────────────────────
    if (abhaSearchMode.value == 'verify' &&
        abhaValidateMode.value == 'aadhaar') {
      await _verifyFindAbhaAadhaarOtp();
      return;
    }

    // ── Fallback: existing mock verify ───────────────────────────────────────
    if (tecAbhaOtp.text.trim() == _generatedAbhaOtp) {
      abhaVerified.value = true;
      _abhaNameAtVerify = tecFullName.text.trim();
      _abhaGenderAtVerify = selectedGender.value;
      ToastManager.toast('ABHA verified');
    } else {
      ToastManager.toast('Invalid OTP');
    }
  }

  Future<void> _verifyFindAbhaMobileOtp() async {
    final otpVal = tecAbhaOtp.text.trim();
    if (otpVal.length != 6) {
      ToastManager.toast('Enter valid 6-digit OTP');
      return;
    }
    if (_findAbhaTxnId.isEmpty) {
      ToastManager.toast('Session expired. Please search again.');
      clearAbhaSearch();
      return;
    }

    ToastManager.showLoader();
    final verifyResult = await _repo.verifyAbhaMobileLoginOtp(
      accessToken: _findAbhaAccessToken,
      publicKey: _findAbhaPublicKey,
      txnId: _findAbhaTxnId,
      otpValue: otpVal,
    );
    if (isClosed) {
      ToastManager.hideLoader();
      return;
    }

    if (verifyResult == null || verifyResult['error'] != null) {
      ToastManager.hideLoader();
      final errMsg = verifyResult?['error']?.toString() ?? '';
      ToastManager.toast(
        errMsg.isNotEmpty
            ? errMsg
            : 'OTP verification failed. Please try again.',
      );
      return;
    }

    // Check authResult field (native checks authResult == "success")
    final authResult = (verifyResult['authResult'] as String?) ?? '';
    if (authResult.isNotEmpty && authResult.toLowerCase() != 'success') {
      ToastManager.hideLoader();
      final msg =
          (verifyResult['message'] as String?) ?? 'OTP verification failed';
      ToastManager.toast(msg);
      return;
    }

    // Extract auth token
    String authToken = (verifyResult['token'] as String?) ?? '';
    if (authToken.isEmpty) {
      final tokens = verifyResult['tokens'] as Map<String, dynamic>?;
      authToken = (tokens?['token'] as String?) ?? '';
    }

    if (authToken.isEmpty) {
      ToastManager.hideLoader();
      ToastManager.toast('Verification failed. Please try again.');
      return;
    }
    _findAbhaAuthToken = authToken;

    // Fetch account profile
    final profile = await _repo.getAbhaAccountProfile(
      accessToken: _findAbhaAccessToken,
      authToken: _findAbhaAuthToken,
    );
    ToastManager.hideLoader();
    if (isClosed) return;

    if (profile == null || profile['error'] != null) {
      ToastManager.toast('Failed to fetch ABHA profile. Please try again.');
      return;
    }

    _findAbhaHealthCard = Map<String, dynamic>.from(profile);
    // profile/account returns 'preferredAbhaAddress' as the ABHA address
    _findAbhaAddress =
        (profile['preferredAbhaAddress'] as String?) ??
        (profile['healthId'] as String?) ??
        (profile['ABHAAddress'] as String?) ??
        '';

    // profile/account splits DOB into yearOfBirth/monthOfBirth/dayOfBirth.
    // Synthesise a 'dob' key so fillFromAbhaCreation can parse it.
    if (_findAbhaHealthCard['dob'] == null) {
      final y = _findAbhaHealthCard['yearOfBirth'] as String?;
      final m = _findAbhaHealthCard['monthOfBirth'] as String?;
      final d = _findAbhaHealthCard['dayOfBirth'] as String?;
      if (y != null && m != null && d != null) {
        _findAbhaHealthCard['dob'] =
            '$y-${m.padLeft(2, '0')}-${d.padLeft(2, '0')}';
      }
    }

    // Always make card available so user can view it
    abhaCardAvailable.value = true;

    // Fill the registration form; show mismatch dialog if names don't match
    final mismatch = fillFromAbhaCreation(
      profile: profile,
      abhaAddress: _findAbhaAddress,
    );

    if (mismatch != null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        final ctx = Get.context;
        if (ctx == null) return;
        ToastManager.showAlertDialog(ctx, mismatch, () {
          Get.back();
          clearAbhaSearch();
        }, title: 'Board and ABHA Details Mismatch');
      });
    } else {
      // Match: show confirmation alert (mirrors native D2DPatientRegistration_Activity.java line 10901)
      WidgetsBinding.instance.addPostFrameCallback((_) {
        final ctx = Get.context;
        if (ctx == null) return;
        ToastManager.showAlertDialog(
          ctx,
          'ABHA and Board details match. The consent link has been shared to the registered mobile number. Kindly provide your consent to proceed further.',
          () => Get.back(),
          title: 'Success',
        );
      });

      // Send DPDP consent SMS via SendRegistrationOTPWithDPDPConsent
      ToastManager.showLoader();
      final consentError = await _repo.sendOtp(
        mobileNo: tecMobileNo.text.trim(),
        otp: FormatterManager.generateRandomDigits(5),
        regdId: '0',
        createdBy: empCode.toString(),
        subOrgId: subOrgId.toString(),
        bocwRegNo: 'MH${tecWorkerRegNo.text.trim()}',
        beneficiaryName: tecFullName.text.trim(),
        relationId: selectedRelation.value?.relId?.toString() ?? '20',
      );
      ToastManager.hideLoader();
      if (consentError != null) {
        WidgetsBinding.instance.addPostFrameCallback((_) {
          final ctx = Get.context;
          if (ctx == null) return;
          ToastManager.showAlertDialog(
            ctx,
            'You Are Not Able To Use This Number Multiple Times.',
            () => Get.back(),
            title: 'Alert',
          );
        });
      }
    }
  }

  /// Navigates to AbhaSuccessScreen to show the ABHA card after Find ABHA verification.
  void onViewAbhaCard() {
    final ctx = Get.context;
    if (ctx == null) return;
    Navigator.push(
      ctx,
      MaterialPageRoute(
        builder:
            (_) => AbhaSuccessScreen(
              abhaAddress: _findAbhaAddress,
              accessToken: _findAbhaAccessToken,
              authToken: _findAbhaAuthToken,
              healthCard: _findAbhaHealthCard,
            ),
      ),
    );
  }

  void _startAbhaOtpTimer([int seconds = 120]) {
    _abhaTimer?.cancel();
    abhaOtpTimer.value = seconds;
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
    // Native uses CameraActivity for ALL cases — FaceDetectionActivity is commented out.
    // The switch only controls IsFaceDetectionEnabled sent to the server, not the capture method.
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

  Future<void> pickConsentPhoto() async {
    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) consentPhotoPath.value = picked.path;
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
    // GPS guard — matches native submitData() check at line 6882
    if (currentLat.value == '0.0' && currentLong.value == '0.0') {
      ToastManager.showAlertDialog(
        Get.context!,
        'Location not captured. Please enable GPS and tap the refresh button.',
        () => Get.back(),
      );
      return false;
    }

    // GP guard — mirrors native radioRural check before submit
    // '0' is the sentinel for "no GP selected" (same as native gpCode = "0")
    if (isRural.value &&
        (selectedGpCode.value.isEmpty || selectedGpCode.value == '0')) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Please select Gram Panchayat',
        () => Get.back(),
      );
      return false;
    }

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

    // Mirrors native submitData() lines 6890-6896: dependent list field must be
    // filled for BOTH with_abha and without_abha when isDependent==1.
    if (isDependent.value &&
        selectedDependent.value == null &&
        !reRegistrationLocked.value) {
      ToastManager.showAlertDialog(
        Get.context!,
        'Please select dependent first',
        () => Get.back(),
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
      ToastManager.toast('Full name is required');
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

    // Mirrors native submitData() lines 7097-7108: if alternate number is
    // entered it must be 10 digits AND OTP-verified (applies to both flows).
    if (tecAltMobileNo.text.trim().isNotEmpty) {
      if (tecAltMobileNo.text.trim().length != 10) {
        ToastManager.toast('Please enter valid alternate mobile number');
        return false;
      }
      if (!altMobileOtpVerified.value) {
        ToastManager.toast('Please verify alternate number');
        return false;
      }
    }

    // Aadhaar validation — covers both flows:
    // without_abha: required when isDependent=Yes or identity=Aadhaar
    // with_abha + worker: required when Aadhaar was provided during ABHA flow
    // with_abha + dependent: always required (dependent enters Aadhaar manually)
    // Full validation: pattern ^[2-9][0-9]{11}$ + Verhoeff checksum,
    // mirrors native isaadharNumberValidate (Utilities.java).
    final needsAadhaarCheck =
        registrationType.value == 'without_abha'
            ? (isDependent.value || isAadhaarMode)
            : (isDependent.value || aadhaarSetForAbha.value);
    if (needsAadhaarCheck && !_isValidAadhaar(originalAadhaar)) {
      // Set inline field error (mirrors native edt_aadhaarno.setError())
      aadhaarError.value = 'Please enter valid Aadhar Card No.';
      ToastManager.showAlertDialog(
        Get.context!,
        'Please enter valid Aadhar Card No.',
        () => Get.back(),
      );
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

    // Ration card — only validated when registering a dependent (matches native).
    // For non-dependent, field is hidden and payload auto-sends "NA".
    if (isDependent.value) {
      final rc = tecRationCardNo.text.trim();
      if (rc.isEmpty) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Please enter ration card number',
          () => Get.back(),
        );
        return false;
      }
      if (isDependent.value) {
        if (rc.length < 3 || rc.length > 15) {
          ToastManager.showAlertDialog(
            Get.context!,
            'Ration card number must be between 3 to 15 digits',
            () => Get.back(),
          );
          return false;
        }
        if (RegExp(r'^(.)\1+$').hasMatch(rc)) {
          ToastManager.showAlertDialog(
            Get.context!,
            'Invalid ration card number',
            () => Get.back(),
          );
          return false;
        }
      }
    }

    // ignore: avoid_print
    print(
      '[IsCellularPhone] Validation reached. isCellularPhone=${isCellularPhone.value} consentPhotoPath=${consentPhotoPath.value}',
    );
    if (isCellularPhone.value && consentPhotoPath.value.isEmpty) {
      // ignore: avoid_print
      print(
        '[IsCellularPhone=true] BLOCKED — basic phone, consent photo missing',
      );
      ToastManager.toast('Please capture consent photo');
      return false;
    }

    // Patient photo is required when face detection is NOT skipped
    if (!skipFaceDetection.value && patientPhotoPath.value.isEmpty) {
      ToastManager.toast(
        'Please capture patient photo (Face Detection is enabled)',
      );
      return false;
    }

    // Req 1: Identity/health card photo always required (including dependent+with_abha)
    if (healthCardPhotoPath.value.isEmpty) {
      ToastManager.toast('Health/Identity card photo is required');
      return false;
    }

    return true;
  }

  // void showConfirmationDialog(BuildContext context) {
  //   showDialog(
  //     context: context,
  //     builder: (context) {
  //       return AlertDialog(
  //         title: const Text('Confirm'),
  //         content: const Text(
  //           "Please confirm the beneficiary's details before submitting",
  //         ),
  //         actions: [
  //           TextButton(
  //             onPressed: () => Navigator.pop(context),
  //             child: const Text('Cancel'),
  //           ),
  //           TextButton(
  //             onPressed: () {
  //               Navigator.pop(context);
  //               submitRegistration(context);
  //             },
  //             child: const Text('Confirm'),
  //           ),
  //         ],
  //       );
  //     },
  //   );
  // }

  /// Step 1: validate → call VerifyDependentDetails_V2.
  /// On success → show confirmation dialog → Step 2 (_doSaveRegistration).
  /// Mirrors native verifyBeneficiaryDetails() gating UploadPatientDetails.
  Future<void> submitRegistration(BuildContext context) async {
    if (!_validateForm()) return;
    isSubmitting.value = true;
    ToastManager.showLoader();
    try {
      final regdNo = 'MH${tecWorkerRegNo.text.trim()}';
      final aadhaar =
          isAadhaarMode ? originalAadhaar : tecAadhaarNo.text.trim();
      final relationId = selectedRelation.value?.relId?.toString() ?? '20';
      final rationCard =
          tecRationCardNo.text.trim().isEmpty
              ? 'NA'
              : tecRationCardNo.text.trim();

      // ── Consent check (new in 9.82) ───────────────────────────────────────
      // isCellularPhone=false(0) → HAS smartphone → web consent link clicked → getConsent() checks server record.
      // isCellularPhone=true(1) → basic/feature phone → can't open link → phlebo captures consent photo → skip getConsent().
      // ignore: avoid_print
      print(
        '[IsCellularPhone] BRANCH DECISION → isCellularPhone=${isCellularPhone.value} RegdNo=$regdNo Name=${tecFullName.text.trim()} RelationId=$relationId',
      );
      if (!isCellularPhone.value) {
        // ignore: avoid_print
        print(
          '[IsCellularPhone=false] HAS smartphone → calling getConsent() API',
        );
        final consentStatus = await _repo.getConsent(
          bocwRegNo: regdNo,
          beneficiaryName: tecFullName.text.trim(),
          relationId: relationId,
        );

        ToastManager.hideLoader();
        isSubmitting.value = false;
        // ignore: avoid_print
        print('[getConsent] Response received. consentStatus=$consentStatus');

        if (consentStatus == null) {
          // ignore: avoid_print
          print('[getConsent] BLOCKED — server not responding');
          ToastManager.showAlertDialog(
            context,
            'Server not responding while checking consent. Please try again.',
            () => Get.back(),
          );
          return;
        }

        if (consentStatus == 0) {
          // ignore: avoid_print
          print('[getConsent] BLOCKED — consent not yet received (status=0)');
          ToastManager.showAlertDialog(
            context,
            'या लाभार्थ्याकडून संमती (Consent) अदयाप प्राप्त झालेला नाही त्यामळे स्क्रीनिंग प्रक्रिया पुढे सुरू करण्यासाठी लाभार्थ्याला संमती सादर करण्यास सांगावे',
            () => Get.back(),
          );
          return;
        }

        if (consentStatus == 2) {
          // ignore: avoid_print
          print('[getConsent] BLOCKED — consent withdrawn (status=2)');
          ToastManager.showAlertDialog(
            context,
            'या लाभार्थ्याकडून संमती (Consent) मागे घेण्यात आली आहे त्यामळे स्क्रीनिंग प्रक्रिया पुढे सुरू करण्यासाठी लाभार्थ्याला संमती सादर करण्यास सांगावे',
            () => Get.back(),
          );
          return;
        }
        // consentStatus == 1 → consent given, proceed
        // ignore: avoid_print
        print(
          '[getConsent] Consent CONFIRMED (status=1) → proceeding to verifyBeneficiaryDetails()',
        );
        isSubmitting.value = true;
        ToastManager.showLoader();
      } else {
        // ignore: avoid_print
        print(
          '[IsCellularPhone=true] Basic/feature phone → skipping getConsent(), consent photo captured by phlebo',
        );
      }
      // ─────────────────────────────────────────────────────────────────────

      final verify = await _repo.verifyBeneficiaryDetails(
        regdNo: regdNo,
        aadhaarNo: aadhaar,
        dependentName: tecFullName.text.trim(),
        relationId: relationId,
        pincode: tecPincode.text.trim(),
        dob: tecDob.text.trim(),
        rationCardNo: rationCard,
      );

      ToastManager.hideLoader();
      isSubmitting.value = false;

      if (verify == null) {
        ToastManager.showAlertDialog(
          context,
          'Server not responding. Please try again.',
          () => Get.back(),
        );
        return;
      }

      if (verify.status?.toLowerCase() != 'success') {
        final msg = verify.message ?? 'Verification failed';
        _clearForm();
        ToastManager.showAlertDialog(context, msg, () => Get.back());
        return;
      }

      // Verified — show confirmation then proceed to save.
      ToastManager().showConfirmationDialog(
        context: context,
        message: "Please confirm the beneficiary's details before submitting",
        didSelectYes: (bool confirmed) {
          Navigator.pop(context);
          if (confirmed) _doSaveRegistration(context);
        },
      );
    } catch (e) {
      isSubmitting.value = false;
      ToastManager.hideLoader();
      ToastManager.showAlertDialog(
        context,
        'Unexpected error: $e',
        () => Get.back(),
      );
    }
  }

  /// Step 2: build params and call saveD2DRegistration.
  Future<void> _doSaveRegistration(BuildContext context) async {
    isSubmitting.value = true;
    ToastManager.showLoader();
    try {
      final fields = <String, String>{
        'SiteId': navSiteId,
        'CampId': navCampId,
        'RegdNo': tecWorkerRegNo.text.trim(),
        // 'RegdNo': '${tecWorkerRegNo.text.trim()}$_beneficiaryCount',
        'Title': selectedTitle.value,
        'EnglishName': tecFullName.text.trim(),
        'MobileNo': tecMobileNo.text.trim(),
        'UID': isAadhaarMode ? originalAadhaar : tecAadhaarNo.text.trim(),
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
        'ReleationID': selectedRelation.value?.relId?.toString() ?? '20',
        'DependREGID': isDependent.value ? _workerRegdId : '0',
        'IndentityId': '1',
        'CW_WorkerName': isDependent.value ? workerNameDisplay.value : '',
        'next_renewal_date': _toApiDate(tecCardExpiry.text.trim()),
        'residential_address_postOffice': tecPostOffice.text.trim(),
        'residential_address_taluka': tecTaluka.text.trim(),
        'residential_address_district': tecDistrict.text.trim(),
        'CurrentAddress': tecCurrentAddr.text.trim(),
        'LandMark': tecLandmark.text.trim(),
        'AlternateMobNo': tecAltMobileNo.text.trim(),
        'IsMobNoVerified': '0',
        // native hardcodes "0" (params[33]); IsSelfMobNo carries the checkbox flag
        'IsSelfMobNo': isNumberNotBelongsToBeneficiary.value ? '0' : '1',
        'MobNoOf': altMobileBelongsTo.value,
        'OptionMode': '2',
        'VersionNo': APIConstants.kNativeVersion,
        'Isrecollection': navType == '5' ? '1' : '0',
        'Rej_Regdid': navType == '5' ? navRegId : '0',
        'Rej_CampID': navType == '5' ? navRejCampId : '0',
        'MaritalStatusID': maritalStatusId,
        'IsFaceDetectionEnabled': skipFaceDetection.value ? '0' : '1',
        'TALLGDCODE': talLgd,
        'DISTLGDCODE': navDistLgd,
        'IsRegdByCall': navType == '7' ? '1' : '0',
        'Latitude': currentLat.value,
        'Longitude': currentLong.value,
        'GPLGDCODE': isRural.value ? selectedGpCode.value : '0',
        'ABHANumber': tecAbhaNumber.text.trim(),
        'ABHAAddress': tecAbhaAddress.text.trim(),
        'IsWhatsAppNo': whatsAppMode.value,
        'WorkerGenderByPhlebo':
            isDependent.value
                ? (workerGenderByPhlebo.value.toLowerCase().startsWith('f')
                    ? 'Female'
                    : 'Male')
                : workerGenderByPhlebo.value.isNotEmpty
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
        // params[51]: native sends isfaceDetection again as IsFaceMatchFlag
        'IsFaceMatchFlag': skipFaceDetection.value ? '0' : '1',
        // params[52]: dependent's BOCW ID (set when user selects from dependent list)
        'Bocw_idDepend': bocwIdDepend.isNotEmpty ? bocwIdDepend : '0',
        // Native sends "NA" when ration card field is empty
        'RationCardNo':
            tecRationCardNo.text.trim().isEmpty
                ? 'NA'
                : tecRationCardNo.text.trim(),
        'IsCellularPhone': isCellularPhone.value ? '1' : '0',
      };
      // ignore: avoid_print
      print('[Reg Params] ===== SUBMIT ${fields.length} FIELDS =====');
      // ignore: avoid_print
      fields.forEach((k, v) => print('[Reg Params] $k = $v'));
      final result = await _repo.saveD2DRegistration(
        fields: fields,
        isFaceDetectionEnabled: !skipFaceDetection.value,
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
        consentPhoto:
            consentPhotoPath.value.isNotEmpty
                ? File(consentPhotoPath.value)
                : null,
      );

      if (result?.status?.toLowerCase() == 'success') {
        ToastManager.toast(result?.message ?? 'Registration successful');

        // Build display name (Title + Full Name)
        final title = selectedTitle.value;
        final fullName = tecFullName.text.trim();
        final displayName = title.isNotEmpty ? '$title $fullName' : fullName;

        // Gender display value
        final genderVal = selectedGender.value; // 'M', 'F', or 'O'

        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (_) => PatientFingerAndSignatureScreen(
                  campId: navCampId,
                  siteId: navSiteId,
                  regNo: tecWorkerRegNo.text.trim(),
                  onSuccess: _clearForm,
                  prefillRegdId: result!.regdId ?? '',
                  prefillRegdNo: tecWorkerRegNo.text.trim(),
                  prefillName: displayName,
                  prefillGender: genderVal,
                  prefillAge: tecAge.text.trim(),
                  prefillDob: tecDob.text.trim(),
                  rationCardNumber: tecRationCardNo.text.trim(),
                  dependentBocId: bocwIdDepend.isNotEmpty ? bocwIdDepend : '0',
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
    final formats = ['yyyy/MM/dd', 'dd/MM/yyyy', 'yyyy-MM-dd', 'dd-MM-yyyy'];
    for (final f in formats) {
      try {
        return DateFormat(f).parseStrict(input);
      } catch (_) {}
    }
    return null;
  }

  @override
  void onClose() {
    _locationTimer?.cancel();
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
    tecRationCardNo.dispose();
    super.onClose();
  }

  // ── Fill form from patient queue item ─────────────────────────────────────

  /// Called when the user taps "Go To Registration" from the patient queue
  /// view screen. Parses [response] JSON (same structure as `profile` from
  /// ABDM getAccountProfile) and pre-fills the registration form.
  void fillFromQueueResponse({
    required String response,
    required String authToken,
  }) {
    try {
      final responseObj = json.decode(response) as Map<String, dynamic>;
      final profileObj = responseObj['profile'] as Map<String, dynamic>? ?? {};
      final patientObj = profileObj['patient'] as Map<String, dynamic>? ?? {};

      final abhaNumber = patientObj['abhaNumber']?.toString() ?? '';
      final abhaAddress = patientObj['abhaAddress']?.toString() ?? '';
      final name = patientObj['name']?.toString() ?? '';
      final gender = patientObj['gender']?.toString() ?? '';
      final yearOfBirth = patientObj['yearOfBirth']?.toString() ?? '';
      final monthOfBirth = patientObj['monthOfBirth']?.toString() ?? '';
      final dayOfBirth = patientObj['dayOfBirth']?.toString() ?? '';
      final phoneNumber = patientObj['phoneNumber']?.toString() ?? '';

      final addressObj = patientObj['address'] as Map<String, dynamic>? ?? {};
      final addressLine = addressObj['line']?.toString() ?? '';
      final pincode = addressObj['pincode']?.toString() ?? '';

      // Store auth token
      _findAbhaAuthToken = authToken;

      // Fill ABHA fields
      tecAbhaNumber.text = abhaNumber;
      tecAbhaAddress.text = abhaAddress;

      // Fill name
      tecFullName.text = name;
      final nameParts = name.trim().split(RegExp(r'\s+'));
      tecFirstName.text = nameParts.isNotEmpty ? nameParts[0] : '';
      tecMiddleName.text = nameParts.length > 2 ? nameParts[1] : '';
      tecLastName.text =
          nameParts.length > 1 ? nameParts[nameParts.length - 1] : '';

      // Fill gender
      if (gender.toUpperCase() == 'M') {
        selectedGender.value = 'Male';
      } else if (gender.toUpperCase() == 'F') {
        selectedGender.value = 'Female';
      } else if (gender.toUpperCase() == 'O') {
        selectedGender.value = 'Other';
      }

      // Fill DOB + Age
      if (yearOfBirth.isNotEmpty &&
          monthOfBirth.isNotEmpty &&
          dayOfBirth.isNotEmpty) {
        final y = int.tryParse(yearOfBirth) ?? 0;
        final mo = int.tryParse(monthOfBirth) ?? 0;
        final d = int.tryParse(dayOfBirth) ?? 0;
        if (y > 0 && mo > 0 && d > 0) {
          final dob = DateTime(y, mo, d);
          tecDob.text =
              '${dob.year}/${dob.month.toString().padLeft(2, '0')}/${dob.day.toString().padLeft(2, '0')}';
          final now = DateTime.now();
          int age = now.year - dob.year;
          if (now.month < dob.month ||
              (now.month == dob.month && now.day < dob.day)) {
            age--;
          }
          tecAge.text = age.toString();
        }
      }

      // Fill mobile
      if (phoneNumber.isNotEmpty) {
        tecMobileNo.text = phoneNumber;
      }

      // Fill address
      if (addressLine.isNotEmpty) {
        tecPermAddr.text = addressLine;
        if (pincode.isNotEmpty && pincode != 'null') {
          tecPermAddr.text = '$addressLine, $pincode';
          tecPincode.text = pincode;
        }
      }

      // Mark ABHA as verified and lock the form
      abhaVerified.value = true;
      abhaFormLocked.value = true;
    } catch (_) {
      // Parsing failed — leave form as-is
    }
  }
}
