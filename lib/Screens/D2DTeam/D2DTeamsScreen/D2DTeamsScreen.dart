// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/DispatchGroup/DispatchGroup.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';

import '../../../Modules/Json_Class/D2DNonWorkingTeamsResponse/D2DNonWorkingTeamsResponse.dart';
import '../../../Modules/Json_Class/D2DTeamMemberDetailsResponse/D2DTeamMemberDetailsResponse.dart';
import '../../../Modules/Json_Class/D2DTeamsCountResponse/D2DTeamsCountResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../CallToTeamPopupView/CallToTeamPopupView.dart';
import '../WorkingTeamsCountView/WorkingTeamsCountView.dart';
import 'D2DTeamFilterView/D2DTeamFilterView.dart';
import 'D2DTeamsRow/D2DTeamsRow.dart';

class D2DTeamScreen extends StatefulWidget {
  const D2DTeamScreen({super.key});

  @override
  State<D2DTeamScreen> createState() => _D2DTeamScreenState();
}

class _D2DTeamScreenState extends State<D2DTeamScreen> {
  String naviTitleString = "D2D Not Working Team";

  int workingTeamsCount = 0;
  int notWorkingTeamsCount = 0;
  int totalTeamsCount = 0;

  String sTATELGDCODE = "2";
  String camptypeId = "0";
  String divisioId = "0";
  int dISTLGDCODE = 0;
  String labId = "0";
  int empCode = 0;
  int dESGID = 0;
  int subOrgId = 0;

  APIManager apiManager = APIManager();

  List<D2DNonWorkingTeamsOutput> listD2DNonWorkingTeamsOutput = [];
  List<D2DTeamMemberDetailsOutput> callingList = [];

  DispatchGroup dispatchGroup = DispatchGroup();
  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;

    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    subOrgId = DataProvider().getParsedUserData()?.output?.first.subOrgId ?? 0;

    groupAPICall();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: naviTitleString,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
        showActions: true,
        actions: [
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
            child: GestureDetector(
              onTap: () {
                _showFilterBottomSheet();
              },
              child: SizedBox(
                width: 20,
                height: 20,
                child: Image.asset(icFilter),
              ),
            ),
          ),
        ],
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(0, 12, 0, 12),
            child: Column(
              children: [
                WorkingTeamsCountView(
                  workingTeamsCount: workingTeamsCount,
                  notWorkingTeamsCount: notWorkingTeamsCount,
                  totalTeamsCount: totalTeamsCount,
                  onNotWorkingTeamsTap: () {
                    dispatchGroup = DispatchGroup();
                    groupAPICall();
                    setState(() {
                      naviTitleString = "D2D Not Working Team";
                    });
                  },
                  onWorkingTeamsTap: () {
                    getWorkingTamData();
                    setState(() {
                      naviTitleString = "D2D Working Team";
                    });
                  },
                  onTotalTeamsTap: () {},
                ).paddingSymmetric(horizontal: 8),
                const SizedBox(height: 12),
                Expanded(
                  child: ListView.builder(
                    itemCount: listD2DNonWorkingTeamsOutput.length,
                    itemBuilder: (context, index) {
                      D2DNonWorkingTeamsOutput item =
                      listD2DNonWorkingTeamsOutput[index];
                      return Padding(
                        padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
                        child: D2DTeamsRow(
                          item: item,
                          onCallingTap: () {
                            getTeamsCalling(item.teamid ?? 0);
                          },
                        ),
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


  void groupAPICall() {
    ToastManager.showLoader();
    dispatchGroup.enter();
    dispatchGroup.enter();
    getAdminDataCount();
    getNotWorkingTamData();

    dispatchGroup.notify(() {
      dispatchGroup.reset();
      debugPrint("All APIs Completed!");
      ToastManager.hideLoader();
      setState(() {});
    });
  }

  void getAdminDataCount() {
    Map<String, String> params = {
      "GLOUSERID": empCode.toString(),
      "CampType": camptypeId,
      "DivId": divisioId,
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "LabCode": labId,
      "DesgId": dESGID.toString(),
      "SubOrgId": subOrgId.toString(),
    };
    apiManager.getActiveInactiveD2DTeamsCountAPI(
      params,
      apiActiveInactiveD2DTeamsCountCallBack,
    );
  }

  void apiActiveInactiveD2DTeamsCountCallBack(
      D2DTeamsCountResponse? response,
      String errorMessage,
      bool success,
      ) async {
    dispatchGroup.leave();
    if (success) {
      workingTeamsCount = response?.output?.first.workingTeamCount ?? 0;
      notWorkingTeamsCount = response?.output?.first.nonWorkingTeamCount ?? 0;
      totalTeamsCount = response?.output?.first.totalTeamCount ?? 0;
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getNotWorkingTamData() {
    Map<String, String> params = {
      "GLOUSERID": empCode.toString(),
      "CampType": camptypeId,
      "DivId": divisioId,
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "LabCode": labId,
      "DesgId": dESGID.toString(),
      "SubOrgId": subOrgId.toString(),
    };
    apiManager.getNotWorkingTeamsCountAPI(
      params,
      apiNotWorkingTeamsCountCallBack,
    );
  }

  void apiNotWorkingTeamsCountCallBack(
      D2DNonWorkingTeamsResponse? response,
      String errorMessage,
      bool success,
      ) async {
    dispatchGroup.leave();
    listD2DNonWorkingTeamsOutput = response?.output ?? [];
    if (success) {
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getWorkingTamData() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "GLOUSERID": empCode.toString(),
      "CampType": camptypeId,
      "DivId": divisioId,
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "LabCode": labId,
      "DesgId": dESGID.toString(),
      "SubOrgId": subOrgId.toString(),
    };
    apiManager.getWorkingTeamsCountAPI(params, apiWorkingTeamsCountCallBack);
  }

  void apiWorkingTeamsCountCallBack(
      D2DNonWorkingTeamsResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();
    listD2DNonWorkingTeamsOutput = response?.output ?? [];
    if (success) {
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTeamsCalling(int teamID) {
    ToastManager.showLoader();
    Map<String, String> params = {"Teamid": teamID.toString()};

    apiManager.getTeamsCallingAPI(params, apiTeamsCallingCallBack);
  }

  void apiTeamsCallingCallBack(
      D2DTeamMemberDetailsResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();
    callingList = response?.output ?? [];
    if (success) {
      showBottomPopup(context);
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void showBottomPopup(BuildContext parentContext) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: CallToTeamPopupView(callingList: callingList),
        );
      },
    );
  }

  void _showFilterBottomSheet() {
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
          height: MediaQuery.of(context).size.width * 0.76,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: D2DTeamFilterView(
            selectedDistrictValue: (value) {
              dISTLGDCODE = value?.dISTLGDCODE ?? 0;
            },
            selectedLabValue: (value) {
              int labCode = value?.labCode ?? 0;
              labId = labCode.toString();
            },
            onApplyTap: () {
              groupAPICall();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

}

