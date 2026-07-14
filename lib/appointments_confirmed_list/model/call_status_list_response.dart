// ignore_for_file: file_names

class CallStatusListResponse {
  String? status;
  String? message;
  List<CallStatusListOutput>? output;

  CallStatusListResponse({this.status, this.message, this.output});

  CallStatusListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CallStatusListOutput>[];
      json['output'].forEach((v) {
        output!.add(CallStatusListOutput.fromJson(v));
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

class CallStatusListOutput {
  int? assignStatusID;
  String? callingStatus;
  bool isSelected = false;

  CallStatusListOutput({this.assignStatusID, this.callingStatus});

  CallStatusListOutput.fromJson(Map<String, dynamic> json) {
    assignStatusID = json['AssignStatusID'];
    callingStatus = json['CallingStatus'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['AssignStatusID'] = assignStatusID;
    data['CallingStatus'] = callingStatus;
    return data;
  }
}
