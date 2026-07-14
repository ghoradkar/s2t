class OrganizationModel {
  String? status;
  String? message;
  List<GetOrganizationOutput>? output;

  OrganizationModel({this.status, this.message, this.output});

  OrganizationModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <GetOrganizationOutput>[];
      json['output'].forEach((v) {
        output!.add(GetOrganizationOutput.fromJson(v));
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

class GetOrganizationOutput {
  int? subOrgId;
  String? subOrgName;

  GetOrganizationOutput({this.subOrgId, this.subOrgName});

  GetOrganizationOutput.fromJson(Map<String, dynamic> json) {
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
