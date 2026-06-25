import 'dart:convert';

/// Root response model
class GetMyOperatorResponse {
  final String? status;
  final String? message;
  final List<OrgKey>? output;

  GetMyOperatorResponse({
    this.status,
    this.message,
    this.output,
  });

  factory GetMyOperatorResponse.fromJson(Map<String, dynamic> json) {
    return GetMyOperatorResponse(
      status: json['status'] as String?,
      message: json['message'] as String?,
      output: json['output'] == null
          ? []
          : List<OrgKey>.from(
        json['output'].map((x) => OrgKey.fromJson(x)),
      ),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'message': message,
      'output': output?.map((x) => x.toJson()).toList(),
    };
  }

  static GetMyOperatorResponse fromRawJson(String str) =>
      GetMyOperatorResponse.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());
}

/// Org Key model
class OrgKey {
  final int? orgKeyID;
  final int? orgId;
  final int? userId;
  final String? virtualNo;
  final String? apiKey;
  final int? isMyOperatorAPI;
  final String? companyID;
  final String? publicIVRID;
  final String? secrateToken;
  final int? type;

  OrgKey({
    this.orgKeyID,
    this.orgId,
    this.userId,
    this.virtualNo,
    this.apiKey,
    this.isMyOperatorAPI,
    this.companyID,
    this.publicIVRID,
    this.secrateToken,
    this.type,
  });

  factory OrgKey.fromJson(Map<String, dynamic> json) {
    return OrgKey(
      orgKeyID: json['OrgKeyID'] as int?,
      orgId: json['OrgId'] as int?,
      userId: json['UserId'] as int?,
      virtualNo: json['VirtualNo'] as String?,
      apiKey: json['APIKey'] as String?,
      isMyOperatorAPI: json['IsMyOperatorAPI'] as int?,
      companyID: json['CompanyID'] as String?,
      publicIVRID: json['Public_IVR_ID'] as String?,
      secrateToken: json['SecrateToken'] as String?,
      type: json['Type'] as int?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'OrgKeyID': orgKeyID,
      'OrgId': orgId,
      'UserId': userId,
      'VirtualNo': virtualNo,
      'APIKey': apiKey,
      'IsMyOperatorAPI': isMyOperatorAPI,
      'CompanyID': companyID,
      'Public_IVR_ID': publicIVRID,
      'SecrateToken': secrateToken,
      'Type': type,
    };
  }
}
