// ignore_for_file: file_names

class RemarkListResponse {
  String? status;
  String? message;
  List<RemarkListOutput>? output;

  RemarkListResponse({this.status, this.message, this.output});

  RemarkListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RemarkListOutput>[];
      json['output'].forEach((v) {
        output!.add(RemarkListOutput.fromJson(v));
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

class RemarkListOutput {
  int? cReamrkID;
  String? callingRemark;
  bool isSelected = false;
  RemarkListOutput({this.cReamrkID, this.callingRemark});

  RemarkListOutput.fromJson(Map<String, dynamic> json) {
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
