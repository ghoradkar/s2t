// ignore_for_file: use_build_context_synchronously, avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Screens/CallingModules/controllers/expected_beneficiary_controller.dart';
import 'package:s2toperational/Screens/CallingModules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/CallingModules/models/CallStatusAppConfirm.dart';
import 'package:s2toperational/Screens/CallingModules/models/ScreeningDependentModel.dart';
import 'package:s2toperational/Screens/CallingModules/models/add_dependent_model.dart';
import 'package:s2toperational/Screens/CallingModules/models/calling_address_model.dart';
import 'package:s2toperational/Screens/CallingModules/models/calling_remark_model.dart';
import 'package:s2toperational/Views/CustomTimePicker/CustomTimePickerDialog.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../custom_widgets/selection_bottom_sheet.dart';

class AppointmentConfirmationController extends GetxController {
  final BeneficiaryOutput beneficiary;

  AppointmentConfirmationController({required this.beneficiary});

  // ─── Service controller ──────────────────────────────────────────────────────
  late final ExpectedBeneficiaryController svc;

  // ─── Static constant lists ───────────────────────────────────────────────────
  static const List<Map<String, dynamic>> genderList = [
    {"id": 1, "name": "Male"},
    {"id": 2, "name": "Female"},
    {"id": 3, "name": "Other"},
  ];

  static const List<Map<String, dynamic>> maritalStatusList = [
    {"id": 1, "name": "Married"},
    {"id": 2, "name": "UnMarried"},
    {"id": 3, "name": "Divorcee"},
    {"id": 4, "name": "Widow"},
  ];

  static const List<Map<String, dynamic>> numberOfDependentPendingList = [
    {"id": 0, "name": "0"},
    {"id": 1, "name": "1"},
    {"id": 2, "name": "2"},
    {"id": 3, "name": "3"},
    {"id": 4, "name": "4"},
    {"id": 5, "name": "5"},
  ];

  // ─── Instance date fields (were top-level in screen file) ───────────────────
  var crrDate = DateTime.now();
  late DateTime firstDayOfWeek;

  // ─── TextEditingControllers ──────────────────────────────────────────────────
  final TextEditingController districtTextController = TextEditingController();
  final TextEditingController talukaTextController = TextEditingController();
  TextEditingController callStatusTextController = TextEditingController();
  TextEditingController teamNumberTextController = TextEditingController();
  TextEditingController dateTypeTextController = TextEditingController();
  TextEditingController dateTextController = TextEditingController();
  TextEditingController houseNumberTextController = TextEditingController();
  TextEditingController roadTextController = TextEditingController();
  TextEditingController areaTextController = TextEditingController();
  TextEditingController landMarkTextController = TextEditingController();
  TextEditingController pincodeTextController = TextEditingController();
  TextEditingController regMobileTextController = TextEditingController();
  TextEditingController alternateMobileTextController = TextEditingController();
  TextEditingController workersGenderTextController = TextEditingController();
  TextEditingController workersMartialStatusTextController =
      TextEditingController();
  TextEditingController noOfDependentTextController = TextEditingController();
  TextEditingController dependentScreeningPendingTextController =
      TextEditingController();
  TextEditingController remarkTextController = TextEditingController();
  TextEditingController timeTextController = TextEditingController();
  TextEditingController timeAppTextController = TextEditingController();

  final GlobalKey<FormState> formKey = GlobalKey();

  // ─── State fields ────────────────────────────────────────────────────────────
  bool currentAddressVisibility = true;
  bool screenedVisibility = true;
  bool personalVisibility = true;

  bool isTextFieldVisibilityDateTime = true;
  bool isTextFieldVisibilityPersonalDet = true;

  bool isSubmitted = false;

  bool isShowCallStatusDropDown = false;
  bool isAppointmentAlertVisible = false;

  int districtCode = 0;
  int talukaCode = 0;
  int remarkId = 0;
  int callLog = 0;
  String marriedStatusID = "";
  int callStatus = 0;
  int screenedBeneficiaryCount = 0;
  int differenceCount = 0;
  int isCurrentAsSameRegId = 0;
  int empCode = 0;
  int numberOfDependent = 0;
  int numberOfDependentPending = 0;
  String workerGender = "";
  String fname = "";
  String mname = "";
  String lname = "";
  String road = "";
  String regAddress = "";

