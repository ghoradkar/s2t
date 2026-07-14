// ignore_for_file: file_names

class ResourceReMappingCampResponse {
  String? status;
  String? message;
  List<ResourceReMappingCampOutput>? output;

  ResourceReMappingCampResponse({this.status, this.message, this.output});

  ResourceReMappingCampResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ResourceReMappingCampOutput>[];
      json['output'].forEach((v) {
        output!.add(ResourceReMappingCampOutput.fromJson(v));
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

class ResourceReMappingCampOutput {
  int? fLAG;
  int? campId;
  int? siteDetailId;
  String? campNo;
  String? campLocation;
  String? campDate;
  String? status;
  String? description;
  int? dISTLGDCODE;
  String? dISTNAME;
  String? remark;
  int? campType;
  String? campTypeDescription;
  int? initiatedBy;
  String? initiatedBy1;
  int? isCampClosed;
  int? createdBy;
  int? lABCODE;
  int? isRegdDone;
  String? campName;
  String? campCreatedBy;

  ResourceReMappingCampOutput({
    this.fLAG,
    this.campId,
    this.siteDetailId,
    this.campNo,
    this.campLocation,
    this.campDate,
    this.status,
    this.description,
    this.dISTLGDCODE,
    this.dISTNAME,
    this.remark,
    this.campType,
    this.campTypeDescription,
    this.initiatedBy,
    this.initiatedBy1,
    this.isCampClosed,
    this.createdBy,
    this.lABCODE,
    this.isRegdDone,
    this.campName,
    this.campCreatedBy,
  });

  ResourceReMappingCampOutput.fromJson(Map<String, dynamic> json) {
    fLAG = json['FLAG'];
    campId = json['CampId'];
    siteDetailId = json['SiteDetailId'];
    campNo = json['CampNo'];
    campLocation = json['CampLocation'];
    campDate = json['CampDate'];
    status = json['Status'];
    description = json['Description'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    remark = json['Remark'];
    campType = json['CampType'];
    campTypeDescription = json['CampTypeDescription'];
    initiatedBy = json['InitiatedBy'];
    initiatedBy1 = json['InitiatedBy1'];
    isCampClosed = json['IsCampClosed'];
    createdBy = json['CreatedBy'];
    lABCODE = json['LABCODE'];
    isRegdDone = json['IsRegdDone'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['FLAG'] = fLAG;
    data['CampId'] = campId;
    data['SiteDetailId'] = siteDetailId;
    data['CampNo'] = campNo;
    data['CampLocation'] = campLocation;
    data['CampDate'] = campDate;
    data['Status'] = status;
    data['Description'] = description;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['Remark'] = remark;
    data['CampType'] = campType;
    data['CampTypeDescription'] = campTypeDescription;
    data['InitiatedBy'] = initiatedBy;
    data['InitiatedBy1'] = initiatedBy1;
    data['IsCampClosed'] = isCampClosed;
    data['CreatedBy'] = createdBy;
    data['LABCODE'] = lABCODE;
    data['IsRegdDone'] = isRegdDone;
    return data;
  }
}
