// ignore_for_file: must_be_immutable

part of 'expected_beneficiary_bloc.dart';

class ExpectedBeneficiaryState extends Equatable {
  ExpectedBeneficiaryState({
    required this.beneficiaryResponse,
    required,
    required this.beneficiaryStatus,
    required this.getCallStatus,
    required this.getCallingResponse,
    required this.teamStatus,
    required this.teamResponse,
    required this.addDependentOutput,
    required this.addDependentStatus,
    required this.getRealtionStatus,
    required this.getRelationResponse,
    required this.getCallStatusForAppointment,
    required this.getCallingForAppointmentResponse,
    required this.getRemarkStatus,
    required this.getRemarkResponse,
    required this.getAddressDetailStatus,
    required this.getAddressDetailResponse,
    required this.getDependentResponse,
    required this.getDependentStatus,
    required this.insertAppointmentResponse,
    required this.insertAppointmentStatus,
    required this.dateTypeWiseDataResponse,
    required this.dateTypeWiseDataStatus,
    required this.getappointmentstatus,
    required this.getappointmentResponse,
    required this.screenedDependetStatus,
    required this.screenedDependentResponse,
  });

  FormzSubmissionStatus beneficiaryStatus;
  String beneficiaryResponse;

  FormzSubmissionStatus getappointmentstatus;
  String getappointmentResponse;

  FormzSubmissionStatus screenedDependetStatus;
  String screenedDependentResponse;

  FormzSubmissionStatus getCallStatus;
  String getCallingResponse;

  FormzSubmissionStatus getDependentStatus;
  String getDependentResponse;

  FormzSubmissionStatus getRemarkStatus;
  String getRemarkResponse;

  FormzSubmissionStatus getAddressDetailStatus;
  String getAddressDetailResponse;

  FormzSubmissionStatus dateTypeWiseDataStatus;
  String dateTypeWiseDataResponse;

  FormzSubmissionStatus getCallStatusForAppointment;
  String getCallingForAppointmentResponse;

  FormzSubmissionStatus insertAppointmentStatus;
  String insertAppointmentResponse;

  FormzSubmissionStatus teamStatus;
  String teamResponse;

  FormzSubmissionStatus getRealtionStatus;
  String getRelationResponse;

  FormzSubmissionStatus addDependentStatus;

  List<AddDependentOutput> addDependentOutput;

  ExpectedBeneficiaryState copyWith({
    FormzSubmissionStatus? beneficiaryStatus,
    String? beneficiaryResponse,
    FormzSubmissionStatus? getCallStatus,
    FormzSubmissionStatus? getCallStatusForAppointment,
    String? getCallingResponse,
    String? getCallingForAppointmentResponse,
    FormzSubmissionStatus? teamStatus,
    String? teamResponse,
    FormzSubmissionStatus? addDependentStatus,
    List<AddDependentOutput>? addDependentModel,
    FormzSubmissionStatus? getRealtionStatus,
    FormzSubmissionStatus? getRemarkStatus,
    String? getRelationResponse,
    String? getRemarkResponse,
    String? getAddressDetailResponse,
    FormzSubmissionStatus? getAddressDetailStatus,
    FormzSubmissionStatus? getDependentStatus,
    FormzSubmissionStatus? insertAppointmentStatus,
    FormzSubmissionStatus? organizationStatus,
    FormzSubmissionStatus? usercreatedStatus,
    FormzSubmissionStatus? insertCallLogStatus,
    FormzSubmissionStatus? agentIdStatus,
    FormzSubmissionStatus? dateTypeWiseDataStatus,
    FormzSubmissionStatus? getappointmentstatus,
    FormzSubmissionStatus? myoperatorStatus,
    FormzSubmissionStatus? getMyoperatorDetailsStatus,
    FormzSubmissionStatus? screenedDependetStatus,
    String? getDependentResponse,
    String? getMyoperatorDetailsResponse,
    String? myOperatorResponse,
    String? organizationResponse,
    String? insertAppointmentResponse,
    String? userCreatedResponse,
    String? inserCallLogResponse,
    String? inserCallLogNewResponse,
    String? agetIdResponse,
    String? apiKeyAgentResponse,
    String? twentyFourBySevenForAgentIdResponse,
    String? apiKeyCallPatchingResponse,
    String? twentyFourBySevenForCallPatchingResponse,
    String? dateTypeWiseDataResponse,
    String? screenedDependentResponse,
    String? getappointmentResponse,
  }) {
    return ExpectedBeneficiaryState(
      beneficiaryResponse: beneficiaryResponse ?? this.beneficiaryResponse,
      beneficiaryStatus: beneficiaryStatus ?? this.beneficiaryStatus,
      getCallingResponse: getCallingResponse ?? this.getCallingResponse,
      getCallStatus: getCallStatus ?? this.getCallStatus,
      teamStatus: teamStatus ?? this.teamStatus,
      teamResponse: teamResponse ?? this.teamResponse,
      addDependentOutput: addDependentModel ?? addDependentOutput,
      addDependentStatus: addDependentStatus ?? this.addDependentStatus,
      getRealtionStatus: getRealtionStatus ?? this.getRealtionStatus,
      getRelationResponse: getRelationResponse ?? this.getRelationResponse,
      getCallStatusForAppointment:
          getCallStatusForAppointment ?? this.getCallStatusForAppointment,
      getCallingForAppointmentResponse:
          getCallingForAppointmentResponse ??
          this.getCallingForAppointmentResponse,
      getRemarkResponse: getRemarkResponse ?? this.getRemarkResponse,
      getRemarkStatus: getRemarkStatus ?? this.getRemarkStatus,
      getAddressDetailResponse:
          getAddressDetailResponse ?? this.getAddressDetailResponse,
      getAddressDetailStatus:
          getAddressDetailStatus ?? this.getAddressDetailStatus,
      getDependentStatus: getDependentStatus ?? this.getDependentStatus,
      getDependentResponse: getDependentResponse ?? this.getDependentResponse,
      insertAppointmentResponse:
          insertAppointmentResponse ?? this.insertAppointmentResponse,
      insertAppointmentStatus:
          insertAppointmentStatus ?? this.insertAppointmentStatus,
      dateTypeWiseDataResponse:
          dateTypeWiseDataResponse ?? this.dateTypeWiseDataResponse,
      dateTypeWiseDataStatus:
          dateTypeWiseDataStatus ?? this.dateTypeWiseDataStatus,
      getappointmentResponse:
          getappointmentResponse ?? this.getappointmentResponse,
      getappointmentstatus: getappointmentstatus ?? this.getappointmentstatus,
      screenedDependetStatus:
          screenedDependetStatus ?? this.screenedDependetStatus,
      screenedDependentResponse:
          screenedDependentResponse ?? this.screenedDependentResponse,
    );
  }

  @override
  List<Object> get props => [
    beneficiaryStatus,
    beneficiaryResponse,
    getCallStatus,
    getCallingResponse,
    getCallStatusForAppointment,
    getCallingForAppointmentResponse,
    teamStatus,
    teamResponse,
    addDependentOutput,
    addDependentStatus,
    getRelationResponse,
    getRealtionStatus,
    getRemarkResponse,
    getRemarkStatus,
    getAddressDetailStatus,
    getAddressDetailResponse,
    getDependentStatus,
    getDependentResponse,
    insertAppointmentResponse,
    insertAppointmentStatus,
    dateTypeWiseDataResponse,
    dateTypeWiseDataStatus,
    getappointmentResponse,
    getappointmentstatus,
    screenedDependetStatus,
    screenedDependentResponse,
  ];
}
