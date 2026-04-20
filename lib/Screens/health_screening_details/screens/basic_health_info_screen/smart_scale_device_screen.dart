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
              onLeadingIconClick: () {
                Navigator.pop(context);
              },
            ),
            body: SafeArea(
              child: SingleChildScrollView(
                padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
                child: Column(
                  children: [
                    _PatientDetailsCard(patientItem: patientItem),
                    SizedBox(height: 12.h),
                    _DeviceDataCard(ctrl: ctrl),
                    SizedBox(height: 20.h),
                    _ScanButton(ctrl: ctrl),
                    SizedBox(height: 12.h),
                    Obx(() {
                      if (!ctrl.deviceFound.value) {
                        return const SizedBox.shrink();
                      }
                      return SizedBox(
                        width: double.infinity,
                        child: AppActiveButton(
                          buttontitle: 'Confirm Data',
                          onTap: () {
                            final w = ctrl.weightStr.value;
                            final b = ctrl.bmiStr.value;
                            if (w == '—' || w.isEmpty) {
                              ToastManager.toast('No weight data to confirm');
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
                      );
                    }),
                    SizedBox(height: 24.h),
                  ],
                ),
              ),
            ),
          ),
    );
  }
}

// ── Patient Details Card ──────────────────────────────────────────────────────

class _PatientDetailsCard extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  const _PatientDetailsCard({this.patientItem});

  @override
  Widget build(BuildContext context) {
    final p = patientItem;
    return _Card(
      title: 'Patient Details',
      icon: Icons.person_outline,
      child: Column(
        children: [
          _infoRow('Name', (p?.englishName ?? '').toUpperCase()),
          // _divider(),
          _infoRow('Reg. No.', p?.regdNo?.toString() ?? '—'),
        ],
      ),
    );
  }
}

// ── Device Data Card ──────────────────────────────────────────────────────────

class _DeviceDataCard extends StatelessWidget {
  final SmartScaleDeviceController ctrl;

  const _DeviceDataCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _Card(
      title: 'Device Data',
      icon: Icons.monitor_weight_outlined,
      child: Obx(
        () => Column(
          children: [
            _infoRow('Device Name', ctrl.deviceName.value),
            // _divider(),
            _infoRow('MAC Address', ctrl.macAddress.value),
            // _divider(),
            _infoRowHighlight('Weight (kg)', ctrl.weightStr.value),
            // _divider(),
            _infoRow('Height (cm)', ctrl.heightStr.value),
            // _divider(),
            _infoRowHighlight('BMI', ctrl.bmiStr.value),
            // _divider(),
            _infoRowStatus('Status', ctrl.statusStr.value),
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
        child: ElevatedButton.icon(
          onPressed:
              disabled
                  ? null
                  : () {
                    if (scanning) {
                      ctrl.stopScan();
                    } else {
                      ctrl.startScan();
                    }
                  },
          icon: Icon(
            scanning ? Icons.stop_circle_outlined : Icons.bluetooth_searching,
            size: 20.r,
          ),
          label: Text(
            scanning ? 'STOP SCAN' : 'SCAN',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              fontWeight: FontWeight.w700,
            ),
          ),
          style: ElevatedButton.styleFrom(
            backgroundColor: scanning ? Colors.red.shade600 : kPrimaryColor,
            foregroundColor: Colors.white,
            disabledBackgroundColor: Colors.grey.shade400,
            disabledForegroundColor: Colors.white,
            padding: EdgeInsets.symmetric(vertical: 14.h),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10.r),
            ),
          ),
        ),
      );
    });
  }
}

// ── Shared Card wrapper ───────────────────────────────────────────────────────

class _Card extends StatelessWidget {
  final String title;
  final IconData icon;
  final Widget child;

  const _Card({required this.title, required this.icon, required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(12.r),
                topRight: Radius.circular(12.r),
              ),
            ),
            child: Row(
              children: [
                Icon(
                  icon,
                  color: Colors.white.withValues(alpha: 0.85),
                  size: 18.r,
                ),
                SizedBox(width: 8.w),
                Text(
                  title,
                  style: TextStyle(
                    color: Colors.white,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: 14.sp,
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
            child: child,
          ),
        ],
      ),
    );
  }
}

// ── Info row helpers ──────────────────────────────────────────────────────────

Widget _infoRow(String label, String value) {
  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 7),
    child: Row(
      children: [
        SizedBox(
          width: 120,
          child: Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13,
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
              fontSize: 13,
              fontWeight: FontWeight.w500,
              color: kTextColor,
            ),
          ),
        ),
      ],
    ),
  );
}

Widget _infoRowHighlight(String label, String value) {
  final hasData = value != '—' && value.isNotEmpty;
  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 7),
    child: Row(
      children: [
        SizedBox(
          width: 120,
          child: Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13,
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
              fontSize: 14,
              fontWeight: hasData ? FontWeight.w700 : FontWeight.w500,
              color: hasData ? kPrimaryColor : kTextColor,
            ),
          ),
        ),
      ],
    ),
  );
}

Widget _infoRowStatus(String label, String value) {
  Color statusColor = kLabelTextColor;
  if (value.toLowerCase().contains('success') ||
      value.toLowerCase().contains('connected')) {
    statusColor = Colors.green.shade700;
  } else if (value.toLowerCase().contains('fail') ||
      value.toLowerCase().contains('error')) {
    statusColor = Colors.red.shade700;
  } else if (value.toLowerCase().contains('scanning') ||
      value.toLowerCase().contains('connecting') ||
      value.toLowerCase().contains('reading')) {
    statusColor = Colors.orange.shade700;
  }

  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 7),
    child: Row(
      children: [
        SizedBox(
          width: 120,
          child: Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13,
              fontWeight: FontWeight.w600,
              color: kLabelTextColor,
            ),
          ),
        ),
        Expanded(
          child: Text(
            value,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13,
              fontWeight: FontWeight.w600,
              color: statusColor,
            ),
          ),
        ),
      ],
    ),
  );
}

Widget _divider() => Divider(height: 1, color: Colors.grey.shade200);
