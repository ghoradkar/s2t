import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/acknowledgement/controller/acknowledgement_signature_controller.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:signature/signature.dart';

class AcknowledgementSignatureScreen extends StatefulWidget {
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
  State<AcknowledgementSignatureScreen> createState() =>
      _AcknowledgementSignatureScreenState();
}

class _AcknowledgementSignatureScreenState
    extends State<AcknowledgementSignatureScreen> {
  late final TextEditingController nameController;
  late final TextEditingController genderController;
  late final TextEditingController ageController;
  late final AcknowledgementSignatureController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.find<AcknowledgementSignatureController>(
      tag: widget.controllerTag,
    );
    nameController =
        TextEditingController(text: widget.patient.englishName ?? '');
    genderController = TextEditingController(text: controller.genderLabel);
    ageController =
        TextEditingController(text: widget.patient.age?.toString() ?? '');
  }

  @override
  void dispose() {
    nameController.dispose();
    genderController.dispose();
    ageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Acknowledgement',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        physics: const NeverScrollableScrollPhysics(),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
          // â”€â”€ Beneficiary details â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
          Padding(
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
                ],
            ),
          ),

          // â”€â”€ Signature section â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
          Container(
            padding: EdgeInsets.fromLTRB(16.w, 0, 16.w, 12.h),
            color: kBackground,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                const Divider(height: 1),
                SizedBox(height: 8.h),
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
                SizedBox(height: 8.h),
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
                          height: 180.h,
                          backgroundColor: Colors.white,
                        ),
                      ),
                      const Divider(height: 1),
                      Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 8,
                          vertical: 4,
                        ),
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
                SizedBox(height: 12.h),
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
              ],
            ),
          ),
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
