// ignore_for_file: file_names

class AppoinmentExpectedBeneficiariesResponse {
  String? status;
  String? message;
  List<AppoinmentExpectedBeneficiariesOutput>? output;

  AppoinmentExpectedBeneficiariesResponse({
    this.status,
    this.message,
    this.output,
  });

  AppoinmentExpectedBeneficiariesResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AppoinmentExpectedBeneficiariesOutput>[];
      json['output'].forEach((v) {
        // ignore: unnecessary_new
        output!.add(new AppoinmentExpectedBeneficiariesOutput.fromJson(v));
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

class AppoinmentExpectedBeneficiariesOutput {
  int? assignCallID;
  String? beneficiaryNo;
  String? beneficiaryName;
  String? nextRenewalDate;
  int? assignStatusID;
  String? assignStatus;
  int? colourSrNo;
  String? road;
  String? landMark;
  String? area;
  String? gender;
  String? regAddress;
  String? mobile;
  String? pincode;
  String? appoinmentDateTime;
  int? dependantScreeningPending;
  String? a2;
  String? remark;
  String? teamNo;
  String? isWorkerScreened;
  int? workerScreeninPending;
  int? workerDependantScreeningStatus;
  String? appoinmentDate;
  String? dependantScreeningStatus;
  int? d2DCallingRemarkID;
  String? d2DCallingRemark;
  String? phleboRemark;
  int? phleboRemarkID;
  int? noOfDependants;
  int? age;

  AppoinmentExpectedBeneficiariesOutput({
    this.assignCallID,
    this.beneficiaryNo,
    this.beneficiaryName,
    this.nextRenewalDate,
    this.assignStatusID,
    this.assignStatus,
    this.colourSrNo,
    this.road,
    this.landMark,
    this.area,
    this.gender,
    this.regAddress,
    this.mobile,
    this.pincode,
    this.appoinmentDateTime,
    this.dependantScreeningPending,
    this.a2,
    this.remark,
    this.teamNo,
    this.isWorkerScreened,
    this.workerScreeninPending,
    this.workerDependantScreeningStatus,
    this.appoinmentDate,
    this.dependantScreeningStatus,
    this.d2DCallingRemarkID,
    this.d2DCallingRemark,
    this.phleboRemark,
    this.phleboRemarkID,
    this.noOfDependants,
    this.age,
  });

  AppoinmentExpectedBeneficiariesOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    beneficiaryNo = json['BeneficiaryNo'];
    beneficiaryName = json['BeneficiaryName'];
    nextRenewalDate = json['NextRenewalDate'];
    assignStatusID = json['AssignStatusID'];
    assignStatus = json['AssignStatus'];
    colourSrNo = json['ColourSrNo'];
    road = json['Road'];
    landMark = json['LandMark'];
    area = json['Area'];
    gender = json['Gender'];
    regAddress = json['RegAddress'];
    mobile = json['Mobile'];
    pincode = json['Pincode'];
    appoinmentDateTime = json['AppoinmentDateTime'];
    dependantScreeningPending = json['DependantScreeningPending'];
    a2 = json['A2'];
    remark = json['Remark'];
    teamNo = json['TeamNo'];
    isWorkerScreened = json['IsWorkerScreened'];
    workerScreeninPending = json['WorkerScreeninPending'];
    workerDependantScreeningStatus = json['WorkerDependantScreeningStatus'];
    appoinmentDate = json['AppoinmentDate'];
    dependantScreeningStatus = json['DependantScreeningStatus'];
    d2DCallingRemarkID = json['D2DCallingRemarkID'];
    d2DCallingRemark = json['D2DCallingRemark'];
    phleboRemark = json['PhleboRemark'];
    phleboRemarkID = json['PhleboRemarkID'];
    noOfDependants = json['NoOfDependants'];
    age = json['Age'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignCallID'] = assignCallID;
    data['BeneficiaryNo'] = beneficiaryNo;
    data['BeneficiaryName'] = beneficiaryName;
    data['NextRenewalDate'] = nextRenewalDate;
    data['AssignStatusID'] = assignStatusID;
    data['AssignStatus'] = assignStatus;
    data['ColourSrNo'] = colourSrNo;
    data['Road'] = road;
    data['LandMark'] = landMark;
    data['Area'] = area;
    data['Gender'] = gender;
    data['RegAddress'] = regAddress;
    data['Mobile'] = mobile;
    data['Pincode'] = pincode;
    data['AppoinmentDateTime'] = appoinmentDateTime;
    data['DependantScreeningPending'] = dependantScreeningPending;
    data['A2'] = a2;
    data['Remark'] = remark;
    data['TeamNo'] = teamNo;
    data['IsWorkerScreened'] = isWorkerScreened;
    data['WorkerScreeninPending'] = workerScreeninPending;
    data['WorkerDependantScreeningStatus'] = workerDependantScreeningStatus;
    data['AppoinmentDate'] = appoinmentDate;
    data['DependantScreeningStatus'] = dependantScreeningStatus;
    data['D2DCallingRemarkID'] = d2DCallingRemarkID;
    data['D2DCallingRemark'] = d2DCallingRemark;
    data['PhleboRemark'] = phleboRemark;
    data['PhleboRemarkID'] = phleboRemarkID;
    data['NoOfDependants'] = noOfDependants;
    data['Age'] = age;
    return data;
  }
}
