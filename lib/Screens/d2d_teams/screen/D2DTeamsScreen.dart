// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/WorkingTeamsCountView.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/d2d_teams/controller/d2d_teams_controller.dart';
import 'package:s2toperational/Screens/d2d_teams/screen/D2DNonWorkingTeamsScreen.dart';
import 'package:url_launcher/url_launcher.dart';

class D2DTeamsScreen extends StatelessWidget {
  final String title;
  final String? total;

  const D2DTeamsScreen({super.key, required this.title, this.total});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<D2DTeamsController>(
      init: D2DTeamsController(),
      dispose: (_) => Get.delete<D2DTeamsController>(),
      builder: (ctrl) {
        final details = ctrl.d2dTeamsListModel?.output;

        return Scaffold(
          appBar: mAppBar(
            scTitle: "D2D Teams",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
            showActions: true,
            actions: [
              InkWell(
                onTap: () {
                  _filterBottomSheet(context, ctrl);
                },
                child: const Icon(
                  Icons.filter_alt_outlined,
                  color: kWhiteColor,
                  size: 28,
                ),
              ).paddingOnly(right: 16),
            ],
          ),
          body: ctrl.hasInternet
              ? AnnotatedRegion(
                  value: const SystemUiOverlayStyle(
                    statusBarColor: kPrimaryColor,
                    statusBarBrightness: Brightness.light,
                    statusBarIconBrightness: Brightness.light,
                  ),
                  child: Column(
                    children: [
                      WorkingTeamsCountView(
                        workingTeamsCount: ctrl.d2dTeamsCountModel?.output
                                .first.workingTeamCount ??
                            0,
                        notWorkingTeamsCount: ctrl.d2dTeamsCountModel?.output
                                .first.nonWorkingTeamCount ??
                            0,
                        totalTeamsCount: ctrl.d2dTeamsCountModel?.output
                                .first.totalTeamCount ??
                            0,
                        onNotWorkingTeamsTap: () {
                          Get.to(D2dNonWorkingTeamScreen(
                            campId: ctrl.resolvedCampId,
                            divId: ctrl.resolvedDivId,
                            distCode: ctrl.resolvedDistCode,
                            labCode: ctrl.resolvedLabCode,
                            orgId: ctrl.resolvedOrgId,
                            title: 'D2D Not Working Team',
                          ));
                        },
                        onWorkingTeamsTap: () {
                          Get.to(D2dNonWorkingTeamScreen(
                            campId: ctrl.resolvedCampId,
                            divId: ctrl.resolvedDivId,
                            distCode: ctrl.resolvedDistCode,
                            labCode: ctrl.resolvedLabCode,
                            orgId: ctrl.resolvedOrgId,
                            title: 'D2D Working Teams',
                          ));
                        },
                        onTotalTeamsTap: () {},
                      ),
                      SizedBox(height: 12.h),

                      // Table
                      Expanded(
                        child: Container(
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(
                              color: Colors.grey.withOpacity(0.2),
                            ),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.03),
                                blurRadius: 8,
                                offset: const Offset(0, 4),
                              ),
                            ],
                          ),
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(12),
                            child: LayoutBuilder(
                              builder: (context, constraints) {
                                return Column(
                                  children: [
                                    const HeaderRow(),
                                    TotalRow(
                                      totalNotWorking: ctrl.d2dTeamsCountModel
                                              ?.output.first.nonWorkingTeamCount ??
                                          0,
                                      totalWorking: ctrl.d2dTeamsCountModel
                                              ?.output.first.workingTeamCount ??
                                          0,
                                    ),
                                    const Divider(height: 0, thickness: .6),
                                    ctrl.isD2dTeamsLoading
                                        ? Expanded(
                                            child:
                                                const CommonSkeletonInvoiceTable(
                                              itemCount: 12,
                                            ),
                                          )
                                        : Expanded(
                                            child: ListView.separated(
                                              padding: EdgeInsets.zero,
                                              itemCount: details?.length ?? 0,
                                              separatorBuilder: (_, __) =>
                                                  Divider(
                                                height: 0,
                                                thickness: .5,
                                                color: Colors.grey.withValues(
                                                    alpha: 0.15),
                                              ),
                                              itemBuilder: (context, i) {
                                                return DataRow(
                                                  district:
                                                      details![i].distname,
                                                  coordinator: details[i]
                                                      .campCoordinatorName,
                                                  notWorking: details[i]
                                                      .nonWorkingTeamCount,
                                                  working: details[i]
                                                      .workingTeamCount,
                                                  onTapCall: () {
                                                    _openDialer(
                                                      context,
                                                      details[i]
                                                          .campCoordinatorMobNo,
                                                    );
                                                  },
                                                  onTapNonWorking: () {
                                                    Get.to(
                                                        D2dNonWorkingTeamScreen(
                                                      campId: details[i]
                                                          .campType
                                                          .toString(),
                                                      divId: details[i]
                                                          .divId
                                                          .toString(),
                                                      distCode: details[i]
                                                          .distlgdcode
                                                          .toString(),
                                                      labCode: '0',
                                                      orgId: '0',
                                                      title:
                                                          'D2D Non Working Teams',
                                                      desigId: details[i]
                                                          .desgId
                                                          .toString(),
                                                      empId: details[i]
                                                          .campCoId
                                                          .toString(),
                                                    ));
                                                  },
                                                  onTapWorking: () {
                                                    Get.to(
                                                        D2dNonWorkingTeamScreen(
                                                      campId: details[i]
                                                          .campType
                                                          .toString(),
                                                      divId: details[i]
                                                          .divId
                                                          .toString(),
                                                      distCode: details[i]
                                                          .distlgdcode
                                                          .toString(),
                                                      labCode: '0',
                                                      orgId: '0',
                                                      title: 'D2D Working Teams',
                                                      desigId: details[i]
                                                          .desgId
                                                          .toString(),
                                                      empId: details[i]
                                                          .campCoId
                                                          .toString(),
                                                    ));
                                                  },
                                                );
                                              },
                                            ),
                                          ),
                                  ],
                                );
                              },
                            ),
                          ),
                        ),
                      ),
                    ],
                  ).paddingSymmetric(vertical: 10.h, horizontal: 12.w),
                )
              : NoInternetWidget(
                  onRetryPressed: () => ctrl.checkInternetAndLoad(),
                ),
        );
      },
    );
  }

  void _filterBottomSheet(BuildContext context, D2DTeamsController ctrl) {
    final formKey = GlobalKey<FormState>();

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return GetBuilder<D2DTeamsController>(
          builder: (controller) {
            final selectedOrgName = controller.selectedOrg;
            final divisions = controller.d2dTeamsDivisionModel?.output ?? [];
            final shouldFilterDivisions =
                selectedOrgName == "HLL Lifecare LTD" ||
                    selectedOrgName == "HSCC(INDIA)LIMITED";
            final filteredDivisions = shouldFilterDivisions
                ? divisions
                    .where((e) => e.subOrgName == selectedOrgName)
                    .toList()
                : divisions;

            return DraggableScrollableSheet(
              expand: false,
              initialChildSize: 0.7,
              minChildSize: 0.4,
              maxChildSize: 0.95,
              builder: (_, scrollController) {
                return Container(
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.only(
                      topLeft: Radius.circular(20),
                      topRight: Radius.circular(20),
                    ),
                  ),
                  child: Form(
                    key: formKey,
                    child: LayoutBuilder(
                      builder: (context, constraints) {
                        return SingleChildScrollView(
                          controller: scrollController,
                          padding: EdgeInsets.only(
                            bottom: MediaQuery.of(context).viewInsets.bottom,
                          ),
                          child: Column(
                            children: [
                              CommonText(
                                text: "Filter",
                                fontSize: 14.sp,
                                fontWeight: FontWeight.w600,
                                textColor: kBlackColor,
                                textAlign: TextAlign.center,
                              ).paddingOnly(top: 14.h, bottom: 10.h),

                              // Camp Type
                              CommonDropdownField(
                                label: 'Camp Type',
                                items: [
                                  "All",
                                  ...(controller.campTypeListModel?.output
                                          .map((e) => e.description)
                                          .toList() ??
                                      []),
                                ],
                                value: controller.selectedCamp,
                                onChanged: (val) {
                                  controller.selectedCamp = val;
                                  controller.update();
                                },
                                icon: icMapPin,
                                isRequired: true,
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 8.h,
                                  left: 10.w,
                                  right: 10.w),

                              // Organization
                              CommonDropdownField(
                                label: 'Organization',
                                items: controller.organizationListModel?.output
                                        .map((e) => e.subOrgName)
                                        .toList() ??
                                    [],
                                value: controller.selectedOrg,
                                onChanged: (val) {
                                  controller.selectedOrg = val;
                                  controller.selectedDiv = null;
                                  controller.selectedDist = null;
                                  controller.selectedLab = null;
                                  controller.d2dTeamsLabModel = null;
                                  controller.update();
                                },
                                icon: icMapPin,
                                isRequired: true,
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 8.h,
                                  left: 10.w,
                                  right: 10.w),

                              // Division
                              CommonDropdownField(
                                label: 'Division',
                                items: [
                                  "Select All",
                                  ...filteredDivisions
                                      .map((e) => e.divName)
                                      .toList(),
                                ],
                                value: (controller.selectedDiv == "Select All" ||
                                        filteredDivisions.any((e) =>
                                            e.divName == controller.selectedDiv))
                                    ? controller.selectedDiv
                                    : null,
                                onChanged: (val) async {
                                  controller.selectedDiv = val;
                                  controller.selectedDist = null;
                                  controller.d2dTeamsLabModel = null;
                                  controller.selectedLab = null;
                                  controller.update();

                                  if (val == "Select All") return;

                                  final subOrgId = controller
                                          .organizationListModel?.output
                                          .firstWhereOrNull((e) =>
                                              e.subOrgName ==
                                              controller.selectedOrg)
                                          ?.subOrgId ??
                                      0;

                                  final divId = filteredDivisions
                                          .firstWhereOrNull((e) =>
                                              e.divName == controller.selectedDiv)
                                          ?.divId ??
                                      0;

                                  await controller.getDistrictList(
                                    subOrgId: subOrgId.toString(),
                                    divId: divId.toString(),
                                    distlgCode: '0',
                                  );
                                },
                                icon: icMapPin,
                                isRequired: true,
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 8.h,
                                  left: 10.w,
                                  right: 10.w),

                              // District
                              CommonDropdownField(
                                key: ValueKey(
                                    'district_${controller.bindDistrictResponse?.output?.length ?? 0}'),
                                label: 'District',
                                items: [
                                  "Select All",
                                  ...(controller.bindDistrictResponse?.output
                                          ?.map((e) => e.dISTNAME!)
                                          .toList() ??
                                      []),
                                ],
                                value: (controller.selectedDist == "Select All" ||
                                        (controller.bindDistrictResponse?.output
                                                ?.any((e) =>
                                                    e.dISTNAME ==
                                                    controller.selectedDist) ??
                                            false))
                                    ? controller.selectedDist
                                    : null,
                                onChanged: (val) async {
                                  controller.selectedDist = val;
                                  controller.selectedLab = null;
                                  controller.d2dTeamsLabModel = null;
                                  controller.update();

                                  if (val == "Select All") return;

                                  final distCode = controller
                                      .bindDistrictResponse?.output
                                      ?.firstWhereOrNull((e) =>
                                          e.dISTNAME == controller.selectedDist)
                                      ?.dISTLGDCODE;

                                  if (distCode != null) {
                                    await controller.getLabList(
                                        distlgCode: distCode.toString());
                                  }
                                },
                                icon: icMapPin,
                                isRequired: true,
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 8.h,
                                  left: 10.w,
                                  right: 10.w),

                              // Lab
                              CommonDropdownField(
                                label: 'Lab',
                                items: controller.d2dTeamsLabModel?.output
                                        .map((e) => e.labName)
                                        .toList() ??
                                    [],
                                value: controller.selectedLab,
                                onChanged: (val) {
                                  controller.selectedLab = val;
                                  controller.update();
                                },
                                icon: icMapPin,
                                isRequired: true,
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 8.h,
                                  left: 10.w,
                                  right: 10.w),

                              Row(
                                children: [
                                  Expanded(
                                    child: AppActiveButton(
                                      buttontitle: "Cancel",
                                      onTap: () => Get.back(),
                                    ),
                                  ),
                                  SizedBox(width: 12.w),
                                  Expanded(
                                    child: AppActiveButton(
                                      buttontitle: "Apply",
                                      onTap: () {
                                        final valid =
                                            formKey.currentState?.validate() ??
                                                false;
                                        if (valid) {
                                          ctrl.applyFilter();
                                          Get.back();
                                        } else {
                                          ScaffoldMessenger.of(
                                            context,
                                          ).showSnackBar(
                                            const SnackBar(
                                              content: Text(
                                                  'Please fill all required fields'),
                                              duration: Duration(seconds: 2),
                                            ),
                                          );
                                        }
                                      },
                                    ),
                                  ),
                                ],
                              ).paddingOnly(
                                  bottom: 8.h,
                                  top: 10.h,
                                  left: 10.w,
                                  right: 10.w),
                            ],
                          ),
                        );
                      },
                    ),
                  ),
                );
              },
            );
          },
        );
      },
    );
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
}

