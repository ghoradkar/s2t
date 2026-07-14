// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/calling_modules/widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/calling_modules/models/calling_dashboard_model.dart';
import 'package:s2toperational/calling_modules/models/team_data_model.dart';
import 'package:s2toperational/calling_modules/repository/calling_dashboard_repository.dart';

class CallingDashboardController extends GetxController {
  final CallingDashboardRepository repository;

  CallingDashboardController({required this.repository});

  // ── Text controllers ───────────────────────────────────────────────────
  late final TextEditingController fromDateController;
  late final TextEditingController toDateController;
  late final TextEditingController teamController;

  // ── Date state (stored as DateTime for formatting) ─────────────────────
  DateTime _fromDate = DateTime.now();
  DateTime _toDate = DateTime.now();

  // ── State ──────────────────────────────────────────────────────────────
  bool isLoading = false;
  bool hasLoaded = false;

  CallingDashboardModel? dashboardModel;
  List<CallingDashboardOutput> dashboardItems = [];

  List<TeamDataOutput> teamList = [];
  TeamDataOutput? selectedTeam;

  int empCode = 0;

  // ── Lifecycle ──────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    fromDateController = TextEditingController();
    toDateController = TextEditingController();
    teamController = TextEditingController();

    final userData = DataProvider().getParsedUserData()?.output?[0];
    empCode = userData?.empCode ?? 0;

    final today = DateTime.now();
    _fromDate = today;
    _toDate = today;
    fromDateController.text = DateFormat('dd-MM-yyyy').format(today);
    toDateController.text = DateFormat('dd-MM-yyyy').format(today);
    teamController.text = 'All';

    selectedTeam = TeamDataOutput(teamid: 0, teamName: 'All');

