// ignore_for_file: must_be_immutable, avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamDetailsResponse/InsertDetailsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/Is24By7IsAccountCreatedResponse/GetMyOpratorResponse.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/CallingModules/repository/beneficiary_card_repository.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../../Modules/Json_Class/AttendancesListUsingSiteDetailsIDResponse/AttendancesListUsingSiteDetailsIDResponse.dart';
import '../../../Modules/Json_Class/InsertBeneficiaryCallingLogResponse/InsertBeneficiaryCallingLogResponse.dart';
import '../../../Modules/Json_Class/Is24By7IsAccountCreatedResponse/Is24By7IsAccountCreatedResponse.dart';
import '../../../Modules/Json_Class/OrganisationWiseAPIKeyResponse/OrganisationWiseAPIKeyResponse.dart';
import '../../../Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import '../../../Modules/Json_Class/T2TCallingAPIDetailsResponse/T2TCallingAPIDetailsResponse.dart';
import '../../../Modules/Json_Class/TeamNumberByCampIdAndUserIdListResponse/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../PhysicalExaminationFormScreen/PhysicalExaminationFormScreen.dart';
import 'AssignedD2DPhysicalExaminationPatientRow/AssignedD2DPhysicalExaminationPatientRow.dart';

class AssignedD2DPhysicalExaminationPatientListScreen extends StatefulWidget {
  int dISTLGDCODE;
  int campId;
  String healthScreentype = "";
  String flag;

  AssignedD2DPhysicalExaminationPatientListScreen({
    super.key,
    required this.dISTLGDCODE,
    required this.campId,
    required this.healthScreentype,
    required this.flag,
  });

  @override
  State<AssignedD2DPhysicalExaminationPatientListScreen> createState() =>
      _AssignedD2DPhysicalExaminationPatientListScreenState();
}

