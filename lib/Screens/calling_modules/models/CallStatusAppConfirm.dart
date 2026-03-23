class CallStatusAppConfirm {
  String? status;
  String? message;
  List<CallStatusOutputAppConfirm>? output;

  CallStatusAppConfirm({this.status, this.message, this.output});

  CallStatusAppConfirm.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CallStatusOutputAppConfirm>[];
      json['output'].forEach((v) {
        output!.add(CallStatusOutputAppConfirm.fromJson(v));
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

class CallStatusOutputAppConfirm {
  String? appointmentStatus;
  String? callingStatus;
  int? groupID;
  int? assignStatusID;
  bool isSelected = false;

  CallStatusOutputAppConfirm({
    this.appointmentStatus,
    this.groupID,
    this.callingStatus,
    this.assignStatusID,
  });

  CallStatusOutputAppConfirm.fromJson(Map<String, dynamic> json) {
    appointmentStatus = json['AppointmentStatus'];
    callingStatus = json['CallingStatus'];
    groupID = json['GroupID'];
    assignStatusID = json['AssignStatusID'];
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
