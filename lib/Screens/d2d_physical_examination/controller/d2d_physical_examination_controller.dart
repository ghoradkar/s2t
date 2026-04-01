import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/AllDistrictListForPhyExamResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/repository/d2d_physical_examination_repository.dart';

class D2DPhysicalExaminationController extends GetxController {
  final _repo = D2DPhysicalExaminationRepository();

  List<D2DPhysicalExamDetailsOutput> physicalExaminationList = [];
  bool isLoading = false;
  String selectedFromDate = "";
  String selectedToDate = "";
  AllDistrictListForPhyExamOutput? selectedDistrict;
  int totalAssigned = 0;
  int totalCallingPending = 0;
  int totalPhyExamPending = 0;
  int empCode = 0;

  @override
  void onInit() {
    super.onInit();
    selectedFromDate = FormatterManager.formatDateToString(DateTime.now());
    selectedToDate = FormatterManager.formatDateToString(DateTime.now());
    empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedDistrict = AllDistrictListForPhyExamOutput(
      dISTLGDCODE: 0,
      district: "All",
    );
    fetchData();
  }

  Future<void> fetchData() async {
    isLoading = true;
    update();
    try {
      final params = <String, String>{
        "LoginType": "4",
        "FromDate": selectedFromDate,
        "ToDate": selectedToDate,
        "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "",
        "DoctorID": empCode.toString(),
      };
      final response = await _repo.fetchPhysicalExamDetails(params);
      final allList = response?.output ?? [];
      final distCode = selectedDistrict?.dISTLGDCODE ?? 0;
      physicalExaminationList = distCode != 0
          ? allList.where((e) => e.dISTLGDCODE == distCode).toList()
          : allList;
      _computeTotals();
    } catch (e) {
      physicalExaminationList = [];
      _computeTotals();
      ToastManager.toast(e.toString());
    } finally {
      isLoading = false;
      update();
    }
  }

  Future<List<AllDistrictListForPhyExamOutput>> fetchDistricts() async {
    ToastManager.showLoader();
    try {
      final params = <String, String>{
        "FromDate": selectedFromDate,
        "ToDate": selectedToDate,
        "DoctorID": "$empCode",
      };
      final response = await _repo.fetchAllDistricts(params);
      final list = List<AllDistrictListForPhyExamOutput>.from(
        response?.output ?? [],
      );
      list.insert(
        0,
        AllDistrictListForPhyExamOutput(dISTLGDCODE: 0, district: "All"),
      );
      return list;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void onFromDateSelected(String date) {
    selectedFromDate = date;
    update();
  }

  void onToDateSelected(String date) {
    selectedToDate = date;
    update();
  }

  void onDistrictSelected(AllDistrictListForPhyExamOutput district) {
    selectedDistrict = district;
    update();
  }

  void applyFilter() {
    fetchData();
  }

  void _computeTotals() {
    totalAssigned = 0;
    totalCallingPending = 0;
    totalPhyExamPending = 0;
    for (final obj in physicalExaminationList) {
      totalAssigned += obj.assigned ?? 0;
      totalCallingPending += obj.callingPending ?? 0;
      totalPhyExamPending += obj.phyExamPending ?? 0;
    }
  }
}