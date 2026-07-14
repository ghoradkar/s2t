import 'dart:async';

import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/camp_calendar/model/camp_list_v3_response.dart';
import 'package:s2toperational/device_and_resource_mapping/models/consumables_list_response.dart';
import '../models/devices_list_response.dart';
import 'package:s2toperational/device_and_resource_mapping/models/resource_list_response.dart';
import '../models/sub_devices_list_response.dart';
import 'package:s2toperational/device_and_resource_mapping/models/sub_resource_list_response.dart';
import '../models/submit_device_mapping_response.dart';

class DeviceAndResourceMappingRepository {
  final APIManager _api = APIManager();

  Future<CampListV3Response> fetchCampList(Map<String, String> params) {
    final c = Completer<CampListV3Response>();
    _api.getCampIDAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<DevicesListResponse> fetchDevices(Map<String, String> params) {
    final c = Completer<DevicesListResponse>();
    _api.getDevicesAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<SubDevicesListResponse> fetchSubDevices(Map<String, String> params) {
    final c = Completer<SubDevicesListResponse>();
    _api.getSubDevicesAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<ConsumablesListResponse> fetchConsumables(Map<String, String> params) {
    final c = Completer<ConsumablesListResponse>();
    _api.getConsumablesAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<ResourceListResponse> fetchResources() {
    final c = Completer<ResourceListResponse>();
    _api.getResourceAPI((res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<SubResourceListResponse> fetchSubResources(Map<String, String> params) {
    final c = Completer<SubResourceListResponse>();
    _api.getSubResourceAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<SubmitDeviceMappingResponse> submitDeviceAllocation(
    Map<String, dynamic> params,
  ) {
    final c = Completer<SubmitDeviceMappingResponse>();
    _api.submitDeviceAllocationAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }
}
