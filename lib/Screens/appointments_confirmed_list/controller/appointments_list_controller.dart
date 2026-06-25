// ignore_for_file: file_names, avoid_print

import 'package:get/get.dart';
import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/appoinment_expected_beneficiaries_response.dart';
import '../model/team_cc_response.dart';
import '../repository/appointments_confirmed_repository.dart';

class AppointmentsListController extends GetxController {
  final _repo = AppointmentsConfirmedRepository();

  DashboardMenu? dashboardType;
  int empCode = 0;
  int dESGID = 0;
  bool showTeamDropDown = true;

  String selectedCampDate = '';
  AppointmentStatusOutput? selectedAppointmentStatus;
  TeamCCOutput? selectedTeam;

  final TextEditingController searchController = TextEditingController();
  List<AppoinmentExpectedBeneficiariesOutput> allBeneficiaries = [];
  List<AppoinmentExpectedBeneficiariesOutput> filteredBeneficiaries = [];

  @override
  void onInit() {
    super.onInit();
    dashboardType = Get.arguments as DashboardMenu?;
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedAppointmentStatus = AppointmentStatusOutput(
      assignStatusID: 2,
      appointmentStatus: 'Confirmed',
    );
    selectedCampDate = FormatterManager.formatDateForAppointmentAPI(DateTime.now());
    loadList();
  }

  @override
  void onClose() {
    searchController.dispose();
    super.onClose();
  }

  Future<void> loadList() async {
    ToastManager.showLoader();
    try {
      if (DataProvider().getRegularCamp()) {
        showTeamDropDown = false;
        await _loadRegularList();
      } else {
        showTeamDropDown = true;
        if (dashboardType == DashboardMenu.PatientRegistration) {
          await _loadRegularList();
        } else {
          await _loadMASList();
        }
      }
    } catch (e) {
      ToastManager.toast(e.toString());
      allBeneficiaries = [];
      filteredBeneficiaries = [];
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> _loadRegularList() async {
    final params = {
      'UserID': empCode.toString(),
      'AppoinmentDate': selectedCampDate,
      'AssignStatusID': selectedAppointmentStatus?.assignStatusID?.toString() ?? '',
    };
    final res = await _repo.fetchBeneficiaryList(params);
    allBeneficiaries = res.output ?? [];
    filteredBeneficiaries = allBeneficiaries;
  }

  Future<void> _loadMASList() async {
    final params = {
      'UserID': empCode.toString(),
      'AppoinmentDate': selectedCampDate,
      'AssignStatusID': selectedAppointmentStatus?.assignStatusID?.toString() ?? '',
      'TeamId': selectedTeam?.teamid?.toString() ?? '0',
    };
    final res = await _repo.fetchBeneficiaryListMAS(params);
    allBeneficiaries = res.output ?? [];
    filteredBeneficiaries = allBeneficiaries;
  }

  void applyFilter(
    AppointmentStatusOutput status,
    String date,
    TeamCCOutput? team,
  ) {
    selectedAppointmentStatus = status;
    selectedCampDate = date;
    selectedTeam = team;
    searchController.clear();
    loadList();
  }

  void search(String query) {
    final lower = query.toLowerCase();
    if (lower.isEmpty) {
      filteredBeneficiaries = allBeneficiaries;
    } else {
      filteredBeneficiaries = allBeneficiaries.where((item) {
        final name = item.beneficiaryName?.toLowerCase() ?? '';
        final benNo = item.beneficiaryNo?.toLowerCase() ?? '';
        final mobile = item.mobile?.toLowerCase() ?? '';
        final area = item.area?.toLowerCase() ?? '';
        final pin = item.pincode?.toLowerCase() ?? '';
        return name.contains(lower) ||
            benNo.contains(lower) ||
            mobile.contains(lower) ||
            area.contains(lower) ||
            pin.contains(lower);
      }).toList();
    }
    update();
  }

  Future<List<TeamCCOutput>> fetchTeamList() async {
    try {
      ToastManager.showLoader();
      final res = await _repo.fetchTeamList({'UserID': empCode.toString()});
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }
}
