// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../Modules/DispatchGroup/DispatchGroup.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/AssignResourcesResponse/AssignResourcesResponse.dart';
import '../../../Modules/Json_Class/AssignTypeModel/AssignTypeModel.dart';
import '../../../Modules/Json_Class/AssignedExternalResourceDetailsResponse/AssignedExternalResourceDetailsResponse.dart';
import '../../../Modules/Json_Class/CampListV3Response/CampListV3Response.dart';
import '../../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../../Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/TeamCampDetailsListResponse/TeamCampDetailsListResponse.dart';
import '../../../Modules/Json_Class/TeamCampLabResponse/TeamCampLabResponse.dart';
import '../../../Modules/Json_Class/TeamDetailsListForAssignResponse/TeamDetailsListForAssignResponse.dart';
import '../../../Modules/Json_Class/TeamsCampTypeWiseResponse/TeamsCampTypeWiseResponse.dart';
import '../../../Modules/Json_Class/TeamsDoctorListResponse/TeamsDoctorListResponse.dart';
import '../../../Modules/Json_Class/TeamsMMUDoctorListResponse/TeamsMMUDoctorListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../Views/TeamDetailsListForAssignView/TeamDetailsListForAssignView.dart';
import '../../../Views/TeamsCampTypeWiseTeamView/TeamsCampTypeWiseTeamView.dart';
import '../../../Views/TeamsDetailsListForAssignView/TeamsDetailsListForAssignView.dart';
import '../AssignedDoctorsView/AssignedDoctorsView.dart';
import '../AssignedFlexiDoctorsView/AssignedFlexiDoctorsView.dart';
import '../AssignedTeamsView/AssignedTeamsView.dart';
import '../MMUDoctorView/MMUDoctorView.dart';

class TeamCampMappingScreen extends StatefulWidget {
  const TeamCampMappingScreen({super.key});

  @override
  State<TeamCampMappingScreen> createState() => _TeamCampMappingScreenState();
}

class _TeamCampMappingScreenState extends State<TeamCampMappingScreen> {
  int dESGID = 0;
  int sTATELGDCODE = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;
  String fromDate = "";
  CampTypeOutput? selectedCampType;
  DistrictOutput? selectedDistrict;
  TeamCampLabOutput? selectedLab;
  CampListV3Output? selectedCampID;
  AssignTypeModel? selectedTeam;
  TeamDetailsListForAssignOutput? selectedTeams;

  List<AssignedExternalResourceDetailsOutput> assignedPhleboList = [];
  List<AssignedExternalResourceDetailsOutput> assignedDataEntryOperatorList =
      [];
  List<AssignedExternalResourceDetailsOutput> assignedDoctorsList = [];
  List<TeamCampDetailsOutput> assignedTeamsList = [];

  List<AssignedExternalResourceDetailsOutput> assignedFlexPhleboList = [];
  List<AssignedExternalResourceDetailsOutput> assignedFlexDoctorsList = [];
  List<TeamsMMUDoctorListOutput> assignedMMUDoctorList = [];
  List<TeamsCampTypeWiseOutput> teamList = [];

  List<TeamDetailsListForAssignOutput> teamsList = [];
  List<AssignResourcesOutput> assignResourcesList = [];

  AssignResourcesOutput? selectedAssignResources;

  List<TeamsDoctorListOutput> phleboList = [];
  List<TeamsDoctorListOutput> deList = [];
  List<TeamsDoctorListOutput> doctorList = [];

  APIManager apiManager = APIManager();
  DispatchGroup apiGroup = DispatchGroup();
  int type = 0;
  int refreshFlag = 0;

  bool showTeamDropDown = true;
  bool assignButtonDisabled = false;

  @override
  void initState() {
    LoginResponseModel? loginResponseModel = DataProvider().getParsedUserData();
    empCode = loginResponseModel?.output?.first.empCode ?? 0;
    dESGID = loginResponseModel?.output?.first.dESGID ?? 0;
    sTATELGDCODE = loginResponseModel?.output?.first.sTATELGDCODE ?? 0;
    dISTLGDCODE = loginResponseModel?.output?.first.dISTLGDCODE ?? 0;
    super.initState();

    fromDate = FormatterManager.formatDateToString(DateTime.now());
    setState(() {});
  }

  Future<void> _selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      fromDate = FormatterManager.formatDateToString(picked);
      String todayDate = FormatterManager.formatDateToString(DateTime.now());

      if (fromDate == todayDate) {
        showTeamDropDown = true;
        assignButtonDisabled = false;
      } else {
        showTeamDropDown = false;
        assignButtonDisabled = true;
      }

