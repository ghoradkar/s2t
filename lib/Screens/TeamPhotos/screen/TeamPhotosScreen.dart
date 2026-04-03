// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/TeamPhotos/controller/team_photos_controller.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/AttendanceDetailsResponse.dart';
import 'package:s2toperational/Screens/TeamPhotos/model/CampListResponse.dart';

class TeamPhotosScreen extends StatelessWidget {
  const TeamPhotosScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final c = Get.put(TeamPhotosController());
    return Scaffold(
      backgroundColor: kBackground,
      appBar: AppBar(
        title: const Text('Team Photos'),
        backgroundColor: kPrimaryColor,
        foregroundColor: kWhiteColor,
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 12.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _DateField(c: c),
            SizedBox(height: 12.h),
            _CampField(c: c),
            SizedBox(height: 16.h),
            _AttendanceSection(c: c),
            SizedBox(height: 16.h),
            _PhotoSection(c: c),
          ],
        ),
      ),
    );
  }
}

// ─── Date field ────────────────────────────────────────────────────────────────

class _DateField extends StatelessWidget {
  final TeamPhotosController c;
  const _DateField({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final date = c.selectedDate.value;
      return AppTextField(
        controller: TextEditingController(text: date),
        label: const Text('Camp Date'),
        readOnly: true,
        suffixIcon: const Icon(Icons.calendar_today, color: kPrimaryColor),
        onTap: () => c.selectDate(context),
      );
    });
  }
}

// ─── Camp dropdown field ───────────────────────────────────────────────────────

class _CampField extends StatelessWidget {
  final TeamPhotosController c;
  const _CampField({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final selected = c.selectedCamp.value;
      final loading = c.isCampListLoading.value;
      return AppTextField(
        controller: TextEditingController(
          text: selected?.displayLabel ?? '',
        ),
        label: const Text('Select Camp'),
        readOnly: true,
        suffixIcon: loading
            ? Padding(
                padding: EdgeInsets.all(12.w),
                child: SizedBox(
                  width: 18.w,
                  height: 18.h,
                  child: CircularProgressIndicator(
                    strokeWidth: 2,
                    color: kPrimaryColor,
                  ),
                ),
              )
            : const Icon(Icons.keyboard_arrow_down, color: kPrimaryColor),
        onTap: () => _showCampPicker(context),
      );
    });
  }

  void _showCampPicker(BuildContext context) {
    if (c.campList.isEmpty) {
      c.loadCampList();
      return;
    }
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (_) => _CampPickerSheet(c: c),
    );
  }
}

class _CampPickerSheet extends StatelessWidget {
  final TeamPhotosController c;
  const _CampPickerSheet({required this.c});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          padding: EdgeInsets.all(16.w),
          decoration: const BoxDecoration(
            color: kPrimaryColor,
            borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
          ),
          child: Row(
            children: [
              Text(
                'Select Camp',
                style: TextStyle(
                  color: kWhiteColor,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  fontSize: 16.sp,
                ),
              ),
            ],
          ),
        ),
        Flexible(
          child: Obx(() {
            final list = c.campList;
            if (list.isEmpty) {
              return Center(
                child: Padding(
                  padding: EdgeInsets.all(24.h),
                  child: Text(
                    'No camps available for selected date',
                    style: TextStyle(
                      color: kLabelTextColor,
                      fontSize: 14.sp,
                    ),
                  ),
                ),
              );
            }
            return ListView.builder(
              shrinkWrap: true,
              itemCount: list.length,
              itemBuilder: (_, i) {
                final camp = list[i];
                return ListTile(
                  title: Text(
                    camp.displayLabel,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 14.sp,
                    ),
                  ),
                  subtitle: camp.campDate != null
                      ? Text(
                          camp.campDate!,
                          style: TextStyle(
                            color: kLabelTextColor,
                            fontSize: 12.sp,
                          ),
                        )
                      : null,
                  onTap: () {
                    Navigator.pop(context);
                    c.onCampSelected(camp);
                  },
                );
              },
            );
          }),
        ),
      ],
    );
  }
}

