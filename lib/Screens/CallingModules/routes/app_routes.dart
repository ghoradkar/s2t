import 'package:flutter/material.dart';

import '../../ForgotPasswordScreen/ForgotPasswordScreen.dart';
import '../../LoginScreen/LoginScreen.dart';
import '../../SplashScreen/SplashScreen.dart';
import '../calling/add_dependent.dart';
import '../calling/appointment_confirmation.dart';
import '../calling/expected_beneficiary_list.dart';
import '../custom_widgets/exceptions/route_exception.dart';
import '../custom_widgets/logout_widget.dart';
import '../forgot_password/ui/forgot_password_otp.dart';
import '../forgot_password/ui/reset_password.dart';

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
          builder: (_) => const ExpectedBeneficiaryList(),
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
        return MaterialPageRoute(
          builder: (_) => const AppointmentConfirmation(),
          settings: settings,
        );
      case addDependent:
        return MaterialPageRoute(
          builder: (_) => const AddDependentScreen(),
          settings: settings,
        );

      default:
        throw const RouteException('Route not found!');
    }
  }
}
