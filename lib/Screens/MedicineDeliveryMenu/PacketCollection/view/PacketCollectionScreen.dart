// ignore_for_file: use_full_hex_values_for_flutter_colors
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppBarCodeTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketCollection/controller/packet_collection_controller.dart';
import '../../../../../Modules/constants/fonts.dart';

class PacketCollectionScreen extends StatefulWidget {
  const PacketCollectionScreen({super.key});

  @override
  State<PacketCollectionScreen> createState() => _PacketCollectionScreenState();
}

class _PacketCollectionScreenState extends State<PacketCollectionScreen> {
  late final PacketCollectionController controller;

  @override
  void initState() {
    super.initState();
    final alreadyRegistered = Get.isRegistered<PacketCollectionController>();
    controller = Get.put(PacketCollectionController());
    if (alreadyRegistered) {
      controller.resetState();
    }
  }

  @override
  void dispose() {
    Get.delete<PacketCollectionController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Packet Collection",
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
                  padding: EdgeInsets.all(8),
                  child: Column(
                    children: [
                      Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          "Note : Dates for selection are w.r.t packet creation",
                          style: TextStyle(
                            color: Colors.red,
                            fontFamily: FontConstants.interFonts,
                            fontSize: 12.sp,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                controller.pickFromDate(context);
                              },
                              controller: TextEditingController(
                                text: controller.fromDateString.value,
                              ),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'From Date *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                icCalendarMonth,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                controller.pickToDate(context);
                              },
                              controller: TextEditingController(
                                text: controller.toDateString.value,
                              ),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'To Date *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                icCalendarMonth,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                controller.showDistrictDropdown(context);
                              },
                              suffixIcon: Icon(
                                Icons.keyboard_arrow_down_rounded,
                              ),
                              controller: TextEditingController(
                                text:
                                    controller
                                        .selectedDistrict
                                        .value
                                        ?.dISTNAME ??
                                    "",
                              ),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'District *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                iconMap,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: AppTextField(
                              onTap: () {
                                controller.showLandingLabDropdown(context);
                              },
                              suffixIcon: Icon(
                                Icons.keyboard_arrow_down_rounded,
                              ),
                              controller: TextEditingController(
                                text:
                                    controller.selectedLab.value?.labName ?? "",
                              ),
                              readOnly: true,
                              label: RichText(
                                text: TextSpan(
                                  text: 'Landing Lab *',
                                  style: TextStyle(
                                    color: kLabelTextColor,
                                    fontSize: 14.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                              prefixIcon: Image.asset(
                                icCalendarMonth,
                                color: kPrimaryColor,
                              ).paddingOnly(left: 6.w),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      AppBarCodeTextfield(
                        titleHeaderString: "Scan packet No.",
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
                ),
              ),
              const SizedBox(height: 4),
              Obx(
                () => Container(
                  padding: EdgeInsets.all(8),
                  child: Row(
                    children: [
                      Expanded(
                        child: Text(
                          "${controller.selectedCount.value} Selected",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(16),
                            fontWeight: FontWeight.w400,
                          ),
                        ),
                      ),
                      SizedBox(
                        width: 160,
                        height: 40,
                        child: AppActiveButton(
                          buttontitle: "Collect",
                          onTap: () {
                            controller.submitSelectedPackets(context);
                          },
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 4),
              Expanded(
                child: Obx(
                  () => ListView.builder(
                    itemCount: controller.packetList.length,
                    itemBuilder: (context, index) {
                      final data = controller.packetList[index];
                      return Padding(
                        padding: const EdgeInsets.fromLTRB(2, 2, 2, 16),
                        child: Container(
                          decoration: BoxDecoration(
                            boxShadow: [
                              BoxShadow(
                                offset: Offset(0, 0.5),
                                color: Colors.black.withValues(alpha: 0.6),
                                spreadRadius: 0,
                                blurRadius: 2,
                              ),
                            ],
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Row(
                            children: [
                              Checkbox(
                                value: data.isSelected,
                                onChanged: (_) {
                                  controller.togglePacketSelection(index);
                                },
                              ),
                              Expanded(
                                child: Padding(
                                  padding: const EdgeInsets.fromLTRB(
                                    8,
                                    2,
                                    4,
                                    8,
                                  ),
                                  child: Column(
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.fromLTRB(
                                          0,
                                          6,
                                          0,
                                          0,
                                        ),
                                        child: Container(
                                          decoration: BoxDecoration(
                                            color: kPrimaryColor,
                                            borderRadius: BorderRadius.only(
                                              topLeft: Radius.circular(5),
                                              topRight: Radius.circular(5),
                                            ),
                                          ),
                                          height: 36.h,
                                          child: Row(
                                            children: [
                                              Expanded(
                                                child: Text(
                                                  "Lab Name",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kWhiteColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              Expanded(
                                                child: Text(
                                                  data.labName ?? "",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kWhiteColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.fromLTRB(
                                          0,
                                          6,
                                          0,
                                          0,
                                        ),
                                        child: Container(
                                          decoration: BoxDecoration(
                                            color: Colors.transparent,
                                            border: Border.all(
                                              color: kTextFieldBorder,
                                              width: 1,
                                            ),
                                          ),
                                          height: 36.h,
                                          child: Row(
                                            children: [
                                              Expanded(
                                                child: Text(
                                                  "District",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kBlackColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              Expanded(
                                                child: Text(
                                                  data.dISTNAME ?? "",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kBlackColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.fromLTRB(
                                          0,
                                          6,
                                          0,
                                          0,
                                        ),
                                        child: Container(
                                          decoration: BoxDecoration(
                                            color: Colors.transparent,
                                            border: Border.all(
                                              color: kTextFieldBorder,
                                              width: 1,
                                            ),
                                            borderRadius: BorderRadius.only(
                                              bottomLeft: Radius.circular(5),
                                              bottomRight: Radius.circular(5),
                                            ),
                                          ),
                                          height: 36.h,
                                          child: Row(
                                            children: [
                                              Expanded(
                                                child: Text(
                                                  "Packet Number",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kBlackColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                              Expanded(
                                                child: Text(
                                                  data.packetNumber ?? "",
                                                  textAlign: TextAlign.center,
                                                  style: TextStyle(
                                                    color: kBlackColor,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w500,
                                                    fontSize: responsiveFont(
                                                      12,
                                                    ),
                                                  ),
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
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
                  ),
                ),
              ),
            ],
          ).paddingSymmetric(vertical: 12, horizontal: 12),
        ),
      ),
    );
  }
}
