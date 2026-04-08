// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';

import '../../../Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import '../../../Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppIconSearchTextfield.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'AcknowledgementPatientRow/AcknowledgementPatientRow.dart';

class AcknowledgementPatientListScreen extends StatefulWidget {
  AcknowledgementPatientListScreen({
    super.key,
    required this.campID,
    required this.districtID,
    required this.districtName,
    required this.siteDetailId,
    required this.testId,
    required this.teamid,
  });

  int campID = 0;
  int districtID = 0;
  String districtName = "";
  int siteDetailId = 0;
  int testId = 0;
  int teamid = 0;

  @override
  State<AcknowledgementPatientListScreen> createState() =>
      _AcknowledgementPatientListScreenState();
}

class _AcknowledgementPatientListScreenState
    extends State<AcknowledgementPatientListScreen> {
  TextEditingController searchController = TextEditingController();
  String teamNumber = "0";
  String teamName = "";
  int empCode = 0;

  APIManager apiManager = APIManager();
  @override
  void initState() {
    super.initState();

    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      teamNumber = "0";
      getUserAttendancesUsingSitedetailsID();
    } else {
      getTeamNumberByCampIdAndUSerId();
    }
  }

  getTeamNumberByCampIdAndUSerId() {
    Map<String, String> dict = {
      "campid": widget.campID.toString(),
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
        teamNumber = firstobj.teamNumber ?? "0";
        teamName = firstobj.teamName ?? "";
        // showTeam = true;
        // getCampdetails();
        getUserAttendancesUsingSitedetailsID();
      } else {
        ToastManager.hideLoader();
        ToastManager.toast("Data not found");
      }
    } else {
      ToastManager.hideLoader();
      if (errorMessage == "Team Number Data not found") {
        ToastManager.toast("Your Selected Camp Not Mapped To You");
      } else {
        ToastManager.toast(errorMessage);
      }
    }
    setState(() {});
  }

  getUserAttendancesUsingSitedetailsID() {
    Map<String, String> dict = {};

    String urlString = "";
    if (DataProvider().getRegularCamp()) {
      print("getRegularCamp");
      dict = {
        "EmpCode": widget.campID.toString(),
        "DistrictId": "0",
        "TestId": widget.testId.toString(),
        "UserId": empCode.toString(),
      };
      urlString =
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNew}";
    } else {
      dict = {
        "EmpCode": widget.campID.toString(),
        "DistrictId": "0",
        "TestId": widget.testId.toString(),
        "UserId": empCode.toString(),
        "TeamId": teamNumber,
      };
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNew}";
    }
    apiManager.getAcknowledgementPatientListAPI(
      urlString,
      dict,
      apiAcknowledgementPatientCallBack,
    );
  }

  void apiAcknowledgementPatientCallBack(
    AcknowledgementPatientListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Patient List",
          //for Health Screening
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              Positioned(
                top: 74,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect4,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 53,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect3,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 30,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect2,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Image.asset(
                fit: BoxFit.fill,
                rect1,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
              Positioned(
                top: 0,
                bottom: 8,
                left: 8,
                right: 8,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    AppIconSearchTextfield(
                      icon: icSearch,
                      titleHeaderString: "Name / Registration No.",
                      controller: searchController,
                      textInputType: TextInputType.number,
                      onChange: (value) {
                        print(value);
                        // searchCampList = searchByDescEn(value);
                        setState(() {});
                      },
                    ),
                    const SizedBox(height: 8),
                    Expanded(
                      child: ListView.builder(
                        itemCount: 1,
                        itemBuilder: (context, index) {
                          return AcknowledgementPatientRow();
                        },
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
