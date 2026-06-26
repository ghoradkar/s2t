// ignore_for_file: avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/controllers/DeviceMappingManager.dart';
import 'package:s2toperational/Screens/camp_calendar/model/camp_list_v3_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/camp_type_response.dart';
import '../models/devices_list_response.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/models/resource_list_response.dart';
import '../models/sub_devices_list_response.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/models/sub_resource_list_response.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../repository/device_and_resource_mapping_repository.dart';

class DeviceAndResourceMappingController extends GetxController {
  final _repository = DeviceAndResourceMappingRepository();
  final ToastManager toastManager = ToastManager();

  String selectedCampDate = '';
  CampTypeOutput? selectedCampType;
  CampListV3Output? selectedCampId;
  int selectedSegmentIndex = 0;
  DevicesOutput selectedDevice = DevicesOutput();
  ResourceOutput? selectedResource;

  final DeviceMappingManager deviceMappingManager = DeviceMappingManager();

  @override
  void onInit() {
    super.onInit();
    selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    final int districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    final int empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    deviceMappingManager.empCode = empCode.toString();
    deviceMappingManager.dISTLGDCODE = districtId.toString();
    update();
  }

  void setSelectedCampDate(String date) {
    selectedCampDate = date;
    deviceMappingManager.deviceList = [];
    deviceMappingManager.consumablesList = [];
    deviceMappingManager.resourceList = [];
    deviceMappingManager.campID = '';
    update();
  }

  void onCampTypeSelected(CampTypeOutput? campType) {
    selectedCampType = campType;
    deviceMappingManager.campType =
        selectedCampType?.cAMPTYPE.toString() ?? '0';
    update();
  }

