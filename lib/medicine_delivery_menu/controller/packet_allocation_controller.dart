// ignore_for_file: file_names
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/medicine_delivery_menu/controller/app_data_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/medicine_delivery_menu/model/packet_accept_data_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/report_delivery_executive_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/user_mapped_taluka_response.dart';
import 'package:s2toperational/medicine_delivery_menu/repository/medicine_delivery_repository.dart';

class PacketAllocationController extends GetxController {
  final _repo = MedicineDeliveryRepository();

  final listOfPackets = <PacketAcceptDataOutput>[].obs;
  final listOfPacketsSearch = <PacketAcceptDataOutput>[].obs;

  Future<void> fetchPackets() async {
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchPacketAssignment({
        "FromDate": AppDataManager.fromDate,
        "ToDate": AppDataManager.toDate,
        "Labcode": "0",
        "TALLGDCODE":
            AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "0",
      });
      listOfPackets.value = response.output ?? [];
      listOfPacketsSearch.value = List.from(listOfPackets);
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> fetchTaluka({
    required int userId,
    required int distLgdCode,
    bool showDropdown = true,
    Function(List<UserMappedTalukaOutput>)? onShowDropdown,
  }) async {
    try {
      final response = await _repo.fetchTaluka({
        "UserId": userId.toString(),
        "DISTLGDCODE": distLgdCode.toString(),
      });
      ToastManager.hideLoader();
      final list = response.output ?? [];
      if (AppDataManager.selectedTaluka == null && list.isNotEmpty) {
        AppDataManager.selectedTaluka = list.first;
      }
      if (showDropdown) {
        onShowDropdown?.call(list);
      }
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  Future<void> fetchDeliveryExecutives({
    Function(List<ReportDeliveryExecutiveOutput>)? onSuccess,
  }) async {
    if (AppDataManager.selectedTaluka == null) {
      ToastManager.toast("Please select Taluka");
      return;
    }
    try {
      final response = await _repo.fetchDeliveryExecutives({
        "TALLGDCODE":
            AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "0",
      });
      ToastManager.hideLoader();
      onSuccess?.call(response.output ?? []);
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  void filterByName(String query) {
    listOfPacketsSearch.value = listOfPackets.where((item) {
      final desc = item.patientName?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  String getPacketDetailsJson() {
    final packetJsonArray = <Map<String, String>>[];
    for (final packetObj in listOfPackets) {
      if (packetObj.isSelected) {
        packetJsonArray.add({
          "DCID": packetObj.deliveryChallanID ?? "",
          "PacketID": packetObj.packetNumber ?? "",
          "TreatmentID": packetObj.prescriptionID?.toString() ?? "",
          "TeamId": AppDataManager.selectedResource?.teamid.toString() ?? "0",
        });
      }
    }
    try {
      return jsonEncode(packetJsonArray);
    } catch (_) {
      return "";
    }
  }

  Future<void> insertPacketAssignDetailsManually({
    VoidCallback? onSuccess,
  }) async {
    final packetDetails = getPacketDetailsJson();
    if (packetDetails.isEmpty) {
      ToastManager.toast("No packets selected for assignment");
      return;
    }
    final empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    try {
      await _repo.insertPacketAssignment({
        "CreatedBy": empCode.toString(),
        "PacketDetails": packetDetails,
        "DeliveryExecutiveID":
            AppDataManager.selectedResource?.userID.toString() ?? "0",
      });
      ToastManager.hideLoader();
      ToastManager.toast("Packet assigned successfully");
      listOfPackets.value = [];
      listOfPacketsSearch.value = [];
      onSuccess?.call();
      fetchPackets();
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
    }
  }
}
