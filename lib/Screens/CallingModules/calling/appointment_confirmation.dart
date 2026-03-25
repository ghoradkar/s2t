// ignore_for_file: use_build_context_synchronously, use_full_hex_values_for_flutter_colors, avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Screens/CallingModules/calling/models/CallStatusAppConfirm.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Views/CustomTimePicker/CustomTimePickerDialog.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../custom_widgets/beneficiary_card.dart';
import '../custom_widgets/no_data_widget.dart';
import '../custom_widgets/selection_bottom_sheet.dart';
import '../routes/app_routes.dart';
import 'bloc/expected_beneficiary_bloc.dart';
import 'models/BeneficiaryResponseModel.dart';
import 'models/ScreeningDependentModel.dart';
import 'models/add_dependent_model.dart';
import 'models/calling_address_model.dart';
import 'models/calling_remark_model.dart';
import 'models/team_data_model.dart';

class AppointmentConfirmation extends StatefulWidget {
  const AppointmentConfirmation({super.key});

  @override
  State<AppointmentConfirmation> createState() =>
      _AppointmentConfirmationState();
}

var crrDate = DateTime.now();
late DateTime firstDayOfWeek;

class _AppointmentConfirmationState extends State<AppointmentConfirmation> {
  final TextEditingController _districtTextController = TextEditingController();
  final TextEditingController _talukaTextController = TextEditingController();
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

  final GlobalKey<FormState> _formKey = GlobalKey();

  bool _currentAddressVisibility = true;
  bool _screenedVisibility = true;
  bool _personalVisibility = true;

  bool _isTextFieldVisibilityDateTime = true;
  bool _isTextFieldVisibilityPersonalDet = true;

  bool _isSubmitted = false;

  bool _isShowCallStatusDropDown = false;
  bool _isAppointmentAlertVisible = false;

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

  bool _isChangeAddress = false;
  CallStatusOutputAppConfirm? selectedCallStatus;
  CallingRemarkOutput? selectedRemark;
  TeamDataOutput? selectedTeamData;
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
  TeamDataModel? teamDataModel;
  List<BeneficiaryOutput> _filteredList = [];
  final List<CallStatusOutputAppConfirm> _filteredCallStatusList = [];
  final List<ScreeningDataOutput> _screeningdataList = [];
  final List<CallingRemarkOutput> _remarkList = [];
  final List<CallingAddressOutput> _addressList = [];

  final List<Map<String, dynamic>> _genderList = [
    {"id": 1, "name": "Male"},
    {"id": 2, "name": "Female"},
    {"id": 3, "name": "Other"},
  ];

  final List<Map<String, dynamic>> _maritalStatusList = [
    {"id": 1, "name": "Married"},
    {"id": 2, "name": "UnMarried"},
    {"id": 3, "name": "Divorcee"},
    {"id": 4, "name": "Widow"},
  ];
  final List<Map<String, dynamic>> _numberOfDependentPendingList = [
    {"id": 0, "name": "0"},
    {"id": 1, "name": "1"},
    {"id": 2, "name": "2"},
    {"id": 3, "name": "3"},
    {"id": 4, "name": "4"},
    {"id": 5, "name": "5"},
  ];

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

