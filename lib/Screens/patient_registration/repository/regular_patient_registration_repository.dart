// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Screens/patient_registration/model/attendance_status_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/beneficiary_details_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/regular_registration_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/select_camp_response.dart';

class RegularPatientRegistrationRepository {
  final _api = APIManager();

  Future<SelectCampResponse?> getCampList({
    required String campDate,
    required String subOrgId,
    required String empCode,
    required String desgId,
  }) async {
    final completer = Completer<SelectCampResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetApprovedCampListDetailsForAppFlexiCampV1}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'CampDATE': campDate,
          'SubOrgId': subOrgId,
          'Divison': '0',
          'DISTLGDCODE': '0',
          'USERID': empCode,
          'DesgId': desgId,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = SelectCampResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  Future<AttendanceStatusResponse?> checkAttendanceStatus({
    required String campDate,
    required String userId,
    required String distLgdCode,
    required String campType,
    required String campId,
  }) async {
    final completer = Completer<AttendanceStatusResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetUserCampMappingRegularCampClose}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'CampDATE': campDate,
          'UserId': userId,
          'DISTLGDCODE': distLgdCode,
          'CampType': campType,
          'CampID': campId,
          'TestId': '1',
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = AttendanceStatusResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  Future<BeneficiaryDetailsResponse?> getBeneficiaryFromBocw({
    required String workerRegNo,
  }) async {
    final completer = Completer<BeneficiaryDetailsResponse?>();
    final url = Uri.parse('${APIManager.kMahabocwBaseURL}bocw-registration/$workerRegNo');
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      final decoded = json.decode(response.body);
      final result = BeneficiaryDetailsResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  Future<BeneficiaryDetailsResponse?> checkInternalRegistration({
    required String workerRegNo,
  }) async {
    final completer = Completer<BeneficiaryDetailsResponse?>();
    final url = Uri.parse(
      '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetWorkerInfroReRegistration}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'RegdNo': workerRegNo},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = BeneficiaryDetailsResponse.fromJson(decoded);
      completer.complete(
        (result.status?.toLowerCase() == 'success') ? result : null,
      );
    } catch (_) {
      completer.complete(null);
    } finally {
      ioClient.close();
    }
    return completer.future;
  }

  Future<RegularRegistrationResponse?> saveRegistration({
    required Map<String, String> fields,
    File? patientPhoto,
    File? healthCardPhoto,
    File? renewalSlipPhoto,
  }) async {
    final url = Uri.parse(
      '${APIManager.kConstructionWorkerBaseURL}handler/BeneficiaryRe_RegistrationNew.ashx',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final request = http.MultipartRequest('POST', url);
      fields.forEach((key, value) => request.fields[key] = value);

      if (patientPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath('file1', patientPhoto.path),
        );
      }
      if (healthCardPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath('file2', healthCardPhoto.path),
        );
      }
      if (renewalSlipPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath('file3', renewalSlipPhoto.path),
        );
      }

      final streamed = await ioClient.send(request);
      final response = await http.Response.fromStream(streamed);
      final decoded = json.decode(response.body);
      final result = RegularRegistrationResponse.fromJson(decoded);
      return (result.status?.toLowerCase() == 'success') ? result : result;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }

  Future<void> syncToBocw({required Map<String, dynamic> payload}) async {
    final url = Uri.parse('${APIManager.kMahabocwBaseURL}bocw-registration');
    final ioClient = _api.getInstanceOfIoClient();
    try {
      await ioClient.post(
        url,
        body: json.encode(payload),
        headers: {'Content-Type': 'application/json'},
      );
    } catch (_) {
      // swallow
    } finally {
      ioClient.close();
    }
  }
}

