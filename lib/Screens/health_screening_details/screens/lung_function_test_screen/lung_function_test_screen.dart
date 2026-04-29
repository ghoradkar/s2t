import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/lung_function_test_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/lung_function_test_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'liquid_fill_widget.dart';

// ── Shared label builder ───────────────────────────────────────────────────────

Widget _lbl(String text) => RichText(
  text: TextSpan(
    text: text,
    style: TextStyle(
      color: kBlackColor,
      fontSize: 14 * 1.2,
      fontFamily: FontConstants.interFonts,
      fontWeight: FontWeight.w600,
    ),
  ),
);

Widget _ct(
  String text, {
  double? size,
  FontWeight weight = FontWeight.w400,
  Color? color,
  TextAlign align = TextAlign.left,
}) =>
    CommonText(
      text: text,
      fontSize: size ?? 13.sp,
      fontWeight: weight,
      textColor: color ?? kTextColor,
      textAlign: align,
    );

// ── Screen ─────────────────────────────────────────────────────────────────────

class LungFunctionTestScreen extends StatelessWidget {
  final int campId;
  final UserAttendancesUsingSitedetailsIDOutput patient;

  const LungFunctionTestScreen({
    super.key,
    required this.campId,
    required this.patient,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<LungFunctionTestController>(
      init: LungFunctionTestController(patient: patient, campId: campId),
      dispose: (_) => Get.delete<LungFunctionTestController>(),
      builder: (ctrl) => Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Lung Function Test',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            padding: EdgeInsets.only(
              left: 14.w,
              right: 14.w,
              bottom: MediaQuery.of(context).viewPadding.bottom + 24.h,
            ),
            child: Column(
              children: [
                SizedBox(height: 12.h),
                _PatientInfoCard(patient: patient),
                SizedBox(height: 12.h),
                _DeviceCard(ctrl: ctrl),
                SizedBox(height: 12.h),
                Obx(() {
                  if (ctrl.deviceStatus.value == LftDeviceStatus.testing) {
                    return Column(
                      children: [
                        _TestProgressCard(ctrl: ctrl),
                        SizedBox(height: 12.h),
                      ],
                    );
                  }
                  return const SizedBox.shrink();
                }),
                Obx(() {
                  if (ctrl.hasResult.value && ctrl.result.value != null) {
                    return Column(
                      children: [
                        _ResultsCard(result: ctrl.result.value!),
                        SizedBox(height: 12.h),
                      ],
                    );
                  }
                  return const SizedBox.shrink();
                }),
                Obx(() {
                  final enabled =
                      ctrl.hasResult.value && !ctrl.isSubmitting.value;
                  return Opacity(
                    opacity: enabled ? 1.0 : 0.5,
                    child: SizedBox(
                      width: double.infinity,
                      child: AppActiveButton(
                        buttontitle: 'SUBMIT',
                        onTap: enabled ? () => ctrl.submit(context) : () {},
                      ),
                    ),
                  );
                }),
                SizedBox(height: 20.h),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ── Patient Info Card ──────────────────────────────────────────────────────────

class _PatientInfoCard extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput patient;

  const _PatientInfoCard({required this.patient});

  @override
  Widget build(BuildContext context) {
    final gender = patient.gender ?? '';
    final genderLabel = gender.toUpperCase().startsWith('M')
        ? 'Male'
        : gender.toUpperCase().startsWith('F')
            ? 'Female'
            : gender;

    return _SectionCard(
      title: 'Patient Information',
      icon: Icons.person_outline,
      child: Column(
        children: [
          AppTextField(
            label: _lbl('Patient Name'),
            controller: TextEditingController(text: patient.englishName ?? ''),
            readOnly: true,
          ),
          SizedBox(height: 10.h),
          AppTextField(
            label: _lbl('Reg. No.'),
            controller: TextEditingController(
              text: patient.regdNo?.toString() ?? '',
            ),
            readOnly: true,
          ),
          SizedBox(height: 10.h),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  label: _lbl('Age'),
                  controller: TextEditingController(
                    text: patient.age?.toString() ?? '',
                  ),
                  readOnly: true,
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  label: _lbl('Gender'),
                  controller: TextEditingController(text: genderLabel),
                  readOnly: true,
                ),
              ),
            ],
          ),
          SizedBox(height: 10.h),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  label: _lbl('Height (cm)'),
                  controller: TextEditingController(
                    text: patient.heightCMs?.toStringAsFixed(1) ?? '--',
                  ),
                  readOnly: true,
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  label: _lbl('Weight (kg)'),
                  controller: TextEditingController(
                    text: patient.weightKGs?.toStringAsFixed(1) ?? '--',
                  ),
                  readOnly: true,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

// ── Device Card ────────────────────────────────────────────────────────────────

class _DeviceCard extends StatelessWidget {
  final LungFunctionTestController ctrl;

  const _DeviceCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Spirometer Device',
      icon: Icons.bluetooth,
      child: Obx(() {
        final status      = ctrl.deviceStatus.value;
        final foundDev    = ctrl.foundDevice.value;
        final isConnected = status == LftDeviceStatus.connected;
        final isTesting   = status == LftDeviceStatus.testing;
        final isDone      = status == LftDeviceStatus.done;

        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Status message box
            if (ctrl.infoMessage.value.isNotEmpty) ...[
              _statusBox(ctrl.infoMessage.value, status),
              SizedBox(height: 12.h),
            ],

            // SCAN FOR SPIROMETER DEVICES — always visible unless testing/done
            if (!isTesting && !isDone)
              _OutlinedActionButton(
                label: 'SCAN FOR SPIROMETER DEVICES',
                icon: Icons.bluetooth_searching,
                onTap: ctrl.scan,
              ),

            // Device found info + CONNECT button
            if (foundDev != null && !isConnected && !isTesting && !isDone) ...[
              // SizedBox(height: 10.h),
              // Container(
              //   padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
              //   decoration: BoxDecoration(
              //     color: kBackground,
              //     borderRadius: BorderRadius.circular(8.r),
              //     border: Border.all(color: kTextFieldBorder),
              //   ),
              //   child: Row(
              //     children: [
              //       Icon(Icons.bluetooth, color: kPrimaryColor, size: 20.r),
              //       SizedBox(width: 10.w),
              //       Expanded(
              //         child: Column(
              //           crossAxisAlignment: CrossAxisAlignment.start,
              //           children: [
              //             _ct('Device Found', size: 11.sp, color: kLabelTextColor),
              //             SizedBox(height: 2.h),
              //             // _ct(
              //             //   '${foundDev['name'] ?? ''} ${foundDev['address'] ?? ''}',
              //             //   size: 13.sp,
              //             //   weight: FontWeight.w600,
              //             // ),
              //           ],
              //         ),
              //       ),
              //     ],
              //   ),
              // ),
              SizedBox(height: 10.h),
              AppActiveButton(
                buttontitle: 'CONNECT TO SAFEY SPIROMETER',
                onTap: () => ctrl.connect(
                  foundDev['address'] ?? '',
                  foundDev['name'] ?? 'Safey Device',
                ),
              ),
            ],

            // Connected badge (hide when testing/done to keep UI clean)
            if (isConnected) ...[
              SizedBox(height: 10.h),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
                decoration: BoxDecoration(
                  color: Colors.green.shade50,
                  borderRadius: BorderRadius.circular(8.r),
                  border: Border.all(color: Colors.green.shade200),
                ),
                child: Row(
                  children: [
                    Icon(Icons.check_circle, color: Colors.green, size: 18.r),
                    SizedBox(width: 8.w),
                    Expanded(
                      child: _ct(
                        'Connected: ${ctrl.connectedDeviceName.value}',
                        size: 13.sp,
                        weight: FontWeight.w600,
                        color: Colors.green.shade800,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(height: 12.h),
              AppActiveButton(
                buttontitle: 'START TEST',
                onTap: ctrl.startTest,
              ),
            ],
          ],
        );
      }),
    );
  }

  Widget _statusBox(String message, LftDeviceStatus status) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
      decoration: BoxDecoration(
        color: _statusColor(status).withValues(alpha: 0.10),
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: _statusColor(status).withValues(alpha: 0.35)),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(_statusIcon(status), color: _statusColor(status), size: 16.r),
          SizedBox(width: 8.w),
          Expanded(
            child: _ct(
              message,
              size: 12.sp,
              color: _statusColor(status),
              weight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }

  Color _statusColor(LftDeviceStatus s) {
    switch (s) {
      case LftDeviceStatus.scanning:
        return Colors.orange;
      case LftDeviceStatus.connected:
        return Colors.green;
      case LftDeviceStatus.testing:
        return kPrimaryColor;
      case LftDeviceStatus.done:
        return Colors.green;
      default:
        return kLabelTextColor;
    }
  }

  IconData _statusIcon(LftDeviceStatus s) {
    switch (s) {
      case LftDeviceStatus.scanning:
        return Icons.bluetooth_searching;
      case LftDeviceStatus.connected:
        return Icons.bluetooth_connected;
      case LftDeviceStatus.testing:
        return Icons.air;
      case LftDeviceStatus.done:
        return Icons.check_circle_outline;
      default:
        return Icons.info_outline;
    }
  }
}

// ── Test Progress Card ─────────────────────────────────────────────────────────

class _TestProgressCard extends StatelessWidget {
  final LungFunctionTestController ctrl;

  const _TestProgressCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Test Progress',
      icon: Icons.air,
      child: Obx(() {
        final progress = ctrl.testProgress.value;
        final instruction = _instructionFor(ctrl.infoMessage.value);
        return Column(
          children: [
            SizedBox(height: 12.h),
            Center(
              child: SizedBox(
                width: 180.r,
                height: 220.r,
                child: LiquidFillWidget(
                  progress: progress,
                  maxProgress: 150,
                  targetProgress: 100,
                  bottomText: instruction,
                ),
              ),
            ),
            SizedBox(height: 12.h),
            if (ctrl.infoMessage.value.isNotEmpty)
              _ct(
                ctrl.infoMessage.value,
                size: 13.sp,
                weight: FontWeight.w500,
                align: TextAlign.center,
              ),
            SizedBox(height: 8.h),
          ],
        );
      }),
    );
  }

  String _instructionFor(String msg) {
    final lower = msg.toLowerCase();
    if (lower.contains('blow hard') || lower.contains('start blowing')) {
      return 'Blow hard & fast!';
    }
    if (lower.contains('keep blowing') || lower.contains('insufficient')) {
      return 'Keep blowing!';
    }
    if (lower.contains('complete') || lower.contains('checking')) {
      return 'Done!';
    }
    return 'Blow!';
  }
}

// ── Results Card ───────────────────────────────────────────────────────────────

class _ResultsCard extends StatelessWidget {
  final LungFunctionTestResult result;

  const _ResultsCard({required this.result});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Trials flow/volume graph
        if (result.trialGraphPoints.isNotEmpty &&
            result.trialGraphPoints.any((t) => t.length > 1)) ...[
          _TrialsGraphCard(result: result),
          SizedBox(height: 12.h),
        ],
        _SectionCard(
          title: 'Spirometry Results',
          icon: Icons.bar_chart,
          child: Column(
            children: [
              _diagnosisBanner(),
              SizedBox(height: 12.h),
              _MeasurementsTable(measurements: result.measurements),
            ],
          ),
        ),
      ],
    );
  }

  Widget _diagnosisBanner() {
    final text = result.diagnosis.isEmpty ? 'N/A' : result.diagnosis;
    final isNormal = text.toLowerCase().contains('normal');
    final color = isNormal ? Colors.green : Colors.orange;

    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 12.h),
      decoration: BoxDecoration(
        color: color.shade50,
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: color.shade200),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _ct('Diagnosis', size: 11.sp, color: kLabelTextColor, weight: FontWeight.w500),
          SizedBox(height: 4.h),
          _ct(text, size: 14.sp, weight: FontWeight.w700, color: color.shade800),
          if (result.sessionScore.isNotEmpty) ...[
            SizedBox(height: 4.h),
            _ct('Session Score: ${result.sessionScore}', size: 12.sp, color: kLabelTextColor),
          ],
        ],
      ),
    );
  }
}

