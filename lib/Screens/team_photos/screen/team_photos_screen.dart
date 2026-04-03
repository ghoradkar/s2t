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
    return Scaffold(
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
      body: SingleChildScrollView(
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
                            onTap: () => c.showCampDropdown(context),
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
                                  : () => _showTeamPicker(context, c),
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
            Obx(() => _AttendanceTable(list: c.attendanceList.value)),
            SizedBox(height: 14.h),

            // ── Photo cards ───────────────────────────────────────────────
            _PhotoCard(title: 'Check-In Photo', isCheckIn: true, controller: c),
            SizedBox(height: 12.h),
            _PhotoCard(
              title: 'Check-Out Photo',
              isCheckIn: false,
              controller: c,
            ),
            SizedBox(height: 16.h),

            // ── Upload / Camp Closing button ──────────────────────────────
            Obx(() {
              if (c.bothPhotosUploaded) {
                if (c.isD2DOrMMU) {
                  return AppActiveButton(
                    buttontitle: 'Camp Closing Confirmation',
                    onTap: () => _onCampClosingTap(context, c),
                  );
                }
                return const SizedBox.shrink();
              }
              return AppActiveButton(
                buttontitle:
                    c.isMarkInOut.value == '0'
                        ? 'Upload Check-In Photo'
                        : 'Upload Check-Out Photo',
                onTap:
                    c.isUploadingPhoto.value
                        ? () {}
                        : () => c.uploadPhoto(
                          isCheckIn: c.isMarkInOut.value == '0',
                        ),
              );
            }),
            SizedBox(height: 20.h),
          ],
        ),
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

  static void _showTeamPicker(BuildContext context, TeamPhotosController c) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => _PickerSheet<TeamsDetailsOutput>(
            title: 'Select Team',
            items: c.teamList,
            labelBuilder: (item) => item.displayName,
            onSelected: (item) {
              Navigator.pop(context);
              c.onTeamSelected(item);
            },
          ),
    );
  }

  static void _onCampClosingTap(BuildContext context, TeamPhotosController c) {
    _toast('Camp Closing Confirmation coming soon');
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
  final bool isCheckIn;
  final TeamPhotosController controller;

  const _PhotoCard({
    required this.title,
    required this.isCheckIn,
    required this.controller,
  });

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final serverUrl =
          isCheckIn
              ? controller.inPhotoServerUrl.value
              : controller.outPhotoServerUrl.value;
      final localPath =
          isCheckIn
              ? controller.inPhotoLocalPath.value
              : controller.outPhotoLocalPath.value;
      final uploadedOn =
          isCheckIn
              ? controller.inPhotoUploadedOn.value
              : controller.outPhotoUploadedOn.value;

      final isUploaded = serverUrl.isNotEmpty;
      final hasLocal = localPath.isNotEmpty;
      final isLocked = !isCheckIn && controller.isMarkInOut.value == '0';

      final Color accentColor =
          isCheckIn ? const Color(0xFF1565C0) : const Color(0xFF2E7D32);

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
                  child: Icon(
                    isCheckIn ? Icons.login_rounded : Icons.logout_rounded,
                    color: accentColor,
                    size: 16,
                  ),
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
                if (isUploaded)
                  _statusBadge(
                    label: 'Uploaded',
                    color: Colors.green,
                    icon: Icons.cloud_done_rounded,
                  )
                else if (isLocked)
                  _statusBadge(
                    label: 'Locked',
                    color: Colors.orange,
                    icon: Icons.lock_outline_rounded,
                  )
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

            // ── Photo preview ────────────────────────────────────────────
            GestureDetector(
              onTap:
                  isUploaded ? () => _openFullScreen(context, serverUrl) : null,
              child: Container(
                width: double.infinity,
                height: (isUploaded || hasLocal) ? 150.h : 90.h,
                decoration: BoxDecoration(
                  color: kBackground,
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(
                    color:
                        isUploaded
                            ? Colors.green.withValues(alpha: 0.4)
                            : kTextFieldBorder,
                    width: isUploaded ? 1.5 : 1,
                  ),
                ),
                clipBehavior: Clip.antiAlias,
                child: _buildPhotoWidget(
                  serverUrl: serverUrl,
                  localPath: localPath,
                ),
              ),
            ),
            SizedBox(height: 12.h),

            // ── Action row ───────────────────────────────────────────────
            if (isLocked)
              _infoRow(
                icon: Icons.lock_outline_rounded,
                text: 'Upload check-in photo first to unlock',
                color: Colors.orange,
              )
            else if (isUploaded)
              _infoRow(
                icon: Icons.access_time_rounded,
                text:
                    uploadedOn.isNotEmpty
                        ? 'Uploaded on $uploadedOn'
                        : 'Photo uploaded successfully',
                color: Colors.green,
              )
            else
              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: hasLocal ? 'Retake Photo' : 'Capture Photo',
                      onTap:
                          () => controller.capturePhoto(isCheckIn: isCheckIn),
                    ),
                  ),
                ],
              ),
          ],
        ),
      );
    });
  }

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
  }) {
    if (serverUrl.isNotEmpty) {
      return Image.network(
        serverUrl,
        fit: BoxFit.cover,
        errorBuilder: (_, __, ___) => _placeholder(),
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
    if (localPath.isNotEmpty) {
      return Image.file(File(localPath), fit: BoxFit.cover);
    }
    return _placeholder();
  }

  Widget _placeholder() {
    final bool isLocked = !isCheckIn && controller.isMarkInOut.value == '0';
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(
          isLocked ? Icons.lock_outline_rounded : Icons.camera_alt_outlined,
          color: kLabelTextColor.withValues(alpha: 0.4),
          size: 40,
        ),
        SizedBox(height: 8.h),
        Text(
          isLocked ? 'Complete check-in first' : 'No photo captured yet',
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 12.sp,
            color: kLabelTextColor.withValues(alpha: 0.6),
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

// ─── Generic picker bottom sheet ──────────────────────────────────────────────

class _PickerSheet<T> extends StatelessWidget {
  final String title;
  final List<T> items;
  final String Function(T) labelBuilder;
  final void Function(T) onSelected;

  const _PickerSheet({
    required this.title,
    required this.items,
    required this.labelBuilder,
    required this.onSelected,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: BoxConstraints(
        maxHeight: MediaQuery.of(context).size.height * 0.55,
      ),
      padding: EdgeInsets.only(
        bottom: MediaQuery.of(context).viewPadding.bottom,
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            margin: EdgeInsets.only(top: 10.h, bottom: 4.h),
            width: 40.w,
            height: 4.h,
            decoration: BoxDecoration(
              color: kTextFieldBorder,
              borderRadius: BorderRadius.circular(2),
            ),
          ),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 10.h),
            child: Text(
              title,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: 16.sp,
                color: kTextColor,
              ),
            ),
          ),
          const Divider(height: 1),
          Flexible(
            child: ListView.separated(
              shrinkWrap: true,
              itemCount: items.length,
              separatorBuilder: (_, __) => const Divider(height: 1),
              itemBuilder: (_, i) {
                final item = items[i];
                return ListTile(
                  title: Text(
                    labelBuilder(item),
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 14.sp,
                      color: kTextColor,
                    ),
                  ),
                  trailing: const Icon(
                    Icons.chevron_right,
                    color: kPrimaryColor,
                  ),
                  onTap: () => onSelected(item),
                );
              },
            ),
          ),
        ],
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
