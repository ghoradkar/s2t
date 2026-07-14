// ignore_for_file: file_names

class T2TCTTeamandBeneficiaryResponse {
  String? status;
  String? message;

  T2TCTTeamandBeneficiaryResponse({this.status, this.message});

  T2TCTTeamandBeneficiaryResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    // ignore: prefer_collection_literals
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    return data;
  }
}
