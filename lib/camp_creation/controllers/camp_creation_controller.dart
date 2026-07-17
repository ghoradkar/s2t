// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../utilities/enums.dart';
import '../../../utilities/formatter_manager.dart';
import '../../../utilities/toast_manager.dart';
import '../../../constants/constants.dart';
import '../../../constants/images.dart';
import '../../../utilities/data_provider.dart';
import '../../../utilities/validators.dart';
import '../../../common_widgets/app_active_button.dart';
import '../../../common_widgets/drop_down_list_screen/drop_down_list_screen.dart';
import '../../../common_widgets/multi_selection_drop_down_list_screen/multi_selection_drop_down_list_screen.dart';
import '../../../camp_creation/models/camp_type_response.dart';
import '../../../camp_creation/models/district_response.dart';
import '../models/home_and_hub_lab_camp_creation_response.dart';
import '../models/initiated_by_response.dart';
import '../../../camp_creation/models/screening_test_camp_creation_response.dart';
import '../../../camp_creation/models/taluka_camp_creation_response.dart';
import '../../../camp_creation/models/landing_lab_camp_creation_response.dart';
import '../repository/camp_creation_repository.dart';

class CampCreationController extends GetxController {
  final CampCreationRepository _repository;

  CampCreationController({CampCreationRepository? repository})
    : _repository = repository ?? CampCreationRepository();

  // ─── Plain fields (loaded once in onInit, never change) ──────────────────────
  int empCode = 0;
  int dESGID = 0;
  bool isRegularCamp = false;

  // ─── Text controllers ─────────────────────────────────────────────────────────
  final TextEditingController campNameController = TextEditingController();
  final TextEditingController campAddressController = TextEditingController();
  final TextEditingController expectedBeneficiaryController =
      TextEditingController();