// ─── Table widget classes ──────────────────────────────────────────────────────

class HeaderRow extends StatelessWidget {
  const HeaderRow({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 60.h,
      color: kPrimaryColor,
      child: const Row(
        children: [
          FlexHeadCell('District', flex: 2),
          FlexHeadCell('Coordinator', flex: 3),
          FlexHeadCell('Not Working Team', flex: 2, center: true),
          FlexHeadCell('Working Teams', flex: 2, center: true),
          FlexHeadCell('Call', flex: 1, center: true, isLast: true),
        ],
      ),
    );
  }
}

class FlexHeadCell extends StatelessWidget {
  final String text;
  final int flex;
  final bool center;
  final bool isLast;

  const FlexHeadCell(
    this.text, {
    super.key,
    required this.flex,
    this.center = false,
    this.isLast = false,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        alignment: center ? Alignment.center : Alignment.centerLeft,
        padding: EdgeInsets.symmetric(horizontal: 6.w),
        decoration: BoxDecoration(
          border: Border(
            right: isLast
                ? BorderSide.none
                : BorderSide(
                    color: Colors.white.withOpacity(0.4), width: 1),
          ),
        ),
        child: Text(
          text,
          maxLines: 3,
          softWrap: true,
          style: TextStyle(
            color: Colors.white,
            fontSize: 14.sp,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
    );
  }
}

class TotalRow extends StatelessWidget {
  final int? totalNotWorking;
  final int? totalWorking;

  const TotalRow({super.key, this.totalNotWorking, this.totalWorking});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 46.h,
      color: Colors.grey.shade100,
      child: Row(
        children: [
          _cell('', flex: 2),
          _cell('Total', flex: 3, bold: true),
          _cell('${totalNotWorking ?? 0}', flex: 2, center: true, bold: true),
          _cell('${totalWorking ?? 0}', flex: 2, center: true, bold: true),
          const Expanded(flex: 1, child: SizedBox()),
        ],
      ),
    );
  }

