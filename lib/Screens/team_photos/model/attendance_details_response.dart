// ignore_for_file: file_names

class AttendanceDetailsResponse {
  String? status;
  String? message;
  List<AttendanceDetailsOutput>? output;

  AttendanceDetailsResponse({this.status, this.message, this.output});

  AttendanceDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AttendanceDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(AttendanceDetailsOutput.fromJson(v));
      });
    }
  }
}

class AttendanceDetailsOutput {
  int? userId;
  String? memberName;
  String? inTime;
  String? inDistanceInKM;
  String? outTime;
  String? outDistanceInKM;

  AttendanceDetailsOutput({
    this.userId,
    this.memberName,
    this.inTime,
    this.inDistanceInKM,
    this.outTime,
    this.outDistanceInKM,
  });

  AttendanceDetailsOutput.fromJson(Map<String, dynamic> json) {
    userId = json['USERID'];
    memberName = json['MemberName'];
    inTime = json['InTime'];
    inDistanceInKM = json['INDistanceInKM'];
    outTime = json['OUTTIME'];
    outDistanceInKM = json['OutDistanceInKM'];
  }

  // Native checks: "Pending".equalsIgnoreCase(inTime) — null and empty are NOT
  // treated as pending. Match that exactly so Flutter does not over-restrict.
  bool get isInPending => inTime?.toLowerCase() == 'pending';
  bool get isOutPending => outTime?.toLowerCase() == 'pending';
}
