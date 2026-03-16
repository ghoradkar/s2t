import 'dart:async';

import 'package:get/get.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/CompanyListResponse/CompanyListResponse.dart';
import '../../../Modules/Json_Class/MonthsResponse/MonthsResponse.dart';
import '../../../Modules/Json_Class/UserInvoicePaymentDetailsResponse/UserInvoicePaymentDetailsResponse.dart';
import '../../../Modules/Json_Class/YearsResponse/YearsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';

class PaymentDetailsController extends GetxController {
  final APIManager apiManager = APIManager();

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

  Future<List<MonthsOutput>> fetchMonths() {
    final completer = Completer<List<MonthsOutput>>();
    ToastManager.showLoader();
    Map<String, String> params = {
      "YearID": selectedYear.value?.yearID?.toString() ?? "0",
    };

    apiManager.getMonthsAPI(params, (
      MonthsResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (success) {
        completer.complete(response?.output ?? <MonthsOutput>[]);
      } else {
        ToastManager.toast(errorMessage);
        completer.complete(<MonthsOutput>[]);
      }
    });
    return completer.future;
  }

  Future<List<CompanyListOutput>> fetchCompanyList() {
    final completer = Completer<List<CompanyListOutput>>();
    ToastManager.showLoader();
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "Year": selectedYear.value?.yearID?.toString() ?? "0",
      "Month": selectedMonth.value?.monthId?.toString() ?? "0",
    };

    apiManager.getPaidByCompanyListAPI(params, (
      CompanyListResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (success) {
        completer.complete(response?.output ?? <CompanyListOutput>[]);
      } else {
        ToastManager.toast(
          "Payment details will be available after Invoice is Generated",
        );
        completer.complete(<CompanyListOutput>[]);
      }
    });
    return completer.future;
  }

  void updateYear(YearsOutput year) {
    selectedYear.value = year;
    selectedCompany.value = null;
    if (_isActive) {
      fetchPaymentDetails();
    }
  }

  void updateMonth(MonthsOutput month) {
    selectedMonth.value = month;
    selectedCompany.value = null;
    if (_isActive) {
      fetchPaymentDetails();
    }
  }

  void updateCompany(CompanyListOutput company) {
    selectedCompany.value = company;
    if (_isActive) {
      fetchPaymentDetails();
    }
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
    Map<String, String> params = {
      "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
      "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
      "UserID": empCode.toString(),
      "LoginUserID": empCode.toString(),
    };
    if (isDoctor) {
      params["PaidByCompanyID"] =
          selectedCompany.value?.paidByCompanyID?.toString() ?? "0";
      apiManager.getUserDoctorInvoicePaymentDetailsAPI(
        params,
        _onPaymentDetailsResponse,
      );
    } else {
      apiManager.getUserInvoicePaymentDetailsAPI(
        params,
        _onPaymentDetailsResponse,
      );
    }
  }

  void _onPaymentDetailsResponse(
    UserInvoicePaymentDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      final list = response?.output ?? <UserInvoicePaymentDetailsOutput>[];
      if (list.isEmpty) {
        _clearPaymentDetails();
        ToastManager.toast(
          "Payment details will be available after Invoice is Generated",
        );
        isLoading.value = false;
        return;
      }
      final details = list.first;
      paymentDetails.value = details;
      userInvoiceId = details.userInviceID ?? 0;
      _applyPaymentStatus(details);
    } else {
      _clearPaymentDetails();
      ToastManager.toast(
        "Payment details will be available after Invoice is Generated",
      );
    }
    isLoading.value = false;
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

  Future<String?> requestPaymentOtp(int paymentStatusId) {
    final completer = Completer<String?>();
    otpNumber = FormatterManager.generateRandomDigits(5);
    if (isDoctor) {
      Map<String, String> param = {
        "UserID": empCode.toString(),
        "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
        "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
        "OTP": otpNumber,
        "CreatedBy": empCode.toString(),
        // "MOBNO": "9673974373",
        // "MOBNO": "8830378568",
        "MOBNO": bMobile,
        "SubOrgID": subOrgId.toString(),
      };
      apiManager.getDoctorInvoiceOTPDetailsAPI(param, (
        dynamic response,
        String errorMessage,
        bool success,
      ) {
        if (success) {
          ToastManager.toast("OTP sent successfully on $bMobile");
          completer.complete(otpNumber);
        } else {
          ToastManager.toast(errorMessage);
          completer.complete(null);
        }
      });
    } else {
      Map<String, String> param = {
        "PaymentStatusID": paymentStatusId.toString(),
        "UserID": empCode.toString(),
        "UserInviceID": userInvoiceId.toString(),
        "OTP": otpNumber,
        "CreatedBy": empCode.toString(),
        "MOBNO": bMobile,
        // "MOBNO": "9673974373",
        // "MOBNO": "8830378568",
        "SubOrgID": subOrgId.toString(),
      };
      apiManager.getOTPForMedicineDeliveryOrgAPI(param, (
        dynamic response,
        String errorMessage,
        bool success,
      ) {
        if (success) {
          ToastManager.toast("OTP sent successfully on $bMobile");
          completer.complete(otpNumber);
        } else {
          ToastManager.toast(errorMessage);
          completer.complete(null);
        }
      });
    }
    return completer.future;
  }

  Future<void> submitPaymentStatus(int paymentStatusId, String otp) async {
    ToastManager.showLoader();
    _lastPaymentStatusId = paymentStatusId;
    if (isDoctor) {
      Map<String, String> param = {
        "PaymentStatusID": paymentStatusId.toString(),
        "UserID": empCode.toString(),
        "InvoiceYear": selectedYear.value?.yearID?.toString() ?? "0",
        "InvoiceMonth": selectedMonth.value?.monthId?.toString() ?? "0",
        "OTP": otp,
        "VerificationRemarkID": "0",
        "CreatedBy": empCode.toString(),
        "OtherRemark": "",
        "UserInviceID": userInvoiceId.toString(),
      };
      apiManager.insertDoctorInvoicePaymentStatusAPI(
        param,
        _onInsertPaymentStatus,
      );
    } else {
      Map<String, String> param = {
        "PaymentStatusID": paymentStatusId.toString(),
        "UserID": empCode.toString(),
        "UserInviceID": userInvoiceId.toString(),
        "OTP": otp,
        "VerificationRemarkID": "0",
        "OtherRemark": "",
        "CreatedBy": empCode.toString(),
      };
      apiManager.insertInvoicePaymentStatusAPI(
        param,
        _onInsertPaymentStatus,
      );
    }
  }

  void _onInsertPaymentStatus(
    dynamic response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      if (_lastPaymentStatusId == 4) {
        ToastManager.toast("You confirmed as payment received. Thanks!");
      } else if (_lastPaymentStatusId == 5) {
        ToastManager.toast("We will revert back to you. Thanks!");
      } else {
        ToastManager.toast("Status updated successfully");
      }
      fetchPaymentDetails();
    } else {
      ToastManager.toast(errorMessage);
    }
  }
}
