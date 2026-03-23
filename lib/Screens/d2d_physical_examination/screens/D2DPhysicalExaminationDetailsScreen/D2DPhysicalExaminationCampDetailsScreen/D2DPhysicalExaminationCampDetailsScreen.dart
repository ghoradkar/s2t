// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/d2d_camp_details_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/AssignedD2DPhysicalExaminationPatientListScreen/AssignedD2DPhysicalExaminationPatientListScreen.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/CallTopTeamD2DScreen/CallTopTeamD2DScreen.dart';
import 'D2DPhysicalExaminationCampRow/D2DPhysicalExaminationCampRow.dart';

class D2DPhysicalExaminationCampDetailsScreen extends StatelessWidget {
  final D2DPhysicalExamDetailsOutput selectedPhysicalExamObj;

  const D2DPhysicalExaminationCampDetailsScreen({
    super.key,
    required this.selectedPhysicalExamObj,
  });

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<D2DCampDetailsController>(
      init: D2DCampDetailsController(),
      initState: (_) => WidgetsBinding.instance.addPostFrameCallback((_) {
        Get.find<D2DCampDetailsController>().fetchData(
          campId: selectedPhysicalExamObj.campId ?? 0,
          doctorId: selectedPhysicalExamObj.doctorID ?? 0,
        );
      }),
      dispose: (_) => Get.delete<D2DCampDetailsController>(),
      builder: (ctrl) {
        return Scaffold(
          appBar: mAppBar(
            scTitle: "D2D Physical Examination Details",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
          ),
          body: KeyboardDismissOnTap(
            dismissOnCapturedTaps: true,
            child: SizedBox(
              height: SizeConfig.screenHeight,
              width: SizeConfig.screenWidth,
              child: Padding(
                padding: EdgeInsets.fromLTRB(12.h, 8.h, 12.h, 8.h),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildCampInfoCard(),
                    SizedBox(height: 8.h),
                    _buildHeaderRow(),
                    _buildTotalsRow(ctrl),
                    SizedBox(height: 6.h),
                    Expanded(
                      child: ListView.builder(
                        itemCount: ctrl.physicalExaminationList.length,
                        itemBuilder: (context, index) {
                          final obj = ctrl.physicalExaminationList[index];
                          return D2DPhysicalExaminationCampRow(
                            obj: obj,
                            onAssignedDidPressed: () async {
                              await Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) =>
                                      AssignedD2DPhysicalExaminationPatientListScreen(
                                        dISTLGDCODE: obj.dISTLGDCODE ?? 0,
                                        campId: obj.campId ?? 0,
                                        healthScreentype: "16",
                                        flag: "2",
                                      ),
                                ),
                              );
                              ctrl.fetchData(
                                campId: selectedPhysicalExamObj.campId ?? 0,
                                doctorId:
                                    selectedPhysicalExamObj.doctorID ?? 0,
                              );
                            },
                            onTeamNoIDDidPressed: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => CallTopTeamD2DScreen(
                                    campId: obj.campId ?? 0,
                                    doctorID: obj.doctorID ?? 0,
                                    teamid: obj.teamid ?? "",
                                  ),
                                ),
                              );
                            },
                            onCallTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => CallTopTeamD2DScreen(
                                    campId: obj.campId ?? 0,
                                    doctorID: obj.doctorID ?? 0,
                                    teamid: obj.teamid ?? "",
                                  ),
                                ),
                              );
                            },
                          );
                        },
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

  Widget _buildCampInfoCard() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 8.h),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.15),
            spreadRadius: 3,
            blurRadius: 8,
          ),
        ],
      ),
      child: Column(
        children: [
          Row(
            children: [
              Expanded(
                child: _infoRow(
                  "Camp ID : ",
                  "${selectedPhysicalExamObj.campId ?? 0}",
                ),
              ),
              Expanded(
                child: _infoRow(
                  "District : ",
                  selectedPhysicalExamObj.district ?? "",
                ),
              ),
            ],
          ),
          SizedBox(height: 8.h),
          Row(
            children: [
              Expanded(
                child: _infoRow(
                  "Camp Date : ",
                  "${selectedPhysicalExamObj.campDate ?? 0}",
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _infoRow(String label, String value) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          label,
          style: TextStyle(
            color: Colors.black,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: 14.sp,
          ),
        ),
        Expanded(
          child: Text(
            value,
            style: TextStyle(
              color: Colors.black,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w500,
              fontSize: 14.sp,
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildHeaderRow() {
    return Container(
      height: 50.h,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey, width: 0.5),
      ),
      child: Row(
        children: [
          _headerCell("Team No", flex: 2),
          _divider(),
          _headerCell("Assigned", flex: 2),
          _divider(),
          _headerCell("Calling\nPending", flex: 2),
          _divider(),
          _headerCell("Phy Exam.\nPending", flex: 2),
          _divider(),
          _headerCell("Call", flex: 1),
        ],
      ),
    );
  }

  Widget _headerCell(String label, {int flex = 1}) {
    return Expanded(
      flex: flex,
      child: Center(
        child: Text(
          label,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: Colors.black,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.bold,
            fontSize: 14.sp,
          ),
        ),
      ),
    );
  }

  Widget _divider() =>
      Container(width: 0.5.w, color: Colors.black.withValues(alpha: 0.4));

  Widget _buildTotalsRow(D2DCampDetailsController ctrl) {
    return Container(
      height: 40.h,
      color: kWhiteColor,
      child: Row(
        children: [
          _totalCell("Total", flex: 2, hasBothBorders: true),
          _totalCell("${ctrl.totalAssigned}", flex: 2),
          _totalCell("${ctrl.totalCallingPending}", flex: 2),
          _totalCell("${ctrl.totalPhyExamPending}", flex: 2),
          _totalCell("", flex: 1),
        ],
      ),
    );
  }

  Widget _totalCell(
    String value, {
    int flex = 1,
    bool hasBothBorders = false,
  }) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border(
            right: const BorderSide(color: Colors.grey, width: 0.5),
            left: hasBothBorders
                ? const BorderSide(color: Colors.grey, width: 0.5)
                : BorderSide.none,
            bottom: const BorderSide(color: Colors.grey, width: 0.5),
          ),
        ),
        child: Center(
          child: Text(
            value,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: Colors.black,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: 14.sp,
            ),
          ),
        ),
      ),
    );
  }
}