// ignore_for_file: file_names

import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';

class AcknowledgementRepository {
  final APIManager _api = APIManager();

  Future<ResourceReMappingCampResponse?> getCampList({
    required String campDate,
    required int userId,
  }) async {
    ResourceReMappingCampResponse? result;
    await _api.getApprovedCampListDetailsForAppAPI(
      {'CampDATE': campDate, 'UserId': userId.toString()},
      (ResourceReMappingCampResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<AcknowledgementPatientListResponse?> getPatientList({
    required int campId,
    required int userId,
  }) async {
    AcknowledgementPatientListResponse? result;
    final url =
        '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDNew}';
    await _api.getAcknowledgementPatientListAPI(
      url,
      {
        'EmpCode': campId.toString(),
        'DistrictId': '0',
        'TestId': '9',
        'UserId': userId.toString(),
      },
      (AcknowledgementPatientListResponse? response, String error, bool success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<Map<String, dynamic>?> uploadSignature({
    required String regdId,
    required String siteId,
    required String campId,
    required String empCode,
    required bool isDeviceAvailable,
    required File signatureFile,
  }) async {
    final url = Uri.parse(
      '${APIManager.kWebservicesBaseURL}${APIConstants.kInsertSignatureandThumbDetails}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final request = http.MultipartRequest('POST', url);
      request.fields['RegdId'] = regdId;
      request.fields['SiteId'] = siteId;
      request.fields['CampId'] = campId;
      request.fields['IsSignature'] = '1';
      request.fields['IsDeviceIssue'] = isDeviceAvailable ? '1' : '0';
      request.fields['CreatedBy'] = empCode;
      request.files.add(
        await http.MultipartFile.fromPath('File2', signatureFile.path),
      );
      final streamed = await ioClient.send(request);
      final response = await http.Response.fromStream(streamed);
      return json.decode(response.body) as Map<String, dynamic>?;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }
}