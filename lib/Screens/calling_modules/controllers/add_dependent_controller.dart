// ignore_for_file: use_build_context_synchronously

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_controller.dart';
import 'package:s2toperational/Screens/calling_modules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/calling_modules/models/ScreeningDependentModel.dart';
import 'package:s2toperational/Screens/calling_modules/models/calling_address_model.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/utilities/SizeConfig.dart';

class AddDependentController extends GetxController {
  final Map<String, dynamic> args;

  AddDependentController({required this.args});

  // ─── Service controller ──────────────────────────────────────────────────────
  late final ExpectedBeneficiaryController svc;

  // ─── TextEditingControllers ──────────────────────────────────────────────────
  final TextEditingController relationController = TextEditingController();
  final TextEditingController ageController = TextEditingController();
  final TextEditingController firstNameController = TextEditingController();
  final TextEditingController middleNameController = TextEditingController();
  final TextEditingController lastNameController = TextEditingController();

  final GlobalKey<FormState> formKey = GlobalKey();

  // ─── State fields ────────────────────────────────────────────────────────────
  RelationOutput? selectedReltion;
  bool isSubmitted = false;
  bool isRelationSheetVisible = false;

  String fname = "";
  String mname = "";
  String lname = "";
  String workerGender = "";
  String gender = "";
  int numberOfDependent = 0;
  int numberOfDependentPending = 0;

  BeneficiaryOutput? selectedBeneficiary;
  CallingAddressModel? callingAddressModel;
  int? assignCallID;
  String? marriedStatusID;
  RelationModel? relationOutput;

  final List<RelationOutput> filteredRelationList = [];
  final List<CallingAddressOutput> addressList = [];
  List<ScreeningDataOutput> screeningdataList = [];

  final List<Worker> _workers = [];

  // Guard: true only while THIS screen's Save action is in flight.
  // Prevents the AppointmentConfirmation prefill (which also calls
  // svc.addDependent) from triggering our success handler.
  bool isSaving = false;

