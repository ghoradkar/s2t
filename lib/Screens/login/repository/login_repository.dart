import 'dart:convert';
import 'dart:io';

import 'package:http/io_client.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../models/login_response_model.dart';
import '../../../Modules/constants/APIConstants.dart';
import '../../../Modules/utilities/DataProvider.dart';

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
    } catch (_) {
      return (model: null, error: 'Server Not Responding', success: false);
    }
  }
}
