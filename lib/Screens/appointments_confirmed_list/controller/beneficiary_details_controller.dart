// ignore_for_file: file_names, avoid_print

import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import 'package:s2toperational/Modules/Json_Class/CallStatusListResponse/CallStatusListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/RemarkListResponse/RemarkListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../model/appoinment_expected_beneficiaries_response.dart';
import '../model/beneficiaries_details_response.dart';
import '../repository/appointments_confirmed_repository.dart';

class BeneficiaryDetailsController extends GetxController {
  final _repo = AppointmentsConfirmedRepository();

  AppoinmentExpectedBeneficiariesOutput? selectedBeneficiary;
  bool isPatientRegistration = false;
  int empCode = 0;

  BeneficiariesDetailsOutput? beneficiaryDetails;
  CallStatusListOutput? selectedCallStatus;
  RemarkListOutput? selectedRemark;

  @override
  void onInit() {
    super.onInit();
    final args = Get.arguments as Map<String, dynamic>?;
    selectedBeneficiary = args?['beneficiary'] as AppoinmentExpectedBeneficiariesOutput?;
    isPatientRegistration = args?['isPatientRegistration'] as bool? ?? false;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedCallStatus = CallStatusListOutput(
      assignStatusID: 2,
      callingStatus: 'Booking Confirmed',
    );
    loadBeneficiaryData();
  }

  Future<void> loadBeneficiaryData() async {
    if (selectedBeneficiary == null) return;
    try {
      final params = {
        'AssignCallID': selectedBeneficiary!.assignCallID.toString(),
      };
      final res = await _repo.fetchBeneficiaryData(params);
      beneficiaryDetails = res.output?.first;
    } catch (e) {
      ToastManager.toast(e.toString());
    }
    update();
  }

  Future<List<CallStatusListOutput>> fetchCallStatusList() async {
    if (selectedBeneficiary == null) return [];
    try {
      ToastManager.showLoader();
      final params = {
        'AssignCallID': selectedBeneficiary!.assignCallID.toString(),
      };
      final res = await _repo.fetchCallStatusList(params);
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<List<RemarkListOutput>> fetchRemarkList() async {
    try {
      ToastManager.showLoader();
      final params = {
        'CallStatusID': selectedCallStatus?.assignStatusID?.toString() ?? '0',
      };
      final res = await _repo.fetchRemarkList(params);
      return res.output ?? [];
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  void setCallStatus(CallStatusListOutput status) {
    selectedCallStatus = status;
    update();
  }

  void setRemark(RemarkListOutput remark) {
    selectedRemark = remark;
    update();
  }

  Future<bool> saveData() async {
    final callStatus = selectedCallStatus?.callingStatus ?? '';
    final appointmentDate = beneficiaryDetails?.appoinmentDate ?? '';
    final appointmentTime = beneficiaryDetails?.appoinmentTime ?? '';
    final remark = selectedRemark?.callingRemark ?? '';

    if (callStatus.isEmpty) {
      ToastManager.toast('Please select call status');
      return false;
    }
    if (appointmentDate.isEmpty) {
      ToastManager.toast('Please select appointment date');
      return false;
    }
    if (appointmentTime.isEmpty) {
      ToastManager.toast('Please select appointment time');
      return false;
    }
    if (remark.isEmpty || remark.toLowerCase() == 'na') {
      ToastManager.toast('Please select remark');
      return false;
    }

    try {
      ToastManager.showLoader();
      final params = {
        'AssignCallID': selectedBeneficiary!.assignCallID.toString(),
        'CallStatusID': selectedCallStatus?.assignStatusID?.toString() ?? '0',
        'AppoinmentDate': appointmentDate,
        'AppoinmentTime': appointmentTime,
        'PhleboRemark': selectedRemark?.callingRemark ?? '',
        'PhleboRemarkID': selectedRemark?.cReamrkID?.toString() ?? '0',
        'CReatedBy': empCode.toString(),
      };
      await _repo.updateAppointmentDetails(params);
      return true;
    } catch (e) {
      final msg = e.toString();
      if (msg.contains('20 appointments are already booked')) {
        ToastManager.toast(
          '20 appointments are already booked for this date for this team. Please book for another date.',
        );
      } else {
        ToastManager.toast(msg);
      }
      return false;
    } finally {
      ToastManager.hideLoader();
    }
  }
}
