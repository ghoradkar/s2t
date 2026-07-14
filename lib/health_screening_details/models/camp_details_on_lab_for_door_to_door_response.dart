// ignore_for_file: file_names

class CampDetailsonLabForDoorToDoorResponse {
  String? status;
  String? message;
  List<CampDetailsonLabForDoorToDoorOutput>? output;

  CampDetailsonLabForDoorToDoorResponse({
    this.status,
    this.message,
    this.output,
  });

  CampDetailsonLabForDoorToDoorResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampDetailsonLabForDoorToDoorOutput>[];
      json['output'].forEach((v) {
        output!.add(CampDetailsonLabForDoorToDoorOutput.fromJson(v));
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

class CampDetailsonLabForDoorToDoorOutput {
  String? dISTNAME;
  int? campId;
  String? campCreatedBy;
  int? createdBy;
  String? campName;
  String? campLocation;
  int? siteDetailId;
  int? dISTLGDCODE;
  int? campType;
  String? campTypeDescription;
  int? initiatedBy;
  String? initiatedBy1;
  String? campStatus;
  String? campStatus1;
  String? campDate;

  CampDetailsonLabForDoorToDoorOutput({
    this.dISTNAME,
    this.campId,
    this.campCreatedBy,
    this.createdBy,
    this.campName,
    this.campLocation,
    this.siteDetailId,
    this.dISTLGDCODE,
    this.campType,
    this.campTypeDescription,
    this.initiatedBy,
    this.initiatedBy1,
    this.campStatus,
    this.campStatus1,
    this.campDate,
  });

  CampDetailsonLabForDoorToDoorOutput.fromJson(Map<String, dynamic> json) {
    dISTNAME = json['DISTNAME'];
    campId = json['CampId'];
    campCreatedBy = json['CampCreatedBy'];
    createdBy = json['CreatedBy'];
    campName = json['CampName'];
    campLocation = json['CampLocation'];
    siteDetailId = json['SiteDetailId'];
    dISTLGDCODE = json['DISTLGDCODE'];
    campType = json['CampType'];
    campTypeDescription = json['CampTypeDescription'];
    initiatedBy = json['InitiatedBy'];
    initiatedBy1 = json['InitiatedBy1'];
    campStatus = json['CampStatus'];
    campStatus1 = json['CampStatus1'];
    campDate = json['CampDate'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTNAME'] = dISTNAME;
    data['CampId'] = campId;
    data['CampCreatedBy'] = campCreatedBy;
    data['CreatedBy'] = createdBy;
    data['CampName'] = campName;
    data['CampLocation'] = campLocation;
    data['SiteDetailId'] = siteDetailId;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['CampType'] = campType;
    data['CampTypeDescription'] = campTypeDescription;
    data['InitiatedBy'] = initiatedBy;
    data['InitiatedBy1'] = initiatedBy1;
    data['CampStatus'] = campStatus;
    data['CampStatus1'] = campStatus1;
    data['CampDate'] = campDate;
    return data;
  }
}
