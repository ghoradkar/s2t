// ignore_for_file: file_names

import 'dart:math' as math;
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Screens/super_admin/controller/super_admin_controller.dart';
import 'package:s2toperational/Screens/super_admin/widget/super_admin_card.dart';

class SuperAdminDashboard extends StatelessWidget {
  final SuperAdminController controller;

  const SuperAdminDashboard({super.key, required this.controller});

  @override
  Widget build(BuildContext context) {
    final adminController = controller;
    return Obx(() {
      if (adminController.isLoading.value) {
        return Column(
          children: [
            _SuperAdminGraphTabsSkeleton(),
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 12.w),
              child: Card(
                child: SizedBox(
                  height: 140.h,
                  child: CommonSkeletonInvoiceTable(
                    itemCount: 4,
                    rowHeight: 30,
                    shrinkWrap: false,
                    physics: const NeverScrollableScrollPhysics(),
                  ),
                ),
              ),
            ),
            Card(
              child: SizedBox(
                height: 140.h,
                child: CommonSkeletonInvoiceTable(
                  itemCount: 4,
                  rowHeight: 30,
                  shrinkWrap: false,
                  physics: const NeverScrollableScrollPhysics(),
                ),
              ),
            ).paddingOnly(left: 12.w, right: 12.w, top: 10.h),
          ],
        );
      }

      final output = adminController.conductedCardSuperAdmin.value?.output ?? [];
      final details = adminController.todaysTableCount.value?.details;
      final totalDetails = adminController.totalTableCount.value?.details;

      // Today's Patients - Filter by SubOrgId
      final todaysOutput =
          adminController.todaysPatientCardSuperAdmin.value?.output ?? [];
      final todaysHllData = todaysOutput.firstWhereOrNull(
        (item) => item.subOrgId == 2,
      );
      final todaysHsccData = todaysOutput.firstWhereOrNull(
        (item) => item.subOrgId == 3,
      );

      // Conducted Camps - Filter by SubOrgId
      final conductedHllData = output.firstWhereOrNull(
        (item) => item.subOrgId == 2,
      );
      final conductedHsccData = output.firstWhereOrNull(
        (item) => item.subOrgId == 3,
      );

      final conductedGroups = [
        _SuperAdminBarGroup(
          label: "Regular",
          hll: conductedHllData?.regularCamp ?? 0,
          hscc: conductedHsccData?.regularCamp ?? 0,
        ),
        _SuperAdminBarGroup(
          label: "D2D",
          hll: conductedHllData?.d2dCamp ?? 0,
          hscc: conductedHsccData?.d2dCamp ?? 0,
        ),
      ];

      final todaysGroups = [
        _SuperAdminBarGroup(
          label: "Regular",
          hll: todaysHllData?.regularCamp ?? 0,
          hscc: todaysHsccData?.regularCamp ?? 0,
        ),
        _SuperAdminBarGroup(
          label: "D2D",
          hll: todaysHllData?.d2dCamp ?? 0,
          hscc: todaysHsccData?.d2dCamp ?? 0,
        ),
      ];

      final conductedSubtitle =
          output.isNotEmpty ? _formatFinancialYear(output.first.financialYear) : "";

      return Column(
        children: [
          _SuperAdminGraphTabs(
            conductedSubtitle: conductedSubtitle,
            conductedGroups: conductedGroups,
            todaysSubtitle: "",
            todaysGroups: todaysGroups,
          ),

          SuperAdminCard(
            enableRowColor: true,
            title: "",
            subtitle:
                "*Today's Data as of :${adminController.todaysTableCount.value?.dateTime ?? "-"}",
            headers: const ["", "", "HLL", "HSCC", "Total"],
            rows: [
              [
                "Treatment",
                "Total Treatment Given",
                details?.treatmentGivenHLL.toString() ?? "",
                details?.treatmentGivenHSCC.toString() ?? "",
                details?.treatmentGivenTotal.toString() ?? "",
              ],
              [
                "IPD",
                "Total IPD Registered Patient",
                details?.ipdRegisteredHLL.toString() ?? "",
                details?.ipdRegisteredHSCC.toString() ?? "",
                details?.ipdRegisteredTotal.toString() ?? "",
              ],
              [
                "IPD",
                "Total Discharge Patient",
                details?.dischargePatientHLL.toString() ?? "",
                details?.dischargePatientHSCC.toString() ?? "",
                details?.dischargePatientTotal.toString() ?? "",
              ],
              [
                "Medicine",
                "Prescription Given",
                details?.prescriptionGivenHLL.toString() ?? "",
                details?.prescriptionGivenHSCC.toString() ?? "",
                details?.prescriptionGivenTotal.toString() ?? "",
              ],
              [
                "Medicine",
                "Prescription Issued",
                details?.prescriptionIssuedHLL.toString() ?? "",
                details?.prescriptionIssuedHSCC.toString() ?? "",
                details?.prescriptionIssuedTotal.toString() ?? "",
              ],
            ],
          ).paddingOnly(bottom: 14),

          SuperAdminCard(
            enableRowColor: true,
            title: "",
            subtitle:
                "*Total Data as of : ${adminController.totalTableCount.value?.dateTime ?? "-"}",
            headers: const ["", "", "HLL", "HSCC", "Total"],
            rows: [
              [
                "Treatment",
                "Total Treatment Given",
                totalDetails?.treatmentGivenHLL.toString() ?? "",
                totalDetails?.treatmentGivenHSCC.toString() ?? "",
                totalDetails?.treatmentGivenTotal.toString() ?? "",
              ],
              [
                "IPD",
                "Total IPD Registered Patient",
                totalDetails?.ipdRegisteredHLL.toString() ?? "",
                totalDetails?.ipdRegisteredHSCC.toString() ?? "",
                totalDetails?.ipdRegisteredTotal.toString() ?? "",
              ],
              [
                "IPD",
                "Total Discharge Patient",
                totalDetails?.dischargePatientHLL.toString() ?? "",
                totalDetails?.dischargePatientHSCC.toString() ?? "",
                totalDetails?.dischargePatientTotal.toString() ?? "",
              ],
              [
                "Medicine",
                "Prescription Given",
                totalDetails?.prescriptionGivenHLL.toString() ?? "",
                totalDetails?.prescriptionGivenHSCC.toString() ?? "",
                totalDetails?.prescriptionGivenTotal.toString() ?? "",
              ],
              [
                "Medicine",
                "Prescription Issued",
                totalDetails?.prescriptionIssuedHLL.toString() ?? "",
                totalDetails?.prescriptionIssuedHSCC.toString() ?? "",
                totalDetails?.prescriptionIssuedTotal.toString() ?? "",
              ],
            ],
          ),
        ],
      ).paddingSymmetric(vertical: 6.h, horizontal: 10.h);
    });
  }

  String _formatFinancialYear(String raw) {
    final trimmed = raw.trim();
    if (trimmed.isEmpty) return "";
    if (trimmed.startsWith("*")) return trimmed;
    if (trimmed.toUpperCase().contains("FY")) {
      return "*$trimmed";
    }
    return "*FY($trimmed)";
  }
}

