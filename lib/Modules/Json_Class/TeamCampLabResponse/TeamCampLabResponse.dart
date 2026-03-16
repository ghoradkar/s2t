// ignore_for_file: file_names

class TeamCampLabResponse {
  String? status;
  String? message;
  List<TeamCampLabOutput>? output;

  TeamCampLabResponse({this.status, this.message, this.output});

  TeamCampLabResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamCampLabOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamCampLabOutput.fromJson(v));
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

class TeamCampLabOutput {
  int? dISTLGDCODE;
  String? dISTNAME;
  int? labCode;
  String? labName;
  bool isSelected = false;
  TeamCampLabOutput({
    this.dISTLGDCODE,
    this.dISTNAME,
    this.labCode,
    this.labName,
  });

  TeamCampLabOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    labCode = json['LabCode'];
    labName = json['LabName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['LabCode'] = labCode;
    data['LabName'] = labName;
    return data;
  }
}
