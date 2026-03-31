// ignore_for_file: file_names

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/check_connectivity.dart';
import 'package:s2toperational/Screens/super_admin/model/conducted_card_super_admin.dart';
import 'package:s2toperational/Screens/super_admin/model/today_and_total_table_count.dart';
import 'package:s2toperational/Screens/super_admin/model/todays_patient_table_model.dart';
import 'package:s2toperational/Screens/super_admin/repository/super_admin_repository.dart';

class SuperAdminController extends GetxController {
  final _repo = SuperAdminRepository();

  LoginResponseModel? loginResponseModel;
  bool hasInternet = true;

  final RxBool isLoading = false.obs;
  final Rxn<ConductedCardSuperAdmin> conductedCardSuperAdmin = Rxn();
  final Rxn<TodaysPatientTableModel> todaysPatientCardSuperAdmin = Rxn();
  final Rxn<TodaysAndTotalTableCount> todaysTableCount = Rxn();
  final Rxn<TodaysAndTotalTableCount> totalTableCount = Rxn();

  Future<void> checkInternetSuperAdmin({bool showLoader = true}) async {
    hasInternet = await CheckConnectivity.checkInternetAndLoadData();
    loginResponseModel = DataProvider().getParsedUserData();

    debugPrint("savedUserData ${jsonEncode(loginResponseModel)}");

    if (!hasInternet) return;

    isLoading.value = true;

    DateTime now = DateTime.now();
    final formattedDate1 = DateFormat('yyyy/MM/dd').format(now);
    final formattedDate = DateFormat('dd-MMM-yyyy').format(now);

    if (showLoader) ToastManager.showLoader();

    try {
      final results = await Future.wait([
        _repo.getTodaysPatientTableData(formattedDate1),
        _repo.getConductedCardTableData(),
        _repo.getTodaysData(formattedDate, formattedDate),
        _repo.getTotalData("", ""),
      ]);

      todaysPatientCardSuperAdmin.value =
          results[0] as TodaysPatientTableModel?;
      conductedCardSuperAdmin.value =
          results[1] as ConductedCardSuperAdmin?;
      todaysTableCount.value =
          results[2] as TodaysAndTotalTableCount?;
      totalTableCount.value =
          results[3] as TodaysAndTotalTableCount?;
    } catch (e) {
      debugPrint('checkInternetSuperAdmin error: $e');
    } finally {
      if (showLoader) ToastManager.hideLoader();
      isLoading.value = false;
    }
  }
}
