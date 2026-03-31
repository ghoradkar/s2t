// ignore_for_file: file_names

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/CampConductedResponse.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/CampTypeListModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DNonWorkingTeams.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsCallingDetails.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsCountModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsDivisionModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsLabModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsListModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/DistrictListResponse.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/FibroScanningDistrictWiseModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/LiverScanningCountModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/LiverScanningTableData.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/OrganizationListModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/S2TAndroidIosCountDistrictWiseModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/S2TAndroidIosCountModel.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';

import '../../calling_modules/custom_widgets/check_connectivity.dart';

class AdminController extends GetxController {
  /// Defaults
  String? selectedDistrict = 'ALL'; // default display for District
  String selectedDistrictCode = '0'; // default code for "ALL"
  String? selectedCampType = 'ALL CAMP'; // default display for Camp Type
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

  /// Camp types (labels normalized to uppercase to match design)
  List<CampType> campTypes = const [
    CampType('0', 'ALL CAMP'),
    CampType('1', 'NORMAL CAMP'),
    CampType('3', 'D2D CAMP'),
  ];

  String fromDate = '';
  String toDate = '';

  /// Derived list for the District picker: ensure "ALL" exists at the top & normalized
  List<String> get districtNamesForPicker {
    final names =
        districtListResponse?.output.map((e) => e.name).toList() ?? <String>[];
    // normalize casing
    for (int i = 0; i < names.length; i++) {
      if (names[i].trim().toUpperCase() == 'ALL') names[i] = 'ALL';
    }
    // insert or move ALL to top
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

  /// Camp type labels for picker (already uppercase)
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

  /// Startup
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
        // final now = DateTime.now();
        //
        // final firstDayOfMonth = DateTime(now.year, now.month, 1);
        //
        // fromDate = FormatterManager.formatDateToStringInDash(
        //   firstDayOfMonth,
        // ); // e.g. 2025/09/01
        // toDate = FormatterManager.formatDateToStringInDash(now);

        if (hasInternet) {
          final now = DateTime.now();

          // Set toDate as today
          toDate = FormatterManager.formatDateToStringInDash(now);

          // Set fromDate as 10 days before today
          final tenDaysBefore = now.subtract(const Duration(days: 10));
          fromDate = FormatterManager.formatDateToStringInDash(tenDaysBefore);

          await getLiverDashCount(showLoader: false);

          Map<String, String> body = {
            'fromdate': fromDate,
            'todate': toDate,
            'userid': loginResponseModel?.output?.first.empCode.toString() ?? '0',
            'desgid': loginResponseModel?.output?.first.dESGID.toString() ?? '0',
            'suborgid': '0',
            'distlgdcode': '0',
          };
          await getLiverTableData(body, showLoader: false);
        }

        await getLiverDashCount(showLoader: false);

        Map<String, String> body = {
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
    String subOrgId,
    {
    bool showLoader = true,
  }) async {
    isD2dNonWorkingTeamsLoading = true;
    update();
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${title == "D2D Working Teams" ? APIConstants.kGetActiveInactiveD2DWorkingTeamsV2 : APIConstants.kGetActiveInactiveD2DNonWorkingTeamsV2}?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrgId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        d2dWorkingOrNonWorkingTeams = D2dNonWorkingTeams.fromJson(
          jsonDecode(response.body),
        );
      } else {
        ToastManager.toast('Failed getting getNonWorkingTeams');
      }
    } catch (e, st) {
      debugPrint('getNonWorkingTeams error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      isD2dNonWorkingTeamsLoading = false;
      update();
    }
  }

