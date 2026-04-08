// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/CampDetailsResponse/CampDetailsResponse.dart';
import '../../../Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import '../../../Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/widgets/AppDropdownTextfield.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Views/CampCalenderCampDetails/CampCalenderCampDetails.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../Views/ScreeningDetailsView/ScreeningDetailsView.dart';

class ScreeningTestCampDetailsScreen extends StatefulWidget {
  int campId = 0;
  int dISTLGDCODE = 0;
  String campDate = "";
  String surveyCoordinatorName = "";
  String dISTNAME = "";
  String mOBNO = "";
  int? cAMPTYPE;
  String? campTypeDescription;
  bool isHealthScreeing = false;

  ScreeningTestCampDetailsScreen({
    super.key,
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.cAMPTYPE,
    required this.campTypeDescription,
    required this.isHealthScreeing,
  });

  @override
  State<ScreeningTestCampDetailsScreen> createState() =>
      _ScreeningTestCampDetailsScreenState();
}

class _ScreeningTestCampDetailsScreenState
    extends State<ScreeningTestCampDetailsScreen> {
  int dESGID = 0;
  int empCode = 0;
  String teamNumber = "All";
  String teamID = "0";
  bool isShowTeamDropDown = false;
  CampDetailOutput? campDetailOutput;
  APIManager apiManager = APIManager();
  bool isLoading = false;

  @override
  void initState() {

    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    if (DataProvider().getRegularCamp()) {
      isLoading = true;
      getCampDetailsCount();
    } else {
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
        getCampDetailsCount();
      } else {
        isLoading = true;
        getCampDetailsCount();
      }
    }
    super.initState();

  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Expanded(
        child: SingleChildScrollView(
          child: Column(
            children: [
              // const SizedBox(height: 10),
              isShowTeamDropDown == true
                  ? AppDropdownTextfield(
                    icon: icTeamIconn,
                    titleHeaderString: "Team",
                    valueString: teamNumber,
                    isDisabled: false,
                    onTap: () {
                      ToastManager.showLoader();
                      getCampWiseTeam();
                    },
                  )
                  : Container(),
              isShowTeamDropDown == true
                  ? const SizedBox(height: 10)
                  : Container(),
              widget.isHealthScreeing == true
                  ? Container()
                  : CampCalenderCampDetails(
                    campId: widget.campId,
                    dISTNAME: widget.dISTNAME,
                    surveyCoordinatorName: widget.surveyCoordinatorName,
                    mOBNO: widget.mOBNO,
                  ),
              // const SizedBox(height: 10),
              isLoading
                  ? const CommonSkeletonScreeningDetailsTable().paddingOnly(
                    top: 6,
                  )
                  : ScreeningDetailsView(campDetailOutput: campDetailOutput),
              const SizedBox(height: 10),
            ],
          ),
        ),
      ),
    );
  }

  void getTeamId() {
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
        teamID = firstobj.teamNumber ?? "0";
        teamNumber = firstobj.teamName ?? "";

        getCampDetailsCount();
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

  void getCampDetailsCount() {
    setState(() {
      isLoading = true;
    });
    String urlStirng = "";
    Map<String, String> param = {};
    if (DataProvider().getRegularCamp()) {
      urlStirng =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsCountRegularInCampTest}";
      param = {
        "CampId": widget.campId.toString(),
        "DISTLGDCODE": widget.dISTLGDCODE.toString(),
        "FromDate": widget.campDate,
        "ToDate": widget.campDate,
      };
    } else {
      urlStirng =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsCountInCampTest}";
      param = {
        "CampId": widget.campId.toString(),
        "DISTLGDCODE": widget.dISTLGDCODE.toString(),
        "FromDate": widget.campDate,
        "ToDate": widget.campDate,
        "TeamID": teamID,
      };
    }

    apiManager.getScreeningTestCampDetailsAPI(
      urlStirng,
      param,
      apiScreeningTestCampDetailsCallBack,
    );
  }

  void apiScreeningTestCampDetailsCallBack(
    CampDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      campDetailOutput = response?.output?.first;
    } else {
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  void getCampWiseTeam() {
    Map<String, String> params = {"CampId": widget.campId.toString()};
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
    ToastManager.hideLoader();

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
                teamID = selectedTeam.teamID ?? "";
                teamNumber = selectedTeam.teamNumber ?? "";
                setState(() {
                  isLoading = true;
                });
                getCampDetailsCount();
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
