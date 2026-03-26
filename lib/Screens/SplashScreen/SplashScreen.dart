// ignore_for_file: use_build_context_synchronously, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/DeviceInfoUtil.dart';
import 'package:s2toperational/Screens/LoginScreen/LoginScreen.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../HomeScreen/HomeScreen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  String? appVersion;

  @override
  void initState() {
    super.initState();
    APIManager apiManager = APIManager();
    apiManager.apiMode = APIMode.Live;
    // apiManager.apiMode = kReleaseMode ? APIMode.Live : APIMode.Beta;
    apiManager.setAPIEnvironment();
    getAppVersion();
    _checkVersionThenNavigate();
  }

  Future<void> _checkVersionThenNavigate() async {
    var deviceInfo = await DeviceInfoUtil().getPackageInfo();
    String version = deviceInfo.version;

    bool updateRequired = false;
    String updateMessage = '';

    await APIManager().checkAppVersionAPI(
      version: version,
      onResult: (status, message) {
        if (status.toLowerCase() == 'success') {
          updateRequired = true;
          updateMessage = message;
        }
      },
    );

    if (!mounted) return;

    if (updateRequired) {
      _showForceUpdateDialog(updateMessage);
      return;
    }

    _navigateNext();
  }

  void _showForceUpdateDialog(String message) {

    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (ctx) => PopScope(
            canPop: false,
            child: ToastManager.commonAlert(
              context,
              softwareUpdate,
              "App Update",
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
                  () {
                SystemNavigator.pop();
              },
              "Update",
              "Cancel",
            )


            // AlertDialog(
            //   title: const Text('App Update'),
            //   content: Text(message),
            //   actions: [
            //     TextButton(
            //       onPressed: () async {
            //         const packageName = 'com.example.s2toperational';
            //         final uri = Uri.parse(
            //           // 'https://play.google.com/store/apps/details?id=$packageName',
            //           'https://play.google.com/store/apps/details?id=in.janarogyaseva.s2t_operational', // remove this once package name decided
            //         );
            //         if (await canLaunchUrl(uri)) {
            //           await launchUrl(
            //             uri,
            //             mode: LaunchMode.externalApplication,
            //           );
            //         }
            //       },
            //       child: const Text('UPDATE'),
            //     ),
            //     TextButton(
            //       onPressed: () => SystemNavigator.pop(),
            //       child: const Text('CANCEL'),
            //     ),
            //   ],
            // ),
          ),
    );
  }

  void _navigateNext() {
    LoginResponseModel? loginResponseModel = DataProvider().getParsedUserData();
    int designaionId = loginResponseModel?.output?.first.dESGID ?? 0;
    if (DataProvider().isLoggedIn()) {
      if (designaionId == 166 || designaionId == 51) {
        showHomeScreen();
      } else {
        String dayString = FormatterManager.getDay(DateTime.now());

        if (DataProvider().getAutoLogoutDate().isEmpty) {
          DataProvider().setAutoLogoutDate(dayString);
          DataProvider().setIsLogin(false);
          showLoginScreen();
        } else if (DataProvider().getAutoLogoutDate() == dayString) {
          if (DataProvider().isLoggedIn()) {
            showHomeScreen();
          } else {
            DataProvider().setAutoLogoutDate(dayString);
            DataProvider().setIsLogin(false);
            showLoginScreen();
          }
        } else {
          DataProvider().setAutoLogoutDate(dayString);
          DataProvider().setIsLogin(false);
          showLoginScreen();
        }
      }
    } else {
      showLoginScreen();
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      body: AnnotatedRegion<SystemUiOverlayStyle>(
        value: SystemUiOverlayStyle.light,
        child: Stack(
          children: [
            Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  const Spacer(),
                  Image.asset(
                    splashScreenLogoNew,
                    height: SizeConfig.screenHeight * 0.2,
                  ),
                  Spacer(),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Version : ",
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color: Colors.black,
                          fontSize: 16,
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                      Text(
                        appVersion ?? '',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color: Colors.black,
                          fontSize: 16,
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void getAppVersion() async {
    var deviceInfo = await DeviceInfoUtil().getPackageInfo();
    appVersion = deviceInfo.version;
    setState(() {});
  }

  void showHomeScreen() {
    Future.delayed(const Duration(seconds: 3), () async {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (BuildContext context) => HomeScreen()),
        (Route route) => false,
      );
    });
  }

  void showLoginScreen() {
    Future.delayed(const Duration(seconds: 3), () async {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(
          builder: (BuildContext context) => const LoginScreen(),
        ),
        (Route route) => false,
      );
    });
  }
}
