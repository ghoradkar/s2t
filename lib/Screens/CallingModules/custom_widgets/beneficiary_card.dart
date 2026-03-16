// ignore_for_file: use_build_context_synchronously, avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../calling/models/APIKeyForMyOperatorModel.dart';
import '../calling/models/BeneficiaryResponseModel.dart';
import '../calling/models/api_key_agent_model.dart';
import '../calling/models/api_key_call_patching_model.dart';
import '../calling/models/organization_model.dart';
import '../calling/models/user_created_model.dart';
import '../routes/app_routes.dart';
import 'bloc/beneficiary_card_bloc_bloc.dart';
import 'repository/beneficiary_card.dart';

class BeneficiaryCard extends StatefulWidget {
  final BeneficiaryOutput beneficiary;

  final int empCode;
  final int desId;
  final int agentId;
  final int mobileNo;
  final String? myOperatorUserId;
  final int index;
  final Future<void> Function()? onAppointmentSaved;

  BeneficiaryCard({
    super.key,
    required this.beneficiary,
    required this.index,
    this.onAppointmentSaved,
    int? empCode,
    int? desId,
    int? mobileNo,
    String? myOperatorUserId,
    int? agentId,
  }) : empCode =
           empCode ??
           DataProvider().getParsedUserData()?.output?[0].empCode ??
           0,
       desId =
           desId ?? DataProvider().getParsedUserData()?.output?[0].dESGID ?? 0,
       mobileNo =
           mobileNo ??
           int.tryParse(
             DataProvider()
                     .getParsedUserData()
                     ?.output?[0]
                     .bMobile
                     ?.toString() ??
                 '',
           ) ??
           0,
       myOperatorUserId =
           myOperatorUserId ??
           DataProvider().getParsedUserData()?.output?[0].myOperatorUserID ??
           "",
       agentId =
           agentId ??
           int.tryParse(
             DataProvider()
                     .getParsedUserData()
                     ?.output?[0]
                     .agentID
                     ?.toString() ??
                 '',
           ) ??
           0;

  @override
  State<BeneficiaryCard> createState() => _BeneficiaryCardState();
}

class _BeneficiaryCardState extends State<BeneficiaryCard> {
  OrganizationModel? organizationModel;
  UserCreatedModel? userCreatedModel;
  ApiKeyForAgentIDModel? apiKeyForAgentIDModel;
  APIKeyForMyOperatorModel? apiKeyForMyOperatorModel;
  ApiKeyForCallPAtchingModel? apiKeyForCallPAtchingModel;

  final List<GetOrganizationOutput> _organizationList = [];
  final List<UserCreatedOutput> _usercreatedBy = [];
  final List<APIKeyForAgentOutput> _apiKeyForAgentList = [];

  // final List<APIKeyForMyOperatorModelOutput> _apiKeyFormyOperator = [];
  // final List<APIKeyForCallPAtchingOutput> _apiKeyForCallPatchingList = [];

  int organizationId = 0;
  int isUserCreatedBy = 0;
  int referanceId = 0;
  String virtualNumber = "";
  String virtualNumberForVodaphone = "";
  int channelFlag = 0;
  int dtmfflag = 0;
  int recordingflag = 0;
  String ModifiedOn = "";
  String Token = "";
  String? virtualNumberForCallPAtching;
  String apikeyForAgent = "";
  String apiKeyForMyOperator = "";
  String companyID = "";
  String public_IVR_ID = "";
  String secrateToken = "";
  String typeForMyOperator = "";
  String apikeyForCAllPAtching = "";
  bool _isCallingLoading = false;

  // Cache static decorations to avoid recreating them
  // static final _cardDecoration = BoxDecoration(
  //   boxShadow: [
  //     BoxShadow(
  //       blurRadius: 10,
  //       spreadRadius: 0,
  //       offset: const Offset(0, 1),
  //       color: const Color(0xFF000000).withValues(alpha: 0.15),
  //     ),
  //   ],
  //   borderRadius: BorderRadius.circular(10),
  //   color: getStatusColor(),
  // );

  static final _phoneButtonDecoration = BoxDecoration(
    color: kPrimaryColor.withValues(alpha: 0.85),
    borderRadius: BorderRadius.circular(50),
  );

  static final _eyeButtonDecoration = BoxDecoration(
    color: kPrimaryColor.withValues(alpha: 0.85),
    borderRadius: BorderRadius.circular(10),
  );

  // bool _isinsertCallNew = false;

