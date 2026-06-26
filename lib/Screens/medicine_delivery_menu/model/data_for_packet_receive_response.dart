// ignore_for_file: file_names

class DataForPacketReceiveResponse {
  String? status;
  String? message;
  List<DataForPacketReceiveOutput>? output;

  DataForPacketReceiveResponse({this.status, this.message, this.output});

  DataForPacketReceiveResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <DataForPacketReceiveOutput>[];
      json['output'].forEach((v) {
        output!.add(DataForPacketReceiveOutput.fromJson(v));
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

class DataForPacketReceiveOutput {
  int? labcode;
  String? labName;
  String? collectFrom;
  String? packetNumber;
  String? dISTNAME;
  bool isSelected = false;
  DataForPacketReceiveOutput({
    this.labcode,
    this.labName,
    this.collectFrom,
    this.packetNumber,
    this.dISTNAME,
  });

  DataForPacketReceiveOutput.fromJson(Map<String, dynamic> json) {
    labcode = json['Labcode'];
    labName = json['LabName'];
    collectFrom = json['CollectFrom'];
    packetNumber = json['PacketNumber'];
    dISTNAME = json['DISTNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Labcode'] = labcode;
    data['LabName'] = labName;
    data['CollectFrom'] = collectFrom;
    data['PacketNumber'] = packetNumber;
    data['DISTNAME'] = dISTNAME;
    return data;
  }
}
