// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/patient_registration/repository/regular_patient_registration_repository.dart';

class RationCardPhotoController extends GetxController {
  final String regdId;
  final String dependentBocId;
  final String rationCardNumber;

  /// Callback invoked after all photos are uploaded successfully — used to
  /// clear the parent registration form and pop back to the start.
  final VoidCallback? onSuccess;

  RationCardPhotoController({
    required this.regdId,
    required this.dependentBocId,
    required this.rationCardNumber,
    this.onSuccess,
  });

  final _repo = RegularPatientRegistrationRepository();

  // 'manual' = max 2 photos required, 'digital' = max 1 photo required
  final selectedType = 'manual'.obs;
  final photos = <File>[].obs;
  final isUploading = false.obs;

  int get maxPhotos => selectedType.value == 'digital' ? 1 : 2;
  int get minPhotos => selectedType.value == 'digital' ? 1 : 2;

  void selectType(String type) {
    selectedType.value = type;
    // Clear photos when switching so limits are re-evaluated
    photos.clear();
  }

  Future<void> capturePhoto(BuildContext context) async {
    if (photos.length >= maxPhotos) {
      ToastManager.toast('Maximum $maxPhotos photo(s) allowed');
      return;
    }
    final picker = ImagePicker();
    final picked = await picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) {
      photos.add(File(picked.path));
    }
  }

  void removePhoto(int index) {
    if (index >= 0 && index < photos.length) {
      photos.removeAt(index);
    }
  }

  Future<void> uploadPhotos(BuildContext context) async {
    if (photos.length < minPhotos) {
      ToastManager.showAlertDialog(
        context,
        'Please capture at least $minPhotos photo(s) for '
        '${selectedType.value == "digital" ? "Digital" : "Manual"} ration card',
        () => Get.back(),
      );
      return;
    }

    final user = DataProvider().getParsedUserData()?.output?.first;
    final empCode = (user?.empCode ?? 0).toString();

    isUploading.value = true;
    try {
      for (final photo in photos) {
        final result = await _repo.insertRationCardDetails(
          regdId: regdId,
          empCode: empCode,
          bocwDependentId: dependentBocId,
          rationCardNo: rationCardNumber,
          photoFile: photo,
        );

        if (result == null ||
            (result['status']?.toString().toLowerCase() != 'success')) {
          if (!context.mounted) return;
          ToastManager.showAlertDialog(
            context,
            result?['message']?.toString() ??
                'Failed to upload ration card photo. Please try again.',
            () => Get.back(),
          );
          return;
        }
      }

      if (!context.mounted) return;
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        'Ration card photos uploaded successfully',
        () {
          onSuccess?.call();
          // Pop ration card screen + signature screen + fingerprint screen
          Navigator.of(context)
            ..pop()
            ..pop()
            ..pop()
            ..pop();
        },
      );
    } finally {
      isUploading.value = false;
    }
  }
}