  // ─── Rx state ─────────────────────────────────────────────────────────────────
  final Rx<CampTypeOutput?> selectedCampType = Rx(null);
  final Rx<InitiatedByOutput?> selectedInitiatedBy = Rx(null);
  final RxInt districtId = 0.obs;
  final RxString districtName = ''.obs;
  final Rx<TalukaCampCreationOutput?> selectedTaluka = Rx(null);
  final Rx<LandingLabCampCreationOutput?> selectedLandingLab = Rx(null);
  final Rx<HomeAndHubLabCampCreationOutput?> selectedHomeAndHubLab = Rx(null);
  final RxList<ScreeningTestCampCreationOutput> selectedScreeningTest =
      <ScreeningTestCampCreationOutput>[].obs;
  final RxString selectedCampDate = ''.obs;
  final RxString selectedPostCampDate = ''.obs;
  final RxBool isManualAddressEntry = false.obs;
  final RxDouble campLatitude = 0.0.obs;
  final RxDouble campLongitude = 0.0.obs;

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    empCode = userData?.empCode ?? 0;
    dESGID = userData?.dESGID ?? 0;
    districtId.value = userData?.dISTLGDCODE ?? 0;
    districtName.value = userData?.district ?? '';
    isRegularCamp = DataProvider().getRegularCamp();
    selectedInitiatedBy.value = InitiatedByOutput(
      iD: 1,
      initiatedBy: 'Internal',
    );
    print(
      '[CampCreation] onInit - empCode: $empCode | dESGID: $dESGID | districtId: ${districtId.value} | districtName: ${districtName.value} | isRegularCamp: $isRegularCamp',
    );
  }

  @override
  void onClose() {
    campNameController.dispose();
    campAddressController.dispose();
    expectedBeneficiaryController.dispose();
    super.onClose();
  }

  // ─── Camp Type ────────────────────────────────────────────────────────────────

  Future<void> loadCampTypes() async {
    print(
      '[CampCreation] loadCampTypes - isRegularCamp: $isRegularCamp | dESGID: $dESGID',
    );
    ToastManager.showLoader();
    CampTypeResponse? response;
    if (isRegularCamp) {
      response = await _repository.fetchCampTypeNonD2D();
    } else if (dESGID == 108) {
      response = await _repository.fetchCampTypeFlexi();
    } else if (dESGID == 136 || dESGID == 139) {
      response = await _repository.fetchCampTypeMMU();
    } else {
      response = await _repository.fetchCampTypeD2D();
    }
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] campType failed or empty');
      ToastManager.toast('Failed to load camp types');
      return;
    }
    print('[CampCreation] campType loaded - count: ${list.length}');
    _showDropDown('Camp Type', list, DropDownTypeMenu.CampType);
  }

  // ─── Initiated By ─────────────────────────────────────────────────────────────

  Future<void> loadInitiatedBy() async {
    print('[CampCreation] loadInitiatedBy');
    ToastManager.showLoader();
    final response = await _repository.fetchInitiatedBy();
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] initiatedBy failed or empty');
      ToastManager.toast('Failed to load initiated by list');
      return;
    }
    print('[CampCreation] initiatedBy loaded - count: ${list.length}');
    _showDropDown('Initiated By', list, DropDownTypeMenu.InitiatedBy);
  }

  // ─── District ─────────────────────────────────────────────────────────────────

  Future<void> loadDistrict() async {
    print('[CampCreation] loadDistrict - empCode: $empCode');
    ToastManager.showLoader();
    final response = await _repository.fetchDistrict(empCode);
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] district failed or empty');
      ToastManager.toast('Failed to load districts');
      return;
    }
    print('[CampCreation] district loaded - count: ${list.length}');
    _showDropDown('District', list, DropDownTypeMenu.District);
  }

  // ─── Taluka ───────────────────────────────────────────────────────────────────

  Future<void> loadTaluka() async {
    print('[CampCreation] loadTaluka - districtId: ${districtId.value}');
    ToastManager.showLoader();
    final response = await _repository.fetchTaluka(districtId.value);
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] taluka failed or empty');
      ToastManager.toast('Failed to load talukas');
      return;
    }
    print('[CampCreation] taluka loaded - count: ${list.length}');
    _showDropDown('Taluka', list, DropDownTypeMenu.TalukaCampList);
  }

  // ─── Landing Lab ──────────────────────────────────────────────────────────────

  Future<void> loadLandingLab() async {
    print('[CampCreation] loadLandingLab - districtId: ${districtId.value}');
    ToastManager.showLoader();
    final response = await _repository.fetchLandingLab(districtId.value);
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] landingLab failed or empty');
      ToastManager.toast('Failed to load landing labs');
      return;
    }
    print('[CampCreation] landingLab loaded - count: ${list.length}');
    _showDropDown('Landing Lab', list, DropDownTypeMenu.LandingLabCampCreation);
  }

  Future<void> _loadHomeAndHubLab() async {
    final labCode = selectedLandingLab.value?.labCode ?? 0;
    print('[CampCreation] loadHomeAndHubLab - labCode: $labCode');
    ToastManager.showLoader();
    final response = await _repository.fetchHomeAndHubLab(labCode);
    ToastManager.hideLoader();

    if (response?.output?.isNotEmpty == true) {
      selectedHomeAndHubLab.value = response!.output!.first;
      print(
        '[CampCreation] homeAndHubLab loaded - homeLab: ${selectedHomeAndHubLab.value?.homeLab} | hubLab: ${selectedHomeAndHubLab.value?.hubLab}',
      );
    } else {
      print('[CampCreation] homeAndHubLab failed or empty');
      ToastManager.toast('Failed to load home/hub lab');
    }
  }

  // ─── Screening Tests ──────────────────────────────────────────────────────────

  Future<void> loadScreeningTests() async {
    print('[CampCreation] loadScreeningTests');
    ToastManager.showLoader();
    final response = await _repository.fetchScreeningTests();
    ToastManager.hideLoader();

    final list = response?.output ?? [];
    if (list.isEmpty) {
      print('[CampCreation] screeningTest failed or empty');
      ToastManager.toast('Failed to load screening tests');
      return;
    }
    print('[CampCreation] screeningTest loaded - count: ${list.length}');
    _showMultiSelectDropDown(
      'Screening Test',
      list,
      DropDownMultipleTypeMenu.ScreeningTest,
    );
  }

  // ─── Selection handlers ───────────────────────────────────────────────────────

  void onCampTypeSelected(CampTypeOutput value) {
    selectedCampType.value = value;
    selectedInitiatedBy.value = InitiatedByOutput(
      iD: 1,
      initiatedBy: 'Internal',
    );
    _resetFromCampType();
    print(
      '[CampCreation] selected campType: ${value.campTypeDescription} (id: ${value.cAMPTYPE})',
    );
  }

  void onInitiatedBySelected(InitiatedByOutput value) {
    selectedInitiatedBy.value = value;
    _resetFromInitiatedBy();
    print(
      '[CampCreation] selected initiatedBy: ${value.initiatedBy} (id: ${value.iD})',
    );
  }

  void onDistrictSelected(DistrictOutput value) {
    districtId.value = value.dISTLGDCODE ?? 0;
    districtName.value = value.dISTNAME ?? '';
    selectedTaluka.value = null;
    selectedLandingLab.value = null;
    selectedHomeAndHubLab.value = null;
    print(
      '[CampCreation] selected district: ${districtName.value} (districtId: ${districtId.value})',
    );
  }

  void onTalukaSelected(TalukaCampCreationOutput value) {
    selectedTaluka.value = value;
    _resetFromTaluka();
    print(
      '[CampCreation] selected taluka: ${value.tALNAME} (code: ${value.tALLGDCODE})',
    );
  }

  void onLandingLabSelected(LandingLabCampCreationOutput value) {
    selectedLandingLab.value = value;
    selectedHomeAndHubLab.value = null;
    selectedScreeningTest.clear();
    campNameController.clear();
    campAddressController.clear();
    selectedCampDate.value = '';
    selectedPostCampDate.value = '';
    expectedBeneficiaryController.clear();
    print(
      '[CampCreation] selected landingLab: ${value.labName} (labCode: ${value.labCode})',
    );
    _loadHomeAndHubLab();
  }

  void onScreeningTestsSelected(List<ScreeningTestCampCreationOutput> value) {
    selectedScreeningTest.assignAll(value);
    print(
      '[CampCreation] selected screeningTests: ${selectedScreeningTest.map((e) => e.testName).join(', ')}',
    );
  }

  // ─── Reset helpers ────────────────────────────────────────────────────────────

  void _resetFromCampType() {
    selectedTaluka.value = null;
    selectedLandingLab.value = null;
    selectedHomeAndHubLab.value = null;
    selectedScreeningTest.clear();
    campNameController.clear();
    campAddressController.clear();
    selectedCampDate.value = '';
    selectedPostCampDate.value = '';
    expectedBeneficiaryController.clear();
  }

  void _resetFromInitiatedBy() {
    selectedTaluka.value = null;
    selectedLandingLab.value = null;
    selectedHomeAndHubLab.value = null;
    selectedScreeningTest.clear();
    campNameController.clear();
    campAddressController.clear();
    selectedCampDate.value = '';
    selectedPostCampDate.value = '';
    expectedBeneficiaryController.clear();
  }

  void _resetFromTaluka() {
    selectedLandingLab.value = null;
    selectedHomeAndHubLab.value = null;
    selectedScreeningTest.clear();
    campNameController.clear();
    campAddressController.clear();
    selectedCampDate.value = '';
    selectedPostCampDate.value = '';
    expectedBeneficiaryController.clear();
  }

  // ─── Camp Date ────────────────────────────────────────────────────────────────

  Future<void> selectCampDate() async {
    final picked = await showDatePicker(
      context: Get.context!,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      selectedCampDate.value = FormatterManager.formatDateToString(picked);
      selectedPostCampDate.value = FormatterManager.formatDateToString(
        picked.add(const Duration(days: 7)),
      );
      print(
        '[CampCreation] selected campDate: ${selectedCampDate.value} | postCampDate: ${selectedPostCampDate.value}',
      );
    }
  }

  // ─── Screening test mapping ───────────────────────────────────────────────────

  String get screeningTestDisplayString =>
      selectedScreeningTest.map((e) => e.testName ?? '').join(',');

  String _buildCampTestMappingJson() {
    final dataList =
        selectedScreeningTest.map((obj) {
          return {
            'CampID': '0',
            'TestID': (obj.testId ?? 0).toString(),
            'IsTestProcess': obj.isSelected ? '1' : '0',
            'IsActive': '1',
            'CreatedBy': empCode.toString(),
          };
        }).toList();

    try {
      final result = jsonEncode(dataList);
      print('[CampCreation] dictionaryToString - campTestMapping: $result');
      return result;
    } catch (e) {
      print('[CampCreation] dictionaryToString - JSON encode failed: $e');
      return '';
    }
  }

  // ─── Validation & Save ────────────────────────────────────────────────────────

  void saveDidPressed() {
    final campName = campNameController.text.trim();
    final campAddress = campAddressController.text.trim();
    final expectedBeneficiary = expectedBeneficiaryController.text.trim();
    print(
      '[CampCreation] saveDidPressed - campName: $campName | campAddress: $campAddress | campDate: ${selectedCampDate.value} | expectedBeneficiary: $expectedBeneficiary',
    );

    if (selectedCampType.value == null) {
      print(
        '[CampCreation] saveDidPressed - validation failed: campType not selected',
      );
      _showAlert('Select Camp Type');
    } else if (selectedInitiatedBy.value == null) {
      print(
        '[CampCreation] saveDidPressed - validation failed: initiatedBy not selected',
      );
      _showAlert('Select Initiated By');
    } else if (districtName.value.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: district not selected',
      );
      _showAlert('Select district');
    } else if (selectedTaluka.value == null) {
      print(
        '[CampCreation] saveDidPressed - validation failed: taluka not selected',
      );
      _showAlert('Select taluka');
    } else if (selectedLandingLab.value == null) {
      print(
        '[CampCreation] saveDidPressed - validation failed: landingLab not selected',
      );
      _showAlert('Select Landing lab');
    } else if (campName.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: campName empty',
      );
      _showAlert('Select camp name');
    } else if (campAddress.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: campAddress empty',
      );
      _showAlert('Select Camp Address');
    } else if (campAddress.length <= 14) {
      print(
        '[CampCreation] saveDidPressed - validation failed: campAddress too short (${campAddress.length} chars)',
      );
      _showAlert(
        'Camp address should be grater than or equal to 15 character length',
      );
    } else if (selectedCampDate.value.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: campDate not selected',
      );
      _showAlert('Select camp date');
    } else if (selectedScreeningTest.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: screeningTest not selected',
      );
      _showAlert('Select screening tests');
    } else if (expectedBeneficiary.isEmpty) {
      print(
        '[CampCreation] saveDidPressed - validation failed: expectedBeneficiary empty',
      );
      _showAlert('Please Enter Expected Beneficiary');
    } else if (UIValidator.isAllZeros(expectedBeneficiary)) {
      print(
        '[CampCreation] saveDidPressed - validation failed: expectedBeneficiary all zeros',
      );
      _showAlert('Please Enter valid Expected Beneficiary');
    } else {
      print(
        '[CampCreation] saveDidPressed - validation passed, proceeding to createCamp',
      );
      _createCamp();
    }
  }

  Future<void> _createCamp() async {
    ToastManager.showLoader();
    final params = {
      'CampName': campNameController.text.trim(),
      'CampDate': selectedCampDate.value,
      'PostCampDate': selectedPostCampDate.value,
      'LABCODE': selectedLandingLab.value?.labCode?.toString() ?? '0',
      'AffilatedHospitalId': '0',
      'Distlgdcode': districtId.value.toString(),
      'UserId': empCode.toString(),
      'CampLocation': campAddressController.text.trim(),
      'CampTestMapping': _buildCampTestMappingJson(),
      'CampType': selectedCampType.value?.cAMPTYPE?.toString() ?? '0',
      'InitiatedBy': selectedInitiatedBy.value?.iD?.toString() ?? '0',
      'LandingLab': selectedLandingLab.value?.labCode?.toString() ?? '0',
      'CscpreapprovedId': '0',
      'TalukaCode': selectedTaluka.value?.tALLGDCODE?.toString() ?? '',
      'ExpBenCount': expectedBeneficiaryController.text.trim(),
      'Lattitude': campLatitude.value.toString(),
      'Longitude': campLongitude.value.toString(),
    };
    print('[CampCreation] createCamp - payload: $params');
    final result = await _repository.createCamp(params);
    ToastManager.hideLoader();

    if (result.success) {
      print('[CampCreation] campCreation success - campID: ${result.campId}');
      _showSuccessDialog(result.campId);
    } else {
      print('[CampCreation] campCreation failed - ${result.error}');
      ToastManager.toast(result.error);
    }
  }

  // ─── Location ─────────────────────────────────────────────────────────────────

  Future<void> searchOnMap() async {
    final confirmed = await _showLocationConfirmDialog();
    if (!confirmed) return;
    _showPlacesSearchBottomSheet();
  }

  Future<void> viewOnMap() async {
    if (campLatitude.value == 0 || campLongitude.value == 0) {
      ToastManager.toast('Please search camp location or use current location');
      return;
    }
    final lat = campLatitude.value;
    final lng = campLongitude.value;
    final geoUri = 'geo:$lat,$lng?q=$lat,$lng(Selected Location)';
    if (Platform.isAndroid) {
      try {
        final intent = AndroidIntent(
          action: 'action_view',
          data: geoUri,
          package: 'com.google.android.apps.maps',
        );
        await intent.launch();
      } catch (_) {
        final fallback = Uri.parse('https://maps.google.com/?q=$lat,$lng');
        if (await canLaunchUrl(fallback)) {
          await launchUrl(fallback, mode: LaunchMode.externalApplication);
        } else {
          ToastManager.toast('Google Maps not installed');
        }
      }
    } else {
      final googleMapsUri = Uri.parse('comgooglemaps://?q=$lat,$lng&zoom=14');

      if (await canLaunchUrl(googleMapsUri)) {
        await launchUrl(googleMapsUri, mode: LaunchMode.externalApplication);
      } else {
        final webUri = Uri.parse(
          'https://www.google.com/maps/search/?api=1&query=$lat,$lng',
        );

        await launchUrl(webUri, mode: LaunchMode.externalApplication);
      }
    }
  }

  void onManualAddressToggled(bool value) {
    isManualAddressEntry.value = value;
    if (value) {
      campAddressController.clear();
      campLatitude.value = 0;
      campLongitude.value = 0;
    }
  }

  Future<void> _fetchPlaceDetails(String placeId) async {
    ToastManager.showLoader();
    final result = await _repository.fetchPlaceDetails(placeId);
    ToastManager.hideLoader();

    if (result != null) {
      print(
        '[CampCreation] placeDetails - address: ${result.address} | lat: ${result.lat} | lng: ${result.lng}',
      );
      campLatitude.value = result.lat;
      campLongitude.value = result.lng;
      campAddressController.text = result.address;
      isManualAddressEntry.value = false;
    } else {
      print('[CampCreation] placeDetails failed');
      ToastManager.toast('Failed to fetch place details');
    }
  }

  // ─── Bottom sheets ────────────────────────────────────────────────────────────

  void _showDropDown(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    Get.bottomSheet(
      Container(
        width: double.infinity,
        height: Get.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: DropDownListScreen(
          titleString: title,
          dropDownList: list,
          dropDownMenu: dropDownType,
          onApplyTap: (selected) {
            if (dropDownType == DropDownTypeMenu.CampType) {
              onCampTypeSelected(selected);
            } else if (dropDownType == DropDownTypeMenu.InitiatedBy) {
              onInitiatedBySelected(selected);
            } else if (dropDownType == DropDownTypeMenu.District) {
              onDistrictSelected(selected);
            } else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
              onTalukaSelected(selected);
            } else if (dropDownType ==
                DropDownTypeMenu.LandingLabCampCreation) {
              onLandingLabSelected(selected);
            }
          },
        ),
      ),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      isScrollControlled: true,
    );
  }

  void _showMultiSelectDropDown(
    String title,
    List<dynamic> list,
    DropDownMultipleTypeMenu dropDownType,
  ) {
    Get.bottomSheet(
      Container(
        width: double.infinity,
        height: Get.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: MultiSelectionDropDownListScreen(
          titleString: title,
          dropDownList: list,
          dropDownMenu: dropDownType,
          preSelectedList:
              dropDownType == DropDownMultipleTypeMenu.ScreeningTest
                  ? selectedScreeningTest.toList()
                  : null,
          onApplyTap: (selected) {
            if (dropDownType == DropDownMultipleTypeMenu.ScreeningTest) {
              onScreeningTestsSelected(
                List<ScreeningTestCampCreationOutput>.from(selected),
              );
            }
          },
        ),
      ),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      isScrollControlled: true,
    );
  }

  void _showPlacesSearchBottomSheet() {
    final searchController = TextEditingController();
    final predictions = <Map<String, dynamic>>[].obs;
    final isSearching = false.obs;

    Get.bottomSheet(
      Obx(() {
        return Container(
          height: Get.height * 0.6,
          color: Colors.white,
          child: Column(
            children: [
              Container(
                margin: const EdgeInsets.only(top: 8),
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
              const SizedBox(height: 12),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 16),
                child: TextField(
                  controller: searchController,
                  autofocus: true,
                  decoration: InputDecoration(
                    hintText: 'Search location...',
                    prefixIcon: const Icon(Icons.search),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                    suffixIcon:
                        isSearching.value
                            ? const Padding(
                              padding: EdgeInsets.all(12),
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                            : null,
                  ),
                  onChanged: (value) async {
                    if (value.length < 3) {
                      predictions.clear();
                      return;
                    }
                    isSearching.value = true;
                    final results = await _repository.autocomplete(value);
                    predictions.assignAll(results);
                    isSearching.value = false;
                  },
                ),
              ),
              const SizedBox(height: 8),
              Expanded(
                child: ListView.separated(
                  itemCount: predictions.length,
                  separatorBuilder: (_, __) => const Divider(height: 1),
                  itemBuilder: (_, index) {
                    final p = predictions[index];
                    final fmt =
                        p['structured_formatting'] as Map<String, dynamic>? ??
                        {};
                    final primary =
                        fmt['main_text'] as String? ??
                        p['description'] as String? ??
                        '';
                    final secondary = fmt['secondary_text'] as String? ?? '';
                    final placeId = p['place_id'] as String? ?? '';
                    return ListTile(
                      leading: const Icon(Icons.location_on_outlined),
                      title: Text(primary),
                      subtitle:
                          secondary.isNotEmpty
                              ? Text(
                                secondary,
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              )
                              : null,
                      onTap: () async {
                        Get.back();
                        await _fetchPlaceDetails(placeId);
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        );
      }),
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
    );
  }

  // ─── Dialogs ──────────────────────────────────────────────────────────────────

  Future<bool> _showLocationConfirmDialog() async {
    final result = await Get.dialog<bool>(
      AlertDialog(
        content: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Image.asset(warning, width: 100.w),
            ),
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Text('कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.'),
            ),
            SizedBox(
              width: 80.w,
              child: AppActiveButton(
                buttontitle: 'OK',
                onTap: () => Get.back(result: true),
              ),
            ),
          ],
        ),
      ),
    );
    return result ?? false;
  }

  void _showManualAddressWarningDialog() {
    Get.dialog(
      AlertDialog(
        content: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Image.asset(warning, width: 100.w),
            ),
            const Padding(
              padding: EdgeInsets.all(8.0),
              child: Text('कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.'),
            ),
            SizedBox(
              width: 80.w,
              child: AppActiveButton(
                buttontitle: 'OK',
                onTap: () => Get.back(),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void onManualAddressCheckboxChanged(bool? val) {
    if (val == true) {
      _showManualAddressWarningDialog();
    }
    onManualAddressToggled(val == true);
  }

  void _showSuccessDialog(String campId) {
    ToastManager().showSuccessOkayDialog(
      context: Get.context!,
      title: 'Success',
      message: 'Camp Created Successfully.\nCamp ID $campId',
      onTap: () {
        Get.back();
        Get.back();
      },
    );
    // Get.dialog(
    //   AlertDialog(
    //     title: const Text('Success'),
    //     content: Text('Camp Created Successfully.\nCamp ID $campId'),
    //     actions: [
    //       TextButton(
    //         onPressed: () {
    //           Get.back();
    //           Get.back();
    //         },
    //         child: const Text('OK'),
    //       ),
    //     ],
    //   ),
    //   barrierDismissible: false,
    // );
  }

  void _showAlert(String message) {
    ToastManager().showAlertMessage(Get.context!, message, Colors.red);
  }
}
