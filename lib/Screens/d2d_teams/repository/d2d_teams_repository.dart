// ignore_for_file: file_names

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';

class D2DTeamsRepository {
  String get _base => APIManager.kD2DBaseURL;

  Future<http.Response> getTeamsList({
    required String empId,
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String desgId,
    required String subOrgId,
  }) {
    final uri =
        '$_base${APIConstants.kGetActiveInactiveD2DTeamsGridDataV2}'
        '?GLOUSERID=$empId&CampType=$campType&DivId=$divId'
        '&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getTeamsCount({
    required String empId,
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String desgId,
    required String subOrgId,
  }) {
    final uri =
        '$_base${APIConstants.kGetActiveInactiveD2DTeamsCountV2}'
        '?GLOUSERID=$empId&CampType=$campType&DivId=$divId'
        '&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getCampTypeList() {
    final uri =
        '$_base${APIConstants.kGetCampTypeByChannelPartner}?CatagoryID=1';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getOrgList({
    required String empId,
    required String desgId,
  }) {
    final uri = '$_base${APIConstants.kGetBindOrg}?UserID=$empId&DESGID=$desgId';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getDivisionList({
    required String empId,
    required String desgId,
  }) {
    final uri =
        '$_base${APIConstants.kBindDivision}?SubOrgId=0&UserID=$empId&DESGID=$desgId';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getDistrictList({
    required String subOrgId,
    required String empId,
    required String desgId,
    required String divId,
    required String distlgCode,
  }) {
    final uri =
        '$_base${APIConstants.kBindDistrict}'
        '?SubOrgId=$subOrgId&UserID=$empId&DESGID=$desgId&DIVID=$divId&DISTLGDCODE=$distlgCode';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getLabList({required String distlgCode}) {
    final uri = '$_base${APIConstants.kGetLab}?DISTLGDCODE=$distlgCode';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getNonWorkingTeams({
    required String title,
    required String empId,
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String desgId,
    required String subOrgId,
  }) {
    final endpoint = title == 'D2D Working Teams'
        ? APIConstants.kGetActiveInactiveD2DWorkingTeamsV2
        : APIConstants.kGetActiveInactiveD2DNonWorkingTeamsV2;
    final uri =
        '$_base$endpoint'
        '?GLOUSERID=$empId&CampType=$campType&DivId=$divId'
        '&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId';
    return Repository.getResponse(uri);
  }

  Future<http.Response> getCallingDetails({required String teamId}) {
    final uri =
        '$_base${APIConstants.kGetTeamMembersDetailsForCalling}?Teamid=$teamId';
    return Repository.getResponse(uri);
  }
}