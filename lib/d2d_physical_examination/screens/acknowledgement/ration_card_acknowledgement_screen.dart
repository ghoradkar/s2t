import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/d2d_physical_examination/controller/ration_card_acknowledgement_controller.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';

class RationCardAcknowledgementScreen extends StatelessWidget {
  final AcknowledgementPatientOutput patient;
  final int campId;

  const RationCardAcknowledgementScreen({
    super.key,
    required this.patient,
    required this.campId,
  });

  @override
  Widget build(BuildContext context) {
    final ctrl = Get.put(
      RationCardAcknowledgementController(patient: patient, campId: campId),
      tag: 'rc_ack_${patient.regdId}',
    );

    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Ration Card Acknowledgement',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Get.delete<RationCardAcknowledgementController>(
              tag: 'rc_ack_${patient.regdId}');
          Navigator.pop(context);
        },
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _PatientDetailsCard(patient: patient, ctrl: ctrl),
            SizedBox(height: 20.h),
            _RationCardTypeSection(ctrl: ctrl),
            SizedBox(height: 20.h),
            _PhotoSection(ctrl: ctrl),
            SizedBox(height: 32.h),
            _UploadButton(ctrl: ctrl),
            SizedBox(height: 24.h),
          ],
        ),
      ),
    );
  }
}

// â”€â”€ Patient Details â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

class _PatientDetailsCard extends StatelessWidget {
  final AcknowledgementPatientOutput patient;
  final RationCardAcknowledgementController ctrl;

  const _PatientDetailsCard({required this.patient, required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(14.r),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.07),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _sectionLabel('Patient Details'),
          SizedBox(height: 10.h),
          _InfoRow('Name', patient.englishName ?? '-'),
          _InfoRow('Reg. No.', patient.regdNo?.toString() ?? '-'),
          _InfoRow('DOB', patient.dOB ?? '-'),
          _InfoRow('Age', patient.age?.toString() ?? '-'),
          _InfoRow('Gender', ctrl.genderLabel),
          _InfoRow(
            'Ration Card No.',
            patient.rationCardNo?.isNotEmpty == true
                ? patient.rationCardNo!
                : 'Not Available',
          ),
        ],
      ),
    );
  }
}

Widget _sectionLabel(String title) => CommonText(
      text: title,
      fontSize: 14.sp,
      fontWeight: FontWeight.w700,
      textColor: kPrimaryColor,
      textAlign: TextAlign.start,
    );

class _InfoRow extends StatelessWidget {
  final String label;
  final String value;
  const _InfoRow(this.label, this.value);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 4.h),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 130.w,
            child: Text(
              label,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: kTextColor,
              ),
            ),
          ),
          Text(
            ': ',
            style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                color: kTextColor),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                color: Colors.black87,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// â”€â”€ Ration Card Type â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

class _RationCardTypeSection extends StatelessWidget {
  final RationCardAcknowledgementController ctrl;
  const _RationCardTypeSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(14.r),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.07),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _sectionLabel('Select Ration Card Type'),
          SizedBox(height: 10.h),
          Obx(
            () => Row(
              children: [
                _RadioOption(
                  label: 'Manual',
                  selected: ctrl.rationCardType.value == RationCardType.manual,
                  onTap: () => ctrl.setRationCardType(RationCardType.manual),
                ),
                SizedBox(width: 24.w),
                _RadioOption(
                  label: 'Digital',
                  selected: ctrl.rationCardType.value == RationCardType.digital,
                  onTap: () => ctrl.setRationCardType(RationCardType.digital),
                ),
              ],
            ),
          ),
          SizedBox(height: 6.h),
          Obx(
            () => Text(
              ctrl.rationCardType.value == RationCardType.manual
                  ? '* Manual requires 2 photos (front & back)'
                  : '* Digital requires 1 photo',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: Colors.grey[600],
                fontStyle: FontStyle.italic,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _RadioOption extends StatelessWidget {
  final String label;
  final bool selected;
  final VoidCallback onTap;

  const _RadioOption({
    required this.label,
    required this.selected,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Row(
        children: [
          Container(
            width: 20.r,
            height: 20.r,
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(
                color: selected ? kPrimaryColor : Colors.grey,
                width: 2,
              ),
            ),
            child: selected
                ? Center(
                    child: Container(
                      width: 10.r,
                      height: 10.r,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: kPrimaryColor,
                      ),
                    ),
                  )
                : null,
          ),
          SizedBox(width: 6.w),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
              color: selected ? kPrimaryColor : kTextColor,
            ),
          ),
        ],
      ),
    );
  }
}

