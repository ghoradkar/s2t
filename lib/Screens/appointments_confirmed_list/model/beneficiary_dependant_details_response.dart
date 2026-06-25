// ignore_for_file: file_names

class BeneficiaryDependantDetailsResponse {
  String? status;
  String? message;
  List<BeneficiaryDependantDetailsOutput>? output;

  BeneficiaryDependantDetailsResponse({this.status, this.message, this.output});

  BeneficiaryDependantDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiaryDependantDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryDependantDetailsOutput.fromJson(v));
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

class BeneficiaryDependantDetailsOutput {
  int? assignCallID;
  int? relId;
  String? age;
  String? firstName;
  String? middleName;
  String? lastName;
  int? srNo;
  int? isDependantAdded;
  String? lastDependantScreeningDate;

  BeneficiaryDependantDetailsOutput({
    this.assignCallID,
    this.relId,
    this.age,
    this.firstName,
    this.middleName,
    this.lastName,
    this.srNo,
    this.isDependantAdded,
    this.lastDependantScreeningDate,
  });

  BeneficiaryDependantDetailsOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    relId = json['RelId'];
    age = json['Age'];
    firstName = json['FirstName'];
    middleName = json['MiddleName'];
    lastName = json['LastName'];
    srNo = json['SrNo'];
    isDependantAdded = json['IsDependantAdded'];
    lastDependantScreeningDate = json['LastDependantScreeningDate'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignCallID'] = assignCallID;
    data['RelId'] = relId;
    data['Age'] = age;
    data['FirstName'] = firstName;
    data['MiddleName'] = middleName;
    data['LastName'] = lastName;
    data['SrNo'] = srNo;
    data['IsDependantAdded'] = isDependantAdded;
    data['LastDependantScreeningDate'] = lastDependantScreeningDate;
    return data;
  }
}
