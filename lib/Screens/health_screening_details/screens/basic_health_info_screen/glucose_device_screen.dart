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
                padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
                child: Column(
                  children: [
                    _PatientDetailsCard(patientItem: patientItem),
                    SizedBox(height: 12.h),
                    _DeviceDataCard(ctrl: ctrl),
                    SizedBox(height: 20.h),
                    _ScanButton(ctrl: ctrl, context: context),
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
          // _divider(),
          // _infoRow('Mobile', p?.mobileNo ?? '—'),
          // _divider(),
          // _infoRow('Gender', p?.gender ?? '—'),
          // _divider(),
          // _infoRow('Age', p?.age != null ? '${p!.age} yrs' : '—'),
        ],
      ),
    );
  }
}

// ── Device Data Card ──────────────────────────────────────────────────────────

class _DeviceDataCard extends StatelessWidget {
  final GlucoseDeviceController ctrl;

  const _DeviceDataCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _Card(
      title: 'Device Data',
      icon: Icons.biotech_outlined,
      child: Obx(
        () => Column(
          children: [
            _infoRow('Device Name', ctrl.deviceName.value),
            // _divider(),
            _infoRow('MAC Address', ctrl.macAddress.value),
            // _divider(),
            _infoRow('Battery Level', ctrl.batteryLevel.value),
            // _divider(),
            _infoRowStatus('Status', ctrl.statusStr.value),
            // _divider(),
            _infoRowHighlight('Data', ctrl.dataStr.value),
            // _divider(),
            _infoRow('Raw Data', ctrl.rawDataStr.value),
          ],
        ),
      ),
    );
  }
}

// ── SCAN Button ───────────────────────────────────────────────────────────────

class _ScanButton extends StatelessWidget {
  final GlucoseDeviceController ctrl;
  final BuildContext context;

  const _ScanButton({required this.ctrl, required this.context});

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
                      // _showPairingBottomSheet(context, ctrl);
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

  // void _showPairingBottomSheet(BuildContext context, GlucoseDeviceController ctrl) {
  //   showModalBottomSheet(
  //     context: context,
  //     shape: RoundedRectangleBorder(
  //       borderRadius: BorderRadius.vertical(top: Radius.circular(16.r)),
  //     ),
  //     backgroundColor: kWhiteColor,
  //     builder: (ctx) => Padding(
  //       padding: EdgeInsets.fromLTRB(20.w, 20.h, 20.w, 32.h),
  //       child: Column(
  //         mainAxisSize: MainAxisSize.min,
  //         crossAxisAlignment: CrossAxisAlignment.start,
  //         children: [
  //           Row(
  //             children: [
  //               Icon(Icons.bluetooth_searching, color: kPrimaryColor, size: 22.r),
  //               SizedBox(width: 10.w),
  //               Text(
  //                 'Pairing Code',
  //                 style: TextStyle(
  //                   fontFamily: FontConstants.interFonts,
  //                   fontSize: 16.sp,
  //                   fontWeight: FontWeight.w700,
  //                   color: kTextColor,
  //                 ),
  //               ),
  //             ],
  //           ),
  //           SizedBox(height: 12.h),
  //           Container(
  //             width: double.infinity,
  //             padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
  //             decoration: BoxDecoration(
  //               color: Colors.blue.shade50,
  //               borderRadius: BorderRadius.circular(8.r),
  //               border: Border.all(color: Colors.blue.shade200),
  //             ),
  //             child: Column(
  //               crossAxisAlignment: CrossAxisAlignment.start,
  //               children: [
  //                 Row(
  //                   children: [
  //                     Icon(Icons.info_outline, color: Colors.blue.shade700, size: 16.r),
  //                     SizedBox(width: 6.w),
  //                     Text(
  //                       'How to pair:',
  //                       style: TextStyle(
  //                         fontFamily: FontConstants.interFonts,
  //                         fontSize: 13.sp,
  //                         fontWeight: FontWeight.w700,
  //                         color: Colors.blue.shade700,
  //                       ),
  //                     ),
  //                   ],
  //                 ),
  //                 SizedBox(height: 6.h),
  //                 Text(
  //                   '1. Make sure your Glucose Meter is turned ON.\n'
  //                   '2. Tap "Start Scan" below to begin scanning.\n'
  //                   '3. When a pairing PIN dialog appears, enter the PIN shown on the glucose meter screen.',
  //                   style: TextStyle(
  //                     fontFamily: FontConstants.interFonts,
  //                     fontSize: 12.sp,
  //                     color: Colors.blue.shade800,
  //                     height: 1.6,
  //                   ),
  //                 ),
  //               ],
  //             ),
  //           ),
  //           SizedBox(height: 16.h),
  //           SizedBox(
  //             width: double.infinity,
  //             child: ElevatedButton(
  //               onPressed: () {
  //                 Navigator.pop(ctx);
  //                 ctrl.startScan();
  //               },
  //               style: ElevatedButton.styleFrom(
  //                 backgroundColor: kPrimaryColor,
  //                 foregroundColor: Colors.white,
  //                 padding: EdgeInsets.symmetric(vertical: 14.h),
  //                 shape: RoundedRectangleBorder(
  //                   borderRadius: BorderRadius.circular(10.r),
  //                 ),
  //               ),
  //               child: Text(
  //                 'Start Scan',
  //                 style: TextStyle(
  //                   fontFamily: FontConstants.interFonts,
  //                   fontSize: 14.sp,
  //                   fontWeight: FontWeight.w700,
  //                 ),
  //               ),
  //             ),
  //           ),
  //         ],
  //       ),
  //     ),
  //   );
  // }
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
          width: 110,
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
          width: 110,
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
  final lower = value.toLowerCase();
  if (lower.contains('success') ||
      lower.contains('connected') ||
      lower.contains('received') ||
      lower.contains('enabled')) {
    statusColor = Colors.green.shade700;
  } else if (lower.contains('fail') ||
      lower.contains('error') ||
      lower.contains('not found')) {
    statusColor = Colors.red.shade700;
  } else if (lower.contains('scanning') ||
      lower.contains('connecting') ||
      lower.contains('reading') ||
      lower.contains('requested') ||
      lower.contains('discovering') ||
      lower.contains('retrying')) {
    statusColor = Colors.orange.shade700;
  }

  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 7),
    child: Row(
      children: [
        SizedBox(
          width: 110,
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
