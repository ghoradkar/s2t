class ScreeningDependentModel {
  String? status;
  String? message;
  List<ScreeningDataOutput>? output;

  ScreeningDependentModel({this.status, this.message, this.output});

  ScreeningDependentModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ScreeningDataOutput>[];
      json['output'].forEach((v) {
        output!.add(new ScreeningDataOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class ScreeningDataOutput {
  int? assignCallID;
  int? relId;
  String? relation;
  String? firstName;
  String? middleName;
  String? lastName;
  String? screeningDate;
  String? age;
  int? noOFScreenedDepedent;

  ScreeningDataOutput(
      {this.assignCallID,
        this.relId,
        this.relation,
        this.firstName,
        this.middleName,
        this.lastName,
        this.screeningDate,
        this.age,
      this.noOFScreenedDepedent});

  ScreeningDataOutput.fromJson(Map<String, dynamic> json) {
    assignCallID = json['AssignCallID'];
    relId = json['RelId'];
    relation = json['Relation'];
    firstName = json['FirstName'];
    middleName = json['MiddleName'];
    lastName = json['LastName'];
    screeningDate = json['ScreeningDate'];
    age = json['Age'];
    noOFScreenedDepedent = json['NoOFScreenedDepedent'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['AssignCallID'] = this.assignCallID;
    data['RelId'] = this.relId;
    data['Relation'] = this.relation;
    data['FirstName'] = this.firstName;
    data['MiddleName'] = this.middleName;
    data['LastName'] = this.lastName;
    data['ScreeningDate'] = this.screeningDate;
    data['Age'] = this.age;
    data['NoOFScreenedDepedent'] = this.noOFScreenedDepedent;
    return data;
  }
}
