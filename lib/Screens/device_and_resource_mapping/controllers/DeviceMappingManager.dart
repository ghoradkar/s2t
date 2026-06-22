// ignore_for_file: file_names

import '../Json_Class/ConsumablesListResponse/ConsumablesListResponse.dart';
import '../Json_Class/DevicesListResponse/DevicesListResponse.dart';
import '../Json_Class/ResourceListResponse/ResourceListResponse.dart';

class DeviceMappingManager {
  static final DeviceMappingManager _singleton =
      DeviceMappingManager._internal();

  factory DeviceMappingManager() {
    return _singleton;
  }
  DeviceMappingManager._internal();

  String empCode = "0";
  String dISTLGDCODE = "0";
  String campType = "0";
  String labCode = "0";
  String campID = "";
  String expectedBeneficiary = "0";
  // String campDate = "";
  String isSkipFlag = "0";
  String devicesId = "0";
  List<DevicesOutput> deviceList = [];
  List<ConsumablesOutput> consumablesList = [];
  List<ResourceOutput> resourceList = [];

  resetDeviceMaaping() {
    empCode = "0";
    dISTLGDCODE = "0";
    campType = "";
    labCode = "0";
    campID = "";
    // campDate = "";
    expectedBeneficiary = "0";
    isSkipFlag = "0";
    deviceList = [];
    consumablesList = [];
  }
}
