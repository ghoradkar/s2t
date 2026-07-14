import 'dart:convert';
import 'dart:io';

import 'package:http/io_client.dart';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/verify_otp/models/OrganisationWiseAPIKeyResponse.dart';
import 'package:s2toperational/constants/api_constants.dart';
import 'package:s2toperational/verify_otp/models/user_android_id_response.dart';

class VerifyOtpRepository {
  IOClient _client() {
    return IOClient(
      HttpClient()..badCertificateCallback = (cert, host, port) => true,
    );
  }

  String get _baseUrl => APIManager.kD2DBaseURL;

  Future<({bool success, String error})> sendOtp(
    Map<String, String> params,
  ) async {
    final url = Uri.parse('$_baseUrl${APIConstants.kGetOTPForLogin}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final model = OrganisationWiseAPIKeyResponse.fromJson(
        json.decode(response.body),
      );
      if (model.status == 'Success') {
        return (success: true, error: '');
      }
      return (success: false, error: model.message ?? 'Failed to send OTP');
    } catch (e) {
      return (success: false, error: 'Exception: $e');
    }
  }

  Future<({bool success, String error})> verifyOtp(
    Map<String, String> params,
  ) async {
    final url = Uri.parse('$_baseUrl${APIConstants.kVerifyOTPForLogin}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final model = OrganisationWiseAPIKeyResponse.fromJson(
        json.decode(response.body),
      );
      if (model.status == 'Success') {
        return (success: true, error: '');
      }
      return (success: false, error: model.message ?? 'OTP verification failed');
    } catch (e) {
      return (success: false, error: 'Exception: $e');
    }
  }

  Future<UserAndroidIDResponse?> getUserAndroidId(
    Map<String, String> params,
  ) async {
    final url = Uri.parse('$_baseUrl${APIConstants.kGetUSERAndroidID}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return UserAndroidIDResponse.fromJson(json.decode(response.body));
    } catch (_) {
      return null;
    }
  }

  Future<UserAndroidIDResponse?> saveAndroidId(
    Map<String, String> params,
  ) async {
    final url = Uri.parse('$_baseUrl${APIConstants.kInsertUSERAndroidID}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return UserAndroidIDResponse.fromJson(json.decode(response.body));
    } catch (_) {
      return null;
    }
  }

  Future<({bool success, String error})> saveAndroidToken(
    Map<String, String> params,
  ) async {
    final url = Uri.parse('$_baseUrl${APIConstants.kSaveAndroidToken}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final model = UserAndroidIDResponse.fromJson(json.decode(response.body));
      if (model.status == 'Success') {
        return (success: true, error: '');
      }
      return (success: false, error: model.message ?? 'Failed to save token');
    } catch (e) {
      return (success: false, error: 'Exception: $e');
    }
  }
}
