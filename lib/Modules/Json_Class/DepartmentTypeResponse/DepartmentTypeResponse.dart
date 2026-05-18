class DepartmentTypeResponse {
  String? status;
  String? message;
  List<DepartmentTypeOutput>? output;

  DepartmentTypeResponse({this.status, this.message, this.output});

  DepartmentTypeResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <DepartmentTypeOutput>[];
      json['output'].forEach((v) {
        output!.add(DepartmentTypeOutput.fromJson(v));
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

class DepartmentTypeOutput {
  int? deptTypeId;
  String? departmentType;
  bool isSelected = false;

  DepartmentTypeOutput({this.deptTypeId, this.departmentType});

  DepartmentTypeOutput.fromJson(Map<String, dynamic> json) {
    deptTypeId = json['DeptTypeId'];
    departmentType = json['DepartmentType'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DeptTypeId'] = deptTypeId;
    data['DepartmentType'] = departmentType;
    return data;
  }
}
