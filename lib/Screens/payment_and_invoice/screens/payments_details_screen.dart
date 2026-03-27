// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../controllers/payment_details_controller.dart';
import '../widgets/otp_verify_sheet.dart';

class PaymentsDetailsScreen extends StatelessWidget {
  const PaymentsDetailsScreen({super.key, required this.controller});

  final PaymentDetailsController controller;

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Obx(() {
        final details = controller.paymentDetails.value;
        return Column(
          children: [
            Row(
              children: [
                Expanded(
                  child: AppTextField(
                    controller: TextEditingController(
                      text: controller.selectedYear.value?.yearName ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      _selectYear(context, controller);
                    },
                    hint: 'Year*',
                    label: CommonText(
                      text: 'Year*',
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
                          calendar,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: AppTextField(
                    controller: TextEditingController(
                      text: controller.selectedMonth.value?.monthNameEng ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      _selectMonth(context, controller);
                    },
                    hint: 'Month *',
                    label: CommonText(
                      text: 'Month *',
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
                          calendar,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 10),
            controller.isDoctor
                ? AppTextField(
                  controller: TextEditingController(
                    text: controller.selectedCompany.value?.paidByCompany ?? "",
                  ),
                  readOnly: true,
                  onTap: () {
                    _selectCompany(context, controller);
                  },
                  hint: 'Paid By Company *',
                  label: CommonText(
                    text: 'Paid By Company *',
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
                  suffixIcon: Icon(Icons.keyboard_arrow_down),
                )
                : const SizedBox.shrink(),
            controller.isDoctor ? const SizedBox(height: 10) : Container(),
            controller.isLoading.value
                ? CommonSkeletonScreeningDetailsTable(
                  rowCount: controller.isDoctor ? 7 : 6,
                )
                : Container(
                  padding: const EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: const [
                      BoxShadow(
                        offset: Offset(0, 1),
                        color: Colors.grey,
                        spreadRadius: 0,
                        blurRadius: 10,
                      ),
                    ],
                  ),
                  child: Column(
                    children: [
                      _detailRow(
                        "Payable Amount",
                        "${details?.payableAmount ?? 0}",
                      ),
                      _detailRow("Penalty", "${details?.penaltyAmount ?? 0}"),
                      _detailRow("TDS", "${details?.tDSAmount ?? 0}"),
                      controller.isDoctor
                          ? _detailRow(
                            "Gross Amount",
                            "${details?.grossAmount ?? 0}",
                          )
                          : const SizedBox.shrink(),
                      _detailRow(
                        "Net Payable Amount",
                        "${details?.finalPayableAmount ?? 0}",
                      ),
                      _detailRow("Payment Date", details?.paymentDate ?? "0"),
                      _detailRow("UTR No", details?.uTRNo ?? "0"),
                    ],
                  ),
                ),
            const SizedBox(height: 20),
            controller.showPaymentInProgressNote.value
                ? Container(
                  padding: EdgeInsets.symmetric(vertical: 2, horizontal: 6),
                  decoration: BoxDecoration(
                    color: reVerifiedRequiredColor,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Text(
                    "Payment In Progress",
                    style: TextStyle(
                      color: kWhiteColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w700,
                      fontSize: responsiveFont(14),
                    ),
                  ),
                )
                : const SizedBox.shrink(),
            controller.showInvoiceLink.value
                ? Padding(
                  padding: const EdgeInsets.only(top: 10),
                  child: GestureDetector(
                    onTap: () async {
                      final url = details?.invoiceUrl ?? "";
                      if (url.trim().isEmpty) {
                        ToastManager.toast("Invoice Not Generated");
                        return;
                      }
                      await launchUrl(
                        Uri.parse(url),
                        mode: LaunchMode.externalApplication,
                      ).then((launched) {
                        if (!launched) {
                          ToastManager.toast("Unable to open invoice");
                        }
                      });
                    },
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(icDoc),
                        ),
                        const SizedBox(width: 6),
                        Text(
                          "View Invoice",
                          style: TextStyle(
                            color: kPrimaryColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ],
                    ),
                  ),
                )
                : const SizedBox.shrink(),
            const SizedBox(height: 14),
            Row(
              children: [
                controller.showPaymentReceivedButton.value
                    ? Expanded(
                      child: AppActiveButton(
                        buttontitle: "Payment Received",
                        onTap: () {
                          _showOtpSheet(context, controller, 4);
                        },
                        isCancel: true,
                      ),
                    )
                    : const SizedBox.shrink(),
                controller.showPaymentReceivedButton.value
                    ? const SizedBox(width: 8)
                    : const SizedBox.shrink(),
                controller.showPaymentNotReceivedButton.value
                    ? Expanded(
                      child: AppActiveButton(
                        buttontitle: "Payment Not Received",
                        onTap: () {
                          _showOtpSheet(context, controller, 5);
                        },
                      ),
                    )
                    : const SizedBox.shrink(),
              ],
            ),
          ],
        );
      }),
    );
  }

  Future<void> _selectYear(
    BuildContext context,
    PaymentDetailsController controller,
  ) async {
    final years = await controller.fetchYears();
    if (years.isEmpty) {
      return;
    }
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
            titleString: "Years",
            dropDownList: years,
            dropDownMenu: DropDownTypeMenu.Years,
            onApplyTap: (p0) {
              controller.updateYear(p0);
            },
          ),
        );
      },
    );
  }

