// export 'package:s2toperational/Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
// export 'package:s2toperational/Modules/Json_Class/UserCampMappingAndAttendanceDataResponse/UserCampMappingAndAttendanceDataResponse.dart';

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
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
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
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['FLAG'] = this.fLAG;
    data['CampId'] = this.campId;
    data['SiteDetailId'] = this.siteDetailId;
    data['CampNo'] = this.campNo;
    data['CampLocation'] = this.campLocation;
    data['CampDate'] = this.campDate;
    data['Status'] = this.status;
    data['Description'] = this.description;
    data['DISTLGDCODE'] = this.dISTLGDCODE;
    data['DISTNAME'] = this.dISTNAME;
    data['Remark'] = this.remark;
    data['CampType'] = this.campType;
    data['CampTypeDescription'] = this.campTypeDescription;
    data['InitiatedBy'] = this.initiatedBy;
    data['InitiatedBy1'] = this.initiatedBy1;
    data['IsCampClosed'] = this.isCampClosed;
    data['CreatedBy'] = this.createdBy;
    data['LABCODE'] = this.lABCODE;
    return data;
  }
}

class UserCampMappingAndAttendanceDataResponse {
  String? status;
  String? message;
  List<UserCampMappingAndAttendanceDataOutput>? output;

  UserCampMappingAndAttendanceDataResponse({
    this.status,
    this.message,
    this.output,
  });

  UserCampMappingAndAttendanceDataResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserCampMappingAndAttendanceDataOutput>[];
      json['output'].forEach((v) {
        output!.add(UserCampMappingAndAttendanceDataOutput.fromJson(v));
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

class UserCampMappingAndAttendanceDataOutput {
  int? attendanceFlag;
  int? campFlag;
  int? testFlag;
  int? isCampClosed;
  int? isReadinessFormFilled;

  UserCampMappingAndAttendanceDataOutput({
    this.attendanceFlag,
    this.campFlag,
    this.testFlag,
    this.isCampClosed,
    this.isReadinessFormFilled,
  });

  UserCampMappingAndAttendanceDataOutput.fromJson(Map<String, dynamic> json) {
    attendanceFlag = json['AttendanceFlag'];
    campFlag = json['CampFlag'];
    testFlag = json['TestFlag'];
    isCampClosed = json['IsCampClosed'];
    isReadinessFormFilled = json['IsReadinessFormFilled'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AttendanceFlag'] = attendanceFlag;
    data['CampFlag'] = campFlag;
    data['TestFlag'] = testFlag;
    data['IsCampClosed'] = isCampClosed;
    data['IsReadinessFormFilled'] = isReadinessFormFilled;
    return data;
  }
}