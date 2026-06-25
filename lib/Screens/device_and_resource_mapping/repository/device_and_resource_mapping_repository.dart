import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/CampListV3Response/CampListV3Response.dart';
import 'package:s2toperational/Modules/Json_Class/ConsumablesListResponse/ConsumablesListResponse.dart';
import '../models/DevicesListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceListResponse/ResourceListResponse.dart';
import '../models/SubDevicesListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SubResourceListResponse/SubResourceListResponse.dart';
import '../models/SubmitDeviceMappingResponse.dart';

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
