import 'dart:convert';

class InsertDetailsResponse {
  final String? status;
  final String? message;

  InsertDetailsResponse({
    this.status,
    this.message,
  });

  factory InsertDetailsResponse.fromJson(Map<String, dynamic> json) {
    return InsertDetailsResponse(
      status: json['status'] as String?,
      message: json['message'] as String?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'message': message,
    };
  }

  static InsertDetailsResponse fromRawJson(String str) =>
      InsertDetailsResponse.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());
}
