import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/PacketCollectionResponse/PacketCollectionResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';

class PacketCollectionController extends GetxController {
  final APIManager apiManager = APIManager();

  final TextEditingController barcodeController = TextEditingController();

  final RxString fromDateString = ''.obs;
  final RxString toDateString = ''.obs;

  DateTime? fromDate;
  DateTime? toDate;

  final Rx<DistrictOutput?> selectedDistrict = Rx<DistrictOutput?>(null);
  final Rx<LandingLabCampCreationOutput?> selectedLab =
      Rx<LandingLabCampCreationOutput?>(null);

  final RxList<PacketCollectionOutput> packetList =
      <PacketCollectionOutput>[].obs;
  final RxInt selectedCount = 0.obs;

  final List<DistrictOutput> districtList = [];
  final List<LandingLabCampCreationOutput> labList = [];

  int empCode = 0;
  int stateLgdcCode = 0;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    stateLgdcCode = user?.sTATELGDCODE ?? 0;
    resetState();
  }

  @override
  void onClose() {
    barcodeController.dispose();
    super.onClose();
  }

  void _updateSelectedCount() {
    selectedCount.value =
        packetList.where((item) => item.isSelected).length;
  }

  void resetState() {
    final now = DateTime.now();
    fromDate = now.subtract(const Duration(days: 3));
    toDate = now;
    fromDateString.value = FormatterManager.formatDateToString(fromDate!);
    toDateString.value = FormatterManager.formatDateToString(toDate!);
    selectedDistrict.value = null;
    selectedLab.value = null;
    districtList.clear();
    labList.clear();
    packetList.clear();
    selectedCount.value = 0;
    barcodeController.clear();
    fetchDistricts(autoSelect: true);
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
    selectedCount.value = 0;
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
    fetchPacketCollection();
  }

  void fetchDistricts({bool autoSelect = false, BuildContext? context}) {
    ToastManager.showLoader();
    final params = {
      "STATELGDCODE": stateLgdcCode.toString(),
      "USERID": empCode.toString(),
    };
    apiManager.getDistrictByUserIDAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (!success || response == null) {
        ToastManager.toast(error);
        return;
      }
      districtList
        ..clear()
        ..addAll(response.output ?? <DistrictOutput>[]);

      if (districtList.isEmpty) {
        ToastManager.toast("District list not found");
        return;
      }

      if (autoSelect) {
        selectedDistrict.value = districtList.first;
        fetchPacketCollection();
      } else if (context != null) {
        _showDropDownBottomSheet(
          context,
          "Select District",
          districtList,
          DropDownTypeMenu.District,
          (value) {
            selectedDistrict.value = value;
            selectedLab.value = null;
            labList.clear();
            packetList.clear();
            selectedCount.value = 0;
          },
        );
      }
    });
  }

  void showDistrictDropdown(BuildContext context) {
    if (districtList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select District",
        districtList,
        DropDownTypeMenu.District,
        (value) {
          selectedDistrict.value = value;
          selectedLab.value = null;
          labList.clear();
          packetList.clear();
          selectedCount.value = 0;
        },
      );
      return;
    }
    fetchDistricts(autoSelect: false, context: context);
  }

  void fetchLandingLabs({bool showDropdown = false, BuildContext? context}) {
    if (selectedDistrict.value == null) {
      ToastManager.toast("Please select district");
      return;
    }
    ToastManager.showLoader();
    final params = {
      "DISTLGDCODE": selectedDistrict.value?.dISTLGDCODE.toString() ?? "0",
    };
    apiManager.getLandingLabAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (!success || response == null) {
        ToastManager.toast(error);
        return;
      }
      labList
        ..clear()
        ..addAll(response.output ?? <LandingLabCampCreationOutput>[]);
      if (labList.isEmpty) {
        ToastManager.toast("Lab list not found");
        return;
      }
      if (showDropdown && context != null) {
        _showDropDownBottomSheet(
          context,
          "Select Lab",
          labList,
          DropDownTypeMenu.BindLab,
          (value) {
            selectedLab.value = value;
            fetchPacketCollection();
          },
        );
      }
    });
  }

  void showLandingLabDropdown(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.toast("Please select to date");
      return;
    }
    if (selectedDistrict.value == null) {
      ToastManager.toast("Please select district");
      return;
    }
    if (labList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select Lab",
        labList,
        DropDownTypeMenu.BindLab,
        (value) {
          selectedLab.value = value;
          fetchPacketCollection();
        },
      );
      return;
    }
    fetchLandingLabs(showDropdown: true, context: context);
  }

  void fetchPacketCollection() {
    if (fromDateString.value.isEmpty || toDateString.value.isEmpty) {
      return;
    }
    ToastManager.showLoader();
    final params = {
      "DISTLGDCODE": selectedDistrict.value?.dISTLGDCODE.toString() ?? "0",
      "FromDate": fromDateString.value,
      "TODate": toDateString.value,
      "Labcode": selectedLab.value?.labCode.toString() ?? "0",
      "UserID": empCode.toString(),
    };
    apiManager.getPacketCollectionDataAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (!success || response == null) {
        packetList.clear();
        selectedCount.value = 0;
        if (error.isNotEmpty) {
          ToastManager.toast(error);
        }
        return;
      }
      packetList
        ..clear()
        ..addAll(response.output ?? <PacketCollectionOutput>[]);
      for (final item in packetList) {
        item.isSelected = false;
      }
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
    if (selectedDistrict.value == null) {
      ToastManager.toast("Please select district");
      return;
    }
    if (selectedLab.value == null) {
      ToastManager.toast("Please select landing lab");
      return;
    }

    final selectedPackets =
        packetList.where((item) => item.isSelected).toList();
    if (selectedPackets.isEmpty) {
      ToastManager.toast("Please select at least one packet");
      return;
    }

    final packetNumbers =
        selectedPackets.map((e) => e.packetNumber ?? "").where((e) => e.isNotEmpty).toList();
    final packetJson = _buildPacketDetailsJson(packetNumbers);
    if (packetJson.isEmpty) {
      ToastManager.toast("Unable to build packet details");
      return;
    }

    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
      "LabCode": selectedLab.value?.labCode.toString() ?? "0",
    };
    apiManager.insertPacketCollectionDetailsAPI(
      params,
      (response, error, success) {
        ToastManager.hideLoader();
        if (!success) {
          ToastManager.toast(error);
          return;
        }
        ToastManager().showSuccessOkayDialog(
          context: context,
          title: "Success",
          message: "${selectedPackets.length} Packets Picked For Lab Transfer",
          onTap: () {
            Navigator.pop(context);
            fetchPacketCollection();
          },
        );
      },
    );
  }

  void submitBarcodePacket(BuildContext context, {String? scannedValue}) {
    final packetNumber = (scannedValue ?? barcodeController.text).trim();
    if (packetNumber.isEmpty) {
      ToastManager.toast("Please enter packet number");
      return;
    }
    if (selectedLab.value == null) {
      ToastManager.toast("Please select landing lab");
      return;
    }

    final packetJson = _buildPacketDetailsJson([packetNumber]);
    if (packetJson.isEmpty) {
      ToastManager.toast("Unable to build packet details");
      return;
    }

    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetJson,
      "LabCode": selectedLab.value?.labCode.toString() ?? "0",
    };
    apiManager.insertPacketCollectionDetailsAPI(
      params,
      (response, error, success) {
        ToastManager.hideLoader();
        if (!success) {
          ToastManager.toast(error);
          return;
        }
        ToastManager().showSuccessOkayDialog(
          context: context,
          title: "Success",
          message: "Packet No : $packetNumber Picked For Lab Transfer",
          onTap: () {
            Navigator.pop(context);
            barcodeController.clear();
            fetchPacketCollection();
          },
        );
      },
    );
  }

  Future<void> openBarCodeScanner(BuildContext context) async {
    if (selectedLab.value == null) {
      ToastManager.toast(
        "Please select landing lab first then scan barcode",
      );
      return;
    }
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
