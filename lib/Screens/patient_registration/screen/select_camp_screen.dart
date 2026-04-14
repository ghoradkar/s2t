// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
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

  void _openFilterSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (sheetContext) {
        return Obx(() {
          _dateCtrl.text = c.selectedDate.value;
          return Padding(
            padding: EdgeInsets.fromLTRB(
              16.w,
              16.h,
              16.w,
              MediaQuery.of(sheetContext).viewInsets.bottom + 24.h,
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Center(
                  child: Container(
                    width: 40.w,
                    height: 4.h,
                    decoration: BoxDecoration(
                      color: kTextFieldBorder,
                      borderRadius: BorderRadius.circular(2),
                    ),
                  ),
                ),
                SizedBox(height: 16.h),
                CommonText(
                  text: 'Filter Camps',
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.left,
                ),
                SizedBox(height: 12.h),
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
                  onTap: () async {
                    Navigator.pop(sheetContext);
                    await c.onDateTapped(context);
                  },
                ),
                SizedBox(height: 12.h),
              ],
            ),
          );
        });
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Select Camp',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
        actions: [
          IconButton(
            onPressed: _openFilterSheet,
            icon: const Icon(Icons.filter_alt_outlined, color: kWhiteColor),
            tooltip: 'Filter',
          ),
        ],
        showActions: true,
      ),
      body: Column(
        children: [
          Padding(
            padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 0),
            child: _SectionCard(
              child: AppTextField(
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
            ),
          ),
          // SizedBox(height: 12.h),
          // Obx(() => Padding(
          //   padding: EdgeInsets.symmetric(horizontal: 12.w),
          //   child: Row(
          //     children: [
          //       CommonText(
          //         text: 'Available Camps',
          //         fontSize: 13.sp,
          //         fontWeight: FontWeight.w600,
          //         textColor: kTextColor,
          //         textAlign: TextAlign.left,
          //       ),
          //       SizedBox(width: 8.w),
          //       Container(
          //         padding: EdgeInsets.symmetric(
          //           horizontal: 10.w,
          //           vertical: 2.h,
          //         ),
          //         decoration: BoxDecoration(
          //           color: kPrimaryColor.withValues(alpha: 0.1),
          //           borderRadius: BorderRadius.circular(20),
          //         ),
          //         child: CommonText(
          //           text: c.filteredCampList.length.toString(),
          //           fontSize: 11.sp,
          //           fontWeight: FontWeight.w600,
          //           textColor: kPrimaryColor,
          //           textAlign: TextAlign.center,
          //         ),
          //       ),
          //     ],
          //   ),
          // )),
          SizedBox(height: 10.h),
          Expanded(
            child: Obx(() {
              if (c.isLoading.value) {
                return const CommonSkeletonPatientList(
                  itemCount: 4,
                ).paddingOnly(left: 12.w, right: 12.w);
              }
              if (c.filteredCampList.isEmpty) {
                return NoDataFound();
              }
              return ListView.separated(
                padding: EdgeInsets.fromLTRB(12.w, 4.h, 12.w, 12.h),
                itemCount: c.filteredCampList.length,
                separatorBuilder: (_, _i) => SizedBox(height: 10.h),
                itemBuilder: (context, index) {
                  final camp = c.filteredCampList[index];
                  return _CampCard(camp: camp, controller: c);
                },
              );
            }),
          ),
        ],
      ),
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
    return Obx(() {
      final isCheckingThis =
          controller.isCheckingAttendance.value &&
          controller.checkingCampId.value == (camp.campId ?? '');
      return InkWell(
        onTap: () => controller.onCampTapped(camp, context),
        borderRadius: BorderRadius.circular(12),
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
              _cardRow(icMapPin, 'Address', camp.campLocation ?? '--'),
              _cardRow(
                icCampCreation,
                'Camp Type',
                camp.campTypeDescription ?? '--',
              ),
              _cardRow(iconFile, 'Initiated By', camp.initiatedBy1 ?? '--'),
              if (isCheckingThis) ...[
                SizedBox(height: 10.h),
                LinearProgressIndicator(
                  color: kPrimaryColor.withValues(alpha: 0.6),
                  backgroundColor: kPrimaryColor.withValues(alpha: 0.2),
                ),
              ],
            ],
          ),
        ),
      );
    });
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
