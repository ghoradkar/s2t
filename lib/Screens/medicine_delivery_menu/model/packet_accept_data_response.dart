// ignore_for_file: file_names

class PacketAcceptDataResponse {
  String? status;
  String? message;
  List<PacketAcceptDataOutput>? output;

  PacketAcceptDataResponse({this.status, this.message, this.output});

  PacketAcceptDataResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <PacketAcceptDataOutput>[];
      json['output'].forEach((v) {
        output!.add(PacketAcceptDataOutput.fromJson(v));
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

class PacketAcceptDataOutput {
  int? dISTLGDCODE;
  String? district;
  int? tALLGDCODE;
  String? taluka;
  int? labcode;
  String? labName;
  int? pINCODE;
  String? deliveryChallanID;
  String? packetNumber;
  int? prescriptionID;
  String? patientName;
  bool isSelected = false;

  PacketAcceptDataOutput({
    this.dISTLGDCODE,
    this.district,
    this.tALLGDCODE,
    this.taluka,
    this.labcode,
    this.labName,
    this.pINCODE,
    this.deliveryChallanID,
    this.packetNumber,
    this.prescriptionID,
    this.patientName,
  });

  PacketAcceptDataOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
    tALLGDCODE = json['TALLGDCODE'];
    taluka = json['Taluka'];
    labcode = json['Labcode'];
    labName = json['LabName'];
    pINCODE = json['PINCODE'];
    deliveryChallanID = json['DeliveryChallanID'];
    packetNumber = json['PacketNumber'];
    prescriptionID = json['PrescriptionID'];
    patientName = json['PatientName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['District'] = district;
    data['TALLGDCODE'] = tALLGDCODE;
    data['Taluka'] = taluka;
    data['Labcode'] = labcode;
    data['LabName'] = labName;
    data['PINCODE'] = pINCODE;
    data['DeliveryChallanID'] = deliveryChallanID;
    data['PacketNumber'] = packetNumber;
    data['PrescriptionID'] = prescriptionID;
    data['PatientName'] = patientName;
    return data;
  }
}
