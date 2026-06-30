import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/constants/APIConstants.dart';
import '../../../Modules/utilities/DeviceInfoUtil.dart';
import '../../../Screens/HomeScreen/screen/home_screen.dart';
import '../../../Screens/login/controllers/login_controller.dart';
import '../../../Screens/login/screens/login_screen.dart';
import '../repository/splash_repository.dart';

class SplashController extends GetxController {
  final SplashRepository _repository = SplashRepository();
  final RxString appVersion = ''.obs;

  @override
  void onInit() {
    super.onInit();
    final apiManager = APIManager();
    apiManager.apiMode = APIMode.Beta;
    apiManager.setAPIEnvironment();
    _initialize();
  }

  Future<void> _initialize() async {
    final deviceInfo = await DeviceInfoUtil().getPackageInfo();
    appVersion.value = deviceInfo.version;
    // Extract major.minor from pubspec version (e.g. "9.79.0" → "9.79")
    final parts = deviceInfo.version.split('.');
    APIConstants.kNativeVersion =
        parts.length >= 2 ? '${parts[0]}.${parts[1]}' : deviceInfo.version;
    await _checkVersionAndNavigate(deviceInfo.version);
  }

  Future<void> _checkVersionAndNavigate(String version) async {
    final result = await _repository.checkAppVersion(version);
    if (result.status.toLowerCase() == 'success') {
      _showForceUpdateDialog(result.message);
      return;
    }
    _navigateNext();
  }

  void _showForceUpdateDialog(String message) {
    Get.dialog(
      PopScope(
        canPop: false,
        child: ToastManager.commonAlert(
          Get.context!,
          softwareUpdate,
          'App Update',
          message,
          () async {
            const packageName = 'com.s2t.operational';
            final uri = Uri.parse(
              'https://play.google.com/store/apps/details?id=$packageName',
            );
            if (await canLaunchUrl(uri)) {
              await launchUrl(uri, mode: LaunchMode.externalApplication);
            }
          },
          () => SystemNavigator.pop(),
          'Update',
          'Cancel',
        ),
      ),
      barrierDismissible: false,
    );
  }

  void _goToLogin() {
    if (Get.isRegistered<LoginController>()) {
      Get.find<LoginController>().resetAndReload();
    } else {
      Get.put(LoginController());
    }
    Get.offAll(() => const LoginScreen());
  }

  void _navigateNext() {
    Future.delayed(const Duration(seconds: 3), () {
      final loginData = DataProvider().getParsedUserData();
      final designationId = loginData?.output?.first.dESGID ?? 0;

      if (!DataProvider().isLoggedIn()) {
        _goToLogin();
        return;
      }

      if (designationId == 166 || designationId == 51) {
        Get.offAll(() => HomeScreen());
        return;
      }

      final dayString = FormatterManager.getDay(DateTime.now());
      final savedDate = DataProvider().getAutoLogoutDate();

      if (savedDate.isEmpty) {
        DataProvider().setAutoLogoutDate(dayString);
        DataProvider().setIsLogin(false);
        _goToLogin();
      } else if (savedDate == dayString) {
        if (DataProvider().isLoggedIn()) {
          Get.offAll(() => HomeScreen());
        } else {
          DataProvider().setAutoLogoutDate(dayString);
          DataProvider().setIsLogin(false);
          _goToLogin();
        }
      } else {
        DataProvider().setAutoLogoutDate(dayString);
        DataProvider().setIsLogin(false);
        _goToLogin();
      }
    });
  }
}
