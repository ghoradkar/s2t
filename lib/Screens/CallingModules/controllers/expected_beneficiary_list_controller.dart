// ignore_for_file: avoid_print

import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/CallingModules/controllers/expected_beneficiary_controller.dart';
import 'package:s2toperational/Screens/CallingModules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/CallingModules/models/CallStatusModel.dart';
import 'package:s2toperational/Screens/CallingModules/models/team_data_model.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../custom_widgets/selection_bottom_sheet.dart';

class ExpectedBeneficiaryListController extends GetxController {
  // ── Service controller (API + status Rx fields) ────────────────────────
  late final ExpectedBeneficiaryController svc;

  // ── TextEditingControllers ─────────────────────────────────────────────
  late final TextEditingController searchTextController;
  late final TextEditingController callStatusTextController;
  late final TextEditingController teamNumberTextController;
  late final TextEditingController dateTypeTextController;
  late final TextEditingController dateTextController;

  // ── Reactive list (drives ListView rebuild) ────────────────────────────
  final filteredList = <BeneficiaryOutput>[].obs;

  // ── Filter / selection state (no Rx needed — TextControllers self-notify)
  CallStatusOutput? selectedCallStatus;
  TeamDataOutput? selectedTeamData;
  Map<String, dynamic>? selectDateType;
  int fromDateTypeData = 0;
  int teamId = 0;
  int dateTypeId = 0;
  String selectedDate = '';

  // ── Internal caches ────────────────────────────────────────────────────
  Beneficiaryresponsemodel? _beneficiaryModel;
  List<CallStatusOutput> filteredCallStatusList = [];
  final List<TeamDataOutput> filteredTeamList = [];

  // ── Search debounce ────────────────────────────────────────────────────
  Timer? _searchDebounce;

  // ── Date helpers (were global top-level vars) ──────────────────────────
  DateTime crrDate = DateTime.now();
  late DateTime firstDayOfWeek;

  // ── Cached user data ───────────────────────────────────────────────────
  int cachedEmpCode = 0;
  int cachedDesId = 0;
  int cachedMobileNo = 0;
  String? cachedMyOperatorUserId;
  int cachedAgentId = 0;

  // ── Image precache guard ───────────────────────────────────────────────
  bool imagesPrecached = false;

  // ── Static lookup data ─────────────────────────────────────────────────
  static const List<Map<String, dynamic>> dateTypeList = [
    {"id": 1, "name": "Assign Date"},
    {"id": 2, "name": "Appointment Date"},
    {"id": 3, "name": "Renewal Date"},
  ];

  // ── Lifecycle ──────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    svc = Get.find<ExpectedBeneficiaryController>();

    searchTextController = TextEditingController();
    callStatusTextController = TextEditingController();
    teamNumberTextController = TextEditingController();
    dateTypeTextController = TextEditingController();
    dateTextController = TextEditingController();

    // Cache user data once
    final userData = DataProvider().getParsedUserData()?.output?[0];
    cachedEmpCode = userData?.empCode ?? 0;
    cachedDesId = userData?.dESGID ?? 0;
    cachedMobileNo =
        int.tryParse(userData?.bMobile?.toString() ?? '') ?? 0;
    cachedMyOperatorUserId = userData?.myOperatorUserID ?? '';
    cachedAgentId =
        int.tryParse(userData?.agentID?.toString() ?? '') ?? 0;

