// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';
import 'package:s2toperational/Screens/user_attendance/model/camp_location_response.dart';
import 'package:s2toperational/Screens/user_attendance/model/user_attandance_response.dart';

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

  /// Checkout API — POST InsertUserInOutAttendance on D2D base URL
  Future<Map<String, dynamic>?> insertUserCheckout({
    required int userId,
    required double latitude,
    required double longitude,
    required String attendanceDate,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}InsertUserInOutAttendance';
      final body = {
        'Userid': userId.toString(),
        'LATITUDE': latitude.toString(),
        'LONGITUDE': longitude.toString(),
        'ATTENDANCEDATE': attendanceDate,
      };
      debugPrint('insertUserCheckout URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('insertUserCheckout raw: ${response.body}');
      return json.decode(response.body) as Map<String, dynamic>;
    } catch (e) {
      debugPrint('insertUserCheckout error: $e');
      return null;
    }
  }

  /// GET GetCampLocationDetails?UserId=X on D2D base URL
  Future<CampLocationOutput?> getCampLocation({required int userId}) async {
    try {
      final uri = Uri.parse(
        '${APIManager.kD2DBaseURL}GetCampLocationDetails?UserId=$userId',
      );
      debugPrint('getCampLocation URL: $uri');
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      debugPrint('getCampLocation raw: $body');
      final data = json.decode(body) as Map<String, dynamic>;
      final parsed = CampLocationResponse.fromJson(data);
      if (parsed.status?.toLowerCase() == 'success' &&
          parsed.output != null &&
          parsed.output!.isNotEmpty) {
        return parsed.output!.first;
      }
    } catch (e) {
      debugPrint('getCampLocation error: $e');
    }
    return null;
  }

  /// GET CheckCampTestStatusOfBeneficiaries?UserId=X on D2D base URL
  Future<String> getTestCompleteFlag({required int userId}) async {
    try {
      final uri = Uri.parse(
        '${APIManager.kD2DBaseURL}CheckCampTestStatusOfBeneficiaries?UserId=$userId',
      );
      debugPrint('getTestCompleteFlag URL: $uri');
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      debugPrint('getTestCompleteFlag raw: $body');
      final data = json.decode(body) as Map<String, dynamic>;
      final status = data['status'] as String? ?? '';
      if (status.toLowerCase() == 'success') {
        final output = (data['output'] as List?)?.firstOrNull;
        return output?['TestFlag']?.toString() ?? '0';
      }
      return '1'; // default: block checkout
    } catch (e) {
      debugPrint('getTestCompleteFlag error: $e');
      return '1'; // default: block checkout on error
    }
  }
}