import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/glucose_device_controller.dart';

class GlucoseDeviceScreen extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  const GlucoseDeviceScreen({super.key, this.patientItem});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<GlucoseDeviceController>(
      init: GlucoseDeviceController(patientItem: patientItem),
      dispose: (_) => Get.delete<GlucoseDeviceController>(),
      builder:
          (ctrl) => Scaffold(
            backgroundColor: kBackground,
            appBar: mAppBar(
              scTitle: 'Glucose Device',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
            ),
            body: SafeArea(
              child: SingleChildScrollView(
                padding: EdgeInsets.fromLTRB(16.w, 14.h, 16.w, 32.h),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _PatientCard(patientItem: patientItem),
                    SizedBox(height: 14.h),
                    _GlucoseReadingCard(ctrl: ctrl),
                    SizedBox(height: 14.h),
                    _DeviceInfoCard(ctrl: ctrl),
                    SizedBox(height: 24.h),
                    _ScanButton(ctrl: ctrl),
                    SizedBox(height: 12.h),
                    Obx(() {
                      if (!ctrl.dataReceived.value)
                        return const SizedBox.shrink();
                      return SizedBox(
                        width: double.infinity,
                        child: AppActiveButton(
                          buttontitle: 'Confirm Data',
                          onTap: () {
                            final g = ctrl.dataStr.value;
                            if (g == '—' || g.isEmpty) {
                              ToastManager.toast('No glucose data to confirm');
                              return;
                            }
                            Navigator.pop(
                              context,
                              GlucoseResult(
                                glucose: g,
                                deviceNameStr: ctrl.deviceName.value,
                              ),
                            );
                          },
                        ),
                      );
                    }),
                  ],
                ),
              ),
            ),
          ),
    );
  }
}

// ── Patient Card ──────────────────────────────────────────────────────────────

class _PatientCard extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  const _PatientCard({this.patientItem});

  @override
  Widget build(BuildContext context) {
    final p = patientItem;
    return _SectionCard(
      title: 'Patient Details',
      icon: Icons.person_outline_rounded,
      child: _gridRow(
        label1: 'Name',
        value1: (p?.englishName ?? '—').toUpperCase(),
        label2: 'Reg. No.',
        value2: p?.regdNo?.toString() ?? '—',
      ),
    );
  }
}

// ── Glucose Reading Card ──────────────────────────────────────────────────────

class _GlucoseReadingCard extends StatelessWidget {
  final GlucoseDeviceController ctrl;

  const _GlucoseReadingCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Glucose Reading',
      icon: Icons.biotech_outlined,
      child: Obx(() {
        final hasData =
            ctrl.dataStr.value != '—' && ctrl.dataStr.value.isNotEmpty;
        return Column(
          children: [_GlucoseTile(value: ctrl.dataStr.value, hasData: hasData)],
        );
      }),
    );
  }
}

// ── Glucose Value Tile ────────────────────────────────────────────────────────

class _GlucoseTile extends StatelessWidget {
  final String value;
  final bool hasData;

  const _GlucoseTile({required this.value, required this.hasData});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 18.h),
      decoration: BoxDecoration(
        color:
            hasData
                ? kPrimaryColor.withValues(alpha: 0.07)
                : Colors.grey.shade50,
        borderRadius: BorderRadius.circular(12.r),
        border: Border.all(
          color:
              hasData
                  ? kPrimaryColor.withValues(alpha: 0.3)
                  : Colors.grey.shade200,
        ),
      ),
      child: Column(
        children: [
          Icon(
            Icons.water_drop_outlined,
            size: 28.r,
            color: hasData ? kPrimaryColor : kLabelTextColor,
          ),
          SizedBox(height: 8.h),
          if (hasData) ...[
            _GlucoseValueText(value: value),
          ] else ...[
            Text(
              '—',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 28.sp,
                fontWeight: FontWeight.w800,
                color: kLabelTextColor,
              ),
            ),
            SizedBox(height: 2.h),
            Text(
              'No data yet',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 11.sp,
                color: kLabelTextColor,
              ),
            ),
          ],
        ],
      ),
    );
  }
}

