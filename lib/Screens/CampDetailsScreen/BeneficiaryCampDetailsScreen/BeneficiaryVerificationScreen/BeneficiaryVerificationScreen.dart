// ignore_for_file: file_names, must_be_immutable, avoid_print

import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/Json_Class/AudioScreeningDetailsResponse/AudioScreeningDetailsResponse.dart';
import '../../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../../Modules/Json_Class/BillSubmissionResponse/BillSubmissionResponse.dart';
import '../../../../Modules/Json_Class/LungFunctionTestDetailsResponse/LungFunctionTestDetailsResponse.dart';
import '../../../../Modules/Json_Class/OtherReasonForPatientRejectionResponse/OtherReasonForPatientRejectionResponse.dart';
import '../../../../Modules/Json_Class/PatientCheckupAnalysisReportResponse/PatientCheckupAnalysisReportResponse.dart';
import '../../../../Modules/Json_Class/TestListForRejectResponse/TestListForRejectResponse.dart';
import '../../../../Modules/Json_Class/VisionScreeningDetailsResponse/VisionScreeningDetailsResponse.dart';
import '../../../../Modules/MultipleAlertManager/MultipleAlertManager.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/DataProvider.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/utilities/WidgetPaddingX.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import '../../../../Modules/widgets/S2TYesNoAlertView.dart';
import '../../../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'AudioScreeningTestInfoScreen/AudioScreeningTestInfoScreen.dart';
import 'BeneficiaryVerificationInfoScreen/BeneficiaryVerificationInfoScreen.dart';
import 'BloodPressureAndSugarInfoScreen/BloodPressureAndSugarInfoScreen.dart';
import 'EditBeneficiaryInfoScreen/EditBeneficiaryInfoScreen.dart';
import 'LungFunctionTestInfoScreen/LungFunctionTestInfoScreen.dart';
import 'RejectTestInCampInfoScreen/RejectTestInCampInfoScreen.dart';
import 'VisionScreeningTestInfoScreen/VisionScreeningTestInfoScreen.dart';

class BeneficiaryVerificationScreen extends StatefulWidget {
  BeneficiaryVerificationScreen({super.key, required this.obj});

  BeneficiaryWorkerOutput obj;

  @override
  State<BeneficiaryVerificationScreen> createState() =>
      _BeneficiaryVerificationScreenState();
}

