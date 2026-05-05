import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';
import 'package:s2toperational/Modules/Json_Class/PacketAcceptDataResponse/PacketAcceptDataResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';

class PickUpMedicinePacketController extends GetxController {
  final APIManager apiManager = APIManager();

  final TextEditingController barcodeController = TextEditingController();

  final RxString fromDateString = ''.obs;
  final RxString toDateString = ''.obs;

  DateTime? fromDate;
  DateTime? toDate;

  // Unified display name shown in the Taluka field
  final RxString selectedDropdownName = ''.obs;

  // Lab mode (default – designation other than 35 / 64 / 86)
  final Rx<LabByUserIDOutput?> selectedLab = Rx<LabByUserIDOutput?>(null);
  final RxList<LabByUserIDOutput> labList = <LabByUserIDOutput>[].obs;

  // Taluka mode (designation 35 / 64 / 86 / 157 – matches native behaviour)
  final Rx<UserMappedTalukaOutput?> selectedTaluka =
      Rx<UserMappedTalukaOutput?>(null);
  final RxList<UserMappedTalukaOutput> talukaList =
      <UserMappedTalukaOutput>[].obs;

  final RxList<PacketAcceptDataOutput> packetList =
      <PacketAcceptDataOutput>[].obs;

  int dISTLGDCODE = 0;
  int empCode = 0;
  int dESGID = 0;

  // true when the dropdown should show talukas instead of labs
  bool get isTalukaMode =>
      dESGID == 35 || dESGID == 64 || dESGID == 86 || dESGID == 157;

