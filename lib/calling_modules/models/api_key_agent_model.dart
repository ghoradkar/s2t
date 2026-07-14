class ApiKeyForAgentIDModel {
  String? status;
  String? message;
  List<APIKeyForAgentOutput>? output;

  ApiKeyForAgentIDModel({this.status, this.message, this.output});

  ApiKeyForAgentIDModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <APIKeyForAgentOutput>[];
      json['output'].forEach((v) {
        output!.add(APIKeyForAgentOutput.fromJson(v));
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

class APIKeyForAgentOutput {
  int? id;
  int? orgID;
  String? servieNumber;
  Null agentLoginID;
  String? aPIKey;
  int? isActive;
  int? createdBy;
  String? createdOn;
  Null modifyby;
  String? modifiedDate;

  APIKeyForAgentOutput(
      {this.id,
      this.orgID,
      this.servieNumber,
      this.agentLoginID,
      this.aPIKey,
      this.isActive,
      this.createdBy,
      this.createdOn,
      this.modifyby,
      this.modifiedDate});

  APIKeyForAgentOutput.fromJson(Map<String, dynamic> json) {
    id = json['Id'];
    orgID = json['OrgID'];
    servieNumber = json['ServieNumber'];
    agentLoginID = json['AgentLoginID'];
    aPIKey = json['APIKey'];
    isActive = json['isActive'];
    createdBy = json['CreatedBy'];
    createdOn = json['CreatedOn'];
    modifyby = json['Modifyby'];
    modifiedDate = json['ModifiedDate'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Id'] = id;
    data['OrgID'] = orgID;
    data['ServieNumber'] = servieNumber;
    data['AgentLoginID'] = agentLoginID;
    data['APIKey'] = aPIKey;
    data['isActive'] = isActive;
    data['CreatedBy'] = createdBy;
    data['CreatedOn'] = createdOn;
    data['Modifyby'] = modifyby;
    data['ModifiedDate'] = modifiedDate;
    return data;
  }
}
