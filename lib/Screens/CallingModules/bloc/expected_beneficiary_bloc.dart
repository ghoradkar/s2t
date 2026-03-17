// ignore_for_file: unused_import, avoid_print

import 'dart:async';
import 'dart:convert';

import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:s2toperational/Screens/CallingModules/models/add_dependent_model.dart';
import 'package:s2toperational/Screens/CallingModules/repository/beneficiary_repository.dart';

import '../../../../Modules/utilities/DataProvider.dart';

part 'expected_beneficiary_event.dart';
part 'expected_beneficiary_state.dart';

class ExpectedBeneficiaryBloc
    extends Bloc<ExpectedBeneficiaryEvent, ExpectedBeneficiaryState> {
  final BeneficiaryRepository repository;

  ExpectedBeneficiaryBloc({required this.repository})
    : super(
        ExpectedBeneficiaryState(
          beneficiaryResponse: '',
          beneficiaryStatus: FormzSubmissionStatus.initial,
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: '',
          teamResponse: '',
          teamStatus: FormzSubmissionStatus.initial,
          addDependentOutput: const [],
          addDependentStatus: FormzSubmissionStatus.initial,
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          getRelationResponse: '',
          getCallingForAppointmentResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getRemarkResponse: '',
          getAddressDetailResponse: '',
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getappointmentstatus: FormzSubmissionStatus.initial,
          screenedDependetStatus: FormzSubmissionStatus.initial,
          getDependentResponse: '',
          insertAppointmentResponse: '',
          screenedDependentResponse: '',
          dateTypeWiseDataResponse: '',
          getappointmentResponse: '',
        ),
      ) {
    on<BeneficiaryRequest>(_onBeneficiaryRequest);
    on<GetCallStatusRequest>(_onGetCallStatusRequest);
    on<ResetExpectedBeneficiaryState>(_onResetExpectedBeneficiaryState);
    on<TeamStatusRequest>(_onTeamStatusRequest);
    on<AddDependentRequest>(_onAddDependent);
    on<DeleteDependentRequest>(_onDeleteDependentRequest);
    on<GetRealationData>(_onGetRealationData);
    on<GetCallStatusForAppointment>(_onGetCallStatusForAppointment);
    on<GetRemark>(_onGetRemark);
    on<GetAddressDetails>(_onGetAddressDetails);
    on<GetDependentDetails>(_onGetDependentDetails);
    on<InsertAppointmentDetails>(_onInsertAppointmentDetails);
    on<BeneficiaryRequestForDateType>(_onBeneficiaryRequestForDateType);
    on<GetAppointmentCount>(_onGetAppointmentCount);
    on<GetScreenedDependentDetails>(_onGetScreenedDependentDetails);
  }

  FutureOr<void> _onBeneficiaryRequest(
    BeneficiaryRequest event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.inProgress,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          insertAppointmentResponse: '',
          getAddressDetailResponse: '',
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          getappointmentstatus: FormzSubmissionStatus.initial,
          myoperatorStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
        ),
      );

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      final Map<String, dynamic> mutablePayload = {
        ...event.payload, // Copy all existing entries
        "UserID": empCode.toString(), // Add new entry
      };
      var res = await repository.expectedBeneficiaryList(mutablePayload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              beneficiaryStatus: FormzSubmissionStatus.success,
              beneficiaryResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              beneficiaryStatus: FormzSubmissionStatus.failure,
              beneficiaryResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            beneficiaryStatus: FormzSubmissionStatus.failure,
            beneficiaryResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      // emit(
      //   state.copyWith(
      //     beneficiaryStatus: FormzSubmissionStatus.failure,
      //     beneficiaryResponse: e.toString(),
      //   ),
      // );
    }
  }

  FutureOr<void> _onGetCallStatusRequest(
    GetCallStatusRequest event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          getCallStatus: FormzSubmissionStatus.inProgress,
          getCallingResponse: "",
          beneficiaryStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getRemarkStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getRealtionStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
        ),
      );

      var res = await repository.getCallStatus();

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          emit(
            state.copyWith(
              getCallStatus: FormzSubmissionStatus.success,
              getCallingResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getCallStatus: FormzSubmissionStatus.failure,
              getCallingResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getCallStatus: FormzSubmissionStatus.failure,
            getCallingResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getCallStatus: FormzSubmissionStatus.failure,
          getCallingResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onResetExpectedBeneficiaryState(
    ResetExpectedBeneficiaryState event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) {
    emit(
      state.copyWith(
        beneficiaryStatus: FormzSubmissionStatus.initial,
        getCallStatus: FormzSubmissionStatus.initial,
        getCallingResponse: '',
        teamResponse: '',
        teamStatus: FormzSubmissionStatus.initial,
        addDependentStatus: FormzSubmissionStatus.initial,
        getRealtionStatus: FormzSubmissionStatus.initial,
        getCallStatusForAppointment: FormzSubmissionStatus.initial,
        getRelationResponse: '',
        getCallingForAppointmentResponse: '',
        getRemarkStatus: FormzSubmissionStatus.initial,
        getRemarkResponse: '',
        getAddressDetailResponse: '',
        getAddressDetailStatus: FormzSubmissionStatus.initial,
        getDependentStatus: FormzSubmissionStatus.initial,
        insertAppointmentStatus: FormzSubmissionStatus.initial,
        organizationStatus: FormzSubmissionStatus.initial,
        usercreatedStatus: FormzSubmissionStatus.initial,
        insertCallLogStatus: FormzSubmissionStatus.initial,
        agentIdStatus: FormzSubmissionStatus.initial,
        dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
        getappointmentstatus: FormzSubmissionStatus.initial,
        myoperatorStatus: FormzSubmissionStatus.initial,
        getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
        screenedDependetStatus: FormzSubmissionStatus.initial,
      ),
    );
  }

  FutureOr<void> _onTeamStatusRequest(
    TeamStatusRequest event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.inProgress,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getCallStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRelationResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
        ),
      );

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      final Map<String, dynamic> mutablePayload = {
        ...event.payload,
        "UserID": empCode.toString(),
      };
      var res = await repository.getTeamData(mutablePayload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              teamStatus: FormzSubmissionStatus.success,
              teamResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              teamStatus: FormzSubmissionStatus.failure,
              teamResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            teamStatus: FormzSubmissionStatus.failure,
            teamResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          teamStatus: FormzSubmissionStatus.failure,
          teamResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onAddDependent(
    AddDependentRequest event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) {
    try {
      emit(
        state.copyWith(
          addDependentStatus: FormzSubmissionStatus.inProgress,
          getCallStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          getRealtionStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
        ),
      );

      // ✅ Create a new list and update it
      List<AddDependentOutput> updatedList = List.from(event.addDependentModel);

      emit(
        state.copyWith(
          addDependentModel: updatedList,
          addDependentStatus: FormzSubmissionStatus.success,
        ),
      );
    } catch (e) {
      print(e);
      emit(state.copyWith(addDependentStatus: FormzSubmissionStatus.failure));
    }
  }

  FutureOr<void> _onGetRealationData(
    GetRealationData event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getCallingForAppointmentResponse: "",
          getRealtionStatus: FormzSubmissionStatus.inProgress,
          getRelationResponse: "",
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,

          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          organizationResponse: '',
          getDependentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          inserCallLogNewResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getRelation(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getRealtionStatus: FormzSubmissionStatus.success,
              getRelationResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getRealtionStatus: FormzSubmissionStatus.failure,
              getRelationResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getRealtionStatus: FormzSubmissionStatus.failure,
            getRelationResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getRealtionStatus: FormzSubmissionStatus.failure,
          getRelationResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetCallStatusForAppointment(
    GetCallStatusForAppointment event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getRemarkStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          screenedDependetStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.inProgress,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getCallStatusForAppointment(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getCallStatusForAppointment: FormzSubmissionStatus.success,
              getCallingForAppointmentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getCallStatusForAppointment: FormzSubmissionStatus.failure,
              getCallingForAppointmentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getCallStatusForAppointment: FormzSubmissionStatus.failure,
            getCallingForAppointmentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getCallStatusForAppointment: FormzSubmissionStatus.failure,
          getCallingForAppointmentResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetRemark(
    GetRemark event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.inProgress,
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getRemark(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getRemarkStatus: FormzSubmissionStatus.success,
              getRemarkResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getRemarkStatus: FormzSubmissionStatus.failure,
              getRemarkResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getRemarkStatus: FormzSubmissionStatus.failure,
            getRemarkResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getRemarkStatus: FormzSubmissionStatus.failure,
          getRemarkResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetAddressDetails(
    GetAddressDetails event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.inProgress,
          getAddressDetailResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getAddressDetails(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getAddressDetailStatus: FormzSubmissionStatus.success,
              getAddressDetailResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getAddressDetailStatus: FormzSubmissionStatus.failure,
              getAddressDetailResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getAddressDetailStatus: FormzSubmissionStatus.failure,
            getAddressDetailResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getAddressDetailStatus: FormzSubmissionStatus.failure,
          getAddressDetailResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetDependentDetails(
    GetDependentDetails event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.inProgress,
          getAddressDetailResponse: '',
          getDependentResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getDependentDetails(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getDependentStatus: FormzSubmissionStatus.success,
              getDependentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getDependentStatus: FormzSubmissionStatus.failure,
              getDependentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getDependentStatus: FormzSubmissionStatus.failure,
            getDependentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getDependentStatus: FormzSubmissionStatus.failure,
          getDependentResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onDeleteDependentRequest(
    DeleteDependentRequest event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.inProgress,
          getAddressDetailResponse: '',
          getDependentResponse: '',
        ),
      );
      state.addDependentOutput.removeAt(event.dependentIndex);
      emit(
        state.copyWith(
          addDependentModel: state.addDependentOutput,
          addDependentStatus: FormzSubmissionStatus.success,
        ),
      );
    } catch (e) {
      print(e);
    }
  }

  FutureOr<void> _onInsertAppointmentDetails(
    InsertAppointmentDetails event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.inProgress,
          getAddressDetailResponse: '',
          insertAppointmentResponse: '',
          getDependentResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.insertAppointmentData(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              insertAppointmentStatus: FormzSubmissionStatus.success,
              insertAppointmentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              insertAppointmentStatus: FormzSubmissionStatus.failure,
              insertAppointmentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            insertAppointmentStatus: FormzSubmissionStatus.failure,
            insertAppointmentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.failure,
          insertAppointmentResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onBeneficiaryRequestForDateType(
    BeneficiaryRequestForDateType event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          insertAppointmentResponse: '',
          getAddressDetailResponse: '',
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          getDependentResponse: '',
          userCreatedResponse: '',
          getRealtionStatus: FormzSubmissionStatus.initial,
          inserCallLogResponse: '',
          inserCallLogNewResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
          getCallingResponse: "",
        ),
      );

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      event.payload.addAll({"UserID": empCode.toString()});
      var res = await repository.expectedBeneficiaryListNew(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              dateTypeWiseDataStatus: FormzSubmissionStatus.success,
              dateTypeWiseDataResponse: resString,
              beneficiaryResponse: resString,
              beneficiaryStatus: FormzSubmissionStatus.success,
            ),
          );
        } else {
          emit(
            state.copyWith(
              dateTypeWiseDataStatus: FormzSubmissionStatus.failure,
              dateTypeWiseDataResponse: resString,
              beneficiaryResponse: resString,
              beneficiaryStatus: FormzSubmissionStatus.failure,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            dateTypeWiseDataStatus: FormzSubmissionStatus.failure,
            dateTypeWiseDataResponse: res.reasonPhrase,
            beneficiaryResponse: res.reasonPhrase,
            beneficiaryStatus: FormzSubmissionStatus.failure,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          dateTypeWiseDataStatus: FormzSubmissionStatus.failure,
          dateTypeWiseDataResponse: e.toString(),
           beneficiaryResponse: e.toString(),
          beneficiaryStatus: FormzSubmissionStatus.failure
        ),
      );
    }
  }

  FutureOr<void> _onGetAppointmentCount(
    GetAppointmentCount event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          insertAppointmentResponse: '',
          getAddressDetailResponse: '',
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          dateTypeWiseDataStatus: FormzSubmissionStatus.initial,
          getappointmentstatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          getDependentResponse: '',
          userCreatedResponse: '',
          getappointmentResponse: '',
          getRealtionStatus: FormzSubmissionStatus.initial,
          inserCallLogResponse: '',
          inserCallLogNewResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
          getCallingResponse: "",
        ),
      );

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      event.payload.addAll({"UserID": empCode.toString()});
      var res = await repository.getAppointmentCount(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getappointmentstatus: FormzSubmissionStatus.success,
              getappointmentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getappointmentstatus: FormzSubmissionStatus.failure,
              getappointmentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getappointmentstatus: FormzSubmissionStatus.failure,
            getappointmentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getappointmentstatus: FormzSubmissionStatus.failure,
          getappointmentResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetScreenedDependentDetails(
    GetScreenedDependentDetails event,
    Emitter<ExpectedBeneficiaryState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          teamResponse: "",
          teamStatus: FormzSubmissionStatus.initial,
          beneficiaryStatus: FormzSubmissionStatus.initial,
          beneficiaryResponse: "",
          getCallStatus: FormzSubmissionStatus.initial,
          getCallingResponse: "",
          getRealtionStatus: FormzSubmissionStatus.initial,
          getCallStatusForAppointment: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          getRelationResponse: "",
          getCallingForAppointmentResponse: '',
          getRemarkResponse: '',
          getRemarkStatus: FormzSubmissionStatus.initial,
          getAddressDetailStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getDependentStatus: FormzSubmissionStatus.initial,
          screenedDependetStatus: FormzSubmissionStatus.inProgress,
          getAddressDetailResponse: '',
          getDependentResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;
      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.getScreenedBeneficiaryList(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              screenedDependetStatus: FormzSubmissionStatus.success,
              screenedDependentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              screenedDependetStatus: FormzSubmissionStatus.failure,
              screenedDependentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            screenedDependetStatus: FormzSubmissionStatus.failure,
            screenedDependentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          screenedDependetStatus: FormzSubmissionStatus.failure,
          screenedDependentResponse: e.toString(),
        ),
      );
    }
  }
}
