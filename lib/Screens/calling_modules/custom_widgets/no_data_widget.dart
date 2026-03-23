import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_controller.dart';

import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';

class NoDataFound extends StatelessWidget {
  const NoDataFound({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
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
            left: 105,
            right: 105,
            child: Stack(
              children: [
                Image.asset(
                  icCircle,
                  height: responsiveHeight(219),
                  width: responsiveHeight(219),
                ),
                Positioned(
                  top: 0,
                  bottom: 0,
                  left: 0,
                  right: 0,
                  child: Center(
                    child: Image.asset(
                      icFolder,
                      height: responsiveHeight(100),
                      width: responsiveHeight(100),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Positioned(
            bottom: 123,
            left: 61,
            right: 61,
            child: Column(
              children: [
                Text(
                  "Data Not Found",
                  style: TextStyle(
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(24),fontFamily: FontConstants.interFonts
                  ),
                ),
                SizedBox(height: responsiveHeight(25)),
                Text(
                  "Maybe go back and try different keyword?",
                  style: TextStyle(fontSize: responsiveFont(18),fontFamily: FontConstants.interFonts),
                  textAlign: TextAlign.center,
                ),
                SizedBox(height: responsiveHeight(50)),
                AppButtonWithIcon(
                  onTap: () {
                    Get.find<ExpectedBeneficiaryController>().fetchBeneficiaries({
                      "CallStatusID": "0",
                      "TeamID": "0",
                      "GroupID": "1",
                    });
                  },
                  title: "Retry",
                  mWidth: responsiveWidth(175),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
