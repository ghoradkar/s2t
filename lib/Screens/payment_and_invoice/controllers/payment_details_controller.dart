import 'package:get/get.dart';

import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../models/months_response.dart';
import '../models/years_response.dart';
import '../models/company_list_model.dart';
import '../models/user_invoice_payment_model.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../repository/payment_repository.dart';

class PaymentDetailsController extends GetxController {
  final _repo = PaymentRepository();

  final Rxn<YearsOutput> selectedYear = Rxn<YearsOutput>();
  final Rxn<MonthsOutput> selectedMonth = Rxn<MonthsOutput>();
  final Rxn<CompanyListOutput> selectedCompany = Rxn<CompanyListOutput>();
  final Rxn<UserInvoicePaymentDetailsOutput> paymentDetails =
      Rxn<UserInvoicePaymentDetailsOutput>();

  final RxBool showPaymentInProgressNote = false.obs;
  final RxBool showPaymentReceivedButton = false.obs;
  final RxBool showPaymentNotReceivedButton = false.obs;
  final RxBool showInvoiceLink = false.obs;
  final RxBool isLoading = false.obs;

  late final int empCode;
  late final int desigId;
  late final int subOrgId;
  late final String bMobile;
  late final bool isDoctor;

  int userInvoiceId = 0;
  String otpNumber = "";
  int _lastPaymentStatusId = 0;
  bool _isActive = false;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    desigId = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    bMobile = user?.bMobile ?? "";
    isDoctor = desigId == 34;

    final now = DateTime.now();
    int month = now.month - 1;
    int year = now.year;
    if (month == 0) {
      month = 12;
      year = now.year - 1;
    }
    selectedYear.value = YearsOutput(yearID: year, yearName: "$year");
    selectedMonth.value = MonthsOutput(
      monthId: month,
      monthNameEng: FormatterManager.getMonthNameFromDate(month),
    );
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