  Widget _cell(String text,
      {required int flex, bool center = false, bool bold = false}) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 6.w),
        decoration: BoxDecoration(
          border: Border(
            right:
                BorderSide(color: Colors.grey.withOpacity(0.3), width: 1),
          ),
        ),
        child: Text(
          text,
          maxLines: 2,
          softWrap: true,
          textAlign: center ? TextAlign.center : TextAlign.left,
          style: TextStyle(
            fontSize: 14.sp,
            fontWeight: bold ? FontWeight.w700 : FontWeight.w400,
            color: kBlackColor,
          ),
        ),
      ),
    );
  }
}

class DataRow extends StatelessWidget {
  final String district;
  final String coordinator;
  final int notWorking;
  final int working;
  final VoidCallback? onTapCall;
  final VoidCallback? onTapNonWorking;
  final VoidCallback? onTapWorking;

  const DataRow({
    super.key,
    required this.district,
    required this.coordinator,
    required this.notWorking,
    required this.working,
    this.onTapCall,
    this.onTapNonWorking,
    this.onTapWorking,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 46.h,
      decoration: BoxDecoration(
        border: Border(
          bottom: BorderSide(color: Colors.grey.withOpacity(0.15)),
        ),
      ),
      child: Row(
        children: [
          _cell(district, flex: 2),
          _cell(coordinator, flex: 3),
          _cell('$notWorking', flex: 2, center: true, onTap: onTapNonWorking),
          _cell('$working', flex: 2, center: true, onTap: onTapWorking),
          Expanded(
            flex: 1,
            child: Center(
              child: IconButton(
                icon: Icon(Icons.call, size: 18, color: kPrimaryColor),
                onPressed: onTapCall,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _cell(String text,
      {required int flex, bool center = false, VoidCallback? onTap}) {
    return Expanded(
      flex: flex,
      child: InkWell(
        onTap: onTap,
        child: Container(
          padding: EdgeInsets.symmetric(horizontal: 6.w),
          decoration: BoxDecoration(
            border: Border(
              right:
                  BorderSide(color: Colors.grey.withOpacity(0.3), width: 1),
            ),
          ),
          child: Text(
            text,
            style: TextStyle(fontSize: 14.sp),
            maxLines: 3,
            softWrap: true,
            textAlign: center ? TextAlign.center : TextAlign.left,
          ),
        ),
      ),
    );
  }
}

class CommonDropdownField<T> extends StatelessWidget {
  final String label;
  final String? icon;
  final List<String> items;
  final String? value;
  final Function(String?) onChanged;
  final bool isRequired;

  const CommonDropdownField({
    super.key,
    required this.label,
    this.icon,
    required this.items,
    required this.value,
    required this.onChanged,
    required this.isRequired,
  });

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        return DropdownButtonFormField<String>(
          autovalidateMode: AutovalidateMode.onUserInteraction,
          validator: isRequired
              ? (value) => value == null ? "$label is required" : null
              : null,
          value: value,
          onChanged: onChanged,
          icon: const Icon(Icons.keyboard_arrow_down),
          isExpanded: true,
          selectedItemBuilder: (BuildContext context) {
            return items.map((item) {
              return Container(
                alignment: Alignment.centerLeft,
                constraints:
                    BoxConstraints(maxWidth: constraints.maxWidth - 30),
                child: Text(
                  item,
                  maxLines: 2,
                  softWrap: true,
                  style: TextStyle(fontSize: 14.sp, color: kBlackColor),
                ),
              );
            }).toList();
          },
          items: items
              .map((item) => DropdownMenuItem(
                    value: item,
                    child: Text(
                      item,
                      maxLines: 2,
                      softWrap: true,
                      style: TextStyle(fontSize: 14.sp, color: kBlackColor),
                    ),
                  ))
              .toList(),
          decoration: InputDecoration(
            label: RichText(
              text: TextSpan(
                text: label,
                style: TextStyle(
                  fontFamily: 'Nunito Sans',
                  fontSize: 16.sp,
                  color: kBlackColor,
                ),
                children: isRequired
                    ? [
                        TextSpan(
                          text: ' *',
                          style: TextStyle(color: noteRedColor),
                        ),
                      ]
                    : [],
              ),
            ),
            border: OutlineInputBorder(
              borderSide: BorderSide(color: dropDownTitleHeader),
            ),
            enabledBorder: OutlineInputBorder(
              borderSide: BorderSide(color: dropDownTitleHeader),
            ),
            focusedBorder: OutlineInputBorder(
              borderSide: BorderSide(color: kPrimaryColor, width: 1.2.w),
            ),
            contentPadding:
                EdgeInsets.symmetric(vertical: 10.h, horizontal: 10.w),
          ),
        );
      },
    );
  }
}