// ─── Attendance section ────────────────────────────────────────────────────────

class _AttendanceSection extends StatelessWidget {
  final TeamPhotosController c;
  const _AttendanceSection({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final loading = c.isAttendanceLoading.value;
      final list = c.attendanceList;
      final camp = c.selectedCamp.value;

      if (camp == null) return const SizedBox.shrink();
      if (loading) {
        return Center(
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 16.h),
            child: const CircularProgressIndicator(color: kPrimaryColor),
          ),
        );
      }
      if (list.isEmpty) return const SizedBox.shrink();

      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Team Attendance',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: 15.sp,
              color: kPrimaryColor,
            ),
          ),
          SizedBox(height: 8.h),
          Container(
            decoration: BoxDecoration(
              color: kWhiteColor,
              borderRadius: BorderRadius.circular(10),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withValues(alpha: 0.06),
                  blurRadius: 6,
                  offset: const Offset(0, 2),
                ),
              ],
            ),
            child: Column(
              children: list
                  .map((member) => _AttendanceRow(member: member))
                  .toList(),
            ),
          ),
        ],
      );
    });
  }
}

class _AttendanceRow extends StatelessWidget {
  final AttendanceDetailsOutput member;
  const _AttendanceRow({required this.member});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
      child: Row(
        children: [
          Expanded(
            flex: 2,
            child: Text(
              member.memberName ?? '-',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w500,
                fontSize: 13.sp,
              ),
            ),
          ),
          _StatusChip(
            label: member.inTime ?? 'Pending',
            isPending: member.isInPending,
            icon: Icons.login,
          ),
          SizedBox(width: 6.w),
          _StatusChip(
            label: member.outTime ?? 'Pending',
            isPending: member.isOutPending,
            icon: Icons.logout,
          ),
        ],
      ),
    );
  }
}

class _StatusChip extends StatelessWidget {
  final String label;
  final bool isPending;
  final IconData icon;
  const _StatusChip({
    required this.label,
    required this.isPending,
    required this.icon,
  });

  @override
  Widget build(BuildContext context) {
    final color = isPending ? Colors.orange : Colors.green;
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 6.w, vertical: 4.h),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.12),
        borderRadius: BorderRadius.circular(6),
        border: Border.all(color: color.withValues(alpha: 0.4)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 12.sp, color: color),
          SizedBox(width: 4.w),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 11.sp,
              color: color,
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }
}

// ─── Photo section ────────────────────────────────────────────────────────────

class _PhotoSection extends StatelessWidget {
  final TeamPhotosController c;
  const _PhotoSection({required this.c});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final camp = c.selectedCamp.value;
      if (camp == null) return const SizedBox.shrink();

      final markInOut = c.isMarkInOut.value;
      final inLocal = c.inPhotoLocalPath.value;
      final outLocal = c.outPhotoLocalPath.value;
      final inServer = c.inPhotoServerUrl.value;
      final outServer = c.outPhotoServerUrl.value;
      final uploading = c.isUploading.value;

      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Team Photos',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: 15.sp,
              color: kPrimaryColor,
            ),
          ),
          SizedBox(height: 12.h),

          // ── Check-In ──────────────────────────────────────────────────────
          _PhotoCard(
            title: 'Check-In Photo',
            isUploaded: markInOut == '1' || markInOut == '2',
            localPath: inLocal,
            serverImageName: inServer,
            onCapture: () => c.captureCheckIn(),
            onUpload: uploading ? null : () => c.uploadCheckIn(),
            uploadLabel: 'Upload Check-In Photo',
          ),

          SizedBox(height: 12.h),

          // ── Check-Out ─────────────────────────────────────────────────────
          _PhotoCard(
            title: 'Check-Out Photo',
            isUploaded: markInOut == '2',
            localPath: outLocal,
            serverImageName: outServer,
            onCapture: markInOut == '1' ? () => c.captureCheckOut() : null,
            onUpload: uploading ? null : () => c.uploadCheckOut(),
            uploadLabel: 'Upload Check-Out Photo',
          ),

          if (uploading) ...[
            SizedBox(height: 12.h),
            const Center(
              child: CircularProgressIndicator(color: kPrimaryColor),
            ),
          ],
        ],
      );
    });
  }
}

