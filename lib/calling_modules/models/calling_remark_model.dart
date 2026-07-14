class CallingRemarkModel {
  String? status;
  String? message;
  List<CallingRemarkOutput>? output;

  CallingRemarkModel({this.status, this.message, this.output});

  CallingRemarkModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CallingRemarkOutput>[];
      json['output'].forEach((v) {
        output!.add(CallingRemarkOutput.fromJson(v));
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

class CallingRemarkOutput {
  int? cReamrkID;
  String? callingRemark;

  CallingRemarkOutput({this.cReamrkID, this.callingRemark});

  CallingRemarkOutput.fromJson(Map<String, dynamic> json) {
    cReamrkID = json['CReamrkID'];
    callingRemark = json['CallingRemark'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CReamrkID'] = cReamrkID;
    data['CallingRemark'] = callingRemark;
    return data;
  }
}
