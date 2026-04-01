// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LabDataResponse/LabDataResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/RecollectionBeneficiaryToTeamResponse/RecollectionBeneficiaryToTeamResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/utilities/WidgetPaddingX.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../RejectedBeneficiaryListScreen/RejectedBeneficiaryListScreen.dart';
import '../RejectedBeneficiaryTrackingScreen/RejectedBeneficiaryTrackingScreen.dart';

class DailyWorkDashboardScreen extends StatefulWidget {
  const DailyWorkDashboardScreen({super.key});

  @override
  State<DailyWorkDashboardScreen> createState() =>
      _DailyWorkDashboardScreenState();
}

class _DailyWorkDashboardScreenState extends State<DailyWorkDashboardScreen> {
  String fromDate = "";
  String toDate = "";
  bool regularCamp = true;
  bool doorToDoorCamp = false;
  int dESGID = 0;
  int empCode = 0;

  // LabDataOutput? selectedLab;
  bool isShowFilter = false;

  // String searchFilterId = "0";
  bool isShowSubOrg = false;

  int? rejectedBeneficiaryCount;
  int? screeningConfimPendingCount;
  int? assignmentPendingCount;
  int? interestedInScreeningCount;
  int? notinterestedInScreeningCount;
  int? wrongNumberCount;
  int? notAvailableForScreeningCount;
  int? sampleCollectedCount;
  int? reScreeningPendingCount;
  int? deniedScreeningCount;
  APIManager apiManager = APIManager();
  SubOrganizationOutput? selectedSubOrganization;
  BindDivisionOutput? selectedDivision;
  BindDistrictOutput? selectedDistrict;
  LandingLabCampCreationOutput? selectedLabVal;
  bool isLoading = true;

  bool get _isGroup1 => [86, 64, 35, 129, 146].contains(dESGID);
  bool get _isGroup3 => [170, 171, 182, 183].contains(dESGID);
  bool get _isAdminDesignation => [
        170, 171, 51, 26, 30, 128, 182, 183, 78,
        47, 83, 101, 102, 103, 168, 173, 196, 198, 60, 61, 69, 79,
      ].contains(dESGID);

