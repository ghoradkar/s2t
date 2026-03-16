// ignore_for_file: file_names

class ConfirmatoryTestsScreeningResponse {
  String? status;
  String? message;
  List<ConfirmatoryTestsScreeningOutput>? output;

  ConfirmatoryTestsScreeningResponse({this.status, this.message, this.output});

  ConfirmatoryTestsScreeningResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ConfirmatoryTestsScreeningOutput>[];
      json['output'].forEach((v) {
        output!.add(ConfirmatoryTestsScreeningOutput.fromJson(v));
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

class ConfirmatoryTestsScreeningOutput {
  int? regdid;
  String? regdNo;
  String? beneficiaryName;
  int? patAge;
  String? gender;
  String? mobileNo;
  String? relationWithWorker;
  int? serviceCode;
  int? catCode;
  String? serviceName;
  String? catName;
  String? appointmentDate;
  String? isAppointmentDone;
  String? labName;
  int? labcode;
  String? sampleQuantityMl;
  String? workersMob;
  String? alternateMobNo;

  ConfirmatoryTestsScreeningOutput({
    this.regdid,
    this.regdNo,
    this.beneficiaryName,
    this.patAge,
    this.gender,
    this.mobileNo,
    this.relationWithWorker,
    this.serviceCode,
    this.catCode,
    this.serviceName,
    this.catName,
    this.appointmentDate,
    this.isAppointmentDone,
    this.labName,
    this.labcode,
    this.sampleQuantityMl,
    this.workersMob,
    this.alternateMobNo,
  });

  ConfirmatoryTestsScreeningOutput.fromJson(Map<String, dynamic> json) {
    regdid = json['Regdid'];
    regdNo = json['RegdNo'];
    beneficiaryName = json['BeneficiaryName'];
    patAge = json['PatAge'];
    gender = json['Gender'];
    mobileNo = json['MobileNo'];
    relationWithWorker = json['RelationWithWorker'];
    serviceCode = json['ServiceCode'];
    catCode = json['CatCode'];
    serviceName = json['ServiceName'];
    catName = json['CatName'];
    appointmentDate = json['AppointmentDate'];
    isAppointmentDone = json['IsAppointmentDone'];
    labName = json['LabName'];
    labcode = json['Labcode'];
    sampleQuantityMl = json['SampleQuantity(ml)'];
    workersMob = json['WorkersMob'];
    alternateMobNo = json['AlternateMobNo'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Regdid'] = regdid;
    data['RegdNo'] = regdNo;
    data['BeneficiaryName'] = beneficiaryName;
    data['PatAge'] = patAge;
    data['Gender'] = gender;
    data['MobileNo'] = mobileNo;
    data['RelationWithWorker'] = relationWithWorker;
    data['ServiceCode'] = serviceCode;
    data['CatCode'] = catCode;
    data['ServiceName'] = serviceName;
    data['CatName'] = catName;
    data['AppointmentDate'] = appointmentDate;
    data['IsAppointmentDone'] = isAppointmentDone;
    data['LabName'] = labName;
    data['Labcode'] = labcode;
    data['SampleQuantity(ml)'] = sampleQuantityMl;
    data['WorkersMob'] = workersMob;
    data['AlternateMobNo'] = alternateMobNo;
    return data;
  }
}
