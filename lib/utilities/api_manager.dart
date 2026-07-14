// ignore_for_file: depend_on_referenced_packages, file_names, avoid_print

import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';

// import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamDetailsResponse/insert_details_response.dart';
import 'package:s2toperational/camp_creation/models/district_response.dart';

// import 'package:s2toperational/Modules/Json_Class/Is24By7IsAccountCreatedResponse/get_my_oprator_response.dart';
import 'package:s2toperational/login/models/login_response_model.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/ct_appointment_beneficiary_model.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/ct_appointment_update_response.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/ct_confirmatory_list_model.dart';
import 'package:s2toperational/d2d_physical_examination/model/months_response.dart';
import 'package:s2toperational/medicine_delivery_menu/model/post_camp_beneficiary_list_response.dart';
import 'package:s2toperational/d2d_physical_examination/model/years_response.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/payment_and_invoice/models/verification_remark_model.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:s2toperational/admin_dashboard/model/conducted_camps_totals.dart';
import 'package:s2toperational/admin_dashboard/model/home_and_hub_processing_model.dart';
import 'package:s2toperational/admin_dashboard/model/hub_homelab_dashboard_response.dart';
import 'package:s2toperational/admin_dashboard/model/todays_patients_response.dart';
import 'package:s2toperational/admin_dashboard/model/totalcamp_and_total_zero_camp_response.dart';
// import '../../Screens/admin_dashboard/model/home_and_hub_processing_model.dart';
import '../../d2d_physical_examination/model/attendances_list_using_site_details_id_response.dart';
import '../../d2d_physical_examination/model/beneficiary_list_by_reg_id_response.dart';
import '../../d2d_physical_examination/model/d2d_camp_mapped_doctor_list_response.dart';
import '../../d2d_physical_examination/model/d2d_physical_exam_details_response.dart';
import '../../d2d_physical_examination/model/get_my_oprator_response.dart';
import '../../d2d_physical_examination/model/insert_details_response.dart';
import '../../d2d_physical_examination/model/is_24_by7_is_account_created_response.dart';
import '../../d2d_physical_examination/model/t2t_calling_api_details_response.dart';
import '../../d2d_physical_examination/model/team_number_by_campId_and_user_id_list_response.dart';
// import '../../Screens/admin_dashboard/model/hub_homelab_dashboard_response.dart';
import '../../admin_dashboard/Model/home_lab_pending_count_table_model.dart';
import 'enums.dart';
// import '../../Screens/admin_dashboard/model/conducted_camps_totals.dart';
// import '../../Screens/admin_dashboard/model/todays_patients_response.dart';
import '../../expense_claim/model/advadetails_new_version_v2_response.dart';
import '../../expense_claim/model/advances_request_details_show_response.dart';
import '../../d2d_physical_examination/model/all_district_list_for_phy_exam_response.dart';
import '../../appointments_confirmed_list/model/appoinment_expected_beneficiaries_response.dart';
import '../../appointments_confirmed_list/model/appointment_status_response.dart';
import '../../device_and_resource_mapping/models/assign_resources_response.dart';
import '../../team_camp_mapping/model/assigned_external_resource_details_response.dart';
import '../../appointment_sample_collection_ct/models/assignment_remarks_response.dart';
import '../../ct_assignment/model/department_type_response.dart';

// import '../Json_Class/AttendancesListUsingSiteDetailsIDResponse/AttendancesListUsingSiteDetailsIDResponse.dart';
import '../../camp_details/model/audio_screening_details_response.dart';
import '../../appointments_confirmed_list/model/beneficiaries_details_response.dart';
import '../../appointments_confirmed_list/model/beneficiary_dependant_details_response.dart';
import '../../ct_assignment/model/beneficiary_details_for_assign_teamid_details_response.dart';
import '../../daily_work_dashboard/model/beneficiary_status_and_details_response.dart';
import '../../camp_details/model/beneficiary_worker_response.dart';
import '../../expense_claim/model/bill_submission_response.dart';
import '../../camp_calendar/model/bind_district_response.dart';
import '../../appointments_confirmed_list/model/call_status_list_response.dart';
import '../../health_screening_details/models/camp_close_camp_details_response.dart';
import '../../health_screening_details/models/camp_close_details_response.dart';
import '../../camp_calendar/model/camp_count_with_day_response.dart';
import '../../camp_details/model/camp_details_response.dart';
import '../../resource_re_mapping/models/CampDetailsntApprovalResponse.dart';
import '../../health_screening_details/models/camp_details_on_lab_for_door_to_door_response.dart';
import '../../camp_readiness_form/models/campId_list_response.dart';
import '../../camp_calendar/model/camp_list_v3_response.dart';
import '../../camp_readiness_form/models/camp_readiness_form_list_response.dart';
import '../../camp_readiness_form/models/camp_readiness_form_submitt_response.dart';
import '../../resource_re_mapping/models/CampResourceAllocationResponse.dart';
import '../../camp_calendar/model/camp_type_and_catagory_response.dart';
import '../../camp_creation/models/camp_type_response.dart';
import '../../expense_claim/model/camp_wise_invoice_details_response.dart';
import '../../camp_creation/models/company_list_response.dart';
import '../../ct_assignment/model/confirmatory_tests_screening_response.dart';
import '../../ct_assignment/model/confirmatory_tests_screening_tube_response.dart';
import '../../device_and_resource_mapping/models/consumable_list_details_response.dart';
import '../../device_and_resource_mapping/models/consumables_list_response.dart';
import '../../d2d_team/model/d2d_non_working_teams_response.dart';

// import '../Json_Class/D2DPhysicalExamDetailsResponse/d2d_physical_exam_details_response.dart';
import '../../d2d_physical_examination/model/d2d_physical_examnination_details_response.dart';
import '../../d2d_team/model/d2d_team_member_details_response.dart';
import '../../d2d_physical_examination/model/d2d_team_wise_phy_exam_details_response.dart';
import '../../d2d_team/model/d2d_teams_count_response.dart';
import '../../medicine_delivery_menu/model/data_for_packet_receive_response.dart';
import '../../device_and_resource_mapping/models/devices_list_response.dart';
import '../../camp_calendar/model/bind_division_response.dart';
import '../../expense_claim/model/expense_camp_id_list_v1_response.dart';
import '../../expense_claim/model/expense_head_response.dart';
import '../../resource_re_mapping/models/GetCampAssignUserResponse.dart';
import '../../d2d_availability/models/get_doc_list_d2d_response.dart';
import '../../camp_creation/models/home_and_hub_lab_camp_creation_response.dart';
import '../../camp_creation/models/initiated_by_response.dart';
import '../../d2d_physical_examination/model/insert_beneficiary_calling_log_response.dart';

