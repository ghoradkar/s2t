import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/payment_and_invoice/models/camp_wise_invoice_model.dart';
import 'package:s2toperational/Screens/payment_and_invoice/models/month_wise_invoice_model.dart';
import 'package:s2toperational/Screens/payment_and_invoice/models/verification_remark_model.dart';

import '../models/years_response.dart';

class InvoiceRepository {
  final APIManager _api = APIManager();

  Future<List<YearsOutput>> fetchYears() {
    final c = Completer<List<YearsOutput>>();
    _api.getYearAPI((YearsResponse? response, String error, bool success) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    });
    return c.future;
  }

  Future<List<MonthWiseInvoiceOutput>> fetchInvoices({
    required bool isDoctor,
    required Map<String, String> params,
  }) {
    final c = Completer<List<MonthWiseInvoiceOutput>>();
    void handle(MonthWiseInvoiceResponse? response, String error, bool success) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    }
    isDoctor
        ? _api.getMonthWiseDoctorInvoiceStatusAPI(params, handle)
        : _api.getMonthWiseInvoiceStatusAPI(params, handle);
    return c.future;
  }

  Future<List<CampWiseInvoiceDetailsOutput>> fetchCampDetails({
    required bool isDoctor,
    required Map<String, String> params,
  }) {
    final c = Completer<List<CampWiseInvoiceDetailsOutput>>();
    void handle(
      CampWiseInvoiceDetailsResponse? response,
      String error,
      bool success,
    ) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    }
    isDoctor
        ? _api.getCampWiseDoctorInvoiceDetailsAPI(params, handle)
        : _api.getCampWiseInvoiceDetailsAPI(params, handle);
    return c.future;
  }

  Future<List<VerificationRemarkOutput>> fetchVerificationRemarks() {
    final c = Completer<List<VerificationRemarkOutput>>();
    _api.getPaymentVerificationRemarkAPI((
      VerificationRemarkResponse? response,
      String error,
      bool success,
    ) {
      success ? c.complete(response?.output ?? []) : c.completeError(error);
    });
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

  Future<void> submitInvoiceStatus({
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
