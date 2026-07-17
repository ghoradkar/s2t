import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/calling_modules/controller/expected_beneficiary_controller.dart';
import 'package:s2toperational/calling_modules/repository/beneficiary_repository.dart';
import 'utilities/data_provider.dart';
import 'utilities/size_config.dart';
import 'utilities/app_routes.dart';
import 'SplashScreen/controller/splash_controller.dart';
import 'SplashScreen/screens/splash_screen.dart';
import 'utilities/route_observer.dart';

/// Bypasses SSL certificate validation globally — required because the server
/// uses a self-signed/untrusted certificate. Consistent with api_manager's
/// existing `badCertificateCallback = true` on all API calls.
class _BypassSslHttpOverrides extends HttpOverrides {
  @override
  HttpClient createHttpClient(SecurityContext? context) {
    return super.createHttpClient(context)
      ..badCertificateCallback =
          (X509Certificate cert, String host, int port) => true;
  }
}

void main() async {
  HttpOverrides.global = _BypassSslHttpOverrides();
  WidgetsFlutterBinding.ensureInitialized();
  await DataProvider.init();

  // Register GetX controllers
  Get.put(ExpectedBeneficiaryController(repository: BeneficiaryRepository()));

  await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return ScreenUtilInit(
      designSize: Size(428, 926),
      builder: (context, child) {
        return GetMaterialApp(
          title: "S2T Operational",
          onGenerateRoute: AppRoutes.onGenerateRoute,
          navigatorObservers: [routeObserver],
          debugShowCheckedModeBanner: false,
          home: Builder(
            builder: (_) {
              Get.lazyPut(() => SplashController());
              return const SplashScreen();
            },
          ),
          builder: EasyLoading.init(),
        );
      },
    );
  }
}
