import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_controller.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_list_controller.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'beneficiary_card.dart' show BeneficiaryCard;
import '../custom_widgets/network_wrapper.dart';
import '../custom_widgets/no_data_widget.dart';
import '../custom_widgets/selection_bottom_sheet.dart';
import '../../../Modules/ToastManager/ToastManager.dart';

class ExpectedBeneficiaryList
    extends GetView<ExpectedBeneficiaryListController> {
  const ExpectedBeneficiaryList({super.key});

  // Service controller — for loading Rx states (beneficiaryStatus, etc.)
  ExpectedBeneficiaryController get _svc =>
      Get.find<ExpectedBeneficiaryController>();

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    // One-time image precaching (guard lives in controller — StatelessWidget safe)
    if (!controller.imagesPrecached) {
      controller.imagesPrecached = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        precacheImage(const AssetImage(icFilter), context);
        precacheImage(const AssetImage(icSearch), context);
        precacheImage(const AssetImage(icArrowDownGreen), context);
        precacheImage(const AssetImage(iconArrow), context);
        precacheImage(const AssetImage(iconBackArrow), context);
        precacheImage(const AssetImage(icHashIcon), context);
        precacheImage(const AssetImage(iconCalender), context);
        precacheImage(const AssetImage(iconPerson), context);
        precacheImage(const AssetImage(iconPersons), context);
        precacheImage(const AssetImage(iconMap), context);
        precacheImage(const AssetImage(iconDocument), context);
        precacheImage(const AssetImage(icSuccessRoundGreen), context);
      });
    }

    return NetworkWrapper(
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        appBar: mAppBar(
          scTitle: 'Expected Beneficiary List',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
          actions: [
            GestureDetector(
              onTap: () => _showFilterSheet(context),
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 4.w),
                child: Image.asset(
                  icFilter,
                  height: 30.h,
                  width: 30.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            SizedBox(width: 12.w),
          ],
          showActions: true,
        ),
        body: Obx(() {
          final isLoading =
              _svc.beneficiaryStatus.value.isInProgress ||
              _svc.dateTypeWiseDataStatus.value.isInProgress;
          return isLoading
              ? CommonSkeletonList(
                key: const ValueKey('skeleton'),
              ).paddingSymmetric(horizontal: 8.w)
              : Column(
                children: [
                  SizedBox(height: 10.h),
                  AppTextField(
                    controller: controller.searchTextController,
                    onChange: controller.onSearchChanged,
                    hint: 'Search Worker Name/Mobile No/Area',
                    hintStyle: TextStyle(
                      fontSize: 14.sp,
                      fontWeight: FontWeight.w400,
                      fontFamily: FontConstants.interFonts,
                    ),
                    inputStyle: TextStyle(
                      fontSize: 14.sp * 1.33,
                      fontWeight: FontWeight.w400,
                      fontFamily: FontConstants.interFonts,
                    ),
                    fieldRadius: 50,
                    suffixIcon: SizedBox(
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
                  ).paddingOnly(top: 6.h),
                  Obx(
                    () =>
                        controller.filteredList.isNotEmpty
                            ? Expanded(
                              child: Padding(
                                padding: EdgeInsets.only(left: 2.w, right: 2.w),
                                child: ListView.builder(
                                  shrinkWrap: true,
                                  itemCount: controller.filteredList.length,
                                  addAutomaticKeepAlives: false,
                                  addRepaintBoundaries: true,
                                  cacheExtent: 500,
                                  itemBuilder: (context, index) {
                                    return RepaintBoundary(
                                      child: BeneficiaryCard(
                                        key: ValueKey(
                                          controller
                                              .filteredList[index]
                                              .assignCallID,
                                        ),
                                        beneficiary:
                                            controller.filteredList[index],
                                        index: index,
                                        onAppointmentSaved:
                                            controller.refreshData,
                                        empCode: controller.cachedEmpCode,
                                        desId: controller.cachedDesId,
                                        mobileNo: controller.cachedMobileNo,
                                        myOperatorUserId:
                                            controller.cachedMyOperatorUserId,
                                        agentId: controller.cachedAgentId,
                                      ),
                                    );
                                  },
                                ),
                              ),
                            )
                            : Expanded(
                              child: const NoDataFound().paddingSymmetric(
                                horizontal: 16.w,
                                vertical: 10.h,
                              ),
                            ),
                  ),
                ],
              );
        }),
      ),
    );
  }

  // ── Filter bottom sheet ────────────────────────────────────────────────

  void _showFilterSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      isDismissible: false,
      enableDrag: false,
      builder: (c) {
        return Container(
          decoration: BoxDecoration(
            color: kWhiteColor,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(responsiveHeight(40)),
              topRight: Radius.circular(responsiveHeight(40)),
            ),
          ),
          width: SizeConfig.screenWidth,
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 20.h, horizontal: 20.w),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      'Filters',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 16.sp,
                      ),
                    ),
                    InkWell(
                      onTap: () => Navigator.pop(c),
                      child: const Icon(Icons.cancel_presentation),
                    ),
                  ],
                ),
                SizedBox(height: 26.h),
                AppTextField(
                  onTap: () => _svc.fetchCallStatus(),
                  controller: controller.callStatusTextController,
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'Call Status',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                      children: [
                        TextSpan(
                          text: ' *',
                          style: TextStyle(color: Colors.red, fontSize: 14.sp),
                        ),
                      ],
                    ),
                  ),
                  prefixIcon: const Icon(
                    Icons.call_outlined,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.0),
                  suffixIcon: Obx(
                    () =>
                        _svc.getCallStatus.value.isInProgress
                            ? SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: const Center(
                                child: CircularProgressIndicator(),
                              ),
                            )
                            : SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: Center(
                                child: Image.asset(
                                  icArrowDownGreen,
                                  height: 20.h,
                                  width: 20.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                  ),
                ),
                SizedBox(height: 26.h),
                AppTextField(
                  onTap: () => _svc.fetchTeamData(const {}),
                  controller: controller.teamNumberTextController,
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'Team Number',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                      children: [
                        TextSpan(
                          text: ' *',
                          style: TextStyle(color: Colors.red, fontSize: 14.sp),
                        ),
                      ],
                    ),
                  ),
                  prefixIcon: const Icon(
                    Icons.numbers,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.0),
                  suffixIcon: Obx(
                    () =>
                        _svc.teamStatus.value.isInProgress
                            ? SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: const Center(
                                child: CircularProgressIndicator(),
                              ),
                            )
                            : SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: Center(
                                child: Image.asset(
                                  icArrowDownGreen,
                                  height: 20.h,
                                  width: 20.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                  ),
                ),
                SizedBox(height: 26.h),
                AppTextField(
                  onTap: () => _showDateTypeSheet(c),
                  controller: controller.dateTypeTextController,
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'Date Type',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                      children: [
                        TextSpan(
                          text: ' *',
                          style: TextStyle(color: Colors.red, fontSize: 14.sp),
                        ),
                      ],
                    ),
                  ),
                  prefixIcon: const Icon(
                    Icons.calendar_today,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.0),
                  suffixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icArrowDownGreen,
                        height: 20.h,
                        width: 20.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 26.h),
                AppTextField(
                  onTap: () => _showDatePicker(c),
                  controller: controller.dateTextController,
                  readOnly: true,
                  label: RichText(
                    text: TextSpan(
                      text: 'Date',
                      style: TextStyle(
                        color: kLabelTextColor,
                        fontSize: 14.sp,
                        fontFamily: FontConstants.interFonts,
                      ),
                      children: [
                        TextSpan(
                          text: ' *',
                          style: TextStyle(color: Colors.red, fontSize: 14.sp),
                        ),
                      ],
                    ),
                  ),
                  prefixIcon: const Icon(
                    Icons.calendar_month,
                    color: kPrimaryColor,
                  ).paddingOnly(left: 6.0),
                  suffixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icArrowDownGreen,
                        height: 20.h,
                        width: 20.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
                SizedBox(height: 57.h),
                Row(
                  children: [
                    Expanded(
                      flex: 1,
                      child: AppButtonWithIcon(
                        onTap: controller.clearFilters,
                        title: 'Clear',
                        textStyle: TextStyle(
                          color: kPrimaryColor,
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        buttonColor: kButtonSecondaryColor,
                        icon: Image.asset(
                          iconArrow,
                          height: 24.h,
                          width: 24.w,
                          color: kPrimaryColor,
                        ),
                      ),
                    ),
                    SizedBox(width: 67.w),
                    Expanded(
                      flex: 1,
                      child: AppButtonWithIcon(
                        onTap: controller.applyFilters,
                        title: 'Apply',
                        textStyle: TextStyle(
                          color: Colors.white,
                          fontSize: 14.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        buttonColor: kPrimaryColor,
                        icon: Image.asset(
                          iconArrow,
                          height: 24.h,
                          width: 24.w,
                          color: kWhiteColor,
                        ),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 60.h),
              ],
            ),
          ),
        );
      },
    );
  }

  // ── Date type selection sheet ──────────────────────────────────────────

  void _showDateTypeSheet(BuildContext context) {
    Map<String, dynamic>? tempSelectedDateType = controller.selectDateType;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (ctx) {
        return StatefulBuilder(
          builder: (c, sheetState) {
            return SelectionBottomSheet<Map<String, dynamic>, int>(
              title: 'Select Date Type',
              items: ExpectedBeneficiaryListController.dateTypeList,
              selectedValue: tempSelectedDateType?['id'],
              valueFor: (item) => item['id'] ?? -1,
              labelFor: (item) => item['name'] ?? 'NA',
              height: 360.h,
              padding: EdgeInsets.only(
                top: 28.h,
                left: 35.w,
                right: 35.w,
                bottom: 60.h,
              ),
              titleTextStyle: TextStyle(
                fontSize: 16.sp,
                fontFamily: FontConstants.interFonts,
              ),
              titleBottomSpacing: 30.h,
              itemPadding: EdgeInsets.symmetric(vertical: 6.h),
              itemContainerPadding: EdgeInsets.symmetric(
                vertical: 8.0.h,
                horizontal: 4.0.w,
              ),
              selectedBackgroundColor: kPrimaryColor.withOpacity(0.1),
              itemTextStyle: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.normal,
                color: kBlackColor,
              ),
              selectedItemTextStyle: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: kPrimaryColor,
              ),
              onItemTap: (item) async {
                sheetState(() => tempSelectedDateType = item);
                await Future.delayed(const Duration(milliseconds: 200));
                controller.onDateTypeSelected(item);
                Navigator.pop(ctx);
                _svc.resetState();
              },
            );
          },
        );
      },
    );
  }

  // ── Date picker ────────────────────────────────────────────────────────

  void _showDatePicker(BuildContext context) {
    if (controller.selectDateType == null) {
      ToastManager.showAlertDialog(
        context,
        'Please select a date type first',
        () => Navigator.pop(context),
      );
      return;
    }

    controller.dateTypeId = controller.selectDateType!['id'];
    final DateTime initialDate = DateTime.now();
    DateTime firstDate = DateTime.now();
    DateTime lastDate = DateTime.now().add(const Duration(days: 10));

    if (controller.dateTypeId == 1) {
      firstDate = DateTime.now().subtract(const Duration(days: 7));
      lastDate = DateTime.now();
    } else if (controller.dateTypeId == 2) {
      firstDate = DateTime.now();
      lastDate = DateTime.now().add(const Duration(days: 10));
    } else if (controller.dateTypeId == 3) {
      firstDate = DateTime.now();
      lastDate = DateTime(2100, 12, 31);
    }

    showDatePicker(
      context: context,
      initialDate: initialDate,
      firstDate: firstDate,
      lastDate: lastDate,
      helpText: "Select ${controller.selectDateType!['name']}",
      initialEntryMode: DatePickerEntryMode.calendarOnly,
    ).then((value) {
      if (value != null) {
        controller.onDateSelected(value);
      }
    });
  }
}