  @override
  void initState() {
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    const adminDesignations = [
      170, 171, 51, 26, 30, 128, 182, 183, 78,
      47, 83, 101, 102, 103, 168, 173, 196, 198, 60, 61, 69, 79,
    ];
    final int daysBack = adminDesignations.contains(dESGID) ? 10 : 7;
    DateTime firstDate = DateTime.now().subtract(Duration(days: daysBack));
    fromDate = FormatterManager.formatDateToString(firstDate);
    toDate = FormatterManager.formatDateToString(DateTime.now());

    callAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Daily Work Dashboard",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
        showActions: !_isGroup1,
        actions: [
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
            child: GestureDetector(
              onTap: () {
                // showDailyWorkDashboardFilterBottomSheet();
                isShowFilter = !isShowFilter;
                setState(() {});
              },
              child: SizedBox(
                width: 26,
                height: 26,
                child: Image.asset(icFilter, color: kWhiteColor),
              ),
            ),
          ),
        ],
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: Padding(
          padding: const EdgeInsets.fromLTRB(0, 12, 0, 12),
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (!_isGroup1)
                Container(
                  width: MediaQuery.of(context).size.width,
                  height: 50,
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    border: Border.all(color: Colors.white, width: 1),
                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(20),
                      bottomLeft: Radius.circular(20),
                      topRight: Radius.circular(20),
                      bottomRight: Radius.circular(20),
                    ),
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: GestureDetector(
                          onTap: () {
                            doorToDoorCamp = false;
                            regularCamp = true;
                            callAPI();
                            setState(() {});
                          },
                          child: Container(
                            decoration: BoxDecoration(
                              color:
                                  regularCamp == true
                                      ? kPrimaryColor
                                      : kWhiteColor,
                              borderRadius: const BorderRadius.only(
                                topLeft: Radius.circular(20),
                                bottomLeft: Radius.circular(20),
                              ),
                              border: Border.all(
                                color: Colors.grey.withValues(alpha: 0.3),
                              ),
                            ),
                            child: Center(
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.center,
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  SizedBox(
                                    width: 20,
                                    height: 20,
                                    child: Image.asset(
                                      icnTent,
                                      color:
                                          regularCamp == false
                                              ? Colors.white
                                              : kPrimaryColor,
                                    ),
                                  ),
                                  const SizedBox(width: 6),
                                  Text(
                                    "Regular Camp",
                                    style: TextStyle(
                                      fontFamily: "Inter",
                                      color:
                                          regularCamp == false
                                              ? kPrimaryColor
                                              : kWhiteColor,
                                      fontSize: responsiveFont(14),
                                      fontWeight: FontWeight.normal,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ),
                      Expanded(
                        child: GestureDetector(
                          onTap: () async {
                            doorToDoorCamp = true;
                            regularCamp = false;
                            await callAPI();
                            setState(() {});
                          },
                          child: Container(
                            decoration: BoxDecoration(
                              color:
                                  doorToDoorCamp == true
                                      ? kPrimaryColor
                                      : kWhiteColor,
                              borderRadius: const BorderRadius.only(
                                topRight: Radius.circular(20),
                                bottomRight: Radius.circular(20),
                              ),
                              border: Border.all(
                                color: Colors.grey.withValues(alpha: 0.3),
                              ),
                            ),
                            child: Center(
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.center,
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  SizedBox(
                                    width: 20,
                                    height: 20,
                                    child: Image.asset(
                                      "assets/icons/home-2.png",
                                      color:
                                          doorToDoorCamp == false
                                              ? Colors.white
                                              : kPrimaryColor,
                                    ),
                                  ),
                                  const SizedBox(width: 6),
                                  Text(
                                    "Door to Door Camp",
                                    style: TextStyle(
                                      fontFamily: "Inter",
                                      color:
                                          doorToDoorCamp == false
                                              ? kPrimaryColor
                                              : kWhiteColor,
                                      fontSize: responsiveFont(14),
                                      fontWeight: FontWeight.normal,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ).paddingSymmetric(horizontal: 6, vertical: 4),
                const SizedBox(height: 10),
                Container(
                  width: SizeConfig.screenWidth,
                  padding: EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: [
                      BoxShadow(
                        offset: Offset(0, 1),
                        color: Colors.black.withValues(alpha: 0.15),
                        spreadRadius: 0,
                        blurRadius: 10,
                      ),
                    ],
                  ),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                _selectCampFromDate(context);
                              },
                              controller: TextEditingController(text: fromDate),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'From Date *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                icCalendarMonth,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                          const SizedBox(width: 10),
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                _selectCampToDate(context);
                              },
                              controller: TextEditingController(text: toDate),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'To Date *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                icCalendarMonth,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                        ],
                      ),

                      Visibility(
                        visible: isShowFilter && _isGroup3,
                        child: AppTextField(
                          onTap: () {
                            isShowSubOrg = true;
                            selectedDivision = null;
                            selectedDistrict = null;
                            selectedLabVal = null;
                            getSubOrganization();
                          },
                          controller: TextEditingController(
                            text: selectedSubOrganization?.subOrgName ?? "",
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Sub Organization',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: 14.sp,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icMapPin,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6.w),
                          suffixIcon: Icon(Icons.keyboard_arrow_down),
                        ).paddingOnly(top: 8.h),
                      ),
                      Visibility(
                        visible: isShowFilter && _isGroup3,

                        child: Row(
                          children: [
                            Expanded(
                              child: AppTextField(
                                onTap: () {
                                  getBindDivision();
                                },
                                controller: TextEditingController(
                                  text: selectedDivision?.dIVNAME ?? "",
                                ),
                                readOnly: true,
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Division',
                                    style: TextStyle(
                                      color: kLabelTextColor,
                                      fontSize: 14.sp,
                                      fontFamily: FontConstants.interFonts,
                                    ),
                                  ),
                                ),
                                prefixIcon: Image.asset(
                                  icMapPin,
                                  color: kPrimaryColor,
                                ).paddingOnly(left: 6.w),
                                suffixIcon: Icon(Icons.keyboard_arrow_down),
                              ),
                            ),
                            SizedBox(width: 10.w),
                            Expanded(
                              child: AppTextField(
                                onTap: () {
                                  selectedLabVal = null;

                                  getBindDistrict();
                                },
                                controller: TextEditingController(
                                  text: selectedDistrict?.dISTNAME ?? "",
                                ),
                                readOnly: true,
                                label: RichText(
                                  text: TextSpan(
                                    text: 'District',
                                    style: TextStyle(
                                      color: kLabelTextColor,
                                      fontSize: 14.sp,
                                      fontFamily: FontConstants.interFonts,
                                    ),
                                  ),
                                ),
                                prefixIcon: Image.asset(
                                  icMapPin,
                                  color: kPrimaryColor,
                                ).paddingOnly(left: 6.w),
                                suffixIcon: Icon(Icons.keyboard_arrow_down),
                              ),
                            ),
                          ],
                        ).paddingOnly(top: 8.h),
                      ),

                      Visibility(
                        visible: isShowFilter && !_isGroup1 && !_isGroup3,

                        child: AppTextField(
                          onTap: () {
                            getLandingLabAPI();
                          },
                          controller: TextEditingController(
                            text: selectedLabVal?.labName ?? "",
                          ),
                          readOnly: true,
                          label: RichText(
                            text: TextSpan(
                              text: 'Lab',
                              style: TextStyle(
                                color: kLabelTextColor,
                                fontSize: 14.sp,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ),
                          ),
                          prefixIcon: Image.asset(
                            icMicroscope,
                            color: kPrimaryColor,
                          ).paddingOnly(left: 6.w),
                          suffixIcon: Icon(Icons.keyboard_arrow_down),
                        ).paddingOnly(top: 8.h, bottom: 4.h),
                      ),
                    ],
                  ),
                ).paddingOnly(left: 12, right: 12),
                const SizedBox(height: 10),
                CommonText(
                  text:
                      "Note : Click on rejected beneficiary count for rejected beneficiary list",
                  fontSize: 14.sp,
                  fontWeight: FontWeight.normal,
                  textColor: noteRedColor,
                  textAlign: TextAlign.start,
                ).paddingOnly(left: 16.w, bottom: 12.h),
                IntrinsicHeight(
                  child: Container(
                    width: SizeConfig.screenWidth,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                      boxShadow: [
                        BoxShadow(
                          offset: Offset(0, 1),
                          color: Colors.black.withValues(alpha: 0.15),
                          blurRadius: 10,
                        ),
                      ],
                    ),
                    child:
                        isLoading
                            ? CommonSkeletonScreeningDetailsTable(
                              radius: 10,
                              rowCount: 10,
                              rowHeight: 30,
                              headerHeight: 30,
                            ).paddingOnly(top: 10)
                            : Column(
                              children: [
                                Container(
                                  height: 36,
                                  decoration: BoxDecoration(
                                    color: kPrimaryColor,
                                    borderRadius: BorderRadius.only(
                                      topLeft: Radius.circular(5),
                                      topRight: Radius.circular(5),
                                    ),
                                  ),
                                  child: Center(
                                    child: Text(
                                      "Re-Screening",
                                      style: TextStyle(
                                        color: kWhiteColor,
                                        fontFamily: "Inter",
                                        fontWeight: FontWeight.w500,
                                        fontSize: responsiveFont(14),
                                      ),
                                    ),
                                  ),
                                ),

                                dashboardRow(
                                  title: "Rejected Beneficiaries",
                                  value:
                                      rejectedBeneficiaryCount != null
                                          ? "$rejectedBeneficiaryCount"
                                          : "0",
                                  valueColor: campOpenColor,
                                  onTap: () {
                                    if (_isAdminDesignation) {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder:
                                              (_) =>
                                                  RejectedBeneficiaryTrackingScreen(
                                                    fromDate: fromDate,
                                                    toDate: toDate,
                                                    oganizationId:
                                                        selectedSubOrganization
                                                            ?.subOrgId
                                                            .toString() ??
                                                        "0",
                                                    divisionId:
                                                        selectedDivision?.dIVID
                                                            .toString() ??
                                                        "0",
                                                    dISTLGDCODE:
                                                        selectedDistrict
                                                            ?.dISTLGDCODE
                                                            .toString() ??
                                                        "0",
                                                    tALLGDCODE: "0",
                                                    landingLabId:
                                                        selectedLabVal?.labCode
                                                            .toString() ??
                                                        "0",
                                                    campType:
                                                        regularCamp ? "1" : "3",
                                                    searchFilterId: "0",
                                                  ),
                                        ),
                                      );
                                    } else {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder:
                                              (_) =>
                                                  RejectedBeneficiaryListScreen(
                                                    fromDate: fromDate,
                                                    toDate: toDate,
                                                    oganizationId:
                                                        selectedSubOrganization
                                                            ?.subOrgId
                                                            .toString() ??
                                                        "0",
                                                    divisionId:
                                                        selectedDivision?.dIVID
                                                            .toString() ??
                                                        "0",
                                                    dISTLGDCODE:
                                                        selectedDistrict
                                                            ?.dISTLGDCODE
                                                            .toString() ??
                                                        "0",
                                                    tALLGDCODE: "0",
                                                    landingLabId:
                                                        selectedLabVal?.labCode
                                                            .toString() ??
                                                        "0",
                                                    campType:
                                                        regularCamp ? "1" : "3",
                                                    searchFilterId: "0",
                                                  ),
                                        ),
                                      );
                                    }
                                  },
                                ),

                                dashboardRow(
                                  title: "Screening Confirmation Pending",
                                  value:
                                      screeningConfimPendingCount != null
                                          ? screeningConfimPendingCount
                                              .toString()
                                          : "0",
                                ),

                                dashboardRow(
                                  title: "Assignment Pending",
                                  value:
                                      assignmentPendingCount != null
                                          ? assignmentPendingCount.toString()
                                          : "0",
                                ),

                                dashboardRow(
                                  title: "Interested in Screening",
                                  value:
                                      interestedInScreeningCount != null
                                          ? interestedInScreeningCount
                                              .toString()
                                          : '0',
                                ),

                                dashboardRow(
                                  title: "Not Interested in Screening",
                                  value:
                                      notinterestedInScreeningCount != null
                                          ? notinterestedInScreeningCount
                                              .toString()
                                          : "0",
                                ),

                                dashboardRow(
                                  title: "Wrong Number",
                                  value:
                                      wrongNumberCount != null
                                          ? wrongNumberCount.toString()
                                          : '0',
                                ),

                                dashboardRow(
                                  title: "Not Available for Screening",
                                  value:
                                      notAvailableForScreeningCount != null
                                          ? notAvailableForScreeningCount
                                              .toString()
                                          : "0",
                                ),

                                dashboardRow(
                                  title: "Sample Collected",
                                  value:
                                      sampleCollectedCount != null
                                          ? sampleCollectedCount.toString()
                                          : "0",
                                  valueColor: approvedBeneficiariesColor,
                                ),

                                dashboardRow(
                                  title: "Re-Screening Pending as Card Expired",
                                  value:
                                      reScreeningPendingCount != null
                                          ? reScreeningPendingCount.toString()
                                          : "0",
                                ),

                                dashboardRow(
                                  title: "Denied for Screening",
                                  value:
                                      deniedScreeningCount != null
                                          ? deniedScreeningCount.toString()
                                          : "0",
                                  valueColor: campOpenColor,
                                  isLast: true,
                                ),
                              ],
                            ),
                  ),
                ).paddingOnly(left: 12, right: 12),
                const SizedBox(height: 16),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget dashboardRow({
    required String title,
    required String value,
    Color valueColor = Colors.black,
    VoidCallback? onTap,
    bool isLast = false,
  }) {
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: kWhiteColor,
          border: Border(
            left: BorderSide(color: kTextFieldBorder),
            right: BorderSide(color: kTextFieldBorder),
            top: BorderSide(color: kTextFieldBorder),
            bottom:
                isLast ? BorderSide(color: kTextFieldBorder) : BorderSide.none,
          ),
          borderRadius:
              isLast
                  ? const BorderRadius.only(
                    bottomLeft: Radius.circular(5),
                    bottomRight: Radius.circular(5),
                  )
                  : BorderRadius.zero,
        ),
        padding: const EdgeInsets.symmetric(vertical: 10),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                decoration: BoxDecoration(
                  border: Border(right: BorderSide(color: kTextFieldBorder)),
                ),
                child: Text(
                  title,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  softWrap: true,
                  style: TextStyle(
                    color: kBlackColor,
                    fontFamily: "Inter",
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(13),
                  ),
                ),
              ),
            ),
            SizedBox(
              width: 100,
              child: Center(
                child: Text(
                  value,
                  style: TextStyle(
                    color: kBlackColor,
                    fontFamily: "Inter",
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(13),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void getBindDivision() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);
    apiManager.getBindDivision(params, apiBindDivisionCallBack);
  }

  void apiBindDivisionCallBack(
    BindDivisionResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Division",
        response?.output ?? [],
        DropDownTypeMenu.BindDivision,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> callAPI() async {
    isLoading = true;
    setState(() {});
    bool didCall = false;
    if ([86, 64, 35, 129, 146].contains(dESGID)) {
      didCall = true;
      getCountForPageloadForTeam();
    } else if ([
      92,
      29,
      160,
      104,
      162,
      78,
      77,
      128,
      30,
      108,
      84,
      139,
      136,
      51,
      170,
      171,
      182,
      183,
      26,
    ].contains(dESGID)) {
      // if (searchFilterId == "0") {
      //   await getCountForPageload();
      // } else {
      didCall = true;
      await getCountForPageload();
      // }
    }
    if (!didCall) {
      isLoading = false;
      setState(() {});
    }
  }

  void getCountForPageloadForTeam() {
    Map<String, String> param = {
      "Fromdate": fromDate,
      "Todate": toDate,
      "Arid": "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "1",
      "CampType": regularCamp ? "1" : "3",
    };

    apiManager.getCountForPageloadForTeamAPI(
      param,
      apiCountForPageloadForTeamCallBack,
    );
  }

  void getBindDistrict() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
      "DIVID": selectedDivision?.dIVID.toString() ?? "0",
      "DISTLGDCODE": "0",
    };
    print(params);
    apiManager.getBindDistrictAPI(params, apiBindDistrictCallBack);
  }

  void apiBindDistrictCallBack(
    BindDistrictResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "District",
        response?.output ?? [],
        DropDownTypeMenu.BindDistrict,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getSubOrganization() {
    if (dESGID == 71) {
      return;
    }

    ToastManager.showLoader();
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);
    apiManager.getSubOrganizationAPI(params, apiSubOrganizationCallBack);
  }

  void apiSubOrganizationCallBack(
    SubOrganizationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      if (isShowSubOrg) {
        _showDropDownBottomSheet(
          "Sub Organization",
          response?.output ?? [],
          DropDownTypeMenu.SubOrganization,
        );
      } else {
        List<SubOrganizationOutput> output = response?.output ?? [];

        if (output.isNotEmpty) {
          if (output.length == 1) {
            selectedSubOrganization = output.first;
          } else {
            selectedSubOrganization = SubOrganizationOutput(
              subOrgId: 0,
              subOrgName: "ALL",
            );
          }
        }
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getLandingLabAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "UserID": empCode.toString(),
    };
    apiManager.getLabforD2DCampCoordinatorAPI(data, apiLabForD2DCallBack);
  }

  void apiLabForD2DCallBack(
    LabDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      final List<LandingLabCampCreationOutput> labs =
          (response?.output ?? [])
              .map(
                (e) => LandingLabCampCreationOutput(
                  labCode: e.labCode,
                  labName: e.labName,
                ),
              )
              .toList();
      _showDropDownBottomSheet("Lab", labs, DropDownTypeMenu.BindLab);
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCountForPageloadForTeamCallBack(
    RecollectionBeneficiaryToTeamResponse? response,
    String errorMessage,
    bool success,
  ) async {
    // ToastManager.hideLoader();

    if (success) {
      showDateInUI(response?.output ?? []);
    } else {
      resetUI();
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  Future<void> getCountForPageload() async {
    Map<String, String> param = {
      "Fromdate": fromDate,
      "Todate": toDate,
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
      "DIVID": selectedDivision?.dIVID.toString() ?? "0",
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
      "TALLGDCODE": "0",
      "Labcode": selectedLabVal?.labCode.toString() ?? "0",
      "Arid": "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "1",
      "DesgID": "$dESGID",
      "CampType": regularCamp == true ? "1" : "3",
    };

    await apiManager.getCountForPageloadAPI(
      param,
      apiCountForPageloadForTeamCallBack,
    );
  }

  Future<void> getCount() async {
    Map<String, String> param = {
      "Fromdate": fromDate,
      "Todate": toDate,
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
      "DIVID": selectedDivision?.dIVID.toString() ?? "0",
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
      "TALLGDCODE": "0",
      "Labcode": selectedLabVal?.labCode.toString() ?? "0",
      "Arid": "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "1",
      "CampType": regularCamp == true ? "1" : "3",
    };

    await apiManager.getCountAPI(param, apiCountForPageloadForTeamCallBack);
  }

  void showDateInUI(List<RecollectionBeneficiaryToTeamOutput> output) {
    resetUI();

    if (output.isNotEmpty) {
      for (RecollectionBeneficiaryToTeamOutput benefiObj in output) {
        if (benefiObj.sequenceNo == 0) {
          rejectedBeneficiaryCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 1) {
          screeningConfimPendingCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 2) {
          assignmentPendingCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 3) {
          interestedInScreeningCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 4) {
          notinterestedInScreeningCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 5) {
          wrongNumberCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 6) {
          notAvailableForScreeningCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 7) {
          sampleCollectedCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 8) {
          reScreeningPendingCount = benefiObj.patientCount ?? 0;
        } else if (benefiObj.sequenceNo == 9) {
          deniedScreeningCount = benefiObj.patientCount ?? 0;
        }
      }
    } else {
      resetUI();
    }
  }

  resetUI() {
    rejectedBeneficiaryCount = 0;
    screeningConfimPendingCount = 0;
    assignmentPendingCount = 0;
    interestedInScreeningCount = 0;
    notinterestedInScreeningCount = 0;
    wrongNumberCount = 0;
    notAvailableForScreeningCount = 0;
    sampleCollectedCount = 0;
    reScreeningPendingCount = 0;
    deniedScreeningCount = 0;
    setState(() {});
  }

  Future<void> _selectCampFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        fromDate = FormatterManager.formatDateToString(picked);
        toDate = "";
      });
    }
  }

  Future<void> _selectCampToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      setState(() {
        toDate = FormatterManager.formatDateToString(picked);
      });
      callAPI();
    }
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33 + MediaQuery.of(context).viewPadding.bottom,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) async {
              if (dropDownType == DropDownTypeMenu.SubOrganization) {
                selectedSubOrganization = p0;

                // selectedDivision = BindDivisionOutput(
                //   dIVID: 0,
                //   dIVNAME: "ALL",
                //   subOrgId: 0,
                //   subOrgName: "ALL",
                // );
              } else if (dropDownType == DropDownTypeMenu.BindDivision) {
                selectedDivision = p0;
                // selectedDivision = BindDivisionOutput(
                //   dIVID: 0,
                //   dIVNAME: "ALL",
                //   subOrgId: 0,
                //   subOrgName: "ALL",
                // );
                // selectedDistrict = BindDistrictOutput(
                //   dIVID: 0,
                //   dIVNAME: "ALL",
                //   subOrgId: 0,
                //   subOrgName: "ALL",
                //   dISTLGDCODE: 0,
                //   dISTNAME: "ALL",
                // );
              } else if (dropDownType == DropDownTypeMenu.BindDistrict) {
                selectedDistrict = p0;
                // selectedDistrict = BindDistrictOutput(
                //   dIVID: 0,
                //   dIVNAME: "ALL",
                //   subOrgId: 0,
                //   subOrgName: "ALL",
                //   dISTLGDCODE: 0,
                //   dISTNAME: "ALL",
                // );
              } else if (dropDownType == DropDownTypeMenu.BindLab) {
                selectedLabVal = p0;
                // selectedLabVal = LandingLabCampCreationOutput(labCode: 0, labName: "ALL");
              }
              await callAPI();
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }
}
