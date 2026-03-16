// ignore_for_file: file_names

class SubResourceListResponse {
  String? status;
  String? message;
  List<SubResourceOutput>? output;

  SubResourceListResponse({this.status, this.message, this.output});

  SubResourceListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SubResourceOutput>[];
      json['output'].forEach((v) {
        output!.add(SubResourceOutput.fromJson(v));
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

class SubResourceOutput {
  int? uSERID;
  String? resourceName;
  int? dESGID;
  int? testId;
  bool isSelected = false;

  SubResourceOutput({this.uSERID, this.resourceName, this.dESGID, this.testId});

  SubResourceOutput.fromJson(Map<String, dynamic> json) {
    uSERID = json['USERID'];
    resourceName = json['ResourceName'];
    dESGID = json['DESGID'];
    testId = json['TestId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['USERID'] = uSERID;
    data['ResourceName'] = resourceName;
    data['DESGID'] = dESGID;
    data['TestId'] = testId;
    return data;
  }
}
