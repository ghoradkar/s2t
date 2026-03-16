// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../Modules/Json_Class/CampDetailsntApprovalResponse/CampDetailsntApprovalResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class ResourceReMappingCampDetails extends StatelessWidget {
  ResourceReMappingCampDetails({super.key, required this.campDetails});

  CampDetailsntApprovalOutput? campDetails;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(8),
      width: MediaQuery.of(context).size.width,
      decoration: BoxDecoration(
        color: Colors.white,
        boxShadow: [
          BoxShadow(
            blurRadius: 4,
            spreadRadius: 0,
            offset: const Offset(0, 1),
            color: const Color(0xFF000000).withValues(alpha: 0.15),
          ),
        ],
        borderRadius: BorderRadius.circular(10),
      ),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(width: 24.w, height: 24.h, child: Image.asset(icMapPin)),
              SizedBox(width: 8.w),
              Expanded(
                child: RichText(
                  text: TextSpan(
                    children: [
                      TextSpan(
                        text: "District : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: 16.sp,
                        ),
                      ),
                      TextSpan(
                        text: campDetails?.district ?? "",
                        style: TextStyle(
                          color: dropDownTitleHeader,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: 16.sp,
                        ),
                      ),
                    ],
                  ),
                  textAlign: TextAlign.start,
                  maxLines: null, // allow multiline
                  overflow: TextOverflow.visible,
                ),
              ),
            ],
          ),

          SizedBox(height: 8.h),
          SizedBox(
            width: SizeConfig.screenWidth,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  width: 24.w,
                  height: 24.h,
                  child: Image.asset(icnTent),
                ),
                SizedBox(width: 8.w),
                Expanded(
                  child: RichText(
                    text: TextSpan(
                      children: [
                        TextSpan(
                          text: "Camp Name : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: 16.sp,
                          ),
                        ),
                        TextSpan(
                          text: campDetails?.campName ?? "",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: 16.sp,
                          ),
                        ),
                      ],
                    ),
                    textAlign: TextAlign.start,
                    maxLines: null,
                    overflow: TextOverflow.visible,
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 8.h),
          SizedBox(
            width: SizeConfig.screenWidth,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  width: 24.w,
                  height: 24.h,
                  child: Image.asset(icMapPin),
                ),
                SizedBox(width: 8.w),
                Expanded(
                  child: RichText(
                    text: TextSpan(
                      children: [
                        TextSpan(
                          text: "Address : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: 16.sp,
                          ),
                        ),
                        TextSpan(
                          text: campDetails?.campLocation ?? "",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: 16.sp,
                          ),
                        ),
                      ],
                    ),
                    textAlign: TextAlign.start,
                    maxLines: null, // allow multiline
                    overflow: TextOverflow.visible,
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 8.h),
          Row(
            children: [
              Expanded(
                child: SizedBox(
                  width: SizeConfig.screenWidth,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(
                        width: 24.w,
                        height: 24.h,
                        child: Image.asset(icCalendarMonth),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: RichText(
                          text: TextSpan(
                            children: [
                              TextSpan(
                                text: "Camp Date : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: 16.sp,
                                ),
                              ),
                              TextSpan(
                                text: campDetails?.campDate ?? "",
                                style: TextStyle(
                                  color: dropDownTitleHeader,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 16.sp,
                                ),
                              ),
                            ],
                          ),
                          textAlign: TextAlign.start,
                          maxLines: null, // allow multiline
                          overflow: TextOverflow.visible,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SizedBox(width: 8.w),
              Expanded(
                child: SizedBox(
                  width: SizeConfig.screenWidth,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(
                        width: 24.w,
                        height: 24.h,
                        child: Image.asset(icCalendarMonth),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: RichText(
                          text: TextSpan(
                            children: [
                              TextSpan(
                                text: "Post Camp Date : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: 16.sp,
                                ),
                              ),
                              TextSpan(
                                text: campDetails?.campDate ?? "",
                                style: TextStyle(
                                  color: dropDownTitleHeader,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 16.sp,
                                ),
                              ),
                            ],
                          ),
                          textAlign: TextAlign.start,
                          maxLines: null, // allow multiline
                          overflow: TextOverflow.visible,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),

          SizedBox(height: 8.h),
          SizedBox(
            width: SizeConfig.screenWidth,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(
                  width: 24.w,
                  height: 24.h,
                  child: Image.asset(icInitiatedBy),
                ),
                SizedBox(width: 8.w),
                Expanded(
                  child: RichText(
                    text: TextSpan(
                      children: [
                        TextSpan(
                          text: "Expected Beneficiary : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: 16.sp,
                          ),
                        ),
                        TextSpan(
                          text:
                              campDetails?.expectedbeneficiarycount
                                  .toString() ??
                              "0",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: 16.sp,
                          ),
                        ),
                      ],
                    ),
                    textAlign: TextAlign.start,
                    maxLines: null, // allow multiline
                    overflow: TextOverflow.visible,
                  ),
                ),
              ],
            ),
          ),
          // const SizedBox(height: 8),
        ],
      ),
    );
  }
}
