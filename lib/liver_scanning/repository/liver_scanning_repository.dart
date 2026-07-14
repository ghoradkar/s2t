// ignore_for_file: file_names

import 'package:http/http.dart' as http;
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/constants/api_constants.dart';
import 'package:s2toperational/constants/api_client.dart';

class LiverScanningRepository {
  Future<http.Response> getLiverDashCount() {
    final uri = "${APIManager.kLiverScann}${APIConstants.getLiverDashCount}";
    return Repository.getResponse(uri);
  }

  Future<http.StreamedResponse> getLiverTableData(
    Map<String, String> body,
  ) {
    final uri =
        "${APIManager.kLiverScann}${APIConstants.getLiverScanningTableData}";
    return Repository.postFormEncodedRequest(
      uri,
      body,
      {'Content-Type': 'application/x-www-form-urlencoded'},
    );
  }

  Future<http.StreamedResponse> getLiverTableDataDistrictWise(
    Map<String, String> body,
  ) {
    final uri =
        "${APIManager.kLiverScann}${APIConstants.getLiverScanningTableDataDistrictWise}";
    return Repository.postFormEncodedRequest(
      uri,
      body,
      {'Content-Type': 'application/x-www-form-urlencoded'},
    );
  }
}