    // ── Workers ─────────────────────────────────────────────────────────
    ever(svc.beneficiaryStatus, _onBeneficiaryStatus);
    ever(svc.dateTypeWiseDataStatus, _onDateTypeWiseStatus);
    ever(svc.getCallStatus, _onCallStatus);
    ever(svc.teamStatus, _onTeamStatus);
  }

  @override
  void onReady() {
    super.onReady();
    // Initial load — replaces addPostFrameCallback in initState
    svc.fetchBeneficiaries(
      const {'CallStatusID': '0', 'TeamID': '0', 'GroupID': '1'},
    );
  }

  @override
  void onClose() {
    _searchDebounce?.cancel();
    searchTextController.dispose();
    callStatusTextController.dispose();
    teamNumberTextController.dispose();
    dateTypeTextController.dispose();
    dateTextController.dispose();
    super.onClose();
  }

  // ── Worker handlers ────────────────────────────────────────────────────

  void _onBeneficiaryStatus(FormzSubmissionStatus status) {
    if (status.isSuccess) {
      _beneficiaryModel = Beneficiaryresponsemodel.fromJson(
        jsonDecode(svc.beneficiaryResponse.value),
      );
      filteredList.assignAll(_beneficiaryModel?.output ?? []);
      Future.delayed(const Duration(milliseconds: 300), () {});
    }
    if (status.isFailure) {
      filteredList.clear();
      try {
        final resJ = jsonDecode(svc.beneficiaryResponse.value);
        if (resJ['message'] !=
            'Assign Expected Beneficiaries List for screens details not found') {
          ToastManager.showAlertDialog(
            Get.context!,
            resJ['message'],
            () => Get.back(),
          );
        }
      } catch (e) {
        ToastManager.showAlertDialog(
          Get.context!,
          'Data Not Found.!!',
          () => Get.back(),
        );
      }
    }
  }

  void _onDateTypeWiseStatus(FormzSubmissionStatus status) {
    if (status.isSuccess) {
      _beneficiaryModel = Beneficiaryresponsemodel.fromJson(
        jsonDecode(svc.beneficiaryResponse.value),
      );
      filteredList.assignAll(_beneficiaryModel!.output!);
      Future.delayed(const Duration(milliseconds: 300), () {});
    }
    if (status.isFailure) {
      filteredList.clear();
    }
  }

  void _onCallStatus(FormzSubmissionStatus status) {
    if (status.isSuccess) {
      final model = Callstatusmodel.fromJson(
        jsonDecode(svc.getCallingResponse.value),
      );
      filteredCallStatusList = [
        CallStatusOutput(
          appointmentStatus: 'All',
          groupID: 0,
          assignStatusID: 0,
        ),
        ...model.output!,
      ];

      print('=== FILTERED LIST ===');
      for (var item in filteredCallStatusList) {
        print(
          'Item: ${item.appointmentStatus} - ID: ${item.assignStatusID}',
        );
      }
      print(
        'Currently selected: ${selectedCallStatus?.appointmentStatus}'
        ' - ID: ${selectedCallStatus?.assignStatusID}',
      );
      print('====================');

      String tempSelectedStatus =
          selectedCallStatus?.appointmentStatus ?? '';

      showModalBottomSheet(
        context: Get.context!,
        isScrollControlled: true,
        builder: (ctx) {
          return StatefulBuilder(
            builder: (c, sheetState) {
              return SelectionBottomSheet<CallStatusOutput, String>(
                title: 'Call Status',
                items: filteredCallStatusList,
                selectedValue: tempSelectedStatus,
                valueFor: (item) => item.appointmentStatus ?? '',
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
                    'Selected item: ${item.appointmentStatus}'
                    ' - ID: ${item.assignStatusID}',
                  );
                  sheetState(
                    () => tempSelectedStatus = item.appointmentStatus ?? '',
                  );
                  await Future.delayed(const Duration(milliseconds: 200));
                  selectedCallStatus = item;
                  callStatusTextController.text =
                      item.appointmentStatus ?? '';
                  teamNumberTextController.text = '';
                  teamId = 0;
                  selectedTeamData = null;
                  dateTextController.text = '';
                  dateTypeId = 0;
                  dateTypeTextController.text = '';
                  selectedDate = '';
                  fromDateTypeData = 0;
                  print(
                    'After selection - selectedCallStatus: '
                    '${selectedCallStatus?.appointmentStatus}'
                    ' - ID: ${selectedCallStatus?.assignStatusID}',
                  );
                  Navigator.pop(ctx);
                  svc.resetState();
                },
              );
            },
          );
        },
      );
    }
    if (status.isFailure) {
      ToastManager.showAlertDialog(
        Get.context!,
        svc.beneficiaryResponse.value.isEmpty
            ? 'Something Went Wrong try again'
            : svc.beneficiaryResponse.value,
        () => Get.back(),
      );
    }
  }

  void _onTeamStatus(FormzSubmissionStatus status) {
    if (status.isSuccess) {
      final model = TeamDataModel.fromJson(
        jsonDecode(svc.teamResponse.value),
      );
      filteredTeamList
        ..clear()
        ..add(TeamDataOutput(
          teamName: 'All',
          teamid: 0,
          member1: 'NA',
          member2: 'NA',
        ))
        ..addAll(model.output!);

      showModalBottomSheet(
        context: Get.context!,
        isScrollControlled: true,
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
        ),
        builder: (ctx) {
          return StatefulBuilder(
            builder: (c, sheetState) {
              return SelectionBottomSheet<TeamDataOutput, int>(
                title: 'Select Team',
                items: filteredTeamList,
                selectedValue: selectedTeamData?.teamid,
                valueFor: (item) => item.teamid ?? 0,
                labelFor: (item) => item.teamName ?? 'NA',
                height: MediaQuery.of(ctx).size.height * 0.7,
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
                      color: isSelected ? kTextOutlineColor : Colors.white,
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(
                        color: isSelected
                            ? const Color(0xFF3B5998)
                            : Colors.grey.shade300,
                        width: 1,
                      ),
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
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
                            '( ${item.teamName ?? 'NA'} )',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.white,
                              fontWeight: FontWeight.w600,
                              fontSize: 14.sp,
                              fontFamily: FontConstants.interFonts,
                            ),
                          ),
                        ),
                        if (item.member1 != null && item.member1!.isNotEmpty)
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
                                color: isSelected
                                    ? Colors.white
                                    : Colors.black87,
                              ),
                            ),
                          ),
                        if (item.member1 != null && item.member2 != null)
                          Padding(
                            padding:
                                EdgeInsets.symmetric(horizontal: 16.w),
                            child: Divider(
                              height: 1,
                              color: isSelected
                                  ? Colors.white38
                                  : Colors.grey.shade300,
                            ),
                          ),
                        if (item.member2 != null && item.member2!.isNotEmpty)
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
                                color: isSelected
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
                  sheetState(() => selectedTeamData = item);
                  if (selectedTeamData != null) {
                    teamNumberTextController.text =
                        selectedTeamData?.teamName ?? '';
                    teamId = selectedTeamData?.teamid ?? 0;
                    Navigator.pop(ctx);
                    svc.resetState();
                  }
                },
              );
            },
          );
        },
      );
    }
  }

  // ── Public business methods ────────────────────────────────────────────

  void onSearchChanged(String value) {
    _searchDebounce?.cancel();
    _searchDebounce = Timer(
      const Duration(milliseconds: 300),
      () => _filterList(value),
    );
  }

  void _filterList(String query) {
    if (_beneficiaryModel == null) return;
    if (query.isEmpty) {
      filteredList.assignAll(_beneficiaryModel!.output ?? []);
    } else {
      filteredList.assignAll(
        _beneficiaryModel!.output!
            .where(
              (item) =>
                  item.beneficiaryName!
                      .toLowerCase()
                      .contains(query.toLowerCase()) ||
                  item.area!
                      .toLowerCase()
                      .contains(query.toLowerCase()) ||
                  item.mobile!
                      .toLowerCase()
                      .contains(query.toLowerCase()),
            )
            .toList(),
      );
    }
  }

  void onDateTypeSelected(Map<String, dynamic> item) {
    selectDateType = item;
    dateTypeTextController.text = item['name'] ?? '';
    dateTextController.text = '';
    // TextEditingController notifies its own listeners — no update() needed
  }

  void onDateSelected(DateTime value) {
    crrDate = value;
    firstDayOfWeek =
        crrDate.subtract(Duration(days: crrDate.weekday - 1));
    final formattedDate = DateFormat('dd-MMMM-yyyy').format(value);
    dateTextController.text = formattedDate;
    selectedDate = formattedDate;
    fromDateTypeData = 1;
    // TextEditingController notifies its own listeners — no update() needed
  }

  void clearFilters() {
    callStatusTextController.clear();
    teamNumberTextController.clear();
    dateTypeTextController.clear();
    dateTextController.clear();
  }

  void applyFilters() {
    Get.back(); // close filter sheet
    if (fromDateTypeData == 0) {
      svc.fetchBeneficiaries({
        'CallStatusID': '0',
        'TeamID': teamId.toString(),
        'GroupID': selectedCallStatus?.groupID.toString() ?? '0',
      });
    } else {
      svc.fetchBeneficiariesByDateType({
        'CallStatusID': '0',
        'TeamID': teamId.toString(),
        'GroupID': selectedCallStatus?.groupID.toString() ?? '0',
        'AssignDate': selectedDate,
        'CallingDateID': dateTypeId.toString(),
      });
    }
  }

  Future<void> refreshData() async {
    if (fromDateTypeData == 0) {
      svc.fetchBeneficiaries({
        'CallStatusID': '0',
        'TeamID': teamId.toString(),
        'GroupID': selectedCallStatus?.groupID.toString() ?? '0',
      });
    } else {
      svc.fetchBeneficiariesByDateType({
        'CallStatusID': '0',
        'TeamID': teamId.toString(),
        'GroupID': selectedCallStatus?.groupID.toString() ?? '0',
        'AssignDate': selectedDate,
        'CallingDateID': dateTypeId.toString(),
      });
    }
    await Future.delayed(const Duration(milliseconds: 500));
  }
}
