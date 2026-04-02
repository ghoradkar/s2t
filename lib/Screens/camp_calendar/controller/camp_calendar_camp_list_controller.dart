// ignore_for_file: avoid_print, file_names

import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/APIConstants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/CampTypeAndCatagoryResponse.dart';
import '../model/MonthlySurveySiteResponse.dart';
import '../repository/camp_calendar_repository.dart';

class CampCalendarCampListController extends GetxController {
  CampCalendarCampListController({
    required this.year,
    required this.month,
    required this.day,
    required this.isShowCampType,
    required this.selectedCampType,
    required this.isFromCampCalener,
    required this.subOrgId,
    required this.dIVID,
    required this.distCode,
    required this.calanderSelectedDate,
    required this.isTodayCount,
  });

  final String year;
  final String month;
  final String day;
  final bool isShowCampType;
  final CampTypeAndCatagoryOutput? selectedCampType;
  final bool isFromCampCalener;
  final int subOrgId;
  final int dIVID;
  final int distCode;
  final String calanderSelectedDate;
  final bool isTodayCount;

  final CampCalendarCampListRepository _repository =
      CampCalendarCampListRepository();

  int dESGID = 0;
  int empCode = 0;
  int totalRegistrationWorkers = 0;

  List<MonthlySurveySiteOutput> campList = [];
  bool isLoading = true;

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    getUserAttendanceDays();
  }

  Future<void> getUserAttendanceDays() async {
    Map<String, String> params = {};
    String urlString = "";

    if (isFromCampCalener) {
      final String method = APIConstants.kGetMonthlySurveySiteRequestForOSOrg;
      urlString = "${APIManager.kD2DBaseURL}$method";
      params = {
        "Month": month,
        "Year": year,
        "DistCode": distCode.toString(),
        "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
        "SubOrgId": subOrgId.toString(),
        "DIVID": dIVID.toString(),
        "UserId": empCode.toString(),
        "DESGID": dESGID.toString(),
      };
    } else {
      final String method = APIConstants.kGetMonthlySurveySiteRequestForOS;
      urlString = "${APIManager.kConstructionWorkerBaseURL}$method";
      params = {
        "Month": month,
        "Year": year,
        "DistCode": distCode.toString(),
        "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
      };
    }

    print(params);
    final result = await _repository.getUserAttendanceDays(urlString, params);

    if (result.success) {
      List<MonthlySurveySiteOutput> tempList = result.data?.output ?? [];
      tempList.sort((a, b) {
        final aValue = a.rEGISTERWORKERS ?? 0;
        final bValue = b.rEGISTERWORKERS ?? 0;
        return aValue.compareTo(bValue);
      });

      if (isTodayCount) {
        final String systemDate = FormatterManager.formatDateToFormatterString(
          DateTime.now(),
          "yyyy-MM-dd",
        );
        for (final MonthlySurveySiteOutput obj in tempList) {
          final String campDate = obj.campDate ?? "";
          if (systemDate == campDate) {
            totalRegistrationWorkers += obj.rEGISTERWORKERS ?? 0;
            campList.add(obj);
          }
        }
      } else {
        if (calanderSelectedDate.isNotEmpty) {
          for (final MonthlySurveySiteOutput obj in tempList) {
            final String campDate = obj.campDate ?? "";
            if (calanderSelectedDate == campDate) {
              totalRegistrationWorkers += obj.rEGISTERWORKERS ?? 0;
              campList.add(obj);
            }
          }
        } else {
          for (final MonthlySurveySiteOutput obj in tempList) {
            totalRegistrationWorkers += obj.rEGISTERWORKERS ?? 0;
            campList.add(obj);
          }
        }
      }
    } else {
      campList = [];
      ToastManager.toast(result.error);
    }
    isLoading = false;
    update();
  }
}