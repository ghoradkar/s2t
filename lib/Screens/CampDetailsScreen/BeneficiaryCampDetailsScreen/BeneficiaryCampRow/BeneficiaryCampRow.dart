// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../BeneficiaryVerificationScreen/BeneficiaryVerificationScreen.dart';

class BeneficiaryCampRow extends StatelessWidget {
  BeneficiaryCampRow({super.key, required this.index, required this.obj});

  int index = 0;
  BeneficiaryWorkerOutput obj;

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Stack(
        children: [
          Padding(
            padding: EdgeInsets.fromLTRB(6.w, 16.h, 0, 12.h),
            child: Container(
              width: SizeConfig.screenWidth,
              decoration: BoxDecoration(
                color: FormatterManager.getBeneficiaryCampRowColor(
                  obj.isApproved ?? 0,
                ),
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
                    child: Container(
                      color: Colors.white,
                      // padding: EdgeInsets.fromLTRB(6, 6, 0, 0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(height: 8.h),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              SizedBox(width: 8.h),

                              Image.asset(icHashIcon,width: 20.w,height: 20.h,),
                              SizedBox(width: 8.w),
                              Expanded(
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      "Unique Screening ID : ",
                                      style: TextStyle(
                                        color: Colors.black,
                                        fontFamily: FontConstants.interFonts,
                                        fontWeight: FontWeight.w500,
                                        fontSize: 14.sp,
                                      ),
                                    ),
                                    Expanded(
                                      child: Text(
                                        "${obj.screeningPatientID ?? ""}",
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
                          SizedBox(height: 8.h),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              SizedBox(width: 8.h),

                              Image.asset(icInitiatedBy,width: 20.w,height: 20.h,),
                              SizedBox(width: 8.w),
                              Expanded(
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      "Name : ",
                                      style: TextStyle(
                                        color: Colors.black,
                                        fontFamily: FontConstants.interFonts,
                                        fontWeight: FontWeight.w500,
                                        fontSize: 14.sp,
                                      ),
                                    ),
                                    Expanded(
                                      child: Text(
                                        obj.name ?? "",
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

                          SizedBox(height: 12.h),
                        ],
                      ).paddingOnly(top: 8.h),
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
                            GestureDetector(
                              onTap: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder:
                                        (context) =>
                                            BeneficiaryVerificationScreen(
                                              obj: obj,
                                            ),
                                  ),
                                );
                              },
                              child: Image.asset(icViewIcon,width: 24.w,height: 24.h,),
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
            bottom: 66,
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
                    fontSize:14.sp,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
