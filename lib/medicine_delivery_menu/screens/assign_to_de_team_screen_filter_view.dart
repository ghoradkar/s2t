// ignore_for_file: prefer_conditional_assignment, must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/medicine_delivery_menu/controller/app_data_manager.dart';
import 'package:s2toperational/utilities/enums.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/medicine_delivery_menu/controller/packet_allocation_controller.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/drop_down_list_screen/drop_down_list_screen.dart';
import '../../../../constants/fonts.dart';

class AssignToDETeamScreenFilterView extends StatefulWidget {
  AssignToDETeamScreenFilterView({
    super.key,
    required this.onTapApply,
  });

  Function() onTapApply;

  @override
  State<AssignToDETeamScreenFilterView> createState() =>
      _AssignToDETeamScreenFilterViewState();
}

class _AssignToDETeamScreenFilterViewState
    extends State<AssignToDETeamScreenFilterView> {
  late final PacketAllocationController controller;
  int dISTLGDCODE = 0;
  int empCode = 0;

  Future<void> selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      AppDataManager.fromDate = FormatterManager.formatDateToString(picked);
      AppDataManager.toDate = "";
      setState(() {});
    }
  }

  Future<void> selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      AppDataManager.toDate = FormatterManager.formatDateToString(picked);
      setState(() {});
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
              if (dropDownType == DropDownTypeMenu.UserMappedTaluka) {
                AppDataManager.selectedTaluka = p0;
              } else if (dropDownType ==
                  DropDownTypeMenu.ReportDeliveryExecutive) {
                AppDataManager.selectedResource = p0;
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

  @override
  void initState() {
    super.initState();
    controller = Get.find<PacketAllocationController>();
    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    if (AppDataManager.selectedTaluka == null) {
      ToastManager.showLoader();
      controller.fetchTaluka(
        userId: empCode,
        distLgdCode: dISTLGDCODE,
        showDropdown: false,
        onShowDropdown: null,
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(10, 20, 10, 10),
      child: Column(
        children: [
          Text(
            "Filters",
            style: TextStyle(
              color: kBlackColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(16),
            ),
          ),
          const SizedBox(height: 16),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(
                    text: AppDataManager.fromDate,
                  ),
                  readOnly: true,
                  onTap: () {
                    selectFromDate(context);
                  },
                  hint: 'From Date *',
                  label: CommonText(
                    text: 'From Date *',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.normal,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  hintStyle: TextStyle(
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  prefixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icCalendarMonth,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(
                    text: AppDataManager.toDate,
                  ),
                  readOnly: true,
                  onTap: () {
                    selectToDate(context);
                  },
                  hint: 'To Date *',
                  label: CommonText(
                    text: 'To Date *',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.normal,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  hintStyle: TextStyle(
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  prefixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icCalendarMonth,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          AppTextField(
            controller: TextEditingController(
              text: AppDataManager.selectedTaluka?.tALNAME ?? "",
            ),
            readOnly: true,
            onTap: () {
              ToastManager.showLoader();
              controller.fetchTaluka(
                userId: empCode,
                distLgdCode: dISTLGDCODE,
                showDropdown: true,
                onShowDropdown: (list) {
                  _showDropDownBottomSheet(
                    "Taluka",
                    list,
                    DropDownTypeMenu.UserMappedTaluka,
                  );
                },
              );
            },
            hint: 'Taluka *',
            label: CommonText(
              text: 'Taluka *',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            prefixIcon: SizedBox(
              height: 20.h,
              width: 20.w,
              child: Center(
                child: Image.asset(
                  icMapPin,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: const Icon(Icons.keyboard_arrow_down_outlined),
          ),
          const SizedBox(height: 8),
          AppTextField(
            controller: TextEditingController(
              text: AppDataManager.selectedResource?.userName ?? "",
            ),
            readOnly: true,
            onTap: () {
              ToastManager.showLoader();
              controller.fetchDeliveryExecutives(
                onSuccess: (list) {
                  _showDropDownBottomSheet(
                    "Select Resource",
                    list,
                    DropDownTypeMenu.ReportDeliveryExecutive,
                  );
                },
              );
            },
            hint: 'Delivery Executive / Team *',
            label: CommonText(
              text: 'Delivery Executive / Team *',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            prefixIcon: SizedBox(
              height: 20.h,
              width: 20.w,
              child: Center(
                child: Image.asset(
                  icUsersGroup,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: const Icon(Icons.keyboard_arrow_down_outlined),
          ),

          const Spacer(),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 4, 16, 20),
            child: Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.transparent,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
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
                  const SizedBox(width: 16),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Apply",
                      onTap: () {
                        if (AppDataManager.selectedResource == null) {
                          ToastManager.toast(
                            "Select Delivery Executive / Team",
                          );
                          return;
                        }
                        Navigator.pop(context);
                        widget.onTapApply();
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}