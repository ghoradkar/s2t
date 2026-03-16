class CampDetailsntApprovalResponse {
  String? status;
  String? message;
  List<CampDetailsntApprovalOutput>? output;

  CampDetailsntApprovalResponse({this.status, this.message, this.output});

  CampDetailsntApprovalResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampDetailsntApprovalOutput>[];
      json['output'].forEach((v) {
        output!.add( CampDetailsntApprovalOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CampDetailsntApprovalOutput {
  String? campDate;
  String? campNo;
  String? campName;
  String? campLocation;
  String? district;
  int? expectedbeneficiarycount;

  CampDetailsntApprovalOutput({
    this.campDate,
    this.campNo,
    this.campName,
    this.campLocation,
    this.district,
    this.expectedbeneficiarycount,
  });

  CampDetailsntApprovalOutput.fromJson(Map<String, dynamic> json) {
    campDate = json['CampDate'];
    campNo = json['CampNo'];
    campName = json['CampName'];
    campLocation = json['CampLocation'];
    district = json['District'];
    expectedbeneficiarycount = json['Expectedbeneficiarycount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['CampDate'] = campDate;
    data['CampNo'] = campNo;
    data['CampName'] = campName;
    data['CampLocation'] = campLocation;
    data['District'] = district;
    data['Expectedbeneficiarycount'] = expectedbeneficiarycount;
    return data;
  }
}
