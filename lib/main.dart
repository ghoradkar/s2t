import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get_navigation/src/root/get_material_app.dart';
import 'package:s2toperational/Modules/themes/AppTheme.dart';
import 'Modules/utilities/DataProvider.dart';
import 'Modules/utilities/SizeConfig.dart';
import 'Screens/CallingModules/calling/bloc/app_bloc_observer.dart';
import 'Screens/CallingModules/calling/bloc/expected_beneficiary_bloc.dart';
import 'Screens/CallingModules/calling/repository/beneficiary_repository.dart';
import 'Screens/CallingModules/custom_widgets/bloc/beneficiary_card_bloc_bloc.dart';
import 'Screens/CallingModules/custom_widgets/repository/beneficiary_card.dart';
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

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
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
          home: const SplashScreen(),
          builder: EasyLoading.init(),
        );
      },
    );
  }
}