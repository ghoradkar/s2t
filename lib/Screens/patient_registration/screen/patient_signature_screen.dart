// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/patient_finger_signature_controller.dart';
import 'package:signature/signature.dart';

class PatientSignatureScreen extends StatelessWidget {
  const PatientSignatureScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final c = Get.find<PatientFingerSignatureController>();

    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Signature',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16.w),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ── Signature Not Applicable checkbox ──────────────────────
            _SectionCard(
              child: Obx(
                () => CheckboxListTile(
                  value: !c.isSignatureApplicable.value,
                  onChanged: (v) => c.onSignatureApplicableToggled(v ?? false),
                  title: CommonText(
                    text: 'Signature Not Applicable',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w500,
                    textColor: kTextColor,
                    textAlign: TextAlign.start,
                  ),
                  activeColor: kPrimaryColor,
                  controlAffinity: ListTileControlAffinity.leading,
                  contentPadding: EdgeInsets.zero,
                ),
              ),
            ),
            SizedBox(height: 16.h),

            // ── Signature Pad ──────────────────────────────────────────
            Obx(() {
              if (!c.isSignatureApplicable.value) {
                return const SizedBox.shrink();
              }
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _sectionLabel('Signature'),
                  SizedBox(height: 8.h),
                  Container(
                    decoration: BoxDecoration(
                      color: kWhiteColor,
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(
                        color: kPrimaryColor.withValues(alpha: 0.3),
                      ),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.05),
                          blurRadius: 8,
                        ),
                      ],
                    ),
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: Signature(
                        controller: c.signatureController,
                        height: 200.h,
                        backgroundColor: kWhiteColor,
                      ),
                    ),
                  ),
                  SizedBox(height: 8.h),
                  Align(
                    alignment: Alignment.centerRight,
                    child: TextButton.icon(
                      onPressed: c.clearSignature,
                      icon: const Icon(Icons.clear, color: kPrimaryColor),
                      label: Text(
                        'Clear',
                        style: TextStyle(
                          color: kPrimaryColor,
                          fontFamily: FontConstants.interFonts,
                          fontSize: 13.sp,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(height: 8.h),
                ],
              );
            }),

            SizedBox(height: 8.h),

            // ── Submit Button ──────────────────────────────────────────
            Obx(
              () => c.isSubmitting.value
                  ? const Center(child: CircularProgressIndicator())
                  : AppActiveButton(
                      buttontitle: 'Submit',
                      onTap: () => c.submitSignatureAndThumb(context),
                    ),
            ),
            SizedBox(height: 16.h),
          ],
        ),
      ),
    );
  }

  Widget _sectionLabel(String text) => Text(
    text,
    style: TextStyle(
      color: kLabelTextColor,
      fontSize: 13.sp,
      fontFamily: FontConstants.interFonts,
      fontWeight: FontWeight.w600,
    ),
  );
}

class _SectionCard extends StatelessWidget {
  final Widget child;

  const _SectionCard({required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(12.w),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 10,
          ),
        ],
      ),
      child: child,
    );
  }
}