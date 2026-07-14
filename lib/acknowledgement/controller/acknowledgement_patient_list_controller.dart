import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:s2toperational/acknowledgement/repository/acknowledgement_repository.dart';

class AcknowledgementPatientListController extends GetxController {
  final AcknowledgementRepository _repo = AcknowledgementRepository();

  final RxList<AcknowledgementPatientOutput> patientList =
      <AcknowledgementPatientOutput>[].obs;
  final RxList<AcknowledgementPatientOutput> searchList =
      <AcknowledgementPatientOutput>[].obs;
  final RxBool isLoading = false.obs;

  final TextEditingController searchController = TextEditingController();

  final int campId;
  final int siteDetailId;
  final String districtName;
  final String testId;

  int empCode = 0;

  AcknowledgementPatientListController({
    required this.campId,
    required this.siteDetailId,
    required this.districtName,
    this.testId = '9',
  });

  @override
  void onInit() {
    super.onInit();
    empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    fetchPatients();
  }

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
  }

  Future<void> fetchPatients() async {
    isLoading.value = true;
    final teamId = await _repo.getTeamId(campId: campId, userId: empCode);
    final response = await _repo.getPatientList(
      campId: campId,
      userId: empCode,
      teamId: teamId,
      testId: testId,
    );
    if (response != null && response.status?.toLowerCase() == 'success') {
      patientList.assignAll(response.output ?? []);
      searchList.assignAll(patientList);
    } else {
      patientList.clear();
      searchList.clear();
      final msg = response?.message;
      if (msg != null && msg.isNotEmpty) ToastManager.toast(msg);
    }
    isLoading.value = false;
  }

  void filterBySearch(String query) {
    if (query.isEmpty) {
      searchList.assignAll(patientList);
    } else {
      searchList.assignAll(
        patientList.where((item) {
          final name = item.englishName?.toLowerCase() ?? '';
          final regdNo = item.regdNo?.toString() ?? '';
          return name.contains(query.toLowerCase()) ||
              regdNo.contains(query);
        }).toList(),
      );
    }
  }
}