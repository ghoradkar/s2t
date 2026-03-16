// ignore_for_file: use_full_hex_values_for_flutter_colors, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import '../../../Modules/Json_Class/AllDistrictListForPhyExamResponse/AllDistrictListForPhyExamResponse.dart';
import '../../../Modules/Json_Class/D2DPhysicalExamDetailsResponse/D2DPhysicalExamDetailsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/utilities/route_observer.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../AssignedD2DPhysicalExaminationPatientListScreen/AssignedD2DPhysicalExaminationPatientListScreen.dart';
import 'D2DPhysicalExaminationCampDetailsScreen/D2DPhysicalExaminationCampDetailsScreen.dart';
import 'D2DPhysicalExaminationFilterView/D2DPhysicalExaminationFilterView.dart';

// import 'D2DPhysicalExaminationTotalDetailsView/D2DPhysicalExaminationTotalDetailsView.dart';
// import 'D2DPhysicalExaminationRow/D2DPhysicalExaminationRow.dart';

class D2DPhysicalExaminationDetailsScreen extends StatefulWidget {
  const D2DPhysicalExaminationDetailsScreen({super.key});

  @override
  State<D2DPhysicalExaminationDetailsScreen> createState() =>
      _D2DPhysicalExaminationDetailsScreenState();
}

