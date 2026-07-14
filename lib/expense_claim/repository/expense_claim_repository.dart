// ignore_for_file: avoid_print

import 'dart:async';
import 'dart:io';

import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/expense_claim/model/bill_submission_response.dart';
import 'package:s2toperational/expense_claim/model/expense_camp_id_list_v1_response.dart';
import 'package:s2toperational/expense_claim/model/expense_head_response.dart';
import 'package:s2toperational/expense_claim/model/sub_expense_heads_response.dart';
import '../model/advadetails_new_version_v2_response.dart';
import '../model/advances_request_details_show_response.dart';

class ExpenseClaimRepository {
  final APIManager _api = APIManager();

  Future<AdvadetailsNewVersionV2Response> fetchAdvanceDetails(
    Map<String, String> params,
  ) {
    final c = Completer<AdvadetailsNewVersionV2Response>();
    _api.getAdvadetailsNewVersionV2API(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<AdvancesRequestDetailsShowResponse> fetchBillDetails(
    Map<String, String> params,
  ) {
    final c = Completer<AdvancesRequestDetailsShowResponse>();
    _api.getBillSubmitdetailsShowAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<ExpenseHeadResponse> fetchExpenseHeads() {
    final c = Completer<ExpenseHeadResponse>();
    _api.getExpenseHeadAPI((response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<SubExpenseHeadsResponse> fetchSubExpenseHeads(
    Map<String, String> params,
  ) {
    final c = Completer<SubExpenseHeadsResponse>();
    _api.getSubExpenseHeadAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<ExpenseCampIDListV1Response> fetchExpenseCampIDList(
    Map<String, String> params,
  ) {
    final c = Completer<ExpenseCampIDListV1Response>();
    _api.getExpenseCampIDListV1API(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<BillSubmissionResponse> saveBillDetails(
    Map<String, String> params,
  ) {
    final c = Completer<BillSubmissionResponse>();
    _api.saveBillDetailsAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<void> uploadBills(
    Map<String, String> params,
    File file,
    String fileType,
  ) {
    final c = Completer<void>();
    _api.uploadBillsAPI(params, file, fileType, (response, error, success) {
      if (success) {
        c.complete();
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<BillSubmissionResponse> insertMultipleCampIDV2(
    Map<String, String> params,
  ) {
    final c = Completer<BillSubmissionResponse>();
    _api.insertMultipleCampIDV2API(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<void> uploadSequentially(
    List<File> files,
    Map<String, String> params,
  ) {
    final c = Completer<void>();
    _api.uploadSequentially(files, params, (response, error, success) {
      if (success) {
        c.complete();
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }
}
