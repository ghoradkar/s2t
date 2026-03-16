import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:formz/formz.dart';

import '../repository/forgot_password_repository.dart';
part 'forgot_password_event.dart';
part 'forgot_password_state.dart';

class ForgotPasswordBloc
    extends Bloc<ForgotPasswordEvent, ForgotPasswordState> {
  ForgotPasswordRepository forgotPasswordRepository;
  ForgotPasswordBloc({required this.forgotPasswordRepository})
    : super(
        const ForgotPasswordState(
          forgotPasswordRequestResponse: "",
          forgotPasswordRequestStatus: FormzSubmissionStatus.initial,
          checkAndGenerateOTPResponse: "",
          checkAndGenerateOTPStatus: FormzSubmissionStatus.initial,
          checkGeneratedOTPResponse: "",
          checkGeneratedOTPStatus: FormzSubmissionStatus.initial,
        ),
      );
}
