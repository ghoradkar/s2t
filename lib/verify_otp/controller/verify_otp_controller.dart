import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/constants/api_constants.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/home_screen/screen/home_screen.dart';
import 'package:s2toperational/login/controllers/device_uuid_manager.dart';
import 'package:s2toperational/login/controllers/login_controller.dart';
import 'package:s2toperational/login/screens/login_screen.dart';
import 'package:s2toperational/verify_otp/repository/verify_otp_repository.dart';

class VerifyOtpController extends GetxController {
  final int empCode;
  final String mOBNO;
  String otp;

  final VerifyOtpRepository _repository;

  VerifyOtpController({
    required this.empCode,
    required this.mOBNO,
    required this.otp,
    VerifyOtpRepository? repository,
  }) : _repository = repository ?? VerifyOtpRepository();

  final TextEditingController otpTextField = TextEditingController();

  final RxBool isButtonEnabled = false.obs;
  final RxInt timerSeconds = 120.obs;

  Timer? _timer;

  @override
  void onInit() {
    super.onInit();
    startTimer();
  }

  @override
  void onClose() {
    _timer?.cancel();
    otpTextField.dispose();
    super.onClose();
  }

  String get timerText {
    int minutes = timerSeconds.value ~/ 60;
    int seconds = timerSeconds.value % 60;
    return '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}';
  }

  void startTimer() {
    timerSeconds.value = 120;
    isButtonEnabled.value = false;
    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (timerSeconds.value == 0) {
        isButtonEnabled.value = true;
        otpTextField.clear();
        timer.cancel();
      } else {
        timerSeconds.value--;
      }
    });
  }

  Future<void> resendOTP() async {
    if (!isButtonEnabled.value) return;

    ToastManager.showLoader();
    otp = FormatterManager.generateRandomDigits(5);

    final result = await _repository.sendOtp({
      'MOBNO': mOBNO,
      'OTP': otp,
      'CreatedBy': empCode.toString(),
    });

    ToastManager.hideLoader();

    if (result.success) {
      ToastManager.toast('OTP sent on $mOBNO number successfully');
      startTimer();
    } else {
      ToastManager.toast(result.error);
    }
  }

  Future<void> verifyOTP() async {
    final otpString = otpTextField.text.trim();
    if (otpString.isEmpty) {
      ToastManager.toast('Please enter OTP');
      return;
    }
    if (otpString.length < 5) {
      ToastManager.toast('Please enter valid OTP');
      return;
    }

    ToastManager.showLoader();
    final result = await _repository.verifyOtp({
      'MOBNO': mOBNO,
      'OTP': otpString,
    });

    if (result.success) {
      await _checkAndroidID();
    } else {
      ToastManager.hideLoader();
      ToastManager.toast(result.error);
    }
  }

  Future<void> _checkAndroidID() async {
    final uuidString = await DeviceUUIDManager().getDeviceUUID();

    final response = await _repository.getUserAndroidId({
      'UserId': '$empCode',
      'AndroidID': uuidString,
    });

    ToastManager.hideLoader();

    if (response == null) {
      ToastManager.toast('Failed to check device');
      return;
    }

    final status = response.status ?? '';
    final msg = response.message ?? '';
    final isLoginAllowed = response.allowedForLogin;

    if (status.toLowerCase() == 'fail') {
      if (msg == '0') {
        _showDialog(
          content:
              'हा मोबाईल लॉगिनसाठी अधिकृत मोबाईल मानला जाईल, तुम्ही तुमच्या स्वतःच्या मोबाईलचा वापर करून लॉग इन करत आहात याची खात्री करा.',
          confirmLabel: 'Confirm',
          onConfirm: () => _saveAndroidID(uuidString),
          cancelLabel: 'Cancel',
          onCancel: _goToLogin,
        );
      } else {
        _showDialog(
          content:
              "You're trying to login from different device $uuidString . Do you want to change your device?",
          confirmLabel: 'Yes',
          onConfirm: () => _saveAndroidID(uuidString),
          cancelLabel: 'No',
          onCancel: _goToLogin,
        );
      }
    } else {
      if (isLoginAllowed == 0) {
        _showSingleButtonDialog(
          content:
              'तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.',
          onOkay: _goToLogin,
        );
      } else {
        await _saveAndroidToken(uuidString);
      }
    }
  }

  Future<void> _saveAndroidID(String uuidString) async {
    ToastManager.showLoader();

    final response = await _repository.saveAndroidId({
      'UserId': '$empCode',
      'AndroidID': uuidString,
      'VersionNo': APIConstants.kNativeVersion,
    });

    ToastManager.hideLoader();

    if (response == null) {
      ToastManager.toast('Failed to save device');
      return;
    }

    final status = response.status ?? '';
    final msg = response.message ?? '';
    final isLoginAllowed = response.allowedForLogin;

    if (status.toLowerCase() == 'fail') {
      if (msg.toLowerCase() ==
          'AndroidID Already Exists With Another User'.toLowerCase()) {
        _showSingleButtonDialog(
          content:
              'हा मोबाईल आधीच दुसऱ्या वापरकर्त्याशी लिंक केलेला आहे. कृपया या ॲप्लिकेशनमध्ये प्रवेश करण्यासाठी तुमचा स्वतःचा मोबाईल वापरा.',
          onOkay: _goToLogin,
        );
      } else {
        ToastManager.toast(msg);
      }
    } else {
      if (isLoginAllowed == 0) {
        _showSingleButtonDialog(
          content:
              'तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.',
          onOkay: _goToLogin,
        );
      } else {
        ToastManager.toast('You have changed device for $msg times');
        await _saveAndroidToken(uuidString);
      }
    }
  }

  Future<void> _saveAndroidToken(String uuidString) async {
    final result = await _repository.saveAndroidToken({
      'USERID': '$empCode',
      'ANDROIEDTOKEN': uuidString,
      'ActiveStatus': '1',
    });

    if (result.success) {
      DataProvider().setIsLogin(true);
      Get.back();
      Get.offAll(() => HomeScreen());
    }
  }

  void _goToLogin() {
    Get.back();
    if (Get.isRegistered<LoginController>()) {
      Get.find<LoginController>().resetAndReload();
    } else {
      Get.put(LoginController());
    }
    Get.offAll(() => const LoginScreen());
  }

  void _showDialog({
    required String content,
    required String confirmLabel,
    required VoidCallback onConfirm,
    required String cancelLabel,
    required VoidCallback onCancel,
  }) {
    Get.dialog(
      AlertDialog(
        title: const Text('Alert'),
        content: Text(content),
        actions: [
          TextButton(
            onPressed: () {
              Get.back();
              onConfirm();
            },
            child: Text(confirmLabel),
          ),
          TextButton(
            onPressed: () {
              Get.back();
              onCancel();
            },
            child: Text(cancelLabel),
          ),
        ],
      ),
    );
  }

  void _showSingleButtonDialog({
    required String content,
    required VoidCallback onOkay,
  }) {
    Get.dialog(
      AlertDialog(
        title: const Text('Alert'),
        content: Text(content),
        actions: [
          TextButton(
            onPressed: () {
              Get.back();
              onOkay();
            },
            child: const Text('Okay'),
          ),
        ],
      ),
    );
  }
}
