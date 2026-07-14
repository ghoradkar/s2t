// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppIconTextfield.dart';
import 'package:s2toperational/verify_otp/controller/verify_otp_controller.dart';

class VerifyOtpScreen extends StatelessWidget {
  const VerifyOtpScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.find<VerifyOtpController>();

    return Center(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
        child: Container(
          width: SizeConfig.screenWidth,
          height: 250,
          padding: const EdgeInsets.fromLTRB(20, 6, 20, 6),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.black, width: 0.5),
          ),
          child: Column(
            children: [
              Text(
                'Verify OTP',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(18),
                  color: kBlackColor,
                  fontWeight: FontWeight.w700,
                ),
              ),
              const SizedBox(height: 10),
              AppIconTextfield(
                icon: callIcon,
                titleHeaderString: 'OTP',
                controller: controller.otpTextField,
                textInputType: TextInputType.number,
                maxLength: 5,
              ),
              const SizedBox(height: 10),
              Obx(
                () => Text(
                  controller.isButtonEnabled.value
                      ? 'You can resend now'
                      : 'Resend in ${controller.timerText}',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: responsiveFont(18),
                    color: kBlackColor,
                    fontWeight: FontWeight.w400,
                  ),
                ),
              ),
              const SizedBox(height: 20),
              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: 'Verify',
                      onTap: controller.verifyOTP,
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: AppActiveButton(
                      isCancel: true,
                      buttontitle: 'Resend OTP',
                      onTap: controller.resendOTP,
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
