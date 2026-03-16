// ignore_for_file: file_names, must_be_immutable

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../../../Modules/Json_Class/PatientCheckupAnalysisReportResponse/PatientCheckupAnalysisReportResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Modules/utilities/SizeConfig.dart';
import '../../../../FullScreenImageScreen/FullScreenImageScreen.dart';

class BeneficiaryVerificationInfoScreen extends StatefulWidget {
  BeneficiaryVerificationInfoScreen({
    super.key,
    required this.patientCheckupAnalysisReportOutput,
    required this.obj,
    required this.selectedBeneficiaryFile,
    required this.selectedCardFile,
    required this.onBeneficiaryImage,
    required this.onCardImage,
  });

  PatientCheckupAnalysisReportOutput? patientCheckupAnalysisReportOutput;
  BeneficiaryWorkerOutput obj;
  File? selectedBeneficiaryFile;
  File? selectedCardFile;

  Function() onBeneficiaryImage;
  Function() onCardImage;
  @override
  State<BeneficiaryVerificationInfoScreen> createState() =>
      _BeneficiaryVerificationInfoScreenState();
}

class _BeneficiaryVerificationInfoScreenState
    extends State<BeneficiaryVerificationInfoScreen> {
  bool showChangeBeneficiaryImageButton = true;
  bool showChangeCardImageButton = true;

  bool isShowRemark = false;

  int dESGID = 0;
  @override
  void initState() {
    super.initState();
    LoginOutput? userLoginDetails =
        DataProvider().getParsedUserData()?.output?.first;

    dESGID = userLoginDetails?.dESGID ?? 0;

    int photoSentForVerification = widget.obj.photoSentForVerification ?? 0;
    int photoVerificationStatusID = widget.obj.photoVerificationStatusID ?? 0;
    if (photoSentForVerification == 0 && photoVerificationStatusID > 1) {
      isShowRemark = true;
    } else {
      if (photoSentForVerification == 1) {
        isShowRemark = false;
      }
    }

    if (widget.obj.isApproved != null) {
      int isApproved = widget.obj.isApproved ?? 0;
      int photoSentForVerification = widget.obj.photoSentForVerification ?? 0;

      if (isApproved == 1 ||
          (isApproved == 3 && photoSentForVerification == 1)) {
        showChangeBeneficiaryImageButton = false;
        showChangeCardImageButton = false;
      } else if (isApproved == 2) {
        showChangeBeneficiaryImageButton = false;
        showChangeCardImageButton = false;
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
          showChangeBeneficiaryImageButton = false;
          showChangeCardImageButton = false;
        } else if (isApproved == 2) {
          showChangeBeneficiaryImageButton = false;
          showChangeCardImageButton = false;
        }
      }
    }

    int campCreatedBy = widget.obj.campCreatedBy ?? 0;
    int userId = userLoginDetails?.empCode ?? 0;

    if (campCreatedBy != userId) {
      showChangeBeneficiaryImageButton = false;
      showChangeCardImageButton = false;
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
      showChangeBeneficiaryImageButton = false;
      showChangeCardImageButton = false;
    }

    if (widget.obj.photoVerifiedByCT != null) {
      int photoVerifiedByCT = widget.obj.photoVerifiedByCT ?? 0;

      if (photoVerifiedByCT == 1) {
        showChangeBeneficiaryImageButton = false;
        showChangeCardImageButton = false;
      }
    }
    if (dESGID == 34 || dESGID == 147 || dESGID == 130 || dESGID == 141) {
      showChangeBeneficiaryImageButton = false;
      showChangeCardImageButton = false;
    }
  }


  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // const SizedBox(height: 12),
        Container(
          width: SizeConfig.screenWidth,
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    width: 22.w,
                    height: 22.h,
                    child: Image.asset(icUserIcon),
                  ),
                   SizedBox(width: 6.w),
                  Expanded(
                    child: RichText(
                      text: TextSpan(
                        text:
                            "${widget.patientCheckupAnalysisReportOutput?.patientName ?? ""} : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: 12.sp,
                        ),
                        children: [
                          TextSpan(
                            text:
                                "${widget.patientCheckupAnalysisReportOutput?.regdNo ?? ""}",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ],
                      ),
                      softWrap: true,
                    ),
                  ),
                ],
              ),
               SizedBox(height: 8.h),
              Container(
                width: SizeConfig.screenWidth,
                height: 30.h,
                decoration: BoxDecoration(
                  color: campCalenderBorder,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(5),
                    topRight: Radius.circular(5),
                  ),
                  border: Border.all(width: 1, color: campCalenderBorder),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.only(
                            topLeft: Radius.circular(5),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "Basic",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: Text(
                            "PE",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: Text(
                            "LFT",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: Text(
                            "VST",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: Text(
                            "AST",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.only(
                            topRight: Radius.circular(5),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "SC",
                            style: TextStyle(
                              color: uploadBillTitleColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
               SizedBox(height: 2.h),
              Container(
                width: SizeConfig.screenWidth,
                height: 30.h,
                decoration: BoxDecoration(
                  color: campCalenderBorder,
                  border: Border.all(width: 1, color: campCalenderBorder),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(color: Colors.white),
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.basicDetails ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.physicalExamination ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.lungFunctioinTest ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.visionScreening ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        color: Colors.white,
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.audioScreeningTest ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                     SizedBox(width: 1.w),
                    Expanded(
                      child: Container(
                        decoration: BoxDecoration(color: Colors.white),
                        child: Center(
                          child: SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              checkPatienStatus(
                                widget
                                        .patientCheckupAnalysisReportOutput
                                        ?.barcode ??
                                    "",
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        Container(
          width: SizeConfig.screenWidth,
          padding: EdgeInsets.fromLTRB(0, 6.h, 0, 0),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: Container(
                  padding: EdgeInsets.fromLTRB(8.w, 0, 0, 0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(
                        "Beneficiary Image",
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: kBlackColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: 12.sp,
                        ),
                      ),
                      // const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Container(
                            width: 70.w,
                            height: 70.h,
                            padding: EdgeInsets.symmetric(vertical: 4.h,horizontal: 4.w),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(10),
                              border: Border.all(
                                width: 1,
                                color: imageBorderColor,
                              ),
                            ),
                            child: Image.network(
                              encodeQueryComponentImageURL(
                                widget.selectedBeneficiaryFile == null
                                    ? widget.obj.regdImagePath ?? ""
                                    : widget.selectedBeneficiaryFile?.path ??
                                        "",
                              ),
                              fit: BoxFit.cover,
                              errorBuilder: (context, error, stackTrace) {
                                return Image.asset(icPhotoPlaceholder);
                              },
                            ),
                          ),
                           SizedBox(width: 8.w),
                          GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => FullScreenImageScreen(
                                        imagePath:
                                            widget.obj.regdImagePath ?? "",
                                      ),
                                ),
                              );
                            },
                            child: SizedBox(
                              width: 26.w,
                              height: 26.h,
                              child: Image.asset(icViewIcon),
                            ),
                          ),
                        ],
                      ),
                      showChangeBeneficiaryImageButton == true
                          ? SizedBox(height: 6.h)
                          : Container(),
                      showChangeBeneficiaryImageButton == true
                          ? GestureDetector(
                            onTap: () {
                              widget.onBeneficiaryImage();
                            },
                            child: SizedBox(
                              width: 70.w,
                              height: 20.h,
                              child: Center(
                                child: Text(
                                  "Change",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    color: Colors.blue,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 12.sp,
                                  ),
                                ),
                              ),
                            ),
                          )
                          : Container(),
                    ],
                  ),
                ),
              ),
              Expanded(
                child: Container(
                  color: Colors.transparent,
                  padding: EdgeInsets.fromLTRB(8.w, 0, 0, 0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(
                        width: 70.w,
                        height: 20.h,
                        child: Center(
                          child: Text(
                            "Card Image",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: 12.sp,
                            ),
                          ),
                        ),
                      ),
                       SizedBox(height: 4.h),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Container(
                            width: 70.w,
                            height: 70.h,
                            padding: EdgeInsets.symmetric(vertical: 4.h,horizontal: 4.w),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(10),
                              border: Border.all(
                                width: 1,
                                color: imageBorderColor,
                              ),
                            ),
                            child: Image.network(
                              encodeQueryComponentImageURL(
                                widget.selectedCardFile == null
                                    ? widget.obj.cardImagePath ?? ""
                                    : widget.selectedCardFile?.path ?? "",
                              ),
                              fit: BoxFit.cover,
                              errorBuilder: (context, error, stackTrace) {
                                return Image.asset(icPhotoPlaceholder);
                              },
                            ),
                          ),
                           SizedBox(width: 8.w),
                          GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => FullScreenImageScreen(
                                        imagePath:
                                            widget.obj.cardImagePath ?? "",
                                      ),
                                ),
                              );
                            },
                            child: SizedBox(
                              width: 26.w,
                              height: 26.h,
                              child: Image.asset(icViewIcon),
                            ),
                          ),
                        ],
                      ),
                      showChangeCardImageButton == true
                          ? SizedBox(height: 6.h)
                          : Container(),
                      showChangeCardImageButton == true
                          ? GestureDetector(
                            onTap: () {
                              widget.onCardImage();
                            },
                            child: SizedBox(
                              width: 70.w,
                              height: 20.h,
                              child: Center(
                                child: Text(
                                  "Change",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    color: Colors.blue,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: 12.sp,
                                  ),
                                ),
                              ),
                            ),
                          )
                          : Container(),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
        isShowRemark == true
            ? Container(
              width: SizeConfig.screenWidth,
              height: 40.h,
              color: Colors.transparent,
              child: Text(
                widget.obj.photoVerificationStatusDescription ?? "",
                textAlign: TextAlign.center,
                style: TextStyle(color: Color(0xffFF474C)),
              ),
            )
            : Container(),
      ],
    );
  }


  String checkPatienStatus(String status) {
    if (status == "0") {
      return icCrossIcon;
    }
    return icCheckIcon;
  }

  String encodeQueryComponentImageURL(String imagePath) {
    // String encodedPath1 = Uri.encodeQueryComponent(imagePath);

    return imagePath;
  }

// String cardImageURL() {
//   String cardImagePath = widget.obj.cardImagePath ?? "";
//   String encodedPath1 = Uri.encodeQueryComponent(cardImagePath);

//   return encodedPath1;
// }
}
