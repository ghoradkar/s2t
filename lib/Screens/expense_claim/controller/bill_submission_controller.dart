// ignore_for_file: avoid_print

import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import '../model/advadetails_new_version_v2_response.dart';
import '../repository/expense_claim_repository.dart';

class BillSubmissionController extends GetxController {
  final _repo = ExpenseClaimRepository();

  LoginOutput? userLoginDetails;
  String fromDate = '';
  String toDate = '';
  DateTime? selectedFromDate;
  List<AdvadetailsNewOutput> campExpensesList = [];

  @override
  void onInit() {
    super.onInit();
    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
    fromDate = FormatterManager.formatDateToString(DateTime.now());
    toDate = FormatterManager.formatDateToString(DateTime.now());
    selectedFromDate = DateTime.now();
    loadAdvanceDetails();
  }

  Future<void> loadAdvanceDetails() async {
    ToastManager.showLoader();
    try {
      final params = {
        'FromReqDate': fromDate,
        'ToReqDate': toDate,
        'distlgdcode': userLoginDetails?.dISTLGDCODE?.toString() ?? '0',
        'USERID': userLoginDetails?.empCode?.toString() ?? '0',
      };
      print(params);
      final response = await _repo.fetchAdvanceDetails(params);
      campExpensesList = response.output ?? [];
    } catch (e) {
      campExpensesList = [];
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  void setFromDate(DateTime date) {
    selectedFromDate = date;
    fromDate = FormatterManager.formatDateToString(date);
    toDate = '';
    update();
  }

  Future<void> setToDate(DateTime date) async {
    toDate = FormatterManager.formatDateToString(date);
    update();
    await loadAdvanceDetails();
  }
}
