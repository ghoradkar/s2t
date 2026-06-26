// ignore_for_file: file_names

import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/camp_details_response.dart';
import '../repository/camp_details_repository.dart';

class ScreeningTestCampDetailsController extends GetxController {
  ScreeningTestCampDetailsController({
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.isHealthScreeing,
    this.cAMPTYPE,
    this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final String campDate;
  final String surveyCoordinatorName;
  final String dISTNAME;
  final String mOBNO;
  final bool isHealthScreeing;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  final _repo = CampDetailsRepository();

  int dESGID = 0;
  int empCode = 0;
  String teamNumber = 'All';
  String teamID = '0';
  bool isShowTeamDropDown = false;
  bool isLoading = false;
  CampDetailOutput? campDetailOutput;

  static const _teamDropDownDesgs = {92, 29, 160, 104, 162, 78, 77, 128, 30, 108, 84, 139, 136};
  static const _teamIdFetchDesgs = {35, 64, 86, 146, 129, 138, 137, 169, 31, 176, 177};

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      fetchCampDetailsCount();
    } else {
      isShowTeamDropDown = _teamDropDownDesgs.contains(dESGID);
      if (_teamIdFetchDesgs.contains(dESGID)) {
        fetchTeamId();
      } else {
        fetchCampDetailsCount();
      }
    }
  }

  Future<void> fetchTeamId() async {
    isLoading = true;
    update();
    try {
      final response = await _repo.fetchTeamId({
        'campid': campId.toString(),
        'UserID': empCode.toString(),
      });
      final first = response.output?.first;
      if (first != null) {
        teamID = first.teamNumber ?? '0';
        teamNumber = first.teamName ?? '';
        await fetchCampDetailsCount();
      } else {
        isLoading = false;
        ToastManager.toast('Data not found');
        update();
      }
    } catch (e) {
      isLoading = false;
      final msg = e.toString();
      ToastManager.toast(msg == 'Team Number Data not found' ? 'Your Selected Camp Not Mapped To You' : msg);
      update();
    }
  }

  Future<void> fetchCampDetailsCount() async {
    isLoading = true;
    update();
    final String url;
    final Map<String, String> params;
    if (DataProvider().getRegularCamp()) {
      url = '${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsCountRegularInCampTest}';
      params = {
        'CampId': campId.toString(),
        'DISTLGDCODE': dISTLGDCODE.toString(),
        'FromDate': campDate,
        'ToDate': campDate,
      };
    } else {
      url = '${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsCountInCampTest}';
      params = {
        'CampId': campId.toString(),
        'DISTLGDCODE': dISTLGDCODE.toString(),
        'FromDate': campDate,
        'ToDate': campDate,
        'TeamID': teamID,
      };
    }
    try {
      final response = await _repo.fetchCampDetailsCount(url, params);
      campDetailOutput = response.output?.first;
    } catch (e) {
      campDetailOutput = null;
      ToastManager.toast(e.toString());
    }
    isLoading = false;
    update();
  }

  Future<List<TeamDetailsOutput>> fetchCampWiseTeams() async {
    try {
      final response = await _repo.fetchCampWiseTeams({'CampId': campId.toString()});
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    }
  }

  void selectTeam(TeamDetailsOutput team) {
    teamID = team.teamID ?? '';
    teamNumber = team.teamNumber ?? '';
    fetchCampDetailsCount();
  }
}
