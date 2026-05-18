import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_appointment_beneficiary_model.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_appointment_update_response.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_confirmatory_list_model.dart';

class CTAppointmentRepository {
  final APIManager _api = APIManager();

  Future<CTConfirmatoryListModel?> getConfirmatoryList(
    Map<String, dynamic> params,
  ) async {
    CTConfirmatoryListModel? result;
    await _api.getCTConfirmatoryListAPI(params, (response, _, success) {
      if (success) result = response;
    });
    return result;
  }

  Future<CTAppointmentBeneficiaryModel?> getAppointmentDetails(
    Map<String, dynamic> params,
  ) async {
    CTAppointmentBeneficiaryModel? result;
    await _api.getCTAppointmentListAPI(params, (response, _, success) {
      if (success) result = response;
    });
    return result;
  }

  Future<CTAppointmentUpdateResponse?> updateAppointmentDate(
    Map<String, dynamic> params,
  ) async {
    CTAppointmentUpdateResponse? result;
    await _api.updateCTAppointmentDateAPI(params, (response, _, success) {
      if (success) result = response;
    });
    return result;
  }

  Future<DistrictResponse?> getDistrictList(int empCode) async {
    DistrictResponse? result;
    await _api.getDistrictByUserIDAPI(
      {'STATELGDCODE': '2', 'USERID': '$empCode'},
      (response, _, success) {
        if (success) result = response;
      },
    );
    return result;
  }

  Future<AssignmentRemarksResponse?> getAssignmentRemarks(String type) async {
    AssignmentRemarksResponse? result;
    await _api.getAssignmentRemarksAPI({'Type': type}, (response, _, success) {
      if (success) result = response;
    });
    return result;
  }
}