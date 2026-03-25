import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_closing_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_d2d_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_regular_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

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
    CampDetailsonLabForDoorToDoorResponse? result;
    await _apiManager.getCampDetailsonLabForDoorToDoorV2API(
      {
        "CampDate": campDate,
        "LabCode": labCode.toString(),
        "SubOrgId": subOrgId.toString(),
        "Divison": "0",
        "DISTLGDCODE": distLgdCode.toString(),
        "USERID": userId.toString(),
        "dESGID": dESGID.toString(),
      },
      (CampDetailsonLabForDoorToDoorResponse? response, String error,
          bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<UserCampMappingAndAttendanceStatusResponse?>
      getUserCampMappingAndAttendanceStatusD2D({
    required String campDate,
    required int userId,
    required int distLgdCode,
    required int campType,
    required int campId,
  }) async {
    UserCampMappingAndAttendanceStatusResponse? result;
    await _apiManager.getUserCampMappingAndAttendanceStatusD2DAPI(
      {
        "CampDATE": campDate,
        "UserId": userId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
        "CampType": campType.toString(),
        "CampID": campId.toString(),
      },
      (UserCampMappingAndAttendanceStatusResponse? response, String error,
          bool success) {
        if (success) result = response;
      },
    );
    return result;
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
}