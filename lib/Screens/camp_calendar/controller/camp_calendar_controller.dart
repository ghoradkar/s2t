// ignore_for_file: avoid_print, file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import '../model/BindDistrictResponse.dart';
import '../model/BindDivisionResponse.dart';
import '../model/CampCountWithDayResponse.dart';
import '../model/CampTypeAndCatagoryResponse.dart';
import '../model/SubOrganizationResponse.dart';
import '../model/HomeAndHubProcessingModel.dart';
import '../repository/camp_calendar_repository.dart';

class CampCalendarController extends GetxController {
  final CampCalendarRepository _repository = CampCalendarRepository();

  int dESGID = 0;
  int empCode = 0;
  bool isShowSubOrg = false;

  int _requestId = 0;
  int year = 0;
  int month = 0;
  String statusDashboardTitle = "";

  Map<DateTime, Map<String, dynamic>> attendanceMap = {};

  SubOrganizationOutput? selectedSubOrganization;
  BindDivisionOutput? selectedDivision;
  BindDistrictOutput? selectedDistrict;
  CampTypeAndCatagoryOutput? selectedCampType;

  HomeAndHubProcessingModel? homeAndHubProcessingModel;

  final TextEditingController subOrgController = TextEditingController();
  final TextEditingController divisionController = TextEditingController();
  final TextEditingController districtController = TextEditingController();
  final TextEditingController campTypeController = TextEditingController();

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    statusDashboardTitle = "Total Camp Status Dashboard";

    final dateTime = DateTime.now();
    year = dateTime.year;
    month = dateTime.month;

    selectedDivision = BindDivisionOutput(
      dIVID: 0,
      dIVNAME: "ALL",
      subOrgId: 0,
      subOrgName: "ALL",
    );
    divisionController.text = "ALL";

    selectedDistrict = BindDistrictOutput(
      dIVID: 0,
      dIVNAME: "ALL",
      subOrgId: 0,
      subOrgName: "ALL",
      dISTLGDCODE: 0,
      dISTNAME: "ALL",
    );
    districtController.text = "ALL";

    selectedCampType = CampTypeAndCatagoryOutput(
      cAMPTYPE: 0,
      campTypeDescription: "All Camp",
      catagoryID: 0,
      catagoryType: "All Camp",
    );
    campTypeController.text = "All Camp";

