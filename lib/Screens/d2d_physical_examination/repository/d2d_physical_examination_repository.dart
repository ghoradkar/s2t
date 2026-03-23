import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/AllDistrictListForPhyExamResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DTeamWisePhyExamDetailsResponse.dart';

class D2DPhysicalExaminationRepository {
  final _api = APIManager();

  Future<D2DPhysicalExamDetailsResponse?> fetchPhysicalExamDetails(
    Map<String, String> params,
  ) {
    final completer = Completer<D2DPhysicalExamDetailsResponse?>();
    _api.getD2DPhysicalExamDetailsAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }

  Future<AllDistrictListForPhyExamResponse?> fetchAllDistricts(
    Map<String, String> params,
  ) {
    final completer = Completer<AllDistrictListForPhyExamResponse?>();
    _api.getAllDistrictListForPhyExamAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }

  Future<D2DTeamWisePhyExamDetailsResponse?> fetchTeamWiseDetails(
    Map<String, String> params,
  ) {
    final completer = Completer<D2DTeamWisePhyExamDetailsResponse?>();
    _api.getD2DTeamWisePhyExamDetailsAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }
}