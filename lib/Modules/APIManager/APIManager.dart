// ignore_for_file: depend_on_referenced_packages, file_names, avoid_print

import 'dart:convert';
import 'dart:io';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
// import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamDetailsResponse/InsertDetailsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
// import 'package:s2toperational/Modules/Json_Class/Is24By7IsAccountCreatedResponse/GetMyOpratorResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/MonthsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/PostCampBeneficiaryListResponse/PostCampBeneficiaryListResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/YearsResponse.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import '../../Screens/AdminDashboard/Model/HomeAndHubProcessingModel.dart';
import '../../Screens/d2d_physical_examination/model/AttendancesListUsingSiteDetailsIDResponse.dart';
import '../../Screens/d2d_physical_examination/model/D2DPhysicalExamDetailsResponse.dart';
import '../../Screens/d2d_physical_examination/model/GetMyOpratorResponse.dart';
import '../../Screens/d2d_physical_examination/model/InsertDetailsResponse.dart';
import '../../Screens/d2d_physical_examination/model/Is24By7IsAccountCreatedResponse.dart';
import '../../Screens/d2d_physical_examination/model/T2TCallingAPIDetailsResponse.dart';
import '../../Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../Json_Class/HubHomelabDashboardResponse/HubHomelabDashboardResponse.dart';
import '../../Screens/AdminDashboard/Model/HomeLabPendingCountTableModel.dart';
import '../Enums/Enums.dart';
import '../Json_Class/AcknowledgementPatientListResponse/AcknowledgementPatientListResponse.dart';
import '../Json_Class/AdminDashboard/ConductedCampsTotals.dart';
import '../Json_Class/AdminDashboard/TodaysPatientsResponse.dart';
import '../Json_Class/AdvadetailsNewVersionV2Response/AdvadetailsNewVersionV2Response.dart';
import '../Json_Class/AdvancesRequestDetailsShowResponse/AdvancesRequestDetailsShowResponse.dart';
import '../../Screens/d2d_physical_examination/model/AllDistrictListForPhyExamResponse.dart';
import '../Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import '../Json_Class/AssignResourcesResponse/AssignResourcesResponse.dart';
import '../Json_Class/AssignedExternalResourceDetailsResponse/AssignedExternalResourceDetailsResponse.dart';
import '../Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
// import '../Json_Class/AttendancesListUsingSiteDetailsIDResponse/AttendancesListUsingSiteDetailsIDResponse.dart';
import '../Json_Class/AudioScreeningDetailsResponse/AudioScreeningDetailsResponse.dart';
import '../Json_Class/BeneficiariesDetailsResponse/BeneficiariesDetailsResponse.dart';
import '../Json_Class/BeneficiaryDependantDetailsResponse/BeneficiaryDependantDetailsResponse.dart';
import '../Json_Class/BeneficiaryDetailsforAssignTeamidDetailsResponse/BeneficiaryDetailsforAssignTeamidDetailsResponse.dart';
import '../Json_Class/BeneficiaryStatusAndDetailsResponse/BeneficiaryStatusAndDetailsResponse.dart';
import '../Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../Json_Class/BillSubmissionResponse/BillSubmissionResponse.dart';
import '../Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import '../Json_Class/CallStatusListResponse/CallStatusListResponse.dart';
import '../Json_Class/CampCloseCampDetailsResponse/CampCloseCampDetailsResponse.dart';
import '../Json_Class/CampCloseDetailsResponse/CampCloseDetailsResponse.dart';
import '../Json_Class/CampCountWithDayResponse/CampCountWithDayResponse.dart';
import '../Json_Class/CampDetailsResponse/CampDetailsResponse.dart';
import '../Json_Class/CampDetailsntApprovalResponse/CampDetailsntApprovalResponse.dart';
import '../Json_Class/CampDetailsonLabForDoorToDoorResponse/CampDetailsonLabForDoorToDoorResponse.dart';
import '../Json_Class/CampIdListResponse/CampIdListResponse.dart';
import '../Json_Class/CampListV3Response/CampListV3Response.dart';
import '../Json_Class/CampReadinessFormListResponse/CampReadinessFormListResponse.dart';
import '../Json_Class/CampReadinessFormSubmittResponse/CampReadinessFormSubmittResponse.dart';
import '../Json_Class/CampResourceAllocationResponse/CampResourceAllocationResponse.dart';
import '../Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import '../Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../Json_Class/CampWiseInvoiceDetailsResponse/CampWiseInvoiceDetailsResponse.dart';
import '../Json_Class/CompanyListResponse/CompanyListResponse.dart';
import '../Json_Class/ConfirmatoryTestsScreeningResponse/ConfirmatoryTestsScreeningResponse.dart';
import '../Json_Class/ConfirmatoryTestsScreeningTubeResponse/ConfirmatoryTestsScreeningTubeResponse.dart';
import '../Json_Class/ConsumableListDetailsResponse/ConsumableListDetailsResponse.dart';
import '../Json_Class/ConsumablesListResponse/ConsumablesListResponse.dart';
import '../Json_Class/D2DNonWorkingTeamsResponse/D2DNonWorkingTeamsResponse.dart';
// import '../Json_Class/D2DPhysicalExamDetailsResponse/D2DPhysicalExamDetailsResponse.dart';
import '../../Screens/d2d_physical_examination/model/D2DPhysicalExamninationDetailsResponse.dart';
import '../Json_Class/D2DTeamMemberDetailsResponse/D2DTeamMemberDetailsResponse.dart';
import '../../Screens/d2d_physical_examination/model/D2DTeamWisePhyExamDetailsResponse.dart';
import '../Json_Class/D2DTeamsCountResponse/D2DTeamsCountResponse.dart';
import '../Json_Class/DataForPacketReceiveResponse/DataForPacketReceiveResponse.dart';
import '../Json_Class/DevicesListResponse/DevicesListResponse.dart';
import '../Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
import '../Json_Class/ExpenseCampIDListV1Response/ExpenseCampIDListV1Response.dart';
import '../Json_Class/ExpenseHeadResponse/ExpenseHeadResponse.dart';
import '../Json_Class/GetCampAssignUserResponse/GetCampAssignUserResponse.dart';
import '../Json_Class/GetDocListD2DResponse/GetDocListD2DResponse.dart';
import '../Json_Class/HomeAndHubLabCampCreationResponse/HomeAndHubLabCampCreationResponse.dart';
import '../Json_Class/InitiatedByResponse/InitiatedByResponse.dart';
import '../../Screens/d2d_physical_examination/model/InsertBeneficiaryCallingLogResponse.dart';
// import '../Json_Class/Is24By7IsAccountCreatedResponse/Is24By7IsAccountCreatedResponse.dart';
import '../Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';
import '../Json_Class/LabDataResponse/LabDataResponse.dart';
import '../Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import '../Json_Class/LungFunctionTestDetailsResponse/LungFunctionTestDetailsResponse.dart';
import '../Json_Class/MonthWiseInvoiceResponse/MonthWiseInvoiceResponse.dart';
import '../Json_Class/MonthlySurveySiteResponse/MonthlySurveySiteResponse.dart';
import '../../Screens/d2d_physical_examination/model/OrganisationWiseAPIKeyResponse.dart';
import '../Json_Class/OtherReasonForPatientRejectionResponse/OtherReasonForPatientRejectionResponse.dart';
import '../Json_Class/PacketAcceptDataResponse/PacketAcceptDataResponse.dart';
import '../Json_Class/PacketCollectionResponse/PacketCollectionResponse.dart';
import '../Json_Class/PacketReturnResponse/PacketReturnResponse.dart';
import '../Json_Class/PatientCheckupAnalysisReportResponse/PatientCheckupAnalysisReportResponse.dart';
import '../Json_Class/PatientListReAllocationforMedicineDeliveryResponse/PatientListReAllocationforMedicineDeliveryResponse.dart';
import '../Json_Class/PatientStatusDetailsListResponse/PatientStatusDetailsListResponse.dart';
import '../Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
import '../Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
import '../Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import '../Json_Class/RecollectionBeneficiaryToTeamResponse/RecollectionBeneficiaryToTeamResponse.dart';
import '../Json_Class/RemarkListResponse/RemarkListResponse.dart';
import '../Json_Class/ReportDeliveryExecutiveResponse/ReportDeliveryExecutiveResponse.dart';
import '../Json_Class/ResourceListResponse/ResourceListResponse.dart';
import '../Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../Json_Class/ScreenedDependentCountResponse/ScreenedDependentCountResponse.dart';
import '../Json_Class/ScreeningTestCampCreationResponse/ScreeningTestCampCreationResponse.dart';
import '../Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
import '../Json_Class/SubDevicesListResponse/SubDevicesListResponse.dart';
import '../Json_Class/SubExpenseHeadsResponse/SubExpenseHeadsResponse.dart';
import '../Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import '../Json_Class/SubResourceListResponse/SubResourceListResponse.dart';
import '../Json_Class/SubmitDeviceMappingResponse/SubmitDeviceMappingResponse.dart';
import '../Json_Class/T2TCTBeneficiaryDetailsResponse/T2TCTBeneficiaryDetailsResponse.dart';
import '../Json_Class/T2TCTTeamandBeneficiaryResponse/T2TCTTeamandBeneficiaryResponse.dart';
import '../Json_Class/T2TCTUserDetailsResponse/T2TCTUserDetailsResponse.dart';
// import '../Json_Class/T2TCallingAPIDetailsResponse/T2TCallingAPIDetailsResponse.dart';
import '../Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';
import '../Json_Class/TeamCCResponse/TeamCCResponse.dart';
import '../Json_Class/TeamCampDetailsListResponse/TeamCampDetailsListResponse.dart';
import '../Json_Class/TeamCampLabResponse/TeamCampLabResponse.dart';
import '../Json_Class/TeamDetailsListForAssignResponse/TeamDetailsListForAssignResponse.dart';
import '../Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
// import '../Json_Class/TeamNumberByCampIdAndUserIdListResponse/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../Screens/d2d_physical_examination/model/TeamWisePhysicalExamDetailsResponse.dart';
import '../Json_Class/TeamsCampTypeWiseResponse/TeamsCampTypeWiseResponse.dart';
import '../Json_Class/TeamsDoctorListResponse/TeamsDoctorListResponse.dart';
import '../Json_Class/TeamsMMUDoctorListResponse/TeamsMMUDoctorListResponse.dart';
import '../Json_Class/TestListForRejectResponse/TestListForRejectResponse.dart';
import '../Json_Class/TotalcampAndTotalZeroCampResponse/TotalcampAndTotalZeroCampResponse.dart';
import '../Json_Class/UpdateSubResourceListResponse/UpdateSubResourceListResponse.dart';
import '../Json_Class/UserAndroidIDResponse/UserAndroidIDResponse.dart';
import '../../Screens/user_attendance/Model/user_attandance_response.dart';
import '../Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import '../Json_Class/UserCampMappingAndAttendanceDataResponse/UserCampMappingAndAttendanceDataResponse.dart';
import '../Json_Class/UserCampMappingAndAttendanceStatusResponse/UserCampMappingAndAttendanceStatusResponse.dart';
import '../Json_Class/UserCampMappingStatusResponse/UserCampMappingStatusResponse.dart';
import '../Json_Class/UserInvoicePaymentDetailsResponse/UserInvoicePaymentDetailsResponse.dart';
import '../Json_Class/VerificationRemarkResponse/VerificationRemarkResponse.dart';
import '../Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import '../Json_Class/VisionScreeningDetailsResponse/VisionScreeningDetailsResponse.dart';
import '../constants/APIConstants.dart';
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
  static String kExpenseBillDetailsHandler = "";
  static String kUploadPostCampFileHandler = "";
  static String kChangeBeneficiaryAndCardImage = "";
  static String kMedicineDeliveryAckHandler = "";
  static String kMediaBaseURL = "";
  static String kt24By7 = "";
  static String kLiverScann = "";
  static String kTreatmentCount = "";
  static String kTwentyFourBySeven =
      "https://app.office24by7.com/v1/common/API/";
  static String kVodaphone =
      "https://cts.myvi.in:8443/Cpaas/api/v1/clicktocall/";
  static String kMyOperator = "https://obd-api.myoperator.co/";
  APIMode apiMode = APIMode.Beta;

  void setAPIEnvironment() {
    switch (apiMode) {
      case APIMode.Live:
        kD2DBaseURL = "https://mcwwb.janarogyaseva.in/webservices/d2d_V2.asmx/";
        kCallingBaseURL =
            "https://mcwwb.janarogyaseva.in/webservices/BeneficiaryCalling.asmx/";
        kConstructionWorkerBaseURL =
            "https://mcwwb.janarogyaseva.in/webservices/ConstructionWorker_V2.asmx/";
        kExpenseBillDetailsHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/ExpenseBillDetailsHandler.ashx";
        kUploadPostCampFileHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/MultipleExpenseBillUploader.ashx";
        kChangeBeneficiaryAndCardImage =
            "https://mcwwb.janarogyaseva.in/webservices/handler/ChangeBeneficiaryAndCardImage_InCampTest.ashx";
        kMedicineDeliveryAckHandler =
            "https://mcwwb.janarogyaseva.in/webservices/handler/CW_MedicineDelivery_V1_DC.ashx";
        kMediaBaseURL = "https://mcwwb.janarogyaseva.in/MCWWBDOCS_LIVE";
        kt24By7 = "https://app.office24by7.com/v1/common/API/";
        kLiverScann =
            "https://reports.myhindlab.com/ReportsMCWLive/API/Liverscan/";
        kTreatmentCount =
            "http://103.251.94.57:8080/disha-t2t-Apis/api/access/master/countdata/";
      case APIMode.Beta:
        kD2DBaseURL =
            "https://testmcwwb.myhindlab.com/webservices/d2d_V2.asmx/";
        kCallingBaseURL =
            "https://testmcwwb.myhindlab.com/webservices/BeneficiaryCalling.asmx/";
        kConstructionWorkerBaseURL =
            "https://testmcwwb.myhindlab.com/webservices/ConstructionWorker_V2.asmx/";
        kExpenseBillDetailsHandler =
            "https://testmcwwb.myhindlab.com/webservices/handler/ExpenseBillDetailsHandler.ashx";
        kUploadPostCampFileHandler =
            "https://testmcwwb.myhindlab.com/webservices/handler/MultipleExpenseBillUploader.ashx";
        kChangeBeneficiaryAndCardImage =
            "https://testmcwwb.myhindlab.com/webservices/handler/ChangeBeneficiaryAndCardImage_InCampTest.ashx";
        kMedicineDeliveryAckHandler =
            "https://testmcwwb.myhindlab.com/webservices/handler/CW_MedicineDelivery_V1_DC.ashx";
        kMediaBaseURL = "https://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS";
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'aplicationId': applicationId, 'versionname': serverVersion},
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print('APKDownloader url: $url');
      print('APKDownloader response: ${response.body}');
      final decoded = json.decode(response.body);
      final status = decoded['status'] ?? '';
      final message = decoded['message'] ?? '';
      onResult(status, message);
    } catch (e) {
      print('APKDownloader error: $e');
      onResult('', '');
    }
  }

  Future<void> getLoginAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kUserLogin;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeNonD2DRAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeNonD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeFlexiAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeFlexi;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeMMUAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeMMU;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeNonD2DAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeNonD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeD2DAPI(dynamic callback) async {
    String method = APIConstants.kCampTypeD2D;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getInitiatedByListForCampAPI(dynamic callback) async {
    String method = APIConstants.kGetInitiatedByListForCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTalukaAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetAllTalukaList;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getLandingLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabDistrictWiseV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      LandingLabCampCreationResponse person =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getHomeLabHubLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetHomeAndHubLabNamesOfLandingLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getScreeningTestAPI(dynamic callback) async {
    String method = APIConstants.kTestList;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(url);
      print(url);

      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> createCampCreationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCampCreationV3;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      LandingLabCampCreationResponse person =
          LandingLabCampCreationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampIDAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetCampListV3;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDevicesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDeviceListForCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(response.body);
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getSubDevicesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetSUBDeviceListNew;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(response.body);
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getConsumablesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListForExpectedBenificiary;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getResourceAPI(dynamic callback) async {
    String method = APIConstants.kGetTestListForMapResourceDesgForD2d;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getSubResourceAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTestidFromResourcelistNewForPatnerResorces;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> submitDeviceAllocationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInserCampCreationD2DWithoutApproval;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampListCampReadinessAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampListCampReadiness;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamNumberByCampIdAndUSerIdAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamNumberByCampIdAndUSerId;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampReadinessFormItemsAPI(
    int campID,
    int teamId,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampReadinessFormItems;

    final url = Uri.parse('$kD2DBaseURL$method?CampID=$campID&TeamId=$teamId');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(json.decode(response.body));
      CampReadinessFormListResponse person =
          CampReadinessFormListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertCampReadinessFormDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCampReadinessFormDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(json.decode(response.body));
      CampReadinessFormSubmittResponse person =
          CampReadinessFormSubmittResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAdvadetailsNewVersionV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAdvadetailsNewVersionV2;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      AdvadetailsNewVersionV2Response person =
          AdvadetailsNewVersionV2Response.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getExpenseHeadAPI(dynamic callback) async {
    String method = APIConstants.kGetExpensesMasterData;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getSubExpenseHeadAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetSubExpensesMasterDataV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);

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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserAttendanceAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetuserAttendance;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getInsertUserAttendancAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertUserAttendance;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> saveBillDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertAdvancesRequestNewChangesV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> uploadBillsAPI(
    Map<String, dynamic> data,
    File selectedFile,
    String fileType,
    dynamic callback,
  ) async {
    final url = Uri.parse(kExpenseBillDetailsHandler);

    final IOClient ioClient = getInstanceOfIoClient();

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
      final streamedResponse = await ioClient.send(request);
      final response = await http.Response.fromStream(streamedResponse);

      print(response.body);
      if (response.statusCode == 200) {
        BillSubmissionResponse person = BillSubmissionResponse.fromJson(
          json.decode(response.body),
        );
        print("Image uploaded successfully: ${response.body}");
        if (person.status == 'Success') {
          callback(person, "", true);
        } else {
          callback(person, person.message, false);
        }
      } else {
        print("Upload failed: ${response.statusCode} - ${response.body}");
        callback(null, "Image not uploaded successfully", false);
      }
    } catch (e) {
      print("Error uploading image: $e");
      callback(null, e.toString(), false);
    } finally {
      ioClient.close();
    }
  }

  Future<void> getExpenseCampIDListV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetExpenseCampIDListV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertMultipleCampIDV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMultipleCampIDV2;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
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
        print("✅ Uploaded ${file.path}");
        successCount++;
      } else {
        print("❌ Failed ${file.path}: ${response.statusCode}");
        failCount++;
      }
    }
    int total = (successCount + failCount);
    print("successCount $successCount");
    print("failCount $failCount");
    print("total $total");
    if (files.length == total) {
      if (successCount == files.length) {
        callback(null, "", true);
      } else {
        callback(null, "", false);
      }
    }

    print('Finished: $successCount success, $failCount failed');
  }

  Future<http.Response> uploadFile(
    File file,
    Map<String, String> params,
  ) async {
    final uri = Uri.parse(kUploadPostCampFileHandler);

    final IOClient ioClient = getInstanceOfIoClient();

    final request = http.MultipartRequest("POST", uri);

    params.forEach((key, value) {
      request.fields[key] = value.toString();
    });

    MediaType contentType = FormatterManager.getMediaTypeFromFile(file);
    print("uploadFile");
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

    final streamedResponse = await ioClient.send(request);
    return await http.Response.fromStream(streamedResponse);
  }

  Future<void> getSubOrganizationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBindOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBindDivision(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kBindDivision;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBindDistrictAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kBindDistrict;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampTypeAndCatagoryAPI(dynamic callback) async {
    String method = APIConstants.kGetCampTypeAndCatagory;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);

      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampCountWithDayAndMonthWiseWithCampTypeOrgAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetCampCountWithDayAndMonthWiseWithCampTypeOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTotalcampAndTotalBeneficiarywithZeroCampAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetTotalcampAndTotalBeneficiarywithZeroCampOrg;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserAttendanceDaysAPI(
    String urlStirng,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlStirng);

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getScreeningTestCampDetailsAPI(
    String urlStirng,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlStirng);

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBillSubmitdetailsShowAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBillSubmitdetailsShow;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getApprovedCampListDetailsForAppFlexiCampAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApprovedCampListDetailsForAppFlexiCampV1;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      ResourceReMappingCampResponse person =
          ResourceReMappingCampResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetUserCampMappingAndAttendanceStatusForRegularCamp;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      UserCampMappingStatusResponse person =
          UserCampMappingStatusResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampAssignUserListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampAssignUserList;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampDetailsForAppForIntApprovalAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsForAppForIntApproval;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      CampDetailsntApprovalResponse person =
          CampDetailsntApprovalResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getResourcesForApprovalAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampResourceDetailsForAppForIntApproval;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      CampResourceAllocationResponse person =
          CampResourceAllocationResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getApproveResourcelstForUpdateAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApproveResourcelstForUpdate;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> removeResourceAllcoationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kRemoveCampMappingResources;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> addResourceAllcoationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateCampMappingResources;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      UpdateSubResourceListResponse person =
          UpdateSubResourceListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getApprovedCampListDetailsForAppAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetApprovedCampListDetailsForApp;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      ResourceReMappingCampResponse person =
          ResourceReMappingCampResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampDetailsonLabForDoorToDoorV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsonLabForDoorToDoorV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserAttendancesUsingSitedetailsIDNewAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserAttendancesUsingSitedetailsIDNew;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAcknowledgementPatientListAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getRegiWorkerDetailsOncampIdAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRegiWorkerDetailsOncampIdInCampTest;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCAMPPatientCheckupAnalysisReportNewAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kCAMPPatientCheckupAnalysisReportV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getLungFunctionTestDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLungFunctionTestDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      LungFunctionTestDetailsResponse person =
          LungFunctionTestDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getVisionScreeningDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetVisionScreeningDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      VisionScreeningDetailsResponse person =
          VisionScreeningDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAudioScreeningDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAudioScreeningDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      AudioScreeningDetailsResponse person =
          AudioScreeningDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTestToRejectAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTestListForReject;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getOtherReasonForPatientRejectionAPI(dynamic callback) async {
    String method = APIConstants.kGetOtherReasonForPatientRejection;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPatientRejectionInCampInCampTestV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPatientRejectionInCampInCampTestV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCAMPPatientCheckupAnalysisReportAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kCAMPPatientCheckupAnalysisReportNew;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PatientStatusDetailsListResponse person =
          PatientStatusDetailsListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampIDWiseTeamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampIDWiseTeamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampDetailsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampDetailsCountRegularInCampTest;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      CampCloseCampDetailsResponse person =
          CampCloseCampDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampCloseDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampCloseDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      CampCloseDetailsResponse person = CampCloseDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getConsumableListDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      ConsumableListDetailsResponse person =
          ConsumableListDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
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

    final IOClient ioClient = getInstanceOfIoClient();

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
      final streamedResponse = await ioClient.send(request);
      final response = await http.Response.fromStream(streamedResponse);

      print(response.body);
      if (response.statusCode == 200) {
        BillSubmissionResponse person = BillSubmissionResponse.fromJson(
          json.decode(response.body),
        );
        print("Image uploaded successfully: ${response.body}");
        if (person.status == 'Success') {
          callback(person, "", true);
        } else {
          callback(person, person.message, false);
        }
      } else {
        print("Upload failed: ${response.statusCode} - ${response.body}");
        callback(null, "Image not uploaded successfully", false);
      }
    } catch (e) {
      print("Error uploading image: $e");
      callback(null, e.toString(), false);
    } finally {
      ioClient.close();
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusD2DAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetConsumableListDetails;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAppointmentStatusListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAppointmentStatusList;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamListCCAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamListCC;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAppointmentBeneficiariesMASListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAppoinmentDetailsV1MAS;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAppointmentBeneficiariesListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAppoinmentDetailsV1;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserMappedTalukaAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserMappedTaluka;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBarcodePostCampDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetPatientListReAllocationforMedicineDeliveryByPacketID;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPacketDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateReallocationMedicalDelivary;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getReportDeliveryExecutiveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetReportDeliveryExecutiveD2DTeam;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      ReportDeliveryExecutiveResponse person =
          ReportDeliveryExecutiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDataForPacketAssignmentAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketAssignment;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPacketAssignDetailsManuallyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAssignDetailsManually;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPhleboCallStatusListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPhleboCallStatusList;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBeneficiaryDataAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryAddressDetails;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      BeneficiariesDetailsResponse person =
          BeneficiariesDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDependentListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBeneficiaryDependantDetails;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getScreeningCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetScreenedDependentCount;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      ScreenedDependentCountResponse person =
          ScreenedDependentCountResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCallingRemarkV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCallingRemarkV1;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> updateAppointmentDetailsMobAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdateAppointmentDetailsMob;

    final url = Uri.parse('$kCallingBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getLabforD2DCampCoordinatorAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabforD2DCampCoordinator;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountForPageloadForTeamAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountTeamV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountForPageloadForTeamV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountTeamV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountForPageloadAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountForPageloadVaAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsCountV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountAPI(Map<String, dynamic> data, dynamic callback) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCountForTeamPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionBeneficiaryToTeam;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getRecollectionAssignmentRemarksAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionAssignmentRemarks;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getRecollectionBeneficiaryDashboardForMobAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionBeneficiaryDashboardForMob;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getBeneficiaryStatusAndDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetRecollectionBeneficiaryStatusandDetailsRemovePincodeV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getRecollectionTeamDetialsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetRecollectionTeamDetials;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      SelectedTeamsDataListResponse person =
          SelectedTeamsDataListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertAppointmentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kUpdaterecollectionAppointmentDate;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertRecollectionTeamandBeneficiaryMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertRecollectionTeamandBeneficiaryMapping;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserCampMappingAndAttendanceStatusReadinessAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetUserCampMappingAndAttendanceStatusReadiness;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getYearAPI(dynamic callback) async {
    String method = APIConstants.kGetYear;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      YearsResponse person = YearsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMonthsAPI(Map<String, dynamic> data, dynamic callback) async {
    String method = APIConstants.kGetMonth;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMonthWiseInvoiceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetMonthWiseInvoiceStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMonthWiseDoctorInvoiceStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetMonthWiseDoctorInvoiceStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserInvoicePaymentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserInvoicePaymentDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserDoctorInvoicePaymentDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserDoctorInvoicePaymentDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      UserInvoicePaymentDetailsResponse person =
          UserInvoicePaymentDetailsResponse.
          fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampWiseInvoiceDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampWiseInvoiceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCampWiseDoctorInvoiceDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetCampWiseDoctorInvoiceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getOTPForMedicineDeliveryOrgAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertInvoiceOTPDetailsOrg;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDoctorInvoiceOTPDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertDoctorInvoiceOTPDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPaidByCompanyListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPaidByCompanyList;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPaymentVerificationRemarkAPI(
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPaymentVerificationRemark;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.get(url);
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserAttendancesUsingSitedetailsIDAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      UserAttendancesUsingSitedetailsIDResponse person =
          UserAttendancesUsingSitedetailsIDResponse.fromJson(
            json.decode(response.body),
          );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertInvoicePaymentStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertInvoicePaymentStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertDoctorInvoicePaymentStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertDoctorInvoicePaymentStatus;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      CampWiseInvoiceDetailsResponse person =
          CampWiseInvoiceDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserDataforPhysicalExamninationAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetUserDataforPhysicalExamnination;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPhysicalExaminationForHSCCV1API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPhysicalExaminationForHSCCV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAllDistrictListForPhyExamAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAllDistrictListForPhyExam;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getD2DPhysicalExamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DPhysicalExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      D2DPhysicalExamDetailsResponse person =
          D2DPhysicalExamDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getD2DTeamWisePhyExamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DTeamWisePhyExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getCallToTeamDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetD2DTeamWisePhyExamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getD2DPhysicalExaminationDetailsPatientListAPI(
    String urlString,
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final url = Uri.parse(urlString);
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }


  Future<void> insertCallDetailsAPI(
      Map<String, dynamic> data,
      dynamic callback,
      ) async {
    String method = APIConstants.kInsertCallDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      InsertDetailsResponse person =
      InsertDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertBeneficiaryCallingLogV2API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertBeneficiaryCallingLogV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> addCallDataAPI(
    Map<String, dynamic> data,
    String method,
    dynamic callback,
  ) async {
    final url = Uri.parse('$kTwentyFourBySeven$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      Is24By7IsAccountCreatedResponse person =
          Is24By7IsAccountCreatedResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUserCreatedBy24By7API(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetIs24By7IsAccountCreatedFlag;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      Is24By7IsAccountCreatedResponse person =
          Is24By7IsAccountCreatedResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getT2TCallingAPIDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCallingAPIDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      T2TCallingAPIDetailsResponse person =
          T2TCallingAPIDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getOrganisationWiseAPIKeyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetOrganisationWiseAPIKey;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }


  Future<void> apiKeyForMyoperator(
      Map<String, dynamic> data,
      dynamic callback,
      ) async {
    String method = APIConstants.kGetOrganisationWiseAPIKeyV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      GetMyOperatorResponse person =
      GetMyOperatorResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      OrganisationWiseAPIKeyResponse person =
          OrganisationWiseAPIKeyResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getUSERAndroidIDAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kGetUSERAndroidID;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      UserAndroidIDResponse person = UserAndroidIDResponse.fromJson(
        json.decode(response.body),
      );

      // if (person.status == 'Success') {
      //   callback(person, "", true);
      // } else {
        callback(person, person.message ?? "", true);
      // }
    } catch (e) {

      callback(null, "Expections: $e", false);
    }
  }

  Future<void> saveAndroidIDAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kInsertUSERAndroidID;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> saveAndroidTokenAPI(
    Map<String, dynamic> data,
    Function(UserAndroidIDResponse? response, String error, bool success)
    callback,
  ) async {
    String method = APIConstants.kSaveAndroidToken;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
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

    final ioClient = getInstanceOfIoClient();

    try {
      print("Request URL: $url");
      print("Request Body: ${json.encode(body)}");

      final response = await ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: body, // Automatically encoded as form data
      );

      final result = json.decode(response.body);
      print("Response: $result");

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
      print("Exception: $e");
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
    final ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: data,
      );
      print(url);
      print(data);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAssignmentRemarksAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTAssignmentRemarks;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPostCampDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetT2TCTBeneficiaryDetailsforDistCoordinatorV3;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      T2TCTBeneficiaryDetailsResponse person =
          T2TCTBeneficiaryDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDependentInfoAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTBeneficiaryDetailsforAssignTeamid;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getT2TTeamDetailsByPincodeAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TTeamDetailsByPincode;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      SelectedTeamsDataListResponse person =
          SelectedTeamsDataListResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getT2TCTUserDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetT2TCTUserDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      T2TCTUserDetailsResponse person = T2TCTUserDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTestInfoAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTestInfoCount(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method =
        APIConstants.kGetConfirmatoryTestsScreeningAppointmentDetailsV1;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertT2TCTTeamandBeneficiaryMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertT2TCTTeamandBeneficiaryMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      String body = response.body;
      print(body);
      print(json.decode(response.body));
      T2TCTTeamandBeneficiaryResponse person =
          T2TCTTeamandBeneficiaryResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTalukaPacketReciveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLabByUserID;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDataForPacketReceiveAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketReceive;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      DataForPacketReceiveResponse person =
          DataForPacketReceiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getInsertPacketReceiveDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketReceiveDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      DataForPacketReceiveResponse person =
          DataForPacketReceiveResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPacketCollectionDataAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetPacketCollectionData;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketCollectionResponse person = PacketCollectionResponse.fromJson(
        json.decode(response.body),
      );

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPacketCollectionDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketCollectionDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMedicineReturnAcceptInLabListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBenfListForMedicalReturnAcceptInLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketReturnResponse person =
          PacketReturnResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMedicineReturnToPharmacyListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetBenfListForMedicineReturnToPharmacy;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketReturnResponse person =
          PacketReturnResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertMedicineReturnAcceptInLabAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMedicineReturnAcceptInLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertMedicineReturnToPharmacyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertMedicineReturnToPharmacy;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketCollectionInsertResponse person =
          PacketCollectionInsertResponse.fromJson(json.decode(response.body));

      if ((person.status ?? "").toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getActiveInactiveD2DTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DTeamsCountV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getNotWorkingTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DNonWorkingTeamsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getWorkingTeamsCountAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetActiveInactiveD2DWorkingTeamsV2;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamsCallingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamMembersDetailsForCalling;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      D2DTeamMemberDetailsResponse person =
          D2DTeamMemberDetailsResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamCampLabListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetLab;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPhleboDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDeopDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedTeamDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> removeTeamMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kRemoveTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getFlexiPhleboDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getFlexiDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMMUDoctorDetailsListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedExternalResourceDetails;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getTeamsCampTypeWiseListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetTeamsCampTypeWiseRegularCamp;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getAssignResourcesAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDesignationsForCampCreationOnlyoctor;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> gtTeamDetailsListForAssignAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetAssignedTeamDetailsFlexi;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      TeamDetailsListForAssignResponse person =
          TeamDetailsListForAssignResponse.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getPhleboListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignation;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDeOpListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignation;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getMMUDoctorListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetResourceFromDesignationMMU;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertTeamCampMappingAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getSelectTeamsCampTypeWiseListAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertTeamCampMapping;

    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
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
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getConductedCamp(dynamic callback) async {
    final method =
        APIConstants.getLandingPageCountsDisplayforFinancialYearForAllSubOrg;
    final url = Uri.parse('$kD2DBaseURL$method');
    final ioClient = getInstanceOfIoClient();

    try {
      final response = await ioClient.get(url);
      final body = json.decode(response.body);
      print(url);
      print(body);

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
    final ioClient = getInstanceOfIoClient();

    try {
      final response = await ioClient.get(url);
      final body = json.decode(response.body);
      print(url);
      print(body);

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

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
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
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getHubAndHomelabDashboardAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetHubandHomelabDashboard;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(response.body);
      HubHomelabDashboardResponse person = HubHomelabDashboardResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getHomeLabPendingTableData(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.getSampleProcessingDashboard;

    final url = Uri.parse('$kD2DBaseURL$method');

    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      //campid
      print(response.body);
      HomeLabPendingCountTableModel person =
          HomeLabPendingCountTableModel.fromJson(json.decode(response.body));

      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message, false);
      }
    } catch (e) {
      // Handle error
      print(e.toString());
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDoctorListCSCCampAvailableAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDoctorListCSCCampAvailaible;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      GetDocListD2DResponse person = GetDocListD2DResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertCscDoctorAvailabilityStatusAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertCscDoctorAvailaibilityStatus;

    final url = Uri.parse('$kConstructionWorkerBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      InsertDetailsResponse person = InsertDetailsResponse.fromJson(
        json.decode(response.body),
      );

      if (person.status?.toLowerCase() == 'success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> getDataForPacketAcceptAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kGetDataForPacketAccept;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPacketAcceptDetailsManuallyAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAcceptDetailsManually;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  Future<void> insertPacketAcceptDetailsAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    String method = APIConstants.kInsertPacketAcceptDetails;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      );
      print(url);
      print(data);
      print(json.decode(response.body));
      PacketAcceptDataResponse person = PacketAcceptDataResponse.fromJson(
        json.decode(response.body),
      );
      if (person.status == 'Success') {
        callback(person, "", true);
      } else {
        callback(person, person.message ?? "", false);
      }
    } catch (e) {
      callback(null, "Expections: $e", false);
    }
  }

  // ── Medicine Delivery Acknowledgement APIs ─────────────────────────────────

  Future<void> sendOTPForMedicineDeliveryAPI(
    Map<String, dynamic> data,
    dynamic callback,
  ) async {
    final method = APIConstants.kGetOTPForMedicineDeliveryOrgOption;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      print(url);
      print(data);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      print(url);
      print(data);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: {},
      );
      print(url);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: data,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      print(url);
      print(data);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'RegdNo': regdNo},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      print(url);
      print(response.body);
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

  Future<void> getFaceDetectionFlagAPI(
    String userId,
    dynamic callback,
  ) async {
    final method = APIConstants.kGetFaceDetectionFlag;
    final url = Uri.parse('$kD2DBaseURL$method');
    final IOClient ioClient = getInstanceOfIoClient();
    try {
      final response = await ioClient.post(
        url,
        body: {'UserId': userId},
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      );
      print(url);
      print(response.body);
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
    final IOClient ioClient = getInstanceOfIoClient();
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

      final streamed = await ioClient.send(request);
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
}
