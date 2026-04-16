// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/patient_finger_signature_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/patient_signature_screen.dart';

class PatientFingerAndSignatureScreen extends StatelessWidget {
  final String campId;
  final String siteId;
  final String regNo;

  /// Called after successful upload when the user taps OK on the success dialog.
  /// Use this to clear the parent registration form.
  final VoidCallback? onSuccess;

  /// Optional pre-filled patient data (used by D2D registration to avoid
  /// a redundant API round-trip after the patient was just registered).
  final String? prefillRegdId;
  final String? prefillRegdNo;
  final String? prefillName;
  final String? prefillGender;
  final String? prefillAge;
  final String? prefillDob;

  const PatientFingerAndSignatureScreen({
    super.key,
    required this.campId,
    required this.siteId,
    required this.regNo,
    this.onSuccess,
    this.prefillRegdId,
    this.prefillRegdNo,
    this.prefillName,
    this.prefillGender,
    this.prefillAge,
    this.prefillDob,
  });

  @override
  Widget build(BuildContext context) {
    // Always start with a fresh controller so a new patient registration
    // does not inherit the previous patient's captured photo or API data.
    if (Get.isRegistered<PatientFingerSignatureController>()) {
      Get.delete<PatientFingerSignatureController>(force: true);
    }
    final c = Get.put(
      PatientFingerSignatureController(
        campId: campId,
        siteId: siteId,
        regNo: regNo,
        onSuccess: onSuccess,
        prefillRegdId: prefillRegdId,
        prefillRegdNo: prefillRegdNo,
        prefillName: prefillName,
        prefillGender: prefillGender,
        prefillAge: prefillAge,
        prefillDob: prefillDob,
      ),
    );

    return PopScope(
      canPop: false,
      child: Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(scTitle: 'Fingerprint & Signature'),
        body: Obx(() {
          if (c.isLoading.value) {
            return const Center(child: CircularProgressIndicator());
          }
          return _Body(c: c);
        }),
      ),
    );
  }
}

class _Body extends StatelessWidget {
  final PatientFingerSignatureController c;

  const _Body({required this.c});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: EdgeInsets.all(16.w),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // ── Patient Info Card ──────────────────────────────────────────
          _PatientInfoCard(c: c),
          SizedBox(height: 16.h),

          // ── Thumb / Fingerprint Capture Area ──────────────────────────
          _sectionLabel('Thumb / Fingerprint Image'),
          SizedBox(height: 8.h),
          Obx(
            () => GestureDetector(
              onTap: () => c.captureThumbImage(context),
              child: Container(
                width: double.infinity,
                height: 180.h,
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
                child: _captureAreaContent(c),
              ),
            ),
          ),
          SizedBox(height: 24.h),

          // ── Next Button ───────────────────────────────────────────────
          AppActiveButton(
            buttontitle: 'Next',
            onTap:
                () => c.onNextTapped(
                  context,
                  () => const PatientSignatureScreen(),
                ),
          ),
          SizedBox(height: 16.h),
        ],
      ),
    );
  }

  Widget _captureAreaContent(PatientFingerSignatureController c) {
    // Image captured
    if (c.thumbImageFile.value != null) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: Image.file(c.thumbImageFile.value!, fit: BoxFit.cover),
      );
    }

    // Placeholder
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(
          Icons.camera_alt_outlined,
          size: 56,
          color: kPrimaryColor.withValues(alpha: 0.5),
        ),
        SizedBox(height: 8.h),
        CommonText(
          text: 'Tap to capture via camera',
          fontSize: 13.sp,
          fontWeight: FontWeight.w400,
          textColor: kLabelTextColor,
          textAlign: TextAlign.center,
        ),
      ],
    );
  }

  Widget _sectionLabel(String text) => CommonText(
    text: text,
    fontSize: 14.sp,
    fontWeight: FontWeight.w600,
    textColor: kLabelTextColor,
    textAlign: TextAlign.start,
  );

  // Text(
  //
  // text,
  // style: TextStyle(
  // color: kLabelTextColor,
  // fontSize: 13.sp,
  // fontFamily: FontConstants.interFonts,
  // fontWeight: FontWeight.w600,
  // ),
  // );
}

// ── Patient info card ─────────────────────────────────────────────────────────

class _PatientInfoCard extends StatelessWidget {
  final PatientFingerSignatureController c;

  const _PatientInfoCard({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final info = c.patientInfo.value;
      if (info == null) return const SizedBox.shrink();
      return _SectionCard(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _infoRow('Reg No', info.regdNo ?? '--'),
            _infoRow('Name', info.englishName ?? '--'),
            _infoRow(
              'Gender',
              info.gender == 'M'
                  ? 'Male'
                  : info.gender == 'F'
                  ? 'Female'
                  : info.gender ?? '--',
            ),
            _infoRow('Age', info.age ?? '--'),
            _infoRow('DOB', info.dobFormatted ?? '--'),
            // _infoRow('Mobile', info.mobileNo ?? '--'),
            // _infoRow('Aadhaar', info.uid?.replaceAll('-', '') ?? '--'),
            // _infoRow('Address', info.permanentAddress ?? '--'),
          ],
        ),
      );
    });
  }

  Widget _infoRow(String label, String value) {
    return Padding(
      padding: EdgeInsets.only(bottom: 6.h),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 80.w,
            child: CommonText(
              text: '$label :',
              fontSize: 12.sp,
              fontWeight: FontWeight.w600,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
          ),
          SizedBox(width: 6.w),
          Expanded(
            child: CommonText(
              text: value,
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kTextColor,
              textAlign: TextAlign.start,
            ),
          ),
        ],
      ),
    );
  }
}

// ── Shared card container ─────────────────────────────────────────────────────

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
