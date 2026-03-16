// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';

import '../../Modules/constants/constants.dart';
import '../../Modules/constants/fonts.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import 'controllers/invoice_controller.dart';
import 'controllers/payment_details_controller.dart';
import 'controllers/payment_invoice_controller.dart';
import 'invoice_details_screen.dart';
import 'payments_details_screen.dart';

class PaymentInvoiceSegmentScreen extends StatefulWidget {
  const PaymentInvoiceSegmentScreen({super.key});

  @override
  State<PaymentInvoiceSegmentScreen> createState() =>
      _PaymentInvoiceSegmentScreenState();
}

class _PaymentInvoiceSegmentScreenState
    extends State<PaymentInvoiceSegmentScreen> {
  late final PaymentInvoiceController tabController;
  late final InvoiceController invoiceController;
  late final PaymentDetailsController paymentsController;
  late final Worker _invoiceYearWorker;
  late final Worker _paymentsYearWorker;
  late final Worker _tabWorker;
  bool _syncingYear = false;

  @override
  void initState() {
    super.initState();
    tabController = Get.put(PaymentInvoiceController());
    invoiceController = Get.put(InvoiceController());
    paymentsController = Get.put(PaymentDetailsController());
    _invoiceYearWorker = ever(invoiceController.selectedYear, (value) {
      if (_syncingYear) return;
      _syncingYear = true;
      if (value != null &&
          paymentsController.selectedYear.value?.yearID != value.yearID) {
        paymentsController.updateYear(value);
      }
      _syncingYear = false;
    });
    _paymentsYearWorker = ever(paymentsController.selectedYear, (value) {
      if (_syncingYear) return;
      _syncingYear = true;
      if (value != null &&
          invoiceController.selectedYear.value?.yearID != value.yearID) {
        invoiceController.updateYear(value);
      }
      _syncingYear = false;
    });
    _tabWorker = ever(tabController.isInvoicesTab, (value) {
      if (!value) {
        paymentsController.activate();
      }
    });

    WidgetsBinding.instance.addPostFrameCallback((_) {
      final initialYear = invoiceController.selectedYear.value;
      if (initialYear != null &&
          paymentsController.selectedYear.value?.yearID != initialYear.yearID) {
        paymentsController.updateYear(initialYear);
      }
    });
  }

  @override
  void dispose() {
    _invoiceYearWorker.dispose();
    _paymentsYearWorker.dispose();
    _tabWorker.dispose();
    Get.delete<PaymentInvoiceController>();
    Get.delete<InvoiceController>();
    Get.delete<PaymentDetailsController>();
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    return Obx(() {
      return NetworkWrapper(
        child: Scaffold(
          backgroundColor: kWhiteColor,
          appBar: mAppBar(
            scTitle: "Payment & Invoice",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Navigator.pop(context);
            },
            showActions: true,
            actions: [
              Visibility(
                visible: tabController.isInvoicesTab.value,
                child: IconButton(
                  onPressed: () {
                    _showColorInfoDialog(context);
                  },
                  icon: Image.asset(icInfo, width: 20, height: 20),
                ),
              ),
            ],
          ),
          body: SizedBox(
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Column(
              children: [
                showRadioCampButton(),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    child:
                        tabController.isInvoicesTab.value
                            ? InvoiceDetailsScreen(controller: invoiceController)
                            : PaymentsDetailsScreen(
                              controller: paymentsController,
                            ),
                  ),
                ),
              ],
            ),
          ),
        ),
      );
    });
  }

  Widget showRadioCampButton() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 12, 12, 10),
      child: Container(
        height: 44,
        // padding: const EdgeInsets.all(4),
        decoration: BoxDecoration(
          color: kWhiteColor,
          border: Border.all(color: kTextFieldBorder),
          borderRadius: BorderRadius.circular(50),
        ),
        child: Obx(() {
          final isInvoicesTab = tabController.isInvoicesTab.value;
          return Row(
            children: [
              Expanded(
                child: GestureDetector(
                  onTap: tabController.showInvoices,
                  child: Container(
                    decoration: BoxDecoration(
                      color: isInvoicesTab ? kPrimaryColor : kWhiteColor,
                      borderRadius: BorderRadius.circular(50),
                    ),
                    child: Center(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 18,
                            height: 18,
                            child: Image.asset(
                              icFileInvoice,
                              color:
                              isInvoicesTab ? kWhiteColor : kPrimaryColor,
                            ),
                          ),
                          const SizedBox(width: 6),
                          Text(
                            "Invoices",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color:
                              isInvoicesTab ? kWhiteColor : kPrimaryColor,
                              fontSize: responsiveFont(14),
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
              const SizedBox(width: 4),
              Expanded(
                child: GestureDetector(
                  onTap: tabController.showPayments,
                  child: Container(
                    decoration: BoxDecoration(
                      color: !isInvoicesTab ? kPrimaryColor : kWhiteColor,
                      borderRadius: BorderRadius.circular(50),
                    ),
                    child: Center(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 18,
                            height: 18,
                            child: Image.asset(
                              icCash,
                              color:
                              !isInvoicesTab ? kWhiteColor : kPrimaryColor,
                            ),
                          ),
                          const SizedBox(width: 6),
                          Text(
                            "Payments",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color:
                              !isInvoicesTab ? kWhiteColor : kPrimaryColor,
                              fontSize: responsiveFont(14),
                              fontWeight: FontWeight.w600,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ],
          );
        }),
      ),
    );
  }


  void _showColorInfoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (dialogContext) {
        return Dialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                padding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 10,
                ),
                decoration: BoxDecoration(
                  color: kPrimaryColor,
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(12),
                    topRight: Radius.circular(12),
                  ),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Text(
                        "Color Info.",
                        style: TextStyle(
                          color: kWhiteColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                    GestureDetector(
                      onTap: () => Navigator.pop(dialogContext),
                      child: Icon(Icons.close, color: kWhiteColor, size: 18),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(12),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "View - ",
                          style: TextStyle(
                            color: const Color(0xFF00B050),
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            "Invoice raised by user",
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(12),
                            ),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "View - ",
                          style: TextStyle(
                            color: const Color(0xFF0096FF),
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            "Invoice auto raised through system after 24 hrs of invoice generation",
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(12),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}
