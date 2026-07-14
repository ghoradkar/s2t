import 'dart:convert';

class D2DAvailabilityUpdateResponse {
  final String? status;
  final String? message;

  D2DAvailabilityUpdateResponse({this.status, this.message});

  factory D2DAvailabilityUpdateResponse.fromJson(Map<String, dynamic> json) {
    return D2DAvailabilityUpdateResponse(
      status: json['status'] as String?,
      message: json['message'] as String?,
    );
  }

  static D2DAvailabilityUpdateResponse fromRawJson(String str) =>
      D2DAvailabilityUpdateResponse.fromJson(json.decode(str));
}