  final RxInt selectedCount = 0.obs;
  final RxBool isAllSelected = false.obs;
  final RxBool isLoading = false.obs;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    empCode = user?.empCode ?? 0;
    dESGID = user?.dESGID ?? 0;
    resetState();
  }

  @override
  void onClose() {
    barcodeController.dispose();
    super.onClose();
  }

  void resetState() {
    final now = DateTime.now();
    fromDate = now.subtract(const Duration(days: 3));
    toDate = now;
    fromDateString.value = FormatterManager.formatDateToString(fromDate!);
    toDateString.value = FormatterManager.formatDateToString(toDate!);
    selectedLab.value = null;
    selectedTaluka.value = null;
    selectedDropdownName.value = '';
    labList.clear();
    talukaList.clear();
    packetList.clear();
    selectedCount.value = 0;
    isAllSelected.value = false;
    barcodeController.clear();
    _fetchDropdownList(autoSelect: true);
  }

  Future<void> pickFromDate(BuildContext context) async {
    final DateTime initial = fromDate ?? DateTime.now();
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: initial,
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked == null) return;

    fromDate = picked;
    fromDateString.value = FormatterManager.formatDateToString(picked);
    toDate = null;
    toDateString.value = '';
    packetList.clear();
    selectedCount.value = 0;
    isAllSelected.value = false;
    barcodeController.clear();
  }

  Future<void> pickToDate(BuildContext context) async {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    final DateTime initial = toDate ?? DateTime.now();
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: initial,
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked == null) return;

    toDate = picked;
    toDateString.value = FormatterManager.formatDateToString(picked);
    fetchPacketList();
  }

  // ── Dropdown loading ────────────────────────────────────────────────────────

  void _fetchDropdownList({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    if (isTalukaMode) {
      _fetchTalukaList(
        autoSelect: autoSelect,
        showDropdown: showDropdown,
        context: context,
      );
    } else {
      _fetchLabList(
        autoSelect: autoSelect,
        showDropdown: showDropdown,
        context: context,
      );
    }
  }

  /// DESGID 35 / 64 / 86 — talukas from GetUserMappedTaluka using user's district
  void _fetchTalukaList({
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
        ..addAll(response.output ?? <UserMappedTalukaOutput>[]);

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
          (value) {
            _onTalukaSelected(value as UserMappedTalukaOutput);
          },
        );
        return;
      }

      if (autoSelect) {
        _onTalukaSelected(talukaList.first);
      }
    });
  }

  /// Other designations — labs from GetLabByUserID
  void _fetchLabList({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    final params = {"DISTLGDCODE": "0", "USERID": empCode.toString()};
    apiManager.getTalukaPacketReciveAPI(params, (
      LabByUserIDResponse? response,
      String errorMessage,
      bool success,
    ) {
      if (!success || response == null) {
        ToastManager.toast(errorMessage);
        return;
      }
      labList
        ..clear()
        ..addAll(response.output ?? <LabByUserIDOutput>[]);

      if (labList.isEmpty) {
        ToastManager.toast("Lab list not found");
        return;
      }

      if (showDropdown && context != null) {
        _showDropDownBottomSheet(
          context,
          "Select Lab",
          labList,
          DropDownTypeMenu.TalukaPacketRecive,
          (value) {
            _onLabSelected(value as LabByUserIDOutput);
          },
        );
        return;
      }

      if (autoSelect) {
        _onLabSelected(labList.first);
      }
    });
  }

  void _onTalukaSelected(UserMappedTalukaOutput taluka) {
    selectedTaluka.value = taluka;
    selectedDropdownName.value = taluka.tALNAME ?? '';
    fetchPacketList();
  }

  void _onLabSelected(LabByUserIDOutput lab) {
    selectedLab.value = lab;
    selectedDropdownName.value = lab.labName ?? '';
    fetchPacketList();
  }

  void showDropdown(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.toast("Please select to date");
      return;
    }

    final bool listLoaded =
        isTalukaMode ? talukaList.isNotEmpty : labList.isNotEmpty;

    if (listLoaded) {
      if (isTalukaMode) {
        _showDropDownBottomSheet(
          context,
          "Select Taluka",
          talukaList,
          DropDownTypeMenu.UserMappedTaluka,
          (value) => _onTalukaSelected(value as UserMappedTalukaOutput),
        );
      } else {
        _showDropDownBottomSheet(
          context,
          "Select Lab",
          labList,
          DropDownTypeMenu.TalukaPacketRecive,
          (value) => _onLabSelected(value as LabByUserIDOutput),
        );
      }
      return;
    }

    _fetchDropdownList(showDropdown: true, context: context);
  }

  // ── Packet fetch ─────────────────────────────────────────────────────────────

  void fetchPacketList() {
    if (fromDateString.value.isEmpty || toDateString.value.isEmpty) return;

    isLoading.value = true;
    final params = {
      "FromDate": fromDateString.value,
      "ToDate": toDateString.value,
      "Labcode":
          isTalukaMode ? "0" : (selectedLab.value?.labCode?.toString() ?? "0"),
      "TALLGDCODE":
          isTalukaMode
              ? (selectedTaluka.value?.tALLGDCODE?.toString() ?? "0")
              : "0",
      "DeliveryExecutiveID": empCode.toString(),
    };
    apiManager.getDataForPacketAcceptAPI(params, (
      PacketAcceptDataResponse? response,
      String errorMessage,
      bool success,
    ) {
      isLoading.value = false;
      if (!success || response == null) {
        packetList.clear();
        selectedCount.value = 0;
        isAllSelected.value = false;
        if (errorMessage.isNotEmpty) {
          ToastManager.toast(errorMessage);
        }
        return;
      }
      packetList
        ..clear()
        ..addAll(response.output ?? <PacketAcceptDataOutput>[]);
      for (final item in packetList) {
        item.isSelected = false;
      }
      selectedCount.value = 0;
      isAllSelected.value = false;
      packetList.refresh();
    });
  }

  // ── Selection ─────────────────────────────────────────────────────────────────

  void togglePacketSelection(int index) {
    if (index < 0 || index >= packetList.length) return;
    packetList[index].isSelected = !packetList[index].isSelected;
    packetList.refresh();
    _updateSelectedCount();
  }

  void toggleSelectAll(bool value) {
    isAllSelected.value = value;
    for (final item in packetList) {
      item.isSelected = value;
    }
    packetList.refresh();
    _updateSelectedCount();
  }

  void _updateSelectedCount() {
    selectedCount.value = packetList.where((item) => item.isSelected).length;
    isAllSelected.value =
        packetList.isNotEmpty && selectedCount.value == packetList.length;
  }

  // ── Submit ────────────────────────────────────────────────────────────────────

  String _buildBulkPacketDetailsJson(List<PacketAcceptDataOutput> items) {
    final list =
        items
            .map(
              (e) => {
                "DCID": e.deliveryChallanID ?? "",
                "PacketID": e.packetNumber ?? "",
                "TreatmentID": e.prescriptionID?.toString() ?? "0",
              },
            )
            .toList();
    try {
      return jsonEncode(list);
    } catch (_) {
      return "";
    }
  }

  String _buildBarcodePacketDetailsJson(String dcId) {
    try {
      return jsonEncode([
        {"DCID": dcId},
      ]);
    } catch (_) {
      return "";
    }
  }

  void submitSelectedPackets(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.showAlertDialog(context, "Please select from date", () {
        Get.back();
      });
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.showAlertDialog(context, "Please select to date", () {
        Get.back();
      });
      return;
    }
    if (!isTalukaMode && selectedLab.value == null) {
      ToastManager.showAlertDialog(context, "Please select receiving lab", () {
        Get.back();
      });
      return;
    }
    if (isTalukaMode && selectedTaluka.value == null) {
      ToastManager.showAlertDialog(context, "Please select taluka", () {
        Get.back();
      });
      return;
    }

    final selectedPackets =
        packetList.where((item) => item.isSelected).toList();
    if (selectedPackets.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please select at least one packet",
        () {
          Get.back();
        },
      );
      return;
    }

    final packetJson = _buildBulkPacketDetailsJson(selectedPackets);
    if (packetJson.isEmpty) {
      ToastManager.showAlertDialog(context, "No Packet Found", () {
        Get.back();
      });
      return;
    }

    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
    };
    ToastManager.showLoader();
    apiManager.insertPacketAcceptDetailsManuallyAPI(params, (
      PacketAcceptDataResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (!success) {
        ToastManager.toast(errorMessage);
        return;
      }
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message:
            "${selectedPackets.length} Delivery Challan Picked Up For Delivery",
        onTap: () {
          Navigator.pop(context);
          fetchPacketList();
        },
      );
    });
  }

  void submitBarcodePacket(BuildContext context, {String? scannedValue}) {
    final dcId = (scannedValue ?? barcodeController.text).trim();
    if (dcId.isEmpty) {
      ToastManager.toast("Please enter Delivery Challan number");
      return;
    }
    if (dcId == "0") {
      ToastManager.toast(
        "Delivery Challan No: Zero, can not be accepted. Please use checkbox for packet pick up.",
      );
      barcodeController.clear();
      return;
    }

    final packetJson = _buildBarcodePacketDetailsJson(dcId);
    if (packetJson.isEmpty) {
      ToastManager.toast("No Packet Found");
      return;
    }

    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
    };
    apiManager.insertPacketAcceptDetailsAPI(params, (
      PacketAcceptDataResponse? response,
      String errorMessage,
      bool success,
    ) {
      if (!success) {
        ToastManager.toast(errorMessage);
        return;
      }
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message: "Delivery Challan No: $dcId Picked Up For Delivery",
        onTap: () {
          Navigator.pop(context);
          barcodeController.clear();
          fetchPacketList();
        },
      );
    });
  }

  Future<void> openBarCodeScanner(BuildContext context) async {
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
    submitBarcodePacket(context, scannedValue: res);
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
