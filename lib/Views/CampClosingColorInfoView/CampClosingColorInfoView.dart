// ignore_for_file: file_names

import 'package:flutter/material.dart';

import '../../Modules/constants/constants.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class CampClosingColorInfoView extends StatelessWidget {
  const CampClosingColorInfoView({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: SizeConfig.screenWidth,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
      ),
      padding: EdgeInsets.fromLTRB(0, 6, 0, 6),
      child: Column(
        children: [
          Row(
            children: [
              const SizedBox(width: 8),
              Center(
                child: Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: rejectedBeneficiaryColor,
                    borderRadius: BorderRadius.circular(5),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Text(
                "Rejected Beneficiary",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  color: kBlackColor,
                  fontSize: responsiveFont(13),
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
          const SizedBox(height: 6),
          Row(
            children: [
              const SizedBox(width: 8),
              Center(
                child: Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: incompleteTests1Color,
                    borderRadius: BorderRadius.circular(5),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Text(
                "Incomplete Tests",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  color: kBlackColor,
                  fontSize: responsiveFont(13),
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
          const SizedBox(height: 6),
          Row(
            children: [
              const SizedBox(width: 8),
              Center(
                child: Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: todayCampOpenColor,
                    borderRadius: BorderRadius.circular(5),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Text(
                "Approved Beneficiary",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  color: kBlackColor,
                  fontSize: responsiveFont(13),
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
          const SizedBox(height: 6),
          Row(
            children: [
              const SizedBox(width: 8),
              Center(
                child: Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: beneficiariesVerifiedColor,
                    borderRadius: BorderRadius.circular(5),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Text(
                "Verified Beneficiary",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  color: kBlackColor,
                  fontSize: responsiveFont(13),
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
