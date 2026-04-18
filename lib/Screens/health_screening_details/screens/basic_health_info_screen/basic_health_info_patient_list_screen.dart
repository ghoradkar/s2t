import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/basic_health_info_patient_list_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/basic_health_info_screen/basic_health_info_form_screen.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

class BasicHealthInfoPatientListScreen extends StatelessWidget {
  final int campID;
  final int siteDetailId;

  const BasicHealthInfoPatientListScreen({
    super.key,
    required this.campID,
    this.siteDetailId = 0,
  });

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(BasicHealthInfoPatientListController());
    controller.loadData(campId: campID, siteDetailId: siteDetailId);

    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient List',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Get.delete<BasicHealthInfoPatientListController>();
          Navigator.pop(context);
        },
      ),
      body: Obx(() {
        if (controller.isLoading.value) {
          return Column(
            children: [
              _searchBarDisabled(),
              _tableHeader().paddingSymmetric(horizontal: 12.w),
              const Expanded(child: CommonSkeletonList()),
            ],
          );
        }
        if (controller.allList.isEmpty) {
          return Column(
            children: [
              _searchBarDisabled(),

              Expanded(child: NoDataFound().paddingSymmetric(horizontal: 12.w)),
            ],
          );
        }
        return Column(
          children: [
            _searchBar(controller),
            _tableHeader().paddingOnly(left: 12.w, right: 12.w),
            Expanded(
              child: Obx(() {
                if (controller.filteredList.isEmpty) {
                  return NoDataFound().paddingSymmetric(
                    vertical: 6.h,
                    horizontal: 12.w,
                  );
                }
                return ListView.builder(
                  padding: EdgeInsets.symmetric(horizontal: 12.w),
                  itemCount: controller.filteredList.length,
                  itemBuilder: (context, index) {
                    final patient = controller.filteredList[index];
                    return _PatientRow(
                      index: index,
                      item: patient,
                      onTap: () => _onRowTapped(context, patient, controller),
                    );
                  },
                );
              }),
            ),
          ],
        );
      }),
    );
  }

  void _onRowTapped(
    BuildContext context,
    UserAttendancesUsingSitedetailsIDOutput item,
    BasicHealthInfoPatientListController controller,
  ) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => BasicHealthInfoFormScreen(
          regdId: item.regdId ?? 0,
          campTypeID: item.campTypeID ?? 0,
          patientItem: item,
        ),
      ),
    ).then((_) => controller.fetchPatients());
  }

  Widget _searchBarDisabled() {
    return Container(
      color: kWhiteColor,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: TextField(
        enabled: false,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 13.sp,
          color: kTextColor,
        ),
        decoration: InputDecoration(
          hintText: 'Search by Name / Reg. No.',
          hintStyle: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            color: kLabelTextColor,
          ),
          prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r),
          filled: true,
          fillColor: kBackground,
          contentPadding: EdgeInsets.symmetric(
            horizontal: 12.w,
            vertical: 10.h,
          ),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          disabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
        ),
      ),
    );
  }

  Widget _searchBar(BasicHealthInfoPatientListController controller) {
    return Container(
      color: kWhiteColor,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: ValueListenableBuilder<TextEditingValue>(
        valueListenable: controller.searchController,
        builder: (context, value, _) => TextField(
          controller: controller.searchController,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            color: kTextColor,
          ),
          decoration: InputDecoration(
            hintText: 'Search by Name / Reg. No.',
            hintStyle: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13.sp,
              color: kLabelTextColor,
            ),
            prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r),
            suffixIcon: value.text.isNotEmpty
                ? GestureDetector(
                    onTap: controller.searchController.clear,
                    child: Icon(Icons.close, color: kLabelTextColor, size: 18.r),
                  )
                : null,
            filled: true,
            fillColor: kBackground,
            contentPadding: EdgeInsets.symmetric(
              horizontal: 12.w,
              vertical: 10.h,
            ),
            border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8.r),
              borderSide: BorderSide(color: kTextFieldBorder),
            ),
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8.r),
              borderSide: BorderSide(color: kTextFieldBorder),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8.r),
              borderSide: BorderSide(color: kPrimaryColor, width: 1.5),
            ),
          ),
        ),
      ),
    );
  }

  Widget _tableHeader() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
      child: Row(
        children: [
          _headerCell('SN', flex: 1),
          _headerCell('Patient Name', flex: 3, align: TextAlign.left),
          _headerCell('Type', flex: 1),
          _headerCell('Reg. No.', flex: 2),
          _headerCell('Call', flex: 1),
        ],
      ),
    );
  }

  Widget _headerCell(String text, {required int flex, TextAlign align = TextAlign.center}) {
    return Expanded(
      flex: flex,
      child: Text(
        text,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          fontWeight: FontWeight.w600,
          color: kWhiteColor,
        ),
        textAlign: align,
      ),
    );
  }
}

class _PatientRow extends StatelessWidget {
  final int index;
  final UserAttendancesUsingSitedetailsIDOutput item;
  final VoidCallback onTap;

  const _PatientRow({
    required this.index,
    required this.item,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final isEven = index % 2 == 0;
    final typeLabel = (item.isDependent == 1) ? 'D' : 'W';

    return GestureDetector(
      onTap: onTap,
      child: Container(
        color: isEven ? kWhiteColor : kBackground,
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            _cell('${index + 1}', flex: 1),
            _cell(item.englishName ?? '', flex: 3, align: TextAlign.left),
            _cell(typeLabel, flex: 1),
            _cell(item.regdNo?.toString() ?? '', flex: 2),
            Expanded(
              flex: 1,
              child: Center(
                child: GestureDetector(
                  onTap: () async {
                    final uri = Uri(scheme: 'tel', path: item.mobileNo ?? '');
                    if (await canLaunchUrl(uri)) await launchUrl(uri);
                  },
                  child: Icon(Icons.phone, color: kPrimaryColor, size: 18.r),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _cell(String text, {required int flex, TextAlign align = TextAlign.center}) {
    return Expanded(
      flex: flex,
      child: Align(
        alignment: align == TextAlign.left ? Alignment.centerLeft : Alignment.center,
        child: Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 14.sp,
            color: kTextColor,
          ),
          textAlign: align,
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
        ),
      ),
    );
  }
}