// â”€â”€ Photo Section â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

class _PhotoSection extends StatelessWidget {
  final RationCardAcknowledgementController ctrl;
  const _PhotoSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(14.r),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.07),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              _sectionLabel('Ration Card Photo'),
              Obx(
                () => GestureDetector(
                  onTap: ctrl.capturedPhotos.length < ctrl.maxPhotos
                      ? ctrl.capturePhoto
                      : null,
                  child: Container(
                    padding:
                        EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
                    decoration: BoxDecoration(
                      color: ctrl.capturedPhotos.length < ctrl.maxPhotos
                          ? kPrimaryColor
                          : Colors.grey[400],
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(Icons.camera_alt,
                            color: Colors.white, size: 18.r),
                        SizedBox(width: 6.w),
                        Text(
                          'Capture',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
          SizedBox(height: 12.h),
          Obx(() {
            if (ctrl.capturedPhotos.isEmpty) {
              return Container(
                width: double.infinity,
                height: 100.h,
                decoration: BoxDecoration(
                  color: kBackground,
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(
                      color: kPrimaryColor.withValues(alpha: 0.3)),
                ),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.image_outlined,
                        size: 36.sp,
                        color: kPrimaryColor.withValues(alpha: 0.4)),
                    SizedBox(height: 6.h),
                    Text(
                      'No photos captured yet',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 12.sp,
                        color: Colors.grey[500],
                      ),
                    ),
                  ],
                ),
              );
            }

            return Column(
              children: List.generate(ctrl.capturedPhotos.length, (i) {
                return _PhotoTile(
                  index: i,
                  file: ctrl.capturedPhotos[i],
                  onDelete: () => ctrl.deletePhoto(i),
                );
              }),
            );
          }),
        ],
      ),
    );
  }
}

class _PhotoTile extends StatelessWidget {
  final int index;
  final File file;
  final VoidCallback onDelete;

  const _PhotoTile({
    required this.index,
    required this.file,
    required this.onDelete,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: 10.h),
      decoration: BoxDecoration(
        border: Border.all(color: borderDashboardColor),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        children: [
          ClipRRect(
            borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(10),
              bottomLeft: Radius.circular(10),
            ),
            child: Image.file(
              file,
              width: 90.w,
              height: 80.h,
              fit: BoxFit.cover,
            ),
          ),
          SizedBox(width: 12.w),
          Expanded(
            child: Text(
              'Photo ${index + 1}',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w500,
                color: kTextColor,
              ),
            ),
          ),
          GestureDetector(
            onTap: onDelete,
            child: Container(
              margin: EdgeInsets.only(right: 10.w),
              padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 6.h),
              decoration: BoxDecoration(
                color: Colors.red[50],
                borderRadius: BorderRadius.circular(6),
                border: Border.all(color: Colors.red.withValues(alpha: 0.4)),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.delete_outline,
                      color: Colors.red, size: 16.r),
                  SizedBox(width: 4.w),
                  Text(
                    'Delete',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 12.sp,
                      fontWeight: FontWeight.w500,
                      color: Colors.red,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// â”€â”€ Upload Button â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

class _UploadButton extends StatelessWidget {
  final RationCardAcknowledgementController ctrl;
  const _UploadButton({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(
      () => SizedBox(
        width: double.infinity,
        height: 50.h,
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            backgroundColor: kPrimaryColor,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10),
            ),
          ),
          onPressed: ctrl.isLoading.value ? null : ctrl.uploadRationCard,
          child: ctrl.isLoading.value
              ? const SizedBox(
                  width: 22,
                  height: 22,
                  child: CircularProgressIndicator(
                    color: Colors.white,
                    strokeWidth: 2,
                  ),
                )
              : CommonText(
                  text: 'Upload Photo',
                  fontSize: 15.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.center,
                ),
        ),
      ),
    );
  }
}