class _PhotoCard extends StatelessWidget {
  final String title;
  final bool isUploaded;
  final String localPath;
  final String serverImageName;
  final VoidCallback? onCapture;
  final VoidCallback? onUpload;
  final String uploadLabel;

  const _PhotoCard({
    required this.title,
    required this.isUploaded,
    required this.localPath,
    required this.serverImageName,
    required this.onCapture,
    required this.onUpload,
    required this.uploadLabel,
  });

  @override
  Widget build(BuildContext context) {
    final mediaBase = APIManager.kMediaBaseURL;
    final serverUrl = serverImageName.isNotEmpty
        ? '$mediaBase/CampTeamAttendance/$serverImageName'
        : null;

    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(14.w),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Text(
                title,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  fontSize: 14.sp,
                ),
              ),
              const Spacer(),
              if (isUploaded)
                Container(
                  padding: EdgeInsets.symmetric(
                    horizontal: 8.w,
                    vertical: 4.h,
                  ),
                  decoration: BoxDecoration(
                    color: Colors.green.withValues(alpha: 0.12),
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Icon(
                        Icons.check_circle_outline,
                        color: Colors.green,
                        size: 14,
                      ),
                      SizedBox(width: 4.w),
                      Text(
                        'Uploaded',
                        style: TextStyle(
                          color: Colors.green,
                          fontSize: 11.sp,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ),
            ],
          ),
          SizedBox(height: 10.h),

          // Photo preview
          if (serverUrl != null)
            _networkImage(serverUrl)
          else if (localPath.isNotEmpty)
            _localImage(localPath)
          else
            _placeholder(),

          SizedBox(height: 12.h),

          // Capture button — only when not yet uploaded
          if (!isUploaded)
            OutlinedButton.icon(
              onPressed: onCapture,
              icon: const Icon(Icons.camera_alt, size: 18),
              label: Text(
                localPath.isEmpty ? 'Capture Photo' : 'Retake Photo',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                ),
              ),
              style: OutlinedButton.styleFrom(
                foregroundColor:
                    onCapture == null ? Colors.grey : kPrimaryColor,
                side: BorderSide(
                  color: onCapture == null ? Colors.grey : kPrimaryColor,
                ),
                minimumSize: Size(double.infinity, 40.h),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
            ),

          // Upload button — when local photo captured and not yet uploaded
          if (localPath.isNotEmpty && !isUploaded) ...[
            SizedBox(height: 8.h),
            AppActiveButton(
              buttontitle: uploadLabel,
              onTap: onUpload ?? () {},
            ),
          ],
        ],
      ),
    );
  }

  Widget _networkImage(String url) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: Image.network(
        url,
        height: 180.h,
        width: double.infinity,
        fit: BoxFit.cover,
        errorBuilder: (_, __, ___) => _placeholder(),
        loadingBuilder: (_, child, progress) {
          if (progress == null) return child;
          return SizedBox(
            height: 180.h,
            child: const Center(
              child: CircularProgressIndicator(color: kPrimaryColor),
            ),
          );
        },
      ),
    );
  }

  Widget _localImage(String path) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: Image.file(
        File(path),
        height: 180.h,
        width: double.infinity,
        fit: BoxFit.cover,
      ),
    );
  }

  Widget _placeholder() {
    return Container(
      height: 180.h,
      width: double.infinity,
      decoration: BoxDecoration(
        color: kBackground,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: kTextFieldBorder),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.photo_camera_outlined, size: 40.sp, color: kLabelTextColor),
          SizedBox(height: 8.h),
          Text(
            'No photo yet',
            style: TextStyle(
              color: kLabelTextColor,
              fontSize: 13.sp,
              fontFamily: FontConstants.interFonts,
            ),
          ),
        ],
      ),
    );
  }
}
