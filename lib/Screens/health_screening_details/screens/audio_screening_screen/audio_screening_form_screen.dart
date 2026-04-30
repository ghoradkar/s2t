import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';

import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/audio_screening_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

// ── Custom dot painters ────────────────────────────────────────────────────────

/// Red hollow ring — audiogram symbol for right ear.
class _HollowRingPainter extends FlDotPainter {
  const _HollowRingPainter();

  @override
  Color get mainColor => Colors.red;

  @override
  List<Object?> get props => [mainColor];

  @override
  Size getSize(FlSpot spot) => const Size(12, 12);

  @override
  FlDotPainter lerp(FlDotPainter a, FlDotPainter b, double t) => b;

  @override
  void draw(Canvas canvas, FlSpot spot, Offset offsetInCanvas) {
    canvas.drawCircle(
      offsetInCanvas,
      5,
      Paint()
        ..color = Colors.red
        ..style = PaintingStyle.stroke
        ..strokeWidth = 2,
    );
  }
}

/// Blue solid circle with white X — audiogram symbol for left ear.
class _CrossCirclePainter extends FlDotPainter {
  const _CrossCirclePainter();

  @override
  Color get mainColor => Colors.blue;

  @override
  List<Object?> get props => [mainColor];

  @override
  Size getSize(FlSpot spot) => const Size(12, 12);

  @override
  FlDotPainter lerp(FlDotPainter a, FlDotPainter b, double t) => b;

  @override
  void draw(Canvas canvas, FlSpot spot, Offset offsetInCanvas) {
    canvas.drawCircle(offsetInCanvas, 5, Paint()..color = Colors.blue);
    final p =
        Paint()
          ..color = Colors.white
          ..strokeWidth = 2
          ..style = PaintingStyle.stroke
          ..strokeCap = StrokeCap.round;
    const r = 3.0;
    canvas.drawLine(
      offsetInCanvas + const Offset(-r, -r),
      offsetInCanvas + const Offset(r, r),
      p,
    );
    canvas.drawLine(
      offsetInCanvas + const Offset(r, -r),
      offsetInCanvas + const Offset(-r, r),
      p,
    );
  }
}

// ── Screen ────────────────────────────────────────────────────────────────────

class AudioScreeningFormScreen extends StatefulWidget {
  final int campId;
  final UserAttendancesUsingSitedetailsIDOutput patient;

  const AudioScreeningFormScreen({
    super.key,
    required this.campId,
    required this.patient,
  });

  @override
  State<AudioScreeningFormScreen> createState() =>
      _AudioScreeningFormScreenState();
}

class _AudioScreeningFormScreenState extends State<AudioScreeningFormScreen> {
  late final AudioScreeningController controller;

  final TextEditingController _deafnessCtrl = TextEditingController();
  final TextEditingController _frequencyCtrl = TextEditingController();
  final TextEditingController _volumeCtrl = TextEditingController();
  final TextEditingController _leftRemarkCtrl = TextEditingController();
  final TextEditingController _rightRemarkCtrl = TextEditingController();

  final List<Worker> _workers = [];

  @override
  void initState() {
    super.initState();
    controller = Get.put(AudioScreeningController());
    controller.init(patientData: widget.patient, campID: widget.campId);

    _frequencyCtrl.text =
        '${AudioScreeningController.frequencies[controller.selectedFrequencyIndex.value]} Hz';
    _volumeCtrl.text = '${controller.currentDb} dB';

    _workers.addAll([
      ever(controller.selectedDeafness, (v) => _deafnessCtrl.text = v),
      ever(controller.selectedFrequencyIndex, (idx) {
        _frequencyCtrl.text = '${AudioScreeningController.frequencies[idx]} Hz';
      }),
      ever(controller.currentDbIndex, (_) {
        _volumeCtrl.text = '${controller.currentDb} dB';
      }),
      ever(controller.leftEarRemark, (v) => _leftRemarkCtrl.text = v),
      ever(controller.rightEarRemark, (v) => _rightRemarkCtrl.text = v),
    ]);
  }

