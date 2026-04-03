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
  /// Pass "1" for Regular, "3" for D2D — matches the dashboard selection.
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
    // Always delete any cached instance so we start fresh every visit.
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
              child: Icon(Icons.info_outline, color: kWhiteColor, size: 22),
            ),
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ── Camp type toggle ──────────────────────────────────────────────
            if (!c.hideTabToggle) ...[
              _CampTypeToggle(controller: c),
              SizedBox(height: 14.h),
            ],

            // ── Filter card ───────────────────────────────────────────────────
            _SectionCard(
              child: Column(
                children: [
                  // Date
                  Obx(
                    () => AppTextField(
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
                  SizedBox(height: 10.h),

                  // Camp ID
                  Obx(
                    () => AppTextField(
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
                  SizedBox(height: 10.h),

                  // Team (D2D / MMU only)
                  Obx(() {
                    if (!c.isD2DOrMMU) return const SizedBox.shrink();
                    return Column(
                      children: [
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
                          suffixIcon: c.isLoadingTeams.value
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
                        SizedBox(height: 10.h),
                      ],
                    );
                  }),
                ],
              ),
            ),
            SizedBox(height: 14.h),

            // ── Attendance table — always visible ─────────────────────────────
            Obx(() => _AttendanceTable(list: c.attendanceList.value)),
            SizedBox(height: 14.h),

            // ── Photo cards — always visible ──────────────────────────────────
            _PhotoCard(
              title: 'Team Photo — Check In',
              isCheckIn: true,
              controller: c,
            ),
            SizedBox(height: 12.h),
            _PhotoCard(
              title: 'Team Photo — Check Out',
              isCheckIn: false,
              controller: c,
            ),
            SizedBox(height: 16.h),

            // Upload button
            Obx(() {
              if (c.bothPhotosUploaded) {
                // Camp closing confirmation (D2D only)
                if (c.isD2DOrMMU) {
                  return AppActiveButton(
                    buttontitle: 'Camp Closing Confirmation',
                    onTap: () => _onCampClosingTap(context, c),
                  );
                }
                return const SizedBox.shrink();
              }
              return AppActiveButton(
                buttontitle: c.isMarkInOut.value == '0'
                    ? 'Upload Check-In Photo'
                    : 'Upload Check-Out Photo',
                onTap: c.isUploadingPhoto.value
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

  // ─── Helpers ──────────────────────────────────────────────────────────────────

  static Widget _label(String text) => Text(
        text,
        style: TextStyle(
          color: kLabelTextColor,
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        ),
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
      builder: (_) => AlertDialog(
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

  static void _showCampPicker(
    BuildContext context,
    TeamPhotosController c,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) => _PickerSheet<CampListOutput>(
        title: 'Select Camp',
        items: c.campList,
        labelBuilder: (item) => 'Camp ID: ${item.campId}',
        onSelected: (item) {
          Navigator.pop(context);
          c.onCampSelected(item);
        },
      ),
    );
  }

  static void _showTeamPicker(
    BuildContext context,
    TeamPhotosController c,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) => _PickerSheet<TeamsDetailsOutput>(
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

  static void _onCampClosingTap(
    BuildContext context,
    TeamPhotosController c,
  ) {
    // TODO: Navigate to CampClosingConfirmationScreen when built
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
            // Regular tab: always visible when showBoth; hidden when single-tab and D2D is active
            if (showBoth || isRegular)
              _tab(
                label: 'Regular Camp',
                icon: icnTent,
                selected: isRegular,
                onTap: showBoth ? controller.selectRegularCamp : () {},
                leftRadius: true,
                rightRadius: showBoth ? false : true, // full radius when alone
              ),
            // D2D tab: always visible when showBoth; hidden when single-tab and Regular is active
            if (showBoth || !isRegular)
              _tab(
                label: 'D2D Camp',
                icon: "assets/icons/home-2.png",
                selected: !isRegular,
                onTap: showBoth ? controller.selectD2DCamp : () {},
                leftRadius: showBoth ? false : true, // full radius when alone
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
              Image.asset(icon, color: selected ? kWhiteColor : kLabelTextColor, width: 20.w, height: 20.h),
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

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Team Attendance',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: 14.sp,
              color: kPrimaryColor,
            ),
          ),
          SizedBox(height: 10.h),
          // Header row
          Container(
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.circular(6),
            ),
            padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
            child: Row(
              children: [
                Expanded(flex: 3, child: _headerCell('Member')),
                Expanded(
                  flex: 3,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _headerCell('Check In'),
                      SizedBox(height: 2.h),
                      _headerCell('Time | Distance', sub: true),
                    ],
                  ),
                ),
                Expanded(
                  flex: 3,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _headerCell('Check Out'),
                      SizedBox(height: 2.h),
                      _headerCell('Time | Distance', sub: true),
                    ],
                  ),
                ),
              ],
            ),
          ),
          // Empty state
          if (list.isEmpty)
            Container(
              color: kBackground,
              width: double.infinity,
              padding: EdgeInsets.symmetric(vertical: 14.h, horizontal: 8.w),
              child: Center(
                child: Text(
                  'No attendance data',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 13.sp,
                    color: kLabelTextColor,
                  ),
                ),
              ),
            ),
          // Data rows
          ...list.asMap().entries.map((e) {
            final isEven = e.key.isEven;
            final m = e.value;
            return Container(
              color: isEven ? kBackground : kWhiteColor,
              padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
              child: Row(
                children: [
                  Expanded(
                    flex: 3,
                    child: Text(
                      m.memberName ?? '—',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 12.sp,
                        color: kTextColor,
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: _timeDistanceCell(
                      time: m.isInPending ? 'Pending' : (m.inTime ?? '—'),
                      distance: m.inDistanceInKM,
                      isPending: m.isInPending,
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: _timeDistanceCell(
                      time: m.isOutPending ? 'Pending' : (m.outTime ?? '—'),
                      distance: m.outDistanceInKM,
                      isPending: m.isOutPending,
                    ),
                  ),
                ],
              ),
            );
          }),
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
      );

  Widget _timeDistanceCell({
    required String time,
    required String? distance,
    required bool isPending,
  }) {
    if (isPending) {
      return Container(
        padding: EdgeInsets.symmetric(horizontal: 6.w, vertical: 2.h),
        decoration: BoxDecoration(
          color: noteRedColor.withValues(alpha: 0.1),
          borderRadius: BorderRadius.circular(4),
          border: Border.all(color: noteRedColor.withValues(alpha: 0.4)),
        ),
        child: Text(
          'Pending',
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 10.sp,
            color: noteRedColor,
            fontWeight: FontWeight.w600,
          ),
        ),
      );
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          time,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 11.sp,
            fontWeight: FontWeight.w600,
            color: kTextColor,
          ),
        ),
        if (distance != null && distance.isNotEmpty)
          Text(
            '$distance km',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
              color: kLabelTextColor,
            ),
          ),
      ],
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
      final serverUrl = isCheckIn
          ? controller.inPhotoServerUrl.value
          : controller.outPhotoServerUrl.value;
      final localPath = isCheckIn
          ? controller.inPhotoLocalPath.value
          : controller.outPhotoLocalPath.value;
      final uploadedOn = isCheckIn
          ? controller.inPhotoUploadedOn.value
          : controller.outPhotoUploadedOn.value;

      final isUploaded = serverUrl.isNotEmpty;
      final hasLocal = localPath.isNotEmpty;

      // Lock check-out until check-in is done
      final isLocked = !isCheckIn && controller.isMarkInOut.value == '0';

      return _SectionCard(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Container(
                  width: 4,
                  height: 16,
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(2),
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
                if (isUploaded)
                  Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: 8.w,
                      vertical: 3.h,
                    ),
                    decoration: BoxDecoration(
                      color: Colors.green.withValues(alpha: 0.1),
                      borderRadius: BorderRadius.circular(4),
                      border: Border.all(
                        color: Colors.green.withValues(alpha: 0.4),
                      ),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(
                          Icons.check_circle_outline,
                          color: Colors.green,
                          size: 12,
                        ),
                        SizedBox(width: 4.w),
                        Text(
                          'Uploaded',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 10.sp,
                            color: Colors.green,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ],
                    ),
                  ),
              ],
            ),
            SizedBox(height: 12.h),
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Photo preview
                GestureDetector(
                  onTap: isUploaded
                      ? () => _openFullScreen(context, serverUrl)
                      : null,
                  child: Container(
                    width: 80.w,
                    height: 80.w,
                    decoration: BoxDecoration(
                      color: kBackground,
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color: kTextFieldBorder),
                    ),
                    clipBehavior: Clip.antiAlias,
                    child: _buildPhotoWidget(
                      serverUrl: serverUrl,
                      localPath: localPath,
                    ),
                  ),
                ),
                SizedBox(width: 14.w),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      if (isLocked)
                        Text(
                          'Upload check-in photo first',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 12.sp,
                            color: kLabelTextColor,
                          ),
                        )
                      else if (!isUploaded && controller.canCapturePhoto)
                        AppActiveButton(
                          buttontitle: hasLocal ? 'Retake Photo' : 'Capture Photo',
                          onTap: () => controller.capturePhoto(
                            isCheckIn: isCheckIn,
                          ),
                        )
                      else
                        const SizedBox.shrink(),
                      if (uploadedOn.isNotEmpty) ...[
                        SizedBox(height: 8.h),
                        Row(
                          children: [
                            const Icon(
                              Icons.access_time,
                              size: 12,
                              color: kLabelTextColor,
                            ),
                            SizedBox(width: 4.w),
                            Expanded(
                              child: Text(
                                uploadedOn,
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: 11.sp,
                                  color: kLabelTextColor,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ],
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
      );
    });
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
          return const Center(child: CircularProgressIndicator(strokeWidth: 2));
        },
      );
    }
    if (localPath.isNotEmpty) {
      return Image.file(File(localPath), fit: BoxFit.cover);
    }
    return _placeholder();
  }

  Widget _placeholder() => Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.camera_alt_outlined, color: kLabelTextColor, size: 28),
          SizedBox(height: 4.h),
          Text(
            'No photo',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
              color: kLabelTextColor,
            ),
          ),
        ],
      );

  void _openFullScreen(BuildContext context, String url) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => _FullScreenImageView(imageUrl: url),
      ),
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
            errorBuilder: (_, __, ___) => const Icon(
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
          // Handle
          Container(
            margin: EdgeInsets.only(top: 10.h, bottom: 4.h),
            width: 40.w,
            height: 4.h,
            decoration: BoxDecoration(
              color: kTextFieldBorder,
              borderRadius: BorderRadius.circular(2),
            ),
          ),
          // Title
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
          // List
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
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.08),
            blurRadius: 8,
          ),
        ],
      ),
      child: child,
    );
  }
}
