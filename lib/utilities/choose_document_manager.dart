// ignore_for_file: file_names

import 'dart:io';
import 'package:file_picker/file_picker.dart';
import 'package:image_picker/image_picker.dart';
import 'package:permission_handler/permission_handler.dart';

enum FileSourceType { camera, gallery, pdf }

class ChosenFileResult {
  final File file;
  final String fileType; // e.g. image, pdf

  ChosenFileResult({required this.file, required this.fileType});
}

class ChooseDocumentManager {
  static final ImagePicker _picker = ImagePicker();

  static Future<ChosenFileResult?> pickFile(
    FileSourceType sourceType, {
    bool preferFrontCamera = false,
  }) async {
    switch (sourceType) {
      case FileSourceType.camera:
        return await _pickImageFromCamera(preferFrontCamera: preferFrontCamera);
      case FileSourceType.gallery:
        return await _pickImageFromGallery();
      case FileSourceType.pdf:
        return await _pickPdf();
    }
  }

  static Future<ChosenFileResult?> _pickImageFromCamera({
    bool preferFrontCamera = false,
  }) async {
    final device = preferFrontCamera ? CameraDevice.front : CameraDevice.rear;
    var cameraStatus = await Permission.camera.status;

    if (Platform.isAndroid) {
      if (cameraStatus.isDenied) {
        await Permission.camera.request();
      } else {
        final pickedFile = await _picker.pickImage(
          source: ImageSource.camera,
          preferredCameraDevice: device,
        );
        if (pickedFile != null) {
          return ChosenFileResult(
            file: File(pickedFile.path),
            fileType: "image",
          );
        }
      }
    } else {
      final pickedFile = await _picker.pickImage(
        source: ImageSource.camera,
        preferredCameraDevice: device,
      );
      if (pickedFile != null) {
        return ChosenFileResult(file: File(pickedFile.path), fileType: "image");
      }
    }

    return null;
  }

  static Future<ChosenFileResult?> _pickImageFromGallery() async {
    if (Platform.isAndroid) {
      final permissionStatus = await Permission.photos.request();
      if (!permissionStatus.isGranted) return null;

      final pickedFile = await _picker.pickImage(source: ImageSource.gallery);
      if (pickedFile != null) {
        return ChosenFileResult(file: File(pickedFile.path), fileType: "image");
      }
    } else {
      final pickedFile = await _picker.pickImage(source: ImageSource.gallery);
      if (pickedFile != null) {
        return ChosenFileResult(file: File(pickedFile.path), fileType: "image");
      }
    }

    return null;
  }

  static Future<ChosenFileResult?> _pickPdf() async {
    final result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['pdf'],
    );

    if (result != null && result.files.single.path != null) {
      return ChosenFileResult(
        file: File(result.files.single.path!),
        fileType: "pdf",
      );
    }
    return null;
  }
}
