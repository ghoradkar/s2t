// ignore_for_file: file_names

class SelectedTeamsDataListResponse {
  String? status;
  String? message;
  List<SelectedTeamsDataLisOutput>? output;

  SelectedTeamsDataListResponse({this.status, this.message, this.output});

  SelectedTeamsDataListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SelectedTeamsDataLisOutput>[];
      json['output'].forEach((v) {
        output!.add(SelectedTeamsDataLisOutput.fromJson(v));
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

class SelectedTeamsDataLisOutput {
  String? isTeamActive;
  int? labCode;
  String? labName;
  String? member1;
  String? member1MOB;
  String? member2;
  String? member2MOB;
  int? memberUserID1;
  int? memberUserID2;
  int? teamID;
  int? teamid;
  String? teamName;
  String? teamNumber;
  bool selected = false;

  SelectedTeamsDataLisOutput({
    this.isTeamActive,
    this.labCode,
    this.labName,
    this.member1,
    this.member1MOB,
    this.member2,
    this.member2MOB,
    this.memberUserID1,
    this.memberUserID2,
    this.teamID,
    this.teamid,
    this.teamName,
    this.teamNumber,
  });

  SelectedTeamsDataLisOutput.fromJson(Map<String, dynamic> json) {
    isTeamActive = json['IsTeamActive'];
    labCode = json['LabCode'];
    labName = json['LabName'];
    member1 = json['Member1'];
    member1MOB = json['Member1MOB'];
    member2 = json['Member2'];
    member2MOB = json['Member2MOB'];
    memberUserID1 = json['MemberUserID1'];
    memberUserID2 = json['MemberUserID2'];
    teamID = json['TeamID'];
    teamid = json['teamid'];
    teamName = json['TeamName'];
    teamNumber = json['TeamNumber'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['IsTeamActive'] = isTeamActive;
    data['LabCode'] = labCode;
    data['LabName'] = labName;
    data['Member1'] = member1;
    data['Member1MOB'] = member1MOB;
    data['Member2'] = member2;
    data['Member2MOB'] = member2MOB;
    data['MemberUserID1'] = memberUserID1;
    data['MemberUserID2'] = memberUserID2;
    data['TeamID'] = teamID;
    data['teamid'] = teamid;
    data['TeamName'] = teamName;
    data['TeamNumber'] = teamNumber;
    return data;
  }
}
