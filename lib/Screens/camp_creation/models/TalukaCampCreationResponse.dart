class TalukaCampCreationResponse {
  String? status;
  String? message;
  List<TalukaCampCreationOutput>? output;

  TalukaCampCreationResponse({this.status, this.message, this.output});

  TalukaCampCreationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TalukaCampCreationOutput>[];
      json['output'].forEach((v) {
        output!.add(new TalukaCampCreationOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class TalukaCampCreationOutput {
  int? tALLGDCODE;
  String? tALNAME;
  bool isSelected = false;
  TalukaCampCreationOutput({this.tALLGDCODE, this.tALNAME});

  TalukaCampCreationOutput.fromJson(Map<String, dynamic> json) {
    tALLGDCODE = json['TALLGDCODE'];
    tALNAME = json['TALNAME'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['TALLGDCODE'] = this.tALLGDCODE;
    data['TALNAME'] = this.tALNAME;
    return data;
  }
}
