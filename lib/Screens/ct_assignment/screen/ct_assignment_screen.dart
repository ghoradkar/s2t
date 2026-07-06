// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import '../controller/ct_assignment_controller.dart';
import '../controller/ct_assignment_details_controller.dart';
import '../screen/ct_assignment_details_screen.dart';
import '../widget/ct_assignment_filter_widget.dart';
import '../widget/ct_assignment_row_widget.dart';

class CTAssignmentScreen extends StatelessWidget {
  const CTAssignmentScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(CTAssignmentController());
    SizeConfig().init(context);
    return GetBuilder<CTAssignmentController>(
      builder: (ctrl) => NetworkWrapper(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Assign Team For CT',
            showActions: true,
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
            actions: [
              Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                child: GestureDetector(
                  onTap: () => _showFilterSheet(context, ctrl),
                  child: SizedBox(
                    width: 20,
                    height: 20,
                    child: Image.asset(icFilter),
                  ),
                ),
              ),
            ],
          ),
          body: SizedBox(
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(8, 8, 8, 4),
                  child: AppTextField(
                    readOnly: false,
                    controller: ctrl.searchController,
                    onChange: ctrl.searchBeneficiaries,
                    hint: 'Search Beneficiary Name / Pincode',
                    label: CommonText(
                      text: 'Search Beneficiary Name / Pincode',
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
                          icSearch,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                Expanded(
                  child: ListView.builder(
                    itemCount: ctrl.searchList.length,
                    itemBuilder: (context, index) {
                      final obj = ctrl.searchList[index];
                      return CTAssignmentRowWidget(
                        obj: obj,
                        onSelectTap: () {
                          Get.delete<CTAssignmentDetailsController>(force: true);
                          Get.to(
                            () => CTAssignmentDetailsScreen(selectedCT: obj),
                          )?.then((_) {
                            ctrl.fetchBeneficiaryList();
                          });
                        },
                      );
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

  void _showFilterSheet(BuildContext context, CTAssignmentController ctrl) {
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
          height: MediaQuery.of(context).size.width * 1.58,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: CTAssignmentFilterWidget(
            onSelectedFromDte: (date) {
              if (date != null) {
                ctrl.fromDateString = FormatterManager.formatDateToString(date);
              }
            },
            onSelectedToDte: (date) {
              if (date != null) {
                ctrl.toDateString = FormatterManager.formatDateToString(date);
              }
            },
            onSelectedDistrict: (district) => ctrl.selectedDistrict = district,
            onSelectedTaluka: (taluka) => ctrl.selectedTaluka = taluka,
            onSelectedPinCode: (pin) => ctrl.pinCode = pin,
            onSelectedStatusRemark: (remark) => ctrl.selectedStatusRemark = remark,
            onSelectedDeptType: (dept) => ctrl.selectedDeptType = dept,
            applyDidPressed: () {
              ToastManager.showLoader();
              ctrl.fetchBeneficiaryList();
            },
          ),
        );
      },
    ).whenComplete(() => ctrl.update());
  }
}

