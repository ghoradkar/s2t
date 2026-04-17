// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_camp_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';

class D2DSelectCampScreen extends StatefulWidget {
  const D2DSelectCampScreen({super.key});

  @override
  State<D2DSelectCampScreen> createState() => _D2DSelectCampScreenState();
}

class _D2DSelectCampScreenState extends State<D2DSelectCampScreen> {
  late final D2DSelectCampController c;
  final _dateCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    c = Get.find<D2DSelectCampController>();
  }

  @override
  void dispose() {
    _dateCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Select D2D Camp',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        _dateCtrl.text = c.selectedDate.value;
        return Column(
          children: [
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
              child: _SectionCard(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(
                          Icons.tune_rounded,
                          color: kPrimaryColor,
                          size: 18,
                        ),
                        SizedBox(width: 8.w),
                        CommonText(
                          text: 'Filter Camps',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kPrimaryColor,
                          textAlign: TextAlign.left,
                        ),
                      ],
                    ),
                    SizedBox(height: 8.h),
                    Divider(height: 1, color: kTextFieldBorder),
                    SizedBox(height: 8.h),
                    AppTextField(
                      controller: _dateCtrl,
                      readOnly: true,
                      label: _label('Camp Date'),
                      hint: 'Select Date',
                      prefixIcon: const Icon(
                        Icons.calendar_today_rounded,
                        color: kPrimaryColor,
                        size: 18,
                      ).paddingOnly(left: 6.w),
                      onTap: () => c.onDateTapped(context),
                    ),
                    SizedBox(height: 10.h),
                    Obx(
                      () => AppTextField(
                        controller: TextEditingController(
                          text: c.selectedDistrict.value?.distName ?? '',
                        ),
                        readOnly: true,
                        label: _label('District'),
                        hint: 'Select District',
                        prefixIcon: const Icon(
                          Icons.map_rounded,
                          color: kPrimaryColor,
                          size: 18,
                        ).paddingOnly(left: 6.w),
                        suffixIcon:
                            c.isLoadingDist.value
                                ? const SizedBox(
                                  width: 14,
                                  height: 14,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                  ),
                                )
                                : const Icon(
                                  Icons.arrow_drop_down,
                                  color: kPrimaryColor,
                                ),
                        onTap: () => _showDistrictPicker(context),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 12.w),
              child: Row(
                children: [
                  CommonText(
                    text: 'Available Camps',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kTextColor,
                    textAlign: TextAlign.left,
                  ),
                  SizedBox(width: 8.w),
                  Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: 10.w,
                      vertical: 2.h,
                    ),
                    decoration: BoxDecoration(
                      color: kPrimaryColor.withValues(alpha: 0.1),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: CommonText(
                      text: c.campList.length.toString(),
                      fontSize: 11.sp,
                      fontWeight: FontWeight.w600,
                      textColor: kPrimaryColor,
                      textAlign: TextAlign.center,
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 8.h),
            Expanded(
              child: Obx(() {
                if (c.isLoadingCamps.value) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (c.campList.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          Icons.search_off,
                          size: 40,
                          color: kLabelTextColor,
                        ),
                        SizedBox(height: 6.h),
                        CommonText(
                          text: 'No camps found',
                          fontSize: 13.sp,
                          fontWeight: FontWeight.w500,
                          textColor: kLabelTextColor,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  );
                }
                return ListView.separated(
                  padding: EdgeInsets.fromLTRB(12.w, 4.h, 12.w, 12.h),
                  itemCount: c.campList.length,
                  separatorBuilder: (_, __) => SizedBox(height: 10.h),
                  itemBuilder: (context, index) {
                    final camp = c.campList[index];
                    return _D2DCampCard(camp: camp, controller: c);
                  },
                );
              }),
            ),
          ],
        );
      }),
    );
  }

  Widget _label(String text) => CommonText(
    text: text,
    fontSize: 14.sp,
    fontWeight: FontWeight.w500,
    textColor: kLabelTextColor,
    textAlign: TextAlign.start,
  );

  // Text(
  //   text,
  //   style: TextStyle(
  //     color: kLabelTextColor,
  //     fontSize: 14.sp,
  //     fontFamily: FontConstants.interFonts,
  //   ),
  // );

  void _showDistrictPicker(BuildContext context) {
    if (c.isLoadingDist.value) {
      ToastManager.toast('Loading districts, please wait...');
      return;
    }
    if (c.districtList.isEmpty) {
      ToastManager.toast('No districts available');
      return;
    }
    showModalBottomSheet(
      context: context,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) {
        return Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            SizedBox(height: 10.h),
            Container(
              width: 40.w,
              height: 4.h,
              decoration: BoxDecoration(
                color: kTextFieldBorder,
                borderRadius: BorderRadius.circular(2),
              ),
            ),
            SizedBox(height: 12.h),
            CommonText(
              text: 'Select District',
              fontSize: 15.sp,
              fontWeight: FontWeight.w600,
              textColor: kTextColor,
              textAlign: TextAlign.center,
            ),
            const Divider(),
            Flexible(
              child: ListView.separated(
                shrinkWrap: true,
                itemCount: c.districtList.length,
                separatorBuilder: (_, __) => const Divider(height: 1),
                itemBuilder: (_, i) {
                  final item = c.districtList[i];
                  final isSelected =
                      c.selectedDistrict.value?.distLgdCode == item.distLgdCode;
                  return ListTile(
                    title: CommonText(
                      text: item.distName ?? '--',
                      fontSize: 14.sp,
                      fontWeight: FontWeight.w500,
                      textColor: isSelected ? kPrimaryColor : kTextColor,
                      textAlign: TextAlign.start,
                    ),
                    trailing:
                        isSelected
                            ? const Icon(Icons.check, color: kPrimaryColor)
                            : const Icon(
                              Icons.chevron_right,
                              color: kPrimaryColor,
                            ),
                    onTap: () {
                      Navigator.pop(context);
                      c.onDistrictSelected(item);
                    },
                  );
                },
              ),
            ),
            SizedBox(height: 16.h),
          ],
        );
      },
    );
  }
}

