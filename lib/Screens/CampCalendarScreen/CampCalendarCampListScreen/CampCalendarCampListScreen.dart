// ignore_for_file: must_be_immutable, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import '../../../Modules/Json_Class/MonthlySurveySiteResponse/MonthlySurveySiteResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/APIConstants.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'CampCalendarCampListRow/CampCalendarCampListRow.dart';

class CampCalendarCampListScreen extends StatefulWidget {
  CampCalendarCampListScreen({
    super.key,
    required this.year,
    required this.month,
    required this.day,
    required this.isShowCampType,
    required this.selectedCampType,
    required this.isFromCampCalener,
    required this.subOrgId,
    required this.dIVID,
    required this.distCode,
    required this.calanderSelectedDate,
    required this.isTodayCount,
  });

  String year = "";
  String month = "";
  String day = "";
  bool isShowCampType = false;
  CampTypeAndCatagoryOutput? selectedCampType;
  bool isFromCampCalener = false;
  int subOrgId = 0;
  int dIVID = 0;
  int distCode = 0;
  String calanderSelectedDate = "";
  bool isTodayCount = false;

  @override
  State<CampCalendarCampListScreen> createState() =>
      _CampCalendarCampListScreenState();
}

class _CampCalendarCampListScreenState
    extends State<CampCalendarCampListScreen> {
  APIManager apiManager = APIManager();

  int dESGID = 0;
  int empCode = 0;
  int totalRegistrationWorkers = 0;

  List<MonthlySurveySiteOutput> campList = [];
  bool isLoading = true;

  @override
  void initState() {
    super.initState();

    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    getUserAttendanceDays();
  }

  getUserAttendanceDays() {
    Map<String, String> params = {};

    String urlString = "";

    if (widget.isFromCampCalener) {
      String method = APIConstants.kGetMonthlySurveySiteRequestForOSOrg;
      urlString = "${APIManager.kD2DBaseURL}$method";
      params = {
        "Month": widget.month,
        "Year": widget.year,
        "DistCode": widget.distCode.toString(),
        "CampType": widget.selectedCampType?.cAMPTYPE.toString() ?? "0",
        "SubOrgId": widget.subOrgId.toString(),
        "DIVID": widget.dIVID.toString(),
        "UserId": empCode.toString(),
        "DESGID": dESGID.toString(),
      };
    } else {
      if (widget.isTodayCount) {
        String method = APIConstants.kGetMonthlySurveySiteRequestForOS;
        urlString = "${APIManager.kConstructionWorkerBaseURL}$method";
      } else {
        String method = APIConstants.kGetMonthlySurveySiteRequestForOS;
        urlString = "${APIManager.kConstructionWorkerBaseURL}$method";
      }

      params = {
        "Month": widget.month,
        "Year": widget.year,
        "DistCode": widget.distCode.toString(),
        "CampType": widget.selectedCampType?.cAMPTYPE.toString() ?? "0",
      };
    }

    print(params);
    apiManager.getUserAttendanceDaysAPI(
      urlString,
      params,
      apiUserAttendanceDaysCallBack,
    );
  }

  void apiUserAttendanceDaysCallBack(
    MonthlySurveySiteResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      List<MonthlySurveySiteOutput> tempList = response?.output ?? [];
      tempList.sort((a, b) {
        final aValue = a.rEGISTERWORKERS ?? 0;
        final bValue = b.rEGISTERWORKERS ?? 0;
        return aValue.compareTo(bValue);
      });

      if (widget.isTodayCount) {
        String systemDate = FormatterManager.formatDateToFormatterString(
          DateTime.now(),
          "yyyy-MM-dd",
        );
        for (MonthlySurveySiteOutput obj in tempList) {
          String campDate = obj.campDate ?? "";
          if (systemDate == campDate) {
            totalRegistrationWorkers += obj.rEGISTERWORKERS ?? 0;
            campList.add(obj);
          }
        }
      } else {
        if (widget.calanderSelectedDate.isNotEmpty) {
          for (MonthlySurveySiteOutput obj in tempList) {
            String campDate = obj.campDate ?? "";
            if (widget.calanderSelectedDate == campDate) {
              int rEGISTERWORKERS = obj.rEGISTERWORKERS ?? 0;
              totalRegistrationWorkers += rEGISTERWORKERS;

              // if (totalRegistrationWorkers != 0) {
              campList.add(obj);
              // }
            }
          }
        } else {
          for (MonthlySurveySiteOutput obj in tempList) {
            totalRegistrationWorkers += obj.rEGISTERWORKERS ?? 0;
            campList.add(obj);
          }
        }
      }
    } else {
      campList = [];
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'All Camp List',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),
          child: Column(
            children: [
              Container(
                width: SizeConfig.screenWidth,
                height: 50.h,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(10),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withValues(alpha: 0.15),
                      blurRadius: 10,
                    ),
                  ],
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Row(
                        children: [
                          Container(
                            width: 20.w,
                            height: 20.h,
                            decoration: BoxDecoration(
                              color: todayCampOpenColor,
                              borderRadius: BorderRadius.circular(5),
                            ),
                          ),
                          SizedBox(width: 4.w),

                          Text(
                            "Today's Open Camps",
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ],
                      ),
                    ),
                    Expanded(
                      child: Row(
                        children: [
                          Container(
                            width: 20.w,
                            height: 20.h,
                            decoration: BoxDecoration(
                              color: campOpenColor,
                              borderRadius: BorderRadius.circular(5),
                            ),
                          ),
                          SizedBox(width: 4.w),
                          Text(
                            "Open Camps",
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 12.sp,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: [
                  Text(
                    "Total : ",
                    textAlign: TextAlign.end,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      fontSize: 14.sp,
                    ),
                  ),
                  SizedBox(width: 4.w),

                  Text(
                    "$totalRegistrationWorkers",
                    style: TextStyle(
                      color: kListTitleTextColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      fontSize: 14.sp,
                    ),
                  ),
                ],
              ).paddingOnly(top: 6.h, bottom: 0.h,right: 4.w),

              Expanded(
                child:
                    isLoading
                        ? const CommonSkeletonPatientList()
                        : ListView.builder(
                          itemCount: campList.length,
                          shrinkWrap: true,
                          itemBuilder: (context, index) {
                            MonthlySurveySiteOutput obj = campList[index];
                            return CampCalendarCampListRow(
                              obj: obj,
                              selectedCampType: widget.selectedCampType,
                              index: index,
                            );
                          },
                        ),
              ),
            ],
          ).paddingSymmetric(vertical: 10.h, horizontal: 10.w),
        ),
      ),
    );
  }
}
