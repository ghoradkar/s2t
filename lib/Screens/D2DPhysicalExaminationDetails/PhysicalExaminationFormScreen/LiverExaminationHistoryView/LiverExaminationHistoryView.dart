// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamninationDetailsResponse/D2DPhysicalExamninationDetailsResponse.dart';
import 'package:s2toperational/Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import 'LiverExaminationHistoryCard/LiverExaminationHistoryCard.dart';

class LiverExaminationHistoryView extends StatefulWidget {
  final PhysicalExaminationFormDataManager physicalExaminationFormDataManager;
  final D2DPhysicalExamninationDetailsOutput? patientObj;

  const LiverExaminationHistoryView({
    super.key,
    required this.physicalExaminationFormDataManager,
    this.patientObj,
  });

  @override
  State<LiverExaminationHistoryView> createState() =>
      _LiverExaminationHistoryViewState();
}

class _LiverExaminationHistoryViewState
    extends State<LiverExaminationHistoryView> {
  bool isExpaneded = true;

  // PhysicalExaminationFormDataManager physicalExaminationFormDataManager =
  //     PhysicalExaminationFormDataManager();

  void checkIsNormalOrAbnormal() {
    if (widget.physicalExaminationFormDataManager.isPalpableLiver == true ||
        widget.physicalExaminationFormDataManager.isHistoryOfChronic == true ||
        widget.physicalExaminationFormDataManager.isLiverRelatedAilments ==
            true ||
        widget.physicalExaminationFormDataManager.isPresenceOfDyslipidemia ==
            true ||
        widget.physicalExaminationFormDataManager.isPresenceOfDiabetes ==
            true ||
        widget.physicalExaminationFormDataManager.isPresenceOfHypertension ==
            true ||
        widget.physicalExaminationFormDataManager.isElevatedALTs == true) {
      widget.physicalExaminationFormDataManager.isNormal = false;
    } else {
      widget.physicalExaminationFormDataManager.isNormal = true;
    }
  }

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
                      "Liver Examination / History *",
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
            isExpaneded == true ? SizedBox(height: 8.h) : Container(),
            isExpaneded == true
                ? Container(
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    border: Border.all(
                      color: kTextColor.withValues(alpha: 0.2),
                    ),
                    borderRadius: BorderRadius.all(Radius.circular(12)),
                  ),
                  padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Expanded(
                            child: FormatterManager.buildLabelWithAsterisk(
                              "1. Is beneficiary in fasting state (3 hours or more)",
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 10.h),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [

                          EnableDisableButton(
                            buttontitle: 'Yes',
                            onTap: () {
                              widget
                                  .physicalExaminationFormDataManager
                                  .isFastingState = true;

                              setState(() {});
                            },
                            isYesButton: "Yes",
                            isButtonSelected:
                                widget
                                            .physicalExaminationFormDataManager
                                            .isFastingState ==
                                        true
                                    ? true
                                    : false, width: 120.w,
                          ),

                          // SizedBox(width: 4.w),
                          EnableDisableButton(
                            buttontitle: 'No',
                            onTap: () {
                              widget
                                  .physicalExaminationFormDataManager
                                  .isFastingState = false;
                              setState(() {});
                            },
                            isYesButton: "No",
                            isButtonSelected:
                                widget
                                            .physicalExaminationFormDataManager
                                            .isFastingState ==
                                        false
                                    ? true
                                    : false, width: 120.w,
                          ),

                        ],
                      ),
                    ],
                  ),
                ).paddingOnly(top: 4, bottom: 4)
                : Container(),

            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPalpableLiverTextField,
                  titleString:
                      "2.Liver related signs & symptoms (Palpable Liver, Right hypochondriac pain/Abdominal Discomfort etc.)",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPalpableLiverTextField
                          .clear();
                    }
                    widget.physicalExaminationFormDataManager.isPalpableLiver =
                        p0;

                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsPalpableLiver = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsPalpableLiver = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionPalpableLiverTextField
                        .text = p0;
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionHistoryOfChronicTextField,
                  titleString:
                      "3.History of chronic consumption of NSAID/Alcohol",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionHistoryOfChronicTextField
                          .clear();
                    }
                    widget
                        .physicalExaminationFormDataManager
                        .isHistoryOfChronic = p0;
                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsHistoryOfChronic = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsHistoryOfChronic = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionHistoryOfChronicTextField
                        .text = p0;
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionLiverRelatedAilmentsTextField,
                  titleString:
                      "4.History of Liver related ailments (Viral and autoimmune hepatitis, PBS etc)",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionLiverRelatedAilmentsTextField
                          .clear();
                    }
                    widget
                        .physicalExaminationFormDataManager
                        .isLiverRelatedAilments = p0;
                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsLiverRelatedAilments = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsLiverRelatedAilments = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionLiverRelatedAilmentsTextField
                        .text = p0;
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfDyslipidemiaTextField,
                  titleString:
                      "5.Presence of Dyslipidemia (Metabolic Syndrome Component)(Elevated TG, low HDL)",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfDyslipidemiaTextField
                          .clear();
                    }
                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfDyslipidemia = p0;
                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfDyslipidemiaUnknown = false;
                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsPresenceOfDyslipidemia = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsPresenceOfDyslipidemia = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionPresenceOfDyslipidemiaTextField
                        .text = p0;
                    setState(() {});
                  },
                  isUnknown: true,
                  selectedUnknown: (val) {
                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfDyslipidemiaUnknown = val;
                    debugPrint(
                      widget
                          .physicalExaminationFormDataManager
                          .isPresenceOfDyslipidemiaUnknown
                          .toString(),
                    );
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfDiabetesTextField,
                  titleString:
                      "6.Presence of Diabetes Mellitus (Metabolic Syndrome Component)",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfDiabetesTextField
                          .clear();
                    }
                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfDiabetes = p0;
                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsPresenceOfDiabetes = p0;
                    setState(() {});
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsPresenceOfDiabetes = p0;
                    setState(() {});
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionPresenceOfDiabetesTextField
                        .text = p0;
                    setState(() {});
                  },
                  isUnknown: true,
                  selectedUnknown: (val) {
                    // physicalExaminationFormDataManager
                    //     .descriptionPresenceOfDiabetesTextField
                    //     .clear();
                    // physicalExaminationFormDataManager
                    //     .selectedMonthsPresenceOfDiabetes = null;
                    // physicalExaminationFormDataManager
                    //     .selectedYearsPresenceOfDiabetes = null;

                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfDiabetesUnknown = val;
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfHypertensionTextField,
                  titleString:
                      "7.Presence of Hypertension (Metabolic Syndrome Component",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionPresenceOfHypertensionTextField
                          .clear();
                    }

                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfHypertension = p0;

                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsPresenceOfHypertension = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsPresenceOfHypertension = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionPresenceOfHypertensionTextField
                        .text = p0;
                    setState(() {});
                  },
                  isUnknown: true,
                  selectedUnknown: (val) {
                    widget
                        .physicalExaminationFormDataManager
                        .isPresenceOfHypertensionUnknown = val;
                    setState(() {});
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCard(
                  descriptionController:
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionElevatedALTsTextField,
                  titleString: "8.Elevated ALT Levels",
                  selectedRadioYesChange: (p0) {
                    if (!p0) {
                      // Clear when "No" is selected
                      widget
                          .physicalExaminationFormDataManager
                          .descriptionElevatedALTsTextField
                          .clear();
                    }
                    widget.physicalExaminationFormDataManager.isElevatedALTs =
                        p0;
                    checkIsNormalOrAbnormal();
                    setState(() {});
                  },
                  selectedYearChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedYearsElevatedALTs = p0;
                  },
                  selectedMonthChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .selectedMonthsElevatedALTs = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    widget
                        .physicalExaminationFormDataManager
                        .descriptionElevatedALTsTextField
                        .text = p0;
                    setState(() {});
                  },
                  isUnknown: true,
                  selectedUnknown: (val) {
                    widget
                        .physicalExaminationFormDataManager
                        .isElevatedALTsUnknown = val;
                  },
                )
                : Container(),
            isExpaneded == true
                ? LiverExaminationHistoryCardDefaultSelected(
                  patientObj: widget.patientObj,
                  titleString: "9.Is BMI >/=23 Kg/m2 (Overweight or obesity)",
                  selectedRadioYesChange:
                      widget.physicalExaminationFormDataManager.isBMI!,
                  value: 'BMI',
                ).paddingSymmetric(vertical: 6)
                : Container(),
            // Align(
            //   alignment: Alignment.centerLeft,
            //   child: Text(
            //     "BMI : ${widget.patientObj?.bMI}",
            //     style: TextStyle(color: kPrimaryColor),
            //     textAlign: TextAlign.left,
            //   ).paddingOnly(left: 6.w, top: 2.h, bottom: 2.h),
            // ),
            isExpaneded == true
                ? LiverExaminationHistoryCardDefaultSelected(
                  patientObj: widget.patientObj,

                  titleString: "10.Elevated Blood Glucose Levels (200mg/dl)",
                  selectedRadioYesChange:
                      widget
                          .physicalExaminationFormDataManager
                          .isElevatedBloodGlucose!,
                  value: 'Blood Glucose',
                ).paddingSymmetric(vertical: 6)
                : Container(),
            isExpaneded == true ? SizedBox(height: 10.h) : Container(),
            // Align(
            //   alignment: Alignment.centerLeft,
            //   child: Text(
            //     "Blood Glucose : ${widget.patientObj?.bloodSugarR}",
            //     style: TextStyle(color: kPrimaryColor),
            //     textAlign: TextAlign.left,
            //   ).paddingOnly(left: 6.w, bottom: 2.h),
            // ),
            isExpaneded == true
                ? Padding(
                  padding: EdgeInsets.symmetric(
                    vertical: 10.h,
                    horizontal: 8.w,
                  ),
                  child: SizedBox(
                    height: 40.h,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        EnableDisableButton(
                          buttontitle: 'Normal',
                          onTap: () {},
                          isYesButton: "Normal",
                          isButtonSelected:
                              widget
                                          .physicalExaminationFormDataManager
                                          .isNormal ==
                                      true
                                  ? true
                                  : false, width: 120.w,
                        ),
                        // Expanded(
                        //   child: Row(
                        //     children: [
                        //       SizedBox(
                        //         width: 26.w,
                        //         height: 26.h,
                        //         child: Image.asset(
                        //           widget
                        //                       .physicalExaminationFormDataManager
                        //                       .isNormal ==
                        //                   true
                        //               ? icRadioSelected
                        //               : icUnRadioSelected,
                        //         ),
                        //       ),
                        //       SizedBox(width: 8.w),
                        //       Text(
                        //         "Normal",
                        //         style: TextStyle(
                        //           color: kBlackColor,
                        //           fontFamily: FontConstants.interFonts,
                        //           fontWeight: FontWeight.w400,
                        //           fontSize: 14.sp,
                        //         ),
                        //       ),
                        //     ],
                        //   ),
                        // ),
                        SizedBox(width: 8.w),
                        EnableDisableButton(
                          buttontitle: 'Abnormal',
                          onTap: () {},
                          isYesButton: "Abnormal",
                          isButtonSelected:
                              widget
                                          .physicalExaminationFormDataManager
                                          .isNormal ==
                                      false
                                  ? true
                                  : false, width: 120.w,
                        ),

                        // Expanded(
                        //   child: Row(
                        //     children: [
                        //       SizedBox(
                        //         width: 26.w,
                        //         height: 26.h,
                        //         child: Image.asset(
                        //           widget
                        //                       .physicalExaminationFormDataManager
                        //                       .isNormal ==
                        //                   false
                        //               ? icRadioSelected
                        //               : icUnRadioSelected,
                        //         ),
                        //       ),
                        //       SizedBox(width: 8.w),
                        //       Text(
                        //         "Abnormal",
                        //         style: TextStyle(
                        //           color: kBlackColor,
                        //           fontFamily: FontConstants.interFonts,
                        //           fontWeight: FontWeight.w400,
                        //           fontSize: 14.sp,
                        //         ),
                        //       ),
                        //     ],
                        //   ),
                        // ),
                      ],
                    ),
                  ),
                )
                : SizedBox(),
          ],
        ),
      ),
    );
  }

  // void resetFields() {
  //   physicalExaminationFormDataManager.isPalpableLiver = false;
  //   physicalExaminationFormDataManager.descriptionPalpableLiverTextField
  //       .clear();
  //   physicalExaminationFormDataManager.isHistoryOfChronic = false;
  //  physicalExaminationFormDataManager
  //       .descriptionHistoryOfChronicTextField
  //       .clear();
  //
  //   physicalExaminationFormDataManager.isLiverRelatedAilments = false;
  //   physicalExaminationFormDataManager
  //       .descriptionLiverRelatedAilmentsTextField
  //       .clear();
  //
  //   physicalExaminationFormDataManager.isPresenceOfDyslipidemia = false;
  //   physicalExaminationFormDataManager.isPresenceOfDyslipidemiaUnknown =
  //       false;
  //  physicalExaminationFormDataManager
  //       .descriptionPresenceOfDyslipidemiaTextField
  //       .clear();
  //
  //   physicalExaminationFormDataManager.isPresenceOfDiabetes = false;
  //   physicalExaminationFormDataManager.isPresenceOfDiabetesUnknown =
  //       false;
  //   physicalExaminationFormDataManager
  //       .descriptionPresenceOfDiabetesTextField
  //       .clear();
  //
  //   physicalExaminationFormDataManager.isPresenceOfHypertension = false;
  //   physicalExaminationFormDataManager.isPresenceOfHypertensionUnknown =
  //       false;
  //   physicalExaminationFormDataManager
  //       .descriptionPresenceOfHypertensionTextField
  //       .clear();
  //
  //   physicalExaminationFormDataManager.isElevatedALTs = false;
  //   physicalExaminationFormDataManager.isElevatedALTsUnknown = false;
  //   physicalExaminationFormDataManager.descriptionElevatedALTsTextField
  //       .clear();
  // }
}
