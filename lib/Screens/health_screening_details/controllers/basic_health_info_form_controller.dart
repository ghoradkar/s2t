import 'package:flutter/material.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamninationDetailsResponse.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/basic_health_info_patient_list_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/bp_device_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

class BasicHealthInfoFormController extends GetxController {
  final int regdId;
  final int campTypeID;
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  BasicHealthInfoFormController({
    required this.regdId,
    required this.campTypeID,
    this.patientItem,
  });

  final _api = APIManager();

  // Patient data (from API, for re-prefill if previously screened)
  D2DPhysicalExamninationDetailsOutput? patientObj;

  // ── Patient Details (read-only, pre-filled from list item) ──────────────
  final beneficiaryNameCtrl = TextEditingController();
  final beneficiaryNoCtrl = TextEditingController();
  final mobileNoCtrl = TextEditingController();
  final genderCtrl = TextEditingController();
  final ageCtrl = TextEditingController();

  // ── Basic Health Info ───────────────────────────────────────────────────
  String? selectedBloodGroup;
  final heightCtrl = TextEditingController();
  final weightCtrl = TextEditingController();
  final bmiCtrl = TextEditingController();

  // BMI Status: 0=Underweight, 1=Normal, 2=Overweight
  int bmiStatusIndex = -1;

  // Fasting: 0=<12hrs, 1=>12hrs
  int fastingIndex = -1;
  final fastingHrsInputCtrl =
      TextEditingController(); // visible only when fastingIndex==0

  // Marital Status: 0=Married, 1=Unmarried
  int maritalStatusIndex = -1;
  final childrenCtrl = TextEditingController();

  // Habits: 0=No, 1=Yes, -1=not selected
  int familyPlanningIndex = -1;

  int smokingIndex = 1;
  final smokingMonthCtrl = TextEditingController();
  final smokingYearCtrl = TextEditingController();

  int alcoholIndex = 1;
  final alcoholMonthCtrl = TextEditingController();
  final alcoholYearCtrl = TextEditingController();

  int tobaccoIndex = 1;
  final tobaccoMonthCtrl = TextEditingController();
  final tobaccoYearCtrl = TextEditingController();

  int drugsIndex = 1;
  final drugsMonthCtrl = TextEditingController();
  final drugsYearCtrl = TextEditingController();

  // ── Blood Sugar ─────────────────────────────────────────────────────────
  final bloodSugarRCtrl = TextEditingController();

  // ── Blood Pressure ──────────────────────────────────────────────────────
  final systolicCtrl = TextEditingController();
  final diastolicCtrl = TextEditingController();

  // ── BLE Device state ────────────────────────────────────────────────────
  final RxString deviceStatus = 'No devices connected'.obs;
  final RxString deviceError = ''.obs;
  final RxBool isTransferring = false.obs;
  final RxBool isWeightDataReceived = false.obs;
  final RxBool isSugarDataReceived = false.obs;
  BluetoothDevice? _weightDevice;
  BluetoothDevice? _sugarDevice;
  final BpDeviceController bpController = BpDeviceController();

  bool get isWeightConnected => _weightDevice != null || isWeightDataReceived.value;

  bool get isSugarConnected => _sugarDevice != null || isSugarDataReceived.value;


  int _empCode = 0;

  bool get isLive => _api.apiMode == APIMode.Live;