  bool hasAddressLoaded = false;

  String isWorkerScreened = "";

  bool isChangeAddress = false;
  CallStatusOutputAppConfirm? selectedCallStatus;
  CallingRemarkOutput? selectedRemark;
  Map<String, dynamic>? selectedGender,
      selectedMaritalStatus,
      selectedNumberOfDependent,
      selectedDependentScreeningPending;

  BeneficiaryOutput? selectedBeneficiary;
  Beneficiaryresponsemodel? beneficiaryresponsemodel;
  ScreeningDependentModel? screeningDependentModel;
  CallStatusAppConfirm? callstatusModel;
  CallingRemarkModel? callingRemarkModel;
  CallingAddressModel? callingAddressModel;
  AddDependentModel? addDependentModel;
  List<BeneficiaryOutput> filteredBeneficiaryList = [];
  final List<CallStatusOutputAppConfirm> filteredCallStatusList = [];
  final List<ScreeningDataOutput> screeningdataList = [];
  final List<CallingRemarkOutput> remarkList = [];
  final List<CallingAddressOutput> addressList = [];

  final List<Worker> _workers = [];

  // ─── Helper: register ever() workers ────────────────────────────────────────
  void _on<T>(Rx<T> obs, void Function(T) cb) =>
      _workers.add(ever(obs, cb));

