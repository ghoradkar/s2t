// ignore_for_file: depend_on_referenced_packages

import 'package:http/http.dart' as http;

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/constants/APIConstants.dart';

class BeneficiaryRepository {
  Future<http.Response> expectedBeneficiaryList(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetAssignedExpectedBefForCallingMobileNew}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> expectedBeneficiaryListNew(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetAssignedExpectedBefForCallingMobileNewV1}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getCallStatus() async {
    return await http.get(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetAppoinmentStatusList}',
      ),
    );
  }

  Future<http.Response> getCallStatusForAppointment(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetAppoinmentCallStatusList}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getTeamData(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetTeamDataByUserId}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getRelation(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetRelationwithMaritalStatus}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getRemark(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetCallingRemarkV1}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getAddressDetails(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetBeneficiaryAddressDetails}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getDependentDetails(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetBeneficiaryDependantDetails}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getScreenedBeneficiaryList(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetScreenedDependentCount}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getAppointmentCount(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetAppointmentDateCount}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> insertAppointmentData(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kInsertBeneficiaryCallingAppointmentDetailsMobNew}',
      ),
      body: payload,
      headers: header,
    );
  }
}
