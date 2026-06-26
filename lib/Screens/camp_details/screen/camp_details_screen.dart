// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import '../controller/camp_details_controller.dart';
import '../widget/beneficiary_camp_color_info_widget.dart';
import '../widget/camp_details_segment_view.dart';
import './beneficiary_camp_details_screen.dart';
import './patient_status_screen.dart';
import './screening_test_camp_details_screen.dart';

class CampDetailsScreen extends StatelessWidget {
  const CampDetailsScreen({
    super.key,
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.isHealthScreeing,
    this.cAMPTYPE,
    this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final String campDate;
  final String surveyCoordinatorName;
  final String dISTNAME;
  final String mOBNO;
  final bool isHealthScreeing;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  @override
  Widget build(BuildContext context) {
    Get.put(CampDetailsController(
      campId: campId,
      dISTLGDCODE: dISTLGDCODE,
      campDate: campDate,
      surveyCoordinatorName: surveyCoordinatorName,
      dISTNAME: dISTNAME,
      mOBNO: mOBNO,
      isHealthScreeing: isHealthScreeing,
      cAMPTYPE: cAMPTYPE,
      campTypeDescription: campTypeDescription,
    ));
    SizeConfig().init(context);
    return GetBuilder<CampDetailsController>(
      builder: (ctrl) => KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Camp Details',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
            showActions: true,
            actions: [
              ctrl.selectedIndex == 1
                  ? Padding(
                    padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                    child: GestureDetector(
                      onTap: () => _showColorInfoPopup(context),
                      child: const Icon(Icons.info, color: Colors.white, size: 24.0),
                    ),
                  )
                  : const SizedBox.shrink(),
            ],
          ),
          body: SizedBox(
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Column(
              children: [
                CampDetailsSegmentView(onChangedTap: ctrl.changeTab),
                _selectedSegmentView(ctrl),
              ],
            ).paddingSymmetric(vertical: 10, horizontal: 8),
          ),
        ),
      ),
    );
  }

  Widget _selectedSegmentView(CampDetailsController ctrl) {
    if (ctrl.selectedIndex == 0) {
      return ScreeningTestCampDetailsScreen(
        campId: campId,
        dISTLGDCODE: dISTLGDCODE,
        campDate: campDate,
        surveyCoordinatorName: surveyCoordinatorName,
        dISTNAME: dISTNAME,
        mOBNO: mOBNO,
        cAMPTYPE: cAMPTYPE,
        campTypeDescription: campTypeDescription,
        isHealthScreeing: isHealthScreeing,
      );
    }
    if (ctrl.selectedIndex == 1) {
      return BeneficiaryCampDetailsScreen(
        campId: campId,
        cAMPTYPE: cAMPTYPE,
        campTypeDescription: campTypeDescription,
      );
    }
    return PatientStatusScreen(
      campId: campId,
      dISTLGDCODE: dISTLGDCODE,
      cAMPTYPE: cAMPTYPE,
      campTypeDescription: campTypeDescription,
    );
  }

  void _showColorInfoPopup(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (sheetContext) => GestureDetector(
        onTap: () => Navigator.of(sheetContext).pop(),
        child: Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(30, 0, 30, 0),
                child: BeneficiaryCampColorInfoView(),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
