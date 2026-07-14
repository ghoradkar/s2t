// ignore_for_file: file_names

class PacketCollectionResponse {
  String? status;
  String? message;
  List<PacketCollectionOutput>? output;

  PacketCollectionResponse({this.status, this.message, this.output});

  PacketCollectionResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <PacketCollectionOutput>[];
      json['output'].forEach((v) {
        output!.add(PacketCollectionOutput.fromJson(v));
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

class PacketCollectionOutput {
  int? labCode;
  String? labName;
  String? collectFrom;
  String? packetNumber;
  String? dISTNAME;
  bool isSelected = false;

  PacketCollectionOutput({
    this.labCode,
    this.labName,
    this.collectFrom,
    this.packetNumber,
    this.dISTNAME,
  });

  PacketCollectionOutput.fromJson(Map<String, dynamic> json) {
    labCode = json['Labcode'];
    labName = json['LabName'];
    collectFrom = json['CollectFrom'];
    packetNumber = json['PacketNumber'];
    dISTNAME = json['DISTNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Labcode'] = labCode;
    data['LabName'] = labName;
    data['CollectFrom'] = collectFrom;
    data['PacketNumber'] = packetNumber;
    data['DISTNAME'] = dISTNAME;
    return data;
  }
}

class PacketCollectionInsertResponse {
  String? status;
  String? message;

  PacketCollectionInsertResponse({this.status, this.message});

  PacketCollectionInsertResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    return data;
  }
}
