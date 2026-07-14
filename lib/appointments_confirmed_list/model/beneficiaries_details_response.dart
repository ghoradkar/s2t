// ignore_for_file: file_names

class BeneficiariesDetailsResponse {
  String? status;
  String? message;
  List<BeneficiariesDetailsOutput>? output;

  BeneficiariesDetailsResponse({this.status, this.message, this.output});

  BeneficiariesDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiariesDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiariesDetailsOutput.fromJson(v));
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

class BeneficiariesDetailsOutput {
  int? assignCallID;
  int? beneficiaryNo;
  String? firstName;
  String? middleName;
  String? lastName;
  int? dIVID;
  String? division;
  int? dISTLGDCODE;
  String? district;
  int? tALLGDCODE;
  String? taluka;
  String? houseNo;
  String? road;
  String? landMark;
  String? area;
  String? regAddress;
  int? pincode;
  int? insertUpdate;
  bool? isAddressChanged;
  String? appoinmentDate;
  String? appoinmentTime;
  int? callingLog;
  String? remark;
  String? workersGender;
  String? workersMaritalStatus;
  String? gender;
  String? altMobileNo;
  int? remarkID;
  String? phleboRemark;
  int? phleboRemarkID;

  BeneficiariesDetailsOutput({
    this.assignCallID,
    this.beneficiaryNo,
    this.firstName,
    this.middleName,
    this.lastName,
    this.dIVID,
    this.division,
    this.dISTLGDCODE,
    this.district,
    this.tALLGDCODE,
    this.taluka,
    this.houseNo,
    this.road,
    this.landMark,
    this.area,
    this.regAddress,
    this.pincode,
    this.insertUpdate,
    this.isAddressChanged,
    this.appoinmentDate,
    this.appoinmentTime,
    this.callingLog,
    this.remark,
    this.workersGender,
    this.workersMaritalStatus,
    this.gender,
    this.altMobileNo,
    this.remarkID,
    this.phleboRemark,
    this.phleboRemarkID,
  });

  BeneficiariesDetailsOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    beneficiaryNo = json['BeneficiaryNo'];
    firstName = json['FirstName'];
    middleName = json['MiddleName'];
    lastName = json['LastName'];
    dIVID = json['DIVID'];
    division = json['Division'];
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
    tALLGDCODE = json['TALLGDCODE'];
    taluka = json['Taluka'];
    houseNo = json['HouseNo'];
    road = json['Road'];
    landMark = json['LandMark'];
    area = json['Area'];
    regAddress = json['RegAddress'];
    pincode = json['Pincode'];
    insertUpdate = json['InsertUpdate'];
    isAddressChanged = json['IsAddressChanged'];
    appoinmentDate = json['AppoinmentDate'];
    appoinmentTime = json['AppoinmentTime'];
    callingLog = json['CallingLog'];
    remark = json['Remark'];
    workersGender = json['WorkersGender'];
    workersMaritalStatus = json['WorkersMaritalStatus'];
    gender = json['Gender'];
    altMobileNo = json['AltMobileNo'];
    remarkID = json['RemarkID'];
    phleboRemark = json['PhleboRemark'];
    phleboRemarkID = json['PhleboRemarkID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignCallID'] = assignCallID;
    data['BeneficiaryNo'] = beneficiaryNo;
    data['FirstName'] = firstName;
    data['MiddleName'] = middleName;
    data['LastName'] = lastName;
    data['DIVID'] = dIVID;
    data['Division'] = division;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['District'] = district;
    data['TALLGDCODE'] = tALLGDCODE;
    data['Taluka'] = taluka;
    data['HouseNo'] = houseNo;
    data['Road'] = road;
    data['LandMark'] = landMark;
    data['Area'] = area;
    data['RegAddress'] = regAddress;
    data['Pincode'] = pincode;
    data['InsertUpdate'] = insertUpdate;
    data['IsAddressChanged'] = isAddressChanged;
    data['AppoinmentDate'] = appoinmentDate;
    data['AppoinmentTime'] = appoinmentTime;
    data['CallingLog'] = callingLog;
    data['Remark'] = remark;
    data['WorkersGender'] = workersGender;
    data['WorkersMaritalStatus'] = workersMaritalStatus;
    data['Gender'] = gender;
    data['AltMobileNo'] = altMobileNo;
    data['RemarkID'] = remarkID;
    data['PhleboRemark'] = phleboRemark;
    data['PhleboRemarkID'] = phleboRemarkID;
    return data;
  }
}
