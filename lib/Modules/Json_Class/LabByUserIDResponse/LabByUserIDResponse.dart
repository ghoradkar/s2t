class LabByUserIDResponse {
  String? status;
  String? message;
  List<LabByUserIDOutput>? output;

  LabByUserIDResponse({this.status, this.message, this.output});

  LabByUserIDResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <LabByUserIDOutput>[];
      json['output'].forEach((v) {
        output!.add(new LabByUserIDOutput.fromJson(v));
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

class LabByUserIDOutput {
  int? dISTLGDCODE;
  String? dISTNAME;
  int? labCode;
  String? labName;
  bool isSelected = false;
  LabByUserIDOutput({
    this.dISTLGDCODE,
    this.dISTNAME,
    this.labCode,
    this.labName,
  });

  LabByUserIDOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    labCode = json['LabCode'];
    labName = json['LabName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['DISTLGDCODE'] = this.dISTLGDCODE;
    data['DISTNAME'] = this.dISTNAME;
    data['LabCode'] = this.labCode;
    data['LabName'] = this.labName;
    return data;
  }
}
