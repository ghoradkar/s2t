// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/CallingModules/models/add_dependent_model.dart';
import 'package:s2toperational/Screens/CallingModules/repository/beneficiary_repository.dart';

import '../../../../Modules/utilities/DataProvider.dart';

class ExpectedBeneficiaryController extends GetxController {
  final BeneficiaryRepository repository;

  ExpectedBeneficiaryController({required this.repository});

  // ─── Beneficiary list ────────────────────────────────────────────────────────
  final beneficiaryStatus = FormzSubmissionStatus.initial.obs;
  final beneficiaryResponse = ''.obs;

  // ─── Call status ─────────────────────────────────────────────────────────────
  final getCallStatus = FormzSubmissionStatus.initial.obs;
  final getCallingResponse = ''.obs;

  // ─── Team ────────────────────────────────────────────────────────────────────
  final teamStatus = FormzSubmissionStatus.initial.obs;
  final teamResponse = ''.obs;

  // ─── Add dependent ───────────────────────────────────────────────────────────
  final addDependentStatus = FormzSubmissionStatus.initial.obs;
  final addDependentOutput = <AddDependentOutput>[].obs;

  // ─── Relation ────────────────────────────────────────────────────────────────
  final getRealtionStatus = FormzSubmissionStatus.initial.obs;
  final getRelationResponse = ''.obs;

  // ─── Call status for appointment ─────────────────────────────────────────────
  final getCallStatusForAppointment = FormzSubmissionStatus.initial.obs;
  final getCallingForAppointmentResponse = ''.obs;

  // ─── Remark ──────────────────────────────────────────────────────────────────
  final getRemarkStatus = FormzSubmissionStatus.initial.obs;
  final getRemarkResponse = ''.obs;

  // ─── Address detail ──────────────────────────────────────────────────────────
  final getAddressDetailStatus = FormzSubmissionStatus.initial.obs;
  final getAddressDetailResponse = ''.obs;

  // ─── Dependent details ───────────────────────────────────────────────────────
  final getDependentStatus = FormzSubmissionStatus.initial.obs;
  final getDependentResponse = ''.obs;

  // ─── Insert appointment ──────────────────────────────────────────────────────
  final insertAppointmentStatus = FormzSubmissionStatus.initial.obs;
  final insertAppointmentResponse = ''.obs;

  // ─── Date type wise ──────────────────────────────────────────────────────────
  final dateTypeWiseDataStatus = FormzSubmissionStatus.initial.obs;
  final dateTypeWiseDataResponse = ''.obs;

  // ─── Appointment count ───────────────────────────────────────────────────────
  final getappointmentstatus = FormzSubmissionStatus.initial.obs;
  final getappointmentResponse = ''.obs;

  // ─── Screened dependent ──────────────────────────────────────────────────────
  final screenedDependetStatus = FormzSubmissionStatus.initial.obs;
  final screenedDependentResponse = ''.obs;

