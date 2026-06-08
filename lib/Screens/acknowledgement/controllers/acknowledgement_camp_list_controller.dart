import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/acknowledgement/repository/acknowledgement_repository.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';

class AcknowledgementCampListController extends GetxController {
  final AcknowledgementRepository _repo = AcknowledgementRepository();
  final bool isD2D;

  AcknowledgementCampListController({this.isD2D = false});

  final RxList<ResourceReMappingCampOutput> campList =
      <ResourceReMappingCampOutput>[].obs;
  final RxList<ResourceReMappingCampOutput> searchList =
      <ResourceReMappingCampOutput>[].obs;
  final RxBool isLoading = false.obs;
  final RxBool isLoadingDist = false.obs;
  final RxString selectedCampDate = ''.obs;

  // District (D2D only)
  final RxList<DistrictOutput> districtList = <DistrictOutput>[].obs;
  final Rxn<DistrictOutput> selectedDistrict = Rxn<DistrictOutput>();

  final TextEditingController dateController = TextEditingController();
  final TextEditingController searchController = TextEditingController();

  int empCode = 0;
  int dESGID = 0;
  int subOrgId = 0;
  int cityCode = 0;
  String userDistLgdCode = '0';
  String divisionId = '0';

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    if (isD2D) {
      dESGID = user?.dESGID ?? 0;
      subOrgId = user?.subOrgId ?? 0;
      cityCode = user?.cityCode ?? 0;
      userDistLgdCode = user?.dISTLGDCODE?.toString() ?? '0';
      divisionId = user?.divid?.toString() ?? '0';
    }
    selectedCampDate.value =
        FormatterManager.formatDateToString(DateTime.now());
    dateController.text = selectedCampDate.value;

    if (isD2D) {
      _loadDistricts();
    } else {
      fetchCamps();
    }
  }

  @override
  void onClose() {
    dateController.dispose();
    searchController.dispose();
    super.onClose();
  }

  Future<void> _loadDistricts() async {
    isLoadingDist.value = true;
    final list = await _repo.getDistrictList(
      empCode: empCode.toString(),
      subOrgId: subOrgId.toString(),
      desgId: dESGID.toString(),
    );
    districtList.assignAll(list);
    if (list.isNotEmpty) {
      selectedDistrict.value = list.firstWhereOrNull(
            (d) => d.distLgdCode == userDistLgdCode,
          ) ??
          list.first;
    }
    isLoadingDist.value = false;
    await fetchCamps();
  }

  void onDistrictSelected(DistrictOutput district) {
    selectedDistrict.value = district;
    searchController.clear();
    filterBySearch('');
    fetchCamps();
  }

  Future<void> fetchCamps() async {
    isLoading.value = true;
    final distCode = isD2D
        ? (selectedDistrict.value?.distLgdCode ?? userDistLgdCode)
        : userDistLgdCode;
    final response = isD2D
        ? await _repo.getD2DCampList(
            campDate: selectedCampDate.value,
            userId: empCode,
            desgId: dESGID,
            subOrgId: subOrgId,
            distLgdCode: distCode,
            cityCode: cityCode,
            divisionId: divisionId,
          )
        : await _repo.getCampList(
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
