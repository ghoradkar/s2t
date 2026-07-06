// ignore_for_file: file_names
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/controller/app_data_manager.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/model/patient_list_re_allocation_for_medicine_delivery_response.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/model/user_mapped_taluka_response.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/repository/medicine_delivery_repository.dart';

class ReturnInLabController extends GetxController {
  final _repo = MedicineDeliveryRepository();

  final returnInLabList =
      <PatientListReAllocationforMedicineDeliveryOutput>[].obs;
  final canAcceptInLab = true.obs;

  Future<void> fetchTaluka({
    required int userId,
    required int distLgdCode,
    bool showDropdown = false,
    Function(List<UserMappedTalukaOutput>)? onShowDropdown,
  }) async {
    try {
      final response = await _repo.fetchTaluka({
        "UserId": userId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
      });
      final list = response.output ?? [];
      if (showDropdown) {
        ToastManager.hideLoader();
        onShowDropdown?.call(list);
      } else {
        if (AppDataManager.selectedTaluka == null && list.isNotEmpty) {
          AppDataManager.selectedTaluka = list.first;
        }
      }
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  Future<void> fetchBarcodePostCampDetails({required String barcode}) async {
    try {
      final response = await _repo.fetchBarcodePostCampDetails({
        "FromDate": "",
        "ToDate": "",
        "Labcode": "0",
        "TALLGDCODE":
            AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "",
        "PacketID": barcode,
      });
      final list = response.output ?? [];
      returnInLabList.value = list;
      canAcceptInLab.value = true;

      int packetSelect = 0;
      int packetCollect = 0;
      for (final item in list) {
        if (item.overallStatusID == 1) packetSelect++;
        if (item.overallStatusID == 2) packetCollect++;
      }
      if (packetSelect > 0) {
        ToastManager.toast(
          "Failure to accept return, process collection of packet first and try again.",
        );
        canAcceptInLab.value = false;
      }
      if (packetCollect > 0) {
        ToastManager.toast(
          "Failure to accept return, process receiving of packet first and try again.",
        );
        canAcceptInLab.value = false;
      }
    } catch (e) {
      returnInLabList.value = [];
      canAcceptInLab.value = false;
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
    }
  }

  String buildPacketJson() {
    final packetJsonArray = <Map<String, String>>[];
    for (final packetObj in returnInLabList) {
      if (packetObj.isSelected) {
        packetJsonArray.add({
          "DISTLGDCODE": packetObj.dISTLGDCODE?.toString() ?? "",
          "TALLGDCODE":
              AppDataManager.selectedTaluka?.tALLGDCODE?.toString() ?? "",
          "Labcode": packetObj.labcode?.toString() ?? "",
          "PacketID": packetObj.packetNumber ?? "",
          "TreatmentID": packetObj.prescriptionID?.toString() ?? "",
          "OverallStatusID": packetObj.overallStatusID?.toString() ?? "",
        });
      }
    }
    try {
      return jsonEncode(packetJsonArray);
    } catch (_) {
      return "";
    }
  }

  Future<void> insertPacketDetails({
    required String json,
    required int empCode,
    VoidCallback? onSuccess,
  }) async {
    try {
      await _repo.insertPacketReturn({
        "CW_ReallocationMedicalDelivary": json,
        "USERID": empCode.toString(),
      });
      ToastManager.hideLoader();
      ToastManager.toast("Packet Accepted successfully");
      onSuccess?.call();
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
    }
  }
}
