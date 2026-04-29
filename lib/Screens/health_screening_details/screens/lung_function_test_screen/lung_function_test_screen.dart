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
      builder:
          (ctrl) => Scaffold(
            backgroundColor: kBackground,
            appBar: mAppBar(
              scTitle: 'Lung Function Test',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
            ),
            body: SafeArea(
              child: SingleChildScrollView(
                padding: EdgeInsets.symmetric(horizontal: 16.w).copyWith(
                  top: 16.h,
                  bottom: MediaQuery.of(context).viewPadding.bottom + 28.h,
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    _PatientInfoCard(patient: patient),
                    SizedBox(height: 14.h),
                    _DeviceCard(ctrl: ctrl),
                    SizedBox(height: 14.h),

                    // Test progress (visible only while testing)
                    Obx(() {
                      if (ctrl.deviceStatus.value == LftDeviceStatus.testing) {
                        return Column(
                          children: [
                            _TestProgressCard(ctrl: ctrl),
                            SizedBox(height: 14.h),
                          ],
                        );
                      }
                      return const SizedBox.shrink();
                    }),

                    // Results + graph (visible after test done)
                    Obx(() {
                      if (ctrl.hasResult.value && ctrl.result.value != null) {
                        return Column(
                          children: [
                            _ResultsCard(result: ctrl.result.value!),
                            SizedBox(height: 14.h),
                          ],
                        );
                      }
                      return const SizedBox.shrink();
                    }),

                    // Submit button (visible only when result is ready)
                    Obx(() {
                      if (!ctrl.hasResult.value) return const SizedBox.shrink();
                      return AppActiveButton(
                        buttontitle:
                            ctrl.isSubmitting.value ? 'Submitting…' : 'SUBMIT',
                        onTap:
                            ctrl.isSubmitting.value
                                ? () {}
                                : () => ctrl.submit(context),
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

// ── Patient Info Card ──────────────────────────────────────────────────────────

class _PatientInfoCard extends StatefulWidget {
  final UserAttendancesUsingSitedetailsIDOutput patient;

  const _PatientInfoCard({required this.patient});

  @override
  State<_PatientInfoCard> createState() => _PatientInfoCardState();
}

class _PatientInfoCardState extends State<_PatientInfoCard> {
  late final TextEditingController _nameCtrl;
  late final TextEditingController _regNoCtrl;
  late final TextEditingController _ageCtrl;
  late final TextEditingController _genderCtrl;
  late final TextEditingController _heightCtrl;
  late final TextEditingController _weightCtrl;

  @override
  void initState() {
    super.initState();
    final p = widget.patient;
    final gender = p.gender ?? '';
    final genderLabel =
        gender.toUpperCase().startsWith('M')
            ? 'Male'
            : gender.toUpperCase().startsWith('F')
            ? 'Female'
            : gender;

    _nameCtrl = TextEditingController(text: p.englishName ?? '');
    _regNoCtrl = TextEditingController(text: p.regdNo?.toString() ?? '');
    _ageCtrl = TextEditingController(text: p.age?.toString() ?? '');
    _genderCtrl = TextEditingController(text: genderLabel);
    _heightCtrl = TextEditingController(
      text: p.heightCMs?.toStringAsFixed(1) ?? '',
    );
    _weightCtrl = TextEditingController(
      text: p.weightKGs?.toStringAsFixed(1) ?? '',
    );
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _regNoCtrl.dispose();
    _ageCtrl.dispose();
    _genderCtrl.dispose();
    _heightCtrl.dispose();
    _weightCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Patient Information',
      icon: Icons.person_outline_rounded,
      child: Column(
        children: [
          AppTextField(
            controller: _nameCtrl,
            readOnly: true,
            onTap: () {},
            label: const Text('Patient Name'),
          ),
          SizedBox(height: 12.h),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: _genderCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: const Text('Gender'),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  controller: _ageCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: const Text('Age'),
                ),
              ),
            ],
          ),
          SizedBox(height: 12.h),

          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: _heightCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: const Text('Height (cm)'),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  controller: _weightCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: const Text('Weight (kg)'),
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
      icon: Icons.bluetooth_rounded,
      child: Obx(() {
        final status = ctrl.deviceStatus.value;
        final foundDev = ctrl.foundDevice.value;
        final isConnected = status == LftDeviceStatus.connected;
        final isTesting = status == LftDeviceStatus.testing;
        final isDone = status == LftDeviceStatus.done;

        return Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Status message banner
            if (ctrl.infoMessage.value.isNotEmpty) ...[
              _StatusBanner(message: ctrl.infoMessage.value, status: status),
              SizedBox(height: 12.h),
            ],

            // Scan button
            if (!isTesting && !isDone)
              _ActionButton(
                label: 'SCAN FOR SPIROMETER DEVICES',
                icon: Icons.bluetooth_searching_rounded,
                onTap: ctrl.scan,
                outlined: true,
              ),

            // Connect button (device found but not yet connected)
            if (foundDev != null && !isConnected && !isTesting && !isDone) ...[
              SizedBox(height: 10.h),
              _ActionButton(
                label: 'CONNECT TO SAFEY SPIROMETER',
                icon: Icons.bluetooth_connected_rounded,
                onTap:
                    () => ctrl.connect(
                      foundDev['address'] ?? '',
                      foundDev['name'] ?? 'Safey Device',
                    ),
              ),
            ],

            // Connected state
            if (isConnected) ...[
              SizedBox(height: 10.h),
              _ConnectedBadge(name: ctrl.connectedDeviceName.value),
              SizedBox(height: 12.h),
              _ActionButton(
                label: 'START TEST',
                icon: Icons.play_arrow_rounded,
                onTap: ctrl.startTest,
              ),
            ],
          ],
        );
      }),
    );
  }
}

class _StatusBanner extends StatelessWidget {
  final String message;
  final LftDeviceStatus status;

  const _StatusBanner({required this.message, required this.status});

  Color get _color {
    switch (status) {
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

  IconData get _icon {
    switch (status) {
      case LftDeviceStatus.scanning:
        return Icons.bluetooth_searching_rounded;
      case LftDeviceStatus.connected:
        return Icons.bluetooth_connected_rounded;
      case LftDeviceStatus.testing:
        return Icons.air_rounded;
      case LftDeviceStatus.done:
        return Icons.check_circle_outline_rounded;
      default:
        return Icons.info_outline_rounded;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
      decoration: BoxDecoration(
        color: _color.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: _color.withValues(alpha: 0.30)),
      ),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(_icon, color: _color, size: 15.r),
          SizedBox(width: 8.w),
          Expanded(
            child: CommonText(
              text: message,
              fontSize: 12.sp,
              fontWeight: FontWeight.w500,
              textColor: _color,
              textAlign: TextAlign.left,
            ),
          ),
        ],
      ),
    );
  }
}

class _ConnectedBadge extends StatelessWidget {
  final String name;

  const _ConnectedBadge({required this.name});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      decoration: BoxDecoration(
        color: Colors.green.shade50,
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: Colors.green.shade200),
      ),
      child: Row(
        children: [
          Icon(
            Icons.check_circle_rounded,
            color: Colors.green.shade600,
            size: 18.r,
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: 'Device Connected',
                  fontSize: 11.sp,
                  fontWeight: FontWeight.w500,
                  textColor: Colors.green.shade700,
                  textAlign: TextAlign.left,
                ),
                SizedBox(height: 2.h),
                CommonText(
                  text: name,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.green.shade800,
                  textAlign: TextAlign.left,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _ActionButton extends StatelessWidget {
  final String label;
  final IconData icon;
  final VoidCallback? onTap;
  final bool outlined;

  const _ActionButton({
    required this.label,
    required this.icon,
    this.onTap,
    this.outlined = false,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: double.infinity,
        padding: EdgeInsets.symmetric(vertical: 13.h, horizontal: 16.w),
        decoration: BoxDecoration(
          color: outlined ? kWhiteColor : kPrimaryColor,
          borderRadius: BorderRadius.circular(8.r),
          border: Border.all(color: kPrimaryColor, width: outlined ? 1.5 : 0),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              icon,
              size: 18.r,
              color: outlined ? kPrimaryColor : kWhiteColor,
            ),
            SizedBox(width: 8.w),
            CommonText(
              text: label,
              fontSize: 13.sp,
              fontWeight: FontWeight.w600,
              textColor: outlined ? kPrimaryColor : kWhiteColor,
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}

// ── Test Progress Card ─────────────────────────────────────────────────────────

class _TestProgressCard extends StatelessWidget {
  final LungFunctionTestController ctrl;

  const _TestProgressCard({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Test in Progress',
      icon: Icons.air_rounded,
      child: Obx(() {
        final progress = ctrl.testProgress.value;
        final instruction = _instructionFor(ctrl.infoMessage.value);
        return Column(
          children: [
            SizedBox(height: 8.h),
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
            SizedBox(height: 14.h),
            if (ctrl.infoMessage.value.isNotEmpty)
              Container(
                width: double.infinity,
                padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
                decoration: BoxDecoration(
                  color: kPrimaryColor.withValues(alpha: 0.06),
                  borderRadius: BorderRadius.circular(8.r),
                ),
                child: CommonText(
                  text: ctrl.infoMessage.value,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.center,
                ),
              ),
            SizedBox(height: 4.h),
          ],
        );
      }),
    );
  }

  String _instructionFor(String msg) {
    final lower = msg.toLowerCase();
    if (lower.contains('blow hard') || lower.contains('start blowing'))
      return 'Blow hard & fast!';
    if (lower.contains('keep blowing') || lower.contains('insufficient'))
      return 'Keep blowing!';
    if (lower.contains('complete') || lower.contains('checking'))
      return 'Done!';
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
        if (result.trialGraphPoints.isNotEmpty &&
            result.trialGraphPoints.any((t) => t.length > 1)) ...[
          _TrialsGraphCard(result: result),
          SizedBox(height: 14.h),
        ],
        _SectionCard(
          title: 'Spirometry Results',
          icon: Icons.bar_chart_rounded,
          child: Column(
            children: [
              _DiagnosisBanner(result: result),
              SizedBox(height: 14.h),
              _MeasurementsTable(measurements: result.measurements),
            ],
          ),
        ),
      ],
    );
  }
}

class _DiagnosisBanner extends StatelessWidget {
  final LungFunctionTestResult result;

  const _DiagnosisBanner({required this.result});

  @override
  Widget build(BuildContext context) {
    final text = result.diagnosis.isEmpty ? 'N/A' : result.diagnosis;
    final isNormal = text.toLowerCase().contains('normal');
    final color = isNormal ? Colors.green : Colors.deepOrange;

    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 14.h),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.07),
        borderRadius: BorderRadius.circular(10.r),
        border: Border.all(color: color.withValues(alpha: 0.25)),
      ),
      child: Row(
        children: [
          Container(
            padding: EdgeInsets.all(8.r),
            decoration: BoxDecoration(
              color: color.withValues(alpha: 0.12),
              shape: BoxShape.circle,
            ),
            child: Icon(
              isNormal
                  ? Icons.check_circle_outline_rounded
                  : Icons.info_outline_rounded,
              color: color,
              size: 20.r,
            ),
          ),
          SizedBox(width: 12.w),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: 'Diagnosis',
                  fontSize: 11.sp,
                  fontWeight: FontWeight.w500,
                  textColor: color.withValues(alpha: 0.75),
                  textAlign: TextAlign.left,
                ),
                SizedBox(height: 3.h),
                CommonText(
                  text: text,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: color,
                  textAlign: TextAlign.left,
                ),
                if (result.sessionScore.isNotEmpty &&
                    result.sessionScore != 'N/A') ...[
                  SizedBox(height: 4.h),
                  CommonText(
                    text: 'Session Score: ${result.sessionScore}',
                    fontSize: 11.sp,
                    fontWeight: FontWeight.w500,
                    textColor: kLabelTextColor,
                    textAlign: TextAlign.left,
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Measurements Table ────────────────────────────────────────────────────────

class _MeasurementsTable extends StatelessWidget {
  final List<TestMeasurement> measurements;

  const _MeasurementsTable({required this.measurements});

  static const _flex = [3, 2, 2, 2, 2];
  static const _headers = ['Measurement', 'Value', '%Pred', 'LLN', 'Z-score'];

  @override
  Widget build(BuildContext context) {
    if (measurements.isEmpty) {
      return Padding(
        padding: EdgeInsets.symmetric(vertical: 20.h),
        child: CommonText(
          text: 'No measurement data available.',
          fontSize: 13.sp,
          fontWeight: FontWeight.w400,
          textColor: kLabelTextColor,
          textAlign: TextAlign.center,
        ),
      );
    }

    return ClipRRect(
      borderRadius: BorderRadius.circular(8.r),
      child: Column(
        children: [
          _headerRow(),
          ...List.generate(
            measurements.length,
            (i) => _dataRow(measurements[i], i),
          ),
        ],
      ),
    );
  }

  Widget _headerRow() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 8.w),
      child: Row(
        children: List.generate(
          _headers.length,
          (i) => Expanded(
            flex: _flex[i],
            child: CommonText(
              text: _headers[i],
              fontSize: 11.sp,
              fontWeight: FontWeight.w700,
              textColor: kWhiteColor,
              textAlign: i == 0 ? TextAlign.left : TextAlign.center,
            ),
          ),
        ),
      ),
    );
  }

  Widget _dataRow(TestMeasurement m, int index) {
    final isEven = index % 2 == 0;
    return Container(
      color: isEven ? kWhiteColor : const Color(0xFFF5F7FA),
      padding: EdgeInsets.symmetric(vertical: 9.h, horizontal: 8.w),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            flex: _flex[0],
            child: CommonText(
              text: m.name,
              fontSize: 12.sp,
              fontWeight: FontWeight.w500,
              textColor: kTextColor,
              textAlign: TextAlign.left,
            ),
          ),
          Expanded(
            flex: _flex[1],
            child: CommonText(
              text: m.formattedValue,
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: kTextColor,
              textAlign: TextAlign.center,
            ),
          ),
          Expanded(
            flex: _flex[2],
            child: CommonText(
              text: m.formattedPredPer,
              fontSize: 12.sp,
              fontWeight: m.predPerGreen ? FontWeight.w600 : FontWeight.w400,
              textColor: m.predPerGreen ? const Color(0xFF00B865) : kTextColor,
              textAlign: TextAlign.center,
            ),
          ),
          Expanded(
            flex: _flex[3],
            child: CommonText(
              text: m.formattedLln,
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: kTextColor,
              textAlign: TextAlign.center,
            ),
          ),
          Expanded(
            flex: _flex[4],
            child: CommonText(
              text: m.formattedZScore,
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: kTextColor,
              textAlign: TextAlign.center,
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
    Color(0xFF1565C0),
    Color(0xFF2E7D32),
    Color(0xFFC62828),
    Color(0xFF6A1B9A),
    Color(0xFFE65100),
  ];

  @override
  Widget build(BuildContext context) {
    final allTrials = result.trialGraphPoints;
    final bestIdx = result.bestTrialIndex;

    final bars = <LineChartBarData>[];
    for (int i = 0; i < allTrials.length; i++) {
      final pts = allTrials[i];
      if (pts.length < 2) continue;
      final isBest = i == bestIdx;
      final color = _trialColors[i % _trialColors.length];
      bars.add(
        LineChartBarData(
          spots: pts.map((p) => FlSpot(p.volume, p.flow)).toList(),
          isCurved: false,
          color: isBest ? color : color.withValues(alpha: 0.40),
          barWidth: isBest ? 2.5 : 1.5,
          dotData: const FlDotData(show: false),
          belowBarData: BarAreaData(show: false),
        ),
      );
    }

    if (bars.isEmpty) return const SizedBox.shrink();

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
      icon: Icons.show_chart_rounded,
      child: SizedBox(
        height: 220.h,
        child: Padding(
          padding: EdgeInsets.only(right: 8.w, top: 8.h, bottom: 4.h),
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
                getDrawingHorizontalLine:
                    (_) => FlLine(color: Colors.grey.shade200, strokeWidth: 1),
                getDrawingVerticalLine:
                    (_) => FlLine(color: Colors.grey.shade200, strokeWidth: 1),
              ),
              borderData: FlBorderData(
                show: true,
                border: Border.all(color: Colors.grey.shade300, width: 1),
              ),
              titlesData: FlTitlesData(
                leftTitles: AxisTitles(
                  axisNameWidget: Text(
                    'Flow (L/s)',
                    style: TextStyle(
                      fontSize: 10.sp,
                      color: kLabelTextColor,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                  axisNameSize: 18,
                  sideTitles: SideTitles(
                    showTitles: true,
                    reservedSize: 38.w,
                    getTitlesWidget:
                        (v, _) => Text(
                          v.toStringAsFixed(1),
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
                    getTitlesWidget:
                        (v, _) => Text(
                          v.toStringAsFixed(1),
                          style: TextStyle(
                            fontSize: 9.sp,
                            color: kLabelTextColor,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                  ),
                ),
                topTitles: const AxisTitles(
                  sideTitles: SideTitles(showTitles: false),
                ),
                rightTitles: const AxisTitles(
                  sideTitles: SideTitles(showTitles: false),
                ),
              ),
              lineBarsData: bars,
            ),
          ),
        ),
      ),
    );
  }
}

// ── Shared section card ────────────────────────────────────────────────────────

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
        borderRadius: BorderRadius.circular(12.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Card header — left accent bar style
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 14.h),
            child: Row(
              children: [
                Container(
                  width: 3.5.w,
                  height: 18.h,
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(4.r),
                  ),
                ),
                SizedBox(width: 10.w),
                Icon(icon, size: 17.r, color: kPrimaryColor),
                SizedBox(width: 8.w),
                CommonText(
                  text: title,
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w700,
                  textColor: kTextColor,
                  textAlign: TextAlign.left,
                ),
              ],
            ),
          ),
          Divider(height: 1, thickness: 1, color: kBackground),
          // Card body
          Padding(padding: EdgeInsets.all(16.w), child: child),
        ],
      ),
    );
  }
}
