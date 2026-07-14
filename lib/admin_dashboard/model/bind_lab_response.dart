// ignore_for_file: file_names

class BindLabResponse {
  String? status;
  String? message;
  List<BindLabOutput>? output;

  BindLabResponse({this.status, this.message, this.output});

  BindLabResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BindLabOutput>[];
      json['output'].forEach((v) {
        output!.add(BindLabOutput.fromJson(v));
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

class BindLabOutput {
  int? labCode;
  String? labName;

  bool isSelected = false;

  BindLabOutput({
    this.labCode,
    this.labName,

  });

  BindLabOutput.fromJson(Map<String, dynamic> json) {
    labCode = json['LabCode'];
    labName = json['LabName'];

  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['LabCode'] = labCode;
    data['LabName'] = labName;

    return data;
  }
}
