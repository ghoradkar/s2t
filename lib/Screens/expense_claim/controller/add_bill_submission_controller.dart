// ignore_for_file: avoid_print

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/ExpenseHeadResponse/ExpenseHeadResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SubExpenseHeadsResponse/SubExpenseHeadsResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import '../model/advadetails_new_version_v2_response.dart';
import '../repository/expense_claim_repository.dart';

class AddBillSubmissionController extends GetxController {
  final _repo = ExpenseClaimRepository();

  AdvadetailsNewOutput? advanceDetails;
  LoginOutput? userLoginDetails;

  ExpenseHeaOutput? selectedExpenseHead;
  SubExpenseHeadsOutput? selectedSubExpenseHead;

  final noOfUnitController = TextEditingController();
  final amountUnitController = TextEditingController();
  final totalController = TextEditingController();
  final remarkController = TextEditingController();

  int organizedById = 0;
  String organizedByName = '';
  bool isShowOrganizedBy = true;
  String registeredWorkers = '';
  String advanceApprovedAmount = 'Rs. 0.0';
  bool showPhotoUpload = false;

  File? selectedFile;
  String? fileType;

  @override
  void onInit() {
    super.onInit();
    advanceDetails = Get.arguments as AdvadetailsNewOutput?;
    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
    organizedById = advanceDetails?.initiatedBy ?? 0;
    _resolveOrganizedBy();
  }

  @override
  void onClose() {
    noOfUnitController.dispose();
    amountUnitController.dispose();
    totalController.dispose();
    remarkController.dispose();
    super.onClose();
  }

  void _resolveOrganizedBy() {
    const names = {
      1: 'Internal Team',
      2: 'Government',
      3: 'NGO',
      4: 'Labor Contractor',
      5: 'Kit Vendor',
      6: 'ACL Office',
      7: 'vendor/union Leader',
      8: 'Flexi camp',
      9: 'GramPanchyat',
      10: 'Self-organized',
    };
    if (names.containsKey(organizedById)) {
      organizedByName = names[organizedById]!;
      isShowOrganizedBy = true;
    } else {
      isShowOrganizedBy = false;
    }
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

  void setExpenseHead(ExpenseHeaOutput head) {
    selectedExpenseHead = head;
    selectedSubExpenseHead = null;
    _resetFormFields();
    addDataOnUI();
    update();
  }

  void setSubExpenseHead(SubExpenseHeadsOutput head) {
    selectedSubExpenseHead = head;
    _resetFormFields();
    addDataOnUI();
    update();
  }

  void _resetFormFields() {
    noOfUnitController.text = '';
    amountUnitController.text = '';
    totalController.text = '';
    remarkController.text = '';
    registeredWorkers = '';
    advanceApprovedAmount = 'Rs. 0.0';
  }

  void addDataOnUI() {
    double maxAllowedAmt = selectedSubExpenseHead?.maxAllowedAmt ?? 0;
    print(maxAllowedAmt);

    int subExpID = selectedSubExpenseHead?.subExpenseID ?? 0;

    final d = advanceDetails;
    if (d == null) return;

    double approvedAmt = 0;
    switch (subExpID) {
      case 1:
        approvedAmt = d.beneficiaryRefreshment ?? 0;
        break;
      case 2:
        approvedAmt = d.campAwarenessUsingBhopu ?? 0;
        break;
      case 3:
        approvedAmt = d.campHallGramPanchayatSchoolGovtOfficeTent ?? 0;
        break;
      case 4:
        approvedAmt = d.chairs ?? 0;
        break;
      case 5:
        approvedAmt = d.cleaningCharges ?? 0;
        break;
      case 6:
        approvedAmt = d.drinkingWater ?? 0;
        break;
      case 7:
        approvedAmt = d.foodToStaffTAAllowance ?? 0;
        break;
      case 8:
        approvedAmt = d.sampleMovementToLabRunnerBoy ?? 0;
        break;
      case 9:
        approvedAmt = d.sampleMovementToLabTSRTCOrAnyOtherCargo ?? 0;
        break;
      case 10:
        approvedAmt = d.transportationOfStaffTAGroupOfTransport ?? 0;
        break;
      case 11:
        approvedAmt = d.transportationOfStaffTAIndividual ?? 0;
        break;
      default:
        approvedAmt = 0;
    }

    advanceApprovedAmount = 'Rs. $approvedAmt';
    registeredWorkers = d.registeredbeneficiarycount?.toString() ?? '';
    showPhotoUpload = (selectedSubExpenseHead?.isBillRequired ?? '') == 'Yes';
    update();
  }

  void setFile(File file, String type) {
    selectedFile = file;
    fileType = type;
    update();
  }

  bool validations(BuildContext context) {
    final noOfUnit = noOfUnitController.text.trim();
    final amountUnit = amountUnitController.text.trim();
    final total = totalController.text.trim();
    final remark = remarkController.text.trim();

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
    if (noOfUnit.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please enter No Of Unit',
        () => Get.back(),
      );
      return false;
    }
    if (amountUnit.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please enter Amount Per Unit',
        () => Get.back(),
      );
      return false;
    }
    if (total.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please enter Total Amount',
        () => Get.back(),
      );
      return false;
    }
    if (remark.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please enter remark',
        () => Get.back(),
      );
      return false;
    }
    if (showPhotoUpload && selectedFile == null) {
      ToastManager.showAlertDialog(
        context,
        'Choose files to upload',
        () => Get.back(),
      );
      return false;
    }
    return true;
  }

  // Returns true on full success (bill saved + optional upload done)
  Future<bool> submitBill() async {
    ToastManager.showLoader();
    try {
      final params = {
        'Campid': advanceDetails?.campid?.toString() ?? '',
        'SubExpenseID': selectedSubExpenseHead?.subExpenseID.toString() ?? '0',
        'ExpenseAmount': totalController.text.trim(),
        'CreatedBy': userLoginDetails?.empCode.toString() ?? '',
        'ProcessID': '1',
        'ApprovalStatus': '0',
        'RequestType': '2',
        'expeDescription': remarkController.text.trim(),
        'NoOfItems': noOfUnitController.text.trim(),
        'PerItemPrice': amountUnitController.text.trim(),
        'OrganisedBy': organizedById.toString(),
      };
      final saveResponse = await _repo.saveBillDetails(params);

      if (showPhotoUpload) {
        final uploadParams = {
          'Billid': saveResponse.message ?? '',
          'createdBy': userLoginDetails?.empCode.toString() ?? '',
          'ExpenseHead': selectedExpenseHead?.expenseHead.toString() ?? '',
        };
        await _repo.uploadBills(uploadParams, selectedFile!, fileType!);
      }
      return true;
    } catch (e) {
      ToastManager.toast(e.toString());
      return false;
    } finally {
      ToastManager.hideLoader();
    }
  }
}