  // ─── Lifecycle ───────────────────────────────────────────────────────────────
  @override
  void onInit() {
    super.onInit();
    svc = Get.find<ExpectedBeneficiaryController>();

    // Extract route arguments immediately (no context needed)
    selectedBeneficiary = args["selectedBeneficiary"] as BeneficiaryOutput?;
    workerGender = args["workerGender"] as String? ?? "";
    numberOfDependent = args["noOfDependent"] as int? ?? 0;
    numberOfDependentPending = args["numberOfDependentPending"] as int? ?? 0;
    assignCallID = args["assignCallId"] as int?;
    marriedStatusID = args["marriedStatus"] as String?;
    screeningdataList =
        args["screeningdataList"] as List<ScreeningDataOutput>? ?? [];

    // ── Worker: addDependentStatus ────────────────────────────────────────────
    _workers.add(
      ever(svc.addDependentStatus, (FormzSubmissionStatus status) {
        // Only react to success when triggered by this screen's Save action,
        // not by AppointmentConfirmation's prefill which also calls addDependent.
        if (status.isSuccess && isSaving) {
          isSaving = false;
          ScaffoldMessenger.of(Get.context!)
            ..clearSnackBars()
            ..showSnackBar(
              const SnackBar(
                content: Text("Depended added successfully!"),
                duration: Duration(seconds: 3),
                backgroundColor: Colors.green,
              ),
            );
          relationController.clear();
          firstNameController.clear();
          middleNameController.clear();
          lastNameController.clear();
          ageController.clear();
          selectedReltion = null;
          update();

          if (svc.addDependentOutput.length == numberOfDependentPending) {
            // Defer pop to post-frame so any pending caret/scroll callbacks
            // on text fields finish before the render objects are detached.
            WidgetsBinding.instance.addPostFrameCallback((_) => Get.back());
          }
        }
      }),
    );

    // ── Worker: getRealtionStatus ─────────────────────────────────────────────
    _workers.add(
      ever(svc.getRealtionStatus, (FormzSubmissionStatus status) {
        if (status.isSuccess && !isRelationSheetVisible) {
          relationOutput = RelationModel.fromJson(
            jsonDecode(svc.getRelationResponse.value),
          );
          filteredRelationList.clear();
          filteredRelationList.addAll(relationOutput!.output!);
          isRelationSheetVisible = true;
          update();

          showModalBottomSheet(
            context: Get.context!,
            isScrollControlled: true,
            builder: (context) {
              return StatefulBuilder(
                builder: (c, sheetState) {
                  return SelectionBottomSheet<RelationOutput, int>(
                    title: "Relation",
                    items: filteredRelationList,
                    selectedValue: selectedReltion?.relId,
                    valueFor: (item) => item.relId ?? 0,
                    labelFor: (item) => item.relName ?? 'NA',
                    height: 360,
                    padding: EdgeInsets.only(
                      top: responsiveHeight(28),
                      left: responsiveHeight(35),
                      right: responsiveHeight(35),
                      bottom: responsiveHeight(60),
                    ),
                    titleTextStyle: TextStyle(
                      fontSize: responsiveFont(16),
                      fontFamily: FontConstants.interFonts,
                    ),
                    titleBottomSpacing: responsiveHeight(30),
                    itemPadding: const EdgeInsets.symmetric(vertical: 8.0),
                    itemContainerPadding: const EdgeInsets.symmetric(
                      horizontal: 4.0,
                    ),
                    selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
                    itemTextStyle: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.normal,
                      color: kBlackColor,
                    ),
                    onItemTap: (item) {
                      sheetState(() {
                        selectedReltion = item;
                      });

                      relationController.text = selectedReltion?.relName ?? "";

                      final relationId =
                          selectedReltion?.relId.toString() ?? "";
                      final relationName = selectedReltion?.relName ?? "";

                      bool isAlreadyAdded = false;
                      int count7 = 0;
                      int count8 = 0;

                      final dependentList = svc.addDependentOutput;

                      for (final dependent in dependentList) {
                        final depRelId = dependent.relId.toString();

                        if (depRelId == relationId &&
                            [
                              "1",
                              "2",
                              "9",
                              "10",
                              "21",
                              "22",
                            ].contains(relationId)) {
                          isAlreadyAdded = true;
                          break;
                        }

                        if (depRelId == "7") count7++;
                        if (depRelId == "8") count8++;
                      }

                      // Also check in screeningdataList
                      for (final screened in screeningdataList) {
                        final screenedRelId = screened.relId.toString();

                        if (screenedRelId == relationId &&
                            [
                              "1",
                              "2",
                              "9",
                              "10",
                              "21",
                              "22",
                            ].contains(relationId)) {
                          isAlreadyAdded = true;
                          break;
                        }

                        if (screenedRelId == "7") count7++;
                        if (screenedRelId == "8") count8++;
                      }

                      if (isAlreadyAdded) {
                        ToastManager.showAlertDialog(
                          Get.context!,
                          "This relation is already added.",
                          () {
                            Navigator.of(
                              Get.context!,
                              rootNavigator: true,
                            ).pop();
                            sheetState(() {
                              selectedReltion = null;
                            });
                          },
                        );

                        relationController.clear();
                        return;
                      }

                      if (relationId == "7" || relationId == "8") {
                        if (count7 >= 2 ||
                            count8 >= 2 ||
                            (count7 >= 1 && count8 >= 1)) {
                          ToastManager.showAlertDialog(
                            Get.context!,
                            "Maximum two children registration is allowed.",
                            () {
                              Navigator.of(
                                Get.context!,
                                rootNavigator: true,
                              ).pop();
                              sheetState(() {
                                selectedReltion = null;
                              });
                            },
                          );

                          relationController.clear();
                          return;
                        }
                      }

                      // Success flow
                      relationController.text = relationName;
                      updateNameFields(relationId, gender, workerGender);

                      Get.back();
                      svc.resetState();
                    },
                  );
                },
              );
            },
          ).whenComplete(() {
            isRelationSheetVisible = false;
            update();
          });
        }

        if (status.isFailure) {
          ToastManager.showAlertDialog(
            Get.context!,
            relationOutput?.message ?? "Something went wrong try again",
            () {
              Get.back();
            },
          );
        }
      }),
    );

