// ignore_for_file: file_names

class TeamCampDetailsListResponse {
  String? status;
  String? message;
  List<TeamCampDetailsOutput>? output;

  TeamCampDetailsListResponse({this.status, this.message, this.output});

  TeamCampDetailsListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamCampDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamCampDetailsOutput.fromJson(v));
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

class TeamCampDetailsOutput {
  String? campDate;
  int? campid;
  String? member1;
  String? member2;
  String? memberDesg1;
  String? memberDesg2;
  String? teamName;
  String? teamNumber;

  TeamCampDetailsOutput({
    this.campDate,
    this.campid,
    this.member1,
    this.member2,
    this.memberDesg1,
    this.memberDesg2,
    this.teamName,
    this.teamNumber,
  });

  TeamCampDetailsOutput.fromJson(Map<String, dynamic> json) {
    campDate = json['CampDate'];
    campid = json['Campid'];
    member1 = json['Member1'];
    member2 = json['Member2'];
    memberDesg1 = json['MemberDesg1'];
    memberDesg2 = json['MemberDesg2'];
    teamName = json['TeamName'];
    teamNumber = json['TeamNumber'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampDate'] = campDate;
    data['Campid'] = campid;
    data['Member1'] = member1;
    data['Member2'] = member2;
    data['MemberDesg1'] = memberDesg1;
    data['MemberDesg2'] = memberDesg2;
    data['TeamName'] = teamName;
    data['TeamNumber'] = teamNumber;
    return data;
  }
}
