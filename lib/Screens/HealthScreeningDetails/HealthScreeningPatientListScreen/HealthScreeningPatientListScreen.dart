// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/PhysicalExaminationFormScreen/PhysicalExaminationFormScreen.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'HealthScreeningPatientRow/HealthScreeningPatientRow.dart';

class HealthScreeningPatientListScreen extends StatefulWidget {
  HealthScreeningPatientListScreen({
    super.key,
    required this.screeningMenu,
    required this.districtID,
    required this.districtName,
    required this.campID,
    required this.siteDetailId,
    required this.testId,
    required this.teamid,
    required this.dISTLGDCODE,
  });

  HealthScreeningDetailsMenu screeningMenu;
  int districtID = 0;
  String districtName = "";
  int campID = 0;
  int siteDetailId = 0;
  int testId = 0;
  int teamid = 0;
  int dISTLGDCODE = 0;

  @override
  State<HealthScreeningPatientListScreen> createState() =>
      _HealthScreeningPatientListScreenState();
}

class _HealthScreeningPatientListScreenState
    extends State<HealthScreeningPatientListScreen> {
  String teamNumber = "0";
  int empCode = 0;

  APIManager apiManager = APIManager();
  TextEditingController searchTextField = TextEditingController();
  List<UserAttendancesUsingSitedetailsIDOutput> paitentList = [];

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      teamNumber = "0";
      ToastManager.showLoader();
      getUserAttendancesUsingSitedetailsID();
    } else {
      getTeamNumberByCampIdAndUSerId();
    }
  }

  getUserAttendancesUsingSitedetailsID() {
    String urlString = "";
    Map<String, String> jsonObject = {};

    if (widget.testId != 13) {
      if (widget.testId == 11) {
        urlString =
            "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDUrineChange}";
        jsonObject = {
          "EmpCode": widget.campID.toString(),
          "DistrictId": "0",
          "TestId": widget.testId.toString(),
          "UserId": empCode.toString(),
        };
      } else {
        if (widget.testId == 16) {
          urlString =
              "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
          jsonObject = {
            "TestId": widget.testId.toString(),
            "EmpCode": widget.campID.toString(),
            "DistrictId": "0",
            "UserId": empCode.toString(),
            "TeamId": teamNumber,
          };
        } else {
          if (DataProvider().getRegularCamp()) {
            urlString =
                "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNew}";
            jsonObject = {
              "EmpCode": widget.campID.toString(),
              "DistrictId": "0",
              "TestId": widget.testId.toString(),
              "UserId": empCode.toString(),
            };
          } else {
            urlString =
                "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDAnti}";
            jsonObject = {
              "EmpCode": widget.campID.toString(),
              "DistrictId": "0",
              "TestId": widget.testId.toString(),
              "UserId": empCode.toString(),
              "TeamId": teamNumber,
            };
          }
        }
      }
    } else {
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
      jsonObject = {
        "TestId": widget.testId.toString(),
        "EmpCode": widget.campID.toString(),
        "DistrictId": "0",
        "UserId": empCode.toString(),
        "TeamId": teamNumber,
      };
    }

    apiManager.getUserAttendancesUsingSitedetailsIDAPI(
      urlString,
      jsonObject,
      apiUserAttendancesUsingSitedetailsIDCallBack,
    );
  }

  void apiUserAttendancesUsingSitedetailsIDCallBack(
    UserAttendancesUsingSitedetailsIDResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    paitentList = response?.output ?? [];
    if (success) {
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTeamNumberByCampIdAndUSerId() {}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Patient List",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),

      body: SizedBox(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth,
        child: Column(
          children: [
            // AppIconSearchTextfield(
            //   icon: icSearch,
            //   titleHeaderString: "Patient Name/Registration No",
            //   controller: searchTextField,
            //   onChange: (p0) {},
            // ),
            AppTextField(
              controller: searchTextField,
              readOnly: false,
              label: RichText(
                text: TextSpan(
                  text: 'Patient Name/Registration No',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: responsiveFont(14),
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              labelStyle: TextStyle(
                fontSize: responsiveFont(12),
                fontFamily: FontConstants.interFonts,
              ),
              suffixIcon: SizedBox(
                height: responsiveHeight(24),
                width: responsiveHeight(24),
                child: Center(
                  child: Image.asset(
                    icSearch,
                    height: responsiveHeight(24),
                    width: responsiveHeight(24),
                  ),
                ),
              ),
            ).paddingOnly(top: 10, bottom: 8),
            const SizedBox(height: 10),
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: paitentList.length,
                itemBuilder: (context, index) {
                  UserAttendancesUsingSitedetailsIDOutput patientObj =
                      paitentList[index];
                  return HealthScreeningPatientRow(
                    obj: patientObj,
                    screeningMenu: widget.screeningMenu,
                    onDidPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder:
                              (context) => PhysicalExaminationFormScreen(
                                regdId: patientObj.regdId ?? 0,
                                campTypeID: 0,
                                healthScreentype: "16",
                              ),
                        ),
                      ).then((value) {
                        if (DataProvider().getRegularCamp()) {
                          teamNumber = "0";
                          ToastManager.showLoader();
                          getUserAttendancesUsingSitedetailsID();
                        } else {
                          getTeamNumberByCampIdAndUSerId();
                        }
                      });
                    },
                  );
                },
              ),
            ),
          ],
        ).paddingSymmetric(horizontal: 4),
      ),
    );
  }
}
