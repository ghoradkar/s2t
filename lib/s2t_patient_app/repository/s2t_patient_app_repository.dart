// ignore_for_file: file_names

import 'package:http/http.dart' as http;
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/constants/api_constants.dart';
import 'package:s2toperational/constants/api_client.dart';

class S2TPatientAppRepository {
  static const Duration _timeout = Duration(minutes: 5);

  Future<http.StreamedResponse> getAndroidIosCount() {
    final uri =
        '${APIManager.kTreatmentCount}${APIConstants.getAndroidIosCount}';
    return Repository.postResponseWithoutBody(uri, timeout: _timeout);
  }

  Future<http.StreamedResponse> getAndroidIosDistrictWiseList() {
    final uri = '${APIManager.kTreatmentCount}${APIConstants.kIosCount}';
    return Repository.postResponseWithoutBody(uri, timeout: _timeout);
  }
}