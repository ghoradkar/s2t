class CampTypeAndCatagoryResponse {
  String? status;
  String? message;
  List<CampTypeAndCatagoryOutput>? output;

  CampTypeAndCatagoryResponse({this.status, this.message, this.output});

  CampTypeAndCatagoryResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampTypeAndCatagoryOutput>[];
      json['output'].forEach((v) {
        output!.add(CampTypeAndCatagoryOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CampTypeAndCatagoryOutput {
  int? cAMPTYPE;
  String? campTypeDescription;
  int? catagoryID;
  String? catagoryType;
  bool isSelected = false;
  CampTypeAndCatagoryOutput({
    this.cAMPTYPE,
    this.campTypeDescription,
    this.catagoryID,
    this.catagoryType,
  });

  CampTypeAndCatagoryOutput.fromJson(Map<String, dynamic> json) {
    cAMPTYPE = json['CAMPTYPE'];
    campTypeDescription = json['CampTypeDescription'];
    catagoryID = json['CatagoryID'];
    catagoryType = json['CatagoryType'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['CAMPTYPE'] = cAMPTYPE;
    data['CampTypeDescription'] = campTypeDescription;
    data['CatagoryID'] = catagoryID;
    data['CatagoryType'] = catagoryType;
    return data;
  }
}
