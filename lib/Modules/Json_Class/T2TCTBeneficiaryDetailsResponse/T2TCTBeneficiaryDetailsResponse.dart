// ignore_for_file: file_names

class T2TCTBeneficiaryDetailsResponse {
  String? status;
  String? message;
  List<T2TCTBeneficiaryDetailsOutput>? output;

  T2TCTBeneficiaryDetailsResponse({this.status, this.message, this.output});

  T2TCTBeneficiaryDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <T2TCTBeneficiaryDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(T2TCTBeneficiaryDetailsOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    // ignore: prefer_collection_literals
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class T2TCTBeneficiaryDetailsOutput {
  int? campId;
  int? regdid;
  String? regdno;
  String? beneficiaryName;
  int? landingLab;
  String? landingLabName;
  String? pinCode;
  String? area;
  String? address;
  int? dISTLGDCODE;
  String? dISTNAME;
  int? tALLGDCODE;
  String? tALNAME;
  String? sampleCollection;
  String? assignTeamid;
  String? isTeamAssign;
  int? arId;
  String? assignmentRemarks;
  String? appointmentDate;

  T2TCTBeneficiaryDetailsOutput({
    this.campId,
    this.regdid,
    this.regdno,
    this.beneficiaryName,
    this.landingLab,
    this.landingLabName,
    this.pinCode,
    this.area,
    this.address,
    this.dISTLGDCODE,
    this.dISTNAME,
    this.tALLGDCODE,
    this.tALNAME,
    this.sampleCollection,
    this.assignTeamid,
    this.isTeamAssign,
    this.arId,
    this.assignmentRemarks,
    this.appointmentDate,
  });

  T2TCTBeneficiaryDetailsOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    regdid = json['Regdid'];
    regdno = json['Regdno'];
    beneficiaryName = json['BeneficiaryName'];
    landingLab = json['LandingLab'];
    landingLabName = json['LandingLabName'];
    pinCode = json['PinCode'];
    area = json['Area'];
    address = json['Address'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    tALLGDCODE = json['TALLGDCODE'];
    tALNAME = json['TALNAME'];
    sampleCollection = json['SampleCollection'];
    assignTeamid = json['AssignTeamid'];
    isTeamAssign = json['IsTeamAssign'];
    arId = json['ArId'];
    assignmentRemarks = json['AssignmentRemarks'];
    appointmentDate = json['AppointmentDate'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['Regdid'] = regdid;
    data['Regdno'] = regdno;
    data['BeneficiaryName'] = beneficiaryName;
    data['LandingLab'] = landingLab;
    data['LandingLabName'] = landingLabName;
    data['PinCode'] = pinCode;
    data['Area'] = area;
    data['Address'] = address;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['TALLGDCODE'] = tALLGDCODE;
    data['TALNAME'] = tALNAME;
    data['SampleCollection'] = sampleCollection;
    data['AssignTeamid'] = assignTeamid;
    data['IsTeamAssign'] = isTeamAssign;
    data['ArId'] = arId;
    data['AssignmentRemarks'] = assignmentRemarks;
    data['AppointmentDate'] = appointmentDate;
    return data;
  }
}
