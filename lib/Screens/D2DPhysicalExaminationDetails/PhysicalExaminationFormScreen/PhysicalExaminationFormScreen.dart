// ignore_for_file: must_be_immutable, file_names, avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/S2TAlertView.dart';
import 'package:s2toperational/Screens/D2DPhysicalExaminationDetails/PhysicalExaminationFormScreen/BasciHealthInfo.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/D2DPhysicalExamninationDetailsResponse/D2DPhysicalExamninationDetailsResponse.dart';
import '../../../Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'AlleriesSurgeriesAndSymptomsView/AlleriesSurgeriesAndSymptomsCard/AlleriesSurgeiesAndSymptoms.dart';
import 'LiverExaminationHistoryView/LiverExaminationHistoryView.dart';
import 'MedicalHistroyView/MedicalHistroyView.dart';
import 'PatientInformationView/PatientInformationView.dart';

class PhysicalExaminationFormScreen extends StatefulWidget {
  int regdId = 0;
  int campTypeID = 0;
  String healthScreentype = "";

  PhysicalExaminationFormScreen({
    super.key,
    required this.regdId,
    required this.campTypeID,
    required this.healthScreentype,
  });

  @override
  State<PhysicalExaminationFormScreen> createState() =>
      _PhysicalExaminationFormScreenState();
}

