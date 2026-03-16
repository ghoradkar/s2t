// ignore_for_file: file_names

class UserMappedTalukaResponse {
  String? status;
  String? message;
  List<UserMappedTalukaOutput>? output;

  UserMappedTalukaResponse({this.status, this.message, this.output});

  UserMappedTalukaResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserMappedTalukaOutput>[];
      json['output'].forEach((v) {
        output!.add(UserMappedTalukaOutput.fromJson(v));
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

class UserMappedTalukaOutput {
  int? tALLGDCODE;
  String? tALNAME;
  bool isSelected = false;

  UserMappedTalukaOutput({this.tALLGDCODE, this.tALNAME});

  UserMappedTalukaOutput.fromJson(Map<String, dynamic> json) {
    tALLGDCODE = json['TALLGDCODE'];
    tALNAME = json['TALNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['TALLGDCODE'] = tALLGDCODE;
    data['TALNAME'] = tALNAME;
    return data;
  }
}
