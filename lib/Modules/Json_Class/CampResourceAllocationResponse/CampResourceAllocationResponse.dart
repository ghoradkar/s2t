// ignore_for_file: file_names

import '../UpdateSubResourceListResponse/UpdateSubResourceListResponse.dart';

class CampResourceAllocationResponse {
  String? status;
  String? message;
  List<CampResourceAllocationOutput>? output;

  CampResourceAllocationResponse({this.status, this.message, this.output});

  CampResourceAllocationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampResourceAllocationOutput>[];
      json['output'].forEach((v) {
        output!.add(CampResourceAllocationOutput.fromJson(v));
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

class CampResourceAllocationOutput {
  int? campId;
  String? testName;
  int? testId;
  String? desgName;
  int? resourceUserId;
  String? resourceName;
  UpdateSubResourceOutput? updateSubResourceObj;

  CampResourceAllocationOutput({
    this.campId,
    this.testName,
    this.testId,
    this.desgName,
    this.resourceUserId,
    this.resourceName,
  });

  CampResourceAllocationOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    testName = json['TestName'];
    testId = json['TestId'];
    desgName = json['DesgName'];
    resourceUserId = json['ResourceUserId'];
    resourceName = json['ResourceName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['TestName'] = testName;
    data['TestId'] = testId;
    data['DesgName'] = desgName;
    data['ResourceUserId'] = resourceUserId;
    data['ResourceName'] = resourceName;
    return data;
  }
}
