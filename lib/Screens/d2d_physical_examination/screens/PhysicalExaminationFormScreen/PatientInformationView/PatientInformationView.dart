// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/Model/D2DPhysicalExamninationDetailsResponse.dart';

class PatientInformationView extends StatefulWidget {
  PatientInformationView({super.key, required this.patientObj});

  D2DPhysicalExamninationDetailsOutput? patientObj;

  @override
  State<PatientInformationView> createState() => _PatientInformationViewState();
}

class _PatientInformationViewState extends State<PatientInformationView> {
  bool isExpaneded = true;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(top: 10.h),
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
              padding: EdgeInsets.fromLTRB(12.w, 4.h, 12.w, 4.h),
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
                      "Patient Information",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: 16.sp,
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 26.w,
                      height: 26.h,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            isExpaneded == true ? SizedBox(height: 10.h) : Container(),
            isExpaneded == true
                ? Padding(
                  padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 8.h),
                  child: AppTextField(
                    onTap: () {},
                    controller: TextEditingController(
                      text:
                          widget.patientObj?.beneficiaryName?.toUpperCase() ??
                          '',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Profile Name',
                        style: TextStyle(
                          color: kBlackColor,
                          fontSize: 12 * 1.33,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icInitiatedBy,
                      color: kPrimaryColor,
                      width: 22.w,
                      height: 22.h,
                    ).paddingOnly(left: 6.w),
                  ),
                )
                : Container(),

            isExpaneded == true
                ? Padding(
                  padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 8.h),
                  child: AppTextField(
                    onTap: () {},
                    controller: TextEditingController(
                      text:
                          widget.patientObj?.beneficiaryRegdNo.toString() ?? '',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Beneficiary Number',
                        style: TextStyle(
                          color: kBlackColor,
                          fontWeight: FontWeight.w600,
                          fontSize: 14.sp * 1.33,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icHashIcon,
                      color: kPrimaryColor,
                      width: 22.w,
                      height: 22.h,
                    ).paddingOnly(left: 6.w),
                  ),
                )
                : Container(),
            isExpaneded == true
                ? Padding(
                  padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 8.h),
                  child: Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.gender ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Gender',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icGenderIcon,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.age.toString() ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Age',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                      ),
                    ],
                  ),
                )
                : Container(),

            isExpaneded == true
                ? Padding(
                  padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 8.h),
                  child: Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.mobNo ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Mobile No.',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            iconMobile,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.maritalStatus ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Marital Status',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            iconPerson,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                      ),
                    ],
                  ),
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}