  static const List<String> bloodGroups = [
    'A+',
    'A-',
    'B+',
    'B-',
    'AB+',
    'AB-',
    'O+',
    'O-',
    'Unknown',
  ];

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    _empCode = userData?.empCode ?? 0;
    _prefillFromListItem();
    _fetchPatient();
  }

  @override
  void onClose() {
    bpController.onClose();
    FlutterBluePlus.stopScan();
    for (final c in [
      beneficiaryNameCtrl,
      beneficiaryNoCtrl,
      mobileNoCtrl,
      genderCtrl,
      ageCtrl,
      heightCtrl,
      weightCtrl,
      bmiCtrl,
      fastingHrsInputCtrl,
      childrenCtrl,
      smokingMonthCtrl,
      smokingYearCtrl,
      alcoholMonthCtrl,
      alcoholYearCtrl,
      tobaccoMonthCtrl,
      tobaccoYearCtrl,
      drugsMonthCtrl,
      drugsYearCtrl,
      bloodSugarRCtrl,
      systolicCtrl,
      diastolicCtrl,
    ]) {
      c.dispose();
    }
    super.onClose();
  }

  // ── Pre-fill from patient list row (always available) ───────────────────

  void _prefillFromListItem() {
    final item = patientItem;
    if (item == null) return;
    beneficiaryNameCtrl.text = (item.englishName ?? '').toUpperCase();
    beneficiaryNoCtrl.text = item.regdNo?.toString() ?? '';
    mobileNoCtrl.text = item.mobileNo ?? '';
    genderCtrl.text = item.gender ?? '';
    ageCtrl.text = item.age?.toString() ?? '';
  }

  // ── Fetch previously saved health data ──────────────────────────────────

  Future<void> _fetchPatient() async {
    ToastManager.showLoader();
    await _api.getUserDataforPhysicalExamninationAPI({
      'RegdId': regdId.toString(),
    }, _onPatientData);
  }

  void _onPatientData(
    D2DPhysicalExamninationDetailsResponse? response,
    String error,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success && (response?.output?.isNotEmpty ?? false)) {
      patientObj = response!.output!.first;
      _prefill();
    }
    // Silent on failure — patient details already filled from list item
    update();
  }

  void _prefill() {
    final p = patientObj;
    if (p == null) return;

    // Patient details (override with API data if available)
    if ((p.beneficiaryName ?? '').isNotEmpty) {
      beneficiaryNameCtrl.text = p.beneficiaryName!.toUpperCase();
    }
    if ((p.beneficiaryRegdNo?.toString() ?? '').isNotEmpty) {
      beneficiaryNoCtrl.text = p.beneficiaryRegdNo.toString();
    }
    if ((p.mobNo ?? '').isNotEmpty) mobileNoCtrl.text = p.mobNo!;
    if ((p.gender ?? '').isNotEmpty) genderCtrl.text = p.gender!;
    if (p.age != null && p.age! > 0) ageCtrl.text = p.age.toString();

    // Blood Group
    final bg = p.bloodGroup ?? '';
    if (bloodGroups.contains(bg)) selectedBloodGroup = bg;

    // Height, Weight, BMI
    heightCtrl.text = _nonZero(p.heightCMs?.toString());
    weightCtrl.text = _nonZero(p.weightKGs?.toString());
    bmiCtrl.text = _nonZero(p.bMI?.toString());

    // BMI status from saved value
    final bmi = p.bMI ?? 0;
    if (bmi > 0) {
      if (bmi < 18.5) {
        bmiStatusIndex = 0;
      } else if (bmi < 25) {
        bmiStatusIndex = 1;
      } else {
        bmiStatusIndex = 2;
      }
    }

    // Fasting — determine radio and optional hours entry
    final fhRaw = p.fastingHrs ?? '';
    final fhLower = fhRaw.toLowerCase().trim();
    if (fhLower.contains('<') || fhLower.contains('less')) {
      fastingIndex = 0;
    } else if (fhLower.contains('>') ||
        fhLower.contains('more') ||
        fhLower.contains('great')) {
      fastingIndex = 1;
    } else {
      final fhNum = double.tryParse(fhRaw.replaceAll(RegExp(r'[^\d.]'), ''));
      if (fhNum != null) {
        if (fhNum < 12) {
          fastingIndex = 0;
          fastingHrsInputCtrl.text = fhNum.toStringAsFixed(0);
        } else {
          fastingIndex = 1;
        }
      }
    }

    // Marital Status
    final ms = (p.maritalStatus ?? '').toLowerCase();
    if (ms.contains('married') && !ms.contains('un')) {
      maritalStatusIndex = 0;
    } else if (ms.contains('unmarried') || ms.contains('single')) {
      maritalStatusIndex = 1;
    }

    // Children
    final nc = p.noOfChildren ?? 0;
    if (nc > 0) childrenCtrl.text = nc.toString();

    // Family Planning
    final fp =
        (p.familyPlanOperation ?? p.famillyPlaning ?? '').toLowerCase().trim();
    if (fp == '1' || fp == 'yes') {
      familyPlanningIndex = 1;
    } else if (fp == '0' || fp == 'no') {
      familyPlanningIndex = 0;
    }

    // Smoking
    final sm = (p.isSmoking ?? p.smoking ?? '').toLowerCase().trim();
    if (sm == '1' || sm == 'yes') {
      smokingIndex = 1;
      smokingMonthCtrl.text = _nonZero(p.smokingSinceMonth?.toString());
      smokingYearCtrl.text = _nonZero(p.smokingSinceYear?.toString());
    } else if (sm == '0' || sm == 'no') {
      smokingIndex = 0;
    }

    // Alcohol
    final al = (p.isAlcohol ?? p.alcohol ?? '').toLowerCase().trim();
    if (al == '1' || al == 'yes') {
      alcoholIndex = 1;
      alcoholMonthCtrl.text = _nonZero(p.alcoholSinceMonth?.toString());
      alcoholYearCtrl.text = _nonZero(p.alcoholSinceYear?.toString());
    } else if (al == '0' || al == 'no') {
      alcoholIndex = 0;
    }

    // Tobacco
    final tb = (p.isTobaco ?? p.tobaco ?? '').toLowerCase().trim();
    if (tb == '1' || tb == 'yes') {
      tobaccoIndex = 1;
      tobaccoMonthCtrl.text = _nonZero(p.tobacoSinceMonth?.toString());
      tobaccoYearCtrl.text = _nonZero(p.tobacoSinceYear?.toString());
    } else if (tb == '0' || tb == 'no') {
      tobaccoIndex = 0;
    }

    // Drugs
    final dr = (p.isDrug ?? '').toLowerCase().trim();
    if (dr == '1' || dr == 'yes') {
      drugsIndex = 1;
      drugsMonthCtrl.text = _nonZero(p.drugSinceMonth?.toString());
      drugsYearCtrl.text = _nonZero(p.drugSinceYear?.toString());
    } else if (dr == '0' || dr == 'no') {
      drugsIndex = 0;
    }

    // Blood Sugar R
    bloodSugarRCtrl.text = _nonZero(p.bloodSugarR);

    // Blood Pressure
    systolicCtrl.text = _nonZero(p.systolic?.toString());
    diastolicCtrl.text = _nonZero(p.diastolic?.toString());
  }

  String _nonZero(String? v) {
    if (v == null || v.isEmpty || v == '0' || v == '0.0') return '';
    return v;
  }

  // ── BMI auto-calculate ──────────────────────────────────────────────────

  void recalculateBMI() {
    final h = double.tryParse(heightCtrl.text);
    final w = double.tryParse(weightCtrl.text);
    if (h != null && h > 0 && w != null && w > 0) {
      final bmi = w / ((h / 100) * (h / 100));
      bmiCtrl.text = bmi.toStringAsFixed(1);
      if (bmi < 18.5) {
        bmiStatusIndex = 0;
      } else if (bmi < 25) {
        bmiStatusIndex = 1;
      } else {
        bmiStatusIndex = 2;
      }
    }
    update();
  }

  // ── Bluetooth check ─────────────────────────────────────────────────────

  Future<bool> isBluetoothOn() async {
    final state = await FlutterBluePlus.adapterState.first;
    return state == BluetoothAdapterState.on;
  }

  // ── Connect devices ─────────────────────────────────────────────────────

  Future<void> connectWeightDevice(BluetoothDevice device) async {
    ToastManager.showLoader();
    try {
      await device.connect(license: License.free, timeout: const Duration(seconds: 10));
      _weightDevice = device;
      deviceError.value = '';
      _updateDeviceStatus();
      ToastManager.toast('Weight machine connected: ${device.platformName}');
    } catch (e) {
      deviceError.value = 'Weight machine connection failed: $e';
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  Future<void> connectSugarDevice(BluetoothDevice device) async {
    ToastManager.showLoader();
    try {
      await device.connect(license: License.free, timeout: const Duration(seconds: 10));
      _sugarDevice = device;
      deviceError.value = '';
      _updateDeviceStatus();
      ToastManager.toast('Glucose device connected: ${device.platformName}');
    } catch (e) {
      deviceError.value = 'Glucose device connection failed: $e';
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  Future<void> connectGeneralDevice(BluetoothDevice device) async {
    if (_weightDevice == null) {
      await connectWeightDevice(device);
    } else {
      await connectSugarDevice(device);
    }
  }

  Future<void> connectBpDevice(BluetoothDevice device) async {
    // SCAN only saves the device — no BLE connection here.
    // Bonding + data fetch happens when user taps TRANSFER.
    await bpController.saveDevice(device);
  }

  Future<void> transferBpData() async {
    final bp = bpController;
    if (bp.savedDeviceMac.value.isEmpty && !bp.isConnected.value) {
      ToastManager.toast('Please SCAN and pair the BP device first.');
      return;
    }
    // Always try to fetch fresh data from the device; cached reading is fallback
    ToastManager.showLoader();
    await bp.requestData();
    ToastManager.hideLoader();
    final reading = bp.getLastReading();
    if (reading == null) {
      ToastManager.toast(
        'No reading stored on device yet.\nPress START on the device to take a measurement, then tap TRANSFER.',
      );
      return;
    }
    _applyBpReading(reading);
  }

  void _applyBpReading(Map<String, int> reading) {
    systolicCtrl.text  = reading['systolic'].toString();
    diastolicCtrl.text = reading['diastolic'].toString();
    ToastManager.toast('Blood pressure reading applied.');
    update();
  }

  void _updateDeviceStatus() {
    final wName = _weightDevice?.platformName ?? '';
    final sName = _sugarDevice?.platformName ?? '';
    final parts = [
      if (wName.isNotEmpty) 'Weight: $wName',
      if (sName.isNotEmpty) 'Sugar: $sName',
    ];
    deviceStatus.value = parts.isEmpty ? 'No devices connected' : parts.join(' | ');
  }

  // ── Transfer data from connected devices ────────────────────────────────

  Future<void> transferData() async {
    final bpReading = bpController.getLastReading();
    if (_weightDevice == null && _sugarDevice == null && bpReading == null) {
      ToastManager.toast(
        isLive
            ? 'Please connect devices before transferring'
            : 'No device connected. Enter values manually.',
      );
      return;
    }
    isTransferring.value = true;
    deviceError.value = '';
    try {
      if (_weightDevice != null) await _readWeightData(_weightDevice!);
      if (_sugarDevice != null) await _readSugarData(_sugarDevice!);
      if (bpReading != null) {
        systolicCtrl.text = bpReading['systolic'].toString();
        diastolicCtrl.text = bpReading['diastolic'].toString();
      }
      recalculateBMI();
      ToastManager.toast('Data transferred successfully');
    } catch (e) {
      deviceError.value = 'Transfer failed: $e';
    }
    isTransferring.value = false;
    update();
  }

  Future<void> _readWeightData(BluetoothDevice device) async {
    // TODO: Implement with actual GATT UUIDs for the weight scale model used.
    // Standard BLE Weight Scale service: 0x181D
    // Weight characteristic: 0x2A98, Height: 0x2A8E
  }

  Future<void> _readSugarData(BluetoothDevice device) async {
    // TODO: Implement with actual GATT UUIDs for the glucometer model used.
    // Standard BLE Glucose service: 0x1808
    // Glucose Measurement characteristic: 0x2A18
  }

  // ── Save ────────────────────────────────────────────────────────────────

  Future<void> onSave() async {
    if (!_validate()) return;
    ToastManager.showLoader();

    final campId = patientItem?.campId ?? 0;
    final age = patientItem?.age ?? 0;
    final gender = patientItem?.gender ?? '';

    // BMIStatus: native uses 1=Underweight, 2=Normal, 3=Overweight
    final bmiStatus = bmiStatusIndex >= 0 ? (bmiStatusIndex + 1).toString() : '0';

    // MaritalStatus: native uses 1=Unmarried, 2=Married
    final maritalStatus = maritalStatusIndex == 0 ? '2' : '1';

    // FastingHrs: send actual hours for <12, send '12' for >=12
    final fastingHrs = fastingIndex == 0
        ? (fastingHrsInputCtrl.text.trim().isEmpty ? '0' : fastingHrsInputCtrl.text.trim())
        : '12';

    _api.insertBasicHealthInfoNewAPI(
      {
        'RegdId': regdId.toString(),
        'CampId': campId.toString(),
        'Height_CMs': heightCtrl.text.trim(),
        'Weight_KGs': weightCtrl.text.trim(),
        'BloodPressure': '0',
        'BloodSugar_F': '',
        'BloodSugar_PP': '',
        'BloodSugar_R': bloodSugarRCtrl.text.trim(),
        'BMI': bmiCtrl.text.trim(),
        'BMIStatus': bmiStatus,
        'BloodGroup': selectedBloodGroup ?? '',
        'MaritalStatus': maritalStatus,
        'NoOfChildren': maritalStatusIndex == 0 ? childrenCtrl.text.trim() : '0',
        'FamilyPlanOperation': familyPlanningIndex == 1 ? '1' : '0',
        'Alcohol': alcoholIndex == 1 ? '1' : '0',
        'Smokin': smokingIndex == 1 ? '1' : '0',
        'Tobaco': tobaccoIndex == 1 ? '1' : '0',
        'Age': age.toString(),
        'Gender': gender,
        'Systolic': systolicCtrl.text.trim(),
        'Diastolic': diastolicCtrl.text.trim(),
        'CreatedBy': _empCode.toString(),
        'Tests_Details': '[]',
        'PulseRate': '0',
        'Drugs': drugsIndex == 1 ? '1' : '0',
        'AlcoholSinceMonth': alcoholIndex == 1 ? alcoholMonthCtrl.text.trim() : '0',
        'AlcoholSinceYear': alcoholIndex == 1 ? alcoholYearCtrl.text.trim() : '0',
        'SmokingSinceMonth': smokingIndex == 1 ? smokingMonthCtrl.text.trim() : '0',
        'SmokingSinceYear': smokingIndex == 1 ? smokingYearCtrl.text.trim() : '0',
        'TobacoSinceMonth': tobaccoIndex == 1 ? tobaccoMonthCtrl.text.trim() : '0',
        'TobacoSinceYear': tobaccoIndex == 1 ? tobaccoYearCtrl.text.trim() : '0',
        'DrugSinceMonth': drugsIndex == 1 ? drugsMonthCtrl.text.trim() : '0',
        'DrugSinceYear': drugsIndex == 1 ? drugsYearCtrl.text.trim() : '0',
        'Temperature': '0',
        'SPO2': '0',
        'AppVersion': '9.63',
        'isBPManual': '1',
        'IsFromWeightMachine': '0',
        'IsFromSugarDevice': '0',
        'IsFromBloodPressureDevice': '0',
        'NameOfWeightMachine': '',
        'NameOfSugarDevice': '',
        'NameOfBloodPressureDevice': '',
        'FastingHrs': fastingHrs,
      },
      _onSaveResult,
    );
  }

  void _onSaveResult(
    dynamic response,
    String error,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      ToastManager().showSuccessOkayDialog(
        context: Get.context!,
        title: 'Success',
        message: 'Saved successfully',
        onTap: () {
          Get.back();
          Get.back();
          try {
            Get.find<BasicHealthInfoPatientListController>().fetchPatients();
          } catch (_) {}
        },
      );
    } else {
      ToastManager.toast(error.isNotEmpty ? error : 'Failed to save');
    }
  }

  void applyWeightData({
    required String weight,
    required String bmi,
    required String deviceNameStr,
  }) {
    weightCtrl.text = weight;
    if (bmi.isNotEmpty) {
      bmiCtrl.text = bmi;
      final bmiVal = double.tryParse(bmi);
      if (bmiVal != null) {
        if (bmiVal < 18.5) {
          bmiStatusIndex = 0;
        } else if (bmiVal < 25) {
          bmiStatusIndex = 1;
        } else {
          bmiStatusIndex = 2;
        }
      }
    }
    isWeightDataReceived.value = true;
    update();
  }

  void applySugarData({
    required String glucose,
    required String deviceNameStr,
  }) {
    // Extract just the numeric mg/dL value from "123.00 mg/dL 2025-01-01 09:30"
    final parts = glucose.split(' ');
    final numericValue = parts.isNotEmpty ? parts[0] : glucose;
    bloodSugarRCtrl.text = numericValue;
    isSugarDataReceived.value = true;
    update();
  }

  // ── Validation ──────────────────────────────────────────────────────────

  bool _validate() {
    if (isLive) {
      if (_weightDevice == null && !isWeightDataReceived.value) {
        ToastManager.toast('Please connect weight machine');
        return false;
      }
      if (_sugarDevice == null && !isSugarDataReceived.value) {
        ToastManager.toast('Please connect sugar device');
        return false;
      }
    }
    if (selectedBloodGroup == null) {
      ToastManager.toast('Please select blood group');
      return false;
    }
    if (heightCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter height');
      return false;
    }
    if (weightCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter weight');
      return false;
    }
    if (bmiStatusIndex == -1) {
      ToastManager.toast('Please select BMI status');
      return false;
    }
    if (fastingIndex == -1) {
      ToastManager.toast('Please select fasting duration');
      return false;
    }
    if (fastingIndex == 0 && fastingHrsInputCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter fasting hours');
      return false;
    }
    if (maritalStatusIndex == -1) {
      ToastManager.toast('Please select marital status');
      return false;
    }
    if (familyPlanningIndex == -1) {
      ToastManager.toast('Please select family planning status');
      return false;
    }
    if (smokingIndex == -1) {
      ToastManager.toast('Please select smoking status');
      return false;
    }
    if (smokingIndex == 1) {
      if (smokingMonthCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter smoking since month');
        return false;
      }
      if (smokingYearCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter smoking since year');
        return false;
      }
    }
    if (alcoholIndex == -1) {
      ToastManager.toast('Please select alcohol status');
      return false;
    }
    if (alcoholIndex == 1) {
      if (alcoholMonthCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter alcohol since month');
        return false;
      }
      if (alcoholYearCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter alcohol since year');
        return false;
      }
    }
    if (tobaccoIndex == -1) {
      ToastManager.toast('Please select tobacco status');
      return false;
    }
    if (tobaccoIndex == 1) {
      if (tobaccoMonthCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter tobacco since month');
        return false;
      }
      if (tobaccoYearCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter tobacco since year');
        return false;
      }
    }
    if (drugsIndex == -1) {
      ToastManager.toast('Please select drugs status');
      return false;
    }
    if (drugsIndex == 1) {
      if (drugsMonthCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter drugs since month');
        return false;
      }
      if (drugsYearCtrl.text.trim().isEmpty) {
        ToastManager.toast('Please enter drugs since year');
        return false;
      }
    }
    if (bloodSugarRCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter blood sugar (R) value');
      return false;
    }
    if (systolicCtrl.text.trim().isEmpty || diastolicCtrl.text.trim().isEmpty) {
      ToastManager.toast('Please enter blood pressure values');
      return false;
    }
    return true;
  }
}
