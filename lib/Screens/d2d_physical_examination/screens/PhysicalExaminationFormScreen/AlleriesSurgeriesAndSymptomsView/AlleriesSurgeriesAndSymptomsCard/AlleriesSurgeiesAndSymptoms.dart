// ignore_for_file: file_names
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';

import '../../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'AlleriesSurgeriesAndSymptomsCard.dart';

class AlleriesSurgeriesAndSymptomsView extends StatefulWidget {
  const AlleriesSurgeriesAndSymptomsView({super.key});

  @override
  State<AlleriesSurgeriesAndSymptomsView> createState() =>
      _AlleriesSurgeriesAndSymptomsViewState();
}

class _AlleriesSurgeriesAndSymptomsViewState
    extends State<AlleriesSurgeriesAndSymptomsView> {
  bool isExpaneded = true;

  PhysicalExaminationFormDataManager physicalExaminationFormDataManager =
      PhysicalExaminationFormDataManager();

  @override
  void initState() {
    super.initState();
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
                      "Allergies, Surgeries and Symptoms",
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
                ? AlleriesSurgeriesAndSymptoms(
                  titleString: 'Alleries',
                  isNoHistory: false,
                  noHistoryChange: (p0) {
                    physicalExaminationFormDataManager.isNoHistory = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager.selectedYearAlleries =
                        p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager.selectedMonthsAlleries =
                        p0;
                  },
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isAlleries = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionAlleriesTextField
                        .text = p0;
                  },
                  showStar: true,
                )
                : Container(),
            isExpaneded == true
                ? AlleriesSurgeriesAndSymptoms(
                  titleString: 'Surgical History',
                  isNoHistory: false,
                  noHistoryChange: (p0) {
                    physicalExaminationFormDataManager.isNoHistory = p0;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsSurgicalHistory = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsSurgicalHistory = p0;
                  },
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isSurgicalHistory = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionSurgicalHistoryTextField
                        .text = p0;
                  },

                  showStar: true,
                )
                : Container(),
            isExpaneded == true
                ? AlleriesSurgeriesAndSymptoms(
                  titleString: 'Current Symtoms',
                  isNoHistory: false,
                  noHistoryChange: (p0) {
                    physicalExaminationFormDataManager.isNoHistory = false;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsCurrentSymtoms = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsCurrentSymtoms = p0;
                  },
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isCurrentSymtoms = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionCurrentSymtomsTextField
                        .text = p0;
                  },

                  showStar: true,
                )
                : Container(),
            isExpaneded == true
                ? AlleriesSurgeriesAndSymptoms(
                  titleString: 'Current Medication',
                  isNoHistory: false,
                  noHistoryChange: (isNoHistory) {
                    physicalExaminationFormDataManager.isNoHistory =
                        isNoHistory;
                  },
                  selectedYearChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedYearsCurrentMedication = p0;
                  },
                  selectedMonthChange: (p0) {
                    physicalExaminationFormDataManager
                        .selectedMonthsCurrentMedication = p0;
                  },
                  selectedRadioYesChange: (p0) {
                    physicalExaminationFormDataManager.isCurrentMedication = p0;
                  },
                  descriptionTextFieldChange: (p0) {
                    physicalExaminationFormDataManager
                        .descriptionCurrentMedicationTextField
                        .text = p0;
                  },

                  showStar: true,
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}
