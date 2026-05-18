import 'dart:io';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:path_provider/path_provider.dart';
import 'package:s2toperational/Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/acknowledgement/repository/acknowledgement_repository.dart';
import 'package:signature/signature.dart';

class AcknowledgementSignatureController extends GetxController {
  final AcknowledgementRepository _repo = AcknowledgementRepository();

  final RxBool isLoading = false.obs;
  final RxBool isNoThumbDevice = false.obs;

  late AcknowledgementPatientOutput patient;
  late int campId;

  int empCode = 0;

  late SignatureController signatureController;

  @override
  void onInit() {
    super.onInit();
    empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    signatureController = SignatureController(
      penStrokeWidth: 3,
      penColor: Colors.black,
      exportBackgroundColor: Colors.white,
    );
  }

  @override
  void onClose() {
    signatureController.dispose();
    super.onClose();
  }

  void initPatient({
    required AcknowledgementPatientOutput patient,
    required int campId,
  }) {
    this.patient = patient;
    this.campId = campId;
  }

  void toggleNoThumbDevice(bool value) {
    isNoThumbDevice.value = value;
  }

  void clearSignature() {
    signatureController.clear();
  }

  Future<void> saveSignature() async {
    if (!signatureController.isNotEmpty) {
      ToastManager.toast('Please draw your signature first');
      return;
    }

    isLoading.value = true;

    try {
      final ui.Image? image = await signatureController.toImage();
      if (image == null) {
        ToastManager.toast('Failed to capture signature');
        isLoading.value = false;
        return;
      }

      final byteData =
          await image.toByteData(format: ui.ImageByteFormat.png);
      if (byteData == null) {
        ToastManager.toast('Failed to process signature');
        isLoading.value = false;
        return;
      }

      final dir = await getTemporaryDirectory();
      final file = File(
        '${dir.path}/${patient.regdId ?? 0}_${campId}_s.png',
      );
      await file.writeAsBytes(byteData.buffer.asUint8List());

      final result = await _repo.uploadSignature(
        regdId: patient.regdId?.toString() ?? '',
        siteId: patient.siteId?.toString() ?? '0',
        campId: campId.toString(),
        empCode: empCode.toString(),
        isDeviceAvailable: !isNoThumbDevice.value,
        signatureFile: file,
      );

      isLoading.value = false;

      if (result != null &&
          result['status']?.toString().toLowerCase() == 'success') {
        ToastManager.toast('Patient signature saved successfully');
        Get.back();
        Get.back();
      } else {
        final msg =
            result?['message']?.toString() ?? 'Failed to save signature';
        ToastManager.toast(msg);
      }
    } catch (e) {
      isLoading.value = false;
      ToastManager.toast('Error: $e');
    }
  }

  String get genderLabel {
    switch (patient.gender?.toUpperCase()) {
      case 'M':
        return 'Male';
      case 'F':
        return 'Female';
      default:
        return 'Other';
    }
  }
}