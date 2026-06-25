// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ConsumablesListResponse/ConsumablesListResponse.dart';
import '../models/DevicesListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceListResponse/ResourceListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import '../controllers/device_and_resource_mapping_controller.dart';
import '../widgets/sub_device_drop_down_screen.dart';

class DeviceAllocationScreen extends StatelessWidget {
  const DeviceAllocationScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<DeviceAndResourceMappingController>(
      init: DeviceAndResourceMappingController(),
      dispose: (_) => Get.delete<DeviceAndResourceMappingController>(),
      builder: (ctrl) {
        return Scaffold(
          backgroundColor: Colors.white,
          appBar: mAppBar(
            scTitle: 'Device Allocation',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              ctrl.deviceMappingManager.resetDeviceMaaping();
              Get.back();
            },
          ),
          body: KeyboardDismissOnTap(
            dismissOnCapturedTaps: true,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: const BorderRadius.all(Radius.circular(10)),
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.15),
                        blurRadius: 4,
                      ),
                    ],
                  ),
                  padding: const EdgeInsets.all(10),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Expanded(
                            child: AppTextField(
                              readOnly: true,
                              controller: TextEditingController(
                                text: ctrl.selectedCampDate,
                              ),
                              onTap: () async {
                                final DateTime? picked =
                                    await showDatePicker(
                                  context: context,
                                  initialDate: DateTime.now(),
                                  firstDate: DateTime(1901),
                                  lastDate: DateTime(2101),
                                );
                                if (picked != null) {
                                  ctrl.setSelectedCampDate(
                                    FormatterManager.formatDateToString(picked),
                                  );
                                }
                              },
                              hint: 'Camp Date*',
                              label: CommonText(
                                text: 'Camp Date*',
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
                            ).paddingOnly(top: 12.h),
                          ),
                          const SizedBox(width: 12),
                          Expanded(
                            child: AppTextField(
                              readOnly: true,
                              controller: TextEditingController(
                                text: ctrl.selectedCampType
                                        ?.campTypeDescription ??
                                    '',
                              ),
                              onTap: () {
                                List<CampTypeOutput> campTypeList = [];
                                if (DataProvider().getRegularCamp()) {
                                  campTypeList.add(
                                    CampTypeOutput(
                                      cAMPTYPE: 1,
                                      campTypeDescription: 'REGULAR',
                                    ),
                                  );
                                } else {
                                  campTypeList.add(
                                    CampTypeOutput(
                                      cAMPTYPE: 1,
                                      campTypeDescription: 'REGULAR',
                                    ),
                                  );
                                  campTypeList.add(
                                    CampTypeOutput(
                                      cAMPTYPE: 2,
                                      campTypeDescription: 'CSC REGULAR CAMP',
                                    ),
                                  );
                                }
                                _showDropDownBottomSheet(
                                  context,
                                  'Camp Type',
                                  campTypeList,
                                  DropDownTypeMenu.CampType,
                                  ctrl,
                                );
                              },
                              hint: 'Camp Type*',
                              label: CommonText(
                                text: 'Camp Type*',
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
                                    icnTent,
                                    height: 24.h,
                                    width: 24.w,
                                    fit: BoxFit.contain,
                                  ),
                                ),
                              ),
                              suffixIcon:
                                  const Icon(Icons.keyboard_arrow_down),
                            ).paddingOnly(top: 12.h),
                          ),
                        ],
                      ),
                      AppTextField(
                        readOnly: true,
                        controller: TextEditingController(
                          text: ctrl.deviceMappingManager.campID,
                        ),
                        onTap: () async {
                          if (ctrl.selectedCampDate.isEmpty) {
                            ctrl.toastManager.showAlertMessage(
                              context,
                              'Select Camp Date',
                              const Color(0xFFEA0000),
                            );
                            return;
                          }
                          if (ctrl.selectedCampType == null) {
                            ctrl.toastManager.showAlertMessage(
                              context,
                              'Select Camp Type',
                              const Color(0xFFEA0000),
                            );
                            return;
                          }
                          final list = await ctrl.fetchCampList();
                          if (list.isNotEmpty && context.mounted) {
                            _showDropDownBottomSheet(
                              context,
                              'Camp ID',
                              list,
                              DropDownTypeMenu.CampID,
                              ctrl,
                            );
                          }
                        },
                        hint: 'Camp ID*',
                        label: CommonText(
                          text: 'Camp ID*',
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
                              icHashIcon,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                        suffixIcon:
                            const Icon(Icons.keyboard_arrow_down),
                      ).paddingOnly(top: 12.h),
                      Container(
                        color: Colors.transparent,
                        padding: const EdgeInsets.all(8),
                        child: Text(
                          textAlign: TextAlign.left,
                          'Note: Quantity has been calculated based on the beneficiary count',
                          style: TextStyle(
                            color: const Color(0xffEA1F1F),
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 12),
                _buildSegmentControl(ctrl),
                const SizedBox(height: 12),
                _buildSegmentLayout(context, ctrl),
                _buildBottomActions(context, ctrl),
              ],
            ).paddingSymmetric(vertical: 10, horizontal: 10),
          ),
        );
      },
    );
  }

  Widget _buildSegmentControl(DeviceAndResourceMappingController ctrl) {
    return SizedBox(
      width: double.infinity,
      height: 50,
      child: Row(
        children: [
          _buildSegmentTab(
            label: 'Device Mapping',
            index: 0,
            currentIndex: ctrl.selectedSegmentIndex,
            borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(20),
              bottomLeft: Radius.circular(20),
            ),
          ),
          _buildSegmentTab(
            label: 'Consumables',
            index: 1,
            currentIndex: ctrl.selectedSegmentIndex,
            borderRadius: BorderRadius.zero,
          ),
          _buildSegmentTab(
            label: 'Resource\nAllocation',
            index: 2,
            currentIndex: ctrl.selectedSegmentIndex,
            borderRadius: const BorderRadius.only(
              topRight: Radius.circular(20),
              bottomRight: Radius.circular(20),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSegmentTab({
    required String label,
    required int index,
    required int currentIndex,
    required BorderRadius borderRadius,
  }) {
    final bool isSelected = currentIndex == index;
    return Expanded(
      child: Container(
        decoration: BoxDecoration(
          border: isSelected
              ? null
              : Border.all(color: const Color(0xffD1D1D1), width: 1),
          color: isSelected ? kPrimaryColor : Colors.white,
          borderRadius: borderRadius,
        ),
        child: Center(
          child: Text(
            label,
            textAlign: TextAlign.center,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w500,
              color: isSelected ? Colors.white : dropDownTitleHeader,
              fontSize: responsiveFont(12),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildSegmentLayout(
    BuildContext context,
    DeviceAndResourceMappingController ctrl,
  ) {
    if (ctrl.selectedSegmentIndex == 0) {
      return Expanded(
        child: ListView.builder(
          itemCount: ctrl.deviceMappingManager.deviceList.length,
          itemBuilder: (context, index) {
            final DevicesOutput devicesOutputObj =
                ctrl.deviceMappingManager.deviceList[index];
            return IntrinsicHeight(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(8, 6, 8, 6),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    boxShadow: [
                      BoxShadow(
                        blurRadius: 10,
                        spreadRadius: 0,
                        offset: const Offset(0, 1),
                        color:
                            const Color(0xFF000000).withValues(alpha: 0.15),
                      ),
                    ],
                    borderRadius: BorderRadius.circular(responsiveHeight(10)),
                  ),
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icDevicesIcon),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: 'Device Name : ',
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text: devicesOutputObj.deviceName,
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 4),
                      Row(
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icnTent),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: 'Quantity : ',
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text: devicesOutputObj.requiredDevice
                                            ?.toString() ??
                                        '',
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      AppTextField(
                        readOnly: true,
                        controller: TextEditingController(
                          text: ctrl.subDeviceSelected(devicesOutputObj),
                        ),
                        onTap: () async {
                          final list =
                              await ctrl.fetchSubDevices(devicesOutputObj);
                          if (list.isNotEmpty && context.mounted) {
                            _showSubDeviceBottomSheet(
                              context,
                              'Select Devices',
                              list,
                              ctrl,
                            );
                          }
                        },
                        hint: 'Select Devices',
                        label: CommonText(
                          text: 'Select Devices',
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
                              icDevicesIcon,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ).paddingOnly(top: 12.h),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      );
    } else if (ctrl.selectedSegmentIndex == 1) {
      return Expanded(
        child: ListView.builder(
          itemCount: ctrl.deviceMappingManager.consumablesList.length,
          itemBuilder: (context, index) {
            final ConsumablesOutput consumablesOutput =
                ctrl.deviceMappingManager.consumablesList[index];
            return IntrinsicHeight(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    boxShadow: [
                      BoxShadow(
                        blurRadius: responsiveHeight(10),
                        spreadRadius: 0,
                        offset: const Offset(0, 1),
                        color:
                            const Color(0xFF000000).withValues(alpha: 0.15),
                      ),
                    ],
                    borderRadius:
                        BorderRadius.circular(responsiveHeight(10)),
                  ),
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      _buildConsumableRow(
                        icConsumableIcon,
                        'Consumable : ',
                        consumablesOutput.productName ?? '',
                      ),
                      const SizedBox(height: 4),
                      _buildConsumableRow(
                        iconPerson,
                        'Per Person Inventory : ',
                        consumablesOutput.productQuantity?.toString() ?? '0',
                      ),
                      const SizedBox(height: 4),
                      _buildConsumableRow(
                        icStockIcon,
                        'Stock Available : ',
                        consumablesOutput.aVAILABELSTOCK?.toString() ?? '0',
                      ),
                      const SizedBox(height: 4),
                      _buildConsumableRow(
                        icPackageImport,
                        'Expected Quantity : ',
                        consumablesOutput.expectedQuantity?.toString() ?? '0',
                      ),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      );
    }

    return Expanded(
      child: ListView.builder(
        itemCount: ctrl.deviceMappingManager.resourceList.length,
        itemBuilder: (context, index) {
          final ResourceOutput devicesOutputObj =
              ctrl.deviceMappingManager.resourceList[index];
          return IntrinsicHeight(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
              child: Container(
                width: MediaQuery.of(context).size.width,
                decoration: BoxDecoration(
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      blurRadius: responsiveHeight(10),
                      spreadRadius: 0,
                      offset: const Offset(0, 1),
                      color:
                          const Color(0xFF000000).withValues(alpha: 0.15),
                    ),
                  ],
                  borderRadius: BorderRadius.circular(responsiveHeight(10)),
                ),
                padding: const EdgeInsets.all(16),
                child: Column(
                  children: [
                    Row(
                      children: [
                        SizedBox(
                          width: 24,
                          height: 24,
                          child: Image.asset(icUserIcon),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: RichText(
                            text: TextSpan(
                              text: 'Role : ',
                              style: TextStyle(
                                color: Colors.black,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w500,
                                fontSize: responsiveFont(14),
                              ),
                              children: [
                                TextSpan(
                                  text: devicesOutputObj.testName ?? '',
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ],
                            ),
                            softWrap: true,
                          ),
                        ),
                      ],
                    ),
                    AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text: ctrl.subResourcesSelected(devicesOutputObj),
                      ),
                      onTap: () async {
                        final list =
                            await ctrl.fetchSubResources(devicesOutputObj);
                        if (list.isNotEmpty && context.mounted) {
                          _showSubResourceBottomSheet(
                            context,
                            'Resource',
                            list,
                            DropDownMultipleTypeMenu.SubResource,
                            ctrl,
                            devicesOutputObj,
                          );
                        }
                      },
                      hint: 'Select Resources',
                      label: CommonText(
                        text: 'Select Resources',
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
                            icUserIcon,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                    ).paddingOnly(top: 12.h),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildBottomActions(
    BuildContext context,
    DeviceAndResourceMappingController ctrl,
  ) {
    return Container(
      width: MediaQuery.of(context).size.width,
      color: Colors.white,
      padding: EdgeInsets.only(
        left: 8,
        right: 8,
        top: 8,
        bottom: 8 + MediaQuery.viewPaddingOf(context).bottom,
      ),
      child: Row(
        children: [
          ctrl.selectedSegmentIndex == 2
              ? const SizedBox.shrink()
              : Expanded(
                  child: AppActiveButton(
                    buttontitle: 'Skip',
                    isCancel: true,
                    onTap: () {
                      ctrl.skipDeviceMapping();
                    },
                  ),
                ),
          ctrl.selectedSegmentIndex == 2
              ? const SizedBox.shrink()
              : const SizedBox(width: 67),
          Expanded(
            child: AppActiveButton(
              buttontitle:
                  ctrl.selectedSegmentIndex == 2 ? 'Submit' : 'NEXT',
              onTap: () {
                if (ctrl.selectedSegmentIndex == 2) {
                  ctrl.submitData();
                } else {
                  ctrl.nextStepScreen();
                }
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildConsumableRow(String icon, String label, String value) {
    return Row(
      children: [
        SizedBox(width: 24, height: 24, child: Image.asset(icon)),
        const SizedBox(width: 8),
        Expanded(
          child: RichText(
            text: TextSpan(
              text: label,
              style: TextStyle(
                color: Colors.black,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w500,
                fontSize: responsiveFont(14),
              ),
              children: [
                TextSpan(
                  text: value,
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ],
            ),
            softWrap: true,
          ),
        ),
      ],
    );
  }

  void _showDropDownBottomSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    DeviceAndResourceMappingController ctrl,
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
              if (dropDownType == DropDownTypeMenu.CampType) {
                ctrl.onCampTypeSelected(p0);
              } else if (dropDownType == DropDownTypeMenu.CampID) {
                ctrl.onCampIdSelected(p0);
              }
            },
          ),
        );
      },
    ).whenComplete(() {
      ctrl.update();
    });
  }

  void _showSubDeviceBottomSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DeviceAndResourceMappingController ctrl,
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
          child: SubDeviceDropDownScreen(
            titleString: title,
            dropDownList: list,
            onApplyTap: (p0) {
              print(p0);
              ctrl.onSubDevicesSelected(p0);
            },
          ),
        );
      },
    ).whenComplete(() {
      ctrl.update();
    });
  }

  void _showSubResourceBottomSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownMultipleTypeMenu dropDownType,
    DeviceAndResourceMappingController ctrl,
    ResourceOutput resource,
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
          child: MultiSelectionDropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            preSelectedList: resource.subResourceList.isNotEmpty
                ? resource.subResourceList
                : null,
            onApplyTap: (p0) {
              ctrl.onSubResourcesSelected(p0);
            },
          ),
        );
      },
    ).whenComplete(() {
      ctrl.update();
    });
  }
}