// import '../Json_Class/Is24By7IsAccountCreatedResponse/is_24_by7_is_account_created_response.dart';
import '../../d2d_team/model/lab_by_user_id_response.dart';
import '../../daily_work_dashboard/model/lab_data_response.dart';
import '../../camp_creation/models/landing_lab_camp_creation_response.dart';
import '../../camp_details/model/lung_function_test_details_response.dart';
import '../../expense_claim/model/month_wise_invoice_response.dart';
import '../../camp_calendar/model/monthly_survey_site_response.dart';
import '../../d2d_physical_examination/model/organisation_wise_api_key_response.dart';
import '../../camp_details/model/other_reason_for_patient_rejection_response.dart';
import '../../medicine_delivery_menu/model/packet_accept_data_response.dart';
import '../../medicine_delivery_menu/model/packet_collection_response.dart';
import '../../medicine_delivery_menu/model/packet_return_response.dart';
import '../../camp_details/model/patient_checkup_analysis_report_response.dart';
import '../../medicine_delivery_menu/model/patient_list_re_allocation_for_medicine_delivery_response.dart';
import '../../camp_details/model/patient_status_details_list_response.dart';
import '../../daily_work_dashboard/model/recollection_assignment_remarks_response.dart';
import '../../daily_work_dashboard/model/recollection_beneficiary_dashboard_for_mob_response.dart';
import '../../daily_work_dashboard/model/recollection_beneficiary_status_and_details_count_v1_response.dart';
import '../../daily_work_dashboard/model/recollection_beneficiary_to_team_response.dart';
import '../../appointments_confirmed_list/model/remark_list_response.dart';
import '../../medicine_delivery_menu/model/report_delivery_executive_response.dart';
import '../../device_and_resource_mapping/models/resource_list_response.dart';
import '../../resource_re_mapping/models/ResourceReMappingCampResponse.dart';
import '../../appointments_confirmed_list/model/screened_dependent_count_response.dart';
import '../../camp_creation/models/screening_test_camp_creation_response.dart';
import '../../ct_assignment/model/selected_teams_data_list_response.dart';
import '../../device_and_resource_mapping/models/sub_devices_list_response.dart';
import '../../expense_claim/model/sub_expense_heads_response.dart';
import '../../camp_calendar/model/sub_organization_response.dart';
import '../../device_and_resource_mapping/models/sub_resource_list_response.dart';
import '../../device_and_resource_mapping/models/submit_device_mapping_response.dart';
import '../../ct_assignment/model/t2t_ct_beneficiary_details_response.dart';
import '../../ct_assignment/model/t2t_ct_team_and_beneficiary_response.dart';
import '../../ct_assignment/model/t2t_ct_user_details_response.dart';

// import '../Json_Class/T2TCallingAPIDetailsResponse/t2t_calling_api_details_response.dart';
import '../../camp_creation/models/taluka_camp_creation_response.dart';
import '../../appointments_confirmed_list/model/team_cc_response.dart';
import '../../team_camp_mapping/model/team_camp_details_list_response.dart';
import '../../team_camp_mapping/model/team_camp_lab_response.dart';
import '../../team_camp_mapping/model/team_details_list_for_assign_response.dart';
import '../../camp_details/model/team_details_list_response.dart';

// import '../Json_Class/TeamNumberByCampIdAndUserIdListResponse/team_number_by_campId_and_user_id_list_response.dart';
import '../../d2d_physical_examination/model/team_wise_physical_exam_details_response.dart';
import '../../team_camp_mapping/model/teams_camp_type_wise_response.dart';
import '../../team_camp_mapping/model/teams_doctor_list_response.dart';
import '../../team_camp_mapping/model/teams_mmu_doctor_list_response.dart';
import '../../camp_details/model/test_list_for_reject_response.dart';
// import '../../Screens/admin_dashboard/model/totalcamp_and_total_zero_camp_response.dart';
import '../../resource_re_mapping/models/UpdateSubResourceListResponse.dart';
import '../../verify_otp/models/user_android_id_response.dart';
import '../../user_attendance/model/user_attandance_response.dart';
import '../../user_attendance/model/user_attendances_using_site_details_id_response.dart';
import '../../user_attendance/model/user_camp_mapping_and_attendance_data_response.dart';
import '../../user_attendance/model/user_camp_mapping_and_attendance_status_response.dart';
import '../../resource_re_mapping/models/UserCampMappingStatusResponse.dart';
import '../../expense_claim/model/user_invoice_payment_details_response.dart';
import '../../medicine_delivery_menu/model/user_mapped_taluka_response.dart';
import '../../camp_details/model/vision_screening_details_response.dart';
import '../constants/api_constants.dart';
import 'package:http/io_client.dart';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart';
import 'package:path/path.dart' as p;

class APIManager {
  // creates an instance of the Singleton class
  static final APIManager _singleton = APIManager._internal();

  factory APIManager() {
    return _singleton;
  }

  APIManager._internal();

  static String kD2DBaseURL = "";
  static String kCallingBaseURL = "";
  static String kConstructionWorkerBaseURL = "";
  static String kWebservicesBaseURL = "";
  static String kExpenseBillDetailsHandler = "";
  static String kUploadPostCampFileHandler = "";
  static String kChangeBeneficiaryAndCardImage = "";
  static String kMedicineDeliveryAckHandler = "";
  static String kCampAttendancePhotoHandler = "";
  static String kCampAttendanceDuringPhotoHandler = "";
  static String kCTSampleCollectionConsentHandler = "";
  static String kMediaBaseURL = "";
  static String kMahabocwBaseURL = "";
  static String kt24By7 = "";
  static String kLiverScann = "";
  static String kTreatmentCount = "";
  static String kTwentyFourBySeven =
      "https://app.office24by7.com/v1/common/API/";
  static String kVodaphone =
      "https://cts.myvi.in:8443/Cpaas/api/v1/clicktocall/";
  static String kMyOperator = "https://obd-api.myoperator.co/";
  APIMode apiMode = APIMode.Beta;
  final IOClient _ioClient = APIManager.getInstanceOfIo1Client();

  void setAPIEnvironment() {
    switch (apiMode) {
      case APIMode.Live:
        kD2DBaseURL = "https://mcwwb.janarogyaseva.in/webservices/d2d_V2.asmx/";
        kCallingBaseURL =
            "https://mcwwb.janarogyaseva.in/webservices/BeneficiaryCalling.asmx/";
        kConstructionWorkerBaseURL =
            "https://mcwwb.janarogyaseva.in/webservices/ConstructionWorker_V2.asmx/";
        kWebservicesBaseURL = "https://mcwwb.janarogyaseva.in/webservices/";
        kMahabocwBaseURL = "https://healthcamp.mahabocw.in/api/";
        kExpenseBillDetailsHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/ExpenseBillDetailsHandler.ashx";
        kUploadPostCampFileHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/MultipleExpenseBillUploader.ashx";
        kChangeBeneficiaryAndCardImage =
            "https://mcwwb.janarogyaseva.in/webservices/handler/ChangeBeneficiaryAndCardImage_InCampTest.ashx";
        kMedicineDeliveryAckHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/CW_MedicineDelivery_V1_DC.ashx";
        kCampAttendancePhotoHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/CampAttendanceCheckInOutImages.ashx";
        kCampAttendanceDuringPhotoHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/CampAttendanceCheckInOutDuringCampImages.ashx";
        kCTSampleCollectionConsentHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/CW_T2TBarcodeCollectionDetails_Consent_V1.ashx";
        kMediaBaseURL = "https://mcwwb.janarogyaseva.in/MCWWBDOCS_LIVE";
        kt24By7 = "https://app.office24by7.com/v1/common/API/";
        kLiverScann =
            "https://reports.myhindlab.com/ReportsMCWLive/API/Liverscan/";
        kTreatmentCount =
            "http://103.251.94.57:8080/disha-t2t-Apis/api/access/master/countdata/";
      case APIMode.Beta:
        String baseUrl = "https://testmcwwb.myhindlab.com/webservices/";
        // String baseUrl = "https://newtesting.myhindlab.com/webservices/";

        kMediaBaseURL = "https://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS";
        // kMediaBaseURL = "https://newtesting.myhindlab.com/BETA_MYHINDLABDOCS";

        kD2DBaseURL = "${baseUrl}D2D_V2.asmx/";
        kCallingBaseURL = "${baseUrl}BeneficiaryCalling.asmx/";
        kConstructionWorkerBaseURL = "${baseUrl}ConstructionWorker_V2.asmx/";
        kWebservicesBaseURL = baseUrl;
        kMahabocwBaseURL = "https://healthcamp.mahabocw.in/api/";
        kExpenseBillDetailsHandler =
            "${baseUrl}handler/ExpenseBillDetailsHandler.ashx";
        kUploadPostCampFileHandler =
            "${baseUrl}handler/MultipleExpenseBillUploader.ashx";
        kChangeBeneficiaryAndCardImage =
            "${baseUrl}handler/ChangeBeneficiaryAndCardImage_InCampTest.ashx";
        kMedicineDeliveryAckHandler =
            "${baseUrl}handler/CW_MedicineDelivery_V1_DC.ashx";
        kCampAttendancePhotoHandler =
            "${baseUrl}handler/CampAttendanceCheckInOutImages.ashx";
        kCampAttendanceDuringPhotoHandler =
            "${baseUrl}handler/CampAttendanceCheckInOutDuringCampImages.ashx";
        kCTSampleCollectionConsentHandler =
            "${baseUrl}handler/CW_T2TBarcodeCollectionDetails_Consent_V1.ashx";

        kt24By7 = "https://app.office24by7.com/v1/common/API/";

        kLiverScann =
            "https://reports.myhindlab.com/ReportsMCWLive/API/Liverscan/";
        kTreatmentCount =
            "http://103.251.94.57:8080/disha-t2t-Apis/api/access/master/countdata/";
    }
  }

