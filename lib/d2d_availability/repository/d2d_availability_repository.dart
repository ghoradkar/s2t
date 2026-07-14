import 'dart:convert';
import 'dart:io';

import 'package:http/io_client.dart';

import '../../../utilities/api_manager.dart';
import '../../../constants/api_constants.dart';
import '../models/d2d_availability_update_response.dart';
import '../models/get_doc_list_d2d_response.dart';

class D2DAvailabilityRepository {
  IOClient _client() {
    return IOClient(
      HttpClient()..badCertificateCallback = (cert, host, port) => true,
    );
  }

  Future<GetDocListD2DResponse?> fetchDoctorStatus(int empCode) async {
    try {
      final url = Uri.parse(
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetDoctorListCSCCampAvailaible}',
      );
      final response = await _client().post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {'DocUserid': empCode.toString()},
      );
      final data = GetDocListD2DResponse.fromJson(json.decode(response.body));
      return data.status?.toLowerCase() == 'success' ? data : null;
    } catch (_) {
      return null;
    }
  }

  Future<D2DAvailabilityUpdateResponse?> updateDoctorStatus(
    int empCode,
    int status,
  ) async {
    try {
      final url = Uri.parse(
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kInsertCscDoctorAvailaibilityStatus}',
      );
      final response = await _client().post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {
          'DocUserid': empCode.toString(),
          'DocStatus': status.toString(),
          'Createdby': empCode.toString(),
        },
      );
      return D2DAvailabilityUpdateResponse.fromJson(json.decode(response.body));
    } catch (_) {
      return null;
    }
  }
}