// ---------------------------------------------------------------------------
// Data model
// ---------------------------------------------------------------------------

class _SuperAdminBarGroup {
  final String label;
  final int hll;
  final int hscc;

  const _SuperAdminBarGroup({
    required this.label,
    required this.hll,
    required this.hscc,
  });

  int get total => hll + hscc;
}

// ---------------------------------------------------------------------------
// Graph tabs (loaded data)
// ---------------------------------------------------------------------------

class _SuperAdminGraphTabs extends StatelessWidget {
  final String conductedSubtitle;
  final List<_SuperAdminBarGroup> conductedGroups;
  final String todaysSubtitle;
  final List<_SuperAdminBarGroup> todaysGroups;

  const _SuperAdminGraphTabs({
    required this.conductedSubtitle,
    required this.conductedGroups,
    required this.todaysSubtitle,
    required this.todaysGroups,
  });

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 6.h),
        child: Column(
          children: [
            Container(
              height: 45.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(30.r),
                border: Border.all(color: Colors.grey.shade300),
              ),
              child: TabBar(
                indicatorSize: TabBarIndicatorSize.tab,
                indicatorPadding: EdgeInsets.zero,
                splashFactory: NoSplash.splashFactory,
                overlayColor: MaterialStateProperty.all(Colors.transparent),
                dividerColor: Colors.transparent,
                indicator: BoxDecoration(
                  borderRadius: BorderRadius.circular(25.r),
                  gradient: const LinearGradient(
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
            SizedBox(height: 12.h),
            SizedBox(
              height: 480.h,
              child: TabBarView(
                children: [
                  _SuperAdminBarChartCard(
                    subtitle: conductedSubtitle,
                    groups: conductedGroups,
                  ),
                  _SuperAdminBarChartCard(
                    subtitle: todaysSubtitle,
                    groups: todaysGroups,
                  ),
                ],
              ),
            ),
            SizedBox(height: 8.h),
          ],
        ),
      ),
    );
  }
}

class _SuperAdminBarChartCard extends StatelessWidget {
  final String subtitle;
  final List<_SuperAdminBarGroup> groups;

  const _SuperAdminBarChartCard({required this.subtitle, required this.groups});