  Future<void> _selectMonth(
    BuildContext context,
    PaymentDetailsController controller,
  ) async {
    final months = await controller.fetchMonths();
    if (months.isEmpty) {
      return;
    }
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
            titleString: "Month",
            dropDownList: months,
            dropDownMenu: DropDownTypeMenu.Months,
            onApplyTap: (p0) {
              controller.updateMonth(p0);
            },
          ),
        );
      },
    );
  }

  Future<void> _selectCompany(
    BuildContext context,
    PaymentDetailsController controller,
  ) async {
    if (controller.selectedYear.value == null) {
      ToastManager.toast("Please select year");
      return;
    }
    if (controller.selectedMonth.value == null) {
      ToastManager.toast("Please select month");
      return;
    }
    final companies = await controller.fetchCompanyList();
    if (companies.isEmpty) {
      return;
    }
    final selectedCompanyId = controller.selectedCompany.value?.paidByCompanyID;
    for (final company in companies) {
      company.isSelected = company.paidByCompanyID == selectedCompanyId;
    }
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
            titleString: "Paid By Company",
            dropDownList: companies,
            dropDownMenu: DropDownTypeMenu.PaidByCompany,
            onApplyTap: (p0) {
              controller.updateCompany(p0);
            },
          ),
        );
      },
    );
  }

  Future<void> _showOtpSheet(
    BuildContext context,
    PaymentDetailsController controller,
    int paymentStatusId,
  ) async {
    if (controller.isDoctor && controller.selectedCompany.value == null) {
      ToastManager.toast("Please select company");
      return;
    }
    final otp = await controller.requestPaymentOtp(paymentStatusId);
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
            onResend: () => controller.requestPaymentOtp(paymentStatusId),
            onVerify: (verifiedOtp) {
              Navigator.pop(sheetContext);
              controller.submitPaymentStatus(paymentStatusId, verifiedOtp);
            },
          ),
        );
      },
    );
  }

  Widget _detailRow(String title, String value) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 6, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: kWhiteColor,
          border: Border.all(color: kTextFieldBorder, width: 1),
        ),
        height: 36,
        child: Row(
          children: [
            Container(
              width: 146,
              color: Colors.transparent,
              padding: const EdgeInsets.fromLTRB(6, 0, 0, 0),
              child: Text(
                title,
                textAlign: TextAlign.left,
                style: TextStyle(
                  color: kBlackColor,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w500,
                  fontSize: responsiveFont(12),
                ),
              ),
            ),
            Expanded(
              child: Container(
                padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
                decoration: const BoxDecoration(
                  color: Colors.transparent,
                  border: Border(
                    left: BorderSide(color: kTextFieldBorder, width: 1),
                  ),
                ),
                child: Align(
                  alignment: Alignment.center,
                  child: Text(
                    value,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(12),
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}