    getSubOrganization();
  }

  @override
  void onClose() {
    subOrgController.dispose();
    divisionController.dispose();
    districtController.dispose();
    campTypeController.dispose();
    super.onClose();
  }

  // ─── Sub Organization ────────────────────────────────────────────────────────

  Future<void> getSubOrganization() async {
    if (dESGID == 71) return;

    ToastManager.showLoader();
    final params = {
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);

    final result = await _repository.getSubOrganization(params);

    if (result.success) {
      if (isShowSubOrg) {
        ToastManager.hideLoader();
        _showDropDownBottomSheet(
          "Sub Organization",
          result.data?.output ?? [],
          DropDownTypeMenu.SubOrganization,
        );
      } else {
        final List<SubOrganizationOutput> output = result.data?.output ?? [];
        if (output.isNotEmpty) {
          ToastManager.hideLoader();
          selectedSubOrganization = output.first;
          subOrgController.text = output.first.subOrgName ?? "";
          print(
            "🏢 SubOrg selected: id=${output.first.subOrgId}, name=${output.first.subOrgName}",
          );
        }
        await groupAPICall();
      }
    } else {
      ToastManager.toast(result.error);
    }
    update();
  }

  // ─── Division ────────────────────────────────────────────────────────────────

  void getBindDivision() async {
    ToastManager.showLoader();
    final params = {
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print(params);

    final result = await _repository.getBindDivision(params);
    ToastManager.hideLoader();

    if (result.success) {
      _showDropDownBottomSheet(
        "Division",
        result.data?.output ?? [],
        DropDownTypeMenu.BindDivision,
      );
    } else {
      ToastManager.toast(result.error);
    }
    update();
  }

  // ─── District ────────────────────────────────────────────────────────────────

  void getBindDistrict() async {
    ToastManager.showLoader();
    final params = {
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "0",
      "UserID": empCode.toString(),
      "DESGID": dESGID.toString(),
      "DIVID": selectedDivision?.dIVID.toString() ?? "0",
      "DISTLGDCODE": "0",
    };
    print(params);

    final result = await _repository.getBindDistrict(params);
    ToastManager.hideLoader();

    if (result.success) {
      _showDropDownBottomSheet(
        "District",
        result.data?.output ?? [],
        DropDownTypeMenu.BindDistrict,
      );
    } else {
      ToastManager.toast(result.error);
    }
    update();
  }

  // ─── Camp Type ───────────────────────────────────────────────────────────────

  void getCampTypeAndCatagory() async {
    ToastManager.showLoader();

    final result = await _repository.getCampTypeAndCatagory();
    ToastManager.hideLoader();

    if (result.success) {
      final List<CampTypeAndCatagoryOutput> campTypes =
          List<CampTypeAndCatagoryOutput>.from(result.data?.output ?? []);
      final bool hasAllCamp = campTypes.any(
        (e) =>
            (e.cAMPTYPE ?? -1) == 0 ||
            (e.campTypeDescription ?? "").toLowerCase() == "all camp",
      );
      if (!hasAllCamp) {
        campTypes.insert(
          0,
          CampTypeAndCatagoryOutput(
            cAMPTYPE: 0,
            campTypeDescription: "All Camp",
            catagoryID: 0,
            catagoryType: "All Camp",
          ),
        );
      }
      _showDropDownBottomSheet(
        "Camp Type",
        campTypes,
        DropDownTypeMenu.CampTypeAndCatagory,
      );
    } else {
      ToastManager.toast(result.error);
    }
    update();
  }

  // ─── Dropdown selection handler ──────────────────────────────────────────────

  void applyDropdownSelection(DropDownTypeMenu dropDownType, dynamic value) {
    if (dropDownType == DropDownTypeMenu.SubOrganization) {
      selectedSubOrganization = value;
      subOrgController.text = value?.subOrgName ?? "";
      selectedDivision = BindDivisionOutput(
        dIVID: 0,
        dIVNAME: "ALL",
        subOrgId: 0,
        subOrgName: "ALL",
      );
      divisionController.text = "ALL";
      selectedDistrict = BindDistrictOutput(
        dIVID: 0,
        dIVNAME: "ALL",
        subOrgId: 0,
        subOrgName: "ALL",
        dISTLGDCODE: 0,
        dISTNAME: "ALL",
      );
      districtController.text = "ALL";
      selectedCampType = CampTypeAndCatagoryOutput(
        cAMPTYPE: 0,
        campTypeDescription: "All Camp",
        catagoryID: 0,
        catagoryType: "All Camp",
      );
      campTypeController.text = "All Camp";
    } else if (dropDownType == DropDownTypeMenu.BindDivision) {
      selectedDivision = value;
      divisionController.text = value?.dIVNAME ?? "";
      selectedDistrict = BindDistrictOutput(
        dIVID: 0,
        dIVNAME: "ALL",
        subOrgId: 0,
        subOrgName: "ALL",
        dISTLGDCODE: 0,
        dISTNAME: "ALL",
      );
      districtController.text = "ALL";
      selectedCampType = CampTypeAndCatagoryOutput(
        cAMPTYPE: 0,
        campTypeDescription: "All Camp",
        catagoryID: 0,
        catagoryType: "All Camp",
      );
      campTypeController.text = "All Camp";
    } else if (dropDownType == DropDownTypeMenu.BindDistrict) {
      selectedDistrict = value;
      districtController.text = value?.dISTNAME ?? "";
      selectedCampType = CampTypeAndCatagoryOutput(
        cAMPTYPE: 0,
        campTypeDescription: "All Camp",
        catagoryID: 0,
        catagoryType: "All Camp",
      );
      campTypeController.text = "All Camp";
    } else if (dropDownType == DropDownTypeMenu.CampTypeAndCatagory) {
      selectedCampType = value;
      campTypeController.text = value?.campTypeDescription ?? "";
    }
    groupAPICall();
    update();
  }

  // ─── Group API call ───────────────────────────────────────────────────────────

  Future<void> groupAPICall() async {
    ToastManager.showLoader();
    final int requestId = ++_requestId;

    await Future.wait([
      _fetchCampCountWithDay(requestId),
      _fetchHomeAndHubProcessedCount(requestId),
    ]);
    ToastManager.hideLoader();
  }

  Future<void> _fetchCampCountWithDay(int requestId) async {
    final params = {
      "MonthId": month.toString().padLeft(2, '0'),
      "Year": year.toString(),
      "Distcode": selectedDistrict?.dISTLGDCODE.toString() ?? "",
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
      "DIVID": selectedDivision?.dIVID.toString() ?? "",
      "UserId": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print("🗓️ CALENDAR API params: $params");

    final result = await _repository.getCampCountWithDay(params);
    if (requestId != _requestId) return;

    attendanceMap = {};
    if (result.success) {
      if (selectedCampType?.cAMPTYPE == 0) {
        statusDashboardTitle = "Total Camp Status Dashboard";
      } else if (selectedCampType?.cAMPTYPE == 1) {
        statusDashboardTitle = "Normal Camp Status Dashboard";
      } else {
        statusDashboardTitle = "D2D Camp Status Dashboard";
      }

      final List<CampCountWithDayOutput> tempList = result.data?.output ?? [];
      for (final CampCountWithDayOutput attendance in tempList) {
        final strDate = attendance.campDate ?? '';
        final now = DateTime.now();
        Color color = Colors.white;
        try {
          final date = FormatterManager.formatStringToDate(strDate);
          if (date.isBefore(DateTime(now.year, now.month, now.day))) {
            color = const Color(0xFF229954).withValues(alpha: 0.4);
          } else if (date.isAfter(DateTime(now.year, now.month, now.day))) {
            color = const Color(0xFF4CB1FB);
          } else {
            color = const Color(0xFF229954).withValues(alpha: 0.4);
          }
        } catch (e) {
          print('Invalid date: $e');
        }
        attendanceMap[DateTime(
          attendance.year ?? 0,
          attendance.month ?? 0,
          attendance.day ?? 0,
        )] = {"color": color, "value": attendance.campCount ?? 0};
      }
    } else {
      ToastManager.toast(result.error);
    }
    update();
  }

  Future<void> _fetchHomeAndHubProcessedCount(int requestId) async {
    final params = {
      "MonthId": month.toString().padLeft(2, '0'),
      "Year": year.toString(),
      "Distcode": selectedDistrict?.dISTLGDCODE.toString() ?? "",
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "",
      "SubOrgId": selectedSubOrganization?.subOrgId.toString() ?? "",
      "DIVID": selectedDivision?.dIVID.toString() ?? "",
      "UserId": empCode.toString(),
      "DESGID": dESGID.toString(),
    };
    print("📊 TABLE API params: $params");

    final result = await _repository.getHomeAndHubProcessedCount(params);
    if (requestId != _requestId) return;

    if (result.success) {
      homeAndHubProcessingModel = result.data;
    } else {
      if (result.error.isNotEmpty) {
        homeAndHubProcessingModel = null;
        update();
        ToastManager.toast(result.error);
      }
    }
    update();
  }

  // ─── Calendar month change ────────────────────────────────────────────────────

  void onMonthChanged(DateTime date) {
    year = date.year;
    month = date.month;
    groupAPICall();
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────────

  String getFormattedDateTime() {
    final now = DateTime.now();
    final formatter = DateFormat('dd/MM/yyyy HH:mm');
    return 'Data as of ${formatter.format(now)}';
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    final context = Get.context!;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              applyDropdownSelection(dropDownType, p0);
            },
          ),
        );
      },
    ).whenComplete(() {
      update();
    });
  }
}