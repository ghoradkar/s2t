import 'dart:convert';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/camp_conducted_response.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/camp_type_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_non_working_teams.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_calling_details.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_count_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_division_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_lab_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/district_list_response.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/fibro_scanning_district_wise_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/liver_scanning_count_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/liver_scanning_table_data.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/organization_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/s2t_android_Ios_count_district_wise_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/s2t_android_Ios_count_model.dart';

class AdminDashboardRepository {
  Future<DistrictListResponse?> fetchDistricts() async {
    final url =
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.getDistrictList}';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return DistrictListResponse.fromJson(response.body);
    }
    return null;
  }

  Future<CampsConductedResponse?> fetchCampsConducted(
    String month,
    String year,
    String distCode,
    String campType,
  ) async {
    final url =
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetMonthlySurveySiteRequestForOS}?Month=$month&Year=$year&DistCode=$distCode&CampType=$campType';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return CampsConductedResponse.fromJson(response.body);
    }
    return null;
  }

  Future<D2dNonWorkingTeams?> fetchTeamsByWorkingStatus(
    bool isWorking,
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrgId,
  ) async {
    final endpoint = isWorking
        ? APIConstants.kGetActiveInactiveD2DWorkingTeamsV2
        : APIConstants.kGetActiveInactiveD2DNonWorkingTeamsV2;
    final url =
        '${APIManager.kD2DBaseURL}$endpoint?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2dNonWorkingTeams.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<D2DTeamsCallingDetails?> fetchCallingDetails(String teamId) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetTeamMembersDetailsForCalling}?Teamid=$teamId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2DTeamsCallingDetails.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<LiverScanningCountModel?> fetchLiverDashCount() async {
    final url =
        '${APIManager.kLiverScann}${APIConstants.getLiverDashCount}';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return LiverScanningCountModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<S2TAndroidIosCountModel?> fetchS2TAppCount() async {
    final url =
        '${APIManager.kTreatmentCount}${APIConstants.getAndroidIosCount}';
    final response = await Repository.postResponseWithoutBody(
      url,
      timeout: const Duration(minutes: 5),
    );
    if (response.statusCode == 200) {
      return S2TAndroidIosCountModel.fromJson(
        jsonDecode(await response.stream.bytesToString()),
      );
    }
    return null;
  }

  Future<S2TAndroidIosCountDistrictWiseModel?> fetchS2TAppDistrictWiseCount() async {
    final url = '${APIManager.kTreatmentCount}${APIConstants.kIosCount}';
    final response = await Repository.postResponseWithoutBody(
      url,
      timeout: const Duration(minutes: 5),
    );
    if (response.statusCode == 200) {
      return S2TAndroidIosCountDistrictWiseModel.fromJson(
        jsonDecode(await response.stream.bytesToString()),
      );
    }
    return null;
  }

  Future<D2DTeamsListModel?> fetchD2DTeamsList(
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrgId,
  ) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetActiveInactiveD2DTeamsGridDataV2}?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2DTeamsListModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<D2DTeamsCountModel?> fetchD2DTeamsCount(
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrgId,
  ) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetActiveInactiveD2DTeamsCountV2}?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2DTeamsCountModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<CampTypeListModel?> fetchCampTypeList() async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetCampTypeByChannelPartner}?CatagoryID=1';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return CampTypeListModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<OrganizationListModel?> fetchOrgList(
    String empId,
    String desgId,
  ) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetBindOrg}?UserID=$empId&DESGID=$desgId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return OrganizationListModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<D2DTeamsDivisionModel?> fetchDivisionList(
    String empId,
    String desgId,
  ) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kBindDivision}?SubOrgId=0&UserID=$empId&DESGID=$desgId';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2DTeamsDivisionModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<BindDistrictResponse?> fetchD2DDistrictList(
    String subOrgId,
    String empId,
    String desgId,
    String divId,
    String distlgdCode,
  ) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kBindDistrict}?SubOrgId=$subOrgId&UserID=$empId&DESGID=$desgId&DIVID=$divId&DISTLGDCODE=$distlgdCode';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return BindDistrictResponse.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<D2DTeamsLabModel?> fetchLabList(String distlgdCode) async {
    final url =
        '${APIManager.kD2DBaseURL}${APIConstants.kGetLab}?DISTLGDCODE=$distlgdCode';
    final response = await Repository.getResponse(url);
    if (response.statusCode == 200) {
      return D2DTeamsLabModel.fromJson(jsonDecode(response.body));
    }
    return null;
  }

  Future<LiverScanningTableData?> fetchLiverTableData(
    Map<String, String> body,
  ) async {
    final url =
        '${APIManager.kLiverScann}${APIConstants.getLiverScanningTableData}';
    final response = await Repository.postFormEncodedRequest(url, body, {
      'Content-Type': 'application/x-www-form-urlencoded',
    });
    if (response.statusCode == 200) {
      final data = json.decode(await response.stream.bytesToString());
      if (data['Status'] == 'Success') {
        return LiverScanningTableData.fromJson(data);
      }
    }
    return null;
  }

  Future<FibroScanningDistrictWiseModel?> fetchLiverTableDataDistrictWise(
    Map<String, String> body,
  ) async {
    final url =
        '${APIManager.kLiverScann}${APIConstants.getLiverScanningTableDataDistrictWise}';
    final response = await Repository.postFormEncodedRequest(url, body, {
      'Content-Type': 'application/x-www-form-urlencoded',
    });
    if (response.statusCode == 200) {
      final data = json.decode(await response.stream.bytesToString());
      if (data['Status'] == 'Success') {
        return FibroScanningDistrictWiseModel.fromJson(data);
      }
    }
    return null;
  }
}
