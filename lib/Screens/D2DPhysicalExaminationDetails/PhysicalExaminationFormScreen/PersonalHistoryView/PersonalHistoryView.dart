// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/D2DPhysicalExamninationDetailsResponse/D2DPhysicalExamninationDetailsResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/utilities/WidgetPaddingX.dart';

class PersonalHistoryView extends StatefulWidget {
  PersonalHistoryView({super.key, required this.patientObj});

  D2DPhysicalExamninationDetailsOutput? patientObj;

  @override
  State<PersonalHistoryView> createState() => _PersonalHistoryViewState();
}

class _PersonalHistoryViewState extends State<PersonalHistoryView> {
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
                      "Personal History",
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
                      text: widget.patientObj?.isSmoking ?? 'NA',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Smoking',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: responsiveFont(14),
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icInitiatedBy,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6),
                  ),

                  // AppIconTextfield(
                  //   icon: icInitiatedBy,
                  //   titleHeaderString: "Smoking",
                  //   isDisabled: true,
                  //   controller: TextEditingController(
                  //     text: widget.patientObj?.isSmoking ?? "No",
                  //   ),
                  // ),
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
                                widget.patientObj?.smokingSinceYear
                                    .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Year',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        ),

                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Year",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.smokingSinceYear ?? "0"}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                                widget.patientObj?.smokingSinceMonth
                                    .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Month',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        ),
                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Month",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.smokingSinceMonth ?? "0"}",
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
                  child: AppTextField(
                    onTap: () {},
                    controller: TextEditingController(
                      text: widget.patientObj?.isAlcohol ?? 'No',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Alcohol',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: responsiveFont(14),
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icInitiatedBy,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6),
                  ),
                  // AppIconTextfield(
                  //   icon: icInitiatedBy,
                  //   titleHeaderString: "Alcohol",
                  //   isDisabled: true,
                  //   controller: TextEditingController(
                  //     text: widget.patientObj?.isAlcohol ?? "No",
                  //   ),
                  // ),
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
                                widget.patientObj?.alcoholSinceYear
                                    .toString() ??
                                'No',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Year',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        ),
                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Year",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.alcoholSinceYear ?? "0"}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                                widget.patientObj?.alcoholSinceMonth
                                    .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Month',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        ),

                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Month",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.alcoholSinceMonth ?? "0"}",
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
                  child: AppTextField(
                    onTap: () {},
                    controller: TextEditingController(
                      text:
                      widget.patientObj?.isTobaco
                          .toString() ??
                          'NO',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Tobacco',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: responsiveFont(14),
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icInitiatedBy,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6),
                  )
                  // AppIconTextfield(
                  //   icon: icInitiatedBy,
                  //   titleHeaderString: "Tobacco",
                  //   isDisabled: true,
                  //   controller: TextEditingController(
                  //     text: widget.patientObj?.isTobaco ?? "No",
                  //   ),
                  // ),
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
                            widget.patientObj?.tobacoSinceYear
                                .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Year',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        )
                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Year",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.tobacoSinceYear ?? "0"}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                            widget.patientObj?.tobacoSinceMonth
                                .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Month',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        )
                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Month",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text:
                        //         "${widget.patientObj?.tobacoSinceMonth ?? "0"}",
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
                  child: AppTextField(
                    onTap: () {},
                    controller: TextEditingController(
                      text:
                      widget.patientObj?.isDrug
                          .toString() ??
                          '0',
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Drugs',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: responsiveFont(14),
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icInitiatedBy,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6),
                  )
                  // AppIconTextfield(
                  //   icon: icInitiatedBy,
                  //   titleHeaderString: "Drugs",
                  //   isDisabled: true,
                  //   controller: TextEditingController(
                  //     text: widget.patientObj?.isDrug ?? "No",
                  //   ),
                  // ),
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
                            widget.patientObj?.drugSinceYear
                                .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Year',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        )

                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Year",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.drugSinceYear ?? "0"}",
                        //   ),
                        // ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          onTap: () {},
                          controller: TextEditingController(
                            text:
                            widget.patientObj?.drugSinceMonth
                                .toString() ??
                                '0',
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Month',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icCalendarMonth,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6),
                        )

                        // AppIconTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Month",
                        //   isDisabled: true,
                        //   controller: TextEditingController(
                        //     text: "${widget.patientObj?.drugSinceMonth ?? "0"}",
                        //   ),
                        // ),
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
