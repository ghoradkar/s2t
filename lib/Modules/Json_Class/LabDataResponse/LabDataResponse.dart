// ignore_for_file: file_names

class LabDataResponse {
  String? status;
  String? message;
  List<LabDataOutput>? output;

  LabDataResponse({this.status, this.message, this.output});

  LabDataResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <LabDataOutput>[];
      json['output'].forEach((v) {
        output!.add(LabDataOutput.fromJson(v));
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

class LabDataOutput {
  int? labCode;
  String? labName;
  bool isSelected = false;

  LabDataOutput({this.labCode, this.labName});

  LabDataOutput.fromJson(Map<String, dynamic> json) {
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
