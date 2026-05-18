import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/acknowledgement/repository/acknowledgement_repository.dart';

class AcknowledgementCampListController extends GetxController {
  final AcknowledgementRepository _repo = AcknowledgementRepository();

  final RxList<ResourceReMappingCampOutput> campList =
      <ResourceReMappingCampOutput>[].obs;
  final RxList<ResourceReMappingCampOutput> searchList =
      <ResourceReMappingCampOutput>[].obs;
  final RxBool isLoading = false.obs;
  final RxString selectedCampDate = ''.obs;

  final TextEditingController dateController = TextEditingController();
  final TextEditingController searchController = TextEditingController();

  int empCode = 0;

  @override
  void onInit() {
    super.onInit();
    empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedCampDate.value =
        FormatterManager.formatDateToString(DateTime.now());
    dateController.text = selectedCampDate.value;
    fetchCamps();
  }

  @override
  void onClose() {
    dateController.dispose();
    searchController.dispose();
    super.onClose();
  }

  Future<void> fetchCamps() async {
    isLoading.value = true;
    final response = await _repo.getCampList(
      campDate: selectedCampDate.value,
      userId: empCode,
    );
    if (response != null && response.status?.toLowerCase() == 'success') {
      campList.assignAll(response.output ?? []);
      searchList.assignAll(campList);
    } else {
      campList.clear();
      searchList.clear();
      final msg = response?.message;
      if (msg != null && msg.isNotEmpty) ToastManager.toast(msg);
    }
    isLoading.value = false;
  }

  void filterBySearch(String query) {
    if (query.isEmpty) {
      searchList.assignAll(campList);
    } else {
      searchList.assignAll(
        campList.where((item) {
          return item.campId?.toString().contains(query) ?? false;
        }).toList(),
      );
    }
  }

  Future<void> onDateChanged(DateTime picked) async {
    selectedCampDate.value = FormatterManager.formatDateToString(picked);
    dateController.text = selectedCampDate.value;
    searchController.clear();
    filterBySearch('');
    await fetchCamps();
  }
}