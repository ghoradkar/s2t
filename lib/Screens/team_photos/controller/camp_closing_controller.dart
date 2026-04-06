import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_closing_model.dart';

import '../../../Modules/constants/images.dart';

class CampClosingController extends GetxController {
  // ─── Observable state ─────────────────────────────────────────────────────
  final RxBool isLoading = false.obs;

  // Disabled when camp is already closed (GetCampCloseDetails returns success)
  final RxBool isUserInteractionEnabled = true.obs;
  final RxList<ConsumableOutput> consumableCampList = <ConsumableOutput>[].obs;

  // Stats from GetCampDetailsCountRegular_InCampTest
  final RxInt facilitedWorkers = 0.obs;
  final RxInt approvedBeneficiaries = 0.obs;
  final RxInt rejectedBeneficiaries = 0.obs;
  final RxInt verifiedBeneficiaries = 0.obs;
  final RxInt basicDetails = 0.obs;
  final RxInt physicalExamination = 0.obs;
  final RxInt lungFunctioinTest = 0.obs;
  final RxInt audioScreeningTest = 0.obs;
  final RxInt visionScreening = 0.obs;
  final RxInt sampleCollection = 0.obs; // barcode count — used for validation
  final RxInt ackowledgement = 0.obs;
  final RxInt totalPhysicalExam = 0.obs;
  final RxInt totalLungTest = 0.obs;
  final RxInt totalAudioTest = 0.obs;
  final RxInt totalVisionTest = 0.obs;
  final RxInt totalUrineCount = 0.obs;
  final RxInt totalBene = 0.obs;

  // Remark field (pre-filled from OtherRemark1 in GetCampDetailsCount)
  final TextEditingController remarkTextField = TextEditingController();

  // Camp identity (set by loadData)
  int _campId = 0;
  int _empCode = 0;

  @override
  void onClose() {
    remarkTextField.dispose();
    super.onClose();
  }

  // ─── Load all data ─────────────────────────────────────────────────────────

  Future<void> loadData({
    required int campId,
    required int distLgdCode,
    required String campDate,
  }) async {
    _campId = campId;
    _empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    isLoading.value = true;
    ToastManager.showLoader();

    await Future.wait([
      _loadCampDetailsCount(
        campId: campId,
        distLgdCode: distLgdCode,
        campDate: campDate,
      ),
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
    try {
      final url = Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsCountRegularInCampTest}',
      );
      final ioClient = APIManager.getInstanceOfIo1Client();
      try {
        final response = await ioClient.post(
          url,
          body: {
            'CampId': campId.toString(),
            'DISTLGDCODE': distLgdCode.toString(),
            'FromDate': campDate,
            'ToDate': campDate,
          },
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        );
        final decoded = jsonDecode(response.body) as Map<String, dynamic>;
        final status = (decoded['status'] ?? '').toString().toLowerCase();
        if (status == 'success') {
          final outputRaw = decoded['output'];
          if (outputRaw is List && outputRaw.isNotEmpty) {
            final out = outputRaw.first as Map<String, dynamic>;
            facilitedWorkers.value = _toInt(out['FacilitatedWorkers']);
            approvedBeneficiaries.value = _toInt(out['ApprovedBeneficiaries']);
            rejectedBeneficiaries.value = _toInt(out['RejectedBeneficiaries']);
            basicDetails.value = _toInt(out['BasicDetails']);
            physicalExamination.value = _toInt(out['PhysicalExamination']);
            lungFunctioinTest.value = _toInt(out['LungFunctioinTest']);
            audioScreeningTest.value = _toInt(out['AudioScreeningTest']);
            visionScreening.value = _toInt(out['VisionScreening']);
            sampleCollection.value = _toInt(out['Barcode']);
            ackowledgement.value = _toInt(out['Ackowledgement']);
            verifiedBeneficiaries.value = _toInt(out['VerifiedBeneficiaries']);
            totalPhysicalExam.value = _toInt(out['PhysicalExamination']);
            totalLungTest.value = _toInt(out['LungFunctioinTest']);
            totalAudioTest.value = _toInt(out['AudioScreeningTest']);
            totalVisionTest.value = _toInt(out['VisionScreening']);
            totalUrineCount.value = _toInt(out['UrineSampleCollection']);
            totalBene.value = _toInt(out['FacilitatedWorkers']);
            remarkTextField.text = (out['OtherRemark1'] ?? '').toString();
          }
        }
      } finally {
        ioClient.close();
      }
    } catch (_) {}
  }