class _GlucoseValueText extends StatelessWidget {
  final String value;

  const _GlucoseValueText({required this.value});

  @override
  Widget build(BuildContext context) {
    // "123.00 mg/dL 2025-01-01 09:30" — split on space
    final parts = value.split(' ');
    final numeric = parts.isNotEmpty ? parts[0] : value;
    final unit = parts.length > 1 ? parts[1] : '';
    final timestamp = parts.length > 2 ? parts.sublist(2).join(' ') : '';

    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            Text(
              numeric,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 32.sp,
                fontWeight: FontWeight.w800,
                color: kPrimaryColor,
              ),
            ),
            if (unit.isNotEmpty) ...[
              SizedBox(width: 4.w),
              Padding(
                padding: EdgeInsets.only(bottom: 4.h),
                child: Text(
                  unit,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    color: kPrimaryColor.withValues(alpha: 0.8),
                  ),
                ),
              ),
            ],
          ],
        ),
        if (timestamp.isNotEmpty) ...[
          SizedBox(height: 4.h),
          Text(
            timestamp,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 11.sp,
              color: kLabelTextColor,
            ),
          ),
        ],
      ],
    );
  }
}

// ── Device Info Card ──────────────────────────────────────────────────────────

class _DeviceInfoCard extends StatelessWidget {
  final GlucoseDeviceController ctrl;

  const _DeviceInfoCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Device Info',
      icon: Icons.bluetooth_rounded,
      child: Obx(
        () => Column(
          children: [
            _infoRow(Icons.device_hub_rounded, 'Device', ctrl.deviceName.value),
            _thinDivider(),
            _infoRow(
              Icons.wifi_tethering_rounded,
              'MAC Address',
              ctrl.macAddress.value,
            ),
            _thinDivider(),
            _infoRow(
              Icons.battery_std_outlined,
              'Battery',
              ctrl.batteryLevel.value,
            ),
            _thinDivider(),
            _infoRow(Icons.memory_outlined, 'Raw Data', ctrl.rawDataStr.value),
            _thinDivider(),

            _StatusRow(status: ctrl.statusStr.value),
          ],
        ),
      ),
    );
  }
}

// ── SCAN Button ───────────────────────────────────────────────────────────────

class _ScanButton extends StatelessWidget {
  final GlucoseDeviceController ctrl;

  const _ScanButton({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final scanning = ctrl.isScanning.value;
      final disabled = ctrl.scanButtonDisabled.value;

      return SizedBox(
        width: double.infinity,
        child: ElevatedButton(
          onPressed:
              disabled
                  ? null
                  : () => scanning ? ctrl.stopScan() : ctrl.startScan(),
          style: ElevatedButton.styleFrom(
            backgroundColor: scanning ? Colors.red.shade600 : kPrimaryColor,
            foregroundColor: Colors.white,
            disabledBackgroundColor: Colors.grey.shade300,
            disabledForegroundColor: Colors.grey.shade500,
            padding: EdgeInsets.symmetric(vertical: 15.h),
            elevation: disabled ? 0 : 2,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12.r),
            ),
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              if (scanning) ...[
                _PulsingDot(),
                SizedBox(width: 10.w),
              ] else ...[
                Icon(Icons.bluetooth_searching_rounded, size: 20.r),
                SizedBox(width: 8.w),
              ],
              Text(
                scanning ? 'STOP SCAN' : 'SCAN FOR DEVICE',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w700,
                  letterSpacing: 0.5,
                ),
              ),
            ],
          ),
        ),
      );
    });
  }
}

// ── Shared Section Card ───────────────────────────────────────────────────────

class _SectionCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final Widget child;

  const _SectionCard({
    required this.title,
    required this.icon,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(14.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 10,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 11.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(14.r),
                topRight: Radius.circular(14.r),
              ),
            ),
            child: Row(
              children: [
                Icon(
                  icon,
                  color: Colors.white.withValues(alpha: 0.9),
                  size: 22.r,
                ),
                SizedBox(width: 8.w),
                Text(
                  title,
                  style: TextStyle(
                    color: Colors.white,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: 14.sp,
                    letterSpacing: 0.2,
                  ),
                ),
              ],
            ),
          ),
          Padding(padding: EdgeInsets.all(14.w), child: child),
        ],
      ),
    );
  }
}

