// ignore_for_file: must_be_immutable

part of 'forgot_password_bloc.dart';

sealed class ForgotPasswordEvent extends Equatable {
  const ForgotPasswordEvent();

  @override
  List<Object> get props => [];
}

class ForgotPasswordRequest extends ForgotPasswordEvent {
  Map<String, dynamic> payload;
  ForgotPasswordRequest({required this.payload});
  @override
  List<Object> get props => [payload];
}

class CheckAndGenerateOTP extends ForgotPasswordEvent {
  Map<String, dynamic> payload;
  CheckAndGenerateOTP({required this.payload});
  @override
  List<Object> get props => [payload];
}

class CheckGeneratedOTP extends ForgotPasswordEvent {
  Map<String, dynamic> payload;
  CheckGeneratedOTP({required this.payload});
  @override
  List<Object> get props => [payload];
}
