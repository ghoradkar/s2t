// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Screens/team_photos/model/attendance_details_response.dart';
import 'package:s2toperational/Screens/team_photos/model/attendance_image_response.dart';
import 'package:s2toperational/Screens/team_photos/model/camp_list_response.dart';
import 'package:s2toperational/Screens/team_photos/model/teams_details_response.dart';

class TeamPhotosRepository {
  final _api = APIManager();

  // ─── Get team members attendance details ─────────────────────────────────────

  Future<AttendanceDetailsResponse?> getAttendanceDetails({
    required String campDate,
    required String teamId,
    required String campId,
    required String statusId,
  }) async {
    final completer = Completer<AttendanceDetailsResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetTeamMembersAttendanceDetailsCampIDWise}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'CampDATE': campDate,
          'TeamID': teamId,
          'CampID': campId,
          'StatusID': statusId,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = AttendanceDetailsResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  // ─── Get camp attendance images ───────────────────────────────────────────────

  Future<AttendanceImageResponse?> getCampImages({
    required String campId,
  }) async {
    final completer = Completer<AttendanceImageResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetCampAttendanceImages}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'CampID': campId},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = AttendanceImageResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  // ─── Get user-wise camp list ──────────────────────────────────────────────────

  Future<CampListResponse?> getCampList({
    required String userId,
    required String campDate,
    required String campType,
  }) async {
    final completer = Completer<CampListResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetUserWiseCampList}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'UserId': userId,
          'CampDate': campDate,
          'CampType': campType,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = CampListResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  // ─── Get assigned team details (D2D / MMU) ────────────────────────────────────

  Future<TeamsDetailsResponse?> getTeamDetails({
    required String campId,
    required String campDate,
    required String campType,
  }) async {
    final completer = Completer<TeamsDetailsResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetAssignedTeamDetailsFlexi}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'campid': campId,
          'CampDate': campDate,
          'CampType': campType,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = TeamsDetailsResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  // ─── Auto-resolve team for DESGID 35 in D2D ─────────────────────────────────

  /// Calls GetTeamNumberByCampIdAndUSerId (used for DESGID 35 in D2D).
  /// Returns the first output item which has TeamNumber + TeamName.
  Future<TeamsDetailsOutput?> getTeamNumberByUser({
    required String campId,
    required String userId,
  }) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetTeamNumberByCampIdAndUSerId}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'campid': campId, 'UserID': userId},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = TeamsDetailsResponse.fromJson(decoded);
      if (result.status?.toLowerCase() == 'success') {
        return result.output?.firstOrNull;
      }
      return null;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }

  // ─── Upload check-in or check-out photo ──────────────────────────────────────

  /// [statusId]: "1" = check-in, "2" = check-out
  Future<bool> uploadPhoto({
    required String campId,
    required String userId,
    required String statusId,
    required File photoFile,
  }) async {
    final url = Uri.parse(APIManager.kCampAttendancePhotoHandler);

    final HttpClient httpClient = HttpClient()
      ..badCertificateCallback =
          (X509Certificate cert, String host, int port) => true;
    final ioClient = APIManager.getInstanceOfIo1Client();

    try {
      final request = http.MultipartRequest('POST', url);
      request.fields['CampID'] = campId;
      request.fields['UserId'] = userId;
      request.fields['StatusID'] = statusId;

      final fieldName = statusId == '1' ? 'InImagePath' : 'OutImagePath';
      final ext = photoFile.path.toLowerCase().endsWith('.png') ? 'png' : 'jpg';
      final timestamp = DateTime.now().millisecondsSinceEpoch;
      final uploadFileName =
          statusId == '1' ? '${timestamp}_IN.jpg' : '${timestamp}_OT.png';

      request.files.add(
        await http.MultipartFile.fromPath(
          fieldName,
          photoFile.path,
          filename: uploadFileName,
        ),
      );

      final streamed = await ioClient.send(request);
      final response = await http.Response.fromStream(streamed);

      if (response.statusCode == 200) {
        final decoded = json.decode(response.body);
        return (decoded['status']?.toString().toLowerCase() == 'success');
      }
      return false;
    } catch (_) {
      return false;
    } finally {
      ioClient.close();
    }
  }
}
