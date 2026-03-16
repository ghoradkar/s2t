// ignore_for_file: file_names

class DeviceMappingSuccessResponse {
  String? status;
  int? message;

  DeviceMappingSuccessResponse({this.status, this.message});

  DeviceMappingSuccessResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    return data;
  }
}
