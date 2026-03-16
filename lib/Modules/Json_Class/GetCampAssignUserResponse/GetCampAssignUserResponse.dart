class GetCampAssignUserResponse {
  String? status;
  String? message;
  List<GetCampAssignUserOutput>? output;

  GetCampAssignUserResponse({this.status, this.message, this.output});

  GetCampAssignUserResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <GetCampAssignUserOutput>[];
      json['output'].forEach((v) {
        output!.add(new GetCampAssignUserOutput.fromJson(v));
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

class GetCampAssignUserOutput {
  int? campId;
  int? resourceUserId;
  int? statusRes;

  GetCampAssignUserOutput({this.campId, this.resourceUserId, this.statusRes});

  GetCampAssignUserOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    resourceUserId = json['ResourceUserId'];
    statusRes = json['statusRes'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['CampId'] = this.campId;
    data['ResourceUserId'] = this.resourceUserId;
    data['statusRes'] = this.statusRes;
    return data;
  }
}
