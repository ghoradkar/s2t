// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';

import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/MonthWiseInvoiceResponse/MonthWiseInvoiceResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/CommonSkeletonList.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'controllers/raise_invoice_controller.dart';
import 'widgets/otp_verify_sheet.dart';
import 'widgets/verification_remark_sheet.dart';

class RaiseInvoiceScreen extends StatefulWidget {
  const RaiseInvoiceScreen({super.key, required this.invoiceObj});

  final MonthWiseInvoiceOutput invoiceObj;

  @override
  State<RaiseInvoiceScreen> createState() => _RaiseInvoiceScreenState();
}

class _RaiseInvoiceScreenState extends State<RaiseInvoiceScreen> {
  late final RaiseInvoiceController controller;
  late final String tag;

  @override
  void initState() {
    super.initState();
    tag =
        "raise_${widget.invoiceObj.userInviceID ?? DateTime.now().millisecondsSinceEpoch}";
    controller = Get.put(
      RaiseInvoiceController(invoiceObj: widget.invoiceObj),
      tag: tag,
    );
  }

  @override
  void dispose() {
    Get.delete<RaiseInvoiceController>(tag: tag);
    super.dispose();
  }

  Future<void> _selectServiceType(BuildContext context) async {
    final list = controller.getServiceTypeOptions();
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
            titleString: "Select Service Type",
            dropDownList: list,
            dropDownMenu: DropDownTypeMenu.Years,
            onApplyTap: (p0) {
              controller.updateServiceType(p0);
            },
          ),
        );
      },
    );
  }

  Future<void> _showRaiseOtpSheet() async {
    if (controller.campList.isEmpty) {
      ToastManager.toast("No data available");
      return;
    }
    final otp = await controller.requestOtp(2);
    if (otp == null) {
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.black.withValues(alpha: 0.5),
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: OtpVerifySheet(
            expectedOtp: otp,
            onResend: () => controller.requestOtp(2),
            onVerify: (verifiedOtp) {
              Navigator.pop(sheetContext);
              controller.submitRaiseInvoice(verifiedOtp);
            },
          ),
        );
      },
    );
  }

  Future<void> _showVerificationSheet() async {
    if (controller.campList.isEmpty) {
      ToastManager.toast("No data available");
      return;
    }
    final remarks = await controller.fetchVerificationRemarks();
    if (remarks.isEmpty) {
      return;
    }
    final otp = await controller.requestOtp(3);
    if (otp == null) {
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.black.withValues(alpha: 0.5),
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: VerificationRemarkSheet(
            remarks: remarks,
            expectedOtp: otp,
            onResend: () => controller.requestOtp(3),
            onVerify: (verifiedOtp, remark, otherRemark) {
              Navigator.pop(sheetContext);
              controller.submitSendForVerification(
                verifiedOtp,
                remark.verificationRemarkID ?? 0,
                otherRemark,
              );
            },
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    String formatValue(double? value) {
      if (value == null) return "0.0";
      return value.toStringAsFixed(1);
    }

    Widget headerCell(String text, int flex) {
      return Expanded(
        flex: flex,
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kWhiteColor,
            fontSize: responsiveFont(9),
            fontWeight: FontWeight.w600,
          ),
        ),
      );
    }

    Widget valueCell(String text, int flex, {bool bold = false}) {
      return Expanded(
        flex: flex,
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: kBlackColor,
            fontSize: responsiveFont(9),
            fontWeight: bold ? FontWeight.w700 : FontWeight.w500,
          ),
        ),
      );
    }

    Widget actionButton(String text, VoidCallback onTap) {
      return Expanded(
        child: InkWell(
          onTap: onTap,
          child: Container(
            height: 44,
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.circular(8),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(
                  text,
                  style: TextStyle(
                    color: kWhiteColor,
                    fontSize: responsiveFont(10),
                    fontWeight: FontWeight.w600,
                  ),
                ),
                const SizedBox(width: 8),
                Icon(Icons.arrow_forward, color: kWhiteColor, size: 16),
              ],
            ),
          ),
        ),
      );
    }

    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Raise Invoice",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
          showActions: true,
          actions: [
            // IconButton(
            //   onPressed: () {},
            //   icon: Image.asset(icInfo, width: 20, height: 20),
            // ),
          ],
        ),
        body: SafeArea(
          bottom: true,
          child: SizedBox(
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Padding(
              padding: const EdgeInsets.fromLTRB(16, 12, 16, 16),
              child: Column(
                children: [
                Obx(() {
                  return Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: kWhiteColor,
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: kTextFieldBorder.withValues(alpha: 0.5),
                          blurRadius: 4,
                          spreadRadius: 4,
                        ),
                      ],
                    ),
                    child: AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text:
                            controller.selectedServiceType.value?.yearName ?? '',
                      ),
                      onTap: () {
                        _selectServiceType(context);
                      },
                      hint: 'Service Type',
                      label: CommonText(
                        text: 'Service Type',
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
                      suffixIcon: Icon(Icons.keyboard_arrow_down),
                    ),

                    // AppDropdownTextfield(
                    //   icon: icnTent,
                    //   titleHeaderString: "Service Type",
                    //   valueString:
                    //       controller.selectedServiceType.value?.yearName ?? "",
                    //   onTap: () {
                    //     _selectServiceType(context);
                    //   },
                    // ),
                  );
                }),
                const SizedBox(height: 12),
                Expanded(
                  child: Container(
                    decoration: BoxDecoration(
                      color: kWhiteColor,
                      borderRadius: BorderRadius.circular(12),
                      boxShadow: [
                        BoxShadow(
                          color: kTextFieldBorder.withValues(alpha: 0.5),
                          blurRadius: 4,
                          spreadRadius: 4,
                        ),
                      ],
                    ),
                    padding: EdgeInsets.all(8),
                    child: SingleChildScrollView(
                      scrollDirection: Axis.vertical,
                      child: Column(
                        children: [
                          Container(
                            decoration: BoxDecoration(
                              color: kPrimaryColor,
                              borderRadius: const BorderRadius.only(
                                topLeft: Radius.circular(12),
                                topRight: Radius.circular(12),
                              ),
                            ),
                            child: Column(
                              children: [
                                Row(
                                  children: [
                                    headerCell("Date", 2),
                                    headerCell("Camp ID", 2),
                                    headerCell("Phy. Exam\nDone", 2),
                                    headerCell("Rejected", 2),
                                    headerCell("Billable", 4),
                                  ],
                                ),
                                Container(
                                  color: kPurpleFaint,
                                  child: Row(
                                    children: [
                                      const Expanded(flex: 8, child: SizedBox()),
                                      valueCell("Same Day", 2),
                                      valueCell("Next Day", 2),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Obx(() {
                            if (controller.isLoading.value) {
                              return const CommonSkeletonInvoiceTable();
                            }
                            return Container(
                              decoration: BoxDecoration(
                                border: Border.all(color: kTextFieldBorder),
                              ),
                              child: Column(
                                children: [
                                  Container(
                                    color: const Color(0xFFF5F5F5),
                                    padding: const EdgeInsets.symmetric(
                                      vertical: 6,
                                    ),
                                    child: Row(
                                      children: [
                                        valueCell("Total", 4),
                                        valueCell(
                                          formatValue(
                                            controller.totalBeneficiaries.value,
                                          ),
                                          2,
                                          bold: true,
                                        ),
                                        valueCell(
                                          formatValue(
                                            controller
                                                .totalIndividualRejected.value,
                                          ),
                                          2,
                                          bold: true,
                                        ),
                                        valueCell(
                                          formatValue(
                                            controller
                                                .totalIndividualBillableWorker
                                                .value,
                                          ),
                                          2,
                                          bold: true,
                                        ),
                                        valueCell(
                                          formatValue(
                                            controller
                                                .totalIndividualBillableDependent
                                                .value,
                                          ),
                                          2,
                                          bold: true,
                                        ),
                                      ],
                                    ),
                                  ),
                                  Obx(() {
                                    return Column(
                                      children: List.generate(
                                        controller.campList.length,
                                        (index) {
                                          final item = controller.campList[index];
                                          return Container(
                                            padding: const EdgeInsets.symmetric(
                                              vertical: 6,
                                            ),
                                            decoration: BoxDecoration(
                                              border: Border(
                                                top: BorderSide(
                                                  color: kTextFieldBorder,
                                                ),
                                              ),
                                            ),
                                            child: Row(
                                              children: [
                                                valueCell(item.campDate ?? "", 2),
                                                valueCell(item.campID ?? "", 2),
                                                valueCell(
                                                  formatValue(
                                                    item.totalBeneficiaries,
                                                  ),
                                                  2,
                                                ),
                                                valueCell(
                                                  formatValue(
                                                    item.totalIndividualRejected,
                                                  ),
                                                  2,
                                                ),
                                                valueCell(
                                                  formatValue(
                                                    item.billablesSameDay ??
                                                        item
                                                            .individualBillableWorker,
                                                  ),
                                                  2,
                                                ),
                                                valueCell(
                                                  formatValue(
                                                    item.billablesAnotherDay ??
                                                        item
                                                            .individualBillableDependent,
                                                  ),
                                                  2,
                                                ),
                                              ],
                                            ),
                                          );
                                        },
                                      ),
                                    );
                                  }),
                                ],
                              ),
                            );
                          }),
                        ],
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 12),
                Obx(() {
                  final hasData = controller.campList.isNotEmpty;
                  final bottomInset = MediaQuery.of(context).padding.bottom;
                  return Padding(
                    padding: EdgeInsets.only(bottom: bottomInset),
                    child: Row(
                      children: [
                        hasData && controller.canRaiseInvoice
                            ? actionButton("Raise Invoice", _showRaiseOtpSheet)
                            : const SizedBox.shrink(),
                        hasData && controller.canRaiseInvoice
                            ? const SizedBox(width: 10)
                            : const SizedBox.shrink(),
                        hasData && controller.canSendForVerification
                            ? actionButton(
                              "Send for Verification",
                              _showVerificationSheet,
                            )
                            : const SizedBox.shrink(),
                      ],
                    ),
                  );
                }),
              ],
            ),
          ),
        ),
      )),
    );

  }
}
