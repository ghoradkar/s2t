class TeamNumberByCampIdAndUserIdListResponse {
  String? status;
  String? message;
  List<TeamNumberByCampIdOutput>? output;

  TeamNumberByCampIdAndUserIdListResponse({
    this.status,
    this.message,
    this.output,
  });

  TeamNumberByCampIdAndUserIdListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamNumberByCampIdOutput>[];
      json['output'].forEach((v) {
        output!.add(new TeamNumberByCampIdOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class TeamNumberByCampIdOutput {
  String? teamNumber;
  String? teamName;

  TeamNumberByCampIdOutput({this.teamNumber, this.teamName});

  TeamNumberByCampIdOutput.fromJson(Map<String, dynamic> json) {
    teamNumber = json['TeamNumber'];
    teamName = json['TeamName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['TeamNumber'] = this.teamNumber;
    data['TeamName'] = this.teamName;
    return data;
  }
}
