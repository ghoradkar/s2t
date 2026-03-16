class InsertBeneficiaryCallingLogResponse {
  String? status;
  String? message;
  int? iD;

  InsertBeneficiaryCallingLogResponse({this.status, this.message, this.iD});

  InsertBeneficiaryCallingLogResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    iD = json['ID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    data['ID'] = this.iD;
    return data;
  }
}
