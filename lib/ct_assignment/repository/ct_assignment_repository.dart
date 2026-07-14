// ignore_for_file: file_names

import 'dart:async';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/assignment_remarks_response.dart';
import 'package:s2toperational/camp_creation/models/landing_lab_camp_creation_response.dart';
import 'package:s2toperational/ct_assignment/model/t2t_ct_user_details_response.dart';
import '../model/beneficiary_details_for_assign_teamid_details_response.dart';
import '../model/confirmatory_tests_screening_response.dart';
import '../model/confirmatory_tests_screening_tube_response.dart';
import '../model/selected_teams_data_list_response.dart';
import '../model/t2t_ct_beneficiary_details_response.dart';
import '../model/t2t_ct_team_and_beneficiary_response.dart';

class CTAssignmentRepository {
  final _api = APIManager();

  Future<T2TCTBeneficiaryDetailsResponse> fetchBeneficiaryList(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<T2TCTBeneficiaryDetailsResponse>();
    _api.getPostCampDetailsAPI(params, (res, error, success) {
      if (success) c.complete(res as T2TCTBeneficiaryDetailsResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<BeneficiaryDetailsforAssignTeamidDetailsResponse> fetchDependentInfo(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<BeneficiaryDetailsforAssignTeamidDetailsResponse>();
    _api.getDependentInfoAPI(params, (res, error, success) {
      if (success) c.complete(res as BeneficiaryDetailsforAssignTeamidDetailsResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<SelectedTeamsDataListResponse> fetchRecollectionTeams(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<SelectedTeamsDataListResponse>();
    _api.getRecollectionTeamDetialsAPI(params, (res, error, success) {
      if (success) c.complete(res as SelectedTeamsDataListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<SelectedTeamsDataListResponse> fetchTeamsByPincode(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<SelectedTeamsDataListResponse>();
    _api.getT2TTeamDetailsByPincodeAPI(params, (res, error, success) {
      if (success) c.complete(res as SelectedTeamsDataListResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<T2TCTUserDetailsResponse> fetchExecutiveList(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<T2TCTUserDetailsResponse>();
    _api.getT2TCTUserDetailsAPI(params, (res, error, success) {
      if (success) c.complete(res as T2TCTUserDetailsResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<T2TCTTeamandBeneficiaryResponse> submitTeamAssignment(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<T2TCTTeamandBeneficiaryResponse>();
    _api.insertT2TCTTeamandBeneficiaryMappingAPI(params, (res, error, success) {
      if (success) c.complete(res as T2TCTTeamandBeneficiaryResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<ConfirmatoryTestsScreeningResponse> fetchTestInfo(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<ConfirmatoryTestsScreeningResponse>();
    _api.getTestInfoAPI(params, (res, error, success) {
      if (success) c.complete(res as ConfirmatoryTestsScreeningResponse);
      else c.complete(ConfirmatoryTestsScreeningResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<ConfirmatoryTestsScreeningTubeResponse> fetchTestInfoCount(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<ConfirmatoryTestsScreeningTubeResponse>();
    _api.getTestInfoCount(params, (res, error, success) {
      if (success) c.complete(res as ConfirmatoryTestsScreeningTubeResponse);
      else c.complete(ConfirmatoryTestsScreeningTubeResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<AssignmentRemarksResponse> fetchAssignmentRemarks(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<AssignmentRemarksResponse>();
    _api.getAssignmentRemarksAPI(params, (res, error, success) {
      if (success) c.complete(res as AssignmentRemarksResponse);
      else c.complete(AssignmentRemarksResponse(status: 'Error', output: []));
    });
    return c.future;
  }

  Future<LandingLabCampCreationResponse> fetchLabList(
    Map<String, dynamic> params,
  ) async {
    final c = Completer<LandingLabCampCreationResponse>();
    _api.getT2TLabDetailsAPI(params, (res, error, success) {
      if (success) c.complete(res as LandingLabCampCreationResponse);
      else c.completeError(error as String);
    });
    return c.future;
  }

  Future<bool> sendOTP(Map<String, dynamic> params) async {
    final c = Completer<bool>();
    _api.sendOTPForCTSampleCollectionAPI(params, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }

  Future<bool> verifyOTP(Map<String, dynamic> params) async {
    final c = Completer<bool>();
    _api.verifyOTPForCTSampleCollectionAPI(params, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }

  Future<bool> submitBarcodeCollectionWithConsent(
    Map<String, String> fields,
    String? photoPath,
    String? consentPath,
  ) async {
    final c = Completer<bool>();
    _api.insertT2TBarcodeCollectionWithConsentAPI(fields, photoPath, consentPath, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }

  Future<bool> submitBarcodeCollection(Map<String, dynamic> params) async {
    final c = Completer<bool>();
    _api.insertT2TBarcodeCollectionAPI(params, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }
}