  @override
  void initState() {
    super.initState();
    // _getCaptchaDetails();

    var userDetails = DataProvider().getParsedUserData();
    empCode = userDetails?.output?[0].empCode ?? 0;

    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      setState(() {
        selectedBeneficiary =
            ModalRoute.of(context)!.settings.arguments! as BeneficiaryOutput;

        callStatusTextController.text = fixEncoding(
          selectedBeneficiary?.callingStatus.toString() ?? "",
        );

        callStatus = selectedBeneficiary?.assignStatusID ?? 0;

        //  callStatus = 5;

        if (callStatus == 5 ||
            callStatus == 4 ||
            callStatus == 6 ||
            callStatus == 7 ||
            callStatus == 8 ||
            callStatus == 9 ||
            callStatus == 12 ||
            callStatus == 13 ||
            callStatus == 11) {
          _isTextFieldVisibilityDateTime = false;
          timeTextController.clear();
          timeAppTextController.clear();
          dateTextController.clear();
          remarkTextController.clear();

          setState(() {
            _currentAddressVisibility = !_currentAddressVisibility;
          });
        } else {
          _isTextFieldVisibilityDateTime = true;
        }

        regMobileTextController.text =
            selectedBeneficiary?.mobile.toString() ?? "";

        noOfDependentTextController.text =
            selectedBeneficiary?.noOfDependants.toString() ?? "";

        // callStatus = selectedBeneficiary?.assignCallID ?? 0;

        dependentScreeningPendingTextController.text =
            selectedBeneficiary?.dependantScreeningPending.toString() ?? "";

        numberOfDependent = selectedBeneficiary?.noOfDependants ?? 0;
        numberOfDependentPending =
            selectedBeneficiary?.dependantScreeningPending ?? 0;
      });
    });

    WidgetsBinding.instance.addPostFrameCallback((_) {
      final assignCallID = selectedBeneficiary?.assignCallID.toString();

      // API: load registered/current address details for this beneficiary call.
      context.read<ExpectedBeneficiaryBloc>().add(
        GetAddressDetails(payload: {"AssignCallID": assignCallID}),
      );

      context.read<ExpectedBeneficiaryBloc>().add(
        // API: fetch already-screened dependents to show counts and status.
        GetScreenedDependentDetails(
          payload: {"AssignCallID": assignCallID, "Type": "1"},
        ),
      );
    });

    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      // API: load previously saved dependents for this call (prefill/add list).
      context.read<ExpectedBeneficiaryBloc>().add(
        GetDependentDetails(
          payload: {
            "AssignCallID": selectedBeneficiary?.assignCallID.toString(),
          },
        ),
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    String marriedStatusIDNew = marriedStatusID;

    List<Map<String, dynamic>> filteredList = getFilteredDependents(
      marriedStatusIDNew,
    );

    String selectedDependentId = numberOfDependent.toString();

    List<Map<String, dynamic>> dependentList = generateDependentList(
      selectedDependentId,
    );

    return NetworkWrapper(
      child: BlocListener<ExpectedBeneficiaryBloc, ExpectedBeneficiaryState>(
        listener: (context, state) {
          if (state.getCallStatusForAppointment.isSuccess) {
            callstatusModel = CallStatusAppConfirm.fromJson(
              jsonDecode(state.getCallingForAppointmentResponse),
            );
            _filteredCallStatusList.clear();

            setState(() {
              _filteredCallStatusList.addAll(callstatusModel!.output!);
              // _filteredCallStatusList.removeAt(0);
              for (int i = 0; i < _filteredCallStatusList.length; i++) {
                if (_filteredCallStatusList[i].callingStatus ==
                    "Calling Pending") {
                  _filteredCallStatusList.removeAt(i);
                }
              }
            });

            if (_isShowCallStatusDropDown == true) {
              return;
            }
            _isShowCallStatusDropDown = true;
            showModalBottomSheet(
              isDismissible: false,
              enableDrag: false,
              context: context,
              isScrollControlled: true,
              builder: (context) {
                return StatefulBuilder(
                  builder: (c, sheetState) {
                    return SelectionBottomSheet<
                      CallStatusOutputAppConfirm,
                      int
                    >(
                      title: "Call Status",
                      items: _filteredCallStatusList,
                      selectedValue: selectedCallStatus?.assignStatusID,
                      valueFor: (item) => item.assignStatusID ?? 0,
                      labelFor:
                          (item) => fixEncoding(item.callingStatus ?? 'NA'),
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
                        fontSize: 13.sp,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.normal,
                        color: kBlackColor,
                      ),
                      onItemTap: (item) {
                        // STEP 1: Update the visual state FIRST using sheetState
                        sheetState(() {
                          selectedCallStatus = item;
                        });

                        // STEP 2: Update the main view state
                        setState(() {
                          _isShowCallStatusDropDown = false;
                          callStatusTextController.text = fixEncoding(
                            selectedCallStatus?.callingStatus ?? "",
                          );

                          callStatus = selectedCallStatus?.assignStatusID ?? 0;

                          updateWorkerScreeningStatus(callStatus);

                          if (callStatus == 2 ||
                              callStatus == 3 ||
                              callStatus == 14) {
                            _isTextFieldVisibilityDateTime = true;
                          } else {
                            _isTextFieldVisibilityDateTime = false;
                          }

                          if (callStatus == 2 ||
                              callStatus == 3 ||
                              callStatus == 14) {
                            _isTextFieldVisibilityPersonalDet = true;
                          } else {
                            _isTextFieldVisibilityPersonalDet = false;
                          }
                        });

                        // STEP 3: Close sheet and trigger bloc event
                        Navigator.pop(context);
                        context.read<ExpectedBeneficiaryBloc>().add(
                          ResetExpectedBeneficiaryState(),
                        );
                      },
                    );
                  },
                );
              },
            );
          }
          if (state.getappointmentstatus.isInProgress) {
            ToastManager.showLoader();
          } else if (state.getappointmentstatus.isSuccess) {
            ToastManager.hideLoader();
            setState(() {
              var jsonResponse = jsonDecode(state.getappointmentResponse);

              var outputList = jsonResponse["output"];

              if (outputList != null && outputList.isNotEmpty) {
                int appointmentCount =
                    outputList[0]["AppointmentDateCount"] ?? 0;

                //  int appointmentCount = 21;

                if (appointmentCount >= 20 &&
                    !_isAppointmentAlertVisible &&
                    mounted) {
                  _isAppointmentAlertVisible = true;
                  showDialog(
                    context: context,
                    builder:
                        (context) => AlertDialog(
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
                    _isAppointmentAlertVisible = false;
                    context.read<ExpectedBeneficiaryBloc>().add(
                      ResetExpectedBeneficiaryState(),
                    );
                  });
                }
              }
            });
          } else if (state.getappointmentstatus.isFailure) {
            ToastManager.hideLoader();
          }

          if (state.insertAppointmentStatus == FormzSubmissionStatus.success) {
            showDialog(
              context: context,
              builder:
                  (context) => AlertDialog(
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
                        width: 80.w,
                        child: AppActiveButton(
                          buttontitle: "OK",
                          onTap: () {
                            Navigator.pop(context);
                            Navigator.pop(context, true);
                          },
                        ),
                      ),
                    ],
                  ),
            );
          } else if (state.insertAppointmentStatus ==
              FormzSubmissionStatus.failure) {
            ToastManager.showAlertDialog(
              context,
              jsonDecode(state.insertAppointmentResponse)['message'],
              () {
                Navigator.pop(context);
              },
            );

            // showDialog(
            //   context: context,
            //   builder:
            //       (context) => AlertDialog(
            //         title: Text(
            //           'Alert',
            //           style: TextStyle(fontFamily: FontConstants.interFonts),
            //         ),
            //         content: Text(
            //           jsonDecode(state.insertAppointmentResponse)['message'],
            //         ),
            //         actions: [
            //           SizedBox(
            //             width: 80.w,
            //             child: AppActiveButton(
            //               buttontitle: "OK",
            //               onTap: () {
            //                 Navigator.pop(context);
            //               },
            //             ),
            //           ),
            //         ],
            //       ),
            // );
          }

          if (state.getRemarkStatus.isSuccess) {
            callingRemarkModel = CallingRemarkModel.fromJson(
              jsonDecode(state.getRemarkResponse),
            );
            _remarkList.clear();

            setState(() {
              _remarkList.addAll(callingRemarkModel!.output!);
            });
            showModalBottomSheet(
              context: context,
              isScrollControlled: true,
              builder: (context) {
                return StatefulBuilder(
                  builder: (c, sheetState) {
                    return SelectionBottomSheet<CallingRemarkOutput, int>(
                      title: "Remark",
                      items: _remarkList,
                      selectedValue: selectedRemark?.cReamrkID,
                      valueFor: (item) => item.cReamrkID ?? 0,
                      labelFor: (item) => fixEncoding(item.callingRemark ?? ""),
                      height: 360.h,
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
                        // STEP 1: Update the visual state FIRST using sheetState
                        sheetState(() {
                          selectedRemark = item;
                        });

                        // STEP 2: Update text controller and remarkId
                        remarkTextController.text = fixEncoding(
                          selectedRemark?.callingRemark ?? "",
                        );
                        remarkId = selectedRemark?.cReamrkID ?? 0;

                        // STEP 3: Close sheet and trigger bloc event
                        Navigator.pop(context);
                        context.read<ExpectedBeneficiaryBloc>().add(
                          ResetExpectedBeneficiaryState(),
                        );
                      },
                    );
                  },
                );
              },
            );
          }

          if (state.getRemarkStatus.isFailure) {
            ToastManager.showAlertDialog(context, "Remark not found", () {
              Navigator.pop(context);
            });
          }

          if (state.getAddressDetailStatus.isSuccess && !hasAddressLoaded) {
            hasAddressLoaded = true;

            callingAddressModel = CallingAddressModel.fromJson(
              jsonDecode(state.getAddressDetailResponse),
            );
            _addressList.clear();

            setState(() {
              var addressData = callingAddressModel?.output;

              if (addressData != null && addressData.isNotEmpty) {
                _addressList.addAll(addressData);

                _districtTextController.text =
                    _addressList[0].district.toString();
                _talukaTextController.text = _addressList[0].taluka.toString();
                houseNumberTextController.text =
                    _addressList[0].houseNo.toString();
                regAddress = _addressList[0].regAddress.toString();
                areaTextController.text = _addressList[0].area.toString();
                landMarkTextController.text =
                    _addressList[0].landMark.toString();
                pincodeTextController.text = _addressList[0].pincode.toString();
                roadTextController.text = _addressList[0].road.toString();

                alternateMobileTextController.text =
                    _addressList[0].altMobileNo?.toString() ?? "NA";

                timeTextController.text =
                    _addressList[0].appoinmentDate?.toString() ?? "NA";

                timeAppTextController.text =
                    _addressList[0].appoinmentTime?.toString() ?? "NA";

                // remarkTextController.text = _addressList[0].remark ?? "";
                remarkTextController.text = fixEncoding(
                  _addressList[0].remark ?? "",
                );
                remarkId = _addressList[0].remarkID ?? 0;
                callLog = _addressList[0].callingLog ?? 0;

                String marriedStatus =
                    _addressList[0].workersMaritalStatus ?? "";
                marriedStatusID =
                    marriedStatus.trim().isEmpty ? "0" : marriedStatus;

                if (marriedStatusID == "1") {
                  workersMartialStatusTextController.text = "Married";
                }

                fname = _addressList[0].firstName ?? "";
                mname = _addressList[0].middleName ?? "";
                lname = _addressList[0].lastName ?? "";
                districtCode = _addressList[0].dISTLGDCODE ?? 0;
                talukaCode = _addressList[0].tALLGDCODE ?? 0;
                workerGender = _addressList[0].gender ?? "";
                workersGenderTextController.text = workerGender;
              } else {
                debugPrint("Address list is empty or null.");
              }
            });

            context.read<ExpectedBeneficiaryBloc>().add(
              ResetExpectedBeneficiaryState(),
            );
          }

          if (state.screenedDependetStatus.isSuccess) {
            // context
            //     .read<ExpectedBeneficiaryBloc>()
            //     .add(ResetExpectedBeneficiaryState());
            ScreeningDependentModel model = ScreeningDependentModel.fromJson(
              jsonDecode(state.screenedDependentResponse),
            );

            if (model.status == 'Success' && model.output != null) {
              setState(() {
                _screeningdataList.clear();
                _screeningdataList.addAll(model.output!);

                screenedBeneficiaryCount = _screeningdataList.length;

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
              });
            } else {
              // Optional: Handle "No Data" or "Failed" response
              setState(() {
                _screeningdataList.clear();
              });
            }
          }

          if (state.screenedDependetStatus.isFailure) {
            screenedBeneficiaryCount = 0;
          }

          if (state.getDependentStatus.isSuccess) {
            AddDependentModel addDependentModel = AddDependentModel.fromJson(
              jsonDecode(state.getDependentResponse),
            );
            if (addDependentModel.status == 'Success') {
              context.read<ExpectedBeneficiaryBloc>().add(
                AddDependentRequest(
                  addDependentModel: addDependentModel.output ?? [],
                ),
              );
            }
            context.read<ExpectedBeneficiaryBloc>().add(
              ResetExpectedBeneficiaryState(),
            );
          }

          if (state.getDependentStatus.isFailure) {
            // Clear the data by using an empty model
            AddDependentModel addDependentModel = AddDependentModel(
              status: 'Failure', // or null/empty depending on your model
              output: [], // Assuming output is a list
            );

            // Optionally dispatch an event with empty data if needed
            context.read<ExpectedBeneficiaryBloc>().add(
              AddDependentRequest(
                addDependentModel: addDependentModel.output ?? [],
              ),
            );

            // Reset the state
            context.read<ExpectedBeneficiaryBloc>().add(
              ResetExpectedBeneficiaryState(),
            );
          }

          if (state.addDependentStatus.isSuccess) {
            context.read<ExpectedBeneficiaryBloc>().add(
              ResetExpectedBeneficiaryState(),
            );
          }
        },
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Appointment Confirmation',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Navigator.pop(context);
            },
          ),
          body: BlocBuilder<ExpectedBeneficiaryBloc, ExpectedBeneficiaryState>(
            builder: (context, state) {
              return selectedBeneficiary != null
                  ? Form(
                    key: _formKey,
                    child: Padding(
                      padding: EdgeInsets.symmetric(
                        vertical: 8.h,
                        horizontal: 14.w,
                      ),
                      child: SingleChildScrollView(
                        padding: EdgeInsets.only(
                          bottom: MediaQuery.of(context).padding.bottom,
                        ),
                        child: Column(
                          children: [
                            SizedBox(height: 6.h),
                            Container(
                              decoration: BoxDecoration(
                                border: Border.all(
                                  color: kTextColor.withValues(alpha: 0.2),
                                ),
                                color: kWhiteColor,
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Padding(
                                padding: EdgeInsets.symmetric(
                                  vertical: 10.h,
                                  horizontal: 10.w,
                                ),
                                child: Column(
                                  children: [
                                    Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceBetween,
                                      crossAxisAlignment:
                                          CrossAxisAlignment
                                              .start, // Aligns text to top
                                      children: [
                                        Expanded(
                                          // Allows text to wrap
                                          child: Text(
                                            selectedBeneficiary
                                                    ?.beneficiaryName ??
                                                "",
                                            style: TextStyle(
                                              fontSize: 14.sp,
                                              fontWeight: FontWeight.w600,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                            ),
                                            softWrap:
                                                true, // Allows text to wrap
                                          ),
                                        ),
                                        Container(
                                          decoration: BoxDecoration(
                                            borderRadius: BorderRadius.circular(
                                              responsiveHeight(5),
                                            ),
                                            color: getStatusColor(
                                              selectedBeneficiary?.groupID ?? 0,
                                            ),
                                          ),
                                          padding: EdgeInsets.symmetric(
                                            vertical: 4.h,
                                            horizontal: 4.w,
                                          ),
                                          child: Text(
                                            selectedBeneficiary?.assignStatus ??
                                                "",
                                            style: TextStyle(
                                              color: kWhiteColor,
                                              fontSize: 11.sp,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                    SizedBox(width: 10.w),
                                    Row(
                                      children: [
                                        Image.asset(calendar, scale: 3.5),
                                        SizedBox(width: 10.w),
                                        Text(
                                          "Age : ${selectedBeneficiary?.age}",
                                          style: TextStyle(
                                            fontSize: 14.sp,
                                            fontWeight: FontWeight.w500,
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ],
                                    ),
                                    SizedBox(height: 18.h),
                                    AppTextField(
                                      onTap: () {
                                        // API: fetch valid call-status options for this appointment.
                                        context
                                            .read<ExpectedBeneficiaryBloc>()
                                            .add(
                                              GetCallStatusForAppointment(
                                                payload: {
                                                  "AssignCallID":
                                                      selectedBeneficiary
                                                          ?.assignCallID
                                                          .toString(),
                                                },
                                              ),
                                            );
                                        remarkTextController.clear();
                                      },
                                      controller: callStatusTextController,
                                      readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: "Call Status",
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14) * 1.33,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w600,
                                          ),
                                          children: <TextSpan>[
                                            TextSpan(
                                              text: ' *',
                                              style: TextStyle(
                                                color: Colors.red,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      inputStyle: TextStyle(
                                        fontSize: responsiveFont(14),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(14),
                                      ),
                                      prefixIcon: Image.asset(
                                        callIcon,
                                        scale: 3.5,
                                      ),
                                      suffixIcon: Image.asset(
                                        downArrow,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: 8.h),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(height: 10.h),
                            Container(
                              decoration: BoxDecoration(
                                boxShadow: const [
                                  BoxShadow(
                                    offset: Offset(0, 1),
                                    color: Color(0xff00000026),
                                    spreadRadius: 0,
                                    blurRadius: 10,
                                  ),
                                ],
                                color: const Color(0XFFFFFFFF),
                                border: Border.all(
                                  color: const Color(0xFFD1D1D1),
                                  width: 1,
                                ),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Column(
                                children: [
                                  Material(
                                    color: Colors.transparent,
                                    child: InkWell(
                                      onTap: () {
                                        setState(() {
                                          _currentAddressVisibility =
                                              !_currentAddressVisibility;
                                        });
                                      },
                                      child: Ink(
                                        decoration: BoxDecoration(
                                          color: const Color(0XFF423897),
                                          borderRadius: BorderRadius.circular(
                                            10,
                                          ),
                                        ),
                                        child: Padding(
                                          padding: EdgeInsets.symmetric(
                                            vertical: 10.h,
                                            horizontal: 10.w,
                                          ),
                                          child: Column(
                                            children: [
                                              Row(
                                                mainAxisAlignment:
                                                    MainAxisAlignment
                                                        .spaceBetween,
                                                children: [
                                                  Text(
                                                    "Current Address & Contact info*",
                                                    style: TextStyle(
                                                      color: const Color(
                                                        0XFFFFFFFF,
                                                      ),
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                      fontSize: responsiveFont(
                                                        14,
                                                      ),
                                                      fontWeight:
                                                          FontWeight.w400,
                                                    ),
                                                  ),
                                                  _currentAddressVisibility
                                                      ? Image.asset(
                                                        upArrow,
                                                        color: Colors.white,
                                                        height: 24.h,
                                                        width: 24.w,
                                                      )
                                                      : Image.asset(
                                                        downArrow,
                                                        height: 24.h,
                                                        width: 24.w,
                                                        color: Colors.white,
                                                      ),
                                                ],
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                  Visibility(
                                    visible: _currentAddressVisibility,
                                    child: Padding(
                                      padding: EdgeInsets.symmetric(
                                        vertical: 20.h,
                                        horizontal: 10.w,
                                      ),
                                      child: Column(
                                        children: [
                                          Row(
                                            children: [
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  controller:
                                                      _districtTextController,
                                                  readOnly: true,
                                                  textInputType:
                                                      TextInputType.name,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'District',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                      children: <TextSpan>[
                                                        TextSpan(
                                                          text: '*',
                                                          style: TextStyle(
                                                            color: Colors.red,
                                                            fontSize:
                                                                responsiveFont(
                                                                  12,
                                                                ),
                                                          ),
                                                        ),
                                                      ],
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMap,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                  suffixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        downArrow,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              SizedBox(
                                                width: responsiveHeight(10),
                                              ),
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  controller:
                                                      _talukaTextController,
                                                  readOnly: true,
                                                  textInputType:
                                                      TextInputType.name,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Taluka',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                      children: <TextSpan>[
                                                        TextSpan(
                                                          text: '*',
                                                          style: TextStyle(
                                                            color: Colors.red,
                                                            fontSize:
                                                                responsiveFont(
                                                                  12,
                                                                ),
                                                          ),
                                                        ),
                                                      ],
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMap,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                  suffixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        downArrow,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),
                                          AppTextField(
                                            controller:
                                                houseNumberTextController,
                                            readOnly: !_isChangeAddress,
                                            textInputType: TextInputType.name,
                                            inputStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                              color: Colors.black,
                                            ),
                                            label: RichText(
                                              text: TextSpan(
                                                text:
                                                    'House Number and Building Name',
                                                style: TextStyle(
                                                  color: kLabelTextColor,
                                                  fontSize:
                                                      responsiveFont(14) * 1.33,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w600,
                                                ),
                                              ),
                                            ),
                                            labelStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                            ),
                                            prefixIcon: SizedBox(
                                              height: responsiveHeight(24),
                                              width: responsiveHeight(24),
                                              child: Center(
                                                child: Image.asset(
                                                  iconMap,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                              ),
                                            ),
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),
                                          AppTextField(
                                            controller: roadTextController,
                                            readOnly: !_isChangeAddress,
                                            textInputType: TextInputType.name,
                                            inputStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                              color: Colors.black,
                                            ),
                                            label: RichText(
                                              text: TextSpan(
                                                text: 'Road',
                                                style: TextStyle(
                                                  color: kLabelTextColor,
                                                  fontSize:
                                                      responsiveFont(14) * 1.33,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w600,
                                                ),
                                              ),
                                            ),
                                            labelStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                            ),
                                            prefixIcon: SizedBox(
                                              height: responsiveHeight(24),
                                              width: responsiveHeight(24),
                                              child: Center(
                                                child: Image.asset(
                                                  iconMap,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                              ),
                                            ),
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),
                                          AppTextField(
                                            controller: areaTextController,
                                            readOnly: !_isChangeAddress,
                                            textInputType: TextInputType.name,
                                            inputStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                              color: Colors.black,
                                            ),
                                            label: RichText(
                                              text: TextSpan(
                                                text: 'Area',
                                                style: TextStyle(
                                                  color: kLabelTextColor,
                                                  fontSize:
                                                      responsiveFont(14) * 1.33,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w600,
                                                ),
                                              ),
                                            ),
                                            labelStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                            ),
                                            prefixIcon: SizedBox(
                                              height: responsiveHeight(24),
                                              width: responsiveHeight(24),
                                              child: Center(
                                                child: Image.asset(
                                                  iconMap,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                              ),
                                            ),
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),
                                          Row(
                                            children: [
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  controller:
                                                      landMarkTextController,
                                                  readOnly: !_isChangeAddress,
                                                  textInputType:
                                                      TextInputType.name,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Landmark',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMap,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              SizedBox(
                                                width: responsiveHeight(10),
                                              ),
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  controller:
                                                      pincodeTextController,
                                                  validators:
                                                      _validatePincodeInput,
                                                  errorText:
                                                      _validatePincodeInput(
                                                        pincodeTextController
                                                            .text,
                                                      ),
                                                  readOnly: true,
                                                  // readOnly: !_isChangeAddress,
                                                  textInputType:
                                                      TextInputType.number,
                                                  maxLength: 6,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Pincode',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        icHashIcon,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),
                                          Row(
                                            children: [
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  errorText:
                                                      _isSubmitted
                                                          ? _validateMobileInput(
                                                            regMobileTextController
                                                                .text,
                                                          )
                                                          : null,
                                                  onChange: (p0) {
                                                    setState(() {});
                                                  },
                                                  controller:
                                                      regMobileTextController,
                                                  readOnly: true,
                                                  // readOnly: !_isChangeAddress,
                                                  textInputType:
                                                      TextInputType.name,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Reg. Mobile No.',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMobile,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              SizedBox(
                                                width: responsiveHeight(10),
                                              ),
                                              Expanded(
                                                flex: 1,
                                                child: AppTextField(
                                                  controller:
                                                      alternateMobileTextController,

                                                  // validators:
                                                  //     _validateMobileInput,
                                                  maxLength: 10,
                                                  readOnly: !_isChangeAddress,
                                                  textInputType:
                                                      TextInputType.number,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Alternate Mobile',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMobile,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),

                                          Row(
                                            children: [
                                              Visibility(
                                                visible:
                                                    _isTextFieldVisibilityDateTime,
                                                child: Expanded(
                                                  child: Transform.scale(
                                                    alignment:
                                                        FractionalOffset
                                                            .centerLeft,
                                                    scale: 1,
                                                    child: Row(
                                                      children: [
                                                        Checkbox(
                                                          materialTapTargetSize:
                                                              MaterialTapTargetSize
                                                                  .shrinkWrap,
                                                          visualDensity:
                                                              const VisualDensity(
                                                                horizontal: -4,
                                                                vertical: -4,
                                                              ),
                                                          value:
                                                              _isChangeAddress,
                                                          onChanged:
                                                              _toggleChangeAddress,
                                                        ),
                                                        Expanded(
                                                          child: GestureDetector(
                                                            onTap: () {
                                                              _toggleChangeAddress(
                                                                !_isChangeAddress,
                                                              );
                                                            },
                                                            child: Text(
                                                              "Click Here To Change Registered Address",
                                                              style: TextStyle(
                                                                color:
                                                                    kBlackColor,
                                                                fontSize:
                                                                    responsiveHeight(
                                                                      12,
                                                                    ),
                                                                fontFamily:
                                                                    FontConstants
                                                                        .interFonts,
                                                              ),
                                                            ),
                                                          ),
                                                        ),
                                                      ],
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ).paddingOnly(top: 20),
                                        ],
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),

                            SizedBox(height: responsiveHeight(10)),

                            Container(
                              decoration: BoxDecoration(
                                boxShadow: const [
                                  BoxShadow(
                                    offset: Offset(0, 1),
                                    color: Color(0xff00000026),
                                    spreadRadius: 0,
                                    blurRadius: 10,
                                  ),
                                ],
                                color: Colors.white,
                                border: Border.all(
                                  color: Color(0xFFD1D1D1),
                                  width: 1,
                                ),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Column(
                                children: [
                                  Material(
                                    color: Colors.transparent,
                                    child: InkWell(
                                      onTap: () {
                                        setState(() {
                                          _screenedVisibility =
                                              !_screenedVisibility;
                                        });
                                      },
                                      child: Ink(
                                        decoration: BoxDecoration(
                                          color: const Color(0XFF423897),
                                          borderRadius: BorderRadius.circular(
                                            10,
                                          ),
                                        ),
                                        child: Padding(
                                          padding: EdgeInsets.all(
                                            responsiveHeight(10),
                                          ),
                                          child: Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              Text(
                                                "Screened Beneficiary",
                                                style: TextStyle(
                                                  color: Colors.white,
                                                  fontSize: responsiveFont(14),
                                                  fontWeight: FontWeight.w400,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                ),
                                              ),
                                              Image.asset(
                                                _screenedVisibility
                                                    ? upArrow
                                                    : downArrow,
                                                color: Colors.white,
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),

                                  /// Collapsible content
                                  Visibility(
                                    visible: _screenedVisibility,
                                    child: Padding(
                                      padding: EdgeInsets.symmetric(
                                        vertical: responsiveHeight(10),
                                        horizontal: 20.w,
                                      ),
                                      child:
                                          _screeningdataList.isEmpty
                                              ? Center(
                                                child: Column(
                                                  children: [
                                                    // SizedBox(height: 10),
                                                    Text(
                                                      "No data available",
                                                      style: TextStyle(
                                                        fontSize:
                                                            responsiveFont(14),
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        color: Colors.grey,
                                                      ),
                                                    ),
                                                  ],
                                                ),
                                              )
                                              : ListView.builder(
                                                shrinkWrap: true,
                                                physics:
                                                    NeverScrollableScrollPhysics(),
                                                itemCount:
                                                    _screeningdataList.length,
                                                itemBuilder: (context, index) {
                                                  final item =
                                                      _screeningdataList[index];
                                                  return Padding(
                                                    padding:
                                                        const EdgeInsets.all(
                                                          6.0,
                                                        ),
                                                    child: Container(
                                                      decoration: BoxDecoration(
                                                        boxShadow: const [
                                                          BoxShadow(
                                                            offset: Offset(
                                                              0,
                                                              1,
                                                            ),
                                                            color: Color(
                                                              0xff00000026,
                                                            ),
                                                            spreadRadius: 0,
                                                            blurRadius: 10,
                                                          ),
                                                        ],
                                                        color: const Color(
                                                          0XFFF8F8FF,
                                                        ),
                                                        borderRadius:
                                                            BorderRadius.circular(
                                                              10,
                                                            ),
                                                        border: Border.all(
                                                          color: const Color(
                                                            0XFFCFCBEC,
                                                          ),
                                                        ),
                                                      ),
                                                      child: Padding(
                                                        padding: EdgeInsets.all(
                                                          responsiveHeight(10),
                                                        ),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            /// Name row (optional if you have name)
                                                            Row(
                                                              children: [
                                                                Image.asset(
                                                                  iconPersons,
                                                                  scale: 3.5,
                                                                ),
                                                                const SizedBox(
                                                                  width: 8,
                                                                ),
                                                                Text.rich(
                                                                  TextSpan(
                                                                    text:
                                                                        'Name: ',
                                                                    style: TextStyle(
                                                                      fontSize:
                                                                          responsiveFont(
                                                                            14,
                                                                          ),
                                                                      fontWeight:
                                                                          FontWeight
                                                                              .w500,
                                                                      fontFamily:
                                                                          FontConstants
                                                                              .interFonts,
                                                                    ),
                                                                    children: [
                                                                      TextSpan(
                                                                        text:
                                                                            item.firstName ??
                                                                            "N/A",
                                                                        style: TextStyle(
                                                                          fontSize: responsiveFont(
                                                                            14,
                                                                          ),
                                                                          fontWeight:
                                                                              FontWeight.w400,
                                                                          color: Color(
                                                                            0xFF666666,
                                                                          ),
                                                                          fontFamily:
                                                                              FontConstants.interFonts,
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                ),
                                                              ],
                                                            ),
                                                            SizedBox(
                                                              height:
                                                                  responsiveHeight(
                                                                    10,
                                                                  ),
                                                            ),

                                                            /// Relation
                                                            Row(
                                                              children: [
                                                                Image.asset(
                                                                  iconPersons,
                                                                  scale: 3.5,
                                                                ),
                                                                const SizedBox(
                                                                  width: 8,
                                                                ),
                                                                Text.rich(
                                                                  TextSpan(
                                                                    text:
                                                                        'Relation: ',
                                                                    style: TextStyle(
                                                                      fontSize:
                                                                          responsiveFont(
                                                                            14,
                                                                          ),
                                                                      fontWeight:
                                                                          FontWeight
                                                                              .w500,
                                                                      fontFamily:
                                                                          FontConstants
                                                                              .interFonts,
                                                                    ),
                                                                    children: [
                                                                      TextSpan(
                                                                        text:
                                                                            item.relation ??
                                                                            "N/A",
                                                                        style: TextStyle(
                                                                          fontSize: responsiveFont(
                                                                            14,
                                                                          ),
                                                                          fontWeight:
                                                                              FontWeight.w400,
                                                                          color: Color(
                                                                            0xFF666666,
                                                                          ),
                                                                          fontFamily:
                                                                              FontConstants.interFonts,
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                ),
                                                              ],
                                                            ),
                                                            SizedBox(
                                                              height:
                                                                  responsiveHeight(
                                                                    10,
                                                                  ),
                                                            ),

                                                            Row(
                                                              children: [
                                                                Image.asset(
                                                                  iconPersons,
                                                                  scale: 3.5,
                                                                ),
                                                                const SizedBox(
                                                                  width: 8,
                                                                ),
                                                                Text.rich(
                                                                  TextSpan(
                                                                    text:
                                                                        'Last Screening Date: ',
                                                                    style: TextStyle(
                                                                      fontSize:
                                                                          responsiveFont(
                                                                            14,
                                                                          ),
                                                                      fontWeight:
                                                                          FontWeight
                                                                              .w500,
                                                                      fontFamily:
                                                                          FontConstants
                                                                              .interFonts,
                                                                    ),
                                                                    children: [
                                                                      TextSpan(
                                                                        text:
                                                                            item.screeningDate ??
                                                                            "N/A",
                                                                        style: TextStyle(
                                                                          fontSize: responsiveFont(
                                                                            14,
                                                                          ),
                                                                          fontWeight:
                                                                              FontWeight.w400,
                                                                          color: Color(
                                                                            0xFF666666,
                                                                          ),
                                                                          fontFamily:
                                                                              FontConstants.interFonts,
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                ),
                                                              ],
                                                            ),

                                                            SizedBox(
                                                              height:
                                                                  responsiveHeight(
                                                                    10,
                                                                  ),
                                                            ),

                                                            /// Age
                                                            Row(
                                                              children: [
                                                                Icon(
                                                                  Icons.cake,
                                                                  size:
                                                                      responsiveFont(
                                                                        20,
                                                                      ),
                                                                  color: Color(
                                                                    0xFF423897,
                                                                  ),
                                                                ),
                                                                const SizedBox(
                                                                  width: 8,
                                                                ),
                                                                Text.rich(
                                                                  TextSpan(
                                                                    text:
                                                                        'Age: ',
                                                                    style: TextStyle(
                                                                      fontSize:
                                                                          responsiveFont(
                                                                            14,
                                                                          ),
                                                                      fontWeight:
                                                                          FontWeight
                                                                              .w500,
                                                                      fontFamily:
                                                                          FontConstants
                                                                              .interFonts,
                                                                    ),
                                                                    children: [
                                                                      TextSpan(
                                                                        text:
                                                                            item.age?.toString() ??
                                                                            "N/A",
                                                                        style: TextStyle(
                                                                          fontSize: responsiveFont(
                                                                            14,
                                                                          ),
                                                                          fontWeight:
                                                                              FontWeight.w400,
                                                                          color: Color(
                                                                            0xFF666666,
                                                                          ),
                                                                          fontFamily:
                                                                              FontConstants.interFonts,
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                ),
                                                              ],
                                                            ),
                                                          ],
                                                        ),
                                                      ),
                                                    ),
                                                  );
                                                },
                                              ),
                                    ),
                                  ),
                                ],
                              ),
                            ),

                            SizedBox(height: responsiveHeight(10)),

                            Container(
                              decoration: BoxDecoration(
                                boxShadow: const [
                                  BoxShadow(
                                    offset: Offset(0, 1),
                                    color: Color(0xff00000026),
                                    spreadRadius: 0,
                                    blurRadius: 10,
                                  ),
                                ],
                                color: const Color(0XFFFFFFFF),
                                border: Border.all(
                                  color: const Color(0xFFD1D1D1),
                                  width: 1,
                                ),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Column(
                                children: [
                                  Material(
                                    color: Colors.transparent,
                                    child: InkWell(
                                      onTap: () {
                                        setState(() {
                                          _personalVisibility =
                                              !_personalVisibility;
                                        });
                                      },
                                      child: Ink(
                                        decoration: BoxDecoration(
                                          color: const Color(0XFF423897),
                                          borderRadius: BorderRadius.circular(
                                            10,
                                          ),
                                        ),
                                        child: Padding(
                                          padding: EdgeInsets.all(
                                            responsiveHeight(10),
                                          ),
                                          child: Column(
                                            children: [
                                              Row(
                                                mainAxisAlignment:
                                                    MainAxisAlignment
                                                        .spaceBetween,
                                                children: [
                                                  Text(
                                                    "Personal & Dependenet info*",
                                                    style: TextStyle(
                                                      color: const Color(
                                                        0XFFFFFFFF,
                                                      ),
                                                      fontSize: responsiveFont(
                                                        14,
                                                      ),
                                                      fontWeight:
                                                          FontWeight.w400,
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                  ),
                                                  _personalVisibility
                                                      ? Image.asset(
                                                        upArrow,
                                                        color: Colors.white,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      )
                                                      : Image.asset(
                                                        downArrow,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        color: Colors.white,
                                                      ),
                                                ],
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                  Visibility(
                                    visible: _personalVisibility,
                                    child: Padding(
                                      padding: EdgeInsets.symmetric(
                                        vertical: 20.h,
                                        horizontal: 10.w,
                                      ),
                                      child: Column(
                                        children: [
                                          Row(
                                            children: [
                                              Expanded(
                                                child: AppTextField(
                                                  onTap: () {
                                                    if (callStatusTextController
                                                        .text
                                                        .isEmpty) {
                                                      ToastManager.showAlertDialog(
                                                        context,
                                                        "Please select call status first",
                                                        () {
                                                          Navigator.pop(
                                                            context,
                                                          );
                                                        },
                                                      );

                                                      return;
                                                    }

                                                    showModalBottomSheet(
                                                      context: context,
                                                      isScrollControlled: true,
                                                      builder: (context) {
                                                        return StatefulBuilder(
                                                          builder: (
                                                            c,
                                                            sheetState,
                                                          ) {
                                                            return SelectionBottomSheet<
                                                              Map<
                                                                String,
                                                                dynamic
                                                              >,
                                                              int
                                                            >(
                                                              title:
                                                                  "Worker Gender",
                                                              items:
                                                                  _genderList,
                                                              selectedValue:
                                                                  selectedGender?["id"],
                                                              valueFor:
                                                                  (item) =>
                                                                      item["id"] ??
                                                                      -1,
                                                              labelFor:
                                                                  (item) =>
                                                                      item["name"] ??
                                                                      'NA',
                                                              height: 260.h,
                                                              padding: EdgeInsets.only(
                                                                top:
                                                                    responsiveHeight(
                                                                      28,
                                                                    ),
                                                                left:
                                                                    responsiveHeight(
                                                                      35,
                                                                    ),
                                                                right:
                                                                    responsiveHeight(
                                                                      35,
                                                                    ),
                                                                bottom:
                                                                    responsiveHeight(
                                                                      60,
                                                                    ),
                                                              ),
                                                              titleTextStyle: TextStyle(
                                                                fontSize:
                                                                    responsiveFont(
                                                                      16,
                                                                    ),
                                                                fontFamily:
                                                                    FontConstants
                                                                        .interFonts,
                                                              ),
                                                              titleBottomSpacing:
                                                                  responsiveHeight(
                                                                    30,
                                                                  ),
                                                              itemContainerPadding:
                                                                  const EdgeInsets.symmetric(
                                                                    vertical:
                                                                        8.0,
                                                                    horizontal:
                                                                        4.0,
                                                                  ),
                                                              itemTextStyle: TextStyle(
                                                                fontFamily:
                                                                    FontConstants
                                                                        .interFonts,
                                                              ),
                                                              selectedItemTextStyle:
                                                                  TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                  ),
                                                              selectedBackgroundColor:
                                                                  kPrimaryColor
                                                                      .withOpacity(
                                                                        0.1,
                                                                      ),
                                                              onItemTap: (
                                                                item,
                                                              ) {
                                                                // STEP 1: Update the visual state FIRST using sheetState
                                                                sheetState(() {
                                                                  selectedGender =
                                                                      item;
                                                                });

                                                                // STEP 2: Update text controller and state
                                                                workersGenderTextController
                                                                        .text =
                                                                    selectedGender?["name"] ??
                                                                    "";

                                                                workerGender =
                                                                    selectedGender?["name"] ??
                                                                    "";

                                                                // STEP 3: Clear dependent fields
                                                                workersMartialStatusTextController
                                                                    .text = "";
                                                                noOfDependentTextController
                                                                    .text = "";
                                                                workersMartialStatusTextController
                                                                    .clear();
                                                                selectedMaritalStatus =
                                                                    null;
                                                                dependentScreeningPendingTextController
                                                                    .text = "";
                                                                selectedNumberOfDependent =
                                                                    null;

                                                                // STEP 4: Close sheet and trigger bloc event
                                                                Navigator.pop(
                                                                  context,
                                                                );
                                                                context
                                                                    .read<
                                                                      ExpectedBeneficiaryBloc
                                                                    >()
                                                                    .add(
                                                                      ResetExpectedBeneficiaryState(),
                                                                    );
                                                              },
                                                            );
                                                          },
                                                        );
                                                      },
                                                    );
                                                  },
                                                  controller:
                                                      workersGenderTextController,
                                                  readOnly: true,
                                                  textInputType:
                                                      TextInputType.name,
                                                  inputStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      14,
                                                    ),
                                                    color: Colors.black,
                                                  ),
                                                  label: RichText(
                                                    text: TextSpan(
                                                      text: 'Workers Gender',
                                                      style: TextStyle(
                                                        color: kLabelTextColor,
                                                        fontSize:
                                                            responsiveFont(14) *
                                                            1.33,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontWeight:
                                                            FontWeight.w600,
                                                      ),
                                                      children: <TextSpan>[
                                                        TextSpan(
                                                          text: ' *',
                                                          style: TextStyle(
                                                            color: Colors.red,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ),
                                                          ),
                                                        ),
                                                      ],
                                                    ),
                                                  ),
                                                  labelStyle: TextStyle(
                                                    fontSize: responsiveFont(
                                                      14,
                                                    ),
                                                  ),
                                                  prefixIcon: SizedBox(
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                    child: Center(
                                                      child: Image.asset(
                                                        iconMars,
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                  suffixIcon: Image.asset(
                                                    downArrow,
                                                    scale: 3.5,
                                                  ),
                                                ),
                                              ),
                                              SizedBox(width: 8.w),
                                              Expanded(
                                                child: Visibility(
                                                  visible:
                                                      _isTextFieldVisibilityPersonalDet,
                                                  child: AppTextField(
                                                    onTap: () {
                                                      if (callStatusTextController
                                                          .text
                                                          .isEmpty) {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select call status first",
                                                          () {
                                                            Navigator.pop(
                                                              context,
                                                            );
                                                          },
                                                        );

                                                        // showDialog(
                                                        //   context: context,
                                                        //   builder: (
                                                        //     BuildContext context,
                                                        //   ) {
                                                        //     return AlertDialog(
                                                        //       title: Text(
                                                        //         "Alert",
                                                        //         style: TextStyle(
                                                        //           fontFamily:
                                                        //               FontConstants
                                                        //                   .interFonts,
                                                        //         ),
                                                        //       ),
                                                        //       content: Text(
                                                        //         "Please select call status first",
                                                        //         style: TextStyle(
                                                        //           fontFamily:
                                                        //               FontConstants
                                                        //                   .interFonts,
                                                        //         ),
                                                        //       ),
                                                        //       actions: [
                                                        //         SizedBox(
                                                        //           width: 80.w,
                                                        //           child:
                                                        //               AppActiveButton(
                                                        //                 buttontitle:
                                                        //                     "OK",
                                                        //                 onTap: () {
                                                        //                   Navigator.of(
                                                        //                     context,
                                                        //                   ).pop();
                                                        //                 },
                                                        //               ),
                                                        //         ),
                                                        //       ],
                                                        //     );
                                                        //   },
                                                        // );
                                                        return;
                                                      }

                                                      showModalBottomSheet(
                                                        context: context,
                                                        isScrollControlled:
                                                            true,
                                                        builder: (context) {
                                                          return StatefulBuilder(
                                                            builder: (
                                                              c,
                                                              sheetState,
                                                            ) {
                                                              return SelectionBottomSheet<
                                                                Map<
                                                                  String,
                                                                  dynamic
                                                                >,
                                                                int
                                                              >(
                                                                title:
                                                                    "Worker Marital Status",
                                                                items:
                                                                    _maritalStatusList,
                                                                selectedValue:
                                                                    selectedMaritalStatus?["id"],
                                                                valueFor:
                                                                    (item) =>
                                                                        item["id"] ??
                                                                        -1,
                                                                labelFor:
                                                                    (item) =>
                                                                        item["name"] ??
                                                                        'NA',
                                                                height: 300.h,
                                                                padding: EdgeInsets.only(
                                                                  top:
                                                                      responsiveHeight(
                                                                        28,
                                                                      ),
                                                                  left:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  right:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  bottom:
                                                                      responsiveHeight(
                                                                        60,
                                                                      ),
                                                                ),
                                                                titleTextStyle: TextStyle(
                                                                  fontSize:
                                                                      responsiveFont(
                                                                        16,
                                                                      ),
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                ),
                                                                titleBottomSpacing:
                                                                    responsiveHeight(
                                                                      30,
                                                                    ),
                                                                itemContainerPadding:
                                                                    const EdgeInsets.symmetric(
                                                                      vertical:
                                                                          8.0,
                                                                      horizontal:
                                                                          4.0,
                                                                    ),
                                                                selectedBackgroundColor:
                                                                    kPrimaryColor
                                                                        .withOpacity(
                                                                          0.1,
                                                                        ),
                                                                itemTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                  fontWeight:
                                                                      FontWeight
                                                                          .normal,
                                                                  color:
                                                                      kBlackColor,
                                                                ),
                                                                selectedItemTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                  fontWeight:
                                                                      FontWeight
                                                                          .normal,
                                                                  color:
                                                                      kBlackColor,
                                                                ),
                                                                onItemTap: (
                                                                  item,
                                                                ) {
                                                                  // STEP 1: Update the visual state FIRST using sheetState
                                                                  sheetState(() {
                                                                    selectedMaritalStatus =
                                                                        item;
                                                                  });

                                                                  // STEP 2: Update text controllers and state
                                                                  workersMartialStatusTextController
                                                                          .text =
                                                                      selectedMaritalStatus?["name"] ??
                                                                      "";

                                                                  marriedStatusID =
                                                                      selectedMaritalStatus?["id"]
                                                                          ?.toString() ??
                                                                      "";

                                                                  // STEP 3: Clear dependent fields
                                                                  noOfDependentTextController
                                                                      .text = "";
                                                                  dependentScreeningPendingTextController
                                                                      .text = "";
                                                                  numberOfDependent =
                                                                      0;
                                                                  numberOfDependentPending =
                                                                      0;

                                                                  // STEP 4: Close sheet and trigger bloc event
                                                                  Navigator.pop(
                                                                    context,
                                                                  );
                                                                  context
                                                                      .read<
                                                                        ExpectedBeneficiaryBloc
                                                                      >()
                                                                      .add(
                                                                        ResetExpectedBeneficiaryState(),
                                                                      );
                                                                },
                                                              );
                                                            },
                                                          );
                                                        },
                                                      );
                                                    },
                                                    controller:
                                                        workersMartialStatusTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text:
                                                            'Workers Marital Status',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: ' *',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    14,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          icUserIcon,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: Image.asset(
                                                      downArrow,
                                                      scale: 3.5,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),

                                          SizedBox(
                                            height: responsiveHeight(30),
                                          ),
                                          Row(
                                            children: [
                                              Expanded(
                                                child: Visibility(
                                                  visible:
                                                      _isTextFieldVisibilityPersonalDet,
                                                  child: AppTextField(
                                                    onTap: () {
                                                      if (callStatusTextController
                                                          .text
                                                          .isEmpty) {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select call status first",
                                                          () {
                                                            Navigator.pop(
                                                              context,
                                                            );
                                                          },
                                                        );

                                                        // showDialog(
                                                        //   context: context,
                                                        //   builder: (
                                                        //     BuildContext context,
                                                        //   ) {
                                                        //     return AlertDialog(
                                                        //       title: const Text(
                                                        //         "Alert",
                                                        //       ),
                                                        //       content: const Text(
                                                        //         "Please select call status first",
                                                        //       ),
                                                        //       actions: [
                                                        //         SizedBox(
                                                        //           width: 80.w,
                                                        //           child:
                                                        //               AppActiveButton(
                                                        //                 buttontitle:
                                                        //                     "OK",
                                                        //                 onTap: () {
                                                        //                   Navigator.of(
                                                        //                     context,
                                                        //                   ).pop();
                                                        //                 },
                                                        //               ),
                                                        //         ),
                                                        //         // TextButton(
                                                        //         //   onPressed: () {
                                                        //         //     Navigator.of(
                                                        //         //       context,
                                                        //         //     ).pop(); // Close the dialog
                                                        //         //   },
                                                        //         //   child: const Text(
                                                        //         //     "OK",
                                                        //         //   ),
                                                        //         // ),
                                                        //       ],
                                                        //     );
                                                        //   },
                                                        // );

                                                        return;
                                                      }

                                                      if (workersMartialStatusTextController
                                                              .text ==
                                                          "") {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select maritial Status",
                                                          () {
                                                            Navigator.pop(
                                                              context,
                                                            );
                                                          },
                                                        );

                                                        // showDialog(
                                                        //   context: context,
                                                        //   builder: (
                                                        //     BuildContext context,
                                                        //   ) {
                                                        //     return AlertDialog(
                                                        //       title: const Text(
                                                        //         "Alert",
                                                        //       ),
                                                        //       content: const Text(
                                                        //         "Please select maritial Status",
                                                        //       ),
                                                        //       actions: [
                                                        //         SizedBox(
                                                        //           width: 80.w,
                                                        //           child:
                                                        //               AppActiveButton(
                                                        //                 buttontitle:
                                                        //                     "OK",
                                                        //                 onTap: () {
                                                        //                   Navigator.of(
                                                        //                     context,
                                                        //                   ).pop();
                                                        //                 },
                                                        //               ),
                                                        //         ),
                                                        //
                                                        //         // TextButton(
                                                        //         //   onPressed: () {
                                                        //         //     Navigator.of(
                                                        //         //       context,
                                                        //         //     ).pop(); // Close the dialog
                                                        //         //   },
                                                        //         //   child: const Text(
                                                        //         //     "OK",
                                                        //         //   ),
                                                        //         // ),
                                                        //       ],
                                                        //     );
                                                        //   },
                                                        // );

                                                        return;
                                                      }

                                                      showModalBottomSheet(
                                                        context: context,
                                                        isScrollControlled:
                                                            true,
                                                        builder: (context) {
                                                          return StatefulBuilder(
                                                            builder: (
                                                              c,
                                                              sheetState,
                                                            ) {
                                                              return SelectionBottomSheet<
                                                                Map<
                                                                  String,
                                                                  dynamic
                                                                >,
                                                                int
                                                              >(
                                                                title:
                                                                    "No. of Dependent",
                                                                items:
                                                                    filteredList,
                                                                selectedValue:
                                                                    selectedNumberOfDependent?["id"],
                                                                valueFor:
                                                                    (item) =>
                                                                        item["id"] ??
                                                                        -1,
                                                                labelFor:
                                                                    (item) =>
                                                                        item["name"] ??
                                                                        'NA',
                                                                height: 320.h,
                                                                padding: EdgeInsets.only(
                                                                  top:
                                                                      responsiveHeight(
                                                                        16,
                                                                      ),
                                                                  left:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  right:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  bottom:
                                                                      responsiveHeight(
                                                                        60,
                                                                      ),
                                                                ),
                                                                titleTextStyle:
                                                                    TextStyle(
                                                                      fontSize:
                                                                          responsiveFont(
                                                                            16,
                                                                          ),
                                                                    ),
                                                                titleBottomSpacing:
                                                                    responsiveHeight(
                                                                      30,
                                                                    ),
                                                                itemPadding:
                                                                    EdgeInsets.symmetric(
                                                                      vertical:
                                                                          4.h,
                                                                    ),
                                                                itemBuilderWithIndex: (
                                                                  context,
                                                                  item,
                                                                  isSelected,
                                                                  index,
                                                                  itemCount,
                                                                ) {
                                                                  return Padding(
                                                                    padding:
                                                                        const EdgeInsets.only(
                                                                          left:
                                                                              12,
                                                                        ),
                                                                    child: Container(
                                                                      padding: const EdgeInsets.only(
                                                                        left:
                                                                            4.0,
                                                                        right:
                                                                            4.0,
                                                                      ),
                                                                      margin:
                                                                          index ==
                                                                                  itemCount -
                                                                                      1
                                                                              ? EdgeInsets.only(
                                                                                bottom: responsiveHeight(
                                                                                  20,
                                                                                ),
                                                                              )
                                                                              : EdgeInsets.zero,
                                                                      decoration: BoxDecoration(
                                                                        color:
                                                                            isSelected
                                                                                ? kPrimaryColor.withOpacity(
                                                                                  0.1,
                                                                                )
                                                                                : Colors.transparent,
                                                                        borderRadius:
                                                                            BorderRadius.circular(
                                                                              8,
                                                                            ),
                                                                      ),
                                                                      child: Row(
                                                                        children: [
                                                                          IgnorePointer(
                                                                            child: Transform.scale(
                                                                              scale:
                                                                                  0.75,
                                                                              child: Radio<
                                                                                int
                                                                              >(
                                                                                // CRITICAL FIX: Use int instead of Map
                                                                                value:
                                                                                    item["id"] ??
                                                                                    0,
                                                                                groupValue:
                                                                                    selectedNumberOfDependent?["id"] ??
                                                                                    -1,
                                                                                onChanged:
                                                                                    (
                                                                                      value,
                                                                                    ) {},
                                                                                // Required but never called
                                                                                materialTapTargetSize:
                                                                                    MaterialTapTargetSize.shrinkWrap,
                                                                                visualDensity: const VisualDensity(
                                                                                  horizontal:
                                                                                      -4,
                                                                                  vertical:
                                                                                      -4,
                                                                                ),
                                                                              ),
                                                                            ),
                                                                          ),
                                                                          Expanded(
                                                                            child: Text(
                                                                              item["name"] ??
                                                                                  'NA',
                                                                              style: TextStyle(
                                                                                fontFamily:
                                                                                    FontConstants.interFonts,
                                                                                fontWeight:
                                                                                    FontWeight.normal,
                                                                                color:
                                                                                    kBlackColor,
                                                                              ),
                                                                            ),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                    ),
                                                                  );
                                                                },
                                                                onItemTap: (
                                                                  item,
                                                                ) {
                                                                  // STEP 1: Update the visual state FIRST using sheetState
                                                                  sheetState(() {
                                                                    selectedNumberOfDependent =
                                                                        item;
                                                                  });

                                                                  noOfDependentTextController
                                                                          .text =
                                                                      selectedNumberOfDependent?["name"] ??
                                                                      "";

                                                                  numberOfDependent =
                                                                      selectedNumberOfDependent?["id"];

                                                                  final noOfDepartmentStr =
                                                                      numberOfDependent;

                                                                  int
                                                                  screenedCount =
                                                                  0;
                                                                  int
                                                                  departmentCount =
                                                                  0;

                                                                  try {
                                                                    screenedCount =
                                                                        screenedBeneficiaryCount;
                                                                    departmentCount =
                                                                        noOfDepartmentStr;
                                                                  } catch (
                                                                  e
                                                                  ) {
                                                                    debugPrint(
                                                                      "Invalid input: $e",
                                                                    );
                                                                  }

                                                                  differenceCount =
                                                                      (screenedCount -
                                                                          departmentCount)
                                                                          .abs();

                                                                  // Validation logic
                                                                  if (screenedBeneficiaryCount !=
                                                                      0) {


                                                                    if (departmentCount <
                                                                        screenedCount) {
                                                                      ToastManager.showAlertDialog(
                                                                        context,
                                                                        "You have added number of dependents greater than $screenedCount screened dependents.",
                                                                        () {
                                                                          Navigator.pop(
                                                                            context,
                                                                          );

                                                                          // Clear selection on validation failure
                                                                          sheetState(() {
                                                                            selectedNumberOfDependent =
                                                                                null;
                                                                          });
                                                                          noOfDependentTextController.text =
                                                                              "";
                                                                          numberOfDependent =
                                                                              0;
                                                                        },
                                                                      );


                                                                      return;
                                                                    }
                                                                  }

                                                                  // If check passes, clear dependent fields and close
                                                                  dependentScreeningPendingTextController
                                                                      .text = "";
                                                                  selectedDependentScreeningPending =
                                                                      null;

                                                                  Navigator.pop(
                                                                    context,
                                                                  );
                                                                  context
                                                                      .read<
                                                                        ExpectedBeneficiaryBloc
                                                                      >()
                                                                      .add(
                                                                        ResetExpectedBeneficiaryState(),
                                                                      );
                                                                },
                                                              );
                                                            },
                                                          );
                                                        },
                                                      );
                                                    },
                                                    controller:
                                                        noOfDependentTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text:
                                                            'No. of Dependent',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: ' *',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    14,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconPersons,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: Image.asset(
                                                      downArrow,
                                                      scale: 3.5,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              SizedBox(width: 8.w),

                                              Expanded(
                                                child: Visibility(
                                                  visible:
                                                      _isTextFieldVisibilityPersonalDet,
                                                  child: AppTextField(
                                                    onTap: () {
                                                      if (callStatusTextController
                                                          .text
                                                          .isEmpty) {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select  call status first",
                                                          () {
                                                            Navigator.of(
                                                              context,
                                                            ).pop();
                                                          },
                                                        );

                                                        return;
                                                      }
                                                      if (noOfDependentTextController
                                                              .text ==
                                                          "") {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select No. Of dependent first",
                                                          () {
                                                            Navigator.of(
                                                              context,
                                                            ).pop();
                                                          },
                                                        );

                                                        return;
                                                      }

                                                      showModalBottomSheet(
                                                        context: context,
                                                        isScrollControlled:
                                                            true,
                                                        builder: (context) {
                                                          return StatefulBuilder(
                                                            builder: (
                                                              c,
                                                              sheetState,
                                                            ) {
                                                              return SelectionBottomSheet<
                                                                Map<
                                                                  String,
                                                                  dynamic
                                                                >,
                                                                int
                                                              >(
                                                                title:
                                                                    "Dependent Screening Pending",
                                                                items:
                                                                    dependentList,
                                                                selectedValue:
                                                                    selectedDependentScreeningPending?["id"],
                                                                valueFor:
                                                                    (item) =>
                                                                        item["id"] ??
                                                                        -1,
                                                                labelFor:
                                                                    (item) =>
                                                                        item["name"] ??
                                                                        'NA',
                                                                height: 360.h,
                                                                padding: EdgeInsets.only(
                                                                  top:
                                                                      responsiveHeight(
                                                                        20,
                                                                      ),
                                                                  left:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  right:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  bottom:
                                                                      responsiveHeight(
                                                                        60,
                                                                      ),
                                                                ),
                                                                titleTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                  fontSize:
                                                                      responsiveFont(
                                                                        16,
                                                                      ),
                                                                ),
                                                                titleBottomSpacing:
                                                                    responsiveHeight(
                                                                      30,
                                                                    ),
                                                                itemPadding:
                                                                    EdgeInsets.symmetric(
                                                                      vertical:
                                                                          4.h,
                                                                    ),
                                                                itemContainerPadding:
                                                                    const EdgeInsets.symmetric(
                                                                      horizontal:
                                                                          4.0,
                                                                    ),
                                                                selectedBackgroundColor:
                                                                    kPrimaryColor
                                                                        .withOpacity(
                                                                          0.1,
                                                                        ),
                                                                itemTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                  fontWeight:
                                                                      FontWeight
                                                                          .normal,
                                                                  color:
                                                                      kBlackColor,
                                                                ),
                                                                selectedItemTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                  fontWeight:
                                                                      FontWeight
                                                                          .normal,
                                                                  color:
                                                                      kBlackColor,
                                                                ),
                                                                onItemTap: (
                                                                  item,
                                                                ) {
                                                                  // STEP 1: Update the visual state FIRST using sheetState
                                                                  sheetState(() {
                                                                    selectedDependentScreeningPending =
                                                                        item;
                                                                  });

                                                                  // STEP 2: Update the text field
                                                                  dependentScreeningPendingTextController
                                                                          .text =
                                                                      item["name"] ??
                                                                      "";
                                                                  numberOfDependentPending =
                                                                      item["id"] ??
                                                                      "";

                                                                  // STEP 3: Validation logic
                                                                  if (screenedBeneficiaryCount !=
                                                                      0) {
                                                                    if (numberOfDependentPending !=
                                                                        differenceCount) {
                                                                      ToastManager.showAlertDialog(
                                                                        context,
                                                                        "मागील ३६५ दिवसांत $screenedBeneficiaryCount dependent ची स्क्रिनिंग झालेली आहे, त्यामुळे त्याची गणना स्क्रिनिंग pending मध्ये करू नये.\nकृपया स्क्रिनिंगसाठी pending असलेल्या dependent ची योग्य संख्या नमूद करा.\nतसेच एकूण dependent ची संख्या बरोबर नोंदलेली आहे की नाही, याचीही खात्री करा",
                                                                        () {
                                                                          Navigator.pop(
                                                                            context,
                                                                          );
                                                                          // Clear selection on validation failure
                                                                          sheetState(() {
                                                                            selectedDependentScreeningPending =
                                                                                null;
                                                                          });
                                                                          dependentScreeningPendingTextController.text =
                                                                              "";
                                                                          numberOfDependentPending =
                                                                              0;
                                                                        },
                                                                      );

                                                                      return;
                                                                    }
                                                                  } else {
                                                                    if (numberOfDependentPending !=
                                                                        differenceCount) {
                                                                      ToastManager.showAlertDialog(
                                                                        context,
                                                                        "एकूण dependent संख्या आणि स्क्रीनिंग pending dependent संख्या यामध्ये तफावत दिसत आहे. कृपया खात्री करून स्क्रीनिंग pending असलेल्या dependent ची अचूक संख्या भरा.",
                                                                        () {
                                                                          Navigator.pop(
                                                                            context,
                                                                          );
                                                                          // Clear selection on validation failure
                                                                          sheetState(() {
                                                                            selectedDependentScreeningPending =
                                                                                null;
                                                                          });
                                                                          dependentScreeningPendingTextController.text =
                                                                              "";
                                                                          numberOfDependentPending =
                                                                              0;
                                                                        },
                                                                      );

                                                                      return;
                                                                    }
                                                                  }

                                                                  // STEP 4: Close sheet and trigger bloc event
                                                                  Navigator.pop(
                                                                    context,
                                                                  );
                                                                  context
                                                                      .read<
                                                                        ExpectedBeneficiaryBloc
                                                                      >()
                                                                      .add(
                                                                        ResetExpectedBeneficiaryState(),
                                                                      );
                                                                },
                                                              );
                                                            },
                                                          );
                                                        },
                                                      );
                                                    },
                                                    controller:
                                                        dependentScreeningPendingTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text:
                                                            'Dependent Screening Pending',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: ' *',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    12,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconPersons,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: Image.asset(
                                                      downArrow,
                                                      scale: 3.5,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),

                                          SizedBox(
                                            height: responsiveHeight(16),
                                          ),

                                          Visibility(
                                            visible:
                                                _isTextFieldVisibilityPersonalDet,
                                            child: AppButtonWithIcon(
                                              buttonColor: kPrimaryColor,
                                              title: "Add Dependent",
                                              icon: Image.asset(
                                                icSquarePlus,
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                              ),
                                              mWidth: responsiveWidth(200),
                                              mHeight: 50.h,
                                              textStyle: TextStyle(
                                                fontWeight: FontWeight.w600,
                                                color: Colors.white,
                                                fontSize: responsiveFont(14),
                                                fontFamily:
                                                    FontConstants.interFonts,
                                              ),
                                              onTap: () async {
                                                // Navigator.pushNamed(context,
                                                //     AppRoutes.addDependent,
                                                //     arguments:
                                                //         selectedBeneficiary);

                                                if (numberOfDependent == 0) {
                                                  ToastManager.showAlertDialog(
                                                    context,
                                                    "Please select number of dependent",
                                                    () {
                                                      Navigator.of(
                                                        context,
                                                      ).pop();
                                                    },
                                                  );

                                                  // showDialog(
                                                  //   context: context,
                                                  //   builder: (
                                                  //     BuildContext context,
                                                  //   ) {
                                                  //     return AlertDialog(
                                                  //       title: Text(
                                                  //         "Alert",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       content: Text(
                                                  //         "Please select number of dependent",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       actions: [
                                                  //         SizedBox(
                                                  //           width: 80.w,
                                                  //           child:
                                                  //               AppActiveButton(
                                                  //                 buttontitle:
                                                  //                     "OK",
                                                  //                 onTap: () {
                                                  //                   Navigator.of(
                                                  //                     context,
                                                  //                   ).pop();
                                                  //                 },
                                                  //               ),
                                                  //         ),
                                                  //
                                                  //
                                                  //       ],
                                                  //     );
                                                  //   },
                                                  // );

                                                  return;
                                                }
                                                if (workersMartialStatusTextController
                                                        .text ==
                                                    "") {
                                                  ToastManager.showAlertDialog(
                                                    context,
                                                    "Please select worker marital status",
                                                    () {
                                                      Navigator.of(
                                                        context,
                                                      ).pop();
                                                    },
                                                  );

                                                  // showDialog(
                                                  //   context: context,
                                                  //   builder: (
                                                  //     BuildContext context,
                                                  //   ) {
                                                  //     return AlertDialog(
                                                  //       title: Text(
                                                  //         "Alert",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       content: Text(
                                                  //         "Please select worker marital status",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       actions: [
                                                  //         SizedBox(
                                                  //           width: 80.w,
                                                  //           child:
                                                  //               AppActiveButton(
                                                  //                 buttontitle:
                                                  //                     "OK",
                                                  //                 onTap: () {
                                                  //                   Navigator.of(
                                                  //                     context,
                                                  //                   ).pop();
                                                  //                 },
                                                  //               ),
                                                  //         ),
                                                  //
                                                  //       ],
                                                  //     );
                                                  //   },
                                                  // );
                                                  return;
                                                }
                                                if (numberOfDependentPending ==
                                                    0) {
                                                  ToastManager.showAlertDialog(
                                                    context,
                                                    "Please select number of dependent screening pending",
                                                    () {
                                                      Navigator.of(
                                                        context,
                                                      ).pop();
                                                    },
                                                  );

                                                  // showDialog(
                                                  //   context: context,
                                                  //   builder: (
                                                  //     BuildContext context,
                                                  //   ) {
                                                  //     return AlertDialog(
                                                  //       title: Text(
                                                  //         "Alert",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       content: Text(
                                                  //         "Please select number of dependent screening pending",
                                                  //         style: TextStyle(
                                                  //           fontFamily:
                                                  //               FontConstants
                                                  //                   .interFonts,
                                                  //         ),
                                                  //       ),
                                                  //       actions: [
                                                  //         SizedBox(
                                                  //           width: 80.w,
                                                  //           child:
                                                  //               AppActiveButton(
                                                  //                 buttontitle:
                                                  //                     "OK",
                                                  //                 onTap: () {
                                                  //                   Navigator.of(
                                                  //                     context,
                                                  //                   ).pop();
                                                  //                 },
                                                  //               ),
                                                  //         ),
                                                  //
                                                  //       ],
                                                  //     );
                                                  //   },
                                                  // );

                                                  return;
                                                }

                                                Navigator.pushNamed(
                                                  context,
                                                  AppRoutes.addDependent,
                                                  arguments: {
                                                    "selectedBeneficiary":
                                                        selectedBeneficiary,
                                                    "workerGender":
                                                        workerGender,
                                                    "noOfDependent":
                                                        numberOfDependent,
                                                    "numberOfDependentPending":
                                                        numberOfDependentPending,

                                                    "currentCount":
                                                        state
                                                            .addDependentOutput
                                                            .length,
                                                    "assignCallId":
                                                        selectedBeneficiary
                                                            ?.assignCallID ??
                                                        0,
                                                    "marriedStatus":
                                                        marriedStatusID,
                                                    "screeningdataList":
                                                        _screeningdataList,
                                                  },
                                                );

                                                // context
                                                //     .read<
                                                //         ExpectedBeneficiaryBloc>()
                                                //     .add(
                                                //         ResetExpectedBeneficiaryState());
                                              },
                                            ),
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(10),
                                          ),
                                          state.getDependentStatus.isInProgress
                                              ? const CircularProgressIndicator()
                                              : ListView.builder(
                                                shrinkWrap: true,
                                                physics:
                                                    const NeverScrollableScrollPhysics(),
                                                itemCount:
                                                    state
                                                        .addDependentOutput
                                                        .length,
                                                itemBuilder:
                                                    (context, index) => Padding(
                                                      padding:
                                                          const EdgeInsets.all(
                                                            6.0,
                                                          ),
                                                      child: Container(
                                                        decoration: BoxDecoration(
                                                          boxShadow: const [
                                                            BoxShadow(
                                                              offset: Offset(
                                                                0,
                                                                1,
                                                              ),
                                                              color: Color(
                                                                0xff00000026,
                                                              ),
                                                              spreadRadius: 0,
                                                              blurRadius: 10,
                                                            ),
                                                          ],
                                                          color: const Color(
                                                            0XFFF8F8FF,
                                                          ),
                                                          borderRadius:
                                                              BorderRadius.circular(
                                                                10,
                                                              ),
                                                          border: Border.all(
                                                            color: const Color(
                                                              0XFFCFCBEC,
                                                            ),
                                                          ),
                                                        ),
                                                        child: Padding(
                                                          padding:
                                                              EdgeInsets.all(
                                                                responsiveHeight(
                                                                  10,
                                                                ),
                                                              ),
                                                          child: Column(
                                                            children: [
                                                              Row(
                                                                mainAxisAlignment:
                                                                    MainAxisAlignment
                                                                        .spaceBetween,
                                                                children: [
                                                                  Expanded(
                                                                    child: Text(
                                                                      '${state.addDependentOutput[index].firstName} ${state.addDependentOutput[index].middleName} ${state.addDependentOutput[index].lastName} ',
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveHeight(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w600,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                    ),
                                                                  ),
                                                                  GestureDetector(
                                                                    onTap: () {
                                                                      // final dependent =
                                                                      //     state.addDependentOutput[index];
                                                                      // final lastScreeningDateStr =
                                                                      // dependent.lastDependantScreeningDate;

                                                                      // if (lastScreeningDateStr != null &&
                                                                      //     lastScreeningDateStr.isNotEmpty) {
                                                                      //   try {
                                                                      //     final sdf = DateFormat('dd/MMM/yy', 'en_US');
                                                                      //     final screeningDate = sdf.parse(lastScreeningDateStr);
                                                                      //     final today = DateTime.now();

                                                                      //     final diffInDays = today.difference(screeningDate).inDays;

                                                                      //     if (diffInDays < 365) {
                                                                      //       // Show alert dialog
                                                                      //       showDialog(
                                                                      //         context: context,
                                                                      //         builder: (context) => AlertDialog(
                                                                      //           title: const Text('Alert'),
                                                                      //           content: const Text('You cannot delete dependent screened in last 365 days.'),
                                                                      //           actions: [
                                                                      //             TextButton(
                                                                      //               onPressed: () => Navigator.pop(context),
                                                                      //               child: const Text('OK'),
                                                                      //             ),
                                                                      //           ],
                                                                      //         ),
                                                                      //       );
                                                                      //       return;
                                                                      //     }
                                                                      //   } catch (e) {
                                                                      //     debugPrint("Parse Error: $e");
                                                                      //     // Optional: handle parse error
                                                                      //   }
                                                                      // }

                                                                      context
                                                                          .read<
                                                                            ExpectedBeneficiaryBloc
                                                                          >()
                                                                          .add(
                                                                            ResetExpectedBeneficiaryState(),
                                                                          );
                                                                      context
                                                                          .read<
                                                                            ExpectedBeneficiaryBloc
                                                                          >()
                                                                          .add(
                                                                            DeleteDependentRequest(
                                                                              dependentIndex:
                                                                                  index,
                                                                            ),
                                                                          );

                                                                      if (state
                                                                          .addDependentStatus
                                                                          .isSuccess) {}
                                                                    },
                                                                    child: Container(
                                                                      padding:
                                                                          EdgeInsets.all(
                                                                            responsiveHeight(
                                                                              8,
                                                                            ),
                                                                          ),
                                                                      // Adjust padding as needed
                                                                      decoration: BoxDecoration(
                                                                        borderRadius: BorderRadius.circular(
                                                                          responsiveHeight(
                                                                            5,
                                                                          ),
                                                                        ),
                                                                      ),
                                                                      child: Image.asset(
                                                                        iconTrash,
                                                                        scale:
                                                                            3.5,
                                                                      ),
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),
                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    iconPersons,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),

                                                                  // Space between image and text
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Relation: ',
                                                                      // First part (static text)
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text: getRelationText(
                                                                            state.addDependentOutput[index].relId,
                                                                          ),
                                                                          // Dynamic text
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            // Different size
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            // Different weight
                                                                            color: const Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts,
                                                                            // Different color (optional)
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),
                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    calendar,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),

                                                                  // Space between image and text
                                                                  // Text(
                                                                  //   'Last Screening Date : ${state.addDependentOutput[index].lastDependantScreeningDate}',
                                                                  //   style:
                                                                  //       TextStyle(
                                                                  //     fontSize:
                                                                  //         responsiveFont(14),
                                                                  //     fontWeight:
                                                                  //         FontWeight.w500,
                                                                  //   ),
                                                                  // ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Last Screening Date: ',
                                                                      // First part (static text)
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              (state.addDependentOutput[index].lastDependantScreeningDate),
                                                                          // Dynamic text
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            // Different size
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            // Different weight
                                                                            color: const Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts, // Different color (optional)
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),
                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    calendar,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),

                                                                  // Space between image and text
                                                                  // Text(
                                                                  //   'Age :${state.addDependentOutput[index].age}',
                                                                  //   style:
                                                                  //       TextStyle(
                                                                  //     fontSize:
                                                                  //         responsiveFont(14),
                                                                  //     fontWeight:
                                                                  //         FontWeight.w500,
                                                                  //   ),
                                                                  // ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Age: ',
                                                                      // First part (static text)
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              (state.addDependentOutput[index].age),
                                                                          // Dynamic text
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            // Different size
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            // Different weight
                                                                            color: const Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts, // Different color (optional)
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),
                                                            ],
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                              ),

                                          SizedBox(
                                            height: responsiveHeight(10),
                                          ),

                                          Visibility(
                                            visible:
                                                _isTextFieldVisibilityDateTime,
                                            child: Row(
                                              children: [
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    onTap: () {
                                                      if (callStatusTextController
                                                          .text
                                                          .isEmpty) {
                                                        ToastManager.showAlertDialog(
                                                          context,
                                                          "Please select call status first",
                                                          () {
                                                            Navigator.of(
                                                              context,
                                                            ).pop();
                                                          },
                                                        );

                                                        // showDialog(
                                                        //   context: context,
                                                        //   builder: (
                                                        //     BuildContext
                                                        //     context,
                                                        //   ) {
                                                        //     return AlertDialog(
                                                        //       title: Text(
                                                        //         "Alert",
                                                        //         style: TextStyle(
                                                        //           fontFamily:
                                                        //               FontConstants
                                                        //                   .interFonts,
                                                        //         ),
                                                        //       ),
                                                        //       content: Text(
                                                        //         "Please select call status first",
                                                        //         style: TextStyle(
                                                        //           fontFamily:
                                                        //               FontConstants
                                                        //                   .interFonts,
                                                        //         ),
                                                        //       ),
                                                        //       actions: [
                                                        //         SizedBox(
                                                        //           width: 80.w,
                                                        //           child: AppActiveButton(
                                                        //             buttontitle:
                                                        //                 "OK",
                                                        //             onTap: () {
                                                        //               Navigator.of(
                                                        //                 context,
                                                        //               ).pop();
                                                        //             },
                                                        //           ),
                                                        //         ),
                                                        //
                                                        //       ],
                                                        //     );
                                                        //   },
                                                        // );

                                                        return;
                                                      }

                                                      DateTime today =
                                                          DateTime.now();

                                                      showDatePicker(
                                                        context: context,
                                                        initialDate: today,
                                                        firstDate: today,
                                                        lastDate: DateTime(
                                                          2100,
                                                        ),
                                                        helpText: "Select Date",
                                                        initialEntryMode:
                                                            DatePickerEntryMode
                                                                .calendarOnly,
                                                      ).then((value) {
                                                        if (value != null) {
                                                          setState(() {
                                                            crrDate = value;
                                                            firstDayOfWeek =
                                                                crrDate.subtract(
                                                                  Duration(
                                                                    days:
                                                                        crrDate
                                                                            .weekday -
                                                                        1,
                                                                  ),
                                                                );

                                                            // Format the selected date as 'dd-MMMM-yyyy'
                                                            String
                                                            formattedDate =
                                                                DateFormat(
                                                                  "dd-MMMM-yyyy",
                                                                ).format(value);

                                                            // Set the selected date to the text controller
                                                            timeTextController
                                                                    .text =
                                                                formattedDate;

                                                            context
                                                                .read<
                                                                  ExpectedBeneficiaryBloc
                                                                >()
                                                                .add(
                                                                  // API: check appointment count/slot availability for the selected date.
                                                                  GetAppointmentCount(
                                                                    payload: {
                                                                      "AppoinmentDate":
                                                                          timeTextController
                                                                              .text,
                                                                    },
                                                                  ),
                                                                );
                                                          });
                                                        }
                                                      });
                                                    },
                                                    controller:
                                                        timeTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Date',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          calendar,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(
                                                  width: responsiveHeight(10),
                                                ),
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    onTap: () async {
                                                      _showCustomTimePicker();
                                                    },
                                                    controller:
                                                        timeAppTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Time',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: ' *',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    14,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconClock,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                          ),
                                          SizedBox(
                                            height: responsiveHeight(10),
                                          ),
                                          AppTextField(
                                            onTap: () {
                                              if (callStatusTextController
                                                  .text
                                                  .isEmpty) {
                                                ToastManager.showAlertDialog(
                                                  context,
                                                  "Please select call status first",
                                                  () {
                                                    Navigator.of(context).pop();
                                                  },
                                                );

                                                // showDialog(
                                                //   context: context,
                                                //   builder: (
                                                //     BuildContext context,
                                                //   ) {
                                                //     return AlertDialog(
                                                //       title: const Text(
                                                //         "Alert",
                                                //       ),
                                                //       content: const Text(
                                                //         "Please select  call status first",
                                                //       ),
                                                //       actions: [
                                                //         SizedBox(
                                                //           width: 80.w,
                                                //           child:
                                                //               AppActiveButton(
                                                //                 buttontitle:
                                                //                     "OK",
                                                //                 onTap: () {
                                                //                   Navigator.of(
                                                //                     context,
                                                //                   ).pop();
                                                //                 },
                                                //               ),
                                                //         ),
                                                //       ],
                                                //     );
                                                //   },
                                                // );

                                                return;
                                              }

                                              context
                                                  .read<
                                                    ExpectedBeneficiaryBloc
                                                  >()
                                                  .add(
                                                    GetRemark(
                                                      payload: {
                                                        "CallStatusID":
                                                            callStatus
                                                                .toString(),
                                                      },
                                                    ),
                                                  );
                                            },
                                            controller: remarkTextController,
                                            readOnly: true,
                                            textInputType: TextInputType.name,
                                            inputStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                              color: Colors.black,
                                            ),
                                            label: RichText(
                                              text: TextSpan(
                                                text: 'Remark',
                                                style: TextStyle(
                                                  color: kLabelTextColor,
                                                  fontSize:
                                                      responsiveFont(14) * 1.33,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w600,
                                                ),
                                                children: <TextSpan>[
                                                  TextSpan(
                                                    text: ' *',
                                                    style: TextStyle(
                                                      color: Colors.red,
                                                      fontSize: responsiveFont(
                                                        14,
                                                      ),
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ),
                                            labelStyle: TextStyle(
                                              fontSize: responsiveFont(12),
                                              fontFamily:
                                                  FontConstants.interFonts,
                                            ),
                                            prefixIcon: SizedBox(
                                              height: responsiveHeight(24),
                                              width: responsiveHeight(24),
                                              child: Center(
                                                child: Image.asset(
                                                  iconFile,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                              ),
                                            ),
                                            suffixIcon: Image.asset(
                                              downArrow,
                                              scale: 3.5,
                                            ),
                                          ),
                                          // SizedBox(
                                          //   height: responsiveHeight(29),
                                          // ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            SizedBox(height: responsiveHeight(20)),

                            AppButtonWithIcon(
                              title: "Save",
                              mHeight: responsiveHeight(50),
                              mWidth: responsiveWidth(140),
                              onTap: () {
                                setState(() {
                                  _isSubmitted = true;
                                });
                                if (callStatusTextController.text.isEmpty) {
                                  ToastManager.showAlertDialog(
                                    context,
                                    "Please select call status first",
                                    () {
                                      Navigator.of(context).pop();
                                    },
                                  );

                                  return;
                                }
                                if (callLog == 0) {
                                  ToastManager.showAlertDialog(
                                    context,
                                    "It seems you have not called to beneficiary. Please call to beneficiary first and fill in the information",
                                    () {
                                      Navigator.of(context).pop();
                                    },
                                  );

                                  return;
                                }

                                if (remarkTextController.text == "") {
                                  ToastManager.showAlertDialog(
                                    context,
                                    "Please select remark",
                                    () {
                                      Navigator.of(context).pop();
                                    },
                                  );

                                  return;
                                }

                                if (callStatus == 2) {
                                  if (!(state.addDependentOutput.length ==
                                      numberOfDependentPending)) {
                                    // if (!(state.addDependentOutput.length ==
                                    //     int.parse(
                                    //       noOfDependentTextController.text,
                                    //     ))) {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please add or remove dependent, Dependent count should be equal to number of dependent count",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (workersGenderTextController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please select worker gender",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (pincodeTextController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please enter pincode",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (_talukaTextController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please enter taluka",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }
                                  if (_districtTextController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please enter district",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }
                                  if (workersMartialStatusTextController.text ==
                                      "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please select worker marital status",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (int.tryParse(marriedStatusID) == 1 ||
                                      int.tryParse(marriedStatusID) == 2 ||
                                      int.tryParse(marriedStatusID) == 3 ||
                                      int.tryParse(marriedStatusID) == 4) {
                                    if (noOfDependentTextController.text ==
                                        "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please select number of dependent",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (dependentScreeningPendingTextController
                                            .text ==
                                        "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please select Number Of Screening Pending Dependent",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (timeTextController.text == "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please select appointment date",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (timeAppTextController.text == "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please select appointment time",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }
                                  }
                                }

                                if (!_formKey.currentState!.validate()) {
                                  return;
                                }

                                String dependentJson =
                                    (state.addDependentOutput.isNotEmpty)
                                        ? jsonEncode(
                                          state.addDependentOutput
                                              .map((e) => e.toJson())
                                              .toList(),
                                        )
                                        : '[{"AssignCallID":"0","RelId":"0","Age":"0","FirstName":" ","MiddleName":" ","LastName":" ","LastDependantScreeningDate":" "}]';

                                print(dependentJson);

                                WidgetsBinding.instance.addPostFrameCallback((
                                  timeStamp,
                                ) {
                                  context.read<ExpectedBeneficiaryBloc>().add(
                                    // API: submit the appointment confirmation details and dependent data.
                                    InsertAppointmentDetails(
                                      payload: {
                                        "AssignCallID":
                                            selectedBeneficiary?.assignCallID
                                                .toString(),
                                        "CallStatusID": callStatus.toString(),
                                        "RegisteredAddress": regAddress,
                                        "CurrentAddress": regAddress,
                                        "IsCurrentSameAsRegd":
                                            isCurrentAsSameRegId.toString(),
                                        "RegMobileNo":
                                            selectedBeneficiary?.mobile
                                                .toString(),
                                        "AltMobileNo":
                                            alternateMobileTextController.text,
                                        "Pincode": pincodeTextController.text,
                                        "Landmark": landMarkTextController.text,
                                        "WorkersGender":
                                            workersGenderTextController.text,
                                        "WorkersMaritalStatus":
                                            (marriedStatusID.isEmpty)
                                                ? "0"
                                                : marriedStatusID,
                                        "NoOfDependants":
                                            numberOfDependent.toString(),
                                        "DependantScreeningPending":
                                            numberOfDependentPending.toString(),
                                        "AppoinmentDate":
                                            timeTextController.text,
                                        "AppoinmentTime":
                                            timeAppTextController.text,
                                        "Remark": remarkTextController.text,
                                        "DISTLGDCODE": districtCode.toString(),
                                        "TALLGDCODE": talukaCode.toString(),
                                        "HouseNo":
                                            houseNumberTextController.text,
                                        "Road": roadTextController.text,
                                        "Area": areaTextController.text,
                                        "DependantDetails": dependentJson,
                                        "CReatedBy": empCode.toString(),
                                        "WorkerScreeningStatus":
                                            selectedBeneficiary
                                                ?.isWorkerScreened
                                                .toString(),
                                        "RemarkID": remarkId.toString(),
                                      },
                                    ),
                                  );

                                  context.read<ExpectedBeneficiaryBloc>().add(
                                    ResetExpectedBeneficiaryState(),
                                  );
                                });
                              },
                              textStyle: TextStyle(
                                color: Colors.white,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w600,
                                fontFamily: FontConstants.interFonts,
                              ),
                              buttonColor: kPrimaryColor,
                              icon: Image.asset(
                                iconArrow,
                                height: responsiveHeight(24),
                                width: responsiveHeight(24),
                                color: kWhiteColor,
                              ),
                            ),
                            SizedBox(height: responsiveHeight(20)),
                          ],
                        ),
                      ),
                    ),
                  )
                  : const NoDataFound();
            },
          ),
          //   ],
          // ),
        ),
      ),
    );
  }

  Future<void> _showCustomTimePicker() async {
    // if (_selectedDate == null) {
    if (timeTextController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select a date first')),
      );
      return;
    }

    final TimeOfDay? picked = await showDialog<TimeOfDay>(
      context: context,
      builder: (BuildContext context) {
        return CustomTimePickerDialog(
          initialTime: stringToTimeOfDay(timeAppTextController.text),
          selectedDate: parseDate(timeTextController.text),
        );
      },
    );

    if (picked != null) {
      setState(() {
        timeAppTextController.text = formatTimeOfDay(picked);
      });
    }
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

  List<Map<String, dynamic>> getFilteredDependents(String? marriedStatusID) {
    int statusId = int.tryParse(marriedStatusID ?? "") ?? 0;

    if (statusId == 1) {
      return _numberOfDependentPendingList
          .where((item) => item["id"] <= 5)
          .toList();
    } else if (statusId == 2) {
      return _numberOfDependentPendingList
          .where((item) => item["id"] <= 2)
          .toList();
    } else if (statusId == 3 || statusId == 4) {
      return _numberOfDependentPendingList
          .where((item) => item["id"] <= 4)
          .toList();
    } else {
      return [];
    }
  }

  String? _validatePincodeInput(String? value) {
    if (value == null || value.isEmpty) {
      return 'Please Enter Pincode';
    }
    if (value.length != 6) {
      return 'Pincode should be 6 letters.';
    }

    return null;
  }

  String? _validateMobileInput(String? value) {
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
          context,
          "You cannot select this status as worker screening is pending.",
          () {
            Navigator.of(context).pop();
          },
        );

        // showDialog(
        //   context: context,
        //   builder: (BuildContext dialogContext) {
        //     return AlertDialog(
        //       title: Text(
        //         "Alert",
        //         style: TextStyle(fontFamily: FontConstants.interFonts),
        //       ),
        //       content: Text(
        //         "You cannot select this status as worker screening is pending.",
        //         style: TextStyle(fontFamily: FontConstants.interFonts),
        //       ),
        //       actions: [
        //         SizedBox(
        //           width: 80.w,
        //           child: AppActiveButton(
        //             buttontitle: "OK",
        //             onTap: () {
        //               Navigator.of(context).pop();
        //             },
        //           ),
        //         ),
        //
        //       ],
        //     );
        //   },
        // );
      });

      setState(() {
        _isTextFieldVisibilityDateTime = false;
        callStatusTextController.clear();
      });
    }
  }

  void filterList(String query) {
    setState(() {
      _filteredList =
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

      if (_filteredList.isEmpty) {
        _filteredList = beneficiaryresponsemodel!.output!;
      }
    });
  }

  void _toggleChangeAddress(bool? value) {
    setState(() {
      _isChangeAddress = value ?? false;
      // Keep the registered/current address flag in sync.
      isCurrentAsSameRegId = _isChangeAddress ? 1 : 0;
    });
  }

  String fixEncoding(String text) {
    return utf8.decode(text.runes.toList());
  }
}
