class AddDependentModel {
  String? status;
  String? message;
  List<AddDependentOutput>? output;

  AddDependentModel({this.status, this.message, this.output});

  AddDependentModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AddDependentOutput>[];
      json['output'].forEach((v) {
        output!.add(AddDependentOutput.fromJson(v));
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

class AddDependentOutput {
  int? assignCallID;
  int? relId;
  String? age;
  String? firstName;
  String? middleName;
  String? lastName;
  int? srNo;
  int? isDependantAdded;
  String? lastDependantScreeningDate;

  AddDependentOutput(
      {this.assignCallID,
      this.relId,
      this.age,
      this.firstName,
      this.middleName,
      this.lastName,
      this.srNo,
      this.isDependantAdded,
      this.lastDependantScreeningDate});

  AddDependentOutput.fromJson(Map<String, dynamic> json) {
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
    data['LastDependantScreeningDate'] = lastDependantScreeningDate;
    return data;
  }
}
