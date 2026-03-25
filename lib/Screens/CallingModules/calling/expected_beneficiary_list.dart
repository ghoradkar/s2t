import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../custom_widgets/beneficiary_card.dart' show BeneficiaryCard;
import '../custom_widgets/network_wrapper.dart';
import '../custom_widgets/no_data_widget.dart';
import '../custom_widgets/selection_bottom_sheet.dart';
import 'bloc/expected_beneficiary_bloc.dart';
import 'models/BeneficiaryResponseModel.dart';
import 'models/CallStatusModel.dart';
import 'models/team_data_model.dart';

class ExpectedBeneficiaryList extends StatefulWidget {
  const ExpectedBeneficiaryList({super.key});

  @override
  State<ExpectedBeneficiaryList> createState() =>
      _ExpectedBeneficiaryListState();
}

var crrDate = DateTime.now();
late DateTime firstDayOfWeek;

class _ExpectedBeneficiaryListState extends State<ExpectedBeneficiaryList> {
  TextEditingController searchTextController = TextEditingController();
  TextEditingController callStatusTextController = TextEditingController();
  TextEditingController teamNumberTextController = TextEditingController();
  TextEditingController dateTypeTextController = TextEditingController();
  TextEditingController dateTextController = TextEditingController();

  CallStatusOutput? selectedCallStatus;
  TeamDataOutput? selectedTeamData;
  Map<String, dynamic>? selectDateType;

  Beneficiaryresponsemodel? beneficiaryresponsemodel;
  Callstatusmodel? callstatusModel;
  TeamDataModel? teamDataModel;
  List<BeneficiaryOutput> _filteredList = [];
  List<CallStatusOutput> filteredCallStatusList = [];
  final List<TeamDataOutput> _filteredTeamList = [];

  int fromDateTypeData = 0;
  int teamId = 0;
  int dateTypeId = 0;
  String selectedDate = "";
  Timer? _searchDebounce;
  bool _isInitialCallStatusLoad = true;
  bool _wasCallStatusSuccess = false;
  bool _hasLoadedOnce = false;

  // Cache user data to avoid repeated DataProvider calls
  int? _cachedEmpCode;
  int? _cachedDesId;
  int? _cachedMobileNo;
  String? _cachedMyOperatorUserId;
  int? _cachedAgentId;

  // final bool _isReturning = false; // Track if returning to the view

  final List<Map<String, dynamic>> _dateTypeList = [
    {"id": 1, "name": "Assign Date"},
    {"id": 2, "name": "Appointment Date"},
    {"id": 3, "name": "Renewal Date"},
  ];

