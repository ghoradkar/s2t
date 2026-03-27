// ignore_for_file: must_be_immutable

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../models/verification_remark_model.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';

class VerificationRemarkSheet extends StatefulWidget {
  VerificationRemarkSheet({
    super.key,
    required this.remarks,
    required this.expectedOtp,
    required this.onResend,
    required this.onVerify,
  });

  final List<VerificationRemarkOutput> remarks;
  String expectedOtp;
  final Future<String?> Function() onResend;
  final void Function(
    String otp,
    VerificationRemarkOutput remark,
    String otherRemark,
  )
  onVerify;

  @override
  State<VerificationRemarkSheet> createState() =>
      _VerificationRemarkSheetState();
}

class _VerificationRemarkSheetState extends State<VerificationRemarkSheet> {
  Timer? timer;
  int totalTime = 120;
  String timerText = "02:00";
  bool showResendButton = false;

  final TextEditingController otpTextField = TextEditingController();
  final TextEditingController otherRemarkController = TextEditingController();
  VerificationRemarkOutput? selectedRemark;

  @override
  void initState() {
    super.initState();
    startTimer();
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

  bool get showOtherRemark {
    final name = (selectedRemark?.verificationRemark ?? "").toLowerCase();
    return name == "other" || name == "others";
  }

  @override
  void dispose() {
    timer?.cancel();
    otpTextField.dispose();
    otherRemarkController.dispose();
    super.dispose();
  }

  Future<void> handleResend() async {
    otpTextField.text = "";
    final updatedOtp = await widget.onResend();
    if (updatedOtp != null) {
      widget.expectedOtp = updatedOtp;
      startTimer();
    }
  }

  void handleVerify() {
    if (selectedRemark == null) {
      ToastManager.toast("Please select remark");
      return;
    }
    if (showOtherRemark && otherRemarkController.text.trim().isEmpty) {
      ToastManager.toast("Please enter remark");
      return;
    }
    if (otpTextField.text.trim().isEmpty) {
      ToastManager.toast("Please enter otp");
      return;
    }
    if (otpTextField.text.trim() != widget.expectedOtp) {
      ToastManager.toast("Entered OTP is not matched");
      return;
    }
    widget.onVerify(
      otpTextField.text.trim(),
      selectedRemark!,
      otherRemarkController.text.trim(),
    );
  }

  Future<void> selectRemark() async {
    if (widget.remarks.isEmpty) {
      ToastManager.toast("No remarks available");
      return;
    }
    final result = await showModalBottomSheet<VerificationRemarkOutput>(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.width * 1.1,
          padding: const EdgeInsets.all(12),
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: Column(
            children: [
              Text(
                "Select Remark",
                style: TextStyle(
                  color: kBlackColor,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  fontSize: responsiveFont(16),
                ),
              ),
              const SizedBox(height: 12),
              Expanded(
                child: ListView.builder(
                  itemCount: widget.remarks.length,
                  itemBuilder: (context, index) {
                    final remark = widget.remarks[index];
                    return ListTile(
                      title: Text(
                        remark.verificationRemark ?? "",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      onTap: () {
                        Navigator.pop(sheetContext, remark);
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );

    if (result != null) {
      setState(() {
        selectedRemark = result;
        if (!showOtherRemark) {
          otherRemarkController.text = "";
        }
      });
    }
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
            height: 360,
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
                  const SizedBox(height: 12),
                  // AppDropdownTextfield(
                  //   icon: icUserIcon,
                  //   titleHeaderString: "Remark",
                  //   valueString: selectedRemark?.verificationRemark ?? "",
                  //   onTap: selectRemark,
                  // ),
                  AppTextField(
                    controller: TextEditingController(
                      text: selectedRemark?.verificationRemark ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      selectRemark();
                    },
                    hint: 'Remark *',
                    label: CommonText(
                      text: 'Remark *',
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
                          icUserIcon,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down_rounded),
                  ),
                  const SizedBox(height: 10),
                  Visibility(
                    visible: showOtherRemark,
                    child: AppTextField(
                      controller: otherRemarkController,
                      readOnly: false,
                      maxLength: 200,
                      hint: 'Other Remark *',
                      label: CommonText(
                        text: 'Other Remark *',
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
                      textInputType: TextInputType.text,
                      prefixIcon: SizedBox(
                        height: 20.h,
                        width: 20.w,
                        child: Center(
                          child: Image.asset(
                            iconFile,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                      // suffixIcon: Icon(Icons.keyboard_arrow_down_rounded),
                    ),
                  ),
                  // showOtherRemark
                  //     ? AppIconTextfield(
                  //       icon: iconFile,
                  //       titleHeaderString: "Other Remark",
                  //       controller: otherRemarkController,
                  //       textInputType: TextInputType.text,
                  //       maxLength: 200,
                  //     )
                  //     : const SizedBox.shrink(),
                  const SizedBox(height: 10),
                  AppTextField(
                    controller: otpTextField,
                    readOnly: false,
                    maxLength: 5,
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
                    textInputType: TextInputType.number,

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
                    // suffixIcon: Icon(Icons.keyboard_arrow_down_rounded),
                  ),

                  // AppIconTextfield(
                  //   icon: iconMobile,
                  //   titleHeaderString: "OTP",
                  //   controller: otpTextField,
                  //   textInputType: TextInputType.number,
                  //   maxLength: 5,
                  // ),
                  SizedBox(height: 4),
                  showResendButton == false
                      ? SizedBox(
                        height: 40,
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
                      : const SizedBox(height: 10),
                  showResendButton == true
                      ? SizedBox(
                        height: 40,
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
                                    color: kBlackColor,
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
}
