import 'dart:convert';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
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

class ResetPasswordScreen extends StatefulWidget {
  const ResetPasswordScreen({super.key});

  @override
  State<ResetPasswordScreen> createState() => _ResetPasswordScreenState();
}

class _ResetPasswordScreenState extends State<ResetPasswordScreen> {
  late TextEditingController _passwordTextController;
  late TextEditingController _confirmPasswordTextController;
  late ForgotPasswordController _controller;
  final List<Worker> _workers = [];
  final _formKey = GlobalKey<FormState>();
  bool _isObscure = true;
  bool _workersSetUp = false;

  void _toggleObscureText() {
    setState(() {
      _isObscure = !_isObscure;
    });
  }

  bool isButtonEnable() {
    return _passwordTextController.text.isNotEmpty &&
        _confirmPasswordTextController.text.isNotEmpty;
  }

  @override
  void initState() {
    _passwordTextController = TextEditingController();
    _confirmPasswordTextController = TextEditingController();
    _controller = Get.put(
      ForgotPasswordController(repository: ForgotPasswordRepository()),
      tag: 'reset_password',
    );
    super.initState();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_workersSetUp) {
      _workersSetUp = true;
      _workers.add(
        ever(_controller.forgotPasswordRequestStatus, (status) {
          if (!mounted) return;
          if (status.isSuccess) {
            final res = jsonDecode(
              _controller.forgotPasswordRequestResponse.value,
            );
            if (res['status'] == 'Success') {
              WidgetsBinding.instance.addPostFrameCallback((_) {
                ScaffoldMessenger.of(context)
                  ..clearSnackBars()
                  ..showSnackBar(
                    const SnackBar(
                      content: Text("Password reset successfully"),
                      backgroundColor: Colors.green,
                      duration: Duration(seconds: 2),
                    ),
                  );
                Navigator.pushNamedAndRemoveUntil(
                  context,
                  AppRoutes.loginScreen,
                  (Route<dynamic> route) => false,
                );
              });
            } else {
              ScaffoldMessenger.of(context)
                ..clearSnackBars()
                ..showSnackBar(
                  SnackBar(
                    content: Text('${res['status']} \nPlease try again!'),
                    backgroundColor: Colors.red,
                    duration: const Duration(seconds: 2),
                  ),
                );
            }
          }
        }),
      );
    }
  }

  @override
  void dispose() {
    for (final w in _workers) {
      w.dispose();
    }
    Get.delete<ForgotPasswordController>(tag: 'reset_password');
    _passwordTextController.dispose();
    _confirmPasswordTextController.dispose();
    super.dispose();
  }

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter password';
    }

    if (value.length < 3) {
      return 'Password must be at least 3 characters long';
    }
    String pattern =
        r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=])[A-Za-z\d@#$%^&+=]{8,}$';
    RegExp regExp = RegExp(pattern);
    bool isValid = regExp.hasMatch(value);
    if (!isValid) {
      return "Password must include mentioned rules ";
    }
    return null;
  }

  String? _valildateConfirmPassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please enter confirm password';
    }

    if (value != _passwordTextController.text) {
      return 'password and confirm password does not match';
    }

    return null;
  }

  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> args =
        ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    final String? mobileNo = args['otp_details']['mobileNo'];
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
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
                    height: SizeConfig.screenHeight * 0.85,
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
                                    "Reset Password",
                                    style: TextStyle(
                                      color: Colors.white,
                                      fontSize: SizeConfig.screenHeight * 0.024,
                                    ),
                                  ),
                                  const SizedBox(height: 10),
                                  Text(
                                    "Set the password to your account so\nyou can login and access all the featuers",
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
                                    controller: _passwordTextController,
                                    validators: _validatePassword,
                                    errorText: _validatePassword(
                                      _passwordTextController.text,
                                    ),
                                    onChange: (p0) {
                                      _validatePassword(p0);
                                      setState(() {});
                                    },
                                    obscureText: _isObscure,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Password',
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
                                      fontSize: responsiveFont(14),
                                    ),
                                    suffixIcon: GestureDetector(
                                      onTap: () {
                                        _toggleObscureText();
                                      },
                                      child:
                                          _isObscure
                                              ? Image.asset(icKey, scale: 3.5)
                                              : Transform.rotate(
                                                angle: pi,
                                                child: Image.asset(
                                                  icKey,
                                                  scale: 3.5,
                                                ),
                                              ),
                                    ),
                                  ),
                                  SizedBox(
                                    height: getProportionateScreenHeight(20),
                                  ),
                                  AppTextField(
                                    controller: _confirmPasswordTextController,
                                    validators: _valildateConfirmPassword,
                                    errorText: _valildateConfirmPassword(
                                      _confirmPasswordTextController.text,
                                    ),
                                    onChange: (p0) {
                                      _valildateConfirmPassword(p0);
                                      setState(() {});
                                    },
                                    obscureText: true,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Confirm Password',
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
                                      fontSize: responsiveFont(14),
                                    ),
                                    suffixIcon: Image.asset(
                                      icKey,
                                      scale: 3.5,
                                    ),
                                  ),
                                  SizedBox(
                                    height: getProportionateScreenHeight(89),
                                  ),
                                  Text(
                                    "Note : The password must contain alphabet with 8 characters long and included at least one number or\nspecial character.\nSpecial Characters allowed (@, !, #, \$, _) ",
                                    style: TextStyle(
                                      color: Colors.white,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                  SizedBox(
                                    height: getProportionateScreenHeight(60),
                                  ),
                                  Obx(() {
                                    return _controller
                                            .forgotPasswordRequestStatus
                                            .value
                                            .isInProgress
                                        ? const CircularProgressIndicator(
                                          color: kWhiteColor,
                                        )
                                        : AppButton(
                                          title: "Update Password",
                                          onTap: () {
                                            if (_formKey.currentState
                                                    ?.validate() ==
                                                false) {
                                              return;
                                            }
                                            final payload = {
                                              'emailId':
                                                  'dk.dishank123@gmail.com',
                                              'mobile': mobileNo,
                                              'confirmPassword':
                                                  _confirmPasswordTextController
                                                      .text,
                                            };
                                            _controller.forgotPasswordRequest(
                                              payload,
                                            );
                                          },
                                          mWidth: SizeConfig.screenWidth * 0.5,
                                          buttonColor:
                                              isButtonEnable()
                                                  ? Colors.white
                                                  : Colors.white.withValues(
                                                    alpha: 0.3,
                                                  ),
                                          textStyle: TextStyle(
                                            fontSize: responsiveFont(16),
                                            fontWeight: FontWeight.w400,
                                            color:
                                                isButtonEnable()
                                                    ? Colors.black
                                                    : Colors.white.withValues(
                                                      alpha: 0.3,
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
      ),
    );
  }
}
