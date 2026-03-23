// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/CampDetailsScreen/BeneficiaryCampDetailsScreen/BeneficiaryCampRow/BeneficiaryCampRow.dart';
import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import '../../../Modules/Json_Class/TeamNumberByCampIdAndUserIdListResponse/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/widgets/AppDropdownTextfield.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';

class BeneficiaryCampDetailsScreen extends StatefulWidget {
  final int? cAMPTYPE;
  final String? campTypeDescription;
  final int campId;

  const BeneficiaryCampDetailsScreen({
    super.key,
    required this.campId,
    required this.cAMPTYPE,
    required this.campTypeDescription,
  });

  @override
  State<BeneficiaryCampDetailsScreen> createState() =>
      _BeneficiaryCampDetailsScreenState();
}

class _BeneficiaryCampDetailsScreenState
    extends State<BeneficiaryCampDetailsScreen> {
  int dESGID = 0;
  int empCode = 0;

  String teamNumber = "0";
  String teamName = "All";

  bool isShowTeamDropDown = false;
  APIManager apiManager = APIManager();

  List<BeneficiaryWorkerOutput> beneficiaryWorkerList = [];
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      isShowTeamDropDown = false;
      getRegiWorkerDetailsOncampId();
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
        getRegiWorkerDetailsOncampId();
      } else {
        getRegiWorkerDetailsOncampId();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Expanded(
        child: Column(
          children: [
            SizedBox(height: 6.h),
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
            isShowTeamDropDown == true ? SizedBox(height: 10.h) : Container(),
            Expanded(
              child:
                  isLoading
                      ? const CommonSkeletonPatientList()
                      : beneficiaryWorkerList.isNotEmpty
                      ? ListView.builder(
                        itemCount: beneficiaryWorkerList.length,
                        itemBuilder: (context, index) {
                          BeneficiaryWorkerOutput obj =
                              beneficiaryWorkerList[index];
                          return BeneficiaryCampRow(index: index, obj: obj);
                        },
                      )
                      : NoDataFound(),
            ),
          ],
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
        teamNumber = firstobj.teamNumber ?? "0";
        teamName = firstobj.teamName ?? "";
        // showTeam = true;
        // getCampdetails();
        getRegiWorkerDetailsOncampId();
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

  void getRegiWorkerDetailsOncampId() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> dict = {
      "CampId": widget.campId.toString(),
      "TeamID": teamNumber,
    };

    apiManager.getRegiWorkerDetailsOncampIdAPI(
      dict,
      apiRegiWorkerDetailsOncampIdCallBack,
    );
  }

  void apiRegiWorkerDetailsOncampIdCallBack(
    BeneficiaryWorkerResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      beneficiaryWorkerList = response?.output ?? [];
    } else {
      beneficiaryWorkerList = [];
      ToastManager.toast("Worker List Not Found");
    }
    isLoading = false;
    setState(() {});
  }

  void getCampWiseTeam() {
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
                teamNumber = selectedTeam.teamID ?? "";
                teamName = selectedTeam.teamNumber ?? "";
                setState(() {
                  isLoading = true;
                });
                getRegiWorkerDetailsOncampId();
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
