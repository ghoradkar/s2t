import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/payment_and_invoice/models/company_list_model.dart';
import 'package:s2toperational/Screens/payment_and_invoice/models/user_invoice_payment_model.dart';

import '../models/months_response.dart';
import '../models/years_response.dart';

class PaymentRepository {
  final APIManager _api = APIManager();

  Future<List<YearsOutput>> fetchYears() {
    final c = Completer<List<YearsOutput>>();
    _api.getYearAPI((YearsResponse? response, String error, bool success) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    });
    return c.future;
  }

  Future<List<MonthsOutput>> fetchMonths(Map<String, String> params) {
    final c = Completer<List<MonthsOutput>>();
    _api.getMonthsAPI(params, (
      MonthsResponse? response,
      String error,
      bool success,
    ) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    });
    return c.future;
  }

  Future<List<CompanyListOutput>> fetchCompanyList(
    Map<String, String> params,
  ) {
    final c = Completer<List<CompanyListOutput>>();
    _api.getPaidByCompanyListAPI(params, (
      CompanyListResponse? response,
      String error,
      bool success,
    ) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    });
    return c.future;
  }

  Future<UserInvoicePaymentDetailsOutput?> fetchPaymentDetails({
    required bool isDoctor,
    required Map<String, String> params,
  }) {
    final c = Completer<UserInvoicePaymentDetailsOutput?>();
    void handle(
      UserInvoicePaymentDetailsResponse? response,
      String error,
      bool success,
    ) {
      if (success) {
        final list = response?.output ?? [];
        c.complete(list.isNotEmpty ? list.first : null);
      } else {
        c.completeError(error);
      }
    }
    isDoctor
        ? _api.getUserDoctorInvoicePaymentDetailsAPI(params, handle)
        : _api.getUserInvoicePaymentDetailsAPI(params, handle);
    return c.future;
  }

  Future<void> requestOtp({
    required bool isDoctor,
    required Map<String, String> params,
  }) {
    final c = Completer<void>();
    void handle(dynamic response, String error, bool success) {
      success ? c.complete() : c.completeError(error);
    }
    isDoctor
        ? _api.getDoctorInvoiceOTPDetailsAPI(params, handle)
        : _api.getOTPForMedicineDeliveryOrgAPI(params, handle);
    return c.future;
  }

  Future<void> submitPaymentStatus({
    required bool isDoctor,
    required Map<String, String> params,
  }) {
    final c = Completer<void>();
    void handle(dynamic response, String error, bool success) {
      success ? c.complete() : c.completeError(error);
    }
    isDoctor
        ? _api.insertDoctorInvoicePaymentStatusAPI(params, handle)
        : _api.insertInvoicePaymentStatusAPI(params, handle);
    return c.future;
  }
}
