// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/check_connectivity.dart';
import 'package:s2toperational/Screens/d2d_teams/model/BindDistrictResponse.dart';
import 'package:s2toperational/Screens/d2d_teams/model/CampTypeListModel.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DNonWorkingTeams.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsCallingDetails.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsCountModel.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsDivisionModel.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsLabModel.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsListModel.dart';
import 'package:s2toperational/Screens/d2d_teams/model/OrganizationListModel.dart';
import '../repository/d2d_teams_repository.dart';

class D2DTeamsController extends GetxController {
  final D2DTeamsRepository _repository = D2DTeamsRepository();

  // ─── User info ────────────────────────────────────────────────────────────────

  String empCode = '0';
  String desgId = '0';

  // ─── Loading state ────────────────────────────────────────────────────────────

  bool hasInternet = true;
  bool isD2dTeamsLoading = false;
  bool isD2dNonWorkingTeamsLoading = false;

  // ─── Main screen models ───────────────────────────────────────────────────────

  D2DTeamsListModel? d2dTeamsListModel;
  D2DTeamsCountModel? d2dTeamsCountModel;

  // ─── Filter dropdown models ───────────────────────────────────────────────────

  CampTypeListModel? campTypeListModel;
  OrganizationListModel? organizationListModel;
  D2DTeamsDivisionModel? d2dTeamsDivisionModel;
  BindDistrictResponse? bindDistrictResponse;
  D2DTeamsLabModel? d2dTeamsLabModel;

  // ─── Filter selections ────────────────────────────────────────────────────────

  String? selectedCamp;
  String? selectedOrg;
  String? selectedDiv;
  String? selectedDist;
  String? selectedLab;

  // ─── Non-working / working teams ─────────────────────────────────────────────

  D2dNonWorkingTeams? d2dWorkingOrNonWorkingTeams;
  D2DTeamsCallingDetails? d2dTeamsCallingDetails;

