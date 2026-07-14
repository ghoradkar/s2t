// ignore_for_file: avoid_print

import 'dart:io';
import 'package:permission_handler/permission_handler.dart';

class PermissionService {
  Future<bool> requestPermissionsAll() async {
    bool photoOk = await PermissionService.requestPhotosPermission();
    bool cameraOk = await PermissionService.requestCameraPermission();
    bool storageOk = await PermissionService.requestStoragePermission();

    if (Platform.isIOS) {
      if (photoOk && cameraOk) {
        print("All permissions granted.");
        return true;
      } else {
        print("Some permissions denied.");
        return false;
      }
    } else {
      if (photoOk && cameraOk && storageOk) {
        print("All permissions granted.");
        return true;
      } else {
        print("Some permissions denied.");
        return false;
      }
    }
  }

  /// Request camera permission
  static Future<bool> requestCameraPermission() async {
    return _requestPermission(Permission.camera);
  }

  /// Request photos/gallery permission
  static Future<bool> requestPhotosPermission() async {
    if (Platform.isIOS) {
      return _requestPermission(Permission.photos);
    } else {
      // Android - request storage or media access depending on API level
      return requestStoragePermission();
    }
  }

  /// Request storage permission for Android
  static Future<bool> requestStoragePermission() async {
    if (Platform.isIOS) {
      return true; // iOS handles it via photos permission
    }

    if (await Permission.manageExternalStorage.isGranted) {
      return true;
    }

    if (await Permission.manageExternalStorage.isDenied ||
        await Permission.manageExternalStorage.isRestricted) {
      return _requestPermission(Permission.manageExternalStorage);
    }

    return _requestPermission(Permission.storage);
  }

  /// Generic method to request any permission
  static Future<bool> _requestPermission(Permission permission) async {
    final status = await permission.request();

    if (status == PermissionStatus.granted) {
      return true;
    } else if (status == PermissionStatus.permanentlyDenied) {
      await openAppSettings();
    }

    return false;
  }
}

// import 'package:abkat_flutter/core/utilities/device_utilities.dart';
// import 'package:permission_handler/permission_handler.dart';

// class PermissionService {
//   Future<bool> requestPermissions() async {
//     var deviceInfo = await DeviceInfoUtil().getDeviceInfo();
//     if (deviceInfo['version']['sdkInt'] > 32) {
//       Map<Permission, PermissionStatus> statuses = await [
//         Permission.photos,
//         Permission.camera,
//       ].request();

//       // Check the status of each permission
//       if (statuses[Permission.camera]!.isGranted &&
//           statuses[Permission.photos]!.isGranted) {
//         return true;
//       } else {
//         print("Permissions denied");
//         return false;
//       }
//     } else {
//       Map<Permission, PermissionStatus> statuses = await [
//         Permission.storage,
//         Permission.camera,
//       ].request();

//       // Check the status of each permission
//       if (statuses[Permission.storage]!.isGranted &&
//           statuses[Permission.camera]!.isGranted) {
//         return true;
//       } else {
//         print("Permissions denied");
//         return false;
//       }
//     }
//   }

//   Future<bool> hasPhonePermission() async {
//     return hasPermission(Permission.phone);
//   }

//   Future<bool> hasPermission(Permission permission) async {
//     var permissionStatus = await permission.status;
//     return permissionStatus == PermissionStatus.granted;
//   }

//   Future<bool> hasAllPermission() async {
//     var deviceInfo = await DeviceInfoUtil().getDeviceInfo();

//     if (deviceInfo['version']['sdkInt'] > 32) {
//       var photosStatus = await Permission.photos.status;
//       var cameraStatus = await Permission.camera.status;
//       if (photosStatus.isGranted && cameraStatus.isGranted) {
//         return true;
//       } else {
//         return false;
//       }
//     } else {
//       var storageStatus = await Permission.storage.status;
//       var cameraStatus = await Permission.camera.status;

//       if (storageStatus.isGranted && cameraStatus.isGranted) {
//         return true;
//       } else {
//         return false;
//       }
//     }
//   }
// }
