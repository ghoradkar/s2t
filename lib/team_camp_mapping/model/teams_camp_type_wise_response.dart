// ignore_for_file: file_names

class TeamsCampTypeWiseResponse {
  String? status;
  String? message;
  List<TeamsCampTypeWiseOutput>? output;

  TeamsCampTypeWiseResponse({this.status, this.message, this.output});

  TeamsCampTypeWiseResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamsCampTypeWiseOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamsCampTypeWiseOutput.fromJson(v));
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

class TeamsCampTypeWiseOutput {
  int? teamid;
  String? teamname;
  int? memberUserID1;
  int? memberUserID2;
  String? member1;
  String? member2;
  int? labcode;
  int? isTeamActive;
  bool selected = false;
  TeamsCampTypeWiseOutput({
    this.teamid,
    this.teamname,
    this.memberUserID1,
    this.memberUserID2,
    this.member1,
    this.member2,
    this.labcode,
    this.isTeamActive,
  });

  TeamsCampTypeWiseOutput.fromJson(Map<String, dynamic> json) {
    teamid = json['teamid'];
    teamname = json['teamname'];
    memberUserID1 = json['MemberUserID1'];
    memberUserID2 = json['MemberUserID2'];
    member1 = json['Member1'];
    member2 = json['Member2'];
    labcode = json['labcode'];
    isTeamActive = json['IsTeamActive'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['teamid'] = teamid;
    data['teamname'] = teamname;
    data['MemberUserID1'] = memberUserID1;
    data['MemberUserID2'] = memberUserID2;
    data['Member1'] = member1;
    data['Member2'] = member2;
    data['labcode'] = labcode;
    data['IsTeamActive'] = isTeamActive;
    return data;
  }
}
