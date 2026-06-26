// ignore_for_file: file_names

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Screens/camp_calendar/model/bind_district_response.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/camp_conducted_response.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/camp_type_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_non_working_teams.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_calling_details.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_count_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_division_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_lab_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/d2d_teams_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/district_list_response.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/fibro_scanning_district_wise_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/liver_scanning_count_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/liver_scanning_table_data.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/organization_list_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/s2t_android_Ios_count_district_wise_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/Model/s2t_android_Ios_count_model.dart';
import 'package:s2toperational/Screens/admin_dashboard/repository/admin_dashboard_repository.dart';

import '../../calling_modules/custom_widgets/check_connectivity.dart';

class AdminController extends GetxController {
  final AdminDashboardRepository _repository = AdminDashboardRepository();

  /// Defaults
  String? selectedDistrict = 'ALL';
  String selectedDistrictCode = '0';
  String? selectedCampType = 'ALL CAMP';
  DateTime selectedMonth = DateTime.now();

  DateTime get pickerFirstDate => DateTime(2019, 1);
  DateTime get pickerLastDate => DateTime(DateTime.now().year + 5, 12);

  bool fetching = false;

  String get monthParam => selectedMonth.month.toString().padLeft(2, '0');
  String get yearParam => selectedMonth.year.toString();

  String get selectedCampTypeId =>
      campTypes
          .firstWhere(
            (e) => e.campType == (selectedCampType ?? 'ALL CAMP'),
            orElse: () => campTypes.first,
          )
          .campTypeId;

  List<CampType> campTypes = const [
    CampType('0', 'ALL CAMP'),
    CampType('1', 'NORMAL CAMP'),
    CampType('3', 'D2D CAMP'),
  ];

  String fromDate = '';
  String toDate = '';

  List<String> get districtNamesForPicker {
    final names =
        districtListResponse?.output.map((e) => e.name).toList() ?? <String>[];
    for (int i = 0; i < names.length; i++) {
      if (names[i].trim().toUpperCase() == 'ALL') names[i] = 'ALL';
    }
    final idxAll = names.indexWhere((n) => n.trim().toUpperCase() == 'ALL');
    if (idxAll == -1) {
      names.insert(0, 'ALL');
    } else if (idxAll > 0) {
      final v = names.removeAt(idxAll);
      names.insert(0, v.toUpperCase());
    } else {
      names[0] = 'ALL';
    }
    return names;
  }

  List<String> get campTypeLabelsForPicker =>
      campTypes.map((e) => e.campType).toList();

  LoginResponseModel? loginResponseModel;
  String? status;
  bool hasInternet = true;
  bool isS2tAppLoading = false;
  bool isS2tAppDistrictLoading = false;
  bool isD2dTeamsLoading = false;
  bool isD2dNonWorkingTeamsLoading = false;
  bool isConductedCampsLoading = false;

  DistrictListResponse? districtListResponse;
  CampsConductedResponse? campsConductedResponse;
  LiverScanningCountModel? liverScanningCountModel;
  S2TAndroidIosCountModel? s2tAndroidIosCountModel;
  S2TAndroidIosCountDistrictWiseModel? s2tAndroidIosCountDistrictWiseModel;
  D2DTeamsListModel? d2dTeamsListModel;
  D2DTeamsCountModel? d2dTeamsCountModel;
  CampTypeListModel? campTypeListModel;
  OrganizationListModel? organizationListModel;
  D2DTeamsDivisionModel? d2dTeamsDivisionModel;
  BindDistrictResponse? bindDistrictResponse;
  D2DTeamsLabModel? d2dTeamsLabModel;
  D2dNonWorkingTeams? d2dWorkingOrNonWorkingTeams;
  D2DTeamsCallingDetails? d2dTeamsCallingDetails;

  LiverScanningTableData? fibroScanResponse;
  FibroScanningDistrictWiseModel? fibroScanDistirctResponse;
  bool isLiverScanningLoading = false;

  String? selectedLab;
  String? selectedDist;
  String? selectedDiv;
  String? selectedOrg;
  String? selectedCamp;

  void resetD2dTeamsFilters() {
    selectedOrg = null;
    selectedDiv = null;
    selectedDist = null;
    selectedLab = null;
    selectedCamp = "All";
    bindDistrictResponse = null;
    d2dTeamsDivisionModel = null;
    d2dTeamsLabModel = null;
    campTypeListModel = null;
    organizationListModel = null;
    d2dTeamsListModel = null;
    d2dTeamsCountModel = null;
    update();
  }

