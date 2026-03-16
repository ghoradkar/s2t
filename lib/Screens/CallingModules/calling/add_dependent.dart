// ignore_for_file: use_full_hex_values_for_flutter_colors

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../custom_widgets/no_data_widget.dart';
import '../custom_widgets/selection_bottom_sheet.dart';
import 'bloc/expected_beneficiary_bloc.dart';
import 'models/BeneficiaryResponseModel.dart';
import 'models/CallStatusModel.dart';
import 'models/ScreeningDependentModel.dart';
import 'models/add_dependent_model.dart';
import 'models/calling_address_model.dart';
import 'models/calling_remark_model.dart';
import 'models/relation_model.dart';
import 'models/team_data_model.dart';

// Import the BeneficiaryCard widget

class AddDependentScreen extends StatefulWidget {
  const AddDependentScreen({super.key});

  @override
  State<AddDependentScreen> createState() => _AddDependentScreenState();
}

var crrDate = DateTime.now();
late DateTime firstDayOfWeek;

class _AddDependentScreenState extends State<AddDependentScreen> {
  TextEditingController relationController = TextEditingController();
  TextEditingController ageController = TextEditingController();
  TextEditingController firstNameController = TextEditingController();
  TextEditingController middleNameController = TextEditingController();
  TextEditingController lastNameController = TextEditingController();

  // final TextEditingController _talukaTextController = TextEditingController();
  TextEditingController callStatusTextController = TextEditingController();
  TextEditingController teamNumberTextController = TextEditingController();
  TextEditingController dateTypeTextController = TextEditingController();
  TextEditingController dateTextController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey();

  // final bool _currentAddressVisibility = true;
  // final bool _personalVisibility = true;

  // final bool _isChangeAddress = false;
  RelationOutput? selectedReltion;
  TeamDataOutput? selectedTeamData;
  Map<String, dynamic>? selectDateType;

  bool _isSubmitted = false;

  String fname = "";
  String mname = "";
  String lname = "";
  String workerGender = "";
  String gender = "";
  int numberOfDependent = 0;
  int numberOfDependentPending = 0;

  bool _isRelationSheetVisible = false;

  BeneficiaryOutput? selectedBeneficiary;
  Beneficiaryresponsemodel? beneficiaryresponsemodel;
  CallingAddressModel? callingAddressModel;
  int? assignCallID;
  String? marriedStatusID;
  CallingRemarkModel? callingRemarkModel;
  Callstatusmodel? callstatusModel;
  RelationModel? relationOutput;
  TeamDataModel? teamDataModel;
  List<BeneficiaryOutput> _filteredList = [];
  final List<RelationOutput> _filteredRelationList = [];

