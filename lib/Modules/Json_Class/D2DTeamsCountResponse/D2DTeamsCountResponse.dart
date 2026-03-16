class D2DTeamsCountResponse {
  String? status;
  String? message;
  List<D2DTeamsCountOutput>? output;

  D2DTeamsCountResponse({this.status, this.message, this.output});

  D2DTeamsCountResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DTeamsCountOutput>[];
      json['output'].forEach((v) {
        output!.add( D2DTeamsCountOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class D2DTeamsCountOutput {
  int? totalTeamCount;
  int? workingTeamCount;
  int? nonWorkingTeamCount;

  D2DTeamsCountOutput({
    this.totalTeamCount,
    this.workingTeamCount,
    this.nonWorkingTeamCount,
  });

  D2DTeamsCountOutput.fromJson(Map<String, dynamic> json) {
    totalTeamCount = json['TotalTeamCount'];
    workingTeamCount = json['WorkingTeamCount'];
    nonWorkingTeamCount = json['NonWorkingTeamCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['TotalTeamCount'] = totalTeamCount;
    data['WorkingTeamCount'] = workingTeamCount;
    data['NonWorkingTeamCount'] = nonWorkingTeamCount;
    return data;
  }
}
