// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/patient_registration/model/attendance_status_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/beneficiary_details_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_camp_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/d2d_registration_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/district_list_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/document_type_response.dart';
import 'package:s2toperational/Screens/patient_registration/model/worker_info_response.dart';

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

  /// Calls the native D2D API with "MH" prefix — mirrors GetWorkerInfoNew AsyncTask
  Future<WorkerInfoResponse?> getWorkerInfoWithMaritalStatus({
    required String regNo,
  }) async {
    final completer = Completer<WorkerInfoResponse?>();
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetBeneficiaryRegistrationDetailsWithMaritalStatus}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'regno': 'MH$regNo'},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = WorkerInfoResponse.fromJson(decoded);
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

  /// GET request — same base as ConstructionWorker_V2.asmx (native: webservice + GetDocumenttype)
  Future<DocumentTypeResponse?> getDocumentTypeList() async {
    final completer = Completer<DocumentTypeResponse?>();
    final url = Uri.parse(
      '${APIManager.kConstructionWorkerBaseURL}${APIConstants.kGetDocumenttype}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      final decoded = json.decode(response.body);
      final result = DocumentTypeResponse.fromJson(decoded);
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

  /// Returns `null` on success, or the API error message string on failure.
  Future<String?> sendOtp({
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
      if (decoded['status']?.toString().toLowerCase() == 'success') {
        return null; // success
      }
      return decoded['message']?.toString().isNotEmpty == true
          ? decoded['message'].toString()
          : 'Failed to send OTP';
    } catch (_) {
      return 'Failed to send OTP';
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

  /// GET — returns all districts (no params needed)
  Future<DistrictListResponse?> getDistrictListForReg() async {
    final url = Uri.parse(
      '${APIManager.kConstructionWorkerBaseURL}${APIConstants.getDistrictList}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      final decoded = json.decode(response.body);
      final result = DistrictListResponse.fromJson(decoded);
      return (result.status?.toLowerCase() == 'success') ? result : null;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }

  /// POST — returns talukas for a given district LGD code
  Future<UserMappedTalukaResponse?> getTalukaListForReg({
    required String distLgdCode,
  }) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetAllTalukaList}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'STATELGDCODE': '2', 'DISTLGDCODE': distLgdCode},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = json.decode(response.body);
      final result = UserMappedTalukaResponse.fromJson(decoded);
      return (result.status?.toLowerCase() == 'success') ? result : null;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }

  /// Returns worker's internal RegdId (DependREGID) and Count from GetBenificiaryRegisterOrNot.
  /// Native appends Count to the worker reg number to form RegdNo in the submit payload.
  Future<Map<String, String>> getWorkerRegdId({required String regdNo}) async {
    final url = Uri.parse(
      '${APIManager.kD2DBaseURL}${APIConstants.kGetBenificiaryRegisterOrNot}',
    );
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'RegdNo': regdNo},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      // ignore: avoid_print
      print('[getWorkerRegdId] raw response: ${response.body}');
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString().toLowerCase();
      if (status == 'success') {
        final output = decoded['output'];
        if (output is List && output.isNotEmpty) {
          final item = output[0] as Map<String, dynamic>;
          // ignore: avoid_print
          print('[getWorkerRegdId] output[0] keys: ${item.keys.toList()}');
          // Try all known casing variants for RegdId and Count
          final regdId = (item['RegdId'] ??
                  item['REGDID'] ??
                  item['regdId'] ??
                  item['RegID'] ??
                  item['Regdid'])
              ?.toString() ??
              '0';
          final count = (item['Count'] ??
                  item['COUNT'] ??
                  item['count'] ??
                  item['BenCount'] ??
                  item['BENCOUNT'])
              ?.toString() ??
              '0';
          // ignore: avoid_print
          print('[getWorkerRegdId] regdId=$regdId  count=$count');
          return {'regdId': regdId, 'count': count};
        }
      }
    } catch (e) {
      // ignore: avoid_print
      print('[getWorkerRegdId] error: $e');
    } finally {
      ioClient.close();
    }
    return {'regdId': '0', 'count': '0'};
  }

  Future<D2DRegistrationResponse?> saveD2DRegistration({
    required Map<String, String> fields,
    required bool isFaceDetectionEnabled,
    File? patientPhoto,
    File? healthCardPhoto,
    File? renewalSlipPhoto,
    File? hivLetterPhoto,
  }) async {
    const endpoint =
        'handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_Taluka_Gender.ashx';
    final url = Uri.parse('${APIManager.kWebservicesBaseURL}$endpoint');
    final ioClient = _api.getInstanceOfIoClient();
    try {
      // ignore: avoid_print
      print('[saveD2DRegistration] FIELDS: ${fields.entries.map((e) => "${e.key}=${e.value}").join(", ")}');
      final request = http.MultipartRequest('POST', url);
      fields.forEach((key, value) => request.fields[key] = value);

      final regdNo = fields['RegdNo'] ?? '';
      if (patientPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'file1',
            patientPhoto.path,
            filename: '${regdNo}_PR.jpg',
          ),
        );
      }
      if (healthCardPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'file2',
            healthCardPhoto.path,
            filename: '${regdNo}_HC.jpg',
          ),
        );
      }
      if (renewalSlipPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'file3',
            renewalSlipPhoto.path,
            filename: '${regdNo}_RS.jpg',
          ),
        );
      }
      if (hivLetterPhoto != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'file4',
            hivLetterPhoto.path,
            filename: '${regdNo}_HIV.jpg',
          ),
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
