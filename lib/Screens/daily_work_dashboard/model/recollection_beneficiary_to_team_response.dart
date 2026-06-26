// ignore_for_file: file_names

class RecollectionBeneficiaryToTeamResponse {
  String? status;
  String? message;
  List<RecollectionBeneficiaryToTeamOutput>? output;

  RecollectionBeneficiaryToTeamResponse({
    this.status,
    this.message,
    this.output,
  });

  RecollectionBeneficiaryToTeamResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RecollectionBeneficiaryToTeamOutput>[];
      json['output'].forEach((v) {
        output!.add(RecollectionBeneficiaryToTeamOutput.fromJson(v));
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

class RecollectionBeneficiaryToTeamOutput {
  int? sequenceNo;
  int? arid;
  String? assignmentRemarks;
  int? patientCount;

  RecollectionBeneficiaryToTeamOutput({
    this.sequenceNo,
    this.arid,
    this.assignmentRemarks,
    this.patientCount,
  });

  RecollectionBeneficiaryToTeamOutput.fromJson(Map<String, dynamic> json) {
    sequenceNo = json['SequenceNo'];
    arid = json['Arid'];
    assignmentRemarks = json['AssignmentRemarks'];
    patientCount = json['PatientCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['SequenceNo'] = sequenceNo;
    data['Arid'] = arid;
    data['AssignmentRemarks'] = assignmentRemarks;
    data['PatientCount'] = patientCount;
    return data;
  }
}
