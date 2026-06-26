// ignore_for_file: file_names

class T2TCTUserDetailsResponse {
  String? status;
  String? message;
  List<T2TCTUserDetailsOutput>? output;

  T2TCTUserDetailsResponse({this.status, this.message, this.output});

  T2TCTUserDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <T2TCTUserDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(T2TCTUserDetailsOutput.fromJson(v));
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

class T2TCTUserDetailsOutput {
  int? uSERID;
  String? uSERNAME;
  String? uSERMOBNO;
  bool isSelected = false;
  T2TCTUserDetailsOutput({this.uSERID, this.uSERNAME, this.uSERMOBNO});

  T2TCTUserDetailsOutput.fromJson(Map<String, dynamic> json) {
    uSERID = json['USERID'];
    uSERNAME = json['USERNAME'];
    uSERMOBNO = json['USERMOBNO'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['USERID'] = uSERID;
    data['USERNAME'] = uSERNAME;
    data['USERMOBNO'] = uSERMOBNO;
    return data;
  }
}
