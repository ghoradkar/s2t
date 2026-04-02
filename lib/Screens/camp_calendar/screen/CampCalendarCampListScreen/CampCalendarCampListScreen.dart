// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/camp_calendar/controller/camp_calendar_camp_list_controller.dart';
import 'CampCalendarCampListRow/CampCalendarCampListRow.dart';

class CampCalendarCampListScreen extends StatelessWidget {
  final String year;
  final String month;
  final String day;
  final bool isShowCampType;
  final CampTypeAndCatagoryOutput? selectedCampType;
  final bool isFromCampCalener;
  final int subOrgId;
  final int dIVID;
  final int distCode;
  final String calanderSelectedDate;
  final bool isTodayCount;

  const CampCalendarCampListScreen({
    super.key,
    required this.year,
    required this.month,
    required this.day,
    required this.isShowCampType,
    required this.selectedCampType,
    required this.isFromCampCalener,
    required this.subOrgId,
    required this.dIVID,
    required this.distCode,
    required this.calanderSelectedDate,
    required this.isTodayCount,
  });



  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CampCalendarCampListController>(
      init: CampCalendarCampListController(
        year: year,
        month: month,
        day: day,
        isShowCampType: isShowCampType,
        selectedCampType: selectedCampType,
        isFromCampCalener: isFromCampCalener,
        subOrgId: subOrgId,
        dIVID: dIVID,
        distCode: distCode,
        calanderSelectedDate: calanderSelectedDate,
        isTodayCount: isTodayCount,
      ),
      dispose: (_) => Get.delete<CampCalendarCampListController>(),
      builder: (ctrl) {
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'All Camp List',
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
              child: Column(
                children: [
                  Container(
                    width: SizeConfig.screenWidth,
                    height: 50.h,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.15),
                          blurRadius: 10,
                        ),
                      ],
                    ),
                    child: Row(
                      children: [
                        Expanded(
                          child: Row(
                            children: [
                              Container(
                                width: 20.w,
                                height: 20.h,
                                decoration: BoxDecoration(
                                  color: todayCampOpenColor,
                                  borderRadius: BorderRadius.circular(5),
                                ),
                              ),
                              SizedBox(width: 4.w),
                              Text(
                                "Today's Open Camps",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 12.sp,
                                ),
                              ),
                            ],
                          ),
                        ),
                        Expanded(
                          child: Row(
                            children: [
                              Container(
                                width: 20.w,
                                height: 20.h,
                                decoration: BoxDecoration(
                                  color: campOpenColor,
                                  borderRadius: BorderRadius.circular(5),
                                ),
                              ),
                              SizedBox(width: 4.w),
                              Text(
                                "Open Camps",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 12.sp,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Text(
                        "Total : ",
                        textAlign: TextAlign.end,
                        style: TextStyle(
                          color: kBlackColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: 14.sp,
                        ),
                      ),
                      SizedBox(width: 4.w),
                      Text(
                        "${ctrl.totalRegistrationWorkers}",
                        style: TextStyle(
                          color: kListTitleTextColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: 14.sp,
                        ),
                      ),
                    ],
                  ).paddingOnly(top: 6.h, bottom: 0.h, right: 4.w),

                  Expanded(
                    child: ctrl.isLoading
                        ? const CommonSkeletonPatientList()
                        : ListView.builder(
                            itemCount: ctrl.campList.length,
                            shrinkWrap: true,
                            itemBuilder: (context, index) {
                              return CampCalendarCampListRow(
                                obj: ctrl.campList[index],
                                selectedCampType: ctrl.selectedCampType,
                                index: index,
                              );
                            },
                          ),
                  ),
                ],
              ).paddingSymmetric(vertical: 10.h, horizontal: 10.w),
            ),
          ),
        );
      },
    );
  }
}