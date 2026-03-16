// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Screens/ExpenseClaimScreen/BillSubmissionScreen/BillSubmissionScreen.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import 'UploadBillScreen/UploadBillScreen.dart';

class ExpenseClaimDashboardScreen extends StatefulWidget {
  const ExpenseClaimDashboardScreen({super.key});

  @override
  State<ExpenseClaimDashboardScreen> createState() =>
      _ExpenseClaimDashboardScreenState();
}

class _ExpenseClaimDashboardScreenState
    extends State<ExpenseClaimDashboardScreen> {
  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Expense/Claim",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.dark,
            statusBarIconBrightness: Brightness.light,
          ),
          child: SingleChildScrollView(
            child: Container(
              color: Colors.white,
              height: SizeConfig.screenHeight + 50,
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
                    top: 10,
                    bottom: 8,
                    left: 20,
                    right: 20,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => const BillSubmissionScreen(),
                                ),
                              );
                            },
                            child: Container(
                              height: 135,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.15),
                                    blurRadius: 6,
                                  ),
                                ],
                                borderRadius: BorderRadius.all(
                                  Radius.circular(10),
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceAround,
                                children: [
                                  SizedBox(
                                    width: 60,
                                    height: 60,
                                    child: Image.asset(icCampExpensesIcon),
                                  ),
                                  Text(
                                    "Camp Expenses",
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      color: kBlackColor,
                                      fontSize: responsiveFont(16),
                                      fontWeight: FontWeight.w400,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),

                        const SizedBox(width: 20),
                        Expanded(
                          child: GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => const UploadBillScreen(),
                                ),
                              );
                            },
                            child: Container(
                              height: 135,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.15),
                                    blurRadius: 6,
                                  ),
                                ],
                                borderRadius: BorderRadius.all(
                                  Radius.circular(10),
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceAround,
                                children: [
                                  SizedBox(
                                    width: 60,
                                    height: 60,
                                    child: Image.asset(icBillUploadIcon),
                                  ),
                                  Text(
                                    "Bill Upload",
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      color: kBlackColor,
                                      fontSize: responsiveFont(16),
                                      fontWeight: FontWeight.w400,
                                    ),
                                  ),
                                ],
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
          ),
        ),
      ),
    );
  }
}

// class ExpenseClaimDashboardScreen extends StatefulWidget {
//   const ExpenseClaimDashboardScreen({super.key});

//   @override
//   State<ExpenseClaimDashboardScreen> createState() =>
//       _ExpenseClaimDashboardScreenState();
// }

// class _ExpenseClaimDashboardScreenState
//     extends State<ExpenseClaimDashboardScreen> {
//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Expense/Claim",
//           leadingIcon: iconBackArrow,
//           onLeadingIconClick: () {
//             Navigator.pop(context);
//           },
//         ),
//         body: AnnotatedRegion(
//           value: SystemUiOverlayStyle(
//             statusBarColor: kPrimaryColor,
//             statusBarBrightness: Brightness.dark,
//             statusBarIconBrightness: Brightness.light,
//           ),
//           child: SingleChildScrollView(
//             child: Container(
//               color: Colors.white,
//               height: SizeConfig.screenHeight + 50,
//               width: SizeConfig.screenWidth,
//               child: Stack(
//                 children: [
//                   Positioned(
//                     top: 74,
//                     child: Image.asset(
//                       fit: BoxFit.fill,
//                       rect4,
//                       width: SizeConfig.screenWidth,
//                       height: responsiveHeight(300.37),
//                     ),
//                   ),
//                   Positioned(
//                     top: 53,
//                     child: Image.asset(
//                       fit: BoxFit.fill,
//                       rect3,
//                       width: SizeConfig.screenWidth,
//                       height: responsiveHeight(300.37),
//                     ),
//                   ),
//                   Positioned(
//                     top: 30,
//                     child: Image.asset(
//                       fit: BoxFit.fill,
//                       rect2,
//                       width: SizeConfig.screenWidth,
//                       height: responsiveHeight(300.37),
//                     ),
//                   ),
//                   Image.asset(
//                     fit: BoxFit.fill,
//                     rect1,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                   Positioned(
//                     top: 10,
//                     bottom: 8,
//                     left: 20,
//                     right: 20,
//                     child: Row(
//                       mainAxisAlignment: MainAxisAlignment.center,
//                       crossAxisAlignment: CrossAxisAlignment.start,
//                       children: [
//                         Expanded(
//                           child: GestureDetector(
//                             onTap: () {
//                               Navigator.push(
//                                 context,
//                                 MaterialPageRoute(
//                                   builder:
//                                       (context) => const BillSubmissionScreen(),
//                                 ),
//                               );
//                             },
//                             child: Container(
//                               height: 135,
//                               decoration: BoxDecoration(
//                                 color: Colors.white,
//                                 boxShadow: [
//                                   BoxShadow(
//                                     color: Colors.black.withValues(alpha: 0.15),
//                                     blurRadius: 10,
//                                   ),
//                                 ],
//                                 borderRadius: BorderRadius.all(
//                                   Radius.circular(10),
//                                 ),
//                               ),
//                               child: Column(
//                                 mainAxisAlignment:
//                                     MainAxisAlignment.spaceAround,
//                                 children: [
//                                   SizedBox(
//                                     width: 60,
//                                     height: 60,
//                                     child: Image.asset(icCampExpensesIcon),
//                                   ),
//                                   Text(
//                                     "Camp Expenses",
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(16),
//                                       fontWeight: FontWeight.w400,
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                           ),
//                         ),

//                         const SizedBox(width: 20),
//                         Expanded(
//                           child: GestureDetector(
//                             onTap: () {
//                               Navigator.push(
//                                 context,
//                                 MaterialPageRoute(
//                                   builder:
//                                       (context) => const UploadBillScreen(),
//                                 ),
//                               );
//                             },
//                             child: Container(
//                               height: 135,
//                               decoration: BoxDecoration(
//                                 color: Colors.white,
//                                 boxShadow: [
//                                   BoxShadow(
//                                     color: Colors.black.withValues(alpha: 0.15),
//                                     blurRadius: 10,
//                                   ),
//                                 ],
//                                 borderRadius: BorderRadius.all(
//                                   Radius.circular(10),
//                                 ),
//                               ),
//                               child: Column(
//                                 mainAxisAlignment:
//                                     MainAxisAlignment.spaceAround,
//                                 children: [
//                                   SizedBox(
//                                     width: 60,
//                                     height: 60,
//                                     child: Image.asset(icBillUploadIcon),
//                                   ),
//                                   Text(
//                                     "Bill Upload",
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(16),
//                                       fontWeight: FontWeight.w400,
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                           ),
//                         ),
//                       ],
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//           ),
//         ),
//       ),
//     );
//   }
// }