  // ─── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode.toString() ?? '0';
    desgId = user?.dESGID.toString() ?? '0';
    resetFilters();
    checkInternetAndLoad();
  }

  // ─── Filter helpers ───────────────────────────────────────────────────────────

  void resetFilters() {
    selectedCamp = null;
    selectedOrg = null;
    selectedDiv = null;
    selectedDist = null;
    selectedLab = null;
    d2dTeamsDivisionModel = null;
    d2dTeamsLabModel = null;
    campTypeListModel = null;
    organizationListModel = null;
    bindDistrictResponse = null;
    d2dTeamsListModel = null;
    d2dTeamsCountModel = null;
  }

  // ─── Resolved filter IDs ──────────────────────────────────────────────────────

  String get resolvedCampId {
    if (selectedCamp == null || selectedCamp == 'All') return '0';
    return campTypeListModel?.output
            .firstWhereOrNull((e) => e.description == selectedCamp)
            ?.campType
            .toString() ??
        '0';
  }

  String get resolvedDivId {
    if (selectedDiv == null || selectedDiv == 'Select All') return '0';
    return d2dTeamsDivisionModel?.output
            .firstWhereOrNull((e) => e.divName == selectedDiv)
            ?.divId
            .toString() ??
        '0';
  }

  String get resolvedDistCode {
    if (selectedDist == null || selectedDist == 'Select All') return '0';
    return bindDistrictResponse?.output
            ?.firstWhereOrNull((e) => e.dISTNAME == selectedDist)
            ?.dISTLGDCODE
            .toString() ??
        '0';
  }

  String get resolvedLabCode {
    if (selectedLab == null) return '0';
    return d2dTeamsLabModel?.output
            .firstWhereOrNull((e) => e.labName == selectedLab)
            ?.labCode
            .toString() ??
        '0';
  }

  String get resolvedOrgId {
    if (selectedOrg == null) return '0';
    return organizationListModel?.output
            .firstWhereOrNull((e) => e.subOrgName == selectedOrg)
            ?.subOrgId
            .toString() ??
        '0';
  }

  // ─── Initial load ─────────────────────────────────────────────────────────────

  Future<void> checkInternetAndLoad() async {
    isD2dTeamsLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      if (hasInternet) {
        await Future.wait([
          _getOrgList(),
          _getTeamsList(campType: '0', divId: '0', distlgdCode: '0',
              labCode: '0', subOrgId: '0'),
          _getTeamsCount(campType: '0', divId: '0', distlgdCode: '0',
              labCode: '0', subOrgId: '0'),
          _getCampTypeList(),
          _getDivisionList(),
        ]);
      }
    } finally {
      isD2dTeamsLoading = false;
      update();
    }
  }

  // ─── Apply filter ─────────────────────────────────────────────────────────────

  Future<void> applyFilter() async {
    isD2dTeamsLoading = true;
    update();
    await Future.wait([
      _getTeamsList(
        campType: resolvedCampId,
        divId: resolvedDivId,
        distlgdCode: resolvedDistCode,
        labCode: resolvedLabCode,
        subOrgId: resolvedOrgId,
      ),
      _getTeamsCount(
        campType: resolvedCampId,
        divId: resolvedDivId,
        distlgdCode: resolvedDistCode,
        labCode: resolvedLabCode,
        subOrgId: resolvedOrgId,
      ),
    ]);
    isD2dTeamsLoading = false;
    update();
  }

  // ─── Teams list & count ───────────────────────────────────────────────────────

  Future<void> _getTeamsList({
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String subOrgId,
  }) async {
    try {
      final response = await _repository.getTeamsList(
        empId: empCode,
        campType: campType,
        divId: divId,
        distlgdCode: distlgdCode,
        labCode: labCode,
        desgId: desgId,
        subOrgId: subOrgId,
      );
      if (response.statusCode == 200) {
        d2dTeamsListModel = D2DTeamsListModel.fromJson(jsonDecode(response.body));
      } else {
        ToastManager.toast('Failed to load teams list');
      }
    } catch (e) {
      debugPrint('getTeamsList error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  Future<void> _getTeamsCount({
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String subOrgId,
  }) async {
    try {
      final response = await _repository.getTeamsCount(
        empId: empCode,
        campType: campType,
        divId: divId,
        distlgdCode: distlgdCode,
        labCode: labCode,
        desgId: desgId,
        subOrgId: subOrgId,
      );
      if (response.statusCode == 200) {
        d2dTeamsCountModel =
            D2DTeamsCountModel.fromJson(jsonDecode(response.body));
      } else {
        ToastManager.toast('Failed to load teams count');
      }
    } catch (e) {
      debugPrint('getTeamsCount error: $e');
    }
    update();
  }

  // ─── Filter dropdowns ─────────────────────────────────────────────────────────

  Future<void> _getCampTypeList() async {
    try {
      final response = await _repository.getCampTypeList();
      if (response.statusCode == 200) {
        campTypeListModel =
            CampTypeListModel.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      debugPrint('getCampTypeList error: $e');
    }
    update();
  }

  Future<void> _getOrgList() async {
    try {
      final response =
          await _repository.getOrgList(empId: empCode, desgId: desgId);
      if (response.statusCode == 200) {
        organizationListModel =
            OrganizationListModel.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      debugPrint('getOrgList error: $e');
    }
    update();
  }

  Future<void> _getDivisionList() async {
    try {
      final response =
          await _repository.getDivisionList(empId: empCode, desgId: desgId);
      if (response.statusCode == 200) {
        d2dTeamsDivisionModel =
            D2DTeamsDivisionModel.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      debugPrint('getDivisionList error: $e');
    }
    update();
  }

  Future<void> getDistrictList({
    required String subOrgId,
    required String divId,
    required String distlgCode,
  }) async {
    try {
      final response = await _repository.getDistrictList(
        subOrgId: subOrgId,
        empId: empCode,
        desgId: desgId,
        divId: divId,
        distlgCode: distlgCode,
      );
      if (response.statusCode == 200) {
        bindDistrictResponse =
            BindDistrictResponse.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      debugPrint('getDistrictList error: $e');
    }
    update();
  }

  Future<void> getLabList({required String distlgCode}) async {
    try {
      final response =
          await _repository.getLabList(distlgCode: distlgCode);
      if (response.statusCode == 200) {
        d2dTeamsLabModel =
            D2DTeamsLabModel.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      debugPrint('getLabList error: $e');
    }
    update();
  }

  // ─── Non-working / working teams ─────────────────────────────────────────────

  Future<void> checkInternetAndLoadNonWorking({
    required String title,
    String? empId,
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String subOrgId,
    String? desigId,
  }) async {
    isD2dNonWorkingTeamsLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      if (hasInternet) {
        await _getNonWorkingTeams(
          title: title,
          empId: empId ?? empCode,
          campType: campType,
          divId: divId,
          distlgdCode: distlgdCode,
          labCode: labCode,
          desgId: desigId ?? desgId,
          subOrgId: subOrgId,
        );
      }
    } finally {
      isD2dNonWorkingTeamsLoading = false;
      update();
    }
  }

  Future<void> _getNonWorkingTeams({
    required String title,
    required String empId,
    required String campType,
    required String divId,
    required String distlgdCode,
    required String labCode,
    required String desgId,
    required String subOrgId,
  }) async {
    try {
      final response = await _repository.getNonWorkingTeams(
        title: title,
        empId: empId,
        campType: campType,
        divId: divId,
        distlgdCode: distlgdCode,
        labCode: labCode,
        desgId: desgId,
        subOrgId: subOrgId,
      );
      if (response.statusCode == 200) {
        d2dWorkingOrNonWorkingTeams =
            D2dNonWorkingTeams.fromJson(jsonDecode(response.body));
      } else {
        ToastManager.toast('Failed to load teams');
      }
    } catch (e) {
      debugPrint('getNonWorkingTeams error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  // ─── Calling details ──────────────────────────────────────────────────────────

  Future<void> getCallingDetails(String teamId) async {
    ToastManager.showLoader();
    try {
      final response =
          await _repository.getCallingDetails(teamId: teamId);
      if (response.statusCode == 200) {
        d2dTeamsCallingDetails =
            D2DTeamsCallingDetails.fromJson(jsonDecode(response.body));
      } else {
        ToastManager.toast('Failed to load calling details');
      }
    } catch (e) {
      debugPrint('getCallingDetails error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }
}