// ignore_for_file: avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:http/io_client.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Modules/Json_Class/HomeAndHubLabCampCreationResponse/HomeAndHubLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/InitiatedByResponse/InitiatedByResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/ScreeningTestCampCreationResponse/ScreeningTestCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';

class CampCreationRepository {
  IOClient _client() => IOClient(
        HttpClient()..badCertificateCallback = (cert, host, port) => true,
      );

  String get _d2dBase => APIManager.kD2DBaseURL;
  String get _cwBase => APIManager.kConstructionWorkerBaseURL;

  // ─── Camp Type ───────────────────────────────────────────────────────────────

  Future<CampTypeResponse?> fetchCampTypeNonD2D() async {
    final url = Uri.parse('$_d2dBase${APIConstants.kCampTypeNonD2D}');
    try {
      final response = await _client().get(url);
      return CampTypeResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchCampTypeNonD2D error: $e');
      return null;
    }
  }

  Future<CampTypeResponse?> fetchCampTypeFlexi() async {
    final url = Uri.parse('$_d2dBase${APIConstants.kCampTypeFlexi}');
    try {
      final response = await _client().get(url);
      return CampTypeResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchCampTypeFlexi error: $e');
      return null;
    }
  }

  Future<CampTypeResponse?> fetchCampTypeMMU() async {
    final url = Uri.parse('$_d2dBase${APIConstants.kCampTypeMMU}');
    try {
      final response = await _client().get(url);
      return CampTypeResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchCampTypeMMU error: $e');
      return null;
    }
  }

  Future<CampTypeResponse?> fetchCampTypeD2D() async {
    final url = Uri.parse('$_d2dBase${APIConstants.kCampTypeD2D}');
    try {
      final response = await _client().get(url);
      return CampTypeResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchCampTypeD2D error: $e');
      return null;
    }
  }

  // ─── Dropdowns ───────────────────────────────────────────────────────────────

  Future<InitiatedByResponse?> fetchInitiatedBy() async {
    final url =
        Uri.parse('$_d2dBase${APIConstants.kGetInitiatedByListForCamp}');
    try {
      final response = await _client().get(url);
      return InitiatedByResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchInitiatedBy error: $e');
      return null;
    }
  }

  Future<DistrictResponse?> fetchDistrict(int empCode) async {
    final url = Uri.parse('$_d2dBase${APIConstants.kGetDistrictByUserID}');
    try {
      final response = await _client().post(
        url,
        body: {'STATELGDCODE': '2', 'USERID': empCode.toString()},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return DistrictResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchDistrict error: $e');
      return null;
    }
  }

  Future<TalukaCampCreationResponse?> fetchTaluka(int districtId) async {
    final url = Uri.parse('$_d2dBase${APIConstants.kGetAllTalukaList}');
    try {
      final response = await _client().post(
        url,
        body: {
          'STATELGDCODE': '2',
          'DISTLGDCODE': districtId.toString(),
        },
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return TalukaCampCreationResponse.fromJson(json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchTaluka error: $e');
      return null;
    }
  }

  Future<LandingLabCampCreationResponse?> fetchLandingLab(
      int districtId) async {
    final url =
        Uri.parse('$_d2dBase${APIConstants.kGetLabDistrictWiseV1}');
    try {
      final response = await _client().post(
        url,
        body: {'DISTLGDCODE': districtId.toString()},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return LandingLabCampCreationResponse.fromJson(
          json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchLandingLab error: $e');
      return null;
    }
  }

  Future<HomeAndHubLabCampCreationResponse?> fetchHomeAndHubLab(
      int labCode) async {
    final url = Uri.parse(
        '$_d2dBase${APIConstants.kGetHomeAndHubLabNamesOfLandingLab}');
    try {
      final response = await _client().post(
        url,
        body: {'LabCode': labCode.toString(), 'TypeID': '0'},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      return HomeAndHubLabCampCreationResponse.fromJson(
          json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchHomeAndHubLab error: $e');
      return null;
    }
  }

  Future<ScreeningTestCampCreationResponse?> fetchScreeningTests() async {
    // Note: uses kConstructionWorkerBaseURL, not kD2DBaseURL
    final url = Uri.parse('$_cwBase${APIConstants.kTestList}');
    try {
      final response = await _client().post(url);
      return ScreeningTestCampCreationResponse.fromJson(
          json.decode(response.body));
    } catch (e) {
      print('[CampCreation] fetchScreeningTests error: $e');
      return null;
    }
  }

  // ─── Create Camp ─────────────────────────────────────────────────────────────

  Future<({bool success, String error, String campId})> createCamp(
      Map<String, String> params) async {
    final url =
        Uri.parse('$_d2dBase${APIConstants.kInsertCampCreationLatLong}');
    try {
      final response = await _client().post(
        url,
        body: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      final model =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));
      if (model.status == 'Success') {
        return (success: true, error: '', campId: model.message ?? '');
      }
      return (success: false, error: model.message ?? 'Camp creation failed', campId: '');
    } catch (e) {
      return (success: false, error: 'Exception: $e', campId: '');
    }
  }

  // ─── Google Maps ─────────────────────────────────────────────────────────────

  Future<List<Map<String, dynamic>>> autocomplete(String input) async {
    try {
      final uri = Uri.parse(
        'https://maps.googleapis.com/maps/api/place/autocomplete/json'
        '?input=${Uri.encodeComponent(input)}'
        '&components=country:in'
        '&location=19.0,76.0&radius=400000'
        '&key=$_googleMapsApiKey',
      );
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      final data = json.decode(body) as Map<String, dynamic>;
      if (data['status'] == 'OK' || data['status'] == 'ZERO_RESULTS') {
        return List<Map<String, dynamic>>.from(data['predictions'] ?? []);
      }
      print('[CampCreation] autocomplete status: ${data['status']}');
    } catch (e) {
      print('[CampCreation] autocomplete error: $e');
    }
    return [];
  }

  Future<({String address, double lat, double lng})?> fetchPlaceDetails(
      String placeId) async {
    try {
      final uri = Uri.parse(
        'https://maps.googleapis.com/maps/api/place/details/json'
        '?place_id=$placeId'
        '&fields=name,geometry,formatted_address'
        '&key=$_googleMapsApiKey',
      );
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      final data = json.decode(body) as Map<String, dynamic>;

      if (data['status'] == 'OK') {
        final result = data['result'] as Map<String, dynamic>;
        final location = result['geometry']['location'];
        final lat = (location['lat'] as num).toDouble();
        final lng = (location['lng'] as num).toDouble();
        final address = result['formatted_address'] as String? ?? '';
        return (address: address, lat: lat, lng: lng);
      }
      print('[CampCreation] fetchPlaceDetails status: ${data['status']}');
    } catch (e) {
      print('[CampCreation] fetchPlaceDetails error: $e');
    }
    return null;
  }

  static const String _googleMapsApiKey =
      'AIzaSyDbtPLpwrcS571PfdJw9ednQAemxBiNhUA';
}
