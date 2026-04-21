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
import 'package:s2toperational/Screens/health_screening_details/controllers/smart_scale_device_controller.dart';

class SmartScaleDeviceScreen extends StatelessWidget {
  final double heightCm;
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  const SmartScaleDeviceScreen({
    super.key,
    required this.heightCm,
    this.patientItem,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<SmartScaleDeviceController>(
      init: SmartScaleDeviceController(
        heightCm: heightCm,
        patientItem: patientItem,
      ),
      dispose: (_) => Get.delete<SmartScaleDeviceController>(),
      builder:
          (ctrl) => Scaffold(
            backgroundColor: kBackground,
            appBar: mAppBar(
              scTitle: 'SmartScale Device',
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
                    _MetricsCard(ctrl: ctrl),
                    SizedBox(height: 14.h),
                    _DeviceInfoCard(ctrl: ctrl),
                    SizedBox(height: 24.h),
                    _ScanButton(ctrl: ctrl),
                    SizedBox(height: 12.h),
                    Obx(() {
                      if (!ctrl.deviceFound.value) {
                        return const SizedBox.shrink();
                      }
                      return Column(
                        children: [
                          SizedBox(
                            width: double.infinity,
                            child: AppActiveButton(
                              buttontitle: 'Confirm Data',
                              onTap: () {
                                final w = ctrl.weightStr.value;
                                final b = ctrl.bmiStr.value;
                                if (w == '—' || w.isEmpty) {
                                  ToastManager.toast(
                                    'No weight data to confirm',
                                  );
                                  return;
                                }
                                Navigator.pop(
                                  context,
                                  SmartScaleResult(
                                    weight: w,
                                    bmi: b == '—' ? '' : b,
                                    deviceNameStr: ctrl.deviceName.value,
                                  ),
                                );
                              },
                            ),
                          ),
                        ],
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
      child: Column(
        children: [
          _gridRow(
            label1: 'Name',
            value1: (p?.englishName ?? '—').toUpperCase(),
            label2: 'Reg. No.',
            value2: p?.regdNo?.toString() ?? '—',
          ),
          // SizedBox(height: 10.h),
          // _gridRow(
          //   label1: 'Gender',
          //   value1: p?.gender ?? '—',
          //   label2: 'Age',
          //   value2: p?.age != null ? '${p!.age} yrs' : '—',
          // ),
        ],
      ),
    );
  }
}

// ── Metrics Card ──────────────────────────────────────────────────────────────

class _MetricsCard extends StatelessWidget {
  final SmartScaleDeviceController ctrl;

  const _MetricsCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Measurements',
      icon: Icons.monitor_weight_outlined,
      child: Obx(() {
        final hasWeight =
            ctrl.weightStr.value != '—' && ctrl.weightStr.value.isNotEmpty;
        final hasBmi = ctrl.bmiStr.value != '—' && ctrl.bmiStr.value.isNotEmpty;

        if (!hasWeight) {
          return _MeasurementsPlaceholder(heightStr: ctrl.heightStr.value);
        }

        return Column(
          children: [
            Row(
              children: [
                Expanded(
                  child: _MetricTile(
                    label: 'Weight',
                    value: ctrl.weightStr.value,
                    unit: 'kg',
                    icon: Icons.monitor_weight_outlined,
                    active: true,
                  ),
                ),
                SizedBox(width: 12.w),
                Expanded(
                  child: _MetricTile(
                    label: 'Height',
                    value: ctrl.heightStr.value,
                    unit: 'cm',
                    icon: Icons.height_rounded,
                    active: true,
                  ),
                ),
              ],
            ),
            SizedBox(height: 12.h),
            Row(
              children: [
                Expanded(
                  child: _MetricTile(
                    label: 'BMI',
                    value: hasBmi ? ctrl.bmiStr.value : '—',
                    unit: '',
                    icon: Icons.health_and_safety_outlined,
                    active: hasBmi,
                    highlight: true,
                  ),
                ),
                SizedBox(width: 12.w),
                Expanded(child: _BmiStatusTile(ctrl: ctrl)),
              ],
            ),
          ],
        );
      }),
    );
  }
}

// ── Measurements Placeholder ──────────────────────────────────────────────────

class _MeasurementsPlaceholder extends StatelessWidget {
  final String heightStr;

  const _MeasurementsPlaceholder({required this.heightStr});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 24.h),
      decoration: BoxDecoration(
        color: Colors.grey.shade50,
        borderRadius: BorderRadius.circular(12.r),
        border: Border.all(color: Colors.grey.shade200),
      ),
      child: Column(
        children: [
          Icon(
            Icons.monitor_weight_outlined,
            size: 36.r,
            color: Colors.grey.shade400,
          ),
          SizedBox(height: 10.h),
          Text(
            'Step on the scale',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13.sp,
              fontWeight: FontWeight.w600,
              color: Colors.grey.shade500,
            ),
          ),
          SizedBox(height: 4.h),
          Text(
            'Height: $heightStr cm  •  Weight & BMI Will Appear Here',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 11.sp,
              color: Colors.grey.shade400,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }
}

// ── BMI Status Tile ───────────────────────────────────────────────────────────

class _BmiStatusTile extends StatelessWidget {
  final SmartScaleDeviceController ctrl;

  const _BmiStatusTile({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return Obx(() {
      final bmi = double.tryParse(ctrl.bmiStr.value) ?? 0;
      String label = '—';
      Color bg = Colors.grey.shade100;
      Color fg = kLabelTextColor;
      IconData icon = Icons.remove;

      if (bmi > 0) {
        if (bmi < 18.5) {
          label = 'Underweight';
          bg = Colors.orange.shade50;
          fg = Colors.orange.shade700;
          icon = Icons.arrow_downward_rounded;
        } else if (bmi < 25) {
          label = 'Normal';
          bg = Colors.green.shade50;
          fg = Colors.green.shade700;
          icon = Icons.check_circle_outline_rounded;
        } else {
          label = 'Overweight';
          bg = Colors.red.shade50;
          fg = Colors.red.shade600;
          icon = Icons.arrow_upward_rounded;
        }
      }

      return Container(
        height: 78.h,
        decoration: BoxDecoration(
          color: bg,
          borderRadius: BorderRadius.circular(12.r),
          border: Border.all(color: fg.withValues(alpha: 0.25)),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 20.r, color: fg),
            SizedBox(height: 4.h),
            Text(
              label,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                fontWeight: FontWeight.w700,
                color: fg,
              ),
            ),
            Text(
              'BMI Status',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 10.sp,
                color: fg.withValues(alpha: 0.7),
              ),
            ),
          ],
        ),
      );
    });
  }
}

// ── Device Info Card ──────────────────────────────────────────────────────────

class _DeviceInfoCard extends StatelessWidget {
  final SmartScaleDeviceController ctrl;

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
            _StatusRow(status: ctrl.statusStr.value),
          ],
        ),
      ),
    );
  }
}

