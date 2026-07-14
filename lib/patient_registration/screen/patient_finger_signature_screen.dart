// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/patient_registration/controller/patient_finger_signature_controller.dart';
import 'package:s2toperational/patient_registration/screen/patient_signature_screen.dart';

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

  /// Ration card number entered during registration — passed to the ration
  /// card photo upload flow (req 3).
  final String? rationCardNumber;

  /// Dependent's BOCW ID — non-empty/'0' value triggers ration card photo
  /// upload after signature upload succeeds (req 3).
  final String? dependentBocId;

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
    this.rationCardNumber,
    this.dependentBocId,
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
        dependentBocId: dependentBocId ?? '',
        rationCardNumber: rationCardNumber ?? '',
      ),
    );

    return PopScope(
      canPop: false,
      child: NetworkWrapper(
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
      ),
    );
  }
}

class _Body extends StatelessWidget {
  final PatientFingerSignatureController c;

  const _Body({required this.c});

  bool get _isDependent =>
      c.dependentBocId.isNotEmpty && c.dependentBocId != '0';

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

          // ── Ration Card section (dependent only) ──────────────────────
          if (_isDependent) ...[
            _RationCardSection(c: c),
            SizedBox(height: 16.h),

            // "Upload Ration Card" replaces the "Next" button for dependent
            Obx(
              () => AppActiveButton(
                buttontitle:
                    c.isUploadingRc.value
                        ? 'Uploading...'
                        : 'Upload Ration Card',
                onTap:
                    c.isUploadingRc.value
                        ? () {}
                        : () => c.uploadRcAndProceed(
                          context,
                          () => const PatientSignatureScreen(),
                        ),
              ),
            ),
          ] else ...[
            // ── Next Button (non-dependent) ────────────────────────────
            AppActiveButton(
              buttontitle: 'Next',
              onTap:
                  () => c.onNextTapped(
                    context,
                    () => const PatientSignatureScreen(),
                  ),
            ),
          ],

          SizedBox(height: 16.h),
        ],
      ),
    );
  }

  Widget _captureAreaContent(PatientFingerSignatureController c) {
    if (c.thumbImageFile.value != null) {
      return ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: Image.file(c.thumbImageFile.value!, fit: BoxFit.cover),
      );
    }
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
}

// ── Ration card section (dependent only) ─────────────────────────────────────

class _RationCardSection extends StatelessWidget {
  final PatientFingerSignatureController c;

  const _RationCardSection({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      return _SectionCard(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Section label
            CommonText(
              text: 'Ration Card Photos',
              fontSize: 14.sp,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.start,
            ),
            SizedBox(height: 8.h),

            // Note
            Container(
              width: double.infinity,
              padding: EdgeInsets.all(10.w),
              decoration: BoxDecoration(
                color: kPrimaryColor.withValues(alpha: 0.07),
                borderRadius: BorderRadius.circular(8),
              ),
              child: CommonText(
                text:
                    'For Old ration card 2 photos required and for digital ration card only 1 photo required',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
            ),
            SizedBox(height: 12.h),

            // Radio buttons
            CommonText(
              text: 'Select Ration Card Status',
              fontSize: 13.sp,
              fontWeight: FontWeight.w500,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            SizedBox(height: 4.h),
            Row(
              children: [
                _RadioOption(
                  label: 'Old',
                  value: 'manual',
                  groupValue: c.rcType.value,
                  onChanged: c.onRcTypeChanged,
                ),
                SizedBox(width: 16.w),
                _RadioOption(
                  label: 'Digital',
                  value: 'digital',
                  groupValue: c.rcType.value,
                  onChanged: c.onRcTypeChanged,
                ),
              ],
            ),
            SizedBox(height: 12.h),

            // Capture button
            Row(
              children: [
                GestureDetector(
                  onTap:
                      c.rcPhotos.length < c.rcMaxPhotos
                          ? () => c.captureRcPhoto(context)
                          : null,
                  child: Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: 14.w,
                      vertical: 10.h,
                    ),
                    decoration: BoxDecoration(
                      color:
                          c.rcPhotos.length < c.rcMaxPhotos
                              ? kPrimaryColor
                              : kPrimaryColor.withValues(alpha: 0.4),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.camera_alt_outlined,
                          color: kWhiteColor,
                          size: 20,
                        ),
                        SizedBox(width: 6.w),
                        CommonText(
                          text: 'Capture Photo',
                          fontSize: 13.sp,
                          fontWeight: FontWeight.w500,
                          textColor: kWhiteColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ),
                  ),
                ),
                SizedBox(width: 10.w),
                CommonText(
                  text: '${c.rcPhotos.length}/${c.rcMaxPhotos} photos',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  textColor: kLabelTextColor,
                  textAlign: TextAlign.start,
                ),
              ],
            ),

            // Photo grid
            if (c.rcPhotos.isNotEmpty) ...[
              SizedBox(height: 12.h),
              Wrap(
                spacing: 10.w,
                runSpacing: 10.h,
                children: List.generate(c.rcPhotos.length, (index) {
                  return Stack(
                    children: [
                      ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.file(
                          c.rcPhotos[index],
                          width: 90.w,
                          height: 90.h,
                          fit: BoxFit.cover,
                        ),
                      ),
                      Positioned(
                        top: 2,
                        right: 2,
                        child: GestureDetector(
                          onTap: () => c.removeRcPhoto(index),
                          child: Container(
                            width: 22,
                            height: 22,
                            decoration: const BoxDecoration(
                              color: Colors.red,
                              shape: BoxShape.circle,
                            ),
                            child: const Icon(
                              Icons.close,
                              color: Colors.white,
                              size: 14,
                            ),
                          ),
                        ),
                      ),
                    ],
                  );
                }),
              ),
            ],
          ],
        ),
      );
    });
  }
}

class _RadioOption extends StatelessWidget {
  final String label;
  final String value;
  final String groupValue;
  final void Function(String) onChanged;

  const _RadioOption({
    required this.label,
    required this.value,
    required this.groupValue,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onChanged(value),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Radio<String>(
            value: value,
            groupValue: groupValue,
            activeColor: kPrimaryColor,
            visualDensity: VisualDensity.compact,
            materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
            onChanged: (v) => onChanged(v!),
          ),
          CommonText(
            text: label,
            fontSize: 13.sp,
            fontWeight: FontWeight.w500,
            textColor: kBlackColor,
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
