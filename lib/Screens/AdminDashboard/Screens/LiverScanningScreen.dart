// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Controller/AdminController.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/FibroScanningPatientDataScreen.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/HomeScreen/DashboardMenuRow/DashboardAdminCard.dart';

class LiverScanningScreen extends StatefulWidget {
  const LiverScanningScreen({super.key});

  @override
  State<LiverScanningScreen> createState() => LiverScanningScreenState();
}

class LiverScanningScreenState extends State<LiverScanningScreen> {
  final AdminController adminController = Get.put(AdminController());

  @override
  void initState() {
    adminController.checkInternetLiverScann();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Liver Scanning",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: GetBuilder<AdminController>(
        builder: (controller) {
          return adminController.hasInternet
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
                    child:
                        controller.isLiverScanningLoading
                            ? const SizedBox(
                              height: 320,
                              child: CommonSkeletonPatientList(itemCount: 2,),
                            ).paddingOnly(left: 10, right: 10, top: 10)
                            : Column(
                              children: [
                                DashboardAdminCard(
                                  title: "Fibroscan Liver Machine Installed",
                                  topRightCaption: '',
                                  leading: Image.asset(icFibroscan, width: 30),
                                  metrics:
                                  {
                                    "Till Date":
                                    controller
                                        .liverScanningCountModel
                                        ?.machineInstalledCount
                                        .toString() ??
                                        '0',
                                  },
                                  onAction: () {
                                    // Get.to(ConductedCampsScreen(isDistAndYearVisiable: true));
                                  },
                                  view: false,
                                ),
                                const SizedBox(height: 10),
                                DashboardAdminCard(
                                  title: "Total Scanning Done",
                                  topRightCaption: "",
                                  leading: Image.asset(icTeamsIcon, width: 30),
                                  metrics:
                                  {
                                    "Till Date":
                                    controller
                                        .liverScanningCountModel
                                        ?.tillDateScanningCount
                                        .toString() ??
                                        '0',
                                    "Today":
                                    controller
                                        .liverScanningCountModel
                                        ?.todayScanningCount
                                        .toString() ??
                                        "0",
                                  },
                                  onAction: () {
                                    Get.to(
                                      FibroScanningPatientDataScreen(
                                        item:
                                        controller
                                            .fibroScanResponse
                                            ?.output ??
                                            [],
                                      ),
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
                onRetryPressed: () => adminController.checkInternetLiverScann(),
              );
        },
      ),
    );
  }
}
