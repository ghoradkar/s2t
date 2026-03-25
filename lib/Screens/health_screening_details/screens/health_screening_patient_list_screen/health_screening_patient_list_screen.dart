// ignore_for_file: must_be_immutable
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/PhysicalExaminationFormScreen/PhysicalExaminationFormScreen.dart';

import 'package:s2toperational/Screens/health_screening_details/controllers/health_screening_patient_list_controller.dart';
import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import 'health_screening_patient_row/health_screening_patient_row.dart';

class HealthScreeningPatientListScreen extends StatefulWidget {
  final HealthScreeningDetailsMenu screeningMenu;
  final int districtID;
  final String districtName;
  final int campID;
  final int siteDetailId;
  final int testId;
  final int teamid;
  final int dISTLGDCODE;

  const HealthScreeningPatientListScreen({
    super.key,
    required this.screeningMenu,
    required this.districtID,
    required this.districtName,
    required this.campID,
    required this.siteDetailId,
    required this.testId,
    required this.teamid,
    required this.dISTLGDCODE,
  });

  @override
  State<HealthScreeningPatientListScreen> createState() =>
      _HealthScreeningPatientListScreenState();
}

class _HealthScreeningPatientListScreenState
    extends State<HealthScreeningPatientListScreen> {
  late HealthScreeningPatientListController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.put(HealthScreeningPatientListController());
    controller.loadData(
      testId: widget.testId,
      campId: widget.campID,
      teamNumber: '0',
      isRegularCamp: DataProvider().getRegularCamp(),
    );
  }

  @override
  void dispose() {
    Get.delete<HealthScreeningPatientListController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Patient List',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SizedBox(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth,
        child: Column(
          children: [
            AppTextField(
              controller: controller.searchController,
              readOnly: false,
              label: RichText(
                text: TextSpan(
                  text: 'Patient Name/Registration No',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: responsiveFont(14),
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              labelStyle: TextStyle(
                fontSize: responsiveFont(12),
                fontFamily: FontConstants.interFonts,
              ),
              suffixIcon: SizedBox(
                height: responsiveHeight(24),
                width: responsiveHeight(24),
                child: Center(
                  child: Image.asset(
                    icSearch,
                    height: responsiveHeight(24),
                    width: responsiveHeight(24),
                  ),
                ),
              ),
            ).paddingOnly(top: 10, bottom: 8),
            const SizedBox(height: 10),
            Expanded(
              child: Obx(
                () => ListView.builder(
                  shrinkWrap: true,
                  itemCount: controller.patientList.length,
                  itemBuilder: (context, index) {
                    final patientObj = controller.patientList[index];
                    return HealthScreeningPatientRow(
                      obj: patientObj,
                      screeningMenu: widget.screeningMenu,
                      onDidPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => PhysicalExaminationFormScreen(
                              regdId: patientObj.regdId ?? 0,
                              campTypeID: 0,
                              healthScreentype: '16',
                            ),
                          ),
                        ).then((_) => controller.refresh());
                      },
                    );
                  },
                ),
              ),
            ),
          ],
        ).paddingSymmetric(horizontal: 4),
      ),
    );
  }
}