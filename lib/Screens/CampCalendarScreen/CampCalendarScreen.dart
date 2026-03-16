// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
import '../../Modules/Json_Class/CampCountWithDayResponse/CampCountWithDayResponse.dart';
import '../../Modules/Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import '../../Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/fonts.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/utilities/WidgetPaddingX.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/CalenderScreen/CalenderScreen.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../AdminDashboard/Model/HomeAndHubProcessingModel.dart';
import '../AdminDashboard/Model/HomeLabPendingCountTableModel.dart';
import '../AdminDashboard/Screens/PendingCountScreen/PendingCountScreen.dart';
import 'CampCalendarCampListScreen/CampCalendarCampListScreen.dart';
import 'CampCalendarCountView/CampCalendarCountView.dart';

class CampCalendarScreen extends StatefulWidget {
  const CampCalendarScreen({super.key});

  @override
  State<CampCalendarScreen> createState() => _CampCalendarScreenState();
}

class _CampCalendarScreenState extends State<CampCalendarScreen> {
  final formKey = GlobalKey<FormState>();
  APIManager apiManager = APIManager();

  int dESGID = 0;
  int empCode = 0;
  bool isShowSubOrg = false;

  int apiCallCount = 0;
  int _requestId = 0;
  int year = 0;
  int month = 0;
  int day = 0;
  String statusDashboardTitle = "";

  Map<DateTime, Map<String, dynamic>> attendanceMap = {};

  SubOrganizationOutput? selectedSubOrganization;
  BindDivisionOutput? selectedDivision;
  BindDistrictOutput? selectedDistrict;
  CampTypeAndCatagoryOutput? selectedCampType;

  // HubHomelabDashboardOutput? hubHomelabDashboard;
  HomeAndHubProcessingModel? homeAndHubProcessingModel;
  HomeLabPendingCountTableModel? homeLabPendingCountTableModel;

  // DispatchGroup dispatchGroup = DispatchGroup();

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    statusDashboardTitle = "Total Camp Status Dashboard";

    DateTime dateTime = DateTime.now();

    year = dateTime.year;
    month = dateTime.month;

    selectedDivision = BindDivisionOutput(
      dIVID: 0,
      dIVNAME: "ALL",
      subOrgId: 0,
      subOrgName: "ALL",
    );

    selectedDistrict = BindDistrictOutput(
      dIVID: 0,
      dIVNAME: "ALL",
      subOrgId: 0,
      subOrgName: "ALL",
      dISTLGDCODE: 0,
      dISTNAME: "ALL",
    );

    selectedCampType = CampTypeAndCatagoryOutput(
      cAMPTYPE: 0,
      campTypeDescription: "All Camp",
      catagoryID: 0,
      catagoryType: "All Camp",
    );
    getSubOrganization();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Camp Calendar',
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

