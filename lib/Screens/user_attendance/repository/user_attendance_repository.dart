// ignore_for_file: file_names

import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/user_attendance/Model/user_attandance_response.dart';

class UserAttendanceRepository {
  final APIManager _apiManager = APIManager();

  Future<UserAttandanceResponse?> getUserAttendance(
    Map<String, String> params,
  ) {
    final completer = Completer<UserAttandanceResponse?>();
    _apiManager.getUserAttendanceAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }

  Future<UserAttandanceResponse?> insertUserAttendance(
    Map<String, String> params,
  ) {
    final completer = Completer<UserAttandanceResponse?>();
    _apiManager.getInsertUserAttendancAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }
}