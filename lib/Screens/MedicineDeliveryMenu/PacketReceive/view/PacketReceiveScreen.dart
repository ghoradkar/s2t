import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppBarCodeTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketReceive/controller/packet_receive_controller.dart';
import '../../../../../Modules/constants/fonts.dart';

class PacketReceiveScreen extends StatefulWidget {
  const PacketReceiveScreen({super.key});

  @override
  State<PacketReceiveScreen> createState() => _PacketReceiveScreenState();
}

class _PacketReceiveScreenState extends State<PacketReceiveScreen> {
  late final PacketReceiveController controller;

  @override
  void initState() {
    super.initState();
    final alreadyRegistered = Get.isRegistered<PacketReceiveController>();
    controller = Get.put(PacketReceiveController());
    if (alreadyRegistered) {
      controller.resetState();
    }
  }

  @override
  void dispose() {
    Get.delete<PacketReceiveController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Packet Received in Lab",
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
                  padding: EdgeInsets.symmetric(vertical: 8.h,horizontal: 8.w),
                  child: Column(
                    children: [
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
                          text: controller.selectedLab.value?.labName ?? "",
                        ),
                        readOnly: true,
                        onTap: () {
                          controller.showLabDropdown(context);
                        },
                        hint: 'Receiving Lab *',
                        label: CommonText(
                          text: 'Receiving Lab *',
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
                        titleHeaderString: "Scan Packet Number",
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

              Obx(
                () => Row(
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
                      width: 160.w,
                      height: 40.h,
                      child: AppActiveButton(
                        buttontitle: "Confirmed",
                        onTap: () {
                          controller.submitSelectedPackets(context);
                        },
                      ),
                    ),
                  ],
                ).paddingOnly(top: 12.h, left: 10.w, right: 10.w, bottom: 8.h),
              ),

               SizedBox(height: 4.h),

              Expanded(
                child: Obx(
                  () => ListView.builder(
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
                            Checkbox(
                              value: data.isSelected,
                              onChanged: (_) {
                                controller.togglePacketSelection(index);
                              },
                            ),
                            Expanded(
                              child: Container(
                                padding: EdgeInsets.fromLTRB(0, 8.h, 8.w, 8.h),
                                child: Column(
                                  children: [
                                    Container(
                                      // padding: EdgeInsets.fromLTRB(0, 6, 0, 6),
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
                                              "Lab Name",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.white,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w400,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            child: Text(
                                              "${data.labName ?? ""} Lab",
                                              textAlign: TextAlign.center,
                                              style: TextStyle(
                                                color: Colors.white,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                                fontWeight: FontWeight.w400,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                     SizedBox(height: 4.h),
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.start,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        Expanded(
                                          child: Container(
                                            padding: EdgeInsets.fromLTRB(
                                              0,
                                              4,
                                              0,
                                              4,
                                            ),
                                            decoration: BoxDecoration(
                                              color: kWhiteColor,
                                              border: Border(
                                                left: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                top: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                bottom: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                right: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "District",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.black,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(14),
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
                                              border: Border(
                                                left: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                top: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                bottom: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                right: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                data.dISTNAME ?? "",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.black,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(14),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                     SizedBox(height: 4.h),
                                    Row(
                                      mainAxisAlignment: MainAxisAlignment.start,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
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
                                              border: Border(
                                                left: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                top: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                bottom: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                right: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                "Packet Number",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.black,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(14),
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
                                              border: Border(
                                                left: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                top: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                bottom: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                                right: BorderSide(
                                                  color: kBlackColor.withValues(
                                                    alpha: 0.2,
                                                  ),
                                                  width: 1,
                                                ),
                                              ),
                                            ),
                                            child: Center(
                                              child: Text(
                                                data.packetNumber ?? "",
                                                textAlign: TextAlign.center,
                                                style: TextStyle(
                                                  color: Colors.black,
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: responsiveFont(14),
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
                      ).paddingOnly(top: 6.h,bottom: 6.h);
                    },
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