  // ─────────────────────────────────────────────────────────────────────────────
  // resetState  (mirrors ResetExpectedBeneficiaryState event)
  // ─────────────────────────────────────────────────────────────────────────────
  void resetState() {
    beneficiaryStatus.value = FormzSubmissionStatus.initial;
    beneficiaryResponse.value = '';
    getCallStatus.value = FormzSubmissionStatus.initial;
    getCallingResponse.value = '';
    teamStatus.value = FormzSubmissionStatus.initial;
    teamResponse.value = '';
    addDependentStatus.value = FormzSubmissionStatus.initial;
    getRealtionStatus.value = FormzSubmissionStatus.initial;
    getRelationResponse.value = '';
    getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
    getCallingForAppointmentResponse.value = '';
    getRemarkStatus.value = FormzSubmissionStatus.initial;
    getRemarkResponse.value = '';
    getAddressDetailStatus.value = FormzSubmissionStatus.initial;
    getAddressDetailResponse.value = '';
    getDependentStatus.value = FormzSubmissionStatus.initial;
    getDependentResponse.value = '';
    insertAppointmentStatus.value = FormzSubmissionStatus.initial;
    insertAppointmentResponse.value = '';
    dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;
    dateTypeWiseDataResponse.value = '';
    getappointmentstatus.value = FormzSubmissionStatus.initial;
    getappointmentResponse.value = '';
    screenedDependetStatus.value = FormzSubmissionStatus.initial;
    screenedDependentResponse.value = '';
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchBeneficiaries  (mirrors BeneficiaryRequest event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchBeneficiaries(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.inProgress;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      getAddressDetailResponse.value = '';
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      getappointmentstatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      final Map<String, dynamic> mutablePayload = {
        ...payload,
        'UserID': empCode.toString(),
      };
      var res = await repository.expectedBeneficiaryList(mutablePayload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          beneficiaryResponse.value = resString;
          beneficiaryStatus.value = FormzSubmissionStatus.success;
        } else {
          beneficiaryResponse.value = resString;
          beneficiaryStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        beneficiaryResponse.value = res.reasonPhrase ?? '';
        beneficiaryStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchCallStatus  (mirrors GetCallStatusRequest event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchCallStatus() async {
    try {
      getCallStatus.value = FormzSubmissionStatus.inProgress;
      getCallingResponse.value = '';
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';

      var res = await repository.getCallStatus();

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getCallingResponse.value = resString;
          getCallStatus.value = FormzSubmissionStatus.success;
        } else {
          getCallingResponse.value = resString;
          getCallStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getCallingResponse.value = res.reasonPhrase ?? '';
        getCallStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getCallingResponse.value = e.toString();
      getCallStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchTeamData  (mirrors TeamStatusRequest event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchTeamData(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.inProgress;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getCallStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRelationResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      final Map<String, dynamic> mutablePayload = {
        ...payload,
        'UserID': empCode.toString(),
      };
      var res = await repository.getTeamData(mutablePayload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          teamResponse.value = resString;
          teamStatus.value = FormzSubmissionStatus.success;
        } else {
          teamResponse.value = resString;
          teamStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        teamResponse.value = res.reasonPhrase ?? '';
        teamStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      teamResponse.value = e.toString();
      teamStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // addDependent  (mirrors AddDependentRequest event)
  // ─────────────────────────────────────────────────────────────────────────────
  void addDependent(List<AddDependentOutput> list) {
    try {
      addDependentStatus.value = FormzSubmissionStatus.inProgress;
      getCallStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;

      addDependentOutput.value = List.from(list);
      addDependentStatus.value = FormzSubmissionStatus.success;
    } catch (e) {
      print(e);
      addDependentStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // deleteDependent  (mirrors DeleteDependentRequest event)
  // ─────────────────────────────────────────────────────────────────────────────
  void deleteDependent(int index) {
    try {
      addDependentOutput.removeAt(index);
      addDependentStatus.value = FormzSubmissionStatus.success;
    } catch (e) {
      print(e);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchRelationData  (mirrors GetRealationData event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchRelationData(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.inProgress;
      getRelationResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      getDependentResponse.value = '';

      var res = await repository.getRelation(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getRelationResponse.value = resString;
          getRealtionStatus.value = FormzSubmissionStatus.success;
        } else {
          getRelationResponse.value = resString;
          getRealtionStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getRelationResponse.value = res.reasonPhrase ?? '';
        getRealtionStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getRelationResponse.value = e.toString();
      getRealtionStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchCallStatusForAppointment  (mirrors GetCallStatusForAppointment event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchCallStatusForAppointment(
    Map<String, dynamic> payload,
  ) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      screenedDependetStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.inProgress;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';

      var res = await repository.getCallStatusForAppointment(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getCallingForAppointmentResponse.value = resString;
          getCallStatusForAppointment.value = FormzSubmissionStatus.success;
        } else {
          getCallingForAppointmentResponse.value = resString;
          getCallStatusForAppointment.value = FormzSubmissionStatus.failure;
        }
      } else {
        getCallingForAppointmentResponse.value = res.reasonPhrase ?? '';
        getCallStatusForAppointment.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getCallingForAppointmentResponse.value = e.toString();
      getCallStatusForAppointment.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchRemarks  (mirrors GetRemark event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchRemarks(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.getRemark(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getRemarkResponse.value = resString;
          getRemarkStatus.value = FormzSubmissionStatus.success;
        } else {
          getRemarkResponse.value = resString;
          getRemarkStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getRemarkResponse.value = res.reasonPhrase ?? '';
        getRemarkStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getRemarkResponse.value = e.toString();
      getRemarkStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchAddressDetails  (mirrors GetAddressDetails event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchAddressDetails(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.inProgress;
      getAddressDetailResponse.value = '';

      var res = await repository.getAddressDetails(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getAddressDetailResponse.value = resString;
          getAddressDetailStatus.value = FormzSubmissionStatus.success;
        } else {
          getAddressDetailResponse.value = resString;
          getAddressDetailStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getAddressDetailResponse.value = res.reasonPhrase ?? '';
        getAddressDetailStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getAddressDetailResponse.value = e.toString();
      getAddressDetailStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchDependentDetails  (mirrors GetDependentDetails event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchDependentDetails(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.inProgress;
      getAddressDetailResponse.value = '';
      getDependentResponse.value = '';

      var res = await repository.getDependentDetails(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getDependentResponse.value = resString;
          getDependentStatus.value = FormzSubmissionStatus.success;
        } else {
          getDependentResponse.value = resString;
          getDependentStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getDependentResponse.value = res.reasonPhrase ?? '';
        getDependentStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getDependentResponse.value = e.toString();
      getDependentStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // insertAppointment  (mirrors InsertAppointmentDetails event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> insertAppointment(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.inProgress;
      getAddressDetailResponse.value = '';
      insertAppointmentResponse.value = '';
      getDependentResponse.value = '';

      var res = await repository.insertAppointmentData(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          insertAppointmentResponse.value = resString;
          insertAppointmentStatus.value = FormzSubmissionStatus.success;
        } else {
          insertAppointmentResponse.value = resString;
          insertAppointmentStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        insertAppointmentResponse.value = res.reasonPhrase ?? '';
        insertAppointmentStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      insertAppointmentResponse.value = e.toString();
      insertAppointmentStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchBeneficiariesByDateType  (mirrors BeneficiaryRequestForDateType event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchBeneficiariesByDateType(
    Map<String, dynamic> payload,
  ) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      getAddressDetailResponse.value = '';
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.inProgress;
      getDependentResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      payload.addAll({'UserID': empCode.toString()});
      var res = await repository.expectedBeneficiaryListNew(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          dateTypeWiseDataResponse.value = resString;
          beneficiaryResponse.value = resString;
          dateTypeWiseDataStatus.value = FormzSubmissionStatus.success;
          beneficiaryStatus.value = FormzSubmissionStatus.success;
        } else {
          dateTypeWiseDataResponse.value = resString;
          beneficiaryResponse.value = resString;
          dateTypeWiseDataStatus.value = FormzSubmissionStatus.failure;
          beneficiaryStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        dateTypeWiseDataResponse.value = res.reasonPhrase ?? '';
        beneficiaryResponse.value = res.reasonPhrase ?? '';
        dateTypeWiseDataStatus.value = FormzSubmissionStatus.failure;
        beneficiaryStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      dateTypeWiseDataResponse.value = e.toString();
      beneficiaryResponse.value = e.toString();
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.failure;
      beneficiaryStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchAppointmentCount  (mirrors GetAppointmentCount event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchAppointmentCount(Map<String, dynamic> payload) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      getAddressDetailResponse.value = '';
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      dateTypeWiseDataStatus.value = FormzSubmissionStatus.initial;
      getappointmentstatus.value = FormzSubmissionStatus.inProgress;
      getDependentResponse.value = '';
      getappointmentResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      payload.addAll({'UserID': empCode.toString()});
      var res = await repository.getAppointmentCount(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getappointmentResponse.value = resString;
          getappointmentstatus.value = FormzSubmissionStatus.success;
        } else {
          getappointmentResponse.value = resString;
          getappointmentstatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getappointmentResponse.value = res.reasonPhrase ?? '';
        getappointmentstatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getappointmentResponse.value = e.toString();
      getappointmentstatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // fetchScreenedDependentDetails  (mirrors GetScreenedDependentDetails event)
  // ─────────────────────────────────────────────────────────────────────────────
  Future<void> fetchScreenedDependentDetails(
    Map<String, dynamic> payload,
  ) async {
    try {
      teamResponse.value = '';
      teamStatus.value = FormzSubmissionStatus.initial;
      beneficiaryStatus.value = FormzSubmissionStatus.initial;
      beneficiaryResponse.value = '';
      getCallStatus.value = FormzSubmissionStatus.initial;
      getCallingResponse.value = '';
      getRealtionStatus.value = FormzSubmissionStatus.initial;
      getCallStatusForAppointment.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      getRelationResponse.value = '';
      getCallingForAppointmentResponse.value = '';
      getRemarkResponse.value = '';
      getRemarkStatus.value = FormzSubmissionStatus.initial;
      getAddressDetailStatus.value = FormzSubmissionStatus.initial;
      getDependentStatus.value = FormzSubmissionStatus.initial;
      screenedDependetStatus.value = FormzSubmissionStatus.inProgress;
      getAddressDetailResponse.value = '';
      getDependentResponse.value = '';

      var res = await repository.getScreenedBeneficiaryList(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          screenedDependentResponse.value = resString;
          screenedDependetStatus.value = FormzSubmissionStatus.success;
        } else {
          screenedDependentResponse.value = resString;
          screenedDependetStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        screenedDependentResponse.value = res.reasonPhrase ?? '';
        screenedDependetStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      screenedDependentResponse.value = e.toString();
      screenedDependetStatus.value = FormzSubmissionStatus.failure;
    }
  }
}