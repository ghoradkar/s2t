// ignore_for_file: file_names

class D2DTeamMemberDetailsResponse {
  String? status;
  String? message;
  List<D2DTeamMemberDetailsOutput>? output;

  D2DTeamMemberDetailsResponse({this.status, this.message, this.output});

  D2DTeamMemberDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DTeamMemberDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DTeamMemberDetailsOutput.fromJson(v));
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

class D2DTeamMemberDetailsOutput {
  int? teamid;
  String? teamName;
  int? userID;
  String? memberName;
  String? mOBNO;
  String? desgName;
  String? desgShortCode;

  D2DTeamMemberDetailsOutput({
    this.teamid,
    this.teamName,
    this.userID,
    this.memberName,
    this.mOBNO,
    this.desgName,
    this.desgShortCode,
  });

  D2DTeamMemberDetailsOutput.fromJson(Map<String, dynamic> json) {
    teamid = json['Teamid'];
    teamName = json['TeamName'];
    userID = json['UserID'];
    memberName = json['MemberName'];
    mOBNO = json['MOBNO'];
    desgName = json['DesgName'];
    desgShortCode = json['DesgShortCode'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Teamid'] = teamid;
    data['TeamName'] = teamName;
    data['UserID'] = userID;
    data['MemberName'] = memberName;
    data['MOBNO'] = mOBNO;
    data['DesgName'] = desgName;
    data['DesgShortCode'] = desgShortCode;
    return data;
  }
}
