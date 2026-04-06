import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../models/camp_closing_model.dart';
import '../repository/health_screening_repository.dart';

class CampClosingController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  // ─── Observable state ─────────────────────────────────────────────────────
  final RxBool isLoading = false.obs;
  final RxBool isShowRemark = false.obs;
  final RxBool isUserInteractionEnabled = true.obs;
  final RxList<ConsumableOutput> consumableCampList =
      <ConsumableOutput>[].obs;

  // Camp stats (populated from API)
  final RxInt facilitedWorkers = 0.obs;
  final RxInt approvedBeneficiaries = 0.obs;
  final RxInt rejectedBeneficiaries = 0.obs;
  final RxInt verifiedBeneficiaries = 0.obs;
  final RxInt basicDetails = 0.obs;
  final RxInt physicalExamination = 0.obs;
  final RxInt lungFunctioinTest = 0.obs;
  final RxInt audioScreeningTest = 0.obs;
  final RxInt visionScreening = 0.obs;
  final RxInt sampleCollection = 0.obs;
  final RxInt ackowledgement = 0.obs;
  final RxInt totalPhysicalExam = 0.obs;
  final RxInt totalLungTest = 0.obs;
  final RxInt totalAudioTest = 0.obs;
  final RxInt totalVisionTest = 0.obs;
  final RxInt totalUrineCount = 0.obs;
  final RxInt totalBene = 0.obs;

  // Summary input values
  int totalBenificiaryTextField = 0;
  int rejectedBeneficiariesTF = 0;
  int sampleCollectionTF = 0;

  // TextEditingControllers bound to summary fields
  final TextEditingController totalApprovedBeneTextField =
      TextEditingController();
  final TextEditingController sampleCollectionTextField =
      TextEditingController();
  final TextEditingController rejectedBeneficiaryTextField =
      TextEditingController();
  final TextEditingController remarkTextField = TextEditingController();

  @override
  void onClose() {
    totalApprovedBeneTextField.dispose();
    sampleCollectionTextField.dispose();
    rejectedBeneficiaryTextField.dispose();
    remarkTextField.dispose();
    super.onClose();
  }

  // ─── Load all data ─────────────────────────────────────────────────────────

  Future<void> loadData({
    required int campId,
    required int distLgdCode,
    required String campDate,
  }) async {
    isLoading.value = true;
    ToastManager.showLoader();

    await Future.wait([
      _loadCampDetailsCount(
          campId: campId, distLgdCode: distLgdCode, campDate: campDate),
      _loadCampCloseDetails(campId: campId),
      _loadConsumables(campId: campId),
    ]);

    ToastManager.hideLoader();
    isLoading.value = false;
  }

  Future<void> _loadCampDetailsCount({
    required int campId,
    required int distLgdCode,
    required String campDate,
  }) async {
    final response = await _repo.getCampDetailsCount(
      campId: campId,
      distLgdCode: distLgdCode,
      campDate: campDate,
    );
    if (response?.output?.isNotEmpty == true) {
      final out = response!.output!.first;
      facilitedWorkers.value = out.facilitatedWorkers ?? 0;
      approvedBeneficiaries.value = out.approvedBeneficiaries ?? 0;
      rejectedBeneficiaries.value = out.rejectedBeneficiaries ?? 0;
      basicDetails.value = out.basicDetails ?? 0;
      physicalExamination.value = out.physicalExamination ?? 0;
      lungFunctioinTest.value = out.lungFunctioinTest ?? 0;
      audioScreeningTest.value = out.audioScreeningTest ?? 0;
      visionScreening.value = out.visionScreening ?? 0;
      sampleCollection.value = out.barcode ?? 0;
      ackowledgement.value = out.ackowledgement ?? 0;
      verifiedBeneficiaries.value = out.verifiedBeneficiaries ?? 0;
      totalPhysicalExam.value = out.physicalExamination ?? 0;
      totalLungTest.value = out.lungFunctioinTest ?? 0;
      totalAudioTest.value = out.audioScreeningTest ?? 0;
      totalVisionTest.value = out.visionScreening ?? 0;
      totalUrineCount.value = out.urineSampleCollection ?? 0;
      totalBene.value = out.facilitatedWorkers ?? 0;

      rejectedBeneficiariesTF = rejectedBeneficiaries.value;
      rejectedBeneficiaryTextField.text = '$rejectedBeneficiariesTF';
      totalBenificiaryTextField = approvedBeneficiaries.value;
      sampleCollectionTF = out.barcode ?? 0;
    }
  }

  Future<void> _loadCampCloseDetails({required int campId}) async {
    final response = await _repo.getCampCloseDetails(campId: campId);
    if (response?.output?.isNotEmpty == true) {
      final out = response!.output!.first;
      totalBenificiaryTextField = out.totalBenificiary ?? 0;
      totalApprovedBeneTextField.text = '$totalBenificiaryTextField';
      sampleCollectionTF = out.sampleCollectionCount ?? 0;
      sampleCollectionTextField.text = '$sampleCollectionTF';
    }
  }

  Future<void> _loadConsumables({required int campId}) async {
    final response = await _repo.getConsumableListDetails(campId: campId);
    if (response != null) {
      consumableCampList.assignAll(response.output ?? []);
    } else {
      consumableCampList.clear();
    }
  }

  // ─── Validation ────────────────────────────────────────────────────────────

  bool validate() {
    final totalSampleScreened = sampleCollection.value;
    final totalBeneCount = facilitedWorkers.value;
    final approvedBene = approvedBeneficiaries.value;
    final rejectedBene = rejectedBeneficiaries.value;
    final countNew = approvedBene + rejectedBene;
    final countAppRej = totalBenificiaryTextField + rejectedBeneficiariesTF;

    if (countNew != totalBeneCount) {
      _alert(
        'Camp will not be closed until rejected beneficiaries and approved '
        'beneficiary count should equal to facilitated beneficiary count',
      );
      return false;
    }

    if (verifiedBeneficiaries.value != approvedBene) {
      _alert(
        'Camp will not be closed until all beneficiaries are not validated '
        'by Central Camp Monitoring Team, Plz connect with them',
      );
      return false;
    }

    if (countAppRej != facilitedWorkers.value) {
      _alert(
        'Camp will not be closed until Facilitated Beneficiary count equal '
        'to addition of total approved beneficiary and rejected beneficiary count',
      );
      return false;
    }

    if (totalSampleScreened == 0) {
      _alert("Can't close this camp without sample collection");
      isUserInteractionEnabled.value = false;
      return false;
    }

    if (basicDetails.value < totalSampleScreened) {
      _alert('Basic Test Count should be equal to Sample Collection');
      return false;
    }

    if (physicalExamination.value < totalSampleScreened) {
      _alert('Physical Test Count should be equal to Sample Collection');
      return false;
    }

    if (lungFunctioinTest.value < totalSampleScreened) {
      _alert('Lung Test Count should be equal to Sample Collection');
      return false;
    }

    if (audioScreeningTest.value < totalSampleScreened) {
      _alert('Audio Test Count should be equal to Sample Collection');
      return false;
    }

    if (visionScreening.value < totalSampleScreened) {
      _alert('Vision Test Count should be equal to Sample Collection');
      return false;
    }

    if (ackowledgement.value < totalSampleScreened) {
      _alert('Acknowledge Count should be equal to Sample Collection');
      return false;
    }

    if (totalBenificiaryTextField == 0) {
      _alert('Please enter Total Beneficiary');
      return false;
    }

    if (sampleCollectionTextField == 0) {
      _alert('Please enter Sample Collection');
      return false;
    } else {
      if (sampleCollectionTextField != totalBenificiaryTextField) {
        isShowRemark.value = true;
        if (remarkTextField.text.trim().isEmpty) {
          _alert('Please enter remark');
          return false;
        }
      } else {
        remarkTextField.text = '';
        isShowRemark.value = false;
      }
    }

    if (consumableCampList.isEmpty) {
      _alert('Please enter consumable details');
      return false;
    }

    return true;
  }

  void _alert(String message) {
    ToastManager.showAlertDialog(Get.context!, message, () => Get.back());
  }
}