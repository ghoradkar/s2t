// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/controller/daily_work_dashboard_controller.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/screen/RejectedBeneficiaryListScreen/RejectedBeneficiaryListScreen.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/screen/RejectedBeneficiaryTrackingScreen/RejectedBeneficiaryTrackingScreen.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';

class DailyWorkDashboardScreen extends StatelessWidget {
  const DailyWorkDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final c = Get.put(DailyWorkDashboardController());
    SizeConfig().init(context);
    return GetBuilder<DailyWorkDashboardController>(
      builder: (c) => Scaffold(
        appBar: mAppBar(
          scTitle: "Daily Work Dashboard",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
          showActions: !c.isGroup1,
          actions: [
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
              child: GestureDetector(
                onTap: () => c.toggleFilter(),
                child: SizedBox(
                  width: 26,
                  height: 26,
                  child: Image.asset(icFilter, color: kWhiteColor),
                ),
              ),
            ),
          ],
        ),
        body: KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(0, 12, 0, 12),
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // ── Camp type toggle (hidden for group1) ──────────────────────
                  if (!c.isGroup1)
                    _CampTypeToggle(c: c)
                        .paddingSymmetric(horizontal: 6, vertical: 4),
                  const SizedBox(height: 10),

                  // ── Filter card ───────────────────────────────────────────────
                  _FilterCard(c: c, screenContext: context)
                      .paddingOnly(left: 12, right: 12),
                  const SizedBox(height: 10),

                  // ── Note ──────────────────────────────────────────────────────
                  CommonText(
                    text:
                        "Note : Click on rejected beneficiary count for rejected beneficiary list",
                    fontSize: 14.sp,
                    fontWeight: FontWeight.normal,
                    textColor: noteRedColor,
                    textAlign: TextAlign.start,
                  ).paddingOnly(left: 16.w, bottom: 12.h),

                  // ── Dashboard table ───────────────────────────────────────────
                  _DashboardTable(c: c, screenContext: context)
                      .paddingOnly(left: 12, right: 12),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// ─── Camp type toggle ─────────────────────────────────────────────────────────

class _CampTypeToggle extends StatelessWidget {
  final DailyWorkDashboardController c;
  const _CampTypeToggle({required this.c});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      height: 50,
      decoration: BoxDecoration(
        color: Colors.transparent,
        border: Border.all(color: Colors.white, width: 1),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Row(
        children: [
          Expanded(
            child: GestureDetector(
              onTap: () => c.setRegularCamp(),
              child: Container(
                decoration: BoxDecoration(
                  color: c.regularCamp ? kPrimaryColor : kWhiteColor,
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(20),
                    bottomLeft: Radius.circular(20),
                  ),
                  border: Border.all(
                    color: Colors.grey.withValues(alpha: 0.3),
                  ),
                ),
                child: Center(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      SizedBox(
                        width: 20,
                        height: 20,
                        child: Image.asset(
                          icnTent,
                          color: c.regularCamp ? kWhiteColor : kPrimaryColor,
                        ),
                      ),
                      const SizedBox(width: 6),
                      Text(
                        "Regular Camp",
                        style: TextStyle(
                          fontFamily: "Inter",
                          color: c.regularCamp ? kWhiteColor : kPrimaryColor,
                          fontSize: responsiveFont(14),
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () => c.setDoorToDoorCamp(),
              child: Container(
                decoration: BoxDecoration(
                  color: c.doorToDoorCamp ? kPrimaryColor : kWhiteColor,
                  borderRadius: const BorderRadius.only(
                    topRight: Radius.circular(20),
                    bottomRight: Radius.circular(20),
                  ),
                  border: Border.all(
                    color: Colors.grey.withValues(alpha: 0.3),
                  ),
                ),
                child: Center(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      SizedBox(
                        width: 20,
                        height: 20,
                        child: Image.asset(
                          "assets/icons/home-2.png",
                          color:
                              c.doorToDoorCamp ? kWhiteColor : kPrimaryColor,
                        ),
                      ),
                      const SizedBox(width: 6),
                      Text(
                        "Door to Door Camp",
                        style: TextStyle(
                          fontFamily: "Inter",
                          color:
                              c.doorToDoorCamp ? kWhiteColor : kPrimaryColor,
                          fontSize: responsiveFont(14),
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ─── Filter card ──────────────────────────────────────────────────────────────

class _FilterCard extends StatelessWidget {
  final DailyWorkDashboardController c;
  final BuildContext screenContext;
  const _FilterCard({required this.c, required this.screenContext});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: SizeConfig.screenWidth,
      padding: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.15),
            spreadRadius: 0,
            blurRadius: 10,
          ),
        ],
      ),
      child: Column(
        children: [
          // ── Date row ──────────────────────────────────────────────────────
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  onTap: () => c.selectFromDate(screenContext),
                  controller:
                      TextEditingController(text: c.fromDate),
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'From Date *',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                  prefixIcon: Image.asset(
                    icCalendarMonth,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.w),
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: AppTextField(
                  onTap: () => c.selectToDate(screenContext),
                  controller:
                      TextEditingController(text: c.toDate),
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'To Date *',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                    ),
                  ),
                  prefixIcon: Image.asset(
                    icCalendarMonth,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.w),
                ),
              ),
            ],
          ),

          // ── Sub Organization (admin only) ─────────────────────────────────
          Visibility(
            visible: c.isShowFilter && !c.isGroup1 && !c.isGroup2,
            child: AppTextField(
              onTap: () => _showSubOrgDropdown(screenContext),
              controller: TextEditingController(
                text: c.selectedSubOrganization?.subOrgName ?? "",
              ),
              readOnly: true,
              label: RichText(
                text: TextSpan(
                  text: 'Sub Organization',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: 14.sp,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              prefixIcon: Image.asset(
                icMapPin,
                color: kPrimaryColor,
              ).paddingOnly(left: 6.w),
              suffixIcon: const Icon(Icons.keyboard_arrow_down),
            ).paddingOnly(top: 8.h),
          ),

          // ── Division + District row (admin only) ──────────────────────────
          Visibility(
            visible: c.isShowFilter && !c.isGroup1 && !c.isGroup2,
            child: Row(
              children: [
                Expanded(
                  child: AppTextField(
                    onTap: () {
                      if (c.selectedSubOrganization == null) {
                        ToastManager.toast(
                            "Please select sub organization");
                        return;
                      }
                      _showDivisionDropdown(screenContext);
                    },
                    controller: TextEditingController(
                      text: c.selectedDivision?.dIVNAME ?? "",
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'Division',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icMapPin,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6.w),
                    suffixIcon: const Icon(Icons.keyboard_arrow_down),
                  ),
                ),
                SizedBox(width: 10.w),
                Expanded(
                  child: AppTextField(
                    onTap: () {
                      if ([51, 47, 83, 166].contains(c.dESGID)) {
                        _showDistrictDropdown(screenContext);
                      } else {
                        if (c.selectedSubOrganization == null) {
                          ToastManager.toast(
                              "Please select sub organization");
                          return;
                        }
                        if (c.selectedDivision == null) {
                          ToastManager.toast("Please select division");
                          return;
                        }
                        _showDistrictDropdown(screenContext);
                      }
                    },
                    controller: TextEditingController(
                      text: c.selectedDistrict?.dISTNAME ?? "",
                    ),
                    readOnly: true,
                    label: RichText(
                      text: TextSpan(
                        text: 'District',
                        style: TextStyle(
                          color: kLabelTextColor,
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ),
                    prefixIcon: Image.asset(
                      icMapPin,
                      color: kPrimaryColor,
                    ).paddingOnly(left: 6.w),
                    suffixIcon: const Icon(Icons.keyboard_arrow_down),
                  ),
                ),
              ],
            ).paddingOnly(top: 8.h),
          ),

          // ── Lab (group2 and admin, not group3) ────────────────────────────
          Visibility(
            visible: c.isShowFilter && !c.isGroup1 && !c.isGroup3,
            child: AppTextField(
              onTap: () => _showLabDropdown(screenContext),
              controller: TextEditingController(
                text: c.selectedLabVal?.labName ?? "",
              ),
              readOnly: true,
              label: RichText(
                text: TextSpan(
                  text: 'Lab',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: 14.sp,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              prefixIcon: Image.asset(
                icMicroscope,
                color: kPrimaryColor,
              ).paddingOnly(left: 6.w),
              suffixIcon: const Icon(Icons.keyboard_arrow_down),
            ).paddingOnly(top: 8.h, bottom: 4.h),
          ),
        ],
      ),
    );
  }

  Future<void> _showSubOrgDropdown(BuildContext ctx) async {
    final list = await c.fetchSubOrganizationList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No sub organizations found");
      return;
    }
    _showBottomSheet(
      ctx,
      "Sub Organization",
      list,
      DropDownTypeMenu.SubOrganization,
      (selected) {
        c.setSubOrganization(selected as SubOrganizationOutput);
      },
    );
  }

  Future<void> _showDivisionDropdown(BuildContext ctx) async {
    final list = await c.fetchDivisionList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No divisions found");
      return;
    }
    _showBottomSheet(
      ctx,
      "Division",
      list,
      DropDownTypeMenu.BindDivision,
      (selected) {
        c.setDivision(selected as BindDivisionOutput);
      },
    );
  }

  Future<void> _showDistrictDropdown(BuildContext ctx) async {
    final list = await c.fetchDistrictList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No districts found");
      return;
    }
    _showBottomSheet(
      ctx,
      "District",
      list,
      DropDownTypeMenu.BindDistrict,
      (selected) {
        c.setDistrict(selected as BindDistrictOutput);
      },
    );
  }

  Future<void> _showLabDropdown(BuildContext ctx) async {
    final list = await c.fetchLabList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No labs found");
      return;
    }
    _showBottomSheet(
      ctx,
      "Lab",
      list,
      DropDownTypeMenu.BindLab,
      (selected) {
        c.setLab(selected as LandingLabCampCreationOutput);
        c.callAPI();
      },
    );
  }

  void _showBottomSheet(
    BuildContext ctx,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    void Function(dynamic) onApply,
  ) {
    showModalBottomSheet(
      context: ctx,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33 +
              MediaQuery.of(context).viewPadding.bottom,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) async {
              onApply(p0);
              if (dropDownType != DropDownTypeMenu.BindLab) {
                await c.callAPI();
              }
            },
          ),
        );
      },
    );
  }
}

// ─── Dashboard table ──────────────────────────────────────────────────────────

class _DashboardTable extends StatelessWidget {
  final DailyWorkDashboardController c;
  final BuildContext screenContext;
  const _DashboardTable({required this.c, required this.screenContext});

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Container(
        width: SizeConfig.screenWidth,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
          boxShadow: [
            BoxShadow(
              offset: const Offset(0, 1),
              color: Colors.black.withValues(alpha: 0.15),
              blurRadius: 10,
            ),
          ],
        ),
        child: c.isLoading
            ? CommonSkeletonScreeningDetailsTable(
                radius: 10,
                rowCount: 10,
                rowHeight: 30,
                headerHeight: 30,
              ).paddingOnly(top: 10)
            : Column(
                children: [
                  // Table header
                  Container(
                    height: 36,
                    decoration: const BoxDecoration(
                      color: kPrimaryColor,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(5),
                        topRight: Radius.circular(5),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        "Re-Screening",
                        style: TextStyle(
                          color: kWhiteColor,
                          fontFamily: "Inter",
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ),

                  _dashboardRow(
                    title: "Rejected Beneficiaries",
                    value: "${c.rejectedBeneficiaryCount ?? 0}",
                    valueColor: campOpenColor,
                    onTap: () => _onRejectedTap(screenContext),
                  ),
                  _dashboardRow(
                    title: "Screening Confirmation Pending",
                    value: "${c.screeningConfimPendingCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Assignment Pending",
                    value: "${c.assignmentPendingCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Interested in Screening",
                    value: "${c.interestedInScreeningCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Not Interested in Screening",
                    value: "${c.notinterestedInScreeningCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Wrong Number",
                    value: "${c.wrongNumberCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Not Available for Screening",
                    value: "${c.notAvailableForScreeningCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Sample Collected",
                    value: "${c.sampleCollectedCount ?? 0}",
                    valueColor: approvedBeneficiariesColor,
                  ),
                  _dashboardRow(
                    title: "Re-Screening Pending as Card Expired",
                    value: "${c.reScreeningPendingCount ?? 0}",
                  ),
                  _dashboardRow(
                    title: "Denied for Screening",
                    value: "${c.deniedScreeningCount ?? 0}",
                    valueColor: campOpenColor,
                    isLast: true,
                  ),
                ],
              ),
      ),
    );
  }

  void _onRejectedTap(BuildContext ctx) {
    if (c.isAdminDesignation) {
      Navigator.push(
        ctx,
        MaterialPageRoute(
          builder: (_) => RejectedBeneficiaryTrackingScreen(
            fromDate: c.fromDate,
            toDate: c.toDate,
            oganizationId: c.resolvedOrgId,
            divisionId: c.resolvedDivId,
            dISTLGDCODE: c.resolvedDistCode,
            tALLGDCODE: "0",
            landingLabId: c.resolvedLabCode,
            campType: c.resolvedCampType,
            searchFilterId: "0",
          ),
        ),
      );
    } else {
      Navigator.push(
        ctx,
        MaterialPageRoute(
          builder: (_) => RejectedBeneficiaryListScreen(
            fromDate: c.fromDate,
            toDate: c.toDate,
            oganizationId: c.resolvedOrgId,
            divisionId: c.resolvedDivId,
            dISTLGDCODE: c.resolvedDistCode,
            tALLGDCODE: "0",
            landingLabId: c.resolvedLabCode,
            campType: c.resolvedCampType,
            searchFilterId: "0",
          ),
        ),
      );
    }
  }

  Widget _dashboardRow({
    required String title,
    required String value,
    Color valueColor = Colors.black,
    VoidCallback? onTap,
    bool isLast = false,
  }) {
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: kWhiteColor,
          border: Border(
            left: BorderSide(color: kTextFieldBorder),
            right: BorderSide(color: kTextFieldBorder),
            top: BorderSide(color: kTextFieldBorder),
            bottom: isLast
                ? BorderSide(color: kTextFieldBorder)
                : BorderSide.none,
          ),
          borderRadius: isLast
              ? const BorderRadius.only(
                  bottomLeft: Radius.circular(5),
                  bottomRight: Radius.circular(5),
                )
              : BorderRadius.zero,
        ),
        padding: const EdgeInsets.symmetric(vertical: 10),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 8),
                decoration: BoxDecoration(
                  border: Border(right: BorderSide(color: kTextFieldBorder)),
                ),
                child: Text(
                  title,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  softWrap: true,
                  style: TextStyle(
                    color: kBlackColor,
                    fontFamily: "Inter",
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(13),
                  ),
                ),
              ),
            ),
            SizedBox(
              width: 100,
              child: Center(
                child: Text(
                  value,
                  style: TextStyle(
                    color: valueColor,
                    fontFamily: "Inter",
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(13),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
