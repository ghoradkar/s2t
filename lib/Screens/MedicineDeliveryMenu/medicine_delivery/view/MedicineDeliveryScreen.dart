import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/AppDataManager/AppDataManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppBarCodeTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/controller/MedicineDeliveryController.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/view/MedicineDeliveryAcknowledgementScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/view/MedicineDeliveryRow.dart';
import '../../../../../Modules/constants/fonts.dart';

class MedicineDeliveryScreen extends StatefulWidget {
  const MedicineDeliveryScreen({super.key});

  @override
  State<MedicineDeliveryScreen> createState() => _MedicineDeliveryScreenState();
}

class _MedicineDeliveryScreenState extends State<MedicineDeliveryScreen> {
  late final MedicineDeliveryController controller;

  @override
  void initState() {
    super.initState();
    final alreadyRegistered = Get.isRegistered<MedicineDeliveryController>();
    controller = Get.put(MedicineDeliveryController());
    if (alreadyRegistered) {
      controller.resetState();
    }
  }

  @override
  void dispose() {
    Get.delete<MedicineDeliveryController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Medicine Delivery",
          showActions: true,
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            AppDataManager.fromDate = "";
            AppDataManager.toDate = "";
            AppDataManager.selectedTaluka = null;
            AppDataManager.selectedResource = null;
            Navigator.pop(context);
          },
          actions: [
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
              child: GestureDetector(
                onTap: () => ToastManager.showInfoPopup(context),
                child: Container(
                  width: 30,
                  height: 30,
                  padding: const EdgeInsets.all(2),
                  child: Image.asset(icInfo),
                ),
              ),
            ),
          ],
        ),
        body: Column(
          children: [
            // ── Filter section ────────────────────────────────────────────────
            Obx(
              () => Container(
                padding: EdgeInsets.only(
                  top: 4.h,
                  bottom: 8.h,
                  left: 4.w,
                  right: 4.w,
                ),
                decoration: BoxDecoration(
                  boxShadow: [
                    BoxShadow(
                      color: kLabelTextColor.withValues(alpha: 0.2),
                      spreadRadius: 2,
                      blurRadius: 4,
                    ),
                  ],
                  color: kWhiteColor,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Column(
                  children: [
                    SizedBox(height: 4.h),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: CommonText(
                        text:
                            "Note: Dates for selection are w.r.t. packet creation",
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        textColor: noteRedColor,
                        textAlign: TextAlign.start,
                      ),
                    ),
                    SizedBox(height: 4.h),

                    // From / To date row
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            controller: TextEditingController(
                              text: controller.fromDateString.value,
                            ),
                            readOnly: true,
                            onTap: () => controller.pickFromDate(context),
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
                            onTap: () => controller.pickToDate(context),
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

                    // District / Taluka row
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            controller: TextEditingController(
                              text: controller.selectedDistrictName.value,
                            ),
                            readOnly: true,
                            onTap:
                                () => controller.showDistrictDropdown(context),
                            hint: 'District *',
                            label: CommonText(
                              text: 'District *',
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
                            suffixIcon: const Icon(Icons.keyboard_arrow_down),
                          ),
                        ),
                        SizedBox(width: 8.w),
                        Expanded(
                          child: AppTextField(
                            controller: TextEditingController(
                              text: controller.selectedTalukaName.value,
                            ),
                            readOnly: true,
                            onTap: () => controller.showTalukaDropdown(context),
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
                            suffixIcon: const Icon(Icons.keyboard_arrow_down),
                          ),
                        ),
                      ],
                    ),
                    ///dont remove
                    // SizedBox(height: 8.h),
                    //
                    // // Barcode scanner
                    // AppBarCodeTextfield(
                    //   titleHeaderString: "Scan delivery challan",
                    //   controller: controller.barcodeController,
                    //   onSearch: (_) => controller.submitBarcode(context),
                    //   onBarcodeScanned:
                    //       () => controller.openBarcodeScanner(context),
                    // ),
                  ],
                ),
              ),
            ),

            SizedBox(height: 8.h),

            // ── Search patient ────────────────────────────────────────────────
            AppTextField(
              controller: controller.searchController,
              readOnly: false,
              hint: 'Search Patient',
              label: CommonText(
                text: 'Search Patient',
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
              onChange: (v) => controller.onSearchChanged(v),
              suffixIcon: SizedBox(
                height: 20.h,
                width: 20.w,
                child: const Center(child: Icon(Icons.search)),
              ),
            ),

            SizedBox(height: 8.h),

            // ── List ──────────────────────────────────────────────────────────
            Expanded(
              child: Obx(() {
                if (controller.isLoading.value) {
                  return const CommonSkeletonPatientList(itemCount: 6);
                }

                return Column(
                  children: [
                    // Header row
                    Container(
                      decoration: BoxDecoration(
                        color: kPrimaryColor,
                        border: Border(
                          top: BorderSide(color: Colors.grey, width: 1),
                          left: BorderSide(color: Colors.grey, width: 1),
                          right: BorderSide(color: Colors.grey, width: 1),
                        ),
                        borderRadius: const BorderRadius.only(
                          topLeft: Radius.circular(8),
                          topRight: Radius.circular(8),
                        ),
                      ),
                      height: 40,
                      child: Row(
                        children: [
                          _headerCell("Sr.\nNo.", width: 36),
                          Expanded(flex: 3, child: _headerCell("Patient Name")),
                          _headerCell("Delivery\nChallan No.", width: 56),
                          // _headerCell("Gender", width: 40),
                          _headerCell("Status", width: 40),
                        ],
                      ),
                    ),

                    // List body
                    Expanded(
                      child:
                          controller.filteredList.isEmpty
                              ?  Center(
                                child: CommonText(
                                  text: "No Data Available",
                                  fontSize: 14.sp,
                                  fontWeight: FontWeight.w600,
                                  textColor: kBlackColor,
                                  textAlign: TextAlign.center,
                                ),
                              )
                              : ListView.builder(
                                itemCount: controller.filteredList.length,
                                itemBuilder: (context, index) {
                                  final item = controller.filteredList[index];
                                  return MedicineDeliveryRow(
                                    index: index,
                                    obj: item,
                                    onTap: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (_) =>
                                              MedicineDeliveryAcknowledgementScreen(
                                            beneficiary: item,
                                          ),
                                        ),
                                      ).then((submitted) {
                                        if (submitted == true) {
                                          controller.lastSubmittedRegdId =
                                              item.regdId?.toString() ?? '';
                                          controller.refreshList();
                                        }
                                      });
                                    },
                                  );
                                },
                              ),
                    ),
                  ],
                );
              }),
            ),
          ],
        ).paddingSymmetric(vertical: 10.h, horizontal: 12.w),
      ),
    );
  }

  Widget _headerCell(String text, {double? width}) {
    final cell = Container(
      decoration: const BoxDecoration(
        border: Border(right: BorderSide(color: Colors.grey, width: 0.5)),
      ),
      child: Center(
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: Colors.white,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w500,
            fontSize: responsiveFont(12),
          ),
        ),
      ),
    );
    if (width != null) return SizedBox(width: width, child: cell);
    return cell;
  }
}
