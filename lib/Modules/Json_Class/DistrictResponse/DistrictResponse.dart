// ignore_for_file: file_names

class DistrictResponse {
  String? status;
  String? message;
  List<DistrictOutput>? output;

  DistrictResponse({this.status, this.message, this.output});

  DistrictResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <DistrictOutput>[];
      json['output'].forEach((v) {
        output!.add(DistrictOutput.fromJson(v));
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

class DistrictOutput {
  int? dISTLGDCODE;
  String? dISTNAME;
  bool isSelected = false;
  DistrictOutput({this.dISTLGDCODE, this.dISTNAME});

  DistrictOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    return data;
  }
}
