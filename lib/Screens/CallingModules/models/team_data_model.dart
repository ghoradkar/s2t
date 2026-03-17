class TeamDataModel {
  String? status;
  String? message;
  List<TeamDataOutput>? output;

  TeamDataModel({this.status, this.message, this.output});

  TeamDataModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamDataOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamDataOutput.fromJson(v));
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

class TeamDataOutput {
  int? teamid;
  String? teamName;
  String? member1;
  String? memberDesg1;
  String? member2;
  String? memberDesg2;

  TeamDataOutput(
      {this.teamid,
      this.teamName,
      this.member1,
      this.memberDesg1,
      this.member2,
      this.memberDesg2});

  TeamDataOutput.fromJson(Map<String, dynamic> json) {
    teamid = json['Teamid'];
    teamName = json['TeamName'];
    member1 = json['Member1'];
    memberDesg1 = json['MemberDesg1'];
    member2 = json['Member2'];
    memberDesg2 = json['MemberDesg2'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Teamid'] = teamid;
    data['TeamName'] = teamName;
    data['Member1'] = member1;
    data['MemberDesg1'] = memberDesg1;
    data['Member2'] = member2;
    data['MemberDesg2'] = memberDesg2;
    return data;
  }
}

