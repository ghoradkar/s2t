// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/AttendancesListUsingSiteDetailsIDResponse/AttendancesListUsingSiteDetailsIDResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';

class AssignedD2DPhysicalExaminationPatientRow extends StatelessWidget {
  AttendancesListUsingSiteDetailsIDOutput obj;
  final int serialNumber;
  Function() onCallDidPressed;

  AssignedD2DPhysicalExaminationPatientRow({
    super.key,
    required this.obj,
    required this.serialNumber,
    required this.onCallDidPressed,
  });

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Stack(
        children: [
          Container(
            // height: 140.h,
            padding: EdgeInsets.fromLTRB(8.w, 20.h, 8.w, 12.h),
            decoration: BoxDecoration(
              color:
                  obj.isCall == "1"
                      ? Color(0xffC8E6C9).withValues(alpha: 0.6)
                      : Colors.white,
              borderRadius: BorderRadius.circular(10),
              boxShadow: [
                BoxShadow(
                  color: Colors.grey.withValues(alpha: 0.2),
                  spreadRadius: 1,
                  blurRadius: 1,
                ),
              ],
            ),
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(icInitiatedBy),
                          ),
                          SizedBox(width: 8.w),
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Patient Name : ".toUpperCase(),
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 14.sp,
                                  ),
                                ),

                                Expanded(
                                  child: Text(
                                    obj.englishName?.toUpperCase() ?? "",
                                    style: TextStyle(
                                      color: dropDownTitleHeader,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 10.h),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(icHashIcon),
                          ),
                          SizedBox(width: 8.w),
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Unique Screening Id : ".toUpperCase(),
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 14.sp,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    obj.screeningPatientID?.toString().toUpperCase() ?? "NA",
                                    style: TextStyle(
                                      color: dropDownTitleHeader,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 10.h),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(icHashIcon),
                          ),
                          SizedBox(width: 8.w),
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Type : ".toUpperCase(),
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 14.sp,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    "W".toUpperCase(),
                                    style: TextStyle(
                                      color: dropDownTitleHeader,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                GestureDetector(
                  onTap: () {
                    onCallDidPressed();
                  },
                  child: Container(
                    width: 32.w,
                    height: 32.h,
                    padding: EdgeInsets.symmetric(vertical: 6.h, horizontal: 6.w),
                    decoration: BoxDecoration(
                      color: kPrimaryColor,
                      borderRadius: BorderRadius.circular(100),
                    ),
                    child: Image.asset(icPhoneWhiteIcon),
                  ),
                ),
              ],
            ),
          ).paddingOnly(top: 10),

          Positioned(
            bottom: 82,
            left: 6,
            child: Container(
              alignment: Alignment.center,
              width: 24.w,
              height: 24.h,
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.circular(50),
              ),
              child: Text(
                textAlign: TextAlign.center,
                (serialNumber + 1).toString(),
                style: TextStyle(
                  color: kWhiteColor,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w500,
                  fontSize: 14.sp,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
