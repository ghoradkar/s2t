// ignore_for_file: depend_on_referenced_packages

import 'dart:convert';
import 'package:http/http.dart' as http;

import '../../../../Modules/APIManager/APIManager.dart';
import '../../../../Modules/constants/APIConstants.dart';

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

  Future<http.Response> getOrganization(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse('${APIManager.kD2DBaseURL}${APIConstants.kGetBindOrg}'),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getUserCreatedBy(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetIs24By7IsAccountCreatedFlag}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> insertCallinglog(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kInsertBeneficiaryCallingLog}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> insertCallinglogNew(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kInsertBeneficiaryCallingLogV1}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> apiKeyForAgentId(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetT2TCallingAPIDetails}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> apiKeyForMyoperator(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetOrganisationWiseAPIKeyV1}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> myOperatorAPIDetails(
    Map<String, dynamic> payload,
    String apiKey,
  ) async {
    var header = {'Content-Type': 'application/json', 'x-api-key': apiKey};

    return await http.post(
      Uri.parse('${APIManager.kMyOperator}${APIConstants.kobdapiv1}'),
      body: json.encode(payload),
      headers: header,
    );
  }

  // Future<http.Response> apiKeyForCallPAtching(
  //   Map<String, dynamic> payload,
  // ) async {
  //   var header = {'Content-Type': 'application/x-www-form-urlencoded'};
  //   return await http.post(
  //     Uri.parse(
  //       '${APIManager.kD2DBaseURL}${APIConstants.kGetOrganisationWiseAPIKey}',
  //     ),
  //     body: payload,
  //     headers: header,
  //   );
  // }

  Future<http.Response> twentyFourBySevenForWithAgentId(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse('${APIManager.kTwentyFourBySeven}${APIConstants.kaddcalldata}'),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> twentyFourBySevenForWithCallPatching(
    Map<String, dynamic> payload,
  ) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse('${APIManager.kTwentyFourBySeven}${APIConstants.kclickToCall}'),
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

  //  Future<http.Response> insertDependent(InsertDependentRequest request) async {
  //     var uri = Uri.parse('${APIManager.kCallingBaseURL}$kInsertBeneficiaryCallingAppointmentDetails_Mob_New');

  //     Map<String, String> body = {
  //       'AssignCallID': request.AssignCallID.toString(),
  //       'CallStatusID': request.CallStatusID.toString(),
  //       'RegisteredAddress': request.RegisteredAddress,
  //       'CurrentAddress': request.CurrentAddress,
  //       'IsCurrentSameAsRegd': request.IsCurrentSameAsRegd.toString(),
  //       'RegMobileNo': request.RegMobileNo,
  //       'AltMobileNo': request.AltMobileNo,
  //       'Pincode': request.Pincode,
  //       'Landmark': request.Landmark,
  //       'WorkersGender': request.WorkersGender,
  //       'WorkersMaritalStatus': request.WorkersMaritalStatus.toString(),
  //       'NoOfDependants': request.NoOfDependants.toString(),
  //       'DependantScreeningPending': request.DependantScreeningPending.toString(),
  //       'AppoinmentDate': request.AppoinmentDate,
  //       'AppoinmentTime': request.AppoinmentTime,
  //       'Remark': request.Remark,
  //       'DISTLGDCODE': request.DISTLGDCODE.toString(),
  //       'TALLGDCODE': request.TALLGDCODE.toString(),
  //       'HouseNo': request.HouseNo,
  //       'Road': request.Road,
  //       'Area': request.Area,
  //       'DependantDetails': request.DependantDetails,
  //       'CReatedBy': request.CReatedBy,
  //       'WorkerScreeningStatus': request.WorkerScreeningStatus,
  //       'RemarkID': request.RemarkID.toString(),
  //     };

  //     var headers = {'Content-Type': 'application/x-www-form-urlencoded'};

  //     return await http.post(uri, body: body, headers: headers);
  //   }

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
