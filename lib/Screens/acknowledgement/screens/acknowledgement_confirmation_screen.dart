import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
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
      body: Obx(() {
        if (controller.isDownloadingAudio.value) {
          return _AudioDownloadingView();
        }
        return _ConfirmationBody(
          controller: controller,
          onNextTap: () => _onNextTap(context, controller),
        );
      }),
    );
  }

  void _onNextTap(
    BuildContext context,
    AcknowledgementSignatureController controller,
  ) {
    if (controller.thumbFile.value == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please capture thumb photo')),
      );
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
}

class _AudioDownloadingView extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          const CircularProgressIndicator(),
          const SizedBox(height: 16),
          Text(
            'Downloading Audiometry File',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: responsiveFont(15),
              fontWeight: FontWeight.w500,
              color: Colors.black87,
            ),
          ),
        ],
      ),
    );
  }
}

class _ConfirmationBody extends StatelessWidget {
  final AcknowledgementSignatureController controller;
  final VoidCallback onNextTap;

  const _ConfirmationBody({
    required this.controller,
    required this.onNextTap,
  });

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _ThumbPrintSection(controller: controller),
          SizedBox(height: 24.h),
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
              onPressed: onNextTap,
              child: CommonText(
                text: 'Next',
                fontSize: 15.sp,
                fontWeight: FontWeight.w600,
                textColor: Colors.white,
                textAlign: TextAlign.center,
              ),
            ),
          ),
          SizedBox(height: 20.h),
        ],
      ),
    );
  }
}

class _ThumbPrintSection extends StatelessWidget {
  final AcknowledgementSignatureController controller;

  const _ThumbPrintSection({required this.controller});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        CommonText(
          text: 'Thumb Print',
          fontSize: 14.sp,
          fontWeight: FontWeight.w700,
          textColor: kPrimaryColor,
          textAlign: TextAlign.start,
        ),
        SizedBox(height: 12.h),
        Obx(
          () => GestureDetector(
            onTap: controller.isNoThumbDevice.value
                ? null
                : controller.captureThumb,
            child: Container(
              width: double.infinity,
              height: 180.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(12),
                border: Border.all(
                  color: kPrimaryColor.withValues(alpha: 0.4),
                  width: 1.5,
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.07),
                    blurRadius: 8,
                  ),
                ],
              ),
              child: controller.thumbFile.value != null
                  ? ClipRRect(
                      borderRadius: BorderRadius.circular(11),
                      child: Image.file(
                        controller.thumbFile.value!,
                        fit: BoxFit.cover,
                      ),
                    )
                  : Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.camera_alt_outlined,
                          size: 48.sp,
                          color: kPrimaryColor.withValues(alpha: 0.6),
                        ),
                        SizedBox(height: 8.h),
                        CommonText(
                          text: 'Tap to capture thumb print',
                          fontSize: 13.sp,
                          fontWeight: FontWeight.w400,
                          textColor: Colors.black54,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
            ),
          ),
        ),
      ],
    );
  }
}
