// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Screens/calling_modules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/calling_modules/repository/beneficiary_card_repository.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/DataProvider.dart';

class BeneficiaryCardController extends GetxController {
  final BeneficiaryCardRepository repository;

  final BeneficiaryOutput beneficiary;
  final int empCode;
  final int desId;
  final int agentId;
  final int mobileNo;
  final String? myOperatorUserId;
  final int index;

  BeneficiaryCardController({
    required this.repository,
    required this.beneficiary,
    required this.empCode,
    required this.desId,
    required this.agentId,
    required this.mobileNo,
    required this.myOperatorUserId,
    required this.index,
  });

  // ─── Insert Appointment ───────────────────────────────────────────────────
  final insertAppointmentStatus = FormzSubmissionStatus.initial.obs;
  final insertAppointmentResponse = ''.obs;

  // ─── Organization ─────────────────────────────────────────────────────────
  final organizationStatus = FormzSubmissionStatus.initial.obs;
  final organizationResponse = ''.obs;

  // ─── User Created By ──────────────────────────────────────────────────────
  final usercreatedStatus = FormzSubmissionStatus.initial.obs;
  final userCreatedResponse = ''.obs;

  // ─── Insert Call Log New ──────────────────────────────────────────────────
  final insertClStatus = FormzSubmissionStatus.initial.obs;
  final insertClResponse = ''.obs;

  // ─── Agent ID ─────────────────────────────────────────────────────────────
  final agentIdStatus = FormzSubmissionStatus.initial.obs;
  final agetIdResponse = ''.obs;

  // ─── API Key for Agent ID ─────────────────────────────────────────────────
  final apikeyAgentIdStatus = FormzSubmissionStatus.initial.obs;
  final apiKeyAgentResponse = ''.obs;

  // ─── 24x7 for Agent ID ───────────────────────────────────────────────────
  final twentyFourBySevenForAgentIdStatus = FormzSubmissionStatus.initial.obs;
  final twentyFourBySevenForAgentIdResponse = ''.obs;

  // ─── API Key for Call Patching ────────────────────────────────────────────
  final apikeyCallPatchingStatus = FormzSubmissionStatus.initial.obs;
  final apiKeyCallPatchingResponse = ''.obs;

  // ─── 24x7 for Call Patching ───────────────────────────────────────────────
  final twentyFourBySevenForCallPatchingStatus =
      FormzSubmissionStatus.initial.obs;
  final twentyFourBySevenForCallPatchingResponse = ''.obs;

  // ─── My Operator ──────────────────────────────────────────────────────────
  final myoperatorStatus = FormzSubmissionStatus.initial.obs;
  final myOperatorResponse = ''.obs;

  // ─── Get My Operator Details ──────────────────────────────────────────────
  final getMyoperatorDetailsStatus = FormzSubmissionStatus.initial.obs;
  final getMyoperatorDetailsResponse = ''.obs;

  // ─── Insert Call Log (legacy) ─────────────────────────────────────────────
  final insertCallLogStatus = FormzSubmissionStatus.initial.obs;

  // ─── Derived call-flow values (parsed from responses, used by gesture handler)
  int organizationId = 0;
  int isUserCreatedBy = 0;
  int referanceId = 0;
  String apikeyForAgent = '';
  String virtualNumber = '';
  String? virtualNumberForCallPAtching;
  String apikeyForCAllPAtching = '';
  String apiKeyForMyOperator = '';
  String companyID = '';
  String publicIvrId = '';
  String secretToken = '';
  String typeForMyOperator = '';

  // ─── Local state fields moved from _BeneficiaryCardState ─────────────────
  String virtualNumberForVodaphone = "";
  int channelFlag = 0;
  int dtmfflag = 0;
  int recordingflag = 0;
  String modifiedOn = "";
  String token = "";
  bool isCallingLoading = false;

  final List<Worker> _workers = [];

