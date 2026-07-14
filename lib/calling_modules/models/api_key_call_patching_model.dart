class ApiKeyForCallPAtchingModel {
  String? status;
  String? message;
  List<APIKeyForCallPAtchingOutput>? output;

  ApiKeyForCallPAtchingModel({this.status, this.message, this.output});

  ApiKeyForCallPAtchingModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <APIKeyForCallPAtchingOutput>[];
      json['output'].forEach((v) {
        output!.add(APIKeyForCallPAtchingOutput.fromJson(v));
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

class APIKeyForCallPAtchingOutput {
  int? id;
  int? orgID;
  int? virtualNo;
  Null agentLoginID;
  String? aPIKey;


  APIKeyForCallPAtchingOutput(
      {this.id,
      this.orgID,
      this.virtualNo,
      this.agentLoginID,
      this.aPIKey,});

  APIKeyForCallPAtchingOutput.fromJson(Map<String, dynamic> json) {
    id = json['Id'];
    orgID = json['OrgID'];
    virtualNo = json['VirtualNo'];
    agentLoginID = json['AgentLoginID'];
    aPIKey = json['APIKey'];

  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Id'] = id;
    data['OrgID'] = orgID;
    data['VirtualNo'] = virtualNo;
    data['AgentLoginID'] = agentLoginID;
    data['APIKey'] = aPIKey;

    return data;
  }
}
