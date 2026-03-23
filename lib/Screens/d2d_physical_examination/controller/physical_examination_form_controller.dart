import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/S2TAlertView.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamninationDetailsResponse.dart';

class PhysicalExaminationFormController extends GetxController {
  final int regdId;
  final int campTypeID;
  final String healthScreentype;

  PhysicalExaminationFormController({
    required this.regdId,
    required this.campTypeID,
    required this.healthScreentype,
  });

  final _api = APIManager();
  final formManager = PhysicalExaminationFormDataManager();
  final formKey = GlobalKey<FormState>();

  D2DPhysicalExamninationDetailsOutput? patientObj;
  int empCode = 0;
  int dESGID = 0;
  bool isLoading = false;

  List<Map<String, String>> _healthList = [];

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    empCode = userData?.empCode ?? 0;
    dESGID = userData?.dESGID ?? 0;
    _fetchPatientData();
  }

  @override
  void onClose() {
    formManager.resetFields();
    super.onClose();
  }

  // ── Fetch patient data ───────────────────────────────────────────────────

  Future<void> _fetchPatientData() async {
    ToastManager.showLoader();
    await _api.getUserDataforPhysicalExamninationAPI(
      {"RegdId": regdId.toString()},
      _onPatientData,
    );
  }

  void _onPatientData(
    D2DPhysicalExamninationDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      patientObj = response?.output?.first;
      if (patientObj!.bMI! <= 23) {
        formManager.isBMI = false;
      } else {
        formManager.isBMI = true;
      }
      if (double.tryParse(patientObj!.bloodSugarR!)! < 200) {
        formManager.isElevatedBloodGlucose = false;
      } else {
        formManager.isElevatedBloodGlucose = true;
      }
      _checkNormalInitially();
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  void _checkNormalInitially() {
    if (formManager.isElevatedBloodGlucose == true || formManager.isBMI == true) {
      formManager.isNormal = false;
    } else {
      formManager.isNormal = true;
    }
  }

  // ── Save ─────────────────────────────────────────────────────────────────

  void onSaveTapped() {
    if (formKey.currentState!.validate()) {
      if (_buildValidations()) {
        if (_shouldShowNoKnownHistoryAlert()) {
          _showNoKnownHistoryAlert(() {
            ToastManager.showLoader();
            _insertBasicInfoMaleV1New();
          });
        } else {
          ToastManager.showLoader();
          _insertBasicInfoMaleV1New();
        }
      }
    }
  }

  bool _buildValidations() {
    _healthList = [];

    if (formManager.isAlleries == true) {
      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "1",
        "PhyExamStatus": "1",
        "Year": formManager.selectedYearAlleries?.yearName ?? "",
        "Month": formManager.selectedMonthsAlleries?.monthNameEng ?? "",
        "Description": formManager.descriptionAlleriesTextField.text,
      });
    } else if (formManager.isAlleries == false) {
      _healthList.add({
        "Regdid": regdId.toString(),
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

    if (formManager.isSurgicalHistory == true) {
      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "2",
        "PhyExamStatus": "1",
        "Year": formManager.selectedYearsSurgicalHistory?.yearName ?? "",
        "Month": formManager.selectedMonthsSurgicalHistory?.monthNameEng ?? "",
        "Description": formManager.descriptionSurgicalHistoryTextField.text,
      });
    } else if (formManager.isSurgicalHistory == false) {
      _healthList.add({
        "Regdid": regdId.toString(),
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

    if (formManager.isCurrentSymtoms == true) {
      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "3",
        "PhyExamStatus": "1",
        "Year": formManager.selectedYearsCurrentSymtoms?.yearName ?? "",
        "Month": formManager.selectedMonthsCurrentSymtoms?.monthNameEng ?? "",
        "Description": formManager.descriptionCurrentSymtomsTextField.text,
      });
    } else if (formManager.isCurrentSymtoms == false) {
      _healthList.add({
        "Regdid": regdId.toString(),
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

    if (formManager.isCurrentMedication == true) {
      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "4",
        "PhyExamStatus": "1",
        "Year": formManager.selectedYearsCurrentMedication?.yearName ?? "",
        "Month": formManager.selectedMonthsCurrentMedication?.monthNameEng ?? "",
        "Description": formManager.descriptionCurrentMedicationTextField.text,
      });
    } else if (formManager.isCurrentMedication == false) {
      _healthList.add({
        "Regdid": regdId.toString(),
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

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "5",
      "PhyExamStatus": formManager.isAsthma ? "1" : "0",
      "Year": formManager.isAsthma ? (formManager.selectedYearsAsthma?.yearName ?? "") : "0",
      "Month": formManager.isAsthma ? (formManager.selectedMonthsAsthma?.monthNameEng ?? "") : "0",
      "Description": formManager.isAsthma ? formManager.descriptionAsthmaTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "6",
      "PhyExamStatus": formManager.iskidenyDisease ? "1" : "0",
      "Year": formManager.iskidenyDisease ? (formManager.selectedYearskidenyDisease?.yearName ?? "") : "0",
      "Month": formManager.iskidenyDisease ? (formManager.selectedMonthskidenyDisease?.monthNameEng ?? "") : "0",
      "Description": formManager.iskidenyDisease ? formManager.descriptionkidenyDiseaseTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "7",
      "PhyExamStatus": formManager.isDiabetes ? "1" : "0",
      "Year": formManager.isDiabetes ? (formManager.selectedYearsDiabetes?.yearName ?? "") : "0",
      "Month": formManager.isDiabetes ? (formManager.selectedMonthsDiabetes?.monthNameEng ?? "") : "0",
      "Description": formManager.isDiabetes ? formManager.descriptionDiabetesTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "8",
      "PhyExamStatus": formManager.isCancer ? "1" : "0",
      "Year": formManager.isCancer ? (formManager.selectedYearsCancer?.yearName ?? "") : "0",
      "Month": formManager.isCancer ? (formManager.selectedMonthsCancer?.monthNameEng ?? "") : "0",
      "Description": formManager.isCancer ? formManager.descriptionCancerTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "9",
      "PhyExamStatus": formManager.isGynecologicalDisorder ? "1" : "0",
      "Year": formManager.isGynecologicalDisorder ? (formManager.selectedYearsGynecologicalDisorder?.yearName ?? "") : "0",
      "Month": formManager.isGynecologicalDisorder ? (formManager.selectedMonthsGynecologicalDisorder?.monthNameEng ?? "") : "0",
      "Description": formManager.isGynecologicalDisorder ? formManager.descriptionGynecologicalDisorderTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "10",
      "PhyExamStatus": formManager.isThyroid ? "1" : "0",
      "Year": formManager.isThyroid ? (formManager.selectedYearsThyroid?.yearName ?? "") : "0",
      "Month": formManager.isThyroid ? (formManager.selectedMonthsThyroid?.monthNameEng ?? "") : "0",
      "Description": formManager.isThyroid ? formManager.descriptionThyroidTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "11",
      "PhyExamStatus": formManager.isNeurologicalDisease ? "1" : "0",
      "Year": formManager.isNeurologicalDisease ? (formManager.selectedYearsNeurologicalDisease?.yearName ?? "") : "0",
      "Month": formManager.isNeurologicalDisease ? (formManager.selectedMonthsNeurologicalDisease?.monthNameEng ?? "") : "0",
      "Description": formManager.isNeurologicalDisease ? formManager.descriptionNeurologicalDiseaseTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "12",
      "PhyExamStatus": formManager.isCardiovascularDisease ? "1" : "0",
      "Year": formManager.isCardiovascularDisease ? (formManager.selectedYearsCardiovascularDisease?.yearName ?? "") : "0",
      "Month": formManager.isCardiovascularDisease ? (formManager.selectedMonthsCardiovascularDisease?.monthNameEng ?? "") : "0",
      "Description": formManager.isCardiovascularDisease ? formManager.descriptionCardiovascularDiseaseTextField.text : "",
    });

    _healthList.add({
      "Regdid": regdId.toString(),
      "PhyExamTypeID": "13",
      "PhyExamStatus": formManager.isOther ? "1" : "0",
      "Year": formManager.isOther ? (formManager.selectedYearsOther?.yearName ?? "") : "0",
      "Month": formManager.isOther ? (formManager.selectedMonthsOther?.monthNameEng ?? "") : "0",
      "Description": formManager.isOther ? formManager.descriptionOtherTextField.text : "",
    });

    if (campTypeID == 6) {
      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "19",
        "PhyExamStatus": formManager.isFastingState == true ? "1" : "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });

      if (formManager.isPalpableLiver == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "20",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsPalpableLiver?.yearName ?? "",
          "Month": formManager.selectedMonthsPalpableLiver?.monthNameEng ?? "",
          "Description": formManager.descriptionPalpableLiverTextField.text,
        });
      } else if (formManager.isPalpableLiver == false) {
        _healthList.add({
          "Regdid": regdId.toString(),
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

      if (formManager.isHistoryOfChronic == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "16",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsHistoryOfChronic?.yearName ?? "",
          "Month": formManager.selectedMonthsHistoryOfChronic?.monthNameEng ?? "",
          "Description": formManager.descriptionHistoryOfChronicTextField.text,
        });
      } else if (formManager.isHistoryOfChronic == false) {
        _healthList.add({
          "Regdid": regdId.toString(),
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

      if (formManager.isLiverRelatedAilments == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "17",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsLiverRelatedAilments?.yearName ?? "",
          "Month": formManager.selectedMonthsLiverRelatedAilments?.monthNameEng ?? "",
          "Description": formManager.descriptionLiverRelatedAilmentsTextField.text,
        });
      } else if (formManager.isLiverRelatedAilments == false) {
        _healthList.add({
          "Regdid": regdId.toString(),
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

      if (formManager.isPresenceOfDyslipidemia == true &&
          (formManager.isPresenceOfDyslipidemiaUnknown == false ||
              formManager.isPresenceOfDyslipidemiaUnknown == null)) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "24",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsPresenceOfDyslipidemia?.yearName ?? "",
          "Month": formManager.selectedMonthsPresenceOfDyslipidemia?.monthNameEng ?? "",
          "Description": formManager.descriptionPresenceOfDyslipidemiaTextField.text,
        });
      } else if (formManager.isPresenceOfDyslipidemia == false ||
          formManager.isPresenceOfDyslipidemiaUnknown == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "24",
          "PhyExamStatus": formManager.isPresenceOfDyslipidemiaUnknown == true ? "2" : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (formManager.isPresenceOfDiabetes == true &&
          (formManager.isPresenceOfDiabetesUnknown == false ||
              formManager.isPresenceOfDiabetesUnknown == null)) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "25",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsPresenceOfDiabetes?.yearName ?? "",
          "Month": formManager.selectedMonthsPresenceOfDiabetes?.monthNameEng ?? "",
          "Description": formManager.descriptionPresenceOfDiabetesTextField.text,
        });
      } else if (formManager.isPresenceOfDiabetes == false ||
          formManager.isPresenceOfDiabetesUnknown == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "25",
          "PhyExamStatus": formManager.isPresenceOfDiabetesUnknown == true ? "2" : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (formManager.isPresenceOfHypertension == true &&
          (formManager.isPresenceOfHypertensionUnknown == false ||
              formManager.isPresenceOfHypertensionUnknown == null)) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "26",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsPresenceOfHypertension?.yearName ?? '0',
          "Month": formManager.selectedMonthsPresenceOfHypertension?.monthNameEng ?? '0',
          "Description": formManager.descriptionPresenceOfHypertensionTextField.text,
        });
      } else if (formManager.isPresenceOfHypertension == false ||
          formManager.isPresenceOfHypertensionUnknown == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "26",
          "PhyExamStatus": formManager.isPresenceOfHypertensionUnknown == true ? "2" : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      if (formManager.isElevatedALTs == true &&
          (formManager.isElevatedALTsUnknown == false ||
              formManager.isElevatedALTsUnknown == null)) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "21",
          "PhyExamStatus": "1",
          "Year": formManager.selectedYearsElevatedALTs?.yearName ?? '0',
          "Month": formManager.selectedMonthsElevatedALTs?.monthNameEng ?? '0',
          "Description": formManager.descriptionElevatedALTsTextField.text,
        });
      } else if (formManager.isElevatedALTs == false ||
          formManager.isElevatedALTsUnknown == true) {
        _healthList.add({
          "Regdid": regdId.toString(),
          "PhyExamTypeID": "21",
          "PhyExamStatus": formManager.isElevatedALTsUnknown == true ? "2" : "0",
          "Year": "0",
          "Month": "0",
          "Description": "",
        });
      } else {
        ToastManager.toast("Please Select Valid Input");
        return false;
      }

      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "22",
        "PhyExamStatus": formManager.isBMI == true ? "1" : "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });

      _healthList.add({
        "Regdid": regdId.toString(),
        "PhyExamTypeID": "23",
        "PhyExamStatus": formManager.isElevatedBloodGlucose == true ? "1" : "0",
        "Year": "0",
        "Month": "0",
        "Description": "",
      });
    }

    return true;
  }

  void _insertBasicInfoMaleV1New() {
    String normalOrAbnormal = formManager.isNormal == true ? "1" : "0";
    _api.insertPhysicalExaminationForHSCCV1API(
      {
        "Createdby": empCode.toString(),
        "T_PhysicalexaminationAndDescription": _jsonToString(_healthList),
        "IsAbnormal": normalOrAbnormal,
        "Camptype": campTypeID.toString(),
      },
      _onInsertResult,
    );
  }

  void _onInsertResult(
    D2DPhysicalExamninationDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      Get.dialog(S2TAlertView(
        icon: icSuccessIcon,
        message: response?.message ?? "",
        onTap: () {
          Get.back(); // close dialog
          Get.back(); // pop form screen
        },
      ));
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  bool _shouldShowNoKnownHistoryAlert() {
    if (formManager.isNoHistory == true) return false;
    final hasKnown = formManager.isAsthma == true ||
        formManager.iskidenyDisease == true ||
        formManager.isDiabetes == true ||
        formManager.isCancer == true ||
        formManager.isGynecologicalDisorder == true ||
        formManager.isThyroid == true ||
        formManager.isNeurologicalDisease == true ||
        formManager.isCardiovascularDisease == true ||
        formManager.isOther == true;
    return !hasKnown;
  }

  void _showNoKnownHistoryAlert(VoidCallback onConfirm) {
    Get.dialog(AlertDialog(
      title: Text("Alert", style: TextStyle(fontFamily: FontConstants.interFonts)),
      content: Text(
        "It seems that the patient has no known medical history. Please confirm to proceed.",
        style: TextStyle(fontFamily: FontConstants.interFonts),
      ),
      actions: [
        TextButton(
          onPressed: () {
            Get.back();
            onConfirm();
          },
          child: Text("Confirm", style: TextStyle(fontFamily: FontConstants.interFonts)),
        ),
        TextButton(
          onPressed: () => Get.back(),
          child: Text("Cancel", style: TextStyle(fontFamily: FontConstants.interFonts)),
        ),
      ],
    ));
  }

  String _jsonToString(List<Map<String, String>> items) {
    try {
      return jsonEncode(items);
    } catch (e) {
      return "";
    }
  }
}