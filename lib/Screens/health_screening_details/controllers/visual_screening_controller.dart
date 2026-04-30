import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/visual_screening_test_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class VisualScreeningController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  late UserAttendancesUsingSitedetailsIDOutput patient;
  late int campId;

  final VisualScreeningData _data = VisualScreeningData();

  // Observable fields — UI rebuilds on change
  final RxString blindnessId = ''.obs;
  final RxString blindnessName = ''.obs;
  final RxString injuryRightId = ''.obs;
  final RxString injuryRightName = ''.obs;
  final RxString injuryLeftId = ''.obs;
  final RxString injuryLeftName = ''.obs;
  final RxString snellenRight = ''.obs;
  final RxString snellenLeft = ''.obs;
  final RxString snellenRightRemark = ''.obs;
  final RxString snellenLeftRemark = ''.obs;
  final RxString jaegarRight = ''.obs;
  final RxString jaegarLeft = ''.obs;
  final RxString rightRemark = ''.obs;
  final RxString leftRemark = ''.obs;
  final RxString nearRemark = ''.obs;
  final RxBool wearsGlasses = true.obs;
  final RxBool isSaving = false.obs;

  bool get rightBlind => _data.rightBlind;

  bool get leftBlind => _data.leftBlind;

  void init({
    required UserAttendancesUsingSitedetailsIDOutput patientData,
    required int campID,
  }) {
    patient = patientData;
    campId = campID;
  }

  // ── Blindness ───────────────────────────────────────────────────────────────

  void onBlindnessChanged(String id, String name) {
    _data.applyBlindness(id, name);
    _syncFromModel();
  }

  void resetBlindness() {
    _data.applyBlindness('', '');
    _syncFromModel();
  }

  // ── Injury ──────────────────────────────────────────────────────────────────

  void onInjuryRightChanged(String id, String name) {
    _data.injuryRightId = id;
    _data.injuryRightName = name;
    injuryRightId.value = id;
    injuryRightName.value = name;
  }

  void onInjuryLeftChanged(String id, String name) {
    _data.injuryLeftId = id;
    _data.injuryLeftName = name;
    injuryLeftId.value = id;
    injuryLeftName.value = name;
  }

  // ── Snellen ─────────────────────────────────────────────────────────────────

  void onSnellenRightChanged(String name) {
    _data.snellenRight = name;
    final remark = VisualScreeningData.snellenRemark(name);
    _data.snellenRightRemark = remark;
    _data.rightRemark = remark;
    snellenRight.value = name;
    snellenRightRemark.value = remark;
    rightRemark.value = remark;
  }

  void onSnellenLeftChanged(String name) {
    _data.snellenLeft = name;
    final remark = VisualScreeningData.snellenRemark(name);
    _data.snellenLeftRemark = remark;
    _data.leftRemark = remark;
    snellenLeft.value = name;
    snellenLeftRemark.value = remark;
    leftRemark.value = remark;
  }

  // ── Jaegar ──────────────────────────────────────────────────────────────────

  void onJaegarRightChanged(String name) {
    _data.jaegarRight = name;
    jaegarRight.value = name;
    _updateNearRemark();
  }

  void onJaegarLeftChanged(String name) {
    _data.jaegarLeft = name;
    jaegarLeft.value = name;
    _updateNearRemark();
  }

  void _updateNearRemark() {
    // Only update nearRemark when no blindness flag has already set it
    if (_data.blindnessId == '1' ||
        _data.blindnessId == '2' ||
        _data.blindnessId == '3') {
      return;
    }

    final r = _data.jaegarRight;
    final l = _data.jaegarLeft;
    if (r.isEmpty && l.isEmpty) return;

    String remark;
    if (r.isEmpty) {
      remark = VisualScreeningData.jaegarRemark(l);
    } else if (l.isEmpty) {
      remark = VisualScreeningData.jaegarRemark(r);
    } else {
      final rr = VisualScreeningData.jaegarRemark(r);
      final lr = VisualScreeningData.jaegarRemark(l);
      remark =
          (rr.contains('referred') || lr.contains('referred'))
              ? 'To be referred to ophthalmologist'
              : 'Normal Vision';
    }

    _data.nearRemark = remark;
    nearRemark.value = remark;
  }

  // ── Glasses ─────────────────────────────────────────────────────────────────

  void onWearsGlassesChanged(bool value) {
    _data.wearsGlasses = value;
    wearsGlasses.value = value;
  }

  // ── Sync helpers ─────────────────────────────────────────────────────────────

  void _syncFromModel() {
    blindnessId.value = _data.blindnessId;
    blindnessName.value = _data.blindnessName;
    injuryRightId.value = _data.injuryRightId;
    injuryRightName.value = _data.injuryRightName;
    injuryLeftId.value = _data.injuryLeftId;
    injuryLeftName.value = _data.injuryLeftName;
    snellenRight.value = _data.snellenRight;
    snellenLeft.value = _data.snellenLeft;
    snellenRightRemark.value = _data.snellenRightRemark;
    snellenLeftRemark.value = _data.snellenLeftRemark;
    jaegarRight.value = _data.jaegarRight;
    jaegarLeft.value = _data.jaegarLeft;
    rightRemark.value = _data.rightRemark;
    leftRemark.value = _data.leftRemark;
    nearRemark.value = _data.nearRemark;
    wearsGlasses.value = _data.wearsGlasses;
  }

  // ── Validation ───────────────────────────────────────────────────────────────

  String? validate() {
    if (_data.blindnessId.isEmpty) {
      return 'Please select visually impaired status.';
    }
    if (!_data.rightBlind) {
      if (_data.injuryRightId.isEmpty) {
        return 'Please select right eye injury/disease.';
      }
      if (_data.snellenRight.isEmpty) {
        return 'Please select right eye Snellen chart value.';
      }
      if (_data.jaegarRight.isEmpty) {
        return 'Please select right eye Jaegar chart value.';
      }
    }
    if (!_data.leftBlind) {
      if (_data.injuryLeftId.isEmpty) {
        return 'Please select left eye injury/disease.';
      }
      if (_data.snellenLeft.isEmpty) {
        return 'Please select left eye Snellen chart value.';
      }
      if (_data.jaegarLeft.isEmpty) {
        return 'Please select left eye Jaegar chart value.';
      }
    }
    return null;
  }

  // ── Submit ───────────────────────────────────────────────────────────────────

  Future<void> save(BuildContext context) async {
    final error = validate();
    if (error != null) {
      ToastManager.toast(error);
      return;
    }

    final userData = DataProvider().getParsedUserData()?.output?.first;
    final empCode = (userData?.empCode ?? 0).toString();
    final userName = userData?.name ?? '';

    isSaving.value = true;
    ToastManager.showLoader();

    final result = await _repo.submitVisualScreeningDetails(
      userId: empCode,
      regdId: (patient.regdId ?? 0).toString(),
      campId: campId.toString(),
      userName: userName,
      suggestion: _data.rightRemark,
      injuryRightName: _data.injuryRightName,
      injuryRightId: _data.injuryRightId,
      nearRemark: _data.nearRemark,
      injuryLeftName: _data.injuryLeftName,
      injuryLeftId: _data.injuryLeftId,
      leftRemark: _data.leftRemark,
      snellenRight: _data.snellenRight,
      snellenLeft: _data.snellenLeft,
      jaegarRight: _data.jaegarRight,
      jaegarLeft: _data.jaegarLeft,
      glassesId: _data.wearsGlasses ? '1' : '0',
      blindnessId: _data.blindnessId,
      snellenRightRemark: _data.snellenRightRemark,
      snellenLeftRemark: _data.snellenLeftRemark,
    );

    ToastManager.hideLoader();
    isSaving.value = false;

    if (!context.mounted) return;

    final status = (result?['status'] as String? ?? '').toLowerCase();

    if (result != null && status == 'success') {
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: 'Success',
        message: 'Visual screening test details submitted successfully',
        onTap: () {
          Get.back(); // close dialog
          Get.back(); // back to patient list
        },
      );
    } else {
      final msg = (result?['status'] as String?)?.isNotEmpty == true
          ? result!['status'] as String
          : 'Server not responding.';
      ToastManager.toast(msg);
    }
  }
}
