// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/team_photos/screen/camp_closing_screen.dart';
import 'package:s2toperational/Screens/team_photos/controller/team_photos_controller.dart';
import 'package:s2toperational/Screens/team_photos/model/attendance_details_response.dart';
import 'package:s2toperational/Screens/team_photos/model/camp_list_response.dart';
import 'package:s2toperational/Screens/team_photos/model/teams_details_response.dart';

class TeamPhotosScreen extends StatefulWidget {
  final String initialCampType;

  const TeamPhotosScreen({super.key, this.initialCampType = '1'});

  @override
  State<TeamPhotosScreen> createState() => _TeamPhotosScreenState();
}

class _TeamPhotosScreenState extends State<TeamPhotosScreen> {
  late final TeamPhotosController c;

  @override
  void initState() {
    super.initState();
    Get.delete<TeamPhotosController>(force: true);
    c = Get.put(TeamPhotosController());
    c.applyCampType(widget.initialCampType);
  }

  @override
  void dispose() {
    Get.delete<TeamPhotosController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Team Photos',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Get.back(),
          showActions: true,
          actions: [
            Padding(
              padding: EdgeInsets.only(right: 12.w),
              child: GestureDetector(
                onTap: () => _showInfoDialog(context),
                child: const Icon(
                  Icons.info_outline,
                  color: kWhiteColor,
                  size: 22,
                ),
              ),
            ),
          ],
        ),
        body: RefreshIndicator(
          onRefresh: c.refreshCampImages,
          child: SingleChildScrollView(
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // ── Camp type toggle ──────────────────────────────────────────
                if (!c.hideTabToggle) ...[
                  _CampTypeToggle(controller: c),
                  SizedBox(height: 14.h),
                ],

                // ── Filter card ───────────────────────────────────────────────
                _SectionCard(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _sectionHeader(
                        icon: Icons.tune_rounded,
                        label: 'Select Filter',
                      ),
                      SizedBox(height: 12.h),
                      Row(
                        children: [
                          // Date
                          Obx(
                            () => Expanded(
                              child: AppTextField(
                                controller: TextEditingController(
                                  text: c.selectedDate.value,
                                ),
                                readOnly: true,
                                onTap: () => c.onDateChanged(context),
                                label: _label('Date'),
                                prefixIcon: Image.asset(
                                  icCalendarMonth,
                                  color: kPrimaryColor,
                                  width: 18,
                                  height: 18,
                                ).paddingOnly(left: 6.w),
                                suffixIcon: const Icon(
                                  Icons.calendar_today,
                                  size: 18,
                                  color: kPrimaryColor,
                                ),
                              ),
                            ),
                          ),
                          SizedBox(width: 10.w),

                          // Camp ID
                          Obx(
                            () => Expanded(
                              child: AppTextField(
                                controller: TextEditingController(
                                  text: c.selectedCamp.value?.campId ?? '',
                                ),
                                readOnly: true,
                                onTap: () => _showCampSheet(context, c),
                                label: _label('Camp ID'),
                                prefixIcon: const Icon(
                                  Icons.location_on_outlined,
                                  color: kPrimaryColor,
                                  size: 18,
                                ).paddingOnly(left: 6.w),
                                suffixIcon: const Icon(
                                  Icons.keyboard_arrow_down,
                                  color: kPrimaryColor,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),

                      // Team (D2D / MMU only)
                      Obx(() {
                        if (!c.isD2DOrMMU) return const SizedBox.shrink();
                        return Column(
                          children: [
                            SizedBox(height: 10.h),
                            AppTextField(
                              controller: TextEditingController(
                                text: c.selectedTeam.value?.displayName ?? '',
                              ),
                              readOnly: true,
                              onTap:
                                  c.selectedCamp.value == null
                                      ? () => _toast('Please select camp first')
                                      : c.hideTabToggle
                                      // DESGID 35: team is auto-resolved on
                                      // camp selection — no manual picker.
                                      ? () {}
                                      : () => _showTeamSheet(context, c),
                              label: _label('Team'),
                              prefixIcon: const Icon(
                                Icons.group_outlined,
                                color: kPrimaryColor,
                                size: 18,
                              ).paddingOnly(left: 6.w),
                              suffixIcon:
                                  c.isLoadingTeams.value
                                      ? const SizedBox(
                                        width: 18,
                                        height: 18,
                                        child: CircularProgressIndicator(
                                          strokeWidth: 2,
                                        ),
                                      ).paddingOnly(right: 8.w)
                                      : const Icon(
                                        Icons.keyboard_arrow_down,
                                        color: kPrimaryColor,
                                      ),
                            ),
                          ],
                        );
                      }),
                    ],
                  ),
                ),
                SizedBox(height: 14.h),

                // ── Attendance table ──────────────────────────────────────────
                Obx(() => _AttendanceTable(list: c.attendanceList.toList())),
                SizedBox(height: 14.h),

                // ── Photo cards ───────────────────────────────────────────────
                _PhotoCard(
                  title: 'Check-In Photo',
                  statusId: '1',
                  controller: c,
                ),
                SizedBox(height: 12.h),

                _PhotoCard(
                  title: 'During Camp Photo With Beneficiary',
                  statusId: '3',
                  controller: c,
                ),
                SizedBox(height: 12.h),
                _PhotoCard(
                  title: 'Check-Out Photo',
                  statusId: '2',
                  controller: c,
                ),
                SizedBox(height: 16.h),

                // ── Camp Closing button (D2D/MMU only, non-CC) ───────────────
                if (c.dESGID != 92 && c.isD2DOrMMU)
                  Obx(() {
                    if (!c.bothPhotosUploaded) return const SizedBox.shrink();
                    if (c.selectedCamp.value?.isConfirmed == true)
                      return const SizedBox.shrink();
                    return AppActiveButton(
                      buttontitle: 'Camp Closing Confirmation',
                      onTap: () {
                        if (c.checkCampClosingAllowed()) {
                          _onCampClosingTap(context, c);
                        }
                      },
                    );
                  }),
                SizedBox(height: 20.h),
              ],
            ),
          ), // SingleChildScrollView
        ), // RefreshIndicator
      ),
    );
  }

  // ─── Shared helpers ───────────────────────────────────────────────────────────

  static Widget _label(String text) => Text(
    text,
    style: TextStyle(
      color: kLabelTextColor,
      fontSize: 14.sp,
      fontFamily: FontConstants.interFonts,
    ),
  );

  static Widget _sectionHeader({
    required IconData icon,
    required String label,
  }) => Row(
    children: [
      Container(
        padding: const EdgeInsets.all(6),
        decoration: BoxDecoration(
          color: kPrimaryColor.withValues(alpha: 0.1),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Icon(icon, color: kPrimaryColor, size: 16),
      ),
      SizedBox(width: 8.w),
      Text(
        label,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontWeight: FontWeight.w600,
          fontSize: 13.sp,
          color: kPrimaryColor,
        ),
      ),
    ],
  );

  static void _toast(String msg) {
    Get.snackbar(
      '',
      msg,
      snackPosition: SnackPosition.BOTTOM,
      backgroundColor: kPrimaryColor,
      colorText: kWhiteColor,
      margin: EdgeInsets.all(12.w),
      duration: const Duration(seconds: 2),
    );
  }

  static void _showInfoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder:
          (_) => AlertDialog(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            title: Text(
              'Team Photos',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: 16.sp,
              ),
            ),
            content: Text(
              'Capture and upload the team group photo for Check-In and Check-Out. '
              'All team members must mark attendance before uploading.',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 14.sp,
                color: kTextColor,
              ),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: Text(
                  'OK',
                  style: TextStyle(
                    color: kPrimaryColor,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
            ],
          ),
    );
  }

  static Future<void> _showCampSheet(
    BuildContext context,
    TeamPhotosController c,
  ) async {
    if (c.selectedDate.value.isEmpty) {
      ToastManager.toast('Please select a date first');
      return;
    }
    if (c.campList.isEmpty) {
      ToastManager.showLoader();
      try {
        await c.fetchCampList();
      } finally {
        ToastManager.hideLoader();
      }
      if (c.campList.isEmpty) return;
    }
    if (!context.mounted) return;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<CampListOutput, String>(
            title: 'Select Camp ID',
            items: c.campList.toList(),
            valueFor: (item) => item.campId ?? '',
            labelFor: (item) => item.campId ?? '',
            selectedValue: c.selectedCamp.value?.campId,
            height: 420.h,
            padding: EdgeInsets.symmetric(horizontal: 20.w, vertical: 20.h),
            titleTextStyle: TextStyle(
              fontSize: 16.sp,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              color: kTextColor,
            ),
            titleBottomSpacing: 16.h,
            itemPadding: EdgeInsets.symmetric(vertical: 4.h),
            onItemTap: (item) {
              Navigator.pop(context);
              c.onCampSelected(item);
            },
          ),
    );
  }

  static void _showTeamSheet(BuildContext context, TeamPhotosController c) {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<TeamsDetailsOutput, String>(
            title: 'Select Team',
            items: c.teamList.toList(),
            valueFor: (item) => item.teamNumber ?? '',
            labelFor: (item) => item.displayName,
            selectedValue: c.selectedTeam.value?.teamNumber,
            height: 420.h,
            padding: EdgeInsets.symmetric(horizontal: 20.w, vertical: 20.h),
            titleTextStyle: TextStyle(
              fontSize: 16.sp,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              color: kTextColor,
            ),
            titleBottomSpacing: 16.h,
            itemPadding: EdgeInsets.symmetric(vertical: 4.h),
            onItemTap: (item) {
              Navigator.pop(context);
              c.onTeamSelected(item);
            },
          ),
    );
  }

  static void _onCampClosingTap(BuildContext context, TeamPhotosController c) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (_) => CampClosingScreen(
              campID: int.tryParse(c.selectedCamp.value?.campId ?? '') ?? 0,
              campDate: c.selectedDate.value,
              dISTLGDCODE:
                  0, // Native always passes "0" (AttendanceDetailsActivity line 573)
            ),
      ),
    );
  }
}

