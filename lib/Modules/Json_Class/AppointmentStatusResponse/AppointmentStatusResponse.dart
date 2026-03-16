// ignore_for_file: file_names

class AppointmentStatusResponse {
  String? status;
  String? message;
  List<AppointmentStatusOutput>? output;

  AppointmentStatusResponse({this.status, this.message, this.output});

  AppointmentStatusResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AppointmentStatusOutput>[];
      json['output'].forEach((v) {
        output!.add(AppointmentStatusOutput.fromJson(v));
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

class AppointmentStatusOutput {
  int? assignStatusID;
  String? appointmentStatus;
  bool isSelected = false;

  AppointmentStatusOutput({this.assignStatusID, this.appointmentStatus});

  AppointmentStatusOutput.fromJson(Map<String, dynamic> json) {
    assignStatusID = json['AssignStatusID'];
    appointmentStatus = json['AppointmentStatus'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignStatusID'] = assignStatusID;
    data['AppointmentStatus'] = appointmentStatus;
    return data;
  }
}
