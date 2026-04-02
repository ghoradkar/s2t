// ignore_for_file: use_full_hex_values_for_flutter_colors, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/d2d_physical_examination_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/AssignedD2DPhysicalExaminationPatientListScreen/AssignedD2DPhysicalExaminationPatientListScreen.dart';
import 'D2DPhysicalExaminationCampDetailsScreen/D2DPhysicalExaminationCampDetailsScreen.dart';
import 'D2DPhysicalExaminationFilterView/D2DPhysicalExaminationFilterView.dart';

class D2DPhysicalExaminationDetailsScreen extends StatelessWidget {
  const D2DPhysicalExaminationDetailsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<D2DPhysicalExaminationController>(
      init: D2DPhysicalExaminationController(),
      dispose: (_) => Get.delete<D2DPhysicalExaminationController>(),
      builder: (ctrl) {
        return NetworkWrapper(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: "D2D Physical Examination Details",
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
              showActions: true,
              actions: [
                Padding(
                  padding: EdgeInsets.fromLTRB(0, 0, 10.w, 0),
                  child: GestureDetector(
                    onTap: () => _showFilterPopup(context),
                    child: SizedBox(
                      width: 26.w,
                      height: 26.h,
                      child: Image.asset(icFilter),
                    ),
                  ),
                ),
              ],
            ),
            body: KeyboardDismissOnTap(
              dismissOnCapturedTaps: true,
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 10.w),
                child: Column(
                  children: [
                    _buildHeaderRow(),
                    _buildTotalsRow(ctrl),
                    Container(height: 0.5.h, color: Colors.grey),
                    Expanded(
                      child: ctrl.isLoading
                          ? const CommonSkeletonD2DPhysicalExamTable()
                          : ListView.builder(
                              padding: EdgeInsets.zero,
                              itemCount: ctrl.physicalExaminationList.length,
                              itemBuilder: (context, index) {
                                return _buildListRow(
                                  context,
                                  ctrl.physicalExaminationList[index],
                                  ctrl,
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

  Widget _buildHeaderRow() {
    return Container(
      height: 50.h,
      decoration: BoxDecoration(
        color: kWhiteColor,
        border: Border.all(color: Colors.grey, width: 0.5),
      ),
      child: Row(
        children: [
          _headerCell("District"),
          Container(width: 0.5, color: Colors.grey),
          _headerCell("Camp ID"),
          Container(width: 0.5, color: Colors.grey),
          _headerCell("Assigned"),
          Container(width: 0.5, color: Colors.grey),
          _headerCell("Calling Pending"),
          Container(width: 0.5, color: Colors.grey),
          _headerCell("Phy. Exam.\nPending"),
        ],
      ),
    );
  }

  Widget _headerCell(String label) {
    return Expanded(
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

  Widget _buildTotalsRow(D2DPhysicalExaminationController ctrl) {
    return Container(
      height: 40.h,
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: const BorderRadius.only(
          bottomLeft: Radius.circular(6),
          bottomRight: Radius.circular(6),
        ),
      ),
      child: Row(
        children: [
          _totalCell("", hasBothBorders: true),
          _totalCell("Total", isBold: true),
          _totalCell("${ctrl.totalAssigned}", isBold: true),
          _totalCell("${ctrl.totalCallingPending}", isBold: true),
          _totalCell("${ctrl.totalPhyExamPending}", isBold: true),
        ],
      ),
    );
  }

  Widget _totalCell(
    String value, {
    bool isBold = false,
    bool hasBothBorders = false,
  }) {
    return Expanded(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border(
            right: BorderSide(color: Colors.grey, width: 0.5),
            left: hasBothBorders
                ? BorderSide(color: Colors.grey, width: 0.5)
                : BorderSide.none,
          ),
        ),
        child: Center(
          child: Text(
            value,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: Colors.black,
              fontFamily: FontConstants.interFonts,
              fontWeight: isBold ? FontWeight.w700 : FontWeight.w500,
              fontSize: isBold ? 14.sp : 11.sp,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildListRow(
    BuildContext context,
    D2DPhysicalExamDetailsOutput obj,
    D2DPhysicalExaminationController ctrl,
  ) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          left: BorderSide(color: Colors.grey, width: 0.5),
          bottom: BorderSide(color: Colors.grey, width: 0.5),
        ),
      ),
      child: IntrinsicHeight(
        child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          _dataCell(obj.district ?? "", isLink: false),
          _tapCell(
            "${obj.campId ?? ""}",
            onTap: () async {
              await Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) =>
                      D2DPhysicalExaminationCampDetailsScreen(
                        selectedPhysicalExamObj: obj,
                      ),
                ),
              );
              ctrl.fetchData();
            },
          ),
          _tapCell(
            "${obj.assigned ?? ""}",
            onTap: () async {
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
              ctrl.fetchData();
            },
          ),
          _dataCell("${obj.callingPending ?? ""}"),
          _dataCell("${obj.phyExamPending ?? ""}"),
        ],
      ),
      ),
    );
  }

  Widget _dataCell(String value, {bool isLink = false}) {
    return Expanded(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border(
            right: BorderSide(color: Colors.grey, width: 0.5),
          ),
        ),
        child: Center(
          child: Text(
            value,
            textAlign: TextAlign.center,
            style: TextStyle(
              color: isLink ? Colors.blue : Colors.black,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: 14.sp,
            ),
          ),
        ),
      ),
    );
  }

  Widget _tapCell(String value, {required VoidCallback onTap}) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
          decoration: BoxDecoration(
            color: Colors.white,
            border: Border(
              right: BorderSide(color: Colors.grey, width: 0.5),
            ),
          ),
          child: Center(
            child: Text(
              value,
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.blue,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: 14.sp,
              ),
            ),
          ),
        ),
      ),
    );
  }

  void _showFilterPopup(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.black.withValues(alpha: 0.5),
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.0,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: const D2DPhysicalExaminationFilterView(),
        );
      },
    );
  }
}