  @override
  Widget build(BuildContext context) {
    const hllColor = Color(0xFF2AC1D1);
    const hsccColor = Color(0xFF8CD63F);

    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 4.h),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16.r),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.08),
              blurRadius: 8,
              spreadRadius: 0,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Padding(
          padding: EdgeInsets.fromLTRB(8.w, 12.h, 12.w, 16.h),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              CommonText(
                text: subtitle,
                fontSize: 12.sp,
                textColor: kTextBlackColor,
                fontWeight: FontWeight.w500,
                textAlign: TextAlign.end,
              ),
              SizedBox(height: 4.h),
              Expanded(
                child: _SuperAdminBarChart(
                  groups: groups,
                  hllColor: hllColor,
                  hsccColor: hsccColor,
                ),
              ),
              SizedBox(height: 8.h),
              _SuperAdminLegend(hllColor: hllColor, hsccColor: hsccColor),
            ],
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Graph tabs (skeleton / loading)
// ---------------------------------------------------------------------------

class _SuperAdminGraphTabsSkeleton extends StatelessWidget {
  const _SuperAdminGraphTabsSkeleton();

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 6.h),
        child: Column(
          children: [
            Container(
              height: 45.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(30.r),
                border: Border.all(color: Colors.grey.shade300),
              ),
              child: TabBar(
                indicatorSize: TabBarIndicatorSize.tab,
                indicatorPadding: EdgeInsets.zero,
                splashFactory: NoSplash.splashFactory,
                overlayColor: MaterialStateProperty.all(Colors.transparent),
                dividerColor: Colors.transparent,
                indicator: BoxDecoration(
                  borderRadius: BorderRadius.circular(25.r),
                  gradient: const LinearGradient(
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
            SizedBox(height: 12.h),
            SizedBox(
              height: 480.h,
              child: TabBarView(
                children: const [
                  CommonSkeletonSuperAdminBarChartCard(),
                  CommonSkeletonSuperAdminBarChartCard(),
                ],
              ),
            ),
            SizedBox(height: 8.h),
          ],
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Bar chart internals
// ---------------------------------------------------------------------------

class _SuperAdminBarChart extends StatelessWidget {
  final List<_SuperAdminBarGroup> groups;
  final Color hllColor;
  final Color hsccColor;

  const _SuperAdminBarChart({
    required this.groups,
    required this.hllColor,
    required this.hsccColor,
  });

  int _roundUpTo(int value, int step) {
    if (value <= 0) return step;
    return ((value + step - 1) ~/ step) * step;
  }

  int _calculateNiceStep(int maxValue) {
    if (maxValue <= 100) return 25;
    if (maxValue <= 500) return 100;
    if (maxValue <= 1000) return 250;
    if (maxValue <= 5000) return 1000;
    if (maxValue <= 10000) return 2000;
    if (maxValue <= 50000) return 10000;
    if (maxValue <= 100000) return 25000;
    return 50000;
  }

  @override
  Widget build(BuildContext context) {
    final maxValueRaw = groups.fold<int>(
      0,
      (maxValue, group) => math.max(maxValue, math.max(group.hll, group.hscc)),
    );
    final step = _calculateNiceStep(maxValueRaw);
    final maxValue = _roundUpTo(maxValueRaw, step);
    const tickCount = 5;

    final allNonZero = groups.every((g) => g.hll > 0 && g.hscc > 0);

    final ticks =
        List.generate(
              tickCount,
              (i) => (maxValue * i / (tickCount - 1)).round(),
            )
            .where((tick) => !allNonZero || tick > 0)
            .toList();

    return LayoutBuilder(
      builder: (context, constraints) {
        final availableHeight = constraints.maxHeight;
        final xAxisLabelHeight = 30.h;
        final chartHeight = availableHeight - xAxisLabelHeight;

        return Column(
          children: [
            SizedBox(
              height: chartHeight,
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Y-axis labels
                  SizedBox(
                    width: 45.w,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: ticks.reversed.map((value) {
                        return Padding(
                          padding: EdgeInsets.only(right: 8.w),
                          child: CommonText(
                            text: value.toString(),
                            fontSize: 10.sp,
                            textColor: Colors.grey.shade600,
                            fontWeight: FontWeight.w500,
                            textAlign: TextAlign.right,
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                  // Chart area
                  Expanded(
                    child: Stack(
                      clipBehavior: Clip.none,
                      children: [
                        // Grid lines
                        Positioned.fill(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: List.generate(
                              ticks.length,
                              (index) => Container(
                                height: 1,
                                color: Colors.grey.withOpacity(0.25),
                              ),
                            ),
                          ),
                        ),
                        // Bar groups with floating total badges
                        Positioned.fill(
                          child: Row(
                            children: _buildBarGroups(
                              maxValue.toDouble(),
                              chartHeight,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 8.h),
            // X-axis labels
            SizedBox(
              height: xAxisLabelHeight - 8.h,
              child: Padding(
                padding: EdgeInsets.only(left: 45.w),
                child: Row(
                  children: groups
                      .map(
                        (group) => Expanded(
                          child: CommonText(
                            text: group.label,
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w600,
                            textColor: kTextBlackColor,
                            textAlign: TextAlign.center,
                          ),
                        ),
                      )
                      .toList(),
                ),
              ),
            ),
          ],
        );
      },
    );
  }

  List<Widget> _buildBarGroups(double maxValue, double chartHeight) {
    final barWidth = 48.w;
    final barSpacing = 8.w;

    return groups.map((group) {
      final hllHeight =
          maxValue <= 0 ? 0.0 : (group.hll / maxValue) * chartHeight;
      final hsccHeight =
          maxValue <= 0 ? 0.0 : (group.hscc / maxValue) * chartHeight;

      final visibleHeights = <double>[];
      if (group.hll > 0) visibleHeights.add(hllHeight);
      if (group.hscc > 0) visibleHeights.add(hsccHeight);
      final maxBarHeight =
          visibleHeights.isNotEmpty
              ? visibleHeights.reduce((a, b) => math.max(a, b))
              : 0.0;

      final labelAboveBarHeight = 10.sp + 4.h;
      final hasLabelAboveBar = maxBarHeight < 40.h && visibleHeights.isNotEmpty;
      final badgeBottomOffset =
          hasLabelAboveBar
              ? maxBarHeight + labelAboveBarHeight + 10.h
              : maxBarHeight + 10.h;

      final showTotalBadge = group.total > 0;

      return Expanded(
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            Positioned.fill(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  _BarWithLabel(
                    height: hllHeight,
                    color: hllColor,
                    width: barWidth,
                    label: group.hll.toString(),
                    value: group.hll,
                  ),
                  SizedBox(width: barSpacing),
                  _BarWithLabel(
                    height: hsccHeight,
                    color: hsccColor,
                    width: barWidth,
                    label: group.hscc.toString(),
                    value: group.hscc,
                  ),
                ],
              ),
            ),
            if (showTotalBadge)
              Positioned(
                bottom: badgeBottomOffset,
                left: 0,
                right: 0,
                child: Center(
                  child: Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: 6.w,
                      vertical: 6.h,
                    ),
                    decoration: BoxDecoration(
                      color: const Color(0xFFE8E0F5),
                      borderRadius: BorderRadius.circular(10.r),
                    ),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        CommonText(
                          text: "${group.label} Total",
                          fontSize: 10.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kPrimaryColor,
                          textAlign: TextAlign.center,
                        ),
                        CommonText(
                          text: group.total.toString(),
                          fontSize: 10.sp,
                          fontWeight: FontWeight.w700,
                          textColor: kPrimaryColor,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
          ],
        ),
      );
    }).toList();
  }
}

class _BarWithLabel extends StatelessWidget {
  final double height;
  final Color color;
  final double width;
  final String label;
  final int value;

  const _BarWithLabel({
    required this.height,
    required this.color,
    required this.width,
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    if (value <= 0) {
      return SizedBox(width: width);
    }

    final showLabelInside = height >= 40.h;

    if (showLabelInside) {
      return Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(6.r),
        ),
        child: Padding(
          padding: EdgeInsets.only(top: height * 0.25),
          child: Align(
            alignment: Alignment.topCenter,
            child: CommonText(
              text: label,
              fontSize: 10.sp,
              fontWeight: FontWeight.bold,
              textColor: Colors.white,
              textAlign: TextAlign.center,
            ),
          ),
        ),
      );
    } else {
      return Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          CommonText(
            text: label,
            fontSize: 10.sp,
            fontWeight: FontWeight.bold,
            textColor: color,
            textAlign: TextAlign.center,
          ),
          SizedBox(height: 4.h),
          Container(
            width: width,
            height: height,
            decoration: BoxDecoration(
              color: color,
              borderRadius: BorderRadius.circular(6.r),
            ),
          ),
        ],
      );
    }
  }
}

class _SuperAdminLegend extends StatelessWidget {
  final Color hllColor;
  final Color hsccColor;

  const _SuperAdminLegend({required this.hllColor, required this.hsccColor});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        _LegendItem(color: hllColor, label: "HLL"),
        SizedBox(width: 30.w),
        _LegendItem(color: hsccColor, label: "HSCC"),
      ],
    );
  }
}

class _LegendItem extends StatelessWidget {
  final Color color;
  final String label;

  const _LegendItem({required this.color, required this.label});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 14.w,
          height: 14.w,
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(3.r),
          ),
        ),
        SizedBox(width: 8.w),
        CommonText(
          text: label,
          fontSize: 13.sp,
          fontWeight: FontWeight.w600,
          textColor: kTextBlackColor,
          textAlign: TextAlign.center,
        ),
      ],
    );
  }
}