// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/controller/rejected_beneficiary_list_controller.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'RejectedBeneficiaryInfoScreen/RejectedBeneficiaryInfoScreen.dart';
import 'RejectedBeneficiaryRow/RejectedBeneficiaryRow.dart';

class RejectedBeneficiaryListScreen extends StatefulWidget {
  const RejectedBeneficiaryListScreen({
    super.key,
    required this.fromDate,
    required this.toDate,
    required this.oganizationId,
    required this.divisionId,
    required this.dISTLGDCODE,
    required this.tALLGDCODE,
    required this.landingLabId,
    required this.campType,
    required this.searchFilterId,
  });

  final String fromDate;
  final String toDate;
  final String oganizationId;
  final String divisionId;
  final String dISTLGDCODE;
  final String tALLGDCODE;
  final String landingLabId;
  final String campType;
  final String searchFilterId;

  @override
  State<RejectedBeneficiaryListScreen> createState() =>
      _RejectedBeneficiaryListScreenState();
}

class _RejectedBeneficiaryListScreenState
    extends State<RejectedBeneficiaryListScreen> {
  late final RejectedBeneficiaryListController c;

  @override
  void initState() {
    super.initState();
    Get.delete<RejectedBeneficiaryListController>(force: true);
    c = Get.put(RejectedBeneficiaryListController());
    c.initWithParams(
      fromDate: widget.fromDate,
      toDate: widget.toDate,
      oganizationId: widget.oganizationId,
      divisionId: widget.divisionId,
      dISTLGDCODE: widget.dISTLGDCODE,
      tALLGDCODE: widget.tALLGDCODE,
      landingLabId: widget.landingLabId,
      campType: widget.campType,
      searchFilterId: widget.searchFilterId,
    );
  }

  @override
  void dispose() {
    Get.delete<RejectedBeneficiaryListController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<RejectedBeneficiaryListController>(
      builder: (c) => Scaffold(
        appBar: mAppBar(
          scTitle: "Rejected Beneficiary List",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 10),

                // ── Filter card ───────────────────────────────────────────
                Container(
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
                      // Status
                      AppTextField(
                        onTap: () => _showStatusDropdown(context),
                        controller: TextEditingController(
                          text: c.selectedStatus?.assignmentRemarks ?? "",
                        ),
                        readOnly: true,
                        label: RichText(
                          text: TextSpan(
                            text: 'Status*',
                            style: TextStyle(
                              color: kLabelTextColor,
                              fontSize: 14.sp,
                              fontFamily: FontConstants.interFonts,
                            ),
                          ),
                        ),
                        suffixIcon:
                            const Icon(Icons.arrow_drop_down_outlined),
                        prefixIcon: Image.asset(
                          icnTent,
                          color: kPrimaryColor,
                        ).paddingOnly(left: 6.w),
                      ),
                      const SizedBox(height: 10),

                      // Search By
                      AppTextField(
                        onTap: () => _showSearchByDropdown(context),
                        controller: TextEditingController(
                          text: c.selectedSearchBy?.assignmentRemarks ?? "",
                        ),
                        readOnly: true,
                        label: RichText(
                          text: TextSpan(
                            text: 'Search By*',
                            style: TextStyle(
                              color: kLabelTextColor,
                              fontSize: 14.sp,
                              fontFamily: FontConstants.interFonts,
                            ),
                          ),
                        ),
                        suffixIcon:
                            const Icon(Icons.arrow_drop_down_outlined),
                        prefixIcon: const Icon(
                          Icons.search_sharp,
                          color: kPrimaryColor,
                          size: 36,
                        ).paddingOnly(left: 6.w),
                      ),
                      const SizedBox(height: 10),

                      // Search Value
                      AppTextField(
                        onChange: (value) => c.applySearch(value),
                        controller: c.searchTextEditingController,
                        label: RichText(
                          text: TextSpan(
                            text: 'Search Value*',
                            style: TextStyle(
                              color: kLabelTextColor,
                              fontSize: 14.sp,
                              fontFamily: FontConstants.interFonts,
                            ),
                          ),
                        ),
                        suffixIcon: const Icon(
                          Icons.search_sharp,
                          color: kPrimaryColor,
                          size: 36,
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 10),

                // ── List ──────────────────────────────────────────────────
                Expanded(
                  child: Column(
                    children: [
                      // Table header
                      Container(
                        decoration: BoxDecoration(
                          color: kPrimaryColor,
                          borderRadius: const BorderRadius.only(
                            topLeft: Radius.circular(8),
                            topRight: Radius.circular(8),
                          ),
                        ),
                        height: 40,
                        child: Row(
                          children: [
                            _headerCell(width: 48, text: "Sr. No."),
                            _headerCellExpanded(text: "Beneficiary Name"),
                            _headerCell(width: 68, text: "Area"),
                            _headerCell(
                                width: 84, text: "Pin code", last: true),
                          ],
                        ),
                      ),

                      // Rows
                      Expanded(
                        child: ListView.builder(
                          itemCount:
                              c.searchRejectedBeneficaryList.length,
                          itemBuilder: (context, index) {
                            final obj =
                                c.searchRejectedBeneficaryList[index];
                            return GestureDetector(
                              onTap: () =>
                                  _openInfoScreen(context, obj),
                              child: RejectedBeneficiaryRow(
                                index: index,
                                obj: obj,
                              ),
                            );
                          },
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // ── Table header helpers ──────────────────────────────────────────────────

  Widget _headerCell({
    required double width,
    required String text,
    bool last = false,
  }) {
    return Container(
      width: width,
      decoration: BoxDecoration(
        color: Colors.transparent,
        border: Border(
          right: last
              ? BorderSide.none
              : const BorderSide(color: kBlackColor, width: 1),
        ),
      ),
      child: Center(
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kWhiteColor,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w500,
            fontSize: responsiveFont(13),
          ),
        ),
      ),
    );
  }

  Widget _headerCellExpanded({required String text}) {
    return Expanded(
      child: Container(
        decoration: const BoxDecoration(
          color: Colors.transparent,
          border: Border(
            right: BorderSide(color: kBlackColor, width: 1),
          ),
        ),
        padding: const EdgeInsets.fromLTRB(4, 0, 4, 0),
        child: Center(
          child: Text(
            text,
            textAlign: TextAlign.left,
            style: TextStyle(
              color: kWhiteColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w500,
              fontSize: responsiveFont(13),
            ),
          ),
        ),
      ),
    );
  }

  // ── Bottom sheet helpers ──────────────────────────────────────────────────

  Future<void> _showStatusDropdown(BuildContext ctx) async {
    final list = await c.fetchStatusList();
    if (list == null || list.isEmpty) {
      ToastManager.toast("No status options found");
      return;
    }
    _showBottomSheet(ctx, "Status", list, DropDownTypeMenu.RejectedStatus,
        (p0) {
      c.setStatus(p0 as RecollectionAssignmentRemarksOutput);
    });
  }

  void _showSearchByDropdown(BuildContext ctx) {
    _showBottomSheet(
      ctx,
      "Select Search By",
      c.searcyByList,
      DropDownTypeMenu.SearchBy,
      (p0) => c.setSearchBy(p0 as RecollectionAssignmentRemarksOutput),
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
          height: MediaQuery.of(context).size.width * 1.33,
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
            onApplyTap: (p0) => onApply(p0),
          ),
        );
      },
    );
  }

  void _openInfoScreen(
    BuildContext ctx,
    RecollectionBeneficiaryStatusandDetailsCountV1Output obj,
  ) {
    Navigator.push(
      ctx,
      MaterialPageRoute(
        builder: (_) => RejectedBeneficiaryInfoScreen(
          obj: obj,
          fromDate: widget.fromDate,
          toDate: widget.toDate,
          oganizationId: widget.oganizationId,
          divisionId: widget.divisionId,
          dISTLGDCODE: widget.dISTLGDCODE,
          tALLGDCODE: widget.tALLGDCODE,
          landingLabId: widget.landingLabId,
          campType: widget.campType,
          statusType: c.selectedStatus?.arId.toString() ?? "0",
        ),
      ),
    ).then((result) {
      if (result == true) {
        c.callAPICall();
      }
    });
  }
}