  @override
  void dispose() {
    for (final w in _workers) {
      w.dispose();
    }
    _deafnessCtrl.dispose();
    _frequencyCtrl.dispose();
    _volumeCtrl.dispose();
    _leftRemarkCtrl.dispose();
    _rightRemarkCtrl.dispose();
    Get.delete<AudioScreeningController>();
    super.dispose();
  }

  // ── Build ─────────────────────────────────────────────────────────────────────
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Audio Screening Test',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.fromLTRB(14.w, 14.h, 14.w, 24.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _patientCard(),
            SizedBox(height: 12.h),
            _deafnessCard(context),
            SizedBox(height: 12.h),
            _aftCard(context),
            SizedBox(height: 12.h),
            _resultsCard(),
            SizedBox(height: 12.h),
            _remarksCard(context),
            SizedBox(height: 16.h),
            Obx(() {
              final saving = controller.isSaving.value;
              return AppActiveButton(
                buttontitle: saving ? 'Saving…' : 'Save',
                onTap: () {
                  if (!saving) controller.save(context);
                },
              );
            }),
          ],
        ),
      ),
    );
  }

  // ── Patient card ──────────────────────────────────────────────────────────────
  Widget _patientCard() {
    final p = widget.patient;
    final gender = (p.gender ?? '').isNotEmpty ? p.gender! : '—';
    final age = p.age?.toString() ?? '—';

    return _card(
      child: Row(
        children: [
          CircleAvatar(
            radius: 24.r,
            backgroundColor: kPurpleFaint,
            child: Icon(Icons.person_rounded, color: kPrimaryColor, size: 26.r),
          ),
          SizedBox(width: 14.w),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: p.englishName ?? '—',
                  fontSize: 15.sp,
                  fontWeight: FontWeight.w700,
                  textColor: kTextColor,
                  textAlign: TextAlign.left,
                  maxLine: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                SizedBox(height: 6.h),
                Wrap(
                  spacing: 12.w,
                  children: [
                    _infoPill(Icons.wc_rounded, gender),
                    _infoPill(Icons.cake_outlined, '$age yrs'),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _infoPill(IconData icon, String text) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(icon, size: 12.r, color: kLabelTextColor),
        SizedBox(width: 3.w),
        CommonText(
          text: text,
          fontSize: 11.sp,
          fontWeight: FontWeight.w400,
          textColor: kLabelTextColor,
          textAlign: TextAlign.left,
        ),
      ],
    );
  }

  // ── Deafness Info card ────────────────────────────────────────────────────────
  Widget _deafnessCard(BuildContext context) {
    return _card(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _sectionHeader('Deafness Info', Icons.hearing_disabled_outlined),
          SizedBox(height: 10.h),
          AppTextField(
            controller: _deafnessCtrl,
            readOnly: true,
            label: const Text('Select Deafness Info *'),
            suffixIcon: const Icon(Icons.arrow_drop_down),
            onTap: () => _showDeafnessPicker(context),
          ),
        ],
      ),
    );
  }

  void _showDeafnessPicker(BuildContext context) {
    final current = controller.selectedDeafness.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<String, String>(
            title: 'Select Deafness Info',
            items: AudioScreeningController.deafnessOptions,
            valueFor: (item) => item,
            labelFor: (item) => item,
            selectedValue: current.isEmpty ? null : current,
            height: 320.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              controller.onDeafnessChanged(item);
              if (item == 'Right Ear' ||
                  item == 'Left Ear' ||
                  item == 'Both Ears') {
                _showMarathiAlert(context);
              }
            },
          ),
    );
  }

  void _showMarathiAlert(BuildContext context) {
    // ToastManager.showAlertDialog(
    //   context,
    //   'तुम्ही मूकबधिर पर्याय निवडला आहे.\nपर्याय बरोबर असल्याची खात्री करा\nअन्यथा योग्य पर्याय निवडा',
    //       () {
    //     Get.back();
    //   },
    //   onNoTap: () {
    //     Get.back();
    //     controller.resetDeafnessSelection();
    //   },
    // );
    ToastManager.commonAlert(
      Get.context!,
      "assets/icons/sign-language.png",
      '',
      'तुम्ही मूकबधिर पर्याय निवडला आहे.\nपर्याय बरोबर असल्याची खात्री करा\nअन्यथा योग्य पर्याय निवडा',
      () {
        Get.back();
      },
      () {
        Get.back();
        controller.resetDeafnessSelection();
      },
      "Yes",
      "No",
    );
  }

  // ── AFT card ──────────────────────────────────────────────────────────────────
  Widget _aftCard(BuildContext context) {
    return _card(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _sectionHeader('Audio Function Test (AFT)', Icons.graphic_eq_rounded),
          SizedBox(height: 12.h),

          // Ear selection toggle buttons
          _earSelection(),
          SizedBox(height: 14.h),

          // Frequency + Volume side by side
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: _frequencyCtrl,
                  readOnly: true,
                  label: const Text('Frequency (Hz)'),
                  suffixIcon: const Icon(Icons.arrow_drop_down),
                  onTap: () => _showFrequencyPicker(context),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  controller: _volumeCtrl,
                  readOnly: true,
                  label: const Text('Volume (dB)'),
                  suffixIcon: const Icon(Icons.volume_up_outlined),
                ),
              ),
            ],
          ),
          SizedBox(height: 14.h),

          _hearingButtons(),
        ],
      ),
    );
  }

  // Ear selection: styled full-width toggle buttons
  Widget _earSelection() {
    return Obx(() {
      final earSel = controller.isLeftEarSelected.value;
      return Row(
        children: [
          Expanded(
            child: _earToggleBtn(
              label: 'Left Ear',
              selected: earSel == true,
              enabled: controller.leftEarEnabled,
              onTap: controller.selectLeftEar,
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: _earToggleBtn(
              label: 'Right Ear',
              selected: earSel == false,
              enabled: controller.rightEarEnabled,
              onTap: controller.selectRightEar,
            ),
          ),
        ],
      );
    });
  }

  Widget _earToggleBtn({
    required String label,
    required bool selected,
    required bool enabled,
    required VoidCallback onTap,
  }) {
    final activeColor = kPrimaryColor;
    return GestureDetector(
      onTap: enabled ? onTap : null,
      child: AnimatedContainer(
        duration: kAnimationDuration,
        padding: EdgeInsets.symmetric(vertical: 11.h),
        decoration: BoxDecoration(
          color:
              selected
                  ? activeColor
                  : (enabled ? kPurpleFaint : Colors.grey.shade100),
          borderRadius: BorderRadius.circular(8.r),
          border: Border.all(
            color:
                selected
                    ? activeColor
                    : (enabled
                        ? activeColor.withValues(alpha: 0.3)
                        : Colors.grey.shade200),
            width: 1.5,
          ),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.hearing_rounded,
              size: 16.r,
              color:
                  selected
                      ? Colors.white
                      : (enabled ? activeColor : Colors.grey.shade400),
            ),
            SizedBox(width: 6.w),
            CommonText(
              text: label,
              fontSize: 13.sp,
              fontWeight: selected ? FontWeight.w700 : FontWeight.w500,
              textColor:
                  selected
                      ? Colors.white
                      : (enabled ? activeColor : Colors.grey.shade400),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }

  void _showFrequencyPicker(BuildContext context) {
    final currentIdx = controller.selectedFrequencyIndex.value;
    final currentFreq = AudioScreeningController.frequencies[currentIdx];
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<int, int>(
            title: 'Select Frequency (Hz)',
            items: AudioScreeningController.frequencies,
            valueFor: (item) => item,
            labelFor: (item) => '$item Hz',
            selectedValue: currentFreq,
            height: 380.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              final idx = AudioScreeningController.frequencies.indexOf(item);
              if (idx >= 0) controller.onFrequencyChanged(idx);
            },
          ),
    );
  }

  // Hearing buttons
  Widget _hearingButtons() {
    return Obx(() {
      final canTest = controller.selectedDeafness.value != 'Both Ears';
      return Row(
        children: [
          Expanded(
            child: _hearingBtn(
              label: 'I CANNOT HEAR',
              icon: Icons.volume_off_rounded,
              color: const Color(0xFFE53935),
              enabled: canTest,
              onTap: controller.cannotHear,
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: _hearingBtn(
              label: 'I CAN HEAR',
              icon: Icons.volume_up_rounded,
              color: const Color(0xFF2E7D32),
              enabled: canTest,
              onTap: controller.canHear,
            ),
          ),
        ],
      );
    });
  }

  Widget _hearingBtn({
    required String label,
    required IconData icon,
    required Color color,
    required bool enabled,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: enabled ? onTap : null,
      child: AnimatedContainer(
        duration: kAnimationDuration,
        padding: EdgeInsets.symmetric(vertical: 12.h),
        decoration: BoxDecoration(
          color: enabled ? color : Colors.grey.shade300,
          borderRadius: BorderRadius.circular(8.r),
          boxShadow:
              enabled
                  ? [
                    BoxShadow(
                      color: color.withValues(alpha: 0.3),
                      blurRadius: 6,
                      offset: const Offset(0, 3),
                    ),
                  ]
                  : null,
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 15.r, color: Colors.white),
            SizedBox(width: 5.w),
            CommonText(
              text: label,
              fontSize: 12.sp,
              fontWeight: FontWeight.w700,
              textColor: Colors.white,
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }

  // ── Results card (table + chart) ──────────────────────────────────────────────
  Widget _resultsCard() {
    return _card(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _sectionHeader('Hearing Data', Icons.table_chart_outlined),
          SizedBox(height: 10.h),
          _hearingTable(),
          SizedBox(height: 16.h),
          _sectionHeader('Pure-Tone Audiogram', Icons.show_chart_rounded),
          SizedBox(height: 10.h),
          _audiogramChart(),
        ],
      ),
    );
  }

  // Table with rounded border container
  Widget _hearingTable() {
    return ClipRRect(
      borderRadius: BorderRadius.circular(8.r),
      child: Container(
        decoration: BoxDecoration(
          border: Border.all(color: kTextFieldBorder),
          borderRadius: BorderRadius.circular(8.r),
        ),
        child: Column(children: [_hearingTableHeader(), _hearingTableRows()]),
      ),
    );
  }

  Widget _hearingTableHeader() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 10.h),
      child: Row(
        children: [
          _headerCell('Freq (Hz)', flex: 2),
          _headerDivider(),
          _headerCell('Left (dB)', flex: 2),
          _headerDivider(),
          _headerCell('Right (dB)', flex: 2),
        ],
      ),
    );
  }

  Widget _headerCell(String text, {required int flex}) {
    return Expanded(
      flex: flex,
      child: CommonText(
        text: text,
        fontSize: 12.sp,
        fontWeight: FontWeight.w600,
        textColor: kWhiteColor,
        textAlign: TextAlign.center,
      ),
    );
  }

  Widget _headerDivider() =>
      Container(width: 1, height: 20.h, color: Colors.white30);

  Widget _hearingTableRows() {
    return Obx(
      () => Column(
        children:
            controller.hearingRecords.asMap().entries.map((entry) {
              final i = entry.key;
              final r = entry.value;
              final isEven = i % 2 == 0;
              return Container(
                color: isEven ? kWhiteColor : const Color(0xFFF7F6FF),
                padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 9.h),
                child: Row(
                  children: [
                    Expanded(
                      flex: 2,
                      child: CommonText(
                        text: '${r.frequency}',
                        fontSize: 12.sp,
                        fontWeight: FontWeight.w500,
                        textColor: kTextColor,
                        textAlign: TextAlign.center,
                      ),
                    ),
                    Container(width: 1, height: 18.h, color: kTextFieldBorder),
                    Expanded(
                      flex: 2,
                      child: CommonText(
                        text: '${r.leftDb}',
                        fontSize: 12.sp,
                        fontWeight: FontWeight.w500,
                        textColor: Colors.blue.shade700,
                        textAlign: TextAlign.center,
                      ),
                    ),
                    Container(width: 1, height: 18.h, color: kTextFieldBorder),
                    Expanded(
                      flex: 2,
                      child: CommonText(
                        text: '${r.rightDb}',
                        fontSize: 12.sp,
                        fontWeight: FontWeight.w500,
                        textColor: Colors.red.shade700,
                        textAlign: TextAlign.center,
                      ),
                    ),
                  ],
                ),
              );
            }).toList(),
      ),
    );
  }

  // Audiogram chart
  Widget _audiogramChart() {
    return Column(
      children: [
        Obx(() => SizedBox(height: 240.h, child: LineChart(_buildChartData()))),
        SizedBox(height: 10.h),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            _legendItem(color: Colors.blue, symbol: '×', label: 'Left Ear'),
            SizedBox(width: 20.w),
            _legendItem(color: Colors.red, symbol: '○', label: 'Right Ear'),
          ],
        ),
      ],
    );
  }

  Widget _legendItem({
    required Color color,
    required String symbol,
    required String label,
  }) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        CommonText(
          text: symbol,
          fontSize: 16.sp,
          fontWeight: FontWeight.w700,
          textColor: color,
          textAlign: TextAlign.center,
        ),
        SizedBox(width: 5.w),
        CommonText(
          text: label,
          fontSize: 11.sp,
          fontWeight: FontWeight.w500,
          textColor: kLabelTextColor,
          textAlign: TextAlign.left,
        ),
      ],
    );
  }

  // Chart matches native app: Y=0 at bottom → 80 at top, X labels on top.
  LineChartData _buildChartData() {
    final records = controller.hearingRecords;
    final leftSpots = <FlSpot>[];
    final rightSpots = <FlSpot>[];
    for (int i = 0; i < records.length; i++) {
      leftSpots.add(FlSpot(i.toDouble(), records[i].leftDb.toDouble()));
      rightSpots.add(FlSpot(i.toDouble(), records[i].rightDb.toDouble()));
    }

    return LineChartData(
      minY: -5,
      maxY: 88,
      minX: 0,
      maxX: (AudioScreeningController.frequencies.length - 1).toDouble(),
      clipData: const FlClipData.all(),
      gridData: FlGridData(
        show: true,
        drawVerticalLine: true,
        horizontalInterval: 10,
        verticalInterval: 1,
        getDrawingHorizontalLine:
            (_) => FlLine(color: Colors.grey.shade200, strokeWidth: 1),
        getDrawingVerticalLine:
            (_) => FlLine(color: Colors.grey.shade200, strokeWidth: 1),
      ),
      titlesData: FlTitlesData(
        leftTitles: AxisTitles(
          axisNameWidget: Text(
            'dB HL',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
            ),
          ),
          sideTitles: SideTitles(
            showTitles: true,
            reservedSize: 36.w,
            interval: 10,
            getTitlesWidget: (val, _) {
              final db = val.round();
              if (db < 0 || db > 80 || db % 10 != 0) return const SizedBox();
              return Padding(
                padding: EdgeInsets.only(right: 4.w),
                child: Text(
                  '$db',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 9.sp,
                    color: kLabelTextColor,
                  ),
                  textAlign: TextAlign.right,
                ),
              );
            },
          ),
        ),
        topTitles: AxisTitles(
          axisNameWidget: Text(
            'Frequency (Hz)',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 10.sp,
            ),
          ),
          sideTitles: SideTitles(
            showTitles: true,
            reservedSize: 32.h,
            interval: 1,
            getTitlesWidget: (val, _) {
              const labels = [
                '125',
                '250',
                '500',
                '1000',
                '2000',
                '4000',
                '8000',
              ];
              final idx = val.toInt();
              if (idx < 0 || idx >= labels.length) return const SizedBox();
              return Padding(
                padding: EdgeInsets.only(bottom: 4.h),
                child: Text(
                  labels[idx],
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 8.sp,
                    color: kLabelTextColor,
                  ),
                  textAlign: TextAlign.center,
                ),
              );
            },
          ),
        ),
        bottomTitles: const AxisTitles(
          sideTitles: SideTitles(showTitles: false),
        ),
        rightTitles: const AxisTitles(
          sideTitles: SideTitles(showTitles: false),
        ),
      ),
      borderData: FlBorderData(
        show: true,
        border: Border.all(color: kTextFieldBorder),
      ),
      lineBarsData: [
        LineChartBarData(
          spots: leftSpots,
          isCurved: false,
          color: Colors.blue,
          barWidth: 2,
          dotData: FlDotData(
            show: true,
            getDotPainter:
                (spot, percent, bar, index) => const _CrossCirclePainter(),
          ),
        ),
        LineChartBarData(
          spots: rightSpots,
          isCurved: false,
          color: Colors.red,
          barWidth: 2,
          dotData: FlDotData(
            show: true,
            getDotPainter:
                (spot, percent, bar, index) => const _HollowRingPainter(),
          ),
        ),
      ],
    );
  }

  // ── Remarks card ──────────────────────────────────────────────────────────────
  Widget _remarksCard(BuildContext context) {
    return _card(
      child: Obx(() {
        final leftEnabled = controller.leftEarEnabled;
        final rightEnabled = controller.rightEarEnabled;
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _sectionHeader('Ear Remarks', Icons.comment_outlined),
            SizedBox(height: 10.h),
            Row(
              children: [
                Expanded(
                  child: AbsorbPointer(
                    absorbing: !leftEnabled,
                    child: Opacity(
                      opacity: leftEnabled ? 1.0 : 0.45,
                      child: AppTextField(
                        controller: _leftRemarkCtrl,
                        readOnly: true,
                        label: const Text('Left Ear Remark *'),
                        suffixIcon: const Icon(Icons.arrow_drop_down),
                        onTap: () => _showRemarkPicker(context, isLeft: true),
                      ),
                    ),
                  ),
                ),
                SizedBox(width: 10.w),
                Expanded(
                  child: AbsorbPointer(
                    absorbing: !rightEnabled,
                    child: Opacity(
                      opacity: rightEnabled ? 1.0 : 0.45,
                      child: AppTextField(
                        controller: _rightRemarkCtrl,
                        readOnly: true,
                        label: const Text('Right Ear Remark *'),
                        suffixIcon: const Icon(Icons.arrow_drop_down),
                        onTap: () => _showRemarkPicker(context, isLeft: false),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ],
        );
      }),
    );
  }

  void _showRemarkPicker(BuildContext context, {required bool isLeft}) {
    final currentRemark =
        isLeft
            ? controller.leftEarRemark.value
            : controller.rightEarRemark.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<String, String>(
            title: isLeft ? 'Left Ear Remark' : 'Right Ear Remark',
            items: AudioScreeningController.remarkOptions,
            valueFor: (item) => item,
            labelFor: (item) => item,
            selectedValue: currentRemark.isEmpty ? null : currentRemark,
            height: 360.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              if (isLeft) {
                controller.onLeftRemarkChanged(item);
              } else {
                controller.onRightRemarkChanged(item);
              }
            },
          ),
    );
  }

  // ── Shared helpers ────────────────────────────────────────────────────────────
  Widget _card({required Widget child}) {
    return Container(
      width: double.infinity,
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
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
      child: child,
    );
  }

  // Section header: accent bar | icon | title ─────
  Widget _sectionHeader(String title, IconData icon) {
    return Row(
      children: [
        Container(
          width: 3.w,
          height: 18.h,
          decoration: BoxDecoration(
            color: kPrimaryColor,
            borderRadius: BorderRadius.circular(2.r),
          ),
        ),
        SizedBox(width: 8.w),
        Icon(icon, size: 15.r, color: kPrimaryColor),
        SizedBox(width: 5.w),
        CommonText(
          text: title,
          fontSize: 13.sp,
          fontWeight: FontWeight.w700,
          textColor: kPrimaryColor,
          textAlign: TextAlign.left,
        ),
        SizedBox(width: 8.w),
        Expanded(child: Divider(color: Colors.grey.shade200, thickness: 1.h)),
      ],
    );
  }
}
