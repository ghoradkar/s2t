// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class CampClosingScreeningDetailsView extends StatefulWidget {
  CampClosingScreeningDetailsView({
    super.key,
    required this.facilitedWorkers,
    required this.approvedBeneficiaries,
    required this.rejectedBeneficiaries,
    required this.basicDetails,
    required this.physicalExamination,
    required this.lungFunctioinTest,
    required this.audioScreeningTest,
    required this.visionScreening,
    required this.sampleCollection,
    required this.ackowledgement,
    required this.verifiedBeneficiaries,
    required this.totalPhysicalExam,
    required this.totalLungTest,
    required this.totalAudioTest,
    required this.totalVisionTest,
    required this.totalUrineCount,
    required this.totalBene,
  });

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
  int verifiedBeneficiaries = 0;

  int totalPhysicalExam = 0;
  int totalLungTest = 0;
  int totalAudioTest = 0;
  int totalVisionTest = 0;
  int totalUrineCount = 0;
  int totalBene = 0;
  @override
  State<CampClosingScreeningDetailsView> createState() =>
      _CampClosingScreeningDetailsViewState();
}

class _CampClosingScreeningDetailsViewState
    extends State<CampClosingScreeningDetailsView> {
  bool isExpaneded = true;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 8, 0, 0),
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
              padding: EdgeInsets.fromLTRB(12, 4, 12, 4),
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
                      "Screening Details",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 30,
                      height: 30,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),

            isExpaneded == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                  child: Column(
                    children: [
                      // const SizedBox(height: 4),
                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                          // borderRadius: BorderRadius.only(
                          //   topLeft: Radius.circular(10),
                          //   topRight: Radius.circular(10),
                          // ),
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Facilitated Beneficiary",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.facilitedWorkers}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      // const SizedBox(height: 4),
                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Approved Beneficiaries",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.approvedBeneficiaries}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: approvedBeneficiariesColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Rejected Beneficiaries",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.rejectedBeneficiaries}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: rejectedBeneficiariesColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Beneficiaries Verified",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.verifiedBeneficiaries}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: beneficiariesVerifiedColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Basic Details",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.basicDetails}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Physical Examination",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.physicalExamination}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Lung Function Test",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.lungFunctioinTest}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Audio Screening Test",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.audioScreeningTest}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Vision Screening Test",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.visionScreening}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Sample Collection",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.sampleCollection}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),

                      Container(
                        decoration: BoxDecoration(
                          border: Border(
                            left: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            right: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                            bottom: BorderSide(
                              color: Color(0xffDEDEDE),
                              width: 0.5,
                            ),
                          ),
                          color: kWhiteColor,
                          // borderRadius: BorderRadius.only(
                          //   bottomLeft: Radius.circular(10),
                          //   bottomRight: Radius.circular(10),
                          // ),
                        ),
                        width: SizeConfig.screenWidth,
                        height: 36,
                        child: Row(
                          children: [
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                                color: Colors.transparent,
                                child: Text(
                                  "Acknowledgement",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              width: 102,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
                              child: Text(
                                "${widget.ackowledgement}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: uploadBillTitleColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(16),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 8),
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