  // final List<TeamDataOutput> _filteredTeamList = [];
  final List<CallingAddressOutput> _addressList = [];
  late List<ScreeningDataOutput> screeningdataList = [];

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      setState(() {
        final Map<String, dynamic>? args =
            ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>?;

        if (args != null) {
          selectedBeneficiary =
              args["selectedBeneficiary"] as BeneficiaryOutput;
          workerGender = args["workerGender"] as String;
          numberOfDependent = args["noOfDependent"] as int;
          numberOfDependentPending = args["numberOfDependentPending"] as int;
          assignCallID = args["assignCallId"] as int;
          marriedStatusID = args["marriedStatus"] as String;
          screeningdataList =
              args["screeningdataList"] as List<ScreeningDataOutput>;
        }

        // API: load address details for this beneficiary call (used for context/display).
        context.read<ExpectedBeneficiaryBloc>().add(
          GetAddressDetails(
            payload: {
              "AssignCallID": selectedBeneficiary?.assignCallID.toString(),
            },
          ),
        );
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return NetworkWrapper(
      child: BlocListener<ExpectedBeneficiaryBloc, ExpectedBeneficiaryState>(
        listener: (context, state) {
          if (state.addDependentStatus.isSuccess) {
            ScaffoldMessenger.of(context)
              ..clearSnackBars()
              ..showSnackBar(
                const SnackBar(
                  content: Text("Depended added successfully!"),
                  duration: Duration(seconds: 3),
                  backgroundColor: Colors.green,
                ),
              );
            setState(() {
              relationController.clear();
              firstNameController.clear();
              middleNameController.clear();
              lastNameController.clear();
              ageController.clear();
              selectedReltion = null;
            });

            if (state.addDependentOutput.length == numberOfDependentPending) {
              Navigator.pop(context);
            }
          }

          // if (state.addDependentOutput.length < numberOfDependent) {
          //   Navigator.pop(context); // This will go back
          // }

          if (state.getRealtionStatus.isSuccess && !_isRelationSheetVisible) {
            // setState(() {
            //   selectedCallStatus=0;
            // });
            relationOutput = RelationModel.fromJson(
              jsonDecode(state.getRelationResponse),
            );
            _filteredRelationList.clear();
            setState(() {
              _filteredRelationList.addAll(relationOutput!.output!);
              _isRelationSheetVisible = true;
            });

            showModalBottomSheet(
              context: context,
              isScrollControlled: true,
              builder: (context) {
                return StatefulBuilder(
                  builder: (c, sheetState) {
                    return SelectionBottomSheet<RelationOutput, int>(
                      title: "Relation",
                      items: _filteredRelationList,
                      selectedValue: selectedReltion?.relId,
                      valueFor: (item) => item.relId ?? 0,
                      labelFor: (item) => item.relName ?? 'NA',
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
                        horizontal: 4.0,
                      ),
                      selectedBackgroundColor: kPrimaryColor.withOpacity(0.1),
                      itemTextStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.normal,
                        color: kBlackColor,
                      ),
                      onItemTap: (item) {
                        sheetState(() {
                          selectedReltion = item;
                        });

                        relationController.text =
                            selectedReltion?.relName ?? "";

                        final relationId =
                            selectedReltion?.relId.toString() ?? "";
                        final relationName = selectedReltion?.relName ?? "";

                        bool isAlreadyAdded = false;
                        int count7 = 0;
                        int count8 = 0;

                        final dependentList =
                            context
                                .read<ExpectedBeneficiaryBloc>()
                                .state
                                .addDependentOutput;

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

                        // Also check in _screeningdataList
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
                            context,
                            "This relation is already added.",
                            () {
                              Navigator.of(context, rootNavigator: true).pop();
                              // Clear selection on validation failure
                              sheetState(() {
                                selectedReltion = null;
                              });
                            },
                          );

                          // showDialog(
                          //   context: context,
                          //   builder: (BuildContext dialogContext) {
                          //     return AlertDialog(
                          //       title: const Text("Notice"),
                          //       content: const Text(
                          //         "This relation is already added.",
                          //       ),
                          //       actions: [
                          //         TextButton(
                          //           onPressed: () {
                          //             Navigator.of(
                          //               context,
                          //               rootNavigator: true,
                          //             ).pop();
                          //             // Clear selection on validation failure
                          //             sheetState(() {
                          //               selectedReltion = null;
                          //             });
                          //           },
                          //           child: const Text("OK"),
                          //         ),
                          //       ],
                          //     );
                          //   },
                          // );

                          relationController.clear();
                          return;
                        }

                        if (relationId == "7" || relationId == "8") {
                          if (count7 >= 2 ||
                              count8 >= 2 ||
                              (count7 >= 1 && count8 >= 1)) {
                            ToastManager.showAlertDialog(
                              context,
                              "Maximum two children registration is allowed.",
                              () {
                                Navigator.of(
                                  context,
                                  rootNavigator: true,
                                ).pop();
                                // Clear selection on validation failure
                                sheetState(() {
                                  selectedReltion = null;
                                });
                              },
                            );

                            // showDialog(
                            //   context: context,
                            //   builder: (BuildContext dialogContext) {
                            //     return AlertDialog(
                            //       title: const Text("Notice"),
                            //       content: const Text(
                            //         "Maximum two children registration is allowed.",
                            //       ),
                            //       actions: [
                            //         TextButton(
                            //           onPressed: () {
                            //             Navigator.of(
                            //               context,
                            //               rootNavigator: true,
                            //             ).pop();
                            //             // Clear selection on validation failure
                            //             sheetState(() {
                            //               selectedReltion = null;
                            //             });
                            //           },
                            //           child: const Text("OK"),
                            //         ),
                            //       ],
                            //     );
                            //   },
                            // );

                            relationController.clear();
                            return;
                          }
                        }

                        // STEP 3: Success flow
                        relationController.text = relationName;
                        updateNameFields(relationId, gender, workerGender);

                        // STEP 4: Close sheet and trigger bloc event
                        Navigator.pop(context);
                        context.read<ExpectedBeneficiaryBloc>().add(
                          ResetExpectedBeneficiaryState(),
                        );
                      },
                    );
                  },
                );
              },
            ).whenComplete(() {
              setState(() {
                _isRelationSheetVisible = false;
              });
            });
          }

          if (state.getAddressDetailStatus.isSuccess) {
            callingAddressModel = CallingAddressModel.fromJson(
              jsonDecode(state.getAddressDetailResponse),
            );
            _addressList.clear();

            setState(() {
              var addressData = callingAddressModel?.output;

              if (addressData != null && addressData.isNotEmpty) {
                _addressList.addAll(addressData);

                fname = _addressList[0].firstName ?? "";

                mname = _addressList[0].middleName ?? "";

                lname = _addressList[0].lastName ?? "";
                gender = _addressList[0].gender ?? "";

                // marriedStatusID = _addressList[0].workersMaritalStatus ?? "";

                marriedStatusID = marriedStatusID;
              } else {
                debugPrint("Address list is empty or null.");
              }
            });
          }

          if (state.getRealtionStatus.isFailure) {
            // var resCall = jsonDecode(state.beneficiaryResponse);
            ToastManager.showAlertDialog(
              context,
              relationOutput?.message ?? "Something went wrong try again",
              () {
                Navigator.pop(context);
              },
            );
            // showDialog(
            //   context: context,
            //   builder: (c) {
            //
            //     return AlertDialog(
            //       title: Text(
            //         "Failed",
            //         style: TextStyle(fontFamily: FontConstants.interFonts),
            //       ),
            //       content: Column(
            //         mainAxisSize: MainAxisSize.min,
            //         children: [
            //           Text(
            //             relationOutput?.message ??
            //                 "Something went wrong try again",
            //             style: TextStyle(fontFamily: FontConstants.interFonts),
            //           ),
            //         ],
            //       ),
            //       actions: [
            //         TextButton(
            //           onPressed: () {
            //             Navigator.pop(context);
            //           },
            //           child: Text(
            //             "Okay",
            //             style: TextStyle(fontFamily: FontConstants.interFonts),
            //           ),
            //         ),
            //       ],
            //     );
            //   },
            // );
          }
        },
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Add Dependent',
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
                      padding: EdgeInsets.all(responsiveHeight(15)),
                      child: SingleChildScrollView(
                        child: Column(
                          children: [
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
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Padding(
                                padding: EdgeInsets.all(responsiveHeight(10)),
                                child: Column(
                                  children: [
                                    SizedBox(height: responsiveHeight(6)),
                                    AppTextField(
                                      onTap: () {
                                        setState(() {
                                          // _isRelationSheetVisible = true;
                                        });

                                        // API: fetch allowed relation options based on marital status and gender.
                                        context
                                            .read<ExpectedBeneficiaryBloc>()
                                            .add(
                                              GetRealationData(
                                                payload: {
                                                  "MaritalStatusID":
                                                      marriedStatusID
                                                          .toString(),
                                                  "Gender": workerGender,
                                                },
                                              ),
                                            );
                                      },
                                      controller: relationController,
                                      readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Relation with Worker',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily:
                                                FontConstants.interFonts,
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
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                      suffixIcon:
                                          state.getRealtionStatus.isInProgress
                                              ? SizedBox(
                                                height: responsiveHeight(20),
                                                width: responsiveHeight(20),
                                                child: const Center(
                                                  child:
                                                      CircularProgressIndicator(),
                                                ),
                                              )
                                              : SizedBox(
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                                child: Center(
                                                  child: Image.asset(
                                                    downArrow,
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                  ),
                                                ),
                                              ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),

                                    AppTextField(
                                      textInputType: TextInputType.number,
                                      maxLength: 2,
                                      onTap: () {
                                        if (selectedReltion == null) {
                                          ToastManager.showAlertDialog(
                                            context,
                                            "Please select relation",
                                            () {
                                              context
                                                  .read<
                                                    ExpectedBeneficiaryBloc
                                                  >()
                                                  .add(
                                                    ResetExpectedBeneficiaryState(),
                                                  );
                                              Navigator.of(context).pop();
                                            },
                                          );

                                          // showDialog(
                                          //   context: context,
                                          //   builder: (BuildContext context) {
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
                                          //         "Please select relation",
                                          //         style: TextStyle(
                                          //           fontFamily:
                                          //               FontConstants
                                          //                   .interFonts,
                                          //         ),
                                          //       ),
                                          //       actions: [
                                          //         TextButton(
                                          //           onPressed: () {
                                          //             context
                                          //                 .read<
                                          //                   ExpectedBeneficiaryBloc
                                          //                 >()
                                          //                 .add(
                                          //                   ResetExpectedBeneficiaryState(),
                                          //                 );
                                          //             Navigator.of(
                                          //               context,
                                          //             ).pop();
                                          //           },
                                          //           child: Text(
                                          //             "OK",
                                          //             style: TextStyle(
                                          //               fontFamily:
                                          //                   FontConstants
                                          //                       .interFonts,
                                          //             ),
                                          //           ),
                                          //         ),
                                          //       ],
                                          //     );
                                          //   },
                                          // );

                                          return;
                                        }
                                      },
                                      controller: ageController,
                                      validators: _validateInput,
                                      errorText:
                                          _isSubmitted
                                              ? _validateInput(
                                                ageController.text,
                                              )
                                              : null,
                                      onChange: (value) {
                                        setState(() {});
                                      },
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Age',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily:
                                                FontConstants.interFonts,
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
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        calendar,
                                        scale: 3.5,
                                      ),
                                    ),

                                    // TextFormField(
                                    //   keyboardType: TextInputType.phone,
                                    //   inputFormatters: [
                                    //     LengthLimitingTextInputFormatter(10),
                                    //   ],
                                    //   decoration: InputDecoration(
                                    //     labelText: 'Mobile Number',
                                    //     border: OutlineInputBorder(
                                    //       borderRadius:
                                    //           BorderRadius.circular(8),
                                    //       borderSide: const BorderSide(
                                    //         color: Colors.grey,
                                    //       ),
                                    //     ),
                                    //     enabledBorder: OutlineInputBorder(
                                    //       borderRadius:
                                    //           BorderRadius.circular(8),
                                    //       borderSide: const BorderSide(
                                    //         color: Colors.grey,
                                    //       ),
                                    //     ),
                                    //     focusedBorder: OutlineInputBorder(
                                    //       borderRadius:
                                    //           BorderRadius.circular(8),
                                    //       borderSide: const BorderSide(
                                    //         color: Colors.blue,
                                    //         width: 2.0,
                                    //       ),
                                    //     ),
                                    //     suffixIcon: IconButton(
                                    //       icon: const Icon(
                                    //         Icons.mobile_friendly,
                                    //         color: Colors.grey,
                                    //       ),
                                    //       onPressed: () {},
                                    //     ),
                                    //   ),
                                    //   validator: (value) {
                                    //     if (value == null || value.isEmpty) {
                                    //       return 'Please enter your mobile number';
                                    //     }
                                    //     if (value.length < 10) {
                                    //       return 'Mobile number must be 10 digits';
                                    //     }
                                    //     return null;
                                    //   },
                                    //   onSaved: (String? value) {
                                    //     // onSaved(value!);
                                    //   },
                                    // ),
                                    SizedBox(height: responsiveHeight(25)),
                                    AppTextField(
                                      controller: firstNameController,
                                      textInputType: TextInputType.text,
                                      // textCapitalization:
                                      // TextCapitalization.characters,
                                      inputFormatters: [
                                        UpperCaseTextFormatter(),
                                      ],

                                      // readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'First Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),
                                    AppTextField(
                                      controller: middleNameController,
                                      textInputType: TextInputType.text,
                                      // textCapitalization:
                                      // TextCapitalization.characters,
                                      inputFormatters: [
                                        UpperCaseTextFormatter(),
                                      ],
                                      // readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Middle Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),
                                    AppTextField(
                                      readOnly:
                                          (relationController.text
                                                          .toLowerCase() ==
                                                      "father in law" ||
                                                  relationController.text
                                                          .toLowerCase() ==
                                                      "mother in law")
                                              ? false
                                              : true,
                                      controller: lastNameController,

                                      textInputType: TextInputType.text,
                                      // textCapitalization:
                                      // TextCapitalization.characters,
                                      inputFormatters: [
                                        UpperCaseTextFormatter(),
                                      ],
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Last Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),
                                    Row(
                                      children: [
                                        Image.asset(
                                          icSquarePulsAlert,
                                          scale: 3.5,
                                          color: Colors.red,
                                        ),
                                        const SizedBox(
                                          width: 8,
                                        ), // Space between image and text
                                        Text(
                                          'Please Use English Keyboard Only',
                                          style: TextStyle(
                                            color: Colors.red,
                                            fontSize: responsiveFont(12),
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                        ),
                                      ],
                                    ),
                                    SizedBox(height: responsiveHeight(34)),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(height: responsiveHeight(60)),
                            Visibility(
                              visible:
                                  state.addDependentOutput.length <
                                      numberOfDependentPending,
                              child: AppButtonWithIcon(
                                title: "Save",
                                mHeight: responsiveHeight(60),
                                mWidth: responsiveWidth(140),
                                onTap: () {
                                  setState(() {
                                    _isSubmitted = true;
                                  });

                                  if (selectedReltion == null) {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please select relation",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }
                                  if (firstNameController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please enter first name",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (lastNameController.text == "") {
                                    ToastManager.showAlertDialog(
                                      context,
                                      "Please enter last name",
                                      () {
                                        Navigator.of(context).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (!_formKey.currentState!.validate()) {
                                    return;
                                  } else {
                                    ExpectedBeneficiaryState
                                    expectedBeneficiaryState =
                                        context
                                            .read<ExpectedBeneficiaryBloc>()
                                            .state;
                                    List<AddDependentOutput>
                                    addDependentOutput = [];
                                    addDependentOutput.addAll(
                                      expectedBeneficiaryState
                                          .addDependentOutput,
                                    );
                                    addDependentOutput.add(
                                      AddDependentOutput(
                                        age: ageController.text,
                                        relId: selectedReltion?.relId,
                                        firstName: firstNameController.text,
                                        middleName: middleNameController.text,
                                        lastName: lastNameController.text,
                                        lastDependantScreeningDate: "",
                                        assignCallID: assignCallID,
                                      ),
                                    );

                                    context.read<ExpectedBeneficiaryBloc>().add(
                                      AddDependentRequest(
                                        addDependentModel: addDependentOutput,
                                      ),
                                    );

                                    setState(() {
                                      _isSubmitted = false;
                                    });

                                    context.read<ExpectedBeneficiaryBloc>().add(
                                      ResetExpectedBeneficiaryState(),
                                    );
                                  }
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
                            ),

                            SizedBox(height: responsiveHeight(44)),
                          ],
                        ),
                      ),
                    ),
                  )
                  : const NoDataFound();
            },
          ),
        ),
      ),
    );
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
                    item.mobile!.toLowerCase().contains(query.toLowerCase()),
              )
              .toList();

      if (_filteredList.isEmpty) {
        _filteredList = beneficiaryresponsemodel!.output!;
      }
    });
  }

  String? _validateInput(String? value) {
    String relationId = selectedReltion?.relId.toString() ?? '';
    int beneficiaryAge = selectedBeneficiary?.age ?? 0;

    if (value == null || value.isEmpty) {
      return 'कृपया वय प्रविष्ट करा.';
    }
    if (value.length != 2) {
      if (relationId == "7" || relationId == "8") {
        return '१. मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त असावे आणि २. वयातील\nफरक किमान १५ वर्ष असावा.';
      } else if ((relationId == "1" ||
          relationId == "2" ||
          relationId == "21" ||
          relationId == "22")) {
        return 'आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम\nकामगारापेक्षा जास्त असावे.';
      } else if (relationId == "10" || relationId == "9") {
        return 'पत्नी/पती  चे वय १८ पेक्षा जास्त आणि ७५ पेक्षा कमी असावे';
      }
    }

    try {
      int enteredAge = int.parse(value);

      if (relationId == "1" ||
          relationId == "2" ||
          relationId == "21" ||
          relationId == "22") {
        if (!(enteredAge >= 18 && enteredAge >= beneficiaryAge + 1)) {
          return 'आई,वडील,सासू,सासरे यांचे वय नोंदणीकृत बांधकाम\nकामगारापेक्षा जास्त असावे.';
        }
      } else if (relationId == "10" || relationId == "9") {
        if (!(enteredAge >= 18 && enteredAge <= 75)) {
          return 'पत्नी/पती  चे वय १८ पेक्षा जास्त आणि ७५ पेक्षा कमी असावे.';
        }
      } else if (relationId == "7" || relationId == "8") {
        if (!(enteredAge >= 10 && enteredAge <= beneficiaryAge - 15)) {
          return '१. मुलगा/मुलीचे वय १० वर्षापेक्षा जास्त असावे आणि\n२. वयातील फरक किमान १५ वर्ष असावा.';
        }
      }
    } catch (e) {
      return 'अवैध वय प्रविष्ट केले आहे.';
    }

    return null; // No error
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
