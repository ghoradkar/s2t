// ignore_for_file: file_names

import 'dart:async';

import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import '../model/BindDistrictResponse.dart';
import '../model/BindDivisionResponse.dart';
import '../model/CampCountWithDayResponse.dart';
import '../model/CampTypeAndCatagoryResponse.dart';
import '../model/MonthlySurveySiteResponse.dart';
import '../model/SubOrganizationResponse.dart';
import '../model/HomeAndHubProcessingModel.dart';

class CampCalendarRepository {
  final APIManager _apiManager = APIManager();

  Future<({SubOrganizationResponse? data, String error, bool success})>
  getSubOrganization(Map<String, String> params) {
    final completer =
        Completer<({SubOrganizationResponse? data, String error, bool success})>();
    _apiManager.getSubOrganizationAPI(params, (response, error, success) {
      completer.complete((
        data: response as SubOrganizationResponse?,
        error: error as String,
        success: success as bool,
      ));
    });
    return completer.future;
  }

  Future<({BindDivisionResponse? data, String error, bool success})>
  getBindDivision(Map<String, String> params) {
    final completer =
        Completer<({BindDivisionResponse? data, String error, bool success})>();
    _apiManager.getBindDivision(params, (response, error, success) {
      completer.complete((
        data: response as BindDivisionResponse?,
        error: error as String,
        success: success as bool,
      ));
    });
    return completer.future;
  }

  Future<({BindDistrictResponse? data, String error, bool success})>
  getBindDistrict(Map<String, String> params) {
    final completer =
        Completer<({BindDistrictResponse? data, String error, bool success})>();
    _apiManager.getBindDistrictAPI(params, (response, error, success) {
      completer.complete((
        data: response as BindDistrictResponse?,
        error: error as String,
        success: success as bool,
      ));
    });
    return completer.future;
  }

  Future<({CampTypeAndCatagoryResponse? data, String error, bool success})>
  getCampTypeAndCatagory() {
    final completer = Completer<
        ({CampTypeAndCatagoryResponse? data, String error, bool success})>();
    _apiManager.getCampTypeAndCatagoryAPI((response, error, success) {
      completer.complete((
        data: response as CampTypeAndCatagoryResponse?,
        error: error as String,
        success: success as bool,
      ));
    });
    return completer.future;
  }

  Future<({CampCountWithDayResponse? data, String error, bool success})>
  getCampCountWithDay(Map<String, String> params) {
    final completer = Completer<
        ({CampCountWithDayResponse? data, String error, bool success})>();
    _apiManager.getCampCountWithDayAndMonthWiseWithCampTypeOrgAPI(
      params,
      (response, error, success) {
        completer.complete((
          data: response as CampCountWithDayResponse?,
          error: error as String,
          success: success as bool,
        ));
      },
    );
    return completer.future;
  }

  Future<({HomeAndHubProcessingModel? data, String error, bool success})>
  getHomeAndHubProcessedCount(Map<String, String> params) {
    final completer = Completer<
        ({HomeAndHubProcessingModel? data, String error, bool success})>();
    _apiManager.getHomeAndHubProcessedCount(params, (response, error, success) {
      completer.complete((
        data: response as HomeAndHubProcessingModel?,
        error: error as String,
        success: success as bool,
      ));
    });
    return completer.future;
  }
}

class CampCalendarCampListRepository {
  final APIManager _apiManager = APIManager();

  Future<({MonthlySurveySiteResponse? data, String error, bool success})>
  getUserAttendanceDays(String urlString, Map<String, String> params) {
    final completer = Completer<
        ({MonthlySurveySiteResponse? data, String error, bool success})>();
    _apiManager.getUserAttendanceDaysAPI(
      urlString,
      params,
      (response, error, success) {
        completer.complete((
          data: response as MonthlySurveySiteResponse?,
          error: error as String,
          success: success as bool,
        ));
      },
    );
    return completer.future;
  }
}