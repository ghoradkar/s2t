import 'dart:convert';
import 'dart:io';

import 'package:http/io_client.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/constants/APIConstants.dart';

class SplashRepository {
  IOClient _client() {
    return IOClient(
      HttpClient()..badCertificateCallback = (cert, host, port) => true,
    );
  }

  Future<({String status, String message})> checkAppVersion(
    String version,
  ) async {
    const String applicationId = '91';
    final parts = version.split('.');
    final serverVersion =
        parts.length >= 2 ? '${parts[0]}.${parts[1]}' : version;

    try {
      final url = Uri.parse(
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kAPKDownloader}',
      );
      final response = await _client().post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {'aplicationId': applicationId, 'versionname': serverVersion},
      );
      final decoded = json.decode(response.body);
      return (
        status: decoded['status'] as String? ?? '',
        message: decoded['message'] as String? ?? '',
      );
    } catch (_) {
      return (status: '', message: '');
    }
  }
}
