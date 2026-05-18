import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/controllers/ct_appointment_list_controller.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/screens/ct_appointment_confirmation_screen.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/widgets/ct_beneficiary_row.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';

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
          builder:
              (c) => Column(
                children: [
                  _buildSearchBar(c),
                  // _buildCountRow(c),
                  Expanded(child: _buildBody(c)),
                ],
              ),
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

  Widget _buildCountRow(CTAppointmentListController c) {
    if (c.fullList.isEmpty) return const SizedBox();
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 6.h),
      alignment: Alignment.centerRight,
      child: CommonText(
        text: 'Total Members: ${c.totalMemberCount}',
        fontSize: 12.sp,
        fontWeight: FontWeight.w600,
        textColor: kPrimaryColor,
        textAlign: TextAlign.end,
      ),
    );
  }

  Widget _buildBody(CTAppointmentListController c) {
    if (c.isLoading) {
      return const CommonSkeletonPatientList().paddingSymmetric(horizontal: 10.w);
    }
    if (c.filteredList.isEmpty) {
      return NoDataFound().paddingOnly(left: 14.w, right: 12.w, top: 8.h);
    }
    return ListView.builder(
      itemCount: c.filteredList.length,
      padding: EdgeInsets.only(top: 4.h, bottom: 16.h),
      itemBuilder: (context, index) {
        final item = c.filteredList[index];
        return CTConfirmatoryRow(
          item: item,
          index: index,
          onTap: () {
            Navigator.push(
              context,
              MaterialPageRoute(
                builder:
                    (_) => CTAppointmentConfirmationScreen(
                      beneficiaryName: item.beneficiaryName ?? '',
                      regNo: item.regdNo ?? '',
                      mobile: item.mobileNo ?? '',
                      status: item.arId ?? '',
                    ),
              ),
            ).then((_) => _c.fetchList());
          },
        );
      },
    );
  }
}
