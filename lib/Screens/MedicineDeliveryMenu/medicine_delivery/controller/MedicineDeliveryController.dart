import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/PostCampBeneficiaryListResponse/PostCampBeneficiaryListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/view/MedicineDeliveryAcknowledgementScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';

class MedicineDeliveryController extends GetxController {
  final APIManager apiManager = APIManager();

  final TextEditingController barcodeController = TextEditingController();
  final TextEditingController searchController = TextEditingController();

  final RxString fromDateString = ''.obs;
  final RxString toDateString = ''.obs;

  DateTime? fromDate;
  DateTime? toDate;

  // District
  final RxList<DistrictOutput> districtList = <DistrictOutput>[].obs;
  final Rx<DistrictOutput?> selectedDistrict = Rx<DistrictOutput?>(null);
  final RxString selectedDistrictName = ''.obs;

  // Taluka
  final RxList<UserMappedTalukaOutput> talukaList =
      <UserMappedTalukaOutput>[].obs;
  final Rx<UserMappedTalukaOutput?> selectedTaluka =
      Rx<UserMappedTalukaOutput?>(null);
  final RxString selectedTalukaName = ''.obs;

  // Patient list
  final RxList<PostCampBeneficiaryOutput> patientList =
      <PostCampBeneficiaryOutput>[].obs;
  final RxList<PostCampBeneficiaryOutput> filteredList =
      <PostCampBeneficiaryOutput>[].obs;

  final RxBool isLoading = false.obs;
  final RxBool isBarcodeScan = false.obs;
  String lastBarcodeValue = '';
  String lastSubmittedRegdId = '';


