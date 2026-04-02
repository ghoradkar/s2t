// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/d2d_teams/controller/d2d_teams_controller.dart';
import 'package:s2toperational/Screens/d2d_teams/model/D2DTeamsCallingDetails.dart';
import 'package:url_launcher/url_launcher.dart';

class D2dNonWorkingTeamScreen extends StatelessWidget {
  final String campId;
  final String divId;
  final String distCode;
  final String labCode;
  final String orgId;
  final String title;
  final String? empId;
  final String? desigId;

  const D2dNonWorkingTeamScreen({
    super.key,
    required this.campId,
    required this.divId,
    required this.distCode,
    required this.labCode,
    required this.orgId,
    required this.title,
    this.desigId,
    this.empId,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<D2DTeamsController>(
      initState: (_) {
        Get.find<D2DTeamsController>().checkInternetAndLoadNonWorking(
          title: title,
          empId: empId,
          campType: campId,
          divId: divId,
          distlgdCode: distCode,
          labCode: labCode,
          subOrgId: orgId,
          desigId: desigId,
        );
      },
      builder: (ctrl) {
        final items = ctrl.d2dWorkingOrNonWorkingTeams?.output ?? const [];

        return Scaffold(
          appBar: mAppBar(
            scTitle: title,
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
          ),
          body: ctrl.hasInternet
              ? AnnotatedRegion<SystemUiOverlayStyle>(
                  value: const SystemUiOverlayStyle(
                    statusBarColor: kPrimaryColor,
                    statusBarBrightness: Brightness.light,
                    statusBarIconBrightness: Brightness.light,
                  ),
                  child: ctrl.isD2dNonWorkingTeamsLoading
                      ? const CommonSkeletonPatientList().paddingSymmetric(
                          vertical: 10, horizontal: 10)
                      : items.isNotEmpty
                          ? ListView.builder(
                              itemCount: items.length,
                              padding: EdgeInsets.only(top: 6.h),
                              itemBuilder: (context, index) {
                                final team = items[index];
                                return Stack(
                                  children: [
                                    Container(
                                      padding: EdgeInsets.symmetric(
                                        vertical: 10.h,
                                        horizontal: 12.w,
                                      ),
                                      decoration: BoxDecoration(
                                        borderRadius:
                                            BorderRadius.circular(12),
                                        color: kWhiteColor,
                                        boxShadow: [
                                          BoxShadow(
                                            color: Colors.grey
                                                .withOpacity(0.6),
                                            blurRadius: 5,
                                            offset: const Offset(0, 2),
                                          ),
                                        ],
                                      ),
                                      child: Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        children: [
                                          Expanded(
                                            child: Column(
                                              crossAxisAlignment:
                                                  CrossAxisAlignment.start,
                                              children: [
                                                _memberRow(
                                                  label: "Member 1 :",
                                                  name:
                                                      team.member1 ?? '-',
                                                ),
                                                SizedBox(height: 6.h),
                                                _memberRow(
                                                  label: "Member 2 :",
                                                  name:
                                                      team.member2 ?? '-',
                                                ),
                                                SizedBox(height: 6.h),
                                                _memberRow(
                                                  label:
                                                      "Reg. Beneficiary :",
                                                  name: team.regBeneficiaries
                                                      .toString(),
                                                ),
                                              ],
                                            ),
                                          ),
                                          InkWell(
                                            onTap: () async {
                                              await ctrl.getCallingDetails(
                                                  team.teamId.toString());
                                              if (ctrl.d2dTeamsCallingDetails
                                                          ?.output !=
                                                      null &&
                                                  ctrl.d2dTeamsCallingDetails!
                                                      .output.isNotEmpty) {
                                                if (context.mounted) {
                                                  _showListDialog(
                                                    context,
                                                    ctrl.d2dTeamsCallingDetails!
                                                        .output,
                                                  );
                                                }
                                              }
                                            },
                                            child: Icon(
                                              Icons.call,
                                              size: 22.sp,
                                              color: kPrimaryColor
                                                  .withOpacity(0.8),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ).paddingSymmetric(
                                      horizontal: 14.w,
                                      vertical: 12.h,
                                    ),
                                    Container(
                                      alignment: Alignment.center,
                                      width: 18,
                                      height: 18,
                                      decoration: BoxDecoration(
                                        color: kPrimaryColor,
                                        borderRadius:
                                            BorderRadius.circular(50),
                                      ),
                                      child: CommonText(
                                        text: (index + 1).toString(),
                                        fontSize: 8,
                                        fontWeight: FontWeight.normal,
                                        textColor: kWhiteColor,
                                        textAlign: TextAlign.center,
                                      ),
                                    ).paddingOnly(left: 20),
                                  ],
                                );
                              },
                            )
                          : NoDataFound(),
                )
              : NoInternetWidget(
                  onRetryPressed: () => ctrl.checkInternetAndLoad(),
                ),
        );
      },
    );
  }

  Future<void> _showListDialog(
      BuildContext context, List<TeamMember> items) async {
    final selected = await showDialog<String>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text("Teams"),
          content: SizedBox(
            height: 200,
            width: double.maxFinite,
            child: ListView.builder(
              shrinkWrap: true,
              itemCount: items.length,
              itemBuilder: (context, index) {
                return Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                      child: CommonText(
                        text: items[index].memberName,
                        fontSize: 14.sp,
                        fontWeight: FontWeight.normal,
                        textColor: kBlackColor,
                        textAlign: TextAlign.start,
                      ),
                    ),
                    InkWell(
                      onTap: () {
                        _openDialer(context, items[index].mobNo);
                      },
                      child: Icon(
                        Icons.call,
                        size: 22.sp,
                        color: kPrimaryColor.withOpacity(0.8),
                      ),
                    ),
                  ],
                ).paddingSymmetric(vertical: 6.h);
              },
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Get.back(),
              child: const Text("Close"),
            ),
          ],
        );
      },
    );

    if (selected != null && context.mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("You selected: $selected")));
    }
  }

  Future<void> _openDialer(BuildContext context, String phone) async {
    final sanitized = phone.replaceAll(RegExp(r'[^0-9+]'), '');
    final uri = Uri(scheme: 'tel', path: sanitized);
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri, mode: LaunchMode.externalApplication);
    } else {
      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Could not open dialer')),
        );
      }
    }
  }

  Widget _memberRow({required String label, required String name}) {
    return Row(
      mainAxisSize: MainAxisSize.max,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Image.asset(icUserIcon, width: 20.w),
        SizedBox(width: 6.w),
        CommonText(
          text: label,
          fontSize: 14.sp,
          fontWeight: FontWeight.w600,
          textColor: kTextColor,
          textAlign: TextAlign.start,
        ),
        SizedBox(width: 6.w),
        Flexible(
          fit: FlexFit.loose,
          child: CommonText(
            text: name,
            fontSize: 14.sp,
            fontWeight: FontWeight.w600,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
            maxLine: 2,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    );
  }
}