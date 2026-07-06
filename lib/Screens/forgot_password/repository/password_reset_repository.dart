// ignore_for_file: file_names

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/utilities/api_manager.dart';
import 'package:s2toperational/Modules/constants/api_constants.dart';
import 'package:s2toperational/Modules/constants/api_client.dart';

class PasswordResetRepository {
  String get _base => APIManager.kD2DBaseURL;

  Future<http.Response> sendOtp({
    required String mobileNo,
    required String otp,
  }) {
    return Repository.postResponse(
      '$_base${APIConstants.kInsertChangePasswordRequest}',
      {'Mobileno': mobileNo, 'OTP': otp},
      {'Content-Type': 'application/x-www-form-urlencoded'},
    );
  }

  Future<http.Response> updatePassword({
    required String userId,
    required String newPassword,
    required String mobileNo,
    required String otp,
  }) {
    return Repository.postResponse(
      '$_base${APIConstants.kUpdateUserPassword}',
      {'USERID': userId, 'Pwd': newPassword, 'MobNo': mobileNo, 'Otp': otp},
      {'Content-Type': 'application/x-www-form-urlencoded'},
    );
  }
}
