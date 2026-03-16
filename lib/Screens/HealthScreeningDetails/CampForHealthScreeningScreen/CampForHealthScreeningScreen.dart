// ignore_for_file: must_be_immutable, avoid_print, file_names, strict_top_level_inference

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../Modules/Json_Class/UserCampMappingAndAttendanceDataResponse/UserCampMappingAndAttendanceDataResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../HealthScreeningDetailsScreen/HealthScreeningDetailsScreen.dart';
import 'CampForHealthScreeningRow/CampForHealthScreeningRow.dart';

class CampForHealthScreeningScreen extends StatefulWidget {
  CampForHealthScreeningScreen({super.key, required this.testID});

  int testID = 0;

  @override
  State<CampForHealthScreeningScreen> createState() =>
      _CampForHealthScreeningScreenState();
}

class _CampForHealthScreeningScreenState
    extends State<CampForHealthScreeningScreen> {
  TextEditingController searchController = TextEditingController();
  APIManager apiManager = APIManager();
  String selectedCampDate = "";
  int subOrgId = 0;
  int empCode = 0;
  int dESGID = 0;

  int dISTLGDCODE = 0;
  String dISTNAME = "";
  int campId = 0;
  int campType = 0;

  List<ResourceReMappingCampOutput> campList = [];
  List<ResourceReMappingCampOutput> searchCampList = [];
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    subOrgId = DataProvider().getParsedUserData()?.output?.first.subOrgId ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    getApprovedCampListDetailsForApp();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Select Camp",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            AppTextField(
              controller: TextEditingController(text: selectedCampDate),
              readOnly: true,
              onTap: () {
                _selectCampDate(context);
              },
              hint: 'Camp Date*',
              label: CommonText(
                text: 'Camp Date*',
                fontSize: 12.sp,
                fontWeight: FontWeight.normal,
                textColor: kBlackColor,
                textAlign: TextAlign.start,
              ),
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                fontFamily: FontConstants.interFonts,
              ),
              fieldRadius: 10,
              prefixIcon: SizedBox(
                height: 20.h,
                width: 20.w,
                child: Center(
                  child: Image.asset(
                    icInitiatedBy,
                    height: 24.h,
                    width: 24.w,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
            ),

            // AppDateTextfield(
            //   icon: icCalendarMonth,
            //   titleHeaderString: "Camp Date*",
            //   valueString: selectedCampDate,
            //   valueColor: Colors.black,
            //   onTap: () {
            //     _selectCampDate(context);
            //   },
            // ),
            const SizedBox(height: 8),

            // AppIconSearchTextfield(
            //   icon: icSearch,
            //   titleHeaderString: "Search Camp ID",
            //   controller: searchController,
            //   textInputType: TextInputType.number,
            //   onChange: (value) {
            //     searchCampList = searchByDescEn(value);
            //     setState(() {});
            //   },
            // ),
            AppTextField(
              textInputType: TextInputType.number,
              controller: searchController,
              readOnly: false,
              onChange: (value) {
                searchCampList = searchByDescEn(value);
                setState(() {});
              },
              hint: 'Search Camp ID',
              label: CommonText(
                text: 'Search Camp ID',
                fontSize: 12.sp,
                fontWeight: FontWeight.normal,
                textColor: kBlackColor,
                textAlign: TextAlign.start,
              ),
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                fontFamily: FontConstants.interFonts,
              ),
              fieldRadius: 10,
              prefixIcon: SizedBox(
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

            const SizedBox(height: 8),
            Expanded(
              child:
                  isLoading
                      ? const CommonSkeletonList()
                      : ListView.builder(
                        itemCount: searchCampList.length,
                        itemBuilder: (context, index) {
                          ResourceReMappingCampOutput reMappingCampOutput =
                              searchCampList[index];
                          return CampForHealthScreeningRow(
                            reMappingCampOutput: reMappingCampOutput,
                            onSelectTap: () {
                              dISTLGDCODE =
                                  reMappingCampOutput.dISTLGDCODE ?? 0;
                              dISTNAME = reMappingCampOutput.dISTNAME ?? "";
                              campId = reMappingCampOutput.campId ?? 0;
                              campType = reMappingCampOutput.campType ?? 0;

                              if (dESGID == 29 ||
                                  dESGID == 92 ||
                                  dESGID == 104 ||
                                  dESGID == 160 ||
                                  dESGID == 105 ||
                                  dESGID == 77 ||
                                  dESGID == 84 ||
                                  dESGID == 30 ||
                                  dESGID == 108 ||
                                  dESGID == 147 ||
                                  dESGID == 130) {
                                pushToNextScreen();
                              } else {
                                if (DataProvider().getRegularCamp()) {
                                  getUserCampMappingAndAttendanceStatus();
                                } else {
                                  getUserCampMappingAndAttendanceStatusD2D();
                                }
                              }
                            },
                          );
                        },
                      ),
            ),
          ],
        ).paddingSymmetric(vertical: 10, horizontal: 10),
      ),
    );
  }

  getApprovedCampListDetailsForApp() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> params = {
      "CampDATE": selectedCampDate,
      "SubOrgId": subOrgId.toString(),
      "Divison": "0",
      "DISTLGDCODE": "0",
      "USERID": empCode.toString(),
      "DesgId": dESGID.toString(),
    };
    print(params);
    apiManager.getApprovedCampListDetailsForAppFlexiCampAPI(
      params,
      apiApprovedCampListDetailsForAppFlexiCampCallBack,
    );
  }

  void apiApprovedCampListDetailsForAppFlexiCampCallBack(
    ResourceReMappingCampResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      campList = response?.output ?? [];
      searchCampList = campList;
    } else {
      campList = [];
      searchCampList = campList;
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  List<ResourceReMappingCampOutput> searchByDescEn(String query) {
    return campList.where((item) {
      final desc = item.campId?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        selectedCampDate = FormatterManager.formatDateToString(picked);
        getApprovedCampListDetailsForApp();
      });
    }
  }

  void getUserCampMappingAndAttendanceStatus() {
    Map<String, String> params = {
      "CampDATE": selectedCampDate,
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "CampType": campType.toString(),
      "CampID": campId.toString(),
      "TestId": "1",
    };

    apiManager.getUserCampMappingAndAttendanceStatusForRegularCampReadinessAPI(
      params,
      apiUserCampMappingAndAttendanceStatusForRegularCampReadinessCallBack,
    );
  }

  void apiUserCampMappingAndAttendanceStatusForRegularCampReadinessCallBack(
    UserCampMappingAndAttendanceDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      UserCampMappingAndAttendanceDataOutput? obj = response?.output?.first;

      if (obj != null) {
        if (obj.isCampClosed == 1) {
          ToastManager.showAlertDialog(context, "This camp is closed", () {
            Navigator.pop(context);
          });
          return;
        }
        if (obj.campFlag == 0) {
          ToastManager.showAlertDialog(
            context,
            "This camp not mapped to you",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }
        if (obj.isReadinessFormFilled == 0) {
          ToastManager.showAlertDialog(
            context,
            "Readiness form is not filled. Please contact camp coordinator",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }
        if (obj.attendanceFlag == 0) {
          ToastManager.showAlertDialog(
            context,
            "Attendance not marked . Please mark attendance first",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }

        if (widget.testID == 3) {
        } else {
          if (obj.attendanceFlag == 0) {
            ToastManager.showAlertDialog(
              context,
              "You are not mapped to do registration",
              () {
                Navigator.pop(context);
              },
            );
            return;
          }
        }

        pushToNextScreen();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getUserCampMappingAndAttendanceStatusD2D() {
    Map<String, String> params = {
      "CampDATE": selectedCampDate,
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "CampType": campType.toString(),
      "CampID": campId.toString(),
    };

    apiManager.getUserCampMappingAndAttendanceStatusReadinessAPI(
      params,
      apiUserCampMappingAndAttendanceStatusReadinessCallBack,
    );
  }

  void apiUserCampMappingAndAttendanceStatusReadinessCallBack(
    UserCampMappingAndAttendanceDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      UserCampMappingAndAttendanceDataOutput? obj = response?.output?.first;

      if (obj != null) {
        if (obj.isCampClosed == 1) {
          ToastManager.showAlertDialog(context, "This camp is closed", () {
            Navigator.pop(context);
          });
          return;
        }
        if (obj.isReadinessFormFilled == 0) {
          ToastManager.showAlertDialog(
            context,
            "Readiness form is not filled. Please contact camp coordinator",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }
        if (obj.campFlag == 0) {
          ToastManager.showAlertDialog(
            context,
            "This camp not mapped to you",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }

        if (obj.attendanceFlag == 0) {
          ToastManager.showAlertDialog(
            context,
            "Please mark attendance first",
            () {
              Navigator.pop(context);
            },
          );
          return;
        }

        if (obj.testFlag == 3) {
        } else {
          if (obj.attendanceFlag == 0) {
            ToastManager.showAlertDialog(
              context,
              "You are not mapped to do registration",
              () {
                Navigator.pop(context);
              },
            );
            return;
          }
        }
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void pushToNextScreen() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (context) => HealthScreeningDetailsScreen(
              testID: widget.testID,
              teamid: 0,
              districtID: dISTLGDCODE,
              districtName: dISTNAME,
              campID: campId,
              dISTLGDCODE: dISTLGDCODE,
              campType: campType,
              campDate: selectedCampDate,
              surveyCoordinatorName: "",
              campTypeDescription: "",
            ),
      ),
    ).then(getApprovedCampListDetailsForApp());
  }
}


