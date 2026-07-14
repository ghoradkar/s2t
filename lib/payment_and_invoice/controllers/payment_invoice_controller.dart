import 'package:get/get.dart';

class PaymentInvoiceController extends GetxController {
  final RxBool isInvoicesTab = true.obs;

  String get title =>
      isInvoicesTab.value
          ? "Invoices & Payments"
          : "Payment Details & Confirmation";

  void showInvoices() => isInvoicesTab.value = true;

  void showPayments() => isInvoicesTab.value = false;
}
