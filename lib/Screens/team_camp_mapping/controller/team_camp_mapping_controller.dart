// ignore_for_file: file_names, avoid_print

import 'dart:convert';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/models/assign_resources_response.dart';
import 'package:s2toperational/Screens/team_camp_mapping/model/assign_type_model.dart';
import 'package:s2toperational/Screens/camp_calendar/model/camp_list_v3_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/camp_type_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/district_response.dart';
import 'package:s2toperational/Screens/team_camp_mapping/model/team_camp_lab_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import '../model/assigned_external_resource_details_response.dart';
import '../model/team_camp_details_list_response.dart';
import '../model/team_details_list_for_assign_response.dart';
import '../model/teams_camp_type_wise_response.dart';
import '../model/teams_doctor_list_response.dart';
import '../model/teams_mmu_doctor_list_response.dart';
import '../repository/team_camp_mapping_repository.dart';

class TeamCampMappingController extends GetxController {
  final _repo = TeamCampMappingRepository();

  int dESGID = 0;
  int sTATELGDCODE = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;

  String fromDate = '';
  CampTypeOutput? selectedCampType;
  DistrictOutput? selectedDistrict;
  TeamCampLabOutput? selectedLab;
  CampListV3Output? selectedCampID;
  AssignTypeModel? selectedTeam;
  TeamDetailsListForAssignOutput? selectedTeams;

  List<AssignedExternalResourceDetailsOutput> assignedPhleboList = [];
  List<AssignedExternalResourceDetailsOutput> assignedDataEntryOperatorList = [];
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

