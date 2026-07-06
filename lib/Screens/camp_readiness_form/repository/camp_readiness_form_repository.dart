import 'package:http/http.dart' as http;
import 'package:s2toperational/Modules/utilities/api_manager.dart';
import 'package:s2toperational/Modules/constants/api_constants.dart';
import 'package:s2toperational/Modules/constants/api_client.dart';

class CampReadinessFormRepository {
  String get _base => APIManager.kD2DBaseURL;

  static const Map<String, String> _formHeader = {
    'Content-Type': 'application/x-www-form-urlencoded',
  };

  Future<http.Response> getDistrict({required String empCode}) {
    final url = '$_base${APIConstants.kGetDistrictByUserID}';
    return Repository.postResponse(
      url,
      {'STATELGDCODE': '2', 'USERID': empCode},
      _formHeader,
    );
  }

  Future<http.Response> getCampTypeFlexi() {
    return Repository.getResponse('$_base${APIConstants.kCampTypeFlexi}');
  }

  Future<http.Response> getCampTypeMMU() {
    return Repository.getResponse('$_base${APIConstants.kCampTypeMMU}');
  }

  Future<http.Response> getCampList({
    required String campDate,
    required String empCode,
    required String distCode,
    required String campType,
  }) {
    final url = '$_base${APIConstants.kGetCampListCampReadiness}';
    return Repository.postResponse(
      url,
      {
        'CampDATE': campDate,
        'UserId': empCode,
        'DISTLGDCODE': distCode,
        'CampType': campType,
        'LABCODE': '0',
      },
      _formHeader,
    );
  }

  Future<http.Response> getTeamNumber({
    required String campId,
    required String empCode,
  }) {
    final url = '$_base${APIConstants.kGetTeamNumberByCampIdAndUSerId}';
    return Repository.postResponse(
      url,
      {'campid': campId, 'UserID': empCode},
      _formHeader,
    );
  }

  Future<http.Response> getCampReadinessFormItems({
    required int campId,
    required int teamId,
  }) {
    final url =
        '$_base${APIConstants.kGetCampReadinessFormItems}?CampID=$campId&TeamId=$teamId';
    return Repository.getResponse(url);
  }

  Future<http.Response> insertCampReadinessFormDetails({
    required String campId,
    required String campType,
    required String createdBy,
    required String teamId,
    required String formJson,
  }) {
    final url = '$_base${APIConstants.kInsertCampReadinessFormDetails}';
    return Repository.postResponse(
      url,
      {
        'CampID': campId,
        'CampType': campType,
        'Createdby': createdBy,
        'TeamId': teamId,
        'Type_CampReadinessForm': formJson,
      },
      _formHeader,
    );
  }
}