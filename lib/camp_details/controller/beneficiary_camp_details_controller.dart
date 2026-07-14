// ignore_for_file: file_names

import 'package:get/get.dart';
import 'package:s2toperational/camp_details/model/team_details_list_response.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../model/beneficiary_worker_response.dart';
import '../repository/camp_details_repository.dart';

class BeneficiaryCampDetailsController extends GetxController {
  BeneficiaryCampDetailsController({
    required this.campId,
    required this.cAMPTYPE,
    required this.campTypeDescription,
  });

  final int campId;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  final _repo = CampDetailsRepository();

  int dESGID = 0;
  int empCode = 0;
  String teamNumber = '0';
  String teamName = 'All';
  bool isShowTeamDropDown = false;
  bool isLoading = false;
  List<BeneficiaryWorkerOutput> beneficiaryWorkerList = [];

  static const _teamDropDownDesgs = {92, 29, 160, 104, 162, 78, 77, 128, 30, 108, 84, 139, 136};
  static const _teamIdFetchDesgs = {35, 64, 86, 146, 129, 138, 137, 169, 31, 176, 177};

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      isShowTeamDropDown = false;
      fetchBeneficiaryList();
    } else {
      isShowTeamDropDown = _teamDropDownDesgs.contains(dESGID);
      if (_teamIdFetchDesgs.contains(dESGID)) {
        fetchTeamId();
      } else {
        fetchBeneficiaryList();
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
        teamNumber = first.teamNumber ?? '0';
        teamName = first.teamName ?? '';
        await fetchBeneficiaryList();
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

  Future<void> fetchBeneficiaryList() async {
    isLoading = true;
    update();
    try {
      final response = await _repo.fetchBeneficiaryList({
        'CampId': campId.toString(),
        'TeamID': teamNumber,
      });
      beneficiaryWorkerList = response.output ?? [];
    } catch (_) {
      beneficiaryWorkerList = [];
      ToastManager.toast('Worker List Not Found');
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
    teamNumber = team.teamID ?? '';
    teamName = team.teamNumber ?? '';
    fetchBeneficiaryList();
  }
}
