// ignore_for_file: must_be_immutable, file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/PatientStatusDetailsListResponse/PatientStatusDetailsListResponse.dart';
import '../../../Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import '../../../Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppDropdownTextfield.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';

class PatientStatusScreen extends StatefulWidget {
  final int? cAMPTYPE;
  final String? campTypeDescription;
  final int campId;
  final int dISTLGDCODE;

  const PatientStatusScreen({
    super.key,
    required this.campId,
    required this.cAMPTYPE,
    required this.campTypeDescription,
    required this.dISTLGDCODE,
  });

  @override
  State<PatientStatusScreen> createState() => _PatientStatusScreenState();
}

class _PatientStatusScreenState extends State<PatientStatusScreen> {
  TextEditingController searchController = TextEditingController();

  int dESGID = 0;
  int empCode = 0;
  String teamId = "0";
  String teamName = "All";
  bool isShowTeamDropDown = false;
  APIManager apiManager = APIManager();

  List<PatientStatusDetailsOutput> patientStatusDetailsList = [];
  List<PatientStatusDetailsOutput> searchPatientStatusDetailsList = [];
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (!DataProvider().getRegularCamp()) {
      if (dESGID == 92 ||
          dESGID == 29 ||
          dESGID == 160 ||
          dESGID == 104 ||
          dESGID == 162 ||
          dESGID == 78 ||
          dESGID == 77 ||
          dESGID == 128 ||
          dESGID == 30 ||
          dESGID == 108 ||
          dESGID == 84 ||
          dESGID == 139 ||
          dESGID == 136) {
        isShowTeamDropDown = true;
      } else {
        isShowTeamDropDown = false;
      }
      if (dESGID == 35 ||
          dESGID == 64 ||
          dESGID == 86 ||
          dESGID == 146 ||
          dESGID == 129 ||
          dESGID == 138 ||
          dESGID == 137 ||
          dESGID == 169 ||
          dESGID == 31 ||
          dESGID == 176 ||
          dESGID == 177) {
        getTeamId();
      } else if (dESGID == 92 ||
          dESGID == 29 ||
          dESGID == 160 ||
          dESGID == 104 ||
          dESGID == 162 ||
          dESGID == 78 ||
          dESGID == 77 ||
          dESGID == 128 ||
          dESGID == 30 ||
          dESGID == 108 ||
          dESGID == 34 ||
          dESGID == 147 ||
          dESGID == 130 ||
          dESGID == 141 ||
          dESGID == 84 ||
          dESGID == 139 ||
          dESGID == 136) {
        isLoading = true;
        getCAMPPatientCheckupAnalysisReport();
      } else {
        isLoading = true;
        getCAMPPatientCheckupAnalysisReport();
      }
    } else {
      isLoading = true;
      getCAMPPatientCheckupAnalysisReport();
    }
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Expanded(
        child: Column(
          children: [
            SizedBox(height: 16.h),
            isShowTeamDropDown == true
                ? AppDropdownTextfield(
                  icon: icTeamIconn,
                  titleHeaderString: "Team",
                  valueString: teamName,
                  isDisabled: false,
                  onTap: () {
                    getCampWiseTeam();
                  },
                )
                : Container(),
            isShowTeamDropDown == true ? SizedBox(height: 8.h) : Container(),
            // AppIconSearchTextfield(
            //   icon: icSearch,
            //   titleHeaderString: "Name or Registration No.",
            //   controller: searchController,
            //   textInputType: TextInputType.number,
            //   onChange: (value) {
            //     print(value);
            //     searchPatientStatusDetailsList = searchByDescEn(value);
            //     setState(() {});
            //   },
            // ),
            AppTextField(
              onChange: (value) {
                print(value);
                searchPatientStatusDetailsList = searchByDescEn(value);
                setState(() {});
              },
              controller: searchController,
              readOnly: true,
              textInputType: TextInputType.number,
              inputStyle: TextStyle(
                fontSize: 14.sp * 1.33,
                color: Colors.black,
              ),
              label: RichText(
                text: TextSpan(
                  text: 'Name or Registration No.',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: 14.sp * 1.33,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              labelStyle: TextStyle(fontSize: 14.sp * 1.33),

              suffixIcon: Icon(Icons.search),
            ),
            SizedBox(height: 8.h),
            Expanded(
              child:
                  isLoading
                      ? const CommonSkeletonPatientList()
                      : searchPatientStatusDetailsList.isNotEmpty
                      ? ListView.builder(
                        itemCount: searchPatientStatusDetailsList.length,
                        itemBuilder: (context, index) {
                          PatientStatusDetailsOutput
                          patientStatusDetailsOutput =
                              searchPatientStatusDetailsList[index];
                          return Padding(
                            padding: EdgeInsets.fromLTRB(0, 0, 0, 12.h),
                            child: Container(
                              padding: EdgeInsets.symmetric(
                                vertical: 8.h,
                                horizontal: 8.w,
                              ),
                              width: SizeConfig.screenWidth,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                boxShadow: [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.15),
                                    blurRadius: 10,
                                  ),
                                ],
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Column(
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.center,
                                    children: [
                                      SizedBox(
                                        width: 20.w,
                                        height: 20.h,
                                        child: Image.asset(icUserIcon),
                                      ),
                                      SizedBox(width: 6.w),
                                      Expanded(
                                        child: RichText(
                                          text: TextSpan(
                                            text:
                                                "${patientStatusDetailsOutput.patientName ?? ""} : ",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: 14.sp,
                                            ),
                                            children: [
                                              TextSpan(
                                                text:
                                                    "${patientStatusDetailsOutput.regdNo ?? ""}",
                                                style: TextStyle(
                                                  color: dropDownTitleHeader,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ],
                                          ),
                                          softWrap: true,
                                        ),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 6.h),
                                  Container(
                                    width: SizeConfig.screenWidth,
                                    height: 30.h,
                                    decoration: BoxDecoration(
                                      color: campCalenderBorder,
                                      borderRadius: BorderRadius.only(
                                        topLeft: Radius.circular(5),
                                        topRight: Radius.circular(5),
                                      ),
                                      border: Border.all(
                                        width: 1,
                                        color: campCalenderBorder,
                                      ),
                                    ),
                                    child: Row(
                                      children: [
                                        Expanded(
                                          child: Container(
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                              borderRadius: BorderRadius.only(
                                                topLeft: Radius.circular(5),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "Basic",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: Text(
                                                "PE",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: Text(
                                                "LFT",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: Text(
                                                "VST",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: Text(
                                                "AST",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                              borderRadius: BorderRadius.only(
                                                topRight: Radius.circular(5),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "SC",
                                                style: TextStyle(
                                                  color: uploadBillTitleColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
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
                                  Container(
                                    width: SizeConfig.screenWidth,
                                    height: 30.h,
                                    decoration: BoxDecoration(
                                      color: campCalenderBorder,
                                      border: Border.all(
                                        width: 1,
                                        color: campCalenderBorder,
                                      ),
                                    ),
                                    child: Row(
                                      children: [
                                        Expanded(
                                          child: Container(
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                            ),
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .basicDetails ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .physicalExamination ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .lungFunctioinTest ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .visionScreening ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            color: Colors.white,
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .audioScreeningTest ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        const SizedBox(width: 1),
                                        Expanded(
                                          child: Container(
                                            decoration: BoxDecoration(
                                              color: Colors.white,
                                            ),
                                            child: Center(
                                              child: SizedBox(
                                                width: 20.w,
                                                height: 20.h,
                                                child: Image.asset(
                                                  checkPatienStatus(
                                                    patientStatusDetailsOutput
                                                            .barcode ??
                                                        "",
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          );
                        },
                      )
                      : NoDataFound(),
            ),
          ],
        ).paddingSymmetric(horizontal: 4.w),
      ),
    );
  }

  getTeamId() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> dict = {
      "campid": widget.campId.toString(),
      "UserID": empCode.toString(),
    };
    apiManager.getTeamNumberByCampIdAndUSerIdAPI(dict, apiTeamIdCallBack);
  }

  void apiTeamIdCallBack(
    TeamNumberByCampIdAndUserIdListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      TeamNumberByCampIdOutput? firstobj = response?.output?.first;

      if (firstobj != null) {
        teamId = firstobj.teamNumber ?? "0";
        teamName = firstobj.teamName ?? "";
        getCAMPPatientCheckupAnalysisReport();
      } else {
        isLoading = false;
        ToastManager.toast("Data not found");
      }
    } else {
      isLoading = false;
      if (errorMessage == "Team Number Data not found") {
        ToastManager.toast("Your Selected Camp Not Mapped To You");
      } else {
        ToastManager.toast(errorMessage);
      }
    }
    setState(() {});
  }

  getCAMPPatientCheckupAnalysisReport() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> params = {
      "CampId": widget.campId.toString(),
      "DISTLGDCODE":
          widget.dISTLGDCODE == 0 ? "0" : widget.dISTLGDCODE.toString(),
      "TeamId": teamId,
    };
    apiManager.getCAMPPatientCheckupAnalysisReportAPI(
      params,
      apiCAMPPatientCheckupAnalysisReportCallBack,
    );
  }

  void apiCAMPPatientCheckupAnalysisReportCallBack(
    PatientStatusDetailsListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      patientStatusDetailsList = response?.output ?? [];
      searchPatientStatusDetailsList = patientStatusDetailsList;
    } else {
      patientStatusDetailsList = [];
      searchPatientStatusDetailsList = [];
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  String checkPatienStatus(String status) {
    if (status == "0") {
      return icCrossIcon;
    }
    return icCheckIcon;
  }

  List<PatientStatusDetailsOutput> searchByDescEn(String query) {
    return patientStatusDetailsList.where((item) {
      final desc = item.regdNo?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  getCampWiseTeam() {
    int campId = widget.campId;
    Map<String, String> params = {"CampId": campId.toString()};
    apiManager.getCampIDWiseTeamDetailsAPI(
      params,
      apiCampIDWiseTeamDetailsCallBack,
    );
  }

  void apiCampIDWiseTeamDetailsCallBack(
    TeamDetailsListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      _showDropDownBottomSheet(
        "Select Team",
        response?.output ?? [],
        DropDownTypeMenu.CampDetailsTeam,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
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
              if (dropDownType == DropDownTypeMenu.CampDetailsTeam) {
                TeamDetailsOutput selectedTeam = p0;
                teamId = selectedTeam.teamID ?? "";
                teamName = selectedTeam.teamNumber ?? "";
                setState(() {
                  isLoading = true;
                });
                getCAMPPatientCheckupAnalysisReport();
              }
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
