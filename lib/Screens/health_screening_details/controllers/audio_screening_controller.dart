import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:just_audio/just_audio.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';


class AudioScreeningController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();
  final AudioPlayer _player = AudioPlayer();

  late UserAttendancesUsingSitedetailsIDOutput patient;
  late int campId;

  static const List<int> dbLevels = [-5, 0, 5, 10, 15, 20, 30, 40, 50, 60, 70, 80];
  static const int _defaultDbIndex = 7; // 40 dB

  static const List<int> frequencies = [125, 250, 500, 1000, 2000, 4000, 8000];

  static const List<String> deafnessOptions = [
    'No Deafness',
    'Right Ear',
    'Left Ear',
    'Both Ears',
  ];

  static const List<String> remarkOptions = [
    'Normal Hearing',
    'Mild Hearing Loss',
    'Moderate Hearing Loss',
    'Severe Hearing Loss',
    'Deafness',
  ];

  // '' = no selection yet
  final RxString selectedDeafness = ''.obs;
  final RxInt selectedFrequencyIndex = 0.obs;

  // null = neither selected, true = left, false = right
  final Rxn<bool> isLeftEarSelected = Rxn<bool>();

  final RxInt currentDbIndex = _defaultDbIndex.obs;
  final RxBool isSaving = false.obs;

  final RxList<HearingRecord> hearingRecords = <HearingRecord>[].obs;

  final RxString leftEarRemark = ''.obs;
  final RxString rightEarRemark = ''.obs;

  int get currentDb => dbLevels[currentDbIndex.value];
  int get currentFrequency => frequencies[selectedFrequencyIndex.value];

  bool get leftEarEnabled {
    if (selectedDeafness.value.isEmpty) return false;
    switch (selectedDeafness.value) {
      case 'Right Ear':
        return true;
      case 'Left Ear':
      case 'Both Ears':
        return false;
      default:
        return true;
    }
  }

  bool get rightEarEnabled {
    if (selectedDeafness.value.isEmpty) return false;
    switch (selectedDeafness.value) {
      case 'Left Ear':
        return true;
      case 'Right Ear':
      case 'Both Ears':
        return false;
      default:
        return true;
    }
  }

  @override
  void onClose() {
    _player.dispose();
    super.onClose();
  }

  void init({
    required UserAttendancesUsingSitedetailsIDOutput patientData,
    required int campID,
  }) {
    patient = patientData;
    campId = campID;
    hearingRecords.assignAll(
      frequencies.map((f) => HearingRecord(frequency: f)).toList(),
    );
  }

  void onDeafnessChanged(String? value) {
    if (value == null) return;
    selectedDeafness.value = value;
    leftEarRemark.value = '';
    rightEarRemark.value = '';

    for (final r in hearingRecords) {
      r.leftDb = 40;
      r.rightDb = 40;
      r.leftRemark = '';
      r.rightRemark = '';
    }

    switch (value) {
      case 'Both Ears':
        // Both ears deaf → left=0, right=0
        isLeftEarSelected.value = null;
        for (final r in hearingRecords) {
          r.leftDb = 0;
          r.rightDb = 0;
        }
        leftEarRemark.value = 'Deafness';
        rightEarRemark.value = 'Deafness';
        break;
      case 'Right Ear':
        // Right ear deaf → right=0, left stays at 40 (healthy, testable)
        isLeftEarSelected.value = true;
        for (final r in hearingRecords) {
          r.rightDb = 0;
        }
        rightEarRemark.value = 'Deafness';
        break;
      case 'Left Ear':
        // Left ear deaf → left=0, right stays at 40 (healthy, testable)
        isLeftEarSelected.value = false;
        for (final r in hearingRecords) {
          r.leftDb = 0;
        }
        leftEarRemark.value = 'Deafness';
        break;
      default:
        // No Deafness → both=40 (already set in reset), auto-select right ear
        isLeftEarSelected.value = false;
        break;
    }

    hearingRecords.refresh();
    _resetVolume();
    _stopPlayer();
  }

  void resetDeafnessSelection() {
    selectedDeafness.value = '';
    isLeftEarSelected.value = null;
    leftEarRemark.value = '';
    rightEarRemark.value = '';
    for (final r in hearingRecords) {
      r.leftDb = 40;
      r.rightDb = 40;
      r.leftRemark = '';
      r.rightRemark = '';
    }
    hearingRecords.refresh();
    _resetVolume();
  }

  void onFrequencyChanged(int index) {
    selectedFrequencyIndex.value = index;
    _resetVolume();
    _stopPlayer();
  }

  void selectLeftEar() {
    if (!leftEarEnabled) return;
    isLeftEarSelected.value = true;
    _resetVolume();
    _stopPlayer();
  }

  void selectRightEar() {
    if (!rightEarEnabled) return;
    isLeftEarSelected.value = false;
    _resetVolume();
    _stopPlayer();
  }

  void cannotHear() {
    if (isLeftEarSelected.value == null) {
      ToastManager.toast('Please select an ear first.');
      return;
    }
    if (currentDbIndex.value >= dbLevels.length - 1) {
      ToastManager.toast('dB cannot be greater than 80 dB');
      return;
    }
    currentDbIndex.value++;      // increment first
    _saveCurrentThreshold();     // save the incremented dB to table
    _playCurrentTone();
  }

  void canHear() {
    if (isLeftEarSelected.value == null) {
      ToastManager.toast('Please select an ear first.');
      return;
    }
    if (currentDbIndex.value <= 0) {
      ToastManager.toast('dB cannot be less than -5 dB');
      return;
    }
    currentDbIndex.value--;      // decrement first
    _saveCurrentThreshold();     // save the decremented dB to table
    _stopPlayer();
  }

  void _saveCurrentThreshold() {
    final record = hearingRecords[selectedFrequencyIndex.value];
    if (isLeftEarSelected.value == true) {
      record.leftDb = currentDb;
    } else {
      record.rightDb = currentDb;
    }
    hearingRecords.refresh();
  }

  void _resetVolume() {
    currentDbIndex.value = _defaultDbIndex;
  }

  Future<void> _playCurrentTone() async {
    final db = currentDb;
    final freq = currentFrequency;
    final assetPath = 'assets/audio/puretone_${freq}_$db.ogg';
    try {
      await _player.stop();
      await _player.setAsset(assetPath);
      await _player.play();
    } catch (e) {
      debugPrint('Audio play error: $e');
    }
  }

  Future<void> _stopPlayer() async {
    try {
      await _player.stop();
    } catch (_) {}
  }

  void onLeftRemarkChanged(String? value) => leftEarRemark.value = value ?? '';
  void onRightRemarkChanged(String? value) => rightEarRemark.value = value ?? '';

  String? validate() {
    if (selectedDeafness.value.isEmpty) return 'Please select deafness info.';
    if (leftEarEnabled && leftEarRemark.value.isEmpty) {
      return 'Please select left ear remark.';
    }
    if (rightEarEnabled && rightEarRemark.value.isEmpty) {
      return 'Please select right ear remark.';
    }
    for (final r in hearingRecords) {
      r.leftRemark = leftEarRemark.value;
      r.rightRemark = rightEarRemark.value;
    }
    return null;
  }

  Future<void> save(BuildContext context) async {
    final error = validate();
    if (error != null) {
      ToastManager.toast(error);
      return;
    }
    final regdId = (patient.regdId ?? 0).toString();
    final campIdStr = campId.toString();
    final empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    final jsonString = json.encode(
      hearingRecords.map((r) => r.toJson(regdId, campIdStr)).toList(),
    );

    isSaving.value = true;
    ToastManager.showLoader();
    final success = await _repo.saveAudioScreeningData(
      createdBy: empCode.toString(),
      jsonString: jsonString,
    );
    ToastManager.hideLoader();
    isSaving.value = false;

    if (success) {
      ToastManager.toast('Audiometric test saved successfully');
      if (context.mounted) Navigator.pop(context);
    } else {
      ToastManager.toast('Please try again!');
    }
  }
}

class HearingRecord {
  final int frequency;
  int leftDb;
  int rightDb;
  String leftRemark;
  String rightRemark;

  HearingRecord({
    required this.frequency,
    this.leftDb = 40,
    this.rightDb = 40,
    this.leftRemark = '',
    this.rightRemark = '',
  });

  Map<String, dynamic> toJson(String regdId, String campId) => {
    'frequency': frequency,
    'lefttVolume': leftDb,
    'rightVolume': rightDb,
    'observation': leftRemark,
    'RightRemark': rightRemark,
    'patientRegId': regdId,
    'campId': campId,
  };
}
