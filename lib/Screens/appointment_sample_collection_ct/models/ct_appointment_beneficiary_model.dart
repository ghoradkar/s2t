class CTAppointmentBeneficiaryModel {
  String? status;
  String? message;
  List<CTAppointmentBeneficiaryOutput>? output;

  CTAppointmentBeneficiaryModel({this.status, this.message, this.output});

  CTAppointmentBeneficiaryModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CTAppointmentBeneficiaryOutput>[];
      json['output'].forEach((v) {
        output!.add(CTAppointmentBeneficiaryOutput.fromJson(v));
      });
    }
  }
}

class CTAppointmentBeneficiaryOutput {
  String? t2tOrderId;
  String? treatmentID;
  String? regdNo;
  String? regdId;
  String? beneficiaryName;
  String? mobileNo;
  String? address;
  String? area;
  String? distName;
  String? appointmentDate;
  String? sampleCollection;
  String? relationWithWorker;
  String? arId;
  String? workersMob;
  String? alternateMobNo;
  int? distLgdCode;

  CTAppointmentBeneficiaryOutput({
    this.t2tOrderId,
    this.treatmentID,
    this.regdNo,
    this.regdId,
    this.beneficiaryName,
    this.mobileNo,
    this.address,
    this.area,
    this.distName,
    this.appointmentDate,
    this.sampleCollection,
    this.relationWithWorker,
    this.arId,
    this.workersMob,
    this.alternateMobNo,
    this.distLgdCode,
  });

  CTAppointmentBeneficiaryOutput.fromJson(Map<String, dynamic> json) {
    t2tOrderId = json['T2T_Order_Id']?.toString() ?? json['T2t_Order_Id']?.toString();
    treatmentID = json['TreatmentID']?.toString();
    regdNo = json['RegdNo']?.toString();
    regdId = json['Regdid']?.toString();
    beneficiaryName = json['BeneficiaryName']?.toString();
    mobileNo = json['MobileNo']?.toString();
    address = json['Address']?.toString();
    area = json['Area']?.toString();
    distName = json['DISTNAME']?.toString() ?? json['dISTNAME']?.toString();
    appointmentDate = json['AppointmentDate']?.toString();
    sampleCollection = json['SampleCollection']?.toString();
    relationWithWorker = json['RelationWithWorker']?.toString();
    arId = json['ArId']?.toString();
    workersMob = json['WorkersMob']?.toString();
    alternateMobNo = json['AlternateMobNo']?.toString();
    distLgdCode = json['DISTLGDCODE'] as int?;
  }
}