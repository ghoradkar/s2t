import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/add_dependent_controller.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/appointment_confirmation_controller.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_list_controller.dart';
import 'package:s2toperational/Screens/calling_modules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/calling_modules/screens/add_dependent.dart';
import 'package:s2toperational/Screens/calling_modules/screens/appointment_confirmation.dart';
import 'package:s2toperational/Screens/calling_modules/screens/expected_beneficiary_list.dart';
import 'package:s2toperational/Screens/forgot_password/ui/forgot_password_otp.dart';
import 'package:s2toperational/Screens/forgot_password/ui/reset_password.dart';
import '../../ForgotPasswordScreen/ForgotPasswordScreen.dart';
import '../../LoginScreen/LoginScreen.dart';
import '../../SplashScreen/SplashScreen.dart';
import '../custom_widgets/logout_widget.dart';

class AppRoutes {
  const AppRoutes._();

  static const String splash = "/";
  static const String loginScreen = "/loginScreen";
  static const String introScreen = "/introScreen";
  static const String dashboard = "/dashboard";
  static const String mobileOTP = "/mobileOTP";
  static const String setOTP = "/setOTP";
  static const String homeScreen = "/homeScreen";
  static const String forgotScreen = "/forgotScreen";
  static const String forgotPasswordOTP = "/forgotPasswordOTP";
  static const String resetPassword = "/resetPassword";
  static const String otpLoginScreen = "/otpLoginScreen";
  static const String registredPatientList = "/registredPatientList";
  static const String newRegistration = "/newRegistration";
  static const String bookAppointments = "/bookAppointments";
  static const String myAppointments = "/myAppointments";
  static const String confirmLoginOTP = "/confirmLoginOTP";
  static const String profileScreen = "/profileScreen";
  static const String registeredPatientScreen = "/registeredPatientScreen";
  static const String addFamilyMemberScreen = "/addFamilyMemberScreen";
  static const String updateFamilyMemberScreen = "/updateFamilyMemberScreen";
  static const String idCardList = "/idCardList";
  static const String idCard = "/idCard";
  static const String menuScreen = "/menuScreen";
  static const String expectectedBeneficiaryList =
      "/expectectedBeneficiaryList";
  static const String logoutScreen = "/logoutScreen";
  static const String appointmentConfirmation = "/appointmentConfirmation";
  static const String addDependent = "/addDependent";

  static Route<dynamic> onGenerateRoute(RouteSettings settings) {
    switch (settings.name) {
      case splash:
        return MaterialPageRoute(builder: (_) => const SplashScreen());
      // case introScreen:
      //   return MaterialPageRoute(builder: (_) => const IntroScreen());
      case loginScreen:
        return MaterialPageRoute(builder: (_) => LoginScreen());

      case forgotScreen:
        return MaterialPageRoute(builder: (_) => ForgotPasswordScreen());

      // case menuScreen:
      //   return MaterialPageRoute(builder: (_) => const SideDrawerMenu());
      case expectectedBeneficiaryList:
        return MaterialPageRoute(
          builder: (_) {
            if (!Get.isRegistered<ExpectedBeneficiaryListController>()) {
              Get.put(ExpectedBeneficiaryListController());
            }
            return const ExpectedBeneficiaryList();
          },
          settings: settings,
        );

      case forgotPasswordOTP:
        return MaterialPageRoute(
          builder: (_) => const ForgotPasswordOTPScreen(),
          settings: settings,
        );
      case resetPassword:
        return MaterialPageRoute(
          builder: (_) => ResetPasswordScreen(),
          settings: settings,
        );
      case logoutScreen:
        return MaterialPageRoute(
          builder: (_) => const LogoutWidget(),
          settings: settings,
        );
      case appointmentConfirmation:
        {
          final beneficiary = settings.arguments as BeneficiaryOutput;
          // Always delete stale instance so each navigation gets a fresh
          // controller initialised with the correct beneficiary.
          Get.delete<AppointmentConfirmationController>(force: true);
          Get.put(AppointmentConfirmationController(beneficiary: beneficiary));
          return MaterialPageRoute(
            builder: (_) => const AppointmentConfirmation(),
            settings: settings,
          );
        }
      case addDependent:
        {
          final args = settings.arguments as Map<String, dynamic>;
          Get.delete<AddDependentController>(force: true);
          Get.put(AddDependentController(args: args));
          return MaterialPageRoute(
            builder: (_) => const AddDependentScreen(),
            settings: settings,
          );
        }

      default:
        throw const RouteException('Route not found!');
    }
  }
}

class RouteException implements Exception {
  final String message;

  const RouteException(this.message);
}
