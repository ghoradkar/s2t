// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/camp_creation/controllers/camp_creation_controller.dart';

class CampCreationScreen extends StatelessWidget {
  const CampCreationScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.find<CampCreationController>();
    SizeConfig().init(context);

    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Camp Creation',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Get.back(),
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SingleChildScrollView(
          padding: EdgeInsets.only(
            bottom: MediaQuery.of(context).viewPadding.bottom,
          ),
          child: Column(
            children: [
              // ── Camp Type ───────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedCampType.value?.campTypeDescription ?? '',
                  ),
                  readOnly: true,
                  onTap: controller.loadCampTypes,
                  hint: 'Select Camp Type',
                  label: CommonText(
                    text: 'Camp Type *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icnTent,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: const Icon(Icons.keyboard_arrow_down),
                ).paddingOnly(top: 12),
              ),

              // ── Initiated By ────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedInitiatedBy.value?.initiatedBy ?? '',
                  ),
                  readOnly: true,
                  onTap: controller.isRegularCamp ? controller.loadInitiatedBy : null,
                  hint: 'Initiated By',
                  label: CommonText(
                    text: 'Initiated By *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icInitiatedBy,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: controller.isRegularCamp
                      ? const Icon(Icons.keyboard_arrow_down)
                      : null,
                ).paddingOnly(top: 12),
              ),

              // ── District ────────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.districtName.value,
                  ),
                  readOnly: true,
                  onTap: !controller.isRegularCamp ? controller.loadDistrict : null,
                  hint: 'District',
                  label: CommonText(
                    text: 'District *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icMapPin,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: !controller.isRegularCamp
                      ? const Icon(Icons.keyboard_arrow_down)
                      : null,
                ).paddingOnly(top: 12),
              ),

              // ── Taluka ──────────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedTaluka.value?.tALNAME ?? '',
                  ),
                  readOnly: true,
                  onTap: controller.loadTaluka,
                  hint: 'Select Taluka',
                  label: CommonText(
                    text: 'Taluka *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icMapPin,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: const Icon(Icons.keyboard_arrow_down),
                ).paddingOnly(top: 12),
              ),

              // ── Landing Lab ─────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedLandingLab.value?.labName ?? '',
                  ),
                  readOnly: true,
                  onTap: controller.loadLandingLab,
                  hint: 'Select Landing Lab',
                  label: CommonText(
                    text: 'Landing Lab *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icLandingLab,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: const Icon(Icons.keyboard_arrow_down),
                ).paddingOnly(top: 12),
              ),

              // ── Camp Name ───────────────────────────────────────────────────
              AppTextField(
                controller: controller.campNameController,
                readOnly: false,
                hint: 'Enter Camp Name',
                label: CommonText(
                  text: 'Camp Name *',
                  fontSize: 14.sp * 1.3,
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
                    child: Image.asset(icnTent,
                        height: 24.h, width: 24.w, fit: BoxFit.contain),
                  ),
                ),
              ).paddingOnly(top: 12),

              // ── Location action buttons ──────────────────────────────────────
              Row(
                children: [
                  InkWell(
                    onTap: controller.searchOnMap,
                    child: Row(
                      children: [
                        Icon(Icons.search, size: 22, color: kPrimaryColor)
                            .paddingOnly(right: 2.w),
                        CommonText(
                          text: 'Search On Map',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ).paddingOnly(
                        top: 8.h, left: 10.w, right: 10.h, bottom: 8.h),
                  ),
                  SizedBox(width: 12.w),
                  InkWell(
                    onTap: controller.viewOnMap,
                    child: Row(
                      children: [
                        Icon(Icons.map_outlined, size: 22, color: kPrimaryColor)
                            .paddingOnly(right: 2.w),
                        CommonText(
                          text: 'View On Map',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ).paddingOnly(
                        top: 8.h, left: 10.w, right: 10.h, bottom: 8.h),
                  ),
                ],
              ).paddingOnly(top: 12),

              // ── Manual address toggle ────────────────────────────────────────
              Obx(
                () => Row(
                  children: [
                    Checkbox(
                      value: controller.isManualAddressEntry.value,
                      onChanged: controller.onManualAddressCheckboxChanged,
                    ),
                    Text(
                      'Enter Address Manually',
                      style: TextStyle(fontSize: 14.sp),
                    ),
                  ],
                ),
              ),

              // ── Camp Address ────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: controller.campAddressController,
                  readOnly: !controller.isManualAddressEntry.value,
                  hint: 'Enter Camp Address',
                  maxLines: 4,
                  minLines: 1,
                  label: CommonText(
                    text: 'Camp Address *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icMapPin,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                ).paddingOnly(top: 4),
              ),

              // ── Camp Date & Post Camp Date ───────────────────────────────────
              Container(
                width: MediaQuery.of(context).size.width,
                color: Colors.transparent,
                child: Row(
                  children: [
                    Expanded(
                      child: Obx(
                        () => AppTextField(
                          onTap: controller.selectCampDate,
                          controller: TextEditingController(
                            text: controller.selectedCampDate.value,
                          ),
                          readOnly: true,
                          hint: 'Select Camp Date',
                          label: CommonText(
                            text: 'Camp Date *',
                            fontSize: 14.sp * 1.3,
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
                              child: Image.asset(icCalendarMonth,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain),
                            ),
                          ),
                        ).paddingOnly(top: 12, left: 12),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Obx(
                        () => AppTextField(
                          controller: TextEditingController(
                            text: controller.selectedPostCampDate.value,
                          ),
                          readOnly: true,
                          hint: 'Post Camp Date',
                          label: CommonText(
                            text: 'Post Camp Date *',
                            fontSize: 14.sp * 1.3,
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
                              child: Image.asset(icCalendarMonth,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain),
                            ),
                          ),
                        ).paddingOnly(top: 12, right: 12),
                      ),
                    ),
                  ],
                ),
              ),

              // ── Screening Tests ─────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.screeningTestDisplayString,
                  ),
                  readOnly: true,
                  onTap: controller.loadScreeningTests,
                  hint: 'Select Screening Tests',
                  label: CommonText(
                    text: 'Screening Tests *',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icScreeningTests,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                  suffixIcon: const Icon(Icons.keyboard_arrow_down),
                ).paddingOnly(top: 12),
              ),

              // ── Home Lab ────────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedHomeAndHubLab.value?.homeLab ?? '',
                  ),
                  readOnly: true,
                  hint: 'Home Lab',
                  label: CommonText(
                    text: 'Home Lab',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icLandingLab,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                ).paddingOnly(top: 12),
              ),

              // ── Hub Lab ─────────────────────────────────────────────────────
              Obx(
                () => AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedHomeAndHubLab.value?.hubLab ?? '',
                  ),
                  readOnly: true,
                  hint: 'Hub Lab',
                  label: CommonText(
                    text: 'Hub Lab',
                    fontSize: 14.sp * 1.3,
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
                      child: Image.asset(icLandingLab,
                          height: 24.h, width: 24.w, fit: BoxFit.contain),
                    ),
                  ),
                ).paddingOnly(top: 12),
              ),

              // ── Expected Beneficiary ────────────────────────────────────────
              AppTextField(
                textInputType: TextInputType.number,
                controller: controller.expectedBeneficiaryController,
                readOnly: false,
                hint: 'Enter Expected Beneficiary',
                label: CommonText(
                  text: 'Expected Beneficiary *',
                  fontSize: 14.sp * 1.3,
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
                    child: Image.asset(icUsersGroup,
                        height: 24.h, width: 24.w, fit: BoxFit.contain),
                  ),
                ),
              ).paddingOnly(top: 12),

              // ── Cancel / Save ───────────────────────────────────────────────
              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: 'Cancel',
                      isCancel: true,
                      onTap: () {},
                    ),
                  ),
                  const SizedBox(width: 62),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: 'Save',
                      onTap: controller.saveDidPressed,
                    ),
                  ),
                ],
              ).paddingOnly(left: 12, right: 12, top: 14, bottom: 14),
            ],
          ),
        ),
      ),
    );
  }
}
