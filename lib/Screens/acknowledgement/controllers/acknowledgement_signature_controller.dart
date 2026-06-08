import 'dart:io';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';
import 'package:s2toperational/Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import 'package:s2toperational/Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/acknowledgement/repository/acknowledgement_repository.dart';
import 'package:signature/signature.dart';

class AcknowledgementSignatureController extends GetxController {
  final AcknowledgementRepository _repo = AcknowledgementRepository();

  final RxBool isLoading = false.obs;
  final RxBool isNoThumbDevice = false.obs;

  // Thumb capture
  final Rxn<File> thumbFile = Rxn<File>();

  // Audio download
  final RxBool isDownloadingAudio = false.obs;
  final RxBool audioDownloaded = false.obs;

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
      penStrokeWidth: 2,
      penColor: Colors.black,
      exportBackgroundColor: Colors.white,
      exportPenColor: Colors.black,
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
    thumbFile.value = null;
    isDownloadingAudio.value = false;
    audioDownloaded.value = false;
    _downloadAudioIfNeeded();
  }

  Future<void> _downloadAudioIfNeeded() async {
    final audioUrl = patient.audioImage ?? '';
    if (audioUrl.isEmpty) return;
    isDownloadingAudio.value = true;
    try {
      final response = await http.get(Uri.parse(audioUrl));
      if (response.statusCode == 200 && response.bodyBytes.isNotEmpty) {
        final dir = await getTemporaryDirectory();
        final filename = Uri.parse(audioUrl).pathSegments.last;
        final file = File('${dir.path}/$filename');
        await file.writeAsBytes(response.bodyBytes);
        audioDownloaded.value = true;
      }
    } catch (_) {
      audioDownloaded.value = false;
    } finally {
      isDownloadingAudio.value = false;
    }
  }

  Future<void> captureThumb() async {
    final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
    if (result != null) {
      // Rename to match native: {regdId}_1_{empCode}_FP.jpg
      final dir = await getTemporaryDirectory();
      final namedFile = File(
        '${dir.path}/${patient.regdId ?? 0}_1_${empCode}_FP.jpg',
      );
      await namedFile.writeAsBytes(await result.file.readAsBytes());
      thumbFile.value = namedFile;
    }
  }

  void toggleNoThumbDevice(bool value) {
    isNoThumbDevice.value = value;
    if (value) thumbFile.value = null;
  }

  void clearSignature() {
    signatureController.clear();
  }

  Future<void> saveSignature() async {
    if (!signatureController.isNotEmpty) {
      print('[AckSign] Save rejected: signature pad is empty');
      ToastManager.toast('Please draw your signature first');
      return;
    }

    isLoading.value = true;
    print('[AckSign] Starting save — RegdId=${patient.regdId} SiteId=${patient.siteId} CampId=$campId CreatedBy=$empCode thumbFile=${thumbFile.value?.path}');

    try {
      final ui.Image? image = await signatureController.toImage();
      if (image == null) {
        print('[AckSign] Error: toImage() returned null');
        ToastManager.toast('Failed to capture signature');
        isLoading.value = false;
        return;
      }

      final byteData =
          await image.toByteData(format: ui.ImageByteFormat.png);
      if (byteData == null) {
        print('[AckSign] Error: toByteData() returned null');
        ToastManager.toast('Failed to process signature');
        isLoading.value = false;
        return;
      }

      final dir = await getTemporaryDirectory();
      // Match native naming: {regdId}_0_{campId}_SG.png
      final file = File(
        '${dir.path}/${patient.regdId ?? 0}_0_${campId}_SG.png',
      );
      await file.writeAsBytes(byteData.buffer.asUint8List());
      print('[AckSign] Signature file saved: ${file.path}');
      print('[AckSign] Uploading to InsertSignatureandThumbDetails_V1_RC.ashx');
      print('[AckSign] Params: RegdId=${patient.regdId} SiteId=${patient.siteId} CampId=$campId IsSignature=1 CreatedBy=$empCode IsDeviceIssue=1 BocwIdDepend=${patient.bocwIdDepend ?? 0}');
      print('[AckSign] UploadedPath=${file.path}');
      print('[AckSign] ThaumbPath=${thumbFile.value?.path ?? "null"}');

      final result = await _repo.uploadSignature(
        regdId: patient.regdId?.toString() ?? '',
        siteId: patient.siteId?.toString() ?? '0',
        campId: campId.toString(),
        empCode: empCode.toString(),
        isDeviceAvailable: true,
        bocwIdDepend: patient.bocwIdDepend ?? 0,
        signatureFile: file,
        thumbFile: thumbFile.value,
      );

      isLoading.value = false;
      print('[AckSign] Response: $result');

      if (result != null &&
          result['status']?.toString().toLowerCase() == 'success') {
        print('[AckSign] SUCCESS');
        ToastManager.toast('Patient signature saved successfully');
        Get.back();
        Get.back();
      } else {
        final msg =
            result?['message']?.toString() ?? 'Failed to save signature';
        print('[AckSign] FAILED: $msg');
        ToastManager.toast(msg);
      }
    } catch (e) {
      isLoading.value = false;
      print('[AckSign] Exception: $e');
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
