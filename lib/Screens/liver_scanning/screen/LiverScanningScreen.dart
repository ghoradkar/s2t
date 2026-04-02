// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/HomeScreen/DashboardMenuRow/DashboardAdminCard.dart';
import 'package:s2toperational/Screens/liver_scanning/controller/liver_scanning_controller.dart';
import 'package:s2toperational/Screens/liver_scanning/screen/FibroScanningPatientDataScreen.dart';

class LiverScanningScreen extends StatelessWidget {
  const LiverScanningScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<LiverScanningController>(
      init: LiverScanningController(),
      dispose: (_) => Get.delete<LiverScanningController>(),
      builder: (ctrl) {
        return Scaffold(
          appBar: mAppBar(
            scTitle: "Liver Scanning",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Navigator.pop(context);
            },
          ),
          body: ctrl.hasInternet
              ? AnnotatedRegion(
                  value: const SystemUiOverlayStyle(
                    statusBarColor: kPrimaryColor,
                    statusBarBrightness: Brightness.light,
                    statusBarIconBrightness: Brightness.light,
                  ),
                  child: Container(
                    color: Colors.white,
                    height: SizeConfig.screenHeight,
                    width: SizeConfig.screenWidth,
                    child: Container(
                      color: Colors.transparent,
                      width: SizeConfig.screenWidth,
                      child: ctrl.isLiverScanningLoading
                          ? const SizedBox(
                              height: 320,
                              child: CommonSkeletonPatientList(itemCount: 2),
                            ).paddingOnly(left: 10, right: 10, top: 10)
                          : Column(
                              children: [
                                DashboardAdminCard(
                                  title: "Fibroscan Liver Machine Installed",
                                  topRightCaption: '',
                                  leading: Image.asset(icFibroscan, width: 30),
                                  metrics: {
                                    "Till Date": ctrl.liverScanningCountModel
                                            ?.machineInstalledCount
                                            .toString() ??
                                        '0',
                                  },
                                  onAction: () {},
                                  view: false,
                                ),
                                const SizedBox(height: 10),
                                DashboardAdminCard(
                                  title: "Total Scanning Done",
                                  topRightCaption: "",
                                  leading:
                                      Image.asset(icTeamsIcon, width: 30),
                                  metrics: {
                                    "Till Date": ctrl.liverScanningCountModel
                                            ?.tillDateScanningCount
                                            .toString() ??
                                        '0',
                                    "Today": ctrl.liverScanningCountModel
                                            ?.todayScanningCount
                                            .toString() ??
                                        "0",
                                  },
                                  onAction: () {
                                    Get.to(
                                      const FibroScanningPatientDataScreen(),
                                    );
                                  },
                                  view: true,
                                ),
                              ],
                            ).paddingOnly(left: 10, right: 10, top: 10),
                    ),
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