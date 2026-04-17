// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
// import 'package:s2toperational/Modules/Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppDateTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/controller/rejected_beneficiary_info_controller.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/screen/RejectedBeneficiaryListScreen/RejectedBeneficiaryTeamView.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
// import 'package:s2toperational/Views/RejectedBeneficiaryTeamView/RejectedBeneficiaryTeamView.dart';
import '../../../model/RecollectionAssignmentRemarksResponse.dart';
import '../../../model/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import 'RejectedBeneficiaryDetailsView/RejectedBeneficiaryDetailsView.dart';
import 'RejectionDetailsView/RejectionDetailsView.dart';

class RejectedBeneficiaryInfoScreen extends StatefulWidget {
  const RejectedBeneficiaryInfoScreen({
    super.key,
    required this.obj,
    required this.fromDate,
    required this.toDate,
    required this.oganizationId,
    required this.divisionId,
    required this.dISTLGDCODE,
    required this.tALLGDCODE,
    required this.landingLabId,
    required this.campType,
    required this.statusType,
  });

  final RecollectionBeneficiaryStatusandDetailsCountV1Output obj;
  final String fromDate;
  final String toDate;
  final String oganizationId;
  final String divisionId;
  final String dISTLGDCODE;
  final String tALLGDCODE;
  final String landingLabId;
  final String campType;
  final String statusType;

  @override
  State<RejectedBeneficiaryInfoScreen> createState() =>
      _RejectedBeneficiaryInfoScreenState();
}

