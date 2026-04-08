// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/patient_registration/model/attendance_status_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/beneficiary_details_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_camp_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_registration_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';

class D2DPatientRegistrationRepository {
  final _api = APIManager();

  Future<DistrictListResponse?> getDistrictList({required String empCode}) async {
    final completer = Completer<DistrictListResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetDistrictByUserID}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'STATELGDCODE': '27', 'USERID': empCode},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = DistrictListResponse.fromJson(decoded);
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

  Future<D2DCampResponse?> getCampList({
    required String campDate,
    required String subOrgId,
    required String distLgdCode,
    required String userId,
    required String desgId,
  }) async {
    final completer = Completer<D2DCampResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetCampDetailsonLabForDoorToDoorV2}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'CampDate': campDate,
          'LabCode': '0',
          'SubOrgId': subOrgId,
          'Param4': '0',
          'DistLgdCode': distLgdCode,
          'UserId': userId,
          'DesignId': desgId,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = D2DCampResponse.fromJson(decoded);
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
    required String campId,
  }) async {
    final completer = Completer<AttendanceStatusResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetUserCampMappingD2DClose}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'CampDATE': campDate,
          'UserId': userId,
          'DISTLGDCODE': distLgdCode,
          'CampType': '3',
          'CampID': campId,
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

  Future<RelationModel?> getRelationList({
    required String maritalStatusId,
    required String genderId,
  }) async {
    final completer = Completer<RelationModel?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetRelationwithMaritalStatus}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'MaritalStatusID': maritalStatusId,
          'Gender': genderId,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = RelationModel.fromJson(decoded);
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

  Future<bool> sendOtp({
    required String mobileNo,
    required String otp,
    required String regdId,
    required String createdBy,
    required String subOrgId,
  }) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetOTPforRegistrationOrg}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {
          'MOBNO': mobileNo,
          'OTP': otp,
          'RegdId': regdId,
          'CreatedBy': createdBy,
          'MsgID': '0',
          'SubOrgID': subOrgId,
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      return decoded['status']?.toString().toLowerCase() == 'success';
    } catch (_) {
      return false;
    } finally {
      ioClient.close();
    }
  }

  Future<bool> verifyOtp({required String mobileNo, required String otp}) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kVerifyRegistrationOTP}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'MOBNO': mobileNo, 'OTP': otp},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      return decoded['status']?.toString().toLowerCase() == 'success';
    } catch (_) {
      return false;
    } finally {
      ioClient.close();
    }
  }

  Future<D2DRegistrationResponse?> saveD2DRegistration({
    required Map<String, String> fields,
    required bool isFaceDetectionEnabled,
    File? patientPhoto,
    File? healthCardPhoto,
    File? renewalSlipPhoto,
    File? hivLetterPhoto,
  }) async {
    final endpoint = isFaceDetectionEnabled
        ? 'handler/DtoDBeneficiaryRe_RegistrationV14.ashx'
        : 'handler/DtoDBeneficiaryRe_RegistrationV15.ashx';
    final url = Uri.parse('${APIManager.kConstructionWorkerBaseURL}$endpoint');
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
      if (hivLetterPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath('file4', hivLetterPhoto.path),
        );
      }

      final streamed = await ioClient.send(request);
      final response = await http.Response.fromStream(streamed);
      final decoded = json.decode(response.body);
      final result = D2DRegistrationResponse.fromJson(decoded);
      return (result.status?.toLowerCase() == 'success') ? result : result;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }
}
