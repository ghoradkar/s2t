// ignore_for_file: file_names

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
  int? isReadinessFormFilled;

  UserCampMappingAndAttendanceStatusOutput({
    this.attendanceFlag,
    this.campFlag,
    this.isCampClosed,
    this.isReadinessFormFilled,
  });

  UserCampMappingAndAttendanceStatusOutput.fromJson(Map<String, dynamic> json) {
    attendanceFlag = json['AttendanceFlag'];
    campFlag = json['CampFlag'];
    isCampClosed = json['IsCampClosed'];
    isReadinessFormFilled = json['IsReadinessFormFilled'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AttendanceFlag'] = attendanceFlag;
    data['CampFlag'] = campFlag;
    data['IsCampClosed'] = isCampClosed;
    data['IsReadinessFormFilled'] = isReadinessFormFilled;
    return data;
  }
}