  bool showTeamDropDown = true;
  bool assignButtonDisabled = false;
  bool shouldOpenTeamSheet = false;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    dESGID = user?.dESGID ?? 0;
    sTATELGDCODE = user?.sTATELGDCODE ?? 0;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    fromDate = FormatterManager.formatDateToString(DateTime.now());
  }

  void selectDate(DateTime picked) {
    fromDate = FormatterManager.formatDateToString(picked);
    final today = FormatterManager.formatDateToString(DateTime.now());
    if (fromDate == today) {
      showTeamDropDown = true;
      assignButtonDisabled = false;
    } else {
      showTeamDropDown = false;
      assignButtonDisabled = true;
    }
    resetAllFields();
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
    update();
  }

  void setCampType(CampTypeOutput value) {
    selectedCampType = value;
    selectedDistrict = null;
    selectedLab = null;
    update();
  }

  void setDistrict(DistrictOutput value) {
    selectedDistrict = value;
    selectedLab = null;
    update();
  }

  void setLab(TeamCampLabOutput value) {
    selectedLab = value;
    update();
  }

  Future<void> setCampID(CampListV3Output value) async {
    selectedCampID = value;
    update();
    await getAllDetails();
  }

  void setAssignedTeam(TeamDetailsListForAssignOutput value) {
    selectedTeams = value;
    update();
  }

  void setAssignResources(AssignResourcesOutput value) {
    selectedAssignResources = value;
    update();
  }

  void setPhleboList(List<TeamsDoctorListOutput> list) {
    phleboList = list;
    update();
  }

  void setDeList(List<TeamsDoctorListOutput> list) {
    deList = list;
    update();
  }

  void setDoctorList(List<TeamsDoctorListOutput> list) {
    doctorList = list;
    update();
  }

  Future<List<CampTypeOutput>> fetchCampType() async {
    ToastManager.showLoader();
    try {
      CampTypeResponse res;
      if (DataProvider().getRegularCamp()) {
        res = await _repo.fetchCampTypeNonD2D();
      } else if (dESGID == 108) {
        res = await _repo.fetchCampTypeFlexi();
      } else if (dESGID == 136 || dESGID == 139) {
        res = await _repo.fetchCampTypeMMU();
      } else {
        res = await _repo.fetchCampTypeD2D();
      }
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<DistrictOutput>> fetchDistrictList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchDistrictList({'STATELGDCODE': '2', 'USERID': '$empCode'});
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamCampLabOutput>> fetchLabList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchLabList({
        'DISTLGDCODE': selectedDistrict?.dISTLGDCODE.toString() ?? '0',
      });
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<CampListV3Output>> fetchCampList() async {
    if (fromDate.isEmpty) {
      ToastManager.toast('Select Camp Date');
      return [];
    }
    if (selectedCampType == null) {
      ToastManager.toast('Select Camp Type');
      return [];
    }
    if (selectedDistrict == null) {
      ToastManager.toast('Select District');
      return [];
    }
    if (selectedLab == null) {
      ToastManager.toast('Select Lab');
      return [];
    }
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchCampList({
        'CampDATE': fromDate,
        'UserId': empCode.toString(),
        'DISTLGDCODE': selectedDistrict?.dISTLGDCODE.toString() ?? '0',
        'CampType': selectedCampType?.cAMPTYPE.toString() ?? '0',
        'LABCODE': selectedLab?.labCode.toString() ?? '0',
      });
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> getAllDetails() async {
    if (selectedCampID == null) return;
    ToastManager.showLoader();
    try {
      final campId = selectedCampID!.campId?.toString() ?? '';
      final results = await Future.wait([
        _repo.fetchPhleboDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '35'}),
        _repo.fetchDeopDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '64'}),
        _repo.fetchDoctorDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '34'}),
        _repo.fetchTeamDetailsList({'campid': campId, 'CampDate': fromDate}),
        _repo.fetchFlexiPhleboDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '146'}),
        _repo.fetchFlexiDoctorDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '147'}),
        _repo.fetchMMUDoctorDetailsList({'campid': campId, 'CampDate': fromDate, 'DESGID': '141'}),
      ]);
      assignedPhleboList = (results[0] as AssignedExternalResourceDetailsResponse).output ?? [];
      assignedDataEntryOperatorList = (results[1] as AssignedExternalResourceDetailsResponse).output ?? [];
      assignedDoctorsList = (results[2] as AssignedExternalResourceDetailsResponse).output ?? [];
      assignedTeamsList = (results[3] as TeamCampDetailsListResponse).output ?? [];
      assignedFlexPhleboList = (results[4] as AssignedExternalResourceDetailsResponse).output ?? [];
      assignedFlexDoctorsList = (results[5] as AssignedExternalResourceDetailsResponse).output ?? [];
      assignedMMUDoctorList = (results[6] as TeamsMMUDoctorListResponse).output ?? [];
    } catch (e) {
      print('getAllDetails error: $e');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<List<TeamsCampTypeWiseOutput>> fetchTeamsCampTypeWise() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchTeamsCampTypeWise({
        'LabCode': selectedLab?.labCode?.toString() ?? '0',
        'CampDate': fromDate,
        'CampType': selectedCampType?.cAMPTYPE.toString() ?? '',
      });
      teamList = res.output ?? [];
      return teamList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> onTeamTypeSelected(AssignTypeModel team) async {
    selectedTeam = team;
    update();
    final list = await fetchTeamsCampTypeWise();
    if (list.isNotEmpty) {
      shouldOpenTeamSheet = true;
      update();
    }
  }

  Future<List<AssignResourcesOutput>> fetchAssignResourcesList() async {
    if (selectedTeam == null) {
      ToastManager.toast('Please select team first');
      return [];
    }
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchAssignResources({
        'CampType': selectedCampType?.cAMPTYPE.toString() ?? '',
      });
      assignResourcesList = res.output ?? [];
      return assignResourcesList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamDetailsListForAssignOutput>> fetchTeamDetailsListForAssign() async {
    if (selectedCampID == null) {
      ToastManager.toast('Please select Camp ID');
      return [];
    }
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchTeamDetailsListForAssign({
        'campid': selectedCampID?.campId.toString() ?? '0',
        'CampDate': fromDate,
        'CampType': selectedCampType?.cAMPTYPE.toString() ?? '0',
      });
      teamsList = res.output ?? [];
      return teamsList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchPhleboList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchPhleboList({
        'LabCode': selectedLab?.labCode?.toString() ?? '0',
        'CampDate': fromDate,
        'CampType': selectedCampType?.cAMPTYPE.toString() ?? '',
      });
      phleboList = res.output ?? [];
      return phleboList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchDeOpList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchDeOpList({
        'DesgId': selectedAssignResources?.desgId.toString() ?? '0',
        'LabCode': selectedLab?.labCode.toString() ?? '0',
        'CampDate': fromDate,
      });
      deList = res.output ?? [];
      return deList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchDoctorList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchDoctorListCluster({
        'DesgId': selectedAssignResources?.desgId.toString() ?? '0',
        'LabCode': '0',
        'CampDate': fromDate,
        'DISTLGDCODE': dISTLGDCODE.toString(),
        'USERID': empCode.toString(),
      });
      doctorList = res.output ?? [];
      return doctorList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchFlexiPhleboList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchDeOpList({
        'DesgId': selectedAssignResources?.desgId.toString() ?? '0',
        'LabCode': selectedLab?.labCode.toString() ?? '0',
        'CampDate': fromDate,
      });
      doctorList = res.output ?? [];
      return doctorList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchFlexiDoctorList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchDeOpList({
        'DesgId': selectedAssignResources?.desgId.toString() ?? '0',
        'LabCode': '0',
        'CampDate': fromDate,
      });
      doctorList = res.output ?? [];
      return doctorList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<TeamsDoctorListOutput>> fetchMMUDoctorList() async {
    ToastManager.showLoader();
    try {
      final res = await _repo.fetchMMUDoctorList({
        'DesgId': selectedAssignResources?.desgId.toString() ?? '0',
        'LabCode': '0',
        'CampDate': fromDate,
      });
      doctorList = res.output ?? [];
      return doctorList;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  String? validateForAssign() {
    if (fromDate.isEmpty) return 'Please Select Date';
    if (selectedCampType == null) return 'Please Select CampType';
    if (selectedDistrict == null) return 'Please Select District';
    if (selectedLab == null) return 'Please Select Lab';
    if (selectedCampID == null) return 'Please Select Camp';
    if (selectedTeams == null) return 'Please first select Team before adding resources';
    if (doctorList.isEmpty || !doctorList.any((d) => d.selected)) {
      return 'Please select Doctor';
    }
    return null;
  }

  String buildTeamString() {
    final array = <Map<String, dynamic>>[];
    for (final obj in teamList) {
      if (obj.selected) {
        array.add({'teamId': obj.teamid.toString(), 'campId': selectedCampID?.campId.toString(), 'userId': obj.memberUserID1.toString(), 'IsTeam': '1'});
        array.add({'teamId': obj.teamid.toString(), 'campId': selectedCampID?.campCreatedBy.toString(), 'userId': obj.memberUserID2.toString(), 'IsTeam': '1'});
      }
    }
    for (final obj in phleboList) {
      if (obj.selected) array.add({'teamId': selectedTeams?.teamNumber ?? '0', 'campId': selectedCampID?.campId.toString(), 'userId': obj.uSERID.toString(), 'IsTeam': '0'});
    }
    for (final obj in assignedFlexPhleboList) {
      if (obj.isSelected) array.add({'teamId': selectedTeams?.teamNumber ?? '0', 'campId': selectedCampID?.campId.toString(), 'userId': obj.userID.toString(), 'IsTeam': '0'});
    }
    for (final obj in deList) {
      if (obj.selected) array.add({'teamId': selectedTeams?.teamNumber ?? '0', 'campId': selectedCampID?.campId.toString(), 'userId': obj.uSERID.toString(), 'IsTeam': '0'});
    }
    for (final obj in doctorList) {
      if (obj.selected) array.add({'teamId': selectedTeams?.teamNumber ?? '0', 'campId': selectedCampID?.campId.toString(), 'userId': obj.uSERID.toString(), 'IsTeam': '0'});
    }
    for (final obj in assignedFlexDoctorsList) {
      if (obj.isSelected) array.add({'teamId': selectedTeams?.teamNumber ?? '0', 'campId': selectedCampID?.campId.toString(), 'userId': obj.userID.toString(), 'IsTeam': '0'});
    }
    return jsonEncode(array);
  }

  Future<bool> submitAssignment() async {
    final error = validateForAssign();
    if (error != null) {
      ToastManager.toast(error);
      return false;
    }
    final teamString = buildTeamString();
    try {
      await _repo.insertTeamCampMapping({
        'CampID': selectedCampID?.campId.toString() ?? '0',
        'UserID': empCode.toString(),
        'TeamNumber': teamString,
        'CreatedBy': empCode.toString(),
      });
      return true;
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
      return false;
    }
  }

  Future<bool> submitTeamOnly() async {
    if (selectedCampID == null) {
      ToastManager.toast('Please Select Camp');
      return false;
    }
    final array = <Map<String, dynamic>>[];
    for (final obj in teamList) {
      if (obj.selected) {
        array.add({'teamId': obj.teamid.toString(), 'campId': selectedCampID?.campId.toString(), 'userId': obj.memberUserID1.toString(), 'IsTeam': '1'});
        array.add({'teamId': obj.teamid.toString(), 'campId': selectedCampID?.campCreatedBy.toString(), 'userId': obj.memberUserID2.toString(), 'IsTeam': '1'});
      }
    }
    ToastManager.showLoader();
    try {
      await _repo.insertTeamCampMapping({
        'CampID': selectedCampID?.campId.toString() ?? '0',
        'UserID': empCode.toString(),
        'TeamNumber': jsonEncode(array),
        'CreatedBy': empCode.toString(),
      });
      return true;
    } catch (e) {
      ToastManager.toast(e.toString());
      return false;
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> reloadAfterAssign({bool isTeamRefresh = false}) async {
    assignedPhleboList = [];
    teamList = [];
    assignedDataEntryOperatorList = [];
    doctorList = [];
    await getAllDetails();
    if (isTeamRefresh) {
      await fetchTeamsCampTypeWise();
      shouldOpenTeamSheet = true;
      update();
    }
  }

  Future<void> performRemove(String member, String teamId, String userId, int removeType) async {
    try {
      final res = await _repo.removeTeamMapping({
        'campid': selectedCampID?.campId.toString() ?? '0',
        'RemovedBy': empCode.toString(),
        'Userid': userId,
        'TeamId': teamId,
        'IsTeam': removeType == 1 ? '1' : '0',
      });
      if (res.status == 'Success') {
        final messages = {
          1: 'Team removed successfully',
          2: 'Doctor removed successfully',
          3: 'Data entry operator removed successfully',
          4: 'Flexi phlebo removed successfully',
          5: 'Flexi Doctor operator removed successfully',
          6: 'MMU Doctor operator removed successfully',
        };
        ToastManager.toast(messages[removeType] ?? 'This team can not be removed as beneficiaries are registered by team member');
        await getAllDetails();
      } else {
        ToastManager.toast('This team can not be removed as beneficiaries are registered by team member');
      }
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }
}
