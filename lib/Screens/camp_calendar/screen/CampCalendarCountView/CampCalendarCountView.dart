// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';


class CampCalendarCountView extends StatelessWidget {
  CampCalendarCountView({
    super.key,
    required this.titleString,
    required this.countValue,
    required this.showTop,
    required this.showBottom,
    this.textCountColor = Colors.black,
  });

  String titleString;
  String countValue;
  bool showTop = true;
  bool showBottom = false;
  Color textCountColor = Colors.black;

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                border: Border(
                  left: BorderSide(color: Colors.grey, width: 0.5),
                  bottom:
                      showBottom == true
                          ? BorderSide(color: Colors.grey, width: 0.5)
                          : BorderSide(color: Colors.grey, width: 0.0),
                  top:
                      showTop == true
                          ? BorderSide(color: Colors.grey, width: 0.5)
                          : BorderSide(color: Colors.grey, width: 0.0),
                ),
              ),
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
                child: Text(
                  titleString,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 12.sp,
                    color: kBlackColor,
                    fontWeight: FontWeight.w400,
                  ),
                ),
              ),
            ),
          ),
          Container(
            width: 130.w,

            decoration: BoxDecoration(
              color: Colors.white,
              border: Border(
                left: BorderSide(color: Colors.grey, width: 0.5),
                right: BorderSide(color: Colors.grey, width: 0.5),
                bottom:
                    showBottom == true
                        ? BorderSide(color: Colors.grey, width: 0.5)
                        : BorderSide(color: Colors.grey, width: 0.0),
                top:
                    showTop == true
                        ? BorderSide(color: Colors.grey, width: 0.5)
                        : BorderSide(color: Colors.grey, width: 0.0),
              ),
            ),
            child: Center(
              child: Text(
                countValue,
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  color: textCountColor,
                  fontWeight: FontWeight.w700,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
