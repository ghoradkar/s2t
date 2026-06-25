// ignore_for_file: file_names

class SubDevicesListResponse {
  String? status;
  String? message;
  List<SubDevicesOutput>? output;

  SubDevicesListResponse({this.status, this.message, this.output});

  SubDevicesListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SubDevicesOutput>[];
      json['output'].forEach((v) {
        output!.add(SubDevicesOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class SubDevicesOutput {
  int? subDevicesId;
  int? devicesId;
  String? deviceCompName;
  String? deviceModel;
  String? deviceSerial;
  bool isSelected = false;

  SubDevicesOutput({
    this.subDevicesId,
    this.devicesId,
    this.deviceCompName,
    this.deviceModel,
    this.deviceSerial,
  });

  SubDevicesOutput.fromJson(Map<String, dynamic> json) {
    subDevicesId = json['SubDevicesId'];
    devicesId = json['DevicesId'];
    deviceCompName = json['DeviceCompName'];
    deviceModel = json['DeviceModel'];
    deviceSerial = json['DeviceSerial'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['SubDevicesId'] = subDevicesId;
    data['DevicesId'] = devicesId;
    data['DeviceCompName'] = deviceCompName;
    data['DeviceModel'] = deviceModel;
    data['DeviceSerial'] = deviceSerial;
    return data;
  }
}
