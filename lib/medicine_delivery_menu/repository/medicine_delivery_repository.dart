// ignore_for_file: file_names
import 'dart:async';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/medicine_delivery_menu/model/packet_accept_data_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/patient_list_re_allocation_for_medicine_delivery_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/report_delivery_executive_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/user_mapped_taluka_response.dart';

class MedicineDeliveryRepository {
  final APIManager _api = APIManager();

  Future<UserMappedTalukaResponse> fetchTaluka(
    Map<String, String> params,
  ) {
    final c = Completer<UserMappedTalukaResponse>();
    _api.getUserMappedTalukaAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<ReportDeliveryExecutiveResponse> fetchDeliveryExecutives(
    Map<String, String> params,
  ) {
    final c = Completer<ReportDeliveryExecutiveResponse>();
    _api.getReportDeliveryExecutiveAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<PacketAcceptDataResponse> fetchPacketAssignment(
    Map<String, String> params,
  ) {
    final c = Completer<PacketAcceptDataResponse>();
    _api.getDataForPacketAssignmentAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<PacketAcceptDataResponse> insertPacketAssignment(
    Map<String, String> params,
  ) {
    final c = Completer<PacketAcceptDataResponse>();
    _api.insertPacketAssignDetailsManuallyAPI(params, (response, error, success) {
      if (success) {
        c.complete(response ?? PacketAcceptDataResponse());
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<PatientListReAllocationforMedicineDeliveryResponse>
      fetchBarcodePostCampDetails(Map<String, String> params) {
    final c = Completer<
        PatientListReAllocationforMedicineDeliveryResponse>();
    _api.getBarcodePostCampDetailsAPI(params, (response, error, success) {
      if (success && response != null) {
        c.complete(response);
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }

  Future<PatientListReAllocationforMedicineDeliveryResponse> insertPacketReturn(
    Map<String, String> params,
  ) {
    final c = Completer<
        PatientListReAllocationforMedicineDeliveryResponse>();
    _api.insertPacketDetailsAPI(params, (response, error, success) {
      if (success) {
        c.complete(
          response ?? PatientListReAllocationforMedicineDeliveryResponse(),
        );
      } else {
        c.completeError(error);
      }
    });
    return c.future;
  }
}
