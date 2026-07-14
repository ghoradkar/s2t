// ignore_for_file: file_names

class ReportDeliveryExecutiveResponse {
  String? status;
  String? message;
  List<ReportDeliveryExecutiveOutput>? output;

  ReportDeliveryExecutiveResponse({this.status, this.message, this.output});

  ReportDeliveryExecutiveResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ReportDeliveryExecutiveOutput>[];
      json['output'].forEach((v) {
        output!.add(ReportDeliveryExecutiveOutput.fromJson(v));
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

class ReportDeliveryExecutiveOutput {
  int? userID;
  String? userName;
  int? tALLGDCODE;
  int? teamid;
  String? teamName;
  String? fIRSTNAME;
  bool isSelected = false;

  ReportDeliveryExecutiveOutput({
    this.userID,
    this.userName,
    this.tALLGDCODE,
    this.teamid,
    this.teamName,
    this.fIRSTNAME,
  });

  ReportDeliveryExecutiveOutput.fromJson(Map<String, dynamic> json) {
    userID = json['UserID'];
    userName = json['UserName'];
    tALLGDCODE = json['TALLGDCODE'];
    teamid = json['Teamid'];
    teamName = json['TeamName'];
    fIRSTNAME = json['FIRSTNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['UserID'] = userID;
    data['UserName'] = userName;
    data['TALLGDCODE'] = tALLGDCODE;
    data['Teamid'] = teamid;
    data['TeamName'] = teamName;
    data['FIRSTNAME'] = fIRSTNAME;
    return data;
  }
}
