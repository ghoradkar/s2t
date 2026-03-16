// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/widgets/S2TYesNoAlertView.dart';
import '../../../../../Modules/constants/fonts.dart';

class CampCalenderCampDetails extends StatelessWidget {
  int campId = 0;
  String dISTNAME = "";
  String surveyCoordinatorName = "";
  String mOBNO = "";

  CampCalenderCampDetails({
    super.key,
    required this.campId,
    required this.dISTNAME,
    required this.surveyCoordinatorName,
    required this.mOBNO,
  });

  Future<void> makePhoneCall(String phoneNumber) async {
    final Uri url = Uri(scheme: 'tel', path: phoneNumber);

    if (await canLaunchUrl(url)) {
      await launchUrl(url);
    } else {
      throw 'Could not launch $url';
    }
  }

  @override
  Widget build(BuildContext context) {
    void showYesNoAlertPopup(
      BuildContext parentContext,
      String icon,
      String title,
    ) {
      showModalBottomSheet(
        context: parentContext,
        isScrollControlled: true,
        backgroundColor: Colors.transparent,
        constraints: const BoxConstraints(minWidth: double.infinity),
        builder: (BuildContext sheetContext) {
          return Container(
            width: double.infinity,
            height: MediaQuery.of(sheetContext).size.height,
            color: Colors.transparent,
            child: S2TYesNoAlertView(
              icon: icon,
              message: title,
              onYesTap: () async {
                makePhoneCall(mOBNO);
              },
              onNoTap: () {
                Navigator.pop(context);
              },
            ),
          );
        },
      );
    }

    return Container(
      width: MediaQuery.of(context).size.width,
      decoration: BoxDecoration(
        color: Colors.white,
        // border: Border.all(color: Colors.white, width: 1),
        borderRadius: const BorderRadius.all(Radius.circular(20)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.16),
            blurRadius: 4,
            spreadRadius: 2,
          ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
           SizedBox(height: 8.h),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                width: 22.w,
                height: 22.h,
                child: Image.asset(icHashIcon),
              ),
               SizedBox(width: 8.w),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
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
                        "$campId",
                        style: TextStyle(
                          color: dropDownTitleHeader,
                          fontFamily: FontConstants.interFonts,
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
           SizedBox(height: 8.h),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                width: 22.w,
                height: 22.h,
                child: Image.asset(icMapPin),
              ),
               SizedBox(width: 8.w),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
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
                        dISTNAME,
                        style: TextStyle(
                          color: dropDownTitleHeader,
                          fontFamily: FontConstants.interFonts,
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
           SizedBox(height: 8.h),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                width: 22.w,
                height: 22.h,
                child: Image.asset(iconPerson),
              ),
               SizedBox(width: 8.w),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Camp Coordinator : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: 12.sp,
                      ),
                    ),
                    Expanded(
                      child: Text(
                        surveyCoordinatorName,
                        style: TextStyle(
                          color: dropDownTitleHeader,
                          fontFamily: FontConstants.interFonts,
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
           SizedBox(height: 8.h),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                width: 22.w,
                height: 22.h,
                child: Image.asset(icPhoneCallIcon),
              ),
               SizedBox(width: 8.w),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Contact Number : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: 12.sp,
                      ),
                    ),
                    Expanded(
                      child: GestureDetector(
                        onTap: () {
                          showYesNoAlertPopup(
                            context,
                            icTelephoneIcon,
                            "Do you really want to call $surveyCoordinatorName?",
                          );
                        },
                        child: Text(
                          mOBNO,
                          style: TextStyle(
                            color: phoneCallNoColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: 12.sp,
                          ),
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
      ).paddingSymmetric(vertical: 4.h, horizontal: 4.w),
    ).paddingOnly(top: 10.h, bottom: 6.h, left: 6.w, right: 6.w);
  }
}
