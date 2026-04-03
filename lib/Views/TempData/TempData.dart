/*

// void _showFilterBottomSheet() {
  //   showModalBottomSheet(
  //     context: context,
  //     isScrollControlled: true,
  //     constraints: const BoxConstraints(minWidth: double.infinity),
  //     backgroundColor: Colors.white,
  //     isDismissible: true,
  //     enableDrag: true,
  //     builder: (BuildContext context) {
  //       return Container(
  //         width: double.infinity,
  //         height: MediaQuery.of(context).size.width * 1.13,
  //         decoration: const BoxDecoration(
  //           color: Colors.white,
  //           borderRadius: BorderRadius.only(
  //             topLeft: Radius.circular(20),
  //             topRight: Radius.circular(20),
  //           ),
  //         ),
  //         child: CampReadinessFormFilterScreen(
  //           campDate: _selectedCampDate,
  //           onTapFilter: (p0, p1) {
  //             selectedCampType = p0;
  //             selectedCampID = p1;
  //             getCampReadinessFormItems();
  //           },
  //         ),
  //       );
  //     },
  //   ).whenComplete(() {
  //     setState(() {});
  //   });
  // }

  getCampReadinessFormItems() {
    ToastManager.showLoader();
    apiManager.getCampReadinessFormItemsAPI(
      selectedCampType?.cAMPTYPE ?? 0,
      selectedCampID?.campId ?? 0,
      apiCampReadinessFormItemsCallBack,
    );
  }

  void apiCampReadinessFormItemsCallBack(
    CampListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campList = response?.output ?? [];
    } else {
      campList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/CampIdListResponse/CampIdListResponse.dart';
import '../../Modules/Json_Class/CampListResponse/camp_list_response.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import 'CampReadinessFormFilterScreen/CampReadinessFormFilterScreen.dart';

class CampReadinessFormScreen extends StatefulWidget {
  const CampReadinessFormScreen({super.key});

  @override
  State<CampReadinessFormScreen> createState() =>
      _CampReadinessFormScreenState();
}

class _CampReadinessFormScreenState extends State<CampReadinessFormScreen> {
  final formKey = GlobalKey<FormState>();
  String _selectedCampDate = "";
  CampTypeOutput? selectedCampType;
  CampIdOutput? selectedCampID;

  List<CampListOutput> campList = [];
  ToastManager toastManager = ToastManager();
  APIManager apiManager = APIManager();
  @override
  void initState() {
    super.initState();
    _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
  }

  void _showFilterBottomSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.13,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: CampReadinessFormFilterScreen(
            campDate: _selectedCampDate,
            onTapFilter: (p0, p1) {
              selectedCampType = p0;
              selectedCampID = p1;
              getCampReadinessFormItems();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  getCampReadinessFormItems() {
    ToastManager.showLoader();
    apiManager.getCampReadinessFormItemsAPI(
      selectedCampType?.cAMPTYPE ?? 0,
      selectedCampID?.campId ?? 0,
      apiCampReadinessFormItemsCallBack,
    );
  }

  void apiCampReadinessFormItemsCallBack(
    CampListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campList = response?.output ?? [];
    } else {
      campList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Camp Readiness Form",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
          showActions: true,
          actions: [
            GestureDetector(
              onTap: () {
                _showFilterBottomSheet();
              },
              child: Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                child: Container(
                  padding: EdgeInsets.all(4),
                  width: 30,
                  height: 30,
                  child: Image.asset(icFilter),
                ),
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
                Positioned(
                  top: 74,
                  child: Image.asset(
                    fit: BoxFit.fill,
                    rect4,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(300.37),
                  ),
                ),
                Positioned(
                  top: 53,
                  child: Image.asset(
                    fit: BoxFit.fill,
                    rect3,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(300.37),
                  ),
                ),
                Positioned(
                  top: 30,
                  child: Image.asset(
                    fit: BoxFit.fill,
                    rect2,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(300.37),
                  ),
                ),
                Image.asset(
                  fit: BoxFit.fill,
                  rect1,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
                Positioned(
                  top: 0,
                  bottom: 8,
                  left: 8,
                  right: 8,
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
                    child: ListView.builder(
                      itemCount: campList.length,
                      // shrinkWrap: true,
                      // physics: NeverScrollableScrollPhysics(),
                      itemBuilder: (context, index) {
                        CampListOutput object = campList[index];
                        return Padding(
                          padding: EdgeInsets.fromLTRB(0, 0, 0, 8),
                          child: GestureDetector(
                            onTap: () {
                              toastManager.showSuccessOpup(
                                context,
                                icWarningIcon,
                                "Please Fill Camp Readiness Form.",
                              );
                            },
                            child: Container(
                              padding: EdgeInsets.all(12),
                              width: SizeConfig.screenWidth,
                              // height: 120,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.15),
                                    blurRadius: 10,
                                  ),
                                ],
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Column(
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 24,
                                        height: 24,
                                        child: Image.asset(icHashIcon),
                                      ),
                                      const SizedBox(width: 8),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text: "Camp ID : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily: FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(16),
                                            ),
                                            children: [
                                              TextSpan(
                                                text: "${object.campId ?? 0}",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily: FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: responsiveFont(16),
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 8),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 24,
                                        height: 24,
                                        child: Image.asset(icMapPin),
                                      ),
                                      const SizedBox(width: 8),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text: "District : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily: FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(16),
                                            ),
                                            children: [
                                              TextSpan(
                                                text: object.dISTNAME ?? "",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily: FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(16),
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 8),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 24,
                                        height: 24,
                                        child: Image.asset(icnTent),
                                      ),
                                      const SizedBox(width: 8),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text: "Camp Type : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily: FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(16),
                                            ),
                                            children: [
                                              TextSpan(
                                                text:
                                                    selectedCampType
                                                        ?.campTypeDescription ??
                                                    "",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily: FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(16),
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),

                                  const SizedBox(height: 8),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 24,
                                        height: 24,
                                        child: Image.asset(icInitiatedByIcon),
                                      ),
                                      const SizedBox(width: 8),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text: "Camp Name : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily: FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(16),
                                            ),
                                            children: [
                                              TextSpan(
                                                text: "${object.campName ?? 0}",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily: FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(16),
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(height: 8),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 24,
                                        height: 24,
                                        child: Image.asset(icInitiatedBy),
                                      ),
                                      const SizedBox(width: 8),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text: "Created By : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily: FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(16),
                                            ),
                                            children: [
                                              TextSpan(
                                                text:
                                                    object.campCreatedBy ?? "",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily: FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(16),
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                              //
                              //icHashIcon
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
        // AnnotatedRegion(
        //   value: SystemUiOverlayStyle(
        //     statusBarColor: kPrimaryColor,
        //     statusBarBrightness: Brightness.dark,
        //     statusBarIconBrightness: Brightness.light,
        //   ),
        //   child: SingleChildScrollView(
        //     child: Container(
        //       color: Colors.white,
        //       height: SizeConfig.screenHeight + 50,
        //       width: SizeConfig.screenWidth,
        //       child: Stack(
        //         children: [
        //           Positioned(
        //             top: 74,
        //             child: Image.asset(
        //               fit: BoxFit.fill,
        //               rect4,
        //               width: SizeConfig.screenWidth,
        //               height: responsiveHeight(300.37),
        //             ),
        //           ),
        //           Positioned(
        //             top: 53,
        //             child: Image.asset(
        //               fit: BoxFit.fill,
        //               rect3,
        //               width: SizeConfig.screenWidth,
        //               height: responsiveHeight(300.37),
        //             ),
        //           ),
        //           Positioned(
        //             top: 30,
        //             child: Image.asset(
        //               fit: BoxFit.fill,
        //               rect2,
        //               width: SizeConfig.screenWidth,
        //               height: responsiveHeight(300.37),
        //             ),
        //           ),
        //           Image.asset(
        //             fit: BoxFit.fill,
        //             rect1,
        //             width: SizeConfig.screenWidth,
        //             height: responsiveHeight(300.37),
        //           ),
        //           Positioned(
        //             top: 44,
        //             child: Container(
        //               width: MediaQuery.of(context).size.width,
        //               decoration: BoxDecoration(
        //                 color: Colors.transparent,

        //                 borderRadius: BorderRadius.circular(10),
        //               ),
        //               child: Column(
        //                 crossAxisAlignment: CrossAxisAlignment.start,
        //                 children: [Container(color: Colors.amber)],
        //               ),
        //             ),
        //           ),
        //           // Positioned(
        //           //   top: responsiveHeight(350),
        //           //   right: 10,
        //           //   left: 10,
        //           //   child: Center(
        //           //     child: SizedBox(
        //           //       height: SizeConfig.screenHeight * 2,
        //           //       width: SizeConfig.screenWidth * 0.9,
        //           //       child: Form(
        //           //         key: formKey,
        //           //         child: Column(
        //           //           children: [SizedBox(height: responsiveHeight(27))],
        //           //         ),
        //           //       ),
        //           //     ),
        //           //   ),
        //           // ),
        //         ],
        //       ),
        //     ),
        //   ),
        // ),
      ),
    );
    // Scaffold(
    //   appBar: mAppBar(
    //     scTitle: "Camp Readiness Form",
    //     leadingIcon: iconBackArrow,
    //     onLeadingIconClick: () {
    //       Navigator.pop(context);
    //     },
    //     actions: [
    //       GestureDetector(
    //         onTap: () {
    //           _showFilterBottomSheet();
    //         },
    //         child: SizedBox(
    //           width: 30,
    //           height: 30,
    //           child: Image.asset(icFilter),
    //         ),
    //       ),
    //     ],
    //   ),
    //   body: KeyboardDismissOnTap(
    //     dismissOnCapturedTaps: true,
    //     child: SizedBox(
    //       height: SizeConfig.screenHeight,
    //       width: SizeConfig.screenWidth,
    //       child: Stack(
    //         children: [
    //           Positioned(
    //             top: 74,
    //             child: Image.asset(
    //               fit: BoxFit.fill,
    //               rect4,
    //               width: SizeConfig.screenWidth,
    //               height: responsiveHeight(300.37),
    //             ),
    //           ),
    //           Positioned(
    //             top: 53,
    //             child: Image.asset(
    //               fit: BoxFit.fill,
    //               rect3,
    //               width: SizeConfig.screenWidth,
    //               height: responsiveHeight(300.37),
    //             ),
    //           ),
    //           Positioned(
    //             top: 30,
    //             child: Image.asset(
    //               fit: BoxFit.fill,
    //               rect2,
    //               width: SizeConfig.screenWidth,
    //               height: responsiveHeight(300.37),
    //             ),
    //           ),
    //           Image.asset(
    //             fit: BoxFit.fill,
    //             rect1,
    //             width: SizeConfig.screenWidth,
    //             height: responsiveHeight(300.37),
    //           ),
    //           Positioned(
    //             top: 0,
    //             bottom: 8,
    //             left: 8,
    //             right: 8,
    //             child: Container(
    //               decoration: BoxDecoration(
    //                 color: Colors.white,
    //                 boxShadow: [
    //                   BoxShadow(
    //                     color: Colors.black.withValues(alpha: 0.15),
    //                     blurRadius: 10,
    //                   ),
    //                 ],
    //                 borderRadius: BorderRadius.circular(10),
    //               ),
    //             ),
    //           ),
    //         ],
    //       ),
    //     ),
    //   ),
    // );
  }
}

// Scaffold(
      //   appBar: mAppBar(
      //     scTitle: "Camp Readiness Form",
      //     leadingIcon: iconBackArrow,
      //     onLeadingIconClick: () {
      //       Navigator.pop(context);
      //     },
      //     showActions: true,
      //     actions: [
      //       GestureDetector(
      //         onTap: () {
      //           _showFilterBottomSheet();
      //         },
      //         child: Padding(
      //           padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
      //           child: Container(
      //             padding: EdgeInsets.all(4),
      //             width: 30,
      //             height: 30,
      //             child: Image.asset(icFilter),
      //           ),
      //         ),
      //       ),
      //     ],
      //   ),
      //   body: KeyboardDismissOnTap(
      //     dismissOnCapturedTaps: true,
      //     child: SizedBox(
      //       height: SizeConfig.screenHeight,
      //       width: SizeConfig.screenWidth,
      //       child: Stack(
      //         children: [
      //           Positioned(
      //             top: 74,
      //             child: Image.asset(
      //               fit: BoxFit.fill,
      //               rect4,
      //               width: SizeConfig.screenWidth,
      //               height: responsiveHeight(300.37),
      //             ),
      //           ),
      //           Positioned(
      //             top: 53,
      //             child: Image.asset(
      //               fit: BoxFit.fill,
      //               rect3,
      //               width: SizeConfig.screenWidth,
      //               height: responsiveHeight(300.37),
      //             ),
      //           ),
      //           Positioned(
      //             top: 30,
      //             child: Image.asset(
      //               fit: BoxFit.fill,
      //               rect2,
      //               width: SizeConfig.screenWidth,
      //               height: responsiveHeight(300.37),
      //             ),
      //           ),
      //           Image.asset(
      //             fit: BoxFit.fill,
      //             rect1,
      //             width: SizeConfig.screenWidth,
      //             height: responsiveHeight(300.37),
      //           ),
      //           Positioned(
      //             top: 0,
      //             bottom: 8,
      //             left: 8,
      //             right: 8,
      //             child: Padding(
      //               padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
      //               child: ListView.builder(
      //                 itemCount: campList.length,
      //                 // shrinkWrap: true,
      //                 // physics: NeverScrollableScrollPhysics(),
      //                 itemBuilder: (context, index) {
      //                   CampListOutput object = campList[index];
      //                   return Padding(
      //                     padding: EdgeInsets.fromLTRB(0, 0, 0, 8),
      //                     child: GestureDetector(
      //                       onTap: () {
      //                         toastManager.showSuccessOpup(
      //                           context,
      //                           icWarningIcon,
      //                           "Please Fill Camp Readiness Form.",
      //                         );
      //                       },
      //                       child: Container(
      //                         padding: EdgeInsets.all(12),
      //                         width: SizeConfig.screenWidth,
      //                         // height: 120,
      //                         decoration: BoxDecoration(
      //                           color: Colors.white,
      //                           boxShadow: [
      //                             BoxShadow(
      //                               color: Colors.black.withValues(alpha: 0.15),
      //                               blurRadius: 10,
      //                             ),
      //                           ],
      //                           borderRadius: BorderRadius.circular(10),
      //                         ),
      //                         child: Column(
      //                           children: [
      //                             Row(
      //                               mainAxisAlignment: MainAxisAlignment.start,
      //                               crossAxisAlignment:
      //                                   CrossAxisAlignment.center,
      //                               children: [
      //                                 SizedBox(
      //                                   width: 24,
      //                                   height: 24,
      //                                   child: Image.asset(icHashIcon),
      //                                 ),
      //                                 const SizedBox(width: 8),
      //                                 Expanded(
      //                                   child: RichText(
      //                                     text: TextSpan(
      //                                       text: "Camp ID : ",
      //                                       style: TextStyle(
      //                                         color: Colors.black,
      //                                         fontFamily: FontConstants.interFonts,
      //                                         fontWeight: FontWeight.w500,
      //                                         fontSize: responsiveFont(16),
      //                                       ),
      //                                       children: [
      //                                         TextSpan(
      //                                           text: "${object.campId ?? 0}",
      //                                           style: TextStyle(
      //                                             color: dropDownTitleHeader,
      //                                             fontFamily: FontConstants.interFonts,
      //                                             fontWeight: FontWeight.w400,
      //                                             fontSize: responsiveFont(16),
      //                                           ),
      //                                         ),
      //                                       ],
      //                                     ),
      //                                     softWrap: true,
      //                                   ),
      //                                 ),
      //                               ],
      //                             ),
      //                             const SizedBox(height: 8),
      //                             Row(
      //                               mainAxisAlignment: MainAxisAlignment.start,
      //                               crossAxisAlignment:
      //                                   CrossAxisAlignment.center,
      //                               children: [
      //                                 SizedBox(
      //                                   width: 24,
      //                                   height: 24,
      //                                   child: Image.asset(icMapPin),
      //                                 ),
      //                                 const SizedBox(width: 8),
      //                                 Expanded(
      //                                   child: RichText(
      //                                     text: TextSpan(
      //                                       text: "District : ",
      //                                       style: TextStyle(
      //                                         color: Colors.black,
      //                                         fontFamily: FontConstants.interFonts,
      //                                         fontWeight: FontWeight.w500,
      //                                         fontSize: responsiveFont(16),
      //                                       ),
      //                                       children: [
      //                                         TextSpan(
      //                                           text: object.dISTNAME ?? "",
      //                                           style: TextStyle(
      //                                             color: dropDownTitleHeader,
      //                                             fontFamily: FontConstants.interFonts,
      //                                             fontWeight: FontWeight.w500,
      //                                             fontSize: responsiveFont(16),
      //                                           ),
      //                                         ),
      //                                       ],
      //                                     ),
      //                                     softWrap: true,
      //                                   ),
      //                                 ),
      //                               ],
      //                             ),
      //                             const SizedBox(height: 8),
      //                             Row(
      //                               mainAxisAlignment: MainAxisAlignment.start,
      //                               crossAxisAlignment:
      //                                   CrossAxisAlignment.center,
      //                               children: [
      //                                 SizedBox(
      //                                   width: 24,
      //                                   height: 24,
      //                                   child: Image.asset(icnTent),
      //                                 ),
      //                                 const SizedBox(width: 8),
      //                                 Expanded(
      //                                   child: RichText(
      //                                     text: TextSpan(
      //                                       text: "Camp Type : ",
      //                                       style: TextStyle(
      //                                         color: Colors.black,
      //                                         fontFamily: FontConstants.interFonts,
      //                                         fontWeight: FontWeight.w500,
      //                                         fontSize: responsiveFont(16),
      //                                       ),
      //                                       children: [
      //                                         TextSpan(
      //                                           text:
      //                                               selectedCampType
      //                                                   ?.campTypeDescription ??
      //                                               "",
      //                                           style: TextStyle(
      //                                             color: dropDownTitleHeader,
      //                                             fontFamily: FontConstants.interFonts,
      //                                             fontWeight: FontWeight.w500,
      //                                             fontSize: responsiveFont(16),
      //                                           ),
      //                                         ),
      //                                       ],
      //                                     ),
      //                                     softWrap: true,
      //                                   ),
      //                                 ),
      //                               ],
      //                             ),

      //                             const SizedBox(height: 8),
      //                             Row(
      //                               mainAxisAlignment: MainAxisAlignment.start,
      //                               crossAxisAlignment:
      //                                   CrossAxisAlignment.center,
      //                               children: [
      //                                 SizedBox(
      //                                   width: 24,
      //                                   height: 24,
      //                                   child: Image.asset(icInitiatedByIcon),
      //                                 ),
      //                                 const SizedBox(width: 8),
      //                                 Expanded(
      //                                   child: RichText(
      //                                     text: TextSpan(
      //                                       text: "Camp Name : ",
      //                                       style: TextStyle(
      //                                         color: Colors.black,
      //                                         fontFamily: FontConstants.interFonts,
      //                                         fontWeight: FontWeight.w500,
      //                                         fontSize: responsiveFont(16),
      //                                       ),
      //                                       children: [
      //                                         TextSpan(
      //                                           text: "${object.campName ?? 0}",
      //                                           style: TextStyle(
      //                                             color: dropDownTitleHeader,
      //                                             fontFamily: FontConstants.interFonts,
      //                                             fontWeight: FontWeight.w500,
      //                                             fontSize: responsiveFont(16),
      //                                           ),
      //                                         ),
      //                                       ],
      //                                     ),
      //                                     softWrap: true,
      //                                   ),
      //                                 ),
      //                               ],
      //                             ),
      //                             const SizedBox(height: 8),
      //                             Row(
      //                               mainAxisAlignment: MainAxisAlignment.start,
      //                               crossAxisAlignment:
      //                                   CrossAxisAlignment.center,
      //                               children: [
      //                                 SizedBox(
      //                                   width: 24,
      //                                   height: 24,
      //                                   child: Image.asset(icInitiatedBy),
      //                                 ),
      //                                 const SizedBox(width: 8),
      //                                 Expanded(
      //                                   child: RichText(
      //                                     text: TextSpan(
      //                                       text: "Created By : ",
      //                                       style: TextStyle(
      //                                         color: Colors.black,
      //                                         fontFamily: FontConstants.interFonts,
      //                                         fontWeight: FontWeight.w500,
      //                                         fontSize: responsiveFont(16),
      //                                       ),
      //                                       children: [
      //                                         TextSpan(
      //                                           text:
      //                                               object.campCreatedBy ?? "",
      //                                           style: TextStyle(
      //                                             color: dropDownTitleHeader,
      //                                             fontFamily: FontConstants.interFonts,
      //                                             fontWeight: FontWeight.w500,
      //                                             fontSize: responsiveFont(16),
      //                                           ),
      //                                         ),
      //                                       ],
      //                                     ),
      //                                     softWrap: true,
      //                                   ),
      //                                 ),
      //                               ],
      //                             ),
      //                           ],
      //                         ),
      //                         //
      //                         //icHashIcon
      //                       ),
      //                     ),
      //                   );
      //                 },
      //               ),
      //             ),
      //           ),
      //         ],
      //       ),
      //     ),
      //   ),
      //   // AnnotatedRegion(
      //   //   value: SystemUiOverlayStyle(
      //   //     statusBarColor: kPrimaryColor,
      //   //     statusBarBrightness: Brightness.dark,
      //   //     statusBarIconBrightness: Brightness.light,
      //   //   ),
      //   //   child: SingleChildScrollView(
      //   //     child: Container(
      //   //       color: Colors.white,
      //   //       height: SizeConfig.screenHeight + 50,
      //   //       width: SizeConfig.screenWidth,
      //   //       child: Stack(
      //   //         children: [
      //   //           Positioned(
      //   //             top: 74,
      //   //             child: Image.asset(
      //   //               fit: BoxFit.fill,
      //   //               rect4,
      //   //               width: SizeConfig.screenWidth,
      //   //               height: responsiveHeight(300.37),
      //   //             ),
      //   //           ),
      //   //           Positioned(
      //   //             top: 53,
      //   //             child: Image.asset(
      //   //               fit: BoxFit.fill,
      //   //               rect3,
      //   //               width: SizeConfig.screenWidth,
      //   //               height: responsiveHeight(300.37),
      //   //             ),
      //   //           ),
      //   //           Positioned(
      //   //             top: 30,
      //   //             child: Image.asset(
      //   //               fit: BoxFit.fill,
      //   //               rect2,
      //   //               width: SizeConfig.screenWidth,
      //   //               height: responsiveHeight(300.37),
      //   //             ),
      //   //           ),
      //   //           Image.asset(
      //   //             fit: BoxFit.fill,
      //   //             rect1,
      //   //             width: SizeConfig.screenWidth,
      //   //             height: responsiveHeight(300.37),
      //   //           ),
      //   //           Positioned(
      //   //             top: 44,
      //   //             child: Container(
      //   //               width: MediaQuery.of(context).size.width,
      //   //               decoration: BoxDecoration(
      //   //                 color: Colors.transparent,

      //   //                 borderRadius: BorderRadius.circular(10),
      //   //               ),
      //   //               child: Column(
      //   //                 crossAxisAlignment: CrossAxisAlignment.start,
      //   //                 children: [Container(color: Colors.amber)],
      //   //               ),
      //   //             ),
      //   //           ),
      //   //           // Positioned(
      //   //           //   top: responsiveHeight(350),
      //   //           //   right: 10,
      //   //           //   left: 10,
      //   //           //   child: Center(
      //   //           //     child: SizedBox(
      //   //           //       height: SizeConfig.screenHeight * 2,
      //   //           //       width: SizeConfig.screenWidth * 0.9,
      //   //           //       child: Form(
      //   //           //         key: formKey,
      //   //           //         child: Column(
      //   //           //           children: [SizedBox(height: responsiveHeight(27))],
      //   //           //         ),
      //   //           //       ),
      //   //           //     ),
      //   //           //   ),
      //   //           // ),
      //   //         ],
      //   //       ),
      //   //     ),
      //   //   ),
      //   // ),
      // ),


      
*/
