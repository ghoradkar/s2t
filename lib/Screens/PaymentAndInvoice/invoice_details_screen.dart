// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/MonthWiseInvoiceResponse/MonthWiseInvoiceResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/CommonSkeletonList.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'controllers/invoice_controller.dart';
import 'raise_invoice_screen.dart';
import 'widgets/invoice_row.dart';

class InvoiceDetailsScreen extends StatelessWidget {
  const InvoiceDetailsScreen({super.key, required this.controller});

  final InvoiceController controller;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Obx(() {
          return Container(
            padding: const EdgeInsets.symmetric(vertical: 12,horizontal: 12),
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

            // AppDropdownTextfield(
            //   icon: calendar,
            //   titleHeaderString: "Year*",
            //   valueString: controller.selectedYear.value?.yearName ?? "",
            //   onTap: () {
            //     _selectYear(context, controller);
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
              boxShadow: const [
                BoxShadow(
                  color: Color(0x0F000000),
                  blurRadius: 12,
                  offset: Offset(0, 6),
                ),
              ],
            ),
            child: Column(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(vertical: 8),
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(12),
                      topRight: Radius.circular(12),
                    ),
                  ),
                  child: Row(
                    children: [
                      SizedBox(
                        width: 70,
                        child: Text(
                          "Invoice\nMonth",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            color: kWhiteColor,
                            fontSize: responsiveFont(10),
                            fontWeight: FontWeight.w600,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                      ),
                      SizedBox(
                        width: 70,
                        child: Text(
                          "Service\nDays",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            color: kWhiteColor,
                            fontSize: responsiveFont(10),
                            fontWeight: FontWeight.w600,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          "Billable\nBeneficiaries",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            color: kWhiteColor,
                            fontSize: responsiveFont(10),
                            fontWeight: FontWeight.w600,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                      ),
                      SizedBox(
                        width: 90,
                        child: Text(
                          "Invoice",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            color: kWhiteColor,
                            fontSize: responsiveFont(10),
                            fontWeight: FontWeight.w600,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Obx(() {
                    final items = controller.invoices;
                    if (controller.isLoading.value) {
                      return const CommonSkeletonInvoiceTable(itemCount: 12,);
                    }
                    if (items.isEmpty) {
                      return Center(
                        child: Text(
                          "No invoices found",
                          style: TextStyle(
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w500,
                            fontFamily: FontConstants.interFonts,
                          ),
                        ),
                      );
                    }
                    return ListView.builder(
                      padding: EdgeInsets.zero,
                      itemCount: items.length,
                      itemBuilder: (context, index) {
                        final item = items[index];
                        return InvoiceRow(
                          obj: item,
                          onMonthTap: () {
                            _openRaiseInvoice(controller, item);
                          },
                          onActionTap: () async {
                            if (item.invoiceStatus == "VIEW") {
                              await controller.openInvoiceUrl(
                                item.invoiceUrl ?? "",
                              );
                              return;
                            }
                            if (item.invoiceStatus == "Raise") {
                              _openRaiseInvoice(controller, item);
                              return;
                            }
                            ToastManager.toast("Invoice not available");
                          },
                          isDoctor: controller.isDoctor,
                        );
                      },
                    );
                  }),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Future<void> _selectYear(
      BuildContext context,
      InvoiceController controller,
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
            // color: Colors.white,
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

  Future<void> _openRaiseInvoice(
      InvoiceController controller,
      MonthWiseInvoiceOutput item,
      ) async {
    await Get.to(() => RaiseInvoiceScreen(invoiceObj: item));
    controller.fetchInvoices();
  }
}
