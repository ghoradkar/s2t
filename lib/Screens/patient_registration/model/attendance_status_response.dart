// ignore_for_file: file_names

class AttendanceStatusResponse {
  String? status;
  String? message;
  AttendanceStatusOutput? output;

  AttendanceStatusResponse({this.status, this.message, this.output});

  AttendanceStatusResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] != null) {
      final out = json['output'];
      if (out is List && out.isNotEmpty) {
        output = AttendanceStatusOutput.fromJson(out.first as Map<String, dynamic>);
      } else if (out is Map<String, dynamic>) {
        output = AttendanceStatusOutput.fromJson(out);
      }
    }
  }
}

class AttendanceStatusOutput {
  String? isOldCampClosed;
  String? isCampClosed;
  String? campFlag;
  String? isReadinessFormFilled;
  String? attendanceFlag;
  String? testFlag;
  String? teamMemberAttendance;

  AttendanceStatusOutput({
    this.isOldCampClosed,
    this.isCampClosed,
    this.campFlag,
    this.isReadinessFormFilled,
    this.attendanceFlag,
    this.testFlag,
    this.teamMemberAttendance,
  });

  AttendanceStatusOutput.fromJson(Map<String, dynamic> json) {
    isOldCampClosed = json['isOldCampClosed']?.toString() ?? json['IsOldCampClosed']?.toString();
    isCampClosed = json['isCampClosed']?.toString() ?? json['IsCampClosed']?.toString();
    campFlag = json['campFlag']?.toString() ?? json['CampFlag']?.toString();
    isReadinessFormFilled =
        json['isReadinessFormFilled']?.toString() ??
        json['IsReadinessFormFilled']?.toString();
    attendanceFlag = json['attendanceFlag']?.toString() ?? json['AttendanceFlag']?.toString();
    testFlag = json['testFlag']?.toString() ?? json['TestFlag']?.toString();
    teamMemberAttendance =
        json['teamMemberAttendance']?.toString() ??
        json['TeamMemberAttendance']?.toString();
  }

  bool get blockOldCamp => isOldCampClosed == '0';
  bool get blockCampClosed => isCampClosed == '1';
  bool get blockNotMapped => campFlag == '0';
  bool get blockReadiness => isReadinessFormFilled == '0';
  bool get blockAttendance => attendanceFlag == '0';
  bool get blockTest => testFlag == '0';
}


