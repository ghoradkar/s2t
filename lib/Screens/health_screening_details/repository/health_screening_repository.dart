import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/DishaResponse/DishaResponse.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SpecimenTypeResponse/SpecimenTypeResponse.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

import '../models/camp_closing_model.dart';
import '../models/camp_d2d_model.dart';
import '../models/camp_regular_model.dart';
import '../models/lung_function_test_model.dart';

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
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kGetApprovedCampListDetailsForAppFlexiCampV1}';
      final body = {
        'CampDATE': campDate,
        'SubOrgId': subOrgId.toString(),
        'Divison': '0',
        'DISTLGDCODE': '0',
        'USERID': userId.toString(),
        'DesgId': dESGID.toString(),
      };
      debugPrint('getApprovedCampList URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getApprovedCampList raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return ResourceReMappingCampResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('getApprovedCampList error: $e');
      return null;
    }
  }

  Future<UserCampMappingAndAttendanceDataResponse?>
      getUserCampMappingAndAttendanceStatusRegular({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kGetUserCampMappingAndAttendanceStatusForRegularCampReadiness}';
      final body = {
        'CampDATE': campDate,
        'UserId': userId.toString(),
        'DISTLGDCODE': distLgdCode.toString(),
        'CampType': campType.toString(),
        'CampID': campId.toString(),
        'TestId': '1',
      };
      debugPrint('getUserCampMappingAndAttendanceStatusRegular URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getUserCampMappingAndAttendanceStatusRegular raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return UserCampMappingAndAttendanceDataResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('getUserCampMappingAndAttendanceStatusRegular error: $e');
      return null;
    }
  }

  Future<UserCampMappingAndAttendanceDataResponse?>
      getUserCampMappingAndAttendanceStatusReadiness({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kGetUserCampMappingAndAttendanceStatusReadiness}';
      final body = {
        'CampDATE': campDate,
        'UserId': userId.toString(),
        'DISTLGDCODE': distLgdCode.toString(),
        'CampType': campType.toString(),
        'CampID': campId.toString(),
      };
      debugPrint('getUserCampMappingAndAttendanceStatusReadiness URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getUserCampMappingAndAttendanceStatusReadiness raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return UserCampMappingAndAttendanceDataResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('getUserCampMappingAndAttendanceStatusReadiness error: $e');
      return null;
    }
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
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDUrineChange}";
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

  Future<UserAttendancesUsingSitedetailsIDResponse?> getPatientListForBasicHealthInfo({
    required int siteDetailId,
    required int userId,
    required String teamId,
  }) async {
    UserAttendancesUsingSitedetailsIDResponse? result;
    final urlString =
        "${APIManager.kD2DBaseURL}${APIConstants.kGetuserAttendanceForSitedetailsIDPhysicalExam}";
    final params = {
      "SiteDetailId": siteDetailId.toString(),
      "DistrictId": "0",
      "TestId": "1",
      "UserId": userId.toString(),
      "TeamId": teamId,
    };
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

  // ─── Sample Collection ─────────────────────────────────────────────────────

  Future<List<SpecimenTypeOutput>> getSpecimenTypes() async {
    try {
      final uri =
          '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetSpecimenType}';
      debugPrint('getSpecimenTypes URL: $uri');
      final response = await Repository.postResponse(
        uri,
        {},
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getSpecimenTypes raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      final model = SpecimenTypeResponse.fromJson(decoded);
      if ((model.status ?? '').toLowerCase() == 'success') {
        return model.output ?? [];
      }
      return [];
    } catch (e) {
      debugPrint('getSpecimenTypes error: $e');
      return [];
    }
  }

  Future<Map<String, dynamic>?> submitSampleCollection({
    required int regdId,
    required int siteId,
    required int campId,
    required String sampleCount,
    required String barcode1,
    required String barcode2,
    required int createdBy,
    required String sampleDate,
    required String sampleTime,
    required String specTypeId,
    required String versionNo,
    required String isScannedBy,
  }) async {
    try {
      final uri =
          '${APIManager.kD2DBaseURL}${APIConstants.kInsertCWPatientBarcodeDetails}';
      final body = {
        'RegdId': regdId.toString(),
        'SiteId': siteId.toString(),
        'CampId': campId.toString(),
        'SampleCount': sampleCount.isEmpty ? '3' : sampleCount,
        'Barcode1': barcode1,
        'Barcode2': barcode2,
        'CreatedBy': createdBy.toString(),
        'Type': '1',
        'sampledate': sampleDate,
        'sampletime': sampleTime,
        'SPECTYPEID': specTypeId,
        'VersionNo': versionNo,
        'IsScannedBy': isScannedBy,
      };
      debugPrint('submitSampleCollection URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('submitSampleCollection raw: ${response.body}');
      return json.decode(response.body) as Map<String, dynamic>;
    } catch (e) {
      debugPrint('submitSampleCollection error: $e');
      return null;
    }
  }

  // ─── Urine Sample Collection ───────────────────────────────────────────────

  Future<Map<String, dynamic>?> submitUrineSampleCollection({
    required int regdId,
    required int campId,
    required String barcode1,
    required int sampleReceiveFlag,
    required int createdBy,
    required String remark,
  }) async {
    try {
      final uri =
          '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kInsertUrineSampleReceived}';
      final body = {
        'RegdId': regdId.toString(),
        'CampId': campId.toString(),
        'Barcode1': barcode1,
        'SampleReciveFlag': sampleReceiveFlag.toString(),
        'CreatedBy': createdBy.toString(),
        'Remark': remark,
      };
      debugPrint('submitUrineSampleCollection URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('submitUrineSampleCollection raw: ${response.body}');
      return json.decode(response.body) as Map<String, dynamic>;
    } catch (e) {
      debugPrint('submitUrineSampleCollection error: $e');
      return null;
    }
  }

  // ─── Disha LIS ─────────────────────────────────────────────────────────────

  static const String _dishaBase = 'http://103.251.94.38:8080/DISHA_API';

  Future<String?> dishaLogin() async {
    try {
      final uri = Uri.parse('$_dishaBase/auth/login');
      final response = await http.post(
        uri,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {'username': 'Suvarna', 'password': 'Suvarna@123'},
      );
      debugPrint('dishaLogin status: ${response.statusCode}');
      debugPrint('dishaLogin raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return DishaLoginResponse.fromJson(decoded).token;
    } catch (e) {
      debugPrint('dishaLogin error: $e');
      return null;
    }
  }

  Future<DishaTestsResponse?> dishaGetTests(String token) async {
    try {
      final uri = Uri.parse('$_dishaBase/api/v1/getTestbyCustomerId?customerCode=NHO');
      final response = await http.get(
        uri,
        headers: {'Authorization': 'Bearer $token'},
      );
      debugPrint('dishaGetTests raw: ${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      return DishaTestsResponse.fromJson(decoded);
    } catch (e) {
      debugPrint('dishaGetTests error: $e');
      return null;
    }
  }

  Future<Map<String, dynamic>?> dishaRegisterPatient({
    required String token,
    required Map<String, dynamic> body,
  }) async {
    try {
      final uri = Uri.parse('$_dishaBase/api/v1/registerPatientFromHMIS');
      final response = await http.post(
        uri,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: json.encode(body),
      );
      debugPrint('dishaRegisterPatient raw: ${response.body}');
      return json.decode(response.body) as Map<String, dynamic>;
    } catch (e) {
      debugPrint('dishaRegisterPatient error: $e');
      return null;
    }
  }

  // ─── Audio Screening ───────────────────────────────────────────────────────

  Future<bool> saveAudioScreeningData({
    required String createdBy,
    required String jsonString,
  }) async {
    return _apiManager.insertMachineHearingTestAPI(
      regdId: '',
      createdBy: createdBy,
      jsonString: jsonString,
    );
  }

  // ─── Lung Function Test ────────────────────────────────────────────────────

  Future<Map<String, dynamic>?> submitLFTDetails({
    required String regId,
    required String campId,
    required String createdBy,
    required String versionNo,
    required String deviceId,
    required LungFunctionTestResult result,
  }) async {
    try {
      final uri = '${APIManager.kD2DBaseURL}${APIConstants.kInsertLFTDetails}';
      final body = {
        'Regid':     regId,
        'CampId':    campId,
        'FCV':       result.fvc.toStringAsFixed(3),
        'FEV1':      result.fev1.toStringAsFixed(3),
        'FEVI_FVC':  result.feviFvc.toStringAsFixed(3),
        'PEF':       result.pef.toStringAsFixed(3),
        'FEF_25_75': result.fef2575.toStringAsFixed(3),
        'FIVC':      result.fivc == 0 ? '' : result.fivc.toStringAsFixed(3),
        'PIF':       result.pif == 0 ? '' : result.pif.toStringAsFixed(3),
        'FET':       result.fet.toStringAsFixed(3),
        'Result':    result.diagnosis,
        'DeviceId':  deviceId,
        'CreatedBy': createdBy,
        'VersionNo': versionNo,
      };
      debugPrint('submitLFTDetails URL: $uri body: $body');
      final response = await Repository.postResponse(
        uri,
        body,
        {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('submitLFTDetails raw: ${response.body}');
      return json.decode(response.body) as Map<String, dynamic>;
    } catch (e) {
      debugPrint('submitLFTDetails error: $e');
      return null;
    }
  }
}