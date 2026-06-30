import 'dart:convert';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';

class ForgotPasswordRepository {
  Future<({bool success, String message})> sendOtp({
    required String mobileNo,
    required String otp,
  }) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kInsertChangePasswordRequest}',
    );
    try {
      final client = APIManager.getInstanceOfIo1Client();
      final response = await client.post(
        url,
        body: {'Mobileno': mobileNo, 'OTP': otp},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      return (success: status.toLowerCase() == 'success', message: message);
    } catch (e) {
      return (success: false, message: 'Server not responding');
    }
  }

  Future<({bool success, String message})> updatePassword({
    required String userId,
    required String newPassword,
    required String mobileNo,
    required String otp,
  }) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kUpdateUserPassword}',
    );
    try {
      final client = APIManager.getInstanceOfIo1Client();
      final response = await client.post(
        url,
        body: {
          'USERID': userId,
          'Pwd': newPassword,
          'MobNo': mobileNo,
          'Otp': otp,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      return (success: status.toLowerCase() == 'success', message: message);
    } catch (e) {
      return (success: false, message: 'Server not responding');
    }
  }
}