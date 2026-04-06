// export 'package:s2toperational/Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';

class BindDistrictResponse {
  String? status;
  String? message;
  List<BindDistrictOutput>? output;

  BindDistrictResponse({this.status, this.message, this.output});

  BindDistrictResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BindDistrictOutput>[];
      json['output'].forEach((v) {
        output!.add(BindDistrictOutput.fromJson(v));
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

class BindDistrictOutput {
  int? dIVID;
  String? dIVNAME;
  int? subOrgId;
  String? subOrgName;
  int? dISTLGDCODE;
  String? dISTNAME;
  bool isSelected = false;

  BindDistrictOutput({
    this.dIVID,
    this.dIVNAME,
    this.subOrgId,
    this.subOrgName,
    this.dISTLGDCODE,
    this.dISTNAME,
  });

  BindDistrictOutput.fromJson(Map<String, dynamic> json) {
    dIVID = json['DIVID'];
    dIVNAME = json['DIVNAME'];
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DIVID'] = dIVID;
    data['DIVNAME'] = dIVNAME;
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    return data;
  }
}