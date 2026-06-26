// ignore_for_file: file_names

import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/camp_calendar/model/bind_district_response.dart';
import 'package:s2toperational/Modules/Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';

import '../model/d2d_non_working_teams_response.dart';
import '../model/d2d_team_member_details_response.dart';
import '../model/d2d_teams_count_response.dart';

class D2DTeamRepository {
  final APIManager _api = APIManager();

  Future<D2DTeamsCountResponse?> getActiveInactiveD2DTeamsCount(
    Map<String, String> params,
  ) {
    final c = Completer<D2DTeamsCountResponse?>();
    _api.getActiveInactiveD2DTeamsCountAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  Future<D2DNonWorkingTeamsResponse?> getNotWorkingTeams(
    Map<String, String> params,
  ) {
    final c = Completer<D2DNonWorkingTeamsResponse?>();
    _api.getNotWorkingTeamsCountAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  Future<D2DNonWorkingTeamsResponse?> getWorkingTeams(
    Map<String, String> params,
  ) {
    final c = Completer<D2DNonWorkingTeamsResponse?>();
    _api.getWorkingTeamsCountAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  Future<D2DTeamMemberDetailsResponse?> getTeamsCalling(
    Map<String, String> params,
  ) {
    final c = Completer<D2DTeamMemberDetailsResponse?>();
    _api.getTeamsCallingAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  Future<BindDistrictResponse?> getBindDistrict(
    Map<String, String> params,
  ) {
    final c = Completer<BindDistrictResponse?>();
    _api.getBindDistrictAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  Future<LabByUserIDResponse?> getLabByUserId(
    Map<String, String> params,
  ) {
    final c = Completer<LabByUserIDResponse?>();
    _api.getTalukaPacketReciveAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }
}
