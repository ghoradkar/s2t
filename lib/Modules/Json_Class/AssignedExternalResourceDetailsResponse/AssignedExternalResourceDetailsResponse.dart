// ignore_for_file: file_names

class AssignedExternalResourceDetailsResponse {
  String? status;
  String? message;
  List<AssignedExternalResourceDetailsOutput>? output;

  AssignedExternalResourceDetailsResponse({
    this.status,
    this.message,
    this.output,
  });

  AssignedExternalResourceDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AssignedExternalResourceDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(AssignedExternalResourceDetailsOutput.fromJson(v));
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

class AssignedExternalResourceDetailsOutput {
  String? campDate;
  int? campid;
  int? desgId;
  String? desgName;
  int? isActive;
  String? memberName;
  String? teamNumber;
  int? userID;
  bool isSelected = false;
  AssignedExternalResourceDetailsOutput({
    this.campDate,
    this.campid,
    this.desgId,
    this.desgName,
    this.isActive,
    this.memberName,
    this.teamNumber,
    this.userID,
  });

  AssignedExternalResourceDetailsOutput.fromJson(Map<String, dynamic> json) {
    campDate = json['CampDate'];
    campid = json['Campid'];
    desgId = json['DesgId'];
    desgName = json['DesgName'];
    isActive = json['IsActive'];
    memberName = json['MemberName'];
    teamNumber = json['TeamNumber'];
    userID = json['UserID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampDate'] = campDate;
    data['Campid'] = campid;
    data['DesgId'] = desgId;
    data['DesgName'] = desgName;
    data['IsActive'] = isActive;
    data['MemberName'] = memberName;
    data['TeamNumber'] = teamNumber;
    data['UserID'] = userID;
    return data;
  }
}
