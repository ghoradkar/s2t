// ignore_for_file: must_be_immutable
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import '../../../CampDetailsScreen/CampDetailsScreen.dart';
import '../../controllers/health_screening_details_controller.dart';
import '../camp_closing_screen/camp_closing_screen.dart';
import '../audio_screening_screen/audio_screening_patient_list_screen.dart';
import '../basic_health_info_screen/basic_health_info_patient_list_screen.dart';
import '../lung_function_test_screen/lung_function_test_patient_list_screen.dart';
import '../health_screening_patient_list_screen/health_screening_patient_list_screen.dart';
import 'health_screening_menu_row/health_screening_menu_row.dart';

class HealthScreeningDetailsScreen extends StatelessWidget {
  final int testID;
  final int teamid;
  final int districtID;
  final String districtName;
  final int campID;
  final int dISTLGDCODE;
  final int campType;
  final int siteDetailId;
  final String campTypeDescription;
  final String campDate;
  final String surveyCoordinatorName;
  final String dISTNAME;
  final String mOBNO;

  const HealthScreeningDetailsScreen({
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
    this.siteDetailId = 0,
    this.dISTNAME = '',
    this.mOBNO = '',
  });

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(HealthScreeningDetailsController());
    SizeConfig().init(context);

    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Health Screening',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(
        () => GridView.builder(
          padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 10),
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 3,
            crossAxisSpacing: 18,
            mainAxisSpacing: 18,
          ),
          itemCount: controller.menuList.length,
          itemBuilder: (context, index) {
            final menu = controller.menuList[index];
            return GestureDetector(
              onTap: () => _onMenuTap(context, menu),
              child: HealthScreeningMenuRow(dashboardMenu: menu),
            );
          },
        ),
      ),
    );
  }

  void _onMenuTap(BuildContext context, HealthScreeningDetailsMenu menu) {
    switch (menu) {
      case HealthScreeningDetailsMenu.ScreeningStatus:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => CampDetailsScreen(
              campId: campID,
              dISTLGDCODE: dISTLGDCODE,
              campDate: campDate,
              surveyCoordinatorName: surveyCoordinatorName,
              dISTNAME: dISTNAME,
              mOBNO: mOBNO,
              cAMPTYPE: campType,
              campTypeDescription: '',
              isHealthScreeing: true,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.CampClosing:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => CampClosingScreen(
              campID: campID,
              campDate: campDate,
              dISTLGDCODE: dISTLGDCODE,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.PhysicalExamination:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => HealthScreeningPatientListScreen(
              screeningMenu: menu,
              testId: 3,
              teamid: 0,
              districtID: dISTLGDCODE,
              districtName: dISTNAME,
              campID: campID,
              dISTLGDCODE: dISTLGDCODE,
              siteDetailId: siteDetailId,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.BasicHealthInfo:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => BasicHealthInfoPatientListScreen(
              campID: campID,
              siteDetailId: siteDetailId,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.SampleCollection:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => HealthScreeningPatientListScreen(
              screeningMenu: menu,
              testId: 7,
              teamid: 0,
              districtID: dISTLGDCODE,
              districtName: dISTNAME,
              campID: campID,
              dISTLGDCODE: dISTLGDCODE,
              siteDetailId: siteDetailId,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.UrineSampleCollection:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => HealthScreeningPatientListScreen(
              screeningMenu: menu,
              testId: 11,
              teamid: 0,
              districtID: dISTLGDCODE,
              districtName: dISTNAME,
              campID: campID,
              dISTLGDCODE: dISTLGDCODE,
              siteDetailId: siteDetailId,
            ),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.AudioScreeningTest:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => AudioScreeningPatientListScreen(campID: campID),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.LungFunctionTest:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => LungFunctionTestPatientListScreen(campID: campID),
          ),
        );
        break;

      case HealthScreeningDetailsMenu.D2DPhysicalExamination:
      case HealthScreeningDetailsMenu.VisualScreeningTest:
        break;
    }
  }
}