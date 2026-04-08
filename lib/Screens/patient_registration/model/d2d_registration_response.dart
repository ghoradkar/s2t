// ignore_for_file: file_names

class D2DRegistrationResponse {
  String? status;
  String? message;
  String? regdId;

  D2DRegistrationResponse({this.status, this.message, this.regdId});

  D2DRegistrationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    regdId = json['Regdid']?.toString() ?? json['regdId']?.toString();
  }
}
