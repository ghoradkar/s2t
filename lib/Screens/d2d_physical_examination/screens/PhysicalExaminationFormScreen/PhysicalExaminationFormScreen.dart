// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/physical_examination_form_controller.dart';
import 'BasciHealthInfo.dart';
import 'AlleriesSurgeriesAndSymptomsView/AlleriesSurgeriesAndSymptomsCard/AlleriesSurgeiesAndSymptoms.dart';
import 'LiverExaminationHistoryView/LiverExaminationHistoryView.dart';
import 'MedicalHistroyView/MedicalHistroyView.dart';
import 'PatientInformationView/PatientInformationView.dart';

class PhysicalExaminationFormScreen extends StatelessWidget {
  final int regdId;
  final int campTypeID;
  final String healthScreentype;

  const PhysicalExaminationFormScreen({
    super.key,
    required this.regdId,
    required this.campTypeID,
    required this.healthScreentype,
  });

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<PhysicalExaminationFormController>(
      init: PhysicalExaminationFormController(
        regdId: regdId,
        campTypeID: campTypeID,
        healthScreentype: healthScreentype,
      ),
      dispose: (_) => Get.delete<PhysicalExaminationFormController>(),
      builder: (ctrl) {
        return PopScope(
          canPop: true,
          child: NetworkWrapper(
            child: Scaffold(
              backgroundColor: kWhiteColor,
              appBar: mAppBar(
                scTitle: DataProvider().getRegularCamp()
                    ? "Physical Examination"
                    : "D2D Physical Examination",
                leadingIcon: iconBackArrow,
                onLeadingIconClick: () => Navigator.pop(context),
              ),
              body: SafeArea(
                child: SingleChildScrollView(
                  child: Padding(
                    padding: EdgeInsets.only(
                      left: 12.w,
                      right: 12.w,
                      bottom: MediaQuery.of(context).viewPadding.bottom + 20.h,
                    ),
                    child: Form(
                      key: ctrl.formKey,
                      child: Column(
                        children: [
                          SizedBox(height: 10.h),
                          PatientInformationView(patientObj: ctrl.patientObj),
                          BasicHealthInfo(patientObj: ctrl.patientObj),
                          AlleriesSurgeriesAndSymptomsView(),
                          MedicalHistroyView(patientObj: ctrl.patientObj),
                          _liverExaminationHistory(ctrl),
                          SizedBox(height: 30.h),
                          SizedBox(
                            width: 150.w,
                            child: Center(
                              child: AppActiveButton(
                                buttontitle: "Save",
                                onTap: ctrl.onSaveTapped,
                              ),
                            ),
                          ).paddingOnly(bottom: 20.h),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  Widget _liverExaminationHistory(PhysicalExaminationFormController ctrl) {
    if (campTypeID == 6) {
      return LiverExaminationHistoryView(
        patientObj: ctrl.patientObj,
        physicalExaminationFormDataManager: ctrl.formManager,
      );
    }
    return const SizedBox.shrink();
  }
}