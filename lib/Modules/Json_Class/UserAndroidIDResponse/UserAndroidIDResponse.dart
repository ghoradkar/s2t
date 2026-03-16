// ignore_for_file: file_names

class UserAndroidIDResponse {
  String? message;
  String? status;
  int? allowedForLogin;

  UserAndroidIDResponse({this.message, this.status, this.allowedForLogin});

  UserAndroidIDResponse.fromJson(Map<String, dynamic> json) {
    message = json['message'];
    status = json['status'];
    allowedForLogin = json['AllowedForLogin'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['message'] = message;
    data['status'] = status;
    data['AllowedForLogin'] = allowedForLogin;
    return data;
  }
}
