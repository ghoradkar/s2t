// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';

import 'package:http/http.dart' as http;
import 'package:pointycastle/export.dart' as pc;
import 'package:pointycastle/asn1.dart' as asn1;
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
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

  // ────────────────────────────────────────────────────────
  // ABHA APIs — environment-aware (matches native isBeta flag)
  // ────────────────────────────────────────────────────────

  bool get _isLive => APIManager().apiMode == APIMode.Live;

  // Session base URL
  //   Beta  → https://dev.abdm.gov.in/
  //   Live  → https://abha.abdm.gov.in/
  String get _abhaSessionUrl => _isLive
      ? 'https://abha.abdm.gov.in/gateway/v0.5/sessions'
      : 'https://dev.abdm.gov.in/gateway/v0.5/sessions';

  // ABDM enrollment/profile API base
  //   Beta  → https://abhasbx.abdm.gov.in/abha/api/v3/
  //   Live  → https://abha.abdm.gov.in/api/abha/v3/
  String get _abdmBase => _isLive
      ? 'https://abha.abdm.gov.in/api/abha/v3/'
      : 'https://abhasbx.abdm.gov.in/abha/api/v3/';

  // Client credentials
  String get _abhaClientId => _isLive ? 'HIL_001' : 'SBXID_009192';
  String get _abhaClientSecret => _isLive
      ? '8c972582-9723-4c1b-9c25-bb880eb65685'
      : 'ea0082ef-fcd6-44d9-aa8a-5530b1edfbd4';
  String get _abhaCmId => _isLive ? 'abdm' : 'sbx';

  // ABDM requires UTC timestamp; native uses SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  // with TimeZone.getTimeZone("UTC") — Android JDK outputs "Z" suffix for UTC zero-offset
  String _abdmTimestamp() {
    final now = DateTime.now().toUtc();
    final ms = now.millisecond.toString().padLeft(3, '0');
    return '${now.year}-${now.month.toString().padLeft(2, '0')}-'
        '${now.day.toString().padLeft(2, '0')}T'
        '${now.hour.toString().padLeft(2, '0')}:'
        '${now.minute.toString().padLeft(2, '0')}:'
        '${now.second.toString().padLeft(2, '0')}.'
        '${ms}Z';
  }

  /// RSA-OAEP with SHA-1 encryption; returns base64-encoded result.
  String _rsaEncryptBase64(String plainText, String base64PublicKey) {
    // Decode DER SubjectPublicKeyInfo
    final keyBytes = base64Decode(base64PublicKey);
    final parser = asn1.ASN1Parser(keyBytes);
    final seq = parser.nextObject() as asn1.ASN1Sequence;
    final bitStr = seq.elements![1] as asn1.ASN1BitString;
    // stringValues contains the DER bytes of the RSAPublicKey inside the BIT STRING
    final innerBytes = Uint8List.fromList(bitStr.stringValues!);
    final innerParser = asn1.ASN1Parser(innerBytes);
    final innerSeq = innerParser.nextObject() as asn1.ASN1Sequence;
    final modulus = (innerSeq.elements![0] as asn1.ASN1Integer).integer!;
    final exponent = (innerSeq.elements![1] as asn1.ASN1Integer).integer!;

    final publicKey = pc.RSAPublicKey(modulus, exponent);
    final cipher = pc.OAEPEncoding.withSHA1(pc.RSAEngine())
      ..init(true, pc.PublicKeyParameter<pc.RSAPublicKey>(publicKey));

    final encrypted =
        cipher.process(Uint8List.fromList(utf8.encode(plainText)));
    return base64Encode(encrypted);
  }

  Future<String?> createAbhaSession() async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      // ignore: avoid_print
      print('[createAbhaSession] url=$_abhaSessionUrl  clientId=$_abhaClientId  cmId=$_abhaCmId');
      final response = await ioClient.post(
        Uri.parse(_abhaSessionUrl),
        headers: {
          'Content-Type': 'application/json',
          'TIMESTAMP': _abdmTimestamp(),
          'REQUEST-ID': _uuid(),
          'X-CM-ID': _abhaCmId,
        },
        body: jsonEncode({
          'clientId': _abhaClientId,
          'clientSecret': _abhaClientSecret,
          'grantType': 'client_credentials',
        }),
      );
      // ignore: avoid_print
      print('[createAbhaSession] status=${response.statusCode}  body=${response.body}');
      if (response.statusCode == 200) {
        final j = jsonDecode(response.body);
        return j['accessToken'] as String?;
      }
    } catch (e) {
      // ignore: avoid_print
      print('[createAbhaSession] exception: $e');
    } finally {
      ioClient.close();
    }
    return null;
  }

  Future<String?> getAbhaPublicCertificate(String accessToken) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final url = '${_abdmBase}profile/public/certificate';
      final ts = _abdmTimestamp();
      // ignore: avoid_print
      print('[getAbhaPublicCertificate] GET $url  TIMESTAMP=$ts');
      final response = await ioClient.get(
        Uri.parse(url),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': ts,
          'REQUEST-ID': _uuid(),
        },
      );
      // ignore: avoid_print
      print('[getAbhaPublicCertificate] status=${response.statusCode}  body=${response.body}');
      if (response.statusCode == 200) {
        final j = jsonDecode(response.body);
        return j['publicKey'] as String?;
      }
    } catch (e) {
      // ignore: avoid_print
      print('[getAbhaPublicCertificate] error: $e');
    } finally {
      ioClient.close();
    }
    return null;
  }

  /// Returns `{'txnId': ..., 'message': ...}` on success, or null on failure.
  Future<Map<String, dynamic>?> generateAadhaarOtp({
    required String accessToken,
    required String publicKey,
    required String aadhaarNumber,
    String txnId = '',
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final encrypted = _rsaEncryptBase64(aadhaarNumber, publicKey);
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/request/otp'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': _abdmTimestamp(),
          'REQUEST-ID': _uuid(),
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'txnId': txnId,
          'scope': ['abha-enrol'],
          'loginHint': 'aadhaar',
          'loginId': encrypted,
          'otpSystem': 'aadhaar',
        }),
      );
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Enrols by Aadhaar OTP. Returns parsed JSON or `{'error': ...}`.
  Future<Map<String, dynamic>?> enrolByAadhaarOtp({
    required String accessToken,
    required String publicKey,
    required String txnId,
    required String otpValue,
    required String mobile,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final encOtp = _rsaEncryptBase64(otpValue, publicKey);
      final ts = _abdmTimestamp();
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/enrol/byAadhaar'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': ts,
          'REQUEST-ID': _uuid(),
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'authData': {
            'authMethods': ['otp'],
            'otp': {
              'txnId': txnId,
              'otpValue': encOtp,
              'mobile': mobile,
              'timeStamp': ts,
            },
          },
          'consent': {'code': 'abha-enrollment', 'version': '1.4'},
        }),
      );
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Sends OTP to mobile for verification.
  Future<Map<String, dynamic>?> sendMobileOtpForAbha({
    required String accessToken,
    required String publicKey,
    required String txnId,
    required String mobile,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final encMobile = _rsaEncryptBase64(mobile, publicKey);
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/request/otp'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': _abdmTimestamp(),
          'REQUEST-ID': _uuid(),
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'txnId': txnId,
          'scope': ['abha-enrol', 'mobile-verify'],
          'loginHint': 'mobile',
          'loginId': encMobile,
          'otpSystem': 'abdm',
        }),
      );
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Verifies mobile OTP for ABHA enrolment.
  Future<Map<String, dynamic>?> verifyMobileOtpForAbha({
    required String accessToken,
    required String publicKey,
    required String txnId,
    required String otpValue,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final encOtp = _rsaEncryptBase64(otpValue, publicKey);
      final ts = _abdmTimestamp();
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/auth/byAbdm'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': ts,
          'REQUEST-ID': _uuid(),
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'scope': ['abha-enrol', 'mobile-verify'],
          'authData': {
            'authMethods': ['otp'],
            'otp': {
              'timeStamp': ts,
              'txnId': txnId,
              'otpValue': encOtp,
            },
          },
        }),
      );
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Saves ABHA details to own server. Returns true on success.
  Future<bool> insertAbhaRegistration({
    required Map<String, dynamic> payload,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final url = Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kInsertAbhaRegistration}',
      );
      final response = await ioClient.post(
        url,
        body: {'AbhaRegistrationJSON': jsonEncode(payload)},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final decoded = jsonDecode(response.body);
      return decoded['status']?.toString().toLowerCase() == 'success';
    } catch (e) {
      // ignore: avoid_print
      print('[insertAbhaRegistration] error: $e');
      return false;
    } finally {
      ioClient.close();
    }
  }

  /// Enrols by demographic data. Returns parsed JSON or `{'error': ...}`.
  Future<Map<String, dynamic>?> enrolByDemographic({
    required String accessToken,
    required String publicKey,
    required String aadhaarNumber,
    required String name,
    required String dob,      // yyyy-MM-dd
    required String gender,   // M, F, O
    required String address,
    required String state,
    required String district,
    required String pinCode,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final encAadhaar = _rsaEncryptBase64(aadhaarNumber, publicKey);
      final ts = _abdmTimestamp();
      // ignore: avoid_print
      print('[enrolByDemographic] name=$name  gender=$gender  dob=$dob  state=$state  district=$district  pinCode=$pinCode');
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/enrol/byDemographic'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': ts,
          'REQUEST-ID': _uuid(),
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'authData': {
            'authMethods': ['demo'],
            'demo': {
              'aadhaarNumber': encAadhaar,
              'name': name,
              'gender': gender,
              'dateOfBirth': dob,
              'address': {
                'district': district,
                'pinCode': pinCode,
                'state': state,
              },
            },
          },
          'consent': {'code': 'abha-enrollment', 'version': '1.4'},
        }),
      );
      // ignore: avoid_print
      print('[enrolByDemographic] status=${response.statusCode}  body=${response.body}');
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      // ignore: avoid_print
      print('[enrolByDemographic] exception: $e');
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Returns list of ABHA address suggestions.
  Future<List<String>> getAbhaAddressSuggestions({
    required String accessToken,
    required String txnId,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.get(
        Uri.parse('${_abdmBase}enrollment/enrol/suggestion'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'TIMESTAMP': _abdmTimestamp(),
          'REQUEST-ID': _uuid(),
          'Transaction_Id': txnId,
        },
      );
      if (response.statusCode == 200) {
        final j = jsonDecode(response.body);
        final arr = j['abhaAddressList'];
        if (arr is List) return arr.map((e) => e.toString()).toList();
      }
    } catch (e) {
      // ignore: avoid_print
      print('[getAbhaAddressSuggestions] error: $e');
    } finally {
      ioClient.close();
    }
    return [];
  }

  /// Creates an ABHA address. Returns parsed JSON or `{'error': ...}`.
  ///
  /// [accessToken] = client_credentials session token (from createAbhaSession).
  /// [authToken]   = enrollment t-token (from enrolByAadhaarOtp tokens.token).
  ///                 Added as X-Token header; required by ABDM v3 sandbox.
  Future<Map<String, dynamic>?> createAbhaAddress({
    required String accessToken,
    required String txnId,
    required String address,
    String authToken = '',
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final reqId = _uuid();
      final ts = _abdmTimestamp();
      // ignore: avoid_print
      print('[createAbhaAddress] accessToken.len=${accessToken.length}  authToken.len=${authToken.length}  txnId=$txnId  address=$address');
      final headers = <String, String>{
        'Authorization': 'Bearer $accessToken',
        'REQUEST-ID': reqId,
        'TIMESTAMP': ts,
        'Content-Type': 'application/json',
      };
      // Add X-Token only when a valid t-token is available
      if (authToken.isNotEmpty) {
        headers['X-Token'] = 'Bearer $authToken';
      }
      final response = await ioClient.post(
        Uri.parse('${_abdmBase}enrollment/enrol/abha-address'),
        headers: headers,
        body: jsonEncode({
          'abhaAddress': address,
          'txnId': txnId,
          'preferred': 1,
        }),
      );
      // ignore: avoid_print
      print('[createAbhaAddress] status: ${response.statusCode}  body: ${response.body}');
      if (response.statusCode == 200) {
        return jsonDecode(response.body) as Map<String, dynamic>;
      }
      return {'error': response.body};
    } catch (e) {
      // ignore: avoid_print
      print('[createAbhaAddress] exception: $e');
      return {'error': e.toString()};
    } finally {
      ioClient.close();
    }
  }

  /// Downloads the ABHA card as a PNG image.
  /// Returns raw bytes on success, null on failure.
  ///
  /// ABDM v3: Authorization = session token, X-Token = enrollment t-token.
  /// The endpoint returns 202 with PNG bytes on success.
  Future<Uint8List?> downloadAbhaCard({
    required String accessToken,
    required String authToken,
  }) async {
    final ioClient = _api.getInstanceOfIoClient();
    try {
      final response = await ioClient.get(
        Uri.parse('${_abdmBase}profile/account/abha-card'),
        headers: {
          'Authorization': 'Bearer $accessToken',
          'X-Token': 'Bearer $authToken',
          'REQUEST-ID': _uuid(),
          'TIMESTAMP': _abdmTimestamp(),
          'Accept': 'image/png',
        },
      );
      if ((response.statusCode == 200 || response.statusCode == 202) &&
          response.bodyBytes.isNotEmpty) {
        return response.bodyBytes;
      }
      return null;
    } catch (_) {
      return null;
    } finally {
      ioClient.close();
    }
  }

  String _uuid() {
    // Simple UUID v4 generation without external package
    final random = List.generate(16, (_) => (DateTime.now().microsecondsSinceEpoch % 256));
    random[6] = (random[6] & 0x0f) | 0x40;
    random[8] = (random[8] & 0x3f) | 0x80;
    String hex(int n) => n.toRadixString(16).padLeft(2, '0');
    return '${hex(random[0])}${hex(random[1])}${hex(random[2])}${hex(random[3])}-'
        '${hex(random[4])}${hex(random[5])}-'
        '${hex(random[6])}${hex(random[7])}-'
        '${hex(random[8])}${hex(random[9])}-'
        '${hex(random[10])}${hex(random[11])}${hex(random[12])}${hex(random[13])}${hex(random[14])}${hex(random[15])}';
  }

  // ────────────────────────────────────────────────────────

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
