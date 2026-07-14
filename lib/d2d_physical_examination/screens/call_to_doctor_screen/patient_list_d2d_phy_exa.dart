import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/d2d_physical_examination/controller/assigned_patient_list_controller.dart';
import 'package:s2toperational/d2d_physical_examination/screens/assigned_d2d_physical_examination_patient_list/assigned_d2d_physical_examination_patient_row.dart';
import 'package:s2toperational/d2d_physical_examination/screens/call_to_doctor_screen/call_to_doctor_screen.dart';

class PatientListD2DPhyExa extends StatelessWidget {
  final int dISTLGDCODE;
  final int campId;
  final String healthScreentype;
  final String flag;

  const PatientListD2DPhyExa({
    super.key,
    required this.dISTLGDCODE,
    required this.campId,
    required this.healthScreentype,
    required this.flag,
  });

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<AssignedPatientListController>(
      init: AssignedPatientListController(
        campId: campId,
        dISTLGDCODE: dISTLGDCODE,
        healthScreentype: healthScreentype,
        flag: flag,
      ),
      dispose: (_) => Get.delete<AssignedPatientListController>(),
      builder: (ctrl) {
        return NetworkWrapper(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: "Patient List",
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
            ),
            body: KeyboardDismissOnTap(
              dismissOnCapturedTaps: true,
              child: SizedBox(
                height: SizeConfig.screenHeight,
                width: SizeConfig.screenWidth,
                child: Padding(
                  padding: EdgeInsets.fromLTRB(8.h, 8.h, 8.w, 8.h),
                  child: Column(
                    children: [
                      AppTextField(
                        controller: ctrl.searchController,
                        onChange: ctrl.filterList,
                        hint: 'Patient Name/Registration No',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 10,
                        suffixIcon: SizedBox(
                          height: 20.h,
                          width: 20.w,
                          child: Center(
                            child: Image.asset(
                              icSearch,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                      SizedBox(height: 10.h),
                      Expanded(
                        child: Padding(
                          padding: EdgeInsets.symmetric(horizontal: 10.w),
                          child: ctrl.isLoading
                              ? const CommonSkeletonPatientList()
                              : ListView.builder(
                            itemCount: ctrl.searchPatientList.length,
                            itemBuilder: (context, index) {
                              final obj = ctrl.searchPatientList[index];
                              void openCallToDoctor() {
                                debugPrint(
                                  '[CALL_ICON_TAP] regdId=${obj.regdId} campId=${obj.campId} '
                                  'isCall=${obj.isCall} doctorMapStatus=${obj.doctorMapStatus}',
                                );
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                    builder: (_) => CallToDoctorScreen(
                                      regdId: obj.regdId ?? 0,
                                      campId: obj.campId ?? 0,
                                      healthScreentype: ctrl.healthScreentype,
                                    ),
                                  ),
                                ).then((_) {
                                  debugPrint('[CALL_ICON_TAP] returned to Patient List, refreshing');
                                  ctrl.refreshAfterNav();
                                });
                              }

                              return GestureDetector(
                                onTap: openCallToDoctor,
                                child: AssignedD2DPhysicalExaminationPatientRow(
                                  obj: obj,
                                  onCallDidPressed: openCallToDoctor,
                                  serialNumber: index,
                                ),
                              );
                            },
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}