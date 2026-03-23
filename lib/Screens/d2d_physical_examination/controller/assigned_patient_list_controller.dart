import 'dart:convert';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Screens/calling_modules/repository/beneficiary_card_repository.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/AttendancesListUsingSiteDetailsIDResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/GetMyOpratorResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/InsertBeneficiaryCallingLogResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/InsertDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/Is24By7IsAccountCreatedResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/OrganisationWiseAPIKeyResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/SubOrgResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/T2TCallingAPIDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import 'dart:io';
import 'package:url_launcher/url_launcher.dart';

import '../../../Modules/constants/images.dart';

class AssignedPatientListController extends GetxController
    with WidgetsBindingObserver {
  final int campId;
  final int dISTLGDCODE;
  final String healthScreentype;
  final String flag;

  AssignedPatientListController({
    required this.campId,
    required this.dISTLGDCODE,
    required this.healthScreentype,
    required this.flag,
  });

  final _api = APIManager();
  final searchController = TextEditingController();

  int empCode = 0;
  int dESGID = 0;
  String teamId = "0";
  int referenceId = 0;
  int isUserCreatedBy = 0;
  String agentID = "0";
  String apiKeyNew = "";
  String apiKey = "";
  String? virtualNumberNew;
  int oganizationId = 0;
  String mobileNo = "";
  String bMobile = "";
  String virtualNumber = "";
  bool isLoading = false;

  String apiKeyForMyOperator = '';
  String companyID = '';
  String public_IVR_ID = '';
  String secrateToken = '';
  String? typeForMyOperator = '';

  List<AttendancesListUsingSiteDetailsIDOutput> patientList = [];
  List<AttendancesListUsingSiteDetailsIDOutput> searchPatientList = [];

  @override
  void onInit() {
    super.onInit();
    WidgetsBinding.instance.addObserver(this);
    final userData = DataProvider().getParsedUserData()?.output?.first;
    empCode = userData?.empCode ?? 0;
    dESGID = userData?.dESGID ?? 0;
    bMobile = userData?.bMobile ?? "";
    agentID = userData?.agentID ?? "0";
    getTeamId();
    getUserCreatedBy24By7();
    getOrganizationNew();
  }

  @override
  void onClose() {
    WidgetsBinding.instance.removeObserver(this);
    searchController.dispose();
    super.onClose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      getD2DGetUserAttendancesUsingSitedetailsID();
    }
  }

  void filterList(String value) {
    value = value.toLowerCase();
    searchPatientList = patientList.where((p) {
      final name = (p.englishName ?? "").toLowerCase();
      return name.contains(value);
    }).toList();
    update();
  }

  // ── Team ID ──────────────────────────────────────────────────────────────

  void getTeamId() {
    isLoading = true;
    update();
    _api.getTeamNumberByCampIdAndUSerIdAPI(
      {"campid": campId.toString(), "UserID": empCode.toString()},
      _onTeamId,
    );
  }

  void _onTeamId(
    TeamNumberByCampIdAndUserIdListResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      teamId = response?.output?.first.teamNumber ?? "0";
      if (flag == "2") {
        getD2DGetUserAttendancesUsingSitedetailsID();
      } else {
        teamId = "0";
        getUserAttendancesUsingSitedetailsID();
      }
    } else {
      isLoading = false;
      final msg = errorMessage == "Team Number Data not found"
          ? "Your Selected Camp Not Mapped To You"
          : errorMessage;
      ToastManager.toast(msg);
    }
    update();
  }

  // ── 24x7 Account Created ─────────────────────────────────────────────────

  void getUserCreatedBy24By7() {
    _api.getUserCreatedBy24By7API(
      {"UserID": empCode.toString()},
      _onUserCreatedBy24By7,
    );
  }

  void _onUserCreatedBy24By7(
    Is24By7IsAccountCreatedResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      isUserCreatedBy =
          response?.output?.first.is24By7IsAccountCreated ?? 0;
    }
    update();
  }

  // ── Organisation / API keys ──────────────────────────────────────────────

  void getOrganizationNew() {
    _api.getSubOrganizationAPI(
      {"UserID": empCode.toString(), "DESGID": dESGID.toString()},
      _onSubOrganization,
    );
  }

  void _onSubOrganization(
    SubOrgResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      oganizationId = response?.output?.first.subOrgId ?? 0;
      await getApiKey();
      await getApiKeyNew();
      await getMyOperatorKey();
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  Future<void> getMyOperatorKey() async {
    await _api.apiKeyForMyoperator(
      {"OrgID": oganizationId.toString(), "UserId": "$empCode"},
      _onMyOperatorKey,
    );
  }

  void _onMyOperatorKey(
    GetMyOperatorResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      try {
        final outputList = response?.output;
        if (outputList != null && outputList.isNotEmpty) {
          final item = outputList.firstWhere((e) => e.isMyOperatorAPI == 1);
          apiKeyForMyOperator = item.apiKey ?? '';
          companyID = item.companyID ?? '';
          public_IVR_ID = item.publicIVRID ?? '';
          secrateToken = item.secrateToken ?? '';
          typeForMyOperator = item.type?.toString() ?? '';
        }
      } catch (e) {
        debugPrint("Error parsing MyOperator response: $e");
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  Future<void> getApiKey() async {
    _api.getT2TCallingAPIDetailsAPI(
      {"OrgID": oganizationId.toString()},
      _onT2TCallingAPIDetails,
    );
  }

  void _onT2TCallingAPIDetails(
    T2TCallingAPIDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      apiKey = response?.output?.first.aPIKey ?? "";
      virtualNumber = response?.output?.first.servieNumber ?? "";
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  Future<void> getApiKeyNew() async {
    _api.getOrganisationWiseAPIKeyAPI(
      {"OrgID": oganizationId.toString(), "UserId": "$empCode"},
      _onOrganisationWiseAPIKey,
    );
  }

  void _onOrganisationWiseAPIKey(
    OrganisationWiseAPIKeyResponse response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      apiKeyNew = response.output?.first.aPIKey ?? "";
      virtualNumberNew = response.output?.first.virtualNo ?? "";
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  // ── Patient list ─────────────────────────────────────────────────────────

  Future<void> getD2DGetUserAttendancesUsingSitedetailsID() async {
    isLoading = true;
    update();

    String urlString;
    Map<String, String> jsonObject;

    if (healthScreentype != "13") {
      if (healthScreentype == "11") {
        jsonObject = {
          "EmpCode": campId.toString(),
          "DistrictId": "0",
          "TestId": healthScreentype,
          "userid": empCode.toString(),
          "teamid": teamId,
        };
        urlString =
            "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDUrineChange}";
      } else if (healthScreentype == "16") {
        jsonObject = {
          "EmpCode": campId.toString(),
          "DistrictId": "0",
          "TestId": "16",
          "userid": empCode.toString(),
          "teamid": teamId,
        };
        urlString =
            "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
      } else {
        jsonObject = {
          "EmpCode": campId.toString(),
          "DistrictId": "0",
          "TestId": "16",
          "userid": empCode.toString(),
          "teamid": teamId,
        };
        urlString =
            "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDAnti}";
      }
    } else {
      jsonObject = {
        "EmpCode": campId.toString(),
        "DistrictId": "0",
        "TestId": "16",
        "userid": empCode.toString(),
        "teamid": teamId,
      };
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
    }

    await _api.getD2DPhysicalExaminationDetailsPatientListAPI(
      urlString,
      jsonObject,
      _onPatientList,
    );
  }

  Future<void> getUserAttendancesUsingSitedetailsID() async {
    isLoading = true;
    update();

    final urlString =
        "${APIManager.kD2DBaseURL}${APIConstants.kGetuserAttendanceForSitedetailsIDPhysicalExam}";
    final jsonObject = {
      "SiteDetailId": campId.toString(),
      "DistrictId": "0",
      "TestId": healthScreentype,
      "UserId": empCode.toString(),
      "TeamId": teamId,
    };

    await _api.getD2DPhysicalExaminationDetailsPatientListAPI(
      urlString,
      jsonObject,
      _onPatientList,
    );
  }

  void _onPatientList(
    AttendancesListUsingSiteDetailsIDResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      patientList = response?.output ?? [];
    } else {
      patientList = [];
      ToastManager.toast(errorMessage);
    }
    searchPatientList = patientList;
    isLoading = false;
    update();
  }

  void refreshAfterNav() {
    if (flag == "2") {
      getD2DGetUserAttendancesUsingSitedetailsID();
    } else {
      getUserAttendancesUsingSitedetailsID();
    }
  }

  // ── Call flow ────────────────────────────────────────────────────────────

  Future<void> insertCallDetails(int regdId) async {
    ToastManager.showLoader();
    await _api.insertCallDetailsAPI(
      {"RegdId": regdId.toString(), "TestID": "16", "CreatedBy": empCode.toString()},
      _onInsertCallDetails,
    );
  }

  void _onInsertCallDetails(
    InsertDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      launchPhoneDialer(mobileNo);
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  Future<void> getCallingStatusNew(int regdId) async {
    ToastManager.showLoader();
    await _api.insertBeneficiaryCallingLogV2API(
      {"RegdId": regdId.toString(), "TestID": "16", "CreatedBy": empCode.toString()},
      _onCallingStatus,
    );
  }

  void _onCallingStatus(
    InsertBeneficiaryCallingLogResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      referenceId = response?.iD ?? 0;
      if (isUserCreatedBy == 1) {
        if (agentID == "0") {
          await getInitiateCallNew();
        } else {
          await getInitiateCall();
        }
      } else if (isUserCreatedBy == 2) {
        getMyOperatorCall(referenceId);
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    update();
  }

  Future<void> getInitiateCallNew() async {
    if (virtualNumberNew != null && virtualNumberNew!.isEmpty) {
      Get.snackbar("Error", "Virtual Number Empty");
      return;
    }

    ToastManager.showLoader();
    final res = await BeneficiaryCardRepository().twentyFourBySevenForWithAgentId({
      "apiKey": apiKeyNew,
      "customernumber": mobileNo,
      "user_number": bMobile,
      "caller_id": virtualNumberNew!,
      "referencestate": referenceId.toString(),
    });

    ToastManager.hideLoader();
    final jsonResponse = jsonDecode(res.body);
    final String statusMessage = jsonResponse["statusMessage"] ?? "Unknown status message";
    final String data =
        jsonResponse["data"]?.toString() ??
        jsonResponse["response"]?.toString() ??
        "No additional information.";

    if (statusMessage.toLowerCase() == "success") {
      Get.dialog(AlertDialog(
        title: const Text("Success"),
        content: Text(data),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text("OK")),
        ],
      ));
    } else {
      Get.dialog(AlertDialog(
        title: const Text("Failed"),
        content: Text(statusMessage),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text("Cancel")),
          TextButton(onPressed: () => Get.back(), child: const Text("Try Again!")),
        ],
      ));
    }
  }

  Future<void> getMyOperatorCall(int refId) async {
    ToastManager.showLoader();
    final res = await BeneficiaryCardRepository().myOperatorAPIDetails(
      {
        "company_id": companyID,
        "secret_token": secrateToken,
        "type": typeForMyOperator,
        "number": "+91$mobileNo",
        "number_2": "+91$bMobile",
        "max_call_duration": 0,
        "region": "",
        "caller_id": "",
        "public_ivr_id": public_IVR_ID,
        "reference_id": refId.toString(),
        "group": "",
        "call_hold": false,
      },
      apiKeyForMyOperator,
    );
    ToastManager.hideLoader();
    final jRes = jsonDecode(res.body);
    if (jRes['status'].toString().toLowerCase() == "success") {
      Get.dialog(AlertDialog(
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Image.asset(icSuccessRoundGreen, width: 100),
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Text("Request accepted successfully"),
            ),
            AppButtonWithIcon(
              mWidth: 100,
              title: 'OK',
              onTap: () => Get.back(),
            ),
          ],
        ),
      ));
      getD2DGetUserAttendancesUsingSitedetailsID();
    } else {
      Get.dialog(AlertDialog(
        title: Text(jRes['status']),
        content: Text(jRes['details']),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text("Cancel")),
          TextButton(onPressed: () => Get.back(), child: const Text("Try Again!")),
        ],
      ));
    }
  }

  Future<void> getInitiateCall() async {
    ToastManager.showLoader();
    await _api.addCallDataAPI(
      {
        "apiKey": apiKey,
        "customernumber": bMobile,
        "servienumber": virtualNumber,
        "format": "json",
        "agentloginid": agentID,
        "referencestate": referenceId.toString(),
      },
      APIConstants.kclickToCall,
      _onInitiateCall,
    );
  }

  void _onInitiateCall(
    Is24By7IsAccountCreatedResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      Get.dialog(AlertDialog(
        title: const Text("Success"),
        content: Text(response?.data ?? "Success"),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text("OK")),
        ],
      ));
    } else {
      Get.dialog(AlertDialog(
        title: const Text("Fail"),
        content: Text(response?.data ?? "Something went wrong"),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text("Cancel")),
          TextButton(
            onPressed: () {
              Get.back();
              getInitiateCall();
            },
            child: const Text("Try Again!"),
          ),
        ],
      ));
    }
    update();
  }

  Future<void> launchPhoneDialer(String phoneNumber) async {
    try {
      if (phoneNumber.isEmpty) throw Exception('Phone number is empty');
      final cleanNumber = phoneNumber.replaceAll(RegExp(r'[^\d+]'), '');
      if (cleanNumber.isEmpty) throw Exception('Invalid phone number');

      if (Platform.isAndroid) {
        final intent = AndroidIntent(
          action: 'android.intent.action.DIAL',
          data: 'tel:$cleanNumber',
          flags: [
            Flag.FLAG_ACTIVITY_NEW_TASK,
            Flag.FLAG_ACTIVITY_CLEAR_TOP,
            Flag.FLAG_ACTIVITY_SINGLE_TOP,
          ],
        );
        await intent.launch();
      } else {
        final telUri = Uri.parse('tel:$cleanNumber');
        if (!await canLaunchUrl(telUri)) throw Exception('No dialer app available');
        await launchUrl(telUri, mode: LaunchMode.externalApplication);
      }
    } catch (e) {
      ToastManager.toast("Cannot open dialer: ${e.toString()}");
    }
  }
}