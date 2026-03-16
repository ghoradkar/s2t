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
        output!.add(new UserCampMappingStatusOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
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
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['AttendanceFlag'] = this.attendanceFlag;
    data['CampFlag'] = this.campFlag;
    data['IsCampClosed'] = this.isCampClosed;
    data['TestFlag'] = this.testFlag;
    data['IsReadinessFormFilled'] = this.isReadinessFormFilled;
    return data;
  }
}
