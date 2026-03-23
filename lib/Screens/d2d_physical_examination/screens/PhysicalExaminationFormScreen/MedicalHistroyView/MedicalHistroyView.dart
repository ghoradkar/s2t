// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/Model/D2DPhysicalExamninationDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/PhysicalExaminationFormScreen/MedicalHistroyView/MedicalHistoryCard.dart';
import '../../../../../Modules/constants/fonts.dart';

class MedicalHistroyView extends StatefulWidget {
  MedicalHistroyView({super.key, required this.patientObj});

  D2DPhysicalExamninationDetailsOutput? patientObj;

  @override
  State<MedicalHistroyView> createState() => _MedicalHistroyViewState();
}

class _MedicalHistroyViewState extends State<MedicalHistroyView> {
  bool isExpaneded = true;
  PhysicalExaminationFormDataManager physicalExaminationFormDataManager =
      PhysicalExaminationFormDataManager();

  void setNoHistory() {
    physicalExaminationFormDataManager.isNoHistory = false;
    physicalExaminationFormDataManager.isAsthma = false;
    physicalExaminationFormDataManager.iskidenyDisease = false;
    physicalExaminationFormDataManager.isDiabetes = false;
    physicalExaminationFormDataManager.isCancer = false;
    physicalExaminationFormDataManager.isGynecologicalDisorder = false;
    physicalExaminationFormDataManager.isThyroid = false;
    physicalExaminationFormDataManager.isNeurologicalDisease = false;
    physicalExaminationFormDataManager.isOther = false;

    physicalExaminationFormDataManager.selectedYearsAsthma = null;
    physicalExaminationFormDataManager.selectedMonthsAsthma = null;

    physicalExaminationFormDataManager.descriptionAsthmaTextField.text = "";

    physicalExaminationFormDataManager.selectedYearskidenyDisease = null;
    physicalExaminationFormDataManager.selectedMonthskidenyDisease = null;

    physicalExaminationFormDataManager.descriptionkidenyDiseaseTextField.text =
        "";

    physicalExaminationFormDataManager.selectedYearsDiabetes = null;
    physicalExaminationFormDataManager.selectedMonthsDiabetes = null;

    physicalExaminationFormDataManager.descriptionDiabetesTextField.text = "";

    physicalExaminationFormDataManager.selectedYearsCancer = null;
    physicalExaminationFormDataManager.selectedMonthsCancer = null;

    physicalExaminationFormDataManager.descriptionCancerTextField.text = "";

    physicalExaminationFormDataManager.selectedYearsGynecologicalDisorder =
        null;
    physicalExaminationFormDataManager.selectedMonthsGynecologicalDisorder =
        null;

    physicalExaminationFormDataManager
        .descriptionGynecologicalDisorderTextField
        .text = "";

    physicalExaminationFormDataManager.selectedYearsThyroid = null;
    physicalExaminationFormDataManager.selectedMonthsThyroid = null;

    physicalExaminationFormDataManager.descriptionThyroidTextField.text = "";

    physicalExaminationFormDataManager.selectedYearsNeurologicalDisease = null;
    physicalExaminationFormDataManager.selectedMonthsNeurologicalDisease = null;

    physicalExaminationFormDataManager
        .descriptionNeurologicalDiseaseTextField
        .text = "";

    physicalExaminationFormDataManager.selectedYearsOther = null;
    physicalExaminationFormDataManager.selectedMonthsOther = null;

    physicalExaminationFormDataManager.descriptionOtherTextField.text = "";

    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    setNoHistory();
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
                      "Medical Histroy",
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
                ? GestureDetector(
                  onTap: () {
                    if (physicalExaminationFormDataManager.isNoHistory) {
                      physicalExaminationFormDataManager.isNoHistory = false;
                    } else {
                      physicalExaminationFormDataManager.isNoHistory = true;
                    }

                    if (physicalExaminationFormDataManager.isNoHistory) {
                      showDialog(
                        context: context,
                        builder: (BuildContext context) {
                          return AlertDialog(
                            title: Text(
                              "Alert",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                            content: Text(
                              "It seems that the patien has no know medical history. Please confirm to proceed.",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                            actions: [
                              TextButton(
                                child: Text(
                                  "CONFIRM",
                                  style: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                                onPressed: () {
                                  setNoHistory();
                                  physicalExaminationFormDataManager
                                      .isNoHistory = true;
                                  Navigator.pop(context);
                                  setState(() {});
                                },
                              ),
                              TextButton(
                                child: Text(
                                  "CANCEL",
                                  style: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                                onPressed: () {
                                  Navigator.pop(context);
                                  physicalExaminationFormDataManager
                                      .isNoHistory = false;
                                  setState(() {});
                                },
                              ),
                            ],
                          );
                        },
                      );
                    }
                    setState(() {});
                  },
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [

                      SizedBox(
                        width: 26.w,
                        height: 26.h,
                        child: Image.asset(
                          physicalExaminationFormDataManager.isNoHistory ==
                                  false
                              ? icUnCheckBoxSelected
                              : icCheckBoxSelected,
                        ),
                      ),
                      // ),
                      SizedBox(width: 8.w),
                      Text(
                        "No History",
                        style: TextStyle(
                          color: kBlackColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w700,
                          fontSize: 16.sp,
                        ),
                      ),
                    ],
                  ).paddingOnly(right: 16.w, bottom: 4.h, top: 4.h),
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Asthma',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,

                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isAsthma = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearsAsthma = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsAsthma =
                        p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionAsthmaTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Kideny Disease',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,

                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.iskidenyDisease = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearskidenyDisease = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthskidenyDisease = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionkidenyDiseaseTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Diabetes',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,

                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isDiabetes = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearsDiabetes =
                        p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsDiabetes =
                        p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionDiabetesTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Cancer',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,
                  // isSelectedRadioYes:
                  //     physicalExaminationFormDataManager.isCancer,
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isCancer = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearsCancer = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsCancer =
                        p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionCancerTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            widget.patientObj?.gender == "Male"
                ? Container()
                : isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Gynecological disorder',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isGynecologicalDisorder =
                        p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsGynecologicalDisorder = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsGynecologicalDisorder = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionGynecologicalDisorderTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Thyroid',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,
                  // isSelectedRadioYes:
                  //     physicalExaminationFormDataManager.isThyroid,
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isThyroid = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearsThyroid =
                        p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsThyroid =
                        p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionThyroidTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Neurological Disease',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,
                  // isSelectedRadioYes:
                  //     physicalExaminationFormDataManager.isNeurologicalDisease,
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isNeurologicalDisease =
                        p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsNeurologicalDisease = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsNeurologicalDisease = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionNeurologicalDiseaseTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },

                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Cardiovascular Disease',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,
                  // isSelectedRadioYes:
                  //     physicalExaminationFormDataManager.isNeurologicalDisease,
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isCardiovascularDisease =
                        p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsCardiovascularDisease = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsCardiovascularDisease = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionCardiovascularDiseaseTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },

                  showStar: false,
                )
                : Container(),
            isExpaneded == true
                ? MedicalHistoryCard(
                  titleString: 'Other',
                  isNoHistory: physicalExaminationFormDataManager.isNoHistory,

                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isOther = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearsOther = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsOther = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionOtherTextField
                        .text = p0;
                  },
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  showStar: false,
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}