  int dISTLGDCODE = 0;
  int empCode = 0;
  int statelgdCode = 0;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    empCode = user?.empCode ?? 0;
    statelgdCode = user?.sTATELGDCODE ?? 0;
    resetState();
  }

  @override
  void onClose() {
    barcodeController.dispose();
    searchController.dispose();
    super.onClose();
  }

  void resetState() {
    final now = DateTime.now();
    fromDate = now.subtract(const Duration(days: 3));
    toDate = now;
    fromDateString.value = FormatterManager.formatDateToString(fromDate!);
    toDateString.value = FormatterManager.formatDateToString(toDate!);
    selectedDistrict.value = null;
    selectedDistrictName.value = '';
    selectedTaluka.value = null;
    selectedTalukaName.value = '';
    districtList.clear();
    talukaList.clear();
    patientList.clear();
    filteredList.clear();
    barcodeController.clear();
    searchController.clear();
    isBarcodeScan.value = false;
    lastBarcodeValue = '';
    lastSubmittedRegdId = '';
    _fetchDistrict(autoSelect: true);
  }

  // ── Date pickers ─────────────────────────────────────────────────────────────

  Future<void> pickFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: fromDate ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked == null) return;
    fromDate = picked;
    fromDateString.value = FormatterManager.formatDateToString(picked);
    toDate = null;
    toDateString.value = '';
    patientList.clear();
    filteredList.clear();
    barcodeController.clear();
    isBarcodeScan.value = false;
    lastBarcodeValue = '';
  }

  Future<void> pickToDate(BuildContext context) async {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: toDate ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked == null) return;
    toDate = picked;
    toDateString.value = FormatterManager.formatDateToString(picked);
    isBarcodeScan.value = false;
    lastBarcodeValue = '';
    _fetchPatientList();
  }

  // ── District ─────────────────────────────────────────────────────────────────

  void _fetchDistrict({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    final params = {
      "STATELGDCODE": statelgdCode.toString(),
      "USERID": empCode.toString(),
    };
    apiManager.getDistrictByUserIDAPI(params, (
      DistrictResponse? response,
      String errorMessage,
      bool success,
    ) {
      // Tolerate lowercase "success" from server
      final output = response?.output ?? [];
      if (output.isEmpty) {
        if (errorMessage.isNotEmpty) ToastManager.toast(errorMessage);
        return;
      }
      districtList
        ..clear()
        ..addAll(output);

      if (showDropdown && context != null) {
        _showDropDownBottomSheet(
          context,
          "Select District",
          districtList,
          DropDownTypeMenu.District,
          (value) => _onDistrictSelected(value as DistrictOutput, context),
        );
        return;
      }

      if (autoSelect) {
        _onDistrictSelected(districtList.first, null, fetchTalukaAuto: true);
      }
    });
  }

  void _onDistrictSelected(
    DistrictOutput district,
    BuildContext? context, {
    bool fetchTalukaAuto = false,
  }) {
    selectedDistrict.value = district;
    selectedDistrictName.value = district.dISTNAME ?? '';
    dISTLGDCODE = district.dISTLGDCODE ?? dISTLGDCODE;
    // Reset taluka
    selectedTaluka.value = null;
    selectedTalukaName.value = '';
    talukaList.clear();

    // Fetch patient list immediately with TALLGDCODE="0" (all talukas),
    // matching native which fetches on district select without requiring taluka.
    isBarcodeScan.value = false;
    lastBarcodeValue = '';
    _fetchPatientList();

    // Load taluka list in background (auto-select first only on startup)
    _fetchTaluka(autoSelect: fetchTalukaAuto);
  }

  void showDistrictDropdown(BuildContext context) {
    if (districtList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select District",
        districtList,
        DropDownTypeMenu.District,
        (value) => _onDistrictSelected(value as DistrictOutput, context),
      );
      return;
    }
    _fetchDistrict(showDropdown: true, context: context);
  }

  // ── Taluka ───────────────────────────────────────────────────────────────────

  void _fetchTaluka({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    final params = {
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
    };
    apiManager.getUserMappedTalukaAPI(params, (
      UserMappedTalukaResponse? response,
      String errorMessage,
      bool success,
    ) {
      if (!success || response == null) {
        ToastManager.toast(errorMessage);
        return;
      }
      talukaList
        ..clear()
        ..addAll(response.output ?? []);

      if (talukaList.isEmpty) {
        ToastManager.toast("Taluka list not found");
        return;
      }

      if (showDropdown && context != null) {
        _showDropDownBottomSheet(
          context,
          "Select Taluka",
          talukaList,
          DropDownTypeMenu.UserMappedTaluka,
          (value) => _onTalukaSelected(value as UserMappedTalukaOutput),
        );
        return;
      }

      if (autoSelect) {
        _onTalukaSelected(talukaList.first);
      }
    });
  }

  void _onTalukaSelected(UserMappedTalukaOutput taluka) {
    selectedTaluka.value = taluka;
    selectedTalukaName.value = taluka.tALNAME ?? '';
    isBarcodeScan.value = false;
    lastBarcodeValue = '';
    _fetchPatientList();
  }

  void showTalukaDropdown(BuildContext context) {
    if (selectedDistrict.value == null) {
      ToastManager.toast("Please select district first");
      return;
    }
    if (talukaList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select Taluka",
        talukaList,
        DropDownTypeMenu.UserMappedTaluka,
        (value) => _onTalukaSelected(value as UserMappedTalukaOutput),
      );
      return;
    }
    _fetchTaluka(showDropdown: true, context: context);
  }

  // ── Patient list ─────────────────────────────────────────────────────────────

  /// Re-fetches the list using the current filters without resetting any state.
  void refreshList() {
    if (isBarcodeScan.value && lastBarcodeValue.isNotEmpty) {
      _fetchPatientListByBarcode(lastBarcodeValue);
    } else {
      _fetchPatientList();
    }
  }

  void _fetchPatientList() {
    if (fromDateString.value.isEmpty || toDateString.value.isEmpty) return;

    isLoading.value = true;
    apiManager.getMedicineDeliveryChallanList(
      (
        PostCampBeneficiaryListResponse? response,
        String errorMessage,
        bool success,
      ) {
        isLoading.value = false;
        if (!success || response == null) {
          patientList.clear();
          filteredList.clear();
          if (errorMessage.isNotEmpty) ToastManager.toast(errorMessage);
          return;
        }
        final output = response.output ?? [];
        // Debug log: check whether image paths are present in list API response.
        for (final item in output.take(10)) {
          debugPrint(
            'MedicineDelivery list item regdId=${item.regdId} '
            'status=${item.deliveryStatusRemarkId} '
            'beneficiaryPhoto=${item.beneficiaryPhotoPath} '
            'consentForm=${item.consentFormPhotoPath} '
            'deliveryChallan=${item.deliveryChallanPhotoPath}',
          );
        }
        if (lastSubmittedRegdId.isNotEmpty) {
          final match = output.firstWhere(
            (e) => e.regdId?.toString() == lastSubmittedRegdId,
            orElse: () => PostCampBeneficiaryOutput(),
          );
          if (match.regdId != null) {
            debugPrint(
              'MedicineDelivery submitted regdId=$lastSubmittedRegdId '
              'status=${match.deliveryStatusRemarkId} '
              'beneficiaryPhoto=${match.beneficiaryPhotoPath} '
              'consentForm=${match.consentFormPhotoPath} '
              'deliveryChallan=${match.deliveryChallanPhotoPath}',
            );
          } else {
            debugPrint(
              'MedicineDelivery submitted regdId=$lastSubmittedRegdId not found in list response',
            );
          }
        }
        patientList
          ..clear()
          ..addAll(output);
        _applySearch(searchController.text);
      },
      "3",
      fromDateString.value,
      toDateString.value,
      dISTLGDCODE.toString(),
      selectedTaluka.value?.tALLGDCODE?.toString() ?? "0",
      "0",
      "0",
      empCode.toString(),
    );
  }

  void _fetchPatientListByBarcode(String dc) {
    if (dc.isEmpty) return;
    isLoading.value = true;
    final params = {"Dc_invoice_no": dc, "UserID": empCode.toString()};
    apiManager.getMedicineDeliveryByBarcodeAPI(params, (
      PostCampBeneficiaryListResponse? response,
      String errorMessage,
      bool success,
    ) {
      isLoading.value = false;
      if (!success || response == null || (response.output ?? []).isEmpty) {
        patientList.clear();
        filteredList.clear();
        if (errorMessage.isNotEmpty) ToastManager.toast(errorMessage);
        return;
      }
      final output = response.output!;
      patientList
        ..clear()
        ..addAll(output);
      _applySearch(searchController.text);
    });
  }

  // ── Search ───────────────────────────────────────────────────────────────────

  void onSearchChanged(String query) {
    _applySearch(query);
  }

  void _applySearch(String query) {
    if (query.trim().isEmpty) {
      filteredList
        ..clear()
        ..addAll(patientList);
    } else {
      final lower = query.toLowerCase();
      filteredList
        ..clear()
        ..addAll(
          patientList.where(
            (p) =>
                (p.patientName ?? '').toLowerCase().startsWith(lower),
          ),
        );
      if (filteredList.isEmpty) {
        ToastManager.toast("No Such Customer Name Found");
      }
    }
  }

  // ── Barcode ──────────────────────────────────────────────────────────────────

  void submitBarcode(BuildContext context, {String? scannedValue}) {
    final dc = (scannedValue ?? barcodeController.text).trim();
    if (dc.isEmpty) {
      ToastManager.toast("Please enter delivery challan number");
      return;
    }
    if (dc == "0") {
      ToastManager.showAlertDialog(
        context,
        "Delivery Challan No: Zero, can not be accepted.\nPlease use checkbox for packet pick up.",
        () => Get.back(),
      );
      barcodeController.clear();
      return;
    }

    isLoading.value = true;
    final params = {"Dc_invoice_no": dc, "UserID": empCode.toString()};
    apiManager.getMedicineDeliveryByBarcodeAPI(params, (
      PostCampBeneficiaryListResponse? response,
      String errorMessage,
      bool success,
    ) {
      isLoading.value = false;
      if (!success || response == null || (response.output ?? []).isEmpty) {
        isBarcodeScan.value = false;
        lastBarcodeValue = '';
        ToastManager.showAlertDialog(
          context,
          "Delivery challan number not found",
          () => Get.back(),
        );
        patientList.clear();
        filteredList.clear();
        return;
      }

      isBarcodeScan.value = true;
      lastBarcodeValue = dc;
      final output = response.output!;

      final first = output.first;
      if ((first.delivarystatus ?? '').toUpperCase() == "N") {
        Get.to(
          () => MedicineDeliveryAcknowledgementScreen(beneficiary: first),
        )?.then((submitted) {
          if (submitted == true) {
            lastSubmittedRegdId = first.regdId?.toString() ?? '';
            refreshList();
          }
        });
        return;
      }

      patientList
        ..clear()
        ..addAll(output);
      _applySearch(searchController.text);
    });
  }

  Future<void> openBarcodeScanner(BuildContext context) async {
    String? res = await SimpleBarcodeScanner.scanBarcode(
      context,
      barcodeAppBar: const BarcodeAppBar(
        appBarTitle: 'Scan Bar Code',
        centerTitle: false,
        enableBackButton: false,
        backButtonIcon: Icon(Icons.arrow_back_ios),
      ),
      isShowFlashIcon: false,
      delayMillis: 2000,
      cameraFace: CameraFace.back,
    );
    if (res == null || res == '-1') return;
    ToastManager.toast(res);
    barcodeController.text = res;
    submitBarcode(context, scannedValue: res);
  }

  // ── Helpers ───────────────────────────────────────────────────────────────────

  void _showDropDownBottomSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    Function(dynamic) onApply,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
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
            onApplyTap: (p0) {
              onApply(p0);
            },
          ),
        );
      },
    );
  }
}
