// ignore_for_file: file_names, must_be_immutable, unrelated_type_equality_checks

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/CampCloseCampDetailsResponse/CampCloseCampDetailsResponse.dart';
import '../../../Modules/Json_Class/CampCloseDetailsResponse/CampCloseDetailsResponse.dart';
import '../../../Modules/Json_Class/ConsumableListDetailsResponse/ConsumableListDetailsResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppIconTextfield.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/CampClosingColorInfoView/CampClosingColorInfoView.dart';
import '../../../Views/CampClosingScreeningDetailsView/CampClosingScreeningDetailsView.dart';
import '../../../Views/CampClosingSummaryView/CampClosingSummaryView.dart';
import '../../../Views/ConsumableConsumptionForCampView/ConsumableConsumptionForCampView.dart';

class CampClosingScreen extends StatefulWidget {
  CampClosingScreen({
    super.key,
    required this.campID,
    required this.campDate,
    required this.dISTLGDCODE,
  });

  int campID = 0;
  String campDate = "";
  int dISTLGDCODE = 0;

  @override
  State<CampClosingScreen> createState() => _CampClosingScreenState();
}

class _CampClosingScreenState extends State<CampClosingScreen> {
  bool isShowRemark = false;

  TextEditingController remarkTextField = TextEditingController();
  TextEditingController totalApprovedBeneTextField = TextEditingController();
  TextEditingController sampleCollectionTextField = TextEditingController();
  TextEditingController rejectedBeneficiaryTextField = TextEditingController();
  int facilitedWorkers = 0;
  int approvedBeneficiaries = 0;
  int rejectedBeneficiaries = 0;
  int basicDetails = 0;
  int physicalExamination = 0;
  int lungFunctioinTest = 0;
  int audioScreeningTest = 0;
  int visionScreening = 0;
  int sampleCollection = 0;
  int ackowledgement = 0;
  int totalBenificiaryTextField = 0;
  int rejectedBeneficiariesTF = 0;
  int sampleCollectionTF = 0;
  int verifiedBeneficiaries = 0;

  int totalPhysicalExam = 0;
  int totalLungTest = 0;
  int totalAudioTest = 0;
  int totalVisionTest = 0;
  int totalUrineCount = 0;
  int totalBene = 0;

  bool isUserInteractionEnabled = true;

  List<ConsumableOutput> consumableCampList = [];

  APIManager apiManager = APIManager();