// ─── Camp type toggle ─────────────────────────────────────────────────────────

class _CampTypeToggle extends StatelessWidget {
  final TeamPhotosController controller;

  const _CampTypeToggle({required this.controller});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final isRegular = controller.campType.value == '1';
      final showBoth = controller.showBothTabs;

      return Container(
        height: 46.h,
        decoration: BoxDecoration(
          color: kWhiteColor,
          borderRadius: BorderRadius.circular(50),
          border: Border.all(color: kTextFieldBorder),
        ),
        child: Row(
          children: [
            if (showBoth || isRegular)
              _tab(
                label: 'Regular Camp',
                icon: icnTent,
                selected: isRegular,
                onTap: showBoth ? controller.selectRegularCamp : () {},
                leftRadius: true,
                rightRadius: showBoth ? false : true,
              ),
            if (showBoth || !isRegular)
              _tab(
                label: 'D2D Camp',
                icon: "assets/icons/home-2.png",
                selected: !isRegular,
                onTap: showBoth ? controller.selectD2DCamp : () {},
                leftRadius: showBoth ? false : true,
                rightRadius: true,
              ),
          ],
        ),
      );
    });
  }

  Widget _tab({
    required String label,
    required String icon,
    required bool selected,
    required VoidCallback onTap,
    required bool leftRadius,
    required bool rightRadius,
  }) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          height: double.infinity,
          decoration: BoxDecoration(
            color: selected ? kPrimaryColor : kWhiteColor,
            borderRadius: BorderRadius.horizontal(
              left: leftRadius ? const Radius.circular(50) : Radius.zero,
              right: rightRadius ? const Radius.circular(50) : Radius.zero,
            ),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(
                icon,
                color: selected ? kWhiteColor : kLabelTextColor,
                width: 20.w,
                height: 20.h,
              ),
              SizedBox(width: 6.w),
              Text(
                label,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                  color: selected ? kWhiteColor : kLabelTextColor,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ─── Attendance table ─────────────────────────────────────────────────────────

class _AttendanceTable extends StatelessWidget {
  final List<AttendanceDetailsOutput> list;

  const _AttendanceTable({required this.list});

  // Vertical separator widget — stretches to row height via IntrinsicHeight
  Widget _vSep(Color color) => Container(width: 1, color: color);

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // ── Section title + badge ──────────────────────────────────────
          Row(
            children: [
              Container(
                padding: const EdgeInsets.all(6),
                decoration: BoxDecoration(
                  color: kPrimaryColor.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Icon(
                  Icons.people_alt_outlined,
                  color: kPrimaryColor,
                  size: 16,
                ),
              ),
              SizedBox(width: 8.w),
              Text(
                'Team Attendance',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  fontSize: 13.sp,
                  color: kPrimaryColor,
                ),
              ),
              const Spacer(),
              if (list.isNotEmpty)
                Container(
                  padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 3.h),
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: Text(
                    '${list.length} Members',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 10.sp,
                      color: kWhiteColor,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ),
            ],
          ),
          SizedBox(height: 12.h),

          // ── Table (clipped for rounded corners) ───────────────────────
          ClipRRect(
            borderRadius: BorderRadius.circular(8),
            child: Column(
              children: [
                // ── Group header row ──────────────────────────────────
                Container(
                  color: kPrimaryColor,
                  child: IntrinsicHeight(
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        // Team column
                        Expanded(
                          flex: 3,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 8.h,
                              horizontal: 8.w,
                            ),
                            child: _headerCell('Team'),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.3)),
                        // Check In group
                        Expanded(
                          flex: 4,
                          child: Padding(
                            padding: EdgeInsets.symmetric(vertical: 8.h),
                            child: Center(child: _headerCell('Check In')),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.3)),
                        // Check Out group
                        Expanded(
                          flex: 4,
                          child: Padding(
                            padding: EdgeInsets.symmetric(vertical: 8.h),
                            child: Center(child: _headerCell('Check Out')),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),

                // ── Sub-header row ────────────────────────────────────
                Container(
                  color: kPrimaryColor.withValues(alpha: 0.82),
                  child: IntrinsicHeight(
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        // Empty team slot
                        Expanded(
                          flex: 3,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 5.h,
                              horizontal: 8.w,
                            ),
                            child: const SizedBox(),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.25)),
                        // In Time
                        Expanded(
                          flex: 2,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 5.h,
                              horizontal: 4.w,
                            ),
                            child: _headerCell('Time', sub: true),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.18)),
                        // In Dist
                        Expanded(
                          flex: 2,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 5.h,
                              horizontal: 4.w,
                            ),
                            child: _headerCell('Dist.', sub: true),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.25)),
                        // Out Time
                        Expanded(
                          flex: 2,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 5.h,
                              horizontal: 4.w,
                            ),
                            child: _headerCell('Time', sub: true),
                          ),
                        ),
                        _vSep(kWhiteColor.withValues(alpha: 0.18)),
                        // Out Dist
                        Expanded(
                          flex: 2,
                          child: Padding(
                            padding: EdgeInsets.symmetric(
                              vertical: 5.h,
                              horizontal: 4.w,
                            ),
                            child: _headerCell('Dist.', sub: true),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),

                // ── Empty state ───────────────────────────────────────
                if (list.isEmpty)
                  Container(
                    width: double.infinity,
                    color: kWhiteColor,
                    padding: EdgeInsets.symmetric(
                      vertical: 24.h,
                      horizontal: 8.w,
                    ),
                    child: Text(
                      'No attendance data',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 13.sp,
                        color: kLabelTextColor,
                      ),
                    ),
                  ),

                // ── Data rows ─────────────────────────────────────────
                ...list.asMap().entries.map((e) {
                  final isEven = e.key.isEven;
                  final m = e.value;
                  return Container(
                    color: isEven ? kBackground : kWhiteColor,
                    child: Column(
                      children: [
                        Container(
                          height: 1,
                          color: kTextFieldBorder.withValues(alpha: 0.5),
                        ),
                        IntrinsicHeight(
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: [
                              // Member name
                              Expanded(
                                flex: 3,
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 8.h,
                                    horizontal: 8.w,
                                  ),
                                  child: Text(
                                    m.memberName ?? '—',
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      fontSize: 11.sp,
                                      color: kTextColor,
                                      fontWeight: FontWeight.w500,
                                    ),
                                    maxLines: 2,
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                ),
                              ),
                              _vSep(kTextFieldBorder),
                              // Check-in Time
                              Expanded(
                                flex: 2,
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 8.h,
                                    horizontal: 4.w,
                                  ),
                                  child: _timeCell(
                                    value:
                                        m.isInPending
                                            ? 'Pending'
                                            : (m.inTime ?? '—'),
                                    isPending: m.isInPending,
                                  ),
                                ),
                              ),
                              _vSep(kTextFieldBorder),
                              // Check-in Distance
                              Expanded(
                                flex: 2,
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 8.h,
                                    horizontal: 4.w,
                                  ),
                                  child: _distanceCell(
                                    value:
                                        m.isInPending
                                            ? '—'
                                            : (m.inDistanceInKM ?? '—'),
                                  ),
                                ),
                              ),
                              _vSep(kTextFieldBorder),
                              // Check-out Time
                              Expanded(
                                flex: 2,
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 8.h,
                                    horizontal: 4.w,
                                  ),
                                  child: _timeCell(
                                    value:
                                        m.isOutPending
                                            ? 'Pending'
                                            : (m.outTime ?? '—'),
                                    isPending: m.isOutPending,
                                  ),
                                ),
                              ),
                              _vSep(kTextFieldBorder),
                              // Check-out Distance
                              Expanded(
                                flex: 2,
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 8.h,
                                    horizontal: 4.w,
                                  ),
                                  child: _distanceCell(
                                    value:
                                        m.isOutPending
                                            ? '—'
                                            : (m.outDistanceInKM ?? '—'),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  );
                }),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _headerCell(String text, {bool sub = false}) => Text(
    text,
    style: TextStyle(
      fontFamily: FontConstants.interFonts,
      fontSize: sub ? 10.sp : 12.sp,
      fontWeight: sub ? FontWeight.w400 : FontWeight.w600,
      color: kWhiteColor.withValues(alpha: sub ? 0.75 : 1.0),
    ),
    textAlign: TextAlign.start,
  );

  Widget _timeCell({required String value, required bool isPending}) {
    if (isPending) {
      return Container(
        padding: EdgeInsets.symmetric(horizontal: 5.w, vertical: 3.h),
        decoration: BoxDecoration(
          color: noteRedColor.withValues(alpha: 0.08),
          borderRadius: BorderRadius.circular(5),
          border: Border.all(color: noteRedColor.withValues(alpha: 0.35)),
        ),
        child: Text(
          'Pending',
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 9.sp,
            color: noteRedColor,
            fontWeight: FontWeight.w600,
          ),
          textAlign: TextAlign.start,
        ),
      );
    }
    return Text(
      value,
      style: TextStyle(
        fontFamily: FontConstants.interFonts,
        fontSize: 11.sp,
        fontWeight: FontWeight.w600,
        color: kTextColor,
      ),
      textAlign: TextAlign.start,
    );
  }

  Widget _distanceCell({required String value}) {
    final isEmpty = value == '—' || value.isEmpty;
    return Text(
      isEmpty ? '—' : '$value km',
      style: TextStyle(
        fontFamily: FontConstants.interFonts,
        fontSize: 10.sp,
        color:
            isEmpty ? kLabelTextColor.withValues(alpha: 0.4) : kLabelTextColor,
      ),
      textAlign: TextAlign.start,
    );
  }
}