      resetAllFields();
    }
  }

  void resetAllFields() {
    selectedCampType = null;
    selectedDistrict = null;
    selectedLab = null;
    selectedCampID = null;
    selectedTeam = null;
    selectedTeams = null;
    assignedPhleboList = [];
    assignedDataEntryOperatorList = [];
    assignedDoctorsList = [];
    assignedTeamsList = [];
    assignedFlexPhleboList = [];
    assignedFlexDoctorsList = [];
    assignedMMUDoctorList = [];
    setState(() {});
  }

  void getCampType() {
    ToastManager.showLoader();
    if (DataProvider().getRegularCamp()) {
      apiManager.getCampTypeNonD2DRAPI(apiCampTypeNonD2CallBack);
    } else {
      if (dESGID == 108) {
        apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
      } else if (dESGID == 136 || dESGID == 139) {
        apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
      } else {
        apiManager.getCampTypeD2DAPI(apiCampTypeD2DCallBack);
      }
    }
  }

  void apiCampTypeNonD2CallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeFlexiCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeMMUCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeD2DCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getAllDistrictList() {
    ToastManager.showLoader();
    Map<String, String> params = {"STATELGDCODE": "2", "USERID": "$empCode"};
    apiManager.getDistrictByUserIDAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        List<DistrictOutput> list = response?.output ?? [];
        _showDropDownBottomSheet(
          "Select District",
          list,
          DropDownTypeMenu.District,
        );
      } else {
        ToastManager.toast(error);
      }
    });
  }

  void getLabList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
    };
    apiManager.getTeamCampLabListAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        List<TeamCampLabOutput> list = response?.output ?? [];
        _showDropDownBottomSheet(
          "Select Lab",
          list,
          DropDownTypeMenu.TeamCampLab,
        );
      } else {
        ToastManager.toast(error);
      }
    });
  }

  void getCampList() {
    if (fromDate.isEmpty) {
      ToastManager().showAlertMessage(context, "Select Camp Date", Colors.red);
    } else if (selectedCampType == null) {
      ToastManager().showAlertMessage(context, "Select Camp Type", Colors.red);
    } else if (selectedDistrict == null) {
      ToastManager().showAlertMessage(context, "Select District", Colors.red);
    } else if (selectedLab == null) {
      ToastManager().showAlertMessage(context, "Select Lab", Colors.red);
    } else {
      ToastManager.showLoader();
      Map<String, String> data = {
        "CampDATE": fromDate,
        "UserId": empCode.toString(),
        "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
        "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
        "LABCODE": selectedLab?.labCode.toString() ?? "0",
      };
      apiManager.getCampIDAPI(data, apiCampIDCallBack);
    }
  }

  void apiCampIDCallBack(
    CampListV3Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp ID",
        response?.output ?? [],
        DropDownTypeMenu.CampID,
      );
    } else {
      ToastManager.toast(errorMessage);
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
              if (dropDownType == DropDownTypeMenu.CampType) {
                selectedCampType = p0;
                selectedDistrict = null;
              } else if (dropDownType == DropDownTypeMenu.District) {
                selectedDistrict = p0;
                selectedLab = null;
              } else if (dropDownType == DropDownTypeMenu.TeamCampLab) {
                selectedLab = p0;
              } else if (dropDownType == DropDownTypeMenu.CampID) {
                selectedCampID = p0;
                getAllDetails();
              } else if (dropDownType == DropDownTypeMenu.SelectTeam) {
                selectedTeam = p0;
                getTeamsList();
              } else if (dropDownType == DropDownTypeMenu.AssignResources) {
                selectedAssignResources = p0;
                if (selectedAssignResources?.desgId == 34) {
                  if (selectedTeams?.teamName == null) {
                    ToastManager.toast(
                      "Please Select Assigned Team Before Adding Resource",
                    );
                  } else {
                    getDoctorList();
                  }
                } else if (selectedAssignResources?.desgId == 35) {
                  if (selectedTeams?.teamName == null) {
                    ToastManager.toast(
                      "Please Select Assigned Team Before Adding Resource",
                    );
                  } else {
                    getPhleboList();
                  }
                } else if (selectedAssignResources?.desgId == 86) {
                  if (selectedTeams?.teamName == null) {
                    ToastManager.toast(
                      "Please Select Assigned Team Before Adding Resource",
                    );
                  } else {
                    getDeOpList();
                  }
                } else if (selectedAssignResources?.desgId == 146 ||
                    selectedAssignResources?.desgId == 129) {
                  getFlexiPhleboList();
                } else if (selectedAssignResources?.desgId == 147 ||
                    selectedAssignResources?.desgId == 129) {
                  getFlexiDoctorList();
                } else if (selectedAssignResources?.desgId == 141) {
                  getMMUDoctorList();
                }
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

  void getAllDetails() {
    apiGroup.enter();
    apiGroup.enter();
    apiGroup.enter();
    apiGroup.enter();
    apiGroup.enter();
    apiGroup.enter();
    apiGroup.enter();
    ToastManager.showLoader();
    getPhleboDetailsList();
    getDeopDetailsList();
    getDoctorDetailsList();
    getTeamDetailsList();
    getFlexiPhleboDetailsList();
    getFlexiDoctorDetailsList();
    getMMUDoctorDetailsList();

    apiGroup.notify(() {
      apiGroup.reset();
      debugPrint("All APIs Completed!");
      ToastManager.hideLoader();
      setState(() {});
    });
  }

  void getPhleboDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "35",
    };

    apiManager.getPhleboDetailsListAPI(params, apiPhleboDetailsCallBack);
  }

  void apiPhleboDetailsCallBack(
    AssignedExternalResourceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedPhleboList = response?.output ?? [];
    apiGroup.leave();
  }

  void getDeopDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "64",
    };

    apiManager.getDeopDetailsListAPI(params, apiDeopDetailsCallBack);
  }

  void apiDeopDetailsCallBack(
    AssignedExternalResourceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedDataEntryOperatorList = response?.output ?? [];
    apiGroup.leave();
  }

  void getDoctorDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "34",
    };

    apiManager.getDoctorDetailsListAPI(params, apiDoctorDetailsCallBack);
  }

  void apiDoctorDetailsCallBack(
    AssignedExternalResourceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedDoctorsList = response?.output ?? [];
    apiGroup.leave();
  }

  void getTeamDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
    };

    apiManager.getTeamDetailsListAPI(params, apiTeamDetailsCallBack);
  }

  void apiTeamDetailsCallBack(
    TeamCampDetailsListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedTeamsList = response?.output ?? [];
    apiGroup.leave();
  }

  void getFlexiPhleboDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "146",
    };

    apiManager.getFlexiPhleboDetailsListAPI(
      params,
      apiFlexiPhleboDetailsCallBack,
    );
  }

  void apiFlexiPhleboDetailsCallBack(
    AssignedExternalResourceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedFlexPhleboList = response?.output ?? [];
    apiGroup.leave();
  }

  void getFlexiDoctorDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "147",
    };

    apiManager.getFlexiDoctorDetailsListAPI(
      params,
      apiFlexiDoctorDetailsCallBack,
    );
  }

  void apiFlexiDoctorDetailsCallBack(
    AssignedExternalResourceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedFlexDoctorsList = response?.output ?? [];
    apiGroup.leave();
  }

  void getMMUDoctorDetailsList() {
    Map<String, String> params = {
      "campid": selectedCampID?.campId?.toString() ?? "",
      "CampDate": fromDate,
      "DESGID": "141",
    };

    apiManager.getMMUDoctorDetailsListAPI(params, apiMMUDoctorDetailsCallBack);
  }

  void apiMMUDoctorDetailsCallBack(
    TeamsMMUDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    assignedMMUDoctorList = response?.output ?? [];
    apiGroup.leave();
  }

  void assignedTeamDelete(String member1, String teamId) {
    ToastManager().showConfirmationDialog(
      context: context,
      message: "Do you really want to remove $member1 Team?",
      didSelectYes: (isYes) {
        if (isYes) {
          Navigator.pop(context);

          debugPrint("User pressed YES");
          removeTeamMapping(teamId);
        } else {
          Navigator.pop(context);

          debugPrint("User pressed NO");
        }
      },
    );
  }

  void removeTeamMapping(String teamNumber) {
    Map<String, String> params = {
      "campid": selectedCampID?.campId.toString() ?? "0",
      "RemovedBy": empCode.toString(),
      "Userid": "0",
      "TeamId": teamNumber,
      "IsTeam": "1",
    };

    apiManager.removeTeamMappingAPI(params, apiRemoveTeamMappingCallBack);
  }

  void apiRemoveTeamMappingCallBack(
    TeamCampDetailsListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      if (type == 1) {
        ToastManager.toast("Team removed successfully");
        getAllDetails();
      } else if (type == 2) {
        ToastManager.toast("Doctor removed successfully");
        getAllDetails();
      } else if (type == 3) {
        ToastManager.toast("Data entry operator removed successfully");
        getAllDetails();
      } else if (type == 4) {
        ToastManager.toast("Flexi phlebo removed successfully");
        getAllDetails();
      } else if (type == 5) {
        ToastManager.toast("Flexi Doctor operator removed successfully");
        getAllDetails();
      } else if (type == 6) {
        ToastManager.toast("MMU Doctor operator removed successfully");
        getAllDetails();
      } else {
        ToastManager.toast(
          "This team can not be removed as beneficiaries are registered by team member",
        );
      }
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void selectTeam() {
    List<AssignTypeModel> list = [AssignTypeModel(typeId: 1, typeName: "Team")];
    _showDropDownBottomSheet("Select Team", list, DropDownTypeMenu.SelectTeam);
  }

  void getTeamsList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "LabCode": selectedLab?.labCode?.toString() ?? "0",
      "CampDate": fromDate,
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
    };

    apiManager.getTeamsCampTypeWiseListAPI(
      params,
      apiTeamsCampTypeWiseCallBack,
    );
  }

  void apiTeamsCampTypeWiseCallBack(
    TeamsCampTypeWiseResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    teamList = response?.output ?? [];
    showAppointmentTeamBottomSheet();
  }

  void getPhleboList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "LabCode": selectedLab?.labCode?.toString() ?? "0",
      "CampDate": fromDate,
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
    };

    apiManager.getPhleboListAPI(params, apiPhleboListCallBack);
  }

  void apiPhleboListCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    phleboList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", phleboList, 0);
  }

  void getDeOpList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DesgId": selectedAssignResources?.desgId.toString() ?? "0",
      "LabCode": selectedLab?.labCode.toString() ?? "0",
      "CampDate": fromDate,
    };
    apiManager.getDeOpListAPI(params, apiDeOpCallBack);
  }

  void apiDeOpCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    deList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", deList, 1);
  }

  void getDoctorList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DesgId": selectedAssignResources?.desgId.toString() ?? "0",
      "LabCode": selectedLab?.labCode.toString() ?? "0",
      "CampDate": fromDate,
    };
    apiManager.getDeOpListAPI(params, apiDoctorCallBack);
  }

  void apiDoctorCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    doctorList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", doctorList, 2);
  }

  void getFlexiPhleboList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DesgId": selectedAssignResources?.desgId.toString() ?? "0",
      "LabCode": selectedLab?.labCode.toString() ?? "0",
      "CampDate": fromDate,
    };
    apiManager.getDeOpListAPI(params, apiFlexiPhleboCallBack);
  }

  void apiFlexiPhleboCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    doctorList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", doctorList, 2);
  }

  void getFlexiDoctorList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DesgId": selectedAssignResources?.desgId.toString() ?? "0",
      "LabCode": "0",
      "CampDate": fromDate,
    };
    apiManager.getDeOpListAPI(params, apiFlexiDoctorCallBack);
  }

  void apiFlexiDoctorCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    doctorList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", doctorList, 2);
  }

  void getMMUDoctorList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "DesgId": selectedAssignResources?.desgId.toString() ?? "0",
      "LabCode": "0",
      "CampDate": fromDate,
    };
    apiManager.getMMUDoctorListAPI(params, apiMMUDoctorCallBack);
  }

  void apiMMUDoctorCallBack(
    TeamsDoctorListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    doctorList = response?.output ?? [];
    showSelectPhleboBottomSheet("Doctor", doctorList, 2);
  }

  void showAppointmentTeamBottomSheet() {
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
          child: TeamsCampTypeWiseTeamView(
            list: teamList,
            onTapTeam: (p0) {
              refreshFlag = 1;
              submitData();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void showSelectTeamBottomSheet() {
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
          child: TeamsDetailsListForAssignView(
            list: teamsList,
            onTapTeam: (p0) {
              selectedTeams = p0;
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void showSelectPhleboBottomSheet(
    String titleString,
    List<TeamsDoctorListOutput> list,
    int index,
  ) {
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
          child: TeamDetailsListForAssignView(
            titleString: titleString,
            list: list,
            onTapTeam: (p0) {
              if (index == 0) {
                phleboList = p0;
              } else if (index == 1) {
                deList = p0;
              } else {
                doctorList = p0;
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

  void getAssignResources() {
    if (selectedTeam == null) {
      ToastManager.toast("Please select team first");
      return;
    }
    ToastManager.showLoader();
    Map<String, String> params = {
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
    };

    apiManager.getAssignResourcesAPI(params, apiAssignResourcesCallBack);
  }

  void apiAssignResourcesCallBack(
    AssignResourcesResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    assignResourcesList = response?.output ?? [];
    if (success) {
      _showDropDownBottomSheet(
        "Assign Resources",
        assignResourcesList,
        DropDownTypeMenu.AssignResources,
      );
    }
  }

  void getTeamDetailsListForAssign() {
    if (selectedCampID == null) {
      ToastManager.toast("Please select Camp ID");
      return;
    }
    ToastManager.showLoader();
    Map<String, String> params = {
      "campid": selectedCampID?.campId.toString() ?? "0",
      "CampDate": fromDate,
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
    };

    apiManager.gtTeamDetailsListForAssignAPI(
      params,
      apiTeamDetailsListForAssignCallBack,
    );
  }

  void apiTeamDetailsListForAssignCallBack(
    TeamDetailsListForAssignResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    teamsList = response?.output ?? [];

    if (teamsList.isEmpty) {
      ToastManager.toast(
        "Team not assigned for selected campID, assign team first",
      );
    } else {
      showSelectTeamBottomSheet();
    }
  }

  void submitData() {
    if (fromDate.isEmpty) {
      ToastManager.toast("Please Select Date");
      return;
    }
    if (selectedCampType == null) {
      ToastManager.toast("Please Select CampType");
      return;
    }
    if (selectedDistrict == null) {
      ToastManager.toast("Please Select District");
      return;
    }
    if (selectedLab == null) {
      ToastManager.toast("Please Select Lab");
      return;
    }
    if (selectedCampID == null) {
      ToastManager.toast("Please Select Camp");
      return;
    }
    int teamListCount = teamList.length;
    int assignedPhleboListCount = assignedPhleboList.length;
    int doctorListCount = doctorList.length;

    if (teamListCount == 0 &&
        assignedPhleboListCount == 0 &&
        selectedAssignResources == null &&
        doctorListCount == 0) {
      ToastManager.toast("Please Select Resource for Camp");
      return;
    }

    String teamNumber = getTeamsStringDictionary();
    insertTeamCampMapping(teamNumber);
  }

  String getTeamsStringDictionary() {
    List<Map<String, dynamic>> teamUserJsonArray = [];
    if (teamList.isNotEmpty) {
      for (var obj in teamList) {
        if (obj.selected) {
          final jsonObject1 = {
            "teamId": obj.teamid.toString(),
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.memberUserID1.toString(),
            "IsTeam": "1",
          };
          teamUserJsonArray.add(jsonObject1);

          final jsonObject2 = {
            "teamId": obj.teamid.toString(),
            "campId": selectedCampID?.campCreatedBy.toString(),
            "userId": obj.memberUserID2.toString(),
            "IsTeam": "1",
          };
          teamUserJsonArray.add(jsonObject2);
        }
      }
    }

    if (phleboList.isNotEmpty) {
      for (TeamsDoctorListOutput obj in phleboList) {
        if (obj.selected) {
          final jsonObject1 = {
            "teamId": selectedTeams?.teamNumber ?? "0",
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.uSERID.toString(),
            "IsTeam": "0",
          };
          teamUserJsonArray.add(jsonObject1);
        }
      }
    }

    if (assignedFlexPhleboList.isNotEmpty) {
      for (var obj in assignedFlexPhleboList) {
        if (obj.isSelected) {
          final jsonObject1 = {
            "teamId": selectedTeams?.teamNumber ?? "0",
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.userID.toString(),
            "IsTeam": "0",
          };
          teamUserJsonArray.add(jsonObject1);
        }
      }
    }

    if (deList.isNotEmpty) {
      for (TeamsDoctorListOutput obj in deList) {
        if (obj.selected) {
          final jsonObject1 = {
            "teamId": selectedTeams?.teamNumber ?? "0",
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.uSERID.toString(),
            "IsTeam": "0",
          };
          teamUserJsonArray.add(jsonObject1);
        }
      }
    }

    if (doctorList.isNotEmpty) {
      for (TeamsDoctorListOutput obj in doctorList) {
        if (obj.selected) {
          final jsonObject1 = {
            "teamId": selectedTeams?.teamNumber ?? "0",
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.uSERID.toString(),
            "IsTeam": "0",
          };
          teamUserJsonArray.add(jsonObject1);
        }
      }
    }

    if (assignedFlexDoctorsList.isNotEmpty) {
      for (AssignedExternalResourceDetailsOutput obj
          in assignedFlexDoctorsList) {
        if (obj.isSelected) {
          final jsonObject1 = {
            "teamId": selectedTeams?.teamNumber ?? "0",
            "campId": selectedCampID?.campId.toString(),
            "userId": obj.userID.toString(),
            "IsTeam": "0",
          };
          teamUserJsonArray.add(jsonObject1);
        }
      }
    }
    return jsonEncode(teamUserJsonArray);
  }

  void insertTeamCampMapping(String teamNumber) {
    Map<String, String> params = {
      "CampID": selectedCampID?.campId.toString() ?? "0",
      "UserID": empCode.toString(),
      "TeamNumber": teamNumber,
      "CreatedBy": empCode.toString(),
    };

    apiManager.insertTeamCampMappingAPI(
      params,
      apiInsertTeamCampMappingCallBack,
    );
  }

  void apiInsertTeamCampMappingCallBack(
    AssignResourcesResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    // assignResourcesList = response?.output ?? [];

    if (success) {
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message: "Teams Assign successfully",
        onTap: () {
          Navigator.pop(context);
          assignedPhleboList = [];
          teamList = [];
          assignedDataEntryOperatorList = [];
          doctorList = [];
          if (refreshFlag == 1) {
            apiGroup.reset();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            apiGroup.enter();
            getPhleboDetailsList();
            getDeopDetailsList();
            getDoctorDetailsList();
            getFlexiPhleboDetailsList();
            getFlexiDoctorDetailsList();
            getMMUDoctorDetailsList();
            getTeamsList();
            getTeamDetailsList();
            apiGroup.notify(() {
              apiGroup.reset();
              debugPrint("All APIs Completed!");
              ToastManager.hideLoader();
              setState(() {});
            });
          } else {
            getAllDetails();
          }
        },
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Team Camp Mapping",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                color: kWhiteColor,
                child: Column(
                  children: [
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            readOnly: true,
                            controller: TextEditingController(text: fromDate),
                            onTap: () {
                              _selectFromDate(context);
                            },
                            hint: 'From Date*',
                            label: CommonText(
                              text: 'From Date*',
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
                                  icCalendarMonth,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                          ),
                          // AppDateTextfield(
                          //   icon: icCalendarMonth,
                          //   titleHeaderString: "From Date*",
                          //   valueString: fromDate,
                          //   onTap: () {
                          //     _selectFromDate(context);
                          //   },
                          // ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            readOnly: true,
                            controller: TextEditingController(
                              text: selectedCampType?.campTypeDescription ?? "",
                            ),
                            onTap: () {
                              getCampType();
                            },
                            hint: 'Camp Type*',
                            label: CommonText(
                              text: 'Camp Type*',
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
                                  icnTent,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                            suffixIcon: Icon(
                              Icons.keyboard_arrow_down_outlined,
                            ),
                          ),
                          // AppDropdownTextfield(
                          //   icon: icnTent,
                          //   titleHeaderString: "Camp Type*",
                          //   valueString:
                          //       selectedCampType?.campTypeDescription ?? "",
                          //   onTap: () {
                          //     getCampType();
                          //   },
                          // ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            readOnly: true,
                            controller: TextEditingController(
                              text: selectedDistrict?.dISTNAME ?? "",
                            ),
                            onTap: () {
                              getAllDistrictList();
                            },
                            hint: 'District*',
                            label: CommonText(
                              text: 'District*',
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
                                  icMapPin,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                            suffixIcon: Icon(
                              Icons.keyboard_arrow_down_outlined,
                            ),
                          ),
                          // AppDropdownTextfield(
                          //                   icon: icMapPin,
                          //                   titleHeaderString: "District*",
                          //                   valueString: selectedDistrict?.dISTNAME ?? "",
                          //                   onTap: () {
                          //                     getAllDistrictList();
                          //                   },
                          //                 ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            readOnly: true,
                            controller: TextEditingController(
                              text: selectedLab?.labName ?? "",
                            ),
                            onTap: () {
                              getLabList();
                            },
                            hint: 'Lab*',
                            label: CommonText(
                              text: 'Lab*',
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
                                  icLandingLab,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                            suffixIcon: Icon(
                              Icons.keyboard_arrow_down_outlined,
                            ),
                          ),

                          // AppDropdownTextfield(
                          //   icon: icLandingLab,
                          //   titleHeaderString: "Lab*",
                          //   valueString: selectedLab?.labName ?? "",
                          //   onTap: () {
                          //     getLabList();
                          //   },
                          // ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text: selectedCampID?.campId.toString() ?? "",
                      ),
                      onTap: () {
                        getCampList();
                      },
                      hint: 'Camp ID*',
                      label: CommonText(
                        text: 'Camp ID*',
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
                            icHashIcon,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                      suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                    ),

                    // AppDropdownTextfield(
                    //   icon: icHashIcon,
                    //   titleHeaderString: "Camp ID*",
                    //   valueString: selectedCampID?.campId.toString() ?? "",
                    //   onTap: () {
                    //     getCampList();
                    //   },
                    // ),
                    showTeamDropDown == true
                        ? const SizedBox(height: 8)
                        : Container(),

                    showTeamDropDown == true
                        ? AppTextField(
                          readOnly: true,
                          controller: TextEditingController(
                            text: selectedTeam?.typeName ?? "",
                          ),
                          onTap: () {
                            selectTeam();
                          },
                          hint: 'Select Team*',
                          label: CommonText(
                            text: 'Select Team*',
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
                                icUsersGroup,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                          suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                        )
                        // AppDropdownTextfield(
                        //       icon: icUsersGroup,
                        //       titleHeaderString: "Select Team*",
                        //       valueString: selectedTeam?.typeName ?? "",
                        //       onTap: () {
                        //         selectTeam();
                        //       },
                        //     )
                        : Container(),
                    showTeamDropDown == true
                        ? const SizedBox(height: 8)
                        : Container(),
                    showTeamDropDown == true
                        ? AppTextField(
                          readOnly: true,
                          controller: TextEditingController(
                            text: selectedTeams?.teamName ?? "",
                          ),
                          onTap: () {
                            getTeamDetailsListForAssign();
                          },
                          hint: 'Select Team*',
                          label: CommonText(
                            text: 'Select Team*',
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
                                icUsersGroup,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                          suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                        )
                        // AppDropdownTextfield(
                        //       icon: icUsersGroup,
                        //       titleHeaderString: "Select Teams*",
                        //       valueString: selectedTeams?.teamName ?? "",
                        //       onTap: () {
                        //         getTeamDetailsListForAssign();
                        //       },
                        //     )
                        : Container(),
                    showTeamDropDown == true
                        ? const SizedBox(height: 8)
                        : Container(),
                    showTeamDropDown == true
                        ? AppTextField(
                          readOnly: true,
                          controller: TextEditingController(
                            text: selectedAssignResources?.desgName ?? "",
                          ),
                          onTap: () {
                            getAssignResources();
                          },
                          hint: 'Assign Resources*',
                          label: CommonText(
                            text: 'Assign Resources*',
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
                                icUsersGroup,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                          suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                        )
                        // AppDropdownTextfield(
                        //       icon: icUsersGroup,
                        //       titleHeaderString: "Assign Resources*",
                        //       valueString: selectedAssignResources?.desgName ?? "",
                        //       onTap: () {
                        //         getAssignResources();
                        //       },
                        //     )
                        : Container(),
                  ],
                ),
              ),
              const SizedBox(height: 20),
              SizedBox(
                height: 40,
                child: Center(
                  child: SizedBox(
                    width: 120,
                    child: AppActiveButton(
                      buttontitle: "Assign",
                      isCancel: assignButtonDisabled,
                      onTap: () {
                        if (!assignButtonDisabled) {
                          if (selectedTeams == null) {
                            ToastManager.toast(
                              "Please first select Team before adding resources",
                            );
                            return;
                          }

                          if (doctorList.isNotEmpty) {
                            int doctorSelected = 0;

                            for (TeamsDoctorListOutput doctorObj
                                in doctorList) {
                              if (doctorObj.selected) {
                                doctorSelected += 1;
                              }
                            }
                            if (doctorSelected == 0) {
                              ToastManager.toast("Please select Doctor");
                              return;
                            }
                          } else {
                            ToastManager.toast("Please select Doctor");
                            return;
                          }
                          refreshFlag = 0;
                          submitData();
                        }
                      },
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 8),
              AssignedTeamsView(
                assignedTeamsList: assignedTeamsList,
                deleteDidPressed: (deleteItem) {
                  type = 0;
                  assignedTeamDelete(
                    deleteItem.member1 ?? "",
                    deleteItem.teamNumber ?? "",
                  );
                },
              ),
              AssignedDoctorsView(
                list: assignedDoctorsList,
                deleteDidPressed: (deleteItem) {
                  type = 2;
                  assignedTeamDelete(deleteItem.memberName ?? "", "0");
                },
              ),
              AssignedFlexiDoctorsView(
                list: assignedFlexDoctorsList,
                deleteDidPressed: (deleteItem) {
                  type = 5;
                  assignedTeamDelete(deleteItem.memberName ?? "", "0");
                },
              ),
              MMUDoctorView(
                list: assignedMMUDoctorList,
                deleteDidPressed: (deleteItem) {
                  type = 6;
                  assignedTeamDelete(deleteItem.memberName ?? "", "0");
                },
              ),
            ],
          ).paddingOnly(left: 10, right: 10, top: 16),
        ),
      ),
    );
  }
}

// class TeamCampMappingScreen extends StatefulWidget {
//   const TeamCampMappingScreen({super.key});

//   @override
//   State<TeamCampMappingScreen> createState() => _TeamCampMappingScreenState();
// }

// class _TeamCampMappingScreenState extends State<TeamCampMappingScreen> {
//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return Scaffold(
//       appBar: mAppBar(
//         scTitle: "Team Camp Mapping",
//         leadingIcon: iconBackArrow,
//         onLeadingIconClick: () {
//           Navigator.pop(context);
//         },
//       ),
//       body: KeyboardDismissOnTap(
//         dismissOnCapturedTaps: true,
//         child: SizedBox(
//           height: SizeConfig.screenHeight,
//           width: SizeConfig.screenWidth,
//           child: Stack(
//             children: [
//               Positioned(
//                 top: 74,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect4,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 53,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect3,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 30,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect2,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Image.asset(
//                 fit: BoxFit.fill,
//                 rect1,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//               Positioned(
//                 top: 0,
//                 bottom: 8,
//                 left: 8,
//                 right: 8,
//                 child: SingleChildScrollView(
//                   child: Column(
//                     mainAxisAlignment: MainAxisAlignment.start,
//                     crossAxisAlignment: CrossAxisAlignment.start,
//                     children: [
//                       Container(
//                         decoration: BoxDecoration(
//                           boxShadow: const [
//                             BoxShadow(
//                               offset: Offset(0, 0.5),
//                               color: kBlackColor,
//                               spreadRadius: 0,
//                               blurRadius: 4,
//                             ),
//                           ],
//                           color: kWhiteColor,
//                           borderRadius: BorderRadius.circular(10),
//                         ),
//                         padding: const EdgeInsets.all(8.0),
//                         child: Column(
//                           children: [
//                             Row(
//                               children: [
//                                 Expanded(
//                                   child: AppDateTextfield(
//                                     icon: icCalendarMonth,
//                                     titleHeaderString: "From Date*",
//                                     valueString: "",
//                                     onTap: () {},
//                                   ),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: AppDropdownTextfield(
//                                     icon: icnTent,
//                                     titleHeaderString: "Camp Type*",
//                                     valueString: "",
//                                     onTap: () {},
//                                   ),
//                                 ),
//                               ],
//                             ),
//                             const SizedBox(height: 8),
//                             Row(
//                               children: [
//                                 Expanded(
//                                   child: AppDropdownTextfield(
//                                     icon: icMapPin,
//                                     titleHeaderString: "District*",
//                                     valueString: "",
//                                     onTap: () {},
//                                   ),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: AppDropdownTextfield(
//                                     icon: icLandingLab,
//                                     titleHeaderString: "Lab*",
//                                     valueString: "",
//                                     onTap: () {},
//                                   ),
//                                 ),
//                               ],
//                             ),
//                             const SizedBox(height: 8),
//                             AppDropdownTextfield(
//                               icon: icHashIcon,
//                               titleHeaderString: "Camp ID*",
//                               valueString: "",
//                               onTap: () {},
//                             ),
//                             const SizedBox(height: 8),
//                             AppDropdownTextfield(
//                               icon: icHashIcon,
//                               titleHeaderString: "Team*",
//                               valueString: "",
//                               onTap: () {},
//                             ),
//                             const SizedBox(height: 8),
//                             AppDropdownTextfield(
//                               icon: icHashIcon,
//                               titleHeaderString: "Assign Resources*",
//                               valueString: "",
//                               onTap: () {},
//                             ),
//                           ],
//                         ),
//                       ),
//                       const SizedBox(height: 20),
//                       SizedBox(
//                         height: 40,
//                         child: Center(
//                           child: SizedBox(
//                             width: 120,
//                             child: AppActiveButton(
//                               buttontitle: "Assign",
//                               onTap: () {},
//                             ),
//                           ),
//                         ),
//                       ),
//                       const SizedBox(height: 8),
//                       AssignedTeamsView(),
//                       const SizedBox(height: 8),
//                       AssignedDoctorsView(),
//                     ],
//                   ),
//                 ),
//               ),
//             ],
//           ),
//         ),
//       ),
//     );
//   }
// }
