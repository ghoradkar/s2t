// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/d2d_physical_examination_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/AllDistrictListForPhyExamResponse.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../../../Modules/constants/fonts.dart';

class D2DPhysicalExaminationFilterView extends StatefulWidget {
  const D2DPhysicalExaminationFilterView({super.key});

  @override
  State<D2DPhysicalExaminationFilterView> createState() =>
      _D2DPhysicalExaminationFilterViewState();
}

class _D2DPhysicalExaminationFilterViewState
    extends State<D2DPhysicalExaminationFilterView> {
  D2DPhysicalExaminationController get ctrl =>
      Get.find<D2DPhysicalExaminationController>();

  @override
  Widget build(BuildContext context) {
    final double bottomInset = MediaQuery.of(context).padding.bottom;
    return Container(
      color: Colors.transparent,
      padding: EdgeInsets.fromLTRB(20.w, 8.h, 20.w, 8.h + bottomInset),
      child: SafeArea(
        top: false,
        child: Column(
          children: [
            SizedBox(height: 6.h),
            Text(
              "Filter",
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                color: kBlackColor,
                fontSize: responsiveFont(16),
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              onTap: () => _selectFromDate(context),
              controller: TextEditingController(text: ctrl.selectedFromDate),
              readOnly: true,
              label: RichText(
                text: TextSpan(
                  text: 'From Date',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: responsiveFont(14),
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              prefixIcon: const Icon(
                Icons.calendar_month,
                color: kPrimaryColor,
              ).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              onTap: () => _selectToDate(context),
              controller: TextEditingController(text: ctrl.selectedToDate),
              readOnly: true,
              label: RichText(
                text: TextSpan(
                  text: 'To Date',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: responsiveFont(14),
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              prefixIcon: const Icon(
                Icons.calendar_month,
                color: kPrimaryColor,
              ).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              onTap: () async {
                final districts = await ctrl.fetchDistricts();
                if (districts.isNotEmpty) {
                  _showDistrictBottomSheet(districts);
                }
              },
              controller: TextEditingController(
                text: ctrl.selectedDistrict?.district ?? "",
              ),
              readOnly: true,
              label: RichText(
                text: TextSpan(
                  text: 'District',
                  style: TextStyle(
                    color: kLabelTextColor,
                    fontSize: responsiveFont(14),
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ),
              suffixIcon: const Icon(Icons.keyboard_arrow_down),
              prefixIcon: const Icon(
                Icons.location_on_outlined,
                color: kPrimaryColor,
              ).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 30.h),
            Row(
              children: [
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Clear",
                    isCancel: true,
                    onTap: () => Navigator.pop(context),
                  ),
                ),
                SizedBox(width: 67.w),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Apply",
                    onTap: () {
                      Navigator.pop(context);
                      ctrl.applyFilter();
                    },
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      ctrl.onFromDateSelected(FormatterManager.formatDateToString(picked));
      setState(() {});
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      ctrl.onToDateSelected(FormatterManager.formatDateToString(picked));
      setState(() {});
    }
  }

  void _showDistrictBottomSheet(
    List<AllDistrictListForPhyExamOutput> districts,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: "District",
            dropDownList: districts,
            dropDownMenu: DropDownTypeMenu.AllDistrictListForPhyExam,
            onApplyTap: (p0) {
              ctrl.onDistrictSelected(p0 as AllDistrictListForPhyExamOutput);
              setState(() {});
            },
          ),
        );
      },
    );
  }
}