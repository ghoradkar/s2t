// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/WebViewWidget.dart';

class AnalyticalDashboard extends StatefulWidget {
  const AnalyticalDashboard({super.key});

  @override
  State<AnalyticalDashboard> createState() => AnalyticalDashboardState();
}

class AnalyticalDashboardState extends State<AnalyticalDashboard> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Analytical Dashboard",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Get.back();
        },
      ),
      body: Column(
        children: [
          Row(
            children: [
              Expanded(
                child: dashCard('Screening MH Summary', () {
                  Get.to(
                    MyWebView(
                      url:
                          'https://app.powerbi.com/view?r=eyJrIjoiNjc2ZTEwOTAtYjYxYS00NTJlLWE0N2QtNzBhZGM3NTM0Mzg1IiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9',
                      title: 'Screening MH Summary',
                    ),
                  );
                }).paddingOnly(left: 8.w, right: 4.w),
              ),
              Expanded(
                child: dashCard('Screening MH\nTAT', () {
                  Get.to(
                    MyWebView(
                      url:
                          'https://app.powerbi.com/view?r=eyJrIjoiMzllNDFmMGYtNGQyMC00NTEzLTg4MTEtM2ZjYmM0OTA4YzFkIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9',
                      title: 'Screening MH TAT',
                    ),
                  );
                }).paddingOnly(left: 8.w, right: 4.w),
              ),
            ],
          ).paddingOnly(bottom: 10.h),
          Row(
            children: [
              Expanded(
                child: dashCard('Screening MH Pending', () {
                  Get.to(
                    MyWebView(
                      url:
                          'https://app.powerbi.com/view?r=eyJrIjoiNzUxNzVmNTAtNWQyZi00NzQxLWFkMzYtMGMyNDRiOWJhMDVkIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9',
                      title: 'Screening MH Pending',
                    ),
                  );
                }).paddingOnly(left: 8.w, right: 4.w),
              ),
              Expanded(
                child: dashCard('Patient App\nDeployment', () {
                  Get.to(
                    MyWebView(
                      url:
                          'https://app.powerbi.com/view?r=eyJrIjoiMzM3NzhmMDAtNzAyMy00MDcwLWEyZTMtOTlhY2JjYTE1YTA2IiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9',
                      title: 'Patient App Deployment',
                    ),
                  );
                }).paddingOnly(left: 8.w, right: 4.w),
              ),
            ],
          ).paddingOnly(bottom: 10.h),
          Row(
            children: [
              Expanded(
                child: dashCard('Screening MH Rejection', () {
                  Get.to(
                    MyWebView(
                      url:
                          'https://app.powerbi.com/view?r=eyJrIjoiNjYwMGM3ZjMtNTU1Yi00MTA0LWFlZTMtMTE4ZTNjMWJkODllIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9',
                      title: 'Screening MH Rejection',
                    ),
                  );
                }).paddingOnly(left: 8.w, right: 4.w),
              ),
              Expanded(child: SizedBox().paddingOnly(left: 8.w, right: 4.w)),
            ],
          ),
        ],
      ).paddingSymmetric(vertical: 10),
    );
  }

  Widget dashCard(String text, Function onTap) {
    return InkWell(
      onTap: () {
        onTap();
      },
      child: Card(
        color: Colors.white,

        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset(
                powerBi,
                width: 40.w,
                height: 40.h,
                color: kPrimaryColor,
              ),
              const SizedBox(height: 4),
              CommonText(
                text: text,
                fontSize: 14.sp,
                fontWeight: FontWeight.normal,
                textColor: kBlackColor,
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