class _D2DCampCard extends StatelessWidget {
  final D2DCampOutput camp;
  final D2DSelectCampController controller;

  const _D2DCampCard({required this.camp, required this.controller});

  @override
  Widget build(BuildContext context) {
    final isCheckingThis =
        controller.isCheckingAttendance.value &&
        controller.checkingCampId.value == (camp.campId ?? '');
    return InkWell(
      onTap: () {
        controller.onCampTapped(camp, context);
      },
      child: Container(
        padding: EdgeInsets.all(12.w),
        decoration: BoxDecoration(
          color: kWhiteColor,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: kPrimaryColor.withValues(alpha: 0.15)),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.05),
              blurRadius: 8,
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _cardRow(icHashIcon, 'Camp ID', camp.campId ?? '--'),
            _cardRow(icMapPin, 'District', camp.distName ?? '--'),
            _cardRow(
              icCampCreation,
              'Camp Type',
              camp.campTypeDescription ?? '--',
            ),
            _cardRow(iconFile, 'Camp Name', camp.campName ?? '--'),
            _cardRow(iconFile, 'Initiated By', camp.initiatedBy ?? '--'),
            _cardRow(iconFile, 'Created By', camp.campCreatedBy ?? '--'),
            SizedBox(height: 12.h),
            if (isCheckingThis)
              LinearProgressIndicator(
                color: kPrimaryColor,
                backgroundColor: kPrimaryColor.withValues(alpha: 0.2),
              ).paddingOnly(bottom: 8.h),
          ],
        ),
      ),
    );
  }

  Widget _cardRow(String icon, String heading, String value) {
    return Padding(
      padding: EdgeInsets.only(bottom: 6.h),
      child: Row(
        children: [
          Image.asset(icon, width: 20.w, height: 20.h).paddingOnly(right: 6.w),
          CommonText(
            text: '$heading : ',
            fontSize: 13.sp,
            fontWeight: FontWeight.w600,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
          Expanded(
            child: CommonText(
              text: value,
              fontSize: 13.sp,
              fontWeight: FontWeight.normal,
              textColor: kTextColor,
              textAlign: TextAlign.start,
            ),
          ),
          if (heading == 'Camp ID')
            Container(
              width: 28.w,
              height: 28.w,
              decoration: BoxDecoration(
                color: kPrimaryColor.withValues(alpha: 0.85),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Center(
                child: Icon(
                  Icons.remove_red_eye_outlined,
                  color: kWhiteColor,
                  size: 16,
                ),
              ),
            ),
        ],
      ),
    );
  }
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
