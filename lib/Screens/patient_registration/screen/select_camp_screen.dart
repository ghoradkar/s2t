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
import 'package:s2toperational/Screens/patient_registration/controller/select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/model/select_camp_response.dart';

class SelectCampScreen extends StatefulWidget {
  const SelectCampScreen({super.key});

  @override
  State<SelectCampScreen> createState() => _SelectCampScreenState();
}

class _SelectCampScreenState extends State<SelectCampScreen> {
  late final SelectCampController c;
  final _dateCtrl = TextEditingController();
  final _searchCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    c = Get.find<SelectCampController>();
  }

  @override
  void dispose() {
    _dateCtrl.dispose();
    _searchCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Select Camp',
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
                      suffixIcon: const Icon(
                        Icons.arrow_drop_down,
                        color: kPrimaryColor,
                      ),
                      onTap: () => c.onDateTapped(context),
                    ),
                    SizedBox(height: 10.h),
                    AppTextField(
                      controller: _searchCtrl,
                      label: _label('Search'),
                      hint: 'Search by Camp ID',
                      prefixIcon: const Icon(
                        Icons.search_rounded,
                        color: kPrimaryColor,
                        size: 18,
                      ).paddingOnly(left: 6.w),
                      onChange: (v) => c.onSearchChanged(v),
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
                      text: c.filteredCampList.length.toString(),
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
                if (c.isLoading.value) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (c.filteredCampList.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.search_off, size: 40, color: kLabelTextColor),
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
                  itemCount: c.filteredCampList.length,
                  separatorBuilder: (_, __) => SizedBox(height: 10.h),
                  itemBuilder: (context, index) {
                    final camp = c.filteredCampList[index];
                    return _CampCard(camp: camp, controller: c);
                  },
                );
              }),
            ),
          ],
        );
      }),
    );
  }

  Widget _label(String text) => Text(
        text,
        style: TextStyle(
          color: kLabelTextColor,
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        ),
      );
}

class _CampCard extends StatelessWidget {
  final SelectCampOutput camp;
  final SelectCampController controller;

  const _CampCard({required this.camp, required this.controller});

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
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 8,
          ),
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
            ],
          ),
          SizedBox(height: 10.h),
          Row(
            children: [
              Icon(Icons.map_rounded, color: kPrimaryColor, size: 16),
              SizedBox(width: 6.w),
              Expanded(
                child: CommonText(
                  text: camp.distName ?? '--',
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
