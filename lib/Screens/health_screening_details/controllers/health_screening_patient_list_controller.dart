import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class HealthScreeningPatientListController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final RxList<UserAttendancesUsingSitedetailsIDOutput> allList =
      <UserAttendancesUsingSitedetailsIDOutput>[].obs;
  final RxList<UserAttendancesUsingSitedetailsIDOutput> filteredList =
      <UserAttendancesUsingSitedetailsIDOutput>[].obs;
  final RxBool isLoading = false.obs;

  final TextEditingController searchController = TextEditingController();

  int _testId = 0;
  int _campId = 0;
  String _teamNumber = '0';
  bool _isRegularCamp = false;
  int _empCode = 0;

  @override
  void onInit() {
    super.onInit();
    searchController.addListener(_onSearchChanged);
  }

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
  }

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
    fetchPatients();
  }

  Future<void> fetchPatients() async {
    isLoading.value = true;
    final response = await _repo.getPatientList(
      testId: _testId,
      campId: _campId,
      userId: _empCode,
      teamNumber: _teamNumber,
      isRegularCamp: _isRegularCamp,
    );
    if (response != null && (response.output?.isNotEmpty ?? false)) {
      allList.assignAll(response.output!);
      filteredList.assignAll(allList);
    } else {
      allList.clear();
      filteredList.clear();
      ToastManager.toast('No patients found.');
    }
    isLoading.value = false;
  }

  void _onSearchChanged() {
    final query = searchController.text.trim().toLowerCase();
    if (query.isEmpty) {
      filteredList.assignAll(allList);
    } else {
      filteredList.assignAll(
        allList.where((p) {
          final name = (p.englishName ?? '').toLowerCase();
          final regNo = (p.regdNo?.toString() ?? '').toLowerCase();
          return name.contains(query) || regNo.contains(query);
        }).toList(),
      );
    }
  }

  Future<void> refresh() async => fetchPatients();
}
