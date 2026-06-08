import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/BeneficiaryListByRegIDResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/D2DCampMappedDoctorListResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';

class CallToDoctorController extends GetxController {
  final int regdId;
  final int campId;
  final String healthScreentype;

  CallToDoctorController({
    required this.regdId,
    required this.campId,
    required this.healthScreentype,
  });

  final _api = APIManager();

  int empCode = 0;
  String teamId = "0";

  bool isLoading = false;
  bool isSubmitting = false;

  List<BeneficiaryListByRegIDOutput> beneficiaryList = [];
  List<D2DCampMappedDoctorOutput> doctorList = [];

  // Selected state per row index
  Map<int, bool> checkedMap = {};

  D2DCampMappedDoctorOutput? selectedDoctor;
  int callType = 0; // 0 = not chosen, 1 = Audio, 2 = Video

  // Controllers for the read-only AppTextFields shown as dropdowns
  final TextEditingController doctorTextCtrl = TextEditingController();
  final TextEditingController callTypeTextCtrl = TextEditingController();

  @override
  void onInit() {
    super.onInit();
    final userData = DataProvider().getParsedUserData()?.output?.first;
    empCode = userData?.empCode ?? 0;
    _loadTeamId();
    _loadBeneficiaryList();
  }

  @override
  void onClose() {
    doctorTextCtrl.dispose();
    callTypeTextCtrl.dispose();
    super.onClose();
  }

  // ── Team ID ───────────────────────────────────────────────────────────────

  void _loadTeamId() {
    _api.getTeamNumberByCampIdAndUSerIdAPI(
      {"campid": campId.toString(), "UserID": empCode.toString()},
      (TeamNumberByCampIdAndUserIdListResponse? response,
          String error,
          bool success) {
        if (success) {
          teamId = response?.output?.first.teamNumber ?? "0";
        }
        update();
      },
    );
  }

  // ── Beneficiary list ──────────────────────────────────────────────────────

  void _loadBeneficiaryList() {
    isLoading = true;
    update();
    _api.getBeneficiaryListByRegIDAPI(
      {"Regdid": regdId.toString()},
      (BeneficiaryListByRegIDResponse? response, String error, bool success) {
        if (success) {
          beneficiaryList = response?.output ?? [];
          checkedMap = {
            for (int i = 0; i < beneficiaryList.length; i++) i: false,
          };
        } else {
          beneficiaryList = [];
          ToastManager.toast(error);
        }
        isLoading = false;
        update();
      },
    );
  }

  // ── Doctor list — fetch only; screen shows the bottom sheet ──────────────

  void fetchDoctorList({required VoidCallback onLoaded}) {
    ToastManager.showLoader();
    _api.getD2DCampMappedDoctorListAPI(
      {"campid": campId.toString(), "TeamId": teamId},
      (D2DCampMappedDoctorListResponse? response, String error, bool success) {
        ToastManager.hideLoader();
        if (success) {
          doctorList = response?.output ?? [];
          if (doctorList.isEmpty) {
            ToastManager.toast("No doctors available for this camp");
          } else {
            onLoaded();
          }
        } else {
          ToastManager.toast(error);
        }
        update();
      },
    );
  }

  // ── Selection setters ─────────────────────────────────────────────────────

  void selectDoctor(D2DCampMappedDoctorOutput doctor) {
    selectedDoctor = doctor;
    doctorTextCtrl.text = doctor.fullName ?? "";
    update();
  }

  void selectCallType(int type) {
    callType = type;
    callTypeTextCtrl.text = type == 1 ? "Audio" : "Video";
    update();
  }

  // ── Toggle checkbox ───────────────────────────────────────────────────────

  void toggleCheck(int index) {
    checkedMap[index] = !(checkedMap[index] ?? false);
    update();
  }

  // ── Submit ────────────────────────────────────────────────────────────────

  void submitAssignment(BuildContext context) {
    final selectedIndices =
        checkedMap.entries.where((e) => e.value).map((e) => e.key).toList();

    if (selectedIndices.isEmpty) {
      ToastManager.toast("Please select at least one beneficiary");
      return;
    }
    if (selectedDoctor == null) {
      ToastManager.toast("Please choose a doctor");
      return;
    }
    if (callType == 0) {
      ToastManager.toast("Please select a call type");
      return;
    }

    final payload = selectedIndices.map((i) {
      final obj = beneficiaryList[i];
      return {
        "Regdid": (obj.beneficiaryRegdId ?? obj.regdId ?? 0).toString(),
        "DoctorID": selectedDoctor!.userId.toString(),
        "CreatedBy": empCode.toString(),
        "CallType": callType.toString(),
        "RoomID": "",
      };
    }).toList();

    isSubmitting = true;
    update();

    _api.insertBeneficiaryDoctorMappingAPI(
      payload,
      (bool success, String message) {
        isSubmitting = false;
        if (success) {
          // Mark assigned rows green locally before closing
          for (final i in selectedIndices) {
            if (i < beneficiaryList.length) {
              beneficiaryList[i].doctorMappedStatus = "1";
            }
          }
          update();
          ToastManager.toast("Call Request Submitted Successfully");
          Navigator.pop(context);
        } else {
          update();
          ToastManager.toast(message.isNotEmpty ? message : "Submission failed");
        }
      },
    );
  }
}
