class CampListV3Response {
  String? status;
  String? message;
  List<CampListV3Output>? output;

  CampListV3Response({this.status, this.message, this.output});

  CampListV3Response.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampListV3Output>[];
      json['output'].forEach((v) {
        output!.add(CampListV3Output.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CampListV3Output {
  int? campId;
  int? siteDetailId;
  String? campNo;
  String? campLocation;
  String? campDate;
  int? dISTLGDCODE;
  String? dISTNAME;
  String? remark;
  int? campType;
  int? createdBy;
  String? campCreatedBy;
  String? campName;
  int? lABCODE;
  int? expectedbeneficiarycount;
  int? resourceMappingFlag;
  String? labName;
  bool isSelected = false;
  CampListV3Output(
      {this.campId,
      this.siteDetailId,
      this.campNo,
      this.campLocation,
      this.campDate,
      this.dISTLGDCODE,
      this.dISTNAME,
      this.remark,
      this.campType,
      this.createdBy,
      this.campCreatedBy,
      this.campName,
      this.lABCODE,
      this.expectedbeneficiarycount,
      this.resourceMappingFlag,
      this.labName});

  CampListV3Output.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    siteDetailId = json['SiteDetailId'];
    campNo = json['CampNo'];
    campLocation = json['CampLocation'];
    campDate = json['CampDate'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    remark = json['Remark'];
    campType = json['CampType'];
    createdBy = json['CreatedBy'];
    campCreatedBy = json['CampCreatedBy'];
    campName = json['CampName'];
    lABCODE = json['LABCODE'];
    expectedbeneficiarycount = json['Expectedbeneficiarycount'];
    resourceMappingFlag = json['ResourceMappingFlag'];
    labName = json['LabName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['CampId'] = campId;
    data['SiteDetailId'] = siteDetailId;
    data['CampNo'] = campNo;
    data['CampLocation'] = campLocation;
    data['CampDate'] = campDate;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['Remark'] = remark;
    data['CampType'] = campType;
    data['CreatedBy'] = createdBy;
    data['CampCreatedBy'] = campCreatedBy;
    data['CampName'] = campName;
    data['LABCODE'] = lABCODE;
    data['Expectedbeneficiarycount'] = expectedbeneficiarycount;
    data['ResourceMappingFlag'] = resourceMappingFlag;
    data['LabName'] = labName;
    return data;
  }
}
