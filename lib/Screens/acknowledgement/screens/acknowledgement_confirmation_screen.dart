import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Screens/acknowledgement/controllers/acknowledgement_signature_controller.dart';
import 'package:s2toperational/Screens/acknowledgement/screens/acknowledgement_signature_screen.dart';

class AcknowledgementConfirmationScreen extends StatelessWidget {
  final AcknowledgementPatientOutput patient;
  final int campId;

  const AcknowledgementConfirmationScreen({
    super.key,
    required this.patient,
    required this.campId,
  });

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(
      AcknowledgementSignatureController(),
      tag: 'ack_sign_${patient.regdId}',
    );
    controller.initPatient(patient: patient, campId: campId);

    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Patient Details Confirmation',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 12.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _SectionHeader(title: 'Patient Information'),
            const SizedBox(height: 8),
            _InfoCard(
              children: [
                _InfoRow(label: 'Name', value: patient.englishName ?? '-'),
                _InfoRow(label: 'Reg No', value: patient.regdNo?.toString() ?? '-'),
                _InfoRow(label: 'Age', value: patient.age?.toString() ?? '-'),
                _InfoRow(
                  label: 'Gender',
                  value: _genderLabel(patient.gender),
                ),
              ],
            ),
            const SizedBox(height: 16),
            _SectionHeader(title: 'Documents Status'),
            const SizedBox(height: 8),
            _DocumentCard(
              title: 'Patient Photo',
              imageUrl: patient.patientPhoto,
            ),
            const SizedBox(height: 8),
            _DocumentCard(
              title: 'Health Card',
              imageUrl: patient.healthCardPath,
            ),
            if (patient.isHCRenewal?.toLowerCase() == 'yes') ...[
              const SizedBox(height: 8),
              _DocumentCard(
                title: 'Renewal Slip',
                imageUrl: patient.hCRenewalFilePath,
              ),
            ],
            const SizedBox(height: 8),
            _StatusCard(
              title: 'Audiometry File',
              isPresent: (patient.audioImage?.isNotEmpty ?? false),
            ),
            const SizedBox(height: 8),
            _DocumentCard(
              title: 'Thumb Print',
              imageUrl: patient.userThumbPath,
            ),
            const SizedBox(height: 12),
            Obx(
              () => CheckboxListTile(
                value: controller.isNoThumbDevice.value,
                onChanged: (val) =>
                    controller.toggleNoThumbDevice(val ?? false),
                title: CommonText(
                  text: 'Thumb print device not available',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                activeColor: kPrimaryColor,
                controlAffinity: ListTileControlAffinity.leading,
                contentPadding: EdgeInsets.zero,
              ),
            ),
            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              height: 48.h,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: kPrimaryColor,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                onPressed: () => _onNextTap(context, controller),
                child: CommonText(
                  text: 'Next',
                  fontSize: 15.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.center,
                ),
              ),
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
  }

  void _onNextTap(
    BuildContext context,
    AcknowledgementSignatureController controller,
  ) {
    final hasThumb =
        patient.userThumbPath?.isNotEmpty ?? false;
    if (!controller.isNoThumbDevice.value && !hasThumb) {
      ToastManager.toast('Upload thumb print photo or check device not available');
      return;
    }

    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => AcknowledgementSignatureScreen(
          patient: patient,
          campId: campId,
          controllerTag: 'ack_sign_${patient.regdId}',
        ),
      ),
    );
  }

  String _genderLabel(String? gender) {
    switch (gender?.toUpperCase()) {
      case 'M':
        return 'Male';
      case 'F':
        return 'Female';
      default:
        return 'Other';
    }
  }
}

class _SectionHeader extends StatelessWidget {
  final String title;
  const _SectionHeader({required this.title});

  @override
  Widget build(BuildContext context) {
    return CommonText(
      text: title,
      fontSize: 14.sp,
      fontWeight: FontWeight.w700,
      textColor: kPrimaryColor,
      textAlign: TextAlign.start,
    );
  }
}

class _InfoCard extends StatelessWidget {
  final List<Widget> children;
  const _InfoCard({required this.children});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.08),
            blurRadius: 8,
          ),
        ],
      ),
      padding: const EdgeInsets.all(12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: children,
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  final String label;
  final String value;
  const _InfoRow({required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 2),
      child: Row(
        children: [
          SizedBox(
            width: 90.w,
            child: Text(
              label,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(13),
                color: Colors.black87,
              ),
            ),
          ),
          Text(
            ': ',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: responsiveFont(13),
              color: Colors.black87,
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w400,
                fontSize: responsiveFont(13),
                color: dropDownTitleHeader,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _DocumentCard extends StatelessWidget {
  final String title;
  final String? imageUrl;

  const _DocumentCard({required this.title, this.imageUrl});

  bool get _hasImage => imageUrl != null && imageUrl!.isNotEmpty;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.08),
            blurRadius: 6,
          ),
        ],
      ),
      padding: const EdgeInsets.all(12),
      child: Row(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: _hasImage
                ? Image.network(
                    imageUrl!,
                    width: 60.w,
                    height: 60.h,
                    fit: BoxFit.cover,
                    errorBuilder: (ctx, e, st) => _placeholder(),
                  )
                : _placeholder(),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: title,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                const SizedBox(height: 4),
                Row(
                  children: [
                    Icon(
                      _hasImage ? Icons.check_circle : Icons.cancel,
                      size: 16,
                      color: _hasImage ? Colors.green : Colors.red,
                    ),
                    const SizedBox(width: 4),
                    CommonText(
                      text: _hasImage ? 'Available' : 'Not available',
                      fontSize: 11.sp,
                      fontWeight: FontWeight.w400,
                      textColor:
                          _hasImage ? Colors.green : Colors.red,
                      textAlign: TextAlign.start,
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _placeholder() {
    return Container(
      width: 60.w,
      height: 60.h,
      decoration: BoxDecoration(
        color: Colors.grey.shade200,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Icon(Icons.image_not_supported, color: Colors.grey, size: 28.sp),
    );
  }
}

class _StatusCard extends StatelessWidget {
  final String title;
  final bool isPresent;

  const _StatusCard({required this.title, required this.isPresent});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.08),
            blurRadius: 6,
          ),
        ],
      ),
      padding: const EdgeInsets.all(12),
      child: Row(
        children: [
          Icon(
            isPresent ? Icons.check_circle : Icons.cancel,
            color: isPresent ? Colors.green : Colors.red,
            size: 28,
          ),
          const SizedBox(width: 12),
          Expanded(
            child: CommonText(
              text: title,
              fontSize: 13.sp,
              fontWeight: FontWeight.w600,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
          ),
          CommonText(
            text: isPresent ? 'Available' : 'Not available',
            fontSize: 11.sp,
            fontWeight: FontWeight.w400,
            textColor: isPresent ? Colors.green : Colors.red,
            textAlign: TextAlign.end,
          ),
        ],
      ),
    );
  }
}