import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/acknowledgement/controllers/acknowledgement_signature_controller.dart';
import 'package:signature/signature.dart';

class AcknowledgementSignatureScreen extends StatelessWidget {
  final AcknowledgementPatientOutput patient;
  final int campId;
  final String controllerTag;

  const AcknowledgementSignatureScreen({
    super.key,
    required this.patient,
    required this.campId,
    required this.controllerTag,
  });

  @override
  Widget build(BuildContext context) {
    final controller = Get.find<AcknowledgementSignatureController>(
      tag: controllerTag,
    );

    final nameController = TextEditingController(
      text: patient.englishName ?? '',
    );
    final genderController = TextEditingController(
      text: controller.genderLabel,
    );
    final ageController = TextEditingController(
      text: patient.age?.toString() ?? '',
    );

    return Scaffold(
      appBar: mAppBar(
        scTitle: 'acknowledgement',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 12.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _SectionHeader(title: 'Beneficiary Details'),
            const SizedBox(height: 8),
            AppTextField(
              controller: nameController,
              readOnly: true,
              hint: 'Beneficiary Name',
              label: CommonText(
                text: 'Beneficiary Name',
                fontSize: 12.sp,
                fontWeight: FontWeight.normal,
                textColor: kBlackColor,
                textAlign: TextAlign.start,
              ),
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                fontFamily: FontConstants.interFonts,
              ),
              fieldRadius: 10,
              prefixIcon: SizedBox(
                height: 20.h,
                width: 20.w,
                child: Center(
                  child: Image.asset(
                    icSearch,
                    height: 20.h,
                    width: 20.w,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 8),
            Row(
              children: [
                Expanded(
                  child: AppTextField(
                    controller: genderController,
                    readOnly: true,
                    hint: 'Gender',
                    label: CommonText(
                      text: 'Gender',
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kBlackColor,
                      textAlign: TextAlign.start,
                    ),
                    hintStyle: TextStyle(
                      fontSize: 12.sp,
                      fontWeight: FontWeight.w400,
                      fontFamily: FontConstants.interFonts,
                    ),
                    fieldRadius: 10,
                  ),
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: AppTextField(
                    controller: ageController,
                    readOnly: true,
                    hint: 'Age',
                    label: CommonText(
                      text: 'Age',
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kBlackColor,
                      textAlign: TextAlign.start,
                    ),
                    hintStyle: TextStyle(
                      fontSize: 12.sp,
                      fontWeight: FontWeight.w400,
                      fontFamily: FontConstants.interFonts,
                    ),
                    fieldRadius: 10,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),
            _SectionHeader(title: 'Patient Signature'),
            const SizedBox(height: 8),
            Container(
              width: double.infinity,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(10),
                border: Border.all(color: borderDashboardColor),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.08),
                    blurRadius: 6,
                  ),
                ],
              ),
              child: Column(
                children: [
                  ClipRRect(
                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(10),
                      topRight: Radius.circular(10),
                    ),
                    child: Signature(
                      controller: controller.signatureController,
                      height: 200.h,
                      backgroundColor: Colors.white,
                    ),
                  ),
                  const Divider(height: 1),
                  Padding(
                    padding: const EdgeInsets.all(8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        TextButton.icon(
                          onPressed: controller.clearSignature,
                          icon: const Icon(Icons.clear, size: 18),
                          label: CommonText(
                            text: 'Clear',
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w500,
                            textColor: Colors.redAccent,
                            textAlign: TextAlign.start,
                          ),
                          style: TextButton.styleFrom(
                            foregroundColor: Colors.redAccent,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: kPrimaryColor.withValues(alpha: 0.06),
                borderRadius: BorderRadius.circular(8),
                border: Border.all(
                  color: kPrimaryColor.withValues(alpha: 0.3),
                ),
              ),
              child: CommonText(
                text:
                    'I have performed all the above tests successfully. I hereby acknowledge that I have received my health reports.',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kBlackColor,
                textAlign: TextAlign.justify,
              ),
            ),
            const SizedBox(height: 24),
            Obx(
              () => SizedBox(
                width: double.infinity,
                height: 48.h,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kPrimaryColor,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  onPressed: controller.isLoading.value
                      ? null
                      : () => controller.saveSignature(),
                  child: controller.isLoading.value
                      ? const SizedBox(
                          width: 22,
                          height: 22,
                          child: CircularProgressIndicator(
                            color: Colors.white,
                            strokeWidth: 2,
                          ),
                        )
                      : CommonText(
                          text: 'Save Signature',
                          fontSize: 15.sp,
                          fontWeight: FontWeight.w600,
                          textColor: Colors.white,
                          textAlign: TextAlign.center,
                        ),
                ),
              ),
            ),
            const SizedBox(height: 20),
          ],
        ),
      ),
    );
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