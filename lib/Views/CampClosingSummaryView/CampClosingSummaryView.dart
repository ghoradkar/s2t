// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';

import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class CampClosingSummaryView extends StatefulWidget {
  CampClosingSummaryView({
    super.key,
    required this.totalApprovedBeneTextField,
    required this.sampleCollectionTextField,
    required this.rejectedBeneficiaryTextField,
  });

  TextEditingController totalApprovedBeneTextField;
  TextEditingController sampleCollectionTextField;
  TextEditingController rejectedBeneficiaryTextField;
  @override
  State<CampClosingSummaryView> createState() => _CampClosingSummaryViewState();
}

class _CampClosingSummaryViewState extends State<CampClosingSummaryView> {
  bool isExpaneded = true;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
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
                      "Camp Closing Summary",
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
                      Container(
                        height: 52,
                        decoration: const BoxDecoration(
                          color: Colors.transparent,
                        ),
                        child: Row(
                          children: [
                            // LEFT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.centerLeft,
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 10,
                                ),
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                child: Text(
                                  "Total Approved Beneficiaries",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            // RIGHT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.center,
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    right: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 20,
                                ),
                                child: TextField(
                                  textAlign: TextAlign.center,
                                  readOnly: true,
                                  controller: widget.totalApprovedBeneTextField,
                                  keyboardType: TextInputType.number,
                                  style: TextStyle(
                                    color: kBlackColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                  decoration: InputDecoration(
                                    isDense:
                                        true, // ✅ tighter but balanced height
                                    contentPadding: const EdgeInsets.symmetric(
                                      vertical: 10,
                                      horizontal: 8,
                                    ), // ✅ clean inner padding
                                    filled: true,
                                    fillColor: Colors.white,
                                    border: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
                                    ),
                                    enabledBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide(
                                        color: Colors.grey,
                                        width: 0.5,
                                      ),
                                    ),
                                    focusedBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
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
                )
                : Container(),
            // isExpaneded == true ? const SizedBox(height: 8) : Container(),
            isExpaneded == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                  child: Column(
                    children: [
                      Container(
                        height: 52,
                        decoration: const BoxDecoration(
                          color: Colors.transparent,
                        ),
                        child: Row(
                          children: [
                            // LEFT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.centerLeft,
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 10,
                                ),
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
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
                            // RIGHT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.center,
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    right: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 20,
                                ),
                                child: TextField(
                                  textAlign: TextAlign.center,
                                  readOnly: true,
                                  controller: widget.sampleCollectionTextField,
                                  keyboardType: TextInputType.number,
                                  style: TextStyle(
                                    color: kBlackColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                  decoration: InputDecoration(
                                    isDense:
                                        true, // ✅ tighter but balanced height
                                    contentPadding: const EdgeInsets.symmetric(
                                      vertical: 10,
                                      horizontal: 8,
                                    ), // ✅ clean inner padding
                                    filled: true,
                                    fillColor: Colors.white,
                                    border: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
                                    ),
                                    enabledBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide(
                                        color: Colors.grey,
                                        width: 0.5,
                                      ),
                                    ),
                                    focusedBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
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
                )
                : Container(),
            // isExpaneded == true ? const SizedBox(height: 8) : Container(),
            isExpaneded == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                  child: Column(
                    children: [
                      Container(
                        height: 52,
                        decoration: const BoxDecoration(
                          color: Colors.transparent,
                        ),
                        child: Row(
                          children: [
                            // LEFT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.centerLeft,
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 10,
                                ),
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                child: Text(
                                  "Rejected Beneficiary",
                                  style: TextStyle(
                                    color: uploadBillTitleColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ),
                            // RIGHT CELL
                            Expanded(
                              child: Container(
                                alignment: Alignment.center,
                                decoration: const BoxDecoration(
                                  color: Colors.white,
                                  border: Border(
                                    left: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    right: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                    // top: BorderSide(
                                    //   color: Colors.grey,
                                    //   width: 0.5,
                                    // ),
                                    bottom: BorderSide(
                                      color: Colors.grey,
                                      width: 0.5,
                                    ),
                                  ),
                                ),
                                padding: const EdgeInsets.symmetric(
                                  horizontal: 20,
                                ),
                                child: TextField(
                                  textAlign: TextAlign.center,
                                  readOnly: true,
                                  controller:
                                      widget.rejectedBeneficiaryTextField,
                                  keyboardType: TextInputType.number,
                                  style: TextStyle(
                                    color: kBlackColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w500,
                                    fontSize: responsiveFont(16),
                                  ),
                                  decoration: InputDecoration(
                                    isDense:
                                        true, // ✅ tighter but balanced height
                                    contentPadding: const EdgeInsets.symmetric(
                                      vertical: 10,
                                      horizontal: 8,
                                    ), // ✅ clean inner padding
                                    filled: true,
                                    fillColor: Colors.white,
                                    border: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
                                    ),
                                    enabledBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide(
                                        color: Colors.grey,
                                        width: 0.5,
                                      ),
                                    ),
                                    focusedBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide: BorderSide.none,
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
                )
                : Container(),
            // isExpaneded == true ? const SizedBox(height: 10) : Container(),
          ],
        ),
      ),
    );
  }
}
