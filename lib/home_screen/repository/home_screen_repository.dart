import 'dart:async';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/admin_dashboard/model/conducted_camps_totals.dart';
import 'package:s2toperational/admin_dashboard/model/todays_patients_response.dart';

class HomeScreenRepository {
  final _api = APIManager();

  Future<ConductedCampsResponse?> fetchConductedCamps() {
    final c = Completer<ConductedCampsResponse?>();
    _api.getConductedCamp((ConductedCampsResponse? res, String err, bool ok) {
      c.complete(ok && res != null ? res : null);
    });
    return c.future;
  }

  Future<TodaysPatientsResponse?> fetchTodaysPatients(String date) {
    final c = Completer<TodaysPatientsResponse?>();
    _api.getTodaysPatent((TodaysPatientsResponse? res, String err, bool ok) {
      c.complete(ok && res != null ? res : null);
    }, date);
    return c.future;
  }
}