  Future<List<CampListV3Output>> fetchCampList() async {
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchCampList({
        'CampDATE': selectedCampDate,
        'UserId': deviceMappingManager.empCode,
        'DISTLGDCODE': deviceMappingManager.dISTLGDCODE,
        'CampType': deviceMappingManager.campType,
        'LABCODE': deviceMappingManager.labCode,
      });
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> onCampIdSelected(CampListV3Output camp) async {
    selectedCampId = camp;
    deviceMappingManager.campID = camp.campId.toString();
    deviceMappingManager.labCode = camp.lABCODE.toString();
    deviceMappingManager.expectedBeneficiary =
        camp.expectedbeneficiarycount.toString();

    if (deviceMappingManager.empCode != (camp.createdBy ?? 0).toString()) {
      toastManager.showAlertMessage(
        Get.context!,
        'This camp not created by you,please select created camp',
        const Color(0xFFEA0000),
      );
      update();
      return;
    }
    if ((camp.resourceMappingFlag ?? 0) == 1) {
      deviceMappingManager.campID = '';
      toastManager.showAlertMessage(
        Get.context!,
        'Device and resource mapping already done for selected camp',
        const Color(0xFFEA0000),
      );
      update();
      return;
    }

    ToastManager.showLoader();
    try {
      final response = await _repository.fetchDevices({
        'BeneficairyCount': deviceMappingManager.expectedBeneficiary,
      });
      deviceMappingManager.deviceList = response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  Future<List<SubDevicesOutput>> fetchSubDevices(
    DevicesOutput devicesOutputObj,
  ) async {
    selectedDevice = devicesOutputObj;
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchSubDevices({
        'DevicesId': devicesOutputObj.devicesId?.toString() ?? '0',
        'CampDate': selectedCampDate,
        'DISTLGDCODE': deviceMappingManager.dISTLGDCODE,
        'LabCode': deviceMappingManager.labCode,
      });
      return response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void onSubDevicesSelected(dynamic selected) {
    selectedDevice.subDeviceList = selected;
    update();
  }

  Future<List<SubResourceOutput>> fetchSubResources(
    ResourceOutput devicesOutput,
  ) async {
    selectedResource = devicesOutput;
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchSubResources({
        'TestId': devicesOutput.testId.toString(),
        'Campdate': selectedCampDate,
        'DISTLGDCODE': deviceMappingManager.dISTLGDCODE,
        'PartnerID': '1',
        'LabCode': deviceMappingManager.labCode,
      });
      final list = response.output ?? [];
      list.sort((a, b) {
        final nameA = a.resourceName?.trim() ?? '';
        final nameB = b.resourceName?.trim() ?? '';
        return nameA.toLowerCase().compareTo(nameB.toLowerCase());
      });
      return list;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void onSubResourcesSelected(dynamic selected) {
    selectedResource?.subResourceList = selected;
    update();
  }

  Future<void> getResourcesAllocation() async {
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchResources();
      deviceMappingManager.resourceList = response.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  String subDeviceSelected(DevicesOutput devicesOutputObj) {
    return devicesOutputObj.subDeviceList.isEmpty
        ? ''
        : '${devicesOutputObj.subDeviceList.length}';
  }

  String subResourcesSelected(ResourceOutput selectedSubResourceObj) {
    return selectedSubResourceObj.subResourceList.isEmpty
        ? ''
        : '${selectedSubResourceObj.subResourceList.length}';
  }

  String getResourceMappingJsonNew() {
    final List<Map<String, dynamic>> resourceMappingList = [];
    resourceMappingList.add({
      'ResourceName': '',
      'TestId': 3,
      'isChecked': true,
      'USERID': 0,
    });
    print(deviceMappingManager.resourceList.length);
    for (ResourceOutput resource in deviceMappingManager.resourceList) {
      print(resource.subResourceList.length);
      for (SubResourceOutput subResource in resource.subResourceList) {
        final objDict = {
          'ResourceName': subResource.resourceName ?? '',
          'TestId': subResource.testId ?? 0,
          'isChecked': true,
          'USERID': subResource.uSERID ?? 0,
        };
        print(objDict);
        resourceMappingList.add(objDict);
      }
    }
    print(resourceMappingList);
    try {
      final json = jsonEncode(resourceMappingList);
      print(json);
      return json;
    } catch (e) {
      print('Error serializing JSON: $e');
      return '';
    }
  }

  String getDeviceMappingJsonNew() {
    final List<Map<String, dynamic>> deviceMappingList = [];
    for (var resource in deviceMappingManager.deviceList) {
      for (var selected in resource.subDeviceList) {
        final objDict = {
          'DeviceCompName': selected.deviceCompName ?? '',
          'DeviceModel': selected.deviceModel ?? '',
          'DeviceSerial': selected.deviceSerial ?? '',
          'DevicesId': selected.devicesId ?? 0,
          'ISActive': 'null',
          'isChecked': true,
          'SubDevicesId': selected.subDevicesId ?? 0,
        };
        print(objDict);
        deviceMappingList.add(objDict);
      }
    }
    print(deviceMappingList);
    try {
      final json = jsonEncode(deviceMappingList);
      print(json);
      return json;
    } catch (e) {
      print('Error serializing JSON: $e');
      return '';
    }
  }

  Future<void> submitData() async {
    String resourceMappingJsonNew;
    String deviceMappingJsonNew;

    if (deviceMappingManager.isSkipFlag == '1') {
      deviceMappingJsonNew =
          '[{"DeviceCompName":"BP-Bavdhan","DeviceModel":"","DeviceSerial":"BP-789","DevicesId":0,"ISActive":null,"isChecked":true,"SubDevicesId":0},{"DeviceCompName":"DA-Bavdhan","DeviceModel":"","DeviceSerial":"DA-987","DevicesId":0,"ISActive":null,"isChecked":true,"SubDevicesId":0},{"DeviceCompName":"SC-Bavdhan","DeviceModel":"","DeviceSerial":"SC-654","DevicesId":0,"ISActive":null,"isChecked":true,"SubDevicesId":0},{"DeviceCompName":"SP-Pune","DeviceModel":"","DeviceSerial":"SP123","DevicesId":0,"ISActive":null,"isChecked":true,"SubDevicesId":0},{"DeviceCompName":"WM-Bavdhan","DeviceModel":"","DeviceSerial":"WM-456","DevicesId":0,"ISActive":null,"isChecked":true,"SubDevicesId":0}]';
      resourceMappingJsonNew = getResourceMappingJsonNew();
    } else {
      deviceMappingJsonNew = getDeviceMappingJsonNew();
      resourceMappingJsonNew = getResourceMappingJsonNew();
    }

    final Map<String, dynamic> data = {
      'DeviceMappingJson': deviceMappingJsonNew,
      'ResourceMappingJson': resourceMappingJsonNew,
      'UserId': deviceMappingManager.empCode,
      'CampId': deviceMappingManager.campID,
    };
    print(data);

    try {
      await _repository.submitDeviceAllocation(data);
      ToastManager.showSuccessPopup(
        Get.context!,
        icSuccessIcon,
        'Data Added Successfully.',
        () {
          Get.back();
          Get.back();
        },
      );
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  void nextStepScreen() {
    if (selectedCampType == null) {
      toastManager.showAlertMessage(
        Get.context!,
        'Select Camp Type',
        const Color(0xFFEA0000),
      );
      return;
    }
    if (selectedCampId == null) {
      toastManager.showAlertMessage(
        Get.context!,
        'Select Camp ID',
        const Color(0xFFEA0000),
      );
      return;
    }

    if (selectedSegmentIndex == 0) {
      if (deviceMappingManager.isSkipFlag == '0') {
        int deviceRq = 0;
        int subDevicesSelectedCount = 0;
        bool isError = false;

        for (DevicesOutput devices in deviceMappingManager.deviceList) {
          final int subDevices = devices.subDeviceList.length;
          deviceRq += devices.requiredDevice ?? 0;
          if (subDevices == 0) {
            isError = true;
            toastManager.showAlertMessage(
              Get.context!,
              'Select ${devices.requiredDevice ?? 0} device',
              const Color(0xFFEA0000),
            );
          }
          subDevicesSelectedCount += subDevices;
        }
        if (!isError) {
          if (subDevicesSelectedCount >= deviceRq) {
            selectedSegmentIndex = 1;
          }
        } else {
          toastManager.showAlertMessage(
            Get.context!,
            'Please select required devices ($deviceRq) for camp',
            const Color(0xFFEA0000),
          );
        }
      } else {
        selectedSegmentIndex = 1;
      }
    } else if (selectedSegmentIndex == 1) {
      getResourcesAllocation();
      selectedSegmentIndex = 2;
    } else {
      print('Resource allocation');
      bool isError = false;
      int resourcesSelectedCount = 0;

      for (ResourceOutput resource in deviceMappingManager.resourceList) {
        if (resource.subResourceList.isEmpty) {
          isError = true;
          break;
        }
        resourcesSelectedCount += resource.subResourceList.length;
      }
      if (isError) {
        toastManager.showAlertMessage(
          Get.context!,
          'Select at least 1 resource',
          const Color(0xFFEA0000),
        );
      } else {
        if (resourcesSelectedCount >= deviceMappingManager.resourceList.length) {
          submitData();
        } else {
          toastManager.showAlertMessage(
            Get.context!,
            'Please select resources for camp',
            const Color(0xFFEA0000),
          );
        }
      }
    }
    update();
  }

  void skipDeviceMapping() {
    deviceMappingManager.isSkipFlag = '1';
    nextStepScreen();
  }
}
