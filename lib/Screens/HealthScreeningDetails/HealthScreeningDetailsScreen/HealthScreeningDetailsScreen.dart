// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../CampDetailsScreen/CampDetailsScreen.dart';
import '../CampClosingScreen/CampClosingScreen.dart';
import '../HealthScreeningPatientListScreen/HealthScreeningPatientListScreen.dart';
import 'HealthScreeningMenuRow/HealthScreeningMenuRow.dart';

class HealthScreeningDetailsScreen extends StatefulWidget {
  HealthScreeningDetailsScreen({
    super.key,
    required this.testID,
    required this.teamid,
    required this.districtID,
    required this.districtName,
    required this.campID,
    required this.dISTLGDCODE,
    required this.campType,
    required this.campTypeDescription,
    required this.campDate,
    required this.surveyCoordinatorName,
  });

  int testID = 0;
  int teamid = 0;
  int districtID = 0;
  String districtName = "";
  int campID = 0;
  int dISTLGDCODE = 0;
  int campType = 0;
  int siteDetailId = 0;
  String campTypeDescription = "";
  String campDate = "";
  String surveyCoordinatorName = "";
  String dISTNAME = "";
  String mOBNO = "";

  @override
  State<HealthScreeningDetailsScreen> createState() =>
      _HealthScreeningDetailsScreenState();
}

class _HealthScreeningDetailsScreenState
    extends State<HealthScreeningDetailsScreen> {
  List<HealthScreeningDetailsMenu> healthScreeningMenu = [];

  int dESGID = 0;
  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;

    if (dESGID == 34 || dESGID == 147 || dESGID == 130) {
      if (DataProvider().getRegularCamp()) {
        healthScreeningMenu.add(HealthScreeningDetailsMenu.PhysicalExamination);
      }
    }
    if (dESGID == 139 ||
        dESGID == 136 ||
        dESGID == 92 ||
        dESGID == 29 ||
        dESGID == 108) {
      healthScreeningMenu.add(HealthScreeningDetailsMenu.ScreeningStatus);
      healthScreeningMenu.add(HealthScreeningDetailsMenu.CampClosing);
    } else if (dESGID == 141 || dESGID == 34) {
      healthScreeningMenu.add(HealthScreeningDetailsMenu.ScreeningStatus);
    } else if (dESGID == 92 ||
        dESGID == 104 ||
        dESGID == 108 ||
        dESGID == 29 ||
        dESGID == 35 ||
        dESGID == 137 ||
        dESGID == 139 ||
        dESGID == 138 ||
        dESGID == 141 ||
        dESGID == 136 ||
        dESGID == 169 ||
        dESGID == 146 ||
        dESGID == 177) {
      healthScreeningMenu.add(HealthScreeningDetailsMenu.SampleCollection);
      healthScreeningMenu.add(HealthScreeningDetailsMenu.BasicHealthInfo);

      if (!DataProvider().getRegularCamp()) {
        healthScreeningMenu.add(
          HealthScreeningDetailsMenu.D2DPhysicalExamination,
        );
      }
      healthScreeningMenu.add(HealthScreeningDetailsMenu.LungFunctionTest);
      healthScreeningMenu.add(HealthScreeningDetailsMenu.AudioScreeningTest);
      healthScreeningMenu.add(HealthScreeningDetailsMenu.VisualScreeningTest);
      healthScreeningMenu.add(HealthScreeningDetailsMenu.ScreeningStatus);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Health Screening",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),

      body: SizedBox(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth,
        child: GridView.builder(
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3,
            crossAxisSpacing: 18,
            mainAxisSpacing: 18,
          ),
          // gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          //   crossAxisCount: 2,
          //   crossAxisSpacing: 18,
          //   mainAxisSpacing: 18,
          // ),
          itemCount: healthScreeningMenu.length,
          itemBuilder: (context, index) {
            HealthScreeningDetailsMenu healthScreeningDetailsMenu =
            healthScreeningMenu[index];
            return GestureDetector(
              onTap: () {
                switch (healthScreeningDetailsMenu) {
                  case HealthScreeningDetailsMenu.ScreeningStatus:
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder:
                            (context) => CampDetailsScreen(
                          campId: widget.campID,
                          dISTLGDCODE: widget.dISTLGDCODE,
                          campDate: widget.campDate,
                          surveyCoordinatorName:
                          widget.surveyCoordinatorName,
                          dISTNAME: widget.dISTNAME,
                          mOBNO: widget.mOBNO,
                          cAMPTYPE: widget.campType,
                          campTypeDescription: "",
                          isHealthScreeing: true,
                        ),
                      ),
                    );
                  case HealthScreeningDetailsMenu.CampClosing:
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder:
                            (context) => CampClosingScreen(
                          campID: widget.campID,
                          campDate: widget.campDate,
                          dISTLGDCODE: widget.dISTLGDCODE,
                        ),
                      ),
                    );
                    break;

                  case HealthScreeningDetailsMenu.PhysicalExamination:
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder:
                            (context) => HealthScreeningPatientListScreen(
                          screeningMenu: healthScreeningDetailsMenu,
                          testId: 3,
                          teamid: 0,
                          districtID: widget.dISTLGDCODE,
                          districtName: widget.dISTNAME,
                          campID: widget.campID,
                          dISTLGDCODE: widget.dISTLGDCODE,
                          siteDetailId: widget.siteDetailId,
                        ),
                      ),
                    );
                    break;
                  case HealthScreeningDetailsMenu.SampleCollection:
                    break;
                  case HealthScreeningDetailsMenu.BasicHealthInfo:
                    break;
                  case HealthScreeningDetailsMenu.D2DPhysicalExamination:
                    break;
                  case HealthScreeningDetailsMenu.LungFunctionTest:
                    break;
                  case HealthScreeningDetailsMenu.AudioScreeningTest:
                    break;
                  case HealthScreeningDetailsMenu.VisualScreeningTest:
                    break;
                }
              },
              child: HealthScreeningMenuRow(
                dashboardMenu: healthScreeningDetailsMenu,
              ),
            );
          },
        ).paddingSymmetric(vertical: 10,horizontal: 10),
      ),
    );
  }
}
