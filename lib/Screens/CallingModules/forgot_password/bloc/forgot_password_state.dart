part of 'forgot_password_bloc.dart';

class ForgotPasswordState extends Equatable {
  const ForgotPasswordState(
      {required this.forgotPasswordRequestStatus,
      required this.forgotPasswordRequestResponse,
      required this.checkAndGenerateOTPResponse,
      required this.checkAndGenerateOTPStatus,
      required this.checkGeneratedOTPResponse,
      required this.checkGeneratedOTPStatus});

  final FormzSubmissionStatus forgotPasswordRequestStatus;
  final String forgotPasswordRequestResponse;

  final FormzSubmissionStatus checkAndGenerateOTPStatus;
  final String checkAndGenerateOTPResponse;

  final FormzSubmissionStatus checkGeneratedOTPStatus;
  final String checkGeneratedOTPResponse;

  ForgotPasswordState copyWith(
      {FormzSubmissionStatus? forgotPasswordRequestStatus,
      String? forgotPasswordRequestResponse,
      FormzSubmissionStatus? checkAndGenerateOTPStatus,
      String? checkAndGenerateOTPResponse,
      FormzSubmissionStatus? checkGeneratedOTPStatus,
      String? checkGeneratedOTPResponse}) {
    return ForgotPasswordState(
        forgotPasswordRequestStatus:
            forgotPasswordRequestStatus ?? this.forgotPasswordRequestStatus,
        forgotPasswordRequestResponse:
            forgotPasswordRequestResponse ?? this.forgotPasswordRequestResponse,
        checkAndGenerateOTPResponse:
            checkAndGenerateOTPResponse ?? this.checkAndGenerateOTPResponse,
        checkAndGenerateOTPStatus:
            checkAndGenerateOTPStatus ?? this.checkAndGenerateOTPStatus,
        checkGeneratedOTPResponse:
            checkGeneratedOTPResponse ?? this.checkGeneratedOTPResponse,
        checkGeneratedOTPStatus:
            checkGeneratedOTPStatus ?? this.checkGeneratedOTPStatus);
  }

  @override
  List<Object> get props => [
        forgotPasswordRequestResponse,
        forgotPasswordRequestStatus,
        checkAndGenerateOTPResponse,
        checkAndGenerateOTPStatus,
        checkGeneratedOTPResponse,
        checkGeneratedOTPStatus
      ];
}