  // ─────────────────────────────────────────────────────────────────────────
  // onInit
  // ─────────────────────────────────────────────────────────────────────────
  @override
  void onInit() {
    super.onInit();
    _workers.add(
      ever(organizationStatus, (status) {
        if (status.isSuccess) {
          WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
            fetchAPIKeyForAgentId({"OrgId": organizationId.toString()});
            fetchAPIKeyForCallPatching({
              "OrgId": organizationId.toString(),
              "UserId": empCode.toString(),
            });
            // Inline MyOperator key fetch
            final payload = {
              "OrgId": organizationId.toString(),
              "UserId": empCode.toString(),
            };
            final res = await BeneficiaryCardRepository().apiKeyForMyoperator(
              payload,
            );
            final jRes = jsonDecode(res.body);
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
                    apiKeyForMyOperator = item['APIKey'] ?? '';
                    companyID = item['CompanyID'] ?? '';
                    publicIvrId = item['Public_IVR_ID'] ?? '';
                    secretToken = item['SecrateToken'] ?? '';
                    typeForMyOperator = item['Type']?.toString() ?? '';
                    debugPrint(apiKeyForMyOperator);
                  }
                }
              } catch (e) {
                debugPrint("Error parsing MyOperator response: $e");
              }
            } else {
              ToastManager.showAlertDialog(Get.context!, jRes['details'], () {
                Navigator.pop(Get.context!);
              });
            }
          });
        }
      }),
    );
  }

  // ─────────────────────────────────────────────────────────────────────────
  // onReady
  // ─────────────────────────────────────────────────────────────────────────
  @override
  void onReady() {
    super.onReady();
    fetchOrganization({"DESGID": desId.toString()});
    fetchUserCreatedBy({"UserID": empCode.toString()});
  }

  // ─────────────────────────────────────────────────────────────────────────
  // onClose
  // ─────────────────────────────────────────────────────────────────────────
  @override
  void onClose() {
    for (final w in _workers) {
      w.dispose();
    }
    super.onClose();
  }

  // ─────────────────────────────────────────────────────────────────────────
  // handleCallTap  (moved from phone button GestureDetector onTap)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> handleCallTap() async {
    if (isCallingLoading) return;
    isCallingLoading = true;
    update();

    if (isUserCreatedBy == 0) {
      final canCall = await canMakePhoneCall(beneficiary.mobile!);
      if (!canCall) {
        isCallingLoading = false;
        update();
        return;
      }

      var res = await BeneficiaryCardRepository().insertCallinglog({
        "AssignCallID": beneficiary.assignCallID.toString(),
        "CreatedBy": empCode.toString(),
      });

      String resString = res.body;
      var jsonResponse = jsonDecode(resString);

      if (jsonResponse['status'] == 'Success') {
        await launchPhoneDialer(
          beneficiary.mobile ?? '',
          context: 'Direct call',
        );
      }
      isCallingLoading = false;
      update();
    } else {
      var res = await BeneficiaryCardRepository().insertCallinglogNew({
        "AssignCallID": beneficiary.assignCallID.toString(),
        "CreatedBy": empCode.toString(),
      });
      String resString = res.body;
      var jsonResponse = jsonDecode(resString);
      referanceId = jsonResponse["ID"];
      if (jsonResponse['status'] == 'Success') {
        if (isUserCreatedBy == 1) {
          if (agentId != 0) {
            var res = await BeneficiaryCardRepository()
                .twentyFourBySevenForWithAgentId({
                  "apiKey": apikeyForAgent,
                  "customernumber": empCode.toString(),
                  "servienumber": virtualNumber,
                  "format": "json",
                  "agentloginid": agentId.toString(),
                  "referencestate": referanceId.toString(),
                });

            var jsonResponse = jsonDecode(res.body);

            final String statusMessage =
                jsonResponse["statusMessage"] ?? "Unknown status message";
            final String data =
                jsonResponse["data"]?.toString() ??
                jsonResponse["response"]?.toString() ??
                "No additional information.";

            if (statusMessage.toLowerCase() == "success") {
              showDialog(
                context: Get.context!,
                builder:
                    (context) => AlertDialog(
                      title: const Text("Success"),
                      content: Text(data),
                      actions: [
                        AppActiveButton(
                          buttontitle: "OK",
                          onTap: () {
                            Navigator.of(context).pop();
                          },
                        ),
                      ],
                    ),
              );
            } else {
              print("failed");
              print("$jsonResponse");
              ToastManager.showAlertDialog(Get.context!, statusMessage, () {
                Navigator.pop(Get.context!);
              });
            }
            isCallingLoading = false;
            update();
          } else {
            if (virtualNumberForCallPAtching == null ||
                virtualNumberForCallPAtching!.isEmpty) {
              print(virtualNumberForCallPAtching);
              print(apikeyForCAllPAtching);
              isCallingLoading = false;
              update();
              ToastManager.showAlertDialog(
                Get.context!,
                "Virtual Number Empty",
                () {
                  Get.back();
                },
              );
              return;
            }

            var res = await BeneficiaryCardRepository()
                .twentyFourBySevenForWithCallPatching({
                  "apiKey": apikeyForCAllPAtching,
                  "customer_number": mobileNo.toString(),
                  "user_number": beneficiary.mobile,
                  "caller_id": virtualNumberForCallPAtching,
                  "reference_id": referanceId.toString(),
                });

            var jsonResponse = jsonDecode(res.body);

            final String statusMessage =
                jsonResponse["statusMessage"] ?? "Unknown status message";

            if (statusMessage.toLowerCase() == "success") {
              await launchPhoneDialer(
                virtualNumberForCallPAtching!,
                context: 'Call Patching',
              );
            } else {
              print("failed");
              print("$jsonResponse");

              ToastManager.showAlertDialog(Get.context!, statusMessage, () {
                Navigator.pop(Get.context!);
              });
              isCallingLoading = false;
              update();
            }
          }
        } else if (isUserCreatedBy == 2) {
          // var payload = {
          //   "company_id": companyID.toString(),
          //   "secret_token": secretToken.toString(),
          //   "type": typeForMyOperator.toString(),
          //   "user_id": myOperatorUserId.toString(),
          //   "number": "+91${beneficiary.mobile}",
          //   // "number": "+91${8999602937}",
          //   "public_ivr_id": public_IVR_ID.toString(),
          //   "reference_id": referanceId.toString(),
          // };

          // var payload = {
          //   "company_id": companyID.toString(),
          //   "secret_token": secretToken.toString(),
          //   "type": typeForMyOperator.toString(),
          //   // "user_id": myOperatorUserId.toString(),
          //   "number": "+91${beneficiary.mobile}",
          //   "number_2":
          //       "+91${DataProvider().getParsedUserData()?.output?[0].bMobile?.toString() ?? ''}",
          //   "max_call_duration": 0,
          //   "region": "",
          //   "caller_id": "",
          //   "public_ivr_id": publicIvrId.toString(),
          //   "reference_id": referanceId.toString(),
          //   "group": "",
          //   "call_hold": false,
          // };

          var payload = {
            "company_id": companyID.toString(),
            "secret_token": secretToken.toString(),
            "type": typeForMyOperator.toString(),
            "number": "+91${9673974373}",
            "number_2": "+918830378568",
            "max_call_duration": 0,
            "region": "",
            "caller_id": "",
            "public_ivr_id": publicIvrId.toString(),
            "reference_id": referanceId.toString(),
            "group": "",
            "call_hold": false,
          };

          var apiKey = apiKeyForMyOperator;

          print(jsonEncode(payload));
          print(apiKeyForMyOperator);
          var res = await BeneficiaryCardRepository().myOperatorAPIDetails(
            payload,
            apiKey,
          );

          var jRes = jsonDecode(res.body);
          if (jRes['status'].toString().toLowerCase() == "success") {
            showDialog(
              context: Get.context!,
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
                          mWidth: 80.w,
                          title: 'OK',
                          onTap: () {
                            Navigator.of(context).pop();
                          },
                        ),
                      ],
                    ),
                  ),
            );
          } else if (jRes['status'].toString().toLowerCase() == "error") {
            ToastManager.showAlertDialog(Get.context!, jRes['message'], () {
              Navigator.pop(Get.context!);
            });
            isCallingLoading = false;
            update();
          } else {
            ToastManager.showAlertDialog(Get.context!, jRes['details'], () {
              Navigator.pop(Get.context!);
            });
            isCallingLoading = false;
            update();
          }
        } else if (isUserCreatedBy == 3) {
          try {
            var res = await BeneficiaryCardRepository().vodaphoneApiDetails();
            String resString = res.body;

            var jsonResponse = jsonDecode(resString);

            if (jsonResponse['status'] == 'Success') {
              var output = jsonResponse['output'][0];

              virtualNumberForVodaphone = output["virtualNumber"];
              channelFlag = output["channelflag"];
              dtmfflag = output["dtmfflag"];
              modifiedOn = output["ModifiedOn"];
              token = output["Token"];
              recordingflag = output["recordingflag"];

              // ===== Check if modifiedOn is older than 24 hours =====
              if (modifiedOn.toString().isNotEmpty) {
                try {
                  final sdf = DateFormat("dd/MM/yyyy");
                  final modifiedDate = sdf.parse(modifiedOn);
                  final currentDate = DateTime.now();

                  final diffInHours =
                      currentDate.difference(modifiedDate).inHours;
                  if (diffInHours >= 24) {
                    await _refreshVodaphoneToken();
                  }
                } catch (e) {
                  debugPrint("Date parse error: $e");
                }
              } else {
                await _refreshVodaphoneToken();
              }

              var callPayload = {
                "cli": virtualNumberForVodaphone,
                "apartyno": mobileNo.toString(),
                "bpartyno": beneficiary.mobile.toString(),
                "reference_id": referanceId.toString(),
                "channelflag": channelFlag.toString(),
                "dtmfflag": dtmfflag.toString(),
                "recordingflag": recordingflag.toString(),
              };
              var bearerToken = token;

              print(jsonEncode(callPayload));
              print(token);
              var res2 = await BeneficiaryCardRepository()
                  .vodaphoneInitiateCall(callPayload, bearerToken);

              var jRes = jsonDecode(res2.body);
              int status = jRes['status'] ?? 0;

              if (status == 1 && jRes['message'] != null) {
                var message = jRes['message'];
                String responseMsg =
                    message['Response']?.toString().toLowerCase() ?? '';

                if (responseMsg == 'success') {
                  showDialog(
                    context: Get.context!,
                    builder:
                        (context) => AlertDialog(
                          title: const Text("Success"),
                          content: const Text("Please wait for call"),
                          actions: [
                            SizedBox(
                              width: 80.w,
                              child: AppActiveButton(
                                buttontitle: "OK",
                                onTap: () {
                                  Navigator.of(context).pop();
                                },
                              ),
                            ),
                          ],
                        ),
                  );
                } else {
                  ToastManager.showAlertDialog(Get.context!, responseMsg, () {
                    Navigator.pop(Get.context!);
                  });
                  isCallingLoading = false;
                  update();
                }
              } else {
                ToastManager.showAlertDialog(
                  Get.context!,
                  "Unexpected response or null message",
                  () {
                    Navigator.pop(Get.context!);
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
      isCallingLoading = false;
      update();
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // _refreshVodaphoneToken  (extracted duplicate token-refresh block)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> _refreshVodaphoneToken() async {
    var payload = {"username": "c2c_tn", "password": "c2c_tn"};

    print("Request Payload: ${jsonEncode(payload)}");

    try {
      var res = await BeneficiaryCardRepository().getVodaphoneAPIKey(payload);

      if (res.statusCode == 200) {
        var jRes = jsonDecode(res.body);
        print("API Response: ${res.body}");

        String idToken = jRes['idToken'] ?? "";

        if (idToken.isNotEmpty) {
          debugPrint("API_TOKEN: $idToken");

          var insertPayload = {"Token": idToken};

          try {
            var res2 = await BeneficiaryCardRepository().insertAuthToken(
              insertPayload,
            );

            if (res2.statusCode == 200 &&
                res2.body.isNotEmpty &&
                res2.body != "[]") {
              var obj = jsonDecode(res2.body);
              String status = obj['status'] ?? "";
              String message = obj['message'] ?? "";

              if (status.toLowerCase() == "success") {
                // Optional Success Dialog
                showDialog(
                  context: Get.context!,
                  barrierDismissible: false,
                  builder:
                      (context) => AlertDialog(
                        title: const Text("Success"),
                        content: Text(message),
                        actions: [
                          SizedBox(
                            width: 80.w,
                            child: AppActiveButton(
                              buttontitle: "OK",
                              onTap: () {
                                Navigator.of(context).pop(); // Close dialog
                                Navigator.of(context).maybePop();
                              },
                            ),
                          ),
                        ],
                      ),
                );
              } else {
                ToastManager.showAlertDialog(Get.context!, message, () {
                  Navigator.pop(Get.context!);
                });
              }
            } else {
              debugPrint(
                "Unexpected response: ${res2.statusCode} - ${res2.body}",
              );
            }
          } catch (e) {
            debugPrint("Exception occurred: $e");
          }
        } else {
          ToastManager.showAlertDialog(Get.context!, "Token not found", () {
            Navigator.pop(Get.context!);
          });
        }
      } else {
        debugPrint("API ERROR: ${res.statusCode} - ${res.reasonPhrase}");
        ToastManager.showAlertDialog(
          Get.context!,
          "Error Status Code: ${res.statusCode}",
          () {
            Navigator.pop(Get.context!);
          },
        );
      }
    } catch (e) {
      ToastManager.showAlertDialog(
        Get.context!,
        "Exception : ${e.toString()}",
        () {
          Navigator.pop(Get.context!);
        },
      );

      debugPrint("Exception: $e");
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // canMakePhoneCall  (moved from state)
  // ─────────────────────────────────────────────────────────────────────────
  Future<bool> canMakePhoneCall(String mobile) async {
    try {
      final Uri telUri = Uri.parse('tel:$mobile');
      final bool canLaunch = await canLaunchUrl(telUri);

      if (!canLaunch) {
        ScaffoldMessenger.of(Get.context!).showSnackBar(
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

  // ─────────────────────────────────────────────────────────────────────────
  // launchPhoneDialer  (moved from state)
  // ─────────────────────────────────────────────────────────────────────────
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
      ToastManager.toast("Cannot open dialer: ${e.toString()}");
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // resetState  (mirrors ResetBaneficiaryCardState event)
  // ─────────────────────────────────────────────────────────────────────────
  void resetState() {
    insertAppointmentStatus.value = FormzSubmissionStatus.initial;
    organizationStatus.value = FormzSubmissionStatus.initial;
    usercreatedStatus.value = FormzSubmissionStatus.initial;
    insertCallLogStatus.value = FormzSubmissionStatus.initial;
    insertClStatus.value = FormzSubmissionStatus.initial;
    agentIdStatus.value = FormzSubmissionStatus.initial;
    apikeyAgentIdStatus.value = FormzSubmissionStatus.initial;
    twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
    apikeyCallPatchingStatus.value = FormzSubmissionStatus.initial;
    twentyFourBySevenForCallPatchingStatus.value =
        FormzSubmissionStatus.initial;
    myoperatorStatus.value = FormzSubmissionStatus.initial;
    getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchOrganization  (mirrors GetOrganization event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchOrganization(Map<String, dynamic> payload) async {
    try {
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      organizationStatus.value = FormzSubmissionStatus.inProgress;

      var userDetails = DataProvider().getParsedUserData();
      int empCode = userDetails?.output?[0].empCode ?? 0;
      payload.addAll({'UserID': empCode.toString()});

      var res = await repository.getOrganization(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          organizationResponse.value = resString;
          final orgData = jsonResponse['output'] as List?;
          if (orgData != null && orgData.isNotEmpty) {
            organizationId = (orgData[0]['SubOrgId'] as num?)?.toInt() ?? 0;
          }
          organizationStatus.value = FormzSubmissionStatus.success;
        } else {
          organizationResponse.value = resString;
          organizationStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        organizationResponse.value = res.reasonPhrase ?? '';
        organizationStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      organizationResponse.value = e.toString();
      organizationStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchUserCreatedBy  (mirrors GetUserCreatedBy event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchUserCreatedBy(Map<String, dynamic> payload) async {
    try {
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      userCreatedResponse.value = '';
      usercreatedStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.getUserCreatedBy(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          userCreatedResponse.value = resString;
          usercreatedStatus.value = FormzSubmissionStatus.success;
          final userData = jsonResponse['output'] as List?;
          if (userData != null && userData.isNotEmpty) {
            // isUserCreatedBy =
            //     (userData[0]['Is24By7IsAccountCreated'] as num?)?.toInt() ?? 0;
            isUserCreatedBy = 2;
          }
        } else {
          userCreatedResponse.value = resString;
          usercreatedStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        userCreatedResponse.value = res.reasonPhrase ?? '';
        usercreatedStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      userCreatedResponse.value = e.toString();
      usercreatedStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // insertCallLogNew  (mirrors InsertCallLogNew event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> insertCallLogNew(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      insertClStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.insertCallinglogNew(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          insertClResponse.value = resString;
          insertClStatus.value = FormzSubmissionStatus.success;
        } else {
          insertClResponse.value = resString;
          insertClStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        insertClResponse.value = res.reasonPhrase ?? '';
        insertClStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      insertClResponse.value = e.toString();
      insertClStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // twentyFourBySevenWithAgentID  (mirrors TwentyFourBySevenWithAgentID event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> twentyFourBySevenWithAgentID(
    Map<String, dynamic> payload,
  ) async {
    try {
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdResponse.value = '';
      twentyFourBySevenForAgentIdStatus.value =
          FormzSubmissionStatus.inProgress;

      var res = await repository.twentyFourBySevenForWithAgentId(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['statusMessage'] == 'success') {
          twentyFourBySevenForAgentIdResponse.value = resString;
          twentyFourBySevenForAgentIdStatus.value =
              FormzSubmissionStatus.success;
        } else {
          twentyFourBySevenForAgentIdResponse.value = resString;
          twentyFourBySevenForAgentIdStatus.value =
              FormzSubmissionStatus.failure;
        }
      } else {
        twentyFourBySevenForAgentIdResponse.value = res.reasonPhrase ?? '';
        twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      twentyFourBySevenForAgentIdResponse.value = e.toString();
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForCallPatching  (mirrors GetAPIKeyForCallPAtching event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForCallPatching(Map<String, dynamic> payload) async {
    try {
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyCallPatchingResponse.value = '';
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForCallPAtching(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          apiKeyCallPatchingResponse.value = resString;
          apikeyCallPatchingStatus.value = FormzSubmissionStatus.success;
          final patchData = jsonResponse['output'] as List?;
          if (patchData != null && patchData.isNotEmpty) {
            apikeyForCAllPAtching = (patchData[0]['APIKey'] as String?) ?? '';
            print((patchData[0]['ServieNumber'] as String?) ?? '');
            virtualNumberForCallPAtching =
                (patchData[0]['ServieNumber'] as String?) ?? "";
            print((patchData[0]['ServieNumber'] as String?) ?? '');
          }
        } else {
          apiKeyCallPatchingResponse.value = resString;
          apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        apiKeyCallPatchingResponse.value = res.reasonPhrase ?? '';
        apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      apiKeyCallPatchingResponse.value = e.toString();
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForAgentId  (mirrors GetAPIKeyForAgentId event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForAgentId(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyAgentResponse.value = '';
      apikeyAgentIdStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForAgentId(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          apiKeyAgentResponse.value = resString;
          apikeyAgentIdStatus.value = FormzSubmissionStatus.success;
          final agentData = jsonResponse['output'] as List?;
          if (agentData != null && agentData.isNotEmpty) {
            apikeyForAgent = (agentData[0]['APIKey'] as String?) ?? '';
            virtualNumber = (agentData[0]['ServieNumber'] as String?) ?? '';
          }
        } else {
          apiKeyAgentResponse.value = resString;
          apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        apiKeyAgentResponse.value = res.reasonPhrase ?? '';
        apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      apiKeyAgentResponse.value = e.toString();
      apikeyAgentIdStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // twentyFourBySevenWithCallPatching
  // (mirrors TwentyFourBySevenWithCallPatching event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> twentyFourBySevenWithCallPatching(
    Map<String, dynamic> payload,
  ) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      twentyFourBySevenForAgentIdResponse.value = '';
      twentyFourBySevenForCallPatchingResponse.value = '';
      twentyFourBySevenForCallPatchingStatus.value =
          FormzSubmissionStatus.inProgress;

      var res = await repository.twentyFourBySevenForWithCallPatching(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          twentyFourBySevenForCallPatchingResponse.value = resString;
          twentyFourBySevenForCallPatchingStatus.value =
              FormzSubmissionStatus.success;
        } else {
          twentyFourBySevenForCallPatchingResponse.value = resString;
          twentyFourBySevenForCallPatchingStatus.value =
              FormzSubmissionStatus.failure;
        }
      } else {
        twentyFourBySevenForCallPatchingResponse.value = res.reasonPhrase ?? '';
        twentyFourBySevenForCallPatchingStatus.value =
            FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      twentyFourBySevenForCallPatchingResponse.value = e.toString();
      twentyFourBySevenForCallPatchingStatus.value =
          FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // myOperator  (mirrors MyOperator event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> myOperator(Map<String, dynamic> payload, String apiKey) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      insertAppointmentResponse.value = '';
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      myOperatorResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      twentyFourBySevenForAgentIdResponse.value = '';
      myoperatorStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.myOperatorAPIDetails(payload, apiKey);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          myOperatorResponse.value = resString;
          myoperatorStatus.value = FormzSubmissionStatus.success;
        } else {
          myOperatorResponse.value = resString;
          myoperatorStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        myOperatorResponse.value = res.reasonPhrase ?? '';
        myoperatorStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      myOperatorResponse.value = e.toString();
      myoperatorStatus.value = FormzSubmissionStatus.failure;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // fetchAPIKeyForMyoperator  (mirrors GetAPIKeyForMyoperator event)
  // ─────────────────────────────────────────────────────────────────────────
  Future<void> fetchAPIKeyForMyoperator(Map<String, dynamic> payload) async {
    try {
      insertAppointmentStatus.value = FormzSubmissionStatus.initial;
      organizationStatus.value = FormzSubmissionStatus.initial;
      usercreatedStatus.value = FormzSubmissionStatus.initial;
      insertCallLogStatus.value = FormzSubmissionStatus.initial;
      insertClStatus.value = FormzSubmissionStatus.initial;
      twentyFourBySevenForAgentIdStatus.value = FormzSubmissionStatus.initial;
      apikeyCallPatchingStatus.value = FormzSubmissionStatus.initial;
      organizationResponse.value = '';
      insertAppointmentResponse.value = '';
      userCreatedResponse.value = '';
      insertClResponse.value = '';
      apiKeyCallPatchingResponse.value = '';
      getMyoperatorDetailsResponse.value = '';
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.inProgress;

      var res = await repository.apiKeyForMyoperator(payload);

      if (res.statusCode == 200) {
        String resString = res.body;
        var jsonResponse = jsonDecode(resString);
        if (jsonResponse['status'] == 'Success') {
          getMyoperatorDetailsResponse.value = resString;
          getMyoperatorDetailsStatus.value = FormzSubmissionStatus.success;
          final moData = jsonResponse['output'] as List?;
          if (moData != null && moData.isNotEmpty) {
            final item = moData.firstWhere(
              (e) => e['IsMyOperatorAPI'] == 1,
              orElse: () => null,
            );
            if (item != null) {
              apiKeyForMyOperator = (item['APIKey'] as String?) ?? '';
              companyID = (item['CompanyID'] as String?) ?? '';
              publicIvrId = (item['Public_IVR_ID'] as String?) ?? '';
              secretToken = (item['SecrateToken'] as String?) ?? '';
              typeForMyOperator = item['Type']?.toString() ?? '';
            }
          }
        } else {
          getMyoperatorDetailsResponse.value = resString;
          getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
        }
      } else {
        getMyoperatorDetailsResponse.value = res.reasonPhrase ?? '';
        getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
      }
    } catch (e) {
      print(e);
      getMyoperatorDetailsResponse.value = e.toString();
      getMyoperatorDetailsStatus.value = FormzSubmissionStatus.failure;
    }
  }
}
