import 'package:http/http.dart' as http;

import '../../../utilities/api_manager.dart';
import '../../../constants/api_constants.dart';

class CallingDashboardRepository {
  Future<http.Response> getDashboardCount(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kD2DBaseURL}${APIConstants.kGetDailyCallingReportForCallerLogin}',
      ),
      body: payload,
      headers: header,
    );
  }

  Future<http.Response> getTeamData(Map<String, dynamic> payload) async {
    var header = {'Content-Type': 'application/x-www-form-urlencoded'};
    return await http.post(
      Uri.parse(
        '${APIManager.kCallingBaseURL}${APIConstants.kGetTeamDataByUserId}',
      ),
      body: payload,
      headers: header,
    );
  }
}
