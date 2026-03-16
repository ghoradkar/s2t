// ignore_for_file: file_names

class TeamCCResponse {
  String? status;
  String? message;
  List<TeamCCOutput>? output;

  TeamCCResponse({this.status, this.message, this.output});

  TeamCCResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamCCOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamCCOutput.fromJson(v));
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

class TeamCCOutput {
  String? teamName;
  int? teamid;
  String? member1;
  String? memberDesg1;
  String? member2;
  String? memberDesg2;
  bool selected = false;
  TeamCCOutput({
    this.teamName,
    this.teamid,
    this.member1,
    this.memberDesg1,
    this.member2,
    this.memberDesg2,
  });

  TeamCCOutput.fromJson(Map<String, dynamic> json) {
    teamName = json['TeamName'];
    teamid = json['Teamid'];
    member1 = json['Member1'];
    memberDesg1 = json['MemberDesg1'];
    member2 = json['Member2'];
    memberDesg2 = json['MemberDesg2'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['TeamName'] = teamName;
    data['Teamid'] = teamid;
    data['Member1'] = member1;
    data['MemberDesg1'] = memberDesg1;
    data['Member2'] = member2;
    data['MemberDesg2'] = memberDesg2;
    return data;
  }
}