  Future<List<MonthsOutput>> fetchMonths() async {
    ToastManager.showLoader();
    try {
      return await _repo.fetchMonths({
        "YearID": selectedYear.value?.yearID?.toString() ?? "0",
      });
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<CompanyListOutput>> fetchCompanyList() async {
    ToastManager.showLoader();
    try {
      return await _repo.fetchCompanyList({
        "UserID": empCode.toString(),
        "Year": selectedYear.value?.yearID?.toString() ?? "0",
        "Month": selectedMonth.value?.monthId?.toString() ?? "0",
      });
    } catch (e) {
      ToastManager.toast(
        "Payment details will be available after Invoice is Generated",
      );
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void updateYear(YearsOutput year) {
    selectedYear.value = year;
    selectedCompany.value = null;
    if (_isActive) fetchPaymentDetails();
  }

  void updateMonth(MonthsOutput month) {
    selectedMonth.value = month;
    selectedCompany.value = null;
    if (_isActive) fetchPaymentDetails();
  }

  void updateCompany(CompanyListOutput company) {
    selectedCompany.value = company;
    if (_isActive) fetchPaymentDetails();
  }

  void activate() {
    _isActive = true;
    fetchPaymentDetails();
  }

  Future<void> fetchPaymentDetails() async {
    if (!_isActive) {
      isLoading.value = false;
      return;
    }
    if (isDoctor && selectedCompany.value == null) {
      _clearPaymentDetails();
      isLoading.value = false;
      return;
    }
    isLoading.value = true;
    final Map<String, String> params = {
      "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
      "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
      "UserID": empCode.toString(),
      "LoginUserID": empCode.toString(),
    };
    if (isDoctor) {
      params["PaidByCompanyID"] =
          selectedCompany.value?.paidByCompanyID?.toString() ?? "0";
    }

    try {
      final details = await _repo.fetchPaymentDetails(
        isDoctor: isDoctor,
        params: params,
      );
      if (details == null) {
        _clearPaymentDetails();
        ToastManager.toast(
          "Payment details will be available after Invoice is Generated",
        );
      } else {
        paymentDetails.value = details;
        userInvoiceId = details.userInviceID ?? 0;
        _applyPaymentStatus(details);
      }
    } catch (e) {
      _clearPaymentDetails();
      ToastManager.toast(
        "Payment details will be available after Invoice is Generated",
      );
    } finally {
      isLoading.value = false;
    }
  }

  void _applyPaymentStatus(UserInvoicePaymentDetailsOutput? details) {
    showInvoiceLink.value = false;
    showPaymentInProgressNote.value = false;
    showPaymentReceivedButton.value = true;
    showPaymentNotReceivedButton.value = true;

    if (details == null) {
      showPaymentReceivedButton.value = false;
      showPaymentNotReceivedButton.value = false;
      return;
    }

    final paymentReceived = details.payementReceivedStatus == 1;
    final paymentNotReceived = details.payementNotReceivedStatus == 1;

    if (paymentReceived) {
      showPaymentReceivedButton.value = false;
      showPaymentNotReceivedButton.value = false;
    } else if (paymentNotReceived) {
      showPaymentNotReceivedButton.value = false;
    }

    final utr = details.uTRNo ?? "";
    final paymentDate = details.paymentDate ?? "";
    if (utr.isEmpty || paymentDate.isEmpty) {
      showPaymentInProgressNote.value = true;
      showPaymentReceivedButton.value = false;
      showPaymentNotReceivedButton.value = false;
    }

    if (isDoctor &&
        details.isPaymentRaised == "1" &&
        (details.invoiceUrl ?? "").trim().isNotEmpty) {
      showInvoiceLink.value = true;
    }
  }

  void _clearPaymentDetails() {
    paymentDetails.value = null;
    showPaymentInProgressNote.value = false;
    showPaymentReceivedButton.value = false;
    showPaymentNotReceivedButton.value = false;
    showInvoiceLink.value = false;
    userInvoiceId = 0;
  }

  Future<String?> requestPaymentOtp(int paymentStatusId) async {
    otpNumber = FormatterManager.generateRandomDigits(5);
    final Map<String, String> params = isDoctor
        ? {
            "UserID": empCode.toString(),
            "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
            "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
            "OTP": otpNumber,
            "CreatedBy": empCode.toString(),
            "MOBNO": bMobile,
            "SubOrgID": subOrgId.toString(),
          }
        : {
            "PaymentStatusID": paymentStatusId.toString(),
            "UserID": empCode.toString(),
            "UserInviceID": userInvoiceId.toString(),
            "OTP": otpNumber,
            "CreatedBy": empCode.toString(),
            "MOBNO": bMobile,
            "SubOrgID": subOrgId.toString(),
          };

    try {
      await _repo.requestOtp(isDoctor: isDoctor, params: params);
      ToastManager.toast("OTP sent successfully on $bMobile");
      return otpNumber;
    } catch (e) {
      ToastManager.toast(e.toString());
      return null;
    }
  }

  Future<void> submitPaymentStatus(int paymentStatusId, String otp) async {
    ToastManager.showLoader();
    _lastPaymentStatusId = paymentStatusId;
    final Map<String, String> params = isDoctor
        ? {
            "PaymentStatusID": paymentStatusId.toString(),
            "UserID": empCode.toString(),
            "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
            "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
            "OTP": otp,
            "VerificationRemarkID": "0",
            "CreatedBy": empCode.toString(),
            "OtherRemark": "",
            "UserInviceID": userInvoiceId.toString(),
          }
        : {
            "PaymentStatusID": paymentStatusId.toString(),
            "UserID": empCode.toString(),
            "UserInviceID": userInvoiceId.toString(),
            "OTP": otp,
            "VerificationRemarkID": "0",
            "OtherRemark": "",
            "CreatedBy": empCode.toString(),
          };

    try {
      await _repo.submitPaymentStatus(isDoctor: isDoctor, params: params);
      ToastManager.hideLoader();
      if (_lastPaymentStatusId == 4) {
        ToastManager.toast("You confirmed as payment received. Thanks!");
      } else if (_lastPaymentStatusId == 5) {
        ToastManager.toast("We will revert back to you. Thanks!");
      } else {
        ToastManager.toast("Status updated successfully");
      }
      fetchPaymentDetails();
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
    }
  }
}
