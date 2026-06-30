import 'dart:async';
import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../repository/forgot_password_repository.dart';

class ForgotPasswordController extends GetxController {
  final ForgotPasswordRepository repository;

  ForgotPasswordController({required this.repository});

  final TextEditingController newPasswordController = TextEditingController();
  final TextEditingController otpController = TextEditingController();

  final RxBool isSendingOtp = false.obs;
  final RxBool isUpdatingPassword = false.obs;
  final RxInt timerSeconds = 0.obs;
  final RxBool canResend = false.obs;

  // Retained for forgot_password UI screens (login-screen flow — pending implementation)
  final checkAndGenerateOTPStatus = FormzSubmissionStatus.initial.obs;
  final checkAndGenerateOTPResponse = ''.obs;
  final checkGeneratedOTPStatus = FormzSubmissionStatus.initial.obs;
  final checkGeneratedOTPResponse = ''.obs;
  final forgotPasswordRequestStatus = FormzSubmissionStatus.initial.obs;
  final forgotPasswordRequestResponse = ''.obs;

  String _generatedOtp = '';
  String _mobileNo = '';
  String _userId = '';

  Timer? _timer;

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    _mobileNo = userData?.bMobile ?? '';
    _userId = userData?.empCode?.toString() ?? '';
  }

  String get mobileNo => _mobileNo;

  Future<({bool success, String message})> sendOtp() async {
    _generatedOtp = FormatterManager.generateRandomDigits(5);
    isSendingOtp.value = true;
    final result = await repository.sendOtp(
      mobileNo: _mobileNo,
      otp: _generatedOtp,
    );
    isSendingOtp.value = false;
    if (result.success) _startTimer();
    return result;
  }

  Future<({bool success, String message})> updatePassword() async {
    final newPassword = newPasswordController.text.trim();
    final otp = otpController.text.trim();

    if (newPassword.isEmpty) {
      return (success: false, message: 'Please enter new password');
    }
    if (newPassword.length < 6) {
      return (success: false, message: 'Password must be at least 6 characters');
    }
    if (otp.isEmpty) {
      return (success: false, message: 'Please enter OTP');
    }
    if (otp != _generatedOtp) {
      return (success: false, message: 'Entered OTP does not match');
    }

    isUpdatingPassword.value = true;
    final result = await repository.updatePassword(
      userId: _userId,
      newPassword: newPassword,
      mobileNo: _mobileNo,
      otp: otp,
    );
    isUpdatingPassword.value = false;
    return result;
  }

  // Stub methods for forgot_password UI screens (login-screen flow — pending implementation)
  Future<void> checkAndGenerateOTP(Map<String, dynamic> payload) async {}
  Future<void> checkGeneratedOTP(Map<String, dynamic> payload) async {}
  Future<void> forgotPasswordRequest(Map<String, dynamic> payload) async {}

  void _startTimer() {
    timerSeconds.value = 120;
    canResend.value = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (t) {
      if (timerSeconds.value == 0) {
        canResend.value = true;
        t.cancel();
      } else {
        timerSeconds.value--;
      }
    });
  }

  @override
  void onClose() {
    newPasswordController.dispose();
    otpController.dispose();
    _timer?.cancel();
    super.onClose();
  }
}