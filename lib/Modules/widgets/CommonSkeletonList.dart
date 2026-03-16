import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:skeletonizer/skeletonizer.dart';

import '../utilities/SizeConfig.dart';

class CommonSkeletonList extends StatelessWidget {
  final int itemCount;
  final EdgeInsets itemPadding;
  final double cardRadius;

  const CommonSkeletonList({
    super.key,
    this.itemCount = 6,
    this.itemPadding = const EdgeInsets.only(
      top: 10,
      bottom: 4,
      left: 8,
      right: 8,
    ),
    this.cardRadius = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: ListView.builder(
        shrinkWrap: true,
        itemCount: itemCount,
        itemBuilder: (context, index) {
          return Padding(
            padding: EdgeInsets.only(
              top: responsiveHeight(itemPadding.top),
              bottom: responsiveHeight(itemPadding.bottom),
              left: responsiveWidth(itemPadding.left),
              right: responsiveWidth(itemPadding.right),
            ),
            child: Container(
              decoration: BoxDecoration(
                boxShadow: [
                  BoxShadow(
                    blurRadius: responsiveHeight(10),
                    spreadRadius: 0,
                    offset: const Offset(0, 1),
                    color: const Color(0xFF000000).withValues(alpha: 0.15),
                  ),
                ],
                borderRadius: BorderRadius.circular(
                  responsiveHeight(cardRadius),
                ),
                color: Colors.white,
              ),
              child: Padding(
                padding: const EdgeInsets.symmetric(
                  vertical: 12,
                  horizontal: 12,
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          height: 14.h,
                          width: 24.w,
                          color: Colors.white,
                        ),
                        SizedBox(width: 8.w),
                        Expanded(
                          child: Container(height: 14.h, color: Colors.white),
                        ),
                        SizedBox(width: 12.w),
                        Container(
                          height: 38.h,
                          width: 38.h,
                          decoration: const BoxDecoration(
                            shape: BoxShape.circle,
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(8)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.5,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(8)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.65,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(8)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.45,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(8)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.6,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(16)),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    // Row(
                    //   mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    //   children: [
                    //     Container(
                    //       height: 18.h,
                    //       width: 90.w,
                    //       decoration: BoxDecoration(
                    //         borderRadius: BorderRadius.circular(5),
                    //         color: Colors.white,
                    //       ),
                    //     ),
                    //     Container(
                    //       height: 36.h,
                    //       width: 36.h,
                    //       decoration: const BoxDecoration(
                    //         shape: BoxShape.circle,
                    //         color: Colors.white,
                    //       ),
                    //     ),
                    //   ],
                    // ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}

class CommonSkeletonPatientList extends StatelessWidget {
  final int itemCount;
  final EdgeInsets itemPadding;
  final double cardRadius;

  const CommonSkeletonPatientList({
    super.key,
    this.itemCount = 6,
    this.itemPadding = const EdgeInsets.only(
      top: 2,
      bottom: 8,
      left: 2,
      right: 2,
    ),
    this.cardRadius = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: ListView.builder(
        shrinkWrap: true,
        itemCount: itemCount,
        itemBuilder: (context, index) {
          return Padding(
            padding: EdgeInsets.only(
              top: responsiveHeight(itemPadding.top),
              bottom: responsiveHeight(itemPadding.bottom),
              left: responsiveWidth(itemPadding.left),
              right: responsiveWidth(itemPadding.right),
            ),
            child: Container(
              decoration: BoxDecoration(
                boxShadow: [
                  BoxShadow(
                    blurRadius: responsiveHeight(10),
                    spreadRadius: 0,
                    offset: const Offset(0, 1),
                    color: const Color(0xFF000000).withValues(alpha: 0.15),
                  ),
                ],
                borderRadius: BorderRadius.circular(
                  responsiveHeight(cardRadius),
                ),
                color: Colors.white,
              ),
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 4, horizontal: 4),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Container(
                          height: 14.h,
                          width: 24.w,
                          color: Colors.white,
                        ),
                        SizedBox(width: 8.w),
                        Expanded(
                          child: Container(height: 14.h, color: Colors.white),
                        ),
                        SizedBox(width: 12.w),
                        Container(
                          height: 38.h,
                          width: 38.h,
                          decoration: const BoxDecoration(
                            shape: BoxShape.circle,
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                    SizedBox(height: responsiveHeight(10)),
                    Container(
                      height: 12.h,
                      width: SizeConfig.screenWidth * 0.55,
                      color: Colors.white,
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}

class CommonSkeletonAdminDashboard extends StatelessWidget {
  const CommonSkeletonAdminDashboard({super.key});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 16.w),
      child: Column(
        children: [
          SizedBox(height: 30.h),
          Container(
            height: 45.h,
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(30.r),
              border: Border.all(color: Colors.grey.shade300),
            ),
            child: Row(
              children: [
                Expanded(
                  child: Container(
                    height: double.infinity,
                    decoration: BoxDecoration(
                      color: kPrimaryColor,
                      borderRadius: BorderRadius.circular(25.r),
                    ),
                    alignment: Alignment.center,
                    child: Text(
                      "Conducted Camps",
                      style: TextStyle(
                        fontSize: 13.sp,
                        fontWeight: FontWeight.w600,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Container(
                    height: double.infinity,
                    alignment: Alignment.center,
                    child: Text(
                      "Today's Patients",
                      style: TextStyle(
                        fontSize: 13.sp,
                        fontWeight: FontWeight.w500,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 25.h),
          Skeletonizer(
            enabled: true,
            child: Container(
              height: 316.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20.r),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.08),
                    blurRadius: 8,
                    spreadRadius: 0,
                    offset: const Offset(0, 2),
                  ),
                ],
              ),
              child: Padding(
                padding: EdgeInsets.fromLTRB(12.w, 10.h, 12.w, 16.h),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Container(
                      height: 12.h,
                      width: 120.w,
                      color: Colors.white,
                    ),
                    SizedBox(height: 8.h),
                    Expanded(
                      child: Row(
                        children: [
                          Expanded(
                            flex: 6,
                            child: Center(
                              child: Stack(
                                alignment: Alignment.center,
                                children: [
                                  Container(
                                    width: 190.w,
                                    height: 190.w,
                                    decoration: BoxDecoration(
                                      shape: BoxShape.circle,
                                      border: Border.all(
                                        color: Colors.white,
                                        width: 26.w,
                                      ),
                                      color: Colors.transparent,
                                    ),
                                  ),
                                  Skeleton.ignore(
                                    child: Container(
                                      width: 92.w,
                                      height: 92.w,
                                      decoration: const BoxDecoration(
                                        color: Colors.white,
                                        shape: BoxShape.circle,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                          SizedBox(width: 20.w),
                          Expanded(
                            flex: 4,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                _legendSkeletonRow(),
                                SizedBox(height: 20.h),
                                _legendSkeletonRow(),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),
                    SizedBox(height: 8.h),
                  ],
                ),
              ),
            ),
          ),
          SizedBox(height: 15.h),
        ],
      ),
    );
  }

  Widget _legendSkeletonRow() {
    return Row(
      children: [
        Container(
          width: 36.w,
          height: 36.h,
          decoration: const BoxDecoration(
            color: Colors.white,
            shape: BoxShape.circle,
          ),
        ),
        SizedBox(width: 12.w),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(height: 14.h, width: 60.w, color: Colors.white),
            SizedBox(height: 6.h),
            Container(height: 12.h, width: 80.w, color: Colors.white),
          ],
        ),
      ],
    );
  }
}

class CommonSkeletonSuperAdminBarChartCard extends StatelessWidget {
  const CommonSkeletonSuperAdminBarChartCard({super.key});

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 4.h),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16.r),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.08),
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
                Container(height: 12.h, width: 120.w, color: Colors.white),
                SizedBox(height: 8.h),
                Expanded(
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      // Y-axis labels placeholder
                      SizedBox(
                        width: 45.w,
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          crossAxisAlignment: CrossAxisAlignment.end,
                          children: List.generate(
                            5,
                            (_) => Container(
                              height: 10.h,
                              width: 28.w,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ),
                      Expanded(
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.end,
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          children: List.generate(2, (index) {
                            return Column(
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: [
                                _barPair(heightA: 300.h, heightB: 220.h),
                                SizedBox(height: 8.h),
                                Container(
                                  height: 10.h,
                                  width: 36.w,
                                  color: Colors.white,
                                ),
                              ],
                            );
                          }),
                        ),
                      ),
                    ],
                  ),
                ),
                SizedBox(height: 8.h),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    _legendChip(),
                    SizedBox(width: 30.w),
                    _legendChip(),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _barPair({required double heightA, required double heightB}) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Container(width: 32.w, height: heightA, color: Colors.white),
        SizedBox(width: 8.w),
        Container(width: 32.w, height: heightB, color: Colors.white),
      ],
    );
  }

  Widget _legendChip() {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(width: 14.w, height: 14.w, color: Colors.white),
        SizedBox(width: 8.w),
        Container(height: 10.h, width: 40.w, color: Colors.white),
      ],
    );
  }
}

class CommonSkeletonDashboardCard extends StatelessWidget {
  final double height;
  final EdgeInsets padding;
  final double radius;

  const CommonSkeletonDashboardCard({
    super.key,
    this.height = 142,
    this.padding = const EdgeInsets.symmetric(vertical: 8, horizontal: 8),
    this.radius = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: Container(
        height: height.h,
        padding: EdgeInsets.only(
          top: responsiveHeight(padding.top),
          bottom: responsiveHeight(padding.bottom),
          left: responsiveWidth(padding.left),
          right: responsiveWidth(padding.right),
        ),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(responsiveHeight(radius)),
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withValues(alpha: 0.4),
              spreadRadius: 2,
              blurRadius: 3,
              offset: const Offset(0, 3),
            ),
          ],
        ),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Container(
                  width: 34.w,
                  height: 34.w,
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    shape: BoxShape.circle,
                  ),
                ),
                SizedBox(width: 14.w),
                Column(
                  children: [
                    Container(height: 14.h, width: 44.w, color: Colors.white),
                    SizedBox(height: 6.h),
                    Container(height: 12.h, width: 52.w, color: Colors.white),
                  ],
                ),
              ],
            ),
            SizedBox(height: 6.h),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Container(height: 12.h, width: 92.w, color: Colors.white),
                SizedBox(width: 8.w),
                Container(
                  width: 20.w,
                  height: 20.w,
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    shape: BoxShape.circle,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class CommonSkeletonBeneficiaryCampList extends StatelessWidget {
  final int itemCount;
  final EdgeInsets itemPadding;
  final double cardRadius;

  const CommonSkeletonBeneficiaryCampList({
    super.key,
    this.itemCount = 6,
    this.itemPadding = const EdgeInsets.only(
      top: 6,
      bottom: 6,
      left: 6,
      right: 6,
    ),
    this.cardRadius = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: ListView.builder(
        shrinkWrap: true,
        itemCount: itemCount,
        itemBuilder: (context, index) {
          return Padding(
            padding: EdgeInsets.only(
              top: responsiveHeight(itemPadding.top),
              bottom: responsiveHeight(itemPadding.bottom),
              left: responsiveWidth(itemPadding.left),
              right: responsiveWidth(itemPadding.right),
            ),
            child: IntrinsicHeight(
              child: Stack(
                children: [
                  Container(
                    width: SizeConfig.screenWidth,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(
                        responsiveHeight(cardRadius),
                      ),
                      color: Colors.white,
                      boxShadow: [
                        BoxShadow(
                          blurRadius: responsiveHeight(10),
                          spreadRadius: 0,
                          offset: const Offset(0, 1),
                          color: const Color(
                            0xFF000000,
                          ).withValues(alpha: 0.15),
                        ),
                      ],
                    ),
                    child: Row(
                      children: [
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.symmetric(
                              vertical: 10,
                              horizontal: 10,
                            ),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Row(
                                  children: [
                                    Container(
                                      height: 18.h,
                                      width: 18.h,
                                      color: Colors.white,
                                    ),
                                    SizedBox(width: 8.w),
                                    Expanded(
                                      child: Container(
                                        height: 14.h,
                                        color: Colors.white,
                                      ),
                                    ),
                                  ],
                                ),
                                SizedBox(height: 8.h),
                                Row(
                                  children: [
                                    Container(
                                      height: 18.h,
                                      width: 18.h,
                                      color: Colors.white,
                                    ),
                                    SizedBox(width: 8.w),
                                    Expanded(
                                      child: Container(
                                        height: 14.h,
                                        color: Colors.white,
                                      ),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ),
                        ),
                        Padding(
                          padding: EdgeInsets.fromLTRB(0, 0, 8.w, 0),
                          child: Container(
                            width: 50.w,
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.only(
                                topRight: Radius.circular(cardRadius),
                                bottomRight: Radius.circular(cardRadius),
                              ),
                            ),
                            child: Padding(
                              padding: EdgeInsets.fromLTRB(0, 0, 0, 12.w),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.end,
                                crossAxisAlignment: CrossAxisAlignment.center,
                                children: [
                                  Container(
                                    width: 30.w,
                                    height: 30.h,
                                    decoration: const BoxDecoration(
                                      shape: BoxShape.circle,
                                      color: Colors.white,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  Positioned(
                    bottom: 42,
                    left: 10,
                    child: Container(
                      width: 22.w,
                      height: 22.h,
                      decoration: const BoxDecoration(
                        shape: BoxShape.circle,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}

class CommonSkeletonScreeningDetailsTable extends StatelessWidget {
  final int rowCount;
  final double rowHeight;
  final double headerHeight;
  final double radius;

  const CommonSkeletonScreeningDetailsTable({
    super.key,
    this.rowCount = 10,
    this.rowHeight = 34,
    this.headerHeight = 34,
    this.radius = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: Padding(
        padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
        child: Column(
          children: [
            Container(
              width: SizeConfig.screenWidth,
              height: headerHeight,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(radius),
                  topRight: Radius.circular(radius),
                ),
                color: Colors.grey.withValues(alpha: 0.15),
              ),
            ),
            SizedBox(height: responsiveHeight(6)),
            Column(
              children: List.generate(rowCount, (index) {
                final bool isFirst = index == 0;
                final bool isLast = index == rowCount - 1;
                return Padding(
                  padding: EdgeInsets.only(
                    bottom:
                        index == rowCount - 1
                            ? responsiveHeight(20)
                            : responsiveHeight(4),
                  ),
                  child: Container(
                    width: SizeConfig.screenWidth,
                    height: rowHeight,
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: const Color(0xffDEDEDE),
                        width: 1,
                      ),
                      color: Colors.white,
                      borderRadius:
                          isFirst
                              ? BorderRadius.only(
                                topLeft: Radius.circular(radius),
                                topRight: Radius.circular(radius),
                              )
                              : isLast
                              ? BorderRadius.only(
                                bottomLeft: Radius.circular(radius),
                                bottomRight: Radius.circular(radius),
                              )
                              : null,
                    ),
                    child: Row(
                      children: [
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 8,
                              vertical: 8,
                            ),
                            child: Container(height: 12.h, color: Colors.white),
                          ),
                        ),
                        Container(
                          width: 102,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 8,
                          ),
                          child: Center(
                            child: Container(
                              height: 12.h,
                              width: 40.w,
                              color: Colors.white,
                            ),
                          ),
                        ),
                        Container(
                          width: 102,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 8,
                          ),
                          child: Center(
                            child: Container(
                              height: 12.h,
                              width: 40.w,
                              color: Colors.white,
                            ),
                          ),
                        ),

                        Container(
                          width: 102,
                          padding: const EdgeInsets.symmetric(
                            horizontal: 8,
                            vertical: 8,
                          ),
                          child: Center(
                            child: Container(
                              height: 12.h,
                              width: 40.w,
                              color: Colors.white,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              }),
            ),
          ],
        ),
      ),
    );
  }
}

class CommonSkeletonInvoiceTable extends StatelessWidget {
  final int itemCount;
  final double rowHeight;
  final bool shrinkWrap;
  final ScrollPhysics? physics;

  const CommonSkeletonInvoiceTable({
    super.key,
    this.itemCount = 8,
    this.rowHeight = 40,
    this.shrinkWrap = false,
    this.physics,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: ListView.builder(
        shrinkWrap: shrinkWrap,
        physics: physics,
        padding: EdgeInsets.zero,
        itemCount: itemCount,
        itemBuilder: (context, index) {
          return Container(
            height: rowHeight,
            decoration: const BoxDecoration(
              color: Colors.white,
              border: Border(
                left: BorderSide(color: Color(0xFFD8D8D8), width: 1),
                right: BorderSide(color: Color(0xFFD8D8D8), width: 1),
                bottom: BorderSide(color: Color(0xFFD8D8D8), width: 1),
              ),
            ),
            child: Row(
              children: [
                SizedBox(
                  width: 70,
                  child: Center(
                    child: Container(
                      height: 10.h,
                      width: 40.w,
                      color: Colors.white,
                    ),
                  ),
                ),
                Container(
                  width: 70,
                  decoration: const BoxDecoration(
                    border: Border(
                      left: BorderSide(color: Color(0xFFD8D8D8), width: 1),
                    ),
                  ),
                  child: Center(
                    child: Container(
                      height: 10.h,
                      width: 26.w,
                      color: Colors.white,
                    ),
                  ),
                ),
                Expanded(
                  child: Container(
                    decoration: const BoxDecoration(
                      border: Border(
                        left: BorderSide(color: Color(0xFFD8D8D8), width: 1),
                      ),
                    ),
                    child: Center(
                      child: Container(
                        height: 10.h,
                        width: 34.w,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
                Container(
                  width: 90,
                  decoration: const BoxDecoration(
                    border: Border(
                      left: BorderSide(color: Color(0xFFD8D8D8), width: 1),
                    ),
                  ),
                  child: Center(
                    child: Container(
                      height: 16.h,
                      width: 50.w,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(10),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }
}

class CommonSkeletonD2DPhysicalExamTable extends StatelessWidget {
  final int rowCount;
  final double rowHeight;

  const CommonSkeletonD2DPhysicalExamTable({
    super.key,
    this.rowCount = 8,
    this.rowHeight = 40,
  });

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: ListView.builder(
        padding: EdgeInsets.zero,
        itemCount: rowCount,
        itemBuilder: (context, index) {
          return Container(
            decoration: BoxDecoration(
              color: Colors.white,
              border: Border(
                bottom: BorderSide(
                  color: const Color(0xFFBDBDBD),
                  width: 0.5.w,
                ),
                left: BorderSide(color: const Color(0xFFBDBDBD), width: 0.5.w),
                right: BorderSide(color: const Color(0xFFBDBDBD), width: 0.5.w),
              ),
            ),
            child: Row(
              children: List.generate(5, (cellIndex) {
                return Expanded(
                  child: Container(
                    height: rowHeight,
                    padding: EdgeInsets.symmetric(
                      vertical: 4.h,
                      horizontal: 4.w,
                    ),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      border: Border(
                        right: BorderSide(
                          color: const Color(0xFFBDBDBD),
                          width: 0.5.w,
                        ),
                      ),
                    ),
                    child: Center(
                      child: Container(
                        height: 10.h,
                        width: 40.w,
                        color: Colors.white,
                      ),
                    ),
                  ),
                );
              }),
            ),
          );
        },
      ),
    );
  }
}

class CommonSkeletonCalender extends StatelessWidget {
  const CommonSkeletonCalender({super.key});

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: Container(
        height: 340,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _skeletonBox(height: 24, width: 24, radius: 6),
                _skeletonBox(height: 20, width: 160, radius: 6),
                _skeletonBox(height: 24, width: 24, radius: 6),
              ],
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: List.generate(
                7,
                (index) => Expanded(
                  child: Center(
                    child: _skeletonBox(height: 14, width: 32, radius: 4),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 8),
            Expanded(
              child: GridView.builder(
                physics: const NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 7,
                ),
                itemCount: 42,
                itemBuilder: (context, index) {
                  return Padding(
                    padding: const EdgeInsets.all(4.0),
                    child: Center(
                      child: _skeletonBox(height: 32, width: 32, radius: 8),
                    ),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _skeletonBox({
    required double height,
    required double width,
    double radius = 8,
  }) {
    return Container(
      height: height,
      width: width,
      decoration: BoxDecoration(
        color: Colors.grey.shade300,
        borderRadius: BorderRadius.circular(radius),
      ),
    );
  }
}