class _AssignedD2DPhysicalExaminationPatientListScreenState
    extends State<AssignedD2DPhysicalExaminationPatientListScreen>
    with WidgetsBindingObserver {
  TextEditingController searchTextField = TextEditingController();

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
  List<AttendancesListUsingSiteDetailsIDOutput> patientList = [];
  List<AttendancesListUsingSiteDetailsIDOutput> searchPatientList = [];
  APIManager apiManager = APIManager();
  bool isLoading = false;

  String apiKeyForMyOperator = '';

  String companyID = '';

  String public_IVR_ID = '';

  String secrateToken = '';

  String? typeForMyOperator = '';

  // int agentId = 0;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    bMobile = DataProvider().getParsedUserData()?.output?.first.bMobile ?? "";
    agentID = DataProvider().getParsedUserData()?.output?.first.agentID ?? "0";
    getTeamId();

    //GetIs24By7IsAccountCreatedFlag
    getUserCreatedBy24By7();

    //BindOrg
    getOrganizationNew();
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this); // Remove observer
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);

    if (state == AppLifecycleState.resumed) {
      // App came back to foreground (from dialer, background, etc.)
      print("App resumed - Refreshing data");
      getD2DGetUserAttendancesUsingSitedetailsID(); // Refresh your UI data
      setState(() {}); // Update UI if needed
    } else if (state == AppLifecycleState.paused) {
      // App went to background (dialer opened, home pressed, etc.)
      print("App paused");
    } else if (state == AppLifecycleState.inactive) {
      // App is inactive (transitioning state)
      print("App inactive");
    } else if (state == AppLifecycleState.detached) {
      // App is detached
      print("App detached");
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Patient List",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Padding(
            padding: EdgeInsets.fromLTRB(8.h, 8.h, 8.w, 8.h),
            child: Column(
              children: [
                AppTextField(
                  controller: searchTextField,
                  onChange: (value) {
                    value = value.toLowerCase();

                    setState(() {
                      searchPatientList =
                          patientList.where((p) {
                            final name = (p.englishName ?? "").toLowerCase();
                            final reg = (p.regdNo ?? "");

                            return name.contains(value);
                          }).toList();
                    });
                  },
                  hint: 'Patient Name/Registration No',
                  hintStyle: TextStyle(
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  suffixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icSearch,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 10.h),
                Expanded(
                  child: Padding(
                    padding: EdgeInsets.symmetric(horizontal: 10.w),
                    child:
                        isLoading
                            ? const CommonSkeletonPatientList()
                            : ListView.builder(
                              itemCount: searchPatientList.length,
                              itemBuilder: (context, index) {
                                AttendancesListUsingSiteDetailsIDOutput obj =
                                    searchPatientList[index];
                                return GestureDetector(
                                  onTap: () {
                                    int subOrgId = obj.isPhy ?? 0;
                                    String isCall = obj.isCall ?? "0";

                                    if (isCall == "0") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Call to Beneficiary to open PHY. Examination form",
                                        () {
                                          Navigator.pop(context);
                                        },
                                      );
                                    } else {
                                      if (subOrgId == 1) {
                                        // Old UI
                                      } else if (subOrgId == 2) {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder:
                                                (context) =>
                                                    PhysicalExaminationFormScreen(
                                                      regdId: obj.regdId ?? 0,
                                                      campTypeID: 0,
                                                      healthScreentype: "",
                                                    ),
                                          ),
                                        );
                                      } else if (subOrgId == 3) {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder:
                                                (context) =>
                                                    PhysicalExaminationFormScreen(
                                                      regdId: obj.regdId ?? 0,
                                                      campTypeID:
                                                          obj.campTypeID ?? 0,
                                                      healthScreentype:
                                                          widget
                                                              .healthScreentype,
                                                    ),
                                          ),
                                        ).then((value) {
                                          if (widget.flag == "2") {
                                            getD2DGetUserAttendancesUsingSitedetailsID();
                                          } else {
                                            getUserAttendancesUsingSitedetailsID();
                                          }
                                        });
                                      }
                                    }
                                  },
                                  child:
                                      AssignedD2DPhysicalExaminationPatientRow(
                                        obj: obj,
                                        onCallDidPressed: () async {
                                          mobileNo = obj.mobileNo ?? "";
                                          if (isUserCreatedBy == 0) {
                                            await insertCallDetails(
                                              obj.regdId ?? 0,
                                            );
                                          } else {
                                            await getCallingStatusNew(
                                              obj.regdId ?? 0,
                                            );
                                          }
                                        },
                                        serialNumber: index,
                                      ),
                                );
                              },
                            ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void getTeamId() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> params = {
      "campid": widget.campId.toString(),
      "UserID": empCode.toString(),
    };
    apiManager.getTeamNumberByCampIdAndUSerIdAPI(params, apiTeamIdCallBack);
  }

  void apiTeamIdCallBack(
    TeamNumberByCampIdAndUserIdListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      TeamNumberByCampIdOutput? firstobj = response?.output?.first;

      teamId = firstobj?.teamNumber ?? "0";

      if (widget.flag == "2") {
        getD2DGetUserAttendancesUsingSitedetailsID();
      } else {
        teamId = "0";
        getUserAttendancesUsingSitedetailsID();
      }
    } else {
      isLoading = false;
      if (errorMessage == "Team Number Data not found") {
        ToastManager.toast("Your Selected Camp Not Mapped To You");
      } else {
        ToastManager.toast(errorMessage);
      }
    }
    setState(() {});
  }

  void getUserCreatedBy24By7() {
    Map<String, String> params = {"UserID": empCode.toString()};

    apiManager.getUserCreatedBy24By7API(params, apiUserCreatedBy24By7CallBack);
  }

  void apiUserCreatedBy24By7CallBack(
    Is24By7IsAccountCreatedResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      isUserCreatedBy =
          response?.output?.first.is24By7IsAccountCreated ??
          0; //uncomment when sharing to nikita
      // isUserCreatedBy = 2;
    }
    setState(() {});
  }

  void getOrganizationNew() {
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);
    apiManager.getSubOrganizationAPI(params, apiSubOrganizationCallBack);
  }

  void apiSubOrganizationCallBack(
    SubOrganizationResponse? response,
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
    setState(() {});
  }

  Future<void> getMyOperatorKey() async {
    Map<String, String> params = {
      "OrgID": oganizationId.toString(),
      "UserId": "$empCode",
    };

    await apiManager.apiKeyForMyoperator(params, callBackaKeyForMyoperator);
  }

  void callBackaKeyForMyoperator(
    GetMyOperatorResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      try {
        final outputList = response?.output;
        if (outputList != null && outputList is List && outputList.isNotEmpty) {
          final item = outputList.firstWhere((e) => e.isMyOperatorAPI == 1);

          if (item != null) {
            setState(() {
              apiKeyForMyOperator = item.apiKey ?? '';
              companyID = item.companyID ?? '';
              public_IVR_ID = item.publicIVRID ?? '';
              secrateToken = item.secrateToken ?? '';
              typeForMyOperator = item.type?.toString() ?? '';
            });
          }

          debugPrint(apiKeyForMyOperator);
        }
      } catch (e) {
        debugPrint("Error parsing MyOperator response: $e");
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> getApiKey() async {
    Map<String, String> param = {"OrgID": oganizationId.toString()};
    apiManager.getT2TCallingAPIDetailsAPI(
      param,
      apiT2TCallingAPIDetailsCallBack,
    );
  }

  void apiT2TCallingAPIDetailsCallBack(
    T2TCallingAPIDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      apiKey = response?.output?.first.aPIKey ?? "";
      virtualNumber = response?.output?.first.servieNumber ?? "";
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> getApiKeyNew() async {
    Map<String, String> params = {
      "OrgID": oganizationId.toString(),
      "UserId": "$empCode",
    };

    apiManager.getOrganisationWiseAPIKeyAPI(
      params,
      apiOrganisationWiseAPIKeyCallBack,
    );
  }

  void apiOrganisationWiseAPIKeyCallBack(
    OrganisationWiseAPIKeyResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      apiKeyNew = response?.output?.first.aPIKey ?? "";
      virtualNumberNew = response?.output?.first.virtualNo ?? "";
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> getD2DGetUserAttendancesUsingSitedetailsID() async {
    setState(() {
      isLoading = true;
    });
    String urlString = "";
    Map<String, String> jsonObject = {};

    urlString =
        "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";

    if (widget.healthScreentype != "13") {
      if (widget.healthScreentype == "11") {
        jsonObject = {
          "EmpCode": widget.campId.toString(),
          "DistrictId": "0",
          "TestId": widget.healthScreentype,
          "userid": empCode.toString(),
          "teamid": teamId,
        };

        urlString =
            "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDUrineChange}";
      } else {
        if (widget.healthScreentype == "16") {
          jsonObject = {
            "EmpCode": widget.campId.toString(),
            "DistrictId": "0",
            "TestId": "16",
            "userid": empCode.toString(),
            "teamid": teamId,
          };

          urlString =
              "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
        } else {
          jsonObject = {
            "EmpCode": widget.campId.toString(),
            "DistrictId": "0",
            "TestId": "16",
            "userid": empCode.toString(),
            "teamid": teamId,
          };

          urlString =
              "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDAnti}";
        }
      }
    } else {
      jsonObject = {
        "EmpCode": widget.campId.toString(),
        "DistrictId": "0",
        "TestId": "16",
        "userid": empCode.toString(),
        "teamid": teamId,
      };
      urlString =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNewD2DV1}";
    }

    print(jsonObject);

    await apiManager.getD2DPhysicalExaminationDetailsPatientListAPI(
      urlString,
      jsonObject,
      apiD2DD2DPhysicalExaminationDetailsPatientListCallBack,
    );
  }

  void apiD2DD2DPhysicalExaminationDetailsPatientListCallBack(
    AttendancesListUsingSiteDetailsIDResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      patientList = response?.output ?? [];
      searchPatientList = patientList;
    } else {
      patientList = [];
      searchPatientList = patientList;
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  Future<void> getUserAttendancesUsingSitedetailsID() async {
    setState(() {
      isLoading = true;
    });

    String urlString =
        "${APIManager.kD2DBaseURL}${APIConstants.kGetuserAttendanceForSitedetailsIDPhysicalExam}";

    Map<String, String> jsonObject = {
      "SiteDetailId": widget.campId.toString(),
      "DistrictId": "0",
      "TestId": widget.healthScreentype,
      "UserId": empCode.toString(),
      "TeamId": teamId,
    };

    await apiManager.getD2DPhysicalExaminationDetailsPatientListAPI(
      urlString,
      jsonObject,
      apiD2DD2DPhysicalExaminationDetailsPatientListCallBack,
    );
  }

  Future<void> insertCallDetails(int regdId) async {
    ToastManager.showLoader();
    Map<String, String> params = {
      "RegdId": regdId.toString(),
      "TestID": "16",
      "CreatedBy": empCode.toString(),
    };

    await apiManager.insertCallDetailsAPI(params, apiCallBackInsertDetails);
  }

  void apiCallBackInsertDetails(
    InsertDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      // startCall();
      launchPhoneDialer(mobileNo);
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> getCallingStatusNew(int regdId) async {
    ToastManager.showLoader();
    Map<String, String> params = {
      "RegdId": regdId.toString(),
      "TestID": "16",
      "CreatedBy": empCode.toString(),
    };
    await apiManager.insertBeneficiaryCallingLogV2API(
      params,
      apiCallingStatusCallBack,
    );
  }

  void apiCallingStatusCallBack(
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
        // startCall();
        getMyOperatorCall(referenceId);
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> getInitiateCallNew() async {
    if (virtualNumberNew != null && virtualNumberNew!.isEmpty) {
      print(virtualNumberNew);
      ScaffoldMessenger.of(context)
        ..clearSnackBars()
        ..showSnackBar(SnackBar(content: Text("Virtual Number Empty")));
      return;
    }

    ToastManager.showLoader();
    // Map<String, String> params = {
    //   "apiKey": apiKeyNew,
    //   "customernumber":bMobile,
    //   "user_number":mobileNo,
    //   "caller_id":virtualNumberNew!,
    //   "referencestate":referenceId.toString(),
    // };

    var res = await BeneficiaryCardRepository().twentyFourBySevenForWithAgentId(
      {
        "apiKey": apiKeyNew,
        // "customernumber": bMobile,
        // "user_number": mobileNo,
        "customernumber": mobileNo,
        "user_number": bMobile,
        "caller_id": virtualNumberNew!,
        "referencestate": referenceId.toString(),
      },
    );

    var jsonResponse = jsonDecode(res.body);

    final String statusMessage =
        jsonResponse["statusMessage"] ?? "Unknown status message";
    final String data =
        jsonResponse["data"]?.toString() ??
        jsonResponse["response"]?.toString() ??
        "No additional information.";
    if (statusMessage.toLowerCase() == "success") {
      ToastManager.hideLoader();

      showDialog(
        context: context,
        builder:
            (context) => AlertDialog(
              title: const Text("Success"),
              content: Text(data),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Text("OK"),
                ),
              ],
            ),
      );
    } else {
      ToastManager.hideLoader();

      print("failed");
      print("$jsonResponse");
      showDialog(
        context: context,
        builder:
            (context) => AlertDialog(
              title: const Text("Failed"),
              content: Text(statusMessage),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Text("Cancel"),
                ),
                TextButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: const Text("Try Again!"),
                ),
              ],
            ),
      );
    }
  }

  Future<void> getMyOperatorCall(int referenceId) async {
    ToastManager.showLoader();

    Map<String, dynamic> params = {
      "company_id": companyID.toString(),
      "secret_token": secrateToken.toString(),
      "type": typeForMyOperator.toString(),
      "number": "+91$mobileNo",
      "number_2": "+91$bMobile", //uncomment when sharing to nikita
      // "number": "+91${8830378568}",
      // "number_2": "+919673974373",
      "max_call_duration": 0,
      "region": "",
      "caller_id": "",
      "public_ivr_id": public_IVR_ID.toString(),
      "reference_id": referenceId.toString(),
      "group": "",
      "call_hold": false,
    };
    var res = await BeneficiaryCardRepository().myOperatorAPIDetails(
      params,
      apiKeyForMyOperator,
    );
    var jRes = jsonDecode(res.body);
    if (jRes['status'].toString().toLowerCase() == "success") {
      ToastManager.hideLoader();

      showDialog(
        context: context,
        builder:
            (context) => AlertDialog(
              // title: Text(jRes['status']),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Image.asset(icSuccessRoundGreen, width: 100.w),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text("Request accepted successfully"),
                  ),
                  AppButtonWithIcon(
                    mWidth: 100.w,
                    title: 'OK',
                    onTap: () {
                      Navigator.of(context).pop();
                    },
                  ),
                ],
              ),
            ),
      );
      getD2DGetUserAttendancesUsingSitedetailsID(); // Refresh your UI data
    } else {
      ToastManager.hideLoader();

      showDialog(
        context: context,
        builder:
            (context) => AlertDialog(
              title: Text(jRes['status']),
              content: Text(jRes['details']),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Text("Cancel"),
                ),
                TextButton(
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                  child: const Text("Try Again!"),
                ),
              ],
            ),
      );
    }
  }

  Future<void> getInitiateCall() async {
    ToastManager.showLoader();
    Map<String, String> params = {
      "apiKey": apiKey,
      "customernumber": bMobile,
      "servienumber": virtualNumber,
      "format": "json",
      "agentloginid": agentID,
      "referencestate": referenceId.toString(),
    };

    await apiManager.addCallDataAPI(
      params,
      APIConstants.kclickToCall,
      apiInitiateCallBack,
    );
  }

  void apiInitiateCallBack(
    Is24By7IsAccountCreatedResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      // ToastManager.toast(response?.data ?? "Something went wrong");
      // startCall();
      showDialog(
        context: context,
        builder:
            (context) => AlertDialog(
              title: const Text("Success"),
              content: Text(response?.data ?? "Success"),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Text("OK"),
                ),
              ],
            ),
      );
    } else {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("Fail"),
            content: Text(response?.data ?? "Something went wrong"),
            actions: [
              TextButton(
                child: const Text("Cancel"),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
              TextButton(
                child: const Text("Try Again!"),
                onPressed: () {
                  Navigator.pop(context);
                  getInitiateCall();
                },
              ),
            ],
          );
        },
      );

      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> startCall() async {
    await launchUrl(Uri.parse('tel:$mobileNo'));
  }

  Future<void> launchPhoneDialer(String phoneNumber) async {
    try {
      if (phoneNumber.isEmpty) {
        throw Exception('Phone number is empty');
      }

      // Clean the phone number
      String cleanNumber = phoneNumber.replaceAll(RegExp(r'[^\d+]'), '');

      if (cleanNumber.isEmpty) {
        throw Exception('Invalid phone number');
      }

      debugPrint('Calling: $cleanNumber - Context:}');

      if (Platform.isAndroid) {
        // Use AndroidIntent with proper flags to prevent MIUI intent caching
        // FLAG_ACTIVITY_NEW_TASK: Start the activity in a new task
        // FLAG_ACTIVITY_CLEAR_TOP: Clear any existing instance of this activity
        // FLAG_ACTIVITY_SINGLE_TOP: Don't launch if already at top of stack
        final AndroidIntent intent = AndroidIntent(
          action: 'android.intent.action.DIAL',
          data: 'tel:$cleanNumber',
          flags: <int>[
            Flag.FLAG_ACTIVITY_NEW_TASK,
            Flag.FLAG_ACTIVITY_CLEAR_TOP,
            Flag.FLAG_ACTIVITY_SINGLE_TOP,
          ],
        );
        await intent.launch();
        debugPrint('✅ Dialer launched successfully via AndroidIntent');
      } else {
        // For iOS, use url_launcher
        final Uri telUri = Uri.parse('tel:$cleanNumber');

        final bool canLaunch = await canLaunchUrl(telUri);
        if (!canLaunch) {
          throw Exception('No dialer app available');
        }

        final bool launched = await launchUrl(
          telUri,
          mode: LaunchMode.externalApplication,
        );

        if (!launched) {
          throw Exception('Failed to launch dialer');
        }
        debugPrint('✅ Dialer launched successfully via url_launcher');
      }
    } catch (e) {
      debugPrint('❌ Dialer error: $e');

      if (mounted) {
        ToastManager.toast("Cannot open dialer: ${e.toString()}");
      }
    }
  }
}
