// ignore_for_file: file_names

class PacketReturnResponse {
  String? status;
  String? message;
  List<PacketReturnOutput>? output;

  PacketReturnResponse({this.status, this.message, this.output});

  PacketReturnResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <PacketReturnOutput>[];
      json['output'].forEach((v) {
        output!.add(PacketReturnOutput.fromJson(v));
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

class PacketReturnOutput {
  int? medicalDelivaryID;
  int? treatmentID;
  int? regdId;
  String? beneficiryNumber;
  String? patientName;
  String? packetNo;
  String? deliveryChallanID;
  String? deniedDate;
  int? dISTLGDCODE;
  int? tALLGDCODE;
  String? returnStatus;
  bool isSelected = false;

  PacketReturnOutput({
    this.medicalDelivaryID,
    this.treatmentID,
    this.regdId,
    this.beneficiryNumber,
    this.patientName,
    this.packetNo,
    this.deliveryChallanID,
    this.deniedDate,
    this.dISTLGDCODE,
    this.tALLGDCODE,
    this.returnStatus,
  });

  PacketReturnOutput.fromJson(Map<String, dynamic> json) {
    medicalDelivaryID = json['MedicalDelivaryID'];
    treatmentID = json['TreatmentID'];
    regdId = json['RegdId'];
    beneficiryNumber = json['Beneficiry_Number'];
    patientName = json['Patient_Name'];
    packetNo = json['PacketNo'];
    deliveryChallanID = json['DeliveryChallanID'];
    deniedDate = json['DeniedDate'];
    dISTLGDCODE = json['DISTLGDCODE'];
    tALLGDCODE = json['TALLGDCODE'];
    returnStatus = json['ReturnStatus'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['MedicalDelivaryID'] = medicalDelivaryID;
    data['TreatmentID'] = treatmentID;
    data['RegdId'] = regdId;
    data['Beneficiry_Number'] = beneficiryNumber;
    data['Patient_Name'] = patientName;
    data['PacketNo'] = packetNo;
    data['DeliveryChallanID'] = deliveryChallanID;
    data['DeniedDate'] = deniedDate;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['TALLGDCODE'] = tALLGDCODE;
    data['ReturnStatus'] = returnStatus;
    return data;
  }
}
