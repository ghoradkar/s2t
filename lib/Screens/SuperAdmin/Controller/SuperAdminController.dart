// ignore_for_file: file_names

import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/AdminDashboard/Repository.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/check_connectivity.dart';
import 'package:s2toperational/Screens/SuperAdmin/Model/ConductedCardSuperAdmin.dart';
import 'package:s2toperational/Screens/SuperAdmin/Model/TodayAndTotalTableCount.dart';
import 'package:s2toperational/Screens/SuperAdmin/Model/TodaysPatientTableModel.dart';

class SuperAdminController extends GetxController {
  LoginResponseModel? loginResponseModel;
  String? status;
  bool hasInternet = true;
  bool isSuperAdminLoading = false;

  ConductedCardSuperAdmin? conductedCardSuperAdmin;
  TodaysPatientTableModel? todaysPatientCardSuperAdmin;
  TodaysAndTotalTableCount? todaysTableCount;
  TodaysAndTotalTableCount? totalTableCount;

  /// Startup
  // checkInternetSuperAdmin() async {
  //   hasInternet = await CheckConnectivity.checkInternetAndLoadData();
  //   loginResponseModel = DataProvider().getParsedUserData();
  //
  //   debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
  //   update();
  //
  //   if (hasInternet) {
  //     DateTime now = DateTime.now();
  //     String formattedDate1 = DateFormat('yyyy/MM/dd').format(now);
  //     String formattedDate = DateFormat('dd-MMM-yyyy').format(now);
  //
  //     await getTodaysPatientTableData(formattedDate1);
  //     await getConductedCardTableData();
  //     await getTodaysData(formattedDate, formattedDate);
  //     await getTotalData("", "");
  //   }
  // }

  Future<void> checkInternetSuperAdmin({bool showLoader = true}) async {
    hasInternet = await CheckConnectivity.checkInternetAndLoadData();
    loginResponseModel = DataProvider().getParsedUserData();

    debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");
    update();

    if (hasInternet) {
      isSuperAdminLoading = true;
      update();
      DateTime now = DateTime.now();
      String formattedDate1 = DateFormat('yyyy/MM/dd').format(now);
      String formattedDate = DateFormat('dd-MMM-yyyy').format(now);

      if (showLoader) {
        // Show loader once before all API calls
        ToastManager.showLoader();
      }

      try {
        // Call all APIs in parallel using Future.wait
        await Future.wait([
          getTodaysPatientTableData(formattedDate1),
          getConductedCardTableData(),
          getTodaysData(formattedDate, formattedDate),
          getTotalData("", ""),
        ]);
      } catch (e) {
        debugPrint('Error loading data: $e');
      } finally {
        if (showLoader) {
          // Hide loader once after all APIs complete
          ToastManager.hideLoader();
        }
        isSuperAdminLoading = false;
        update();
      }
    }
  }