  @override
  void onInit() {
    super.onInit();
    svc = Get.find<ExpectedBeneficiaryController>();

    // ── ever() workers (replace BlocListener) ──────────────────────────────
    _on(svc.getCallStatusForAppointment, (status) {
      if (status.isSuccess) {
        callstatusModel = CallStatusAppConfirm.fromJson(
          jsonDecode(svc.getCallingForAppointmentResponse.value),
        );
        filteredCallStatusList.clear();

        filteredCallStatusList.addAll(callstatusModel!.output!);
        for (int i = 0; i < filteredCallStatusList.length; i++) {
          if (filteredCallStatusList[i].callingStatus == "Calling Pending") {
            filteredCallStatusList.removeAt(i);
          }
        }
        update();

        if (isShowCallStatusDropDown == true) {
          return;
        }
        isShowCallStatusDropDown = true;
        showModalBottomSheet(
          isDismissible: false,
          enableDrag: false,
          context: Get.context!,
          isScrollControlled: true,
          builder: (context) {
            return StatefulBuilder(
              builder: (c, sheetState) {
                return SelectionBottomSheet<
                  CallStatusOutputAppConfirm,
                  int
                >(
                  title: "Call Status",
                  items: filteredCallStatusList,
                  selectedValue: selectedCallStatus?.assignStatusID,
                  valueFor: (item) => item.assignStatusID ?? 0,
                  labelFor: (item) => fixEncoding(item.callingStatus ?? 'NA'),
                  height: responsiveHeight(500),
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
                  titleBottomSpacing: responsiveHeight(20),
                  itemContainerPadding: const EdgeInsets.symmetric(
                    vertical: 8.0,
                    horizontal: 4.0,
                  ),
                  selectedBackgroundColor: kPrimaryColor.withOpacity(0.1),
                  itemTextStyle: TextStyle(
                    fontSize: 13,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.normal,
                    color: kBlackColor,
                  ),
                  onItemTap: (item) {
                    sheetState(() {
                      selectedCallStatus = item;
                    });

                    isShowCallStatusDropDown = false;
                    callStatusTextController.text = fixEncoding(
                      selectedCallStatus?.callingStatus ?? "",
                    );

                    callStatus = selectedCallStatus?.assignStatusID ?? 0;

                    updateWorkerScreeningStatus(callStatus);

                    if (callStatus == 2 ||
                        callStatus == 3 ||
                        callStatus == 14) {
                      isTextFieldVisibilityDateTime = true;
                    } else {
                      isTextFieldVisibilityDateTime = false;
                    }

                    if (callStatus == 2 ||
                        callStatus == 3 ||
                        callStatus == 14) {
                      isTextFieldVisibilityPersonalDet = true;
                    } else {
                      isTextFieldVisibilityPersonalDet = false;
                    }
                    update();

                    Get.back();
                    svc.resetState();
                  },
                );
              },
            );
          },
        );
      }
    });

    _on(svc.getappointmentstatus, (status) {
      if (status.isInProgress) {
        ToastManager.showLoader();
      } else if (status.isSuccess) {
        ToastManager.hideLoader();
        var jsonResponse =
            jsonDecode(svc.getappointmentResponse.value);

        var outputList = jsonResponse["output"];

        if (outputList != null && outputList.isNotEmpty) {
          int appointmentCount =
              outputList[0]["AppointmentDateCount"] ?? 0;

          if (appointmentCount >= 20 && !isAppointmentAlertVisible) {
            isAppointmentAlertVisible = true;
            showDialog(
              context: Get.context!,
              builder: (context) => AlertDialog(
                title: Text(
                  'Alert',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
                content: Text(
                  '20 appointments are already confirmed on ${timeTextController.text} Are you still want to book this appointment?',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
                actions: [
                  TextButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                    child: Text(
                      'Yes',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                  TextButton(
                    onPressed: () {
                      timeTextController.text = "";
                      Navigator.of(context).pop();
                    },
                    child: Text(
                      'No',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                ],
              ),
            ).whenComplete(() {
              isAppointmentAlertVisible = false;
              svc.resetState();
            });
          }
        }
        update();
      } else if (status.isFailure) {
        ToastManager.hideLoader();
      }
    });

    _on(svc.insertAppointmentStatus, (status) {
      if (status == FormzSubmissionStatus.success) {
        showDialog(
          context: Get.context!,
          builder: (context) => AlertDialog(
            title: Text(
              'Success',
              style: TextStyle(fontFamily: FontConstants.interFonts),
            ),
            content: Text(
              'Details Saved successfully',
              style: TextStyle(fontFamily: FontConstants.interFonts),
            ),
            actions: [
              SizedBox(
                width: 80,
                child: AppActiveButton(
                  buttontitle: "OK",
                  onTap: () {
                    Get.back();
                    Get.back(result: true);
                  },
                ),
              ),
            ],
          ),
        );
      } else if (status == FormzSubmissionStatus.failure) {
        ToastManager.showAlertDialog(
          Get.context!,
          jsonDecode(svc.insertAppointmentResponse.value)['message'],
          () {
            Get.back();
          },
        );
      }
    });

    _on(svc.getRemarkStatus, (status) {
      if (status.isSuccess) {
        callingRemarkModel = CallingRemarkModel.fromJson(
          jsonDecode(svc.getRemarkResponse.value),
        );
        remarkList.clear();

        remarkList.addAll(callingRemarkModel!.output!);
        update();

        showModalBottomSheet(
          context: Get.context!,
          isScrollControlled: true,
          builder: (context) {
            return StatefulBuilder(
              builder: (c, sheetState) {
                return SelectionBottomSheet<CallingRemarkOutput, int>(
                  title: "Remark",
                  items: remarkList,
                  selectedValue: selectedRemark?.cReamrkID,
                  valueFor: (item) => item.cReamrkID ?? 0,
                  labelFor: (item) => fixEncoding(item.callingRemark ?? ""),
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
                    vertical: 8.0,
                    horizontal: 4.0,
                  ),
                  selectedBackgroundColor: kPrimaryColor.withOpacity(0.1),
                  itemTextStyle: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.normal,
                    color: kBlackColor,
                  ),
                  selectedItemTextStyle: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.normal,
                    color: kBlackColor,
                  ),
                  onItemTap: (item) {
                    sheetState(() {
                      selectedRemark = item;
                    });

                    remarkTextController.text = fixEncoding(
                      selectedRemark?.callingRemark ?? "",
                    );
                    remarkId = selectedRemark?.cReamrkID ?? 0;

                    Get.back();
                    svc.resetState();
                  },
                );
              },
            );
          },
        );
      }

      if (status.isFailure) {
        ToastManager.showAlertDialog(Get.context!, "Remark not found", () {
          Get.back();
        });
      }
    });

    _on(svc.getAddressDetailStatus, (status) {
      if (status.isSuccess && !hasAddressLoaded) {
        hasAddressLoaded = true;

        callingAddressModel = CallingAddressModel.fromJson(
          jsonDecode(svc.getAddressDetailResponse.value),
        );
        addressList.clear();

        var addressData = callingAddressModel?.output;

        if (addressData != null && addressData.isNotEmpty) {
          addressList.addAll(addressData);

          districtTextController.text =
              addressList[0].district.toString();
          talukaTextController.text = addressList[0].taluka.toString();
          houseNumberTextController.text =
              addressList[0].houseNo.toString();
          regAddress = addressList[0].regAddress.toString();
          areaTextController.text = addressList[0].area.toString();
          landMarkTextController.text =
              addressList[0].landMark.toString();
          pincodeTextController.text = addressList[0].pincode.toString();
          roadTextController.text = addressList[0].road.toString();

          alternateMobileTextController.text =
              addressList[0].altMobileNo?.toString() ?? "NA";

          timeTextController.text =
              addressList[0].appoinmentDate?.toString() ?? "NA";

          timeAppTextController.text =
              addressList[0].appoinmentTime?.toString() ?? "NA";

          remarkTextController.text = fixEncoding(
            addressList[0].remark ?? "",
          );
          remarkId = addressList[0].remarkID ?? 0;
          callLog = addressList[0].callingLog ?? 0;

          String marriedStatus =
              addressList[0].workersMaritalStatus ?? "";
          marriedStatusID =
              marriedStatus.trim().isEmpty ? "0" : marriedStatus;

          if (marriedStatusID == "1") {
            workersMartialStatusTextController.text = "Married";
          }

          fname = addressList[0].firstName ?? "";
          mname = addressList[0].middleName ?? "";
          lname = addressList[0].lastName ?? "";
          districtCode = addressList[0].dISTLGDCODE ?? 0;
          talukaCode = addressList[0].tALLGDCODE ?? 0;
          workerGender = addressList[0].gender ?? "";
          workersGenderTextController.text = workerGender;
        } else {
          debugPrint("Address list is empty or null.");
        }
        update();

        svc.resetState();
      }
    });

    _on(svc.screenedDependetStatus, (status) {
      if (status.isSuccess) {
        ScreeningDependentModel model = ScreeningDependentModel.fromJson(
          jsonDecode(svc.screenedDependentResponse.value),
        );

        if (model.status == 'Success' && model.output != null) {
          screeningdataList.clear();
          screeningdataList.addAll(model.output!);

          screenedBeneficiaryCount = screeningdataList.length;

          final noOfDepartmentStr = numberOfDependent;

          int screenedCount = 0;
          int departmentCount = 0;

          try {
            screenedCount = screenedBeneficiaryCount;
            departmentCount = noOfDepartmentStr;
          } catch (e) {
            debugPrint("Invalid input: $e");
          }

          differenceCount = (screenedCount - departmentCount).abs();
          update();
        } else {
          screeningdataList.clear();
          update();
        }
      }

      if (status.isFailure) {
        screenedBeneficiaryCount = 0;
      }
    });

    _on(svc.getDependentStatus, (status) {
      if (status.isSuccess) {
        AddDependentModel addDependentModelLocal = AddDependentModel.fromJson(
          jsonDecode(svc.getDependentResponse.value),
        );
        if (addDependentModelLocal.status == 'Success') {
          svc.addDependent(addDependentModelLocal.output ?? []);
        }
        svc.resetState();
      }

      if (status.isFailure) {
        AddDependentModel addDependentModelLocal = AddDependentModel(
          status: 'Failure',
          output: [],
        );

        svc.addDependent(addDependentModelLocal.output ?? []);
        svc.resetState();
      }
    });

    _on(svc.addDependentStatus, (status) {
      if (status.isSuccess) {
        svc.resetState();
      }
    });
    // ── end ever() workers ─────────────────────────────────────────────────

    var userDetails = DataProvider().getParsedUserData();
    empCode = userDetails?.output?[0].empCode ?? 0;

    // Initialise selectedBeneficiary immediately — no postFrame needed
    // since beneficiary is passed via constructor (no context dependency).
    selectedBeneficiary = beneficiary;

    callStatusTextController.text = fixEncoding(
      selectedBeneficiary?.callingStatus.toString() ?? "",
    );

    callStatus = selectedBeneficiary?.assignStatusID ?? 0;

    if (callStatus == 5 ||
        callStatus == 4 ||
        callStatus == 6 ||
        callStatus == 7 ||
        callStatus == 8 ||
        callStatus == 9 ||
        callStatus == 12 ||
        callStatus == 13 ||
        callStatus == 11) {
      isTextFieldVisibilityDateTime = false;
      timeTextController.clear();
      timeAppTextController.clear();
      dateTextController.clear();
      remarkTextController.clear();

      currentAddressVisibility = !currentAddressVisibility;
    } else {
      isTextFieldVisibilityDateTime = true;
    }

    regMobileTextController.text =
        selectedBeneficiary?.mobile.toString() ?? "";

    noOfDependentTextController.text =
        selectedBeneficiary?.noOfDependants.toString() ?? "";

    dependentScreeningPendingTextController.text =
        selectedBeneficiary?.dependantScreeningPending.toString() ?? "";

    numberOfDependent = selectedBeneficiary?.noOfDependants ?? 0;
    numberOfDependentPending =
        selectedBeneficiary?.dependantScreeningPending ?? 0;
  }

  @override
  void onReady() {
    super.onReady();
    // onReady() is already called after the first frame — no extra
    // addPostFrameCallback wrapper needed.
    final assignCallID = selectedBeneficiary?.assignCallID.toString();

    // API: load registered/current address details for this beneficiary call.
    svc.fetchAddressDetails({"AssignCallID": assignCallID});

    // API: fetch already-screened dependents to show counts and status.
    svc.fetchScreenedDependentDetails(
      {"AssignCallID": assignCallID, "Type": "1"},
    );

    // API: load previously saved dependents for this call (prefill/add list).
    svc.fetchDependentDetails({
      "AssignCallID": selectedBeneficiary?.assignCallID.toString(),
    });
  }

  @override
  void onClose() {
    for (final w in _workers) {
      w.dispose();
    }
    districtTextController.dispose();
    talukaTextController.dispose();
    callStatusTextController.dispose();
    teamNumberTextController.dispose();
    dateTypeTextController.dispose();
    dateTextController.dispose();
    houseNumberTextController.dispose();
    roadTextController.dispose();
    areaTextController.dispose();
    landMarkTextController.dispose();
    pincodeTextController.dispose();
    regMobileTextController.dispose();
    alternateMobileTextController.dispose();
    workersGenderTextController.dispose();
    workersMartialStatusTextController.dispose();
    noOfDependentTextController.dispose();
    dependentScreeningPendingTextController.dispose();
    remarkTextController.dispose();
    timeTextController.dispose();
    timeAppTextController.dispose();
    super.onClose();
  }

  // ─── Helper methods ──────────────────────────────────────────────────────────

  String getRelationText(int? relId) {
    Map<int, String> relationMap = {
      1: 'Father',
      2: 'Mother',
      7: 'Son',
      8: 'Daughter',
      9: 'Husband',
      10: 'Wife',
      21: 'Mother-in-law',
      22: 'Father-in-law',
    };

    return relationMap[relId] ?? 'Unknown';
  }

  List<Map<String, dynamic>> generateDependentList(String? noOfDependent) {
    int count = int.tryParse(noOfDependent ?? "0") ?? 0;

    return List.generate(count + 1, (i) => {"id": i, "name": i.toString()});
  }

  String fixEncoding(String text) {
    return utf8.decode(text.runes.toList());
  }

  String formatTimeOfDay(TimeOfDay time) {
    final now = DateTime.now();
    final dateTime = DateTime(
      now.year,
      now.month,
      now.day,
      time.hour,
      time.minute,
    );
    return DateFormat('hh:mm a').format(dateTime);
  }

  DateTime parseDate(String date) {
    final String trimmed = date.trim();
    if (trimmed.isEmpty) {
      return DateTime.now();
    }

    final List<DateFormat> formats = [
      DateFormat('dd-MMMM-yyyy'),
      DateFormat('dd-MMM-yyyy'),
      DateFormat('dd-MMM-yy'),
      DateFormat('dd-MM-yyyy'),
      DateFormat('dd/MM/yyyy'),
    ];

    for (final format in formats) {
      try {
        return format.parse(trimmed);
      } catch (_) {
        // Try next format.
      }
    }

    // Fallback to now if the date is in an unexpected format.
    return DateTime.now();
  }

  TimeOfDay stringToTimeOfDay(String? time) {
    if (time == null || time.trim().isEmpty) {
      return TimeOfDay.now(); // default when field is empty
    }

    final String trimmed = time.trim();
    // Accept both "4:30PM" and "4:30 PM" by normalizing spacing before AM/PM.
    final String normalized = trimmed.replaceAllMapped(
      RegExp(r'\s*(am|pm)$', caseSensitive: false),
      (match) => ' ${match.group(1)!.toUpperCase()}',
    );

    try {
      return TimeOfDay.fromDateTime(DateFormat('hh:mm a').parse(normalized));
    } catch (_) {
      // Fallback for any unexpected format to avoid crashing the picker.
      return TimeOfDay.now();
    }
  }

  List<Map<String, dynamic>> getFilteredDependents(String? marriedStatusIDParam) {
    int statusId = int.tryParse(marriedStatusIDParam ?? "") ?? 0;

    if (statusId == 1) {
      return numberOfDependentPendingList
          .where((item) => item["id"] <= 5)
          .toList();
    } else if (statusId == 2) {
      return numberOfDependentPendingList
          .where((item) => item["id"] <= 2)
          .toList();
    } else if (statusId == 3 || statusId == 4) {
      return numberOfDependentPendingList
          .where((item) => item["id"] <= 4)
          .toList();
    } else {
      return [];
    }
  }

  String? validatePincodeInput(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please Enter Pincode';
    }
    if (value.length != 6) {
      return 'Pincode should be 6 letters.';
    }

    return null;
  }

  String? validateMobileInput(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please Enter mobile number';
    }
    if (value.length != 10) {
      return 'mobile number should be 10 digit.';
    }

    return null;
  }

