// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/DailyWorkDashboard/controller/rejected_beneficiary_tracking_controller.dart';

class RejectedBeneficiaryTrackingScreen extends StatefulWidget {
  const RejectedBeneficiaryTrackingScreen({
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
  State<RejectedBeneficiaryTrackingScreen> createState() =>
      _RejectedBeneficiaryTrackingScreenState();
}

class _RejectedBeneficiaryTrackingScreenState
    extends State<RejectedBeneficiaryTrackingScreen> {
  late final RejectedBeneficiaryTrackingController c;

  @override
  void initState() {
    super.initState();
    Get.delete<RejectedBeneficiaryTrackingController>(force: true);
    c = Get.put(RejectedBeneficiaryTrackingController());
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
    Get.delete<RejectedBeneficiaryTrackingController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<RejectedBeneficiaryTrackingController>(
      builder: (c) => Scaffold(
        appBar: mAppBar(
          scTitle: "Rejected Beneficiary Tracking",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // ── Date filter card ──────────────────────────────────────
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
                  child: Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          onTap: () => c.selectFromDate(context),
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
                          onTap: () => c.selectToDate(context),
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
                ),
                const SizedBox(height: 10),

                // ── Table header ──────────────────────────────────────────
                _trackingHeaderRow(),

                // ── Table rows ────────────────────────────────────────────
                Expanded(
                  child: c.trackingList.isEmpty
                      ? const SizedBox.shrink()
                      : ListView.builder(
                          itemCount: c.trackingList.length,
                          itemBuilder: (context, index) {
                            return _trackingDataRow(
                                c.trackingList[index]);
                          },
                        ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _trackingHeaderRow() {
    return Container(
      decoration: BoxDecoration(
        color: kPrimaryColor,
        border: Border.all(color: kTextFieldBorder),
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(6),
          topRight: Radius.circular(6),
        ),
      ),
      child: Row(
        children: [
          _headerCell("District"),
          _headerCell("Rejected"),
          _headerCell("Interested"),
          _headerCell("Not\nInterested"),
          _headerCell("Denied"),
          _headerCell("Re\nScreened", isLast: true),
        ],
      ),
    );
  }

  Widget _trackingDataRow(
      RecollectionBeneficiaryDashboardForMobOutput obj) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        border: Border(
          left: BorderSide(color: kTextFieldBorder),
          right: BorderSide(color: kTextFieldBorder),
          bottom: BorderSide(color: kTextFieldBorder),
        ),
      ),
      child: Row(
        children: [
          _dataCell(obj.district ?? ""),
          _dataCell((obj.rejectedBeneficiaries ?? 0).toString()),
          _dataCell((obj.interestedInScreening ?? 0).toString()),
          _dataCell((obj.notInterestedInScreening ?? 0).toString()),
          _dataCell((obj.deniedForScreening ?? 0).toString()),
          _dataCell(
            (obj.reScreenedBeneficiaries ?? 0).toString(),
            isLast: true,
          ),
        ],
      ),
    );
  }

  Widget _headerCell(String text, {bool isLast = false}) {
    return Expanded(
      child: Container(
        padding:
            const EdgeInsets.symmetric(vertical: 6, horizontal: 4),
        decoration: BoxDecoration(
          border: Border(
            right: isLast
                ? BorderSide.none
                : BorderSide(color: kBlackColor),
          ),
        ),
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kWhiteColor,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w500,
            fontSize: responsiveFont(11),
          ),
        ),
      ),
    );
  }

  Widget _dataCell(String text, {bool isLast = false}) {
    return Expanded(
      child: Container(
        padding:
            const EdgeInsets.symmetric(vertical: 6, horizontal: 4),
        decoration: BoxDecoration(
          border: Border(
            right: isLast
                ? BorderSide.none
                : BorderSide(color: kTextFieldBorder),
          ),
        ),
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kBlackColor,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w500,
            fontSize: responsiveFont(11),
          ),
        ),
      ),
    );
  }
}
