// ignore_for_file: file_names

class OtherReasonForPatientRejectionResponse {
  String? status;
  String? message;
  List<OtherReasonOutput>? output;

  OtherReasonForPatientRejectionResponse({
    this.status,
    this.message,
    this.output,
  });

  OtherReasonForPatientRejectionResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <OtherReasonOutput>[];
      json['output'].forEach((v) {
        output!.add(OtherReasonOutput.fromJson(v));
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

class OtherReasonOutput {
  int? reasonId;
  String? reasonDescription;
  bool isSelected = false;

  OtherReasonOutput({this.reasonId, this.reasonDescription});

  OtherReasonOutput.fromJson(Map<String, dynamic> json) {
    reasonId = json['ReasonId'];
    reasonDescription = json['ReasonDescription'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ReasonId'] = reasonId;
    data['ReasonDescription'] = reasonDescription;
    return data;
  }
}
