// ignore_for_file: file_names

import 'dart:async';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/device_and_resource_mapping/models/assign_resources_response.dart';
import 'package:s2toperational/camp_calendar/model/camp_list_v3_response.dart';
import 'package:s2toperational/camp_creation/models/camp_type_response.dart';
import 'package:s2toperational/camp_creation/models/district_response.dart';
import 'package:s2toperational/team_camp_mapping/model/team_camp_lab_response.dart';
import '../model/assigned_external_resource_details_response.dart';
import '../model/team_camp_details_list_response.dart';
import '../model/team_details_list_for_assign_response.dart';
import '../model/teams_camp_type_wise_response.dart';
import '../model/teams_doctor_list_response.dart';
import '../model/teams_mmu_doctor_list_response.dart';

class TeamCampMappingRepository {
  final _api = APIManager();

  Future<CampTypeResponse> fetchCampTypeNonD2D() async {
    final c = Completer<CampTypeResponse>();
    _api.getCampTypeNonD2DRAPI((res, error, success) {
      if (success) c.complete(res as CampTypeResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<CampTypeResponse> fetchCampTypeFlexi() async {
    final c = Completer<CampTypeResponse>();
    _api.getCampTypeFlexiAPI((res, error, success) {
      if (success) c.complete(res as CampTypeResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<CampTypeResponse> fetchCampTypeMMU() async {
    final c = Completer<CampTypeResponse>();
    _api.getCampTypeMMUAPI((res, error, success) {
      if (success) c.complete(res as CampTypeResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<CampTypeResponse> fetchCampTypeD2D() async {
    final c = Completer<CampTypeResponse>();
    _api.getCampTypeD2DAPI((res, error, success) {
      if (success) c.complete(res as CampTypeResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<DistrictResponse> fetchDistrictList(Map<String, dynamic> params) async {
    final c = Completer<DistrictResponse>();
    _api.getDistrictByUserIDAPI(params, (res, error, success) {
      if (success) c.complete(res as DistrictResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamCampLabResponse> fetchLabList(Map<String, dynamic> params) async {
    final c = Completer<TeamCampLabResponse>();
    _api.getTeamCampLabListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamCampLabResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<CampListV3Response> fetchCampList(Map<String, dynamic> params) async {
    final c = Completer<CampListV3Response>();
    _api.getCampIDAPI(params, (res, error, success) {
      if (success) c.complete(res as CampListV3Response);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<AssignedExternalResourceDetailsResponse> fetchPhleboDetailsList(Map<String, dynamic> params) async {
    final c = Completer<AssignedExternalResourceDetailsResponse>();
    _api.getPhleboDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignedExternalResourceDetailsResponse);
      else c.complete(AssignedExternalResourceDetailsResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<AssignedExternalResourceDetailsResponse> fetchDeopDetailsList(Map<String, dynamic> params) async {
    final c = Completer<AssignedExternalResourceDetailsResponse>();
    _api.getDeopDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignedExternalResourceDetailsResponse);
      else c.complete(AssignedExternalResourceDetailsResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<AssignedExternalResourceDetailsResponse> fetchDoctorDetailsList(Map<String, dynamic> params) async {
    final c = Completer<AssignedExternalResourceDetailsResponse>();
    _api.getDoctorDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignedExternalResourceDetailsResponse);
      else c.complete(AssignedExternalResourceDetailsResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<TeamCampDetailsListResponse> fetchTeamDetailsList(Map<String, dynamic> params) async {
    final c = Completer<TeamCampDetailsListResponse>();
    _api.getTeamDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamCampDetailsListResponse);
      else c.complete(TeamCampDetailsListResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<AssignedExternalResourceDetailsResponse> fetchFlexiPhleboDetailsList(Map<String, dynamic> params) async {
    // getFlexiPhleboDetailsListAPI has a wrong-type bug; use getPhleboDetailsListAPI (same endpoint) instead.
    final c = Completer<AssignedExternalResourceDetailsResponse>();
    _api.getPhleboDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignedExternalResourceDetailsResponse);
      else c.complete(AssignedExternalResourceDetailsResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<AssignedExternalResourceDetailsResponse> fetchFlexiDoctorDetailsList(Map<String, dynamic> params) async {
    // getFlexiDoctorDetailsListAPI has a wrong-type bug; use getDoctorDetailsListAPI (same endpoint) instead.
    final c = Completer<AssignedExternalResourceDetailsResponse>();
    _api.getDoctorDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignedExternalResourceDetailsResponse);
      else c.complete(AssignedExternalResourceDetailsResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<TeamsMMUDoctorListResponse> fetchMMUDoctorDetailsList(Map<String, dynamic> params) async {
    final c = Completer<TeamsMMUDoctorListResponse>();
    _api.getMMUDoctorDetailsListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsMMUDoctorListResponse);
      else c.complete(TeamsMMUDoctorListResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<TeamCampDetailsListResponse> removeTeamMapping(Map<String, dynamic> params) async {
    final c = Completer<TeamCampDetailsListResponse>();
    _api.removeTeamMappingAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamCampDetailsListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamsCampTypeWiseResponse> fetchTeamsCampTypeWise(Map<String, dynamic> params) async {
    final c = Completer<TeamsCampTypeWiseResponse>();
    _api.getTeamsCampTypeWiseListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsCampTypeWiseResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<AssignResourcesResponse> fetchAssignResources(Map<String, dynamic> params) async {
    final c = Completer<AssignResourcesResponse>();
    _api.getAssignResourcesAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignResourcesResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamDetailsListForAssignResponse> fetchTeamDetailsListForAssign(Map<String, dynamic> params) async {
    final c = Completer<TeamDetailsListForAssignResponse>();
    _api.gtTeamDetailsListForAssignAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamDetailsListForAssignResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamsDoctorListResponse> fetchPhleboList(Map<String, dynamic> params) async {
    final c = Completer<TeamsDoctorListResponse>();
    _api.getPhleboListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsDoctorListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamsDoctorListResponse> fetchDeOpList(Map<String, dynamic> params) async {
    final c = Completer<TeamsDoctorListResponse>();
    _api.getDeOpListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsDoctorListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamsDoctorListResponse> fetchDoctorListCluster(Map<String, dynamic> params) async {
    final c = Completer<TeamsDoctorListResponse>();
    _api.getDoctorListClusterAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsDoctorListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<TeamsDoctorListResponse> fetchMMUDoctorList(Map<String, dynamic> params) async {
    final c = Completer<TeamsDoctorListResponse>();
    _api.getMMUDoctorListAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamsDoctorListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<AssignResourcesResponse> insertTeamCampMapping(Map<String, dynamic> params) async {
    final c = Completer<AssignResourcesResponse>();
    _api.insertTeamCampMappingAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignResourcesResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }
}
