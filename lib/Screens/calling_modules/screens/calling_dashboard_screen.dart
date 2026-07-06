// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/controller/calling_dashboard_controller.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/calling_modules/models/calling_dashboard_model.dart';

class CallingDashboardScreen extends GetView<CallingDashboardController> {
  const CallingDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Calling Summary',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
        showActions: true,
        actions: [
          GestureDetector(
            onTap: () => controller.showFilterSheet(context),
            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 12.w),
              child: Image.asset(
                icFilter,
                height: 26.h,
                width: 26.w,
                color: kWhiteColor,
                fit: BoxFit.contain,
              ),
            ),
          ),
        ],
      ),
      body: GetBuilder<CallingDashboardController>(
        builder:
            (c) => Column(
              children: [
                if (c.hasLoaded && c.dashboardModel != null)
                  _NoteCard(lastUpdatedOn: c.dashboardModel?.lastUpdatedOn),
                Expanded(
                  child:
                      c.isLoading
                          ? const CommonSkeletonPatientList(
                            itemCount: 10,
                          ).paddingSymmetric(horizontal: 10.w, vertical: 10.h)
                          : c.dashboardItems.isEmpty
                          ? NoDataFound().paddingSymmetric(
                            horizontal: 10.w,
                            vertical: 10.h,
                          )
                          : ListView.separated(
                            padding: EdgeInsets.symmetric(
                              horizontal: 16.w,
                              vertical: 12.h,
                            ),
                            itemCount: c.dashboardItems.length,
                            separatorBuilder: (_, __) => SizedBox(height: 8.h),
                            itemBuilder:
                                (_, index) => _DashboardRow(
                                  item: c.dashboardItems[index],
                                ),
                          ),
                ),
              ],
            ),
      ),
    );
  }
}

class _NoteCard extends StatelessWidget {
  const _NoteCard({this.lastUpdatedOn});

  final String? lastUpdatedOn;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: EdgeInsets.symmetric(horizontal: 16.w, vertical: 10.h),
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
      decoration: BoxDecoration(
        color: kPurpleFaint,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.2)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          CommonText(
            text:
                'Note: Real-time data will be updated at intervals of 4 hours',
            fontSize: 11.sp,
            fontWeight: FontWeight.w500,
            textColor: kPrimaryColor,
            textAlign: TextAlign.start,
          ),
          if (lastUpdatedOn != null && lastUpdatedOn!.isNotEmpty) ...[
            SizedBox(height: 2.h),
            CommonText(
              text: 'Data as of: $lastUpdatedOn',
              fontSize: 11.sp,
              fontWeight: FontWeight.w400,
              textColor: kLabelTextColor,
              textAlign: TextAlign.start,
            ),
          ],
        ],
      ),
    );
  }
}

class _DashboardRow extends StatelessWidget {
  const _DashboardRow({required this.item});

  final CallingDashboardOutput item;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 14.h),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 6,
            height: 40.h,
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.circular(4),
            ),
          ),
          SizedBox(width: 12.w),
          Expanded(
            child: CommonText(
              text: item.columnName ?? '-',
              fontSize: 13.sp,
              fontWeight: FontWeight.w500,
              textColor: kTextColor,
              textAlign: TextAlign.start,
            ),
          ),
          Container(
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 6.h),
            decoration: BoxDecoration(
              color: kPrimaryColor.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(20),
            ),
            child: CommonText(
              text: item.totalValue ?? '0',
              fontSize: 14.sp,
              fontWeight: FontWeight.w700,
              textColor: kPrimaryColor,
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }
}