  int apiCount = 0;
  bool validations() {
    int totalSampleScreened = sampleCollection;

    int totalBasicDetails = basicDetails;
    int totalPhysicalExam = physicalExamination;
    int totalLungTest = lungFunctioinTest;
    int totalAudioTest = audioScreeningTest;
    int totalVisionTest = visionScreening;
    int totalAcknowlege = ackowledgement;
    int totalBene = facilitedWorkers;
    int approvedBene = approvedBeneficiaries;
    int totalBeneApp = totalBenificiaryTextField;
    int totalBeneRej = rejectedBeneficiariesTF;
    int rejectedBene = rejectedBeneficiaries;

    int countNew = approvedBene + rejectedBene;

    int countAppRej = totalBeneApp + totalBeneRej;

    if (!(countNew == totalBene)) {
      ToastManager.showAlertDialog(
        context,
        "Camp will not be closed until rejected beneficiaries and approved beneficiary count should equal to facilitated beneficiary count",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (!(verifiedBeneficiaries == approvedBene)) {
      ToastManager.showAlertDialog(
        context,
        "Camp will not be closed until all beneficiaries are not validated by Central Camp Monitoring Team, Plz connect with them",(){
        Navigator.pop(context);

      }
      );
      return false;
    }
    if (!(countAppRej == facilitedWorkers)) {
      ToastManager.showAlertDialog(
        context,
        "Camp will not be closed until Facilitated Beneficiary count equal to addition of total approved beneficiary and rejected beneficiary count",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalSampleScreened == 0) {
      ToastManager.showAlertDialog(
        context,
        "Can't close this camp without sample collection",(){
        Navigator.pop(context);

      }
      );
      isUserInteractionEnabled = false;

      return false;
    }

    if (totalBasicDetails < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Basic Test Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalPhysicalExam < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Physical Test Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalLungTest < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Lung Test Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalAudioTest < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Audio Test Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalVisionTest < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Vision Test Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalAcknowlege < totalSampleScreened) {
      ToastManager.showAlertDialog(
        context,
        "Acknowledge Count should be equal to Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (totalBenificiaryTextField == 0) {
      ToastManager.showAlertDialog(
        context,
        "Please enter Total Beneficiary",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    if (sampleCollectionTextField == 0) {
      ToastManager.showAlertDialog(
        context,
        "Please enter Sample Collection",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else {
      if (sampleCollectionTextField != totalBenificiaryTextField) {
        isShowRemark = true;

        String remark = remarkTextField.text.trim();
        if (remark.isEmpty) {
          ToastManager.showAlertDialog(context, "Please enter remark",(){
            Navigator.pop(context);

          });
          return false;
        }
      } else {
        remarkTextField.text = "";
        isShowRemark = false;
      }
    }

    if (consumableCampList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please enter consumable details",(){
        Navigator.pop(context);

      }
      );
      return false;
    }

    return true;
  }

  @override
  void initState() {
    super.initState();

    totalApprovedBeneTextField.text = "$totalBenificiaryTextField";
    sampleCollectionTextField.text = "$sampleCollectionTF";
    rejectedBeneficiaryTextField.text = "$rejectedBeneficiariesTF";
    ToastManager.showLoader();
    getCampDetailsCount();
    getCampCloseDetails();
    getConsumableListDetails();
  }

  void getCampDetailsCount() {
    Map<String, String> params = {
      "CampId": widget.campID.toString(),
      "DISTLGDCODE": widget.dISTLGDCODE.toString(),
      "FromDate": widget.campDate,
      "ToDate": widget.campDate,
    };
    apiManager.getCampDetailsCountAPI(params, apiCampDetailsCountCallBack);
  }

  void apiCampDetailsCountCallBack(
    CampCloseCampDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    facilitedWorkers = response?.output?.first.facilitatedWorkers ?? 0;
    approvedBeneficiaries = response?.output?.first.approvedBeneficiaries ?? 0;
    rejectedBeneficiaries = response?.output?.first.rejectedBeneficiaries ?? 0;
    basicDetails = response?.output?.first.basicDetails ?? 0;
    physicalExamination = response?.output?.first.physicalExamination ?? 0;
    lungFunctioinTest = response?.output?.first.lungFunctioinTest ?? 0;
    audioScreeningTest = response?.output?.first.audioScreeningTest ?? 0;
    visionScreening = response?.output?.first.visionScreening ?? 0;
    sampleCollection = response?.output?.first.barcode ?? 0;
    ackowledgement = response?.output?.first.ackowledgement ?? 0;
    verifiedBeneficiaries = response?.output?.first.verifiedBeneficiaries ?? 0;

    rejectedBeneficiariesTF = rejectedBeneficiaries;
    rejectedBeneficiaryTextField.text = "$rejectedBeneficiariesTF";
    totalBenificiaryTextField = approvedBeneficiaries;
    sampleCollectionTF = response?.output?.first.barcode ?? 0;

    totalPhysicalExam = response?.output?.first.physicalExamination ?? 0;
    totalLungTest = response?.output?.first.lungFunctioinTest ?? 0;
    totalAudioTest = response?.output?.first.audioScreeningTest ?? 0;
    totalVisionTest = response?.output?.first.visionScreening ?? 0;

    totalUrineCount = response?.output?.first.urineSampleCollection ?? 0;
    totalBene = response?.output?.first.facilitatedWorkers ?? 0;

    apiCount += 1;
    if (apiCount == 3) {
      ToastManager.hideLoader();
    }
  }

  void getCampCloseDetails() {
    Map<String, String> params = {"CampId": widget.campID.toString()};
    apiManager.getCampCloseDetailsAPI(params, apiCampCloseDetailsCallBack);
  }

  void apiCampCloseDetailsCallBack(
    CampCloseDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    totalBenificiaryTextField = response?.output?.first.totalBenificiary ?? 0;
    totalApprovedBeneTextField.text = "$totalBenificiaryTextField";
    sampleCollectionTF = response?.output?.first.sampleCollectionCount ?? 0;
    sampleCollectionTextField.text = "$sampleCollectionTF";

    apiCount += 1;
    if (apiCount == 3) {
      ToastManager.hideLoader();
    }
  }

  void getConsumableListDetails() {
    Map<String, String> params = {"CampId": widget.campID.toString()};
    apiManager.getConsumableListDetailsAPI(
      params,
      apiConsumableListDetailsCallBack,
    );
  }

  void apiConsumableListDetailsCallBack(
    ConsumableListDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      consumableCampList = response?.output ?? [];
    } else {
      consumableCampList = [];
    }
    apiCount += 1;
    if (apiCount == 3) {
      ToastManager.hideLoader();
      setState(() {});
    }
  }

  void showBottomPopup(BuildContext parentContext) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return GestureDetector(
          onTap: () {
            Navigator.of(sheetContext).pop();
          },
          child: Container(
            width: double.infinity,
            height: MediaQuery.of(sheetContext).size.height,
            color: Colors.transparent,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 0, 30, 0),
                  child: CampClosingColorInfoView(),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Camp Closing",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
        showActions: true,
        actions: [
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
            child: GestureDetector(
              onTap: () {
                showBottomPopup(context);
              },
              child: Icon(Icons.info, color: Colors.white, size: 24.0),
            ),
          ),
        ],
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              // Positioned(
              //   top: 74,
              //   child: Image.asset(
              //     fit: BoxFit.fill,
              //     rect4,
              //     width: SizeConfig.screenWidth,
              //     height: responsiveHeight(300.37),
              //   ),
              // ),
              // Positioned(
              //   top: 53,
              //   child: Image.asset(
              //     fit: BoxFit.fill,
              //     rect3,
              //     width: SizeConfig.screenWidth,
              //     height: responsiveHeight(300.37),
              //   ),
              // ),
              // Positioned(
              //   top: 30,
              //   child: Image.asset(
              //     fit: BoxFit.fill,
              //     rect2,
              //     width: SizeConfig.screenWidth,
              //     height: responsiveHeight(300.37),
              //   ),
              // ),
              // Image.asset(
              //   fit: BoxFit.fill,
              //   rect1,
              //   width: SizeConfig.screenWidth,
              //   height: responsiveHeight(300.37),
              // ),
              Positioned(
                top: 0,
                bottom: 8,
                left: 8,
                right: 8,
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      // CampClosingColorInfoView(),
                      CampClosingScreeningDetailsView(
                        facilitedWorkers: facilitedWorkers,
                        approvedBeneficiaries: approvedBeneficiaries,
                        rejectedBeneficiaries: rejectedBeneficiaries,
                        verifiedBeneficiaries: verifiedBeneficiaries,
                        basicDetails: basicDetails,
                        physicalExamination: physicalExamination,
                        lungFunctioinTest: lungFunctioinTest,
                        audioScreeningTest: audioScreeningTest,
                        visionScreening: visionScreening,
                        sampleCollection: sampleCollection,
                        ackowledgement: ackowledgement,
                        totalPhysicalExam: totalPhysicalExam,
                        totalLungTest: totalLungTest,
                        totalAudioTest: totalAudioTest,
                        totalVisionTest: totalVisionTest,
                        totalUrineCount: totalUrineCount,
                        totalBene: totalBene,
                      ),
                      CampClosingSummaryView(
                        totalApprovedBeneTextField: totalApprovedBeneTextField,
                        sampleCollectionTextField: sampleCollectionTextField,
                        rejectedBeneficiaryTextField:
                            rejectedBeneficiaryTextField,
                      ),
                      ConsumableConsumptionForCampView(
                        consumableCampList: consumableCampList,
                      ),
                      isShowRemark == true
                          ? SizedBox(height: responsiveHeight(20))
                          : Container(),
                      isShowRemark == true
                          ? AppIconTextfield(
                            icon: icScreeningTests,
                            titleHeaderString: "Remark",
                            controller: remarkTextField,
                          )
                          : Container(),
                      SizedBox(height: responsiveHeight(26)),
                      Center(
                        child: SizedBox(
                          width: 146,
                          height: 40,
                          child: AppButtonWithIcon(
                            buttonColor:
                                isUserInteractionEnabled == true
                                    ? kButtonColor
                                    : Colors.grey,
                            title: "Close Camp",
                            icon: Image.asset(
                              iconArrow,
                              height: responsiveHeight(24),
                              width: responsiveHeight(24),
                            ),
                            mWidth: SizeConfig.screenWidth,
                            textStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color: Colors.white,
                              fontSize: responsiveFont(16),
                            ),
                            onTap: () {
                              if (isUserInteractionEnabled) {
                                if (validations()) {}
                              }
                            },
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// class CampClosingScreen extends StatefulWidget {
//   CampClosingScreen({
//     super.key,
//     required this.campID,
//     required this.campDate,
//     required this.dISTLGDCODE,
//   });

//   int campID = 0;
//   String campDate = "";
//   int dISTLGDCODE = 0;

//   @override
//   State<CampClosingScreen> createState() => _CampClosingScreenState();
// }

// class _CampClosingScreenState extends State<CampClosingScreen> {
//   bool isShowRemark = false;

//   TextEditingController remarkTextField = TextEditingController();
//   TextEditingController totalApprovedBeneTextField = TextEditingController();
//   TextEditingController sampleCollectionTextField = TextEditingController();
//   TextEditingController rejectedBeneficiaryTextField = TextEditingController();
//   int facilitedWorkers = 0;
//   int approvedBeneficiaries = 0;
//   int rejectedBeneficiaries = 0;
//   int basicDetails = 0;
//   int physicalExamination = 0;
//   int lungFunctioinTest = 0;
//   int audioScreeningTest = 0;
//   int visionScreening = 0;
//   int sampleCollection = 0;
//   int ackowledgement = 0;
//   int totalBenificiaryTextField = 0;
//   int rejectedBeneficiariesTF = 0;
//   int sampleCollectionTF = 0;
//   int verifiedBeneficiaries = 0;

//   int totalPhysicalExam = 0;
//   int totalLungTest = 0;
//   int totalAudioTest = 0;
//   int totalVisionTest = 0;
//   int totalUrineCount = 0;
//   int totalBene = 0;

//   bool isUserInteractionEnabled = true;

//   List<ConsumableOutput> consumableCampList = [];

//   APIManager apiManager = APIManager();

//   int apiCount = 0;
//   bool validations() {
//     int totalSampleScreened = sampleCollection;

//     int totalBasicDetails = basicDetails;
//     int totalPhysicalExam = physicalExamination;
//     int totalLungTest = lungFunctioinTest;
//     int totalAudioTest = audioScreeningTest;
//     int totalVisionTest = visionScreening;
//     int totalAcknowlege = ackowledgement;
//     int totalBene = facilitedWorkers;
//     int approvedBene = approvedBeneficiaries;
//     int totalBeneApp = totalBenificiaryTextField;
//     int totalBeneRej = rejectedBeneficiariesTF;
//     int rejectedBene = rejectedBeneficiaries;

//     int countNew = approvedBene + rejectedBene;

//     int countAppRej = totalBeneApp + totalBeneRej;

//     if (!(countNew == totalBene)) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Camp will not be closed until rejected beneficiaries and approved beneficiary count should equal to facilitated beneficiary count",
//       );
//       return false;
//     }

//     if (!(verifiedBeneficiaries == approvedBene)) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Camp will not be closed until all beneficiaries are not validated by Central Camp Monitoring Team, Plz connect with them",
//       );
//       return false;
//     }
//     if (!(countAppRej == facilitedWorkers)) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Camp will not be closed until Facilitated Beneficiary count equal to addition of total approved beneficiary and rejected beneficiary count",
//       );
//       return false;
//     }

//     if (totalSampleScreened == 0) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Can't close this camp without sample collection",
//       );
//       isUserInteractionEnabled = false;

//       return false;
//     }

//     if (totalBasicDetails < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Basic Test Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalPhysicalExam < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Physical Test Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalLungTest < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Lung Test Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalAudioTest < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Audio Test Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalVisionTest < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Vision Test Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalAcknowlege < totalSampleScreened) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Acknowledge Count should be equal to Sample Collection",
//       );
//       return false;
//     }

//     if (totalBenificiaryTextField == 0) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter Total Beneficiary",
//       );
//       return false;
//     }

//     if (sampleCollectionTextField == 0) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter Sample Collection",
//       );
//       return false;
//     } else {
//       if (sampleCollectionTextField != totalBenificiaryTextField) {
//         isShowRemark = true;

//         String remark = remarkTextField.text.trim();
//         if (remark.isEmpty) {
//           ToastManager.showAlertDialog(context, "Alert", "Please enter remark");
//           return false;
//         }
//       } else {
//         remarkTextField.text = "";
//         isShowRemark = false;
//       }
//     }

//     if (consumableCampList.isEmpty) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter consumable details",
//       );
//       return false;
//     }

//     return true;
//   }

//   @override
//   void initState() {
//     super.initState();

//     totalApprovedBeneTextField.text = "$totalBenificiaryTextField";
//     sampleCollectionTextField.text = "$sampleCollectionTF";
//     rejectedBeneficiaryTextField.text = "$rejectedBeneficiariesTF";
//     ToastManager.showLoader();
//     getCampDetailsCount();
//     getCampCloseDetails();
//     getConsumableListDetails();
//   }

//   getCampDetailsCount() {
//     Map<String, String> params = {
//       "CampId": widget.campID.toString(),
//       "DISTLGDCODE": widget.dISTLGDCODE.toString(),
//       "FromDate": widget.campDate,
//       "ToDate": widget.campDate,
//     };
//     apiManager.getCampDetailsCountAPI(params, apiCampDetailsCountCallBack);
//   }

//   void apiCampDetailsCountCallBack(
//     CampCloseCampDetailsResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     facilitedWorkers = response?.output?.first.facilitatedWorkers ?? 0;
//     approvedBeneficiaries = response?.output?.first.approvedBeneficiaries ?? 0;
//     rejectedBeneficiaries = response?.output?.first.rejectedBeneficiaries ?? 0;
//     basicDetails = response?.output?.first.basicDetails ?? 0;
//     physicalExamination = response?.output?.first.physicalExamination ?? 0;
//     lungFunctioinTest = response?.output?.first.lungFunctioinTest ?? 0;
//     audioScreeningTest = response?.output?.first.audioScreeningTest ?? 0;
//     visionScreening = response?.output?.first.visionScreening ?? 0;
//     sampleCollection = response?.output?.first.barcode ?? 0;
//     ackowledgement = response?.output?.first.ackowledgement ?? 0;
//     verifiedBeneficiaries = response?.output?.first.verifiedBeneficiaries ?? 0;

//     rejectedBeneficiariesTF = rejectedBeneficiaries;
//     rejectedBeneficiaryTextField.text = "$rejectedBeneficiariesTF";
//     totalBenificiaryTextField = approvedBeneficiaries;
//     sampleCollectionTF = response?.output?.first.barcode ?? 0;

//     totalPhysicalExam = response?.output?.first.physicalExamination ?? 0;
//     totalLungTest = response?.output?.first.lungFunctioinTest ?? 0;
//     totalAudioTest = response?.output?.first.audioScreeningTest ?? 0;
//     totalVisionTest = response?.output?.first.visionScreening ?? 0;

//     totalUrineCount = response?.output?.first.urineSampleCollection ?? 0;
//     totalBene = response?.output?.first.facilitatedWorkers ?? 0;

//     apiCount += 1;
//     if (apiCount == 3) {
//       ToastManager.hideLoader();
//     }
//   }

//   getCampCloseDetails() {
//     Map<String, String> params = {"CampId": widget.campID.toString()};
//     apiManager.getCampCloseDetailsAPI(params, apiCampCloseDetailsCallBack);
//   }

//   void apiCampCloseDetailsCallBack(
//     CampCloseDetailsResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     totalBenificiaryTextField = response?.output?.first.totalBenificiary ?? 0;
//     totalApprovedBeneTextField.text = "$totalBenificiaryTextField";
//     sampleCollectionTF = response?.output?.first.sampleCollectionCount ?? 0;
//     sampleCollectionTextField.text = "$sampleCollectionTF";

//     apiCount += 1;
//     if (apiCount == 3) {
//       ToastManager.hideLoader();
//     }
//   }

//   getConsumableListDetails() {
//     Map<String, String> params = {"CampId": widget.campID.toString()};
//     apiManager.getConsumableListDetailsAPI(
//       params,
//       apiConsumableListDetailsCallBack,
//     );
//   }

//   void apiConsumableListDetailsCallBack(
//     ConsumableListDetailsResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     if (success) {
//       consumableCampList = response?.output ?? [];
//     } else {
//       consumableCampList = [];
//     }
//     apiCount += 1;
//     if (apiCount == 3) {
//       ToastManager.hideLoader();
//       setState(() {});
//     }
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: mAppBar(
//         scTitle: "Camp Closing",
//         leadingIcon: iconBackArrow,
//         onLeadingIconClick: () {
//           Navigator.pop(context);
//         },
//       ),
//       body: KeyboardDismissOnTap(
//         dismissOnCapturedTaps: true,
//         child: SizedBox(
//           height: SizeConfig.screenHeight,
//           width: SizeConfig.screenWidth,
//           child: Stack(
//             children: [
//               Positioned(
//                 top: 74,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect4,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 53,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect3,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 30,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect2,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Image.asset(
//                 fit: BoxFit.fill,
//                 rect1,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//               Positioned(
//                 top: 8,
//                 bottom: 8,
//                 left: 8,
//                 right: 8,
//                 child: SingleChildScrollView(
//                   child: Column(
//                     children: [
//                       CampClosingColorInfoView(),
//                       CampClosingScreeningDetailsView(
//                         facilitedWorkers: facilitedWorkers,
//                         approvedBeneficiaries: approvedBeneficiaries,
//                         rejectedBeneficiaries: rejectedBeneficiaries,
//                         verifiedBeneficiaries: verifiedBeneficiaries,
//                         basicDetails: basicDetails,
//                         physicalExamination: physicalExamination,
//                         lungFunctioinTest: lungFunctioinTest,
//                         audioScreeningTest: audioScreeningTest,
//                         visionScreening: visionScreening,
//                         sampleCollection: sampleCollection,
//                         ackowledgement: ackowledgement,
//                         totalPhysicalExam: totalPhysicalExam,
//                         totalLungTest: totalLungTest,
//                         totalAudioTest: totalAudioTest,
//                         totalVisionTest: totalVisionTest,
//                         totalUrineCount: totalUrineCount,
//                         totalBene: totalBene,
//                       ),
//                       CampClosingSummaryView(
//                         totalApprovedBeneTextField: totalApprovedBeneTextField,
//                         sampleCollectionTextField: sampleCollectionTextField,
//                         rejectedBeneficiaryTextField:
//                             rejectedBeneficiaryTextField,
//                       ),
//                       ConsumableConsumptionForCampView(
//                         consumableCampList: consumableCampList,
//                       ),
//                       isShowRemark == true
//                           ? SizedBox(height: responsiveHeight(20))
//                           : Container(),
//                       isShowRemark == true
//                           ? AppIconTextfield(
//                             icon: icScreeningTests,
//                             titleHeaderString: "Remark",
//                             controller: remarkTextField,
//                           )
//                           : Container(),
//                       SizedBox(height: responsiveHeight(26)),
//                       Center(
//                         child: SizedBox(
//                           width: 146,
//                           height: 40,
//                           child: AppButtonWithIcon(
//                             buttonColor:
//                                 isUserInteractionEnabled == true
//                                     ? kButtonColor
//                                     : Colors.grey,
//                             title: "Close Camp",
//                             icon: Image.asset(
//                               iconArrow,
//                               height: responsiveHeight(24),
//                               width: responsiveHeight(24),
//                             ),
//                             mWidth: SizeConfig.screenWidth,
//                             textStyle: TextStyle(
//                               fontFamily: FontConstants.interFonts,
//                               color: Colors.white,
//                               fontSize: responsiveFont(16),
//                             ),
//                             onTap: () {
//                               if (isUserInteractionEnabled) {
//                                 if (validations()) {}
//                               }
//                             },
//                           ),
//                         ),
//                       ),
//                     ],
//                   ),
//                 ),
//               ),
//             ],
//           ),
//         ),
//       ),
//     );
//   }
// }