  void updateWorkerScreeningStatus(int? value) {
    isWorkerScreened = selectedBeneficiary?.isWorkerScreened ?? "";

    if (isWorkerScreened == "NO" && callStatus == 11) {
      Future.delayed(const Duration(milliseconds: 300), () {
        ToastManager.showAlertDialog(
          Get.context!,
          "You cannot select this status as worker screening is pending.",
          () {
            Get.back();
          },
        );
      });

      isTextFieldVisibilityDateTime = false;
      callStatusTextController.clear();
      update();
    }
  }

  void filterList(String query) {
    filteredBeneficiaryList =
        beneficiaryresponsemodel!.output!
            .where(
              (item) =>
                  item.beneficiaryName!.toLowerCase().contains(
                    query.toLowerCase(),
                  ) ||
                  item.area!.toLowerCase().contains(query.toLowerCase()) ||
                  item.mobile!.toLowerCase().contains(query.toLowerCase()) ||
                  item.assignCallID!.toString().toLowerCase().contains(
                    query.toLowerCase(),
                  ),
            )
            .toList();

    if (filteredBeneficiaryList.isEmpty) {
      filteredBeneficiaryList = beneficiaryresponsemodel!.output!;
    }
    update();
  }

  void toggleChangeAddress(bool? value) {
    isChangeAddress = value ?? false;
    // Keep the registered/current address flag in sync.
    isCurrentAsSameRegId = isChangeAddress ? 1 : 0;
    update();
  }

  Future<void> showCustomTimePicker() async {
    if (timeTextController.text.isEmpty) {
      ScaffoldMessenger.of(Get.context!).showSnackBar(
        const SnackBar(content: Text('Please select a date first')),
      );
      return;
    }

    final TimeOfDay? picked = await showDialog<TimeOfDay>(
      context: Get.context!,
      builder: (BuildContext context) {
        return CustomTimePickerDialog(
          initialTime: stringToTimeOfDay(timeAppTextController.text),
          selectedDate: parseDate(timeTextController.text),
        );
      },
    );

    if (picked != null) {
      timeAppTextController.text = formatTimeOfDay(picked);
      update();
    }
  }
}
