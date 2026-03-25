// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../../../../../Modules/Enums/Enums.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Modules/utilities/SizeConfig.dart';

import '../../../../../Modules/constants/fonts.dart';

class HealthScreeningMenuRow extends StatelessWidget {
  HealthScreeningDetailsMenu dashboardMenu;

  HealthScreeningMenuRow({super.key, required this.dashboardMenu});


  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withValues(alpha: 0.4),
            spreadRadius: 2,
            blurRadius: 3,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(height: 6),
          SizedBox(
            width: responsiveWidth(30),
            height: responsiveWidth(30),
            child: Image.asset(getIconName()),
          ),
          const SizedBox(height: 6),
          Padding(
            padding: const EdgeInsets.fromLTRB(6, 0, 6, 0),
            child: Text(
              getTitleName(),
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(14),
                color: gridTitleColor,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
          const SizedBox(height: 6),
        ],
      ),
      // Column(
      //   mainAxisAlignment: MainAxisAlignment.center,
      //   children: [
      //     const Spacer(),
      //     SizedBox(width: 80, height: 80, child: Image.asset(getIconName())),
      //     const Spacer(),
      //     Text(
      //       getTitleName(),
      //       textAlign: TextAlign.center,
      //       style: TextStyle(
      //         fontFamily: FontConstants.interFonts,
      //         fontSize: 12,
      //         color: kPrimaryColor,
      //         fontWeight: FontWeight.bold,
      //       ),
      //     ),
      //     const Spacer(),
      //   ],
      // ),
    );
  }

  String getIconName() {
    String icon;
    switch (dashboardMenu) {
      case HealthScreeningDetailsMenu.ScreeningStatus:
        icon = icScreeningStatus;
        break;
      case HealthScreeningDetailsMenu.CampClosing:
        icon = icCampClosing;
        break;
      case HealthScreeningDetailsMenu.PhysicalExamination:
        icon = icCampClosing;
        break;
      case HealthScreeningDetailsMenu.SampleCollection:
        icon = icSampleCollection;
        break;
      case HealthScreeningDetailsMenu.BasicHealthInfo:
        icon = icBasicHealthInfo;
        break;
      case HealthScreeningDetailsMenu.D2DPhysicalExamination:
        icon = icD2DPhysicalExamination;
        break;
      case HealthScreeningDetailsMenu.LungFunctionTest:
        icon = icLungFunctionTest;
        break;
      case HealthScreeningDetailsMenu.AudioScreeningTest:
        icon = icAudioScreeningTest;
        break;
      case HealthScreeningDetailsMenu.VisualScreeningTest:
        icon = icVisualScreeningTest;
        break;
    }
    return icon;
  }

  String getTitleName() {
    String title;

    switch (dashboardMenu) {
      case HealthScreeningDetailsMenu.ScreeningStatus:
        title = "Screening Status";
        break;
      case HealthScreeningDetailsMenu.CampClosing:
        title = "Camp Closing";
        break;
      case HealthScreeningDetailsMenu.PhysicalExamination:
        title = "Physical Examination";
        break;
      case HealthScreeningDetailsMenu.SampleCollection:
        title = "Sample Collection";
        break;
      case HealthScreeningDetailsMenu.BasicHealthInfo:
        title = "Basic Health Info";
        break;
      case HealthScreeningDetailsMenu.D2DPhysicalExamination:
        title = "D2D Physical Examination";
        break;
      case HealthScreeningDetailsMenu.LungFunctionTest:
        title = "Lung Function Test";
        break;
      case HealthScreeningDetailsMenu.AudioScreeningTest:
        title = "Audio Screening Test";
        break;
      case HealthScreeningDetailsMenu.VisualScreeningTest:
        title = "Visual Screening Test";
        break;
    }
    return title;
  }
}
