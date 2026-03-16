// ignore_for_file: unnecessary_question_mark

class T2TCallingAPIDetailsResponse {
  String? status;
  String? message;
  String? data;
  List<T2TCallingAPIDetailsOutput>? output;

  T2TCallingAPIDetailsResponse({
    this.status,
    this.message,
    this.data,
    this.output,
  });

  T2TCallingAPIDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    data = json['data'];
    if (json['output'] != null) {
      output = <T2TCallingAPIDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(T2TCallingAPIDetailsOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    data['data'] = this.data;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class T2TCallingAPIDetailsOutput {
  String? aPIKey;
  Null? agentLoginID;
  int? createdBy;
  String? createdOn;
  int? id;
  String? modifiedDate;
  Null? modifyby;
  int? orgID;
  String? servieNumber;
  int? isActive;

  T2TCallingAPIDetailsOutput({
    this.aPIKey,
    this.agentLoginID,
    this.createdBy,
    this.createdOn,
    this.id,
    this.modifiedDate,
    this.modifyby,
    this.orgID,
    this.servieNumber,
    this.isActive,
  });

  T2TCallingAPIDetailsOutput.fromJson(Map<String, dynamic> json) {
    aPIKey = json['APIKey'];
    agentLoginID = json['AgentLoginID'];
    createdBy = json['CreatedBy'];
    createdOn = json['CreatedOn'];
    id = json['Id'];
    modifiedDate = json['ModifiedDate'];
    modifyby = json['Modifyby'];
    orgID = json['OrgID'];
    servieNumber = json['ServieNumber'];
    isActive = json['isActive'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['APIKey'] = aPIKey;
    data['AgentLoginID'] = agentLoginID;
    data['CreatedBy'] = createdBy;
    data['CreatedOn'] = createdOn;
    data['Id'] = id;
    data['ModifiedDate'] = modifiedDate;
    data['Modifyby'] = modifyby;
    data['OrgID'] = orgID;
    data['ServieNumber'] = servieNumber;
    data['isActive'] = isActive;
    return data;
  }
}