    fetchDashboard();
  }

  @override
  void onClose() {
    fromDateController.dispose();
    toDateController.dispose();
    teamController.dispose();
    super.onClose();
  }

  // ── Public methods ─────────────────────────────────────────────────────

  Future<void> fetchDashboard() async {
    isLoading = true;
    update();

    try {
      final payload = {
        'FromDate': DateFormat('yyyy-MM-dd').format(_fromDate),
        'ToDate': DateFormat('yyyy-MM-dd').format(_toDate),
        'CallingExcUserID': empCode.toString(),
        'TeamId': (selectedTeam?.teamid ?? 0).toString(),
      };

      final response = await repository.getDashboardCount(payload);
      final json = jsonDecode(response.body);
      dashboardModel = CallingDashboardModel.fromJson(json);

      if (dashboardModel?.status == 'Success') {
        dashboardItems = dashboardModel?.output ?? [];
        hasLoaded = true;
      } else {
        dashboardItems = [];
        ToastManager.toast(dashboardModel?.message ?? 'No data found');
      }
    } catch (e) {
      print('CallingDashboard fetchDashboard error: $e');
      dashboardItems = [];
    } finally {
      isLoading = false;
      update();
    }
  }

  void pickFromDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _fromDate,
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      builder: (ctx, child) => Theme(
        data: ThemeData.light().copyWith(
          colorScheme: const ColorScheme.light(primary: kPrimaryColor),
        ),
        child: child!,
      ),
    );
    if (picked != null) {
      _fromDate = picked;
      fromDateController.text = DateFormat('dd-MM-yyyy').format(picked);
    }
  }

  void pickToDate(BuildContext context) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: _toDate,
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      builder: (ctx, child) => Theme(
        data: ThemeData.light().copyWith(
          colorScheme: const ColorScheme.light(primary: kPrimaryColor),
        ),
        child: child!,
      ),
    );
    if (picked != null) {
      _toDate = picked;
      toDateController.text = DateFormat('dd-MM-yyyy').format(picked);
    }
  }

  void showFilterSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (ctx) {
        final bottomPadding = MediaQuery.of(ctx).viewInsets.bottom +
            MediaQuery.of(ctx).padding.bottom;
        return Container(
          decoration: const BoxDecoration(
            color: kWhiteColor,
            borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
          ),
          padding: EdgeInsets.only(
            left: 16.w,
            right: 16.w,
            top: 12.h,
            bottom: bottomPadding + 16.h,
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // drag handle
              Center(
                child: Container(
                  width: 36.w,
                  height: 4.h,
                  decoration: BoxDecoration(
                    color: Colors.grey[300],
                    borderRadius: BorderRadius.circular(2),
                  ),
                ),
              ),
              SizedBox(height: 14.h),
              CommonText(
                text: 'Filter',
                fontSize: 16.sp,
                fontWeight: FontWeight.w600,
                textColor: kPrimaryColor,
                textAlign: TextAlign.center,
              ),
              const Divider(height: 20),
              // From Date
              AppTextField(
                controller: fromDateController,
                hint: 'From Date',
                readOnly: true,
                onTap: () => pickFromDate(ctx),
                label: CommonText(
                  text: 'From Date',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                suffixIcon: Icon(
                  Icons.calendar_today_outlined,
                  size: 18,
                  color: kLabelTextColor,
                ),
                fieldRadius: 8,
              ),
              SizedBox(height: 12.h),
              // To Date
              AppTextField(
                controller: toDateController,
                hint: 'To Date',
                readOnly: true,
                onTap: () => pickToDate(ctx),
                label: CommonText(
                  text: 'To Date',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                suffixIcon: Icon(
                  Icons.calendar_today_outlined,
                  size: 18,
                  color: kLabelTextColor,
                ),
                fieldRadius: 8,
              ),
              SizedBox(height: 12.h),
              // Team
              AppTextField(
                controller: teamController,
                hint: 'Select Team',
                readOnly: true,
                onTap: () => openTeamPicker(),
                label: CommonText(
                  text: 'Team',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                suffixIcon: Icon(
                  Icons.keyboard_arrow_down_rounded,
                  size: 22,
                  color: kLabelTextColor,
                ),
                fieldRadius: 8,
              ),
              SizedBox(height: 20.h),
              // Search button
              SizedBox(
                width: double.infinity,
                height: 48.h,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pop(ctx);
                    fetchDashboard();
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kPrimaryColor,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                  child: CommonText(
                    text: 'Search',
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kWhiteColor,
                    textAlign: TextAlign.center,
                  ),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Future<void> openTeamPicker() async {
    if (teamList.isEmpty) {
      await _loadTeams();
      if (teamList.isEmpty) return;
    }
    _showTeamSheet();
  }

  // ── Private helpers ────────────────────────────────────────────────────

  Future<void> _loadTeams() async {
    try {
      final response = await repository.getTeamData({
        'UserID': empCode.toString(),
      });
      final json = jsonDecode(response.body);
      final model = TeamDataModel.fromJson(json);
      teamList = [
        TeamDataOutput(teamid: 0, teamName: 'All'),
        ...?model.output,
      ];
    } catch (e) {
      print('CallingDashboard _loadTeams error: $e');
    }
  }

  void _showTeamSheet() {
    TeamDataOutput? tempSelected = selectedTeam;
    showModalBottomSheet(
      context: Get.context!,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (ctx) {
        return StatefulBuilder(
          builder: (_, sheetState) {
            return SelectionBottomSheet<TeamDataOutput, int>(
              title: 'Select Team',
              items: teamList,
              selectedValue: tempSelected?.teamid,
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
                            item.member1!,
                            style: TextStyle(
                              fontSize: 12.sp,
                              fontWeight: FontWeight.w500,
                              fontFamily: FontConstants.interFonts,
                              color:
                                  isSelected ? Colors.white : Colors.black87,
                            ),
                          ),
                        ),
                      if (item.member1 != null && item.member2 != null)
                        Padding(
                          padding: EdgeInsets.symmetric(horizontal: 16.w),
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
                            item.member2!,
                            style: TextStyle(
                              fontSize: 12.sp,
                              fontWeight: FontWeight.w500,
                              fontFamily: FontConstants.interFonts,
                              color:
                                  isSelected ? Colors.white : Colors.black87,
                            ),
                          ),
                        ),
                    ],
                  ),
                );
              },
              onItemTap: (item) {
                sheetState(() => tempSelected = item);
                selectedTeam = item;
                teamController.text = item.teamName ?? 'All';
                Navigator.pop(ctx);
              },
            );
          },
        );
      },
    );
  }

}
