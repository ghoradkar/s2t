// ignore_for_file: file_names

class CampIdListResponse {
  String? status;
  String? message;
  List<CampIdOutput>? output;

  CampIdListResponse({this.status, this.message, this.output});

  CampIdListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampIdOutput>[];
      json['output'].forEach((v) {
        output!.add(CampIdOutput.fromJson(v));
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

class CampIdOutput {
  String? campCreatedBy;
  String? campDate;
  int? campId;
  String? campLocation;
  String? campName;
  String? campNo;
  String? campReadinessFlag;
  int? campType;
  int? createdBy;
  int? dISTLGDCODE;
  String? dISTNAME;
  String? remark;
  int? siteDetailId;

  bool isSelected = false;
  CampIdOutput({
    this.campCreatedBy,
    this.campDate,
    this.campId,
    this.campLocation,
    this.campName,
    this.campNo,
    this.campReadinessFlag,
    this.campType,
    this.createdBy,
    this.dISTLGDCODE,
    this.dISTNAME,
    this.remark,
    this.siteDetailId,
  });

  CampIdOutput.fromJson(Map<String, dynamic> json) {
    campCreatedBy = json['CampCreatedBy'];
    campDate = json['CampDate'];
    campId = json['CampId'];
    campLocation = json['CampLocation'];
    campName = json['CampName'];
    campNo = json['CampNo'];
    campReadinessFlag = json['CampReadinessFlag'];
    campType = json['CampType'];
    createdBy = json['CreatedBy'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    remark = json['Remark'];
    siteDetailId = json['SiteDetailId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampCreatedBy'] = campCreatedBy;
    data['CampDate'] = campDate;
    data['CampId'] = campId;
    data['CampLocation'] = campLocation;
    data['CampName'] = campName;
    data['CampNo'] = campNo;
    data['CampReadinessFlag'] = campReadinessFlag;
    data['CampType'] = campType;
    data['CreatedBy'] = createdBy;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['Remark'] = remark;
    data['SiteDetailId'] = siteDetailId;
    return data;
  }
}
