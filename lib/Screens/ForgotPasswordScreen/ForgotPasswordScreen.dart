// ignoreforfile: filenames

// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppButtonWithIcon.dart';
import '../../Modules/widgets/AppTextField.dart';

class ForgotPasswordScreen extends StatefulWidget {
  const ForgotPasswordScreen({super.key});

  @override
  State<ForgotPasswordScreen> createState() => ForgotPasswordScreenState();
}

class ForgotPasswordScreenState extends State<ForgotPasswordScreen> {
  late TextEditingController mobileTextController;

  late TextEditingController emailTextController;
  final formKey = GlobalKey<FormState>();

  bool isButtonEnable() {
    return mobileTextController.text.isNotEmpty ||
        emailTextController.text.isNotEmpty;
  }

  @override
  void initState() {
    super.initState();
    mobileTextController = TextEditingController();
    emailTextController = TextEditingController();
  }

  @override
  void dispose() {
    mobileTextController.dispose();
    emailTextController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor, // Change to your desired color
            statusBarBrightness: Brightness.dark,
            statusBarIconBrightness:
                Brightness.light, // For light text/icons on the status bar
          ),
          child: SingleChildScrollView(
            child: Container(
              color: Colors.white,
              height: SizeConfig.screenHeight + 50,
              width: SizeConfig.screenWidth,
              child: Stack(
                children: [
                  Positioned(
                    top: 74,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect4,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Positioned(
                    top: 53,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect3,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Positioned(
                    top: 30,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect2,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Image.asset(
                    fit: BoxFit.fill,
                    rect1,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(300.37),
                  ),
                  Positioned(
                    top: responsiveHeight(44),
                    child: Padding(
                      padding: EdgeInsets.all(responsiveHeight(24)),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Forgot Password",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(24),
                              color: kWhiteColor,
                              fontWeight: FontWeight.w700,
                            ),
                          ),
                          SizedBox(height: responsiveHeight(15)),
                          Text(
                            "Enter your Email or Mobile number for the\nverification process and we will send you OTP\nto your Email or Number",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(18),
                              color: kWhiteColor,
                              fontWeight: FontWeight.w400,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Positioned(
                    top: responsiveHeight(380),
                    right: 10,
                    left: 10,
                    child: Center(
                      child: SizedBox(
                        height: SizeConfig.screenHeight * 2,
                        width: SizeConfig.screenWidth * 0.9,
                        child: Form(
                          key: formKey,
                          child: Column(
                            children: [
                              SizedBox(height: responsiveHeight(27)),
                              AppTextField(
                                controller: mobileTextController,
                                textInputType: TextInputType.number,
                                maxLength: 10,

                                // errorText:
                                //     isSubmitted
                                //         ? UIValidator.validateInput(
                                //           mobileTextController.text,
                                //         )
                                //         : null,
                                onChange: (p0) {
                                  setState(
                                    () {},
                                  ); // Update the UI when the user types
                                },
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Mobile No',
                                    style: TextStyle(
                                      color: kLabelTextColor,
                                      fontSize: responsiveFont(18),
                                    ),
                                    children: <TextSpan>[
                                      TextSpan(
                                        text: ' *',
                                        style: TextStyle(
                                          fontFamily: FontConstants.interFonts,
                                          color: Colors.red,
                                          fontSize: responsiveFont(18),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                                labelStyle: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: responsiveFont(18),
                                ),
                                prefixIcon: Image.asset(userRound, scale: 3.5),
                              ),
                              SizedBox(height: responsiveHeight(80)),
                              Padding(
                                padding: const EdgeInsets.only(bottom: 8.0),
                                child: AppButtonWithIcon(
                                  buttonColor: kButtonColor,
                                  title: "GET OTP",
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
                                  ),
                                  onTap: () {
                                    // loginUser();
                                  },
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
    // Scaffold(
    //   body: SingleChildScrollView(
    //     child: Column(
    //       children: [
    //         Container(
    //           color: Colors.white,
    //           child: Column(
    //             children: [
    //               const SizedBox(height: 50),
    //               Image.asset(smallLogo, height: 100),
    //               const SizedBox(height: 20),
    //             ],
    //           ),
    //         ),
    //         Stack(
    //           children: [
    //             Container(
    //               height: SizeConfig.screenHeight * 0.8,
    //               decoration: const BoxDecoration(
    //                 image: DecorationImage(
    //                   image: AssetImage(background),
    //                   fit: BoxFit.fill,
    //                 ),
    //               ),
    //             ),
    //             Center(
    //               child: Padding(
    //                 padding: const EdgeInsets.all(16.0),
    //                 child: Column(
    //                   mainAxisAlignment: MainAxisAlignment.center,
    //                   children: <Widget>[
    //                     Container(
    //                       padding: const EdgeInsets.all(16.0),
    //                       child: Form(
    //                         key: formKey,
    //                         child: Column(
    //                           children: [
    //                             Text(
    //                               "Forgot Password",
    //                               style: TextStyle(
    //                                 color: Colors.white,
    //                                 fontSize: SizeConfig.screenHeight * 0.024,
    //                               ),
    //                             ),
    //                             const SizedBox(height: 10),
    //                             Text(
    //                               "Enter your Email or Mobile number\nfor the verification process\nand we will send you OTP to your Email or Number",
    //                               style: TextStyle(
    //                                 color: Colors.white,
    //                                 fontSize: SizeConfig.screenHeight * 0.016,
    //                               ),
    //                               textAlign: TextAlign.center,
    //                             ),
    //                             SizedBox(height: SizeConfig.screenHeight * 0.1),
    //                             AppTextField(
    //                               controller: mobileTextController,
    //                               textInputType: TextInputType.phone,
    //                               validators:
    //                                   emailTextController.text.isEmpty
    //                                       ? UIValidator.validateMobileNo
    //                                       : null,
    //                               errorText:
    //                                   emailTextController.text.isEmpty
    //                                       ? UIValidator.validateMobileNo(
    //                                         mobileTextController.text,
    //                                       )
    //                                       : null,
    //                               onChange: (p0) {
    //                                 emailTextController.clear();
    //                                 UIValidator.validateMobileNo(
    //                                   mobileTextController.text,
    //                                 );
    //                                 setState(() {});
    //                               },
    //                               maxLength: 10,
    //                               label: RichText(
    //                                 text: TextSpan(
    //                                   text: 'Mobile Number',
    //                                   style: TextStyle(
    //                                     color: kLabelTextColor,
    //                                     fontSize: responsiveFont(14),
    //                                   ),
    //                                   children: <TextSpan>[
    //                                     TextSpan(
    //                                       text: ' *',
    //                                       style: TextStyle(
    //                                         color: Colors.red,
    //                                         fontSize: responsiveFont(14),
    //                                       ),
    //                                     ),
    //                                   ],
    //                                 ),
    //                               ),
    //                               labelStyle: TextStyle(
    //                                 color: Colors.white,
    //                                 fontSize: SizeConfig.screenHeight * 0.014,
    //                               ),
    //                               suffixIcon: Image.asset(
    //                                 icMobileNumber,
    //                                 scale: 3.5,
    //                               ),
    //                             ),
    //                             SizedBox(
    //                               height: SizeConfig.screenHeight * 0.05,
    //                             ),
    //                             Text(
    //                               "OR",
    //                               style: TextStyle(
    //                                 color: Colors.white,
    //                                 fontSize: SizeConfig.screenHeight * 0.014,
    //                               ),
    //                             ),
    //                             AppTextField(
    //                               controller: emailTextController,
    //                               textInputType: TextInputType.emailAddress,
    //                               validators:
    //                                   mobileTextController.text.isEmpty
    //                                       ? UIValidator.validateEmail
    //                                       : null,
    //                               errorText:
    //                                   mobileTextController.text.isEmpty
    //                                       ? UIValidator.validateEmail(
    //                                         emailTextController.text,
    //                                       )
    //                                       : null,
    //                               onChange: (p0) {
    //                                 mobileTextController.clear();
    //                                 UIValidator.validateEmail(
    //                                   mobileTextController.text,
    //                                 );
    //                                 setState(() {});
    //                               },
    //                               maxLength: 100,
    //                               label: RichText(
    //                                 text: TextSpan(
    //                                   text: 'Email',
    //                                   style: TextStyle(
    //                                     color: kLabelTextColor,
    //                                     fontSize: responsiveFont(14),
    //                                   ),
    //                                   children: <TextSpan>[
    //                                     TextSpan(
    //                                       text: ' *',
    //                                       style: TextStyle(
    //                                         color: Colors.red,
    //                                         fontSize: responsiveFont(14),
    //                                       ),
    //                                     ),
    //                                   ],
    //                                 ),
    //                               ),
    //                               labelStyle: TextStyle(
    //                                 color: Colors.white,
    //                                 fontSize: SizeConfig.screenHeight * 0.014,
    //                               ),
    //                               suffixIcon: Image.asset(
    //                                 icEmailWhite,
    //                                 scale: 3.5,
    //                               ),
    //                             ),
    //                             SizedBox(
    //                               height: getProportionateScreenHeight(130),
    //                             ),
    //                             AppButton(
    //                               title: "Send OTP",
    //                               onTap: () {
    //                                 // if (formKey.currentState?.validate() ==
    //                                 //     false) {
    //                                 //   return;
    //                                 // }

    //                                 // context.read<ForgotPasswordBloc>().add(
    //                                 //   CheckAndGenerateOTP(
    //                                 //     payload: {
    //                                 //       'mobile': mobileTextController.text,
    //                                 //       'email': emailTextController.text,
    //                                 //     },
    //                                 //   ),
    //                                 // );
    //                               },
    //                               mWidth: SizeConfig.screenWidth * 0.5,
    //                               buttonColor:
    //                                   isButtonEnable()
    //                                       ? Colors.white
    //                                       : Colors.white.withValues(alpha: 0.3),
    //                               textStyle: TextStyle(
    //                                 color:
    //                                     isButtonEnable()
    //                                         ? Colors.black
    //                                         : Colors.white.withValues(
    //                                           alpha: 0.3,
    //                                         ),
    //                               ),
    //                             ),
    //                             const SizedBox(height: 20),
    //                           ],
    //                         ),
    //                       ),
    //                     ),
    //                   ],
    //                 ),
    //               ),
    //             ),
    //           ],
    //         ),
    //       ],
    //     ),
    //   ),
    // );
  }
}
