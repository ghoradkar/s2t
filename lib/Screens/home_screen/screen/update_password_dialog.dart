import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Screens/forgot_password/controller/password_reset_controller.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';

class UpdatePasswordDialog extends StatefulWidget {
  const UpdatePasswordDialog({super.key});

  @override
  State<UpdatePasswordDialog> createState() => _UpdatePasswordDialogState();
}

class _UpdatePasswordDialogState extends State<UpdatePasswordDialog> {
  final _passwordController = TextEditingController();
  final _otpController = TextEditingController();

  @override
  void dispose() {
    _passwordController.dispose();
    _otpController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GetBuilder<PasswordResetController>(
      builder: (ctrl) {
        if (ctrl.passwordUpdated) {
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (Navigator.canPop(context)) Navigator.pop(context);
            DataProvider().clearSession(context);
          });
        }
        return AlertDialog(
          title: CommonText(
            text: 'Reset Password',
            fontSize: 16,
            fontWeight: FontWeight.w600,
            textColor: Colors.black,
            textAlign: TextAlign.left,
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              AppTextField(
                controller: _passwordController,
                obscureText: true,
                label: CommonText(
                  text: 'Enter New Password',
                  fontSize: 14,
                  fontWeight: FontWeight.w400,
                  textColor: Colors.black,
                  textAlign: TextAlign.left,
                ),
              ),
              const SizedBox(height: 12),
              AppTextField(
                controller: _otpController,
                textInputType: TextInputType.number,
                label: CommonText(
                  text: 'Enter OTP',
                  fontSize: 14,
                  fontWeight: FontWeight.w400,
                  textColor: Colors.black,
                  textAlign: TextAlign.left,
                ),
              ),
              const SizedBox(height: 8),
              if (ctrl.timerSeconds > 0)
                CommonText(
                  text: 'Resend OTP in: ${ctrl.timerSeconds} sec',
                  fontSize: 12,
                  fontWeight: FontWeight.w400,
                  textColor: Colors.grey,
                  textAlign: TextAlign.left,
                ),
            ],
          ),
          actions: [
            if (ctrl.canResend)
              TextButton(
                onPressed: () => ctrl.sendOtp(),
                child: CommonText(
                  text: 'RESEND OTP',
                  fontSize: 14,
                  fontWeight: FontWeight.w600,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.center,
                ),
              ),
            ctrl.isUpdatingPassword
                ? const Padding(
                    padding: EdgeInsets.all(8),
                    child: CircularProgressIndicator(),
                  )
                : TextButton(
                    onPressed: () => ctrl.updatePassword(
                      newPassword: _passwordController.text.trim(),
                      otp: _otpController.text.trim(),
                    ),
                    child: CommonText(
                      text: 'SUBMIT',
                      fontSize: 14,
                      fontWeight: FontWeight.w600,
                      textColor: kPrimaryColor,
                      textAlign: TextAlign.center,
                    ),
                  ),
          ],
        );
      },
    );
  }
}