  Future<void> checkInternet() async {
    isConductedCampsLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      loginResponseModel = DataProvider().getParsedUserData();
      debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
      update();

      if (hasInternet) {
        await getDistrict(showLoader: false);
        await getTableDataWithSkeleton(
          selectedMonth.month.toString(),
          selectedMonth.year.toString(),
          '0',
          '0',
        );
      }
    } finally {
      isConductedCampsLoading = false;
      update();
    }
  }

  Future<void> checkInternetLiverScann() async {
    isLiverScanningLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      loginResponseModel = DataProvider().getParsedUserData();
      debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
      update();

      if (hasInternet) {
        final now = DateTime.now();
        toDate = FormatterManager.formatDateToStringInDash(now);
        fromDate = FormatterManager.formatDateToStringInDash(
          now.subtract(const Duration(days: 10)),
        );

        await getLiverDashCount(showLoader: false);

        final body = {
          'fromdate': fromDate,
          'todate': toDate,
          'userid': loginResponseModel?.output?.first.empCode.toString() ?? '0',
          'desgid': loginResponseModel?.output?.first.dESGID.toString() ?? '0',
          'suborgid': '0',
          'distlgdcode': '0',
        };
        await getLiverTableData(body, showLoader: false);
      }
    } finally {
      isLiverScanningLoading = false;
      update();
    }
  }

  void checkInternetS2TApp() async {
    isS2tAppLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      loginResponseModel = DataProvider().getParsedUserData();
      debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
      update();

      if (hasInternet) {
        await getS2tAndroidIosCount(showLoader: false);
      }
    } finally {
      isS2tAppLoading = false;
      update();
    }
  }

  Future<void> getNonWorkingTeams(
    String title,
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrgId, {
    bool showLoader = true,
  }) async {
    isD2dNonWorkingTeamsLoading = true;
    update();
    if (showLoader) ToastManager.showLoader();
    try {
      d2dWorkingOrNonWorkingTeams = await _repository.fetchTeamsByWorkingStatus(
        title == "D2D Working Teams",
        empId, campType, divId, distlgdCode, labCode, desgId, subOrgId,
      );
      if (d2dWorkingOrNonWorkingTeams == null) {
        ToastManager.toast('Failed getting teams');
      }
    } catch (e) {
      debugPrint('getNonWorkingTeams error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      isD2dNonWorkingTeamsLoading = false;
      update();
    }
  }

  Future<void> getCallingDetails(String teamId) async {
    ToastManager.showLoader();
    try {
      d2dTeamsCallingDetails = await _repository.fetchCallingDetails(teamId);
      if (d2dTeamsCallingDetails == null) {
        ToastManager.toast('Failed getting calling details');
      }
    } catch (e) {
      debugPrint('getCallingDetails error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getLiverDashCount({bool showLoader = true}) async {
    if (showLoader) ToastManager.showLoader();
    try {
      liverScanningCountModel = await _repository.fetchLiverDashCount();
      if (liverScanningCountModel == null) {
        ToastManager.toast('Failed getting liver dash count');
      }
    } catch (e) {
      debugPrint('getLiverDashCount error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getS2tAndroidIosCount({bool showLoader = true}) async {
    if (showLoader) ToastManager.showLoader();
    try {
      s2tAndroidIosCountModel = await _repository.fetchS2TAppCount();
      if (s2tAndroidIosCountModel == null) {
        ToastManager.toast('Failed getting S2T app count');
      }
    } catch (e) {
      debugPrint('getS2tAndroidIosCount error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> checkInternetS2TAppDistrictWise() async {
    hasInternet = await CheckConnectivity.checkInternetAndLoadData();
    loginResponseModel = DataProvider().getParsedUserData();
    debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
    update();

    if (hasInternet) {
      await getS2tAndroidIosDistrictWiseList(showLoader: false);
    }
  }

  Future<void> getD2dTeamsList(
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrdId, {
    bool showLoader = true,
  }) async {
    isD2dTeamsLoading = true;
    update();
    if (showLoader) ToastManager.showLoader();
    try {
      d2dTeamsListModel = await _repository.fetchD2DTeamsList(
        empId, campType, divId, distlgdCode, labCode, desgId, subOrdId,
      );
      if (d2dTeamsListModel == null) {
        ToastManager.toast('Failed getting D2D teams list');
      }
    } catch (e) {
      debugPrint('getD2dTeamsList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      isD2dTeamsLoading = false;
      update();
    }
  }

  Future<void> getD2dTeamsCount(
    String empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String desgId,
    String subOrdId, {
    bool showLoader = true,
  }) async {
    if (showLoader) ToastManager.showLoader();
    try {
      d2dTeamsCountModel = await _repository.fetchD2DTeamsCount(
        empId, campType, divId, distlgdCode, labCode, desgId, subOrdId,
      );
      if (d2dTeamsCountModel == null) {
        ToastManager.toast('Failed getting D2D teams count');
      }
    } catch (e) {
      debugPrint('getD2dTeamsCount error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getCampTypeList({bool showLoader = true}) async {
    if (showLoader) ToastManager.showLoader();
    try {
      campTypeListModel = await _repository.fetchCampTypeList();
      if (campTypeListModel == null) {
        ToastManager.toast('Failed getting camp type list');
      }
    } catch (e) {
      debugPrint('getCampTypeList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getOrgList(
    String empId,
    String desigId, {
    bool showLoader = true,
  }) async {
    if (showLoader) ToastManager.showLoader();
    try {
      organizationListModel = await _repository.fetchOrgList(empId, desigId);
      if (organizationListModel == null) {
        ToastManager.toast('Failed getting org list');
      }
    } catch (e) {
      debugPrint('getOrgList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getDivisionList(
    String empId,
    String desigId, {
    bool showLoader = true,
  }) async {
    if (showLoader) ToastManager.showLoader();
    try {
      d2dTeamsDivisionModel = await _repository.fetchDivisionList(empId, desigId);
      if (d2dTeamsDivisionModel == null) {
        ToastManager.toast('Failed getting division list');
      }
    } catch (e) {
      debugPrint('getDivisionList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getDistrictList(
    String subOrgId,
    String empId,
    String desigId,
    String diviD,
    String distlgCODE,
  ) async {
    ToastManager.showLoader();
    try {
      bindDistrictResponse = await _repository.fetchD2DDistrictList(
        subOrgId, empId, desigId, diviD, distlgCODE,
      );
      if (bindDistrictResponse == null) {
        ToastManager.toast('Failed getting district list');
      }
    } catch (e) {
      debugPrint('getDistrictList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getLabList(String distlgCODE) async {
    ToastManager.showLoader();
    try {
      d2dTeamsLabModel = await _repository.fetchLabList(distlgCODE);
      if (d2dTeamsLabModel == null) {
        ToastManager.toast('Failed getting lab list');
      }
    } catch (e) {
      debugPrint('getLabList error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> checkInternetD2DTeams() async {
    hasInternet = await CheckConnectivity.checkInternetAndLoadData();
    loginResponseModel = DataProvider().getParsedUserData();
    debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
    update();

    if (hasInternet) {
      await getOrgList(
        loginResponseModel!.output!.first.empCode.toString(),
        loginResponseModel!.output!.first.dESGID.toString(),
        showLoader: false,
      );

      if (organizationListModel?.output != null &&
          organizationListModel!.output.isNotEmpty) {
        selectedOrg = organizationListModel!.output.first.subOrgName;
        update();
      }

      await getD2dTeamsList(
        loginResponseModel!.output!.first.empCode.toString(),
        '0', '0', '0', '0',
        loginResponseModel!.output!.first.dESGID.toString(),
        '0',
        showLoader: false,
      );
      await getD2dTeamsCount(
        loginResponseModel!.output!.first.empCode.toString(),
        '0', '0', '0', '0',
        loginResponseModel!.output!.first.dESGID.toString(),
        organizationListModel!.output.first.subOrgId.toString(),
        showLoader: false,
      );
      await getCampTypeList(showLoader: false);
      await getDivisionList(
        loginResponseModel!.output!.first.empCode.toString(),
        loginResponseModel!.output!.first.dESGID.toString(),
        showLoader: false,
      );
    }
  }

  void checkInternetD2dNonWorking(
    String title,
    String? empId,
    String campType,
    String divId,
    String distlgdCode,
    String labCode,
    String subOrgId,
    String? desigId,
  ) async {
    hasInternet = await CheckConnectivity.checkInternetAndLoadData();
    loginResponseModel = DataProvider().getParsedUserData();
    debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
    update();

    if (hasInternet) {
      await getNonWorkingTeams(
        title,
        empId ?? loginResponseModel!.output!.first.empCode.toString(),
        campType, divId, distlgdCode, labCode,
        desigId ?? loginResponseModel!.output!.first.dESGID.toString(),
        subOrgId,
        showLoader: false,
      );
    }
  }

  Future<void> getS2tAndroidIosDistrictWiseList({bool showLoader = true}) async {
    isS2tAppDistrictLoading = true;
    update();
    if (showLoader) ToastManager.showLoader();
    try {
      s2tAndroidIosCountDistrictWiseModel =
          await _repository.fetchS2TAppDistrictWiseCount();
      if (s2tAndroidIosCountDistrictWiseModel != null) {
        debugPrint(
          'Data loaded: ${s2tAndroidIosCountDistrictWiseModel?.details.count.length ?? 0} items',
        );
      } else {
        ToastManager.toast('Failed getting S2T district-wise count');
      }
    } catch (e) {
      debugPrint('getS2tAndroidIosDistrictWiseList error: $e');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      isS2tAppDistrictLoading = false;
      update();
    }
  }

  Future<void> getLiverTableData(
    Map<String, String> body, {
    bool showLoader = true,
  }) async {
    try {
      fibroScanResponse = await _repository.fetchLiverTableData(body);
    } catch (e) {
      debugPrint('getLiverTableData error: $e');
      if (showLoader) ToastManager.toast('Failed getting liver table data');
    }
    update();
  }

  void getLiverTableDatadistrictWise(Map<String, String> body) async {
    ToastManager.showLoader();
    try {
      fibroScanDistirctResponse =
          await _repository.fetchLiverTableDataDistrictWise(body);
      if (fibroScanDistirctResponse == null) {
        ToastManager.toast('Failed getting liver district-wise data');
      }
    } catch (e) {
      debugPrint('getLiverTableDatadistrictWise error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getDistrict({bool showLoader = true}) async {
    if (showLoader) ToastManager.showLoader();
    try {
      districtListResponse = await _repository.fetchDistricts();
      if (districtListResponse?.status == 'Success') {
        status = districtListResponse!.message;
        selectedDistrict = 'ALL';
        selectedDistrictCode = '0';
      } else if (districtListResponse != null) {
        status = districtListResponse!.message;
        ToastManager.toast(status!);
      } else {
        ToastManager.toast('Failed getting districts');
      }
    } catch (e) {
      debugPrint('getDistrict error: $e');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      update();
    }
  }

  Future<void> _getTableDataInternal(
    String month,
    String year,
    String distCode,
    String campType,
  ) async {
    try {
      campsConductedResponse = await _repository.fetchCampsConducted(
        month, year, distCode, campType,
      );
      if (campsConductedResponse?.status == 'Success') {
        status = campsConductedResponse!.message;
      } else if (campsConductedResponse != null) {
        status = campsConductedResponse!.message;
      } else {
        ToastManager.toast('Failed getting table data');
      }
    } catch (e) {
      debugPrint('getTableData error: $e');
      ToastManager.toast('Something went wrong');
    }
  }

  Future<void> getTableData(
    String month,
    String year,
    String distCode,
    String campType,
  ) async {
    ToastManager.showLoader();
    try {
      await _getTableDataInternal(month, year, distCode, campType);
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getTableDataWithSkeleton(
    String month,
    String year,
    String distCode,
    String campType,
  ) async {
    isConductedCampsLoading = true;
    update();
    try {
      await _getTableDataInternal(month, year, distCode, campType);
    } finally {
      isConductedCampsLoading = false;
      update();
    }
  }

  void setDistrictFromName(String? name) {
    if (name == null || name.trim().toUpperCase() == 'ALL') {
      selectedDistrict = 'ALL';
      selectedDistrictCode = '0';
      update();
      return;
    }
    selectedDistrict = name;
    String code = '0';
    final list = districtListResponse?.output ?? [];
    for (final d in list) {
      if (d.name.trim().toUpperCase() == name.trim().toUpperCase()) {
        code = '${d.lgdCode}';
        break;
      }
    }
    selectedDistrictCode = code;
    update();
  }

  void setCampTypeFromLabel(String? label) {
    selectedCampType = (label ?? 'ALL CAMP').trim().toUpperCase();
    update();
  }

  Future<void> refreshTableForCurrentFilters() async {
    if (fetching) return;
    fetching = true;
    try {
      await getTableDataWithSkeleton(
        monthParam, yearParam, selectedDistrictCode, selectedCampTypeId,
      );
    } finally {
      fetching = false;
    }
  }
}

class CampType {
  final String campTypeId;
  final String campType;

  const CampType(this.campTypeId, this.campType);
}

enum CampRowStatus { todayOpen, open, none }
