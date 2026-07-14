// ignore_for_file: file_names

class RegularRegistrationResponse {
  String? status;
  String? message;

  RegularRegistrationResponse({this.status, this.message});

  RegularRegistrationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
  }
}
