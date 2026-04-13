// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
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
    if (c.districtList.isEmpty) return;
    showModalBottomSheet(
      context: context,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) {
        return Container(
          constraints: BoxConstraints(
            maxHeight: MediaQuery.of(context).size.height * 0.55,
          ),
          child: ListView.separated(
            itemCount: c.districtList.length,
            separatorBuilder: (_, __) => const Divider(height: 1),
            itemBuilder: (_, i) {
              final item = c.districtList[i];
              return ListTile(
                title:
                // Text(
                //   item.distName ?? '--',
                //   style: TextStyle(
                //     fontFamily: FontConstants.interFonts,
                //     fontSize: 14.sp,
                //     color: kTextColor,
                //   ),
                // ),
                  CommonText(
                    text: item.distName ?? '--',
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w500,
                    textColor: kTextColor,
                    textAlign: TextAlign.start,
                  ),
                trailing: const Icon(Icons.chevron_right, color: kPrimaryColor),
                onTap: () {
                  Navigator.pop(context);
                  c.onDistrictSelected(item);
                },
              );
            },
          ),
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
    return Container(
      padding: EdgeInsets.all(12.w),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.15)),
        boxShadow: [
          BoxShadow(color: Colors.black.withValues(alpha: 0.05), blurRadius: 8),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 4.h),
                decoration: BoxDecoration(
                  color: kPrimaryColor.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(6),
                ),
                child: CommonText(
                  text: camp.campId ?? '--',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.center,
                ),
              ),
              const Spacer(),
              CommonText(
                text: camp.distLgdCode ?? '--',
                fontSize: 11.sp,
                fontWeight: FontWeight.w600,
                textColor: kLabelTextColor,
                textAlign: TextAlign.right,
              ),
            ],
          ),
          SizedBox(height: 10.h),
          Row(
            children: [
              Icon(Icons.map_rounded, color: kPrimaryColor, size: 16),
              SizedBox(width: 6.w),
              Expanded(
                child: CommonText(
                  text: camp.campLocation ?? '--',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kTextColor,
                  textAlign: TextAlign.left,
                ),
              ),
            ],
          ),
          SizedBox(height: 12.h),
          if (isCheckingThis)
            LinearProgressIndicator(
              color: kPrimaryColor,
              backgroundColor: kPrimaryColor.withValues(alpha: 0.2),
            ).paddingOnly(bottom: 8.h),
          AppButtonWithIcon(
            title: 'Select This Camp',
            mHeight: 40,
            mWidth: double.infinity,
            icon: const Icon(
              Icons.arrow_forward_rounded,
              color: Colors.white,
              size: 18,
            ),
            onTap: () => controller.onCampTapped(camp, context),
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
