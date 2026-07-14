class AssignmentRemarksResponse {
  String? status;
  String? message;
  List<AssignmentRemarksOutput>? output;

  AssignmentRemarksResponse({this.status, this.message, this.output});

  AssignmentRemarksResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AssignmentRemarksOutput>[];
      json['output'].forEach((v) {
        output!.add(AssignmentRemarksOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class AssignmentRemarksOutput {
  int? arId;
  String? assignmentRemarks;
  bool isSelected = false;
  AssignmentRemarksOutput({this.arId, this.assignmentRemarks});

  AssignmentRemarksOutput.fromJson(Map<String, dynamic> json) {
    arId = json['ArId'];
    assignmentRemarks = json['AssignmentRemarks'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['ArId'] = arId;
    data['AssignmentRemarks'] = assignmentRemarks;
    return data;
  }
}
