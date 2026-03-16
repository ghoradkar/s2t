// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/Json_Class/LungFunctionTestDetailsResponse/LungFunctionTestDetailsResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Modules/constants/fonts.dart';

class LungFunctionTestInfoScreen extends StatefulWidget {
  LungFunctionTestInfoScreen({super.key, required this.lungFunctionTest});

  LungFunctionTestDetailsOutput? lungFunctionTest;
  @override
  State<LungFunctionTestInfoScreen> createState() =>
      _LungFunctionTestInfoScreenState();
}

class _LungFunctionTestInfoScreenState
    extends State<LungFunctionTestInfoScreen> {
  bool isExpaneded = true;
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
                      "Lung Function Test",
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

            // isExpaneded == true ? const SizedBox(height: 20) : Container(),
            isExpaneded == true
                ? Column(
                  children: [
                    Container(
                      height: 40.h,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        // border: Border.all(width: 1, color: droDownBGColor),
                        // borderRadius: BorderRadius.only(
                        //   topLeft: Radius.circular(8),
                        //   topRight: Radius.circular(8),
                        // ),
                        border: Border(
                          left: BorderSide(color: Colors.grey, width: 0.5),
                          right: BorderSide(color: Colors.grey, width: 0.5),
                          bottom: BorderSide(color: Colors.grey, width: 0.5),
                        ),
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: Container(
                              alignment: Alignment.centerLeft,
                              padding: EdgeInsets.fromLTRB(10.w, 0, 0, 0),
                              decoration: BoxDecoration(
                                color: Color(0xffEFEDFD),
                                // borderRadius: BorderRadius.only(
                                //   topLeft: Radius.circular(8),
                                // ),
                              ),
                              child: Text(
                                "FEV1/FVC(%)",
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize:12.sp,
                                ),
                              ),
                            ),
                          ),

                          Expanded(
                            child: Container(
                              alignment: Alignment.centerLeft,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.only(
                                  topRight: Radius.circular(8),
                                ),
                                border: Border(
                                  left: BorderSide(
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
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: Center(
                                    child: Text(
                                      widget.lungFunctionTest?.fEVIFVC != null ? widget.lungFunctionTest!.fEVIFVC.toString() : "",
                                      textAlign: TextAlign.left,
                                      style: TextStyle(
                                        color: uploadBillTitleColor,
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

            // isExpaneded == true ? const SizedBox(height: 8) : Container(),
            isExpaneded == true
                ? Column(
                  children: [
                    Container(
                      height: 40,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        border: Border(
                          left: BorderSide(color: Colors.grey, width: 0.5),
                          right: BorderSide(color: Colors.grey, width: 0.5),
                          bottom: BorderSide(color: Colors.grey, width: 0.5),
                        ),
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
                                color: Color(0xffEFEDFD),
                                // borderRadius: BorderRadius.only(
                                //   bottomLeft: Radius.circular(8),
                                // ),
                                border: Border(
                                  right: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5,
                                  ),
                                ),
                              ),
                              child: Text(
                                "Investigation",
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
                                borderRadius: BorderRadius.only(
                                  bottomRight: Radius.circular(8),
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
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: Center(
                                    child: Text(
                                      widget.lungFunctionTest?.result ?? "",
                                      textAlign: TextAlign.left,
                                      style: TextStyle(
                                        color: uploadBillTitleColor,
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
