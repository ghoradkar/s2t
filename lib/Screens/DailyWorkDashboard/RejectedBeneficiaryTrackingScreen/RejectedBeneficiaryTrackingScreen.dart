// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/utilities/WidgetPaddingX.dart';

class RejectedBeneficiaryTrackingScreen extends StatefulWidget {
  final String fromDate;
  final String toDate;
  final String oganizationId;
  final String divisionId;
  final String dISTLGDCODE;
  final String tALLGDCODE;
  final String landingLabId;
  final String campType;
  final String searchFilterId;


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


  @override
  State<RejectedBeneficiaryTrackingScreen> createState() =>
      _RejectedBeneficiaryTrackingScreenState();
}

class _RejectedBeneficiaryTrackingScreenState
    extends State<RejectedBeneficiaryTrackingScreen> {
  final APIManager apiManager = APIManager();
  List<RecollectionBeneficiaryDashboardForMobOutput> trackingList = [];

  String fromDate = "";
  String toDate = "";
  int dESGID = 0;
  int empCode = 0;

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (widget.fromDate.isNotEmpty) {
      fromDate = widget.fromDate;
    } else {
      DateTime firstDate = DateTime.now().subtract(const Duration(days: 10));
      fromDate = FormatterManager.formatDateToString(firstDate);
    }

    if (widget.toDate.isNotEmpty) {
      toDate = widget.toDate;
    } else {
      toDate = FormatterManager.formatDateToString(DateTime.now());
    }

    callAPI();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Rejected Beneficiary Tracking",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: Padding(
          padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
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
                        onTap: () {
                          _selectFromDate(context);
                        },
                        controller: TextEditingController(text: fromDate),
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
                        onTap: () {
                          _selectToDate(context);
                        },
                        controller: TextEditingController(text: toDate),
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
              _trackingHeaderRow(),
              Expanded(
                child:
                    trackingList.isEmpty
                        ? const SizedBox.shrink()
                        : ListView.builder(
                          itemCount: trackingList.length,
                          itemBuilder: (context, index) {
                            final obj = trackingList[index];
                            return _trackingDataRow(obj);
                          },
                        ),
              ),
            ],
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

  Widget _trackingDataRow(RecollectionBeneficiaryDashboardForMobOutput obj) {
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
        padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 4),
        decoration: BoxDecoration(
          border: Border(
            right: isLast ? BorderSide.none : BorderSide(color: kBlackColor),
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
        padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 4),
        decoration: BoxDecoration(
          border: Border(
            right:
                isLast ? BorderSide.none : BorderSide(color: kTextFieldBorder),
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

  Future<void> callAPI() async {
    if (toDate.isEmpty) {
      return;
    }

    ToastManager.showLoader();
    Map<String, String> params = {
      "Fromdate": fromDate,
      "Todate": toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": widget.divisionId,
      "DISTLGDCODE": widget.dISTLGDCODE,
      "Arid": "0",
      "UserId": empCode.toString(),
      "DESGID": dESGID.toString(),
      "Type": "1",
    };

    apiManager.getRecollectionBeneficiaryDashboardForMobAPI(
      params,
      apiTrackingDataCallBack,
    );
  }

  void apiTrackingDataCallBack(
    RecollectionBeneficiaryDashboardForMobResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      trackingList = response?.output ?? [];
      if (trackingList.isEmpty) {
        ToastManager.toast("No Data Found");
      }
    } else {
      trackingList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> _selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        fromDate = FormatterManager.formatDateToString(picked);
        toDate = "";
      });
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    if (fromDate.isEmpty) {
      ToastManager.toast("Please Select From Date");
      return;
    }

    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      setState(() {
        toDate = FormatterManager.formatDateToString(picked);
        if (FormatterManager.isAscendingOrder(fromDate, toDate)) {
          callAPI();
        } else {
          toDate = "";
          ToastManager.toast("To Date cannot be before From Date");
        }
      });
    }
  }
}
