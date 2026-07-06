// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import 'package:s2toperational/Screens/expense_claim/model/expense_camp_id_list_v1_response.dart';
import 'package:s2toperational/Screens/expense_claim/model/expense_head_response.dart';
import 'package:s2toperational/Screens/expense_claim/model/sub_expense_heads_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import '../repository/expense_claim_repository.dart';

class UploadBillController extends GetxController {
  final _repo = ExpenseClaimRepository();

  LoginOutput? userLoginDetails;
  String fromDate = '';
  String toDate = '';
  DateTime? selectedFromDate;

  ExpenseHeaOutput? selectedExpenseHead;
  SubExpenseHeadsOutput? selectedSubExpenseHead;
  List<ExpenseCampIDListV1Output> campIDList = [];
  List<File> fileAttachmentList = [];

  final amountOnBillController = TextEditingController();
  final totalAmountController = TextEditingController();

  @override
  void onInit() {
    super.onInit();
    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
    _resetDates();
  }

  @override
  void onClose() {
    amountOnBillController.dispose();
    totalAmountController.dispose();
    super.onClose();
  }

  void _resetDates() {
    selectedFromDate = DateTime.now();
    fromDate = FormatterManager.formatDateToString(DateTime.now());
    toDate = FormatterManager.formatDateToString(DateTime.now());
  }

  void setFromDate(DateTime date) {
    selectedFromDate = date;
    fromDate = FormatterManager.formatDateToString(date);
    toDate = '';
    update();
  }

  void setToDate(DateTime date) {
    toDate = FormatterManager.formatDateToString(date);
    update();
  }

  Future<List<ExpenseHeaOutput>> fetchExpenseHeads() async {
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchExpenseHeads();
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<SubExpenseHeadsOutput>> fetchSubExpenseHeads() async {
    if (selectedExpenseHead == null) {
      ToastManager.toast('Please select Expense Head');
      return [];
    }
    ToastManager.showLoader();
    try {
      final params = {
        'ExpenseHead': selectedExpenseHead?.expenseHead?.toString() ?? '0',
        'OrganisedBy': '1',
        'CampType': '1',
      };
      print(params);
      final response = await _repo.fetchSubExpenseHeads(params);
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<ExpenseCampIDListV1Output>> fetchCampIDList() async {
    if (selectedSubExpenseHead == null) {
      ToastManager.toast('Please select Sub Expense Head');
      return [];
    }
    ToastManager.showLoader();
    try {
      final params = {
        'FromReqDate': fromDate,
        'ToReqDate': toDate,
        'distlgdcode': userLoginDetails?.dISTLGDCODE.toString() ?? '0',
        'UserID': userLoginDetails?.empCode.toString() ?? '0',
        'SubExpenseID': selectedSubExpenseHead?.subExpenseID.toString() ?? '0',
      };
      print(params);
      final response = await _repo.fetchExpenseCampIDList(params);
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void setExpenseHead(ExpenseHeaOutput head) {
    selectedExpenseHead = head;
    selectedSubExpenseHead = null;
    totalAmountController.text = '';
    campIDList = [];
    _resetDates();
    update();
  }

  void setSubExpenseHead(SubExpenseHeadsOutput head) {
    selectedSubExpenseHead = head;
    totalAmountController.text = '';
    campIDList = [];
    _resetDates();
    update();
  }

  void setCampIDList(List<ExpenseCampIDListV1Output> list) {
    campIDList = list;
    double totalSum = 0;
    for (final obj in campIDList) {
      totalSum += obj.expenseAmount ?? 0;
    }
    totalAmountController.text = '$totalSum';
    update();
  }

  void addFile(File file) {
    fileAttachmentList.add(file);
    update();
  }

  void removeFile(int index) {
    fileAttachmentList.removeAt(index);
    update();
  }

  String getCampIDText() {
    if (campIDList.isEmpty) return '';
    return 'Selected Camp ${campIDList.length}';
  }

  String _convertCampIDsToJsonString() {
    final campIDJsonArray = campIDList
        .map((obj) => {'CampID': obj.campid.toString()})
        .toList();
    return jsonEncode(campIDJsonArray);
  }

  bool validations(BuildContext context) {
    final amountBill = amountOnBillController.text.trim();
    final totalAmount = totalAmountController.text.trim();

    if (selectedExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        'Please select Expense Head',
        () => Get.back(),
      );
      return false;
    }
    if (selectedSubExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        'Please select Sub Expense Head',
        () => Get.back(),
      );
      return false;
    }
    if (campIDList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please select Camps',
        () => Get.back(),
      );
      return false;
    }
    if (amountBill.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please Enter Amount',
        () => Get.back(),
      );
      return false;
    }
    if (double.parse(amountBill) != double.parse(totalAmount)) {
      ToastManager.showAlertDialog(
        context,
        'Bill amount is not matching with expenses entered for selected camps. Please verify camp wise entered expenses',
        () => Get.back(),
      );
      return false;
    }
    if (fileAttachmentList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Choose files to upload',
        () => Get.back(),
      );
      return false;
    }
    return true;
  }

  // Returns true on full success
  Future<bool> submitData() async {
    ToastManager.showLoader();
    try {
      final jsonString = _convertCampIDsToJsonString();
      final createdBy = userLoginDetails?.empCode ?? 0;
      final amountBill = amountOnBillController.text.trim();

      final submitParams = {
        'TYPE_MultipleBillCampDetails': jsonString,
        'RequestType': '2',
        'SubExpenseID': selectedSubExpenseHead?.subExpenseID.toString() ?? '0',
        'AmountOnBill': amountBill,
        'CreatedBy': createdBy.toString(),
      };
      final saveResponse = await _repo.insertMultipleCampIDV2(submitParams);

      final uploadParams = {
        'Billid': saveResponse.message ?? '',
        'createdBy': createdBy.toString(),
        'ExpenseHead': selectedExpenseHead?.expenseHead.toString() ?? '',
        'TYPE_MultipleBillCampDetails': jsonString,
        'SubExpenseID': selectedSubExpenseHead?.subExpenseID.toString() ?? '',
      };
      await _repo.uploadSequentially(fileAttachmentList, uploadParams);
      return true;
    } catch (e) {
      ToastManager.toast(e.toString());
      return false;
    } finally {
      ToastManager.hideLoader();
    }
  }
}
