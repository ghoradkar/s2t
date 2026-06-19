// ignore_for_file: file_names

class UpdateSubResourceListResponse {
  String? status;
  String? message;
  List<UpdateSubResourceOutput>? output;

  UpdateSubResourceListResponse({this.status, this.message, this.output});

  UpdateSubResourceListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UpdateSubResourceOutput>[];
      json['output'].forEach((v) {
        output!.add(UpdateSubResourceOutput.fromJson(v));
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

class UpdateSubResourceOutput {
  int? dISTLGDCODE;
  int? uSERID;
  String? resourceName;
  int? desgId;
  int? testId;
  String? resStatus;
  int? isApproved;
  String? remark;
  int? campId;
  bool isSelected = false;

  UpdateSubResourceOutput({
    this.dISTLGDCODE,
    this.uSERID,
    this.resourceName,
    this.desgId,
    this.testId,
    this.resStatus,
    this.isApproved,
    this.remark,
    this.campId,
  });

  UpdateSubResourceOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    uSERID = json['USERID'];
    resourceName = json['ResourceName'];
    desgId = json['DesgId'];
    testId = json['TestId'];
    resStatus = json['ResStatus'];
    isApproved = json['IsApproved'];
    remark = json['Remark'];
    campId = json['CampId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['USERID'] = uSERID;
    data['ResourceName'] = resourceName;
    data['DesgId'] = desgId;
    data['TestId'] = testId;
    data['ResStatus'] = resStatus;
    data['IsApproved'] = isApproved;
    data['Remark'] = remark;
    data['CampId'] = campId;
    return data;
  }
}