class _BeneficiaryVerificationScreenState
    extends State<BeneficiaryVerificationScreen> {
  APIManager apiManager = APIManager();

  PatientCheckupAnalysisReportOutput? patientCheckupAnalysisReportOutput;
  final MultipleAlertManager alertManager = MultipleAlertManager();
  TextEditingController otherReasonTextField = TextEditingController();
  int dESGID = 0;
  int empCode = 0;

  bool isShowPhlebotomistName = true;
  String rightRemark = "";
  String remark = "";

  bool isShowDeny = true;
  bool isShowApprove = true;

  int _apiCallCount = 0;
  VisionScreeningDetailsOutput? visionScreeningDetailsOutput;
  LungFunctionTestDetailsOutput? lungFunctionTestDetailsOutput;

  int? testToRejectID;
  String testToRejectString = "";
  bool isUserInteractionEnabled = true;

  int? reasonId;
  String reasonDescription = "";

  File? selectedBeneficiaryFile;
  File? selectedCardFile;
  String? fileType;
  bool showOtherTextField = false;

  String imageFileName = "";
  String isType = "1";

  @override
  void initState() {
    super.initState();
    requestStoragePermission();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    if (dESGID == 35 ||
        dESGID == 146 ||
        dESGID == 129 ||
        dESGID == 138 ||
        dESGID == 137 ||
        dESGID == 169 ||
        dESGID == 31 ||
        dESGID == 176 ||
        dESGID == 177) {
      isShowPhlebotomistName = false;
    } else {
      isShowPhlebotomistName = true;
    }

    if (widget.obj.isApproved != null) {
      int isApproved = widget.obj.isApproved ?? 0;
      int photoSentForVerification = widget.obj.photoSentForVerification ?? 0;

      if (isApproved == 1 ||
          (isApproved == 3 && photoSentForVerification == 1)) {
        isShowDeny = false;
      } else if (isApproved == 2) {
        isShowDeny = false;
      }
    }
    if (dESGID == 92 ||
        dESGID == 29 ||
        dESGID == 104 ||
        dESGID == 162 ||
        dESGID == 78 ||
        dESGID == 77 ||
        dESGID == 128 ||
        dESGID == 108 ||
        dESGID == 139 ||
        dESGID == 136) {
      if (widget.obj.isApproved != null) {
        int isApproved = widget.obj.isApproved ?? 0;
        int photoSentForVerification = widget.obj.photoSentForVerification ?? 0;

        if (isApproved == 1 ||
            (isApproved == 3 && photoSentForVerification == 1)) {
          isShowApprove = false;
          isShowDeny = false;
        } else if (isApproved == 2) {
          isShowApprove = false;
          isShowDeny = false;
        }
      }
    }

    int campCreatedBy = widget.obj.campCreatedBy ?? 0;

    if (campCreatedBy != empCode) {
      isShowApprove = false;
      isShowDeny = false;
    }

    if (dESGID == 77 ||
        dESGID == 84 ||
        dESGID == 30 ||
        dESGID == 35 ||
        dESGID == 86 ||
        dESGID == 64 ||
        dESGID == 129 ||
        dESGID == 146 ||
        dESGID == 138 ||
        dESGID == 169 ||
        dESGID == 177 ||
        dESGID == 137 ||
        dESGID == 176 ||
        dESGID == 31) {
      isShowApprove = false;
    }
    if (dESGID == 34 || dESGID == 147 || dESGID == 130 || dESGID == 141) {
      isShowApprove = false;
      isShowDeny = false;
    }

    if (widget.obj.testId != null) {
      testToRejectID = widget.obj.testId ?? 0;
      if (testToRejectID == 2) {
        isUserInteractionEnabled = false;
        testToRejectString = "Basic Details";
      } else if (testToRejectID == 3) {
        isUserInteractionEnabled = false;
        testToRejectString = "Physical Examination";
      } else if (testToRejectID == 4) {
        isUserInteractionEnabled = false;
        testToRejectString = "Lung Functioin Test";
      } else if (testToRejectID == 5) {
        isUserInteractionEnabled = false;
        testToRejectString = "Audio Screening Test";
      } else if (testToRejectID == 6) {
        isUserInteractionEnabled = false;
        testToRejectString = "Vision Screening";
      } else if (testToRejectID == 7) {
        isUserInteractionEnabled = false;
        testToRejectString = "Sample Collection";
      } else if (testToRejectID == 8) {
        isUserInteractionEnabled = false;
        testToRejectString = "Random Sugar Test";
      } else if (testToRejectID == 9) {
        isUserInteractionEnabled = false;
        testToRejectString = "Ackowledgement";
      }
    }
    String otherDescription = widget.obj.otherDescription ?? "";
    if (otherDescription.isNotEmpty) {
      showOtherTextField = true;
    }
    groupAPICall();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Beneficiary Verification',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),

          child: Container(
            color: Colors.white,
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: SingleChildScrollView(
              child: Column(
                children: [
                  BeneficiaryVerificationInfoScreen(
                    patientCheckupAnalysisReportOutput:
                        patientCheckupAnalysisReportOutput,
                    obj: widget.obj,
                    selectedBeneficiaryFile: selectedBeneficiaryFile,
                    selectedCardFile: selectedCardFile,
                    onBeneficiaryImage: () {
                      chooseDocumentTypeAlert(true);
                    },
                    onCardImage: () {
                      chooseDocumentTypeAlert(false);
                    },
                  ),
                  EditBeneficiaryInfoScreen(
                    beneficiaryWorkerOutput: widget.obj,
                  ),
                  AudioScreeningTestInfoScreen(
                    remark: remark,
                    rightRemark: rightRemark,
                  ),
                  VisionScreeningTestInfoScreen(
                    visionScreeningDetailsOutput: visionScreeningDetailsOutput,
                  ),
                  BloodPressureAndSugarInfoScreen(
                    visionScreeningDetailsOutput: visionScreeningDetailsOutput,
                  ),
                  LungFunctionTestInfoScreen(
                    lungFunctionTest: lungFunctionTestDetailsOutput,
                  ),

                  RejectTestInCampInfoScreen(
                    testToRejectID: testToRejectID,
                    testToRejectString: testToRejectString,
                    reasonId: reasonId,
                    reasonDescription: reasonDescription,
                    isUserInteractionEnabled: isUserInteractionEnabled,
                    otherDescription: widget.obj.otherDescription ?? "",
                    otherReasonTextField: otherReasonTextField,
                    onTestToRejectTap: () {
                      getTestToReject();
                    },
                    onReasonTap: () {
                      getOtherReasonForPatientRejection();
                    },
                  ),
                  isShowPhlebotomistName == true
                      ? const SizedBox(height: 10)
                      : Container(),
                  isShowPhlebotomistName == true
                      ? Row(
                        children: [

                          Expanded(
                            child: AppTextField(
                              readOnly: false,
                              controller: TextEditingController(
                                text: widget.obj.phleboName,
                              ),

                              inputStyle: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                fontSize: 18,
                              ),
                              label: RichText(
                                text: TextSpan(
                                  text: 'Phlebotomist Name*',
                                  style: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                    color: kLabelTextColor,
                                    fontSize: responsiveFont(20),
                                    fontWeight: FontWeight.w400,
                                  ),
                                  children: <TextSpan>[],
                                ),
                              ),
                              labelStyle: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w400,
                                fontSize: responsiveFont(20),
                              ),
                              prefixIcon: Image.asset(icMapPin, scale: 4.0),
                            ),
                          ),
                          const SizedBox(width: 10),
                          SizedBox(
                            width: 30,
                            height: 30,
                            child: Image.asset(icPhoneCallGreenIcon),
                          ),
                          const SizedBox(width: 10),
                        ],
                      )
                      : Container(),

                  const SizedBox(height: 10),
                  Container(
                    width: SizeConfig.screenWidth,
                    height: 40,
                    color: Colors.transparent,
                    child: Row(
                      children: [
                        isShowDeny == true
                            ? Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  if (denyValidation()) {
                                    showDenyPopup(
                                      context,
                                      "Are you sure you want to Deny Beneficiary Verification ?",
                                    );
                                  }
                                },
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.orange,
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: Center(
                                    child: Text(
                                      "Deny",
                                      style: TextStyle(
                                        color: Colors.black,
                                        fontFamily: FontConstants.interFonts,
                                        fontWeight: FontWeight.w600,
                                        fontSize: responsiveFont(16),
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            )
                            : Container(),
                        const SizedBox(width: 10),
                        isShowApprove == true
                            ? Expanded(
                              child: GestureDetector(
                                onTap: () {
                                  if (approveValidation()) {
                                    showApprovePopup(
                                      context,
                                      "Are you sure you want to Approve Beneficiary Verification ?",
                                    );
                                  }
                                },
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.green,
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: Center(
                                    child: Text(
                                      "Approve",
                                      style: TextStyle(
                                        color: Colors.black,
                                        fontFamily: FontConstants.interFonts,
                                        fontWeight: FontWeight.w600,
                                        fontSize: responsiveFont(16),
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            )
                            : Container(),
                      ],
                    ),
                  ),
                  const SizedBox(height: 30),
                ],
              ).paddingSymmetric(vertical: 10, horizontal: 12),
            ),
          ),
        ),
      ),
    );
  }

  groupAPICall() {
    _apiCallCount = 0;
    ToastManager.showLoader();
    getCAMPPatientCheckupAnalysisReportNewAPI();
    getAudioScreeningDetailsAPI();
    getVisionScreeningDetailsAPI();
    getLungFunctionTestDetailsAPI();
  }

  refreshUI() {
    print("refreshUI $_apiCallCount");

    if (_apiCallCount == 4) {
      ToastManager.hideLoader();
      setState(() {});
    }
  }

  void requestStoragePermission() async {
    if (!kIsWeb) {
      var status = await Permission.storage.status;
      if (!status.isGranted) {
        await Permission.storage.request();
      }

      var cameraStatus = await Permission.camera.status;
      if (!cameraStatus.isGranted) {
        await Permission.camera.request();
      }
    }
  }

  getCAMPPatientCheckupAnalysisReportNewAPI() {
    Map<String, String> params = {"RegdId": widget.obj.regdId.toString()};

    apiManager.getCAMPPatientCheckupAnalysisReportNewAPI(
      params,
      apiCAMPPatientCheckupAnalysisReportCallBack,
    );
  }

  void apiCAMPPatientCheckupAnalysisReportCallBack(
    PatientCheckupAnalysisReportResponse? response,
    String errorMessage,
    bool success,
  ) async {
    _apiCallCount += 1;
    if (success) {
      patientCheckupAnalysisReportOutput = response?.output?.first;
    }
    refreshUI();
  }

  getAudioScreeningDetailsAPI() {
    Map<String, String> params = {"RegdId": widget.obj.regdId.toString()};

    apiManager.getAudioScreeningDetailsAPI(
      params,
      apiAudioScreeningDetailsCallBack,
    );
  }

  void apiAudioScreeningDetailsCallBack(
    AudioScreeningDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    _apiCallCount += 1;
    if (success) {
      rightRemark = response?.output?.first.rightRemark ?? "";
      remark = response?.output?.first.remark ?? "";
      if (rightRemark.toLowerCase() == "Deafness".toLowerCase()) {
        alertManager.messagesList.add(
          "लाभार्थी उजव्या कानाने मूकबधिर आहे. बरोबर असल्याची खात्री करा.",
        );
      }
      if (remark.toLowerCase() == "Deafness".toLowerCase()) {
        alertManager.messagesList.add(
          "लाभार्थी डाव्या कानाने मूकबधिर आहे. बरोबर असल्याची खात्री करा.",
        );
      }
    } else {
      rightRemark = "";
      remark = "";
      // ToastManager.toast(errorMessage);
    }
    refreshUI();
  }

  getVisionScreeningDetailsAPI() {
    Map<String, String> params = {"RegdId": widget.obj.regdId.toString()};

    apiManager.getVisionScreeningDetailsAPI(
      params,
      apiVisionScreeningDetailsCallBack,
    );
  }

  void apiVisionScreeningDetailsCallBack(
    VisionScreeningDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    _apiCallCount += 1;
    if (success) {
      String rightRemark = response?.output?.first.rightRemark ?? "";
      String leftRemark = response?.output?.first.leftRemark ?? "";
      visionScreeningDetailsOutput = response?.output?.first;
      if (rightRemark.toLowerCase() == "Right Eye Blind".toLowerCase()) {
        alertManager.messagesList.add(
          "लाभार्थी उजव्या कानाने मूकबधिर आहे. बरोबर असल्याची खात्री करा.",
        );
      }
      if (leftRemark.toLowerCase() == "Left Eye Blind".toLowerCase()) {
        alertManager.messagesList.add(
          "लाभार्थी डाव्या डोळ्याने अंध आहे. बरोबर असल्याची खात्री करा.",
        );
      }
    }
    refreshUI();
  }

  getLungFunctionTestDetailsAPI() {
    Map<String, String> params = {"RegdId": widget.obj.regdId.toString()};

    apiManager.getLungFunctionTestDetailsAPI(
      params,
      apiLungFunctionTestDetailsCallBack,
    );
  }

  void apiLungFunctionTestDetailsCallBack(
    LungFunctionTestDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    _apiCallCount += 1;
    // if (success) {
    lungFunctionTestDetailsOutput = response?.output?.first;
    // }
    refreshUI();
  }

  getTestToReject() {
    ToastManager.showLoader();
    Map<String, String> params = {"RegdId": widget.obj.regdId.toString()};

    apiManager.getTestToRejectAPI(params, apiTestToRejectCallBack);
  }

  void apiTestToRejectCallBack(
    TestListForRejectResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      _showDropDownBottomSheet(
        "Test to Reject",
        response?.output ?? [],
        DropDownTypeMenu.TestToReject,
      );
    } else {
      ToastManager.toast(errorMessage);
    }

    setState(() {});
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.TestToReject) {
                TestListForRejectOutput testListForRejectOutput = p0;
                testToRejectID = testListForRejectOutput.testId ?? 0;
                testToRejectString = testListForRejectOutput.testName ?? "";
              } else if (dropDownType == DropDownTypeMenu.ReasonTest) {
                OtherReasonOutput otherReasonOutput = p0;
                reasonId = otherReasonOutput.reasonId ?? 0;
                reasonDescription = otherReasonOutput.reasonDescription ?? "";
              }

              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  getOtherReasonForPatientRejection() {
    ToastManager.showLoader();
    apiManager.getOtherReasonForPatientRejectionAPI(
      apiOtherReasonForPatientRejectionCallBack,
    );
  }

  void apiOtherReasonForPatientRejectionCallBack(
    OtherReasonForPatientRejectionResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      _showDropDownBottomSheet(
        "Reason",
        response?.output ?? [],
        DropDownTypeMenu.ReasonTest,
      );
    } else {
      ToastManager.toast(errorMessage);
    }

    setState(() {});
  }

  chooseDocumentTypeAlert(bool isBeneficiary) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Select Photo"),
          content: Text(""),
          actions: [
            TextButton(
              child: const Text("Take a Photo"),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.camera, isBeneficiary);
              },
            ),
            TextButton(
              child: const Text("Choose from Photo Library"),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.gallery, isBeneficiary);
              },
            ),
            // TextButton(
            //   child: const Text("PDF"),
            //   onPressed: () {
            //     Navigator.pop(context);
            //     _handleFilePick(FileSourceType.pdf,isBeneficiary);
            //   },
            // ),
            TextButton(
              child: const Text("Cancel"),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
          ],
        );
      },
    );
  }

  Future<void> _handleFilePick(FileSourceType type, bool isBeneficiary) async {
    final result = await ChooseDocumentManager.pickFile(type);
    if (result != null) {
      setState(() {
        fileType = result.fileType;
        if (isBeneficiary) {
          selectedBeneficiaryFile = result.file;
          isType = "1";
          String randomStirng = FormatterManager.generateRandomDigits(5);
          String timeStamp = FormatterManager.getFileNameFromDateTime();
          String imageFileName = "${randomStirng}_${timeStamp}_PR.png";
          uploadImage(selectedBeneficiaryFile!, imageFileName);
        } else {
          selectedCardFile = result.file;
          String randomStirng = FormatterManager.generateRandomDigits(5);
          String timeStamp = FormatterManager.getFileNameFromDateTime();
          String imageFileName = "${randomStirng}_${timeStamp}_HC.png";
          uploadImage(selectedCardFile!, imageFileName);
          isType = "2";
        }

        print(fileType);
      });
    }
  }

  uploadImage(File imageFile, String imageFileName) {
    int regdId = widget.obj.regdId ?? 0;
    int siteDetailId = widget.obj.siteDetailId ?? 0;
    Map<String, String> params = {
      "RegdNo": regdId.toString(),
      "IsType": isType,
      "CreatedBy": empCode.toString(),
      "SiteId": siteDetailId.toString(),
    };

    apiManager.uploadBeneficiaryVerificationAPI(
      params,
      imageFileName,
      imageFile,
      isType,
      apiUploadBeneficiaryVerificationCallBack,
    );
  }

  void apiUploadBeneficiaryVerificationCallBack(
    BillSubmissionResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      selectedBeneficiaryFile = null;
      selectedCardFile = null;
      ToastManager.toast("Photo updated successfully");
      groupAPICall();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  bool approveValidation() {
    int aLLTESTDONE = widget.obj.aLLTESTDONE ?? 0;
    if (aLLTESTDONE == 0) {
      ToastManager.toast("Beneficiary test are pending");
      return false;
    }
    return true;
  }

  bool denyValidation() {
    if (testToRejectID == null) {
      ToastManager.toast("Please select Test To Reject");
      return false;
    }
    if (reasonId == null) {
      ToastManager.toast("Please select reason");
      return false;
    }

    if (showOtherTextField) {
      if (otherReasonTextField.text.isEmpty) {
        ToastManager.toast("Please enter other description");
        return false;
      }
    }
    return true;
  }

  insertRejectOrAccept(
    String regdNo,
    String campId,
    String testId,
    String reason,
    String isApproved,
    String createdBy,
    String reasonId,
    String otherDescription,
  ) {
    Map<String, String> params = {
      "RegdNo": regdNo,
      "CampId": campId,
      "TestId": testId,
      "Reason": reason,
      "IsApproved": isApproved,
      "CreatedBy": createdBy,
      "ReasonId": reasonId,
      "OtherDescription": otherDescription,
    };

    apiManager.insertPatientRejectionInCampInCampTestV1API(
      params,
      apiInsertPatientRejectionInCampInCampTestCallBack,
    );
  }

  void apiInsertPatientRejectionInCampInCampTestCallBack(
    TestListForRejectResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        response?.message ?? "",
      );
    } else {
      ToastManager.toast(errorMessage);
    }

    setState(() {});
  }

  void showApprovePopup(BuildContext parentContext, String title) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: S2TYesNoAlertView(
            icon: icApproveIcon,
            message: title,
            onYesTap: () {
              int regdId = widget.obj.regdId ?? 0;
              int campId = widget.obj.campId ?? 0;

              insertRejectOrAccept(
                regdId.toString(),
                campId.toString(),
                testToRejectID.toString(),
                reasonDescription,
                "1",
                empCode.toString(),
                reasonId.toString(),
                otherReasonTextField.text,
              );
            },
            onNoTap: () {
              Navigator.pop(context);
            },
          ),
        );
      },
    );
  }

  void showDenyPopup(BuildContext parentContext, String title) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: S2TYesNoAlertView(
            icon: icDeniedIcon,
            message: title,
            onYesTap: () {
              int regdId = widget.obj.regdId ?? 0;
              int campId = widget.obj.campId ?? 0;

              insertRejectOrAccept(
                regdId.toString(),
                campId.toString(),
                testToRejectID.toString(),
                reasonDescription,
                "2",
                empCode.toString(),
                reasonId.toString(),
                otherReasonTextField.text,
              );
            },
            onNoTap: () {
              Navigator.pop(context);
            },
          ),
        );
      },
    );
  }
}
