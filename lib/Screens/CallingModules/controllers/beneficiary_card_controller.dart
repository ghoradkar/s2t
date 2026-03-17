// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/CallingModules/repository/beneficiary_card_repository.dart';

import '../../../../Modules/utilities/DataProvider.dart';

class BeneficiaryCardController extends GetxController {
  final BeneficiaryCardRepository repository;

  BeneficiaryCardController({required this.repository});

  // ─── Insert Appointment ───────────────────────────────────────────────────
  final insertAppointmentStatus = FormzSubmissionStatus.initial.obs;
  final insertAppointmentResponse = ''.obs;

  // ─── Organization ─────────────────────────────────────────────────────────
  final organizationStatus = FormzSubmissionStatus.initial.obs;
  final organizationResponse = ''.obs;

  // ─── User Created By ──────────────────────────────────────────────────────
  final usercreatedStatus = FormzSubmissionStatus.initial.obs;
  final userCreatedResponse = ''.obs;

  // ─── Insert Call Log New ──────────────────────────────────────────────────
  final insertClStatus = FormzSubmissionStatus.initial.obs;
  final insertClResponse = ''.obs;

  // ─── Agent ID ─────────────────────────────────────────────────────────────
  final agentIdStatus = FormzSubmissionStatus.initial.obs;
  final agetIdResponse = ''.obs;

  // ─── API Key for Agent ID ─────────────────────────────────────────────────
  final apikeyAgentIdStatus = FormzSubmissionStatus.initial.obs;
  final apiKeyAgentResponse = ''.obs;

  // ─── 24x7 for Agent ID ───────────────────────────────────────────────────
  final twentyFourBySevenForAgentIdStatus = FormzSubmissionStatus.initial.obs;
  final twentyFourBySevenForAgentIdResponse = ''.obs;

  // ─── API Key for Call Patching ────────────────────────────────────────────
  final apikeyCallPatchingStatus = FormzSubmissionStatus.initial.obs;
  final apiKeyCallPatchingResponse = ''.obs;

  // ─── 24x7 for Call Patching ───────────────────────────────────────────────
  final twentyFourBySevenForCallPatchingStatus =
      FormzSubmissionStatus.initial.obs;
  final twentyFourBySevenForCallPatchingResponse = ''.obs;

  // ─── My Operator ──────────────────────────────────────────────────────────
  final myoperatorStatus = FormzSubmissionStatus.initial.obs;
  final myOperatorResponse = ''.obs;

  // ─── Get My Operator Details ──────────────────────────────────────────────
  final getMyoperatorDetailsStatus = FormzSubmissionStatus.initial.obs;
  final getMyoperatorDetailsResponse = ''.obs;

  // ─── Insert Call Log (legacy) ─────────────────────────────────────────────
  final insertCallLogStatus = FormzSubmissionStatus.initial.obs;

