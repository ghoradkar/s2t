// ignore_for_file: file_names

class SubOrgResponse {
  String? status;
  String? message;
  List<SubOrganizationOutput>? output;

  SubOrgResponse({this.status, this.message, this.output});

  SubOrgResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SubOrganizationOutput>[];
      json['output'].forEach((v) {
        output!.add(SubOrganizationOutput.fromJson(v));
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

class SubOrganizationOutput {
  int? subOrgId;
  String? subOrgName;
  bool isSelected = false;

  SubOrganizationOutput({this.subOrgId, this.subOrgName});

  SubOrganizationOutput.fromJson(Map<String, dynamic> json) {
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    return data;
  }
}
