// ignore_for_file: avoid_print

import 'dart:async';
import 'dart:convert';
import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:flutter/material.dart';
import 'package:formz/formz.dart';

import '../../../../Modules/utilities/DataProvider.dart';
import '../repository/beneficiary_card.dart';

part 'beneficiary_card_bloc_event.dart';
part 'beneficiary_card_bloc_state.dart';

class BeneficiaryCardBlocBloc
    extends Bloc<BeneficiaryCardBlocEvent, BeneficiaryCardBlocState> {
  final BeneficiaryCardRepository repository;

  BeneficiaryCardBlocBloc({required this.repository})
    : super(
        BeneficiaryCardBlocState(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          agentIdStatus: FormzSubmissionStatus.initial,
          apikeyAgentIdStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          apikeyCallPatchingStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForCallPatchingStatus: FormzSubmissionStatus.initial,
          myoperatorStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsResponse: '',
          myOperatorResponse: '',
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          insertClResponse: '',
          agetIdResponse: '',
          apiKeyAgentResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
          apiKeyCallPatchingResponse: '',
          twentyFourBySevenForCallPatchingResponse: '',
        ),
      ) {
    on<ResetBaneficiaryCardState>(_onResetBaneficiaryCardState);
    // on<AddDependentRequest>(_onAddDependent);

    // on<GetCallStatusForAppointment>(_onGetCallStatusForAppointment);

    on<GetOrganization>(_onGetOrganization);
    on<GetUserCreatedBy>(_onGetUserCreatedBy);
    // on<InsertCallLog>(_onInsertCallLog);
    on<InsertCallLogNew>(_onInsertCallLogNew);
    on<TwentyFourBySevenWithAgentID>(_onTwentyFourBySevenWithAgentID);
    on<GetAPIKeyForAgentId>(_onGetAPIKeyForAgentId);
    on<GetAPIKeyForCallPAtching>(_onGetAPIKeyForCallPAtching);
    on<TwentyFourBySevenWithCallPatching>(_onTwentyFourBySevenWithCallPatching);
    on<MyOperator>(_onMyOperator);
    on<GetAPIKeyForMyoperator>(_onGetAPIKeyForMyoperator);
  }

  FutureOr<void> _onResetBaneficiaryCardState(
    ResetBaneficiaryCardState event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) {
    emit(
      state.copyWith(
        insertAppointmentStatus: FormzSubmissionStatus.initial,
        organizationStatus: FormzSubmissionStatus.initial,
        usercreatedStatus: FormzSubmissionStatus.initial,
        insertCallLogStatus: FormzSubmissionStatus.initial,
        insertClStatus: FormzSubmissionStatus.initial,
        agentIdStatus: FormzSubmissionStatus.initial,
        apikeyAgentIdStatus: FormzSubmissionStatus.initial,
        twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
        apikeyCallPatchingStatus: FormzSubmissionStatus.initial,
        twentyFourBySevenForCallPatchingStatus: FormzSubmissionStatus.initial,
        myoperatorStatus: FormzSubmissionStatus.initial,
        getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
      ),
    );
  }

  FutureOr<void> _onGetOrganization(
    GetOrganization event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          insertAppointmentResponse: '',
        ),
      );

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;

      event.payload.addAll({"UserID": empCode.toString()});
      var res = await repository.getOrganization(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              organizationStatus: FormzSubmissionStatus.success,
              organizationResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              organizationStatus: FormzSubmissionStatus.failure,
              organizationResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            organizationStatus: FormzSubmissionStatus.failure,
            organizationResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          organizationStatus: FormzSubmissionStatus.failure,
          organizationResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetUserCreatedBy(
    GetUserCreatedBy event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.getUserCreatedBy(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          debugPrint(resString);
          emit(
            state.copyWith(
              usercreatedStatus: FormzSubmissionStatus.success,
              userCreatedResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              usercreatedStatus: FormzSubmissionStatus.failure,
              userCreatedResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            usercreatedStatus: FormzSubmissionStatus.failure,
            userCreatedResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          usercreatedStatus: FormzSubmissionStatus.failure,
          userCreatedResponse: e.toString(),
        ),
      );
    }
  }

  // FutureOr<void> _onInsertCallLog(
  //     InsertCallLog event, Emitter<BeneficiaryCardBlocState> emit) async {
  //   try {
  //     emit(state.copyWith(
  //       insertAppointmentStatus: FormzSubmissionStatus.initial,
  //       getMyoperatorDetailsStatus: FormzSubmissionStatus.initial,
  //       organizationStatus: FormzSubmissionStatus.initial,
  //       usercreatedStatus: FormzSubmissionStatus.initial,
  //       twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
  //       insertCallLogStatus: FormzSubmissionStatus.inProgress,
  //       organizationResponse: '',
  //       insertAppointmentResponse: '',
  //       userCreatedResponse: '',
  //       inserCallLogResponse: '',
  //     ));

  //     // var userDetails = DataProvider().getParsedUserData();
  //     // int empCode = userDetails?.output?[0].empCode ?? 0;

  //     // event.payload.addAll({
  //     //   "UserID": empCode.toString(),

  //     // });
  //     var res = await repository.insertCallinglog(event.payload);

  //     if (res.statusCode == 200) {
  //       String resString = res.body;

  //       var jsonResponse = jsonDecode(resString);

  //       if (jsonResponse['status'] == 'Success') {
  //         // await DataProvider().storeUserData(resString);
  //         emit(state.copyWith(
  //             insertCallLogStatus: FormzSubmissionStatus.success,
  //             inserCallLogResponse: resString));
  //       } else {
  //         emit(state.copyWith(
  //             insertCallLogStatus: FormzSubmissionStatus.failure,
  //             inserCallLogResponse: resString));
  //       }
  //     } else {
  //       emit(state.copyWith(
  //           insertCallLogStatus: FormzSubmissionStatus.failure,
  //           inserCallLogResponse: res.reasonPhrase));
  //     }
  //   } catch (e) {
  //     print(e);
  //     emit(state.copyWith(
  //         insertCallLogStatus: FormzSubmissionStatus.failure,
  //         inserCallLogResponse: e.toString()));
  //   }
  // }

  FutureOr<void> _onInsertCallLogNew(
    InsertCallLogNew event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.insertCallinglogNew(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);

          // if (event.payload['agent'] == "0") {
          //   var pay = {
          //     "apiKey": event.payload['apiKey'],
          //     "customernumber": event.payload['customernumber'],
          //     "servienumber": event.payload['servienumber'],
          //     "format": "json",
          //     "agentloginid": event.payload['agentloginid'],
          //     "referencestate": event.payload['referencestate']
          //   };
          //   var res = await repository.twentyFourBySevenForWithAgentId(pay);

          //   if (res.statusCode == 200) {
          //     String resString = res.body;

          //     var jsonResponse = jsonDecode(resString);

          //     if (jsonResponse['statusMessage'] == 'success') {
          //       // await DataProvider().storeUserData(resString);
          //       emit(state.copyWith(
          //           twentyFourBySevenForAgentIdStatus:
          //               FormzSubmissionStatus.success,
          //           twentyFourBySevenForAgentIdResponse: resString));
          //     } else {
          //       emit(state.copyWith(
          //           twentyFourBySevenForAgentIdStatus:
          //               FormzSubmissionStatus.failure,
          //           twentyFourBySevenForAgentIdResponse: resString));
          //     }
          //   } else {
          //     emit(state.copyWith(
          //         twentyFourBySevenForAgentIdStatus:
          //             FormzSubmissionStatus.failure,
          //         twentyFourBySevenForAgentIdResponse: res.reasonPhrase));
          //   }
          // } else {}

          emit(
            state.copyWith(
              insertClStatus: FormzSubmissionStatus.success,
              insertClResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              insertClStatus: FormzSubmissionStatus.failure,
              insertClResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            insertClStatus: FormzSubmissionStatus.failure,
            insertClResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          insertClStatus: FormzSubmissionStatus.failure,
          insertClResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onTwentyFourBySevenWithAgentID(
    TwentyFourBySevenWithAgentID event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.inProgress,
          twentyFourBySevenForAgentIdResponse: '',
          insertClStatus: FormzSubmissionStatus.initial,
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.twentyFourBySevenForWithAgentId(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['statusMessage'] == 'success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.success,
              twentyFourBySevenForAgentIdResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.failure,
              twentyFourBySevenForAgentIdResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.failure,
            twentyFourBySevenForAgentIdResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.failure,
          twentyFourBySevenForAgentIdResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetAPIKeyForCallPAtching(
    GetAPIKeyForCallPAtching event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          apikeyCallPatchingStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
          apiKeyCallPatchingResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.apiKeyForCallPAtching(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              apikeyCallPatchingStatus: FormzSubmissionStatus.success,
              apiKeyCallPatchingResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              apikeyCallPatchingStatus: FormzSubmissionStatus.failure,
              apiKeyCallPatchingResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            apikeyCallPatchingStatus: FormzSubmissionStatus.failure,
            apiKeyCallPatchingResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          apikeyCallPatchingStatus: FormzSubmissionStatus.failure,
          apiKeyCallPatchingResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetAPIKeyForAgentId(
    GetAPIKeyForAgentId event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          apikeyAgentIdStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
          apiKeyAgentResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),
      // });
      var res = await repository.apiKeyForAgentId(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              apikeyAgentIdStatus: FormzSubmissionStatus.success,
              apiKeyAgentResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              apikeyAgentIdStatus: FormzSubmissionStatus.failure,
              apiKeyAgentResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            apikeyAgentIdStatus: FormzSubmissionStatus.failure,
            apiKeyAgentResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          apikeyAgentIdStatus: FormzSubmissionStatus.failure,
          apiKeyAgentResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onTwentyFourBySevenWithCallPatching(
    TwentyFourBySevenWithCallPatching event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.twentyFourBySevenForWithCallPatching(
        event.payload,
      );

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              twentyFourBySevenForCallPatchingStatus:
                  FormzSubmissionStatus.success,
              twentyFourBySevenForCallPatchingResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              twentyFourBySevenForCallPatchingStatus:
                  FormzSubmissionStatus.failure,
              twentyFourBySevenForCallPatchingResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            twentyFourBySevenForCallPatchingStatus:
                FormzSubmissionStatus.failure,
            twentyFourBySevenForCallPatchingResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.failure,
          twentyFourBySevenForCallPatchingResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onMyOperator(
    MyOperator event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          insertAppointmentResponse: '',
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          myoperatorStatus: FormzSubmissionStatus.inProgress,
          organizationResponse: '',
          myOperatorResponse: '',
          userCreatedResponse: '',
          getappointmentResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
          twentyFourBySevenForAgentIdResponse: '',
        ),
      );

      var res = await repository.myOperatorAPIDetails(
        event.payload,
        event.apiKey,
      );

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              myoperatorStatus: FormzSubmissionStatus.success,
              myOperatorResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              myoperatorStatus: FormzSubmissionStatus.failure,
              myOperatorResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            myoperatorStatus: FormzSubmissionStatus.failure,
            myOperatorResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          myoperatorStatus: FormzSubmissionStatus.failure,
          myOperatorResponse: e.toString(),
        ),
      );
    }
  }

  FutureOr<void> _onGetAPIKeyForMyoperator(
    GetAPIKeyForMyoperator event,
    Emitter<BeneficiaryCardBlocState> emit,
  ) async {
    try {
      emit(
        state.copyWith(
          insertAppointmentStatus: FormzSubmissionStatus.initial,
          organizationStatus: FormzSubmissionStatus.initial,
          usercreatedStatus: FormzSubmissionStatus.initial,
          insertCallLogStatus: FormzSubmissionStatus.initial,
          insertClStatus: FormzSubmissionStatus.initial,
          twentyFourBySevenForAgentIdStatus: FormzSubmissionStatus.initial,
          apikeyCallPatchingStatus: FormzSubmissionStatus.initial,
          getMyoperatorDetailsStatus: FormzSubmissionStatus.inProgress,
          getMyoperatorDetailsResponse: '',
          organizationResponse: '',
          insertAppointmentResponse: '',
          userCreatedResponse: '',
          inserCallLogResponse: '',
          insertClResponse: '',
          apiKeyCallPatchingResponse: '',
        ),
      );

      // var userDetails = DataProvider().getParsedUserData();
      // int empCode = userDetails?.output?[0].empCode ?? 0;

      // event.payload.addAll({
      //   "UserID": empCode.toString(),

      // });
      var res = await repository.apiKeyForMyoperator(event.payload);

      if (res.statusCode == 200) {
        String resString = res.body;

        var jsonResponse = jsonDecode(resString);

        if (jsonResponse['status'] == 'Success') {
          // await DataProvider().storeUserData(resString);
          emit(
            state.copyWith(
              getMyoperatorDetailsStatus: FormzSubmissionStatus.success,
              getMyoperatorDetailsResponse: resString,
            ),
          );
        } else {
          emit(
            state.copyWith(
              getMyoperatorDetailsStatus: FormzSubmissionStatus.failure,
              getMyoperatorDetailsResponse: resString,
            ),
          );
        }
      } else {
        emit(
          state.copyWith(
            getMyoperatorDetailsStatus: FormzSubmissionStatus.failure,
            getMyoperatorDetailsResponse: res.reasonPhrase,
          ),
        );
      }
    } catch (e) {
      print(e);
      emit(
        state.copyWith(
          getMyoperatorDetailsStatus: FormzSubmissionStatus.failure,
          getMyoperatorDetailsResponse: e.toString(),
        ),
      );
    }
  }

  // @override
  // Stream<BeneficiaryCardBlocState> mapEventToState(
  //   BeneficiaryCardBlocEvent event,
  // ) async* {}
}
