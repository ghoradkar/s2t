import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';
// import 'package:s2toperational/Screens/health_screening_details/models/camp_d2d_model.dart';
// import 'package:s2toperational/Screens/health_screening_details/models/camp_regular_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

import '../models/camp_closing_model.dart';
import '../models/camp_d2d_model.dart';
import '../models/camp_regular_model.dart';

class HealthScreeningRepository {
  final APIManager _apiManager = APIManager();

  // ─── Camp Closing ──────────────────────────────────────────────────────────

  Future<CampCloseCampDetailsResponse?> getCampDetailsCount({
    required int campId,
    required int distLgdCode,
    required String campDate,
  }) async {
    CampCloseCampDetailsResponse? result;
    await _apiManager.getCampDetailsCountAPI(
      {
        "CampId": campId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
        "FromDate": campDate,
        "ToDate": campDate,
      },
      (CampCloseCampDetailsResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<CampCloseDetailsResponse?> getCampCloseDetails({
    required int campId,
  }) async {
    CampCloseDetailsResponse? result;
    await _apiManager.getCampCloseDetailsAPI(
      {"CampId": campId.toString()},
      (CampCloseDetailsResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<ConsumableListDetailsResponse?> getConsumableListDetails({
    required int campId,
  }) async {
    ConsumableListDetailsResponse? result;
    await _apiManager.getConsumableListDetailsAPI(
      {"CampId": campId.toString()},
      (ConsumableListDetailsResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  // ─── Camp D2D ──────────────────────────────────────────────────────────────

  Future<CampDetailsonLabForDoorToDoorResponse?> getCampDetailsForD2D({
    required String campDate,
    required int labCode,
    required int subOrgId,
    required int distLgdCode,
    required int userId,
    required int dESGID,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsonLabForDoorToDoorV2}';
      final body = {
        'CampDate': campDate,
        'LabCode': '0',
        'SubOrgId': subOrgId.toString(),
        'Divison': '0',
        'DISTLGDCODE': distLgdCode.toString(),
        'USERID': userId.toString(),
        'DesgId': dESGID.toString(),
      };
      debugPrint('getCampDetailsForD2D URL: $uri');
      debugPrint('getCampDetailsForD2D body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getCampDetailsForD2D raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return CampDetailsonLabForDoorToDoorResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('getCampDetailsForD2D error: $e');
      return null;
    }
  }

  Future<UserCampMappingAndAttendanceStatusResponse?>
      getUserCampMappingAndAttendanceStatusD2D({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kGetUserCampMappingAndAttendanceStatusReadinessCampClose}';
      final body = {
        'CampDATE': campDate,
        'UserId': userId.toString(),
        'DISTLGDCODE': distLgdCode.toString(),
        'CampType': campType.toString(),
        'CampID': campId.toString(),
      };
      debugPrint('getUserCampMappingAndAttendanceStatusD2D URL: $uri');
      debugPrint('getUserCampMappingAndAttendanceStatusD2D body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getUserCampMappingAndAttendanceStatusD2D raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return UserCampMappingAndAttendanceStatusResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('getUserCampMappingAndAttendanceStatusD2D error: $e');
      return null;
    }
  }

  // ─── Camp Regular ──────────────────────────────────────────────────────────

  Future<ResourceReMappingCampResponse?> getApprovedCampList({
    required String campDate,
    required int subOrgId,
    required int userId,
    required int dESGID,
  }) async {
    ResourceReMappingCampResponse? result;
    await _apiManager.getApprovedCampListDetailsForAppFlexiCampAPI(
      {
        "CampDATE": campDate,
        "SubOrgId": subOrgId.toString(),
        "Divison": "0",
        "DISTLGDCODE": "0",
        "USERID": userId.toString(),
        "DesgId": dESGID.toString(),
      },
      (ResourceReMappingCampResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<UserCampMappingAndAttendanceDataResponse?>
      getUserCampMappingAndAttendanceStatusRegular({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    UserCampMappingAndAttendanceDataResponse? result;
    await _apiManager
        .getUserCampMappingAndAttendanceStatusForRegularCampReadinessAPI(
      {
        "CampDATE": campDate,
        "UserId": userId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
        "CampType": campType.toString(),
        "CampID": campId.toString(),
        "TestId": "1",
      },
      (UserCampMappingAndAttendanceDataResponse? response, String error,
          bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<UserCampMappingAndAttendanceDataResponse?>
      getUserCampMappingAndAttendanceStatusReadiness({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    UserCampMappingAndAttendanceDataResponse? result;
    await _apiManager.getUserCampMappingAndAttendanceStatusReadinessAPI(
      {
        "CampDATE": campDate,
        "UserId": userId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
        "CampType": campType.toString(),
        "CampID": campId.toString(),
      },
      (UserCampMappingAndAttendanceDataResponse? response, String error,
          bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  // ─── Patient List ──────────────────────────────────────────────────────────

  Future<UserAttendancesUsingSitedetailsIDResponse?> getPatientList({
    required int testId,
    required int campId,
    required int userId,
    required String teamNumber,
    required bool isRegularCamp,
  }) async {
    UserAttendancesUsingSitedetailsIDResponse? result;

    String urlString;
    Map<String, String> params;

    if (testId == 11) {
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDUrineChange}";
      params = {
        "EmpCode": campId.toString(),
        "DistrictId": "0",
        "TestId": testId.toString(),
        "UserId": userId.toString(),
      };
    } else if (testId == 16 || testId == 13) {
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
      params = {
        "TestId": testId.toString(),
        "EmpCode": campId.toString(),
        "DistrictId": "0",
        "UserId": userId.toString(),
        "TeamId": teamNumber,
      };
    } else if (isRegularCamp) {
      urlString =
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNew}";
      params = {
        "EmpCode": campId.toString(),
        "DistrictId": "0",
        "TestId": testId.toString(),
        "UserId": userId.toString(),
      };
    } else {
      urlString =
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDAnti}";
      params = {
        "EmpCode": campId.toString(),
        "DistrictId": "0",
        "TestId": testId.toString(),
        "UserId": userId.toString(),
        "TeamId": teamNumber,
      };
    }

    await _apiManager.getUserAttendancesUsingSitedetailsIDAPI(
      urlString,
      params,
      (UserAttendancesUsingSitedetailsIDResponse? response, String error,
          bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  // ─── District List ─────────────────────────────────────────────────────────

  Future<DistrictResponse?> getDistrictByUserID({
    required int userId,
  }) async {
    DistrictResponse? result;
    await _apiManager.getDistrictByUserIDAPI(
      {
        'STATELGDCODE': '2',
        'USERID': userId.toString(),
      },
      (DistrictResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }
}