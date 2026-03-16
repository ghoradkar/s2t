// ignore_for_file: file_names

class InitiatedByResponse {
  String? status;
  String? message;
  List<InitiatedByOutput>? output;

  InitiatedByResponse({this.status, this.message, this.output});

  InitiatedByResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <InitiatedByOutput>[];
      json['output'].forEach((v) {
        output!.add(InitiatedByOutput.fromJson(v));
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

class InitiatedByOutput {
  int? iD;
  String? initiatedBy;
  bool isSelected = false;
  InitiatedByOutput({this.iD, this.initiatedBy});

  InitiatedByOutput.fromJson(Map<String, dynamic> json) {
    iD = json['ID'];
    initiatedBy = json['InitiatedBy'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ID'] = iD;
    data['InitiatedBy'] = initiatedBy;
    return data;
  }
}
