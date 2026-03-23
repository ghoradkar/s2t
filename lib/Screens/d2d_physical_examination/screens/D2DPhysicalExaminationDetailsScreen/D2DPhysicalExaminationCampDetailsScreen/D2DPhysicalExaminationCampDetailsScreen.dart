// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamDetailsResponse/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/utilities/route_observer.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/AssignedD2DPhysicalExaminationPatientListScreen/AssignedD2DPhysicalExaminationPatientListScreen.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/CallTopTeamD2DScreen/CallTopTeamD2DScreen.dart';
import '../../../../../Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/Model/D2DTeamWisePhyExamDetailsResponse.dart';
import 'D2DPhysicalExaminationCampRow/D2DPhysicalExaminationCampRow.dart';

class D2DPhysicalExaminationCampDetailsScreen extends StatefulWidget {
  D2DPhysicalExaminationCampDetailsScreen({
    super.key,
    required this.selectedPhysicalExamObj,
  });

  D2DPhysicalExamDetailsOutput selectedPhysicalExamObj;

  @override
  State<D2DPhysicalExaminationCampDetailsScreen> createState() =>
      _D2DPhysicalExaminationCampDetailsScreenState();
}

class _D2DPhysicalExaminationCampDetailsScreenState
    extends State<D2DPhysicalExaminationCampDetailsScreen>
    with RouteAware {
  int totalAssigned = 0;
  int totalCallingPending = 0;
  int totalPhyExamPending = 0;

  APIManager apiManager = APIManager();
  List<D2DTeamWisePhyExamDetailsOutput> physicalExaminationList = [];

  @override
  void initState() {
    getTeamWiseData();
    super.initState();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final route = ModalRoute.of(context);
    if (route is PageRoute) {
      routeObserver.subscribe(this, route);
    }
  }

  @override
  void dispose() {
    routeObserver.unsubscribe(this);
    super.dispose();
  }

  @override
  void didPopNext() {
    getTeamWiseData();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "D2D Physical Examination Details",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
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
                Container(
                  padding: EdgeInsets.symmetric(
                    vertical: 10.h,
                    horizontal: 8.h,
                  ),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: [
                      BoxShadow(
                        offset: Offset(0, 1),
                        color: Colors.black.withValues(alpha: 0.15),
                        spreadRadius: 3,
                        blurRadius: 8,
                      ),
                    ],
                  ),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Camp ID : ",
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: 14.sp,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    "${widget.selectedPhysicalExamObj.campId ?? 0}",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w500,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "District : ",
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: 14.sp,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    widget.selectedPhysicalExamObj.district ??
                                        "",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w500,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      SizedBox(height: 8.h),

                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          Expanded(
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "Camp Date : ",
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: 14.sp,
                                  ),
                                ),
                                Expanded(
                                  child: Text(
                                    "${widget.selectedPhysicalExamObj.campDate ?? 0}",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w500,
                                      fontSize: 14.sp,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
                SizedBox(height: 8.h),

                Container(
                  height: 50.h,
                  decoration: BoxDecoration(
                    // color: kPrimaryColor,
                    border: Border.all(color: Colors.grey, width: 0.5),
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        flex: 2,
                        child: Center(
                          child: Text(
                            "Team No",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.bold,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                      Container(
                        width: 0.5.w,
                        color: Colors.black.withValues(alpha: 0.4),
                      ),
                      Expanded(
                        flex: 2,
                        child: Center(
                          child: Text(
                            "Assigned",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.bold,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                      Container(
                        width: 0.5.w,
                        color: Colors.black.withValues(alpha: 0.4),
                      ),
                      Expanded(
                        flex: 2,
                        child: Center(
                          child: Text(
                            "Calling\nPending",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.bold,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                      Container(
                        width: 0.5.w,
                        color: Colors.black.withValues(alpha: 0.4),
                      ),
                      Expanded(
                        flex: 2,
                        child: Center(
                          child: Text(
                            "Phy Exam.\nPending",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.bold,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                      Container(
                        width: 0.5.w,
                        color: Colors.black.withValues(alpha: 0.4),
                      ),
                      Expanded(
                        flex: 1,
                        child: Center(
                          child: Text(
                            "Call",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.bold,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),

                // Total Row
                Container(
                  height: 40.h,
                  decoration: BoxDecoration(color: kWhiteColor),
                  child: Row(
                    children: [
                      Expanded(
                        flex: 2,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                              left: BorderSide(color: Colors.grey, width: 0.5),
                              bottom: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "Total",
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
                      ),
                      Expanded(
                        flex: 2,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                              bottom: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "$totalAssigned",
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
                      ),
                      Expanded(
                        flex: 2,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                              bottom: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "$totalCallingPending",
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
                      ),
                      Expanded(
                        flex: 2,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                              bottom: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "$totalPhyExamPending",
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
                      ),
                      Expanded(
                        flex: 1,
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                              bottom: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "",
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
                      ),
                    ],
                  ),
                ),
                SizedBox(height: 6.h),
                Expanded(
                  child: ListView.builder(
                    itemCount: physicalExaminationList.length,
                    itemBuilder: (context, index) {
                      D2DTeamWisePhyExamDetailsOutput obj =
                          physicalExaminationList[index];
                      return D2DPhysicalExaminationCampRow(
                        obj: obj,
                        onAssignedDidPressed: () async {
                          await Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder:
                                  (context) =>
                                      AssignedD2DPhysicalExaminationPatientListScreen(
                                        dISTLGDCODE: obj.dISTLGDCODE ?? 0,
                                        campId: obj.campId ?? 0,
                                        healthScreentype: "16",
                                        flag: "2",
                                      ),
                            ),
                          );
                          getTeamWiseData();
                        },
                        onTeamNoIDDidPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder:
                                  (context) => CallTopTeamD2DScreen(
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
                              builder:
                                  (context) => CallTopTeamD2DScreen(
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
  }

  void getTeamWiseData() {
    ToastManager.showLoader();
    totalAssigned = 0;
    totalCallingPending = 0;
    totalPhyExamPending = 0;
    Map<String, String> params = {
      "Type": "1",
      "CampID": widget.selectedPhysicalExamObj.campId.toString(),
      "DoctorID": widget.selectedPhysicalExamObj.doctorID.toString(),
      "TeamID": "0",
    };
    apiManager.getD2DTeamWisePhyExamDetailsAPI(
      params,
      apiD2DTeamWisePhyExamDetailsCallBack,
    );
  }

  void apiD2DTeamWisePhyExamDetailsCallBack(
    D2DTeamWisePhyExamDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      physicalExaminationList = response?.output ?? [];
      totalAssigned = 0;
      totalCallingPending = 0;
      totalPhyExamPending = 0;
      for (D2DTeamWisePhyExamDetailsOutput objL in physicalExaminationList) {
        totalAssigned += objL.assigned ?? 0;
        totalCallingPending += objL.callingPending ?? 0;
        totalPhyExamPending += objL.phyExamPending ?? 0;
      }
    } else {
      totalAssigned = 0;
      totalCallingPending = 0;
      totalPhyExamPending = 0;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }
}
