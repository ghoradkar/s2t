class AllDistrictListForPhyExamResponse {
  String? status;
  String? message;
  List<AllDistrictListForPhyExamOutput>? output;

  AllDistrictListForPhyExamResponse({this.status, this.message, this.output});

  AllDistrictListForPhyExamResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AllDistrictListForPhyExamOutput>[];
      json['output'].forEach((v) {
        output!.add(AllDistrictListForPhyExamOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = status;
    data['message'] =message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class AllDistrictListForPhyExamOutput {
  int? dISTLGDCODE;
  String? district;
  bool isSelected = false;

  AllDistrictListForPhyExamOutput({this.dISTLGDCODE, this.district});

  AllDistrictListForPhyExamOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['District'] = district;
    return data;
  }
}
