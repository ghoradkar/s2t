// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/BeneficiaryCampColorInfoView/BeneficiaryCampColorInfoView.dart';
import '../../Views/CampDetailsSegmentView/CampDetailsSegmentView.dart';
import 'BeneficiaryCampDetailsScreen/BeneficiaryCampDetailsScreen.dart';
import 'PatientStatusScreen/PatientStatusScreen.dart';
import 'ScreeningTestCampDetailsScreen/ScreeningTestCampDetailsScreen.dart';

class CampDetailsScreen extends StatefulWidget {
  CampDetailsScreen({
    super.key,
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.cAMPTYPE,
    required this.campTypeDescription,
    required this.isHealthScreeing,
  });

  int campId = 0;
  int dISTLGDCODE = 0;
  String campDate = "";
  String surveyCoordinatorName = "";
  String dISTNAME = "";
  String mOBNO = "";
  int? cAMPTYPE;
  String? campTypeDescription;

  bool isHealthScreeing = false;

  @override
  State<CampDetailsScreen> createState() => _CampDetailsScreenState();
}

class _CampDetailsScreenState extends State<CampDetailsScreen> {
  int selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Camp Details',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
          showActions: true,
          actions: [
            selectedIndex == 1
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                  child: GestureDetector(
                    onTap: () {
                      showBottomPopup(context);
                    },
                    child: Icon(Icons.info, color: Colors.white, size: 24.0),
                  ),
                )
                : Container(),
          ],
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Column(
            children: [
              CampDetailsSegmentView(
                onChangedTap: (p0) {
                  selectedIndex = p0;
                  setState(() {});
                },
              ),
              selectedSegmentView(),
            ],
          ).paddingSymmetric(vertical: 10,horizontal: 8),
        ),
      ),
    );
  }

  Widget selectedSegmentView() {
    if (selectedIndex == 0) {
      return ScreeningTestCampDetailsScreen(
        campId: widget.campId,
        dISTLGDCODE: widget.dISTLGDCODE,
        campDate: widget.campDate,
        surveyCoordinatorName: widget.surveyCoordinatorName,
        dISTNAME: widget.dISTNAME,
        mOBNO: widget.mOBNO,
        cAMPTYPE: widget.cAMPTYPE,
        campTypeDescription: widget.campTypeDescription,
        isHealthScreeing: widget.isHealthScreeing,
      );
    }
    if (selectedIndex == 1) {
      return BeneficiaryCampDetailsScreen(
        campId: widget.campId,
        cAMPTYPE: widget.cAMPTYPE,
        campTypeDescription: widget.campTypeDescription,
      );
    }
    return PatientStatusScreen(
      campId: widget.campId,
      cAMPTYPE: widget.cAMPTYPE,
      campTypeDescription: widget.campTypeDescription,
      dISTLGDCODE: widget.dISTLGDCODE,
    );
  }

  void showBottomPopup(BuildContext parentContext) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return GestureDetector(
          onTap: () {
            Navigator.of(sheetContext).pop();
          },
          child: Container(
            width: double.infinity,
            height: MediaQuery.of(sheetContext).size.height,
            color: Colors.transparent,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 0, 30, 0),
                  child: BeneficiaryCampColorInfoView(),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