  static int _toInt(dynamic v) {
    if (v == null) return 0;
    if (v is int) return v;
    return int.tryParse(v.toString()) ?? 0;
  }

  // ─── GetCampCloseDetails ───────────────────────────────────────────────────
  // Native: if status != "fail" → camp already closed → disable button.

  Future<void> _loadCampCloseDetails({required int campId}) async {
    try {
      final url = Uri.parse(
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetCampCloseDetails}',
      );
      final ioClient = APIManager.getInstanceOfIo1Client();
      try {
        final response = await ioClient.post(
          url,
          body: {'CampId': campId.toString()},
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        );
        final decoded = jsonDecode(response.body) as Map<String, dynamic>;
        final status = (decoded['status'] ?? '').toString().toLowerCase();
        // Non-fail status means camp is already closed → disable button
        if (status != 'fail') {
          isUserInteractionEnabled.value = false;
        }
      } finally {
        ioClient.close();
      }
    } catch (_) {}
  }

  Future<void> _loadConsumables({required int campId}) async {
    try {
      final url = Uri.parse(
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetConsumableListDetails}',
      );
      final ioClient = APIManager.getInstanceOfIo1Client();
      try {
        final response = await ioClient.post(
          url,
          body: {'CampId': campId.toString()},
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        );
        final decoded = jsonDecode(response.body) as Map<String, dynamic>;
        final status = (decoded['status'] ?? '').toString().toLowerCase();
        if (status == 'success') {
          final outputRaw = decoded['output'];
          if (outputRaw is List) {
            final items =
                outputRaw.map((item) {
                  return ConsumableOutput.fromJson(
                    item as Map<String, dynamic>,
                  );
                }).toList();
            consumableCampList.assignAll(items);
          }
        } else {
          consumableCampList.clear();
        }
      } finally {
        ioClient.close();
      }
    } catch (_) {
      consumableCampList.clear();
    }
  }

  // ─── Validation (matches native exactly) ──────────────────────────────────
  // Native only validates consumables — no beneficiary count comparisons.

  bool validate() {
    if (consumableCampList.isEmpty) {
      _alert('Please enter consumable details');
      return false;
    }

    final totalBarcode = sampleCollection.value;
    int totalCountSum = 0;

    for (final item in consumableCampList) {
      final countText = item.textEditingController.text.trim();
      if (countText.isEmpty) {
        _alert(
          'Enter consumed count for ${item.consumableName ?? "consumable"}',
        );
        return false;
      }

      final count = int.tryParse(countText) ?? 0;
      final name = (item.consumableName ?? '').toLowerCase();

      if (name == 'gloves' || name == 'sanitizer bottle') {
        final required = totalBarcode * 2;
        if (count < required) {
          _alert(
            '${item.consumableName} count should be two times of Total Sample Collection!',
          );
          return false;
        }
      } else if (name == 'gel tube') {
        final required = totalBarcode * 2;
        if (count != required) {
          _alert(
            'Gel ट्यूब काउंट हा Rejected नसलेल्या सॅम्पल कलेक्शन झालेल्या लाभार्थ्यांच्या एकूण संख्येच्या दुप्पट असावा.',
          );
          return false;
        }
      } else if (name == 'edta tube') {
        if (count != totalBarcode) {
          _alert(
            'EDTA ट्यूब काउंट हा Rejected नसलेल्या सॅम्पल कलेक्शन झालेल्या लाभार्थ्यांच्या एकूण संख्येएवढा असावा.',
          );
          return false;
        }
      }

      totalCountSum += count;
    }

    if (totalCountSum == 0) {
      _alert('All consumable counts cannot be zero');
      return false;
    }

    return true;
  }

  // ─── Confirmation dialog + submit ──────────────────────────────────────────

  void onCloseCampTap() {
    if (!validate()) return;
    ToastManager().showConfirmationDialog(
      context: Get.context!,
      message:
          'कॅम्प closing confirmation देण्यापूर्वी Beneficiary आणि ट्यूबची माहिती बरोबर असल्याची खात्री करा.',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Get.back();
          _submitCampClosing();
        } else if (p1 == false) {
          Get.back();
        }
      },
    );
    // showDialog<void>(
    //   context: Get.context!,
    //   builder:
    //       (_) => AlertDialog(
    //         title: const Text('Confirm!'),
    //         content: const Text(
    //           'कॅम्प closing confirmation देण्यापूर्वी Beneficiary आणि ट्यूबची माहिती बरोबर असल्याची खात्री करा.',
    //         ),
    //         actions: [
    //           TextButton(onPressed: () => Get.back(), child: const Text('No')),
    //           TextButton(
    //             onPressed: () {
    //               Get.back();
    //               _submitCampClosing();
    //             },
    //             child: const Text('Yes'),
    //           ),
    //         ],
    //       ),
    // );
  }

  // ─── InsertCampClosingConfirmation ─────────────────────────────────────────

  Future<void> _submitCampClosing() async {
    // Build consumable JSON array
    final consumableJson =
        consumableCampList.map((item) {
          return {
            'ConsumableId': item.consumableID,
            'TotalCount': item.textEditingController.text.trim(),
            'CreatedBy': item.createdBy,
          };
        }).toList();

    ToastManager.showLoader();
    try {
      final url = Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kInsertCampClosingConfirmation}',
      );
      final ioClient = APIManager.getInstanceOfIo1Client();
      try {
        final response = await ioClient.post(
          url,
          body: {
            'CampID': _campId.toString(),
            'CampCloseUserid': _empCode.toString(),
            'JsonConsumableDetails': jsonEncode(consumableJson),
            'OtherRemark': '',
            'TotalBenificiary': '0',
            'SampleCollectionCount': '0',
            'SampleSendToHubLabCount': '0',
            'SampleSendToHomeLabCount': '0',
            'OtherRemarkSummary': remarkTextField.text.trim(),
          },
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        );

        final decoded = jsonDecode(response.body) as Map<String, dynamic>;
        final status = (decoded['status'] ?? '').toString();

        if (!status.toLowerCase().contains('fail')) {
          ToastManager.showSuccessPopup(
            Get.context!,
            icSuccessIcon,
            "Camp confirmation done submitted successfully",
          );
          // _showSuccessAndFinish();
        } else {
          final exMsg =
              decoded['ExceptionValue'] ?? decoded['message'] ?? 'Failed';
          _alert(exMsg.toString());
        }
      } finally {
        ioClient.close();
      }
    } catch (e) {
      _alert('Server error. Please try again.');
    } finally {
      ToastManager.hideLoader();
    }
  }

  void _showSuccessAndFinish() {
    showDialog<void>(
      context: Get.context!,
      barrierDismissible: false,
      builder:
          (_) => AlertDialog(
            title: const Text('Success'),
            content: const Text(
              'Camp confirmation done submitted successfully',
            ),
            actions: [
              TextButton(
                onPressed: () {
                  Get.back(); // close dialog
                  Get.back(); // close CampClosingScreen
                },
                child: const Text('Okay'),
              ),
            ],
          ),
    );
  }

  void _alert(String message) {
    ToastManager.showAlertDialog(Get.context!, message, () => Get.back());
  }
}
