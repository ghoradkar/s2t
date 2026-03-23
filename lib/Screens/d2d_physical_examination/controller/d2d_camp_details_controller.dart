import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DTeamWisePhyExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/repository/d2d_physical_examination_repository.dart';

class D2DCampDetailsController extends GetxController {
  final _repo = D2DPhysicalExaminationRepository();

  List<D2DTeamWisePhyExamDetailsOutput> physicalExaminationList = [];
  bool isLoading = false;
  int totalAssigned = 0;
  int totalCallingPending = 0;
  int totalPhyExamPending = 0;

  Future<void> fetchData({
    required int campId,
    required int doctorId,
  }) async {
    isLoading = true;
    update();
    ToastManager.showLoader();
    try {
      final params = <String, String>{
        "Type": "1",
        "CampID": campId.toString(),
        "DoctorID": doctorId.toString(),
        "TeamID": "0",
      };
      final response = await _repo.fetchTeamWiseDetails(params);
      physicalExaminationList = response?.output ?? [];
      _computeTotals();
    } catch (e) {
      physicalExaminationList = [];
      _computeTotals();
      ToastManager.toast(e.toString());
    } finally {
      isLoading = false;
      ToastManager.hideLoader();
      update();
    }
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