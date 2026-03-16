// ignore_for_file: file_names

class AssignResourcesResponse {
  String? status;
  String? message;
  List<AssignResourcesOutput>? output;

  AssignResourcesResponse({this.status, this.message, this.output});

  AssignResourcesResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AssignResourcesOutput>[];
      json['output'].forEach((v) {
        output!.add(AssignResourcesOutput.fromJson(v));
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

class AssignResourcesOutput {
  int? desgId;
  String? desgName;
  bool isSelected = false;
  AssignResourcesOutput({this.desgId, this.desgName});

  AssignResourcesOutput.fromJson(Map<String, dynamic> json) {
    desgId = json['DesgId'];
    desgName = json['DesgName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DesgId'] = desgId;
    data['DesgName'] = desgName;
    return data;
  }
}
