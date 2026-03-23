import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/assigned_patient_list_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/PhysicalExaminationFormScreen/PhysicalExaminationFormScreen.dart';
import 'AssignedD2DPhysicalExaminationPatientRow/AssignedD2DPhysicalExaminationPatientRow.dart';

class AssignedD2DPhysicalExaminationPatientListScreen extends StatelessWidget {
  final int dISTLGDCODE;
  final int campId;
  final String healthScreentype;
  final String flag;

  const AssignedD2DPhysicalExaminationPatientListScreen({
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
        return Scaffold(
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
                                  return GestureDetector(
                                    onTap: () {
                                      final subOrgId = obj.isPhy ?? 0;
                                      final isCall = obj.isCall ?? "0";
                                      if (isCall == "0") {
                                        ToastManager.showAlertDialog(
                                          context,
                                          "Call to Beneficiary to open PHY. Examination form",
                                          () => Navigator.pop(context),
                                        );
                                      } else if (subOrgId == 2) {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (_) => PhysicalExaminationFormScreen(
                                              regdId: obj.regdId ?? 0,
                                              campTypeID: 0,
                                              healthScreentype: "",
                                            ),
                                          ),
                                        );
                                      } else if (subOrgId == 3) {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (_) => PhysicalExaminationFormScreen(
                                              regdId: obj.regdId ?? 0,
                                              campTypeID: obj.campTypeID ?? 0,
                                              healthScreentype: ctrl.healthScreentype,
                                            ),
                                          ),
                                        ).then((_) => ctrl.refreshAfterNav());
                                      }
                                    },
                                    child: AssignedD2DPhysicalExaminationPatientRow(
                                      obj: obj,
                                      onCallDidPressed: () async {
                                        ctrl.mobileNo = obj.mobileNo ?? "";
                                        if (ctrl.isUserCreatedBy == 0) {
                                          await ctrl.insertCallDetails(obj.regdId ?? 0);
                                        } else {
                                          await ctrl.getCallingStatusNew(obj.regdId ?? 0);
                                        }
                                      },
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
        );
      },
    );
  }
}