  // ─────────────────────────────────────────────────────────────────────────
  // resetState  (mirrors ResetBaneficiaryCardState event)
  // ─────────────────────────────────────────────────────────────────────────
  void resetState() {
    insertAppointmentStatus.value = FormzSubmissionStatus.initial;
    organizationStatus.value = FormzSubmissionStatus.initial;
    usercreatedStatus.value = FormzSubmissionStatus.initial;
    insertCallLogStatus.value = FormzSubmissionStatus.initial;
    insertClStatus.value = FormzSubmissionStatus.initial;
    agentIdStatus.value = FormzSubmissionStatus.initial;
    apikeyAgentIdStatus.value = FormzSubmissionStatus.initial;
    twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
    apikeyCallPatchingStatus.value = FormzSubmissionStatus.initial;
    twentyFourBySevenForCallPatchingStatus.value =
        FormzSubmissionStatus.initial;
    myoperatorStatus.value = FormzSubmissionStatus.initial;
    getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchOrganization  (mirrors GetOrganization event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchOrganization(Map<String, dynamic> payload) async {
    try {
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      organizationStatus.value = FormzSubmissionStatus.inProgress;

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      payload.addAll({'UserID': empCode.toString()});

      var res = await repository.getOrganization(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          organizationResponse.value = resString;
          organizationStatus.value = FormzSubmissionStatus.success;
        } else {
          organizationResponse.value = resString;
          organizationStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        organizationResponse.value = res.reasonPhrase ?? '';
        organizationStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      organizationResponse.value = e.toString();
      organizationStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchUserCreatedBy  (mirrors GetUserCreatedBy event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchUserCreatedBy(Map<String, dynamic> payload) async {
    try {
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      userCreatedResponse.value = '';
      usercreatedStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.getUserCreatedBy(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          userCreatedResponse.value = resString;
          usercreatedStatus.value = FormzSubmissionStatus.success;
        } else {
          userCreatedResponse.value = resString;
          usercreatedStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        userCreatedResponse.value = res.reasonPhrase ?? '';
        usercreatedStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      userCreatedResponse.value = e.toString();
      usercreatedStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // insertCallLogNew  (mirrors InsertCallLogNew event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> insertCallLogNew(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      insertClStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.insertCallinglogNew(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          insertClResponse.value = resString;
          insertClStatus.value = FormzSubmissionStatus.success;
        } else {
          insertClResponse.value = resString;
          insertClStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        insertClResponse.value = res.reasonPhrase ?? '';
        insertClStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      insertClResponse.value = e.toString();
      insertClStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // twentyFourBySevenWithAgentID  (mirrors TwentyFourBySevenWithAgentID event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> twentyFourBySevenWithAgentID(
    Map<String, dynamic> payload,
  ) async {
    try {
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdResponse.value = '';
      twentyFourBySevenForAgentIdStatus.value =
          FormzSubmissionStatus.inProgress;

      var res =
          await repository.twentyFourBySevenForWithAgentId(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['statusMessage'] == 'success') {
          twentyFourBySevenForAgentIdResponse.value = resString;
          twentyFourBySevenForAgentIdStatus.value =
              FormzSubmissionStatus.success;
        } else {
          twentyFourBySevenForAgentIdResponse.value = resString;
          twentyFourBySevenForAgentIdStatus.value =
              FormzSubmissionStatus.failure;
        }
      } else {
        twentyFourBySevenForAgentIdResponse.value =
            res.reasonPhrase ?? '';
        twentyFourBySevenForAgentIdStatus.value =
            FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      twentyFourBySevenForAgentIdResponse.value = e.toString();
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForCallPatching  (mirrors GetAPIKeyForCallPAtching event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForCallPatching(
    Map<String, dynamic> payload,
  ) async {
    try {
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyCallPatchingResponse.value = '';
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForCallPAtching(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          apiKeyCallPatchingResponse.value = resString;
          apikeyCallPatchingStatus.value = FormzSubmissionStatus.success;
        } else {
          apiKeyCallPatchingResponse.value = resString;
          apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        apiKeyCallPatchingResponse.value = res.reasonPhrase ?? '';
        apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      apiKeyCallPatchingResponse.value = e.toString();
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForAgentId  (mirrors GetAPIKeyForAgentId event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForAgentId(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyAgentResponse.value = '';
      apikeyAgentIdStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForAgentId(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          apiKeyAgentResponse.value = resString;
          apikeyAgentIdStatus.value = FormzSubmissionStatus.success;
        } else {
          apiKeyAgentResponse.value = resString;
          apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        apiKeyAgentResponse.value = res.reasonPhrase ?? '';
        apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      apiKeyAgentResponse.value = e.toString();
      apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // twentyFourBySevenWithCallPatching
  // (mirrors TwentyFourBySevenWithCallPatching event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> twentyFourBySevenWithCallPatching(
    Map<String, dynamic> payload,
  ) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      twentyFourBySevenForAgentIdResponse.value = '';
      twentyFourBySevenForCallPatchingResponse.value = '';
      twentyFourBySevenForCallPatchingStatus.value =
          FormzSubmissionStatus.inProgress;

      var res =
          await repository.twentyFourBySevenForWithCallPatching(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          twentyFourBySevenForCallPatchingResponse.value = resString;
          twentyFourBySevenForCallPatchingStatus.value =
              FormzSubmissionStatus.success;
        } else {
          twentyFourBySevenForCallPatchingResponse.value = resString;
          twentyFourBySevenForCallPatchingStatus.value =
              FormzSubmissionStatus.failure;
        }
      } else {
        twentyFourBySevenForCallPatchingResponse.value =
            res.reasonPhrase ?? '';
        twentyFourBySevenForCallPatchingStatus.value =
            FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      twentyFourBySevenForCallPatchingResponse.value = e.toString();
      twentyFourBySevenForCallPatchingStatus.value =
          FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // myOperator  (mirrors MyOperator event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> myOperator(
    Map<String, dynamic> payload,
    String apiKey,
  ) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      myOperatorResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      twentyFourBySevenForAgentIdResponse.value = '';
      myoperatorStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.myOperatorAPIDetails(payload, apiKey);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          myOperatorResponse.value = resString;
          myoperatorStatus.value = FormzSubmissionStatus.success;
        } else {
          myOperatorResponse.value = resString;
          myoperatorStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        myOperatorResponse.value = res.reasonPhrase ?? '';
        myoperatorStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      myOperatorResponse.value = e.toString();
      myoperatorStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForMyoperator  (mirrors GetAPIKeyForMyoperator event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForMyoperator(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyCallPatchingResponse.value = '';
      getMyoperatorDetailsResponse.value = '';
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForMyoperator(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getMyoperatorDetailsResponse.value = resString;
          getMyoperatorDetailsStatus.value = FormzSubmissionStatus.success;
        } else {
          getMyoperatorDetailsResponse.value = resString;
          getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getMyoperatorDetailsResponse.value = res.reasonPhrase ?? '';
        getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getMyoperatorDetailsResponse.value = e.toString();
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
    }
  }
}
