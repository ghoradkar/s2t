// ignore_for_file: file_names

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
