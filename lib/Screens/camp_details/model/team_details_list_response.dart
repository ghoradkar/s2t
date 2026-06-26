// ignore_for_file: file_names

class TeamDetailsListResponse {
  String? status;
  String? message;
  List<TeamDetailsOutput>? output;

  TeamDetailsListResponse({this.status, this.message, this.output});

  TeamDetailsListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamDetailsOutput.fromJson(v));
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

class TeamDetailsOutput {
  String? teamNumber;
  String? teamID;
  String? campNo;
  int? campId;
  String? member1ContactNo;
  String? member1;
  String? member2ContactNo;
  String? member2;
  bool isSelected = false;

  TeamDetailsOutput({
    this.teamNumber,
    this.teamID,
    this.campNo,
    this.campId,
    this.member1ContactNo,
    this.member1,
    this.member2ContactNo,
    this.member2,
  });

  TeamDetailsOutput.fromJson(Map<String, dynamic> json) {
    teamNumber = json['TeamNumber'];
    teamID = json['TeamID'];
    campNo = json['CampNo'];
    campId = json['CampId'];
    member1ContactNo = json['Member1ContactNo'];
    member1 = json['Member1'];
    member2ContactNo = json['Member2ContactNo'];
    member2 = json['Member2'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['TeamNumber'] = teamNumber;
    data['TeamID'] = teamID;
    data['CampNo'] = campNo;
    data['CampId'] = campId;
    data['Member1ContactNo'] = member1ContactNo;
    data['Member1'] = member1;
    data['Member2ContactNo'] = member2ContactNo;
    data['Member2'] = member2;
    return data;
  }
}
