// ignore_for_file: file_names

import 'dart:async';
import 'dart:io';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/AttendanceDetailsResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/AttendanceImageResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/CampListResponse.dart';

class TeamPhotosRepository {
  final APIManager _api = APIManager();

  Future<CampListResponse?> getUserWiseCampList(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<CampListResponse?>();
    await _api.getUserWiseCampListAPI(params, (data, msg, success) {
      if (success && data != null) {
        c.complete(CampListResponse.fromJson(data));
      } else {
        c.complete(null);
      }
    });
    return c.future;
  }

  Future<AttendanceDetailsResponse?> getAttendanceDetails(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<AttendanceDetailsResponse?>();
    await _api.getTeamMembersAttendanceDetailsCampIDWiseAPI(
      params,
      (data, msg, success) {
        if (success && data != null) {
          c.complete(AttendanceDetailsResponse.fromJson(data));
        } else {
          c.complete(null);
        }
      },
    );
    return c.future;
  }

  Future<AttendanceImageResponse?> getCampImages(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<AttendanceImageResponse?>();
    await _api.getCampAttendanceImagesAPI(params, (data, msg, success) {
      if (success && data != null) {
        c.complete(AttendanceImageResponse.fromJson(data));
      } else {
        c.complete(null);
      }
    });
    return c.future;
  }

  Future<bool> uploadPhoto({
    required Map<String, String> fields,
    required File photoFile,
    required String statusId,
  }) async {
    final c = Completer<bool>();
    await _api.uploadCampAttendancePhotoAPI(
      fields: fields,
      photoFile: photoFile,
      statusId: statusId,
      callback: (data, msg, success) {
        c.complete(success == true);
      },
    );
    return c.future;
  }
}
