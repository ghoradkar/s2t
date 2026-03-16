import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppBarCodeTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/pick_up_medicine_packet/controller/PickUpMedicinePacketController.dart';
import '../../../../../../Modules/constants/fonts.dart';

class PickUpMedicinePacketScreen extends StatefulWidget {
  const PickUpMedicinePacketScreen({super.key});

  @override
  State<PickUpMedicinePacketScreen> createState() =>
      _PickUpMedicinePacketScreenState();
}

class _PickUpMedicinePacketScreenState
    extends State<PickUpMedicinePacketScreen> {
  late final PickUpMedicinePacketController controller;

  @override
  void initState() {
    final alreadyRegistered =
        Get.isRegistered<PickUpMedicinePacketController>();
    controller = Get.put(PickUpMedicinePacketController());
    if (alreadyRegistered) {
      controller.resetState();
    }
    super.initState();
  }

  @override
  void dispose() {
    Get.delete<PickUpMedicinePacketController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Pick-Up Medicine Packet",
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
              // ── Filter section ──────────────────────────────────────────
              Obx(
                () => Container(
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
                  padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
                  child: Column(
                    children: [
                      Align(
                        alignment: Alignment.centerLeft,
                        child: CommonText(
                          text:
                              "Note: Dates for selection are w.r.t. packet creation",
                          fontSize: 14.sp,
                          fontWeight: FontWeight.normal,
                          textColor: noteRedColor,
                          textAlign: TextAlign.start,
                        ).paddingOnly(left: 4.w, bottom: 8.h),
                      ),
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
                          text: controller.selectedDropdownName.value,
                        ),
                        readOnly: true,
                        onTap: () {
                          controller.showDropdown(context);
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
                              icLandingLab,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                        suffixIcon: Icon(Icons.keyboard_arrow_down),
                      ),
                      SizedBox(height: 8.h),
                      AppBarCodeTextfield(
                        titleHeaderString: "Scan Delivery Challan",
                        controller: controller.barcodeController,
                        onSearch: (p0) {
                          controller.submitBarcodePacket(context);
                        },
                        onBarcodeScanned: () {
                          controller.openBarCodeScanner(context);
                        },
                      ),
                    ],
                  ),
                ).paddingOnly(top: 12.h, left: 10.w, right: 10.w, bottom: 8.h),
              ),

              SizedBox(height: 4.h),

              // ── Select All + Pick-Up button row ─────────────────────────
              Obx(() {
                if (controller.packetList.isEmpty)
                  return const SizedBox.shrink();
                return Row(
                  children: [
                    Checkbox(
                      value: controller.isAllSelected.value,
                      onChanged: (value) {
                        controller.toggleSelectAll(value ?? false);
                      },
                    ),
                    Expanded(
                      child: Text(
                        "Select All  •  ${controller.selectedCount.value} Selected",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color: kBlackColor,
                          fontSize: responsiveFont(16),
                          fontWeight: FontWeight.w400,
                        ),
                      ),
                    ),
                    SizedBox(
                      width: 160.w,
                      height: 40.h,
                      child: AppActiveButton(
                        buttontitle: "Pick-Up Packet",
                        onTap: () {
                          controller.submitSelectedPackets(context);
                        },
                      ),
                    ),
                  ],
                ).paddingOnly(top: 4.h, left: 10.w, right: 10.w, bottom: 4.h);
              }),

              // ── Packet list ─────────────────────────────────────────────
              Obx(() {
                if (controller.isLoading.value) {
                  return const Expanded(
                    child: CommonSkeletonPatientList(itemCount: 6),
                  );
                }
                if (controller.packetList.isEmpty) {
                  return Expanded(
                    child: Center(
                      child: CommonText(
                        text: "Data Not Found",
                        fontSize: 16.sp,
                        fontWeight: FontWeight.w600,
                        textColor: kBlackColor,
                        textAlign: TextAlign.center,
                      ),
                    ),
                  );
                }
                return Expanded(
                  child: ListView.builder(
                    padding: EdgeInsets.only(left: 10.w, right: 10.w),
                    itemCount: controller.packetList.length,
                    itemBuilder: (context, index) {
                      final data = controller.packetList[index];
                      return Container(
                        decoration: BoxDecoration(
                          boxShadow: [
                            BoxShadow(
                              color: dropDownTitleHeader.withValues(alpha: 0.2),
                              spreadRadius: 2,
                              blurRadius: 2,
                            ),
                          ],
                          color: kWhiteColor,
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Row(
                          children: [
                            // Checkbox
                            Checkbox(
                              value: data.isSelected,
                              onChanged: (_) {
                                controller.togglePacketSelection(index);
                              },
                            ),
                            // Detail table
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(0, 8.h, 8.w, 8.h),
                                child: Column(
                                  children: [
                                    // Header row — labels
                                    _tableRow(
                                      isHeader: true,
                                      srNo: "Sr. No",
                                      label: "Patient Name",
                                      value: "Delivery Challan No",
                                    ),
                                    SizedBox(height: 4.h),
                                    // Value row
                                    _tableRow(
                                      srNo: "${index + 1}",
                                      label: data.patientName ?? "",
                                      value: data.deliveryChallanID ?? "",
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ),
                      ).paddingOnly(top: 6.h, bottom: 6.h);
                    },
                  ),
                );
              }),
            ],
          ),
        ),
      ),
    );
  }

  Widget _tableRow({
    bool isHeader = false,
    String? srNo,
    required String label,
    required String value,
  }) {
    final textStyle = TextStyle(
      color: isHeader ? Colors.white : Colors.black,
      fontFamily: FontConstants.interFonts,
      fontWeight: isHeader ? FontWeight.w400 : FontWeight.w500,
      fontSize: responsiveFont(14),
    );
    return Container(
      decoration: BoxDecoration(
        color: isHeader ? kPrimaryColor : kWhiteColor,
        borderRadius:
            isHeader
                ? BorderRadius.only(
                  topLeft: Radius.circular(5),
                  topRight: Radius.circular(5),
                )
                : null,
        border:
            isHeader
                ? null
                : Border.all(
                  color: kBlackColor.withValues(alpha: 0.2),
                  width: 1,
                ),
      ),
      child: Row(
        children: [
          if (srNo != null)
            Expanded(
              flex: 1,
              child: Center(
                child: Text(
                  srNo,
                  textAlign: TextAlign.center,
                  style: textStyle,
                ),
              ),
            ),
          Expanded(
            flex: 2,
            child: Center(
              child: Text(label, textAlign: TextAlign.center, style: textStyle),
            ),
          ),
          Expanded(
            flex: 2,
            child: Center(
              child: Text(value, textAlign: TextAlign.center, style: textStyle),
            ),
          ),
        ],
      ),
    );
  }
}
