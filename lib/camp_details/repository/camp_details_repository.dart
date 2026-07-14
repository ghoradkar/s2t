// ignore_for_file: file_names

import 'dart:async';
import 'dart:io';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/camp_details/model/other_reason_for_patient_rejection_response.dart';
import 'package:s2toperational/camp_details/model/team_details_list_response.dart';
import 'package:s2toperational/camp_details/model/test_list_for_reject_response.dart';
import 'package:s2toperational/d2d_physical_examination/model/team_number_by_campId_and_user_id_list_response.dart';
import '../model/audio_screening_details_response.dart';
import '../model/beneficiary_worker_response.dart';
import '../model/camp_details_response.dart';
import '../model/lung_function_test_details_response.dart';
import '../model/patient_checkup_analysis_report_response.dart';
import '../model/patient_status_details_list_response.dart';
import '../model/vision_screening_details_response.dart';

class CampDetailsRepository {
  final APIManager _api = APIManager();

  Future<TeamNumberByCampIdAndUserIdListResponse> fetchTeamId(Map<String, String> params) async {
    final c = Completer<TeamNumberByCampIdAndUserIdListResponse>();
    _api.getTeamNumberByCampIdAndUSerIdAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamNumberByCampIdAndUserIdListResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<BeneficiaryWorkerResponse> fetchBeneficiaryList(Map<String, String> params) async {
    final c = Completer<BeneficiaryWorkerResponse>();
    _api.getRegiWorkerDetailsOncampIdAPI(params, (res, error, success) {
      if (success) c.complete(res as BeneficiaryWorkerResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<TeamDetailsListResponse> fetchCampWiseTeams(Map<String, String> params) async {
    final c = Completer<TeamDetailsListResponse>();
    _api.getCampIDWiseTeamDetailsAPI(params, (res, error, success) {
      if (success) c.complete(res as TeamDetailsListResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<CampDetailsResponse> fetchCampDetailsCount(String url, Map<String, String> params) async {
    final c = Completer<CampDetailsResponse>();
    _api.getScreeningTestCampDetailsAPI(url, params, (res, error, success) {
      if (success) c.complete(res as CampDetailsResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<PatientStatusDetailsListResponse> fetchPatientStatus(Map<String, String> params) async {
    final c = Completer<PatientStatusDetailsListResponse>();
    _api.getCAMPPatientCheckupAnalysisReportAPI(params, (res, error, success) {
      if (success) c.complete(res as PatientStatusDetailsListResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  // "Safe" variants return null on failure â€” used with Future.wait so all 4 complete even if one fails
  Future<PatientCheckupAnalysisReportResponse?> fetchPatientCheckupSafe(Map<String, String> params) async {
    final c = Completer<PatientCheckupAnalysisReportResponse?>();
    _api.getCAMPPatientCheckupAnalysisReportNewAPI(params, (res, error, success) {
      c.complete(success ? res as PatientCheckupAnalysisReportResponse : null);
    });
    return c.future;
  }

  Future<AudioScreeningDetailsResponse?> fetchAudioScreeningDetailsSafe(Map<String, String> params) async {
    final c = Completer<AudioScreeningDetailsResponse?>();
    _api.getAudioScreeningDetailsAPI(params, (res, error, success) {
      c.complete(success ? res as AudioScreeningDetailsResponse : null);
    });
    return c.future;
  }

  Future<VisionScreeningDetailsResponse?> fetchVisionScreeningDetailsSafe(Map<String, String> params) async {
    final c = Completer<VisionScreeningDetailsResponse?>();
    _api.getVisionScreeningDetailsAPI(params, (res, error, success) {
      c.complete(success ? res as VisionScreeningDetailsResponse : null);
    });
    return c.future;
  }

  Future<LungFunctionTestDetailsResponse?> fetchLungFunctionDetailsSafe(Map<String, String> params) async {
    final c = Completer<LungFunctionTestDetailsResponse?>();
    _api.getLungFunctionTestDetailsAPI(params, (res, error, success) {
      c.complete(success ? res as LungFunctionTestDetailsResponse : null);
    });
    return c.future;
  }

  Future<TestListForRejectResponse> fetchTestsToReject(Map<String, String> params) async {
    final c = Completer<TestListForRejectResponse>();
    _api.getTestToRejectAPI(params, (res, error, success) {
      if (success) c.complete(res as TestListForRejectResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<OtherReasonForPatientRejectionResponse> fetchRejectionReasons() async {
    final c = Completer<OtherReasonForPatientRejectionResponse>();
    _api.getOtherReasonForPatientRejectionAPI((res, error, success) {
      if (success) c.complete(res as OtherReasonForPatientRejectionResponse);
      else c.completeError(error);
    });
    return c.future;
  }

  Future<bool> uploadVerificationPhoto(
    Map<String, String> params,
    String fileName,
    File imageFile,
    String isType,
  ) async {
    final c = Completer<bool>();
    _api.uploadBeneficiaryVerificationAPI(params, fileName, imageFile, isType, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }

  Future<bool> submitApproveOrDeny(Map<String, String> params) async {
    final c = Completer<bool>();
    _api.insertPatientRejectionInCampInCampTestV1API(params, (res, error, success) {
      c.complete(success);
    });
    return c.future;
  }
}
