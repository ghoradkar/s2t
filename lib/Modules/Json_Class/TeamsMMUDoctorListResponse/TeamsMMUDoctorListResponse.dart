// ignore_for_file: file_names

class TeamsMMUDoctorListResponse {
  String? status;
  String? message;
  List<TeamsMMUDoctorListOutput>? output;

  TeamsMMUDoctorListResponse({this.status, this.message, this.output});

  TeamsMMUDoctorListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamsMMUDoctorListOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamsMMUDoctorListOutput.fromJson(v));
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

class TeamsMMUDoctorListOutput {
  int? campid;
  String? campDate;
  int? userID;
  String? teamNumber;
  String? memberName;
  int? desgId;
  String? desgName;
  int? isActive;

  TeamsMMUDoctorListOutput({
    this.campid,
    this.campDate,
    this.userID,
    this.teamNumber,
    this.memberName,
    this.desgId,
    this.desgName,
    this.isActive,
  });

  TeamsMMUDoctorListOutput.fromJson(Map<String, dynamic> json) {
    campid = json['Campid'];
    campDate = json['CampDate'];
    userID = json['UserID'];
    teamNumber = json['TeamNumber'];
    memberName = json['MemberName'];
    desgId = json['DesgId'];
    desgName = json['DesgName'];
    isActive = json['IsActive'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Campid'] = campid;
    data['CampDate'] = campDate;
    data['UserID'] = userID;
    data['TeamNumber'] = teamNumber;
    data['MemberName'] = memberName;
    data['DesgId'] = desgId;
    data['DesgName'] = desgName;
    data['IsActive'] = isActive;
    return data;
  }
}