// ── SCAN Button ───────────────────────────────────────────────────────────────

class _ScanButton extends StatelessWidget {
  final SmartScaleDeviceController ctrl;

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

// ── Metric Tile ───────────────────────────────────────────────────────────────

class _MetricTile extends StatelessWidget {
  final String label;
  final String value;
  final String unit;
  final IconData icon;
  final bool active;
  final bool highlight;

  const _MetricTile({
    required this.label,
    required this.value,
    required this.unit,
    required this.icon,
    required this.active,
    this.highlight = false,
  });

  @override
  Widget build(BuildContext context) {
    final Color accent =
        highlight
            ? (active ? kPrimaryColor : kLabelTextColor)
            : (active ? kPrimaryColor : kLabelTextColor);
    final Color bg =
        active
            ? (highlight
                ? kPrimaryColor.withValues(alpha: 0.07)
                : Colors.grey.shade50)
            : Colors.grey.shade50;

    return Container(
      height: 78.h,
      decoration: BoxDecoration(
        color: bg,
        borderRadius: BorderRadius.circular(12.r),
        border: Border.all(
          color: active ? accent.withValues(alpha: 0.3) : Colors.grey.shade200,
        ),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 18.r, color: active ? accent : kLabelTextColor),
          SizedBox(height: 3.h),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              Text(
                value,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: active ? 20.sp : 16.sp,
                  fontWeight: FontWeight.w800,
                  color: active ? accent : kLabelTextColor,
                ),
              ),
              if (unit.isNotEmpty && value != '—') ...[
                SizedBox(width: 2.w),
                Padding(
                  padding: EdgeInsets.only(bottom: 2.h),
                  child: Text(
                    unit,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 10.sp,
                      fontWeight: FontWeight.w500,
                      color:
                          active
                              ? accent.withValues(alpha: 0.8)
                              : kLabelTextColor,
                    ),
                  ),
                ),
              ],
            ],
          ),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
              color: kLabelTextColor,
              fontWeight: FontWeight.w500,
            ),
          ),
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
        lower.contains('found') ||
        lower.contains('step on')) {
      color = Colors.green.shade600;
      icon = Icons.check_circle_rounded;
    } else if (lower.contains('fail') ||
        lower.contains('error') ||
        lower.contains('retry')) {
      color = Colors.red.shade600;
      icon = Icons.error_outline_rounded;
    } else if (lower.contains('scanning') ||
        lower.contains('processing') ||
        lower.contains('connecting') ||
        lower.contains('ready')) {
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
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    ),
  );
}

Widget _thinDivider() =>
    Divider(height: 1, thickness: 0.8, color: Colors.grey.shade100);
