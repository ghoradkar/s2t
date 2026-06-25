// ignore_for_file: file_names

class TeamDetailsListForAssignResponse {
  String? status;
  String? message;
  List<TeamDetailsListForAssignOutput>? output;

  TeamDetailsListForAssignResponse({this.status, this.message, this.output});

  TeamDetailsListForAssignResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamDetailsListForAssignOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamDetailsListForAssignOutput.fromJson(v));
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

class TeamDetailsListForAssignOutput {
  int? campid;
  String? campDate;
  String? teamNumber;
  String? teamName;
  String? member1;
  String? memberDesg1;
  String? member2;
  String? memberDesg2;
  bool selected = false;
  TeamDetailsListForAssignOutput({
    this.campid,
    this.campDate,
    this.teamNumber,
    this.teamName,
    this.member1,
    this.memberDesg1,
    this.member2,
    this.memberDesg2,
  });

  TeamDetailsListForAssignOutput.fromJson(Map<String, dynamic> json) {
    campid = json['Campid'];
    campDate = json['CampDate'];
    teamNumber = json['TeamNumber'];
    teamName = json['TeamName'];
    member1 = json['Member1'];
    memberDesg1 = json['MemberDesg1'];
    member2 = json['Member2'];
    memberDesg2 = json['MemberDesg2'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Campid'] = campid;
    data['CampDate'] = campDate;
    data['TeamNumber'] = teamNumber;
    data['TeamName'] = teamName;
    data['Member1'] = member1;
    data['MemberDesg1'] = memberDesg1;
    data['Member2'] = member2;
    data['MemberDesg2'] = memberDesg2;
    return data;
  }
}
