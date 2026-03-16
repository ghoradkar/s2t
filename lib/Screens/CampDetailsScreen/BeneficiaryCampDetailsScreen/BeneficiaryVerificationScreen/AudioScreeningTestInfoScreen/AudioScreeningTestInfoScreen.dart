// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/constants/images.dart';

class AudioScreeningTestInfoScreen extends StatefulWidget {
  AudioScreeningTestInfoScreen({
    super.key,
    required this.remark,
    required this.rightRemark,
  });

  String rightRemark = "";
  String remark = "";
  @override
  State<AudioScreeningTestInfoScreen> createState() =>
      _AudioScreeningTestInfoScreenState();
}

class _AudioScreeningTestInfoScreenState
    extends State<AudioScreeningTestInfoScreen> {
  bool isExpaneded = true;

  Color rightRemarkColor = Colors.black;
  Color leftRemarkColor = Colors.black;

  @override
  void initState() {
    super.initState();

    if (widget.rightRemark.toLowerCase() == "Deafness".toLowerCase()) {
      rightRemarkColor = Color(0xffFF474C);
    }
    if (widget.remark.toLowerCase() == "Deafness".toLowerCase()) {
      leftRemarkColor = Color(0xffFF474C);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding:  EdgeInsets.fromLTRB(0, 4.h, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(width: 1, color: droDownBGColor),
          borderRadius:
              isExpaneded == true
                  ? BorderRadius.only(
                    topLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                  )
                  : BorderRadius.all(Radius.circular(8)),
        ),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.fromLTRB(12.w, 3.h, 12.w, 3.h),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius:
                    isExpaneded == true
                        ? BorderRadius.only(
                          topLeft: Radius.circular(8),
                          topRight: Radius.circular(8),
                        )
                        : BorderRadius.all(Radius.circular(8)),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      "Audio Screening Test",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: 12.sp,
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 30.w,
                      height: 30.h,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // isExpaneded == true ? const SizedBox(height: 10) : Container(),
            isExpaneded == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                  child: Column(
                    children: [
                      Container(
                        height: 40.h,
                        decoration: BoxDecoration(
                          color: Colors.red,
                          // border: Border.all(width: 1, color: droDownBGColor),
                          // borderRadius: BorderRadius.only(
                          //   topLeft: Radius.circular(8),
                          //   topRight: Radius.circular(8),
                          // ),
                        ),
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                alignment: Alignment.centerLeft,
                                padding: EdgeInsets.fromLTRB(10.w, 0, 0, 0),
                                decoration: BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                child: Text(
                                  "Right Ear",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 12.sp,
                                  ),
                                ),
                              ),
                            ),
                            Expanded(
                              child: Container(
                                alignment: Alignment.centerLeft,
                                decoration: BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    right: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                child: Padding(
                                  padding:  EdgeInsets.fromLTRB(
                                    8.w,
                                    4.h,
                                    8.w,
                                    4.h,
                                  ),
                                  child: Container(
                                    decoration: BoxDecoration(
                                      color: Colors.white,
                                      border: Border.all(
                                        width: 0.5,
                                        color: Colors.grey,
                                      ),
                                      borderRadius: BorderRadius.circular(8),
                                    ),
                                    child: Center(
                                      child: Text(
                                        widget.rightRemark,
                                        textAlign: TextAlign.left,
                                        style: TextStyle(
                                          color: rightRemarkColor,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 12.sp,
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                )
                : Container(),

            // isExpaneded == true ? const SizedBox(height: 8) : Container(),
            isExpaneded == true
                ? Column(
                  children: [
                    Container(
                      height: 40.h,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        // border: Border.all(width: 1, color: droDownBGColor),
                        // borderRadius: BorderRadius.only(
                        //   bottomLeft: Radius.circular(8),
                        //   bottomRight: Radius.circular(8),
                        // ),
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: Container(
                              alignment: Alignment.centerLeft,
                              padding: EdgeInsets.fromLTRB(10.w, 0, 0, 0),
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.only(
                                  bottomLeft: Radius.circular(8),
                                ),
                                border: Border(
                                  left: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                  right: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                  bottom: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                ),
                              ),
                              child: Text(
                                "Left Ear",
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: 12.sp,
                                ),
                              ),
                            ),
                          ),

                          Expanded(
                            child: Container(
                              alignment: Alignment.centerLeft,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                // borderRadius: BorderRadius.only(
                                //   bottomRight: Radius.circular(8),
                                // ),
                                border: Border(
                                  // left: BorderSide(
                                  //   color: Colors.grey,
                                  //   width: 0.5,
                                  // ),
                                  right: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                  bottom: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                ),
                              ),
                              child: Padding(
                                padding:  EdgeInsets.fromLTRB(
                                  8.w,
                                  4.h,
                                  8.w,
                                  4.h,
                                ),
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.white,
                                    border: Border.all(
                                      width: 0.5,
                                      color: Colors.grey,
                                    ),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: Center(
                                    child: Text(
                                      widget.remark,
                                      textAlign: TextAlign.left,
                                      style: TextStyle(
                                        color: leftRemarkColor,
                                        fontFamily: FontConstants.interFonts,
                                        fontWeight: FontWeight.w500,
                                        fontSize: 12.sp,
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                )
                : Container(),
            isExpaneded == true ?  SizedBox(height: 4.h) : Container(),
          ],
        ),
      ),
    );
  }
}
