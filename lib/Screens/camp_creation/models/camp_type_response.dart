// ignore_for_file: file_names

class CampTypeResponse {
  String? status;
  String? message;
  List<CampTypeOutput>? output;

  CampTypeResponse({this.status, this.message, this.output});

  CampTypeResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampTypeOutput>[];
      json['output'].forEach((v) {
        output!.add(CampTypeOutput.fromJson(v));
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

class CampTypeOutput {
  int? cAMPTYPE;
  String? campTypeDescription;
  bool isSelected = false;
  CampTypeOutput({this.cAMPTYPE, this.campTypeDescription});

  CampTypeOutput.fromJson(Map<String, dynamic> json) {
    cAMPTYPE = json['CAMPTYPE'];
    campTypeDescription = json['CampTypeDescription'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CAMPTYPE'] = cAMPTYPE;
    data['CampTypeDescription'] = campTypeDescription;
    return data;
  }
}
