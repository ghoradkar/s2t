import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/controller/ct_appointment_list_controller.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/screens/ct_appointment_confirmation_screen.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/widgets/ct_beneficiary_row.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/no_data_widget.dart';

class CTAppointmentListScreen extends StatefulWidget {
  const CTAppointmentListScreen({super.key});

  @override
  State<CTAppointmentListScreen> createState() =>
      _CTAppointmentListScreenState();
}

class _CTAppointmentListScreenState extends State<CTAppointmentListScreen> {
  late final CTAppointmentListController _c;

  @override
  void initState() {
    super.initState();
    Get.delete<CTAppointmentListController>(force: true);
    _c = Get.put(CTAppointmentListController());
  }

  @override
  void dispose() {
    Get.delete<CTAppointmentListController>(force: true);
    super.dispose();
  }

  void _showFilterSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => GetBuilder<CTAppointmentListController>(
            builder:
                (c) => Padding(
                  padding: EdgeInsets.fromLTRB(16.w, 20.h, 16.w, 32.h),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(height: 10.h),
                      Center(
                        child: CommonText(
                          text: 'Filters',
                          fontSize: 16.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.center,
                        ),
                      ),
                      SizedBox(height: 16.h),
                      // From Date
                      AppTextField(
                        controller: c.fromDateController,
                        readOnly: true,
                        onTap: () => c.pickDate(c.fromDateController),
                        label: CommonText(
                          text: 'From Date',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.start,
                        ),
                        hint: 'dd-MM-yyyy',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        suffixIcon: Icon(
                          Icons.calendar_today,
                          size: 16.sp,
                          color: kPrimaryColor,
                        ),
                        fieldRadius: 8,
                      ),
                      SizedBox(height: 10.h),
                      // To Date
                      AppTextField(
                        controller: c.toDateController,
                        readOnly: true,
                        onTap: () => c.pickDate(c.toDateController),
                        label: CommonText(
                          text: 'To Date',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.start,
                        ),
                        hint: 'dd-MM-yyyy',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        suffixIcon: Icon(
                          Icons.calendar_today,
                          size: 16.sp,
                          color: kPrimaryColor,
                        ),
                        fieldRadius: 8,
                      ),
                      SizedBox(height: 10.h),
                      // District
                      AppTextField(
                        controller: c.districtController,
                        readOnly: true,
                        onTap: () => c.showDistrictPicker(),
                        label: CommonText(
                          text: 'District',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.start,
                        ),
                        hint: 'Select District',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        suffixIcon: Icon(
                          Icons.arrow_drop_down,
                          color: kPrimaryColor,
                        ),
                        fieldRadius: 8,
                      ),
                      SizedBox(height: 10.h),
                      // Status
                      AppTextField(
                        controller: c.remarkController,
                        readOnly: true,
                        onTap: () => c.showRemarkPicker(),
                        label: CommonText(
                          text: 'Status',
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.start,
                        ),
                        hint: 'Select Status',
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        suffixIcon: Icon(
                          Icons.arrow_drop_down,
                          color: kPrimaryColor,
                        ),
                        fieldRadius: 8,
                      ),
                      SizedBox(height: 18.h),
                      // Search button
                      Align(
                        alignment: Alignment.center,
                        child: SizedBox(
                          width: 200.w,
                          height: 46.h,
                          child: AppActiveButton(
                            buttontitle: 'Search',
                            onTap: () {
                              Navigator.pop(context);
                              c.fetchList();
                            },
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
          ),
    );
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Confirmatory Tests Screening',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
          showActions: true,
          actions: [
            Padding(
              padding: EdgeInsets.only(right: 12.w),
              child: GestureDetector(
                onTap: _showFilterSheet,
                child: Icon(
                  Icons.filter_alt_outlined,
                  color: Colors.white,
                  size: 24.sp,
                ),
              ),
            ),
          ],
        ),
        body: GetBuilder<CTAppointmentListController>(
          builder: (c) => _buildBody(c),
        ),
      ),
    );
  }

  Widget _buildSearchBar(CTAppointmentListController c) {
    return Padding(
      padding: EdgeInsets.fromLTRB(12.w, 14.h, 12.w, 4.h),
      child: AppTextField(
        controller: c.searchController,
        onChange: (q) => c.filterList(q),
        hint: 'Search by name or reg no...',
        label: CommonText(
          text: 'Search',
          fontSize: 14.sp,
          fontWeight: FontWeight.w400,
          textColor: kTextColor,
          textAlign: TextAlign.start,
        ),
        hintStyle: TextStyle(
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        ),
        prefixIcon: Icon(Icons.search, size: 18.sp, color: Colors.grey),
        fieldRadius: 8,
      ),
    );
  }

  // ── Table header with rounded top corners ─────────────────────────────────
  Widget _tableHeader() {
    return Container(
      decoration: const BoxDecoration(
        color: kPrimaryColor,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(12),
          topRight: Radius.circular(12),
        ),
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 11.h),
      child: Row(
        children: [
          SizedBox(
            width: 28.w,
            child: Text(
              'Sr.',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: Colors.white,
              ),
            ),
          ),
          SizedBox(width: 8.w),
          Expanded(
            flex: 3,
            child: Text(
              'Beneficiary Name',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: Colors.white,
              ),
            ),
          ),
          SizedBox(
            width: 70.w,
            child: Text(
              'Members',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: Colors.white,
              ),
            ),
          ),
        ],
      ),
    );
  }

  // ── Total row sits between header and data rows ────────────────────────────
  Widget _totalRow(CTAppointmentListController c) {
    return Container(
      decoration: BoxDecoration(
        color: kPrimaryColor.withValues(alpha: 0.08),
        border: Border.symmetric(
          vertical: BorderSide(color: Colors.grey.shade200),
          horizontal: BorderSide(color: Colors.grey.shade200),
        ),
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: Row(
        children: [
          SizedBox(width: 28.w + 8.w),
          Expanded(
            flex: 3,
            child: Text(
              'Total',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w700,
                color: kPrimaryColor,
              ),
            ),
          ),
          SizedBox(
            width: 70.w,
            child: Text(
              '${c.totalMemberCount}',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w700,
                color: kPrimaryColor,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildBody(CTAppointmentListController c) {
    if (c.isLoading) {
      return Column(
        children: [
          _buildSearchBar(c),
          const Expanded(
            child: CommonSkeletonScreeningDetailsTable(),
          ),
        ],
      );
    }
    if (c.filteredList.isEmpty) {
      return Column(
        children: [
          _buildSearchBar(c),
          Expanded(
            child: NoDataFound().paddingOnly(
              left: 14.w,
              right: 12.w,
              top: 8.h,
            ),
          ),
        ],
      );
    }
    return Column(
      children: [
        _buildSearchBar(c),
        Expanded(
          child: SingleChildScrollView(
            padding: EdgeInsets.fromLTRB(12.w, 10.h, 12.w, 20.h),
            child: Column(
              children: [
                // ── Drop shadow wrapper around entire table ──
                Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.08),
                        blurRadius: 8,
                        offset: const Offset(0, 3),
                      ),
                    ],
                  ),
                  child: Column(
                    children: [
                      _tableHeader(),
                      _totalRow(c),
                      ...List.generate(c.filteredList.length, (index) {
                        final item = c.filteredList[index];
                        final isLast = index == c.filteredList.length - 1;
                        return CTConfirmatoryRow(
                          item: item,
                          index: index,
                          isLast: isLast,
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) => CTAppointmentConfirmationScreen(
                                  beneficiaryName: item.beneficiaryName ?? '',
                                  regNo: item.regdNo ?? '',
                                  mobile: item.mobileNo ?? '',
                                  status: item.arId ?? '',
                                ),
                              ),
                            ).then((_) => _c.fetchList());
                          },
                        );
                      }),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
