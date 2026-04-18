import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class BasicHealthInfoPatientListController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final RxList<UserAttendancesUsingSitedetailsIDOutput> allList =
      <UserAttendancesUsingSitedetailsIDOutput>[].obs;
  final RxList<UserAttendancesUsingSitedetailsIDOutput> filteredList =
      <UserAttendancesUsingSitedetailsIDOutput>[].obs;
  final RxBool isLoading = false.obs;

  final TextEditingController searchController = TextEditingController();

  int _campId = 0;
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

  void loadData({required int campId, int siteDetailId = 0}) {
    _campId = campId;
    _empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    fetchPatients();
  }

  Future<void> fetchPatients() async {
    isLoading.value = true;
    final response = await _repo.getPatientList(
      testId: 2,
      campId: _campId,
      userId: _empCode,
      teamNumber: '0',
      isRegularCamp: DataProvider().getRegularCamp(),
    );
    if (response != null && (response.output?.isNotEmpty ?? false)) {
      allList.assignAll(response.output!);
      filteredList.assignAll(allList);
    } else {
      allList.clear();
      filteredList.clear();
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
}