// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/AdvancesRequestDetailsShowResponse/AdvancesRequestDetailsShowResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';

class RequestBillDetailsScreen extends StatefulWidget {
  RequestBillDetailsScreen({super.key, required this.campid});

  int campid = 0;
  @override
  State<RequestBillDetailsScreen> createState() =>
      _RequestBillDetailsScreenState();
}

class _RequestBillDetailsScreenState extends State<RequestBillDetailsScreen> {
  APIManager apiManager = APIManager();

  AdvancesRequestDetailsShowOutput? advancesRequestDetails;
  @override
  void initState() {
    super.initState();
    getBillSubmitdetailsShow();
  }

  void getBillSubmitdetailsShow() {
    ToastManager.showLoader();
    Map<String, String> params = {"campid": widget.campid.toString()};
    apiManager.getBillSubmitdetailsShowAPI(
      params,
      apiBillSubmitdetailsShowBack,
    );
  }

  void apiBillSubmitdetailsShowBack(
    AdvancesRequestDetailsShowResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      advancesRequestDetails = response?.output?.first;
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Request Bill Details",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
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
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    // boxShadow: [
                    //   BoxShadow(
                    //     color: Colors.black.withValues(alpha: 0.15),
                    //     blurRadius: 10,
                    //   ),
                    // ],
                    // borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(0, 8, 0, 8),
                    child: SingleChildScrollView(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icHashIcon),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Camp ID : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              '${advancesRequestDetails?.campid ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icMapPin),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "District : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              advancesRequestDetails
                                                  ?.dISTNAME ??
                                              "",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icCalendarMonth),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Camp Date : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              advancesRequestDetails
                                                  ?.campDate ??
                                              "",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icUsersGroup),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Expected Beneficiary : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              '${advancesRequestDetails?.expectedbeneficiarycount ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),

                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icAdvance),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Total Advance Taken : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.totalAdvanceTaken ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(iconPerson),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Fund Requested By : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              '${advancesRequestDetails?.fundRequestedBy ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icBeneficiary),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Beneficiary Refreshment : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.beneficiaryRefreshment ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icAwareness),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Camp Awareness using Bhopu : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.campAwarenessUsingBhopu ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icHall),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text:
                                              "Camp Hall Grampanchayat/School/Govt. Office/Tent : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.campHallGramPanchayatSchoolGovtOfficeTent ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icChairs),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Chairs : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.chairs ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icCleaning),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Cleaning Charges : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.cleaningCharges ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icDrinking),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Drinking Water : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.drinkingWater ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icFood),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text:
                                              "Food to Staff (TA) Allowance : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.foodToStaffTAAllowance ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icnTent),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Post Camp Expense : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text: 'RS: 0',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icRunnerBoy),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text:
                                              'Sample Movement to Lab - TSRTC or Any other Cargo : ',
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.sampleMovementToLabRunnerBoy ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icBillUploadedIcon),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text:
                                              "Transportation of Staff (TA) Group of Transport : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.transportationOfStaffTAGroupOfTransport ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icTransportation),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text:
                                              "Transportation of Staff (TA) Individual : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.transportationOfStaffTAIndividual ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icBillUploadedIcon),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Total Amount : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              'RS. ${advancesRequestDetails?.totalBillSubmittedAmt ?? 0}',
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SizedBox(
                            width: SizeConfig.screenWidth,
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                SizedBox(
                                  width: responsiveWidth(22),
                                  height: responsiveWidth(22),
                                  child: Image.asset(icProgressIcon),
                                ),
                                const SizedBox(width: 8),
                                Expanded(
                                  child: RichText(
                                    text: TextSpan(
                                      children: [
                                        TextSpan(
                                          text: "Approval Status : ",
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                        TextSpan(
                                          text:
                                              advancesRequestDetails
                                                  ?.approvalStatus ??
                                              "",
                                          style: TextStyle(
                                            color: dropDownTitleHeader,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ],
                                    ),
                                    textAlign: TextAlign.start,
                                    maxLines: null, // allow multiline
                                    overflow: TextOverflow.visible,
                                  ),
                                ),
                                // Expanded(
                                //   child: Row(
                                //     mainAxisAlignment: MainAxisAlignment.start,
                                //     crossAxisAlignment:
                                //         CrossAxisAlignment.start,
                                //     children: [
                                //       Text(
                                //         "Approval Status : ",
                                //         style: TextStyle(
                                //           color: Colors.black,
                                //           fontFamily: FontConstants.interFonts,
                                //           fontWeight: FontWeight.w500,
                                //           fontSize: responsiveFont(16),
                                //         ),
                                //       ),
                                //       Expanded(
                                //         child: Text(
                                //           "Pending",
                                //           style: TextStyle(
                                //             color: dropDownTitleHeader,
                                //             fontFamily: FontConstants.interFonts,
                                //             fontWeight: FontWeight.w400,
                                //             fontSize: responsiveFont(16),
                                //           ),
                                //         ),
                                //       ),
                                //     ],
                                //   ),
                                // ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
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

// class RequestBillDetailsScreen extends StatefulWidget {
//   RequestBillDetailsScreen({super.key, required this.campid});

//   int campid = 0;
//   @override
//   State<RequestBillDetailsScreen> createState() =>
//       _RequestBillDetailsScreenState();
// }

// class _RequestBillDetailsScreenState extends State<RequestBillDetailsScreen> {
//   APIManager apiManager = APIManager();

//   AdvancesRequestDetailsShowOutput? advancesRequestDetails;
//   @override
//   void initState() {
//     super.initState();
//     getBillSubmitdetailsShow();
//   }

//   void getBillSubmitdetailsShow() {
//     ToastManager.showLoader();
//     Map<String, String> params = {"campid": widget.campid.toString()};
//     apiManager.getBillSubmitdetailsShowAPI(
//       params,
//       apiBillSubmitdetailsShowBack,
//     );
//   }

//   void apiBillSubmitdetailsShowBack(
//     AdvancesRequestDetailsShowResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       advancesRequestDetails = response?.output?.first;
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return Scaffold(
//       appBar: mAppBar(
//         scTitle: "Request Bill Details",
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
//                 top: 0,
//                 bottom: 8,
//                 left: 8,
//                 right: 8,
//                 child: Container(
//                   decoration: BoxDecoration(
//                     color: Colors.white,
//                     boxShadow: [
//                       BoxShadow(
//                         color: Colors.black.withValues(alpha: 0.15),
//                         blurRadius: 10,
//                       ),
//                     ],
//                     borderRadius: BorderRadius.circular(10),
//                   ),
//                   child: Padding(
//                     padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
//                     child: SingleChildScrollView(
//                       child: Column(
//                         mainAxisAlignment: MainAxisAlignment.start,
//                         crossAxisAlignment: CrossAxisAlignment.start,
//                         children: [
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icHashIcon),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Camp ID : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               '${advancesRequestDetails?.campid ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icMapPin),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "District : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               advancesRequestDetails
//                                                   ?.dISTNAME ??
//                                               "",
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icCalendarMonth),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Camp Date : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               advancesRequestDetails
//                                                   ?.campDate ??
//                                               "",
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icUsersGroup),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Expected Beneficiary : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               '${advancesRequestDetails?.expectedbeneficiarycount ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),

//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icAdvance),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Total Advance Taken : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.totalAdvanceTaken ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(iconPerson),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Fund Requested By : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               '${advancesRequestDetails?.fundRequestedBy ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icBeneficiary),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Beneficiary Refreshment : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.beneficiaryRefreshment ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icAwareness),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Camp Awareness using Bhopu : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.campAwarenessUsingBhopu ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icHall),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text:
//                                               "Camp Hall Grampanchayat/School/Govt. Office/Tent : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.campHallGramPanchayatSchoolGovtOfficeTent ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icChairs),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Chairs : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.chairs ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icCleaning),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Cleaning Charges : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.cleaningCharges ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icDrinking),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Drinking Water : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.drinkingWater ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icFood),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text:
//                                               "Food to Staff (TA) Allowance : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.foodToStaffTAAllowance ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icnTent),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Post Camp Expense : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text: 'RS: 0',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icRunnerBoy),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text:
//                                               'Sample Movement to Lab - TSRTC or Any other Cargo : ',
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.sampleMovementToLabRunnerBoy ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icBillUploadedIcon),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text:
//                                               "Transportation of Staff (TA) Group of Transport : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.transportationOfStaffTAGroupOfTransport ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icTransportation),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text:
//                                               "Transportation of Staff (TA) Individual : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.transportationOfStaffTAIndividual ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icBillUploadedIcon),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Total Amount : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               'RS. ${advancesRequestDetails?.totalBillSubmittedAmt ?? 0}',
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                               ],
//                             ),
//                           ),
//                           const SizedBox(height: 8),
//                           SizedBox(
//                             width: SizeConfig.screenWidth,
//                             child: Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.center,
//                               children: [
//                                 SizedBox(
//                                   width: responsiveHeight(30),
//                                   height: responsiveHeight(30),
//                                   child: Image.asset(icProgressIcon),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: RichText(
//                                     text: TextSpan(
//                                       children: [
//                                         TextSpan(
//                                           text: "Approval Status : ",
//                                           style: TextStyle(
//                                             color: Colors.black,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w500,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                         TextSpan(
//                                           text:
//                                               advancesRequestDetails
//                                                   ?.approvalStatus ??
//                                               "",
//                                           style: TextStyle(
//                                             color: dropDownTitleHeader,
//                                             fontFamily:
//                                                 FontConstants.interFonts,
//                                             fontWeight: FontWeight.w400,
//                                             fontSize: responsiveFont(16),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     textAlign: TextAlign.start,
//                                     maxLines: null, // allow multiline
//                                     overflow: TextOverflow.visible,
//                                   ),
//                                 ),
//                                 // Expanded(
//                                 //   child: Row(
//                                 //     mainAxisAlignment: MainAxisAlignment.start,
//                                 //     crossAxisAlignment:
//                                 //         CrossAxisAlignment.start,
//                                 //     children: [
//                                 //       Text(
//                                 //         "Approval Status : ",
//                                 //         style: TextStyle(
//                                 //           color: Colors.black,
//                                 //           fontFamily: FontConstants.interFonts,
//                                 //           fontWeight: FontWeight.w500,
//                                 //           fontSize: responsiveFont(16),
//                                 //         ),
//                                 //       ),
//                                 //       Expanded(
//                                 //         child: Text(
//                                 //           "Pending",
//                                 //           style: TextStyle(
//                                 //             color: dropDownTitleHeader,
//                                 //             fontFamily: FontConstants.interFonts,
//                                 //             fontWeight: FontWeight.w400,
//                                 //             fontSize: responsiveFont(16),
//                                 //           ),
//                                 //         ),
//                                 //       ),
//                                 //     ],
//                                 //   ),
//                                 // ),
//                               ],
//                             ),
//                           ),
//                         ],
//                       ),
//                     ),
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
