// export 'package:s2toperational/Modules/Json_Class/CampDetailsonLabForDoorToDoorResponse/CampDetailsonLabForDoorToDoorResponse.dart';
// export 'package:s2toperational/Modules/Json_Class/UserCampMappingAndAttendanceStatusResponse/UserCampMappingAndAttendanceStatusResponse.dart';

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


class UserCampMappingAndAttendanceStatusResponse {
  String? status;
  String? message;
  List<UserCampMappingAndAttendanceStatusOutput>? output;

  UserCampMappingAndAttendanceStatusResponse({
    this.status,
    this.message,
    this.output,
  });

  UserCampMappingAndAttendanceStatusResponse.fromJson(
      Map<String, dynamic> json,
      ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserCampMappingAndAttendanceStatusOutput>[];
      json['output'].forEach((v) {
        output!.add(UserCampMappingAndAttendanceStatusOutput.fromJson(v));
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

class UserCampMappingAndAttendanceStatusOutput {
  int? attendanceFlag;
  int? campFlag;
  int? isCampClosed;
  int? isOldCampClosed;
  int? isReadinessFormFilled;
  int? teamMemberAttendance;

  UserCampMappingAndAttendanceStatusOutput({
    this.attendanceFlag,
    this.campFlag,
    this.isCampClosed,
    this.isOldCampClosed,
    this.isReadinessFormFilled,
    this.teamMemberAttendance,
  });

  UserCampMappingAndAttendanceStatusOutput.fromJson(Map<String, dynamic> json) {
    attendanceFlag = json['AttendanceFlag'];
    campFlag = json['CampFlag'];
    isCampClosed = json['IsCampClosed'];
    isOldCampClosed = json['IsOldCampClosed'];
    isReadinessFormFilled = json['IsReadinessFormFilled'];
    teamMemberAttendance = json['TeamMemberAttendance'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AttendanceFlag'] = attendanceFlag;
    data['CampFlag'] = campFlag;
    data['IsCampClosed'] = isCampClosed;
    data['IsOldCampClosed'] = isOldCampClosed;
    data['IsReadinessFormFilled'] = isReadinessFormFilled;
    data['TeamMemberAttendance'] = teamMemberAttendance;
    return data;
  }
}