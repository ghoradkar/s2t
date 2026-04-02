// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/PendingCountScreen/PendingCountScreen.dart';
import 'package:s2toperational/Views/CalenderScreen/CalenderScreen.dart';
import '../controller/camp_calendar_controller.dart';
import 'CampCalendarCampListScreen/CampCalendarCampListScreen.dart';
import 'CampCalendarCountView/CampCalendarCountView.dart';

class CampCalendarScreen extends StatelessWidget {
  const CampCalendarScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CampCalendarController>(
      init: CampCalendarController(),
      dispose: (_) => Get.delete<CampCalendarController>(),
      builder: (ctrl) {
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Camp Calendar',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Navigator.pop(context);
              },
            ),
            body: AnnotatedRegion(
              value: const SystemUiOverlayStyle(
                statusBarColor: kPrimaryColor,
                statusBarBrightness: Brightness.light,
                statusBarIconBrightness: Brightness.light,
              ),
              child: Container(
                color: Colors.white,
                height: SizeConfig.screenHeight,
                width: SizeConfig.screenWidth,
                child: SingleChildScrollView(
                  child: Container(
                    color: Colors.transparent,
                    width: SizeConfig.screenWidth,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // ── Filter fields ──────────────────────────────────
                        Container(
                          width: MediaQuery.of(context).size.width,
                          decoration: const BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.all(
                              Radius.circular(10),
                            ),
                          ),
                          padding: EdgeInsets.symmetric(
                            vertical: 10.h,
                            horizontal: 10.w,
                          ),
                          child: Column(
                            children: [
                              AppTextField(
                                onTap: () {
                                  ctrl.isShowSubOrg = true;
                                  ctrl.getSubOrganization();
                                },
                                controller: ctrl.subOrgController,
                                readOnly: true,
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Sub Organization',
                                    style: TextStyle(
                                      color: kLabelTextColor,
                                      fontSize: 14.sp,
                                      fontFamily: FontConstants.interFonts,
                                    ),
                                  ),
                                ),
                                suffixIcon: const Icon(
                                  Icons.arrow_drop_down_outlined,
                                ),
                                prefixIcon: Image.asset(
                                  icMapPin,
                                  color: kPrimaryColor,
                                ).paddingOnly(left: 6.w),
                              ),

                              SizedBox(height: 8.h),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.start,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Expanded(
                                    child: AppTextField(
                                      onTap: () => ctrl.getBindDivision(),
                                      controller: ctrl.divisionController,
                                      readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Division',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: 14.sp,
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      suffixIcon: const Icon(
                                        Icons.arrow_drop_down_outlined,
                                      ),
                                      prefixIcon: Image.asset(
                                        icMapPin,
                                        color: kPrimaryColor,
                                      ).paddingOnly(left: 6.w),
                                    ),
                                  ),

                                  SizedBox(width: 12.w),
                                  Expanded(
                                    child: AppTextField(
                                      onTap: () => ctrl.getBindDistrict(),
                                      controller: ctrl.districtController,
                                      readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'District',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: 14.sp,
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      suffixIcon: const Icon(
                                        Icons.arrow_drop_down_outlined,
                                      ),
                                      prefixIcon: Image.asset(
                                        icMapPin,
                                        color: kPrimaryColor,
                                      ).paddingOnly(left: 6.w),
                                    ),
                                  ),
                                ],
                              ).paddingSymmetric(horizontal: 4.w),
                              SizedBox(height: 8.h),
                              AppTextField(
                                onTap: () => ctrl.getCampTypeAndCatagory(),
                                controller: ctrl.campTypeController,
                                readOnly: true,
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Camp Type',
                                    style: TextStyle(
                                      color: kLabelTextColor,
                                      fontSize: 14.sp,
                                      fontFamily: FontConstants.interFonts,
                                    ),
                                  ),
                                ),
                                suffixIcon: const Icon(
                                  Icons.arrow_drop_down_outlined,
                                ),
                                prefixIcon: Image.asset(
                                  icnTent,
                                  color: kPrimaryColor,
                                ).paddingOnly(left: 6.w),
                              ),
                            ],
                          ),
                        ),

                        // ── Calendar ───────────────────────────────────────
                        CalenderScreen(
                          attendanceMap: ctrl.attendanceMap,
                          didChangeDate: (p0) => ctrl.onMonthChanged(p0),
                          onDateSelectedTap: (date) {
                            int months = date.month;
                            String monthsString =
                                months <= 9 ? "0$months" : "$months";

                            int days = date.day;
                            String dayString = days <= 9 ? "0$days" : "$days";

                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder:
                                    (context) => CampCalendarCampListScreen(
                                      year: date.year.toString(),
                                      month: date.month.toString(),
                                      day: date.day.toString(),
                                      isShowCampType: false,
                                      selectedCampType: ctrl.selectedCampType,
                                      isFromCampCalener: true,
                                      subOrgId:
                                          ctrl.selectedSubOrganization
                                              ?.subOrgId ??
                                          0,
                                      dIVID:
                                          ctrl.selectedDivision?.dIVID ?? 0,
                                      distCode:
                                          ctrl.selectedDistrict
                                              ?.dISTLGDCODE ??
                                          0,
                                      calanderSelectedDate:
                                          "${date.year}-$monthsString-$dayString",
                                      isTodayCount: false,
                                    ),
                              ),
                            );
                          },
                        ).paddingSymmetric(horizontal: 18.w),
                        SizedBox(height: 8.h),

                        // ── Dashboard title ────────────────────────────────
                        Text(
                          ctrl.statusDashboardTitle,
                          style: TextStyle(
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w700,
                            fontSize: 14.sp,
                          ),
                        ).paddingSymmetric(horizontal: 18.w),
                        SizedBox(height: 8.h),

                        // ── Count cards ────────────────────────────────────
                        Container(
                          color: Colors.white,
                          child: Column(
                            children: [
                              CampCalendarCountView(
                                titleString: "Total Month's Beneficiary Count",
                                countValue:
                                    ctrl
                                        .homeAndHubProcessingModel
                                        ?.output
                                        .first
                                        .totalMonthsBeneficiaryCount
                                        .toString() ??
                                    'NA',
                                showTop: true,
                                showBottom: false,
                              ),
                              CampCalendarCountView(
                                titleString: "Today's Beneficiary Count",
                                countValue:
                                    ctrl
                                        .homeAndHubProcessingModel
                                        ?.output
                                        .first
                                        .todaysBeneficiaryCount
                                        .toString() ??
                                    'NA',
                                showTop: false,
                                showBottom: false,
                              ),
                              CampCalendarCountView(
                                titleString: "Home Lab Processed Count",
                                countValue:
                                    ctrl
                                        .homeAndHubProcessingModel
                                        ?.output
                                        .first
                                        .homeLabProcessedCount
                                        .toString() ??
                                    'NA',
                                showTop: false,
                                showBottom: false,
                              ),
                              CampCalendarCountView(
                                titleString: "Hub Lab Processed Count",
                                countValue:
                                    ctrl
                                        .homeAndHubProcessingModel
                                        ?.output
                                        .first
                                        .hubLabProcessedCount
                                        .toString() ??
                                    'NA',
                                showTop: false,
                                showBottom: false,
                              ),
                              GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder:
                                          (context) => PendingCountScreen(
                                            monthId: '${ctrl.month}',
                                            year: '${ctrl.year}',
                                            subOrgId:
                                                '${ctrl.selectedSubOrganization?.subOrgId ?? 0}',
                                            dIVID:
                                                '${ctrl.selectedDivision?.dIVID ?? 0}',
                                            distcode:
                                                '${ctrl.selectedDistrict?.dISTLGDCODE ?? 0}',
                                            userId: '${ctrl.empCode}',
                                            dESGID: '${ctrl.dESGID}',
                                            campType:
                                                '${ctrl.selectedCampType?.cAMPTYPE ?? 0}',
                                          ),
                                    ),
                                  );
                                },
                                child: CampCalendarCountView(
                                  titleString: "Home Lab Pending Count",
                                  countValue:
                                      ctrl
                                          .homeAndHubProcessingModel
                                          ?.output
                                          .first
                                          .homeLabProcessedPendingCount
                                          .toString() ??
                                      'NA',
                                  showTop: false,
                                  textCountColor: Colors.blue,
                                  showBottom: false,
                                ),
                              ),
                              GestureDetector(
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder:
                                          (context) => PendingCountScreen(
                                            monthId: '${ctrl.month}',
                                            year: '${ctrl.year}',
                                            subOrgId:
                                                '${ctrl.selectedSubOrganization?.subOrgId ?? 0}',
                                            dIVID:
                                                '${ctrl.selectedDivision?.dIVID ?? 0}',
                                            distcode:
                                                '${ctrl.selectedDistrict?.dISTLGDCODE ?? 0}',
                                            userId: '${ctrl.empCode}',
                                            dESGID: '${ctrl.dESGID}',
                                            campType:
                                                '${ctrl.selectedCampType?.cAMPTYPE ?? 0}',
                                          ),
                                    ),
                                  );
                                },
                                child: CampCalendarCountView(
                                  titleString: "Hub Lab Pending Count",
                                  countValue:
                                      ctrl
                                          .homeAndHubProcessingModel
                                          ?.output
                                          .first
                                          .hubLabProcessedPendingCount
                                          .toString() ??
                                      'NA',
                                  showTop: false,
                                  textCountColor: Colors.blue,
                                  showBottom: true,
                                ),
                              ),
                              Align(
                                alignment: Alignment.centerRight,
                                child: CommonText(
                                  text: ctrl.getFormattedDateTime(),
                                  fontSize: 12,
                                  fontWeight: FontWeight.normal,
                                  textColor: kTextColor,
                                  textAlign: TextAlign.end,
                                ),
                              ).paddingSymmetric(vertical: 2),
                            ],
                          ),
                        ).paddingSymmetric(horizontal: 18.w),
                        SizedBox(height: 12.h),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}