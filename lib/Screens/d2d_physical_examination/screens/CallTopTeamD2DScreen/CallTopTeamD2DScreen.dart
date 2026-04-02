// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/call_to_team_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/TeamWisePhysicalExamDetailsResponse.dart';
import 'package:url_launcher/url_launcher.dart';

class CallTopTeamD2DScreen extends StatelessWidget {
  final int campId;
  final int doctorID;
  final String teamid;

  const CallTopTeamD2DScreen({
    super.key,
    required this.campId,
    required this.doctorID,
    required this.teamid,
  });

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CallToTeamController>(
      init: CallToTeamController(),
      initState: (_) => WidgetsBinding.instance.addPostFrameCallback((_) {
        Get.find<CallToTeamController>().fetchTeamDetails(
          campId: campId,
          doctorId: doctorID,
          teamId: teamid,
        );
      }),
      dispose: (_) => Get.delete<CallToTeamController>(),
      builder: (ctrl) {
        return NetworkWrapper(
          child: Scaffold(
            backgroundColor: kWhiteColor,
            appBar: mAppBar(
              scTitle: "Call To Team",
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Navigator.pop(context);
              },
            ),
            body: SizedBox(
              height: SizeConfig.screenHeight,
              width: SizeConfig.screenWidth,
              child: Padding(
                padding: EdgeInsets.fromLTRB(0, 8.h, 0, 8.h),
                child: ListView.builder(
                  itemCount: ctrl.physiCalList.length,
                  itemBuilder: (context, index) {
                    final TeamWisePhysicalExamDetailsOutput obj =
                        ctrl.physiCalList[index];
                    return Padding(
                      padding: EdgeInsets.fromLTRB(10.w, 0, 10.h, 10.h),
                      child: Container(
                        padding: EdgeInsets.symmetric(
                          vertical: 8.h,
                          horizontal: 8.w,
                        ),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(10),
                          boxShadow: [
                            BoxShadow(
                              offset: const Offset(0, 1),
                              color: Colors.black.withValues(alpha: 0.15),
                              spreadRadius: 0,
                              blurRadius: 4,
                            ),
                          ],
                        ),
                        child: Row(
                          children: [
                            Expanded(
                              child: Column(
                                children: [
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      SizedBox(
                                        width: responsiveHeight(20),
                                        height: responsiveHeight(20),
                                        child: Image.asset(icInitiatedBy),
                                      ),
                                      SizedBox(width: 8.w),
                                      Expanded(
                                        child: Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.start,
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              "Team Member : ",
                                              style: TextStyle(
                                                color: Colors.black,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w500,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                            Expanded(
                                              child: Text(
                                                obj.teamNumberName ?? "",
                                                style: TextStyle(
                                                  color: kBlackColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                  SizedBox(height: 8.h),
                                  Row(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      SizedBox(
                                        width: responsiveHeight(20),
                                        height: responsiveHeight(20),
                                        child: Image.asset(icDesignation),
                                      ),
                                      SizedBox(width: 8.w),
                                      Expanded(
                                        child: Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.start,
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              "Designation : ",
                                              style: TextStyle(
                                                color: Colors.black,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w500,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                            Expanded(
                                              child: Text(
                                                obj.desgName ?? "",
                                                style: TextStyle(
                                                  color: kBlackColor,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: 14.sp,
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),
                            Container(
                              padding: const EdgeInsets.all(8),
                              child: Center(
                                child: GestureDetector(
                                  onTap: () {
                                    launchUrl(
                                      Uri.parse('tel:${obj.mobileNo ?? ""}'),
                                    );
                                    ctrl.launchPhoneDialer(obj.mobileNo ?? "");
                                  },
                                  child: SizedBox(
                                    width: 30.w,
                                    height: 30.h,
                                    child: Image.asset(icCallingExpected),
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}