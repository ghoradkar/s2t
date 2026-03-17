// ignore_for_file: library_private_types_in_public_api

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:pin_code_fields/pin_code_fields.dart';
import 'package:s2toperational/Screens/CallingModules/routes/app_routes.dart';

import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppButton.dart';
import '../controllers/forgot_password_controller.dart';
import '../repository/forgot_password_repository.dart';

class ForgotPasswordOTPScreen extends StatefulWidget {
  const ForgotPasswordOTPScreen({super.key});

  @override
  _ForgotPasswordOTPScreenState createState() =>
      _ForgotPasswordOTPScreenState();
}

class _ForgotPasswordOTPScreenState extends State<ForgotPasswordOTPScreen> {
  late TextEditingController _otpController;
  late ForgotPasswordController _controller;
  final List<Worker> _workers = [];
  String otp = '';
  final _formKey = GlobalKey<FormState>();
  bool _workersSetUp = false;

  @override
  void initState() {
    super.initState();
    _otpController = TextEditingController();
    _controller = Get.put(
      ForgotPasswordController(repository: ForgotPasswordRepository()),
      tag: 'forgot_otp',
    );
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_workersSetUp) {
      _workersSetUp = true;
      final args =
          ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;

      _workers.add(
        ever(_controller.checkGeneratedOTPStatus, (status) {
          if (!mounted) return;
          if (status.isSuccess) {
            final res = jsonDecode(_controller.checkGeneratedOTPResponse.value);
            if (res['status'] == 'Success') {
              WidgetsBinding.instance.addPostFrameCallback((_) {
                ScaffoldMessenger.of(context)
                  ..clearSnackBars()
                  ..showSnackBar(
                    const SnackBar(
                      content: Text("OTP verified successfully"),
                      backgroundColor: Colors.green,
                      duration: Duration(seconds: 2),
                    ),
                  );
                Navigator.pushNamed(
                  context,
                  AppRoutes.resetPassword,
                  arguments: args,
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
          if (status.isFailure) {
            ScaffoldMessenger.of(context)
              ..clearSnackBars()
              ..showSnackBar(
                const SnackBar(
                  content: Text("Unable to verify OTP"),
                  backgroundColor: Colors.red,
                  duration: Duration(seconds: 2),
                ),
              );
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
    Get.delete<ForgotPasswordController>(tag: 'forgot_otp');
    _otpController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> args =
        ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    final String? mobileNo = args['otp_details']['mobileNo'];
    final String email = args['email'] as String? ?? '';

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
                          SizedBox(height: getProportionateScreenHeight(24)),
                          Text(
                            "Enter 4 digits Code",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontSize: responsiveFont(24),
                              color: Colors.white,
                            ),
                          ),
                          SizedBox(height: getProportionateScreenHeight(30)),
                          Text(
                            mobileNo != null
                                ? 'Enter OTP that you have received on\nyour mobile number ($mobileNo)'
                                : 'Enter OTP that you have received on\nyour email ($email)',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontSize: responsiveFont(16),
                              color: Colors.white,
                            ),
                          ),
                          SizedBox(height: getProportionateScreenHeight(110)),
                          Form(
                            key: _formKey,
                            child: SizedBox(
                              width: SizeConfig.screenWidth * 0.7,
                              child: PinCodeTextField(
                                appContext: context,
                                keyboardType: TextInputType.number,
                                validator: (value) {
                                  if (value != null && value.length < 4) {
                                    return "Please enter 4 digit OTP";
                                  }
                                  return null;
                                },
                                textStyle: TextStyle(
                                  fontSize: responsiveFont(20),
                                  fontWeight: FontWeight.normal,
                                  color: Colors.white,
                                ),
                                length: 4,
                                controller: _otpController,
                                onChanged: (value) {
                                  setState(() {
                                    otp = value;
                                  });
                                },
                                pinTheme: PinTheme(
                                  shape: PinCodeFieldShape.box,
                                  borderRadius: BorderRadius.circular(5),
                                  fieldHeight: 50,
                                  fieldWidth: 40,
                                  fieldOuterPadding: EdgeInsets.zero,
                                  inactiveColor: Colors.white,
                                  inactiveBorderWidth: 0.5,
                                  borderWidth: 0.5,
                                  activeBorderWidth: 0.5,
                                  selectedBorderWidth: 1,
                                  selectedColor: Colors.white,
                                  activeColor: Colors.white,
                                  activeFillColor: Colors.white,
                                ),
                              ),
                            ),
                          ),
                          SizedBox(height: getProportionateScreenHeight(20)),
                          Obx(() {
                            final isLoading =
                                _controller
                                    .checkGeneratedOTPStatus
                                    .value
                                    .isInProgress ||
                                _controller
                                    .checkAndGenerateOTPStatus
                                    .value
                                    .isInProgress;
                            return isLoading
                                ? const CircularProgressIndicator(
                                  color: kWhiteColor,
                                )
                                : TextButton(
                                  onPressed: () {
                                    _controller.checkAndGenerateOTP({
                                      'mobile': mobileNo ?? '',
                                      'email': email,
                                    });
                                  },
                                  child: Text(
                                    "Didn't get OTP ? Resend",
                                    style: TextStyle(
                                      color: Colors.white,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                );
                          }),
                          SizedBox(height: getProportionateScreenHeight(64)),
                          Obx(() {
                            final isLoading =
                                _controller
                                    .checkGeneratedOTPStatus
                                    .value
                                    .isInProgress ||
                                _controller
                                    .checkAndGenerateOTPStatus
                                    .value
                                    .isInProgress;
                            return isLoading
                                ? const CircularProgressIndicator(
                                  color: kWhiteColor,
                                )
                                : AppButton(
                                  title: "Submit",
                                  onTap: () {
                                    if (_formKey.currentState?.validate() ==
                                        false) {
                                      return;
                                    }
                                    _controller.checkGeneratedOTP({
                                      'emailId': email,
                                      'mobile': mobileNo,
                                      'Otp': _otpController.text,
                                    });
                                  },
                                  textStyle: const TextStyle(
                                    color: Colors.black,
                                    fontWeight: FontWeight.w400,
                                  ),
                                  mHeight: getProportionateScreenHeight(49),
                                  mWidth: SizeConfig.screenWidth * 0.5,
                                  buttonColor: Colors.white,
                                );
                          }),
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
