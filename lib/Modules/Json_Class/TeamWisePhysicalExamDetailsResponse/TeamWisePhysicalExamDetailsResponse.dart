class TeamWisePhysicalExamDetailsResponse {
  String? status;
  String? message;
  List<TeamWisePhysicalExamDetailsOutput>? output;

  TeamWisePhysicalExamDetailsResponse({this.status, this.message, this.output});

  TeamWisePhysicalExamDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamWisePhysicalExamDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(new TeamWisePhysicalExamDetailsOutput.fromJson(v));
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

class TeamWisePhysicalExamDetailsOutput {
  int? dISTLGDCODE;
  String? district;
  int? campId;
  String? campDate;
  String? teamid;
  String? teamNo;
  int? userID;
  String? teamNumberName;
  String? desgName;
  String? mobileNo;

  TeamWisePhysicalExamDetailsOutput({
    this.dISTLGDCODE,
    this.district,
    this.campId,
    this.campDate,
    this.teamid,
    this.teamNo,
    this.userID,
    this.teamNumberName,
    this.desgName,
    this.mobileNo,
  });

  TeamWisePhysicalExamDetailsOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
    campId = json['CampId'];
    campDate = json['CampDate'];
    teamid = json['Teamid'];
    teamNo = json['TeamNo'];
    userID = json['UserID'];
    teamNumberName = json['TeamNumberName'];
    desgName = json['DesgName'];
    mobileNo = json['MobileNo'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['DISTLGDCODE'] = this.dISTLGDCODE;
    data['District'] = this.district;
    data['CampId'] = this.campId;
    data['CampDate'] = this.campDate;
    data['Teamid'] = this.teamid;
    data['TeamNo'] = this.teamNo;
    data['UserID'] = this.userID;
    data['TeamNumberName'] = this.teamNumberName;
    data['DesgName'] = this.desgName;
    data['MobileNo'] = this.mobileNo;
    return data;
  }
}
