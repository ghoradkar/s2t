// ignore_for_file: file_names

class PatientListReAllocationforMedicineDeliveryResponse {
  String? status;
  String? message;
  List<PatientListReAllocationforMedicineDeliveryOutput>? output;

  PatientListReAllocationforMedicineDeliveryResponse({
    this.status,
    this.message,
    this.output,
  });

  PatientListReAllocationforMedicineDeliveryResponse.fromJson(
    Map<String, dynamic> json,
  ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <PatientListReAllocationforMedicineDeliveryOutput>[];
      json['output'].forEach((v) {
        output!.add(
          PatientListReAllocationforMedicineDeliveryOutput.fromJson(v),
        );
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

class PatientListReAllocationforMedicineDeliveryOutput {
  int? dISTLGDCODE;
  String? deliveryChallanID;
  String? district;
  String? labName;
  int? labcode;
  String? overallStatus;
  int? overallStatusID;
  int? pINCODE;
  String? packetNumber;
  String? patientName;
  int? prescriptionID;
  int? tALLGDCODE;
  String? taluka;
  bool isSelected = false;

  PatientListReAllocationforMedicineDeliveryOutput({
    this.dISTLGDCODE,
    this.deliveryChallanID,
    this.district,
    this.labName,
    this.labcode,
    this.overallStatus,
    this.overallStatusID,
    this.pINCODE,
    this.packetNumber,
    this.patientName,
    this.prescriptionID,
    this.tALLGDCODE,
    this.taluka,
  });

  PatientListReAllocationforMedicineDeliveryOutput.fromJson(
    Map<String, dynamic> json,
  ) {
    dISTLGDCODE = json['DISTLGDCODE'];
    deliveryChallanID = json['DeliveryChallanID'];
    district = json['District'];
    labName = json['LabName'];
    labcode = json['Labcode'];
    overallStatus = json['OverallStatus'];
    overallStatusID = json['OverallStatusID'];
    pINCODE = json['PINCODE'];
    packetNumber = json['PacketNumber'];
    patientName = json['PatientName'];
    prescriptionID = json['PrescriptionID'];
    tALLGDCODE = json['TALLGDCODE'];
    taluka = json['Taluka'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DeliveryChallanID'] = deliveryChallanID;
    data['District'] = district;
    data['LabName'] = labName;
    data['Labcode'] = labcode;
    data['OverallStatus'] = overallStatus;
    data['OverallStatusID'] = overallStatusID;
    data['PINCODE'] = pINCODE;
    data['PacketNumber'] = packetNumber;
    data['PatientName'] = patientName;
    data['PrescriptionID'] = prescriptionID;
    data['TALLGDCODE'] = tALLGDCODE;
    data['Taluka'] = taluka;
    return data;
  }
}
