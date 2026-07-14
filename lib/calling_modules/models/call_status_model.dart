class Callstatusmodel {
  String? status;
  String? message;
  List<CallStatusOutput>? output;

  Callstatusmodel({this.status, this.message, this.output});

  Callstatusmodel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CallStatusOutput>[];
      json['output'].forEach((v) {
        output!.add(CallStatusOutput.fromJson(v));
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

class CallStatusOutput {
  String? appointmentStatus;
  String? callingStatus;
  int? groupID;
  int? assignStatusID;
  bool isSelected = false;

  CallStatusOutput({
    this.appointmentStatus,
    this.groupID,
    this.callingStatus,
    this.assignStatusID,
  });

  CallStatusOutput.fromJson(Map<String, dynamic> json) {
    appointmentStatus = json['AppointmentStatus'];
    callingStatus = json['CallingStatus'];
    groupID = json['GroupID'];
    assignStatusID = json['GroupID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AppointmentStatus'] = appointmentStatus;
    data['CallingStatus'] = callingStatus;
    data['GroupID'] = groupID;
    data['AssignStatusID'] = assignStatusID;
    return data;
  }
}
