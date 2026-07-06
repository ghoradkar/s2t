// ignore_for_file: file_names

import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';
import 'package:s2toperational/Modules/utilities/api_manager.dart';
import 'package:s2toperational/Screens/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:s2toperational/Screens/health_screening_details/models/camp_details_on_lab_for_door_to_door_response.dart';
import 'package:s2toperational/Screens/resource_re_mapping/models/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/constants/api_constants.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/team_number_by_campId_and_user_id_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';

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

  Future<List<DistrictOutput>> getDistrictList({
    required String empCode,
    required String subOrgId,
    required String desgId,
  }) async {
    final repo = D2DPatientRegistrationRepository();
    final result = await repo.getDistrictList(
      empCode: empCode,
      subOrgId: subOrgId,
      desgId: desgId,
    );
    return result?.output ?? [];
  }

  Future<ResourceReMappingCampResponse?> getD2DCampList({
    required String campDate,
    required int userId,
    required int desgId,
    required int subOrgId,
    required String distLgdCode,
    required int cityCode,
    required String divisionId,
  }) async {
    CampDetailsonLabForDoorToDoorResponse? d2dResponse;
    await _api.getCampDetailsonLabForDoorToDoorV2API(
      {
        'CampDate': campDate,
        'LabCode': '0',
        'SubOrgId': subOrgId.toString(),
        'Divison': '0',
        'DISTLGDCODE': distLgdCode,
        'USERID': userId.toString(),
        'DesgId': desgId.toString(),
      },
      (CampDetailsonLabForDoorToDoorResponse? response, String error, bool success) {
        if (success) d2dResponse = response;
      },
    );
    if (d2dResponse == null) return null;
    final converted = d2dResponse!.output?.map((d) {
      return ResourceReMappingCampOutput()
        ..campId = d.campId
        ..siteDetailId = d.siteDetailId
        ..campLocation = d.campLocation
        ..campDate = d.campDate
        ..dISTNAME = d.dISTNAME
        ..campType = d.campType
        ..campTypeDescription = d.campTypeDescription
        ..campName = d.campName
        ..initiatedBy1 = d.initiatedBy1
        ..campCreatedBy = d.campCreatedBy;
    }).toList() ?? [];
    return ResourceReMappingCampResponse()
      ..status = d2dResponse!.status
      ..message = d2dResponse!.message
      ..output = converted;
  }

  Future<String> getTeamId({
    required int campId,
    required int userId,
  }) async {
    String teamId = '0';
    await _api.getTeamNumberByCampIdAndUSerIdAPI(
      {'campid': campId.toString(), 'UserID': userId.toString()},
      (TeamNumberByCampIdAndUserIdListResponse? response, String error, bool success) {
        if (success && response?.output?.isNotEmpty == true) {
          teamId = response!.output!.first.teamNumber ?? '0';
        }
      },
    );
    return teamId;
  }

  Future<AcknowledgementPatientListResponse?> getPatientList({
    required int campId,
    required int userId,
    required String teamId,
    String testId = '9',
  }) async {
    AcknowledgementPatientListResponse? result;

    // Regular Camp (no team assigned) â†’ _RationCard, no TeamId (matches native)
    // D2D Camp (team found) â†’ _Anti_RationCard, include TeamId (matches native)
    final isD2D = teamId != '0';
    final url = isD2D
        ? '${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDAntiRationCard}'
        : '${APIManager.kD2DBaseURL}${APIConstants.kGetUserAttendancesUsingSitedetailsIDRationCard}';

    final params = <String, String>{
      'EmpCode': campId.toString(),
      'DistrictId': '0',
      'TestId': testId,
      'UserId': userId.toString(),
    };
    if (isD2D) params['TeamId'] = teamId;

    await _api.getAcknowledgementPatientListAPI(
      url,
      params,
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
    int bocwIdDepend = 0,
    required File signatureFile,
    File? thumbFile,
  }) async {
    final url = Uri.parse(
      '${APIManager.kWebservicesBaseURL}${APIConstants.kInsertSignatureandThumbDetailsV1RC}',
    );
    try {
      // Build multipart body exactly like native MultipartUtility:
      //   boundary = ===<timestamp>===
      //   file parts include Content-Transfer-Encoding: binary
      final boundary = '===${DateTime.now().millisecondsSinceEpoch}===';
      const crlf = '\r\n';
      final body = <int>[];

      void addField(String name, String value) {
        body.addAll(utf8.encode('--$boundary$crlf'));
        body.addAll(utf8.encode('Content-Disposition: form-data; name="$name"$crlf'));
        body.addAll(utf8.encode(crlf));
        body.addAll(utf8.encode('$value$crlf'));
      }

      Future<void> addFile(
          String name, String filename, File file, String mimeType) async {
        body.addAll(utf8.encode('--$boundary$crlf'));
        body.addAll(utf8.encode(
            'Content-Disposition: form-data; name="$name"; filename="$filename"$crlf'));
        body.addAll(utf8.encode('Content-Type: $mimeType$crlf'));
        body.addAll(utf8.encode('Content-Transfer-Encoding: binary$crlf'));
        body.addAll(utf8.encode(crlf));
        body.addAll(await file.readAsBytes());
        body.addAll(utf8.encode(crlf));
      }

      addField('RegdId', regdId);
      addField('SiteId', siteId);
      addField('CampId', campId);
      addField('IsSignature', '1');
      addField('CreatedBy', empCode);
      addField('IsDeviceIssue', isDeviceAvailable ? '1' : '0');
      addField('Bocw_idDepend', bocwIdDepend.toString());
      await addFile('UploadedPath', signatureFile.uri.pathSegments.last,
          signatureFile, 'image/png');
      if (thumbFile != null) {
        await addFile('ThaumbPath', thumbFile.uri.pathSegments.last,
            thumbFile, 'image/jpeg');
      }
      body.addAll(utf8.encode('--$boundary--$crlf'));

      final httpClient = HttpClient();
      try {
        final ioRequest = await httpClient.postUrl(url);
        ioRequest.headers.set(
            HttpHeaders.contentTypeHeader,
            'multipart/form-data; boundary=$boundary');
        ioRequest.headers.contentLength = body.length;
        ioRequest.add(body);
        final ioResponse = await ioRequest.close();
        final responseBody =
            await ioResponse.transform(const Utf8Decoder()).join();
        print('[AckSign] HTTP status: ${ioResponse.statusCode}');
        print('[AckSign] Raw response: $responseBody');
        return json.decode(responseBody) as Map<String, dynamic>?;
      } finally {
        httpClient.close();
      }
    } catch (e) {
      print('[AckSign] uploadSignature exception: $e');
      return null;
    }
  }
}
