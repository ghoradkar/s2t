// ignore_for_file: file_names

class UserCampMappingStatusResponse {
  String? status;
  String? message;
  List<UserCampMappingStatusOutput>? output;

  UserCampMappingStatusResponse({this.status, this.message, this.output});

  UserCampMappingStatusResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserCampMappingStatusOutput>[];
      json['output'].forEach((v) {
        output!.add(UserCampMappingStatusOutput.fromJson(v));
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

class UserCampMappingStatusOutput {
  int? attendanceFlag;
  int? campFlag;
  int? isCampClosed;
  int? testFlag;
  int? isReadinessFormFilled;

  UserCampMappingStatusOutput({
    this.attendanceFlag,
    this.campFlag,
    this.isCampClosed,
    this.testFlag,
    this.isReadinessFormFilled,
  });

  UserCampMappingStatusOutput.fromJson(Map<String, dynamic> json) {
    attendanceFlag = json['AttendanceFlag'];
    campFlag = json['CampFlag'];
    isCampClosed = json['IsCampClosed'];
    testFlag = json['TestFlag'];
    isReadinessFormFilled = json['IsReadinessFormFilled'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AttendanceFlag'] = attendanceFlag;
    data['CampFlag'] = campFlag;
    data['IsCampClosed'] = isCampClosed;
    data['TestFlag'] = testFlag;
    data['IsReadinessFormFilled'] = isReadinessFormFilled;
    return data;
  }
}
