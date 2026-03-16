import 'dart:async';

import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/Json_Class/MonthWiseInvoiceResponse/MonthWiseInvoiceResponse.dart';
import '../../../Modules/Json_Class/YearsResponse/YearsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';

class InvoiceController extends GetxController {
  final APIManager apiManager = APIManager();

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

  Future<List<YearsOutput>> fetchYears() {
    final completer = Completer<List<YearsOutput>>();
    ToastManager.showLoader();
    apiManager.getYearAPI((
      YearsResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (success) {
        completer.complete(response?.output ?? <YearsOutput>[]);
      } else {
        ToastManager.toast(errorMessage);
        completer.complete(<YearsOutput>[]);
      }
    });
    return completer.future;
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

    if (isDoctor) {
      apiManager.getMonthWiseDoctorInvoiceStatusAPI(
        params,
        _onMonthWiseInvoiceResponse,
      );
    } else {
      params["DValueAv"] = statusType;
      apiManager.getMonthWiseInvoiceStatusAPI(
        params,
        _onMonthWiseInvoiceResponse,
      );
    }
  }

  void _onMonthWiseInvoiceResponse(
    MonthWiseInvoiceResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      invoices.value = response?.output ?? <MonthWiseInvoiceOutput>[];
    } else {
      invoices.clear();
      ToastManager.toast(errorMessage);
    }
    isLoading.value = false;
  }

  Future<void> openInvoiceUrl(String url) async {
    if (url.trim().isEmpty) {
      ToastManager.toast("Invoice Not Generated");
      return;
    }
    final uri = Uri.parse(url);
    final launched = await launchUrl(uri, mode: LaunchMode.externalApplication);
    if (!launched) {
      ToastManager.toast("Unable to open invoice");
    }
  }
}
