// ignore_for_file: must_be_immutable, file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppDateTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/RejectedBeneficiaryListScreen/RejectedBeneficiaryInfoScreen/RejectedBeneficiaryDetailsView/RejectedBeneficiaryDetailsView.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/BeneficiaryStatusAndDetailsResponse/BeneficiaryStatusAndDetailsResponse.dart';
import '../../../../Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import '../../../../Modules/Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/DataProvider.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Views/RejectedBeneficiaryTeamView/RejectedBeneficiaryTeamView.dart';
import 'RejectionDetailsView/RejectionDetailsView.dart';

class RejectedBeneficiaryInfoScreen extends StatefulWidget {
  RejectedBeneficiaryInfoScreen({
    super.key,
    required this.obj,
    required this.fromDate,
    required this.toDate,
    required this.oganizationId,
    required this.divisionId,
    required this.dISTLGDCODE,
    required this.tALLGDCODE,
    required this.landingLabId,
    required this.campType,
    required this.statusType,
  });

  RecollectionBeneficiaryStatusandDetailsCountV1Output obj;
  String fromDate = "";
  String toDate = "";
  String oganizationId = "";
  String divisionId = "";
  String dISTLGDCODE = "";
  String tALLGDCODE = "";
  String landingLabId = "";
  String campType = "";
  String statusType = "";

  @override
  State<RejectedBeneficiaryInfoScreen> createState() =>
      _RejectedBeneficiaryInfoScreenState();
}

