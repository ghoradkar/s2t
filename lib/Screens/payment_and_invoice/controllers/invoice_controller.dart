import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import '../models/years_response.dart';
import '../models/month_wise_invoice_model.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../repository/invoice_repository.dart';

class InvoiceController extends GetxController {
  final _repo = InvoiceRepository();

  final RxList<MonthWiseInvoiceOutput> invoices = <MonthWiseInvoiceOutput>[].obs;
  final Rxn<YearsOutput> selectedYear = Rxn<YearsOutput>();
  final RxBool isLoading = false.obs;

  late final int empCode;
  late final int desigId;
  late final bool isDoctor;
  late final String statusType;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    desigId = user?.dESGID ?? 0;
    isDoctor = desigId == 34;
    statusType = isDoctor ? "2" : "1";
    _initializeYearSelection();
  }

  void _initializeYearSelection() {
    final now = DateTime.now();
    int month = now.month - 1;
    int year = now.year;
    if (month == 0) {
      month = 12;
      year = now.year - 1;
    }
    selectedYear.value = YearsOutput(yearID: year, yearName: "$year");
    fetchInvoices();
  }

  Future<List<YearsOutput>> fetchYears() async {
    ToastManager.showLoader();
    try {
      return await _repo.fetchYears();
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void updateYear(YearsOutput year) {
    selectedYear.value = year;
    fetchInvoices();
  }

  Future<void> fetchInvoices() async {
    isLoading.value = true;
    final yearId = selectedYear.value?.yearID?.toString() ?? "0";
    final Map<String, String> params = {
      "Year": yearId,
      "UserID": empCode.toString(),
      "LoginUserID": empCode.toString(),
    };
    if (!isDoctor) params["DValueAv"] = statusType;

    try {
      invoices.value = await _repo.fetchInvoices(
        isDoctor: isDoctor,
        params: params,
      );
    } catch (e) {
      invoices.clear();
      ToastManager.toast(e.toString());
    } finally {
      isLoading.value = false;
    }
  }

  Future<void> openInvoiceUrl(String url) async {
    if (url.trim().isEmpty) {
      // ToastManager.toast("Invoice Not Generated");
      return;
    }
    final uri = Uri.parse(url);
    final launched = await launchUrl(uri, mode: LaunchMode.externalApplication);
    if (!launched) {
      ToastManager.toast("Unable to open invoice");
    }
  }
}
