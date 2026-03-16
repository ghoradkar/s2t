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
        output!.add(new UpdateSubResourceOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
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
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['DISTLGDCODE'] = this.dISTLGDCODE;
    data['USERID'] = this.uSERID;
    data['ResourceName'] = this.resourceName;
    data['DesgId'] = this.desgId;
    data['TestId'] = this.testId;
    data['ResStatus'] = this.resStatus;
    data['IsApproved'] = this.isApproved;
    data['Remark'] = this.remark;
    data['CampId'] = this.campId;
    return data;
  }
}
