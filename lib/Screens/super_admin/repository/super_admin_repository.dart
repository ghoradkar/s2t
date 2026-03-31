// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/constants/Repository.dart';
import 'package:s2toperational/Screens/super_admin/model/conducted_card_super_admin.dart';
import 'package:s2toperational/Screens/super_admin/model/today_and_total_table_count.dart';
import 'package:s2toperational/Screens/super_admin/model/todays_patient_table_model.dart';

class SuperAdminRepository {
  Future<ConductedCardSuperAdmin?> getConductedCardTableData() async {
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetLandingPageCountsDisplayforFinancialYearV1WithSubOrg}";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == 'Success') {
          return ConductedCardSuperAdmin.fromJson(body);
        } else {
          final msg = body['message'] ?? "Failed";
          ToastManager.toast(msg);
        }
      } else {
        ToastManager.toast('Failed getting getConductedCardTableData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getConductedCardTableData timeout: $e');
    } on SocketException catch (e) {
      debugPrint('getConductedCardTableData network error: $e');
      ToastManager.toast('Network error. Please check your internet connection.');
    } on FormatException catch (e) {
      debugPrint('getConductedCardTableData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getConductedCardTableData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
    return null;
  }

  Future<TodaysPatientTableModel?> getTodaysPatientTableData(String date) async {
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetTodaysPatientCountV1}?Date=$date";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == 'Success') {
          return TodaysPatientTableModel.fromJson(body);
        } else {
          final msg = body['message'] ?? "Failed";
          ToastManager.toast(msg);
        }
      } else {
        ToastManager.toast('Failed getting getTodaysPatientTableData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTodaysPatientTableData timeout: $e');
    } on SocketException catch (e) {
      debugPrint('getTodaysPatientTableData network error: $e');
      ToastManager.toast('Network error. Please check your internet connection.');
    } on FormatException catch (e) {
      debugPrint('getTodaysPatientTableData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTodaysPatientTableData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
    return null;
  }

  Future<TodaysAndTotalTableCount?> getTodaysData(
      String fromDate, String toDate) async {
    try {
      final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";
      debugPrint(uri);

      final Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};
      final response = await Repository.postResponse(uri, body, {
        'Content-Type': 'application/x-www-form-urlencoded',
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseBody = jsonDecode(response.body);

        if (responseBody['status'] == '1') {
          return TodaysAndTotalTableCount.fromJson(responseBody);
        } else {
          final msg = responseBody['message'] ?? "Failed";
          ToastManager.toast(msg);
        }
      } else {
        ToastManager.toast('Failed getting getTodaysData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTodaysData timeout: $e');
    } on SocketException catch (e) {
      debugPrint('getTodaysData network error: $e');
      ToastManager.toast('Network error. Please check your internet connection.');
    } on FormatException catch (e) {
      debugPrint('getTodaysData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTodaysData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
    return null;
  }

  Future<TodaysAndTotalTableCount?> getTotalData(
      String fromDate, String toDate) async {
    try {
      final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";
      debugPrint(uri);

      final Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};
      final response = await Repository.postResponse(uri, body, {
        'Content-Type': 'application/x-www-form-urlencoded',
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseBody = jsonDecode(response.body);

        if (responseBody['status'] == '1') {
          return TodaysAndTotalTableCount.fromJson(responseBody);
        } else {
          final msg = responseBody['message'] ?? "Failed";
          ToastManager.toast(msg);
        }
      } else {
        ToastManager.toast('Failed getting getTotalData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTotalData timeout: $e');
    } on SocketException catch (e) {
      debugPrint('getTotalData network error: $e');
      ToastManager.toast('Network error. Please check your internet connection.');
    } on FormatException catch (e) {
      debugPrint('getTotalData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTotalData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
    return null;
  }
}