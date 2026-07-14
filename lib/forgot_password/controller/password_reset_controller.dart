// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../model/PasswordResetResponseModel.dart';
import '../repository/password_reset_repository.dart';

class PasswordResetController extends GetxController {
  final _repository = PasswordResetRepository();

  // ─── User info ────────────────────────────────────────────────────────────

  String mobileNo = '';
  String _userId = '';
  String _generatedOtp = '';

  // ─── UI state ─────────────────────────────────────────────────────────────

  bool isSendingOtp = false;
  bool isUpdatingPassword = false;
  int timerSeconds = 0;
  bool canResend = false;
  bool otpSent = false;
  bool passwordUpdated = false;

  Timer? _timer;

  // ─── Lifecycle ────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    mobileNo = userData?.bMobile ?? '';
    _userId = userData?.empCode?.toString() ?? '';
  }

  @override
  void onClose() {
    _timer?.cancel();
    super.onClose();
  }

  // ─── OTP ──────────────────────────────────────────────────────────────────

  Future<void> sendOtp() async {
    _generatedOtp = FormatterManager.generateRandomDigits(5);
    isSendingOtp = true;
    otpSent = false;
    update();
    try {
      final response = await _repository.sendOtp(
        mobileNo: mobileNo,
        otp: _generatedOtp,
      );
      if (response.statusCode == 200) {
        final model = PasswordResetResponseModel.fromJson(json.decode(response.body));
        if (model.isSuccess) {
          otpSent = true;
          _startTimer();
          ToastManager.toast('OTP sent to $mobileNo');
        } else {
          ToastManager.toast(model.message.isNotEmpty ? model.message : 'Failed to send OTP');
        }
      } else {
        ToastManager.toast('Server not responding');
      }
    } catch (e) {
      debugPrint('sendOtp error: $e');
      ToastManager.toast('Server not responding');
    } finally {
      isSendingOtp = false;
    }
    update();
  }

  // ─── Password update ──────────────────────────────────────────────────────

  Future<void> updatePassword({
    required String newPassword,
    required String otp,
  }) async {
    if (newPassword.isEmpty) {
      ToastManager.toast('Please enter new password');
      return;
    }
    if (newPassword.length < 6) {
      ToastManager.toast('Password must be at least 6 characters');
      return;
    }
    if (otp.isEmpty) {
      ToastManager.toast('Please enter OTP');
      return;
    }
    if (otp != _generatedOtp) {
      ToastManager.toast('Entered OTP does not match');
      return;
    }

    isUpdatingPassword = true;
    passwordUpdated = false;
    update();
    try {
      final response = await _repository.updatePassword(
        userId: _userId,
        newPassword: newPassword,
        mobileNo: mobileNo,
        otp: otp,
      );
      if (response.statusCode == 200) {
        final model = PasswordResetResponseModel.fromJson(json.decode(response.body));
        if (model.isSuccess) {
          passwordUpdated = true;
          ToastManager.toast(model.message.isNotEmpty ? model.message : 'Password updated successfully');
        } else {
          ToastManager.toast(model.message.isNotEmpty ? model.message : 'Failed to update password');
        }
      } else {
        ToastManager.toast('Server not responding');
      }
    } catch (e) {
      debugPrint('updatePassword error: $e');
      ToastManager.toast('Server not responding');
    } finally {
      isUpdatingPassword = false;
    }
    update();
  }

  // ─── Timer ────────────────────────────────────────────────────────────────

  void _startTimer() {
    timerSeconds = 120;
    canResend = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (t) {
      if (timerSeconds == 0) {
        canResend = true;
        t.cancel();
      } else {
        timerSeconds--;
      }
      update();
    });
  }
}
