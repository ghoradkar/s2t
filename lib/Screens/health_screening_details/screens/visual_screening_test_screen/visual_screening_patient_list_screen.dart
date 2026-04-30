import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/visual_screening_patient_list_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'visual_screening_test_screen.dart';

class VisualScreeningPatientListScreen extends StatefulWidget {
  final int campID;

  const VisualScreeningPatientListScreen({super.key, required this.campID});

  @override
  State<VisualScreeningPatientListScreen> createState() =>
      _VisualScreeningPatientListScreenState();
}

class _VisualScreeningPatientListScreenState
    extends State<VisualScreeningPatientListScreen> {
  late final VisualScreeningPatientListController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.put(VisualScreeningPatientListController());
    controller.loadData(campId: widget.campID);
  }

  @override
  void dispose() {
    Get.delete<VisualScreeningPatientListController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient List',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (controller.isLoading.value) {
          return Column(
            children: [
              _searchBarDisabled().paddingSymmetric(
                horizontal: 12.w,
                vertical: 8.h,
              ),
              _tableHeader().paddingSymmetric(horizontal: 12.w),
              const Expanded(child: CommonSkeletonList()),
            ],
          );
        }
        if (controller.allList.isEmpty) {
          return Column(
            children: [
              _searchBarDisabled().paddingSymmetric(
                horizontal: 12.w,
                vertical: 8.h,
              ),
              Expanded(
                child: NoDataFound().paddingSymmetric(horizontal: 12.w),
              ),
            ],
          );
        }
        return Column(
          children: [
            _searchBar(),
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
                    return _VisualPatientRow(
                      index: index,
                      item: patient,
                      onTap: () => _onRowTapped(patient),
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

  void _onRowTapped(UserAttendancesUsingSitedetailsIDOutput patient) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => VisualScreeningTestScreen(
          campId: widget.campID,
          patient: patient,
        ),
      ),
    ).then((_) => controller.fetchPatients());
  }

  Widget _searchBarDisabled() {
    return TextField(
      enabled: false,
      decoration: InputDecoration(
        hintText: 'Search by Name / Reg. No.',
        hintStyle: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          color: kLabelTextColor,
        ),
        prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r),
        filled: true,
        fillColor: kBackground,
        contentPadding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8.r),
          borderSide: BorderSide(color: kTextFieldBorder),
        ),
        disabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8.r),
          borderSide: BorderSide(color: kTextFieldBorder),
        ),
      ),
    );
  }

  Widget _searchBar() {
    return Container(
      color: kWhiteColor,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: ValueListenableBuilder<TextEditingValue>(
        valueListenable: controller.searchController,
        builder: (context, value, _) => TextField(
          controller: controller.searchController,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 14.sp,
            color: kTextColor,
          ),
          decoration: InputDecoration(
            hintText: 'Search by Name / Reg. No.',
            hintStyle: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              color: kLabelTextColor,
            ),
            prefixIcon: Icon(
              Icons.search,
              color: kLabelTextColor,
              size: 20.r,
            ),
            suffixIcon: value.text.isNotEmpty
                ? GestureDetector(
                    onTap: controller.searchController.clear,
                    child: Icon(
                      Icons.close,
                      color: kLabelTextColor,
                      size: 18.r,
                    ),
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

  Widget _headerCell(
    String text, {
    required int flex,
    TextAlign align = TextAlign.center,
  }) {
    return Expanded(
      flex: flex,
      child: CommonText(
        text: text,
        fontSize: 14.sp,
        fontWeight: FontWeight.w600,
        textColor: kWhiteColor,
        textAlign: align,
      ),
    );
  }
}

class _VisualPatientRow extends StatelessWidget {
  final int index;
  final UserAttendancesUsingSitedetailsIDOutput item;
  final VoidCallback onTap;

  const _VisualPatientRow({
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

  Widget _cell(
    String text, {
    required int flex,
    TextAlign align = TextAlign.center,
  }) {
    return Expanded(
      flex: flex,
      child: Align(
        alignment:
            align == TextAlign.left ? Alignment.centerLeft : Alignment.center,
        child: CommonText(
          text: text,
          fontSize: 14.sp,
          fontWeight: FontWeight.w400,
          textColor: kTextColor,
          textAlign: align,
          maxLine: 2,
          overflow: TextOverflow.ellipsis,
        ),
      ),
    );
  }
}
