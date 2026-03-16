// ignore_for_file: file_names

class RecollectionAssignmentRemarksResponse {
  String? status;
  String? message;
  List<RecollectionAssignmentRemarksOutput>? output;

  RecollectionAssignmentRemarksResponse({
    this.status,
    this.message,
    this.output,
  });

  RecollectionAssignmentRemarksResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RecollectionAssignmentRemarksOutput>[];
      json['output'].forEach((v) {
        output!.add(RecollectionAssignmentRemarksOutput.fromJson(v));
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

class RecollectionAssignmentRemarksOutput {
  int? arId;
  String? assignmentRemarks;
  bool isSelected = false;

  RecollectionAssignmentRemarksOutput({this.arId, this.assignmentRemarks});

  RecollectionAssignmentRemarksOutput.fromJson(Map<String, dynamic> json) {
    arId = json['ArId'];
    assignmentRemarks = json['AssignmentRemarks'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ArId'] = arId;
    data['AssignmentRemarks'] = assignmentRemarks;
    return data;
  }
}
