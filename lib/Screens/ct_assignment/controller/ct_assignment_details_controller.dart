// ignore_for_file: file_names

import 'dart:convert';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/T2TCTUserDetailsResponse/T2TCTUserDetailsResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/beneficiary_details_for_assign_teamid_details_response.dart';
import '../model/selected_teams_data_list_response.dart';
import '../model/t2t_ct_beneficiary_details_response.dart';
import '../repository/ct_assignment_repository.dart';

class CTAssignmentDetailsController extends GetxController {
  CTAssignmentDetailsController({required this.selectedCT});

  final T2TCTBeneficiaryDetailsOutput selectedCT;
  final _repo = CTAssignmentRepository();

  bool isSelectedAssignD2DTeam = false;
  bool isShowAllTeams = false;
  bool isShowAssignButton = true;
  bool isShowTeamDropDown = false;
  bool isShowExecutiveDropDown = true;
  bool isUserAlreadyAssigned = false;

  int empCode = 0;
  BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
  SelectedTeamsDataLisOutput? selectedTeam;
  T2TCTUserDetailsOutput? selectedExecutive;

  @override
  void onInit() {
    super.onInit();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (selectedCT.sampleCollection == 'Yes') {
      isShowAssignButton = false;
      isShowTeamDropDown = false;
      isShowExecutiveDropDown = false;
      isShowAllTeams = false;
      ToastManager.toast('Sample collection done for this beneficiary');
    }

    if (selectedCT.arId == 4) {
      ToastManager.toast(
        'You are not allowed to change the status as the beneficiary is not interested in CT.',
      );
      isShowAssignButton = false;
    }

    getDependentInfo();
  }

  Future<void> getDependentInfo() async {
    ToastManager.showLoader();
    try {
      final params = {
        'T2T_Order_Id': selectedCT.t2tOrderId.toString(),
        'Regdno': selectedCT.regdno ?? '0',
      };
      final response = await _repo.fetchDependentInfo(params);
      beneficiaryDetails = response.output?.first;
      isUserAlreadyAssigned =
          (beneficiaryDetails?.isTeamAssign ?? '').toLowerCase() == 'yes';

      selectedExecutive = T2TCTUserDetailsOutput(
        uSERID: beneficiaryDetails?.uSERID ?? 0,
        uSERMOBNO: beneficiaryDetails?.mOBNO,
        uSERNAME: beneficiaryDetails?.uSERNAME,
      );
      selectedTeam = SelectedTeamsDataLisOutput(
        labCode: beneficiaryDetails?.landingLab,
        labName: beneficiaryDetails?.landingLabName,
        member1: beneficiaryDetails?.member1,
        member1MOB: beneficiaryDetails?.member1MOB,
        member2: beneficiaryDetails?.member1,
        member2MOB: beneficiaryDetails?.member1MOB,
        memberUserID1: beneficiaryDetails?.memberUserID1,
        memberUserID2: beneficiaryDetails?.memberUserID2,
        teamID: beneficiaryDetails?.teamid,
        teamid: beneficiaryDetails?.teamid,
        teamName: beneficiaryDetails?.teamname,
        teamNumber: beneficiaryDetails?.teamname,
      );
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<List<SelectedTeamsDataLisOutput>> fetchTeamsForDropdown() async {
    final pincode = isShowAllTeams ? '0' : (beneficiaryDetails?.pinCode ?? '0');
    ToastManager.showLoader();
    try {
      final params = {'Pincode': pincode, 'USERID': empCode.toString()};
      final response = await _repo.fetchTeamsByPincode(params);
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<T2TCTUserDetailsOutput>> fetchExecutiveList() async {
    ToastManager.showLoader();
    try {
      final params = {
        'DISTLGDCODE': beneficiaryDetails?.dISTLGDCODE.toString() ?? '0',
        'Pincode': '0',
      };
      final response = await _repo.fetchExecutiveList(params);
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void selectTeam(SelectedTeamsDataLisOutput team) {
    selectedTeam = team;
    update();
  }

  void selectExecutive(T2TCTUserDetailsOutput executive) {
    selectedExecutive = executive;
    update();
  }

  void toggleAssignD2DTeam(bool value) {
    isSelectedAssignD2DTeam = value;
    isShowTeamDropDown = value;
    isShowExecutiveDropDown = !value;
    if (value) selectedTeam = null;
    update();
  }

  void toggleShowAllTeams(bool value) {
    isShowAllTeams = value;
    selectedTeam = null;
    update();
  }

  Future<bool> submitAssignment() async {
    List<Map<String, dynamic>> jsonArray = [];

    if (isSelectedAssignD2DTeam) {
      if (selectedTeam == null) {
        ToastManager.toast('Please select team');
        return false;
      }
      jsonArray.add({'USERID': (selectedTeam?.memberUserID1 ?? 0).toString(), 'Teamid': (selectedTeam?.teamID ?? 0).toString()});
      jsonArray.add({'USERID': (selectedTeam?.memberUserID2 ?? 0).toString(), 'Teamid': (selectedTeam?.teamID ?? 0).toString()});
    } else {
      if (selectedExecutive == null) {
        ToastManager.toast('Please Select Resource');
        return false;
      }
      jsonArray.add({'USERID': (selectedExecutive?.uSERID ?? 0).toString(), 'Teamid': '0'});
    }

    if (selectedCT.arId == 4) return false;

    ToastManager.showLoader();
    try {
      final params = {
        'Regdid': '0',
        'Campid': selectedCT.campId.toString(),
        'T2T_CT_TeamBene': jsonEncode(jsonArray),
        'AssignedBy': empCode.toString(),
        'T2T_Order_Id': selectedCT.t2tOrderId.toString(),
        'TreatmentID': selectedCT.regdid.toString(),
      };
      await _repo.submitTeamAssignment(params);
      return true;
    } catch (e) {
      ToastManager.toast(e.toString());
      return false;
    } finally {
      ToastManager.hideLoader();
    }
  }
}