  Future<void> getConductedCardTableData() async {
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetLandingPageCountsDisplayforFinancialYearV1WithSubOrg}";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == 'Success') {
          conductedCardSuperAdmin = ConductedCardSuperAdmin.fromJson(body);
          status = conductedCardSuperAdmin?.message;
        } else {
          status = body['message'] ?? "Failed";
          ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getConductedCardTableData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getConductedCardTableData timeout: $e');
      // ToastManager.toast(
      //   'Request timeout. Please check your internet connection.',
      // );
    } on SocketException catch (e) {
      debugPrint('getConductedCardTableData network error: $e');
      ToastManager.toast(
        'Network error. Please check your internet connection.',
      );
    } on FormatException catch (e) {
      debugPrint('getConductedCardTableData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getConductedCardTableData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
  }

  Future<void> getTodaysPatientTableData(String date) async {
    try {
      final uri =
          "${APIManager.kD2DBaseURL}${APIConstants.kGetTodaysPatientCountV1}?Date=$date";
      debugPrint(uri);

      final response = await Repository.getResponse(uri);
      debugPrint("response.body : ${response.body}");

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == 'Success') {
          todaysPatientCardSuperAdmin = TodaysPatientTableModel.fromJson(body);
          status = todaysPatientCardSuperAdmin?.message;
        } else {
          status = body['message'] ?? "Failed";
          ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getTodaysPatientTableData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTodaysPatientTableData timeout: $e');
      // ToastManager.toast(
      //   'Request timeout. Please check your internet connection.',
      // );
    } on SocketException catch (e) {
      debugPrint('getTodaysPatientTableData network error: $e');
      ToastManager.toast(
        'Network error. Please check your internet connection.',
      );
    } on FormatException catch (e) {
      debugPrint('getTodaysPatientTableData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTodaysPatientTableData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
  }

  Future<void> getTodaysData(String fromDate, String toDate) async {
    try {
      final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";

      debugPrint(uri);
      Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};

      final response = await Repository.postResponse(uri, body, {
        'Content-Type': 'application/x-www-form-urlencoded',
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == '1') {
          todaysTableCount = TodaysAndTotalTableCount.fromJson(body);
          status = todaysPatientCardSuperAdmin?.message;
        } else {
          status = body['message'] ?? "Failed";
          ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getTodaysData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTodaysData timeout: $e');
      // ToastManager.toast(
      //   'Request timeout. Please check your internet connection.',
      // );
    } on SocketException catch (e) {
      debugPrint('getTodaysData network error: $e');
      ToastManager.toast(
        'Network error. Please check your internet connection.',
      );
    } on FormatException catch (e) {
      debugPrint('getTodaysData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTodaysData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
  }

  Future<void> getTotalData(String fromDate, String toDate) async {
    try {
      final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";

      debugPrint(uri);
      Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};

      final response = await Repository.postResponse(uri, body, {
        'Content-Type': 'application/x-www-form-urlencoded',
      });

      if (response.statusCode == 200) {
        final Map<String, dynamic> body = jsonDecode(response.body);

        if (body['status'] == '1') {
          totalTableCount = TodaysAndTotalTableCount.fromJson(body);
          status = todaysPatientCardSuperAdmin?.message;
        } else {
          status = body['message'] ?? "Failed";
          ToastManager.toast(status!);
        }
      } else {
        ToastManager.toast('Failed getting getTodaysData');
      }
    } on TimeoutException catch (e) {
      debugPrint('getTotalData timeout: $e');
      // ToastManager.toast(
      //   'Request timeout. Please check your internet connection.',
      // );
    } on SocketException catch (e) {
      debugPrint('getTotalData network error: $e');
      ToastManager.toast(
        'Network error. Please check your internet connection.',
      );
    } on FormatException catch (e) {
      debugPrint('getTotalData format error: $e');
      ToastManager.toast('Invalid response format.');
    } catch (e, st) {
      debugPrint('getTotalData error: $e\n$st');
      ToastManager.toast('Something went wrong');
    }
  }

  // Future<void> getConductedCardTableData() async {
  //   ToastManager.showLoader();
  //   try {
  //     final uri =
  //         "${APIManager.kD2DBaseURL}${APIConstants.kGetLandingPageCountsDisplayforFinancialYearV1WithSubOrg}";
  //     debugPrint(uri);
  //
  //     final response = await repository.getResponse(uri);
  //     debugPrint("response.body : ${response.body}");
  //
  //     if (response.statusCode == 200) {
  //       final Map<String, dynamic> body = jsonDecode(response.body);
  //
  //       if (body['status'] == 'Success') {
  //         conductedCardSuperAdmin = ConductedCardSuperAdmin.fromJson(body);
  //         status = conductedCardSuperAdmin?.message;
  //       } else {
  //         status = body['message'] ?? "Failed";
  //         ToastManager.toast(status!);
  //       }
  //     } else {
  //       ToastManager.toast('Failed getting getConductedCardTableData');
  //     }
  //   } catch (e, st) {
  //     debugPrint('getConductedCardTableData error: $e\n$st');
  //     ToastManager.toast('Something went wrong');
  //   } finally {
  //     ToastManager.hideLoader();
  //     update();
  //   }
  // }
  //
  // Future<void> getTodaysPatientTableData(String date) async {
  //   ToastManager.showLoader();
  //   try {
  //     // "${APIManager.kD2DBaseURL}${APIConstants.GetTodaysPatientCount_V1}?Date=$date";
  //
  //     final uri =
  //         "${APIManager.kD2DBaseURL}${APIConstants.kGetTodaysPatientCountV1}?Date=$date";
  //     debugPrint(uri);
  //
  //     final response = await repository.getResponse(uri);
  //     debugPrint("response.body : ${response.body}");
  //
  //     if (response.statusCode == 200) {
  //       final Map<String, dynamic> body = jsonDecode(response.body);
  //
  //       if (body['status'] == 'Success') {
  //         todaysPatientCardSuperAdmin = TodaysPatientTableModel.fromJson(body);
  //         status = todaysPatientCardSuperAdmin?.message;
  //       } else {
  //         status = body['message'] ?? "Failed";
  //         ToastManager.toast(status!);
  //       }
  //     } else {
  //       ToastManager.toast('Failed getting getTodaysPatientTableData');
  //     }
  //   } catch (e, st) {
  //     debugPrint('getTodaysPatientTableData error: $e\n$st');
  //     ToastManager.toast('Something went wrong');
  //   } finally {
  //     ToastManager.hideLoader();
  //     update();
  //   }
  // }
  //
  //  Future<void> getTodaysData(String fromDate, String toDate) async {
  //   ToastManager.showLoader();
  //   final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";
  //
  //   debugPrint(uri);
  //   Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};
  //
  //   final response = await repository.postResponse(uri, body, {
  //     'Content-Type': 'application/x-www-form-urlencoded',
  //   });
  //
  //   if (response.statusCode == 200) {
  //     ToastManager.hideLoader();
  //
  //     final Map<String, dynamic> body = jsonDecode(response.body);
  //
  //     if (body['status'] == '1') {
  //       todaysTableCount = TodaysAndTotalTableCount.fromJson(body);
  //       status = todaysPatientCardSuperAdmin?.message;
  //     } else {
  //       status = body['message'] ?? "Failed";
  //       ToastManager.toast(status!);
  //     }
  //   } else {
  //     ToastManager.hideLoader();
  //
  //     ToastManager.toast('Failed getting getTodaysData');
  //   }
  //   update();
  // }
  //
  //  Future<void> getTotalData(String fromDate, String toDate) async {
  //   ToastManager.showLoader();
  //   final uri = "${APIManager.kTreatmentCount}${APIConstants.count}";
  //
  //   debugPrint(uri);
  //   Map<String, String> body = {"fromDate": fromDate, "toDate": toDate};
  //
  //   final response = await repository.postResponse(uri, body, {
  //     'Content-Type': 'application/x-www-form-urlencoded',
  //   });
  //
  //   if (response.statusCode == 200) {
  //     ToastManager.hideLoader();
  //
  //     final Map<String, dynamic> body = jsonDecode(response.body);
  //
  //     if (body['status'] == '1') {
  //       totalTableCount = TodaysAndTotalTableCount.fromJson(body);
  //       status = todaysPatientCardSuperAdmin?.message;
  //     } else {
  //       status = body['message'] ?? "Failed";
  //       ToastManager.toast(status!);
  //     }
  //   } else {
  //     ToastManager.hideLoader();
  //
  //     ToastManager.toast('Failed getting getTodaysData');
  //   }
  //   update();
  // }
}