// ─── Photo card ───────────────────────────────────────────────────────────────

class _PhotoCard extends StatelessWidget {
  final String title;

  /// "1" = check-in, "2" = check-out, "3" = during-camp
  final String statusId;
  final TeamPhotosController controller;

  const _PhotoCard({
    required this.title,
    required this.statusId,
    required this.controller,
  });

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final serverUrl =
          statusId == '1'
              ? controller.inPhotoServerUrl.value
              : statusId == '3'
              ? controller.duringPhotoServerUrl.value
              : controller.outPhotoServerUrl.value;

      final localPath =
          statusId == '1'
              ? controller.inPhotoLocalPath.value
              : statusId == '3'
              ? controller.duringPhotoLocalPath.value
              : controller.outPhotoLocalPath.value;

      final uploadedOn =
          statusId == '1'
              ? controller.inPhotoUploadedOn.value
              : statusId == '3'
              ? controller.duringPhotoUploadedOn.value
              : controller.outPhotoUploadedOn.value;

      final approvalStatus =
          statusId == '1'
              ? controller.inPhotoApprovalStatus.value
              : statusId == '3'
              ? controller.duringPhotoApprovalStatus.value
              : controller.outPhotoApprovalStatus.value;

      final isApproved = approvalStatus == 'Approved';
      final isRejected = approvalStatus == 'Rejected';
      final hasLocal = localPath.isNotEmpty;
      final isUploaded = serverUrl.isNotEmpty;

