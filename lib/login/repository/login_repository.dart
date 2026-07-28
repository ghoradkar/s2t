import 'dart:convert';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:http/io_client.dart';

import '../../../utilities/api_manager.dart';
import '../models/login_response_model.dart';
import '../../../constants/api_constants.dart';
import '../../../utilities/data_provider.dart';

class LoginRepository {
  IOClient _client() {
    return IOClient(
      HttpClient()..badCertificateCallback = (cert, host, port) => true,
    );
  }

  Future<({LoginResponseModel? model, String error, bool success})> login(
    String username,
    String password,
  ) async {
    final url = Uri.parse(
      '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kUserLogin}',
    );
    try {
      final response = await _client().post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {'username': username, 'password': password},
      );
      if (response.statusCode == 200) {
        final model = LoginResponseModel.fromJson(json.decode(response.body));
        if (model.status == 'Success') {
          DataProvider().storeUserData(response.body);
          DataProvider().storeUserCredential(
            jsonEncode({'username': username, 'password': password}),
          );
          DataProvider().isRegularCamp(true);
          DataProvider().save(DataProvider().kUserName, username);
          DataProvider().save(DataProvider().kPassword, password);
          return (model: model, error: '', success: true);
        }
        return (
          model: model,
          error: model.message ?? 'Login failed',
          success: false,
        );
      }
      return (model: null, error: 'Server error', success: false);
    } catch (e) {
      debugPrint(e.toString());
      return (model: null, error: 'Server Not Responding', success: false);
    }
  }
}
