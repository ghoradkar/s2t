import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/DataForPacketReceiveResponse/DataForPacketReceiveResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';

class PacketReceiveController extends GetxController {
  final APIManager apiManager = APIManager();

  final TextEditingController barcodeController = TextEditingController();

  final RxString fromDateString = ''.obs;
  final RxString toDateString = ''.obs;

  DateTime? fromDate;
  DateTime? toDate;

  final Rx<LabByUserIDOutput?> selectedLab = Rx<LabByUserIDOutput?>(null);
  final RxList<LabByUserIDOutput> labList = <LabByUserIDOutput>[].obs;

  final RxList<DataForPacketReceiveOutput> packetList =
      <DataForPacketReceiveOutput>[].obs;

  int dISTLGDCODE = 0;
  int empCode = 0;

  final RxInt selectedCount = 0.obs;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    dISTLGDCODE = user?.dISTLGDCODE ?? 0;
    empCode = user?.empCode ?? 0;
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
    labList.clear();
    packetList.clear();
    selectedCount.value = 0;
    barcodeController.clear();
    fetchLabs(autoSelect: true);
  }

  Future<void> pickFromDate(BuildContext context) async {
    final DateTime initial = fromDate ?? DateTime.now();
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: initial,
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked == null) {
      return;
    }

    fromDate = picked;
    fromDateString.value = FormatterManager.formatDateToString(picked);
    toDate = null;
    toDateString.value = '';
    packetList.clear();
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
    if (picked == null) {
      return;
    }

    toDate = picked;
    toDateString.value = FormatterManager.formatDateToString(picked);
    fetchPacketReceive();
  }

  void fetchLabs({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    ToastManager.showLoader();
    final params = {"DISTLGDCODE": "0", "USERID": empCode.toString()};
    apiManager.getTalukaPacketReciveAPI(params, (
      LabByUserIDResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
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
            selectedLab.value = value;
            fetchPacketReceive();
          },
        );
        return;
      }

      if (autoSelect) {
        selectedLab.value = labList.first;
        fetchPacketReceive();
      }
    });
  }

  void showLabDropdown(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.toast("Please select to date");
      return;
    }
    if (labList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select Lab",
        labList,
        DropDownTypeMenu.TalukaPacketRecive,
        (value) {
          selectedLab.value = value;
          fetchPacketReceive();
        },
      );
      return;
    }
    fetchLabs(showDropdown: true, context: context);
  }

  void fetchPacketReceive() {
    if (fromDateString.value.isEmpty || toDateString.value.isEmpty) {
      return;
    }
    ToastManager.showLoader();
    final params = {
      "FromDate": fromDateString.value,
      "TODate": toDateString.value,
      "Labcode": selectedLab.value?.labCode.toString() ?? "0",
    };
    apiManager.getDataForPacketReceiveAPI(params, (
      DataForPacketReceiveResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (!success || response == null) {
        packetList.clear();
        selectedCount.value = 0;
        if (errorMessage.isNotEmpty) {
          ToastManager.toast(errorMessage);
        }
        return;
      }
      packetList
        ..clear()
        ..addAll(response.output ?? <DataForPacketReceiveOutput>[]);
      for (final item in packetList) {
        item.isSelected = false;
      }
      packetList.refresh();
      _updateSelectedCount();
    });
  }

  void togglePacketSelection(int index) {
    if (index < 0 || index >= packetList.length) {
      return;
    }
    packetList[index].isSelected = !packetList[index].isSelected;
    packetList.refresh();
    _updateSelectedCount();
  }

  void _updateSelectedCount() {
    selectedCount.value = packetList.where((item) => item.isSelected).length;
  }

  String _buildPacketDetailsJson(List<String> packetNumbers) {
    final packetJsonArray =
        packetNumbers.map((id) => {"PacketID": id}).toList();
    try {
      return jsonEncode(packetJsonArray);
    } catch (_) {
      return "";
    }
  }

  void submitSelectedPackets(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.toast("Please select to date");
      return;
    }
    if (selectedLab.value == null) {
      ToastManager.toast("Please select receiving lab");
      return;
    }

    final selectedPackets =
        packetList.where((item) => item.isSelected).toList();
    if (selectedPackets.isEmpty) {
      ToastManager.toast("Please select at least one packet");
      return;
    }

    final packetNumbers =
        selectedPackets
            .map((e) => e.packetNumber ?? "")
            .where((e) => e.isNotEmpty)
            .toList();
    final packetJson = _buildPacketDetailsJson(packetNumbers);
    if (packetJson.isEmpty) {
      ToastManager.toast("No Packet Found");
      return;
    }

    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
      "LabCode": selectedLab.value?.labCode.toString() ?? "0",
    };
    apiManager.getInsertPacketReceiveDetailsAPI(params, (
      DataForPacketReceiveResponse? response,
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
        message: "${packetNumbers.length} Packets Received In Lab For Delivery",
        onTap: () {
          Navigator.pop(context);
          fetchPacketReceive();
        },
      );
    });
  }

  void submitBarcodePacket(BuildContext context, {String? scannedValue}) {
    final packetNumber = (scannedValue ?? barcodeController.text).trim();
    if (packetNumber.isEmpty) {
      ToastManager.toast("Please enter packet number");
      return;
    }
    if (selectedLab.value == null) {
      ToastManager.toast("Please select lab");
      return;
    }

    final packetJson = _buildPacketDetailsJson([packetNumber]);
    if (packetJson.isEmpty) {
      ToastManager.toast("No Packet Found");
      return;
    }

    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
      "LabCode": selectedLab.value?.labCode.toString() ?? "0",
    };
    apiManager.getInsertPacketReceiveDetailsAPI(params, (
      DataForPacketReceiveResponse? response,
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
        message: "Packet No : $packetNumber Received In Lab For Delivery",
        onTap: () {
          Navigator.pop(context);
          barcodeController.clear();
          fetchPacketReceive();
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
    if (res == null || res == '-1') {
      return;
    }
    barcodeController.text = res;
    submitBarcodePacket(context, scannedValue: res);
  }

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
