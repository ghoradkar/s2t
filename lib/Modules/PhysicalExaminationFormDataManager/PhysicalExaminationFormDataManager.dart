// ignore_for_file: file_names

import 'package:flutter/material.dart';

import '../../Screens/d2d_physical_examination/model/MonthsResponse.dart';
import '../../Screens/d2d_physical_examination/model/YearsResponse.dart';
//
// import '../Json_Class/MonthsResponse/MonthsResponse.dart';
// import '../Json_Class/YearsResponse/YearsResponse.dart';

class PhysicalExaminationFormDataManager {
  static final PhysicalExaminationFormDataManager _singleton =
      PhysicalExaminationFormDataManager._internal();

  factory PhysicalExaminationFormDataManager() {
    return _singleton;
  }
  PhysicalExaminationFormDataManager._internal();

  TextEditingController descriptionAlleriesTextField = TextEditingController();
  TextEditingController descriptionSurgicalHistoryTextField =
      TextEditingController();
  TextEditingController descriptionCurrentSymtomsTextField =
      TextEditingController();
  TextEditingController descriptionCurrentMedicationTextField =
      TextEditingController();

  bool? isAlleries;
  bool? isSurgicalHistory;
  bool? isCurrentSymtoms;
  bool? isCurrentMedication;

  YearsOutput? selectedYearAlleries;
  YearsOutput? selectedYearsSurgicalHistory;
  YearsOutput? selectedYearsCurrentSymtoms;
  YearsOutput? selectedYearsCurrentMedication;

  MonthsOutput? selectedMonthsAlleries;
  MonthsOutput? selectedMonthsSurgicalHistory;
  MonthsOutput? selectedMonthsCurrentSymtoms;
  MonthsOutput? selectedMonthsCurrentMedication;

  bool isAsthma = false;
  YearsOutput? selectedYearsAsthma;
  MonthsOutput? selectedMonthsAsthma;

  TextEditingController descriptionAsthmaTextField = TextEditingController();

  bool iskidenyDisease = false;
  YearsOutput? selectedYearskidenyDisease;
  MonthsOutput? selectedMonthskidenyDisease;

  TextEditingController descriptionkidenyDiseaseTextField =
      TextEditingController();

  bool isDiabetes = false;
  YearsOutput? selectedYearsDiabetes;
  MonthsOutput? selectedMonthsDiabetes;

  TextEditingController descriptionDiabetesTextField = TextEditingController();

  bool isCancer = false;
  YearsOutput? selectedYearsCancer;
  MonthsOutput? selectedMonthsCancer;

  TextEditingController descriptionCancerTextField = TextEditingController();

  bool isGynecologicalDisorder = false;
  YearsOutput? selectedYearsGynecologicalDisorder;
  MonthsOutput? selectedMonthsGynecologicalDisorder;

  TextEditingController descriptionGynecologicalDisorderTextField =
      TextEditingController();

  bool isThyroid = false;
  YearsOutput? selectedYearsThyroid;
  MonthsOutput? selectedMonthsThyroid;

  TextEditingController descriptionThyroidTextField = TextEditingController();

  bool isNeurologicalDisease = false;
  YearsOutput? selectedYearsNeurologicalDisease;
  MonthsOutput? selectedMonthsNeurologicalDisease;

  TextEditingController descriptionNeurologicalDiseaseTextField =
      TextEditingController();

  bool isCardiovascularDisease = false;
  YearsOutput? selectedYearsCardiovascularDisease;
  MonthsOutput? selectedMonthsCardiovascularDisease;

  TextEditingController descriptionCardiovascularDiseaseTextField =
      TextEditingController();

  bool isOther = false;
  YearsOutput? selectedYearsOther;
  MonthsOutput? selectedMonthsOther;

  TextEditingController descriptionOtherTextField = TextEditingController();

  bool isNoHistory = false;

  bool? isFastingState;

  bool? isPalpableLiver;
  YearsOutput? selectedYearsPalpableLiver;
  MonthsOutput? selectedMonthsPalpableLiver;

  TextEditingController descriptionPalpableLiverTextField =
      TextEditingController();

  bool? isHistoryOfChronic;
  YearsOutput? selectedYearsHistoryOfChronic;
  MonthsOutput? selectedMonthsHistoryOfChronic;

  TextEditingController descriptionHistoryOfChronicTextField =
      TextEditingController();

  bool? isLiverRelatedAilments;
  YearsOutput? selectedYearsLiverRelatedAilments;
  MonthsOutput? selectedMonthsLiverRelatedAilments;

  TextEditingController descriptionLiverRelatedAilmentsTextField =
      TextEditingController();

  bool? isPresenceOfDyslipidemia;
  bool? isPresenceOfDyslipidemiaUnknown;
  YearsOutput? selectedYearsPresenceOfDyslipidemia;
  MonthsOutput? selectedMonthsPresenceOfDyslipidemia;

  TextEditingController descriptionPresenceOfDyslipidemiaTextField =
      TextEditingController();

  bool? isPresenceOfDiabetes;
  bool? isPresenceOfDiabetesUnknown;
  YearsOutput? selectedYearsPresenceOfDiabetes;
  MonthsOutput? selectedMonthsPresenceOfDiabetes;

  TextEditingController descriptionPresenceOfDiabetesTextField =
      TextEditingController();


  bool? isPresenceOfHypertension;
  bool? isPresenceOfHypertensionUnknown;
  YearsOutput? selectedYearsPresenceOfHypertension;
  MonthsOutput? selectedMonthsPresenceOfHypertension;

  TextEditingController descriptionPresenceOfHypertensionTextField =
  TextEditingController();


  bool? isElevatedALTs;
  bool? isElevatedALTsUnknown;
  YearsOutput? selectedYearsElevatedALTs;
  MonthsOutput? selectedMonthsElevatedALTs;

  TextEditingController descriptionElevatedALTsTextField =
  TextEditingController();

  bool? isBMI;
  bool? isElevatedBloodGlucose;

  // bool isNormal = false;
  bool? isNormal;
  // bool isNormalOrAbnormal = false;

  void resetFields() {
    isFastingState = null;
    isPalpableLiver = null;
    descriptionPalpableLiverTextField.clear();
    isHistoryOfChronic = null;
    descriptionHistoryOfChronicTextField.clear();

    isLiverRelatedAilments = null;
    descriptionLiverRelatedAilmentsTextField
        .clear();

    isPresenceOfDyslipidemia = null;
    isPresenceOfDyslipidemiaUnknown = null;
    descriptionPresenceOfDyslipidemiaTextField
        .clear();

    isPresenceOfDiabetes = null;
    isPresenceOfDiabetesUnknown = null;
    descriptionPresenceOfDiabetesTextField
        .clear();

    isPresenceOfHypertension = null;
    isPresenceOfHypertensionUnknown = null;
    descriptionPresenceOfHypertensionTextField
        .clear();

    isElevatedALTs = null;
    isElevatedALTsUnknown = null;
    descriptionElevatedALTsTextField.clear();

    isAlleries = null;
    isSurgicalHistory = null;
    isCurrentSymtoms = null;
    isCurrentMedication = null;
    isNormal = null;


  }

}
