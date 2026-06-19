// ignore_for_file: file_names

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
        output!.add(GetCampAssignUserOutput.fromJson(v));
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
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['ResourceUserId'] = resourceUserId;
    data['statusRes'] = statusRes;
    return data;
  }
}