  @override
  void initState() {
    //BindOrg
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      context.read<BeneficiaryCardBlocBloc>().add(
        GetOrganization(payload: {"DESGID": widget.desId.toString()}),
      );

      //GetIs24By7IsAccountCreatedFlag
      context.read<BeneficiaryCardBlocBloc>().add(
        GetUserCreatedBy(payload: {"UserID": widget.empCode.toString()}),
      );
    });

    super.initState();
  }

  // BeneficiaryCard({required this.beneficiary});
  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return BlocListener<BeneficiaryCardBlocBloc, BeneficiaryCardBlocState>(
      listener: (context, state) {
        //BindOrg
        if (state.organizationStatus.isSuccess) {
          organizationModel = OrganizationModel.fromJson(
            jsonDecode(state.organizationResponse),
          );
          _organizationList.clear();

          WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
            context.read<BeneficiaryCardBlocBloc>().add(
              GetAPIKeyForAgentId(
                payload: {
                  "OrgId": (_organizationList[0].subOrgId ?? 0).toString(),
                },
              ),
            );

            context.read<BeneficiaryCardBlocBloc>().add(
              GetAPIKeyForCallPAtching(
                payload: {
                  "OrgId": (_organizationList[0].subOrgId ?? 0).toString(),
                  "UserId": widget.empCode.toString(),
                },
              ),
            );

            var payload = {
              "OrgId": (_organizationList[0].subOrgId ?? 0).toString(),
              "UserId": widget.empCode.toString(),
            };

            print(jsonEncode(payload));

            var res = await BeneficiaryCardRepository().apiKeyForMyoperator(
              payload,
            );

            var jRes = jsonDecode(res.body);
            if (jRes['status'].toString().toLowerCase() == "success") {
              try {
                final outputList = jRes['output'];
                if (outputList != null &&
                    outputList is List &&
                    outputList.isNotEmpty) {
                  final item = outputList.firstWhere(
                    (e) => e['IsMyOperatorAPI'] == 1,
                    orElse: () => null,
                  );

                  if (item != null) {
                    setState(() {
                      apiKeyForMyOperator = item['APIKey'] ?? '';
                      companyID = item['CompanyID'] ?? '';
                      public_IVR_ID = item['Public_IVR_ID'] ?? '';
                      secrateToken = item['SecrateToken'] ?? '';
                      typeForMyOperator = item['Type']?.toString() ?? '';
                    });
                  }

                  debugPrint(apiKeyForMyOperator);
                }
              } catch (e) {
                debugPrint("Error parsing MyOperator response: $e");
              }
            } else {
              ToastManager.showAlertDialog(context, jRes['details'], () {
                Navigator.pop(context);
              });
            }
          });

          setState(() {
            var orgdata = organizationModel?.output;

            if (orgdata != null && orgdata.isNotEmpty) {
              _organizationList.addAll(orgdata);

              organizationId = _organizationList[0].subOrgId ?? 0;
            } else {
              debugPrint(" empty or null.");
            }
          });
        }

        //GetIs24By7IsAccountCreatedFlag
        if (state.usercreatedStatus.isSuccess) {
          userCreatedModel = UserCreatedModel.fromJson(
            jsonDecode(state.userCreatedResponse),
          );
          _usercreatedBy.clear();

          setState(() {
            var orgdata = userCreatedModel?.output;

            if (orgdata != null && orgdata.isNotEmpty) {
              _usercreatedBy.addAll(orgdata);

              isUserCreatedBy = orgdata[0].is24By7IsAccountCreated ?? 0;
              print(isUserCreatedBy.toString());
              // isUserCreatedBy = 2;
            } else {
              debugPrint("empty or null.");
            }
          });
        }

        if (state.apikeyAgentIdStatus.isSuccess) {
          setState(() {
            apiKeyForAgentIDModel = ApiKeyForAgentIDModel.fromJson(
              jsonDecode(state.apiKeyAgentResponse),
            );
            _apiKeyForAgentList.clear();
            var apiKeyData = apiKeyForAgentIDModel?.output;

            if (apiKeyData != null && apiKeyData.isNotEmpty) {
              _apiKeyForAgentList.addAll(apiKeyData);

              apikeyForAgent = _apiKeyForAgentList[0].aPIKey ?? "";
              virtualNumber = _apiKeyForAgentList[0].servieNumber ?? "";
            } else {
              debugPrint(" empty or null.");
            }
          });
        }

        if (state.getMyoperatorDetailsStatus.isSuccess) {
          final responseStr = state.getMyoperatorDetailsResponse;

          if (responseStr.isNotEmpty) {
            try {
              final jRes = jsonDecode(responseStr);

              final outputList = jRes['output'];
              if (outputList != null &&
                  outputList is List &&
                  outputList.isNotEmpty) {
                final item = outputList.firstWhere(
                  (e) => e['IsMyOperatorAPI'] == 1,
                  orElse: () => null,
                );

                if (item != null) {
                  setState(() {
                    apiKeyForMyOperator = item['APIKey'] ?? '';
                    companyID = item['CompanyID'] ?? '';
                    public_IVR_ID = item['Public_IVR_ID'] ?? '';
                    secrateToken = item['SecrateToken'] ?? '';
                    typeForMyOperator = item['Type']?.toString() ?? '';
                  });
                }

                debugPrint(apiKeyForMyOperator);
              }
            } catch (e) {
              debugPrint("Error parsing MyOperator response: $e");
            }
          }
        }

        if (state.apikeyCallPatchingStatus.isSuccess) {
          if (state.apiKeyCallPatchingResponse.isNotEmpty) {
            var resJ = jsonDecode(state.apiKeyCallPatchingResponse);
            print(resJ['output'][0]['VirtualNo']);
            setState(() {
              apikeyForCAllPAtching = resJ['output'][0]['APIKey'];
              virtualNumberForCallPAtching = resJ['output'][0]['ServieNumber'];
              debugPrint(virtualNumberForCallPAtching);
              // virtualNumberForCallPAtching = resJ['output'][0]['VirtualNo'];
            });
          }
        }
      },
      child: Padding(
        padding: EdgeInsets.only(
          top: responsiveHeight(16),
          bottom: responsiveHeight(4),
          left: responsiveWidth(15),
          right: responsiveWidth(15),
        ),
        child: InkWell(
          onTap: () async {
            final result = await Navigator.of(context).pushNamed(
              AppRoutes.appointmentConfirmation,
              arguments: widget.beneficiary,
            );
            if (result == true) {
              await widget.onAppointmentSaved?.call();
            }
          },
          child: Container(
            decoration: BoxDecoration(
              // boxShadow: [
              //   BoxShadow(
              //     blurRadius: 10,
              //     spreadRadius: 0,
              //     offset: const Offset(0, 1),
              //     color: const Color(0xFF000000),
              //   ),
              // ],
              borderRadius: BorderRadius.circular(10),
              color: getStatusColor(
                widget.beneficiary.groupID ?? 0,
              ).withValues(alpha: 0.1),
            ),
            child: Material(
              color: Colors.transparent,
              child: InkWell(
                borderRadius: BorderRadius.circular(responsiveHeight(10)),

                child: Ink(
                  child: Padding(
                    padding: EdgeInsets.symmetric(
                      vertical: 8.h,
                      horizontal: 8.w,
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            Text(
                              "${(widget.index + 1).toString()}. ",
                              style: TextStyle(
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                            ).paddingOnly(left: 4.w),
                            SizedBox(width: 2.w),
                            Expanded(
                              child: Text(
                                widget.beneficiary.beneficiaryName
                                        ?.toUpperCase() ??
                                    '',
                                style: TextStyle(
                                  fontSize: responsiveFont(14),
                                  fontWeight: FontWeight.w600,
                                  fontFamily: FontConstants.interFonts,
                                ),
                              ),
                            ),

                            GestureDetector(
                              behavior: HitTestBehavior.translucent,
                              onTap: () async {
                                if (_isCallingLoading) return;
                                setState(() {
                                  _isCallingLoading = true;
                                });
                                if (isUserCreatedBy == 0) {
                                  final canCall = await canMakePhoneCall(
                                    widget.beneficiary.mobile!,
                                  );
                                  if (!canCall) {
                                    if (mounted) {
                                      setState(() {
                                        _isCallingLoading = false;
                                      });
                                    }
                                    return;
                                  }

                                  var res = await BeneficiaryCardRepository()
                                      .insertCallinglog({
                                        "AssignCallID":
                                            widget.beneficiary.assignCallID
                                                .toString(),
                                        "CreatedBy": widget.empCode.toString(),
                                      });

                                  String resString = res.body;
                                  var jsonResponse = jsonDecode(resString);

                                  if (jsonResponse['status'] == 'Success') {
                                    await launchPhoneDialer(
                                      widget.beneficiary.mobile ?? '',
                                      context: 'Direct call',
                                    );
                                  }
                                  if (mounted) {
                                    setState(() {
                                      _isCallingLoading = false;
                                    });
                                  }
                                } else {
                                  var res = await BeneficiaryCardRepository()
                                      .insertCallinglogNew({
                                        "AssignCallID":
                                            widget.beneficiary.assignCallID
                                                .toString(),
                                        "CreatedBy": widget.empCode.toString(),
                                      });
                                  String resString = res.body;
                                  var jsonResponse = jsonDecode(resString);
                                  referanceId = jsonResponse["ID"];
                                  if (jsonResponse['status'] == 'Success') {
                                    if (isUserCreatedBy == 1) {
                                      if (widget.agentId != 0) {
                                        var res =
                                            await BeneficiaryCardRepository()
                                                .twentyFourBySevenForWithAgentId(
                                                  {
                                                    "apiKey": apikeyForAgent,
                                                    "customernumber":
                                                        widget.empCode
                                                            .toString(),
                                                    "servienumber":
                                                        virtualNumber,
                                                    "format": "json",
                                                    "agentloginid":
                                                        widget.agentId
                                                            .toString(),
                                                    "referencestate":
                                                        referanceId.toString(),
                                                  },
                                                );

                                        var jsonResponse = jsonDecode(res.body);

                                        final String statusMessage =
                                            jsonResponse["statusMessage"] ??
                                            "Unknown status message";
                                        final String data =
                                            jsonResponse["data"]?.toString() ??
                                            jsonResponse["response"]
                                                ?.toString() ??
                                            "No additional information.";

                                        if (statusMessage.toLowerCase() ==
                                            "success") {
                                          showDialog(
                                            context: context,
                                            builder:
                                                (context) => AlertDialog(
                                                  title: const Text("Success"),
                                                  content: Text(data),
                                                  actions: [
                                                    AppActiveButton(
                                                      buttontitle: "OK",
                                                      onTap: () {
                                                        Navigator.of(
                                                          context,
                                                        ).pop();
                                                      },
                                                    ),
                                                  ],
                                                ),
                                          );
                                        } else {
                                          print("failed");
                                          print("$jsonResponse");
                                          ToastManager.showAlertDialog(
                                            context,
                                            statusMessage,
                                            () {
                                              Navigator.pop(context);
                                            },
                                          );
                                        }
                                        if (mounted) {
                                          setState(() {
                                            _isCallingLoading = false;
                                          });
                                        }
                                      } else {
                                        if (virtualNumberForCallPAtching !=
                                                null &&
                                            virtualNumberForCallPAtching!
                                                .isEmpty) {
                                          print(virtualNumberForCallPAtching);
                                          ScaffoldMessenger.of(context)
                                            ..clearSnackBars()
                                            ..showSnackBar(
                                              SnackBar(
                                                content: Text(
                                                  "Virtual Number Empty",
                                                ),
                                              ),
                                            );
                                          return;
                                        }

                                        var res = await BeneficiaryCardRepository()
                                            .twentyFourBySevenForWithCallPatching({
                                              "apiKey": apikeyForCAllPAtching,
                                              "customer_number":
                                                  widget.mobileNo.toString(),
                                              "user_number":
                                                  widget.beneficiary.mobile,
                                              "caller_id":
                                                  virtualNumberForCallPAtching,
                                              "reference_id":
                                                  referanceId.toString(),
                                            });
                                        // .twentyFourBySevenForWithCallPatching({
                                        //   "apiKey": apikeyForCAllPAtching,
                                        //   "customernumber":
                                        //       widget.mobileNo.toString(),
                                        //   "user_number":
                                        //       widget.beneficiary.mobile,
                                        //   "caller_id":
                                        //       virtualNumberForCallPAtching,
                                        //   "referencestate":
                                        //       referanceId.toString(),
                                        // });

                                        var jsonResponse = jsonDecode(res.body);

                                        final String statusMessage =
                                            jsonResponse["statusMessage"] ??
                                            "Unknown status message";

                                        if (statusMessage.toLowerCase() ==
                                            "success") {
                                          await launchPhoneDialer(
                                            virtualNumberForCallPAtching!,
                                            context: 'Call Patching',
                                          );
                                        } else {
                                          print("failed");
                                          print("$jsonResponse");

                                          ToastManager.showAlertDialog(
                                            context,
                                            statusMessage,
                                            () {
                                              Navigator.pop(context);
                                            },
                                          );
                                        }
                                      }
                                    } else if (isUserCreatedBy == 2) {
                                      // var payload = {
                                      //   "company_id": companyID.toString(),
                                      //   "secret_token": secrateToken.toString(),
                                      //   "type": typeForMyOperator.toString(),
                                      //   "user_id":
                                      //       widget.myOperatorUserId.toString(),
                                      //   "number":
                                      //       "+91${widget.beneficiary.mobile}",
                                      //   // "number": "+91${8999602937}",
                                      //   "public_ivr_id":
                                      //       public_IVR_ID.toString(),
                                      //   "reference_id": referanceId.toString(),
                                      // };

                                      var payload = {
                                        "company_id": companyID.toString(),
                                        "secret_token": secrateToken.toString(),
                                        "type": typeForMyOperator.toString(),
                                        // "user_id":
                                        //     widget.myOperatorUserId.toString(),
                                        "number":
                                            "+91${widget.beneficiary.mobile}",
                                        "number_2":
                                            "+91${DataProvider().getParsedUserData()?.output?[0].bMobile?.toString() ?? ''}",
                                        "max_call_duration": 0,
                                        "region": "",
                                        "caller_id": "",
                                        "public_ivr_id":
                                            public_IVR_ID.toString(),
                                        "reference_id": referanceId.toString(),
                                        "group": "",
                                        "call_hold": false,
                                      };

                                      // var payload = {
                                      //   "company_id": companyID.toString(),
                                      //   "secret_token": secrateToken.toString(),
                                      //   "type": typeForMyOperator.toString(),
                                      //   "number": "+91${9673974373}",
                                      //   "number_2": "+918830378568",
                                      //   "max_call_duration": 0,
                                      //   "region": "",
                                      //   "caller_id": "",
                                      //   "public_ivr_id": public_IVR_ID.toString(),
                                      //   "reference_id": referanceId.toString(),
                                      //   "group": "",
                                      //   "call_hold": false,
                                      // };

                                      var apiKey = apiKeyForMyOperator;

                                      print(jsonEncode(payload));
                                      print(apiKeyForMyOperator);
                                      var res =
                                          await BeneficiaryCardRepository()
                                              .myOperatorAPIDetails(
                                                payload,
                                                apiKey,
                                              );

                                      var jRes = jsonDecode(res.body);
                                      if (jRes['status']
                                              .toString()
                                              .toLowerCase() ==
                                          "success") {
                                        showDialog(
                                          context: context,
                                          builder:
                                              (context) => AlertDialog(
                                                // title: Text(jRes['status']),
                                                content: Column(
                                                  mainAxisSize:
                                                      MainAxisSize.min,
                                                  children: [
                                                    Image.asset(
                                                      icSuccessRoundGreen,
                                                      width: 100.w,
                                                    ),
                                                    Padding(
                                                      padding:
                                                          const EdgeInsets.all(
                                                            8.0,
                                                          ),
                                                      child: Text(
                                                        "Request accepted successfully",
                                                      ),
                                                    ),
                                                    AppButtonWithIcon(
                                                      mWidth: 80.w,
                                                      title: 'OK',
                                                      onTap: () {
                                                        Navigator.of(
                                                          context,
                                                        ).pop();
                                                      },
                                                    ),
                                                  ],
                                                ),
                                              ),
                                        );
                                      } else {
                                        ToastManager.showAlertDialog(
                                          context,
                                          jRes['details'],
                                          () {
                                            Navigator.pop(context);
                                          },
                                        );
                                      }
                                    } else if (isUserCreatedBy == 3) {
                                      try {
                                        var res =
                                            await BeneficiaryCardRepository()
                                                .vodaphoneApiDetails();
                                        String resString = res.body;

                                        var jsonResponse = jsonDecode(
                                          resString,
                                        );

                                        if (jsonResponse['status'] ==
                                            'Success') {
                                          var output =
                                              jsonResponse['output'][0];

                                          virtualNumberForVodaphone =
                                              output["virtualNumber"];
                                          channelFlag = output["channelflag"];
                                          dtmfflag = output["dtmfflag"];
                                          ModifiedOn = output["ModifiedOn"];
                                          Token = output["Token"];
                                          recordingflag =
                                              output["recordingflag"];

                                          // ===== Check if ModifiedOn is older than 24 hours =====
                                          if (ModifiedOn.toString()
                                              .isNotEmpty) {
                                            try {
                                              final sdf = DateFormat(
                                                "dd/MM/yyyy",
                                              );
                                              final modifiedDate = sdf.parse(
                                                ModifiedOn,
                                              );
                                              final currentDate =
                                                  DateTime.now();

                                              final diffInHours =
                                                  currentDate
                                                      .difference(modifiedDate)
                                                      .inHours;
                                              if (diffInHours >= 24) {
                                                var payload = {
                                                  "username": "c2c_tn",
                                                  "password": "c2c_tn",
                                                };

                                                print(
                                                  "Request Payload: ${jsonEncode(payload)}",
                                                );

                                                try {
                                                  var res =
                                                      await BeneficiaryCardRepository()
                                                          .getVodaphoneAPIKey(
                                                            payload,
                                                          );

                                                  if (res.statusCode == 200) {
                                                    var jRes = jsonDecode(
                                                      res.body,
                                                    );
                                                    print(
                                                      "API Response: ${res.body}",
                                                    );

                                                    String idToken =
                                                        jRes['idToken'] ?? "";

                                                    if (idToken.isNotEmpty) {
                                                      debugPrint(
                                                        "API_TOKEN: $idToken",
                                                      );

                                                      var insertPayload = {
                                                        "Token": idToken,
                                                      };

                                                      try {
                                                        var res =
                                                            await BeneficiaryCardRepository()
                                                                .insertAuthToken(
                                                                  insertPayload,
                                                                );

                                                        if (res.statusCode ==
                                                                200 &&
                                                            res
                                                                .body
                                                                .isNotEmpty &&
                                                            res.body != "[]") {
                                                          var obj = jsonDecode(
                                                            res.body,
                                                          );
                                                          String status =
                                                              obj['status'] ??
                                                              "";
                                                          String message =
                                                              obj['message'] ??
                                                              "";

                                                          if (status
                                                                  .toLowerCase() ==
                                                              "success") {
                                                            // Optional Success Dialog
                                                            showDialog(
                                                              context: context,
                                                              barrierDismissible:
                                                                  false,
                                                              builder:
                                                                  (
                                                                    context,
                                                                  ) => AlertDialog(
                                                                    title: const Text(
                                                                      "Success",
                                                                    ),
                                                                    content: Text(
                                                                      message,
                                                                    ),
                                                                    actions: [
                                                                      SizedBox(
                                                                        width:
                                                                            80.w,
                                                                        child: AppActiveButton(
                                                                          buttontitle:
                                                                              "OK",
                                                                          onTap: () {
                                                                            Navigator.of(
                                                                              context,
                                                                            ).pop(); // Close dialog
                                                                            Navigator.of(
                                                                              context,
                                                                            ).maybePop();
                                                                          },
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                            );
                                                          } else {
                                                            ToastManager.showAlertDialog(
                                                              context,
                                                              message,
                                                              () {
                                                                Navigator.pop(
                                                                  context,
                                                                );
                                                              },
                                                            );
                                                          }
                                                        } else {
                                                          debugPrint(
                                                            "Unexpected response: ${res.statusCode} - ${res.body}",
                                                          );
                                                        }
                                                      } catch (e) {
                                                        debugPrint(
                                                          "Exception occurred: $e",
                                                        );
                                                      }
                                                    } else {
                                                      ToastManager.showAlertDialog(
                                                        context,
                                                        "Token not found",
                                                        () {
                                                          Navigator.pop(
                                                            context,
                                                          );
                                                        },
                                                      );
                                                    }
                                                  } else {
                                                    debugPrint(
                                                      "API ERROR: ${res.statusCode} - ${res.reasonPhrase}",
                                                    );
                                                    ToastManager.showAlertDialog(
                                                      context,
                                                      "Error Status Code: ${res.statusCode}",
                                                      () {
                                                        Navigator.pop(context);
                                                      },
                                                    );
                                                  }
                                                } catch (e) {
                                                  ToastManager.showAlertDialog(
                                                    context,
                                                    "Exception : ${e.toString()}",
                                                    () {
                                                      Navigator.pop(context);
                                                    },
                                                  );

                                                  debugPrint("Exception: $e");
                                                }
                                              }
                                            } catch (e) {
                                              debugPrint(
                                                "Date parse error: $e",
                                              );
                                            }
                                          } else {
                                            var payload = {
                                              "username": "c2c_tn",
                                              "password": "c2c_tn",
                                            };

                                            print(
                                              "Request Payload: ${jsonEncode(payload)}",
                                            );

                                            try {
                                              var res =
                                                  await BeneficiaryCardRepository()
                                                      .getVodaphoneAPIKey(
                                                        payload,
                                                      );

                                              if (res.statusCode == 200) {
                                                var jRes = jsonDecode(res.body);
                                                print(
                                                  "API Response: ${res.body}",
                                                );

                                                String idToken =
                                                    jRes['idToken'] ?? "";

                                                if (idToken.isNotEmpty) {
                                                  debugPrint(
                                                    "API_TOKEN: $idToken",
                                                  );

                                                  var insertPayload = {
                                                    "Token": idToken,
                                                  };

                                                  try {
                                                    var res =
                                                        await BeneficiaryCardRepository()
                                                            .insertAuthToken(
                                                              insertPayload,
                                                            );

                                                    if (res.statusCode == 200 &&
                                                        res.body.isNotEmpty &&
                                                        res.body != "[]") {
                                                      var obj = jsonDecode(
                                                        res.body,
                                                      );
                                                      String status =
                                                          obj['status'] ?? "";
                                                      String message =
                                                          obj['message'] ?? "";

                                                      if (status
                                                              .toLowerCase() ==
                                                          "success") {
                                                        // Optional Success Dialog
                                                        showDialog(
                                                          context: context,
                                                          barrierDismissible:
                                                              false,
                                                          builder:
                                                              (
                                                                context,
                                                              ) => AlertDialog(
                                                                title:
                                                                    const Text(
                                                                      "Success",
                                                                    ),
                                                                content: Text(
                                                                  message,
                                                                ),
                                                                actions: [
                                                                  SizedBox(
                                                                    width: 80.w,
                                                                    child: AppActiveButton(
                                                                      buttontitle:
                                                                          "OK",
                                                                      onTap: () {
                                                                        Navigator.of(
                                                                          context,
                                                                        ).pop(); // Close dialog
                                                                        Navigator.of(
                                                                          context,
                                                                        ).maybePop();
                                                                      },
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                        );
                                                      } else {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          message,
                                                          () {
                                                            Navigator.pop(
                                                              context,
                                                            );
                                                          },
                                                        );
                                                      }
                                                    } else {
                                                      debugPrint(
                                                        "Unexpected response: ${res.statusCode} - ${res.body}",
                                                      );
                                                    }
                                                  } catch (e) {
                                                    debugPrint(
                                                      "Exception occurred: $e",
                                                    );
                                                  }
                                                } else {
                                                  ToastManager.showAlertDialog(
                                                    context,
                                                    "Token not found",
                                                    () {
                                                      Navigator.pop(context);
                                                    },
                                                  );
                                                }
                                              } else {
                                                ToastManager.showAlertDialog(
                                                  context,
                                                  "Error Status Code: ${res.statusCode}",
                                                  () {
                                                    Navigator.pop(context);
                                                  },
                                                );

                                                debugPrint(
                                                  "API ERROR: ${res.statusCode} - ${res.reasonPhrase}",
                                                );
                                              }
                                            } catch (e) {
                                              debugPrint("Exception: $e");
                                              ToastManager.showAlertDialog(
                                                context,
                                                "Exception ${e.toString()}",
                                                () {
                                                  Navigator.pop(context);
                                                },
                                              );
                                            }
                                          }

                                          var payload = {
                                            "cli": virtualNumberForVodaphone,
                                            "apartyno":
                                                widget.mobileNo.toString(),
                                            "bpartyno":
                                                widget.beneficiary.mobile
                                                    .toString(),
                                            "reference_id":
                                                referanceId.toString(),
                                            "channelflag":
                                                channelFlag.toString(),
                                            "dtmfflag": dtmfflag.toString(),
                                            "recordingflag":
                                                recordingflag.toString(),
                                          };
                                          var bearerToken = Token;

                                          print(jsonEncode(payload));
                                          print(Token);
                                          var res =
                                              await BeneficiaryCardRepository()
                                                  .vodaphoneInitiateCall(
                                                    payload,
                                                    bearerToken,
                                                  );

                                          var jRes = jsonDecode(res.body);
                                          int status = jRes['status'] ?? 0;

                                          if (status == 1 &&
                                              jRes['message'] != null) {
                                            var message = jRes['message'];
                                            String responseMsg =
                                                message['Response']
                                                    ?.toString()
                                                    .toLowerCase() ??
                                                '';

                                            if (responseMsg == 'success') {
                                              showDialog(
                                                context: context,
                                                builder:
                                                    (context) => AlertDialog(
                                                      title: const Text(
                                                        "Success",
                                                      ),
                                                      content: const Text(
                                                        "Please wait for call",
                                                      ),
                                                      actions: [
                                                        SizedBox(
                                                          width: 80.w,
                                                          child:
                                                              AppActiveButton(
                                                                buttontitle:
                                                                    "OK",
                                                                onTap: () {
                                                                  Navigator.of(
                                                                    context,
                                                                  ).pop();
                                                                },
                                                              ),
                                                        ),
                                                      ],
                                                    ),
                                              );
                                            } else {
                                              ToastManager.showAlertDialog(
                                                context,
                                                responseMsg,
                                                () {
                                                  Navigator.pop(context);
                                                },
                                              );
                                            }
                                          } else {
                                            ToastManager.showAlertDialog(
                                              context,
                                              "Unexpected response or null message",
                                              () {
                                                Navigator.pop(context);
                                              },
                                            );
                                          }
                                        } else {
                                          // Optionally log or handle non-success
                                        }
                                      } catch (e) {
                                        debugPrint("Vodafone API error: $e");
                                      }
                                    }
                                  }
                                  if (mounted) {
                                    setState(() {
                                      _isCallingLoading = false;
                                    });
                                  }
                                }
                              },
                              child: Container(
                                width: responsiveHeight(36),
                                height: responsiveHeight(36),
                                decoration: _phoneButtonDecoration,
                                child: Center(
                                  child:
                                      _isCallingLoading
                                          ? SizedBox(
                                            width: responsiveHeight(22),
                                            height: responsiveHeight(22),
                                            child:
                                                const CircularProgressIndicator(
                                                  color: kWhiteColor,
                                                  strokeWidth: 2,
                                                ),
                                          )
                                          : Icon(
                                            Icons.phone_outlined,
                                            color: kWhiteColor,
                                            size: 20,
                                          ),
                                ),
                              ),
                            ),
                          ],
                        ),
                        SizedBox(height: responsiveHeight(2)),

                        infoRow(
                          icHashIcon,
                          "Beneficiary ID",
                          '${widget.beneficiary.beneficiaryNo ?? ''}',
                        ),
                        SizedBox(height: responsiveHeight(2)),
                        infoRow(
                          iconCalender,
                          "Renewal Date",
                          widget.beneficiary.nextRenewalDate ?? '',
                        ),
                        SizedBox(height: responsiveHeight(2)),
                        infoRow(
                          iconPerson,
                          "Worker Screened/Date",
                          '${widget.beneficiary.isWorkerScreened ?? ''}/${widget.beneficiary.lastScreeningDate ?? ''}',
                        ),
                        SizedBox(height: responsiveHeight(2)),
                        infoRow(
                          iconPersons,
                          "Dependent Screen Pending",
                          '${widget.beneficiary.dependantScreeningPending ?? ''}',
                        ),
                        SizedBox(height: responsiveHeight(2)),
                        infoRow(iconMap, "Area", widget.beneficiary.area ?? ''),
                        SizedBox(height: responsiveHeight(2)),
                        (widget.beneficiary.groupID == 2 ||
                                widget.beneficiary.groupID == 5)
                            ? Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                infoRow(
                                  iconMap,
                                  "Appoint.Date/Time",
                                  '${widget.beneficiary.appoinmentDate ?? ''}/${widget.beneficiary.appoinmentTime ?? ''}',
                                ),
                                SizedBox(height: responsiveHeight(12)),
                              ],
                            )
                            : const SizedBox.shrink(),
                        infoRow(
                          iconDocument,
                          "Phlebo Remark",
                          widget.beneficiary.phleboRemark ?? '',
                        ),
                        SizedBox(height: responsiveHeight(2)),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(
                                  responsiveHeight(5),
                                ),

                                color: getStatusColor(
                                  widget.beneficiary.groupID ?? 0,
                                ),

                                // String statusName = beneficiary.assignStatus
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(
                                  widget.beneficiary.assignStatus.toString(),
                                  style: const TextStyle(
                                    color: kWhiteColor,
                                    fontSize: 11,
                                    fontWeight: FontWeight.w500,
                                  ),
                                ),
                              ),
                            ),

                            Container(
                              width: responsiveHeight(36),
                              height: responsiveHeight(36),
                              decoration: _eyeButtonDecoration,
                              child: Center(
                                child: Icon(
                                  Icons.remove_red_eye_outlined,
                                  color: kWhiteColor,
                                  size: 20,
                                ),
                              ),
                            ),

                            // ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Future<bool> canMakePhoneCall(String mobile) async {
    try {
      final Uri telUri = Uri.parse('tel:$mobile');
      final bool canLaunch = await canLaunchUrl(telUri);

      if (!canLaunch && mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Dialer app not available'),
            backgroundColor: Colors.red,
          ),
        );
      }
      return canLaunch;
    } catch (e) {
      debugPrint('Error checking phone capability: $e');
      return false;
    }
  }

  Future<void> launchPhoneDialer(String phoneNumber, {String? context}) async {
    try {
      if (phoneNumber.isEmpty) {
        throw Exception('Phone number is empty');
      }

      // Clean the phone number
      String cleanNumber = phoneNumber.replaceAll(RegExp(r'[^\d+]'), '');

      if (cleanNumber.isEmpty) {
        throw Exception('Invalid phone number');
      }

      debugPrint('Calling: $cleanNumber - Context: ${context ?? "Direct"}');

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
        debugPrint('Dialer launched successfully via url_launcher');
      }
    } catch (e) {
      debugPrint('Dialer error: $e');

      if (mounted) {
        ToastManager.toast("Cannot open dialer: ${e.toString()}");
      }
    }
  }

  Widget infoRow(String icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0),
      child: Row(
        children: [
          Image.asset(
            icon,
            height: 18,
            width: 18,
            filterQuality: FilterQuality.low,
          ),
          const SizedBox(width: 10),
          Text(
            "$label : ",
            style: TextStyle(
              fontWeight: FontWeight.w500,
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              color: kBlackColor,
            ),
          ),
          Expanded(
            child: Text(
              capitalizeEachWord(value),
              style: TextStyle(
                color: Colors.grey[600],
                fontWeight: FontWeight.w600,
                fontSize: 14.sp,
                fontFamily: FontConstants.interFonts,
              ),
            ),
          ),
        ],
      ),
    );
  }

  String capitalizeEachWord(String text) {
    return text
        .split(' ')
        .map(
          (word) =>
              word.isNotEmpty
                  ? word[0].toUpperCase() + word.substring(1).toLowerCase()
                  : '',
        )
        .join(' ');
  }
}

Color getStatusColor(int? groupID) {
  if (groupID == null) return Colors.grey; // Handle null values

  switch (groupID) {
    case 1:
      return const Color.fromRGBO(103, 103, 103, 1);
    case 2:
      return const Color.fromRGBO(77, 191, 129, 1);
    case 3:
      return const Color.fromRGBO(249, 133, 83, 1);
    case 4:
      return const Color.fromRGBO(26, 195, 207, 1);

    case 5:
      return const Color.fromRGBO(183, 102, 210, 1);

    case 7:
      return const Color.fromRGBO(249, 133, 83, 1);

    default:
      return Colors.grey; // Default color
  }
}

class Beneficiary {
  final String name;
  final String id;
  final String renewalDate;
  final String workerScreened;
  final String dependentPending;
  final String area;
  final String phleboRemark;

  Beneficiary({
    required this.name,
    required this.id,
    required this.renewalDate,
    required this.workerScreened,
    required this.dependentPending,
    required this.area,
    required this.phleboRemark,
  });
}
