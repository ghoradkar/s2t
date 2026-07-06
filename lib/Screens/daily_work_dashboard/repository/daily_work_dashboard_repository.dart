// ignore_for_file: file_names

import 'dart:async';

import 'package:s2toperational/Modules/utilities/api_manager.dart';
// import 'package:s2toperational/Modules/Json_Class/BeneficiaryStatusAndDetailsResponse/beneficiary_status_and_details_response.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/bind_district_response.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/bind_division_response.dart';
// import 'package:s2toperational/Modules/Json_Class/LabDataResponse/lab_data_response.dart';
// import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/landing_lab_camp_creation_response.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/recollection_assignment_remarks_response.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/recollection_beneficiary_dashboard_for_mob_response.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/recollection_beneficiary_status_and_details_count_v1_response.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryToTeamResponse/recollection_beneficiary_to_team_response.dart';
// import 'package:s2toperational/Modules/Json_Class/SelectedTeamsDataListResponse/selected_teams_data_list_response.dart';
// import 'package:s2toperational/Modules/Json_Class/SubOrganizationResponse/sub_organization_response.dart';

import '../model/beneficiary_status_and_details_response.dart';
import '../model/bind_district_response.dart';
import '../model/bind_division_response.dart';
import '../model/lab_data_response.dart';
import '../model/landing_lab_camp_creation_response.dart';
import '../model/recollection_assignment_remarks_response.dart';
import '../model/recollection_beneficiary_dashboard_for_mob_response.dart';
import '../model/recollection_beneficiary_status_and_details_count_v1_response.dart';
import '../model/recollection_beneficiary_to_team_response.dart';
import '../model/selected_teams_data_list_response.dart';
import '../model/sub_organization_response.dart';

class DailyWorkDashboardRepository {
  final APIManager _api = APIManager();

  // ─── Dashboard counts ─────────────────────────────────────────────────────────

  Future<RecollectionBeneficiaryToTeamResponse?> getCountForPageloadForTeam(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryToTeamResponse?>();
    _api.getCountForPageloadForTeamAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<RecollectionBeneficiaryToTeamResponse?> getCountForPageload(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryToTeamResponse?>();
    _api.getCountForPageloadAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  // ─── Filter dropdowns ─────────────────────────────────────────────────────────

  Future<SubOrganizationResponse?> getSubOrganization(
    Map<String, String> params,
  ) {
    final c = Completer<SubOrganizationResponse?>();
    _api.getSubOrganizationAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<BindDivisionResponse?> getBindDivision(
    Map<String, String> params,
  ) {
    final c = Completer<BindDivisionResponse?>();
    _api.getBindDivision(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<BindDistrictResponse?> getBindDistrict(
    Map<String, String> params,
  ) {
    final c = Completer<BindDistrictResponse?>();
    _api.getBindDistrictAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<LabDataResponse?> getLabForD2DCampCoordinator(
    Map<String, String> params,
  ) {
    final c = Completer<LabDataResponse?>();
    _api.getLabforD2DCampCoordinatorAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<LandingLabCampCreationResponse?> getLabDistrictWise(
    Map<String, String> params,
  ) {
    final c = Completer<LandingLabCampCreationResponse?>();
    _api.getLandingLabAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  // ─── Tracking screen ─────────────────────────────────────────────────────────

  Future<RecollectionBeneficiaryDashboardForMobResponse?> getRecollectionBeneficiaryDashboardForMob(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryDashboardForMobResponse?>();
    _api.getRecollectionBeneficiaryDashboardForMobAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }

  // ─── Beneficiary list ─────────────────────────────────────────────────────────

  Future<RecollectionBeneficiaryStatusandDetailsCountV1Response?> getCountForPageloadForTeamV1(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryStatusandDetailsCountV1Response?>();
    _api.getCountForPageloadForTeamV1API(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<RecollectionBeneficiaryStatusandDetailsCountV1Response?> getCountForPageloadVa(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryStatusandDetailsCountV1Response?>();
    _api.getCountForPageloadVaAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<RecollectionBeneficiaryStatusandDetailsCountV1Response?> getCountV1(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryStatusandDetailsCountV1Response?>();
    _api.getCountV1API(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<RecollectionBeneficiaryStatusandDetailsCountV1Response?> getCountForTeam(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionBeneficiaryStatusandDetailsCountV1Response?>();
    _api.getCountForTeamPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<RecollectionAssignmentRemarksResponse?> getRecollectionAssignmentRemarks(
    Map<String, String> params,
  ) {
    final c = Completer<RecollectionAssignmentRemarksResponse?>();
    _api.getRecollectionAssignmentRemarksAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  // ─── Beneficiary info ─────────────────────────────────────────────────────────

  Future<BeneficiaryStatusAndDetailsResponse?> getBeneficiaryStatusAndDetails(
    Map<String, String> params,
  ) {
    final c = Completer<BeneficiaryStatusAndDetailsResponse?>();
    _api.getBeneficiaryStatusAndDetailsAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<SelectedTeamsDataListResponse?> getRecollectionTeamDetails(
    Map<String, String> params,
  ) {
    final c = Completer<SelectedTeamsDataListResponse?>();
    _api.getRecollectionTeamDetialsAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<BeneficiaryStatusAndDetailsResponse?> insertAppointmentDetails(
    Map<String, String> params,
  ) {
    final c = Completer<BeneficiaryStatusAndDetailsResponse?>();
    _api.insertAppointmentDetailsAPI(params, (res, _, ok) => c.complete(ok ? res : null));
    return c.future;
  }

  Future<BeneficiaryStatusAndDetailsResponse?> insertRecollectionTeamAndBeneficiaryMapping(
    Map<String, String> params,
  ) {
    final c = Completer<BeneficiaryStatusAndDetailsResponse?>();
    _api.insertRecollectionTeamandBeneficiaryMappingAPI(
      params,
      (res, _, ok) => c.complete(ok ? res : null),
    );
    return c.future;
  }
}
