import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get_navigation/src/root/get_material_app.dart';
import 'package:s2toperational/Modules/themes/AppTheme.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/CallingModules/bloc/app_bloc_observer.dart';
import 'package:s2toperational/Screens/CallingModules/bloc/beneficiary_card_bloc_bloc.dart';
import 'package:s2toperational/Screens/CallingModules/bloc/expected_beneficiary_bloc.dart';
import 'package:s2toperational/Screens/CallingModules/controllers/beneficiary_card_controller.dart';
import 'package:s2toperational/Screens/CallingModules/controllers/expected_beneficiary_controller.dart';
import 'package:s2toperational/Screens/CallingModules/repository/beneficiary_card_repository.dart';
import 'package:upgrader/upgrader.dart';
import 'Modules/utilities/DataProvider.dart';
import 'Modules/utilities/SizeConfig.dart';
import 'Screens/CallingModules/repository/beneficiary_repository.dart';
import 'Screens/CallingModules/routes/app_routes.dart';
import 'Screens/SplashScreen/SplashScreen.dart';
import 'Modules/utilities/route_observer.dart';

/// Bypasses SSL certificate validation globally — required because the server
/// uses a self-signed/untrusted certificate. Consistent with APIManager's
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

  Bloc.observer = AppBlocObserver();

  // Register GetX controllers (running alongside BLoC during migration)
  Get.put(ExpectedBeneficiaryController(repository: BeneficiaryRepository()));
  Get.put(BeneficiaryCardController(repository: BeneficiaryCardRepository()));

  await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  runApp(
    MultiRepositoryProvider(
      providers: [
        RepositoryProvider<BeneficiaryRepository>(
          create: (context) => BeneficiaryRepository(),
        ),
        RepositoryProvider<BeneficiaryCardRepository>(
          create: (context) => BeneficiaryCardRepository(),
        ),
      ],
      child: MultiBlocProvider(
        providers: [
          BlocProvider(
            create:
                (context) => ExpectedBeneficiaryBloc(
                  repository: context.read<BeneficiaryRepository>(),
                ),
          ),
          BlocProvider(
            create:
                (context) => BeneficiaryCardBlocBloc(
                  repository: context.read<BeneficiaryCardRepository>(),
                ),
          ),
        ],
        child: const MyApp(),
      ),
    ),
  );
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
          theme: AppTheme.lightTheme,
          onGenerateRoute: AppRoutes.onGenerateRoute,
          navigatorObservers: [routeObserver],
          debugShowCheckedModeBanner: false,
          home: UpgradeAlert(
            showIgnore: false,
            showLater: false,
            onUpdate: () => true,
            shouldPopScope: () => false,
            upgrader: Upgrader(debugDisplayAlways: false, debugLogging: false),
            child: const SplashScreen(),
          ),
          builder: EasyLoading.init(),
        );
      },
    );
  }
}
