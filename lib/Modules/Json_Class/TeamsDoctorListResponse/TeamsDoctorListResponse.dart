// ignore_for_file: file_names

class TeamsDoctorListResponse {
  String? status;
  String? message;
  List<TeamsDoctorListOutput>? output;

  TeamsDoctorListResponse({this.status, this.message, this.output});

  TeamsDoctorListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamsDoctorListOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamsDoctorListOutput.fromJson(v));
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

class TeamsDoctorListOutput {
  int? uSERID;
  String? resourceName;
  int? desgid;
  bool selected = false;
  TeamsDoctorListOutput({this.uSERID, this.resourceName, this.desgid});

  TeamsDoctorListOutput.fromJson(Map<String, dynamic> json) {
    uSERID = json['USERID'];
    resourceName = json['ResourceName'];
    desgid = json['desgid'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['USERID'] = uSERID;
    data['ResourceName'] = resourceName;
    data['desgid'] = desgid;
    return data;
  }
}