class _D2DPhysicalExaminationDetailsScreenState
    extends State<D2DPhysicalExaminationDetailsScreen>
    with RouteAware {
  String selectedFromDate = "";
  String selectedToDate = "";
  AllDistrictListForPhyExamOutput? selectedDistrict;
  List<D2DPhysicalExamDetailsOutput> physicalExaminationList = [];
  APIManager apiManager = APIManager();
  int empCode = 0;
  int totalAssigned = 0;
  int totalCallingPending = 0;
  int totalPhyExamPending = 0;
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    selectedFromDate = FormatterManager.formatDateToString(DateTime.now());
    selectedToDate = FormatterManager.formatDateToString(DateTime.now());
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedDistrict = AllDistrictListForPhyExamOutput(
      dISTLGDCODE: 0,
      district: "All",
    );
    getAdminData("4");
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
    getAdminData("4");
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
        showActions: true,
        actions: [
          Padding(
            padding: EdgeInsets.fromLTRB(0, 0, 10.w, 0),
            child: GestureDetector(
              onTap: () {
                showFilterPopup(context);
              },
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
              Container(
                height: 50.h,
                decoration: BoxDecoration(
                  color: kWhiteColor,
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                    left: BorderSide(color: Colors.grey, width: 0.5),
                    top: BorderSide(color: Colors.grey, width: 0.5),
                    bottom: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Center(
                        child: Text(
                          "District",
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
                    Container(width: 0.5.w, color: Colors.grey),
                    Expanded(
                      child: Center(
                        child: Text(
                          "Camp ID",
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
                    Container(width: 0.5.w, color: Colors.grey),
                    Expanded(
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
                    Container(width: 0.5.w, color: Colors.grey),
                    Expanded(
                      child: Center(
                        child: Text(
                          "Calling Pending",
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
                    Container(width: 0.5.w, color: Colors.grey),
                    Expanded(
                      child: Center(
                        child: Text(
                          "Phy. Exam.\nPending",
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

              Container(
                height: 40.h,
                decoration: BoxDecoration(
                  color: kWhiteColor,
                  borderRadius: BorderRadius.only(
                    bottomLeft: Radius.circular(6),
                    bottomRight: Radius.circular(6),
                  ),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 4.h,
                          horizontal: 4.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            right: BorderSide(color: Colors.grey, width: 0.5.w),
                            left: BorderSide(color: Colors.grey, width: 0.5.w),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: 11.sp,
                            ),
                          ),
                        ),
                      ),
                    ),

                    Expanded(
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 4.h,
                          horizontal: 4.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            right: BorderSide(color: Colors.grey, width: 0.5.w),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "Total",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                    ),

                    Expanded(
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 4.h,
                          horizontal: 4.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            right: BorderSide(color: Colors.grey, width: 0.5.w),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "$totalAssigned",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                    Expanded(
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 4.h,
                          horizontal: 4.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            right: BorderSide(color: Colors.grey, width: 0.5.w),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "$totalCallingPending",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                    Expanded(
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 4.h,
                          horizontal: 4.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            right: BorderSide(color: Colors.grey, width: 0.5.w),
                          ),
                        ),
                        child: Center(
                          child: Text(
                            "$totalPhyExamPending",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: 14.sp,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Container(height: 0.5.h, color: Colors.grey),
              Expanded(
                child:
                    isLoading
                        ? const CommonSkeletonD2DPhysicalExamTable()
                        : ListView.builder(
                          padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                          itemCount: physicalExaminationList.length,
                          itemBuilder: (context, index) {
                            D2DPhysicalExamDetailsOutput obj =
                                physicalExaminationList[index];
                            return Container(
                              decoration: BoxDecoration(
                                color: Colors.white,
                                border: Border(
                                  bottom: BorderSide(
                                    color: Colors.grey,
                                    width: 0.5.w,
                                  ),
                                ),
                              ),
                              child: Column(
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Expanded(
                                        child: Container(
                                          padding: EdgeInsets.symmetric(
                                            vertical: 4.h,
                                            horizontal: 4.w,
                                          ),
                                          decoration: BoxDecoration(
                                            color: Colors.white,
                                            border: Border(
                                              right: BorderSide(
                                                color: Colors.grey,
                                                width: 0.5.w,
                                              ),
                                              left: BorderSide(
                                                color: Colors.grey,
                                                width: 0.5.w,
                                              ),
                                            ),
                                          ),
                                          child: Center(
                                            child: Text(
                                              obj.district ?? "",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.black,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w600,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Expanded(
                                        child: GestureDetector(
                                          onTap: () async {
                                            await Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder:
                                                    (
                                                      context,
                                                    ) => D2DPhysicalExaminationCampDetailsScreen(
                                                      selectedPhysicalExamObj:
                                                          obj,
                                                    ),
                                              ),
                                            );
                                            getAdminData("4");
                                          },
                                          child: Container(
                                            padding: EdgeInsets.symmetric(
                                              vertical: 4.h,
                                              horizontal: 4.w,
                                            ),
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                              border: Border(
                                                right: BorderSide(
                                                  color: Colors.grey,
                                                  width: 0.5.w,
                                                ),
                                              ),
                                            ),
                                            child: Text(
                                              "${obj.campId ?? ""}",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.blue,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w600,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        width: 0.5.w,
                                        color: Colors.grey,
                                      ),
                                      Expanded(
                                        child: GestureDetector(
                                          onTap: () async {
                                            await Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                builder:
                                                    (context) =>
                                                        AssignedD2DPhysicalExaminationPatientListScreen(
                                                          dISTLGDCODE:
                                                              obj.dISTLGDCODE ??
                                                              0,
                                                          campId:
                                                              obj.campId ?? 0,
                                                          healthScreentype:
                                                              "16",
                                                          flag: "2",
                                                        ),
                                              ),
                                            );
                                            getAdminData("4");
                                          },
                                          child: Container(
                                            padding: EdgeInsets.symmetric(
                                              vertical: 4.h,
                                              horizontal: 4.w,
                                            ),
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                              border: Border(
                                                right: BorderSide(
                                                  color: Colors.grey,
                                                  width: 0.5.w,
                                                ),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "${obj.assigned ?? ""}",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.blue,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w600,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        width: 0.5.w,
                                        color: Colors.grey,
                                      ),
                                      Expanded(
                                        child: Container(
                                          padding: EdgeInsets.symmetric(
                                            vertical: 4.h,
                                            horizontal: 4.w,
                                          ),
                                          decoration: BoxDecoration(
                                            color: Colors.white,
                                            border: Border(
                                              right: BorderSide(
                                                color: Colors.grey,
                                                width: 0.5.w,
                                              ),
                                            ),
                                          ),
                                          child: Center(
                                            child: Text(
                                              "${obj.callingPending ?? ""}",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.black,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w600,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      Container(
                                        width: 0.5.w,
                                        color: Colors.grey,
                                      ),
                                      Expanded(
                                        child: Container(
                                          padding: EdgeInsets.symmetric(
                                            vertical: 4.h,
                                            horizontal: 4.w,
                                          ),
                                          decoration: BoxDecoration(
                                            color: Colors.white,
                                            border: Border(
                                              right: BorderSide(
                                                color: Colors.grey,
                                                width: 0.5.w,
                                              ),
                                            ),
                                          ),
                                          child: Center(
                                            child: Text(
                                              "${obj.phyExamPending ?? ""}",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.black,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w600,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                      SizedBox(height: 2.h),
                                      Container(
                                        height: 0.5.h,
                                        color: Colors.grey,
                                      ),
                                      SizedBox(height: 2.h),
                                    ],
                                  ),
                                ],
                              ),
                            );
                          },
                        ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void getAdminData(String loginType) {
    setState(() {
      isLoading = true;
    });
    Map<String, String> param = {
      "LoginType": loginType,
      "FromDate": selectedFromDate,
      "ToDate": selectedToDate,
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "",
      "DoctorID": empCode.toString(),
    };
    apiManager.getD2DPhysicalExamDetailsAPI(
      param,
      apiD2DPhysicalExamDetailsCallBack,
    );
  }

  void apiD2DPhysicalExamDetailsCallBack(
    D2DPhysicalExamDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      physicalExaminationList = response?.output ?? [];
      totalAssigned = 0;
      totalCallingPending = 0;
      totalPhyExamPending = 0;
      for (D2DPhysicalExamDetailsOutput objL in physicalExaminationList) {
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
    isLoading = false;
    setState(() {});
  }

  void showFilterPopup(BuildContext parentContext) {
    showModalBottomSheet(
      context: parentContext,
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
          child: D2DPhysicalExaminationFilterView(
            selectedFromDate: selectedFromDate,
            selectedToDate: selectedToDate,
            selectedDistrict: selectedDistrict,
            onSelectedFromDate: (p0) {
              selectedFromDate = p0;
            },
            onSelectedToDate: (p0) {
              selectedToDate = p0;
            },
            onSelectedDistrict: (p0) {
              selectedDistrict = p0;
            },
            onApply: () {
              getAdminData("4");
            },
          ),
        );
      },
    );
  }
}