  IOClient getInstanceOfIoClient() {
    final HttpClient httpClient =
        HttpClient()
          ..badCertificateCallback =
              (X509Certificate cert, String host, int port) => true;

    return IOClient(httpClient);
  }

  static IOClient getInstanceOfIo1Client() {
    final HttpClient httpClient =
        HttpClient()
          ..badCertificateCallback =
              (X509Certificate cert, String host, int port) => true;

    return IOClient(httpClient);
  }

  Future<void> checkAppVersionAPI({
    required String version,
    required Function(String status, String message) onResult,
  }) async {
    const String applicationId = "91";
    String method = APIConstants.kAPKDownloader;

    // Server expects "major.minor" format (e.g. "1.0"), not "major.minor.patch"
    final parts = version.split('.');
    final serverVersion =
        parts.length >= 2 ? '${parts[0]}.${parts[1]}' : version;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    var body = {'aplicationId': applicationId, 'versionname': serverVersion};
    try {
      final response = await _ioClient.post(
        url,
        body: body,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('APKDownloader req body: $body');
      debugPrint('APKDownloader url: $url');
      debugPrint('APKDownloader response: ${response.body}');
      final decoded = json.decode(response.body);
      final status = decoded['status'] ?? '';
      final message = decoded['message'] ?? '';
      onResult(status, message);
    } catch (e) {
      debugPrint('APKDownloader error: $e');
      onResult('', '');
    }
  }

  Future<void> getLoginAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kUserLogin;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      if (response.statusCode == 200) {
        LoginResponseModel person = LoginResponseModel.fromJson(
          json.decode(response.body),
        );
        if (person.status == 'Success') {
          DataProvider().storeUserData(response.body);
          DataProvider().storeUserCredential(jsonEncode(data));
          DataProvider().isRegularCamp(true);
          DataProvider().save(DataProvider().kUserName, data['username']);
          DataProvider().save(DataProvider().kPassword, data['password']);
          callback(person, "", true);
        } else {
          callback(person, person.message, false);
        }
      }
    } catch (e) {
      // Handle error
      callback(null, "Server Not Responding", false);
      debugPrint("Exception: $e");
    }
  }

  Future<void> getCampTypeNonD2DRAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeNonD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      debugPrint('${json.decode(response.body)}');
      CampTypeResponse person = CampTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampTypeFlexiAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeFlexi;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      debugPrint('${json.decode(response.body)}');
      CampTypeResponse person = CampTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampTypeMMUAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeMMU;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      CampTypeResponse person = CampTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampTypeNonD2DAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeNonD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      CampTypeResponse person = CampTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampTypeD2DAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      CampTypeResponse person = CampTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getInitiatedByListForCampAPI(dynamic callback) async {
    String method = APIConstants.kGetInitiatedByListForCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      InitiatedByResponse person = InitiatedByResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTalukaAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetAllTalukaList;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TalukaCampCreationResponse person = TalukaCampCreationResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getLandingLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabDistrictWiseV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      LandingLabCampCreationResponse person =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getT2TLabDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TLabDetails;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      LandingLabCampCreationResponse person =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> sendOTPForCTSampleCollectionAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kInsertOTPForCTSampleCollection;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> verifyOTPForCTSampleCollectionAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kVerifyCTOTP;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getHomeLabHubLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetHomeAndHubLabNamesOfLandingLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      HomeAndHubLabCampCreationResponse person =
          HomeAndHubLabCampCreationResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getScreeningTestAPI(dynamic callback) async {
    String method = APIConstants.kTestList;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      ScreeningTestCampCreationResponse person =
          ScreeningTestCampCreationResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> createCampCreationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCampCreationV3;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      LandingLabCampCreationResponse person =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampIDAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetCampListV3;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      CampListV3Response person = CampListV3Response.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDevicesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDeviceListForCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      DevicesListResponse person = DevicesListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getSubDevicesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetSUBDeviceListNew;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      SubDevicesListResponse person = SubDevicesListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getConsumablesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListForExpectedBenificiary;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      ConsumablesListResponse person = ConsumablesListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getResourceAPI(dynamic callback) async {
    String method = APIConstants.kGetTestListForMapResourceDesgForD2d;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      debugPrint('${json.decode(response.body)}');
      ResourceListResponse person = ResourceListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getSubResourceAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTestidFromResourcelistNewForPatnerResorces;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      SubResourceListResponse person = SubResourceListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> submitDeviceAllocationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInserCampCreationD2DWithoutApproval;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      SubmitDeviceMappingResponse person = SubmitDeviceMappingResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        if (person.message == null) {
          callback(person, person.exceptionValue, false);
        } else {
          callback(person, person.message, false);
        }
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampListCampReadinessAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampListCampReadiness;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      CampIdListResponse person = CampIdListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamNumberByCampIdAndUSerIdAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamNumberByCampIdAndUSerId;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint('${json.decode(response.body)}');
      TeamNumberByCampIdAndUserIdListResponse person =
          TeamNumberByCampIdAndUserIdListResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPatientAndTestValidationCountAPI(
    Map<String, String> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(
      '$kD2DBaseURL${APIConstants.kGetPatientAndTestValidationCount}',
    );
    debugPrint('getPatientAndTestValidationCountAPI URL: $url');
    debugPrint('getPatientAndTestValidationCountAPI body: $data');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('getPatientAndTestValidationCountAPI response: ${response.body}');
      final decoded =
          json.decode(utf8.decode(response.bodyBytes)) as Map<String, dynamic>;
      final status = decoded['status'] as String? ?? '';
      if (status == 'Success') {
        final output = decoded['output'] as List?;
        if (output != null && output.isNotEmpty) {
          final allTestDone = output.first['ALLTESTDONE']?.toString() ?? '1';
          callback(allTestDone, '', true);
        } else {
          callback('1', '', true);
        }
      } else {
        callback('1', decoded['message'] as String? ?? '', false);
      }
    } catch (e) {
      callback('1', 'Exception: $e', false);
    }
  }

  Future<void> getCampReadinessFormItemsAPI(
    int campID,
    int teamId,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampReadinessFormItems;

    final url = Uri.parse('$kD2DBaseURL$method?CampID=$campID&TeamId=$teamId');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint('${json.decode(response.body)}');
      CampReadinessFormListResponse person =
          CampReadinessFormListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertCampReadinessFormDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCampReadinessFormDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint('${json.decode(response.body)}');
      CampReadinessFormSubmittResponse person =
          CampReadinessFormSubmittResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAdvadetailsNewVersionV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAdvadetailsNewVersionV2;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      AdvadetailsNewVersionV2Response person =
          AdvadetailsNewVersionV2Response.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getExpenseHeadAPI(dynamic callback) async {
    String method = APIConstants.kGetExpensesMasterData;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      debugPrint(response.body);
      ExpenseHeadResponse person = ExpenseHeadResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getSubExpenseHeadAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetSubExpensesMasterDataV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');

      SubExpenseHeadsResponse person = SubExpenseHeadsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserAttendanceAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetuserAttendance;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      UserAttandanceResponse person = UserAttandanceResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getInsertUserAttendancAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertUserAttendance;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      UserAttandanceResponse person = UserAttandanceResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> saveBillDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertAdvancesRequestNewChangesV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      BillSubmissionResponse person = BillSubmissionResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> uploadBillsAPI(
    Map<String, dynamic> data,
    File selectedFile,
    String fileType,
    dynamic callback,
  ) async {
    final url = Uri.parse(kExpenseBillDetailsHandler);
    final IOClient uploadClient = getInstanceOfIoClient();

    try {
      var request = http.MultipartRequest("POST", url);

      data.forEach((key, value) {
        if (value != null) {
          request.fields[key] = value.toString();
        }
      });

      MediaType contentType = FormatterManager.getMediaTypeFromFile(
        selectedFile,
      );

      String fileName = FormatterManager.getFileNameInfo(selectedFile);
      request.files.add(
        await http.MultipartFile.fromPath(
          'FileName',
          selectedFile.path,
          contentType: contentType,
          filename: fileName,
        ),
      );

      // Send request
      final streamedResponse = await uploadClient.send(request);
      final response = await http.Response.fromStream(streamedResponse);

      debugPrint(response.body);
      if (response.statusCode == 200) {
        BillSubmissionResponse person = BillSubmissionResponse.fromJson(
          json.decode(response.body),
        );
        debugPrint("Image uploaded successfully: ${response.body}");
        if (person.status == 'Success') {
          callback(person, "", true);
        } else {
          callback(person, person.message, false);
        }
      } else {
        debugPrint("Upload failed: ${response.statusCode} - ${response.body}");
        callback(null, "Image not uploaded successfully", false);
      }
    } catch (e) {
      debugPrint("Error uploading image: $e");
      callback(null, e.toString(), false);
    } finally {
      uploadClient.close();
    }
  }

  Future<void> getExpenseCampIDListV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetExpenseCampIDListV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      ExpenseCampIDListV1Response person = ExpenseCampIDListV1Response.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertMultipleCampIDV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMultipleCampIDV2;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      BillSubmissionResponse person = BillSubmissionResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> uploadSequentially(
    List<File> files,
    Map<String, String> params,
    dynamic callback,
  ) async {
    int successCount = 0;
    int failCount = 0;

    for (var file in files) {
      final response = await uploadFile(file, params);
      if (response.statusCode == 200) {
        debugPrint("âœ… Uploaded ${file.path}");
        successCount++;
      } else {
        debugPrint("âŒ Failed ${file.path}: ${response.statusCode}");
        failCount++;
      }
    }
    int total = (successCount + failCount);
    debugPrint("successCount $successCount");
    debugPrint("failCount $failCount");
    debugPrint("total $total");
    if (files.length == total) {
      if (successCount == files.length) {
        callback(null, "", true);
      } else {
        callback(null, "", false);
      }
    }

    debugPrint('Finished: $successCount success, $failCount failed');
  }

  Future<http.Response> uploadFile(
    File file,
    Map<String, String> params,
  ) async {
    final uri = Uri.parse(kUploadPostCampFileHandler);


    final request = http.MultipartRequest("POST", uri);

    params.forEach((key, value) {
      request.fields[key] = value.toString();
    });

    MediaType contentType = FormatterManager.getMediaTypeFromFile(file);
    debugPrint("uploadFile");
    // Add image file
    String fileName = FormatterManager.getFileNameInfo(file);
    request.files.add(
      await http.MultipartFile.fromPath(
        'AttachementProof',
        file.path,
        filename: fileName,
        contentType: contentType,
      ),
    );

    final streamedResponse = await _ioClient.send(request);
    return await http.Response.fromStream(streamedResponse);
  }

  Future<void> getSubOrganizationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBindOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      SubOrganizationResponse person = SubOrganizationResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBindDivision(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kBindDivision;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      BindDivisionResponse person = BindDivisionResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBindDistrictAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kBindDistrict;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      BindDistrictResponse person = BindDistrictResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampTypeAndCatagoryAPI(dynamic callback) async {
    String method = APIConstants.kGetCampTypeAndCatagory;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');

      debugPrint(response.body);
      CampTypeAndCatagoryResponse person = CampTypeAndCatagoryResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampCountWithDayAndMonthWiseWithCampTypeOrgAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetCampCountWithDayAndMonthWiseWithCampTypeOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      CampCountWithDayResponse person = CampCountWithDayResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTotalcampAndTotalBeneficiarywithZeroCampAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetTotalcampAndTotalBeneficiarywithZeroCampOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      TotalcampAndTotalZeroCampResponse person =
          TotalcampAndTotalZeroCampResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserAttendanceDaysAPI(
    String urlStirng,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlStirng);

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      MonthlySurveySiteResponse person = MonthlySurveySiteResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getScreeningTestCampDetailsAPI(
    String urlStirng,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlStirng);

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      CampDetailsResponse person = CampDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBillSubmitdetailsShowAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBillSubmitdetailsShow;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      AdvancesRequestDetailsShowResponse person =
          AdvancesRequestDetailsShowResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getApprovedCampListDetailsForAppFlexiCampAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApprovedCampListDetailsForAppFlexiCampV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('=== getApprovedCampListDetailsForAppFlexiCampAPI ===');
      debugPrint('URL: $url');
      debugPrint('Body: $data');
      debugPrint('Response: ${response.body}');
      final decoded = json.decode(response.body);
      ResourceReMappingCampResponse person =
          ResourceReMappingCampResponse.fromJson(
            decoded as Map<String, dynamic>,
          );
      debugPrint(
        'Status: ${person.status} | Message: ${person.message} | OutputCount: ${person.output?.length}',
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? '', false);
      }
    } catch (e, st) {
      debugPrint('getApprovedCampListDetailsForAppFlexiCampAPI error: $e\n$st');
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetUserCampMappingAndAttendanceStatusForRegularCamp;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      UserCampMappingStatusResponse person =
          UserCampMappingStatusResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampAssignUserListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampAssignUserList;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      GetCampAssignUserResponse person = GetCampAssignUserResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampDetailsForAppForIntApprovalAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsForAppForIntApproval;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      CampDetailsntApprovalResponse person =
          CampDetailsntApprovalResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getResourcesForApprovalAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampResourceDetailsForAppForIntApproval;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      CampResourceAllocationResponse person =
          CampResourceAllocationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getApproveResourcelstForUpdateAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApproveResourcelstForUpdate;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> removeResourceAllcoationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kRemoveCampMappingResources;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> addResourceAllcoationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateCampMappingResources;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getApprovedCampListDetailsForAppAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApprovedCampListDetailsForApp;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      ResourceReMappingCampResponse person =
          ResourceReMappingCampResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampDetailsonLabForDoorToDoorV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsonLabForDoorToDoorV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      CampDetailsonLabForDoorToDoorResponse person =
          CampDetailsonLabForDoorToDoorResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserAttendancesUsingSitedetailsIDNewAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserAttendancesUsingSitedetailsIDNew;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AcknowledgementPatientListResponse person =
          AcknowledgementPatientListResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAcknowledgementPatientListAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AcknowledgementPatientListResponse person =
          AcknowledgementPatientListResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getRegiWorkerDetailsOncampIdAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRegiWorkerDetailsOncampIdInCampTest;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      BeneficiaryWorkerResponse person = BeneficiaryWorkerResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCAMPPatientCheckupAnalysisReportNewAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kCAMPPatientCheckupAnalysisReportV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PatientCheckupAnalysisReportResponse person =
          PatientCheckupAnalysisReportResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getLungFunctionTestDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLungFunctionTestDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      LungFunctionTestDetailsResponse person =
          LungFunctionTestDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getVisionScreeningDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetVisionScreeningDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      VisionScreeningDetailsResponse person =
          VisionScreeningDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAudioScreeningDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAudioScreeningDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AudioScreeningDetailsResponse person =
          AudioScreeningDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<bool> insertMachineHearingTestAPI({
    required String regdId,
    required String createdBy,
    required String jsonString,
  }) async {
    final url = Uri.parse(
      '${kConstructionWorkerBaseURL}${APIConstants.kInsertMachineHearingTestNew}',
    ).replace(
      queryParameters: {'CreatedBy': createdBy, 'JsonString': jsonString},
    );
    try {
      final response = await _ioClient.get(url);
      debugPrint('insertMachineHearingTestAPI response: ${response.body}');
      final body = json.decode(response.body) as Map<String, dynamic>;
      return (body['status'] ?? '').toString().toLowerCase() == 'success';
    } catch (e) {
      debugPrint('insertMachineHearingTestAPI error: $e');
      return false;
    }
  }

  Future<void> getTestToRejectAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTestListForReject;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TestListForRejectResponse person = TestListForRejectResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getOtherReasonForPatientRejectionAPI(dynamic callback) async {
    String method = APIConstants.kGetOtherReasonForPatientRejection;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      debugPrint('${json.decode(response.body)}');
      OtherReasonForPatientRejectionResponse person =
          OtherReasonForPatientRejectionResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPatientRejectionInCampInCampTestV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPatientRejectionInCampInCampTestV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TestListForRejectResponse person = TestListForRejectResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCAMPPatientCheckupAnalysisReportAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kCAMPPatientCheckupAnalysisReportNew;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PatientStatusDetailsListResponse person =
          PatientStatusDetailsListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampIDWiseTeamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampIDWiseTeamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamDetailsListResponse person = TeamDetailsListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampDetailsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsCountRegularInCampTest;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      CampCloseCampDetailsResponse person =
          CampCloseCampDetailsResponse.fromJson(json.decode(response.body));

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampCloseDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampCloseDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      CampCloseDetailsResponse person = CampCloseDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getConsumableListDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      ConsumableListDetailsResponse person =
          ConsumableListDetailsResponse.fromJson(json.decode(response.body));

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> uploadBeneficiaryVerificationAPI(
    Map<String, dynamic> data,
    String imageFileName,
    File imageFile,
    String isType,
    dynamic callback,
  ) async {
    final url = Uri.parse(kChangeBeneficiaryAndCardImage);
    final IOClient uploadClient = getInstanceOfIoClient();

    try {
      var request = http.MultipartRequest("POST", url);

      data.forEach((key, value) {
        if (value != null) {
          request.fields[key] = value.toString();
        }
      });

      MediaType contentType = FormatterManager.getMediaTypeFromFile(imageFile);

      request.files.add(
        await http.MultipartFile.fromPath(
          'FilePath',
          imageFile.path,
          contentType: contentType,
          filename: imageFileName,
        ),
      );

      // Send request
      final streamedResponse = await uploadClient.send(request);
      final response = await http.Response.fromStream(streamedResponse);

      debugPrint(response.body);
      if (response.statusCode == 200) {
        BillSubmissionResponse person = BillSubmissionResponse.fromJson(
          json.decode(response.body),
        );
        debugPrint("Image uploaded successfully: ${response.body}");
        if (person.status == 'Success') {
          callback(person, "", true);
        } else {
          callback(person, person.message, false);
        }
      } else {
        debugPrint("Upload failed: ${response.statusCode} - ${response.body}");
        callback(null, "Image not uploaded successfully", false);
      }
    } catch (e) {
      debugPrint("Error uploading image: $e");
      callback(null, e.toString(), false);
    } finally {
      uploadClient.close();
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusD2DAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      UserCampMappingAndAttendanceStatusResponse person =
          UserCampMappingAndAttendanceStatusResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAppointmentStatusListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAppointmentStatusList;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AppointmentStatusResponse person = AppointmentStatusResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamListCCAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamListCC;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCCResponse person = TeamCCResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAppointmentBeneficiariesMASListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAppoinmentDetailsV1MAS;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AppoinmentExpectedBeneficiariesResponse person =
          AppoinmentExpectedBeneficiariesResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAppointmentBeneficiariesListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAppoinmentDetailsV1;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AppoinmentExpectedBeneficiariesResponse person =
          AppoinmentExpectedBeneficiariesResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserMappedTalukaAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserMappedTaluka;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      UserMappedTalukaResponse person = UserMappedTalukaResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBarcodePostCampDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetPatientListReAllocationforMedicineDeliveryByPacketID;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PatientListReAllocationforMedicineDeliveryResponse person =
          PatientListReAllocationforMedicineDeliveryResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, "Packet No. Delivery Challan No. Not Found", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPacketDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateReallocationMedicalDelivary;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PatientListReAllocationforMedicineDeliveryResponse person =
          PatientListReAllocationforMedicineDeliveryResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getReportDeliveryExecutiveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetReportDeliveryExecutiveD2DTeam;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      ReportDeliveryExecutiveResponse person =
          ReportDeliveryExecutiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDataForPacketAssignmentAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketAssignment;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPacketAssignDetailsManuallyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAssignDetailsManually;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPhleboCallStatusListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPhleboCallStatusList;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CallStatusListResponse person = CallStatusListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBeneficiaryDataAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAddressDetails;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiariesDetailsResponse person =
          BeneficiariesDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDependentListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryDependantDetails;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiaryDependantDetailsResponse person =
          BeneficiaryDependantDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getScreeningCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetScreenedDependentCount;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      ScreenedDependentCountResponse person =
          ScreenedDependentCountResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCallingRemarkV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCallingRemarkV1;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RemarkListResponse person = RemarkListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> updateAppointmentDetailsMobAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateAppointmentDetailsMob;

    final url = Uri.parse('$kCallingBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RemarkListResponse person = RemarkListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getLabforD2DCampCoordinatorAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabforD2DCampCoordinator;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      LabDataResponse person = LabDataResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountForPageloadForTeamAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountTeamV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryToTeamResponse person =
          RecollectionBeneficiaryToTeamResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountForPageloadForTeamV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountTeamV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryStatusandDetailsCountV1Response person =
          RecollectionBeneficiaryStatusandDetailsCountV1Response.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountForPageloadAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryToTeamResponse person =
          RecollectionBeneficiaryToTeamResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountForPageloadVaAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryStatusandDetailsCountV1Response person =
          RecollectionBeneficiaryStatusandDetailsCountV1Response.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountAPI(Map<String, dynamic> data, dynamic callback) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryToTeamResponse person =
          RecollectionBeneficiaryToTeamResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryStatusandDetailsCountV1Response person =
          RecollectionBeneficiaryStatusandDetailsCountV1Response.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCountForTeamPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionBeneficiaryToTeam;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryStatusandDetailsCountV1Response person =
          RecollectionBeneficiaryStatusandDetailsCountV1Response.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getRecollectionAssignmentRemarksAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionAssignmentRemarks;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionAssignmentRemarksResponse person =
          RecollectionAssignmentRemarksResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getRecollectionBeneficiaryDashboardForMobAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionBeneficiaryDashboardForMob;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      RecollectionBeneficiaryDashboardForMobResponse person =
          RecollectionBeneficiaryDashboardForMobResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBeneficiaryStatusAndDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiaryStatusAndDetailsResponse person =
          BeneficiaryStatusAndDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getRecollectionTeamDetialsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionTeamDetials;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      SelectedTeamsDataListResponse person =
          SelectedTeamsDataListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertAppointmentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdaterecollectionAppointmentDate;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiaryStatusAndDetailsResponse person =
          BeneficiaryStatusAndDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertRecollectionTeamandBeneficiaryMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertRecollectionTeamandBeneficiaryMapping;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiaryStatusAndDetailsResponse person =
          BeneficiaryStatusAndDetailsResponse.fromJson(
            json.decode(response.body),
          );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusForRegularCampReadinessAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants
            .kGetUserCampMappingAndAttendanceStatusForRegularCampReadiness;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserCampMappingAndAttendanceDataResponse person =
          UserCampMappingAndAttendanceDataResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusReadinessAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetUserCampMappingAndAttendanceStatusReadiness;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserCampMappingAndAttendanceDataResponse person =
          UserCampMappingAndAttendanceDataResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getYearAPI(dynamic callback) async {
    String method = APIConstants.kGetYear;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      YearsResponse person = YearsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMonthsAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetMonth;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      MonthsResponse person = MonthsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMonthWiseInvoiceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetMonthWiseInvoiceStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      MonthWiseInvoiceResponse person = MonthWiseInvoiceResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMonthWiseDoctorInvoiceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetMonthWiseDoctorInvoiceStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      MonthWiseInvoiceResponse person = MonthWiseInvoiceResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserInvoicePaymentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserInvoicePaymentDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserInvoicePaymentDetailsResponse person =
          UserInvoicePaymentDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserDoctorInvoicePaymentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserDoctorInvoicePaymentDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserInvoicePaymentDetailsResponse person =
          UserInvoicePaymentDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampWiseInvoiceDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampWiseInvoiceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCampWiseDoctorInvoiceDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampWiseDoctorInvoiceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getOTPForMedicineDeliveryOrgAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertInvoiceOTPDetailsOrg;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDoctorInvoiceOTPDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertDoctorInvoiceOTPDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPaidByCompanyListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPaidByCompanyList;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CompanyListResponse person = CompanyListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPaymentVerificationRemarkAPI(dynamic callback) async {
    String method = APIConstants.kGetPaymentVerificationRemark;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(url);
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      VerificationRemarkResponse person = VerificationRemarkResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserAttendancesUsingSitedetailsIDAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      String body = response.body;
      debugPrint(body);
      UserAttendancesUsingSitedetailsIDResponse person =
          UserAttendancesUsingSitedetailsIDResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertInvoicePaymentStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertInvoicePaymentStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertDoctorInvoicePaymentStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertDoctorInvoicePaymentStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserDataforPhysicalExamninationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserDataforPhysicalExamnination;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      D2DPhysicalExamninationDetailsResponse person =
          D2DPhysicalExamninationDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPhysicalExaminationForHSCCV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPhysicalExaminationForHSCCV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      D2DPhysicalExamninationDetailsResponse person =
          D2DPhysicalExamninationDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertBasicHealthInfoNewAPI(
    Map<String, String> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(
      '$kD2DBaseURL${APIConstants.kInsertBasicHealthInfoNewWithVersionFastingHrs}',
    );
    // ignore: avoid_print
    debugPrint('[insertBasicHealthInfo] URL: $url');
    // ignore: avoid_print
    debugPrint('[insertBasicHealthInfo] ===== ${data.length} PARAMS =====');
    // ignore: avoid_print
    data.forEach((k, v) => debugPrint('[insertBasicHealthInfo] Param: $k = $v'));
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      // ignore: avoid_print
      debugPrint('[insertBasicHealthInfo] response status=${response.statusCode} body=${response.body}');
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      final status = decoded['status'] as String? ?? '';
      final message = decoded['message'] as String? ?? '';
      if (status == 'Success') {
        callback(null, message, true);
      } else {
        callback(null, message, false);
      }
    } catch (e) {
      // ignore: avoid_print
      debugPrint('[insertBasicHealthInfo] error: $e');
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getAllDistrictListForPhyExamAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAllDistrictListForPhyExam;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      AllDistrictListForPhyExamResponse person =
          AllDistrictListForPhyExamResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getD2DPhysicalExamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DPhysicalExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      D2DPhysicalExamDetailsResponse person =
          D2DPhysicalExamDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getD2DTeamWisePhyExamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DTeamWisePhyExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      D2DTeamWisePhyExamDetailsResponse person =
          D2DTeamWisePhyExamDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCallToTeamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DTeamWisePhyExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      TeamWisePhysicalExamDetailsResponse person =
          TeamWisePhysicalExamDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getD2DPhysicalExaminationDetailsPatientListAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AttendancesListUsingSiteDetailsIDResponse person =
          AttendancesListUsingSiteDetailsIDResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertCallDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCallDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      InsertDetailsResponse person = InsertDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertBeneficiaryCallingLogV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertBeneficiaryCallingLogV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      InsertBeneficiaryCallingLogResponse person =
          InsertBeneficiaryCallingLogResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> addCallDataAPI(
    Map<String, dynamic> data,
    String method,
    dynamic callback,
  ) async {
    final url = Uri.parse('$kTwentyFourBySeven$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      Is24By7IsAccountCreatedResponse person =
          Is24By7IsAccountCreatedResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUserCreatedBy24By7API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetIs24By7IsAccountCreatedFlag;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      Is24By7IsAccountCreatedResponse person =
          Is24By7IsAccountCreatedResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getT2TCallingAPIDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCallingAPIDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      T2TCallingAPIDetailsResponse person =
          T2TCallingAPIDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getOrganisationWiseAPIKeyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetOrganisationWiseAPIKey;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> apiKeyForMyoperator(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetOrganisationWiseAPIKeyV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      GetMyOperatorResponse person = GetMyOperatorResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getOTPForLoginAPI(
    Map<String, dynamic> data,
    Function(
      OrganisationWiseAPIKeyResponse? response,
      String error,
      bool success,
    )
    callback,
  ) async {
    String method = APIConstants.kGetOTPForLogin;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> verifyOTPMedicineDeliveryAPI(
    Map<String, dynamic> data,
    Function(
      OrganisationWiseAPIKeyResponse? response,
      String error,
      bool success,
    )
    callback,
  ) async {
    String method = APIConstants.kVerifyOTPForLogin;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getUSERAndroidIDAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kGetUSERAndroidID;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserAndroidIDResponse person = UserAndroidIDResponse.fromJson(
        json.decode(response.body),
      );

      // if (person.status == 'Success') {
      //   callback(person, "", true);
      // } else {
      callback(person, person.message ?? "", true);
      // }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> saveAndroidIDAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kInsertUSERAndroidID;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserAndroidIDResponse person = UserAndroidIDResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> saveAndroidTokenAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kSaveAndroidToken;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      UserAndroidIDResponse person = UserAndroidIDResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMedicineDeliveryChallanList(
    dynamic callback,
    String type,
    String fromDate,
    String toDate,
    String distlgdCode,
    String tallgdCode,
    String labCode,
    String teamId,
    String userId,
  ) async {
    final method = APIConstants.kGetBeneficiaryListForMedicalDeliveryV1;
    final url = Uri.parse('$kD2DBaseURL$method');

    // Build parameters as query or body depending on API implementation
    final body = {
      "type": type,
      "Fromdate": fromDate,
      "todate": toDate,
      "DISTLGDCODE": distlgdCode,
      "TALLGDCODE": tallgdCode,
      "labcode": labCode,
      "teamid": teamId,
      "UserID": userId,
    };

    try {
      debugPrint("Request URL: $url");
      debugPrint("Request Body: ${json.encode(body)}");

      final response = await _ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: body, // Automatically encoded as form data
      );

      final result = json.decode(response.body);
      debugPrint("Response: $result");

      PostCampBeneficiaryListResponse postCampBenList =
          PostCampBeneficiaryListResponse.fromJson(result);

      // More tolerant success check
      final status = postCampBenList.status?.trim().toLowerCase();
      final message = postCampBenList.message?.trim().toLowerCase();
      final isSuccess = status == 'success' || message!.contains('success');

      if (isSuccess) {
        callback(postCampBenList, "", true);
      } else {
        callback(
          postCampBenList,
          postCampBenList.message!.isEmpty
              ? "Unknown error"
              : postCampBenList.message,
          false,
        );
      }
    } catch (e) {
      debugPrint("Exception: $e");
      callback(null, "Exception occurred: $e", false);
    }
  }

  Future<void> getMedicineDeliveryByBarcodeAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method =
        APIConstants.kGetBeneficiaryListForMedicalDeliveryBarcodeScannerV1;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: data,
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final result = json.decode(response.body);
      PostCampBeneficiaryListResponse res =
          PostCampBeneficiaryListResponse.fromJson(result);
      if (res.status?.toLowerCase() == 'success') {
        callback(res, "", true);
      } else {
        callback(res, "Delivery challan number not found", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDistrictByUserIDAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDistrictByUserID;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      DistrictResponse person = DistrictResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAssignmentRemarksAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTAssignmentRemarks;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      AssignmentRemarksResponse person = AssignmentRemarksResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPostCampDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetT2TCTBeneficiaryDetailsforDistCoordinatorV4;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      T2TCTBeneficiaryDetailsResponse person =
          T2TCTBeneficiaryDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDepartmentTypeAPI(dynamic callback) async {
    String method = APIConstants.kGetDepartmentType;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.get(
        url,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      DepartmentTypeResponse person = DepartmentTypeResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDependentInfoAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTBeneficiaryDetailsforAssignTeamid;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      BeneficiaryDetailsforAssignTeamidDetailsResponse person =
          BeneficiaryDetailsforAssignTeamidDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getT2TTeamDetailsByPincodeAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TTeamDetailsByPincode;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      SelectedTeamsDataListResponse person =
          SelectedTeamsDataListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getT2TCTUserDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTUserDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      T2TCTUserDetailsResponse person = T2TCTUserDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTestInfoAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      ConfirmatoryTestsScreeningResponse person =
          ConfirmatoryTestsScreeningResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTestInfoCount(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      ConfirmatoryTestsScreeningTubeResponse person =
          ConfirmatoryTestsScreeningTubeResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  // remark != 1: form-encoded submit (no photos)
  Future<void> insertT2TBarcodeCollectionAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    const method = "InsertT2TBarcodeCollectionDetails_V2";
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body) as Map<String, dynamic>;
      final status = decoded['status'] ?? '';
      final message = decoded['message'] ?? '';
      if (status.toString().toLowerCase() == 'success') {
        callback(decoded, "", true);
      } else {
        callback(decoded, message.toString(), false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  // remark == 1: multipart submit (with patient photo + consent photo)
  Future<void> insertT2TBarcodeCollectionWithConsentAPI(
    Map<String, String> fields,
    String? patientPhotoPath,
    String? consentPhotoPath,
    dynamic callback,
  ) async {
    final uri = Uri.parse(kCTSampleCollectionConsentHandler);
    try {
      final request = http.MultipartRequest('POST', uri);
      request.fields.addAll(fields);
      if (patientPhotoPath != null && patientPhotoPath.isNotEmpty) {
        final ts = DateTime.now().millisecondsSinceEpoch;
        request.files.add(
          await http.MultipartFile.fromPath(
            'BarcodeImagePath',
            patientPhotoPath,
            filename: '${ts}_PR.jpg',
          ),
        );
      }
      if (consentPhotoPath != null && consentPhotoPath.isNotEmpty) {
        final ts = DateTime.now().millisecondsSinceEpoch;
        request.files.add(
          await http.MultipartFile.fromPath(
            'ConsentPath',
            consentPhotoPath,
            filename: '${ts}_CF.jpg',
          ),
        );
      }
      debugPrint('$uri');
      debugPrint('$fields');
      debugPrint('BarcodeImagePath: $patientPhotoPath');
      debugPrint('ConsentPath: $consentPhotoPath');
      final streamed = await request.send();
      final body = await streamed.stream.bytesToString();
      debugPrint(body);
      final decoded = json.decode(body) as Map<String, dynamic>;
      final status = decoded['status'] ?? '';
      final message = decoded['message'] ?? '';
      if (status.toString().toLowerCase() == 'success') {
        callback(decoded, "", true);
      } else {
        callback(decoded, message.toString(), false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertT2TCTTeamandBeneficiaryMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertT2TCTTeamandBeneficiaryMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      String body = response.body;
      debugPrint(body);
      debugPrint('${json.decode(response.body)}');
      T2TCTTeamandBeneficiaryResponse person =
          T2TCTTeamandBeneficiaryResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTalukaPacketReciveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabByUserID;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      LabByUserIDResponse person = LabByUserIDResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDataForPacketReceiveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketReceive;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      DataForPacketReceiveResponse person =
          DataForPacketReceiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getInsertPacketReceiveDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketReceiveDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      DataForPacketReceiveResponse person =
          DataForPacketReceiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPacketCollectionDataAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPacketCollectionData;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketCollectionResponse person = PacketCollectionResponse.fromJson(
        json.decode(response.body),
      );

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPacketCollectionDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketCollectionDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMedicineReturnAcceptInLabListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBenfListForMedicalReturnAcceptInLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketReturnResponse person = PacketReturnResponse.fromJson(
        json.decode(response.body),
      );

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMedicineReturnToPharmacyListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBenfListForMedicineReturnToPharmacy;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketReturnResponse person = PacketReturnResponse.fromJson(
        json.decode(response.body),
      );

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertMedicineReturnAcceptInLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMedicineReturnAcceptInLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertMedicineReturnToPharmacyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMedicineReturnToPharmacy;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getActiveInactiveD2DTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DTeamsCountV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      D2DTeamsCountResponse person = D2DTeamsCountResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getNotWorkingTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DNonWorkingTeamsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      D2DNonWorkingTeamsResponse person = D2DNonWorkingTeamsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getWorkingTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DWorkingTeamsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      D2DNonWorkingTeamsResponse person = D2DNonWorkingTeamsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamsCallingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamMembersDetailsForCalling;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      D2DTeamMemberDetailsResponse person =
          D2DTeamMemberDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamCampLabListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCampLabResponse person = TeamCampLabResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPhleboDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignedExternalResourceDetailsResponse person =
          AssignedExternalResourceDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDeopDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignedExternalResourceDetailsResponse person =
          AssignedExternalResourceDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignedExternalResourceDetailsResponse person =
          AssignedExternalResourceDetailsResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedTeamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCampDetailsListResponse person = TeamCampDetailsListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> removeTeamMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kRemoveTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCampDetailsListResponse person = TeamCampDetailsListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getFlexiPhleboDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCampDetailsListResponse person = TeamCampDetailsListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getFlexiDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamCampDetailsListResponse person = TeamCampDetailsListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMMUDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsMMUDoctorListResponse person = TeamsMMUDoctorListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getTeamsCampTypeWiseListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamsCampTypeWiseRegularCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsCampTypeWiseResponse person = TeamsCampTypeWiseResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getAssignResourcesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDesignationsForCampCreationOnlyoctor;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignResourcesResponse person = AssignResourcesResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> gtTeamDetailsListForAssignAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedTeamDetailsFlexi;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamDetailsListForAssignResponse person =
          TeamDetailsListForAssignResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getPhleboListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignation;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsDoctorListResponse person = TeamsDoctorListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDeOpListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignation;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsDoctorListResponse person = TeamsDoctorListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDoctorListClusterAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignationCluster;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsDoctorListResponse person = TeamsDoctorListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getMMUDoctorListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignationMMU;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      TeamsDoctorListResponse person = TeamsDoctorListResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertTeamCampMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignResourcesResponse person = AssignResourcesResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getSelectTeamsCampTypeWiseListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      AssignResourcesResponse person = AssignResourcesResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getConductedCamp(dynamic callback) async {
    final method =
        APIConstants.getLandingPageCountsDisplayforFinancialYearForAllSubOrg;
    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.get(url);
      final body = json.decode(response.body);
      debugPrint('$url');
      debugPrint(body);

      final campsResponse = ConductedCampsResponse.fromJson(body);

      // Be robust to casing / alternative fields
      final status = campsResponse.status.trim().toLowerCase();
      final message = campsResponse.message.trim().toLowerCase();
      final isSuccess = status == 'success' || message.contains('success');

      if (isSuccess) {
        callback(campsResponse, "", true);
      } else {
        callback(
          campsResponse,
          campsResponse.message.isEmpty
              ? 'Unknown error'
              : campsResponse.message,
          false,
        );
      }
    } catch (e) {
      callback(null, "Exceptions: $e", false);
    }
  }

  Future<void> getTodaysPatent(dynamic callback, String date) async {
    final method = APIConstants.getTodaysPatientCount;
    final url = Uri.parse('$kD2DBaseURL$method?Date=$date');

    try {
      final response = await _ioClient.get(url);
      final body = json.decode(response.body);
      debugPrint('$url');
      debugPrint(body);

      final todaysPatientsResponse = TodaysPatientsResponse.fromJson(body);

      // More tolerant success check
      final status = todaysPatientsResponse.status.trim().toLowerCase();
      final message = todaysPatientsResponse.message.trim().toLowerCase();
      final isSuccess = status == 'success' || message.contains('success');

      if (isSuccess) {
        callback(todaysPatientsResponse, "", true);
      } else {
        callback(
          todaysPatientsResponse,
          todaysPatientsResponse.message.isEmpty
              ? "Unknown error"
              : todaysPatientsResponse.message,
          false,
        );
      }
    } catch (e) {
      callback(null, "Exceptions: $e", false);
    }
  }

  Future<void> getHomeAndHubProcessedCount(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.getTotalAndTodaysBeneficiaryCountWithProcessCount;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      HomeAndHubProcessingModel person = HomeAndHubProcessingModel.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getHubAndHomelabDashboardAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetHubandHomelabDashboard;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      HubHomelabDashboardResponse person = HubHomelabDashboardResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getHomeLabPendingTableData(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.getSampleProcessingDashboard;

    final url = Uri.parse('$kD2DBaseURL$method');

    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      //campid
      debugPrint(response.body);
      HomeLabPendingCountTableModel person =
          HomeLabPendingCountTableModel.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      debugPrint(e.toString());
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDoctorListCSCCampAvailableAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDoctorListCSCCampAvailaible;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      GetDocListD2DResponse person = GetDocListD2DResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertCscDoctorAvailabilityStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCscDoctorAvailaibilityStatus;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      InsertDetailsResponse person = InsertDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getDataForPacketAcceptAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketAccept;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPacketAcceptDetailsManuallyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAcceptDetailsManually;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertPacketAcceptDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAcceptDetails;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint('${json.decode(response.body)}');
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  // â”€â”€ Medicine Delivery acknowledgement APIs â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> sendOTPForMedicineDeliveryAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kGetOTPForMedicineDeliveryOrgOption;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> verifyOTPForMedicineDeliveryAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kVerifyOTPMedicineDelivery;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getMedicineDeliveryStatusListAPI(dynamic callback) async {
    final method = APIConstants.kBindMedicineDeliveryStatus;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getMedicineDeliveryRemarkListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kBindMedicineDeliveryRemark;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getMobileNumbersForDeliveryAPI(
    String regdNo,
    dynamic callback,
  ) async {
    final method = APIConstants.kGetMobileNoCTandMDOTP;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: {'RegdNo': regdNo},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getFaceDetectionFlagAPI(String userId, dynamic callback) async {
    final method = APIConstants.kGetFaceDetectionFlag;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: {'UserId': userId},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> submitMedicineDeliveryAckAPI({
    required Map<String, String> fields,
    File? beneficiaryPhotoFile,
    File? consentFormFile,
    File? deliveryChallanFile,
    required dynamic callback,
  }) async {
    MediaType _mediaTypeForPath(String path) {
      final ext = p.extension(path).toLowerCase();
      if (ext == '.png') return MediaType('image', 'png');
      if (ext == '.jpg' || ext == '.jpeg') return MediaType('image', 'jpeg');
      return MediaType('application', 'octet-stream');
    }

    final uri = Uri.parse(kMedicineDeliveryAckHandler);
    try {
      final request = http.MultipartRequest('POST', uri);
      fields.forEach((key, value) => request.fields[key] = value);

      if (beneficiaryPhotoFile != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'Beneficiary_photoPath',
            beneficiaryPhotoFile.path,
            contentType: _mediaTypeForPath(beneficiaryPhotoFile.path),
          ),
        );
      }
      if (consentFormFile != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'Consent_Form_PhotoPath',
            consentFormFile.path,
            contentType: _mediaTypeForPath(consentFormFile.path),
          ),
        );
      }
      if (deliveryChallanFile != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'DeliveryChallan_PhotoPath',
            deliveryChallanFile.path,
            contentType: _mediaTypeForPath(deliveryChallanFile.path),
          ),
        );
      }

      final streamed = await _ioClient.send(request);
      final response = await http.Response.fromStream(streamed);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  // â”€â”€â”€ Team Photos â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  Future<void> getUserWiseCampListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse('$kD2DBaseURL${APIConstants.kGetUserWiseCampList}');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getTeamMembersAttendanceDetailsCampIDWiseAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(
      '$kD2DBaseURL${APIConstants.kGetTeamMembersAttendanceDetailsCampIDWise}',
    );
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getCampAttendanceImagesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(
      '$kD2DBaseURL${APIConstants.kGetCampAttendanceImages}',
    );
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> uploadCampAttendancePhotoAPI({
    required Map<String, String> fields,
    required File photoFile,
    required String statusId,
    required dynamic callback,
  }) async {
    final uri = Uri.parse(kCampAttendancePhotoHandler);
    try {
      final request = http.MultipartRequest('POST', uri);
      fields.forEach((key, value) => request.fields[key] = value);
      request.fields['StatusID'] = statusId;

      final fieldName = statusId == '1' ? 'InImagePath' : 'OutImagePath';
      request.files.add(
        await http.MultipartFile.fromPath(
          fieldName,
          photoFile.path,
          contentType: MediaType('image', 'jpeg'),
        ),
      );

      final streamed = await _ioClient.send(request);
      final response = await http.Response.fromStream(streamed);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status.toLowerCase() == 'success') {
        callback(decoded, '', true);
      } else {
        callback(decoded, message, false);
      }
    } catch (e) {
      callback(null, 'Exception: $e', false);
    }
  }

  Future<void> getCTConfirmatoryListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV2;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      CTConfirmatoryListModel person = CTConfirmatoryListModel.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getCTAppointmentListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV2;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      CTAppointmentBeneficiaryModel person =
          CTAppointmentBeneficiaryModel.fromJson(json.decode(response.body));
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> updateCTAppointmentDateAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateT2TCTAppointmentDate;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint('$data');
      debugPrint(response.body);
      CTAppointmentUpdateResponse person = CTAppointmentUpdateResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getBeneficiaryListByRegIDAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryListByRegID;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint(response.body);
      BeneficiaryListByRegIDResponse person =
          BeneficiaryListByRegIDResponse.fromJson(json.decode(response.body));
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> getD2DCampMappedDoctorListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DCampMappedDoctorList;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      final response = await _ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint(response.body);
      D2DCampMappedDoctorListResponse person =
          D2DCampMappedDoctorListResponse.fromJson(json.decode(response.body));
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Exception: $e", false);
    }
  }

  Future<void> insertBeneficiaryDoctorMappingAPI(
    List<Map<String, dynamic>> dataList,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertBeneficiaryDoctorMapping;
    final url = Uri.parse('$kD2DBaseURL$method');
    try {
      // Native sends JSON array as URL-encoded form parameter "MapData"
      final mapDataValue = json.encode(dataList);
      final response = await _ioClient.post(
        url,
        body: {"MapData": mapDataValue},
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      debugPrint('$url');
      debugPrint(response.body);
      final decoded = json.decode(response.body);
      final status = decoded['status']?.toString() ?? '';
      final message = decoded['message']?.toString() ?? '';
      if (status == 'Success') {
        callback(true, "");
      } else {
        callback(false, message);
      }
    } catch (e) {
      callback(false, "Exception: $e");
    }
  }
}
