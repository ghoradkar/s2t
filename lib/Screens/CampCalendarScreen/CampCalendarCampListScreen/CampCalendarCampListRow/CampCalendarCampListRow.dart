// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Screens/CampDetailsScreen/CampDetailsScreen.dart';

import '../../../../Modules/Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import '../../../../Modules/Json_Class/MonthlySurveySiteResponse/MonthlySurveySiteResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class CampCalendarCampListRow extends StatelessWidget {
  CampCalendarCampListRow({
    super.key,
    required this.obj,
    required this.selectedCampType,
    required this.index,
  });

  MonthlySurveySiteOutput obj;
  CampTypeAndCatagoryOutput? selectedCampType;
  int index;

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (context) => CampDetailsScreen(
                  campId: obj.campId ?? 0,
                  dISTLGDCODE: obj.dISTLGDCODE ?? 0,
                  campDate: obj.campDate ?? "",
                  surveyCoordinatorName: obj.surveyCoordinatorName ?? "",
                  dISTNAME: obj.dISTNAME ?? "",
                  mOBNO: obj.mOBNO ?? "",
                  cAMPTYPE: selectedCampType?.cAMPTYPE ?? 0,
                  campTypeDescription:
                      selectedCampType?.campTypeDescription ?? "",
                  isHealthScreeing: false,
                ),
          ),
        );
      },
      child: IntrinsicHeight(
        child: Stack(
          children: [
            Padding(
              padding: EdgeInsets.fromLTRB(4.w, 10.h, 2.w, 12.h),
              child: Container(
                width: SizeConfig.screenWidth,
                decoration: BoxDecoration(
                  color: FormatterManager.getCellBackgroundColor(
                    obj.campStatus ?? "",
                    obj.campDate ?? "",
                  ),
                  borderRadius: BorderRadius.circular(10),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withValues(alpha: 0.15),
                      blurRadius: 6,
                      spreadRadius: 2,
                    ),
                  ],
                ),

                child: Row(
                  children: [
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        padding: EdgeInsets.fromLTRB(6.w, 6.h, 0, 0),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            SizedBox(height: 10.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(height: 2.h),
                                SizedBox(
                                  width: 22.w,
                                  height: 22.h,
                                  child: Image.asset(icHashIcon),
                                ),
                                SizedBox(width: 4.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "Camp ID : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 12.sp,
                                        ),
                                      ),
                                      Expanded(
                                        child: Text(
                                          "${obj.campId ?? 0}",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 12.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: 4.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: 22.w,
                                  height: 22.h,
                                  child: Image.asset(icMapPin),
                                ),
                                SizedBox(width: 4.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "District : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 12.sp,
                                        ),
                                      ),
                                      Expanded(
                                        child: Text(
                                          obj.dISTNAME ?? "",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 12.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: 4.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: 22.w,
                                  height: 22.h,
                                  child: Image.asset(icCalendarMonth),
                                ),
                                SizedBox(width: 4.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "Date : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 12.sp,
                                        ),
                                      ),
                                      Expanded(
                                        child: Text(
                                          obj.campDate ?? "",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 12.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: 4.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: 22.w,
                                  height: 22.h,
                                  child: Image.asset(iconPerson),
                                ),
                                SizedBox(width: 4.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "Reg. Workers : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 12.sp,
                                        ),
                                      ),
                                      Expanded(
                                        child: Text(
                                          "${obj.rEGISTERWORKERS ?? 0}",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 12.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: 12.h),
                          ],
                        ),
                      ),
                    ),
                    Padding(
                      padding: EdgeInsets.fromLTRB(0, 0, 8.w, 0),
                      child: Container(
                        width: 50.w,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.only(
                            topRight: Radius.circular(10),
                            bottomRight: Radius.circular(10),
                          ),
                        ),
                        child: Padding(
                          padding: EdgeInsets.fromLTRB(0, 0, 0, 12.w),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.end,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              SizedBox(
                                width: 30,
                                height: 30,
                                child: Image.asset(icViewIcon),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Positioned(
              top: 0,
              left: 14,
              child: Container(
                width: 25.w,
                height: 25.h,
                decoration: BoxDecoration(
                  color: kPrimaryColor,
                  borderRadius: BorderRadius.circular(100),
                ),
                child: Center(
                  child: Text(
                    "${index + 1}",
                    style: TextStyle(
                      color: kWhiteColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.bold,
                      fontSize: 12.sp,
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
