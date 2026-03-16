// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../constants/images.dart';
import '../utilities/SizeConfig.dart';

class AppActiveButton extends StatelessWidget {
  AppActiveButton({
    super.key,
    required this.buttontitle,
    required this.onTap,
    this.isCancel = false,
  });

  Function() onTap;
  String buttontitle;
  bool isCancel = false;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        onTap();
      },
      child: Container(
        width: MediaQuery.of(context).size.width,
        height: 50.h,
        decoration: BoxDecoration(
          color: isCancel == false ? kPrimaryColor : buttonCancel,
          borderRadius: BorderRadius.circular(8),
        ),
        padding: EdgeInsets.fromLTRB(12.w, 0, 12.w, 0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              buttontitle,
              style: TextStyle(
                color: isCancel == false ? kWhiteColor : kPrimaryColor,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(14),
              ),
            ),
            // const Spacer(),
            SizedBox(
              width: 22.w,
              height: 22.h,
              child: Image.asset(
                icActiveArrowRight,
                color: isCancel == false ? kWhiteColor : kPrimaryColor,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class EnableDisableButton extends StatelessWidget {
  final Function() onTap;
  final String buttontitle;
  final bool? isButtonSelected;
  final String isYesButton;
  final double width;

  const EnableDisableButton({
    super.key,
    required this.buttontitle,
    required this.onTap,
    this.isButtonSelected,
    required this.isYesButton,
    required this.width,
  });

  getColor() {
    if (isButtonSelected == true &&
        (isYesButton == "Yes" || isYesButton == "Normal")) {
      return kPrimaryColor;
    } else if (isButtonSelected == true &&
        (isYesButton == "No" || isYesButton == "Abnormal")) {
      return noteRedColor;
    } else if (isButtonSelected == true && isYesButton == "Unknown") {
      return kLiverBackGroundColor;
    } else {
      return kBackground;
    }
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        onTap();
      },
      child: Container(
        width: width,
        height: 40.h,
        decoration: BoxDecoration(
          color: getColor(),
          // color: isButtonSelected == false ? kBackground : noteRedColor,
          borderRadius: BorderRadius.circular(8),
        ),
        // padding: EdgeInsets.fromLTRB(4.w, 0, 4.w, 0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            isYesButton == "Yes"
                ? Icon(
                  Icons.check,
                  color:
                      isButtonSelected == false ? kLabelTextColor : kWhiteColor,
                  size: 20,
                )
                : Image.asset(
                  cancel,
                  color:
                      isButtonSelected == false ? kLabelTextColor : kWhiteColor,
                  width: 20,
                  height: 20,
                ),

            Text(
              buttontitle,
              style: TextStyle(
                color:
                    isButtonSelected == false ? kLabelTextColor : kWhiteColor,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: 14.sp,
              ),
            ).paddingOnly(left: 6.w),
            // const Spacer(),
          ],
        ),
      ).paddingSymmetric(horizontal: 6.w),
    );
  }
}
