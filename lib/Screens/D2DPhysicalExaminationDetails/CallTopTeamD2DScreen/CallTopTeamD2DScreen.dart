// ignore_for_file: must_be_immutable, file_names

import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/TeamWisePhysicalExamDetailsResponse/TeamWisePhysicalExamDetailsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';

class CallTopTeamD2DScreen extends StatefulWidget {
  CallTopTeamD2DScreen({
    super.key,
    required this.campId,
    required this.doctorID,
    required this.teamid,
  });

  int campId;
  int doctorID;
  String teamid;

  @override
  State<CallTopTeamD2DScreen> createState() => _CallTopTeamD2DScreenState();
}

class _CallTopTeamD2DScreenState extends State<CallTopTeamD2DScreen> {
  APIManager apiManager = APIManager();

  List<TeamWisePhysicalExamDetailsOutput> physiCalList = [];

  @override
  void initState() {
    super.initState();
    getTeamWiseData();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      backgroundColor: kWhiteColor,
      appBar: mAppBar(
        scTitle: "Call To Team",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: SizedBox(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth,
        child: Padding(
          padding: EdgeInsets.fromLTRB(0, 8.h, 0, 8.h),
          child: ListView.builder(
            itemCount: physiCalList.length,
            itemBuilder: (context, index) {
              TeamWisePhysicalExamDetailsOutput obj = physiCalList[index];
              return Padding(
                padding: EdgeInsets.fromLTRB(10.w, 0, 10.h, 10.h),
                child: Container(
                  padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: [
                      BoxShadow(
                        offset: Offset(0, 1),
                        color: Colors.black.withValues(alpha: 0.15),
                        spreadRadius: 0,
                        blurRadius: 4,
                      ),
                    ],
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: Column(
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: responsiveHeight(20),
                                  height: responsiveHeight(20),
                                  child: Image.asset(icInitiatedBy),
                                ),
                                SizedBox(width: 8.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "Team Member : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 14.sp,
                                        ),
                                      ),

                                      Expanded(
                                        child: Text(
                                          obj.teamNumberName ?? "",
                                          style: TextStyle(
                                            color: kBlackColor,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 14.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: 8.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: responsiveHeight(20),
                                  height: responsiveHeight(20),
                                  child: Image.asset(icDesignation),
                                ),
                                SizedBox(width: 8.w),
                                Expanded(
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        "Designation : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w500,
                                          fontSize: 14.sp,
                                        ),
                                      ),

                                      Expanded(
                                        child: Text(
                                          obj.desgName ?? "",
                                          style: TextStyle(
                                            color: kBlackColor,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 14.sp,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: EdgeInsets.all(8),
                        child: Center(
                          child: GestureDetector(
                            onTap: () {
                              launchUrl(Uri.parse('tel:${obj.mobileNo ?? ""}'));
                              launchPhoneDialer(obj.mobileNo!);
                            },
                            child: SizedBox(
                              width: 30.w,
                              height: 30.h,
                              child: Image.asset(icCallingExpected),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ),
      ),
    );
  }

  Future<void> launchPhoneDialer(String phoneNumber) async {
    try {
      if (phoneNumber.isEmpty) {
        throw Exception('Phone number is empty');
      }

      // Clean the phone number
      String cleanNumber = phoneNumber.replaceAll(RegExp(r'[^\d+]'), '');

      if (cleanNumber.isEmpty) {
        throw Exception('Invalid phone number');
      }

      debugPrint('Calling: $cleanNumber - Context:}');

      if (Platform.isAndroid) {
        // Use AndroidIntent with proper flags to prevent MIUI intent caching
        // FLAG_ACTIVITY_NEW_TASK: Start the activity in a new task
        // FLAG_ACTIVITY_CLEAR_TOP: Clear any existing instance of this activity
        // FLAG_ACTIVITY_SINGLE_TOP: Don't launch if already at top of stack
        final AndroidIntent intent = AndroidIntent(
          action: 'android.intent.action.DIAL',
          data: 'tel:$cleanNumber',
          flags: <int>[
            Flag.FLAG_ACTIVITY_NEW_TASK,
            Flag.FLAG_ACTIVITY_CLEAR_TOP,
            Flag.FLAG_ACTIVITY_SINGLE_TOP,
          ],
        );
        await intent.launch();
        debugPrint('✅ Dialer launched successfully via AndroidIntent');
      } else {
        // For iOS, use url_launcher
        final Uri telUri = Uri.parse('tel:$cleanNumber');

        final bool canLaunch = await canLaunchUrl(telUri);
        if (!canLaunch) {
          throw Exception('No dialer app available');
        }

        final bool launched = await launchUrl(
          telUri,
          mode: LaunchMode.externalApplication,
        );

        if (!launched) {
          throw Exception('Failed to launch dialer');
        }
        debugPrint('✅ Dialer launched successfully via url_launcher');
      }
    } catch (e) {
      debugPrint('❌ Dialer error: $e');

      if (mounted) {
        ToastManager.toast("Cannot open dialer: ${e.toString()}");
      }
    }
  }

  void getTeamWiseData() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "Type": "2",
      "CampID": widget.campId.toString(),
      "DoctorID": widget.doctorID.toString(),
      "TeamID": widget.teamid,
    };
    apiManager.getCallToTeamDetailsAPI(params, apiCallToTeamDetailsCallBack);
  }

  void apiCallToTeamDetailsCallBack(
    TeamWisePhysicalExamDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      physiCalList = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }
}

// class CallTopTeamD2DScreen extends StatefulWidget {
//   CallTopTeamD2DScreen({
//     super.key,
//     required this.campId,
//     required this.doctorID,
//     required this.teamid,
//   });

//   int campId;
//   int doctorID;
//   String teamid;
//   @override
//   State<CallTopTeamD2DScreen> createState() => _CallTopTeamD2DScreenState();
// }

// class _CallTopTeamD2DScreenState extends State<CallTopTeamD2DScreen> {
//   APIManager apiManager = APIManager();

//   List<TeamWisePhysicalExamDetailsOutput> physiCalList = [];
//   @override
//   void initState() {
//     super.initState();
//     getTeamWiseData();
//   }

//   void getTeamWiseData() {
//     ToastManager.showLoader();
//     Map<String, String> params = {
//       "Type": "2",
//       "CampID": widget.campId.toString(),
//       "DoctorID": widget.doctorID.toString(),
//       "TeamID": widget.teamid,
//     };
//     apiManager.getCallToTeamDetailsAPI(params, apiCallToTeamDetailsCallBack);
//   }

//   void apiCallToTeamDetailsCallBack(
//     TeamWisePhysicalExamDetailsResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       physiCalList = response?.output ?? [];
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return Scaffold(
//       backgroundColor: kWhiteColor,
//       appBar: mAppBar(
//         scTitle: "Call To Team",
//         leadingIcon: iconBackArrow,
//         onLeadingIconClick: () {
//           Navigator.pop(context);
//         },
//       ),
//       body: SizedBox(
//         height: SizeConfig.screenHeight,
//         width: SizeConfig.screenWidth,
//         child: Stack(
//           children: [
//             Positioned(
//               top: 74,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect4,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Positioned(
//               top: 53,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect3,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Positioned(
//               top: 30,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect2,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Image.asset(
//               fit: BoxFit.fill,
//               rect1,
//               width: SizeConfig.screenWidth,
//               height: responsiveHeight(300.37),
//             ),
//             Positioned(
//               top: 0,
//               bottom: 8,
//               left: 8,
//               right: 8,
//               child: Padding(
//                 padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
//                 child: ListView.builder(
//                   itemCount: physiCalList.length,
//                   itemBuilder: (context, index) {
//                     TeamWisePhysicalExamDetailsOutput obj = physiCalList[index];
//                     return Padding(
//                       padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
//                       child: Container(
//                         padding: EdgeInsets.all(8),
//                         decoration: BoxDecoration(
//                           color: Colors.white,
//                           borderRadius: BorderRadius.circular(10),
//                           boxShadow: [
//                             BoxShadow(
//                               offset: Offset(0, 1),
//                               color: Colors.black.withValues(alpha: 0.15),
//                               spreadRadius: 0,
//                               blurRadius: 10,
//                             ),
//                           ],
//                         ),
//                         child: Row(
//                           children: [
//                             Expanded(
//                               child: Column(
//                                 children: [
//                                   Row(
//                                     mainAxisAlignment: MainAxisAlignment.start,
//                                     crossAxisAlignment:
//                                         CrossAxisAlignment.start,
//                                     children: [
//                                       SizedBox(
//                                         width: responsiveHeight(30),
//                                         height: responsiveHeight(30),
//                                         child: Image.asset(icInitiatedBy),
//                                       ),
//                                       const SizedBox(width: 8),
//                                       Expanded(
//                                         child: Row(
//                                           mainAxisAlignment:
//                                               MainAxisAlignment.start,
//                                           crossAxisAlignment:
//                                               CrossAxisAlignment.start,
//                                           children: [
//                                             Text(
//                                               "Team Member : ",
//                                               style: TextStyle(
//                                                 color: Colors.black,
//                                                 fontFamily:
//                                                     FontConstants.interFonts,
//                                                 fontWeight: FontWeight.w500,
//                                                 fontSize: responsiveFont(16),
//                                               ),
//                                             ),

//                                             Expanded(
//                                               child: Text(
//                                                 obj.teamNumberName ?? "",
//                                                 style: TextStyle(
//                                                   color: dropDownTitleHeader,
//                                                   fontFamily:
//                                                       FontConstants.interFonts,
//                                                   fontWeight: FontWeight.w400,
//                                                   fontSize: responsiveFont(16),
//                                                 ),
//                                               ),
//                                             ),
//                                           ],
//                                         ),
//                                       ),
//                                     ],
//                                   ),
//                                   const SizedBox(height: 8),
//                                   Row(
//                                     mainAxisAlignment: MainAxisAlignment.start,
//                                     crossAxisAlignment:
//                                         CrossAxisAlignment.start,
//                                     children: [
//                                       SizedBox(
//                                         width: responsiveHeight(30),
//                                         height: responsiveHeight(30),
//                                         child: Image.asset(icDesignation),
//                                       ),
//                                       const SizedBox(width: 8),
//                                       Expanded(
//                                         child: Row(
//                                           mainAxisAlignment:
//                                               MainAxisAlignment.start,
//                                           crossAxisAlignment:
//                                               CrossAxisAlignment.start,
//                                           children: [
//                                             Text(
//                                               "Designation : ",
//                                               style: TextStyle(
//                                                 color: Colors.black,
//                                                 fontFamily:
//                                                     FontConstants.interFonts,
//                                                 fontWeight: FontWeight.w500,
//                                                 fontSize: responsiveFont(16),
//                                               ),
//                                             ),

//                                             Expanded(
//                                               child: Text(
//                                                 obj.desgName ?? "",
//                                                 style: TextStyle(
//                                                   color: dropDownTitleHeader,
//                                                   fontFamily:
//                                                       FontConstants.interFonts,
//                                                   fontWeight: FontWeight.w400,
//                                                   fontSize: responsiveFont(16),
//                                                 ),
//                                               ),
//                                             ),
//                                           ],
//                                         ),
//                                       ),
//                                     ],
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Container(
//                               padding: EdgeInsets.all(8),
//                               child: Center(
//                                 child: GestureDetector(
//                                   onTap: () {
//                                     launchUrl(
//                                       Uri.parse('tel:${obj.mobileNo ?? ""}'),
//                                     );
//                                   },
//                                   child: SizedBox(
//                                     width: 30,
//                                     height: 30,
//                                     child: Image.asset(icCallingExpected),
//                                   ),
//                                 ),
//                               ),
//                             ),
//                           ],
//                         ),
//                       ),
//                     );
//                   },
//                 ),
//               ),
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }
