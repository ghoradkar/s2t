import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/PacketReturnResponse/PacketReturnResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ReportDeliveryExecutiveResponse/ReportDeliveryExecutiveResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';

enum MedicineReturnMode { acceptInLab, returnToPharmacy }

class MedicineReturnController extends GetxController {
  final APIManager apiManager = APIManager();

  final TextEditingController barcodeController = TextEditingController();
  final TextEditingController searchController = TextEditingController();

  final RxString fromDateString = ''.obs;
  final RxString toDateString = ''.obs;
  DateTime? fromDate;
  DateTime? toDate;

  final Rx<MedicineReturnMode> mode = MedicineReturnMode.acceptInLab.obs;

  final Rx<UserMappedTalukaOutput?> selectedTaluka =
      Rx<UserMappedTalukaOutput?>(null);
  final Rx<ReportDeliveryExecutiveOutput?> selectedResource =
      Rx<ReportDeliveryExecutiveOutput?>(null);

  final RxList<UserMappedTalukaOutput> talukaList =
      <UserMappedTalukaOutput>[].obs;
  final RxList<ReportDeliveryExecutiveOutput> resourceList =
      <ReportDeliveryExecutiveOutput>[].obs;

  final RxList<PacketReturnOutput> returnList = <PacketReturnOutput>[].obs;
  final RxList<PacketReturnOutput> filteredList = <PacketReturnOutput>[].obs;

  final RxBool selectAll = false.obs;