// ── Status Row ────────────────────────────────────────────────────────────────

class _StatusRow extends StatelessWidget {
  final String status;

  const _StatusRow({required this.status});

  @override
  Widget build(BuildContext context) {
    final lower = status.toLowerCase();
    Color color;
    IconData icon;

    if (lower.contains('success') ||
        lower.contains('received') ||
        lower.contains('connected') ||
        lower.contains('enabled')) {
      color = Colors.green.shade600;
      icon = Icons.check_circle_rounded;
    } else if (lower.contains('fail') ||
        lower.contains('error') ||
        lower.contains('not found') ||
        lower.contains('retry')) {
      color = Colors.red.shade600;
      icon = Icons.error_outline_rounded;
    } else if (lower.contains('scanning') ||
        lower.contains('connecting') ||
        lower.contains('reading') ||
        lower.contains('requested') ||
        lower.contains('pairing') ||
        lower.contains('pin') ||
        lower.contains('discovering')) {
      color = Colors.orange.shade600;
      icon = Icons.pending_outlined;
    } else {
      color = kLabelTextColor;
      icon = Icons.info_outline_rounded;
    }

    return Padding(
      padding: EdgeInsets.only(top: 4.h),
      child: Row(
        children: [
          Icon(icon, size: 20.r, color: color),
          SizedBox(width: 8.w),
          Expanded(
            child: Text(
              status,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 14.sp,
                fontWeight: FontWeight.w600,
                color: color,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ── Pulsing Dot (scan animation) ──────────────────────────────────────────────

class _PulsingDot extends StatefulWidget {
  @override
  State<_PulsingDot> createState() => _PulsingDotState();
}

class _PulsingDotState extends State<_PulsingDot>
    with SingleTickerProviderStateMixin {
  late final AnimationController _ctrl;
  late final Animation<double> _anim;

  @override
  void initState() {
    super.initState();
    _ctrl = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 800),
    )..repeat(reverse: true);
    _anim = Tween<double>(
      begin: 0.4,
      end: 1.0,
    ).animate(CurvedAnimation(parent: _ctrl, curve: Curves.easeInOut));
  }

  @override
  void dispose() {
    _ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _anim,
      builder:
          (context, child) => Opacity(
            opacity: _anim.value,
            child: Container(
              width: 10.r,
              height: 10.r,
              decoration: const BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
              ),
            ),
          ),
    );
  }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

Widget _gridRow({
  required String label1,
  required String value1,
  required String label2,
  required String value2,
}) {
  return Row(
    children: [
      Expanded(child: _gridCell(label1, value1)),
      SizedBox(width: 12.w),
      Expanded(child: _gridCell(label2, value2)),
    ],
  );
}

Widget _gridCell(String label, String value) {
  return Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      Text(
        label,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          color: kBlackColor,
          fontWeight: FontWeight.normal,
        ),
      ),
      SizedBox(height: 2.h),
      Text(
        value.isEmpty ? '—' : value,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          fontWeight: FontWeight.w700,
          color: kTextColor,
        ),
        maxLines: 1,
        overflow: TextOverflow.ellipsis,
      ),
    ],
  );
}

Widget _infoRow(IconData icon, String label, String value) {
  return Padding(
    padding: EdgeInsets.symmetric(vertical: 8.h),
    child: Row(
      children: [
        Icon(icon, size: 20.r, color: kPrimaryColor.withValues(alpha: 0.7)),
        SizedBox(width: 10.w),
        SizedBox(
          width: 100.w,
          child: Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              fontWeight: FontWeight.w600,
              color: kLabelTextColor,
            ),
          ),
        ),
        Expanded(
          child: Text(
            value.isEmpty ? '—' : value,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              fontWeight: FontWeight.w500,
              color: kTextColor,
            ),
            maxLines: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    ),
  );
}

Widget _thinDivider() =>
    Divider(height: 1, thickness: 0.8, color: Colors.grey.shade100);
