// ignore_for_file: file_names

import 'package:get/get.dart';
import 'package:s2toperational/Screens/camp_calendar/model/bind_district_response.dart';
import 'package:s2toperational/Screens/d2d_team/model/lab_by_user_id_response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';

import '../model/d2d_non_working_teams_response.dart';
import '../model/d2d_team_member_details_response.dart';
import '../model/d2d_teams_count_response.dart';
import '../repository/d2d_team_repository.dart';

class D2DTeamController extends GetxController {
  final D2DTeamRepository _repository = D2DTeamRepository();

  // â”€â”€â”€ User info â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  int empCode = 0;
  int dISTLGDCODE = 0;
  int dESGID = 0;
  int subOrgId = 0;

  // â”€â”€â”€ Filter params â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  String camptypeId = "0";
  String divisioId = "0";
  String labId = "0";

  BindDistrictOutput? selectedDistrict;
  LabByUserIDOutput? selectedLab;

  // â”€â”€â”€ UI state â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  String naviTitleString = "D2D Not Working Team";
  int workingTeamsCount = 0;
  int notWorkingTeamsCount = 0;
  int totalTeamsCount = 0;

  List<D2DNonWorkingTeamsOutput> teamList = [];
  List<D2DTeamMemberDetailsOutput> callingList = [];

  // â”€â”€â”€ Lifecycle â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    dESGID = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    groupAPICall();
  }

  // â”€â”€â”€ Params builder â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Map<String, String> get _filterParams => {
    "GLOUSERID": empCode.toString(),
    "CampType": camptypeId,
    "DivId": divisioId,
    "DISTLGDCODE": dISTLGDCODE.toString(),
    "LabCode": labId,
    "DesgId": dESGID.toString(),
    "SubOrgId": subOrgId.toString(),
  };

  // â”€â”€â”€ Group API call (count + not-working list in parallel) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> groupAPICall() async {
    ToastManager.showLoader();
    final results = await Future.wait([
      _repository.getActiveInactiveD2DTeamsCount(_filterParams),
      _repository.getNotWorkingTeams(_filterParams),
    ]);
    ToastManager.hideLoader();

    final countResponse = results[0] as D2DTeamsCountResponse?;
    final teamsResponse = results[1] as D2DNonWorkingTeamsResponse?;

    if (countResponse != null) {
      workingTeamsCount = countResponse.output?.first.workingTeamCount ?? 0;
      notWorkingTeamsCount = countResponse.output?.first.nonWorkingTeamCount ?? 0;
      totalTeamsCount = countResponse.output?.first.totalTeamCount ?? 0;
    } else {
      ToastManager.toast("Failed to load team counts");
    }

    teamList = teamsResponse?.output ?? [];
    if (teamsResponse == null) ToastManager.toast("Failed to load teams");

    naviTitleString = "D2D Not Working Team";
    update();
  }

  // â”€â”€â”€ Working teams â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> loadWorkingTeams() async {
    ToastManager.showLoader();
    final response = await _repository.getWorkingTeams(_filterParams);
    ToastManager.hideLoader();
    teamList = response?.output ?? [];
    if (response == null) ToastManager.toast("Failed to load working teams");
    naviTitleString = "D2D Working Team";
    update();
  }

  // â”€â”€â”€ Calling popup data â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<bool> loadTeamsCalling(int teamId) async {
    ToastManager.showLoader();
    final response = await _repository.getTeamsCalling({"Teamid": teamId.toString()});
    ToastManager.hideLoader();
    if (response != null) {
      callingList = response.output ?? [];
      update();
      return true;
    } else {
      ToastManager.toast("Failed to load calling details");
      return false;
    }
  }

  // â”€â”€â”€ Filter: fetch district list â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<List<BindDistrictOutput>?> fetchDistrictList() async {
    ToastManager.showLoader();
    try {
      final params = {
        "SubOrgId": "0",
        "UserID": empCode.toString(),
        "DESGID": dESGID.toString(),
        "DIVID": "0",
        "DISTLGDCODE": dISTLGDCODE.toString(),
      };
      final response = await _repository.getBindDistrict(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  // â”€â”€â”€ Filter: fetch lab list â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<List<LabByUserIDOutput>?> fetchLabList() async {
    if (selectedDistrict == null) {
      ToastManager.toast("Please select district");
      return null;
    }
    ToastManager.showLoader();
    try {
      final params = {
        "DISTLGDCODE": selectedDistrict?.dISTLGDCODE?.toString() ?? "0",
        "USERID": empCode.toString(),
      };
      final response = await _repository.getLabByUserId(params);
      return response?.output;
    } finally {
      ToastManager.hideLoader();
    }
  }

  // â”€â”€â”€ Filter: selection setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  void setSelectedDistrict(BindDistrictOutput? val) {
    selectedDistrict = val;
    selectedLab = null;
    dISTLGDCODE = val?.dISTLGDCODE ?? 0;
    labId = "0";
    update();
  }

  void setSelectedLab(LabByUserIDOutput? val) {
    selectedLab = val;
    labId = val?.labCode?.toString() ?? "0";
    update();
  }

  void applyFilter() {
    groupAPICall();
  }
}
