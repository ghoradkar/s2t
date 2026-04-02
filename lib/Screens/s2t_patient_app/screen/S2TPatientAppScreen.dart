// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/s2t_patient_app/controller/s2t_patient_app_controller.dart';
import 'package:s2toperational/Screens/s2t_patient_app/screen/AndroidAndIosPatientCountScreen.dart';

class S2tPatientAppScreen extends StatelessWidget {
  const S2tPatientAppScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<S2TPatientAppController>(
      init: S2TPatientAppController(),
      dispose: (_) => Get.delete<S2TPatientAppController>(),
      builder: (ctrl) {
        return Scaffold(
          appBar: mAppBar(
            scTitle: "S2T Patient App",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
            showActions: true,
          ),
          body: ctrl.hasInternet
              ? Container(
                  color: Colors.white,
                  height: SizeConfig.screenHeight,
                  width: SizeConfig.screenWidth,
                  child: Column(
                    children: [
                      Row(
                        children: ctrl.isS2tAppLoading
                            ? [
                                Expanded(
                                  child: const CommonSkeletonDashboardCard()
                                      .paddingSymmetric(
                                    vertical: 8.h,
                                    horizontal: 8.w,
                                  ),
                                ),
                                Expanded(
                                  child: const CommonSkeletonDashboardCard()
                                      .paddingSymmetric(
                                    vertical: 8.h,
                                    horizontal: 8.w,
                                  ),
                                ),
                              ]
                            : [
                                // Android card
                                Expanded(
                                  child: InkWell(
                                    onTap: () {
                                      Get.to(
                                        AndroidAndIosPatientCountScreen(
                                          title: 'Android Total Patient',
                                          total: ctrl.s2tAndroidIosCountModel
                                              ?.details.count.first.android
                                              .toString(),
                                        ),
                                      );
                                    },
                                    child: Container(
                                      height: 142.h,
                                      padding: EdgeInsets.symmetric(
                                        vertical: 8.h,
                                        horizontal: 8.w,
                                      ),
                                      decoration: BoxDecoration(
                                        color: Colors.white,
                                        borderRadius:
                                            BorderRadius.circular(10),
                                        boxShadow: [
                                          BoxShadow(
                                            color: Colors.grey.withValues(
                                              alpha: 0.4,
                                            ),
                                            spreadRadius: 2,
                                            blurRadius: 3,
                                            offset: const Offset(0, 3),
                                          ),
                                        ],
                                      ),
                                      child: Column(
                                        children: [
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              Image.asset(
                                                icAndroid,
                                                width: 34.w,
                                              ),
                                              SizedBox(width: 14.w),
                                              Column(
                                                children: [
                                                  CommonText(
                                                    text: ctrl
                                                            .s2tAndroidIosCountModel
                                                            ?.details
                                                            .count
                                                            .first
                                                            .android
                                                            .toString() ??
                                                        '',
                                                    fontSize: 16,
                                                    fontWeight:
                                                        FontWeight.w600,
                                                    textColor: kBlackColor,
                                                    textAlign:
                                                        TextAlign.start,
                                                  ),
                                                  CommonText(
                                                    text: "Till Date",
                                                    fontSize: 14,
                                                    fontWeight:
                                                        FontWeight.normal,
                                                    textColor: kBlackColor,
                                                    textAlign:
                                                        TextAlign.start,
                                                  ),
                                                ],
                                              ),
                                            ],
                                          ),
                                          SizedBox(height: 6.h),
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              CommonText(
                                                text:
                                                    "Android Total\nPatient",
                                                fontSize: 14,
                                                fontWeight: FontWeight.w600,
                                                textColor: kBlackColor,
                                                textAlign: TextAlign.start,
                                              ),
                                              SizedBox(width: 8.w),
                                              Icon(
                                                Icons.remove_red_eye_outlined,
                                                color: kPrimaryColor,
                                              ),
                                            ],
                                          ),
                                        ],
                                      ),
                                    ),
                                  ).paddingSymmetric(
                                    vertical: 8.h,
                                    horizontal: 8.w,
                                  ),
                                ),

                                // iOS card
                                Expanded(
                                  child: InkWell(
                                    onTap: () {
                                      Get.to(
                                        AndroidAndIosPatientCountScreen(
                                          title: 'IOS Total Patient',
                                          total: ctrl.s2tAndroidIosCountModel
                                              ?.details.count.first.iOS
                                              .toString(),
                                        ),
                                      );
                                    },
                                    child: Container(
                                      height: 142.h,
                                      padding: EdgeInsets.symmetric(
                                        vertical: 8.h,
                                        horizontal: 8.w,
                                      ),
                                      decoration: BoxDecoration(
                                        color: Colors.white,
                                        borderRadius:
                                            BorderRadius.circular(10),
                                        boxShadow: [
                                          BoxShadow(
                                            color: Colors.grey.withValues(
                                              alpha: 0.4,
                                            ),
                                            spreadRadius: 2,
                                            blurRadius: 3,
                                            offset: const Offset(0, 3),
                                          ),
                                        ],
                                      ),
                                      child: Column(
                                        children: [
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              Image.asset(
                                                icIphone,
                                                width: 34.w,
                                              ),
                                              SizedBox(width: 12.w),
                                              Column(
                                                children: [
                                                  CommonText(
                                                    text: ctrl
                                                            .s2tAndroidIosCountModel
                                                            ?.details
                                                            .count
                                                            .first
                                                            .iOS
                                                            .toString() ??
                                                        '',
                                                    fontSize: 16,
                                                    fontWeight:
                                                        FontWeight.w600,
                                                    textColor: kBlackColor,
                                                    textAlign:
                                                        TextAlign.start,
                                                  ),
                                                  CommonText(
                                                    text: "Till Date",
                                                    fontSize: 14,
                                                    fontWeight:
                                                        FontWeight.normal,
                                                    textColor: kBlackColor,
                                                    textAlign:
                                                        TextAlign.start,
                                                  ),
                                                ],
                                              ),
                                            ],
                                          ),
                                          SizedBox(height: 6.h),
                                          Row(
                                            mainAxisAlignment:
                                                MainAxisAlignment.spaceBetween,
                                            children: [
                                              CommonText(
                                                text: "iOS Total\nPatients",
                                                fontSize: 14,
                                                fontWeight: FontWeight.w600,
                                                textColor: kBlackColor,
                                                textAlign: TextAlign.start,
                                              ),
                                              SizedBox(width: 12.w),
                                              Icon(
                                                Icons.remove_red_eye_outlined,
                                                color: kPrimaryColor,
                                              ),
                                            ],
                                          ),
                                        ],
                                      ),
                                    ),
                                  ).paddingSymmetric(
                                    vertical: 8.h,
                                    horizontal: 8.w,
                                  ),
                                ),
                              ],
                      ),
                    ],
                  ),
                )
              : NoInternetWidget(
                  onRetryPressed: () => ctrl.checkInternetAndLoad(),
                ),
        );
      },
    );
  }
}