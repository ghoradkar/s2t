import '../SubDevicesListResponse/SubDevicesListResponse.dart';

class DevicesListResponse {
  String? status;
  String? message;
  List<DevicesOutput>? output;

  DevicesListResponse({this.status, this.message, this.output});

  DevicesListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <DevicesOutput>[];
      json['output'].forEach((v) {
        output!.add(DevicesOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class DevicesOutput {
  int? devicesId;
  String? deviceName;
  int? expectedBeneficary;
  int? requiredDevice;
  List<SubDevicesOutput> subDeviceList = [];

  DevicesOutput(
      {this.devicesId,
      this.deviceName,
      this.expectedBeneficary,
      this.requiredDevice});

  DevicesOutput.fromJson(Map<String, dynamic> json) {
    devicesId = json['DevicesId'];
    deviceName = json['DeviceName'];
    expectedBeneficary = json['ExpectedBeneficary'];
    requiredDevice = json['RequiredDevice'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['DevicesId'] = devicesId;
    data['DeviceName'] = deviceName;
    data['ExpectedBeneficary'] = expectedBeneficary;
    data['RequiredDevice'] = requiredDevice;
    return data;
  }
}
