// ignore_for_file: file_names

import 'dart:async';
import 'package:s2toperational/Modules/utilities/api_manager.dart';
import 'package:s2toperational/Screens/appointments_confirmed_list/model/appointment_status_response.dart';
import 'package:s2toperational/Screens/appointments_confirmed_list/model/call_status_list_response.dart';
import 'package:s2toperational/Screens/appointments_confirmed_list/model/remark_list_response.dart';
import '../model/appoinment_expected_beneficiaries_response.dart';
import '../model/beneficiaries_details_response.dart';
import '../model/beneficiary_dependant_details_response.dart';
import '../model/screened_dependent_count_response.dart';
import '../model/team_cc_response.dart';

class AppointmentsConfirmedRepository {
  final APIManager _api = APIManager();

  Future<AppoinmentExpectedBeneficiariesResponse> fetchBeneficiaryList(
    Map<String, String> params,
  ) {
    final c = Completer<AppoinmentExpectedBeneficiariesResponse>();
    _api.getAppointmentBeneficiariesListAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<AppoinmentExpectedBeneficiariesResponse> fetchBeneficiaryListMAS(
    Map<String, String> params,
  ) {
    final c = Completer<AppoinmentExpectedBeneficiariesResponse>();
    _api.getAppointmentBeneficiariesMASListAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<TeamCCResponse> fetchTeamList(Map<String, String> params) {
    final c = Completer<TeamCCResponse>();
    _api.getTeamListCCAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<AppointmentStatusResponse> fetchAppointmentStatusList(
    Map<String, String> params,
  ) {
    final c = Completer<AppointmentStatusResponse>();
    _api.getAppointmentStatusListAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<BeneficiariesDetailsResponse> fetchBeneficiaryData(
    Map<String, String> params,
  ) {
    final c = Completer<BeneficiariesDetailsResponse>();
    _api.getBeneficiaryDataAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<CallStatusListResponse> fetchCallStatusList(
    Map<String, String> params,
  ) {
    final c = Completer<CallStatusListResponse>();
    _api.getPhleboCallStatusListAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<RemarkListResponse> fetchRemarkList(Map<String, String> params) {
    final c = Completer<RemarkListResponse>();
    _api.getCallingRemarkV1API(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<bool> updateAppointmentDetails(Map<String, String> params) {
    final c = Completer<bool>();
    _api.updateAppointmentDetailsMobAPI(params, (res, err, ok) {
      ok ? c.complete(true) : c.completeError(err);
    });
    return c.future;
  }

  Future<ScreenedDependentCountResponse> fetchScreenedDependentCount(
    Map<String, dynamic> params,
  ) {
    final c = Completer<ScreenedDependentCountResponse>();
    _api.getScreeningCountAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }

  Future<BeneficiaryDependantDetailsResponse> fetchBeneficiaryDependantDetails(
    Map<String, dynamic> params,
  ) {
    final c = Completer<BeneficiaryDependantDetailsResponse>();
    _api.getDependentListAPI(params, (res, err, ok) {
      ok ? c.complete(res!) : c.completeError(err);
    });
    return c.future;
  }
}
