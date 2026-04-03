// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/AttendanceDetailsResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/AttendanceImageResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/CampListResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/repository/team_photos_repository.dart';

class TeamPhotosController extends GetxController {
  final TeamPhotosRepository _repository = TeamPhotosRepository();

  // ─── User info ────────────────────────────────────────────────────────────
  int dESGID = 0;
  int empCode = 0;

  // ─── Observables ─────────────────────────────────────────────────────────
  final selectedDate = ''.obs;
  final selectedCamp = Rxn<CampListOutput>();
  final campList = <CampListOutput>[].obs;
  final attendanceList = <AttendanceDetailsOutput>[].obs;

  final inPhotoLocalPath = ''.obs;
  final outPhotoLocalPath = ''.obs;
  final inPhotoServerUrl = ''.obs;
  final outPhotoServerUrl = ''.obs;

  // isMarkInOut: "0"=nothing uploaded, "1"=check-in done, "2"=both done
  final isMarkInOut = '0'.obs;

  final isCampListLoading = false.obs;
  final isAttendanceLoading = false.obs;
  final isUploading = false.obs;

  // ─── Lifecycle ────────────────────────────────────────────────────────────
  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    dESGID = user?.dESGID ?? 0;
    empCode = user?.empCode ?? 0;
    selectedDate.value = FormatterManager.formatDateToString(DateTime.now());
  }

  // ─── Camp list ────────────────────────────────────────────────────────────
  Future<void> loadCampList() async {
    isCampListLoading.value = true;
    campList.clear();
    selectedCamp.value = null;
    clearPhotos();

    final params = {
      'UserId': empCode.toString(),
      'CampDate': selectedDate.value,
      'DesgID': dESGID.toString(),
    };
    final response = await _repository.getUserWiseCampList(params);
    isCampListLoading.value = false;

    if (response?.output != null) {
      campList.assignAll(response!.output!);
    } else {
      ToastManager.toast('Could not load camp list');
    }
  }

  // ─── Date picker ─────────────────────────────────────────────────────────
  Future<void> selectDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2020),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      selectedDate.value = FormatterManager.formatDateToString(picked);
      await loadCampList();
    }
  }

  // ─── Camp selection ───────────────────────────────────────────────────────
  Future<void> onCampSelected(CampListOutput camp) async {
    selectedCamp.value = camp;
    clearPhotos();
    await _loadAttendanceAndImages();
  }

  Future<void> _loadAttendanceAndImages() async {
    final camp = selectedCamp.value;
    if (camp == null) return;

    isAttendanceLoading.value = true;
    attendanceList.clear();

    final params = {
      'CampDATE': selectedDate.value,
      'TeamID': '0',
      'CampID': camp.campId.toString(),
      'StatusID': '0',
    };

    final results = await Future.wait([
      _repository.getAttendanceDetails(params),
      _repository.getCampImages({'CampID': camp.campId.toString()}),
    ]);

    isAttendanceLoading.value = false;

    final attendanceResponse = results[0] as AttendanceDetailsResponse?;
    final imageResponse = results[1] as AttendanceImageResponse?;

    if (attendanceResponse?.output != null) {
      attendanceList.assignAll(attendanceResponse!.output!);
    }

    if (imageResponse?.output != null && imageResponse!.output!.isNotEmpty) {
      final img = imageResponse.output!.first;
      if (img.hasInImage) {
        inPhotoServerUrl.value = img.inImage!;
        isMarkInOut.value = '1';
      }
      if (img.hasOutImage) {
        outPhotoServerUrl.value = img.outImage!;
        isMarkInOut.value = '2';
      }
    }
  }

  // ─── Photo capture ────────────────────────────────────────────────────────
  Future<void> captureCheckIn() async {
    final path = await _pickPhoto();
    if (path != null) {
      inPhotoLocalPath.value = path;
    }
  }

  Future<void> captureCheckOut() async {
    final path = await _pickPhoto();
    if (path != null) {
      outPhotoLocalPath.value = path;
    }
  }

  Future<String?> _pickPhoto() async {
    final picker = ImagePicker();
    final xFile = await picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    return xFile?.path;
  }

  // ─── Upload ───────────────────────────────────────────────────────────────
  bool get canUploadCheckIn =>
      inPhotoLocalPath.value.isNotEmpty && isMarkInOut.value == '0';

  bool get canUploadCheckOut =>
      outPhotoLocalPath.value.isNotEmpty && isMarkInOut.value == '1';

  bool get allAttendanceMarkedIn =>
      attendanceList.isNotEmpty &&
      attendanceList.every((m) => !m.isInPending);

  bool get allAttendanceMarkedOut =>
      attendanceList.isNotEmpty &&
      attendanceList.every((m) => !m.isOutPending);

  Future<void> uploadCheckIn() async {
    if (selectedCamp.value == null) {
      ToastManager.toast('Please select a camp');
      return;
    }
    if (inPhotoLocalPath.value.isEmpty) {
      ToastManager.toast('Please capture check-in photo first');
      return;
    }
    if (!allAttendanceMarkedIn) {
      ToastManager.toast(
        'All team members must mark attendance before uploading check-in photo',
      );
      return;
    }

    isUploading.value = true;
    final success = await _repository.uploadPhoto(
      fields: {
        'CampID': selectedCamp.value!.campId.toString(),
        'UserId': empCode.toString(),
      },
      photoFile: File(inPhotoLocalPath.value),
      statusId: '1',
    );
    isUploading.value = false;

    if (success) {
      isMarkInOut.value = '1';
      ToastManager.toast('Check-in photo uploaded successfully');
      await _loadAttendanceAndImages();
    } else {
      ToastManager.toast('Upload failed. Please try again.');
    }
  }

  Future<void> uploadCheckOut() async {
    if (selectedCamp.value == null) {
      ToastManager.toast('Please select a camp');
      return;
    }
    if (outPhotoLocalPath.value.isEmpty) {
      ToastManager.toast('Please capture check-out photo first');
      return;
    }
    if (!allAttendanceMarkedOut) {
      ToastManager.toast(
        'All team members must mark attendance before uploading check-out photo',
      );
      return;
    }

    isUploading.value = true;
    final success = await _repository.uploadPhoto(
      fields: {
        'CampID': selectedCamp.value!.campId.toString(),
        'UserId': empCode.toString(),
      },
      photoFile: File(outPhotoLocalPath.value),
      statusId: '2',
    );
    isUploading.value = false;

    if (success) {
      isMarkInOut.value = '2';
      ToastManager.toast('Check-out photo uploaded successfully');
      await _loadAttendanceAndImages();
    } else {
      ToastManager.toast('Upload failed. Please try again.');
    }
  }

  void clearPhotos() {
    inPhotoLocalPath.value = '';
    outPhotoLocalPath.value = '';
    inPhotoServerUrl.value = '';
    outPhotoServerUrl.value = '';
    isMarkInOut.value = '0';
    attendanceList.clear();
  }
}
