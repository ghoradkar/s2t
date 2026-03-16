import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_return/controller/medicine_return_controller.dart';
import '../../../../Modules/constants/fonts.dart';

class MedicineReturnScreen extends StatefulWidget {
  const MedicineReturnScreen({super.key});

  @override
  State<MedicineReturnScreen> createState() => _MedicineReturnScreenState();
}

class _MedicineReturnScreenState extends State<MedicineReturnScreen> {
  late final MedicineReturnController controller;

  @override
  void initState() {
    super.initState();
    final alreadyRegistered = Get.isRegistered<MedicineReturnController>();
    controller = Get.put(MedicineReturnController());
    if (alreadyRegistered) {
      controller.resetState();
    }
  }

  @override
  void dispose() {
    Get.delete<MedicineReturnController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Packet Return To Pharmacy",
          showActions: true,
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Column(
            children: [
              Obx(
                () => Container(
                  width: double.infinity,
                  height: 50.h,
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    border: Border.all(color: kTextFieldBorder, width: 1),
                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(20),
                      bottomLeft: Radius.circular(20),
                      topRight: Radius.circular(20),
                      bottomRight: Radius.circular(20),
                    ),
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: GestureDetector(
                          onTap: () {
                            controller.setMode(MedicineReturnMode.acceptInLab);
                          },
                          child: Container(
                            decoration: BoxDecoration(
                              color:
                                  controller.mode.value ==
                                          MedicineReturnMode.acceptInLab
                                      ? kPrimaryColor
                                      : kWhiteColor,
                              borderRadius: const BorderRadius.only(
                                topLeft: Radius.circular(20),
                                bottomLeft: Radius.circular(20),
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Denied Packet",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color:
                                      controller.mode.value ==
                                              MedicineReturnMode.acceptInLab
                                          ? Colors.white
                                          : Colors.black,
                                  fontSize: responsiveFont(12),
                                  fontWeight: FontWeight.normal,
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                      Expanded(
                        child: GestureDetector(
                          onTap: () {
                            controller.setMode(
                              MedicineReturnMode.returnToPharmacy,
                            );
                          },
                          child: Container(
                            decoration: BoxDecoration(
                              color:
                                  controller.mode.value ==
                                          MedicineReturnMode.returnToPharmacy
                                      ? kPrimaryColor
                                      : kWhiteColor,
                              borderRadius: const BorderRadius.only(
                                topRight: Radius.circular(20),
                                bottomRight: Radius.circular(20),
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Return To Pharmacy",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color:
                                      controller.mode.value ==
                                              MedicineReturnMode.returnToPharmacy
                                          ? Colors.white
                                          : Colors.black,
                                  fontSize: responsiveFont(12),
                                  fontWeight: FontWeight.normal,
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ).paddingSymmetric(vertical: 6.h, horizontal: 8.w),
              ),

              Obx(
                () => Container(
                  padding: EdgeInsets.only(
                    top: 4.h,
                    bottom: 4.h,
                    left: 4.w,
                    right: 4.w,
                  ),
                  decoration: BoxDecoration(
                    color: kWhiteColor,
                    boxShadow: [
                      BoxShadow(
                        color: kTextFieldBorder.withValues(alpha: 0.5),
                        spreadRadius: 2,
                        blurRadius: 4,
                      ),
                    ],
                    borderRadius: BorderRadius.circular(10),
                  ),
                  // padding: EdgeInsets.all(8),
                  child: Column(
                    children: [
                      SizedBox(height: 8.h),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(
                                text: controller.fromDateString.value,
                              ),
                              readOnly: true,
                              onTap: () {
                                controller.pickFromDate(context);
                              },
                              hint: 'From Date*',
                              label: CommonText(
                                text: 'From Date*',
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
                          SizedBox(width: 8.w),
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(
                                text: controller.toDateString.value,
                              ),
                              readOnly: true,
                              onTap: () {
                                controller.pickToDate(context);
                              },
                              hint: 'To Date*',
                              label: CommonText(
                                text: 'To Date*',
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
                      SizedBox(height: 8.h),
                      AppTextField(
                        controller: TextEditingController(
                          text: controller.selectedTaluka.value?.tALNAME ?? "",
                        ),
                        readOnly: true,
                        onTap: () {
                          controller.showTalukaDropdown(context);
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
                        suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                      ),
                      SizedBox(height: 8.h),
                      AppTextField(
                        controller: TextEditingController(
                          text: controller.selectedResource.value?.userName ?? "",
                        ),
                        readOnly: true,
                        onTap: () {
                          controller.showResourceDropdown(context);
                        },
                        hint: 'Delivery Executive *',
                        label: CommonText(
                          text: 'Delivery Executive *',
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
                        suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                      ),
                      SizedBox(height: 8.h),
                      AppTextField(
                        controller: controller.searchController,
                        readOnly: false,
                        onTap: () {},
                        onChange: (value) {
                          controller.filterByBeneficiary(value);
                        },
                        hint: 'Search Beneficiary',
                        label: CommonText(
                          text: 'Search Beneficiary',
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
                    ],
                  ),
                ).paddingSymmetric(vertical: 6.h, horizontal: 12.w),
              ),
              SizedBox(height: 8.h),
              Expanded(
                child: Obx(
                  () => ListView.builder(
                    itemCount: controller.filteredList.length,
                    itemBuilder: (context, index) {
                      final item = controller.filteredList[index];
                      return Padding(
                        padding: EdgeInsets.fromLTRB(0, 0, 0, 10.h),
                        child: Container(
                          decoration: BoxDecoration(
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withValues(alpha: 0.2),
                                spreadRadius: 2,
                                blurRadius: 2,
                              ),
                            ],
                            color: const Color(0XFFFFFFFF),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Row(
                            children: [
                              Checkbox(
                                value: item.isSelected,
                                onChanged: (_) {
                                  controller.toggleItemSelection(index);
                                },
                              ),
                              Expanded(
                                child: Container(
                                  padding: EdgeInsets.only(
                                    bottom: 4.h,
                                    top: 4.h,
                                    right: 4.w,
                                  ),
                                  child: Column(
                                    children: [
                                      Container(
                                        padding: EdgeInsets.fromLTRB(
                                          0,
                                          6.h,
                                          0,
                                          6.h,
                                        ),
                                        decoration: BoxDecoration(
                                          color: kPrimaryColor,
                                          borderRadius: BorderRadius.only(
                                            topLeft: Radius.circular(5),
                                            topRight: Radius.circular(5),
                                          ),
                                        ),
                                        child: Row(
                                          children: [
                                            Expanded(
                                              child: Text(
                                                "Patient Name",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.white,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: responsiveFont(12),
                                                ),
                                              ),
                                            ),
                                            Expanded(
                                              child: Text(
                                                item.patientName ?? "",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.white,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w400,
                                                  fontSize: responsiveFont(12),
                                                ),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      SizedBox(height: 4.h),
                                      Row(
                                        children: [
                                          Expanded(
                                            child: Container(
                                              padding: EdgeInsets.fromLTRB(
                                                0,
                                                4.h,
                                                0,
                                                4.h,
                                              ),
                                              decoration: BoxDecoration(
                                                color: kWhiteColor,
                                                border: Border.all(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                              child: Center(
                                                child: Text(
                                                  "Packet No.",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: Colors.black,
                                                    fontFamily:
                                                        FontConstants.interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(12),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            child: Container(
                                              padding: EdgeInsets.fromLTRB(
                                                0,
                                                4.h,
                                                0,
                                                4.h,
                                              ),
                                              decoration: BoxDecoration(
                                                color: kWhiteColor,
                                                border: Border.all(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                              child: Center(
                                                child: Text(
                                                  item.packetNo ?? "",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: Colors.black,
                                                    fontFamily:
                                                        FontConstants.interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(12),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                      SizedBox(height: 4.h),
                                      Row(
                                        children: [
                                          Expanded(
                                            child: Container(
                                              padding: EdgeInsets.fromLTRB(
                                                0,
                                                4.h,
                                                0,
                                                4.h,
                                              ),
                                              decoration: BoxDecoration(
                                                color: kWhiteColor,
                                                border: Border.all(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                              child: Center(
                                                child: Text(
                                                  "Delivery Challan No.",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: Colors.black,
                                                    fontFamily:
                                                        FontConstants.interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(12),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            child: Container(
                                              padding: EdgeInsets.fromLTRB(
                                                0,
                                                4.h,
                                                0,
                                                4.h,
                                              ),
                                              decoration: BoxDecoration(
                                                color: kWhiteColor,
                                                border: Border.all(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                              child: Center(
                                                child: Text(
                                                  item.deliveryChallanID ?? "",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: Colors.black,
                                                    fontFamily:
                                                        FontConstants.interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(12),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ).paddingSymmetric(vertical: 6.h, horizontal: 14.w),
                ),
              ),
              Obx(
                () => SizedBox(
                  width: 200.w,
                  child: AppActiveButton(
                    buttontitle:
                        controller.mode.value == MedicineReturnMode.acceptInLab
                            ? "Accept In Lab"
                            : "Return To Pharmacy",
                    onTap: () {
                      controller.submitSelected(context);
                    },
                  ),
                ).paddingOnly(bottom: 10.h),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
