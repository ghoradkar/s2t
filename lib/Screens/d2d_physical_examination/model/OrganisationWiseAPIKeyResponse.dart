// ignore_for_file: unnecessary_question_mark, file_names

class OrganisationWiseAPIKeyResponse {
  String? status;
  String? message;
  String? data;
  List<OrganisationWiseAPIKeyOutput>? output;

  OrganisationWiseAPIKeyResponse({
    this.status,
    this.message,
    this.data,
    this.output,
  });

  OrganisationWiseAPIKeyResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    data = json['data'];
    if (json['output'] != null) {
      output = <OrganisationWiseAPIKeyOutput>[];
      json['output'].forEach((v) {
        output!.add(OrganisationWiseAPIKeyOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    data['data'] = this.data;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class OrganisationWiseAPIKeyOutput {
  String? aPIKey;
  int? orgId;
  int? orgKeyID;
  Null? userId;
  String? virtualNo;

  OrganisationWiseAPIKeyOutput({
    this.aPIKey,
    this.orgId,
    this.orgKeyID,
    this.userId,
    this.virtualNo,
  });

  OrganisationWiseAPIKeyOutput.fromJson(Map<String, dynamic> json) {
    aPIKey = json['APIKey'];
    orgId = json['OrgId'];
    orgKeyID = json['OrgKeyID'];
    userId = json['UserId'];
    virtualNo = json['VirtualNo'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['APIKey'] = this.aPIKey;
    data['OrgId'] = this.orgId;
    data['OrgKeyID'] = this.orgKeyID;
    data['UserId'] = this.userId;
    data['VirtualNo'] = this.virtualNo;
    return data;
  }
}