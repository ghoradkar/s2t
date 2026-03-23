import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'dart:math' as math;
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/ConductedCampsTotals.dart';
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/TodaysPatientsResponse.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/ConductedCampScreen.dart';

class AdminDashboardWidget extends StatefulWidget {
  final ConductedCampsTotals? conductedTotals;
  final TodaysPatientsTotals? todaysTotals;

  const AdminDashboardWidget({
    super.key,
    this.conductedTotals,
    this.todaysTotals,
  });

  @override
  State<AdminDashboardWidget> createState() => _AdminDashboardWidgetState();
}

class _AdminDashboardWidgetState extends State<AdminDashboardWidget>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 16.w),
      child: Column(
        children: [
          SizedBox(height: 30.h),

          // Tab Bar
          Container(
            height: 45.h,
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(30.r),
              border: Border.all(color: Colors.grey.shade300),
            ),
            child: TabBar(
              controller: _tabController,
              indicatorSize: TabBarIndicatorSize.tab,
              indicatorPadding: EdgeInsets.zero,
              splashFactory: NoSplash.splashFactory,
              overlayColor: MaterialStateProperty.all(Colors.transparent),
              dividerColor: Colors.transparent,
              indicator: BoxDecoration(
                borderRadius: BorderRadius.circular(25.r),
                gradient: LinearGradient(
                  colors: [kPrimaryColor, kPrimaryColor],
                ),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.15),
                    blurRadius: 8,
                    offset: const Offset(0, 3),
                  ),
                ],
              ),
              labelColor: Colors.white,
              unselectedLabelColor: Colors.grey.shade600,
              labelStyle: TextStyle(
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
              ),
              unselectedLabelStyle: TextStyle(
                fontSize: 13.sp,
                fontWeight: FontWeight.w500,
              ),
              tabs: const [
                Tab(text: "Conducted Camps"),
                Tab(text: "Today's Patients"),
              ],
            ),
          ),
          SizedBox(height: 25.h),

          // Tab Bar View
          Container(
            height: 316.h,
            decoration: BoxDecoration(
              color: Colors.transparent,
              borderRadius: BorderRadius.circular(16),
            ),
            child: TabBarView(
              controller: _tabController,
              children: [
                _buildConductedCampsView(() {
                  Get.to(
                    ConductedCampsScreen(
                      isDistAndYearVisiable: true,
                      title: 'Conducted Camps',
                      totalString: 'Total Camps Status',
                    ),
                  );
                }),
                _buildTodaysPatientsView(() {
                  Get.to(
                    ConductedCampsScreen(
                      isDistAndYearVisiable: false,
                      title: 'All Camp List',
                      totalString: 'Total',
                    ),
                  );
                }),
              ],
            ),
          ),
          SizedBox(height: 15.h),
        ],
      ),
    );
  }

  Widget _buildConductedCampsView(Function onTapConducted) {
    ConductedCampsTotals? totals = widget.conductedTotals;
    // if (totals == null) {
    //   return const Center(child: CircularProgressIndicator());
    // }

    final total = totals?.total ?? 0;
    final regular = totals?.regular ?? 0;
    final d2d = totals?.d2d ?? 0;

    // Create segments for pie chart - only add if value > 0
    final segments = <PieSegment>[];
    if (regular > 0) {
      segments.add(
        PieSegment(
          value: regular.toDouble(),
          color: const Color(0xFFEDF8B1),
          label: 'Regular',
        ),
      );
    }
    if (d2d > 0) {
      segments.add(
        PieSegment(
          value: d2d.toDouble(),
          color: const Color(0xFF7FCDBB),
          label: 'D2D',
        ),
      );
    }

    return InkWell(
      onTap: () {
        onTapConducted();
      },
      child: Padding(
        padding: EdgeInsets.all(6.w),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20.r),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.15),
                blurRadius: 5,
                spreadRadius: 1,
                offset: Offset.zero, // all sides
              ),
            ],
          ),
          child: Stack(
            children: [
              Column(
                children: [
                  // Header
                  Align(
                    alignment: Alignment.centerRight,
                    child: CommonText(
                      text: "*FY(${totals?.financialYear ?? "0"})",
                      textAlign: TextAlign.end,
                      fontSize: 11.sp,
                      textColor: kTextBlackColor,
                      fontWeight: FontWeight.normal,
                    ).paddingOnly(right: 8.w, top: 6.h),
                  ),

                  // BODY
                  Expanded(
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        // Pie
                        Flexible(
                          flex: 6,
                          child: Center(
                            child: SizedBox(
                              width: 230.w,
                              height: 230.w,
                              child: Stack(
                                alignment: Alignment.center,
                                children: [
                                  CustomPaint(
                                    size: Size(230.w, 230.w),
                                    painter: PieChartPainter(
                                      segments: segments,
                                    ),
                                  ),
                                  Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      CommonText(
                                        text: 'Total',
                                        fontSize: 16.sp,
                                        textColor: kBlackColor,
                                        textAlign: TextAlign.center,
                                        fontWeight: FontWeight.w400,
                                      ),
                                      CommonText(
                                        text: FormatterManager.formatCompact(
                                          total,
                                        ),
                                        fontSize: 18.sp,
                                        fontWeight: FontWeight.bold,
                                        textAlign: TextAlign.center,
                                        textColor: kBlackColor,
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),

                        SizedBox(width: 24.w),

                        Flexible(
                          flex: 4,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              _buildLegendItem(
                                color: const Color(0xFFEDF8B1),
                                label: 'Regular',
                                value: FormatterManager.formatCompact(regular),
                                icon: icRegularAdmin,
                              ),
                              SizedBox(height: 20.h),
                              _buildLegendItem(
                                color: const Color(0xFF7FCDBB),
                                label: 'D2D',
                                value: FormatterManager.formatCompact(d2d),
                                icon: icD2dAdmin,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ).paddingOnly(left: 20.w, bottom: 16.h),
                  ),
                ],
              ),

              // 👁 Eye icon OVERLAY (does NOT affect size)
              // Positioned(
              //   right: 10.w,
              //   bottom: 10.h,
              //   child: Image.asset(
              //     view,
              //     width: 30.w,
              //     height: 30.h,
              //     color: kPrimaryColor,
              //   ),
              // ),
              Positioned(
                right: 10.w,
                bottom: 10.h,
                child: Container(
                  width: responsiveHeight(40),
                  height: responsiveHeight(40),
                  decoration: _eyeButtonDecoration,
                  child: Center(
                    child: Icon(
                      Icons.remove_red_eye_outlined,
                      color: kWhiteColor,
                      size: 23,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  static final _eyeButtonDecoration = BoxDecoration(
    color: kPrimaryColor.withValues(alpha: 0.85),
    borderRadius: BorderRadius.circular(10),
  );

  Widget _buildTodaysPatientsView(Function onTapTodaysPatient) {
    TodaysPatientsTotals? totals = widget.todaysTotals;
    // if (totals == null) {
    //   return const Center(child: CircularProgressIndicator());
    // }

    int? total = totals?.total ?? 0;
    int? regular = totals?.regular ?? 0;
    int? d2d = totals?.d2d ?? 0;

    // Create segments for pie chart - only add if value > 0
    final segments = <PieSegment>[];
    if (regular > 0) {
      segments.add(
        PieSegment(
          value: regular.toDouble(),
          color: const Color(0xFFEDF8B1),
          label: 'Regular',
        ),
      );
    }
    if (d2d > 0) {
      segments.add(
        PieSegment(
          value: d2d.toDouble(),
          color: const Color(0xFF7FCDBB),
          label: 'D2D',
        ),
      );
    }

    return InkWell(
      onTap: () {
        onTapTodaysPatient();
      },
      child: Padding(
        padding: EdgeInsets.all(6.w),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20.r),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withOpacity(0.15),
                blurRadius: 5,
                spreadRadius: 1,
                offset: Offset.zero, // all sides
              ),
            ],
          ),
          child: Stack(
            children: [
              Column(
                children: [
                  // Header
                  Align(
                    alignment: Alignment.centerRight,
                    child: CommonText(
                      text:
                          "*Data as of : ${DateTime.now().toString().substring(5, 10).replaceAll('-', '/')}/${DateTime.now().year} | ${TimeOfDay.now().format(context)}",
                      fontSize: 11.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kTextBlackColor,
                      textAlign: TextAlign.end,
                    ).paddingOnly(right: 8.w, top: 2.h),
                  ),

                  // BODY
                  Expanded(
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        // Pie
                        Flexible(
                          flex: 6,
                          child: Center(
                            child: SizedBox(
                              width: 230.w, // 🔥 increase diameter safely
                              height: 230.w,
                              child: Stack(
                                alignment: Alignment.center,
                                children: [
                                  CustomPaint(
                                    size: Size(230.w, 230.w),
                                    painter: PieChartPainter(
                                      segments: segments,
                                    ),
                                  ),
                                  Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      CommonText(
                                        text: 'Total',
                                        fontSize: 16.sp,
                                        textColor: kBlackColor,
                                        fontWeight: FontWeight.w400,
                                        textAlign: TextAlign.center,
                                      ),
                                      CommonText(
                                        text: FormatterManager.formatCompact(
                                          total,
                                        ),
                                        fontSize: 18.sp,
                                        fontWeight: FontWeight.bold,
                                        textColor: kBlackColor,
                                        textAlign: TextAlign.center,
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),

                        SizedBox(width: 24.w),

                        Flexible(
                          flex: 4,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              _buildLegendItem(
                                color: const Color(0xFFEDF8B1),
                                label: 'Regular',
                                value: FormatterManager.formatCompact(regular),
                                icon: icRegularAdmin,
                              ),
                              SizedBox(height: 20.h),
                              _buildLegendItem(
                                color: const Color(0xFF7FCDBB),
                                label: 'D2D',
                                value: FormatterManager.formatCompact(d2d),
                                icon: icD2dAdmin,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ).paddingOnly(left: 20.w, bottom: 16.h),
                  ),
                ],
              ),

              // 👁 Eye icon OVERLAY (does NOT affect size)
              Positioned(
                right: 10.w,
                bottom: 10.h,
                child: Image.asset(
                  view,
                  width: 30.w,
                  height: 30.h,
                  color: kPrimaryColor,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildLegendItem({
    required Color color,
    required String label,
    required String value,
    required String icon,
  }) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Image.asset(icon, width: 36.w, height: 36.h),
        SizedBox(width: 12.w),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CommonText(
              text: value,
              fontSize: 18.sp,
              fontWeight: FontWeight.w600,
              textColor: kTextBlackColor,
              textAlign: TextAlign.start,
            ),
            CommonText(
              text: label,
              fontSize: 13.sp,
              fontWeight: FontWeight.normal,
              textColor: kTextBlackColor,
              textAlign: TextAlign.start,
            ),
          ],
        ),
      ],
    );
  }
}

// Pie Chart Data model
class PieSegment {
  final double value;
  final Color color;
  final String label;

  PieSegment({required this.value, required this.color, required this.label});
}

class PieChartPainter extends CustomPainter {
  final List<PieSegment> segments;

  PieChartPainter({required this.segments});

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = math.min(size.width, size.height) / 2;
    const strokeWidth = 35.0;

    if (segments.isEmpty ||
        segments.fold<double>(0, (s, e) => s + e.value) == 0) {
      canvas.drawCircle(
        center,
        radius - strokeWidth / 2,
        Paint()
          ..color = Colors.grey.shade300
          ..style = PaintingStyle.stroke
          ..strokeWidth = strokeWidth,
      );
      return;
    }

    final total = segments.fold<double>(0, (s, e) => s + e.value);
    double startAngle = -math.pi / 2;

    /// -------- DRAW RING --------
    for (final seg in segments) {
      final sweep = seg.value / total * 2 * math.pi;
      canvas.drawArc(
        Rect.fromCircle(center: center, radius: radius - strokeWidth / 2),
        startAngle,
        sweep - 0.01,
        false,
        Paint()
          ..color = seg.color
          ..style = PaintingStyle.stroke
          ..strokeWidth = strokeWidth,
      );
      startAngle += sweep;
    }

    /// -------- DRAW LABELS --------
    startAngle = -math.pi / 2;

    for (final seg in segments) {
      final sweep = seg.value / total * 2 * math.pi;
      final mid = startAngle + sweep / 2;

      final text =
          seg.value >= 1000
              ? '${(seg.value / 1000).round()}k ${seg.label}'
              : '${seg.value.toInt()} ${seg.label}';

      final tp = TextPainter(
        text: TextSpan(
          text: seg.label,
          style: const TextStyle(
            fontSize: 12,
            fontWeight: FontWeight.w600,
            color: Colors.black87,
          ),
        ),
        textDirection: TextDirection.ltr,
      )..layout();

      /// Position label - reduced distance
      final labelDist = radius + 22; // Reduced from 28 to 22
      final labelCenter = Offset(
        center.dx + labelDist * math.cos(mid),
        center.dy + labelDist * math.sin(mid),
      );

      const padding = EdgeInsets.symmetric(horizontal: 12, vertical: 6);
      const arrowSize = 6.0; // Reduced from 10.0 to 6.0
      const r = 10.0;

      /// Determine arrow side
      final dx = math.cos(mid);
      final dy = math.sin(mid);

      // Calculate rect position based on arrow direction
      Rect rect;
      final baseRect = Rect.fromCenter(
        center: labelCenter,
        width: tp.width + padding.horizontal,
        height: tp.height + padding.vertical,
      );

      if (dy > 0.4) {
        // Arrow UP - shift rect down
        rect = baseRect.shift(const Offset(0, arrowSize));
      } else if (dy < -0.4) {
        // Arrow DOWN - no shift needed
        rect = baseRect;
      } else if (dx > 0) {
        // Arrow LEFT - shift rect right
        rect = baseRect.shift(const Offset(arrowSize, 0));
      } else {
        // Arrow RIGHT - no shift needed
        rect = baseRect;
      }

      /// Arrow tip touches ring
      final tip = Offset(
        center.dx + (radius - strokeWidth / 8) * math.cos(mid),
        center.dy + (radius - strokeWidth / 8) * math.sin(mid),
      );

      final path = Path();

      if (dy > 0.4) {
        /// Arrow UP
        path
          ..addRRect(RRect.fromRectAndRadius(rect, const Radius.circular(r)))
          ..moveTo(rect.center.dx - arrowSize, rect.top)
          ..lineTo(tip.dx, tip.dy)
          ..lineTo(rect.center.dx + arrowSize, rect.top);
      } else if (dy < -0.4) {
        /// Arrow DOWN
        path
          ..addRRect(RRect.fromRectAndRadius(rect, const Radius.circular(r)))
          ..moveTo(rect.center.dx - arrowSize, rect.bottom)
          ..lineTo(tip.dx, tip.dy)
          ..lineTo(rect.center.dx + arrowSize, rect.bottom);
      } else if (dx > 0) {
        /// Arrow LEFT
        path
          ..addRRect(RRect.fromRectAndRadius(rect, const Radius.circular(r)))
          ..moveTo(rect.left, rect.center.dy - arrowSize)
          ..lineTo(tip.dx, tip.dy)
          ..lineTo(rect.left, rect.center.dy + arrowSize);
      } else {
        /// Arrow RIGHT
        path
          ..addRRect(RRect.fromRectAndRadius(rect, const Radius.circular(r)))
          ..moveTo(rect.right, rect.center.dy - arrowSize)
          ..lineTo(tip.dx, tip.dy)
          ..lineTo(rect.right, rect.center.dy + arrowSize);
      }

      /// Shadow
      canvas.drawShadow(path, Colors.black.withOpacity(0.18), 6, true);

      /// Fill
      canvas.drawPath(path, Paint()..color = Colors.white);

      /// Border
      canvas.drawPath(
        path,
        Paint()
          ..style = PaintingStyle.stroke
          ..color = Colors.grey.shade300
          ..strokeWidth = 1,
      );

      /// Text - now properly centered in the rect
      tp.paint(
        canvas,
        Offset(rect.center.dx - tp.width / 2, rect.center.dy - tp.height / 2),
      );

      startAngle += sweep;
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}