    // ── Worker: getAddressDetailStatus ────────────────────────────────────────
    _workers.add(
      ever(svc.getAddressDetailStatus, (FormzSubmissionStatus status) {
        if (status.isSuccess) {
          callingAddressModel = CallingAddressModel.fromJson(
            jsonDecode(svc.getAddressDetailResponse.value),
          );
          addressList.clear();

          final addressData = callingAddressModel?.output;

          if (addressData != null && addressData.isNotEmpty) {
            addressList.addAll(addressData);

            fname = addressList[0].firstName ?? "";
            mname = addressList[0].middleName ?? "";
            lname = addressList[0].lastName ?? "";
            gender = addressList[0].gender ?? "";
          } else {
            debugPrint("Address list is empty or null.");
          }
          update();
        }
      }),
    );
  }

  @override
  void onReady() {
    super.onReady();
    // API: load address details for this beneficiary call.
    svc.fetchAddressDetails({
      "AssignCallID": selectedBeneficiary?.assignCallID.toString(),
    });
  }

  @override
  void onClose() {
    for (final w in _workers) {
      w.dispose();
    }
    relationController.dispose();
    ageController.dispose();
    firstNameController.dispose();
    middleNameController.dispose();
    lastNameController.dispose();
    super.onClose();
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────────

  String? validateInput(String? value) {
    final String relationId = selectedReltion?.relId.toString() ?? '';
    final int beneficiaryAge = selectedBeneficiary?.age ?? 0;

    if (value == null || value.isEmpty) {
      return 'कृपया वय प्रविष्ट करा.';
    }

    final int? enteredAge = int.tryParse(value);

    if (enteredAge == null) {
      return 'अवैध वय प्रविष्ट केले आहे.';
    }

    // Parents / In-laws
    if (relationId == "1" ||
        relationId == "2" ||
        relationId == "21" ||
        relationId == "22") {
      if (enteredAge <= beneficiaryAge) {
        return 'आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम\nकामगारापेक्षा जास्त असावे.';
      }
    }
    // Husband / Wife
    else if (relationId == "10" || relationId == "9") {
      if (enteredAge <= 18 || enteredAge >= 75) {
        return 'पत्नी/पती चे वय १८ पेक्षा जास्त आणि ७५ पेक्षा कमी असावे.';
      }
    }
    // Son / Daughter
    else if (relationId == "7" || relationId == "8") {
      final int ageDifference = beneficiaryAge - enteredAge;

      if (enteredAge <= 10 || ageDifference < 15) {
        return '१. मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त असावे आणि\n२. वयातील फरक किमान १५ वर्ष असावा.';
      }
    }

    return null;
  }

  void updateNameFields(String relationId, String gender, String workerGender) {
    if (relationId == "1") {
      if (gender == "Female" && workerGender == "Female") {
        firstNameController.text = mname;
        middleNameController.text = "";
        lastNameController.text = lname;
      } else if (gender == "Female" && workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = "";
        lastNameController.text = lname;
      } else if (gender == "Male" && workerGender == "Male") {
        firstNameController.text = mname;
        middleNameController.text = "";
        lastNameController.text = lname;
      } else if (gender == "Male" && workerGender == "Female") {
        firstNameController.text = "";
        middleNameController.text = "";
        lastNameController.text = lname;
      }
    } else if (relationId == "2") {
      if (gender == "Female" && workerGender == "Female") {
        firstNameController.text = "";
        middleNameController.text = mname;
        lastNameController.text = lname;
      } else if (gender == "Female" && workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = "";
        lastNameController.text = lname;
      } else if (gender == "Male" && workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = mname;
        lastNameController.text = lname;
      } else if (gender == "Male" && workerGender == "Female") {
        firstNameController.text = "";
        middleNameController.text = "";
        lastNameController.text = lname;
      }
    } else if (relationId == "21" || relationId == "22") {
      firstNameController.text = "";
      middleNameController.text = "";
      lastNameController.text = "";
    } else if (relationId == "7" || relationId == "8") {
      if (gender == "Male" && workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = fname;
        lastNameController.text = lname;
      } else if (gender == "Female" || workerGender == "Female") {
        firstNameController.text = "";
        middleNameController.text = mname;
        lastNameController.text = lname;
      }
    } else if (relationId == "9") {
      firstNameController.text = mname;
      middleNameController.text = "";
      lastNameController.text = lname;
    } else if (relationId == "10") {
      if (gender == "Female" && workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = "";
        lastNameController.text = lname;
      } else if (gender == "Male" || workerGender == "Male") {
        firstNameController.text = "";
        middleNameController.text = fname;
        lastNameController.text = lname;
      }
    }
  }
}
