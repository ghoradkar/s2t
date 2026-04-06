// export 'package:s2toperational/Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';

class BindDivisionResponse {
  String? status;
  String? message;
  List<BindDivisionOutput>? output;

  BindDivisionResponse({this.status, this.message, this.output});

  BindDivisionResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BindDivisionOutput>[];
      json['output'].forEach((v) {
        output!.add(BindDivisionOutput.fromJson(v));
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

class BindDivisionOutput {
  int? dIVID;
  String? dIVNAME;
  int? subOrgId;
  String? subOrgName;
  bool isSelected = false;

  BindDivisionOutput({
    this.dIVID,
    this.dIVNAME,
    this.subOrgId,
    this.subOrgName,
  });

  BindDivisionOutput.fromJson(Map<String, dynamic> json) {
    dIVID = json['DIVID'];
    dIVNAME = json['DIVNAME'];
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DIVID'] = dIVID;
    data['DIVNAME'] = dIVNAME;
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    return data;
  }
}