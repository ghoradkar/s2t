import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Screens/HomeScreen/HomeScreen.dart';
import '../repository/login_repository.dart';

class LoginController extends GetxController {
  final LoginRepository _repository;

  LoginController({LoginRepository? repository})
      : _repository = repository ?? LoginRepository();

  final TextEditingController usernameController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  final RxBool isObscure = true.obs;
  final RxBool isRememberMe = false.obs;
  final RxString usernameError = ''.obs;
  final RxString passwordError = ''.obs;

  @override
  void onInit() {
    super.onInit();
    _loadSavedCredentials();
  }

  Future<void> _loadSavedCredentials() async {
    final keepFlag = await DataProvider().getKeepSignedIn();
    if (!keepFlag) return;
    final userName = await DataProvider().read(DataProvider().kUserName);
    final password = await DataProvider().read(DataProvider().kPassword);
    isRememberMe.value = true;
    usernameController.text = userName ?? '';
    passwordController.text = password ?? '';
  }

  void toggleObscure() => isObscure.value = !isObscure.value;

  void toggleRememberMe(bool? value) {
    isRememberMe.value = value ?? !isRememberMe.value;
    DataProvider().setKeepSignedIn(isRememberMe.value);
  }

  void onUsernameChanged(String value) {
    if (usernameError.value.isNotEmpty) {
      usernameError.value = value.trim().isEmpty ? 'Please enter username' : '';
    }
  }

  void onPasswordChanged(String value) {
    if (passwordError.value.isNotEmpty) {
      passwordError.value = value.trim().isEmpty ? 'Please enter password' : '';
    }
  }

  Future<void> login() async {
    final userName = usernameController.text.trim();
    final password = passwordController.text.trim();

    usernameError.value = userName.isEmpty ? 'Please enter username' : '';
    passwordError.value = password.isEmpty ? 'Please enter password' : '';

    if (usernameError.value.isNotEmpty || passwordError.value.isNotEmpty) return;

    ToastManager.showLoader();
    final result = await _repository.login(userName, password);
    ToastManager.hideLoader();

    if (result.success) {
      DataProvider().setIsLogin(true);
      Get.offAll(() => HomeScreen());
    } else {
      ToastManager.toast(result.error);
    }
  }

  @override
  void onClose() {
    usernameController.dispose();
    passwordController.dispose();
    super.onClose();
  }
}