  Future<void> getCallingDetails(String teamId) async {
    ToastManager.showLoader();
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetTeamMembersDetailsForCalling}?Teamid=$teamId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        d2dTeamsCallingDetails = D2DTeamsCallingDetails.fromJson(
          jsonDecode(response.body),
        );
      } else {
        ToastManager.toast('Failed getting getCallingDetails');
      }
    } catch (e, st) {
      debugPrint('getCallingDetails error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getLiverDashCount({bool showLoader = true}) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri = "${APIManager.kLiverScann}${APIConstants.getLiverDashCount}";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        liverScanningCountModel = LiverScanningCountModel.fromJson(
          jsonDecode(response.body),
        );
      } else {
        ToastManager.toast('Failed getting getLiverDashCount');
      }
    } catch (e, st) {
      debugPrint('getLiverDashCount error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      update();
    }
  }

  Future<void> getS2tAndroidIosCount({bool showLoader = true}) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kTreatmentCount}${APIConstants.getAndroidIosCount}";
      debugPrint(uri);

      final response = await Repository.postResponseWithoutBody(
        uri,
        timeout: const Duration(minutes: 5),
      );

      if (response.statusCode == 200) {
        var resp = await response.stream.bytesToString();
        s2tAndroidIosCountModel = S2TAndroidIosCountModel.fromJson(
          jsonDecode(resp),
        );
      } else {
        ToastManager.toast('Failed getting getS2tAndroidIosCount');
      }
    } catch (e, st) {
      debugPrint('getS2tAndroidIosCount error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
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
    String subOrdId,
    {
    bool showLoader = true,
  }) async {
    isD2dTeamsLoading = true;
    update();
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetActiveInactiveD2DTeamsGridDataV2}?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrdId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = await response.body;
        d2dTeamsListModel = D2DTeamsListModel.fromJson(jsonDecode(resp));
      } else {
        ToastManager.toast('Failed getting getD2dTeamsList');
      }
    } catch (e, st) {
      debugPrint('getD2dTeamsList error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
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
    String subOrdId,
    {
    bool showLoader = true,
  }) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetActiveInactiveD2DTeamsCountV2}?GLOUSERID=$empId&CampType=$campType&DivId=$divId&DISTLGDCODE=$distlgdCode&LabCode=$labCode&DesgId=$desgId&SubOrgId=$subOrdId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = await response.body;
        d2dTeamsCountModel = D2DTeamsCountModel.fromJson(jsonDecode(resp));
      } else {
        ToastManager.toast('Failed getting getD2dTeamsCount');
      }
    } catch (e, st) {
      debugPrint('getD2dTeamsCount error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      update();
    }
  }

  Future<void> getCampTypeList({bool showLoader = true}) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetCampTypeByChannelPartner}?CatagoryID=1";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = await response.body;
        campTypeListModel = CampTypeListModel.fromJson(jsonDecode(resp));
      } else {
        ToastManager.toast('Failed getting getCampTypeList');
      }
    } catch (e, st) {
      debugPrint('getCampTypeList error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      update();
    }
  }

  Future<void> getOrgList(
    String empId,
    String desigId, {
    bool showLoader = true,
  }) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetBindOrg}?UserID=$empId&DESGID=$desigId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = response.body;
        organizationListModel = OrganizationListModel.fromJson(
          jsonDecode(resp),
        );
      } else {
        ToastManager.toast('Failed getting getOrgList');
      }
    } catch (e, st) {
      debugPrint('getOrgList error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      update();
    }
  }

  Future<void> getDivisionList(
    String empId,
    String desigId, {
    bool showLoader = true,
  }) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kBindDivision}?SubOrgId=0&UserID=$empId&DESGID=$desigId";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = response.body;
        d2dTeamsDivisionModel = D2DTeamsDivisionModel.fromJson(
          jsonDecode(resp),
        );
      } else {
        ToastManager.toast('Failed getting getDivisionList');
      }
    } catch (e, st) {
      debugPrint('getDivisionList error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
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
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kBindDistrict}?SubOrgId=$subOrgId&UserID=$empId&DESGID=$desigId&DIVID=$diviD&DISTLGDCODE=$distlgCODE";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = await response.body;
        bindDistrictResponse = BindDistrictResponse.fromJson(jsonDecode(resp));
      } else {
        ToastManager.toast('Failed getting getDistrictList');
      }
    } catch (e, st) {
      debugPrint('getDistrictList error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> getLabList(String distlgCODE) async {
    ToastManager.showLoader();
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetLab}?DISTLGDCODE=$distlgCODE";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);

      if (response.statusCode == 200) {
        var resp = await response.body;
        d2dTeamsLabModel = D2DTeamsLabModel.fromJson(jsonDecode(resp));
      } else {
        ToastManager.toast('Failed getting getLabList');
      }
    } catch (e, st) {
      debugPrint('getLabList error: $e\n$st');
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
        '0',
        '0',
        '0',
        '0',
        loginResponseModel!.output!.first.dESGID.toString(),
        '0',
        showLoader: false,
      );
      await getD2dTeamsCount(
        loginResponseModel!.output!.first.empCode.toString(),
        '0',
        '0',
        '0',
        '0',
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

      // await getDistrictList(
      //   '0',
      //   loginResponseModel!.output!.first.empCode.toString(),
      //   loginResponseModel!.output!.first.dESGID.toString(),
      //   '0',
      //   '0',
      // );
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
        campType,
        divId,
        distlgdCode,
        labCode,
        desigId ?? loginResponseModel!.output!.first.dESGID.toString(),
        subOrgId,
        showLoader: false,
      );
    }
  }

  // Future<void> getS2tAndroidIosDistrictWiseList() async {
  //   ToastManager.showLoader();
  //   try {
  //     final uri = "${APIManager.kTreatmentCount}${APIConstants.kIosCount}";
  //     debugPrint(uri);
  //
  //     final response = await repository.postResponseWithoutBody(uri);
  //
  //     if (response.statusCode == 200) {
  //       var resp = await response.stream.bytesToString();
  //       s2tAndroidIosCountDistrictWiseModel =
  //           S2TAndroidIosCountDistrictWiseModel.fromJson(jsonDecode(resp));
  //     } else {
  //       ToastManager.toast('Failed getting getS2tAndroidIosDistrictWiseList');
  //     }
  //   } catch (e, st) {
  //     debugPrint('getS2tAndroidIosDistrictWiseList error: $e\n$st');
  //     ToastManager.toast('Something went wrong');
  //   } finally {
  //     ToastManager.hideLoader();
  //     update();
  //   }
  // }

  Future<void> getS2tAndroidIosDistrictWiseList({
    bool showLoader = true,
  }) async {
    isS2tAppDistrictLoading = true;
    update();
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri = "${APIManager.kTreatmentCount}${APIConstants.kIosCount}";
      debugPrint(uri);

      final response = await Repository.postResponseWithoutBody(uri,timeout: Duration(minutes: 5));

      if (response.statusCode == 200) {
        var resp = await response.stream.bytesToString();
        s2tAndroidIosCountDistrictWiseModel =
            S2TAndroidIosCountDistrictWiseModel.fromJson(jsonDecode(resp));

        update();
        debugPrint('Data loaded: ${s2tAndroidIosCountDistrictWiseModel?.details.count.length ?? 0} items');
      } else {
        ToastManager.toast('Failed getting getS2tAndroidIosDistrictWiseList');
      }
    } catch (e, st) {
      debugPrint('getS2tAndroidIosDistrictWiseList error: $e\n$st');
      // ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      isS2tAppDistrictLoading = false;
      update();
    }
  }

  Future<void> getLiverTableData(
    Map<String, String> body, {
    bool showLoader = true,
  }) async {
    if (showLoader) {
      // ToastManager.showLoader();
    }
    final uri =
        "${APIManager.kLiverScann}${APIConstants.getLiverScanningTableData}";

    debugPrint(uri);

    var response = await Repository.postFormEncodedRequest(uri, body, {
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    if (response.statusCode == 200) {
      if (showLoader) {
        // ToastManager.hideLoader();
      }

      final data = json.decode(await response.stream.bytesToString());
      if (data['Status'] == 'Success') {
        fibroScanResponse = LiverScanningTableData.fromJson(data);
      }
    } else {
      if (showLoader) {
        ToastManager.hideLoader();
      }

      ToastManager.toast('Failed getting getLiverTableData');
    }
    update();
  }

  void getLiverTableDatadistrictWise(Map<String, String> body) async {
    ToastManager.showLoader();
    final uri =
        "${APIManager.kLiverScann}${APIConstants.getLiverScanningTableDataDistrictWise}";

    debugPrint(uri);

    var response = await Repository.postFormEncodedRequest(uri, body, {
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    if (response.statusCode == 200) {
      ToastManager.hideLoader();

      final data = json.decode(await response.stream.bytesToString());
      if (data['Status'] == 'Success') {
        // CustomMessage.toast(data['message']);
        fibroScanDistirctResponse = FibroScanningDistrictWiseModel.fromJson(
          data,
        );
      }
    } else {
      ToastManager.hideLoader();

      ToastManager.toast('Failed getting getLiverTableDatadistrictWise');
    }
    update();
  }

  /// API: Districts
  Future<void> getDistrict({bool showLoader = true}) async {
    if (showLoader) {
      ToastManager.showLoader();
    }
    try {
      final uri =
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.getDistrictList}";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        districtListResponse = DistrictListResponse.fromJson(response.body);

        if (districtListResponse!.status == 'Success') {
          status = districtListResponse!.message;
          // Keep default selection 'ALL' and code '0'
          selectedDistrict = 'ALL';
          selectedDistrictCode = '0';
        } else {
          status = districtListResponse!.message;
          ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getDistrict');
      }
    } catch (e, st) {
      debugPrint('getDistrict error: $e\n$st');
      ToastManager.toast('Something went wrong');
    } finally {
      if (showLoader) {
        ToastManager.hideLoader();
      }
      update();
    }
  }

  /// API: Table data
  Future<void> _getTableDataInternal(
    String month,
    String year,
    String distCode,
    String campType,
  ) async {
    try {
      final uri =
          "${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetMonthlySurveySiteRequestForOS}?Month=$month&Year=$year&DistCode=$distCode&CampType=$campType";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        campsConductedResponse = CampsConductedResponse.fromJson(response.body);

        if (campsConductedResponse!.status == 'Success') {
          status = campsConductedResponse!.message;
        } else {
          status = campsConductedResponse!.message;
          // ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getTableData');
      }
    } catch (e, st) {
      debugPrint('getTableData error: $e\n$st');
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

  /// Map selected district name -> code for API
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

  /// Set camp type by label
  void setCampTypeFromLabel(String? label) {
    final lbl = (label ?? 'ALL CAMP').trim().toUpperCase();
    selectedCampType = lbl;
    update();
  }

  Future<void> refreshTableForCurrentFilters() async {
    if (fetching) return;
    fetching = true;
    try {
      await getTableDataWithSkeleton(
        monthParam,
        yearParam,
        selectedDistrictCode,
        selectedCampTypeId,
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
