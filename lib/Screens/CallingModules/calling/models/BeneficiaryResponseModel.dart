class Beneficiaryresponsemodel {
  String? status;
  String? message;
  List<BeneficiaryOutput>? output;

  Beneficiaryresponsemodel({this.status, this.message, this.output});

  Beneficiaryresponsemodel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiaryOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryOutput.fromJson(v));
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

class BeneficiaryOutput {
  int? assignCallID;
  int? beneficiaryNo;
  String? beneficiaryName;
  String? nextRenewalDate;
  int? assignStatusID;
  String? assignStatus;
  String? callingStatus;
  int? colourSrNo;
  int? groupID;
  String? mobile;
  int? noOfDependants;
  int? dependantScreeningPending;
  String? isWorkerScreened;
  String? appoinmentDate;
  String? appoinmentTime;
  int? workerDependantScreeningStatus;
  String? lastScreeningDate;
  String? concatNameMobile;
  String? updatedDate;
  String? area;
  int? age;
  String? phleboCallingStatus;
  String? phleboRemark;

  BeneficiaryOutput({
    this.assignCallID,
    this.beneficiaryNo,
    this.beneficiaryName,
    this.nextRenewalDate,
    this.assignStatusID,
    this.assignStatus,
    this.callingStatus,
    this.colourSrNo,
    this.groupID,
    this.mobile,
    this.noOfDependants,
    this.dependantScreeningPending,
    this.isWorkerScreened,
    this.appoinmentDate,
    this.appoinmentTime,
    this.workerDependantScreeningStatus,
    this.lastScreeningDate,
    this.concatNameMobile,
    this.updatedDate,
    this.area,
    this.age,
    this.phleboCallingStatus,
    this.phleboRemark,
  });

  BeneficiaryOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    beneficiaryNo =
        json['BeneficiaryNo'] is String
            ? int.parse(json['BeneficiaryNo'])
            : json['BeneficiaryNo'];
    beneficiaryName = json['BeneficiaryName'];
    nextRenewalDate = json['NextRenewalDate'];
    assignStatusID = json['AssignStatusID'];
    assignStatus = json['AssignStatus'];
    callingStatus = json['CallingStatus'];
    colourSrNo = json['ColourSrNo'];
    groupID = json['GroupID'];
    mobile = json['Mobile'];
    noOfDependants = json['NoOfDependants'];
    dependantScreeningPending = json['DependantScreeningPending'];
    isWorkerScreened = json['IsWorkerScreened'];
    appoinmentDate = json['AppoinmentDate'];
    appoinmentTime = json['AppoinmentTime'];
    workerDependantScreeningStatus = json['WorkerDependantScreeningStatus'];
    lastScreeningDate = json['LastScreeningDate'];
    concatNameMobile = json['ConcatNameMobile'];
    updatedDate = json['UpdatedDate'] ?? '';
    area = json['Area'];
    age = json['Age'];
    phleboCallingStatus = json['PhleboCallingStatus'];
    phleboRemark = json['PhleboRemark'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignCallID'] = assignCallID;
    data['BeneficiaryNo'] = beneficiaryNo;
    data['BeneficiaryName'] = beneficiaryName;
    data['NextRenewalDate'] = nextRenewalDate;
    data['AssignStatusID'] = assignStatusID;
    data['AssignStatus'] = assignStatus;
    data['CallingStatus'] = callingStatus;
    data['ColourSrNo'] = colourSrNo;
    data['GroupID'] = groupID;
    data['Mobile'] = mobile;
    data['NoOfDependants'] = noOfDependants;
    data['DependantScreeningPending'] = dependantScreeningPending;
    data['IsWorkerScreened'] = isWorkerScreened;
    data['AppoinmentDate'] = appoinmentDate;
    data['AppoinmentTime'] = appoinmentTime;
    data['WorkerDependantScreeningStatus'] = workerDependantScreeningStatus;
    data['LastScreeningDate'] = lastScreeningDate;
    data['ConcatNameMobile'] = concatNameMobile;
    data['UpdatedDate'] = updatedDate;
    data['Area'] = area;
    data['Age'] = age;
    data['PhleboCallingStatus'] = phleboCallingStatus;
    data['PhleboRemark'] = phleboRemark;
    return data;
  }
}