  @override
  void initState() {
    // Cache user data once to avoid repeated DataProvider calls in cards
    final userData = DataProvider().getParsedUserData()?.output?[0];
    _cachedEmpCode = userData?.empCode ?? 0;
    _cachedDesId = userData?.dESGID ?? 0;
    _cachedMobileNo = int.tryParse(userData?.bMobile?.toString() ?? '') ?? 0;
    _cachedMyOperatorUserId = userData?.myOperatorUserID ?? "";
    _cachedAgentId = int.tryParse(userData?.agentID?.toString() ?? '') ?? 0;

    // API: load the initial list of beneficiaries expected for calling.
    // Pre-set Team Number default to "All"
    teamId = 0;
    selectedTeamData = TeamDataOutput(
      teamName: "All",
      teamid: 0,
      member1: "NA",
      member2: "NA",
    );
    teamNumberTextController.text = "All";

    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      context.read<ExpectedBeneficiaryBloc>().add(
        BeneficiaryRequest(
          payload: const {"CallStatusID": "0", "TeamID": "0", "GroupID": "1"},
        ),
      );

      // Load call status list on init to auto-select "Calling Pending" as default
      context.read<ExpectedBeneficiaryBloc>().add(GetCallStatusRequest());

      // Pre-cache images for better performance
      precacheImage(const AssetImage(icFilter), context);
      precacheImage(const AssetImage(icSearch), context);
      precacheImage(const AssetImage(icArrowDownGreen), context);
      precacheImage(const AssetImage(iconArrow), context);
      precacheImage(const AssetImage(iconBackArrow), context);

      // Pre-cache card images (icons used in BeneficiaryCard)
      precacheImage(const AssetImage(icHashIcon), context);
      precacheImage(const AssetImage(iconCalender), context);
      precacheImage(const AssetImage(iconPerson), context);
      precacheImage(const AssetImage(iconPersons), context);
      precacheImage(const AssetImage(iconMap), context);
      precacheImage(const AssetImage(iconDocument), context);
      precacheImage(const AssetImage(icSuccessRoundGreen), context);
    });

    super.initState();
  }

  @override
  void dispose() {
    _searchDebounce?.cancel();
    searchTextController.dispose();
    callStatusTextController.dispose();
    teamNumberTextController.dispose();
    dateTypeTextController.dispose();
    dateTextController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return NetworkWrapper(
      child: BlocListener<ExpectedBeneficiaryBloc, ExpectedBeneficiaryState>(
        listener: (context, state) {
          // Track transition: only true when getCallStatus just became success this call
          final callStatusJustSucceeded =
              state.getCallStatus.isSuccess && !_wasCallStatusSuccess;
          _wasCallStatusSuccess = state.getCallStatus.isSuccess;

          if (state.beneficiaryStatus.isSuccess) {
            beneficiaryresponsemodel = Beneficiaryresponsemodel.fromJson(
              jsonDecode(state.beneficiaryResponse),
            );
            _filteredList.clear();
            _filteredList = beneficiaryresponsemodel?.output ?? [];
            setState(() {
              _hasLoadedOnce = true;
            });

            // Delay state reset to ensure UI updates first
            Future.delayed(const Duration(milliseconds: 300), () {
              // context
              //     .read<ExpectedBeneficiaryBloc>()
              //     .add(ResetExpectedBeneficiaryState());
            });
          }

          if (state.beneficiaryStatus.isFailure) {
            setState(() {
              _filteredList.clear();
            });

            try {
              var resJ = jsonDecode(state.beneficiaryResponse);
              if (resJ['message'] !=
                  "Assign Expected Beneficiaries List for calling details not found") {
                ToastManager.showAlertDialog(context, resJ['message'], () {
                  Navigator.pop(context);
                });
              }
            } catch (e) {
              ToastManager.showAlertDialog(context, "Data Not Found.!!", () {
                Navigator.pop(context);
              });
              // showDialog(
              //   context: context,
              //   builder: (c) {
              //     return AlertDialog(
              //       title: Text(
              //         "Failed",
              //         style: TextStyle(fontFamily: FontConstants.interFonts),
              //       ),
              //       content: Column(
              //         mainAxisSize: MainAxisSize.min,
              //         children: [
              //           Text(
              //             state.beneficiaryResponse,
              //             style: TextStyle(
              //               fontFamily: FontConstants.interFonts,
              //             ),
              //           ),
              //         ],
              //       ),
              //       actions: [
              //         SizedBox(
              //           width: 80.w,
              //           child: AppActiveButton(
              //             buttontitle: "OK",
              //             onTap: () {
              //               Navigator.pop(context);
              //             },
              //           ),
              //         ),
              //
              //       ],
              //     );
              //   },
              // );
            }
          }

          if (state.dateTypeWiseDataStatus.isSuccess) {
            setState(() {
              _hasLoadedOnce = true;
              beneficiaryresponsemodel = Beneficiaryresponsemodel.fromJson(
                jsonDecode(state.beneficiaryResponse),
              );
              _filteredList.clear();
              _filteredList.addAll(beneficiaryresponsemodel!.output!);
            });

            Future.delayed(const Duration(milliseconds: 300), () {});
          }

          if (state.dateTypeWiseDataStatus.isFailure) {
            setState(() {
              _filteredList.clear();
            });
          }

          if (callStatusJustSucceeded) {
            callstatusModel = Callstatusmodel.fromJson(
              jsonDecode(state.getCallingResponse),
            );
            filteredCallStatusList.clear();
            filteredCallStatusList.add(
              CallStatusOutput(
                appointmentStatus: "All",
                groupID: 0,
                assignStatusID: 0,
              ),
            );
            filteredCallStatusList.addAll(callstatusModel!.output!);

            print("=== FILTERED LIST ===");
            for (var item in filteredCallStatusList) {
              print(
                "Item: ${item.appointmentStatus} - ID: ${item.assignStatusID}",
              );
            }
            print(
              "Currently selected: ${selectedCallStatus?.appointmentStatus} - ID: ${selectedCallStatus?.assignStatusID}",
            );
            print("====================");

            if (_isInitialCallStatusLoad) {
              _isInitialCallStatusLoad = false;
              final callingPending = filteredCallStatusList.firstWhere(
                (item) => item.appointmentStatus == "Calling Pending",
                orElse: () => filteredCallStatusList.first,
              );
              setState(() {
                selectedCallStatus = callingPending;
                callStatusTextController.text =
                    callingPending.appointmentStatus ?? "";
              });
              return;
            }

            String tempSelectedStatus =
                selectedCallStatus?.appointmentStatus ?? "";
            showModalBottomSheet(
              context: context,
              isScrollControlled: true,
              builder: (context) {
                return StatefulBuilder(
                  builder: (c, sheetState) {
                    return SelectionBottomSheet<CallStatusOutput, String>(
                      title: "Call Status",
                      items: filteredCallStatusList,
                      selectedValue: tempSelectedStatus,
                      valueFor: (item) => item.appointmentStatus ?? "",
                      labelFor: (item) => item.appointmentStatus ?? 'NA',
                      height: 360.h,
                      padding: EdgeInsets.only(
                        top: responsiveHeight(28),
                        left: responsiveHeight(35),
                        right: responsiveHeight(35),
                        bottom: responsiveHeight(60),
                      ),
                      titleTextStyle: TextStyle(
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                      titleBottomSpacing: 30.h,
                      itemPadding: EdgeInsets.symmetric(vertical: 4.h),
                      itemContainerPadding: const EdgeInsets.symmetric(
                        vertical: 8.0,
                        horizontal: 4.0,
                      ),
                      selectedBackgroundColor: kPrimaryColor.withOpacity(0.1),
                      itemTextStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 13.sp,
                        fontWeight: FontWeight.normal,
                        color: kBlackColor,
                      ),
                      selectedItemTextStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 13.sp,
                        fontWeight: FontWeight.w600,
                        color: kPrimaryColor,
                      ),
                      onItemTap: (item) async {
                        print(
                          "Selected item: ${item.appointmentStatus} - ID: ${item.assignStatusID}",
                        );
                        sheetState(() {
                          tempSelectedStatus = item.appointmentStatus ?? "";
                        });

                        await Future.delayed(const Duration(milliseconds: 200));

                        setState(() {
                          selectedCallStatus = item;
                          callStatusTextController.text =
                              item.appointmentStatus ?? "";
                          teamNumberTextController.text = "";
                          teamId = 0;
                          selectedTeamData = null;
                          dateTextController.text = "";
                          dateTypeId = 0;
                          dateTypeTextController.text = "";
                          selectedDate = "";
                          fromDateTypeData = 0;
                        });

                        print(
                          "After setState - selectedCallStatus: ${selectedCallStatus?.appointmentStatus} - ID: ${selectedCallStatus?.assignStatusID}",
                        );

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

          if (state.getCallStatus.isFailure) {
            ToastManager.showAlertDialog(
              context,
              state.beneficiaryResponse.isEmpty
                  ? "Something Went Wrong try again"
                  : state.beneficiaryResponse,
              () {
                Navigator.pop(context);
              },
            );

            // showDialog(
            //   context: context,
            //   builder: (c) {
            //     return AlertDialog(
            //       title: Text(
            //         "Failed",
            //         style: TextStyle(fontFamily: FontConstants.interFonts),
            //       ),
            //       content: Column(
            //         mainAxisSize: MainAxisSize.min,
            //         children: [
            //           Text(
            //             state.beneficiaryResponse.isEmpty
            //                 ? "Something Went Wrong try again"
            //                 : state.beneficiaryResponse,
            //             style: TextStyle(fontFamily: FontConstants.interFonts),
            //           ),
            //         ],
            //       ),
            //       actions: [
            //         SizedBox(
            //           width: 80.w,
            //           child: AppActiveButton(
            //             buttontitle: "OK",
            //             onTap: () {
            //               Navigator.pop(context);
            //             },
            //           ),
            //         ),
            //
            //       ],
            //     );
            //   },
            // );
          }

          if (state.teamStatus.isSuccess) {
            teamDataModel = TeamDataModel.fromJson(
              jsonDecode(state.teamResponse),
            );
            _filteredTeamList.clear();
            _filteredTeamList.add(
              TeamDataOutput(
                teamName: "All",
                teamid: 0,
                member1: "NA",
                member2: "NA",
              ),
            );
            _filteredTeamList.addAll(teamDataModel!.output!);

            showModalBottomSheet(
              context: context,
              isScrollControlled: true,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
              ),
              builder: (context) {
                return StatefulBuilder(
                  builder: (c, sheetState) {
                    return SelectionBottomSheet<TeamDataOutput, int>(
                      title: "Select Team",
                      items: _filteredTeamList,
                      selectedValue: selectedTeamData?.teamid,
                      valueFor: (item) => item.teamid ?? 0,
                      labelFor: (item) => item.teamName ?? 'NA',
                      height: MediaQuery.of(context).size.height * 0.7,
                      padding: EdgeInsets.only(
                        top: responsiveHeight(20),
                        left: responsiveHeight(20),
                        right: responsiveHeight(20),
                        bottom: responsiveHeight(20),
                      ),
                      titleTextStyle: TextStyle(
                        fontSize: responsiveFont(16),
                        fontWeight: FontWeight.normal,
                        fontFamily: FontConstants.interFonts,
                      ),
                      titleBottomSpacing: responsiveHeight(16),
                      showRadio: false,
                      useInkWell: false,
                      itemBuilder: (context, item, isSelected) {
                        return Container(
                          margin: EdgeInsets.only(bottom: 8.h),
                          decoration: BoxDecoration(
                            color:
                                isSelected ? kTextOutlineColor : Colors.white,
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(
                              color:
                                  isSelected
                                      ? const Color(0xFF3B5998)
                                      : Colors.grey.shade300,
                              width: 1,
                            ),
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              // Team ID Header
                              Container(
                                width: double.infinity,
                                padding: EdgeInsets.symmetric(
                                  vertical: 4.h,
                                  horizontal: 16.w,
                                ),
                                decoration: BoxDecoration(
                                  gradient: LinearGradient(
                                    begin: Alignment.bottomRight,
                                    end: Alignment.topLeft,
                                    colors: [
                                      kFirstAppBarcolor.withValues(alpha: 0.4),
                                      kFirstAppBarcolor,
                                    ],
                                  ),
                                  borderRadius: const BorderRadius.only(
                                    topLeft: Radius.circular(8),
                                    topRight: Radius.circular(8),
                                  ),
                                ),
                                child: Text(
                                  "( ${item.teamName ?? 'NA'} )",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontWeight: FontWeight.w600,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              // Member 1
                              if (item.member1 != null &&
                                  item.member1!.isNotEmpty)
                                Padding(
                                  padding: EdgeInsets.symmetric(
                                    horizontal: 16.w,
                                    vertical: 6.h,
                                  ),
                                  child: Text(
                                    item.member1 ?? 'NA',
                                    style: TextStyle(
                                      fontSize: 12.sp,
                                      fontWeight: FontWeight.w500,
                                      fontFamily: FontConstants.interFonts,
                                      color:
                                          isSelected
                                              ? Colors.white
                                              : Colors.black87,
                                    ),
                                  ),
                                ),
                              // Divider
                              if (item.member1 != null && item.member2 != null)
                                Padding(
                                  padding: EdgeInsets.symmetric(
                                    horizontal: 16.w,
                                  ),
                                  child: Divider(
                                    height: 1,
                                    color:
                                        isSelected
                                            ? Colors.white38
                                            : Colors.grey.shade300,
                                  ),
                                ),
                              // Member 2
                              if (item.member2 != null &&
                                  item.member2!.isNotEmpty)
                                Padding(
                                  padding: EdgeInsets.symmetric(
                                    horizontal: 16.w,
                                    vertical: 6.h,
                                  ),
                                  child: Text(
                                    item.member2 ?? 'NA',
                                    style: TextStyle(
                                      fontSize: 12.sp,
                                      fontWeight: FontWeight.w500,
                                      fontFamily: FontConstants.interFonts,
                                      color:
                                          isSelected
                                              ? Colors.white
                                              : Colors.black87,
                                    ),
                                  ),
                                ),
                            ],
                          ),
                        );
                      },
                      onItemTap: (item) {
                        sheetState(() {
                          selectedTeamData = item;
                        });
                        if (selectedTeamData != null) {
                          teamNumberTextController.text =
                              selectedTeamData?.teamName ?? "";
                          teamId = selectedTeamData?.teamid ?? 0;
                          Navigator.pop(context);
                          context.read().add(ResetExpectedBeneficiaryState());
                        }
                      },
                    );
                  },
                );
              },
            );
          }
        },
        child: Scaffold(
          resizeToAvoidBottomInset: false,
          appBar: mAppBar(
            scTitle: 'Expected Beneficiary List',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Navigator.pop(context);
            },
            actions: [
              GestureDetector(
                onTap: () {
                  showModalBottomSheet(
                    context: context,
                    isScrollControlled: true,
                    isDismissible: false,
                    enableDrag: false,
                    builder: (c) {
                      return BlocBuilder<
                        ExpectedBeneficiaryBloc,
                        ExpectedBeneficiaryState
                      >(
                        builder: (context, state) {
                          return Container(
                            decoration: BoxDecoration(
                              color: kWhiteColor,
                              borderRadius: BorderRadius.only(
                                topLeft: Radius.circular(responsiveHeight(40)),
                                topRight: Radius.circular(responsiveHeight(40)),
                              ),
                            ),
                            width: SizeConfig.screenWidth,
                            child: Padding(
                              padding: EdgeInsets.symmetric(
                                vertical: 20.h,
                                horizontal: 20.w,
                              ),
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(
                                        "Filters",
                                        style: TextStyle(
                                          fontFamily: FontConstants.interFonts,
                                          fontSize: 16.sp,
                                        ),
                                      ),
                                      InkWell(
                                        onTap: () {
                                          // callStatusTextController.clear();
                                          // teamNumberTextController.clear();
                                          // dateTypeTextController.clear();
                                          // dateTextController.clear();
                                          Navigator.pop(context);
                                        },
                                        child: Icon(Icons.cancel_presentation),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 26.h),
                                  AppTextField(
                                    onTap: () {
                                      // API: fetch available call-status options for filtering.
                                      context
                                          .read<ExpectedBeneficiaryBloc>()
                                          .add(GetCallStatusRequest());
                                    },
                                    controller: callStatusTextController,
                                    readOnly: true,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Call Status',
                                        style: TextStyle(
                                          color: kLabelTextColor,
                                          fontSize: 14.sp,
                                          fontFamily: FontConstants.interFonts,
                                        ),
                                        children: <TextSpan>[
                                          TextSpan(
                                            text: ' *',
                                            style: TextStyle(
                                              color: Colors.red,
                                              fontSize: 14.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    prefixIcon: const Icon(
                                      Icons.call_outlined,
                                      color: kPrimaryColor,
                                    ).paddingOnly(left: 6.0),
                                    suffixIcon:
                                        state.getCallStatus.isInProgress
                                            ? SizedBox(
                                              height: 20.h,
                                              width: 20.w,
                                              child: const Center(
                                                child:
                                                    CircularProgressIndicator(),
                                              ),
                                            )
                                            : SizedBox(
                                              height: 20.h,
                                              width: 20.w,
                                              child: Center(
                                                child: Image.asset(
                                                  icArrowDownGreen,
                                                  height: 20.h,
                                                  width: 20.w,
                                                  fit: BoxFit.contain,
                                                ),
                                              ),
                                            ),
                                  ),
                                  SizedBox(height: 26.h),
                                  AppTextField(
                                    onTap: () {
                                      // API: fetch team list to filter beneficiaries by team.
                                      context
                                          .read<ExpectedBeneficiaryBloc>()
                                          .add(
                                            TeamStatusRequest(
                                              payload: const {},
                                            ),
                                          );
                                    },
                                    controller: teamNumberTextController,
                                    readOnly: true,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Team Number',
                                        style: TextStyle(
                                          color: kLabelTextColor,
                                          fontSize: 14.sp,
                                          fontFamily: FontConstants.interFonts,
                                        ),
                                        children: <TextSpan>[
                                          TextSpan(
                                            text: ' *',
                                            style: TextStyle(
                                              color: Colors.red,
                                              fontSize: 14.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    prefixIcon: const Icon(
                                      Icons.numbers,
                                      color: kPrimaryColor,
                                    ).paddingOnly(left: 6.0),
                                    suffixIcon:
                                        state.teamStatus.isInProgress
                                            ? SizedBox(
                                              height: 20.h,
                                              width: 20.w,
                                              child: const Center(
                                                child:
                                                    CircularProgressIndicator(),
                                              ),
                                            )
                                            : SizedBox(
                                              height: 20.h,
                                              width: 20.w,
                                              child: Center(
                                                child: Image.asset(
                                                  icArrowDownGreen,
                                                  height: 20.h,
                                                  width: 20.w,
                                                  fit: BoxFit.contain,
                                                ),
                                              ),
                                            ),
                                  ),
                                  SizedBox(height: 26.h),
                                  AppTextField(
                                    onTap: () {
                                      showModalBottomSheet(
                                        context: context,
                                        isScrollControlled: true,
                                        builder: (context) {
                                          // ✅ Create local copy to preserve previous selection
                                          Map<String, dynamic>?
                                          tempSelectedDateType = selectDateType;

                                          return StatefulBuilder(
                                            builder: (c, sheetState) {
                                              return SelectionBottomSheet<
                                                Map<String, dynamic>,
                                                int
                                              >(
                                                title: "Select Date Type",
                                                items: _dateTypeList,
                                                selectedValue:
                                                    tempSelectedDateType?["id"],
                                                valueFor:
                                                    (item) => item["id"] ?? -1,
                                                labelFor:
                                                    (item) =>
                                                        item["name"] ?? 'NA',
                                                height: 360.h,
                                                padding: EdgeInsets.only(
                                                  top: 28.h,
                                                  left: 35.w,
                                                  right: 35.w,
                                                  bottom: 60.h,
                                                ),
                                                titleTextStyle: TextStyle(
                                                  fontSize: 16.sp,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                ),
                                                titleBottomSpacing: 30.h,
                                                itemPadding:
                                                    EdgeInsets.symmetric(
                                                      vertical: 6.h,
                                                    ),
                                                itemContainerPadding:
                                                    EdgeInsets.symmetric(
                                                      vertical: 8.0.h,
                                                      horizontal: 4.0.w,
                                                    ),
                                                selectedBackgroundColor:
                                                    kPrimaryColor.withOpacity(
                                                      0.1,
                                                    ),
                                                itemTextStyle: TextStyle(
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontSize: 13.sp,
                                                  fontWeight: FontWeight.normal,
                                                  color: kBlackColor,
                                                ),
                                                selectedItemTextStyle:
                                                    TextStyle(
                                                      fontFamily:
                                                          FontConstants
                                                              .interFonts,
                                                      fontSize: 13.sp,
                                                      fontWeight:
                                                          FontWeight.w600,
                                                      color: kPrimaryColor,
                                                    ),
                                                onItemTap: (item) async {
                                                  sheetState(() {
                                                    tempSelectedDateType = item;
                                                  });

                                                  await Future.delayed(
                                                    const Duration(
                                                      milliseconds: 200,
                                                    ),
                                                  );

                                                  setState(() {
                                                    selectDateType =
                                                        tempSelectedDateType;
                                                    dateTypeTextController
                                                            .text =
                                                        selectDateType?["name"] ??
                                                        "";
                                                    dateTextController.text =
                                                        "";
                                                  });

                                                  Navigator.pop(context);
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
                                    controller: dateTypeTextController,
                                    readOnly: true,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Date Type',
                                        style: TextStyle(
                                          color: kLabelTextColor,
                                          fontSize: 14.sp,
                                          fontFamily: FontConstants.interFonts,
                                        ),
                                        children: <TextSpan>[
                                          TextSpan(
                                            text: ' *',
                                            style: TextStyle(
                                              color: Colors.red,
                                              fontSize: 14.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    prefixIcon: const Icon(
                                      Icons.calendar_today,
                                      color: kPrimaryColor,
                                    ).paddingOnly(left: 6.0),
                                    suffixIcon: SizedBox(
                                      height: 20.h,
                                      width: 20.w,
                                      child: Center(
                                        child: Image.asset(
                                          icArrowDownGreen,
                                          height: 20.h,
                                          width: 20.w,
                                          fit: BoxFit.contain,
                                        ),
                                      ),
                                    ),
                                  ),
                                  SizedBox(height: 26.h),
                                  AppTextField(
                                    onTap: () {
                                      if (selectDateType == null) {
                                        ToastManager.showAlertDialog(
                                          context,
                                          "Please select a date type first",
                                          () {
                                            Navigator.pop(context);
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
                                        //               FontConstants.interFonts,
                                        //         ),
                                        //       ),
                                        //       content: Text(
                                        //         "Please select a date type first",
                                        //         style: TextStyle(
                                        //           fontFamily:
                                        //               FontConstants.interFonts,
                                        //         ),
                                        //       ),
                                        //       actions: [
                                        //         SizedBox(
                                        //           width: 80.w,
                                        //           child: AppActiveButton(
                                        //             buttontitle: "OK",
                                        //             onTap: () {
                                        //               Navigator.pop(context);
                                        //             },
                                        //           ),
                                        //         ),
                                        //
                                        //

                                        //       ],
                                        //     );
                                        //   },
                                        // );

                                        return;
                                      }

                                      dateTypeId = selectDateType!["id"];

                                      DateTime initialDate = DateTime.now();
                                      DateTime firstDate = DateTime.now();
                                      DateTime lastDate = DateTime.now().add(
                                        const Duration(days: 10),
                                      );

                                      if (dateTypeId == 1) {
                                        // Assign Date: Allow selection from 7 days ago to today
                                        firstDate = DateTime.now().subtract(
                                          const Duration(days: 7),
                                        );
                                        lastDate = DateTime.now();
                                      } else if (dateTypeId == 2) {
                                        // Appointment Date: Allow selection from today to 10 days ahead
                                        firstDate = DateTime.now();
                                        lastDate = DateTime.now().add(
                                          const Duration(days: 10),
                                        );
                                      } else if (dateTypeId == 3) {
                                        // Renewal Date: Allow selection from today onwards
                                        firstDate = DateTime.now();
                                        lastDate = DateTime(
                                          2100,
                                          12,
                                          31,
                                        ); // Far future date
                                      }

                                      showDatePicker(
                                        context: context,
                                        initialDate: initialDate,
                                        firstDate: firstDate,
                                        lastDate: lastDate,
                                        helpText:
                                            "Select ${selectDateType!['name']}",
                                        initialEntryMode:
                                            DatePickerEntryMode.calendarOnly,
                                      ).then((value) {
                                        if (value != null) {
                                          setState(() {
                                            crrDate = value;
                                            firstDayOfWeek = crrDate.subtract(
                                              Duration(
                                                days: crrDate.weekday - 1,
                                              ),
                                            );

                                            // Format the selected date as 'dd/MM/yyyy'
                                            String formattedDate = DateFormat(
                                              "dd-MMMM-yyyy",
                                            ).format(value);

                                            // Set the selected date to the text controller
                                            dateTextController.text =
                                                formattedDate;

                                            selectedDate = formattedDate;

                                            fromDateTypeData = 1;
                                          });
                                        }
                                      });
                                    },
                                    controller: dateTextController,
                                    readOnly: true,
                                    label: RichText(
                                      text: TextSpan(
                                        text: 'Date',
                                        style: TextStyle(
                                          color: kLabelTextColor,
                                          fontSize: 14.sp,
                                          fontFamily: FontConstants.interFonts,
                                        ),
                                        children: <TextSpan>[
                                          TextSpan(
                                            text: ' *',
                                            style: TextStyle(
                                              color: Colors.red,
                                              fontSize: 14.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                    prefixIcon: const Icon(
                                      Icons.calendar_month,
                                      color: kPrimaryColor,
                                    ).paddingOnly(left: 6.0),
                                    suffixIcon: SizedBox(
                                      height: 20.h,
                                      width: 20.w,
                                      child: Center(
                                        child: Image.asset(
                                          icArrowDownGreen,
                                          height: 20.h,
                                          width: 20.w,
                                          fit: BoxFit.contain,
                                        ),
                                      ),
                                    ),
                                  ),
                                  SizedBox(height: 57.h),
                                  Row(
                                    children: [
                                      Expanded(
                                        flex: 1,
                                        child: AppButtonWithIcon(
                                          onTap: () {
                                            callStatusTextController.clear();
                                            teamNumberTextController.clear();
                                            dateTypeTextController.clear();
                                            dateTextController.clear();
                                          },
                                          title: "Clear",
                                          textStyle: TextStyle(
                                            color: kPrimaryColor,
                                            fontSize: 14.sp,
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                          buttonColor: kButtonSecondaryColor,
                                          icon: Image.asset(
                                            iconArrow,
                                            height: 24.h,
                                            width: 24.w,
                                            color: kPrimaryColor,
                                          ),
                                        ),
                                      ),
                                      SizedBox(width: 67.w),
                                      Expanded(
                                        flex: 1,
                                        child: AppButtonWithIcon(
                                          onTap: () {
                                            Navigator.pop(context);
                                            if (fromDateTypeData == 0) {
                                              // API: fetch beneficiaries using selected call status and team.
                                              context
                                                  .read<
                                                    ExpectedBeneficiaryBloc
                                                  >()
                                                  .add(
                                                    BeneficiaryRequest(
                                                      payload: {
                                                        "CallStatusID": "0",
                                                        "TeamID":
                                                            teamId.toString(),
                                                        "GroupID":
                                                            selectedCallStatus
                                                                ?.groupID
                                                                .toString() ??
                                                            "0",
                                                      },
                                                    ),
                                                  );
                                            } else {
                                              // API: fetch beneficiaries filtered by date type and selected date.
                                              context
                                                  .read<
                                                    ExpectedBeneficiaryBloc
                                                  >()
                                                  .add(
                                                    BeneficiaryRequestForDateType(
                                                      payload: {
                                                        "CallStatusID": "0",
                                                        "TeamID":
                                                            teamId.toString(),
                                                        "GroupID":
                                                            selectedCallStatus
                                                                ?.groupID
                                                                .toString() ??
                                                            "0",
                                                        "AssignDate":
                                                            selectedDate,
                                                        "CallingDateID":
                                                            dateTypeId
                                                                .toString(),
                                                      },
                                                    ),
                                                  );
                                            }
                                            // callStatusTextController.clear();
                                            // teamNumberTextController.clear();
                                            // dateTypeTextController.clear();
                                            // dateTextController.clear();

                                            setState(() {});
                                          },
                                          title: "Apply",
                                          textStyle: TextStyle(
                                            color: Colors.white,
                                            fontSize: 14.sp,
                                            fontFamily:
                                                FontConstants.interFonts,
                                          ),
                                          buttonColor: kPrimaryColor,
                                          icon: Image.asset(
                                            iconArrow,
                                            height: 24.h,
                                            width: 24.w,
                                            color: kWhiteColor,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 60.h),
                                ],
                              ),
                            ),
                          );
                        },
                      );
                    },
                  );
                },

                child: Padding(
                  padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
                  child: Image.asset(
                    icFilter,
                    height: 30.h,
                    width: 30.w,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
              SizedBox(width: 12.w),
            ],
            showActions: true,
          ),
          body: BlocBuilder<ExpectedBeneficiaryBloc, ExpectedBeneficiaryState>(
            builder: (context, state) {
              return state.beneficiaryStatus.isInProgress ||
                      state.dateTypeWiseDataStatus.isInProgress ||
                      !_hasLoadedOnce
                  ? CommonSkeletonList(
                    key: const ValueKey('skeleton'),
                  ).paddingSymmetric(horizontal: 8.w)
                  : Column(
                    children: [
                      SizedBox(height: 10.h),
                      AppTextField(
                        controller: searchTextController,
                        onChange: (value) {
                          _searchDebounce?.cancel();
                          _searchDebounce = Timer(
                            const Duration(milliseconds: 300),
                            () {
                              filterList(value);
                            },
                          );
                        },
                        hint: 'Search Worker Name/Mobile No/Area',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        inputStyle: TextStyle(
                          fontSize: 14.sp * 1.33,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 50,
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
                      ).paddingOnly(top: 6.h),
                      _filteredList.isNotEmpty
                          ? Expanded(
                            child: Padding(
                              padding: EdgeInsets.only(left: 2.w, right: 2.w),
                              child: ListView.builder(
                                shrinkWrap: true,
                                itemCount: _filteredList.length,
                                addAutomaticKeepAlives: false,
                                addRepaintBoundaries: true,
                                cacheExtent: 500,
                                itemBuilder: (context, index) {
                                  return RepaintBoundary(
                                    child: BeneficiaryCard(
                                      key: ValueKey(_filteredList[index].assignCallID),
                                      beneficiary: _filteredList[index],
                                      index: index,
                                      onAppointmentSaved: _refreshData,
                                      empCode: _cachedEmpCode ?? 0,
                                      desId: _cachedDesId ?? 0,
                                      mobileNo: _cachedMobileNo ?? 0,
                                      myOperatorUserId: _cachedMyOperatorUserId,
                                      agentId: _cachedAgentId ?? 0,
                                    ),
                                  );
                                },
                              ),
                            ),
                          )
                          : Expanded(
                            child: const NoDataFound().paddingSymmetric(
                              horizontal: 16.w,
                              vertical: 10.h,
                            ),
                          ),
                    ],
                  );
            },
          ),
        ),
      ),
    );
  }

  void filterList(String query) {
    setState(() {
      if (query.isEmpty) {
        _filteredList = beneficiaryresponsemodel!.output!;
      } else {
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
      }
    });
  }

  Future<void> _refreshData() async {
    // Reset filters and fetch fresh data
    if (fromDateTypeData == 0) {
      // API: refresh beneficiaries using current call status and team filters.
      context.read<ExpectedBeneficiaryBloc>().add(
        BeneficiaryRequest(
          payload: {
            "CallStatusID": "0",
            "TeamID": teamId.toString(),
            "GroupID": selectedCallStatus?.groupID.toString() ?? "0",
          },
        ),
      );
    } else {
      // API: refresh beneficiaries filtered by date type and selected date.
      context.read<ExpectedBeneficiaryBloc>().add(
        BeneficiaryRequestForDateType(
          payload: {
            "CallStatusID": "0",
            "TeamID": teamId.toString(),
            "GroupID": selectedCallStatus?.groupID.toString() ?? "0",
            "AssignDate": selectedDate,
            "CallingDateID": dateTypeId.toString(),
          },
        ),
      );
    }

    // Wait a bit for the request to complete
    await Future.delayed(const Duration(milliseconds: 500));
  }
}
