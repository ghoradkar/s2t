// ignore_for_file: file_names

class PasswordResetResponseModel {
  final String status;
  final String message;

  PasswordResetResponseModel({required this.status, required this.message});

  factory PasswordResetResponseModel.fromJson(Map<String, dynamic> json) {
    return PasswordResetResponseModel(
      status: json['status']?.toString() ?? '',
      message: json['message']?.toString() ?? '',
    );
  }

  bool get isSuccess => status.toLowerCase() == 'success';
}
