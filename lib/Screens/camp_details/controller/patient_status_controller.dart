// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/camp_details/model/team_details_list_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import '../model/patient_status_details_list_response.dart';
import '../repository/camp_details_repository.dart';

class PatientStatusController extends GetxController {
  PatientStatusController({
    required this.campId,
    required this.dISTLGDCODE,
    required this.cAMPTYPE,
    required this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  final _repo = CampDetailsRepository();

  int dESGID = 0;
  int empCode = 0;
  String teamId = '0';
  String teamName = 'All';
  bool isShowTeamDropDown = false;
  bool isLoading = false;

  List<PatientStatusDetailsOutput> patientStatusDetailsList = [];
  List<PatientStatusDetailsOutput> searchPatientStatusDetailsList = [];
  final TextEditingController searchController = TextEditingController();

  static const _teamDropDownDesgs = {92, 29, 160, 104, 162, 78, 77, 128, 30, 108, 84, 139, 136};
  static const _teamIdFetchDesgs = {35, 64, 86, 146, 129, 138, 137, 169, 31, 176, 177};

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (DataProvider().getRegularCamp()) {
      fetchPatientStatus();
    } else {
      isShowTeamDropDown = _teamDropDownDesgs.contains(dESGID);
      if (_teamIdFetchDesgs.contains(dESGID)) {
        fetchTeamId();
      } else {
        fetchPatientStatus();
      }
    }
  }

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
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
        teamId = first.teamNumber ?? '0';
        teamName = first.teamName ?? '';
        await fetchPatientStatus();
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

  Future<void> fetchPatientStatus() async {
    isLoading = true;
    update();
    try {
      final response = await _repo.fetchPatientStatus({
        'CampId': campId.toString(),
        'DISTLGDCODE': dISTLGDCODE == 0 ? '0' : dISTLGDCODE.toString(),
        'TeamId': teamId,
      });
      patientStatusDetailsList = response.output ?? [];
      searchPatientStatusDetailsList = patientStatusDetailsList;
    } catch (e) {
      patientStatusDetailsList = [];
      searchPatientStatusDetailsList = [];
      ToastManager.toast(e.toString());
    }
    isLoading = false;
    update();
  }

  void searchPatients(String query) {
    if (query.isEmpty) {
      searchPatientStatusDetailsList = patientStatusDetailsList;
    } else {
      searchPatientStatusDetailsList = patientStatusDetailsList.where((item) {
        final regdNo = item.regdNo?.toString().toLowerCase() ?? '';
        return regdNo.contains(query.toLowerCase());
      }).toList();
    }
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
    teamId = team.teamID ?? '';
    teamName = team.teamNumber ?? '';
    fetchPatientStatus();
  }

  String checkPatientStatus(String status) => status == '0' ? icCrossIcon : icCheckIcon;
}