class _RejectedBeneficiaryInfoScreenState
    extends State<RejectedBeneficiaryInfoScreen> {
  late final RejectedBeneficiaryInfoController c;

  @override
  void initState() {
    super.initState();
    Get.delete<RejectedBeneficiaryInfoController>(force: true);
    c = Get.put(RejectedBeneficiaryInfoController());
    c.initWithParams(
      obj: widget.obj,
      fromDate: widget.fromDate,
      toDate: widget.toDate,
      oganizationId: widget.oganizationId,
      divisionId: widget.divisionId,
      dISTLGDCODE: widget.dISTLGDCODE,
      tALLGDCODE: widget.tALLGDCODE,
      landingLabId: widget.landingLabId,
      campType: widget.campType,
      statusType: widget.statusType,
    );
  }

  @override
  void dispose() {
    Get.delete<RejectedBeneficiaryInfoController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<RejectedBeneficiaryInfoController>(
      builder: (c) => Scaffold(
        appBar: mAppBar(
          scTitle: "Beneficiary ",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: SingleChildScrollView(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(12, 0, 12, 8),
              child: Column(
                children: [
                  RejectedBeneficiaryDetailsView(
                    beneficiaryObj: c.beneficiaryObj,
                    mobile: widget.obj.mobileNo ?? "",
                  ),
                  const SizedBox(height: 10),
                  RejectionDetailsView(beneficiaryObj: c.beneficiaryObj),
                  const SizedBox(height: 10),

                  // ── Action card ───────────────────────────────────────
                  Container(
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
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          c.teamActivae,
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kPrimaryColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w600,
                          ),
                        ),

                        // Team selector (non-group1 only)
                        if (c.showTeam) ...[
                          const SizedBox(height: 10),
                          AppTextField(
                            onTap: () => _showTeamSheet(context),
                            controller: TextEditingController(
                                text: c.teamName),
                            readOnly: true,
                            label: RichText(
                              text: TextSpan(
                                text: 'Select Team *',
                                style: TextStyle(
                                  color: kLabelTextColor,
                                  fontSize: 14.sp,
                                  fontFamily: FontConstants.interFonts,
                                ),
                              ),
                            ),
                            suffixIcon: Icon(
                              Icons.keyboard_arrow_down_outlined,
                              color: kPrimaryColor,
                              size: 26,
                            ).paddingOnly(left: 6.w),
                            prefixIcon: Image.asset(
                              icUsersGroup,
                              color: kPrimaryColor,
                              width: 24,
                              height: 24,
                            ),
                          ),
                        ],

                        // Remark (group1 only)
                        if (c.showRemark) ...[
                          const SizedBox(height: 10),
                          AppDropdownTextfield(
                            icon: iconDocument,
                            titleHeaderString: "Remark *",
                            valueString:
                                c.selectedRemark?.assignmentRemarks ?? "",
                            onTap: () => _showRemarkSheet(context),
                          ),
                        ],

                        // Appointment date (when remark = 3)
                        if (c.showAppoointmentDate) ...[
                          const SizedBox(height: 10),
                          AppDateTextfield(
                            icon: icCalendarMonth,
                            titleHeaderString: "Appointment Date *",
                            valueString: c.appoointmentDate,
                            onTap: () =>
                                c.selectAppointmentDate(context),
                          ),
                        ],
                      ],
                    ),
                  ),
                  const SizedBox(height: 30),

                  // ── Buttons ───────────────────────────────────────────
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      if (c.showAssignButton)
                        SizedBox(
                          width: 140,
                          height: 40,
                          child: AppActiveButton(
                            buttontitle: c.buttonTitle,
                            isCancel: false,
                            onTap: () => _onSubmitTap(context),
                          ),
                        ),
                      if (c.showAssignButton && c.showReRegisterButton)
                        const SizedBox(width: 12),
                      if (c.showReRegisterButton)
                        SizedBox(
                          width: 140,
                          height: 40,
                          child: AppActiveButton(
                            buttontitle: "Re-Register",
                            isCancel: false,
                            onTap: () => ToastManager.toast(
                                "Re-Register screen not yet implemented"),
                          ),
                        ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  // ─── Team bottom sheet ────────────────────────────────────────────────────────

  Future<void> _showTeamSheet(BuildContext ctx) async {
    final list = await c.fetchTeamList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No teams found");
      return;
    }
    if (!mounted) return;
    showModalBottomSheet(
      context: ctx,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (context) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.38,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: RejectedBeneficiaryTeamView(
          list: list,
          onTapTeam: (p0) {
            c.selectTeam(p0);
          },
        ),
      ),
    );
  }

  // ─── Remark bottom sheet ──────────────────────────────────────────────────────

  Future<void> _showRemarkSheet(BuildContext ctx) async {
    final list = await c.fetchRemarkList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No remarks found");
      return;
    }
    if (!mounted) return;
    showModalBottomSheet(
      context: ctx,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (context) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: DropDownListScreen(
          titleString: "Remark",
          dropDownList: list,
          dropDownMenu: DropDownTypeMenu.RejectedStatus,
          onApplyTap: (p0) =>
              c.selectRemark(p0 as RecollectionAssignmentRemarksOutput),
        ),
      ),
    );
  }

  // ─── Submit ───────────────────────────────────────────────────────────────────

  void _onSubmitTap(BuildContext ctx) {
    ToastManager().showConfirmationDialog(
      context: ctx,
      message: 'Are you sure you want to Continue?',
      didSelectYes: (bool confirmed) async {
        if (!confirmed) {
          Navigator.pop(ctx);
          return;
        }
        Navigator.pop(ctx); // close dialog
        final bool success =
            c.isGroup1 ? await c.submitGroup1() : await c.submitGroup2();
        if (success) {
          final String msg = c.isGroup1
              ? (c.remarkId == 3
                  ? "Appointment Booked successfully"
                  : "Data Saved successfully")
              : "Team assigned successfully";
          if (!mounted) return;
          ToastManager().showSuccessOkayDialog(
            context: context,
            title: "Success",
            message: msg,
            onTap: () {
              Navigator.pop(context); // close dialog
              Navigator.pop(context, true); // pop with refresh signal
            },
          );
        }
      },
    );
  }
}