class _PhysicalExaminationFormScreenState
    extends State<PhysicalExaminationFormScreen> {
  APIManager apiManager = APIManager();
  D2DPhysicalExamninationDetailsOutput? patientObj;
  PhysicalExaminationFormDataManager physicalExaminationFormManager =
      PhysicalExaminationFormDataManager();
  List<Map<String, String>> healthList = [];
  int empCode = 0;
  int dESGID = 0;
  final formKey = GlobalKey<FormState>();

  // bool isDataLoaded = false;

  @override
  void initState() {
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    getUserDataforPhysicalExamnination();

    super.initState();
  }

  @override
  void dispose() {
    physicalExaminationFormManager.resetFields();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return WillPopScope(
      onWillPop: () async {
        physicalExaminationFormManager.resetFields();
        setState(() {});
        return true;
      },
      child: Scaffold(
        backgroundColor: kWhiteColor,
        appBar: mAppBar(
          scTitle:
              DataProvider().getRegularCamp()
                  ? "Physical Examination"
                  : "D2D Physical Examination",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            physicalExaminationFormManager.resetFields();
            setState(() {});
            Navigator.pop(context);
          },
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            child: Padding(
              padding: EdgeInsets.only(
                left: 12.w,
                right: 12.w,
                bottom: MediaQuery.of(context).viewPadding.bottom + 20.h,
              ),
              child: Form(
                key: formKey,
                child: Column(
                  children: [
                    SizedBox(height: 10.h),
                    PatientInformationView(patientObj: patientObj),
                    BasicHealthInfo(patientObj: patientObj),
                    AlleriesSurgeriesAndSymptomsView(),
                    MedicalHistroyView(patientObj: patientObj),
                    liverExaminationHistory(patientObj),
                    SizedBox(height: 30.h),

                    SizedBox(
                      width: 150.w,
                      child: Center(
                        child: AppActiveButton(
                          buttontitle: "Save",
                          onTap: () {
                            if (formKey.currentState!.validate()) {
                              if (validations()) {
                                if (shouldShowNoKnownHistoryAlert()) {
                                  showNoKnownHistoryAlert(() {
                                    ToastManager.showLoader();
                                    insertBasicInfoMaleV1New();
                                  });
                                } else {
                                  ToastManager.showLoader();
                                  insertBasicInfoMaleV1New();
                                }
                              }
                            }
                          },
                        ),
                      ),
                    ).paddingOnly(bottom: 20.h),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Future<void> getUserDataforPhysicalExamnination() async {
    ToastManager.showLoader();
    Map<String, String> params = {"RegdId": widget.regdId.toString()};

    await apiManager.getUserDataforPhysicalExamninationAPI(
      params,
      apiUserDataforPhysicalExamninationCallBack,
    );
  }

  void apiUserDataforPhysicalExamninationCallBack(
    D2DPhysicalExamninationDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      ToastManager.hideLoader();
      patientObj = response?.output?.first;
      if (patientObj!.bMI! <= 23) {
        physicalExaminationFormManager.isBMI = false;
      } else {
        physicalExaminationFormManager.isBMI = true;
      }

      if (double.tryParse(patientObj!.bloodSugarR!)! < 200) {
        physicalExaminationFormManager.isElevatedBloodGlucose = false;
      } else {
        physicalExaminationFormManager.isElevatedBloodGlucose = true;
      }
      checkNormalInitially();
      setState(() {});
    } else {
      ToastManager.hideLoader();

      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void checkNormalInitially() {
    if (physicalExaminationFormManager.isElevatedBloodGlucose == true ||
        physicalExaminationFormManager.isBMI == true) {
      physicalExaminationFormManager.isNormal = false;
    } else {
      physicalExaminationFormManager.isNormal = true;
    }
  }

  bool validations() {
    healthList = [];

    if (physicalExaminationFormManager.isAlleries == true) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "1",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearAlleries?.yearName ?? "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsAlleries
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionAlleriesTextField.text,
      });
    } else if (physicalExaminationFormManager.isAlleries == false) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "1",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    } else {
      ToastManager.toast("Please Select Input For alleries");
      return false;
    }

    if (physicalExaminationFormManager.isSurgicalHistory == true) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "2",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsSurgicalHistory
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsSurgicalHistory
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionSurgicalHistoryTextField
                .text,
      });
    } else if (physicalExaminationFormManager.isSurgicalHistory == false) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "2",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    } else {
      ToastManager.toast("Please Select Input For Surgical");
      return false;
    }

    if (physicalExaminationFormManager.isCurrentSymtoms == true) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "3",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsCurrentSymtoms
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsCurrentSymtoms
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionCurrentSymtomsTextField
                .text,
      });
    } else if (physicalExaminationFormManager.isCurrentSymtoms == false) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "3",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    } else {
      ToastManager.toast("Please Select Input For Current Symtoms");
      return false;
    }

    if (physicalExaminationFormManager.isCurrentMedication == true) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "4",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsCurrentMedication
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsCurrentMedication
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionCurrentMedicationTextField
                .text,
      });
    } else if (physicalExaminationFormManager.isCurrentMedication == false) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "4",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    } else {
      ToastManager.toast("Please Select Input For Current Medication");
      return false;
    }

    if (physicalExaminationFormManager.isAsthma) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "5",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearsAsthma?.yearName ?? "",
        "Month":
            physicalExaminationFormManager.selectedMonthsAsthma?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionAsthmaTextField.text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "5",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.iskidenyDisease) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "6",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearskidenyDisease
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthskidenyDisease
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionkidenyDiseaseTextField
                .text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "6",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isDiabetes) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "7",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearsDiabetes?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsDiabetes
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionDiabetesTextField.text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "7",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isCancer) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "8",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearsCancer?.yearName ?? "",
        "Month":
            physicalExaminationFormManager.selectedMonthsCancer?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionCancerTextField.text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "8",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isGynecologicalDisorder) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "9",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsGynecologicalDisorder
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsGynecologicalDisorder
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionGynecologicalDisorderTextField
                .text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "9",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isThyroid) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "10",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearsThyroid?.yearName ?? "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsThyroid
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionThyroidTextField.text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "10",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isNeurologicalDisease) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "11",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsNeurologicalDisease
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsNeurologicalDisease
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionNeurologicalDiseaseTextField
                .text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "11",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isCardiovascularDisease) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "12",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager
                .selectedYearsCardiovascularDisease
                ?.yearName ??
            "",
        "Month":
            physicalExaminationFormManager
                .selectedMonthsCardiovascularDisease
                ?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager
                .descriptionCardiovascularDiseaseTextField
                .text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "12",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (physicalExaminationFormManager.isOther) {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "13",
        "PhyExamStatus": "1",
        "Year":
            physicalExaminationFormManager.selectedYearsOther?.yearName ?? "",
        "Month":
            physicalExaminationFormManager.selectedMonthsOther?.monthNameEng ??
            "",
        "Description":
            physicalExaminationFormManager.descriptionOtherTextField.text,
      });
    } else {
      healthList.add({
        "Regdid": widget.regdId.toString(),
        "PhyExamTypeID": "13",
        "PhyExamStatus": "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    if (widget.campTypeID == 6) {
      if (physicalExaminationFormManager.isFastingState == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "19",
          "PhyExamStatus": "1",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "19",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      }
      if (physicalExaminationFormManager.isPalpableLiver == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "20",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsPalpableLiver
                  ?.yearName ??
              "",
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsPalpableLiver
                  ?.monthNameEng ??
              "",
          "Description":
              physicalExaminationFormManager
                  .descriptionPalpableLiverTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isPalpableLiver == false) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "20",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isHistoryOfChronic == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "16",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsHistoryOfChronic
                  ?.yearName ??
              "",
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsHistoryOfChronic
                  ?.monthNameEng ??
              "",
          "Description":
              physicalExaminationFormManager
                  .descriptionHistoryOfChronicTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isHistoryOfChronic == false) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "16",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isLiverRelatedAilments == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "17",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsLiverRelatedAilments
                  ?.yearName ??
              "",
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsLiverRelatedAilments
                  ?.monthNameEng ??
              "",
          "Description":
              physicalExaminationFormManager
                  .descriptionLiverRelatedAilmentsTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isLiverRelatedAilments ==
          false) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "17",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isPresenceOfDyslipidemia == true &&
          (physicalExaminationFormManager.isPresenceOfDyslipidemiaUnknown ==
                  false ||
              physicalExaminationFormManager.isPresenceOfDyslipidemiaUnknown ==
                  null)) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "24",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsPresenceOfDyslipidemia
                  ?.yearName ??
              "",
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsPresenceOfDyslipidemia
                  ?.monthNameEng ??
              "",
          "Description":
              physicalExaminationFormManager
                  .descriptionPresenceOfDyslipidemiaTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isPresenceOfDyslipidemia ==
              false ||
          physicalExaminationFormManager.isPresenceOfDyslipidemiaUnknown ==
              true) {
        // if (physicalExaminationFormManager.isPresenceOfDyslipidemiaUnknown ==
        //     true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "24",
          "PhyExamStatus":
              physicalExaminationFormManager.isPresenceOfDyslipidemiaUnknown ==
                      true
                  ? "2"
                  : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isPresenceOfDiabetes == true &&
          (physicalExaminationFormManager.isPresenceOfDiabetesUnknown ==
                  false ||
              physicalExaminationFormManager.isPresenceOfDiabetesUnknown ==
                  null)) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "25",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsPresenceOfDiabetes
                  ?.yearName ??
              "",
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsPresenceOfDiabetes
                  ?.monthNameEng ??
              "",
          "Description":
              physicalExaminationFormManager
                  .descriptionPresenceOfDiabetesTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isPresenceOfDiabetes == false ||
          physicalExaminationFormManager.isPresenceOfDiabetesUnknown == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "25",
          "PhyExamStatus":
              physicalExaminationFormManager.isPresenceOfDiabetesUnknown == true
                  ? "2"
                  : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isPresenceOfHypertension == true &&
          (physicalExaminationFormManager.isPresenceOfHypertensionUnknown ==
                  false ||
              physicalExaminationFormManager.isPresenceOfHypertensionUnknown ==
                  null)) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "26",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsPresenceOfHypertension
                  ?.yearName ??
              '0',
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsPresenceOfHypertension
                  ?.monthNameEng ??
              '0',
          "Description":
              physicalExaminationFormManager
                  .descriptionPresenceOfHypertensionTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isPresenceOfHypertension ==
              false ||
          physicalExaminationFormManager.isPresenceOfHypertensionUnknown ==
              true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "26",
          "PhyExamStatus":
              physicalExaminationFormManager.isPresenceOfHypertensionUnknown ==
                      true
                  ? "2"
                  : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isElevatedALTs == true &&
          (physicalExaminationFormManager.isElevatedALTsUnknown == false ||
              physicalExaminationFormManager.isElevatedALTsUnknown == null)) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "21",
          "PhyExamStatus": "1",
          "Year":
              physicalExaminationFormManager
                  .selectedYearsElevatedALTs
                  ?.yearName ??
              '0',
          "Month":
              physicalExaminationFormManager
                  .selectedMonthsElevatedALTs
                  ?.monthNameEng ??
              '0',
          "Description":
              physicalExaminationFormManager
                  .descriptionElevatedALTsTextField
                  .text,
        });
      } else if (physicalExaminationFormManager.isElevatedALTs == false ||
          physicalExaminationFormManager.isElevatedALTsUnknown == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "21",
          "PhyExamStatus":
              physicalExaminationFormManager.isElevatedALTsUnknown == true
                  ? "2"
                  : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (physicalExaminationFormManager.isBMI == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "22",
          "PhyExamStatus": "1",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "22",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      }

      if (physicalExaminationFormManager.isElevatedBloodGlucose == true) {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "23",
          "PhyExamStatus": "1",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        healthList.add({
          "Regdid": widget.regdId.toString(),
          "PhyExamTypeID": "23",
          "PhyExamStatus": "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      }
    }

    return true;
  }

  void insertBasicInfoMaleV1New() {
    String jsonString = jsontoStringMethod(healthList);

    String normalOrAbnormal = "0";

    if (physicalExaminationFormManager.isNormal == true) {
      normalOrAbnormal = "1";
    } else {
      normalOrAbnormal = "0";
    }
    Map<String, String> params = {
      "Createdby": empCode.toString(),
      "T_PhysicalexaminationAndDescription": jsonString,
      "IsAbnormal": normalOrAbnormal,
      "Camptype": widget.campTypeID.toString(),
    };

    apiManager.insertPhysicalExaminationForHSCCV1API(
      params,
      apiInsertPhysicalExaminationForHSCCV1APICallBack,
    );
  }

  bool shouldShowNoKnownHistoryAlert() {
    if (physicalExaminationFormManager.isNoHistory == true) {
      return false;
    }

    final hasKnownMedicalHistory =
        physicalExaminationFormManager.isAsthma == true ||
        physicalExaminationFormManager.iskidenyDisease == true ||
        physicalExaminationFormManager.isDiabetes == true ||
        physicalExaminationFormManager.isCancer == true ||
        physicalExaminationFormManager.isGynecologicalDisorder == true ||
        physicalExaminationFormManager.isThyroid == true ||
        physicalExaminationFormManager.isNeurologicalDisease == true ||
        physicalExaminationFormManager.isCardiovascularDisease == true ||
        physicalExaminationFormManager.isOther == true;

    return !hasKnownMedicalHistory;
  }

  void showNoKnownHistoryAlert(VoidCallback onConfirm) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(
            "Alert",
            style: TextStyle(fontFamily: FontConstants.interFonts),
          ),
          content: Text(
            "It seems that the patient has no known medical history. Please confirm to proceed.",
            style: TextStyle(fontFamily: FontConstants.interFonts),
          ),
          actions: [
            TextButton(
              child: Text(
                "Confirm",
                style: TextStyle(fontFamily: FontConstants.interFonts),
              ),
              onPressed: () {
                Navigator.pop(context);
                onConfirm();
              },
            ),
            TextButton(
              child: Text(
                "Cancel",
                style: TextStyle(fontFamily: FontConstants.interFonts),
              ),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
          ],
        );
      },
    );
  }

  void apiInsertPhysicalExaminationForHSCCV1APICallBack(
    D2DPhysicalExamninationDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      // ToastManager.showSuccessPopup(
      //   context,
      //   icSuccessIcon,
      //   response?.message ?? "",
      // );
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return S2TAlertView(
            icon: icSuccessIcon,
            message: response?.message ?? "",
            onTap: () {
              Navigator.pop(context);
              Navigator.pop(context);
            },
          );
        },
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  String jsontoStringMethod(List<Map<String, String>> items) {
    try {
      String jsonString = jsonEncode(items);
      return jsonString;
    } catch (e) {
      print("Error encoding JSON: $e");
      return "";
    }
  }

  Widget liverExaminationHistory(patientObj) {
    if (widget.campTypeID == 6) {
      return LiverExaminationHistoryView(
        patientObj: patientObj,
        physicalExaminationFormDataManager: physicalExaminationFormManager,
      );
    }
    return Container();
  }
}
