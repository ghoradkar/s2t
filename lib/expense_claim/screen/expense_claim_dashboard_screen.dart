// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'bill_submission_screen.dart';
import 'upload_bill_screen.dart';

class ExpenseClaimDashboardScreen extends StatelessWidget {
  const ExpenseClaimDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Expense/Claim',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Get.back(),
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
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
                            onTap: () => Get.to(() => const BillSubmissionScreen()),
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
                                borderRadius: const BorderRadius.all(
                                  Radius.circular(10),
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.spaceAround,
                                children: [
                                  SizedBox(
                                    width: 60,
                                    height: 60,
                                    child: Image.asset(icCampExpensesIcon),
                                  ),
                                  Text(
                                    'Camp Expenses',
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
                            onTap: () => Get.to(() => const UploadBillScreen()),
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
                                borderRadius: const BorderRadius.all(
                                  Radius.circular(10),
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.spaceAround,
                                children: [
                                  SizedBox(
                                    width: 60,
                                    height: 60,
                                    child: Image.asset(icBillUploadIcon),
                                  ),
                                  Text(
                                    'Bill Upload',
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
