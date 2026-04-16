// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pin_code_fields/pin_code_fields.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/abha_creation_controller.dart';

class AbhaCreationScreen extends StatefulWidget {
  final String campId;
  final String siteId;
  final String distLgdCode;
  final String district;
  final String campType;
  final int empCode;
  final String initialMobile;

  const AbhaCreationScreen({
    super.key,
    required this.campId,
    required this.siteId,
    required this.distLgdCode,
    required this.district,
    required this.campType,
    required this.empCode,
    this.initialMobile = '',
  });

  @override
  State<AbhaCreationScreen> createState() => _AbhaCreationScreenState();
}

class _AbhaCreationScreenState extends State<AbhaCreationScreen> {
  late final AbhaCreationController ctrl;

  @override
  void initState() {
    super.initState();
    Get.delete<AbhaCreationController>(force: true);
    ctrl = Get.put(AbhaCreationController()
      ..campId = widget.campId
      ..siteId = widget.siteId
      ..distLgdCode = widget.distLgdCode
      ..district = widget.district
      ..campType = widget.campType
      ..empCode = widget.empCode
      ..initialMobile = widget.initialMobile);
  }

  @override
  void dispose() {
    Get.delete<AbhaCreationController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade100,
      appBar: mAppBar(
        scTitle: 'ABHA Creation',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (!ctrl.sessionReady.value && ctrl.sessionError.value.isNotEmpty) {
          return _buildSessionError();
        }
        if (ctrl.sessionLoading.value) return const SizedBox.shrink();
        return SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildLogosCard(),
              Padding(
                padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 12.h),
                child: _buildForm(),
              ),
            ],
          ),
        );
      }),
    );
  }

  // ── Session error ─────────────────────────────────────────────────

  Widget _buildSessionError() {
    return Center(
      child: Padding(
        padding: EdgeInsets.all(24.w),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.error_outline, color: Colors.red.shade400, size: 48.sp),
            SizedBox(height: 12.h),
            CommonText(
              text: ctrl.sessionError.value,
              fontSize: 13.sp,
              fontWeight: FontWeight.w400,
              textColor: Colors.red.shade700,
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 20.h),
            OutlinedButton(
              onPressed: ctrl.initSession,
              style: OutlinedButton.styleFrom(
                foregroundColor: kPrimaryColor,
                side: const BorderSide(color: kPrimaryColor, width: 1.5),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6)),
                padding:
                    EdgeInsets.symmetric(horizontal: 28.w, vertical: 12.h),
              ),
              child: CommonText(
                text: 'Retry',
                fontSize: 15.sp,
                fontWeight: FontWeight.w600,
                textColor: kPrimaryColor,
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ── Logos card ────────────────────────────────────────────────────

  Widget _buildLogosCard() {
    return Card(
      margin: EdgeInsets.all(10.w),
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
      child: Row(
        children: [
          Expanded(
            child: Image.asset(
              'assets/images/nha_logo.png',
              height: 80.h,
              fit: BoxFit.contain,
            ),
          ),
          Expanded(
            child: Image.asset(
              'assets/images/abha_logo.png',
              height: 80.h,
              fit: BoxFit.contain,
            ),
          ),
        ],
      ),
    );
  }

  // ── Main form ─────────────────────────────────────────────────────

  Widget _buildForm() {
    final showOtpPhase = ctrl.phase.value == AbhaPhase.aadhaarOtp ||
        ctrl.phase.value == AbhaPhase.mobileOtp;
    final showMobileField = ctrl.phase.value == AbhaPhase.aadhaarOtp ||
        ctrl.phase.value == AbhaPhase.mobileInput ||
        ctrl.phase.value == AbhaPhase.mobileOtp;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [

        // ── Instruction text ──────────────────────────────────────
        if (ctrl.phase.value == AbhaPhase.input) ...[
          CommonText(
            text:
                'Enter your Aadhaar number and\nkeep registered mobile number with you',
            fontSize: 13.sp,
            fontWeight: FontWeight.w400,
            textColor: Colors.black87,
            textAlign: TextAlign.center,
          ),
          SizedBox(height: 14.h),
        ],

        // ── Aadhaar field ─────────────────────────────────────────
        AbsorbPointer(
          absorbing: ctrl.phase.value != AbhaPhase.input,
          child: Opacity(
            opacity: ctrl.phase.value != AbhaPhase.input ? 0.55 : 1.0,
            child: AppTextField(
              controller: ctrl.aadhaarCtrl,
              label: const Text('Aadhaar Number *'),
              hint: 'Enter Aadhaar Number*',
              textInputType: TextInputType.number,
              maxLength: 12,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
          ),
        ),

        // ── OTP info banner ───────────────────────────────────────
        if (showOtpPhase) ...[
          SizedBox(height: 14.h),
          Obx(() => ctrl.otpInfoMsg.value.isNotEmpty
              ? Container(
                  width: double.infinity,
                  padding: EdgeInsets.all(10.w),
                  decoration: BoxDecoration(
                    border: Border.all(color: Colors.grey.shade400),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: CommonText(
                    text: ctrl.otpInfoMsg.value,
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    textColor: Colors.black87,
                    textAlign: TextAlign.start,
                  ),
                )
              : const SizedBox.shrink()),
        ],

        // ── OTP pin view ──────────────────────────────────────────
        if (showOtpPhase) ...[
          SizedBox(height: 10.h),
          Center(
            child: PinCodeTextField(
              appContext: context,
              length: 6,
              controller: ctrl.otpCtrl,
              keyboardType: TextInputType.number,
              onChanged: (_) {},
              pinTheme: PinTheme(
                shape: PinCodeFieldShape.box,
                borderRadius: BorderRadius.circular(4),
                fieldHeight: 48.h,
                fieldWidth: 48.w,
                activeFillColor: Colors.white,
                inactiveFillColor: Colors.white,
                selectedFillColor: Colors.white,
                activeColor: kPrimaryColor,
                inactiveColor: kPrimaryColor,
                selectedColor: kPrimaryColor,
              ),
              enableActiveFill: true,
              textStyle: TextStyle(
                fontSize: 18.sp,
                fontWeight: FontWeight.bold,
                fontFamily: FontConstants.interFonts,
              ),
            ),
          ),
        ],

        // ── Timer + Resend + Attempts ─────────────────────────────
        if (showOtpPhase) ...[
          SizedBox(height: 6.h),
          Obx(() => Center(
                child: CommonText(
                  text: ctrl.timerFinished.value
                      ? ''
                      : '${ctrl.timerSeconds.value} Sec',
                  fontSize: 17.sp,
                  fontWeight: FontWeight.bold,
                  textColor: Colors.black87,
                  textAlign: TextAlign.center,
                ),
              )),
          SizedBox(height: 6.h),
          Obx(() => Center(
                child: TextButton(
                  onPressed:
                      ctrl.timerFinished.value ? ctrl.onResend : null,
                  child: CommonText(
                    text: 'Resend OTP',
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w400,
                    textColor: ctrl.timerFinished.value
                        ? kPrimaryColor
                        : Colors.grey,
                    textAlign: TextAlign.center,
                  ),
                ),
              )),
          Obx(() {
            final attempts = ctrl.phase.value == AbhaPhase.aadhaarOtp
                ? ctrl.resendAadhaarCount - 1
                : ctrl.resendMobileCount - 1;
            return Center(
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  CommonText(
                    text: 'OTP Attempts: ',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    textColor: Colors.black54,
                    textAlign: TextAlign.start,
                  ),
                  CommonText(
                    text: '${attempts.clamp(0, 2)}/2',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    textColor: Colors.black54,
                    textAlign: TextAlign.start,
                  ),
                ],
              ),
            );
          }),
        ],

        // ── Terms & Conditions (input phase only) ─────────────────
        if (ctrl.phase.value == AbhaPhase.input) ...[
          SizedBox(height: 16.h),
          _buildTermsSection(),
        ],

        SizedBox(height: 16.h),

        // ── Get OTP button (input phase) ──────────────────────────
        if (ctrl.phase.value == AbhaPhase.input)
          Center(
            child: OutlinedButton(
              onPressed: ctrl.sessionReady.value ? ctrl.onGetOtp : null,
              style: OutlinedButton.styleFrom(
                foregroundColor: kPrimaryColor,
                side: BorderSide(
                  color: ctrl.sessionReady.value
                      ? kPrimaryColor
                      : Colors.grey,
                  width: 1.5,
                ),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6)),
                padding: EdgeInsets.symmetric(
                    horizontal: 28.w, vertical: 12.h),
              ),
              child: CommonText(
                text: 'Get OTP',
                fontSize: 16.sp,
                fontWeight: FontWeight.bold,
                textColor: ctrl.sessionReady.value
                    ? kPrimaryColor
                    : Colors.grey,
                textAlign: TextAlign.center,
              ),
            ),
          ),

        // ── Mobile field (OTP phases) ─────────────────────────────
        if (showMobileField) ...[
          SizedBox(height: 10.h),
          AppTextField(
            controller: ctrl.mobileCtrl,
            label: const Text('Communication Mobile Number *'),
            hint: 'Enter Communication Mobile Number*',
            textInputType: TextInputType.phone,
            maxLength: 10,
            readOnly: ctrl.phase.value == AbhaPhase.mobileOtp,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          ),
        ],

        // ── Mobile input phase: send OTP button ───────────────────
        if (ctrl.phase.value == AbhaPhase.mobileInput) ...[
          SizedBox(height: 12.h),
          CommonText(
            text:
                'Entered mobile is not linked with Aadhaar. Please verify your mobile to continue.',
            fontSize: 12.sp,
            fontWeight: FontWeight.w400,
            textColor: Colors.orange.shade800,
            textAlign: TextAlign.start,
          ),
          SizedBox(height: 10.h),
          AppButtonWithIcon(
            title: 'Send Mobile OTP',
            mHeight: 44,
            mWidth: double.infinity,
            onTap: ctrl.onSendMobileOtp,
          ),
        ],

        // ── Verify OTP button (OTP phases) ────────────────────────
        if (showOtpPhase) ...[
          SizedBox(height: 14.h),
          Center(
            child: OutlinedButton(
              onPressed: ctrl.phase.value == AbhaPhase.aadhaarOtp
                  ? ctrl.onVerifyAadhaarOtp
                  : ctrl.onVerifyMobileOtp,
              style: OutlinedButton.styleFrom(
                foregroundColor: kPrimaryColor,
                side: const BorderSide(color: kPrimaryColor, width: 1.5),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6)),
                padding: EdgeInsets.symmetric(
                    horizontal: 28.w, vertical: 12.h),
              ),
              child: CommonText(
                text: 'Verify OTP',
                fontSize: 16.sp,
                fontWeight: FontWeight.bold,
                textColor: kPrimaryColor,
                textAlign: TextAlign.center,
              ),
            ),
          ),
        ],

        SizedBox(height: 20.h),
      ],
    );
  }

  // ── Terms & Conditions section ────────────────────────────────────

  Widget _buildTermsSection() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey.shade400),
        borderRadius: BorderRadius.circular(6),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 8.h),
            child: CommonText(
              text: 'Terms and Conditions',
              fontSize: 14.sp,
              fontWeight: FontWeight.bold,
              textColor: Colors.black87,
              textAlign: TextAlign.start,
            ),
          ),
          Divider(height: 1, color: Colors.grey.shade300),
          Padding(
            padding:
                EdgeInsets.symmetric(horizontal: 10.w, vertical: 6.h),
            child: CommonText(
              text: 'I hereby declare that',
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: Colors.black87,
              textAlign: TextAlign.start,
            ),
          ),
          Obx(() => _consentTile(
                value: ctrl.cbAadhaar.value,
                text: AbhaCreationController.consentAadhaar,
                onChanged: (v) => ctrl.cbAadhaar.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAbhaLink.value,
                text: AbhaCreationController.consentAbhaLink,
                onChanged: (v) => ctrl.cbAbhaLink.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbHealthRecord.value,
                text: AbhaCreationController.consentHealthRecords,
                onChanged: (v) => ctrl.cbHealthRecord.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon.value,
                text: AbhaCreationController.consentAnonymization,
                onChanged: (v) => ctrl.cbAnon.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon1.value,
                text: AbhaCreationController.consentAnonymization1,
                indent: true,
                onChanged: (v) => ctrl.cbAnon1.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon2.value,
                text: AbhaCreationController.consentAnonymization2,
                indent: true,
                onChanged: (v) => ctrl.cbAnon2.value = v ?? false,
              )),
          SizedBox(height: 4.h),
        ],
      ),
    );
  }

  Widget _consentTile({
    required bool value,
    required String text,
    required ValueChanged<bool?> onChanged,
    bool indent = false,
  }) {
    return Padding(
      padding:
          EdgeInsets.only(left: indent ? 20.w : 0, top: 4.h, bottom: 4.h),
      child: Container(
        decoration: BoxDecoration(
          border: Border(
              top: BorderSide(color: Colors.grey.shade300, width: 0.5)),
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Checkbox(
              value: value,
              onChanged: onChanged,
              activeColor: kPrimaryColor,
              materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
              visualDensity: VisualDensity.compact,
            ),
            Expanded(
              child: Padding(
                padding:
                    EdgeInsets.only(top: 10.h, right: 8.w, bottom: 6.h),
                child: CommonText(
                  text: text,
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w400,
                  textColor: Colors.black87,
                  textAlign: TextAlign.start,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}