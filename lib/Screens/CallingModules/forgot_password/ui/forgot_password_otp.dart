// ignore_for_file: library_private_types_in_public_api

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:formz/formz.dart';
import 'package:pin_code_fields/pin_code_fields.dart';

import 'dart:convert';

import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppButton.dart';
import '../../routes/app_routes.dart';
import '../bloc/forgot_password_bloc.dart';

class ForgotPasswordOTPScreen extends StatefulWidget {
  const ForgotPasswordOTPScreen({super.key});

  @override
  _ForgotPasswordOTPScreenState createState() =>
      _ForgotPasswordOTPScreenState();
}

class _ForgotPasswordOTPScreenState extends State<ForgotPasswordOTPScreen> {
  late TextEditingController _otpController;
  String otp = '';
  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    _otpController = TextEditingController();
  }

  @override
  void dispose() {
    _otpController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Map<String, dynamic> args =
        ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    print(args);
    // String otpType = args['otp_type'];
    String? mobileNo = args['otp_details']['mobileNo'];
    String email = args['email'];

    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: BlocListener<ForgotPasswordBloc, ForgotPasswordState>(
        listener: (context, state) {
          if (state.checkGeneratedOTPStatus.isSuccess) {
            var res = jsonDecode(state.checkGeneratedOTPResponse);
            if (res['status'] == 'Success') {
              WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
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

          if (state.checkGeneratedOTPStatus.isFailure) {
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
        },
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
                            BlocBuilder<
                              ForgotPasswordBloc,
                              ForgotPasswordState
                            >(
                              builder: (context, state) {
                                return state
                                            .checkGeneratedOTPStatus
                                            .isInProgress ||
                                        state
                                            .checkAndGenerateOTPStatus
                                            .isInProgress
                                    ? const CircularProgressIndicator(
                                      color: kWhiteColor,
                                    )
                                    : TextButton(
                                      onPressed: () {
                                        context.read<ForgotPasswordBloc>().add(
                                          CheckAndGenerateOTP(
                                            payload: {
                                              'mobile': mobileNo ?? '',
                                              'email': email,
                                            },
                                          ),
                                        );
                                      },
                                      child: Text(
                                        "Didn’t get OTP ? Resend",
                                        style: TextStyle(
                                          color: Colors.white,
                                          fontSize: responsiveFont(14),
                                        ),
                                      ),
                                    );
                              },
                            ),
                            SizedBox(height: getProportionateScreenHeight(64)),
                            BlocBuilder<
                              ForgotPasswordBloc,
                              ForgotPasswordState
                            >(
                              builder: (context, state) {
                                return state
                                            .checkGeneratedOTPStatus
                                            .isInProgress ||
                                        state
                                            .checkAndGenerateOTPStatus
                                            .isInProgress
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
                                        var payload = {
                                          'emailId': email,
                                          'mobile': mobileNo,
                                          'Otp': _otpController.text,
                                        };
                                        context.read<ForgotPasswordBloc>().add(
                                          CheckGeneratedOTP(payload: payload),
                                        );
                                      },
                                      textStyle: const TextStyle(
                                        color: Colors.black,
                                        fontWeight: FontWeight.w400,
                                      ),
                                      mHeight: getProportionateScreenHeight(49),
                                      mWidth: SizeConfig.screenWidth * 0.5,
                                      buttonColor: Colors.white,
                                    );
                              },
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
      ),
    );
  }
}
