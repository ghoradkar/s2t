// ignore_for_file: must_be_immutable

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';

class OtpVerifySheet extends StatefulWidget {
  OtpVerifySheet({
    super.key,
    required this.expectedOtp,
    required this.onVerify,
    required this.onResend,
  });

  String expectedOtp;
  final Future<String?> Function() onResend;
  final void Function(String otp) onVerify;

  @override
  State<OtpVerifySheet> createState() => _OtpVerifySheetState();
}

class _OtpVerifySheetState extends State<OtpVerifySheet> {
  Timer? timer;
  int totalTime = 120;
  String timerText = "02:00";
  bool showResendButton = false;
  String? expectedOtp;

  final TextEditingController otpTextField = TextEditingController();

  @override
  void initState() {
    super.initState();
    expectedOtp = widget.expectedOtp;
    startTimer();
  }

  @override
  Widget build(BuildContext context) {
    return KeyboardDismissOnTap(
      dismissOnCapturedTaps: false,
      child: Center(
        child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
          child: Container(
            decoration: BoxDecoration(
              color: Colors.white,
              border: Border.all(color: Colors.grey, width: 1),
              borderRadius: BorderRadius.circular(8),
            ),
            height: 280,
            padding: const EdgeInsets.all(8),
            child: Center(
              child: Column(
                children: [
                  Text(
                    "Enter OTP",
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w700,
                      fontSize: responsiveFont(16),
                    ),
                  ),
                  const SizedBox(height: 20),
                  // AppIconTextfield(
                  //   icon: iconMobile,
                  //   titleHeaderString: "OTP",
                  //   controller: otpTextField,
                  //   textInputType: TextInputType.number,
                  //   maxLength: 5,
                  // ),
                  AppTextField(
                    maxLength: 5,
                    readOnly: false,
                    controller: otpTextField,
                    hint: 'OTP *',
                    label: CommonText(
                      text: 'OTP *',
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kBlackColor,
                      textAlign: TextAlign.start,
                    ),
                    hintStyle: TextStyle(
                      fontSize: 12.sp,
                      fontWeight: FontWeight.w400,
                      fontFamily: FontConstants.interFonts,
                    ),
                    fieldRadius: 10,
                    prefixIcon: SizedBox(
                      height: 20.h,
                      width: 20.w,
                      child: Center(
                        child: Image.asset(
                          iconMobile,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    textInputType: TextInputType.number,
                  ),
                  const SizedBox(height: 8),
                  showResendButton == false
                      ? SizedBox(
                        height: 60,
                        child: Center(
                          child: Text(
                            timerText,
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                        ),
                      )
                      : const SizedBox(height: 20),
                  const SizedBox(height: 8),
                  showResendButton == true
                      ? SizedBox(
                        height: 60,
                        child: Center(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Text(
                                "Didn't get OTP ?",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                              GestureDetector(
                                onTap: handleResend,
                                child: Text(
                                  "  Resend",
                                  style: TextStyle(
                                    color: kPrimaryColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w700,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      )
                      : const SizedBox(height: 20),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      SizedBox(
                        width: 150,
                        child: AppActiveButton(
                          buttontitle: "Cancel",
                          onTap: () {
                            Navigator.pop(context);
                          },
                          isCancel: true,
                        ),
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: AppActiveButton(
                          buttontitle: "Verify OTP",
                          onTap: handleVerify,
                        ),
                      ),
                      const SizedBox(height: 8),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  void startTimer() {
    setState(() {
      totalTime = 120;
      showResendButton = false;
      timerText = formatTime(totalTime);
    });

    timer?.cancel();
    timer = Timer.periodic(const Duration(seconds: 1), (t) {
      if (totalTime > 0) {
        setState(() {
          totalTime--;
          timerText = formatTime(totalTime);
        });
      } else {
        t.cancel();
        setState(() {
          showResendButton = true;
        });
      }
    });
  }

  String formatTime(int seconds) {
    int minutes = seconds ~/ 60;
    int remainingSeconds = seconds % 60;
    return "${minutes.toString().padLeft(2, '0')}:${remainingSeconds.toString().padLeft(2, '0')}";
  }

  @override
  void dispose() {
    timer?.cancel();
    otpTextField.dispose();
    super.dispose();
  }

  Future<void> handleResend() async {
    otpTextField.text = "";
    final updatedOtp = await widget.onResend();
    if (updatedOtp != null) {
      setState(() {
        expectedOtp = updatedOtp;
      });
      startTimer();
    }
  }

  void handleVerify() {
    if (otpTextField.text.trim().isEmpty) {
      ToastManager.toast("Please enter otp");
      return;
    }
    final expected = expectedOtp ?? widget.expectedOtp;
    if (otpTextField.text.trim() != expected.trim()) {
      ToastManager.toast("Entered OTP is not matched");
      return;
    }
    widget.onVerify(otpTextField.text.trim());
  }
}