          child: Container(
            color: Colors.white,
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: SingleChildScrollView(
              child: Container(
                color: Colors.transparent,
                width: SizeConfig.screenWidth,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      width: MediaQuery.of(context).size.width,
                      decoration: const BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                      padding: EdgeInsets.symmetric(
                        vertical: 10.h,
                        horizontal: 10.w,
                      ),
                      child: Column(
                        children: [
                          AppTextField(
                            onTap: () {
                              isShowSubOrg = true;
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
                            suffixIcon: Icon(Icons.arrow_drop_down_outlined),
                            prefixIcon: Image.asset(
                              icMapPin,
                              color: kPrimaryColor,
                            ).paddingOnly(left: 6.w),
                          ),

                          SizedBox(height: 8.h),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
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
                                  suffixIcon: Icon(
                                    Icons.arrow_drop_down_outlined,
                                  ),
                                  prefixIcon: Image.asset(
                                    icMapPin,
                                    color: kPrimaryColor,
                                  ).paddingOnly(left: 6.w),
                                ),
                              ),

                              SizedBox(width: 12.w),
                              Expanded(
                                child: AppTextField(
                                  onTap: () {
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
                                  suffixIcon: Icon(
                                    Icons.arrow_drop_down_outlined,
                                  ),
                                  prefixIcon: Image.asset(
                                    icMapPin,
                                    color: kPrimaryColor,
                                  ).paddingOnly(left: 6.w),
                                ),
                              ),
                            ],
                          ).paddingSymmetric(horizontal: 4.w),
                          SizedBox(height: 8.h),
                          AppTextField(
                            onTap: () {
                              getCampTypeAndCatagory();
                            },
                            controller: TextEditingController(
                              text: selectedCampType?.campTypeDescription ?? "",
                            ),
                            readOnly: true,
                            label: RichText(
                              text: TextSpan(
                                text: 'Camp Type',
                                style: TextStyle(
                                  color: kLabelTextColor,
                                  fontSize: 14.sp,
                                  fontFamily: FontConstants.interFonts,
                                ),
                              ),
                            ),
                            suffixIcon: Icon(Icons.arrow_drop_down_outlined),
                            prefixIcon: Image.asset(
                              icnTent,
                              color: kPrimaryColor,
                            ).paddingOnly(left: 6.w),
                          ),
                        ],
                      ),
                    ),
                    CalenderScreen(
                      attendanceMap: attendanceMap,
                      didChangeDate: (p0) {
                        year = p0.year;
                        month = p0.month;
                        groupAPICall();
                      },
                      onDateSelectedTap: (date) {
                        int months = date.month;
                        String monthsString = "0";
                        if (months <= 9) {
                          monthsString = "0$months";
                        } else {
                          monthsString = "$months";
                        }

                        int days = date.day;
                        String dayString = "0";
                        if (days <= 9) {
                          dayString = "0$days";
                        } else {
                          dayString = "$days";
                        }

                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder:
                                (context) => CampCalendarCampListScreen(
                                  year: date.year.toString(),
                                  month: date.month.toString(),
                                  day: date.day.toString(),
                                  isShowCampType: false,
                                  selectedCampType: selectedCampType,
                                  isFromCampCalener: true,
                                  subOrgId:
                                      selectedSubOrganization?.subOrgId ?? 0,
                                  dIVID: selectedDivision?.dIVID ?? 0,
                                  distCode: selectedDistrict?.dISTLGDCODE ?? 0,
                                  calanderSelectedDate:
                                      "${date.year}-$monthsString-$dayString",
                                  isTodayCount: false,
                                ),
                          ),
                        );
                      },
                    ).paddingSymmetric(horizontal: 18.w),
                    SizedBox(height: 8.h),
                    Text(
                      statusDashboardTitle,
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w700,
                        fontSize: 14.sp,
                      ),
                    ).paddingSymmetric(horizontal: 18.w),
                    SizedBox(height: 8.h),
                    Container(
                      color: Colors.white,
                      child: Column(
                        children: [
                          CampCalendarCountView(
                            titleString: "Total Month's Beneficiary Count",
                            countValue:
                                homeAndHubProcessingModel
                                    ?.output
                                    .first
                                    .totalMonthsBeneficiaryCount
                                    .toString() ??
                                'NA',
                            showTop: true,
                            showBottom: false,
                          ),
                          CampCalendarCountView(
                            titleString: "Today's Beneficiary Count",
                            countValue:
                                homeAndHubProcessingModel
                                    ?.output
                                    .first
                                    .todaysBeneficiaryCount
                                    .toString() ??
                                'NA',
                            showTop: false,
                            showBottom: false,
                          ),
                          CampCalendarCountView(
                            titleString: "Home Lab Processed Count",
                            countValue:
                                homeAndHubProcessingModel
                                    ?.output
                                    .first
                                    .homeLabProcessedCount
                                    .toString() ??
                                'NA',
                            showTop: false,
                            showBottom: false,
                          ),
                          CampCalendarCountView(
                            titleString: "Hub Lab Processed Count",
                            countValue:
                                homeAndHubProcessingModel
                                    ?.output
                                    .first
                                    .hubLabProcessedCount
                                    .toString() ??
                                'NA',
                            showTop: false,
                            showBottom: false,
                          ),
                          // CampCalendarCountView(
                          //   titleString: "Home Lab",
                          //   countValue:
                          //       hubHomelabDashboard?.homeLab?.toString() ??
                          //       'NA',
                          //   showTop: false,
                          //   showBottom: false,
                          // ),
                          // CampCalendarCountView(
                          //   titleString: "Hub Lab",
                          //   countValue:
                          //       hubHomelabDashboard?.hubLab?.toString() ??
                          //       'NA',
                          //   showTop: false,
                          //   showBottom: false,
                          // ),
                          GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => PendingCountScreen(
                                        monthId: '$month',
                                        year: '$year',
                                        subOrgId:
                                            '${selectedSubOrganization?.subOrgId ?? 0}',
                                        dIVID:
                                            '${selectedDivision?.dIVID ?? 0}',
                                        distcode:
                                            '${selectedDistrict?.dISTLGDCODE ?? 0}',
                                        userId: '$empCode',
                                        dESGID: '$dESGID',
                                        campType:
                                            '${selectedCampType?.cAMPTYPE ?? 0}',
                                      ),
                                ),
                              );
                            },
                            child: CampCalendarCountView(
                              titleString: "Home Lab Pending Count",
                              countValue:
                                  homeAndHubProcessingModel
                                      ?.output
                                      .first
                                      .homeLabProcessedPendingCount
                                      .toString() ??
                                  'NA',
                              showTop: false,
                              textCountColor: Colors.blue,
                              showBottom: false,
                            ),
                          ),
                          GestureDetector(
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) => PendingCountScreen(
                                        monthId: '$month',
                                        year: '$year',
                                        subOrgId:
                                            '${selectedSubOrganization?.subOrgId ?? 0}',
                                        dIVID:
                                            '${selectedDivision?.dIVID ?? 0}',
                                        distcode:
                                            '${selectedDistrict?.dISTLGDCODE ?? 0}',
                                        userId: '$empCode',
                                        dESGID: '$dESGID',
                                        campType:
                                            '${selectedCampType?.cAMPTYPE ?? 0}',
                                      ),
                                ),
                              );
                            },
                            child: CampCalendarCountView(
                              titleString: "Hub Lab Pending Count",
                              countValue:
                                  homeAndHubProcessingModel
                                      ?.output
                                      .first
                                      .hubLabProcessedPendingCount
                                      .toString() ??
                                  'NA',
                              showTop: false,
                              textCountColor: Colors.blue,
                              showBottom: true,
                            ),
                          ),
                          Align(
                            alignment: Alignment.centerRight,
                            child: CommonText(
                              text: getFormattedDateTime(),
                              fontSize: 12,
                              fontWeight: FontWeight.normal,
                              textColor: kTextColor,
                              textAlign: TextAlign.end,
                            ),
                          ).paddingSymmetric(vertical: 2),
                        ],
                      ),
                    ).paddingSymmetric(horizontal: 18.w),
                    SizedBox(height: 12.h),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
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
          height: MediaQuery.of(context).size.width * 1.33,
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
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.SubOrganization) {
                selectedSubOrganization = p0;
                selectedDivision = BindDivisionOutput(
                  dIVID: 0,
                  dIVNAME: "ALL",
                  subOrgId: 0,
                  subOrgName: "ALL",
                );

                selectedDistrict = BindDistrictOutput(
                  dIVID: 0,
                  dIVNAME: "ALL",
                  subOrgId: 0,
                  subOrgName: "ALL",
                  dISTLGDCODE: 0,
                  dISTNAME: "ALL",
                );

                selectedCampType = CampTypeAndCatagoryOutput(
                  cAMPTYPE: 0,
                  campTypeDescription: "All Camp",
                  catagoryID: 0,
                  catagoryType: "All Camp",
                );
              } else if (dropDownType == DropDownTypeMenu.BindDivision) {
                selectedDivision = p0;

                selectedDistrict = BindDistrictOutput(
                  dIVID: 0,
                  dIVNAME: "ALL",
                  subOrgId: 0,
                  subOrgName: "ALL",
                  dISTLGDCODE: 0,
                  dISTNAME: "ALL",
                );

                selectedCampType = CampTypeAndCatagoryOutput(
                  cAMPTYPE: 0,
                  campTypeDescription: "All Camp",
                  catagoryID: 0,
                  catagoryType: "All Camp",
                );
              } else if (dropDownType == DropDownTypeMenu.BindDistrict) {
                selectedDistrict = p0;

                selectedCampType = CampTypeAndCatagoryOutput(
                  cAMPTYPE: 0,
                  campTypeDescription: "All Camp",
                  catagoryID: 0,
                  catagoryType: "All Camp",
                );
              } else if (dropDownType == DropDownTypeMenu.CampTypeAndCatagory) {
                selectedCampType = p0;
              }
              groupAPICall();
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  Future<void> getSubOrganization() async {
    if (dESGID == 71) {
      return;
    }

    ToastManager.showLoader();
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);
    await apiManager.getSubOrganizationAPI(params, apiSubOrganizationCallBack);
  }

  void apiSubOrganizationCallBack(
    SubOrganizationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      if (isShowSubOrg) {
        ToastManager.hideLoader();
        _showDropDownBottomSheet(
          "Sub Organization",
          response?.output ?? [],
          DropDownTypeMenu.SubOrganization,
        );
      } else {
        List<SubOrganizationOutput> output = response?.output ?? [];

        if (output.isNotEmpty) {
          ToastManager.hideLoader();
          selectedSubOrganization = output.first;
          print(
            "🏢 SubOrg selected: id=${output.first.subOrgId}, name=${output.first.subOrgName}",
          );
        }
        await groupAPICall();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getBindDivision() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
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

  void getCampTypeAndCatagory() {
    ToastManager.showLoader();
    apiManager.getCampTypeAndCatagoryAPI(apiCampTypeAndCatagoryCallBack);
  }

  void apiCampTypeAndCatagoryCallBack(
    CampTypeAndCatagoryResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      final List<CampTypeAndCatagoryOutput> campTypes =
          List<CampTypeAndCatagoryOutput>.from(response?.output ?? []);
      final bool hasAllCamp = campTypes.any(
        (e) =>
            (e.cAMPTYPE ?? -1) == 0 ||
            (e.campTypeDescription ?? "").toLowerCase() == "all camp",
      );
      if (!hasAllCamp) {
        campTypes.insert(
          0,
          CampTypeAndCatagoryOutput(
            cAMPTYPE: 0,
            campTypeDescription: "All Camp",
            catagoryID: 0,
            catagoryType: "All Camp",
          ),
        );
      }
      _showDropDownBottomSheet(
        "Camp Type",
        campTypes,
        DropDownTypeMenu.CampTypeAndCatagory,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> groupAPICall() async {
    ToastManager.showLoader();
    final requestId = ++_requestId;

    await Future.wait([
      getMonthlySurveySiteRequestForOSNew(requestId),
      getHomeAndHubProcessedCount(requestId),
      // getHubAndHomelabDashboard(requestId),
      // getHomeLabPendingTableData(),
    ]);
    ToastManager.hideLoader();
  }

  Future<void> getMonthlySurveySiteRequestForOSNew(int requestId) async {
    Map<String, String> data = {
      "MonthId": month.toString().padLeft(2, '0'),
      "Year": year.toString(),
      "Distcode": selectedDistrict?.dISTLGDCODE.toString() ?? "",
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
      "DIVID": selectedDivision?.dIVID.toString() ?? "",
      "UserId": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print("🗓️ CALENDAR API params: $data");

    await apiManager.getCampCountWithDayAndMonthWiseWithCampTypeOrgAPI(data, (
      response,
      errorMessage,
      success,
    ) {
      if (requestId != _requestId) return;
      apiCampCountWithDayAndMonthWiseWithCampTypeOrCallBack(
        response,
        errorMessage,
        success,
      );
    });
  }

  void apiCampCountWithDayAndMonthWiseWithCampTypeOrCallBack(
    CampCountWithDayResponse? response,
    String errorMessage,
    bool success,
  ) async {
    // dispatchGroup.leave();
    attendanceMap = {};

    if (success) {
      if (selectedCampType?.cAMPTYPE == 0) {
        statusDashboardTitle = "Total Camp Status Dashboard";
      } else if (selectedCampType?.cAMPTYPE == 1) {
        statusDashboardTitle = "Normal Camp Status Dashboard";
      } else {
        statusDashboardTitle = "D2D Camp Status Dashboard";
      }
      List<CampCountWithDayOutput> tempList = response?.output ?? [];
      for (CampCountWithDayOutput attendace in tempList) {
        final strDate = attendace.campDate ?? '';
        final now = DateTime.now();
        Color color = Colors.white;
        try {
          final date = FormatterManager.formatStringToDate(strDate);

          if (date.isBefore(DateTime(now.year, now.month, now.day))) {
            color = const Color(0xFF229954).withValues(alpha: 0.4);
          } else if (date.isAfter(DateTime(now.year, now.month, now.day))) {
            color = const Color(0xFF4CB1FB);
          } else {
            color = const Color(0xFF229954).withValues(alpha: 0.4);
          }
        } catch (e) {
          // Handle invalid or null date
          print('Invalid date: $e');
        }
        Map<String, dynamic> dict = {
          "color": color,
          "value": attendace.campCount ?? 0,
        };

        attendanceMap[DateTime(
              attendace.year ?? 0,
              attendace.month ?? 0,
              attendace.day ?? 0,
            )] =
            dict;
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  // Future<void> getHubAndHomelabDashboard(int requestId) async {
  //   Map<String, String> data = {
  //     "MonthID": month.toString().padLeft(2, '0'),
  //     "Year": year.toString(),
  //     "DIVID": selectedDivision?.dIVID.toString() ?? "0",
  //     "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
  //     "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
  //     "USERID": empCode.toString(),
  //     "DesgId": dESGID.toString(),
  //   };
  //   print("🏥 HUB/HOMELAB API params: $data");
  //
  //   await apiManager.getHubAndHomelabDashboardAPI(
  //     data,
  //     (response, errorMessage, success) {
  //       if (requestId != _requestId) return;
  //       apiHubHomelabDashboardCallBack(response, errorMessage, success);
  //     },
  //   );
  // }

  // void apiHubHomelabDashboardCallBack(
  //   HubHomelabDashboardResponse? response,
  //   String errorMessage,
  //   bool success,
  // ) {
  //   if (success) {
  //     hubHomelabDashboard = response?.output?.first;
  //   } else {
  //     if (errorMessage.isNotEmpty) {
  //       ToastManager.toast(errorMessage);
  //     }
  //   }
  //   setState(() {});
  // }

  String getFormattedDateTime() {
    final now = DateTime.now();
    final formatter = DateFormat('dd/MM/yyyy HH:mm');
    return 'Data as of ${formatter.format(now)}';
  }

  Future<void> getHomeAndHubProcessedCount(int requestId) async {
    Map<String, String> data = {
      "MonthId": month.toString().padLeft(2, '0'),
      "Year": year.toString(),
      "Distcode": selectedDistrict?.dISTLGDCODE.toString() ?? "",
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
      "DIVID": selectedDivision?.dIVID.toString() ?? "",
      "UserId": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print("📊 TABLE API params: $data");

    await apiManager.getHomeAndHubProcessedCount(data, (
      response,
      errorMessage,
      success,
    ) {
      if (requestId != _requestId) return;
      apiGetHomeHubProcessed(response, errorMessage, success);
    });
  }

  Future<void> apiGetHomeHubProcessed(
    HomeAndHubProcessingModel? response,
    String errorMessage,
    bool success,
  ) async {
    // dispatchGroup.leave();
    if (success) {
      homeAndHubProcessingModel = response;
    } else {
      if (errorMessage.isNotEmpty) {
        homeAndHubProcessingModel = null;
        setState(() {});
        ToastManager.toast(errorMessage);
      }
    }
    setState(() {});
  }
}
