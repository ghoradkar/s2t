import 'dart:async';

import 'package:s2toperational/Modules/utilities/api_manager.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/team_wise_physical_exam_details_response.dart';

class CallToTeamRepository {
  final APIManager _apiManager = APIManager();

  Future<TeamWisePhysicalExamDetailsResponse?> fetchTeamDetails({
    required int campId,
    required int doctorId,
    required String teamId,
  }) {
    final completer = Completer<TeamWisePhysicalExamDetailsResponse?>();
    final params = <String, String>{
      "Type": "2",
      "CampID": campId.toString(),
      "DoctorID": doctorId.toString(),
      "TeamID": teamId,
    };
    _apiManager.getCallToTeamDetailsAPI(params, (response, error, success) {
      if (success) {
        completer.complete(response);
      } else {
        completer.completeError(error);
      }
    });
    return completer.future;
  }
}