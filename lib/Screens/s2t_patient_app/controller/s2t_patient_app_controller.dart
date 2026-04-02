// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/check_connectivity.dart';
import 'package:s2toperational/Screens/s2t_patient_app/model/S2TAndroidIosCountModel.dart';
import 'package:s2toperational/Screens/s2t_patient_app/model/S2TAndroidIosCountDistrictWiseModel.dart';
import '../repository/s2t_patient_app_repository.dart';

class S2TPatientAppController extends GetxController {
  final S2TPatientAppRepository _repository = S2TPatientAppRepository();

  // ─── State ────────────────────────────────────────────────────────────────────

  bool hasInternet = true;
  bool isS2tAppLoading = false;
  bool isS2tAppDistrictLoading = false;

  S2TAndroidIosCountModel? s2tAndroidIosCountModel;
  S2TAndroidIosCountDistrictWiseModel? s2tAndroidIosCountDistrictWiseModel;

  // ─── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    checkInternetAndLoad();
  }

  // ─── Main screen load ─────────────────────────────────────────────────────────

  Future<void> checkInternetAndLoad() async {
    isS2tAppLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      if (hasInternet) {
        await _getAndroidIosCount();
      }
    } finally {
      isS2tAppLoading = false;
      update();
    }
  }

  Future<void> _getAndroidIosCount() async {
    try {
      final response = await _repository.getAndroidIosCount();
      if (response.statusCode == 200) {
        final data = jsonDecode(await response.stream.bytesToString());
        s2tAndroidIosCountModel = S2TAndroidIosCountModel.fromJson(data);
      } else {
        ToastManager.toast('Failed to load patient app count');
      }
    } catch (e) {
      debugPrint('getAndroidIosCount error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }

  // ─── District-wise screen load ────────────────────────────────────────────────

  Future<void> checkInternetAndLoadDistrictWise() async {
    isS2tAppDistrictLoading = true;
    update();
    try {
      hasInternet = await CheckConnectivity.checkInternetAndLoadData();
      if (hasInternet) {
        await _getAndroidIosDistrictWiseList();
      }
    } finally {
      isS2tAppDistrictLoading = false;
      update();
    }
  }

  Future<void> refreshDistrictWise() async {
    isS2tAppDistrictLoading = true;
    update();
    await _getAndroidIosDistrictWiseList();
    isS2tAppDistrictLoading = false;
    update();
  }

  Future<void> _getAndroidIosDistrictWiseList() async {
    try {
      final response = await _repository.getAndroidIosDistrictWiseList();
      if (response.statusCode == 200) {
        final data = jsonDecode(await response.stream.bytesToString());
        s2tAndroidIosCountDistrictWiseModel =
            S2TAndroidIosCountDistrictWiseModel.fromJson(data);
      } else {
        ToastManager.toast('Failed to load district-wise count');
      }
    } catch (e) {
      debugPrint('getAndroidIosDistrictWiseList error: $e');
      ToastManager.toast('Something went wrong');
    }
    update();
  }
}