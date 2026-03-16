// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/D2DPhysicalExamninationDetailsResponse/D2DPhysicalExamninationDetailsResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/WidgetPaddingX.dart';

class BasicHealthInfo extends StatefulWidget {
  BasicHealthInfo({super.key, required this.patientObj});

  D2DPhysicalExamninationDetailsOutput? patientObj;

  @override
  State<BasicHealthInfo> createState() => _BasicHealthInfoState();
}

class _BasicHealthInfoState extends State<BasicHealthInfo> {
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
                      "Basic Health Info",
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
                  child: Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.heightCMs.toString() ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Height (cms)',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icHeightIcon,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icHeightIcon,
                        //   titleHeaderString: "Height (cms)",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.heightCMs ?? ""}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text: widget.patientObj?.weightKGs.toString() ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Weight (kg)',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icWeightIcon,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icWeightIcon,
                        //   titleHeaderString: "Weight (kg)",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.weightKGs ?? ""}",
                        //   ),
                        // ),
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
                            text: widget.patientObj?.bMI.toString() ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'BMI',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icBMI,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icBMI,
                        //   titleHeaderString: "BMI",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.bMI ?? ""}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: Expanded(
                          child: AppTextField(
                            onTap: () {},
                            controller: TextEditingController(
                              text:
                                  widget.patientObj?.bMIStatus.toString() ?? '',
                            ),
                            readOnly: true,
                            label: RichText(
                              text: TextSpan(
                                text: 'BMI Status',
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontSize: 14.sp * 1.33,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                            ),
                            prefixIcon: Image.asset(
                              icBMI,
                              color: kPrimaryColor,
                              width: 22.w,
                              height: 22.h,
                            ).paddingOnly(left: 6.w),
                          ),
                          // AppIconTextfield(
                          //   icon: icBMI,
                          //   titleHeaderString: "BMI",
                          //   isDisabled: true,
                          //   controller: TextEditingController(
                          //     text: "${widget.patientObj?.bMI ?? ""}",
                          //   ),
                          // ),
                        ),

                        // AppIconTextfield(
                        //   icon: icWeightIcon,
                        //   titleHeaderString: "Category",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: widget.patientObj?.bMIStatus ?? "",
                        //   ),
                        // ),
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
                            text: widget.patientObj?.bloodGroup ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Blood Group',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icBloodGroup,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icBloodGroup,
                        //   titleHeaderString: "Blood Group",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: widget.patientObj?.bloodGroup ?? "",
                        //   ),
                        // ),
                      ),

                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                                widget.patientObj?.bloodSugarR.toString() ?? '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Blood Sugar',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icMapPin,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icMapPin,
                        //   titleHeaderString: "No. of Children",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.noOfChildren ?? ""}",
                        //   ),
                        // ),
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
                            text:
                                widget.patientObj?.systolic != null
                                    ? widget.patientObj?.systolic.toString()
                                    : '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Systolic',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icBloodGroup,
                            color: kPrimaryColor,
                            width: 22.w,
                            height: 22.h,
                          ).paddingOnly(left: 6.w),
                        ),
                        // AppIconTextfield(
                        //   icon: icBloodGroup,
                        //   titleHeaderString: "Blood Group",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: widget.patientObj?.bloodGroup ?? "",
                        //   ),
                        // ),
                      ),

                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                                widget.patientObj?.diastolic != null
                                    ? widget.patientObj?.diastolic.toString()
                                    : '',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'diastolic',
                              style: TextStyle(
                                color: kBlackColor,
                                fontSize: 14.sp * 1.33,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icBloodGroup,
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
                ? AppTextField(
                  onTap: () {},
                  controller: TextEditingController(
                    text:
                        widget.patientObj?.fastingHrs != null
                            ? widget.patientObj?.fastingHrs.toString()
                            : '',
                  ),
                  readOnly: true,
                  inputStyle: TextStyle(color: noteRedColor),
                  label: RichText(
                    text: TextSpan(
                      text: 'Duration between last meal and registration',
                      style: TextStyle(
                        color: kBlackColor,
                        fontSize: 14.sp * 1.33,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                  prefixIcon: Image.asset(
                    icBloodGroup,
                    color: kPrimaryColor,
                    width: 22.w,
                    height: 22.h,
                  ).paddingOnly(left: 6.w),
                ).paddingOnly(right: 10.w, left: 10.w, bottom: 8.h)
                : Container(),
          ],
        ),
      ),
    );
  }
}
