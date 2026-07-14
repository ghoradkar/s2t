// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/assignment_remarks_response.dart';
import 'package:s2toperational/ct_assignment/model/department_type_response.dart';
import 'package:s2toperational/camp_creation/models/district_response.dart';
import 'package:s2toperational/camp_creation/models/taluka_camp_creation_response.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../model/t2t_ct_beneficiary_details_response.dart';
import '../repository/ct_assignment_repository.dart';

class CTAssignmentController extends GetxController {
  final _repo = CTAssignmentRepository();

  String fromDateString = '';
  String toDateString = '';
  DistrictOutput? selectedDistrict;
  TalukaCampCreationOutput? selectedTaluka;
  String pinCode = '';
  AssignmentRemarksOutput? selectedStatusRemark;
  DepartmentTypeOutput? selectedDeptType;
  int empCode = 0;

  List<T2TCTBeneficiaryDetailsOutput> list = [];
  List<T2TCTBeneficiaryDetailsOutput> searchList = [];
  final TextEditingController searchController = TextEditingController();

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;

    final toDate = DateTime.now();
    final fromDate = toDate.subtract(const Duration(days: 3));
    fromDateString = FormatterManager.formatDateToString(fromDate);
    toDateString = FormatterManager.formatDateToString(toDate);

    selectedStatusRemark = AssignmentRemarksOutput(
      arId: 2,
      assignmentRemarks: 'Assignment Pending',
    );
    selectedDistrict = DistrictOutput(
      dISTLGDCODE: user?.dISTLGDCODE ?? 0,
      dISTNAME: user?.district ?? '',
    );

    fetchBeneficiaryList();
  }

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
  }

  Future<void> fetchBeneficiaryList() async {
    ToastManager.showLoader();
    try {
      final params = {
        'FROMDATE': fromDateString,
        'TODATE': toDateString,
        'USERID': '$empCode',
        'DISTLGDCODE': selectedDistrict?.dISTLGDCODE.toString() ?? '0',
        'TALLGDCODE': selectedTaluka?.tALLGDCODE.toString() ?? '0',
        'PINCODE': pinCode.isEmpty ? '0' : pinCode,
        'TYPE': selectedStatusRemark?.arId.toString() ?? '0',
        'DeptTypeId': selectedDeptType?.deptTypeId.toString() ?? '0',
      };
      final response = await _repo.fetchBeneficiaryList(params);
      list = response.output ?? [];
      searchList = list;
      searchController.clear();
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  void searchBeneficiaries(String query) {
    if (query.isEmpty) {
      searchList = list;
    } else {
      final lowerQuery = query.toLowerCase();
      searchList = list.where((item) {
        final name = item.beneficiaryName?.toLowerCase() ?? '';
        final pin = item.pinCode?.toLowerCase() ?? '';
        return name.contains(lowerQuery) || pin.contains(lowerQuery);
      }).toList();
    }
    update();
  }
}
