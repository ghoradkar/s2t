import 'package:flutter/material.dart' show TextEditingController;
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class HealthScreeningPatientListController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final RxList<UserAttendancesUsingSitedetailsIDOutput> patientList =
      <UserAttendancesUsingSitedetailsIDOutput>[].obs;
  final RxBool isLoading = false.obs;
  final TextEditingController searchController = TextEditingController();

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
  }

  int _testId = 0;
  int _campId = 0;
  String _teamNumber = '0';
  bool _isRegularCamp = false;
  int _empCode = 0;

  void loadData({
    required int testId,
    required int campId,
    required String teamNumber,
    required bool isRegularCamp,
  }) {
    _testId = testId;
    _campId = campId;
    _teamNumber = teamNumber;
    _isRegularCamp = isRegularCamp;
    _empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (_isRegularCamp) {
      fetchPatients();
    }
  }

  Future<void> fetchPatients() async {
    isLoading.value = true;
    ToastManager.showLoader();

    final response = await _repo.getPatientList(
      testId: _testId,
      campId: _campId,
      userId: _empCode,
      teamNumber: _teamNumber,
      isRegularCamp: _isRegularCamp,
    );

    ToastManager.hideLoader();

    if (response != null) {
      patientList.assignAll(response.output ?? []);
    } else {
      patientList.clear();
      ToastManager.toast('Failed to load patients');
    }
    isLoading.value = false;
  }

  @override
  Future<void> refresh() async {
    if (_isRegularCamp) {
      _teamNumber = '0';
      await fetchPatients();
    }
  }
}