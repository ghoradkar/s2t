// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../../Modules/Json_Class/AllDistrictListForPhyExamResponse/AllDistrictListForPhyExamResponse.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/utilities/DataProvider.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Views/DropDownListScreen/DropDownListScreen.dart';

class D2DPhysicalExaminationFilterView extends StatefulWidget {
  String selectedFromDate = "";
  String selectedToDate = "";
  AllDistrictListForPhyExamOutput? selectedDistrict;
  Function(String) onSelectedFromDate;
  Function(String) onSelectedToDate;
  Function(AllDistrictListForPhyExamOutput) onSelectedDistrict;
  Function() onApply;

  D2DPhysicalExaminationFilterView({
    super.key,
    required this.selectedFromDate,
    required this.selectedToDate,
    required this.selectedDistrict,
    required this.onSelectedFromDate,
    required this.onSelectedToDate,
    required this.onSelectedDistrict,
    required this.onApply,
  });

  @override
  State<D2DPhysicalExaminationFilterView> createState() =>
      _D2DPhysicalExaminationFilterViewState();
}

class _D2DPhysicalExaminationFilterViewState
    extends State<D2DPhysicalExaminationFilterView> {
  APIManager apiManager = APIManager();
  int empCode = 0;

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
  }

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

            // AppDateTextfield(
            //   icon: icCalendarMonth,
            //   titleHeaderString: "From Date",
            //   valueString: widget.selectedFromDate,
            //   onTap: () {
            //     _selectFromDate(context);
            //   },
            // ),
            AppTextField(
              onTap: () {
                _selectFromDate(context);

              },
              controller: TextEditingController(text: widget.selectedFromDate),
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
              prefixIcon: const Icon(Icons.calendar_month, color: kPrimaryColor).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 12.h),
            // AppDateTextfield(
            //   icon: icCalendarMonth,
            //   titleHeaderString: "To Date",
            //   valueString: widget.selectedToDate,
            //   onTap: () {
            //     _selectToDate(context);
            //   },
            // ),
            AppTextField(
              onTap: () {
                _selectToDate(context);

              },
              controller: TextEditingController(text: widget.selectedToDate),
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
              prefixIcon: const Icon(Icons.calendar_month, color: kPrimaryColor).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 12.h),
            // AppDropdownTextfield(
            //   icon: icMapPin,
            //   titleHeaderString: "District",
            //   valueString: widget.selectedDistrict?.district ?? "",
            //   onTap: () {
            //     ToastManager.showLoader();
            //     getAllDistrictListForPhyExam();
            //   },
            // ),
            AppTextField(
              onTap: () {
                ToastManager.showLoader();
                    getAllDistrictListForPhyExam();
              },
              controller: TextEditingController(text: widget.selectedDistrict?.district ?? ""),
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
              suffixIcon: Icon(Icons.keyboard_arrow_down),
              prefixIcon: const Icon(Icons.location_on_outlined, color: kPrimaryColor).paddingOnly(left: 8.w),
            ),
            SizedBox(height: 30.h),
            Row(
              children: [
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Clear",
                    isCancel: true,
                    onTap: () {
                      Navigator.pop(context);
                    },
                  ),
                ),
                SizedBox(width: 67.w),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Apply",
                    onTap: () {
                      Navigator.pop(context);
                      widget.onApply();
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
      setState(() {
        widget.selectedFromDate = FormatterManager.formatDateToString(picked);
        widget.onSelectedFromDate(widget.selectedFromDate);
      });
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        widget.selectedToDate = FormatterManager.formatDateToString(picked);
        widget.onSelectedToDate(widget.selectedToDate);
      });
    }
  }

  void getAllDistrictListForPhyExam() {
    Map<String, String> params = {
      "FromDate": widget.selectedFromDate,
      "ToDate": widget.selectedToDate,
      "DoctorID": "$empCode",
    };

    apiManager.getAllDistrictListForPhyExamAPI(
      params,
      apiAllDistrictListForPhyExamCallBack,
    );
  }

  void apiAllDistrictListForPhyExamCallBack(
    AllDistrictListForPhyExamResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      List<AllDistrictListForPhyExamOutput>? list = response?.output ?? [];
      list.insert(
        0,
        AllDistrictListForPhyExamOutput(dISTLGDCODE: 0, district: "All"),
      );
      _showDropDownBottomSheet(
        "District",
        list,
        DropDownTypeMenu.AllDistrictListForPhyExam,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
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
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.AllDistrictListForPhyExam) {
                widget.selectedDistrict = p0;
              }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }
}