      // Cards are inactive until required selections are made:
      // Regular camp: camp must be selected
      // D2D/MMU: both camp AND team must be selected
      final isInactive =
          controller.selectedCamp.value == null ||
          (controller.isD2DOrMMU && controller.selectedTeam.value == null);

      // checkout and during-camp locked until check-in is Approved
      final isLocked =
          (statusId == '2' || statusId == '3') && !controller.isCheckInApproved;

      final isCCDesig = controller.dESGID == 92;

      final Color accentColor =
          statusId == '1'
              ? const Color(0xFF1565C0)
              : statusId == '3'
              ? const Color(0xFF6A1B9A)
              : const Color(0xFF2E7D32);

      final IconData headerIcon =
          statusId == '1'
              ? Icons.login_rounded
              : statusId == '3'
              ? Icons.photo_camera_outlined
              : Icons.logout_rounded;

      return _SectionCard(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ── Card header ─────────────────────────────────────────────
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.all(6),
                  decoration: BoxDecoration(
                    color: accentColor.withValues(alpha: 0.1),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Icon(headerIcon, color: accentColor, size: 16),
                ),
                SizedBox(width: 8.w),
                Expanded(
                  child: Text(
                    title,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      fontSize: 13.sp,
                      color: kTextColor,
                    ),
                  ),
                ),
                // Status badge
                if (isApproved)
                  _statusBadge(
                    label: 'Approved',
                    color: Colors.green,
                    icon: Icons.verified_rounded,
                  )
                else if (isRejected)
                  _statusBadge(
                    label: 'Rejected',
                    color: Colors.red,
                    icon: Icons.cancel_outlined,
                  )
                else if (isUploaded || isLocked)
                  _statusBadge(
                    label: 'Pending',
                    color: Colors.orange,
                    icon: Icons.hourglass_empty_rounded,
                  )
                // else if (isLocked)
                //   _statusBadge(
                //     label: 'Locked',
                //     color: Colors.orange,
                //     icon: Icons.lock_outline_rounded,
                //   )
                else if (hasLocal)
                  _statusBadge(
                    label: 'Ready',
                    color: Colors.blue,
                    icon: Icons.check_circle_outline_rounded,
                  )
                else
                  _statusBadge(
                    label: 'Pending',
                    color: kLabelTextColor,
                    icon: Icons.radio_button_unchecked_rounded,
                  ),
              ],
            ),
            SizedBox(height: 12.h),

            // ── Photo preview — tap to capture+upload (non-CC, non-approved) ──
            GestureDetector(
              onTap:
                  isInactive
                      ? null
                      : isCCDesig
                      ? (isUploaded
                          ? () => _openFullScreen(context, serverUrl)
                          : null)
                      : isLocked
                      ? null
                      : isApproved
                      ? () => _openFullScreen(context, serverUrl)
                      : controller.isUploadingPhoto.value
                      ? null
                      : () =>
                          controller.captureAndUploadPhoto(statusId: statusId),
              child: Container(
                width: double.infinity,
                height: 150.h,
                decoration: BoxDecoration(
                  color: kBackground,
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(
                    color:
                        isInactive
                            ? kTextFieldBorder
                            : isApproved
                            ? Colors.green.withValues(alpha: 0.5)
                            : isRejected
                            ? Colors.red.withValues(alpha: 0.4)
                            : isUploaded
                            ? Colors.orange.withValues(alpha: 0.4)
                            : (!isCCDesig && !isLocked)
                            ? kPrimaryColor.withValues(alpha: 0.35)
                            : kTextFieldBorder,
                    width: (isUploaded || isApproved || isRejected) ? 1.5 : 1,
                  ),
                ),
                clipBehavior: Clip.antiAlias,
                child: _buildPhotoWidget(
                  serverUrl: serverUrl,
                  localPath: localPath,
                  isLocked: isLocked,
                  isTappable:
                      !isInactive && !isCCDesig && !isLocked && !isApproved,
                  isInactive: isInactive,
                ),
              ),
            ),
            SizedBox(height: 12.h),

            // ── Action row ───────────────────────────────────────────────
            if (isCCDesig)
              _buildCCActionRow(
                context: context,
                isInactive: isInactive,
                serverUrl: serverUrl,
                isApproved: isApproved,
                isRejected: isRejected,
                uploadedOn: uploadedOn,
              )
            else
              _buildUserActionRow(
                context: context,
                isInactive: isInactive,
                isLocked: isLocked,
                isApproved: isApproved,
                isRejected: isRejected,
                isUploaded: isUploaded,
                uploadedOn: uploadedOn,
              ),
          ],
        ),
      );
    });
  }

  // Action row for field team (non-CC)
  Widget _buildUserActionRow({
    required BuildContext context,
    required bool isInactive,
    required bool isLocked,
    required bool isApproved,
    required bool isRejected,
    required bool isUploaded,
    required String uploadedOn,
  }) {
    // 0. Inactive — camp or team not yet selected
    if (isInactive) {
      return _infoRow(
        icon: Icons.touch_app_outlined,
        text: 'Select camp & team to continue',
        color: kLabelTextColor,
      );
    }

    // 1. Locked — check-in not yet approved
    if (isLocked) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _infoRow(
            icon: Icons.lock_outline_rounded,
            text: 'Check-in photo must be approved first',
            color: Colors.orange,
          ),
          SizedBox(height: 8.h),
          GestureDetector(
            onTap: () => controller.refreshCampImages(),
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 6.h),
              decoration: BoxDecoration(
                color: Colors.orange.withValues(alpha: 0.1),
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: Colors.orange.withValues(alpha: 0.4)),
              ),
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.refresh_rounded, size: 14, color: Colors.orange),
                  SizedBox(width: 4.w),
                  Text(
                    'Refresh Status',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 11.sp,
                      color: Colors.orange,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      );
    }

    final List<Widget> rows = [];

    // Upload timestamp
    if (isUploaded && uploadedOn.isNotEmpty) {
      final Color tsColor =
          isApproved
              ? Colors.green
              : isRejected
              ? Colors.red
              : Colors.orange;
      rows.add(
        _infoRow(
          icon: Icons.access_time_rounded,
          text: 'Uploaded on $uploadedOn',
          color: tsColor,
        ),
      );
      if (rows.isNotEmpty) rows.add(SizedBox(height: 6.h));
    }

    if (isApproved) {
      rows.add(
        _infoRow(
          icon: Icons.verified_rounded,
          text: 'Photo Approved',
          color: Colors.green,
        ),
      );
    } else if (isRejected) {
      rows.add(
        _infoRow(
          icon: Icons.cancel_outlined,
          text: _rejectionText,
          color: Colors.red,
        ),
      );
      rows.add(SizedBox(height: 4.h));
      rows.add(
        _infoRow(
          icon: Icons.touch_app_outlined,
          text: 'Tap photo to re-capture and upload',
          color: Colors.red.withValues(alpha: 0.75),
        ),
      );
    } else if (isUploaded) {
      rows.add(
        _infoRow(
          icon: Icons.hourglass_empty_rounded,
          text: 'Waiting for approval',
          color: Colors.orange,
        ),
      );
    } else {
      rows.add(
        _infoRow(
          icon: Icons.touch_app_outlined,
          text: 'Tap photo to capture and upload',
          color: kPrimaryColor,
        ),
      );
    }

    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: rows);
  }

  // Action row for CC / manager (DESGID 92)
  Widget _buildCCActionRow({
    required BuildContext context,
    required bool isInactive,
    required String serverUrl,
    required bool isApproved,
    required bool isRejected,
    required String uploadedOn,
  }) {
    // Photo approved — hide buttons entirely, show confirmed status only
    if (isApproved) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (uploadedOn.isNotEmpty) ...[
            _infoRow(
              icon: Icons.access_time_rounded,
              text: 'Uploaded on $uploadedOn',
              color: Colors.green,
            ),
            SizedBox(height: 6.h),
          ],
          _infoRow(
            icon: Icons.verified_rounded,
            text: 'फोटो approve करण्यात आलेला आहे.',
            color: Colors.green,
          ),
        ],
      );
    }

    // Buttons are active only when photo is uploaded and pending (not yet reviewed)
    final bool isPending = !isInactive && serverUrl.isNotEmpty && !isRejected;
    final List<Widget> rows = [];

    if (serverUrl.isEmpty) {
      // No photo yet — show info text then disabled buttons
      rows.add(
        _infoRow(
          icon: Icons.info_outline_rounded,
          text: 'टीमकडून फोटो अजून अपलोड झालेला नाही.',
          color: kLabelTextColor,
        ),
      );
      rows.add(SizedBox(height: 8.h));
    } else {
      // Photo exists — show timestamp
      if (uploadedOn.isNotEmpty) {
        rows.add(
          _infoRow(
            icon: Icons.access_time_rounded,
            text: 'Uploaded on $uploadedOn',
            color: isRejected ? Colors.red : Colors.orange,
          ),
        );
        rows.add(SizedBox(height: 6.h));
      }
      if (isRejected) {
        // Rejected — waiting for phlebo to re-upload
        rows.add(
          _infoRow(
            icon: Icons.cancel_outlined,
            text: 'फोटो reject करण्यात आलेला आहे.',
            color: Colors.red,
          ),
        );
        rows.add(SizedBox(height: 8.h));
      }
    }

    rows.add(_approveRejectButtons(enabled: isPending));
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: rows);
  }

  Widget _approveRejectButtons({required bool enabled}) {
    return Opacity(
      opacity: enabled ? 1.0 : 0.4,
      child: Row(
        children: [
          Expanded(
            child: GestureDetector(
              onTap:
                  enabled
                      ? () => controller.approveRejectPhoto(
                        statusId: statusId,
                        approvalStatusId: '1',
                      )
                      : null,
              child: Container(
                height: 40.h,
                decoration: BoxDecoration(
                  color: Colors.green,
                  borderRadius: BorderRadius.circular(8),
                ),
                alignment: Alignment.center,
                child: Text(
                  'Approve',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    color: kWhiteColor,
                  ),
                ),
              ),
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: GestureDetector(
              onTap:
                  enabled
                      ? () => controller.approveRejectPhoto(
                        statusId: statusId,
                        approvalStatusId: '2',
                      )
                      : null,
              child: Container(
                height: 40.h,
                decoration: BoxDecoration(
                  color: Colors.red,
                  borderRadius: BorderRadius.circular(8),
                ),
                alignment: Alignment.center,
                child: Text(
                  'Reject',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    color: kWhiteColor,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  String get _rejectionText => switch (statusId) {
    '1' => 'तुमचा चेक इन फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
    '3' => 'तुमचा कॅम्प फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
    _ => 'तुमचा चेक आऊट फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
  };

  Widget _statusBadge({
    required String label,
    required Color color,
    required IconData icon,
  }) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 3.h),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(color: color.withValues(alpha: 0.35)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 11, color: color),
          SizedBox(width: 3.w),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
              color: color,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
    );
  }

  Widget _infoRow({
    required IconData icon,
    required String text,
    required Color color,
  }) {
    return Row(
      children: [
        Icon(icon, size: 14, color: color),
        SizedBox(width: 6.w),
        Expanded(
          child: Text(
            text,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 11.sp,
              color: color,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildPhotoWidget({
    required String serverUrl,
    required String localPath,
    required bool isLocked,
    required bool isTappable,
    required bool isInactive,
  }) {
    // Local path means photo was just captured and is uploading
    if (localPath.isNotEmpty) {
      return Stack(
        fit: StackFit.expand,
        children: [
          Image.file(File(localPath), fit: BoxFit.cover),
          Container(
            color: Colors.black.withValues(alpha: 0.45),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const CircularProgressIndicator(
                  color: Colors.white,
                  strokeWidth: 2,
                ),
                SizedBox(height: 8.h),
                Text(
                  'Uploading...',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 12.sp,
                    color: Colors.white,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ],
            ),
          ),
        ],
      );
    }
    if (!isInactive && serverUrl.isNotEmpty) {
      return Image.network(
        serverUrl,
        fit: BoxFit.cover,
        errorBuilder:
            (_, __, ___) => _placeholder(
              isLocked: isLocked,
              isTappable: isTappable,
              isInactive: isInactive,
            ),
        loadingBuilder: (_, child, progress) {
          if (progress == null) return child;
          return Center(
            child: CircularProgressIndicator(
              value:
                  progress.expectedTotalBytes != null
                      ? progress.cumulativeBytesLoaded /
                          progress.expectedTotalBytes!
                      : null,
              color: kPrimaryColor,
              strokeWidth: 2,
            ),
          );
        },
      );
    }
    return _placeholder(
      isLocked: isLocked,
      isTappable: isTappable,
      isInactive: isInactive,
    );
  }

  Widget _placeholder({
    required bool isLocked,
    required bool isTappable,
    required bool isInactive,
  }) {
    final IconData icon =
        isInactive
            ? Icons.camera_alt_outlined
            : isLocked
            ? Icons.lock_outline_rounded
            : isTappable
            ? Icons.camera_alt_rounded
            : Icons.camera_alt_outlined;
    final String text =
        isInactive
            ? 'No photo yet'
            : isLocked
            ? 'Complete check-in first'
            : isTappable
            ? 'Tap here to capture photo'
            : 'No photo captured yet';
    final Color color =
        isTappable && !isLocked
            ? kPrimaryColor.withValues(alpha: 0.5)
            : kLabelTextColor.withValues(alpha: 0.4);

    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(icon, color: color, size: 42),
        SizedBox(height: 8.h),
        Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 12.sp,
            color: color,
            fontWeight: isTappable ? FontWeight.w600 : FontWeight.w400,
          ),
        ),
      ],
    );
  }

  void _openFullScreen(BuildContext context, String url) {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => _FullScreenImageView(imageUrl: url)),
    );
  }
}

// ─── Full screen image viewer ─────────────────────────────────────────────────

class _FullScreenImageView extends StatelessWidget {
  final String imageUrl;

  const _FullScreenImageView({required this.imageUrl});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        iconTheme: const IconThemeData(color: Colors.white),
      ),
      body: Center(
        child: InteractiveViewer(
          child: Image.network(
            imageUrl,
            fit: BoxFit.contain,
            errorBuilder:
                (_, __, ___) => const Icon(
                  Icons.broken_image,
                  color: Colors.white,
                  size: 64,
                ),
          ),
        ),
      ),
    );
  }
}

// ─── Section card ─────────────────────────────────────────────────────────────

class _SectionCard extends StatelessWidget {
  final Widget child;

  const _SectionCard({required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(14.w),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 2),
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 10,
            spreadRadius: 0,
          ),
        ],
      ),
      child: child,
    );
  }
}