  int dISTLGDCODE = 0;
  int empCode = 0;

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
    searchController.dispose();
    super.onClose();
  }

  void resetState() {
    final now = DateTime.now();
    fromDate = now.subtract(const Duration(days: 3));
    toDate = now;
    fromDateString.value = FormatterManager.formatDateToString(fromDate!);
    toDateString.value = FormatterManager.formatDateToString(toDate!);
    selectedTaluka.value = null;
    selectedResource.value = null;
    talukaList.clear();
    resourceList.clear();
    returnList.clear();
    filteredList.clear();
    selectAll.value = false;
    barcodeController.clear();
    searchController.clear();
    fetchTaluka(autoSelect: true);
  }

  void setMode(MedicineReturnMode newMode) {
    if (mode.value == newMode) return;
    mode.value = newMode;
    barcodeController.clear();
    searchController.clear();
    selectAll.value = false;
    returnList.clear();
    filteredList.clear();
    if (mode.value == MedicineReturnMode.acceptInLab) {
      selectedResource.value = null;
      resourceList.clear();
    }
    fetchReturnList();
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
    returnList.clear();
    selectAll.value = false;
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
    fetchReturnList();
  }

  void fetchTaluka({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    ToastManager.showLoader();
    final params = {
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
    };
    apiManager.getUserMappedTalukaAPI(params, (
      UserMappedTalukaResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
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
          "Taluka",
          talukaList,
          DropDownTypeMenu.UserMappedTaluka,
          (value) {
            selectedTaluka.value = value;
            selectedResource.value = null;
            resourceList.clear();
            fetchResources(
              autoSelect: mode.value == MedicineReturnMode.returnToPharmacy,
            );
            fetchReturnList();
          },
        );
        return;
      }

      if (autoSelect) {
        selectedTaluka.value = talukaList.first;
        fetchResources(
          autoSelect: mode.value == MedicineReturnMode.returnToPharmacy,
        );
        fetchReturnList();
      }
    });
  }

  void showTalukaDropdown(BuildContext context) {
    if (talukaList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Taluka",
        talukaList,
        DropDownTypeMenu.UserMappedTaluka,
        (value) {
          selectedTaluka.value = value;
          selectedResource.value = null;
          resourceList.clear();
          fetchResources(showDropdown: true, context: context);
        },
      );
      return;
    }
    fetchTaluka(showDropdown: true, context: context);
  }

  void fetchResources({
    bool autoSelect = false,
    bool showDropdown = false,
    BuildContext? context,
  }) {
    if (selectedTaluka.value == null) {
      ToastManager.toast("Please select Taluka");
      return;
    }
    ToastManager.showLoader();
    final params = {
      "TALLGDCODE": selectedTaluka.value?.tALLGDCODE.toString() ?? "0",
    };
    apiManager.getReportDeliveryExecutiveAPI(params, (
      ReportDeliveryExecutiveResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (!success || response == null) {
        ToastManager.toast(errorMessage);
        return;
      }
      resourceList
        ..clear()
        ..addAll(response.output ?? <ReportDeliveryExecutiveOutput>[]);

      if (resourceList.isEmpty) {
        ToastManager.toast("Delivery executive list not found");
        return;
      }

      if (showDropdown && context != null) {
        _showDropDownBottomSheet(
          context,
          "Select Resource",
          resourceList,
          DropDownTypeMenu.ReportDeliveryExecutive,
          (value) {
            selectedResource.value = value;
            fetchReturnList();
          },
        );
        return;
      }

      if (autoSelect) {
        if (mode.value == MedicineReturnMode.returnToPharmacy) {
          selectedResource.value = resourceList.first;
          fetchReturnList();
        }
      }
    });
  }

  void showResourceDropdown(BuildContext context) {
    if (selectedTaluka.value == null) {
      ToastManager.toast("Please select Taluka");
      return;
    }
    if (resourceList.isNotEmpty) {
      _showDropDownBottomSheet(
        context,
        "Select Resource",
        resourceList,
        DropDownTypeMenu.ReportDeliveryExecutive,
        (value) {
          selectedResource.value = value;
          fetchReturnList();
        },
      );
      return;
    }
    fetchResources(showDropdown: true, context: context);
  }

  void fetchReturnList() {
    if (fromDateString.value.isEmpty || toDateString.value.isEmpty) {
      return;
    }
    if (selectedTaluka.value == null) {
      return;
    }
    ToastManager.showLoader();
    final params = {
      "Fromdate": fromDateString.value,
      "todate": toDateString.value,
      "DISTLGDCODE": "0",
      "TALLGDCODE": selectedTaluka.value?.tALLGDCODE.toString() ?? "0",
      "UserID": selectedResource.value?.userID.toString() ?? "0",
      "LoginUserID": empCode.toString(),
    };

    if (mode.value == MedicineReturnMode.acceptInLab) {
      apiManager.getMedicineReturnAcceptInLabListAPI(
        params,
        _handleReturnListResponse,
      );
    } else {
      apiManager.getMedicineReturnToPharmacyListAPI(
        params,
        _handleReturnListResponse,
      );
    }
  }

  void _handleReturnListResponse(
    PacketReturnResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (!success || response == null) {
      returnList.clear();
      selectAll.value = false;
      if (errorMessage.isNotEmpty) {
        ToastManager.toast(errorMessage);
      }
      return;
    }
    returnList
      ..clear()
      ..addAll(response.output ?? <PacketReturnOutput>[]);
    filteredList
      ..clear()
      ..addAll(returnList);
    for (final item in returnList) {
      item.isSelected = false;
    }
    selectAll.value = false;
    returnList.refresh();
    filteredList.refresh();
  }

  void toggleSelectAll(bool value) {
    selectAll.value = value;
    for (final item in filteredList) {
      item.isSelected = value;
    }
    filteredList.refresh();
  }

  void toggleItemSelection(int index) {
    if (index < 0 || index >= filteredList.length) return;
    filteredList[index].isSelected = !filteredList[index].isSelected;
    selectAll.value =
        filteredList.isNotEmpty && filteredList.every((e) => e.isSelected);
    filteredList.refresh();
  }

  void filterByBeneficiary(String query) {
    final q = query.trim().toLowerCase();
    if (q.isEmpty) {
      filteredList
        ..clear()
        ..addAll(returnList);
      filteredList.refresh();
      selectAll.value =
          filteredList.isNotEmpty && filteredList.every((e) => e.isSelected);
      return;
    }
    filteredList
      ..clear()
      ..addAll(
        returnList.where((item) {
          final name = (item.patientName ?? "").toLowerCase();
          return name.contains(q);
        }),
      );
    filteredList.refresh();
    selectAll.value =
        filteredList.isNotEmpty && filteredList.every((e) => e.isSelected);
  }

  void onSearchPressed() {
    if (mode.value == MedicineReturnMode.returnToPharmacy &&
        barcodeController.text.trim() == "0") {
      ToastManager.toast(
        "Delivery Challan No : Zero , can not be accepted.\nPlease use checkbox for packet pick up.",
      );
      barcodeController.clear();
      return;
    }
    fetchReturnList();
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
    onSearchPressed();
  }

  void submitSelected(BuildContext context) {
    if (fromDateString.value.isEmpty) {
      ToastManager.toast("Please select from date");
      return;
    }
    if (toDateString.value.isEmpty) {
      ToastManager.toast("Please select to date");
      return;
    }

    final selected = returnList.where((e) => e.isSelected).toList();
    if (selected.isEmpty) {
      ToastManager.toast("Please select at least one patient");
      return;
    }

    final payload =
        selected
            .map(
              (e) => {
                "PacketNo": e.packetNo ?? "",
                "TreatmentID": e.treatmentID?.toString() ?? "",
              },
            )
            .toList();

    final detailsJson = jsonEncode(payload);

    if (mode.value == MedicineReturnMode.returnToPharmacy) {
      _showReturnToPharmacyConfirm(context, detailsJson);

      return;
    }
    _submitAcceptInLab(context, detailsJson);
  }

  void _showReturnToPharmacyConfirm(BuildContext context, String detailsJson) {
    ToastManager().showConfirmationDialog(
      context: context,
      message: 'Are you sure you want to Continue?',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Navigator.pop(context);
          _submitReturnToPharmacy(context, detailsJson);
        } else if (p1 == false) {
          Navigator.pop(context);
        }
      },
    );

    // showDialog(
    //   context: context,
    //   barrierDismissible: false,
    //   builder: (BuildContext dialogContext) {
    //     return AlertDialog(
    //       content: Column(
    //         mainAxisSize: MainAxisSize.min,
    //         children: [
    //           Image.asset(icQuestionMark),
    //           Text("Are you sure you want to Continue?"),
    //         ],
    //       ),
    //       actions: [
    //         TextButton(
    //           child: const Text("Yes"),
    //           onPressed: () {
    //             Navigator.pop(dialogContext);
    //             _submitReturnToPharmacy(context, detailsJson);
    //           },
    //         ),
    //         TextButton(
    //           child: const Text("No"),
    //           onPressed: () {
    //             Navigator.pop(dialogContext);
    //           },
    //         ),
    //       ],
    //     );
    //
    //     // return AlertDialog(
    //     //   content: const Text("Are you sure you want to Continue?"),
    //     //   actions: [
    //     //     TextButton(
    //     //       onPressed: () => Navigator.pop(dialogContext),
    //     //       child: const Text("No"),
    //     //     ),
    //     //     TextButton(
    //     //       onPressed: () {
    //     //         Navigator.pop(dialogContext);
    //     //         _submitReturnToPharmacy(context, detailsJson);
    //     //       },
    //     //       child: const Text("Yes"),
    //     //     ),
    //     //   ],
    //     // );
    //   },
    // );
  }

  void _submitAcceptInLab(BuildContext context, String detailsJson) {
    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "AccpetDetails": detailsJson,
    };
    apiManager.insertMedicineReturnAcceptInLabAPI(params, (
      response,
      errorMessage,
      success,
    ) {
      ToastManager.hideLoader();
      if (!success) {
        ToastManager.toast(errorMessage);
        return;
      }
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message: "Packet Accepted successfully",
        onTap: () {
          Navigator.pop(context);
          fetchReturnList();
        },
      );
    });
  }

  void _submitReturnToPharmacy(BuildContext context, String detailsJson) {
    ToastManager.showLoader();
    final params = {
      "CreatedBy": empCode.toString(),
      "ReturnDetails": detailsJson,
    };
    apiManager.insertMedicineReturnToPharmacyAPI(params, (
      response,
      errorMessage,
      success,
    ) {
      ToastManager.hideLoader();
      if (!success) {
        ToastManager.toast(errorMessage);
        return;
      }
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message: "Packet Return successfully",
        onTap: () {
          Navigator.pop(context);
          fetchReturnList();
        },
      );
    });
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