class _RejectedBeneficiaryInfoScreenState
    extends State<RejectedBeneficiaryInfoScreen> {
  int dESGID = 0;
  int empCode = 0;
  bool showTeam = true;
  bool showRemark = true;
  bool showAppoointmentDate = true;
  bool showAssignButton = true;
  bool showReRegisterButton = false;

  String buttonTitle = "";
  String teamActivae = "";
  int teamID = 0;
  String teamName = "";
  String appoointmentDate = "";
  SelectedTeamsDataLisOutput? selectedTeam;
  APIManager apiManager = APIManager();
  BeneficiaryStatusAndDetailsOutput? beneficiaryObj;

  int? remarkId;
  String remark = "";

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      showTeam = false;

      showRemark = true;

      showAppoointmentDate = true;

      buttonTitle = "Save";
      showAssignButton = true;
      showReRegisterButton = false;
    } else {
      buttonTitle = "Assign";
      showAssignButton = true;
      showReRegisterButton = false;
      showRemark = false;

      showAppoointmentDate = false;
    }
    callAPIGrpup();
  }

  callAPIGrpup() {
    ToastManager.showLoader();
    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      getBeneficiaryDataForTeam();
    } else {
      getBeneficiaryData();
    }
  }

  getBeneficiaryDataForTeam() {
    Map<String, String> params = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": widget.divisionId,
      "DISTLGDCODE": widget.dISTLGDCODE,
      "TALLGDCODE": widget.tALLGDCODE,
      "Labcode": widget.landingLabId,
      "Arid": "0",
      "BeneficiaryNumber": widget.obj.rejRegdid.toString(),
      "UserId": empCode.toString(),
      "Type": "3",
      "CampType": widget.campType,
    };
    apiManager.getBeneficiaryStatusAndDetailsAPI(
      params,
      apiBeneficiaryDataForTeamCallBack,
    );
  }

  void apiBeneficiaryDataForTeamCallBack(
    BeneficiaryStatusAndDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      beneficiaryObj = response?.output?.first;

      if (beneficiaryObj?.isTeamActive == 1) {
        teamActivae = "${beneficiaryObj?.teamName ?? "NA"} (Active)";
      } else {
        teamActivae = "${beneficiaryObj?.teamName ?? "NA"} (InActive)";
      }
      if (beneficiaryObj?.isAppointmentConfirm == 1) {
        showAssignButton = false;
        showAppoointmentDate = true;
        showReRegisterButton = true;
      } else {
        showReRegisterButton = false;
      }

      if (widget.statusType == "1" ||
          widget.statusType == "8" ||
          widget.statusType == "0" ||
          widget.statusType == "9") {
        showAssignButton = false;
        showReRegisterButton = false;
        showRemark = true;
      }
      if (widget.statusType == "1") {
        showReRegisterButton = false;
      }
      if (beneficiaryObj?.isTeamMapped == 1) {
        ToastManager.toast("Team assigned for this beneficiary");
      }
      teamID = beneficiaryObj?.teamID ?? 0;
      teamName = beneficiaryObj?.teamName ?? "";

      remarkId = beneficiaryObj?.arid;

      if (remarkId != null) {
        if (remarkId == 3) {
          showAppoointmentDate = true;
        } else {
          showAppoointmentDate = false;
        }
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  getBeneficiaryData() {
    Map<String, String> params = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": widget.divisionId,
      "DISTLGDCODE": widget.dISTLGDCODE,
      "TALLGDCODE": "0",
      "Labcode": widget.landingLabId,
      "Arid": "0",
      "BeneficiaryNumber": widget.obj.rejRegdid.toString(),
      "UserId": empCode.toString(),
      "Type": "3",
      "CampType": widget.campType,
    };
    apiManager.getBeneficiaryStatusAndDetailsAPI(
      params,
      apiBeneficiaryStatusAndDetailsCallBack,
    );
  }

  void apiBeneficiaryStatusAndDetailsCallBack(
    BeneficiaryStatusAndDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      beneficiaryObj = response?.output?.first;

      if (beneficiaryObj?.isTeamActive == 1) {
        teamActivae = "${beneficiaryObj?.teamName ?? "NA"} (Active)";
      } else {
        teamActivae = "${beneficiaryObj?.teamName ?? "NA"} (InActive)";
      }
      if (beneficiaryObj?.isAppointmentConfirm == 1) {
        showAssignButton = false;
      }

      if (widget.statusType == "1" ||
          widget.statusType == "8" ||
          widget.statusType == "0" ||
          widget.statusType == "9") {
        showAssignButton = false;
      }
      if (beneficiaryObj?.isTeamMapped == 1) {
        ToastManager.toast("Team assigned for this beneficiary");
      }
      teamID = beneficiaryObj?.teamID ?? 0;
      teamName = beneficiaryObj?.teamName ?? "";
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTeamDetailsListForAssign() {
    Map<String, String> params = {
      "Pincode": beneficiaryObj?.pincode ?? "",
      "USERID": empCode.toString(),
    };
    apiManager.getRecollectionTeamDetialsAPI(
      params,
      apiRecollectionTeamDetialsCallBack,
    );
  }

  void apiRecollectionTeamDetialsCallBack(
    SelectedTeamsDataListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAppointmentTeamBottomSheet(response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void showAppointmentTeamBottomSheet(List<SelectedTeamsDataLisOutput> list) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.38,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: RejectedBeneficiaryTeamView(
            list: list,
            onTapTeam: (p0) {
              selectedTeam = p0;
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  String jsonToStringConvert(List<Map<String, String>> list) {
    try {
      return jsonEncode(list);
    } catch (e) {
      return "";
    }
  }

  void insertAppointmentDetails(String jsonString) {
    Map<String, String> params = {
      "RecollectionAppointmentDateType": jsonString,
      "ArId": remarkId?.toString() ?? "0",
      "Userid": empCode.toString(),
    };

    apiManager.insertAppointmentDetailsAPI(
      params,
      apiInsertAppointmentDetailsCallBack,
    );
  }

  void apiInsertAppointmentDetailsCallBack(
    BeneficiaryStatusAndDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      if (remarkId == 3) {
        ToastManager.showSuccessPopup(
          context,
          icSuccessIcon,
          "Appointment Booked successfully",
        );
      } else {
        ToastManager.showSuccessPopup(
          context,
          icSuccessIcon,
          "Data Saved successfully",
        );
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  submitData() {
    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      if (remark.isEmpty) {
        ToastManager.toast("Please Select remark");
        return;
      }

      if (remarkId == 3) {
        if (appoointmentDate.isEmpty) {
          ToastManager.toast("Please Select appointment date");
          return;
        }
      }

      ToastManager().showConfirmationDialog(
        context: context,
        message: 'Are you sure you want to Continue?',
        didSelectYes: (bool p1) {
          if(p1 == true){
            Navigator.pop(context);
            ToastManager.showLoader();
            List<Map<String, String>> teamUserJsonArray = [];
            final jsonObject = {
              "Regdid": beneficiaryObj?.rejRegdid.toString() ?? "",
              "Regdno": teamID.toString(),
              "AppointmentDate": appoointmentDate,
            };
            teamUserJsonArray.add(jsonObject);
            final jsonString = jsonToStringConvert(teamUserJsonArray);
            insertAppointmentDetails(jsonString);
          }else  if(p1 == false){
            Navigator.pop(context);

          }
        },
      );

      // showDialog(
      //   context: context,
      //   builder: (BuildContext context) {
      //     return AlertDialog(
      //       title: Text("Alert"),
      //       content: Text("Are you sure you want to Continue?"),
      //       actions: [
      //         TextButton(
      //           child: const Text("Yes"),
      //           onPressed: () {
      //             Navigator.pop(context);
      //             ToastManager.showLoader();
      //             List<Map<String, String>> teamUserJsonArray = [];
      //             final jsonObject = {
      //               "Regdid": beneficiaryObj?.rejRegdid.toString() ?? "",
      //               "Regdno": teamID.toString(),
      //               "AppointmentDate": appoointmentDate,
      //             };
      //             teamUserJsonArray.add(jsonObject);
      //             final jsonString = jsonToStringConvert(teamUserJsonArray);
      //             insertAppointmentDetails(jsonString);
      //           },
      //         ),
      //         TextButton(
      //           child: const Text("No"),
      //           onPressed: () {
      //             Navigator.pop(context);
      //           },
      //         ),
      //       ],
      //     );
      //   },
      // );
    } else {
      if (teamName.isEmpty) {
        ToastManager.toast("Please Select Team");

        return;
      }
      if (teamName == "NA") {
        ToastManager.toast("Please Select Team");
        return;
      }

      // showDialog(
      //   context: context,
      //   builder: (BuildContext context) {
      //     return AlertDialog(
      //       title: Text("Alert"),
      //       content: Text("Are you sure you want to Continue?"),
      //       actions: [
      //         TextButton(
      //           child: const Text("Yes"),
      //           onPressed: () {
      //             Navigator.pop(context);
      //             ToastManager.showLoader();
      //             List<Map<String, String>> teamUserJsonArray = [];
      //
      //             final jsonObject = {
      //               "USERID": selectedTeam?.memberUserID1.toString() ?? "0",
      //               "Teamid": selectedTeam?.teamID.toString() ?? "0",
      //             };
      //             final jsonObject1 = {
      //               "USERID": selectedTeam?.memberUserID2.toString() ?? "0",
      //               "Teamid": selectedTeam?.teamID.toString() ?? "0",
      //             };
      //
      //             teamUserJsonArray.add(jsonObject);
      //             teamUserJsonArray.add(jsonObject1);
      //
      //             final jsonString = jsonToStringConvert(teamUserJsonArray);
      //             insertAppointmentDetails(jsonString);
      //           },
      //         ),
      //         TextButton(
      //           child: const Text("No"),
      //           onPressed: () {
      //             Navigator.pop(context);
      //           },
      //         ),
      //       ],
      //     );
      //   },
      // );


      ToastManager().showConfirmationDialog(
        context: context,
        message: 'Are you sure you want to Continue?',
        didSelectYes: (bool p1) {
          if(p1 == true){
            Navigator.pop(context);
            ToastManager.showLoader();
            List<Map<String, String>> teamUserJsonArray = [];

            final jsonObject = {
              "USERID": selectedTeam?.memberUserID1.toString() ?? "0",
              "Teamid": selectedTeam?.teamID.toString() ?? "0",
            };
            final jsonObject1 = {
              "USERID": selectedTeam?.memberUserID2.toString() ?? "0",
              "Teamid": selectedTeam?.teamID.toString() ?? "0",
            };

            teamUserJsonArray.add(jsonObject);
            teamUserJsonArray.add(jsonObject1);

            final jsonString = jsonToStringConvert(teamUserJsonArray);
            insertAppointmentDetails(jsonString);
          }else  if(p1 == false){
            Navigator.pop(context);

          }
        },
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Beneficiary Details",
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
                child: SingleChildScrollView(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        RejectedBeneficiaryDetailsView(
                          beneficiaryObj: beneficiaryObj,
                          mobile: widget.obj.mobileNo ?? "",
                        ),
                        const SizedBox(height: 20),
                        RejectionDetailsView(beneficiaryObj: beneficiaryObj),
                        const SizedBox(height: 20),
                        Container(
                          padding: EdgeInsets.all(10),
                          width: MediaQuery.of(context).size.width - 40,
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
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                teamActivae,
                                textAlign: TextAlign.left,
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kPrimaryColor,
                                  fontSize: responsiveFont(16),
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                              const SizedBox(height: 10),
                              AppDropdownTextfield(
                                icon: icUsersGroup,
                                titleHeaderString: "Team *",
                                valueString: teamName,
                                onTap: () {
                                  getTeamDetailsListForAssign();
                                },
                              ),
                              showRemark == true
                                  ? const SizedBox(height: 10)
                                  : Container(),
                              showRemark == true
                                  ? AppDropdownTextfield(
                                    icon: iconDocument,
                                    titleHeaderString: "Remark *",
                                    valueString: "",
                                    onTap: () {},
                                  )
                                  : Container(),
                              showAppoointmentDate == true
                                  ? AppDateTextfield(
                                    icon: icCalendarMonth,
                                    titleHeaderString: "Appointment Date *",
                                    valueString: appoointmentDate,
                                    onTap: () {},
                                  )
                                  : Container(),
                            ],
                          ),
                        ),
                        const SizedBox(height: 30),
                        Center(
                          child: SizedBox(
                            width: 140,
                            height: 40,
                            child: AppActiveButton(
                              buttontitle: "Assign",
                              isCancel: false,
                              onTap: () {
                                submitData();
                              },
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
