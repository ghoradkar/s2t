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
import 'package:s2toperational/Screens/patient_registration/screen/patient_signature_screen.dart';

class PatientFingerAndSignatureScreen extends StatelessWidget {
  final String campId;
  final String siteId;
  final String regNo;
  /// Called after successful upload when the user taps OK on the success dialog.
  /// Use this to clear the parent registration form.
  final VoidCallback? onSuccess;

  const PatientFingerAndSignatureScreen({
    super.key,
    required this.campId,
    required this.siteId,
    required this.regNo,
    this.onSuccess,
  });

  @override
  Widget build(BuildContext context) {
    final c = Get.put(
      PatientFingerSignatureController(
        campId: campId,
        siteId: siteId,
        regNo: regNo,
        onSuccess: onSuccess,
      ),
    );

    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Fingerprint & Signature',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (c.isLoading.value) {
          return const Center(child: CircularProgressIndicator());
        }
        return _Body(c: c);
      }),
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

          // ── Device status banner ───────────────────────────────────────
          Obx(() => _DeviceStatusBanner(isConnected: c.isDeviceConnected.value)),
          SizedBox(height: 12.h),

          // ── Finger Issue Checkbox ──────────────────────────────────────
          _SectionCard(
            child: Obx(
              () => CheckboxListTile(
                value: c.isFingerPrintIssue.value,
                onChanged: (v) => c.onFingerIssueToggled(v ?? false),
                title: CommonText(
                  text: 'Finger Issue (use camera instead of scanner)',
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

          // ── Thumb / Fingerprint Capture Area ──────────────────────────
          _sectionLabel('Thumb / Fingerprint Image'),
          SizedBox(height: 8.h),
          Obx(
            () => GestureDetector(
              onTap: c.isCapturing.value
                  ? null
                  : () => c.captureThumbImage(context),
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
            onTap: () => c.onNextTapped(
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
    // Scanning in progress
    if (c.isCapturing.value) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const CircularProgressIndicator(),
          SizedBox(height: 12.h),
          CommonText(
            text: 'Place your finger on the sensor…',
            fontSize: 13.sp,
            fontWeight: FontWeight.w400,
            textColor: kLabelTextColor,
            textAlign: TextAlign.center,
          ),
        ],
      );
    }

    // Image captured
    if (c.thumbImageFile.value != null) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: Image.file(c.thumbImageFile.value!, fit: BoxFit.cover),
      );
    }

    // Placeholder — show different hint based on mode
    final useScanner = !c.isFingerPrintIssue.value && c.isDeviceConnected.value;
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(
          useScanner ? Icons.fingerprint : Icons.camera_alt_outlined,
          size: 56,
          color: kPrimaryColor.withValues(alpha: 0.5),
        ),
        SizedBox(height: 8.h),
        CommonText(
          text: useScanner
              ? 'Tap to start scanner capture'
              : 'Tap to capture via camera',
          fontSize: 13.sp,
          fontWeight: FontWeight.w400,
          textColor: kLabelTextColor,
          textAlign: TextAlign.center,
        ),
      ],
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

// ── Device status banner ─────────────────────────────────────────────────────

class _DeviceStatusBanner extends StatelessWidget {
  final bool isConnected;

  const _DeviceStatusBanner({required this.isConnected});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
      decoration: BoxDecoration(
        color: isConnected
            ? Colors.green.withValues(alpha: 0.1)
            : Colors.orange.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(
          color: isConnected
              ? Colors.green.withValues(alpha: 0.4)
              : Colors.orange.withValues(alpha: 0.4),
        ),
      ),
      child: Row(
        children: [
          Icon(
            isConnected
                ? Icons.usb_rounded
                : Icons.usb_off_rounded,
            size: 18,
            color: isConnected ? Colors.green : Colors.orange,
          ),
          SizedBox(width: 8.w),
          CommonText(
            text: isConnected
                ? 'Mantra scanner connected — hardware capture ready'
                : 'No scanner detected — camera will be used',
            fontSize: 12.sp,
            fontWeight: FontWeight.w500,
            textColor: isConnected ? Colors.green.shade700 : Colors.orange.shade800,
            textAlign: TextAlign.start,
          ),
        ],
      ),
    );
  }
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