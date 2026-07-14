import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/calling_modules/widgets/network_wrapper.dart';

import '../../../utilities/toast_manager.dart';
import '../../../constants/constants.dart';
import '../../../constants/fonts.dart';
import '../../../constants/images.dart';
import '../../../utilities/size_config.dart';
import '../../../common_widgets/AppButtonWithIcon.dart';
import '../../../common_widgets/AppTextField.dart';
import '../controllers/login_controller.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  LoginController get controller {
    if (!Get.isRegistered<LoginController>()) {
      Get.lazyPut<LoginController>(() => LoginController());
    }
    return Get.find<LoginController>();
  }

  @override
  Widget build(BuildContext context) {
    final ctrl = controller;
    return NetworkWrapper(
      onRetry: () async {
        final result = await Connectivity().checkConnectivity();
        final isConnected =
            result.contains(ConnectivityResult.mobile) ||
            result.contains(ConnectivityResult.wifi);
        if (!isConnected) ToastManager.toast("No Internet");
      },
      child: Scaffold(
        backgroundColor: kWhiteColor,
        resizeToAvoidBottomInset: true,
        body: AnnotatedRegion<SystemUiOverlayStyle>(
          value: SystemUiOverlayStyle.light,
          child: SingleChildScrollView(
            child: Container(
              color: Colors.white,
              width: SizeConfig.screenWidth,
              child: Stack(
                children: [
                  Positioned(
                    top: 38,
                    child: Image.asset(
                      rect4,
                      fit: BoxFit.fill,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(280.37),
                    ),
                  ),
                  Positioned(
                    top: 38,
                    child: Image.asset(
                      rect3,
                      fit: BoxFit.fill,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(260.37),
                    ),
                  ),
                  Positioned(
                    top: 38,
                    child: Image.asset(
                      rect2,
                      fit: BoxFit.fill,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(240.37),
                    ),
                  ),
                  Image.asset(
                    rect1,
                    fit: BoxFit.fill,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(270.37),
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(
                      horizontal: 24.h,
                      vertical: 84,
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Welcome!",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: responsiveFont(24),
                            color: kWhiteColor,
                            fontWeight: FontWeight.w700,
                          ),
                        ),
                        SizedBox(height: responsiveHeight(10)),
                        SizedBox(
                          width: SizeConfig.screenWidth - responsiveHeight(48),
                          child: Text(
                            "Enter your Username & Password to continue",
                            maxLines: 2,
                            overflow: TextOverflow.visible,
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(20),
                              color: kWhiteColor,
                              fontWeight: FontWeight.w400,
                            ),
                          ),
                        ),
                        SizedBox(height: responsiveHeight(16)),
                        Align(
                          alignment: FractionalOffset.centerLeft,
                          child: Text(
                            "Login",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(24),
                              fontWeight: FontWeight.w500,
                              color: Colors.black,
                            ),
                          ).paddingOnly(top: 110),
                        ),
                        SizedBox(height: responsiveHeight(14)),

                        // Username field — Obx only for error text
                        Obx(
                          () => AppTextField(
                            controller: ctrl.usernameController,
                            errorText: ctrl.usernameError.value.isEmpty
                                ? null
                                : ctrl.usernameError.value,
                            onChange: ctrl.onUsernameChanged,
                            inputStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: 16,
                            ),
                            label: RichText(
                              text: TextSpan(
                                text: 'Username',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kLabelTextColor,
                                  fontSize: responsiveFont(16),
                                  fontWeight: FontWeight.w400,
                                ),
                                children: [
                                  TextSpan(
                                    text: ' *',
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      color: Colors.red,
                                      fontSize: responsiveFont(18),
                                      fontWeight: FontWeight.w400,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            labelStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(16),
                            ),
                            prefixIcon: Image.asset(userRound, scale: 3.5),
                          ),
                        ),
                        const SizedBox(height: 20),

                        // Password field — Obx for error text + obscure toggle
                        Obx(
                          () => AppTextField(
                            controller: ctrl.passwordController,
                            errorText: ctrl.passwordError.value.isEmpty
                                ? null
                                : ctrl.passwordError.value,
                            onChange: ctrl.onPasswordChanged,
                            obscureText: ctrl.isObscure.value,
                            inputStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: 16,
                            ),
                            label: RichText(
                              text: TextSpan(
                                text: 'Password',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kLabelTextColor,
                                  fontSize: responsiveFont(16),
                                  fontWeight: FontWeight.w400,
                                ),
                                children: [
                                  TextSpan(
                                    text: ' *',
                                    style: TextStyle(color: Colors.red),
                                  ),
                                ],
                              ),
                            ),
                            labelStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(20),
                            ),
                            prefixIcon: Image.asset(iconPass, scale: 3.5),
                            suffixIcon: IconButton(
                              icon: Icon(
                                ctrl.isObscure.value
                                    ? Icons.visibility_off
                                    : Icons.visibility,
                                color: Colors.grey,
                              ),
                              onPressed: ctrl.toggleObscure,
                            ),
                          ),
                        ),
                        SizedBox(height: responsiveHeight(10)),

                        // Checkbox — Obx for checked state
                        Obx(
                          () => Transform.scale(
                            alignment: FractionalOffset.centerLeft,
                            scale: 1,
                            child: CheckboxListTile(
                              controlAffinity: ListTileControlAffinity.leading,
                              materialTapTargetSize:
                                  MaterialTapTargetSize.shrinkWrap,
                              value: ctrl.isRememberMe.value,
                              contentPadding: EdgeInsets.zero,
                              onChanged: ctrl.toggleRememberMe,
                              title: Text(
                                "Keep Me Sign In?",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kBlackColor,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ),
                        ),
                        SizedBox(height: responsiveHeight(14)),

                        // Login button — no Obx needed (loader via ToastManager)
                        Padding(
                          padding: const EdgeInsets.only(bottom: 8.0),
                          child: AppButtonWithIcon(
                            buttonColor: kButtonColor,
                            title: "Login",
                            icon: Image.asset(
                              iconArrow,
                              height: responsiveHeight(24),
                              width: responsiveHeight(24),
                            ),
                            mWidth: SizeConfig.screenWidth,
                            textStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color: Colors.white,
                              fontSize: responsiveFont(16),
                              fontWeight: FontWeight.w700,
                            ),
                            onTap: ctrl.login,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
