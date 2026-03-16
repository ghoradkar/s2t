// ignore_for_file: file_names

class SubmitDeviceMappingResponse {
  String? status;
  String? exceptionValue;
  String? message;

  SubmitDeviceMappingResponse({this.status, this.exceptionValue});

  SubmitDeviceMappingResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    exceptionValue = json['ExceptionValue'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['ExceptionValue'] = exceptionValue;
    data['message'] = message;
    return data;
  }
}
