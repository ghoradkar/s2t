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
        output!.add(new SubDevicesOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
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
  SubDevicesOutput(
      {this.subDevicesId,
      this.devicesId,
      this.deviceCompName,
      this.deviceModel,
      this.deviceSerial});

  SubDevicesOutput.fromJson(Map<String, dynamic> json) {
    subDevicesId = json['SubDevicesId'];
    devicesId = json['DevicesId'];
    deviceCompName = json['DeviceCompName'];
    deviceModel = json['DeviceModel'];
    deviceSerial = json['DeviceSerial'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['SubDevicesId'] = this.subDevicesId;
    data['DevicesId'] = this.devicesId;
    data['DeviceCompName'] = this.deviceCompName;
    data['DeviceModel'] = this.deviceModel;
    data['DeviceSerial'] = this.deviceSerial;
    return data;
  }
}
