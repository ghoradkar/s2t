import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/calling_modules/routes/app_routes.dart';

import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppButton.dart';
import '../../../../Modules/widgets/AppTextField.dart';
import '../controllers/forgot_password_controller.dart';
import '../repository/forgot_password_repository.dart';
import '../validators.dart' show UIValidator;

class ForgotPasswordScreen extends StatefulWidget {
  const ForgotPasswordScreen({super.key});

  @override
  State<ForgotPasswordScreen> createState() => _ForgotPasswordScreenState();
}

class _ForgotPasswordScreenState extends State<ForgotPasswordScreen> {
  late TextEditingController _mobileTextController;
  late TextEditingController _emailTextController;
  late ForgotPasswordController _controller;
  final List<Worker> _workers = [];
  final _formKey = GlobalKey<FormState>();

  bool isButtonEnable() {
    return _mobileTextController.text.isNotEmpty ||
        _emailTextController.text.isNotEmpty;
  }

  @override
  void initState() {
    super.initState();
    _mobileTextController = TextEditingController();
    _emailTextController = TextEditingController();
    _controller = Get.put(
      ForgotPasswordController(repository: ForgotPasswordRepository()),
      tag: 'forgot_password',
    );
    _workers.add(
      ever(_controller.checkAndGenerateOTPStatus, (status) {
        if (!mounted) return;
        if (status.isSuccess) {
          final res = jsonDecode(
            _controller.checkAndGenerateOTPResponse.value,
          );
          if (res['status'] == 'Success') {
            WidgetsBinding.instance.addPostFrameCallback((_) {
              ScaffoldMessenger.of(context)
                ..clearSnackBars()
                ..showSnackBar(
                  const SnackBar(
                    content: Text("OTP Sent successfully"),
                    backgroundColor: Colors.green,
                    duration: Duration(seconds: 2),
                  ),
                );
              final Map<String, dynamic> arguments = {
                "otp_details": res,
                "otp_type":
                    _mobileTextController.text.isEmpty ? 'email' : 'mobile',
                'email': _emailTextController.text,
              };
              Navigator.pushNamed(
                context,
                AppRoutes.forgotPasswordOTP,
                arguments: arguments,
              );
            });
          }
        }
      }),
    );
  }

  @override
  void dispose() {
    for (final w in _workers) {
      w.dispose();
    }
    Get.delete<ForgotPasswordController>(tag: 'forgot_password');
    _mobileTextController.dispose();
    _emailTextController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
          children: [
            Container(
              color: Colors.white,
              child: Column(
                children: [
                  const SizedBox(height: 50),
                  Image.asset(smallLogo, height: 100),
                  const SizedBox(height: 20),
                ],
              ),
            ),
            Stack(
              children: [
                Container(
                  height: SizeConfig.screenHeight * 0.8,
                  decoration: const BoxDecoration(
                    image: DecorationImage(
                      image: AssetImage(background),
                      fit: BoxFit.fill,
                    ),
                  ),
                ),
                Center(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        Container(
                          padding: const EdgeInsets.all(16.0),
                          child: Form(
                            key: _formKey,
                            child: Column(
                              children: [
                                Text(
                                  "Forgot Password",
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontSize: SizeConfig.screenHeight * 0.024,
                                  ),
                                ),
                                const SizedBox(height: 10),
                                Text(
                                  "Enter your Email or Mobile number\nfor the verification process\nand we will send you OTP to your Email or Number",
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontSize: SizeConfig.screenHeight * 0.016,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                                SizedBox(
                                  height: SizeConfig.screenHeight * 0.1,
                                ),
                                AppTextField(
                                  controller: _mobileTextController,
                                  textInputType: TextInputType.phone,
                                  validators:
                                      _emailTextController.text.isEmpty
                                          ? UIValidator.validateMobileNo
                                          : null,
                                  errorText:
                                      _emailTextController.text.isEmpty
                                          ? UIValidator.validateMobileNo(
                                            _mobileTextController.text,
                                          )
                                          : null,
                                  onChange: (p0) {
                                    _emailTextController.clear();
                                    UIValidator.validateMobileNo(
                                      _mobileTextController.text,
                                    );
                                    setState(() {});
                                  },
                                  maxLength: 10,
                                  label: RichText(
                                    text: TextSpan(
                                      text: 'Mobile Number',
                                      style: TextStyle(
                                        color: kLabelTextColor,
                                        fontSize: responsiveFont(14),
                                      ),
                                      children: <TextSpan>[
                                        TextSpan(
                                          text: ' *',
                                          style: TextStyle(
                                            color: Colors.red,
                                            fontSize: responsiveFont(14),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  labelStyle: TextStyle(
                                    color: Colors.white,
                                    fontSize: SizeConfig.screenHeight * 0.014,
                                  ),
                                  suffixIcon: Image.asset(
                                    icMobileNumber,
                                    scale: 3.5,
                                  ),
                                ),
                                SizedBox(
                                  height: SizeConfig.screenHeight * 0.05,
                                ),
                                Text(
                                  "OR",
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontSize: SizeConfig.screenHeight * 0.014,
                                  ),
                                ),
                                AppTextField(
                                  controller: _emailTextController,
                                  textInputType: TextInputType.emailAddress,
                                  validators:
                                      _mobileTextController.text.isEmpty
                                          ? UIValidator.validateEmail
                                          : null,
                                  errorText:
                                      _mobileTextController.text.isEmpty
                                          ? UIValidator.validateEmail(
                                            _emailTextController.text,
                                          )
                                          : null,
                                  onChange: (p0) {
                                    _mobileTextController.clear();
                                    UIValidator.validateEmail(
                                      _mobileTextController.text,
                                    );
                                    setState(() {});
                                  },
                                  maxLength: 100,
                                  label: RichText(
                                    text: TextSpan(
                                      text: 'Email',
                                      style: TextStyle(
                                        color: kLabelTextColor,
                                        fontSize: responsiveFont(14),
                                      ),
                                      children: <TextSpan>[
                                        TextSpan(
                                          text: ' *',
                                          style: TextStyle(
                                            color: Colors.red,
                                            fontSize: responsiveFont(14),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                  labelStyle: TextStyle(
                                    color: Colors.white,
                                    fontSize: SizeConfig.screenHeight * 0.014,
                                  ),
                                  suffixIcon: Image.asset(
                                    icEmailWhite,
                                    scale: 3.5,
                                  ),
                                ),
                                SizedBox(
                                  height: getProportionateScreenHeight(130),
                                ),
                                Obx(() {
                                  return _controller
                                          .checkAndGenerateOTPStatus
                                          .value
                                          .isInProgress
                                      ? const CircularProgressIndicator(
                                        color: kWhiteColor,
                                      )
                                      : AppButton(
                                        title: "Send OTP",
                                        onTap: () {
                                          if (_formKey.currentState
                                                  ?.validate() ==
                                              false) {
                                            return;
                                          }
                                          _controller.checkAndGenerateOTP({
                                            'mobile':
                                                _mobileTextController.text,
                                            'email': _emailTextController.text,
                                          });
                                        },
                                        mWidth: SizeConfig.screenWidth * 0.5,
                                        buttonColor:
                                            isButtonEnable()
                                                ? Colors.white
                                                : Colors.white.withOpacity(0.3),
                                        textStyle: TextStyle(
                                          color:
                                              isButtonEnable()
                                                  ? Colors.black
                                                  : Colors.white.withOpacity(
                                                    0.3,
                                                  ),
                                        ),
                                      );
                                }),
                                const SizedBox(height: 20),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
