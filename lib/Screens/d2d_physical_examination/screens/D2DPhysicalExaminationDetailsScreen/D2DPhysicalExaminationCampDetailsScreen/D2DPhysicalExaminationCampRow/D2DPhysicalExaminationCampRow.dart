// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DTeamWisePhyExamDetailsResponse.dart';

class D2DPhysicalExaminationCampRow extends StatelessWidget {
  D2DPhysicalExaminationCampRow({
    super.key,
    required this.obj,
    required this.onAssignedDidPressed,
    required this.onTeamNoIDDidPressed,
    required this.onCallTap,
  });

  D2DTeamWisePhyExamDetailsOutput obj;
  Function() onTeamNoIDDidPressed;
  Function() onAssignedDidPressed;
  Function() onCallTap;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          height: 52.h,
          // decoration: BoxDecoration(
          //   color: kWhiteColor,
          // ),
          child: Row(
            children: [
              // Expanded(
              //   flex: 2,
              //   child: Container(
              //     padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
              //     decoration: BoxDecoration(
              //       color: Colors.white,
              //       border: Border(
              //         right: BorderSide(color: Colors.grey, width: 0.5),
              //         left: BorderSide(color: Colors.grey, width: 0.5),
              //         bottom: BorderSide(color: Colors.grey, width: 0.5),
              //       ),
              //     ),
              //     child: Center(
              //       child: Text(
              //         "Total",
              //         textAlign: TextAlign.center,
              //         style: TextStyle(
              //           color: Colors.black,
              //           fontFamily: FontConstants.interFonts,
              //           fontWeight: FontWeight.w700,
              //           fontSize: 14.sp,
              //         ),
              //       ),
              //     ),
              //   ),
              // ),
              Expanded(
                flex: 2,
                child: InkWell(
                  onTap: () {
                    onTeamNoIDDidPressed();
                  },
                  child: Container(
                    padding: EdgeInsets.symmetric(
                      vertical: 4.h,
                      horizontal: 4.w,
                    ),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      border: Border(
                        right: BorderSide(color: Colors.grey, width: 0.5),
                        bottom: BorderSide(color: Colors.grey, width: 0.5),
                        top: BorderSide(color: Colors.grey, width: 0.5),
                        left: BorderSide(color: Colors.grey, width: 0.5),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        obj.teamNo ?? "",
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.blue,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: 12.sp,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 2,
                child: InkWell(
                  onTap: () {
                    onAssignedDidPressed();
                  },
                  child: Container(
                    padding: EdgeInsets.symmetric(
                      vertical: 4.h,
                      horizontal: 4.w,
                    ),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      border: Border(
                        right: BorderSide(color: Colors.grey, width: 0.5),
                        bottom: BorderSide(color: Colors.grey, width: 0.5),
                        top: BorderSide(color: Colors.grey, width: 0.5),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        "${obj.assigned ?? 0}",
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.blue,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: 12.sp,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 2,
                child: Container(
                  padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border(
                      right: BorderSide(color: Colors.grey, width: 0.5),
                      bottom: BorderSide(color: Colors.grey, width: 0.5),
                      top: BorderSide(color: Colors.grey, width: 0.5),
                    ),
                  ),
                  child: Center(
                    child: Text(
                      "${obj.callingPending ?? 0}",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: 12.sp,
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 2,
                child: Container(
                  padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border(
                      right: BorderSide(color: Colors.grey, width: 0.5),
                      bottom: BorderSide(color: Colors.grey, width: 0.5),
                      top: BorderSide(color: Colors.grey, width: 0.5),
                    ),
                  ),
                  child: Center(
                    child: Text(
                      "${obj.phyExamPending ?? 0}",
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: 12.sp,
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 1,
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border(
                      right: BorderSide(color: Colors.grey, width: 0.5),
                      bottom: BorderSide(color: Colors.grey, width: 0.5),
                      top: BorderSide(color: Colors.grey, width: 0.5),
                    ),
                  ),
                  child: Center(
                    child: GestureDetector(
                      onTap: () {
                        onCallTap();
                        // launchUrl(Uri.parse('tel:${obj.campCoordinatorMobNo}'));
                      },
                      child: SizedBox(
                        width: 26.w,
                        height: 26.h,
                        child: Image.asset(icCallingExpected),
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
        // Row(
        //   mainAxisAlignment: MainAxisAlignment.start,
        //   crossAxisAlignment: CrossAxisAlignment.start,
        //   children: [
        //
        //
        //     // Expanded(
        //     //   child: Row(
        //     //     mainAxisAlignment: MainAxisAlignment.start,
        //     //     crossAxisAlignment: CrossAxisAlignment.start,
        //     //     children: [
        //     //       Text(
        //     //         "Team No : ",
        //     //         style: TextStyle(
        //     //           color: Colors.black,
        //     //           fontFamily: FontConstants.interFonts,
        //     //           fontWeight: FontWeight.w500,
        //     //           fontSize: responsiveFont(16),
        //     //         ),
        //     //       ),
        //     //       Expanded(
        //     //         child: GestureDetector(
        //     //           onTap: () {
        //     //             onTeamNoIDDidPressed();
        //     //           },
        //     //           child: Text(
        //     //             obj.teamNo ?? "",
        //     //             style: TextStyle(
        //     //               color: kPrimaryColor,
        //     //               fontFamily: FontConstants.interFonts,
        //     //               fontWeight: FontWeight.w700,
        //     //               fontSize: responsiveFont(16),
        //     //             ),
        //     //           ),
        //     //         ),
        //     //       ),
        //     //     ],
        //     //   ),
        //     // ),
        //   ],
        // ),
        // const SizedBox(height: 8),
        // Row(
        //   mainAxisAlignment: MainAxisAlignment.start,
        //   crossAxisAlignment: CrossAxisAlignment.start,
        //   children: [
        //     // SizedBox(
        //     //   width: responsiveHeight(30),
        //     //   height: responsiveHeight(30),
        //     //   child: Image.asset(icInitiatedBy),
        //     // ),
        //     // const SizedBox(width: 8),
        //     Expanded(
        //       child: Row(
        //         mainAxisAlignment: MainAxisAlignment.start,
        //         crossAxisAlignment: CrossAxisAlignment.start,
        //         children: [
        //           Text(
        //             "Assigned : ",
        //             style: TextStyle(
        //               color: Colors.black,
        //               fontFamily: FontConstants.interFonts,
        //               fontWeight: FontWeight.w500,
        //               fontSize: responsiveFont(16),
        //             ),
        //           ),
        //
        //           Expanded(
        //             child: GestureDetector(
        //               onTap: () {
        //                 onAssignedDidPressed();
        //               },
        //               child: Text(
        //                 "${obj.assigned ?? 0}",
        //                 style: TextStyle(
        //                   color: dropDownTitleHeader,
        //                   fontFamily: FontConstants.interFonts,
        //                   fontWeight: FontWeight.w700,
        //                   fontSize: responsiveFont(16),
        //                 ),
        //               ),
        //             ),
        //           ),
        //         ],
        //       ),
        //     ),
        //     Expanded(
        //       child: Row(
        //         mainAxisAlignment: MainAxisAlignment.start,
        //         crossAxisAlignment: CrossAxisAlignment.start,
        //         children: [
        //           Text(
        //             "Calling Pending : ",
        //             style: TextStyle(
        //               color: Colors.black,
        //               fontFamily: FontConstants.interFonts,
        //               fontWeight: FontWeight.w500,
        //               fontSize: responsiveFont(16),
        //             ),
        //           ),
        //
        //           Expanded(
        //             child: Text(
        //               "${obj.callingPending ?? 0}",
        //               style: TextStyle(
        //                 color: dropDownTitleHeader,
        //                 fontFamily: FontConstants.interFonts,
        //                 fontWeight: FontWeight.w400,
        //                 fontSize: responsiveFont(16),
        //               ),
        //             ),
        //           ),
        //         ],
        //       ),
        //     ),
        //   ],
        // ),
        // const SizedBox(height: 8),
        //
        // Row(
        //   mainAxisAlignment: MainAxisAlignment.start,
        //   crossAxisAlignment: CrossAxisAlignment.start,
        //   children: [
        //     Expanded(
        //       child: Row(
        //         mainAxisAlignment: MainAxisAlignment.start,
        //         crossAxisAlignment: CrossAxisAlignment.start,
        //         children: [
        //           Text(
        //             "Physical Exam Pending : ",
        //             style: TextStyle(
        //               color: Colors.black,
        //               fontFamily: FontConstants.interFonts,
        //               fontWeight: FontWeight.w500,
        //               fontSize: responsiveFont(16),
        //             ),
        //           ),
        //
        //           Expanded(
        //             child: Text(
        //               "${obj.phyExamPending ?? 0}",
        //               style: TextStyle(
        //                 color: dropDownTitleHeader,
        //                 fontFamily: FontConstants.interFonts,
        //                 fontWeight: FontWeight.w400,
        //                 fontSize: responsiveFont(16),
        //               ),
        //             ),
        //           ),
        //         ],
        //       ),
        //     ),
        //   ],
        // ),
      ],
    );
  }
}