// ── Measurements table (matches native RecyclerView with TestResultAdapter) ────

class _MeasurementsTable extends StatelessWidget {
  final List<TestMeasurement> measurements;

  const _MeasurementsTable({required this.measurements});

  // Column flex weights matching native layout
  static const _flex = [3, 2, 2, 2, 2];
  static const _headers = ['Measurement', 'Value', '%Pred', 'LLN', 'Z-score'];

  @override
  Widget build(BuildContext context) {
    if (measurements.isEmpty) {
      return _ct('No measurement data available.',
          size: 13.sp, color: kLabelTextColor, align: TextAlign.center);
    }
    return Column(
      children: [
        _headerRow(),
        ListView.builder(
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          itemCount: measurements.length,
          itemBuilder: (_, i) => _dataRow(measurements[i], i),
        ),
      ],
    );
  }

  Widget _headerRow() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 6.w),
      child: Row(
        children: List.generate(
          _headers.length,
          (i) => Expanded(
            flex: _flex[i],
            child: Text(
              _headers[i],
              textAlign: i == 0 ? TextAlign.left : TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                fontWeight: FontWeight.w600,
                color: kWhiteColor,
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _dataRow(TestMeasurement m, int index) {
    final isEven = index % 2 == 0;
    return Container(
      color: isEven ? kWhiteColor : kBackground,
      padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 6.w),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // Measurement name
          Expanded(
            flex: _flex[0],
            child: Text(
              m.name,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: kTextColor,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
          // Value
          Expanded(
            flex: _flex[1],
            child: Text(
              m.formattedValue,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: kTextColor,
              ),
            ),
          ),
          // %Pred — green if >= 100, black otherwise (matches native)
          Expanded(
            flex: _flex[2],
            child: Text(
              m.formattedPredPer,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: m.predPerGreen
                    ? const Color(0xFF00D16C)
                    : kTextColor,
                fontWeight: m.predPerGreen ? FontWeight.w600 : FontWeight.w400,
              ),
            ),
          ),
          // LLN
          Expanded(
            flex: _flex[3],
            child: Text(
              m.formattedLln,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: kTextColor,
              ),
            ),
          ),
          // Z-score
          Expanded(
            flex: _flex[4],
            child: Text(
              m.formattedZScore,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: kTextColor,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ── Trials Flow/Volume Graph ───────────────────────────────────────────────────

class _TrialsGraphCard extends StatelessWidget {
  final LungFunctionTestResult result;

  const _TrialsGraphCard({required this.result});

  static const _trialColors = [
    Color(0xFF1565C0), // blue  – trial 1
    Color(0xFF2E7D32), // green – trial 2
    Color(0xFFC62828), // red   – trial 3
    Color(0xFF6A1B9A), // purple– trial 4
    Color(0xFFE65100), // orange– trial 5
  ];

  @override
  Widget build(BuildContext context) {
    final allTrials = result.trialGraphPoints;
    final bestIdx = result.bestTrialIndex;

    // Build one LineChartBarData per trial (skip empty / single-point)
    final bars = <LineChartBarData>[];
    for (int i = 0; i < allTrials.length; i++) {
      final pts = allTrials[i];
      if (pts.length < 2) continue;
      final isBest = i == bestIdx;
      final color = _trialColors[i % _trialColors.length];
      bars.add(LineChartBarData(
        spots: pts.map((p) => FlSpot(p.volume, p.flow)).toList(),
        isCurved: false,
        color: isBest ? color : color.withValues(alpha: 0.45),
        barWidth: isBest ? 2.5 : 1.5,
        dotData: const FlDotData(show: false),
        belowBarData: BarAreaData(show: false),
      ));
    }

    if (bars.isEmpty) return const SizedBox.shrink();

    // Axis bounds
    double minX = double.infinity, maxX = double.negativeInfinity;
    double minY = double.infinity, maxY = double.negativeInfinity;
    for (final trial in allTrials) {
      for (final p in trial) {
        if (p.volume < minX) minX = p.volume;
        if (p.volume > maxX) maxX = p.volume;
        if (p.flow < minY) minY = p.flow;
        if (p.flow > maxY) maxY = p.flow;
      }
    }
    minX = (minX.isInfinite ? 0 : minX).floorToDouble();
    maxX = (maxX.isInfinite ? 6 : maxX).ceilToDouble();
    minY = (minY.isInfinite ? -2 : minY).floorToDouble();
    maxY = (maxY.isInfinite ? 12 : maxY).ceilToDouble();

    return _SectionCard(
      title: 'Trials',
      icon: Icons.show_chart,
      child: SizedBox(
        height: 220.h,
        child: Padding(
          padding: EdgeInsets.only(right: 8.w, top: 8.h),
          child: LineChart(
            LineChartData(
              minX: minX,
              maxX: maxX,
              minY: minY,
              maxY: maxY,
              clipData: const FlClipData.all(),
              gridData: FlGridData(
                show: true,
                drawVerticalLine: true,
                getDrawingHorizontalLine: (_) => FlLine(
                  color: Colors.grey.shade200,
                  strokeWidth: 1,
                ),
                getDrawingVerticalLine: (_) => FlLine(
                  color: Colors.grey.shade200,
                  strokeWidth: 1,
                ),
              ),
              borderData: FlBorderData(
                show: true,
                border: Border.all(color: Colors.grey.shade400, width: 1),
              ),
              titlesData: FlTitlesData(
                leftTitles: AxisTitles(
                  axisNameWidget: Text(
                    'Flow(L/s)',
                    style: TextStyle(
                      fontSize: 10.sp,
                      color: kLabelTextColor,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                  axisNameSize: 18,
                  sideTitles: SideTitles(
                    showTitles: true,
                    reservedSize: 36.w,
                    getTitlesWidget: (value, meta) => Text(
                      value.toStringAsFixed(1),
                      style: TextStyle(
                        fontSize: 9.sp,
                        color: kLabelTextColor,
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                ),
                bottomTitles: AxisTitles(
                  axisNameWidget: Text(
                    'Volume',
                    style: TextStyle(
                      fontSize: 10.sp,
                      color: kLabelTextColor,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                  axisNameSize: 18,
                  sideTitles: SideTitles(
                    showTitles: true,
                    reservedSize: 24.h,
                    getTitlesWidget: (value, meta) => Text(
                      value.toStringAsFixed(1),
                      style: TextStyle(
                        fontSize: 9.sp,
                        color: kLabelTextColor,
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                ),
                topTitles: const AxisTitles(
                    sideTitles: SideTitles(showTitles: false)),
                rightTitles: const AxisTitles(
                    sideTitles: SideTitles(showTitles: false)),
              ),
              lineBarsData: bars,
            ),
          ),
        ),
      ),
    );
  }
}

// ── Shared card wrapper ────────────────────────────────────────────────────────

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
        borderRadius: BorderRadius.circular(10.r),
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
          Container(
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
            decoration: BoxDecoration(
              color: kPrimaryColor.withValues(alpha: 0.08),
              borderRadius: BorderRadius.vertical(top: Radius.circular(10.r)),
            ),
            child: Row(
              children: [
                Icon(icon, size: 18.r, color: kPrimaryColor),
                SizedBox(width: 8.w),
                _ct(title, size: 14.sp, weight: FontWeight.w600, color: kPrimaryColor),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.all(14.w),
            child: child,
          ),
        ],
      ),
    );
  }
}

class _OutlinedActionButton extends StatelessWidget {
  final String label;
  final IconData icon;
  final VoidCallback? onTap;

  const _OutlinedActionButton({
    required this.label,
    required this.icon,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: double.infinity,
        padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 16.w),
        decoration: BoxDecoration(
          color: kWhiteColor,
          borderRadius: BorderRadius.circular(8.r),
          border: Border.all(color: kPrimaryColor, width: 1.5),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 18.r, color: kPrimaryColor),
            SizedBox(width: 8.w),
            _ct(label, size: 13.sp, weight: FontWeight.w600, color: kPrimaryColor),
          ],
        ),
      ),
    );
  }
}
