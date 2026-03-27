class CompanyListResponse {
  String? status;
  String? message;
  List<CompanyListOutput>? output;

  CompanyListResponse({this.status, this.message, this.output});

  CompanyListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CompanyListOutput>[];
      json['output'].forEach((v) {
        output!.add(CompanyListOutput.fromJson(v));
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

class CompanyListOutput {
  int? paidByCompanyID;
  String? paidByCompany;
  bool isSelected = false;

  CompanyListOutput({this.paidByCompanyID, this.paidByCompany});

  CompanyListOutput.fromJson(Map<String, dynamic> json) {
    paidByCompanyID = json['PaidByCompanyID'];
    paidByCompany = json['PaidByCompany'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['PaidByCompanyID'] = paidByCompanyID;
    data['PaidByCompany'] = paidByCompany;
    return data;
  }
}
