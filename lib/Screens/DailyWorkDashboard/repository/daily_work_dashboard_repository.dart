// ignore_for_file: file_names

import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
// import 'package:s2toperational/Modules/Json_Class/BeneficiaryStatusAndDetailsResponse/BeneficiaryStatusAndDetailsResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/LabDataResponse/LabDataResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryToTeamResponse/RecollectionBeneficiaryToTeamResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';

import '../model/BeneficiaryStatusAndDetailsResponse.dart';
import '../model/BindDistrictResponse.dart';
import '../model/BindDivisionResponse.dart';
import '../model/LabDataResponse.dart';
import '../model/LandingLabCampCreationResponse.dart';
import '../model/RecollectionAssignmentRemarksResponse.dart';
import '../model/RecollectionBeneficiaryDashboardForMobResponse.dart';
import '../model/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import '../model/RecollectionBeneficiaryToTeamResponse.dart';
import '../model/SelectedTeamsDataListResponse.dart';
import '../model/SubOrganizationResponse.dart';

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
