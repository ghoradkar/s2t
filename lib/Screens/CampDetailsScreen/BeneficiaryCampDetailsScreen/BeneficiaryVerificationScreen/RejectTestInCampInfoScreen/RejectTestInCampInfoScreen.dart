// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class RejectTestInCampInfoScreen extends StatefulWidget {
  RejectTestInCampInfoScreen({
    super.key,
    required this.testToRejectID,
    required this.testToRejectString,
    required this.otherDescription,
    required this.reasonId,
    required this.reasonDescription,
    required this.otherReasonTextField,
    required this.isUserInteractionEnabled,
    required this.onTestToRejectTap,
    required this.onReasonTap,
  });

  int? testToRejectID;
  String testToRejectString = "";
  String otherDescription = "";
  bool isUserInteractionEnabled = true;
  int? reasonId;
  String reasonDescription = "";
  TextEditingController otherReasonTextField;
  Function() onTestToRejectTap;
  Function() onReasonTap;

  @override
  State<RejectTestInCampInfoScreen> createState() =>
      _RejectTestInCampInfoScreenState();
}

class _RejectTestInCampInfoScreenState
    extends State<RejectTestInCampInfoScreen> {
  bool isExpaneded = true;

  bool showOtherTextField = false;

  @override
  void initState() {
    super.initState();

    if (widget.otherDescription.isNotEmpty) {
      String otherDescription = widget.otherDescription;

      if (otherDescription.isNotEmpty) {
        showOtherTextField = true;
      }
    }
    widget.otherReasonTextField.text = widget.otherDescription;
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 4, 0, 0),
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
              padding: EdgeInsets.fromLTRB(12, 3, 12, 3),
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
                      "Reject Test in Camp",
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

            isExpaneded == true ? const SizedBox(height: 10) : Container(),

            // isExpaneded == true
            //     ? AppDropdownTextfield(
            //       icon: icScreeningTests,
            //       titleHeaderString: "Select Test to Reject*",
            //       valueString: widget.testToRejectString,
            //       onTap: () {
            //         widget.onTestToRejectTap();
            //         // getScreeningTestAPI();
            //       },
            //     )
            //     : Container(),
            isExpaneded == true
                ? AppTextField(
                  readOnly: true,
                  onTap: () {
                    widget.onTestToRejectTap();
                  },
                  controller: TextEditingController(
                    text: widget.testToRejectString,
                  ),

                  inputStyle: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 18,
                  ),
                  label: RichText(
                    text: TextSpan(
                      text: 'Select Test to Reject*',
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
                  prefixIcon: Image.asset(icScreeningTests, scale: 4.0),
                  suffixIcon: Icon(Icons.keyboard_arrow_down),
                )
                : Container(),

            isExpaneded == true ? const SizedBox(height: 8) : Container(),
            // isExpaneded == true
            //     ? Padding(
            //       padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
            //       child: AppDropdownTextfield(
            //         icon: icScreeningTests,
            //         titleHeaderString: "Reason*",
            //         valueString: widget.reasonDescription,
            //         onTap: () {
            //           widget.onReasonTap();
            //           // getScreeningTestAPI();
            //         },
            //       ),
            //     )
            //     : Container(),

            isExpaneded == true
                ? AppTextField(
              readOnly: true,
              onTap: () {
                widget.onReasonTap();
              },
              controller: TextEditingController(
                text: widget.reasonDescription,
              ),

              inputStyle: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 18,
              ),
              label: RichText(
                text: TextSpan(
                  text: 'Reason*',
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
              prefixIcon: Image.asset(icScreeningTests, scale: 4.0),
              suffixIcon: Icon(Icons.keyboard_arrow_down),
            )
                : Container(),

            // isExpaneded == true ? const SizedBox(height: 10) : Container(),
            isExpaneded == true
                ? showOtherTextField == true
                    ? const SizedBox(height: 8)
                    : Container()
                : Container(),

            // isExpaneded == true
            //     ? showOtherTextField == true
            //         ? Padding(
            //           padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
            //           child: AppIconTextfield(
            //             icon: icScreeningTests,
            //             titleHeaderString: "Other Descrption",
            //             controller: widget.otherReasonTextField,
            //           ),
            //         )
            //         : Container()
            //     : Container(),
            isExpaneded == true
                ? showOtherTextField == true
                ?  AppTextField(
              controller: TextEditingController(
                text: widget.otherReasonTextField.text,
              ),

              inputStyle: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 18,
              ),
              label: RichText(
                text: TextSpan(
                  text: 'Other Descrption',
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
              prefixIcon: Image.asset(icScreeningTests, scale: 4.0),

            ) : Container()
                : Container(),

            isExpaneded == true
                ? showOtherTextField == true
                    ? const SizedBox(height: 10)
                    : Container()
                : Container(),
          ],
        ),
      ),
    );
  }
}
