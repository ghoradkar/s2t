class APIKeyForMyOperatorModel {
  String? status;
  String? message;
  List<APIKeyForMyOperatorModelOutput>? output;

  APIKeyForMyOperatorModel({this.status, this.message, this.output});

  APIKeyForMyOperatorModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <APIKeyForMyOperatorModelOutput>[];
      json['output'].forEach((v) {
        output!.add(APIKeyForMyOperatorModelOutput.fromJson(v));
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

class APIKeyForMyOperatorModelOutput {
  int? orgKeyID;
  int? orgId;
  Null userId;
  Null virtualNo;
  String? aPIKey;
  int? isMyOperatorAPI;
  String? companyID;
  String? publicIVRID;
  String? secrateToken;
  int? type;

  APIKeyForMyOperatorModelOutput(
      {this.orgKeyID,
      this.orgId,
      this.userId,
      this.virtualNo,
      this.aPIKey,
      this.isMyOperatorAPI,
      this.companyID,
      this.publicIVRID,
      this.secrateToken,
      this.type});

  APIKeyForMyOperatorModelOutput.fromJson(Map<String, dynamic> json) {
    orgKeyID = json['OrgKeyID'];
    orgId = json['OrgId'];
    userId = json['UserId'];
    virtualNo = json['VirtualNo'];
    aPIKey = json['APIKey'];
    isMyOperatorAPI = json['IsMyOperatorAPI'];
    companyID = json['CompanyID'];
    publicIVRID = json['Public_IVR_ID'];
    secrateToken = json['SecrateToken'];
    type = json['Type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['OrgKeyID'] = orgKeyID;
    data['OrgId'] = orgId;
    data['UserId'] = userId;
    data['VirtualNo'] = virtualNo;
    data['APIKey'] = aPIKey;
    data['IsMyOperatorAPI'] = isMyOperatorAPI;
    data['CompanyID'] = companyID;
    data['Public_IVR_ID'] = publicIVRID;
    data['SecrateToken'] = secrateToken;
    data['Type'] = type;
    return data;
  }
}
