// ignore_for_file: must_be_immutable

part of 'beneficiary_card_bloc_bloc.dart';

class BeneficiaryCardBlocState extends Equatable {
  BeneficiaryCardBlocState({
    required this.insertAppointmentResponse,
    required this.insertAppointmentStatus,
    required this.organizationStatus,
    required this.organizationResponse,
    required this.usercreatedStatus,
    required this.userCreatedResponse,

    required this.insertClStatus,
    required this.insertClResponse,
    required this.agentIdStatus,
    required this.agetIdResponse,
    required this.apikeyAgentIdStatus,
    required this.apiKeyAgentResponse,
    required this.twentyFourBySevenForAgentIdStatus,
    required this.twentyFourBySevenForAgentIdResponse,
    required this.apiKeyCallPatchingResponse,
    required this.apikeyCallPatchingStatus,
    required this.twentyFourBySevenForCallPatchingResponse,
    required this.twentyFourBySevenForCallPatchingStatus,

    required this.myoperatorStatus,
    required this.myOperatorResponse,
    required this.getMyoperatorDetailsStatus,
    required this.getMyoperatorDetailsResponse,
  });

  FormzSubmissionStatus getMyoperatorDetailsStatus;
  String getMyoperatorDetailsResponse;
  //
  // FormzSubmissionStatus vodaphoneApiDetails;
  // String getMyoperatorDetailsResponse;

  FormzSubmissionStatus apikeyAgentIdStatus;
  String apiKeyAgentResponse;

  FormzSubmissionStatus apikeyCallPatchingStatus;
  String apiKeyCallPatchingResponse;

  FormzSubmissionStatus agentIdStatus;
  String agetIdResponse;

  FormzSubmissionStatus insertClStatus;
  String insertClResponse;

  FormzSubmissionStatus twentyFourBySevenForAgentIdStatus;
  String twentyFourBySevenForAgentIdResponse;

  FormzSubmissionStatus twentyFourBySevenForCallPatchingStatus;
  String twentyFourBySevenForCallPatchingResponse;

  FormzSubmissionStatus myoperatorStatus;
  String myOperatorResponse;

  FormzSubmissionStatus usercreatedStatus;
  String userCreatedResponse;

  FormzSubmissionStatus organizationStatus;
  String organizationResponse;

  FormzSubmissionStatus insertAppointmentStatus;
  String insertAppointmentResponse;

  BeneficiaryCardBlocState copyWith({
    FormzSubmissionStatus? insertAppointmentStatus,
    FormzSubmissionStatus? organizationStatus,
    FormzSubmissionStatus? usercreatedStatus,
    FormzSubmissionStatus? insertCallLogStatus,
    FormzSubmissionStatus? insertClStatus,
    FormzSubmissionStatus? agentIdStatus,
    FormzSubmissionStatus? apikeyAgentIdStatus,
    FormzSubmissionStatus? twentyFourBySevenForAgentIdStatus,
    FormzSubmissionStatus? apikeyCallPatchingStatus,
    FormzSubmissionStatus? twentyFourBySevenForCallPatchingStatus,
    FormzSubmissionStatus? myoperatorStatus,
    FormzSubmissionStatus? getMyoperatorDetailsStatus,
    String? getMyoperatorDetailsResponse,
    String? myOperatorResponse,
    String? organizationResponse,
    String? insertAppointmentResponse,
    String? userCreatedResponse,
    String? inserCallLogResponse,
    String? insertClResponse,
    String? agetIdResponse,
    String? apiKeyAgentResponse,
    String? twentyFourBySevenForAgentIdResponse,
    String? apiKeyCallPatchingResponse,
    String? twentyFourBySevenForCallPatchingResponse,
    String? getappointmentResponse,
  }) {
    return BeneficiaryCardBlocState(
      insertAppointmentResponse:
          insertAppointmentResponse ?? this.insertAppointmentResponse,
      insertAppointmentStatus:
          insertAppointmentStatus ?? this.insertAppointmentStatus,
      organizationStatus: organizationStatus ?? this.organizationStatus,
      organizationResponse: organizationResponse ?? this.organizationResponse,
      usercreatedStatus: usercreatedStatus ?? this.usercreatedStatus,
      userCreatedResponse: userCreatedResponse ?? this.userCreatedResponse,

      insertClStatus: insertClStatus ?? this.insertClStatus,
      insertClResponse: insertClResponse ?? this.insertClResponse,
      agentIdStatus: agentIdStatus ?? this.agentIdStatus,
      agetIdResponse: agetIdResponse ?? this.agetIdResponse,
      apikeyAgentIdStatus: apikeyAgentIdStatus ?? this.apikeyAgentIdStatus,
      apiKeyAgentResponse: apiKeyAgentResponse ?? this.apiKeyAgentResponse,
      twentyFourBySevenForAgentIdStatus:
          twentyFourBySevenForAgentIdStatus ??
          this.twentyFourBySevenForAgentIdStatus,
      twentyFourBySevenForAgentIdResponse:
          twentyFourBySevenForAgentIdResponse ??
          this.twentyFourBySevenForAgentIdResponse,
      apiKeyCallPatchingResponse:
          apiKeyCallPatchingResponse ?? this.apiKeyCallPatchingResponse,
      apikeyCallPatchingStatus:
          apikeyCallPatchingStatus ?? this.apikeyCallPatchingStatus,
      twentyFourBySevenForCallPatchingResponse:
          twentyFourBySevenForCallPatchingResponse ??
          this.twentyFourBySevenForCallPatchingResponse,
      twentyFourBySevenForCallPatchingStatus:
          twentyFourBySevenForCallPatchingStatus ??
          this.twentyFourBySevenForCallPatchingStatus,

      myoperatorStatus: myoperatorStatus ?? this.myoperatorStatus,
      myOperatorResponse: myOperatorResponse ?? this.myOperatorResponse,
      getMyoperatorDetailsStatus:
          getMyoperatorDetailsStatus ?? this.getMyoperatorDetailsStatus,
      getMyoperatorDetailsResponse:
          getMyoperatorDetailsResponse ?? this.getMyoperatorDetailsResponse,
    );
  }

  @override
  List<Object> get props => [
    insertAppointmentResponse,
    insertAppointmentStatus,
    organizationResponse,
    organizationStatus,
    userCreatedResponse,
    usercreatedStatus,

    insertClResponse,
    insertClStatus,
    agentIdStatus,
    agetIdResponse,
    apiKeyAgentResponse,
    twentyFourBySevenForAgentIdResponse,
    agentIdStatus,
    twentyFourBySevenForAgentIdStatus,
    apiKeyCallPatchingResponse,
    apikeyCallPatchingStatus,
    twentyFourBySevenForCallPatchingResponse,
    twentyFourBySevenForCallPatchingStatus,
    myoperatorStatus,
    myOperatorResponse,
    getMyoperatorDetailsStatus,
    getMyoperatorDetailsResponse,
  ];
}
