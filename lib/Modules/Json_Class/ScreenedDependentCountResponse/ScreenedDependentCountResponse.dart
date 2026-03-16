class ScreenedDependentCountResponse {
  String? status;
  String? message;
  List<ScreenedDependentCountOutput>? output;

  ScreenedDependentCountResponse({this.status, this.message, this.output});

  ScreenedDependentCountResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ScreenedDependentCountOutput>[];
      json['output'].forEach((v) {
        output!.add(ScreenedDependentCountOutput.fromJson(v));
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

class ScreenedDependentCountOutput {
  int? assignCallID;
  int? relId;
  String? relation;
  String? firstName;
  String? middleName;
  String? lastName;
  String? screeningDate;
  String? age;

  ScreenedDependentCountOutput({
    this.assignCallID,
    this.relId,
    this.relation,
    this.firstName,
    this.middleName,
    this.lastName,
    this.screeningDate,
    this.age,
  });

  ScreenedDependentCountOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    relId = json['RelId'];
    relation = json['Relation'];
    firstName = json['FirstName'];
    middleName = json['MiddleName'];
    lastName = json['LastName'];
    screeningDate = json['ScreeningDate'];
    age = json['Age'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignCallID'] = assignCallID;
    data['RelId'] = relId;
    data['Relation'] = relation;
    data['FirstName'] = firstName;
    data['MiddleName'] = middleName;
    data['LastName'] = lastName;
    data['ScreeningDate'] = screeningDate;
    data['Age'] = age;
    return data;
  }
}
