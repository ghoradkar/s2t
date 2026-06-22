package com.myhindlab.abkat.utilities;

import com.myhindlab.abkat.BuildConfig;

public class ApplicationConstants {

    public static final String PREFER_NAME = "HealthCheckup";
    public static final String ApplicationID = "90";
    public static final int OKSUCCESS = 200;
    public static final String IS_USER_LOGIN = "IS_USER_LOGIN";
    public static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";
    public static final String KEY_LAST_LOGIN_DATE = "KEY_LAST_LOGIN_DATE";

    public static final String KEY_DEVICE_INFO = "KEY_DEVICE_INFO";

    public static final String mediProcsWebservice = BuildConfig.mediProcsBaseURl;

    public static final String FCM_TOKEN = "FCM_TOKEN";


    public static final String IS_HLL_USER = "IS_HLL_USER";
    public static final String KEY_LOGIN_INFO = "KEY_LOGIN_INFO";
    public static final String KEY_LOCATIONFLAG = "KEY_LOCATIONFLAG";
    public static final String KEY_STATICALVIEW = "KEY_STATICALVIEW";
    public static final String DEVICE_NAME = "DEVICE_NAME";
    public static final String DEVICE_ID = "DEVICE_ID";
    public static final String FORCE_LOGOUT = "FORCE_LOGOUT";

    /**
     * ABHA
     */


    public static final String ABDMFacilityId = BuildConfig.HIPID;
    public static final String ABDMFacilityName = BuildConfig.HIPName;

    public static final String sessions = "api/hiecm/gateway/v3/sessions";
    public static final String generateOtp = "v1/registration/aadhaar/generateOtp";
    public static final String generateMobileOtp = "v1/registration/aadhaar/generateMobileOTP";
    public static final String verifyOTP = "v1/registration/aadhaar/verifyOTP";
    public static final String checkAndGenerateMobileOTP = "v2/registration/aadhaar/checkAndGenerateMobileOTP";
    public static final String verifyMobileOTP = "v1/registration/aadhaar/verifyMobileOTP";
    public static final String createHealthIdWithAadhaarOtp = "v1/registration/aadhaar/createHealthIdWithAadhaarOtp";
    public static final String createHealthIdWithPreVerified = "v1/registration/aadhaar/createHealthIdWithPreVerified";
    public static final String searchABHA = "v1/search/searchByHealthId";
    public static final String initAuth = "users/auth/init";
    public static final String hidInitAuth = "v1/auth/init";
    public static final String fetchModes = "users/auth/fetch-modes";
    public static final String confirmAuth = "users/auth/confirm";
    public static final String confirmWithMobileOtp = "v1/auth/confirmWithMobileOTP";
    public static final String confirmWithAadhaarOtp = "v1/auth/confirmWithAadhaarOtp";
    public static final String addContext = "links/link/add-contexts";
    //    public static final String smsNotify = "patients/sms/notify2";
    public static final String phrSuggestions = "enrollment/enrol/suggestion";
    public static final String phrISExists = "v1/phr/search/isExist";
    public static final String linkProfileDetails = "v1/phr/profile/link/profileDetails";

    public static final String createPHRAddress = "enrollment/enrol/abha-address";
    public static final String getPngCard = "profile/account/abha-card";
    public static final String sendOTP = "enrollment/request/otp";
    public static final String enrollByAadhaar = "enrollment/enrol/byAadhaar";
    public static final String sendMobileOTP = "enrollment/request/otp";
    public static final String verifyMobileOTPABDM = "enrollment/auth/byAbdm";
    public static final String getPublicCertificate = "profile/public/certificate";
    public static final String InsertAbhaRegistration = "InsertAbhaRegistration";
    public static final String loginByMobileSendOTP = "profile/login/request/otp";
    public static final String loginByMobileVerifyOTP = "profile/login/verify";
    public static final String accountProfile = "profile/account";
    public static final String verifyLoginProfileUser = "profile/login/verify/user";
    public static final String searchABHAAddress = "phr/web/login/abha/search";
    public static final String sendOTPABHAAddress = "phr/web/login/abha/request/otp";
    public static final String verifyOTPABHAAddress = "phr/web/login/abha/verify";
    public static final String getABHAProfileAddress = "phr/web/login/profile/abha-profile";
    public static final String getPngCardPHR = "phr/web/login/profile/abha/phr-card";
    public static final String getPatientQueue = "CreateQueusingCampid";
    public static final String generateLinkToken = "api/hiecm/v3/token/generate-token";
    public static final String getTokenFromAdhar = "GetTokenFromAdhar";
    public static final String linkCareContext = "api/hiecm/hip/v3/link/carecontext";
    public static final String contextNotify = "api/hiecm/hip/v3/link/context/notify";
    public static final String smsNotify = "api/hiecm/hip/v3/link/patient/links/sms/notify2";
    public static final String getStateList = "getStateList";
    public static final String UpdateQueFlag = "UpdateQueFlag";

    public static final String GetState = "get-states.php";
    public static final String GetDistrictsOnState = "get-district-by-state.php";
    //Search ABHA
    public static final String findABHA = "profile/account/abha/search";





//    ------------------------------------Regular camp Creation------------------------------------------

    public static final String GetSUBDeviceListNew = "GetSUBDeviceListNew";
    public static final String VerifyLoginOTP = "VerifyLoginOTP";
    public static final String VerifyOTPForLogin = "VerifyOTPForLogin";
    public static final String GetTeamMembersAttendanceDetailsCampIDWise = "GetTeamMembersAttendanceDetailsCampIDWise";
    public static final String GetDailyCallingReportForCallerLogin = "GetDailyCallingReportForCallerLogin";
    public static final String GetCampAttendanceImages = "GetCampAttendanceImages";
    public static final String GetCampAttendanceImages_V1 = "GetCampAttendanceImages_V1";
    public static final String GetOTPforLogin = "GetOTPforLogin";
    public static final String GetOTPForLogin = "GetOTPForLogin";
    public static final String GetDeviceListForCamp = "GetDeviceListForCamp";
    public static final String GetConsumableListForExpectedBenificiary = "GetConsumableListForExpectedBenificiary";
    public static final String GetLabOnDistrict = "GetLabOnDistrict";
    public static final String InserCampCreationWithoutApproval = "InserCampCreationWithoutApproval";
    public static final String SaveAndroidToken = "SaveAndroidToken";
    public static final String InserCampCreationWithoutApprovalNew = "InserCampCreationWithoutApprovalNew";
    public static final String GetYear = "GetYear";
    public static final String GetYear_UserIDWise = "GetYear_UserIDWise";
    public static final String GetPaidByCompanyList = "GetPaidByCompanyList";
    public static final String GetMonth = "GetMonth";
    public static final String GetMonth_User = "GetMonth_User";
    public static final String GetUserWiseCampList = "GetUserWiseCampList";
    public static final String GetDepartmentType = "GetDepartmentType";
    public static final String GetMobileNoCTandMDOTP = "GetMobileNoCTandMDOTP";
    public static final String GetDependentDetailsFromBoardData = "GetDependentDetailsFromBoardData";
    public static final String GetDependentDetailsForRescreening = "GetDependentDetailsForRescreening";
    public static final String GetDependentDetailsFromBoardData_V1 = "GetDependentDetailsFromBoardData_V1";
    public static final String GetGPListTalukaWise = "GetGPListTalukaWise";
    public static final String GetLabProductStockListByLab = "GetLabProductStockListByLab";
    public static final String AllocatedProductListForCamp = "AllocatedProductListForCamp";
    public static final String ConsumeProductListForCamp = "ConsumeProductListForCamp";
    public static final String InserCampCreationWithoutApprovalForPartner = "InserCampCreationWithoutApprovalForPartner";
    public static final String InserCampCreationD2DWithoutApprovalNew = "InserCampCreationD2DWithoutApprovalNew";
    public static final String InserCampCreationD2DWithoutApproval = "InserCampCreationD2DWithoutApproval";
    public static final String GetTestListForMapResourceDesgForD2d = "GetTestListForMapResourceDesgForD2d";
    public static final String GetTestListForMapResourceDesg = "GetTestListForMapResourceDesg";
    public static final String CheckFlagForCampCalender = "CheckFlagForCampCalender";
    public static final String CheckFlagForTeamCampMapping = "CheckFlagForTeamCampMapping";
    public static final String GetTestidFromResourcelistNew = "GetTestidFromResourcelistNew";
    public static final String InsertInternalCampApprovalForPartner = "InsertInternalCampApprovalForPartner";
    public static final String GetCampCountApproveHolePendingCount = "GetCampCountApproveHolePendingCount";
    public static final String InserCampCreationD2DWithoutApprovalForPartner = "InserCampCreationD2DWithoutApprovalForPartner";
    public static final String GetTestidFromResourcelistNewForPatnerResorces = "GetTestidFromResourcelistNewForPatnerResorces";
    public static final String GetTestListDisha = "api/v1/getTestbyCustomerId";
    public static final String GetAPIDishaToken = "auth/login";
    public static final String InsertDisha = "api/v1/registerPatientFromHMIS ";


    public static final String KEY_BLUETOOTH_NAME = "KEY_BLUETOOTH_NAME";
    public static final String KEY_BLUETOOTH_MAC_ADDRESS = "KEY_BLUETOOTH_MAC_ADDRESS";
    public static final String KEY_SPO = "KEY_SPO";
    public static final String KEY_TEMPERATURE = "KEY_TEMPERATURE";


    public static final String IS_OMRON_BP_MAC = "IS_OMRON_BP_MAC";
    public static final String OMRON_BP_MAC = "OMRON_BP_MAC";

    public static final String LFT_DEVICE_1 = "LFT_DEVICE_NAME_1";
    public static final String IS_LFT1_DEVICE_CONFIGURED = "IS_LFT1_DEVICE_CONFIGURED";
    public static final String SESSION_USER_DISTRICT = "SESSION_USER_DISTRICT";
    public static final String SESSION_USER_MOBILE = "SESSION_USER_MOBILE";
    public static final String LFT_DEVICE_2 = "LFT_DEVICE_NAME_2";
    public static final String IS_LFT2_DEVICE_CONFIGURED = "IS_LFT2_DEVICE_CONFIGURED";

    public static final String CHANNEL_ID = "ABKAT_CHANNEL";
    public static final String CHANNEL_TITLE = "ABKAT";
    //    public static final String LICENSE_KEY_SPIROMETER = "R/eXUGTvDYAXYtP8J9+EzE2DvQ/0vX+WSEgbsWa+SnN1JuwlW8GQAm6B6ddfhr97kJb0IVM3jCFCJeYutSN75uMW5+5ERVmKjXBOmwwmwP9AG0cbDBWDK9rNQraWhDUip+1K4ZDidif2rCUxlaN4zxZiLaTAeeMdU/HhqL9rk*b9uJgx*l1G8pNr9L9BWHY13iGJCInluVnIaOSiOzlq8WuaUm1l9UCf3qyyUd//HyKPAQannG4Nws9VOXimlPzQ=";
//    public static final String LICENSE_KEY_SPIROMETER = "CJn75GR3baAFbrgssUNgUEcsOYLQMaAsS1MG7mQx22I1eDOZZdo3oB6LoBaWqmub0z5IJv/5bOokpnr10DNizIvKOVn0++QcC4a0vDYswfdfdlvSTzzR2L77TUtk+T$HIwFMd+c5Vst0Qcv1NHfxCRjBB3oCkE4q1YuvGe1vB9J+d/proRxSQnIUERQzcixkwnqkPmiWcxBDMlxbEHLDNzOkEBD2RouI9qc5NXESas/KyK2uNJSlwSLXOeucTG+QUjk0dmc/LmxAgu6ktXQkK";
    public static final String LICENSE_KEY_SPIROMETER = "CRBRDA/4EaAdjOtXAkqDtEI4KhW+wcT+SEXjn5LDXc01nurkrwAxTm69NIEhRI3EpRHrOGf2hTT7EB5kPrMRmVzpdhOZ7kWDFmjahrMGgd78etzrQPVyVPmDX9vpbcUy95L0cOFwpx08a+Prnzi5wCYcZrf1n9spvvxQJHlTTBjHtmmgk9p3gAC/3sOgw3wEp_lXaOhorf-mHhQdmRkx0pBHkOtvdvopYFws62oLX/RMghUfyNsPcmd3vyyRAXvN+TimH1QObpL8k9bFLoGLu";

//    public static final String DEVELOPER_KEY = "AIzaSyDB-DrjjhTCreQ0dE0lxrW8A8yCSHBDfaY";


    //-----------------Temp HLL Code ---------------//
    public static final String GetUserAttendanceDays = "GetUserAttendanceDays";
    public static final String TodayAttendanceReport_LABWISE_Datewise = "TodayAttendanceReport_LABWISE_Datewise";

    ////////////// team camp mapping //////////////

    public static final String GetAllDistrictList = "GetAllDistrictList";
    public static final String InsertRegistrationDetails = "InsertRegistrationDetails";
    public static final String GetExternalPhleboList = "GetExternalPhleboList";

    public static final String InsertUSERAndroidID = "InsertUSERAndroidID";
    public static final String GetTeams = "GetTeams";

    public static final String GetExpensesMasterData = "GetExpensesMasterData";
    public static final String GetTeamsCampTypeWise = "GetTeamsCampTypeWise";
    public static final String GetTeamsCampTypeWise_RegularCamp = "GetTeamsCampTypeWise_RegularCamp";
    public static final String GetCampList = "GetCampList";
    public static final String GetT2T_CT_UserDetails = "GetT2T_CT_UserDetails";
    public static final String GetCampList_CampReadiness = "GetCampList_CampReadiness";
    public static final String GetCampList_V2 = "GetCampList_V2";
    public static final String GetCampList_V3 = "GetCampList_V3";
    public static final String GetT2TTeamDetailsByPincode = "GetT2TTeamDetailsByPincode";
    public static final String CW_ReportDeliveryAck = BuildConfig.baseURL + "handler/CW_ReportDeliveryAcknowledgement.ashx";
    public static final String CW_ReportDeliveryAckNew = BuildConfig.baseURL + "handler/CW_ReportDeliveryAcknowledgement_V1.ashx";
    public static final String CW_MedicineDeliveryAck = BuildConfig.baseURL + "handler/CW_MedicineDelivery.ashx";
//    public static final String CW_MedicineDeliveryAckNew = BuildConfig.baseURL + "handler/CW_MedicineDelivery_V1.ashx";
    public static final String CW_MedicineDeliveryAckNew = BuildConfig.baseURL + "handler/CW_MedicineDelivery_V1_DC.ashx";
    public static final String CW_AttendanceInout = BuildConfig.baseURL + "handler/CampAttendanceCheckInOutImages.ashx";
    public static final String CW_AttendanceInoutNew = BuildConfig.baseURL + "handler/CampAttendanceCheckInOutDuringCampImages.ashx";
    public static final String CW_SampleCollectioCT = BuildConfig.baseURL + "handler/CW_T2TBarcodeCollectionDetails.ashx";
    public static final String CW_SampleCollectioCT_Consent = BuildConfig.baseURL + "handler/CW_T2TBarcodeCollectionDetails_Consent.ashx";
    public static final String CW_SampleCollectioCT_Consent_New = BuildConfig.baseURL + "handler/CW_T2TBarcodeCollectionDetails_Consent_V1.ashx";
    public static final String CW_MedicineDeliveryAckTest = BuildConfig.baseURL + "";
    public static final String BeneficiaryVerification = BuildConfig.baseURL + "handler/ChangeBeneficiaryAndCardImage.ashx";
    public static final String BeneficiaryVerificationNew = BuildConfig.baseURL + "handler/ChangeBeneficiaryAndCardImage_InCampTest.ashx";

    public static final String Upload_Post_Camp_File_Handler = BuildConfig.baseURL + "handler/MultipleExpenseBillUploader.ashx";

    public static final String InsertTeamCampMapping = "InsertTeamCampMapping";
    public static final String InsertPacketCollectionDetails = "InsertPacketCollectionDetails";
    public static final String InsertPacketReceiveDetails = "InsertPacketReceiveDetails";
    public static final String InsertPacketAcceptDetails = "InsertPacketAcceptDetails";
    public static final String InsertPacketAssignDetailsManually = "InsertPacketAssignDetailsManually";
    public static final String InsertPacketAssignDetailsManually_D2DTeam = "InsertPacketAssignDetailsManually_D2DTeam";
    public static final String InsertMedicineReturn_ToPharmacy  = "InsertMedicineReturn_ToPharmacy ";
    public static final String UpdateReallocationMedicalDelivary = "UpdateReallocationMedicalDelivary";
    public static final String InsertMedicineReturn_AcceptInLab = "InsertMedicineReturn_AcceptInLab";
    public static final String InsertPacketAcceptDetailsManually = "InsertPacketAcceptDetailsManually";
    public static final String InsertPayoutPaymentReceive = "InsertPayoutPaymentReceive";
    public static final String InsertPacketAssignDetails = "InsertPacketAssignDetails";
    public static final String GetResourceFromDesignation = "GetResourceFromDesignation";
    public static final String GetResourceFromDesignation_Cluster = "GetResourceFromDesignation_Cluster";
    public static final String GetResourceFromDesignation_MMU = "GetResourceFromDesignation_MMU";
    public static final String GetAssignedTeamDetails = "GetAssignedTeamDetails";
    public static final String GetAssignedTeamDetailsFlexi = "GetAssignedTeamDetailsFlexi";
    public static final String GetRecollectionTeamDetials = "GetRecollectionTeamDetials";
    public static final String GetAssignedExternalResourceDetails = "GetAssignedExternalResourceDetails";
    public static final String RemoveTeamCampMapping = "RemoveTeamCampMapping";
    public static final String GetDesignationsForCampCreation = "GetDesignationsForCampCreation";
    public static final String GetDistrictListWithGloMapping = "GetDistrictListWithGloMapping";
    public static final String GetApproveResourcelistNew = "GetApproveResourcelistNew";
    public static final String GetCampSiteDetails_ForAppForIntApproval = "GetCampSiteDetails_ForAppForIntApproval";
    public static final String GetDesignationsForCampCreationFlexi = "GetDesignationsForCampCreationFlexi";
    public static final String GetDesignationsForCampCreationOnlyoctor = "GetDesignationsForCampCreationOnlyoctor";
    public static final String GetCampDetails_ForAppForIntApproval = "GetCampDetails_ForAppForIntApproval";
    public static final String UpdateCampMappingResources = "UpdateCampMappingResources";
    public static final String RemoveCampMappingResources = "RemoveCampMappingResources";
    public static final String GetUserCampMappingAndAttendanceStatus = "GetUserCampMappingAndAttendanceStatus";
    public static final String GetUserCampMappingAndAttendanceStatus_Readiness = "GetUserCampMappingAndAttendanceStatus_Readiness";
    public static final String GetUserCampMappingAndAttendanceStatus_Readiness_CampClose = "GetUserCampMappingAndAttendanceStatus_Readiness_CampClose";
    public static final String GetUserCampMappingAndAttendanceStatus_Readiness_CampClose_V1 = "GetUserCampMappingAndAttendanceStatus_Readiness_CampClose_V1";
    public static final String GetUserCampMappingAndAttendanceStatusForRegularCamp = "GetUserCampMappingAndAttendanceStatusForRegularCamp";
    public static final String GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness  = "GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness";
    public static final String GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness_CampClose  = "GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness_CampClose";
    public static final String GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness_CampClose_V1  = "GetUserCampMappingAndAttendanceStatusForRegularCamp_Readiness_CampClose_V1";
    public static final String GetUserCampMappingAndAttendanceStatus_V1 = "GetUserCampMappingAndAttendanceStatus_V1";
    public static final String InsertCW_PatientBarcodeDetails_WithUrineTest_V1 = "InsertCW_PatientBarcodeDetails_WithUrineTest_V1";
    public static final String InsertCW_PatientBarcodeDetails_New_D2D_V1 = "InsertCW_PatientBarcodeDetails_New_D2D_V1";
    public static final String InsertCW_PatientBarcodeDetails_New_D2D_VersionNo = "InsertCW_PatientBarcodeDetails_New_D2D_VersionNo";
    public static final String InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag = "InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag";
    public static final String InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag_LatLong = "InsertCW_PatientBarcodeDetails_New_D2D_VersionNo_ScanningFlag_LatLong";


    //    -----------------------------------------------Courier Module-----------------------------------------
    public static final String GetCourierCategory = "GetCourierCategory";
    public static final String GetBeneficiaryInformationByRegdNo = "GetBeneficiaryInformationByRegdNo";
    public static final String GetBillSubmitdetailsShow = "GetBillSubmitdetailsShow";
    public static final String GetCourierTransportMode = "GetCourierTransportMode";
    public static final String GetRunnerBoyOnMultipleLabcode = "GetRunnerBoyOnMultipleLabcode";
    public static final String GetSampleTemperature = "GetSampleTemperature";
    public static final String GetServiceTubes = "GetServiceTubes";
    public static final String GetAdvaDemandeddetailsShow = "GetAdvaDemandeddetailsShow";
    public static final String InsertCourierSentDetails_Updated = "InsertCourierSentDetails_Updated";
    public static final String GetCourierDetailsByReciever = "GetCourierDetailsByReciever";
    public static final String InsertDataForReceivedCourier = "InsertDataForReceivedCourier";
    public static final String GetForwardCourierDetails = "GetForwardCourierDetails";
    public static final String GetMachineAvailabilityFlag = "GetMachineAvailabilityFlag";
    public static final String GetMachineAvailabilityFlag_V1 = "GetMachineAvailabilityFlag_V1";
    public static final String GetTubeJson = "GetTubeJson";
    public static final String GetCampListForApproveForPartner = "GetCampListForApproveForPartner";
    public static final String GetCampCountWithDayAndMonthWiseWithCampType = "GetCampCountWithDayAndMonthWiseWithCampType";
    public static final String GetCampCountWithDayAndMonthWiseWithCampType_Org = "GetCampCountWithDayAndMonthWiseWithCampType_Org";
    public static final String GetMonthlySurveySiteRequestForOSV1New_Org = "GetMonthlySurveySiteRequestForOSV1New_Org";
    public static final String GetMonthlySurveySiteRequestForOS_Org = "GetMonthlySurveySiteRequestForOS_Org";
    public static final String GetMonthlySurveySiteRequestForOS_Org_Cluster = "GetMonthlySurveySiteRequestForOS_Org_Cluster";
    public static final String GetCampListForApprove = "GetCampListForApprove";
    public static final String GetCampResourceDetails_ForAppForIntApproval = "GetCampResourceDetails_ForAppForIntApproval";
    public static final String GetCampDeviceDetails_ForAppForIntApproval = "GetCampDeviceDetails_ForAppForIntApproval";
    public static final String GetSubExpensesMasterDataForMgehaCampChanges = "GetSubExpensesMasterDataForMgehaCampChanges";
    public static final String GetSubExpensesMasterData_V1 = "GetSubExpensesMasterData_V1";
    public static final String GetConfirmatoryTestsScreeningAppointmentDetails = "GetConfirmatoryTestsScreeningAppointmentDetails";
    public static final String GetT2T_CT_LabNameDetails = "GetT2T_CT_LabNameDetails";
    public static final String GetT2T_CT_BeneficiaryDetailsforDistCoordinator = "GetT2T_CT_BeneficiaryDetailsforDistCoordinator";
    public static final String GetBeneficiaryDetailsforDistCoordinator = "GetBeneficiaryDetailsforDistCoordinator";
    public static final String GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V2 = "GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V2";
    public static final String GetT2T_CT_BeneficiaryDetailsforAssignTeamid = "GetT2T_CT_BeneficiaryDetailsforAssignTeamid";
    public static final String GetT2T_CT_BeneficiaryDetailsforAssignTeamid_V1 = "GetT2T_CT_BeneficiaryDetailsforAssignTeamid_V1";
    public static final String GetTeamDataByUserId = "GetTeamDataByUserId";
    public static final String GetCourierDetailsBySender = "GetCourierDetailsBySender";
    public static final String GetCourierDetailsByRunner = "GetCourierDetailsByRunner";
    public static final String InsertCourierTransferToRunnerBoy = "InsertCourierTransferToRunnerBoy";
    public static final String GetCourierRemarks_New = "GetCourierRemarks_New";
    public static final String GetFromLab = "GetFromLab";
    public static final String GetOTPWithLGDCode = "GetOTPWithLGDCode";
    public static final String GetOTPWithLGDCode_Org = "GetOTPWithLGDCode_Org";
    public static final String GenerateAndSendOTP = "GenerateAndSendOTP";
    public static final String GetOTPForMedicineDelivery = "GetOTPForMedicineDelivery";
    public static final String GetOTPForMedicineDelivery_Org = "GetOTPForMedicineDelivery_Org";
    public static final String GetOTPForMedicineDelivery_Org_Test = "GetOTPForMedicineDelivery_Org_Test";
    public static final String GetOTPForMedicineDelivery_Org_Option = "GetOTPForMedicineDelivery_Org_Option";
    public static final String GetOTPForMedicineDelivery_Org_New = "GetOTPForMedicineDelivery_Org_New";
    public static final String GetFromOutSource = "GetFromOutSource";
    public static final String GetCampReadinessFormItems = "GetCampReadinessFormItems";
    public static final String GetApproveSubDeviceListForCampAllo = "GetApproveSubDeviceListForCampAllo";
    public static final String GetLBMLabsDetails = "GetLBMLabsDetails";
    public static final String GetMonthlyCalenderForAdvancesTaken = "GetMonthlyCalenderForAdvancesTaken";
    public static final String GetMonthlyCalenderForAdvancesTaken_Close = "GetMonthlyCalenderForAdvancesTaken_Close";
    public static final String GetCourierSampleType = "GetCourierSampleType";
    public static final String GetCourierRemarks = "GetCourierRemarks";
    public static final String GetRelation = "GetRelation";
    public static final String GetRelation_with_Marital_Status = "GetRelation_with_Marital_Status";
    public static final String GetCampAssignUserList = "GetCampAssignUserList";
    public static final String GetDCToLabMapping = "GetDCToLabMapping";
    public static final String InsertCourierSentDetails = "InsertCourierSentDetails";
    public static final String GetReceivedCourierDetails = "GetReceivedCourierDetails";
    public static final String InsertCourierReceivedDetails = "InsertCourierReceivedDetails";
    public static final String GetSentCourierDetails = "GetSentCourierDetails";
    public static final String InsertCourierSentDetailsForApp = "InsertCourierSentDetailsForApp";
    public static final String InsertCourierSentDetailsForApp_New = "InsertCourierSentDetailsForApp_New";
    public static final String InsertCourierReceivedDetailsForApp = "InsertCourierReceivedDetailsForApp";
    public static final String getCenterList = "getCenterList";
    public static final String DEPRELACOUNT = "DEPRELACOUNT";
    public static final String GetRelationWiseDependantCountwithMaritalStatus = "GetRelationWiseDependantCountwithMaritalStatus";
    public static final String GetRecollectionIsrejected = "GetRecollectionIsrejected";
    public static final String GetPatientAndTestValidationCount = "GetPatientAndTestValidationCount";
    public static final String CW_ReportDelivery_V1 = BuildConfig.baseURL + "handler/CW_ReportDelivery_V1.ashx";
    public static final String getClientlist = "getClientlist";
    public static final String GetBarcodeList = "GetBarcodeList";
    public static final String GetBeneficiaryAppoinmentDetails = "GetBeneficiaryAppoinmentDetails";
    public static final String GetBeneficiaryAppoinmentDetails_V1 = "GetBeneficiaryAppoinmentDetails_V1";
    public static final String GetBeneficiaryAppoinmentDetails_V1_MAS = "GetBeneficiaryAppoinmentDetails_V1_MAS";
    public static final String GetCourierBarcodeServicedetail = "GetCourierBarcodeServicedetail";
    public static final String GetUserMappedLabList = "GetUserMappedLabList";
    public static final String GetCampListByLandingLab = "GetCampListByLandingLab";
    public static final String GetExpenseCampIDList = "GetExpenseCampIDList";
    public static final String GetExpenseCampIDList_V1 = "GetExpenseCampIDList_V1";
    public static final String GetSampleBarcodeList = "GetSampleBarcodeList";
    public static final String GetMonthlySurveySiteRequestForOSV1New = "GetMonthlySurveySiteRequestForOSV1New";
    public static final String InsertSampleAcceptance = "InsertSampleAcceptance";
    public static final String InsertMultipleCampID = "InsertMultipleCampID";
    public static final String InsertMultipleCampID_V2 = "InsertMultipleCampID_V2";
    public static final String UpdatePatientAppointmentDate = "UpdatePatientAppointmentDate";
    public static final String Insert_T2T_CT_TeamandBeneficiaryMapping = "Insert_T2T_CT_TeamandBeneficiaryMapping";
    public static final String Insert_T2T_CT_TeamandBeneficiaryMapping_V1 = "Insert_T2T_CT_TeamandBeneficiaryMapping_V1";
    public static final String Insert_RecollectionTeamandBeneficiaryMapping = "Insert_RecollectionTeamandBeneficiaryMapping";
    public static final String UpdaterecollectionAppointmentDate = "UpdaterecollectionAppointmentDate";
    public static final String InsertInvoicePaymentStatus = "InsertInvoicePaymentStatus";
    public static final String InsertDoctorInvoicePaymentStatus = "InsertDoctorInvoicePaymentStatus";
    public static final String InsertPatientRejectionInCamp = "InsertPatientRejectionInCamp";
    public static final String InsertPatientRejectionInCamp_InCampTest = "InsertPatientRejectionInCamp_InCampTest";
    public static final String InsertPatientRejectionInCamp_InCampTest_V1 = "InsertPatientRejectionInCamp_InCampTest_V1";
    public static final String InsertD2DCallingRemark = "InsertD2DCallingRemark";
    public static final String GetApproveResourcelstForUpdate = "GetApproveResourcelstForUpdate";
    public static final String GetTeamDetailsForCentrifuge = "GetTeamDetailsForCentrifuge";
    public static final String GetTeamDetailsForSampleAcceptance = "GetTeamDetailsForSampleAcceptance";
    public static final String GetSampleBarcodeListForCentrifuge = "GetSampleBarcodeListForCentrifuge";
    public static final String GetCourierToLab = "GetCourierToLab";
    public static final String GetSampleBarcodeListForSend = "GetSampleBarcodeListForSend";
    public static final String GetCourierCampDetailsByCourierID = "GetCourierCampDetailsByCourierID";
    public static final String GetSentToLabList = "GetSentToLabList";
    public static final String InsertBeneficiaryCallingLog = "InsertBeneficiaryCallingLog";
    public static final String InsertBeneficiaryCallingLog_V1 = "InsertBeneficiaryCallingLog_V1";
    public static final String InsertBeneficiaryCallingLog_V2 = "InsertBeneficiaryCallingLog_V2";
    public static final String GetT2TCallingAPIDetails = "GetT2TCallingAPIDetails";
    public static final String GetIs24By7IsAccountCreatedFlag = "GetIs24By7IsAccountCreatedFlag";
    public static final String GetIs24By7IsAccountCreatedFlag_V1 = "GetIs24By7IsAccountCreatedFlag_V1";
    public static final String GetUpdateLabRemark = "GetUpdateLabRemark";
    public static final String UpdateProcessLab = "UpdateProcessLab";
    public static final String GetHomeAndHubLab = "GetHomeAndHubLab";
    public static final String GetCampDetailsForLabUpate = "GetCampDetailsForLabUpate";
    public static final String CampTypeD2D = "CampTypeD2D";
    public static final String CampTypeMMU = "CampTypeMMU";
    public static final String CampTypeFlexi = "CampTypeFlexi";
    public static final String CampTypeNonD2D = "CampTypeNonD2D";
    public static final String InsertCampCreation = "InsertCampCreation";
    public static final String InsertCampCreation_V1 = "InsertCampCreation_V1";
    public static final String InsertCampCreation_V2 = "InsertCampCreation_V2";
    public static final String InsertCampCreation_V3 = "InsertCampCreation_V3";
    public static final String InsertCampCreation_LatLong = "InsertCampCreation_LatLong";
    public static final String InsertCampCreation_MMU = "InsertCampCreation_MMU";
    public static final String InsertCampDetails = "InsertCampDetails";
    public static final String GetCampConsumptionDetails_ForAppForIntApproval = "GetCampConsumptionDetails_ForAppForIntApproval";
    public static final String GetHomeAndHubLabNamesOfLandingLab = "GetHomeAndHubLabNamesOfLandingLab";
    public static final String CheckRegistrationMethod = "CheckRegistrationMethod";
    public static final String GetTestMappedtoUser = "GetTestMappedtoUser";
    public static final String GetOTP = "GetOTP";
    public static final String GenerateOTP = "GenerateOTP";
    public static final String Insert_PostCampAcknowledgement = "Insert_PostCampAcknowledgement";
    public static final String GetPostCampDetails_V1 = "GetPostCampDetails_V1";
    public static final String GetPostCampDetails_V3 = "GetPostCampDetails_V3";
    public static final String GetPostCampDetails_V2 = "GetPostCampDetails_V2";
    public static final String GetBeneficiaryListForMedicalDelivery = "GetBeneficiaryListForMedicalDelivery";
    public static final String GetBeneficiaryListForMedicalDelivery_V1 = "GetBeneficiaryListForMedicalDelivery_V1";
    public static final String GetUserPayoutCampDetails = "GetUserPayoutCampDetails";
    public static final String GetUserPayoutDetails = "GetUserPayoutDetails";
    public static final String GetBeneficiaryListForMedicalDelivery_BarcodeScanner = "GetBeneficiaryListForMedicalDelivery_BarcodeScanner";
    public static final String GetBeneficiaryListForMedicalDelivery_BarcodeScanner_V1 = "GetBeneficiaryListForMedicalDelivery_BarcodeScanner_V1";
    public static final String CheckMatchedFlagForBarcodeScanning = "CheckMatchedFlagForBarcodeScanning";
    public static final String GetPatientListReAllocationforMedicineDeliveryByPacketID = "GetPatientListReAllocationforMedicineDeliveryByPacketID";
    public static final String GetBenfListForMedicalReturn_AcceptInLab = "GetBenfListForMedicalReturn_AcceptInLab";
    public static final String GetPacketCollectionData = "GetPacketCollectionData";
    public static final String GetDataForPacketAccept = "GetDataForPacketAccept";
    public static final String GetDataForPacketAssignment = "GetDataForPacketAssignment";
    public static final String GetBenfListForMedicineReturn_ToPharmacy  = "GetBenfListForMedicineReturn_ToPharmacy ";
    public static final String GetDataForPacketReceive = "GetDataForPacketReceive";
    public static final String GetCampName = "GetCampName";
    public static final String InsertInternalCampApproval = "InsertInternalCampApproval";
    public static final String GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V3 = "GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V3";
    public static final String GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V4 = "GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V4";

    public static final String ClientCode = "4";
    public static final String ClientName = "PLUS CARE";

//    public static final String courier_handler =
//            "https://pluscare.org/webservice/handler/Handler1.ashx";

    public static final String PLUSCARE_WESERVICE = "https://pluscare.org/webservice/pluscare.asmx/";
    public static final String LIFENITY_WESERVICE = "https://diagnostics.lifenitywellness.com/WEBSERVICE/LifenityLab.asmx/";


    //////////////////
    //*****************************Methods used in web service***************************//

    public static final String APKDownloader = "APKDownloader";
    public static final String UserLoginApp = "UserLoginApp";
    public static final String GetDistrictList = "GetDistrictList";
    public static final String BindOrg = "BindOrg";
    public static final String GetOrganisationWiseAPIKey = "GetOrganisationWiseAPIKey";
    public static final String GetOrganisationWiseAPIKey_V1 = "GetOrganisationWiseAPIKey_V1";
    public static final String BindDistrict = "BindDistrict";
    public static final String GetPincode = "GetPincode";
    public static final String GetArea = "GetArea";
    public static final String BindDivision = "BindDivision";
    public static final String BindDivision_Cluster = "BindDivision_Cluster";
    public static final String BindDistrict_Cluster = "BindDistrict_Cluster";
    public static final String GetCampCountWithDayAndMonthWiseWithCampType_Org_Cluster = "GetCampCountWithDayAndMonthWiseWithCampType_Org_Cluster";
    public static final String GetTotalcampAndTotalBeneficiarywithZeroCamp_Org_Cluster = "GetTotalcampAndTotalBeneficiarywithZeroCamp_Org_Cluster";
    public static final String BindMedicineDeliveryStatus = "BindMedicineDeliveryStatus";
    public static final String BindMedicineDeliveryRemark = "BindMedicineDeliveryRemark";


    public static final String GetCampTypeByChannelPartner = "GetCampTypeByChannelPartner";
    public static final String GetDivision = "GetDivision";
    public static final String GetChannelPartnerList = "GetChannelPartnerList";
    public static final String GetActiveInactiveD2DTeamsGridData = "GetActiveInactiveD2DTeamsGridData";
    public static final String GetActiveInactiveD2DTeamsGridData_V1 = "GetActiveInactiveD2DTeamsGridData_V1";
    public static final String GetActiveInactiveD2DTeamsGridData_V2 = "GetActiveInactiveD2DTeamsGridData_V2";
    public static final String GetRecollectionBeneficiaryStatusandDetails = "GetRecollectionBeneficiaryStatusandDetails";
    public static final String GetRecollectionBeneficiaryStatusandDetails_RemovePincode_V1 = "GetRecollectionBeneficiaryStatusandDetails_RemovePincode_V1";
    public static final String GetRecollectionBeneficiaryDashboardForMob = "GetRecollectionBeneficiaryDashboardForMob";
    public static final String GetRecollectionBeneficiaryStatusandDetailsCount = "GetRecollectionBeneficiaryStatusandDetailsCount";
    public static final String GetRecollectionBeneficiaryStatusandDetailsCount_V1 = "GetRecollectionBeneficiaryStatusandDetailsCount_V1";
    public static final String GetRecollectionBeneficiaryStatusandDetails_Count_V1    = "GetRecollectionBeneficiaryStatusandDetails_Count_V1   ";
    public static final String GetRecollectionBeneficiaryStatusandDetailsCount_Team = "GetRecollectionBeneficiaryStatusandDetailsCount_Team";
    public static final String GetRecollectionBeneficiaryStatusandDetailsCount_Team_V1 = "GetRecollectionBeneficiaryStatusandDetailsCount_Team_V1";
    public static final String GetRecollectionBeneficiaryStatusandDetails_Count_Team_V1    = "GetRecollectionBeneficiaryStatusandDetails_Count_Team_V1   ";
    public static final String GetRecollectionBeneficiaryToTeam = "GetRecollectionBeneficiaryToTeam";
    public static final String GetRecollectionBeneficiaryToTeam_RemovePincode_V1 = "GetRecollectionBeneficiaryToTeam_RemovePincode_V1";
    public static final String GetActiveInactiveD2DNonWorkingTeamsWithCatagoryID = "GetActiveInactiveD2DNonWorkingTeamsWithCatagoryID";
    public static final String GetActiveInactiveD2DTeamsGridDataWithCatagoryID = "GetActiveInactiveD2DTeamsGridDataWithCatagoryID";
    public static final String GetBeneficiaryAddressDetails = "GetBeneficiaryAddressDetails";
    public static final String GetD2DPhysicalExamDetails = "GetD2DPhysicalExamDetails";
    public static final String GetD2DTeamWisePhyExamDetails = "GetD2DTeamWisePhyExamDetails";
    public static final String GetActiveInactiveD2DTeamsCount = "GetActiveInactiveD2DTeamsCount";
    public static final String GetActiveInactiveD2DTeamsCount_V1 = "GetActiveInactiveD2DTeamsCount_V1";
    public static final String GetActiveInactiveD2DTeamsCount_V2 = "GetActiveInactiveD2DTeamsCount_V2";
    public static final String GetActiveInactiveD2DTeamsCountWithCatagoryID = "GetActiveInactiveD2DTeamsCountWithCatagoryID";
    public static final String GetDivisionWiseDistrict = "GetDivisionWiseDistrict";
    public static final String GetAllDistrictListForPhyExam = "GetAllDistrictListForPhyExam";
    public static final String GetActiveInactiveD2DNonWorkingTeams = "GetActiveInactiveD2DNonWorkingTeams";
    public static final String GetActiveInactiveD2DNonWorkingTeams_V1 = "GetActiveInactiveD2DNonWorkingTeams_V1";
    public static final String GetActiveInactiveD2DNonWorkingTeams_V2 = "GetActiveInactiveD2DNonWorkingTeams_V2";
    public static final String GetAudioScreeningDetails = "GetAudioScreeningDetails";
    public static final String GetLungFunctionTestDetails = "GetLungFunctionTestDetails";
    public static final String GetVisionScreeningDetails = "GetVisionScreeningDetails";
    public static final String GetTeamMembersDetailsForCalling = "GetTeamMembersDetailsForCalling";
    public static final String GetRegdWiseListOfMobileNosForAppointments = "GetRegdWiseListOfMobileNosForAppointments";
    public static final String GetActiveInactiveD2DWorkingTeams = "GetActiveInactiveD2DWorkingTeams";
    public static final String GetActiveInactiveD2DWorkingTeams_V1 = "GetActiveInactiveD2DWorkingTeams_V1";
    public static final String GetActiveInactiveD2DWorkingTeams_V2 = "GetActiveInactiveD2DWorkingTeams_V2";
    public static final String GetAppointmentDateCount = "GetAppointmentDateCount";
    public static final String GetActiveInactiveD2DWorkingTeamsWithCatagoryID = "GetActiveInactiveD2DWorkingTeamsWithCatagoryID";
    public static final String GetDivisionWiseDistrictAndUserID = "GetDivisionWiseDistrictAndUserID";
    public static final String GetDistrictByUserID = "GetDistrictByUserID";
    public static final String GetLabByUserID = "GetLabByUserID";


    public static final String GetTargetDivision = "GetTargetDivision";
    public static final String BeneficairyTargetTracking = "BeneficairyTargetTracking";
    public static final String GetDeviceImageDetails = "GetDeviceImageDetails";
    public static final String CheckCampClosedforBreastScreening = "CheckCampClosedforBreastScreening";
    public static final String GetDoctorType = "GetDoctorType";
    public static final String DoctorRegistration = "DoctorRegistration";
    public static final String CAMPPatientCheckupAnalysis_Report_New = "CAMPPatientCheckupAnalysis_Report_New";
    public static final String GetTeamNumberByUserId = "GetTeamNumberByUserId";
    public static final String GetTeamNumberByUserId_MedicineDelivery = "GetTeamNumberByUserId_MedicineDelivery";
    public static final String InsertBasicInfoMale = "InsertBasicInfoMale";
    public static final String InsertBasicInfoMale_VersionNo = "InsertBasicInfoMale_VersionNo";
    public static final String InsertBasicInfoFemale = "InsertBasicInfoFemale";
    public static final String InsertBasicInfoFemale_VersionNo = "InsertBasicInfoFemale_VersionNo";
    public static final String GetNearistHospitalList1 = "GetNearistHospitalList";
    public static final String getTestList = "getTestList";
    public static final String getTestListForReject = "getTestListForReject";
    public static final String GetOtherReasonForPatientRejection = "GetOtherReasonForPatientRejection";
    public static final String GetD2DCallingRemark = "GetD2DCallingRemark";
    public static final String getServiceGroupList = "getServiceGroupList";
    public static final String InsertBasicHealthInfo = "InsertBasicHealthInfo";
    public static final String InsertBasicHealthInfo_New = "InsertBasicHealthInfo_New";
    public static final String InsertBasicHealthInfo_New_WithVersion = "InsertBasicHealthInfo_New_WithVersion";
    public static final String InsertBasicHealthInfo_New_WithVersion_V1 = "InsertBasicHealthInfo_New_WithVersion_V1";
    public static final String InsertBasicHealthInfo_New_WithVersion_V2 = "InsertBasicHealthInfo_New_WithVersion_V2";
    public static final String InsertBasicHealthInfo_New_WithVersion_FastingHrs = "InsertBasicHealthInfo_New_WithVersion_FastingHrs";
    public static final String GetQuestionnaire = "GetQuestionnaire";
    public static final String InsertBasicHealthInfoFortest = "InsertBasicHealthInfoFortest";
    public static final String GetActiveCampOnDate_CW = "GetActiveCampOnDate_CW";
    public static final String MappCWCampWithDoctor = "MappCWCampWithDoctor";
    public static final String GetMappedCampForUser = "GetMappedCampForUser";
    public static final String InsertPhysicalExaminationDetails = "InsertPhysicalExaminationDetails";
    public static final String InsertLungFunctionDetails = "InsertLungFunctionDetails";
    public static final String InsertCW_PatientBarcodeDetails = "InsertCW_PatientBarcodeDetails";
    public static final String InsertCW_UrineSampleRecived = "InsertCW_UrineSampleRecived";
    public static final String InsertCW_PatientBarcodeDetails_New = "InsertCW_PatientBarcodeDetails_New";
    public static final String InsertCW_PatientBarcodeDetails_WithUrineTest = "InsertCW_PatientBarcodeDetails_WithUrineTest";
    public static final String InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo = "InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo";
    public static final String InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag = "InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag";
    public static final String InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag_LatLong = "InsertCW_PatientBarcodeDetails_WithUrineTest_VersionNo_ScanningFlag_LatLong";
    public static final String InsertT2TBarcodeCollectionDetails = "InsertT2TBarcodeCollectionDetails";
    public static final String UpdateBloodPP = "UpdateBloodPP";
    public static final String InsertEyeScreeningDetails = "InsertEyeScreeningDetails";
    public static final String InsertEyeScreeningDetails_New = "InsertEyeScreeningDetails_New";
    public static final String InsertEyeScreeningDetails_V1 = "InsertEyeScreeningDetails_V1";
    public static final String InsertEyeScreeningDetails_VersionNo = "InsertEyeScreeningDetails_VersionNo";
    public static final String InsertVisionScreeningJSONByMachine = "InsertVisionScreeningJSONByMachine";
    public static final String InsertVisionScreeningJSONByMachineDash = "InsertVisionScreeningJSONByMachineDash";
    public static final String InsertEarScreeningDetails = "InsertEarScreeningDetails";
    public static final String GetListOfreraregisterbuilding = "GetListOfreraregisterbuilding";
    public static final String GetListOfreraregisterbuildingByPincode = "GetListOfreraregisterbuildingByPincode";
    public static final String GetApprovedCampListDetailsForApp = "GetApprovedCampListDetailsForApp";
    public static final String GetApprovedCampListDetailsForApp_FlexiCamp = "GetApprovedCampListDetailsForApp_FlexiCamp";
    public static final String GetApprovedCampListDetails_RegularCamp = "GetApprovedCampListDetails_RegularCamp";
    public static final String GetApprovedCampListDetailsForAppDistrictWise = "GetApprovedCampListDetailsForAppDistrictWise";
    public static final String GetApprovedCampListforAwareNess = "GetApprovedCampListforAwareNess";
    public static final String GetMonthlySurveySiteRequest = "GetMonthlySurveySiteRequest";
    public static final String GetMonthlySurveySiteRequestForOS = "GetMonthlySurveySiteRequestForOS";
    public static final String GetTodaysPatientCount = "GetTodaysPatientCount";
    public static final String GetTodaysPatientCount_V1 = "GetTodaysPatientCount_V1";
    public static final String GetLandingPageCountsDisplayforFinancialYear_V1 = "GetLandingPageCountsDisplayforFinancialYear_V1";
    public static final String GetLandingPageCountsDisplayforFinancialYear_V1_WithSubOrg = "GetLandingPageCountsDisplayforFinancialYear_V1_WithSubOrg";
    public static final String GetTotalcampAndTotalBeneficiarywithZeroCamp = "GetTotalcampAndTotalBeneficiarywithZeroCamp";
    public static final String GetTotalcampAndTotalBeneficiarywithZeroCamp_Org = "GetTotalcampAndTotalBeneficiarywithZeroCamp_Org";
    public static final String GetTotalAndTodaysBeneficiaryCountWithProcessCount = "GetTotalAndTodaysBeneficiaryCountWithProcessCount";
    public static final String GetM_AdvertisementTypeList = "GetM_AdvertisementTypeList";
    public static final String CampClosedbyAdmin = "CampClosedbyAdmin";
    public static final String InsertCallDetails = "InsertCallDetails";
    public static final String DeleteRegistrationID = "DeleteRegistrationID";
    public static final String GetListOfConstructionSite = "GetListOfConstructionSite";
    public static final String GetLab = "GetLab";
    public static final String GetT2TLabDetails = "GetT2TLabDetails";
    public static final String GetCampType = "GetCampType";
    public static final String CampType = "CampType";
    public static final String GetLabDistrictWise_V1 = "GetLabDistrictWise_V1";
    public static final String GetLabforD2DCampCoordinator = "GetLabforD2DCampCoordinator";
    public static final String GetReportDeliveryExecutive = "GetReportDeliveryExecutive";
    public static final String GetReportDeliveryExecutive_D2DTeam = "GetReportDeliveryExecutive_D2DTeam";
    public static final String GetInitiatedByListForCamp = "GetInitiatedByListForCamp";
    public static final String GetAllTalukaList = "GetAllTalukaList";
    public static final String GetLabTalukaWise = "GetLabTalukaWise";
    public static final String GetUserMappedTaluka = "GetUserMappedTaluka";
    public static final String GetLabTalukaWise_V1 = "GetLabTalukaWise_V1";

    public static final String InsertConstructionSiteDetails = "InsertConstructionSiteDetails";
    public static final String SampleProcessingReport = "SampleProcessingReport";
    public static final String GetWorkerInfroFromWorkerRegid = "GetWorkerInfroFromWorkerRegid";
    public static final String GetSiteSurveyRequest = "GetSiteSurveyRequest";
    public static final String InsertUserAttendanceForConstructionWorker = "InsertUserAttendanceForConstructionWorker";
    public static final String InsertCampTargetDetails = "InsertCampTargetDetails";
    public static final String GetCampTargetDetails = "GetCampTargetDetails";
    public static final String GLO_ApprovedWorkerSiteMapping = "GLO_ApprovedWorkerSiteMapping";
    public static final String InsCW_UnRegisterWorkerDetails = "InsCW_UnRegisterWorkerDetails";
    public static final String CreateCamp = "CreateCamp";
    public static final String ApproveCamp = "ApproveCamp";
    public static final String InsCW_RegistrationDetails = "InsCW_RegistrationDetails";
    public static final String GetUserAttendancesUsingSitedetailsID_Anti = "GetUserAttendancesUsingSitedetailsID_Anti";
    public static final String GetUserAttendancesUsingSitedetailsID_Anti_RationCard = "GetUserAttendancesUsingSitedetailsID_Anti_RationCard";
    public static final String GetUserAttendancesUsingSitedetailsID_Anti_D2D_V1 = "GetUserAttendancesUsingSitedetailsID_Anti_D2D_V1";
    public static final String GetUserAttendancesUsingSitedetailsID_New = "GetUserAttendancesUsingSitedetailsID_New";
    public static final String GetUserAttendancesUsingSitedetailsID_RationCard = "GetUserAttendancesUsingSitedetailsID_RationCard";
    public static final String GetUserAttendancesUsingSitedetailsID_New_D2D_V1 = "GetUserAttendancesUsingSitedetailsID_New_D2D_V1";
    public static final String GetUserAttendancesUsingSitedetailsID_UrineChange = "GetUserAttendancesUsingSitedetailsID_UrineChange";
    public static final String GetUserAttendancesUsingSitedetailsID_UrineChange_D2D_V1 = "GetUserAttendancesUsingSitedetailsID_UrineChange_D2D_V1";
    public static final String insertCW_BuilderDetailsForApp = "insertCW_BuilderDetailsForApp";
    public static final String GetNearistHospitalList = "GetNearistHospitalList";
    public static final String GetMenuDashboard = "GetMenuDashboard";
    public static final String SelectVidieoLinkData = "SelectVidieoLinkData";
    public static final String InserTrainigVideoHistrory = "InserTrainigVideoHistrory";
    public static final String GetWorkerInfroRe_Registration = "GetWorkerInfroRe_Registration";
    public static final String D2DGetWorkerInfroRe_Registration = "D2DGetWorkerInfroRe_Registration";
    public static final String GetBeneficiaryInfoHLL = "GetBeneficiaryInfoHLL";
    public static final String VerifyOTP = "VerifyOTP";
    public static final String VerifyOTPMedicineDelivery = "VerifyOTPMedicineDelivery";
    public static final String VerifyRegistrationOTP = "VerifyRegistrationOTP";
    public static final String GetOTPforRegistration = "GetOTPforRegistration";
    public static final String GetOTPforRegistration_V1 = "GetOTPforRegistration_V1";
    public static final String GetOTPforRegistration_Org = "GetOTPforRegistration_Org";
    public static final String SendRegistrationOTPWithDPDPConsent = "SendRegistrationOTPWithDPDPConsent";
    public static final String InsertInvoiceOTPDetails = "InsertInvoiceOTPDetails";
    public static final String InsertInvoiceOTPDetails_Org = "InsertInvoiceOTPDetails_Org";
    public static final String InsertDoctorInvoiceOTPDetails = "InsertDoctorInvoiceOTPDetails";
    public static final String InsertChangePasswordRequest = "InsertChangePasswordRequest";
    public static final String InsertForgotPasswordRequest = "InsertForgotPasswordRequest";
    public static final String UpdateAttendanceImageApproval = "UpdateAttendanceImageApproval";
    public static final String InsertOTPForCTSampleCollection = "InsertOTPForCTSampleCollection";
    public static final String InsertOTPForCTSampleCollection_Option = "InsertOTPForCTSampleCollection_Option";
    public static final String UpdateUserPassword = "UpdateUserPassword";
    //    public static final String GetNearistHospitalList = "GetNearistHospitalList";
    public static final String InsertConstructionWorkerDetails = "InsertConstructionWorkerDetails";
    public static final String GetUnRegisterUserDetails = "GetUnRegisterUserDetails";
    public static final String GetListOfBuilderDetails = "GetListOfBuilderDetails";
    public static final String GetM_HealthHistoryDetails = "GetM_HealthHistoryDetails";
    public static final String GetUserDataforPhysicalExamnination = "GetUserDataforPhysicalExamnination";
    public static final String GetBreastScreeningDetails = "GetBreastScreeningDetails";
    public static final String GetCW_AudiometicMasterDetails = "GetCW_AudiometicMasterDetails";
    public static final String GetListOfWorkerDetails = "GetListOfWorkerDetails";
    public static final String GetHomePageCountDetails = "GetHomePageCountDetails";
    public static final String GetHomePageCountDetailsByFinancialYear = "GetHomePageCountDetailsByFinancialYear";
    public static final String GetHomePageCountDetailsForOS = "GetHomePageCountDetailsForOS";
    public static final String GetHomePageCountDetailsForOSByFinancialYear = "GetHomePageCountDetailsForOSByFinancialYear";
    public static final String GetUserForceLogout = "GetUserForceLogout";
    public static final String UpdateLogoutUser = "UpdateLogoutUser";
    public static final String GetCampDetailsTodayCount = "GetCampDetailsTodayCount";
    public static final String GetListOfLungandAudioImageDetails = "GetListOfLungandAudioImageDetails";
    public static final String InsertMachineHearingTest = "InsertMachineHearingTest";
    public static final String InsertMachineHearingTest_New = "InsertMachineHearingTest_New";
    public static final String InsertQuestionnaireDetails = "InsertQuestionnaireDetails";
    public static final String GetExaminarNoteList = "GetExaminarNoteList";
    public static final String GetDeviceList = "GetDeviceList";
    public static final String GetDoctorList = "GetDoctorList";
    public static final String GetReferToList = "GetReferToList";
    public static final String GetLabWiseTestStatus = "GetLabWiseTestStatus";
    public static final String GetLabWiseTestStatusWithService = "GetLabWiseTestStatusWithService";
    public static final String GetDateWiseCount = "GetDateWiseCount";
    public static final String GetFingerPrintPath = "GetFingerPrintPath";
    public static final String GetInventoryDetailsOncampId = "GetInventoryDetailsOncampId";
    public static final String GetuseInventoryDetailsOncampId = "GetuseInventoryDetailsOncampId";
    public static final String GetCampResourceListOncampId = "GetCampResourceListOncampId";
    public static final String GetRegiWorkerDetailsOncampId = "GetRegiWorkerDetailsOncampId";
    public static final String GetuserAttendance = "GetuserAttendance";
    public static final String InsertBasicInfoMale_V1 = "InsertBasicInfoMale_V1";
    public static final String InsertBasicInfoMale_VersionNoNew = "InsertBasicInfoMale_VersionNoNew";
    public static final String InsertPhysicalExaminationForHSCC = "InsertPhysicalExaminationForHSCC";
    public static final String InsertPhysicalExaminationForHSCC_V1 = "InsertPhysicalExaminationForHSCC_V1";
    public static final String InsertPhysicalExaminationForHSCC_VersionNo = "InsertPhysicalExaminationForHSCC_VersionNo";
    public static final String InsertPhysicalExaminationForHSCC_VersionNo_V2 = "InsertPhysicalExaminationForHSCC_VersionNo_V2";
    public static final String SaveAndroidToken_V1 = "SaveAndroidToken_V1";
    public static final String UpdateVodafoneToken = "UpdateVodafoneToken";
    public static final String UpdateLogOutDetails = "UpdateLogOutDetails";
    public static final String UpdateNewUserPassword = "UpdateNewUserPassword";
    public static final String InsertUserInOutAttendance  = "InsertUserInOutAttendance ";
    public static final String VerifyCTOTP = "VerifyCTOTP";
    public static final String InsertUserAttendance = "InsertUserAttendance";
    public static final String GetSummaryDrillDownDetails = "GetSummaryDrillDownDetails";
    public static final String GetActiveUserList = "GetActiveUserList";
    public static final String GetAttandanceReportUserwise = "GetAttandanceReportUserwise";
    public static final String GetAttendanceReportDistrictWise = "GetAttendanceReportDistrictWise";
    public static final String UpdateInventoryDetailsOncampId = "UpdateInventoryDetailsOncampId";
    public static final String GetCampDetailsCount = "GetCampDetailsCount";
    public static final String GetCampDetailsCountRegular_InCampTest = "GetCampDetailsCountRegular_InCampTest";
    public static final String GetCampDetailsCount_V1 = "GetCampDetailsCount_V1";
    public static final String GetCampDetailsCount_InCampTest = "GetCampDetailsCount_InCampTest";
    public static final String CAMPPatientCheckupAnalysis_Report = "CAMPPatientCheckupAnalysis_Report";
    public static final String CAMPPatientCheckupAnalysis_Report_V1 = "CAMPPatientCheckupAnalysis_Report_V1";
    public static final String InsertLFTDetails = "InsertLFTDetails";
    public static final String InsertLFTDetails_VersionNo = "InsertLFTDetails_VersionNo";
    public static final String ApprovSiteSurveyRequest = "ApprovSiteSurveyRequest";
    public static final String RejectSiteSurveyRequest = "RejectSiteSurveyRequest";
    public static final String BeneficiearyStatus = "BeneficiearyStatus";
    public static final String InvoiceAndPostCampDashboard = "InvoiceAndPostCampDashboard";
    public static final String BreastScreeningDetails = "BreastScreeningDetails";
    public static final String getBreastQuadrantValues = "getBreastQuadrantValues";
    public static final String GetPostCampDetails = "GetPostCampDetails";
    public static final String DoctorsScreeningRemark = "DoctorsScreeningRemark";
    public static final String GetRtpcrLabName = "GetRtpcrLabName";
    public static final String GetTestOnlyCovid = "GetTestOnlyCovid";
    public static final String GetSpecimenType = "GetSpecimenType";
    public static final String GetAntigenLabCode = "GetAntigenLabCode";
    public static final String GetConfirmatoryTestsScreeningAppointmentDetails_V1 = "GetConfirmatoryTestsScreeningAppointmentDetails_V1";
    public static final String GetConfirmatoryTestsScreeningAppointmentDetails_V2 = "GetConfirmatoryTestsScreeningAppointmentDetails_V2";
    public static final String Get_T2T_CT_AssignmentRemarks = "Get_T2T_CT_AssignmentRemarks";
    public static final String GetRecollectionAssignmentRemarks = "GetRecollectionAssignmentRemarks";
    public static final String GetMaritalMaster = "GetMaritalMaster";
    public static final String UpdateT2T_CT_AppointmentDate = "UpdateT2T_CT_AppointmentDate";
    public static final String UpdateT2T_CT_AppointmentDate_V2 = "UpdateT2T_CT_AppointmentDate_V2";
    public static final String InsertBasicHealthtestForAntigen = "InsertBasicHealthtestForAntigen";
    public static final String MAHABOCBASEURL = "https://healthcamp.mahabocw.in/api/";
    public static final String GetWorkerInfo = "beneficiary-details-api/beneficiary-details/";
    public static final String GetWorkerInfoNew = "https://healthcamp.mahabocw.in/api/beneficiary-details-api/beneficiary-details/";

    //  ---------------------------------------New D2d Methods----------------------------------

    //CSC
    public static final String GETCSCPreCampInfoForApproval = "GETCSCPreCampInfoForApproval";
    public static final String CSCCampApprovalUpdate = "CSCCampApprovalUpdate";
    public static final String GetRegiWorkerDetailsOncampId_V1 = "GetRegiWorkerDetailsOncampId_V1";
    public static final String GetRegiWorkerDetailsOncampId_InCampTest = "GetRegiWorkerDetailsOncampId_InCampTest";
    public static final String GetRegiWorkerDetailsOncampId_InCampTest_V1 = "GetRegiWorkerDetailsOncampId_InCampTest_V1";
    public static final String Insert_DivisionalManagerCampRequestApproval = "Insert_DivisionalManagerCampRequestApproval";

    //HLL Door To Door
    public static final String GetCampDetailsonLabForDoorToDoor = "GetCampDetailsonLabForDoorToDoor";
    public static final String GetApprovedCampListDetailsForAppD2D = "GetApprovedCampListDetailsForAppD2D";
    public static final String GetDoctorListCSCCampAvailaible = "GetDoctorListCSCCampAvailaible";
    public static final String InsertCscDoctorAvailaibilityStatus = "InsertCscDoctorAvailaibilityStatus";
    public static final String token_Id = "/846b2bb2-0e05-11eb-9b3b-0a0520b0bd9c/";
    public static final String InsAPIRegistrationResponse = "InsAPIRegistrationResponse";
    public static final String D2DGetUserAttendancesUsingSitedetailsID_Anti = "GetUserAttendancesUsingSitedetailsID_AntiD2D";
    public static final String D2DGetUserAttendancesUsingSitedetailsID_New = "GetUserAttendancesUsingSitedetailsID_NewD2D";
    public static final String InsertCW_PatientBarcodeDetails_New_D2D = "InsertCW_PatientBarcodeDetails_New_D2D";
    public static final String InsertCampCloseActivity = "InsertCampCloseActivity";
    public static final String InsertCampCloseActivitywithUrineChanges = "InsertCampCloseActivitywithUrineChanges";
    public static final String InsertCampClosingConfirmation = "InsertCampClosingConfirmation";
    public static final String GetCampCloseDetails = "GetCampCloseDetails";
    public static final String GetConsumableListDetails = "GetConsumableListDetails";
    public static final String GetBeneficiaryListByRegID = "GetBeneficiaryListByRegID";
    public static final String GetD2DCampMappedDoctorList = "GetD2DCampMappedDoctorList";
    public static final String GetUSERAndroidID = "GetUSERAndroidID";
    public static final String InsertBeneficiaryDoctorMapping = "InsertBeneficiaryDoctorMapping";
    public static final String GetAssignedBeneficiaryAndDoctorList = "GetAssignedBeneficiaryAndDoctorList";
    public static final String GetuserAttendanceForSitedetailsID_PhysicalExam = "GetuserAttendanceForSitedetailsID_PhysicalExam";
    public static final String GetTeamNumberByCampIdAndUSerId = "GetTeamNumberByCampIdAndUSerId";
    public static final String GetCampDetailsonLabForDoorToDoor_V1 = "GetCampDetailsonLabForDoorToDoor_V1";
    public static final String GETCSCPreCampInfoForApproval_V1 = "GETCSCPreCampInfoForApproval_V1";

    public static final String GETCSCPreCampInfoForApprovalListForDivisionalManager_V1 = "GETCSCPreCampInfoForApprovalListForDivisionalManager_V1";
    public static final String GETCSCPreCampInfoForApprovalListForCampCoordinator_V1 = "GETCSCPreCampInfoForApprovalListForCampCoordinator_V1";

    public static final String patientRegUrl = "https://staging.livehealth.solutions/LHRegisterBillAPI";
    public static final String GetDependentListFromRegdId = "GetDependentListFromRegdId";
    public static final String InsertAdvancesRequest = "InsertAdvancesRequest";
    public static final String GetBenificiaryRegisterOrNot = "GetBenificiaryRegisterOrNot";
    public static final String GetBeneficiaryRegistrationDetails = "GetBeneficiaryRegistrationDetails";
    public static final String GetBeneficiaryRegistrationDetailsWithMaritalStatus = "GetBeneficiaryRegistrationDetailsWithMaritalStatus";
    public static final String GetBeneficiaryRegistrationDetailsWithMaritalStatus_GP = "GetBeneficiaryRegistrationDetailsWithMaritalStatus_GP";
    public static final String GetDocumenttype = "GetDocumenttype";
    public static final String GetAdvadetailsNewVersion = "GetAdvadetailsNewVersion";
    public static final String GetAdvadetailsNewVersion_V1 = "GetAdvadetailsNewVersion_V1";
    public static final String GetAdvadetailsNewVersion_V2 = "GetAdvadetailsNewVersion_V2";
    public static final String InsertAdvancesRequestNewChanges_V3 = "InsertAdvancesRequestNewChanges_V3";
    public static final String ExpenseBillDetailsHandler = "handler/ExpenseBillDetailsHandler.ashx";
    public static final String ProofOfPermissionHandler = "handler/ExpensePermissionProof.ashx";
    public static final String InsertAdvancesRequestNewChanges_New = "InsertAdvancesRequestNewChanges_New";
    public static final String InsertAdvancesRequestNewChanges_V1 = "InsertAdvancesRequestNewChanges_V1";
    public static final String InsertAdvancesRequestNewChanges_V2 = "InsertAdvancesRequestNewChanges_V2";

    public static final String organisation_id = "281310";
    public static final String test_id = "234";

    //Auto Camp Creation
    public static final String BindD2DCheckListData = "BindD2DCheckListData";
    public static final String GetCampLocationDetails = "GetCampLocationDetails";
    public static final String GetCampClosingConfirmationStatus = "GetCampClosingConfirmationStatus";
    public static final String GetCampClosingConfirmationStatus_V1 = "GetCampClosingConfirmationStatus_V1";
    public static final String CheckDependentRegistrationStatus = "CheckDependentRegistrationStatus";
    public static final String VerifyDependentDetails = "VerifyDependentDetails";
    public static final String VerifyDependentDetails_V1 = "VerifyDependentDetails_V1";
    public static final String VerifyDependentDetails_V2 = "VerifyDependentDetails_V2";
    public static final String GetBeneficiaryConsentDetails = "GetBeneficiaryConsentDetails";
    public static final String GetTeamDetailsByCampID = "GetTeamDetailsByCampID";
    public static final String Insert_D2D_CampReadiness = "Insert_D2D_CampReadiness";
    public static final String GetLocationDetailsByLabCode = "GetLocationDetailsByLabCode";
    public static final String GetCampIDWiseTeamDetails = "GetCampIDWiseTeamDetails";
    public static final String GetCampTypeAndCatagory = "GetCampTypeAndCatagory";
    public static final String InsertT2TBarcodeCollectionDetails_V1 = "InsertT2TBarcodeCollectionDetails_V1";
    public static final String InsertT2TBarcodeCollectionDetails_V2 = "InsertT2TBarcodeCollectionDetails_V2";
    public static final String GetLandingPageCountsDisplayforFinancialYear = "GetLandingPageCountsDisplayforFinancialYear";
    public static final String GetLandingPageCountsDisplayforFinancialYearForAllSubOrg = "GetLandingPageCountsDisplayforFinancialYearForAllSubOrg";


    // --- Beta URL
//    public static final Boolean isBeta = true;
//    public static final String domain = "https://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS";
//    public static final String baseURL = "https://testmcwwb.myhindlab.com/webservices/";
//    public static final String hllBaseURl = "http://beta.hllconnect.in/webservice/hllconnect.asmx/";

    //     --- Live URL https://testmcwwb.myhindlab.com/webservices/
    public static final Boolean isBeta = false;
    public static final String domain = "https://mcwwb.myhindlab.com/MCWWBDOCS_LIVE";
    public static final String baseURL = "https://mcwwb.myhindlab.com/webservices/";
    public static final String hllBaseURl = "https://erp.hllconnect.in/webservice/hllconnect.asmx/";


    public static final String mahabocwBaseURL = "xc";
    public static final String bocwRegistration = "bocw-registration";
    public static final String CSCPreCampImage = "https://mcwwb.myhindlab.com//MCWWBDOCS_LIVE/CSCPreCampImage/";


//    public static final String webservice = BuildConfig.baseURL + "ConstructionWorker.asmx/";
    public static final String webservice = BuildConfig.baseURL + "ConstructionWorker_V2.asmx/";
//    public static final String webservice = BuildConfig.baseURL + "ConstructionWorker.asmx/";
    public static final String webserviceHandlerExpense = BuildConfig.baseURL;
    public static final String patientRegistrationHandler = BuildConfig.baseURL + "handler/InsertCWCampPatientInfo.ashx";
    public static final String patientRegistrationHandlerNew = BuildConfig.baseURL + "handler/PatientRegistration.ashx";
    public static final String patientRegistrationHandlerHealthCard = BuildConfig.baseURL + "handler/PatientHealthCardDetailsInsert.ashx";
    public static final String BeneficiaryPhotoUpload = BuildConfig.baseURL + "handler/BeneficiaryPhotoUpload.ashx";
    public static final String PatientRegistrationAndHealthCard = BuildConfig.baseURL + "handler/PatientRegistrationAndHealthCard.ashx";
    public static final String BeneficiaryRe_Registration = BuildConfig.baseURL + "handler/BeneficiaryRe_Registration.ashx";
    public static final String D2DBeneficiaryRe_Registration = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_Registration.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV1 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationV1.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV2 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationV2.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV3 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationV3.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV4 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationV4.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV5 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationV5.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV6 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationVNew.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV7 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationVersionLatest.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV8 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationVersionWithVersionNo.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV9 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationVersionWithVersionNoGeoTag.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV10 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_ReScreening.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV11 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_V1.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV12 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_Taluka_ABDM.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV14 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_Taluka_Gender.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV16 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_Taluka_Gender_V1.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV18 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRegistration_Gender_V1_Dependent.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV20 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRegistration_Gender_V1_Dependent_CF.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV15 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_Taluka_Gender_FaceMatch.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV17 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_Taluka_Gender_FaceMatch_V1.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV19 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRegistration_Gender_FaceMatch_V1_Dependent.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV21 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRegistration_Gender_FaceMatch_V1_Dependent_CF.ashx";
    public static final String DtoDBeneficiaryRe_RegistrationV13 = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationWithVersionNo_MaritalStatus_Taluka_WhatsApp.ashx";
    public static final String DtoDBeneficiaryRe_Registration_Face_Match = BuildConfig.baseURL + "handler/DtoDBeneficiaryRe_RegistrationForFaceMatch.ashx";
    public static final String D2DPrescription = BuildConfig.baseURL + "handler/Priscription.ashx";

    public static final String BeneficiaryRe_RegistrationOTP = BuildConfig.baseURL + "handler/BeneficiaryRe_RegistrationOTP.ashx";
    public static final String BeneficiaryRe_RegistrationNew = BuildConfig.baseURL + "handler/BeneficiaryRe_RegistrationNew.ashx";
    public static final String InsertBreastSceeningDeviceDetails = BuildConfig.baseURL + "handler/InsertBreastSceeningDeviceDetails.ashx";
    public static final String InsertBreastScreeningDetails = BuildConfig.baseURL + "handler/InsertBreastScreeningDetails.ashx";
    public static final String InsertBreastScreening_PDF = BuildConfig.baseURL + "handler/InsertBreastScreening_PDF.ashx";
    public static final String CW_CampCreation = BuildConfig.baseURL + "handler/CW_CampCreation.ashx";
    public static final String AntigenTestDetails = BuildConfig.baseURL + "handler/AntigenTestDetails.ashx";
    public static final String CW_CampCreationNew = BuildConfig.baseURL + "handler/CW_CampCreationNew.ashx";
    public static final String CW_CampCreationNewD2d = BuildConfig.baseURL + "handler/CW_CampCreationNew_V1.ashx";
    public static final String CW_CentriFugeConfirmation = BuildConfig.baseURL + "handler/CW_CourierModule.ashx";
    public static final String courier_handler = BuildConfig.baseURL + "handler/Handler1.ashx";
    public static final String InsertCloseBreastScreening = BuildConfig.baseURL + "handler/InsertCloseBreastScreening.ashx";
    public static final String InsertSignatureandThumbDetails = BuildConfig.baseURL + "handler/InsertSignatureandThumbDetails.ashx";
    public static final String InsertRationCardDetails = BuildConfig.baseURL + "handler/CW_Registration_RationCard.ashx";
    public static final String InsertBreastScreening_CSV = BuildConfig.baseURL + "handler/InsertBreastScreening_CSV.ashx";
    public static final String InsertBreastScreening_CSVNew = BuildConfig.baseURL + "handler/InsertBreastScreening_CSVNew.ashx";
    public static final String InsertBreastScreening_CSVNew1 = BuildConfig.baseURL + "handler/InsertBreastScreening_CSVNew1.ashx";
    public static final String trfPhotoHandler = BuildConfig.baseURL + "handler/UploadTRFPhotoDetails.ashx";
    public static final String siteMappingHandler = BuildConfig.baseURL + "handler/GLO_ApprovedWorkerSiteMapping.ashx";
    public static final String instantRegHandler = BuildConfig.baseURL + "handler/GLO_ApprovedWorkerSiteMappingNEW.ashx";
    public static final String ecgHandler = BuildConfig.baseURL + "handler/InsertEcgDetails.ashx";
    public static final String workerRegNoAssignHandler = BuildConfig.baseURL + "handler/InsCW_RegistrationDetails.ashx";
    public static final String InsertConstructionWorkDetails = BuildConfig.baseURL + "handler/InsertConstructionWorkDetails.ashx";
    public static final String CWPatientSignhandlerNew1 = BuildConfig.baseURL + "handler/CWPatientSignhandlerNew1.ashx";
    public static final String CWPatientSignhandlerNew2 = BuildConfig.baseURL + "handler/CWPatientSignhandlerNew2.ashx";
    public static final String CWPatientSignhandlerNew3 = BuildConfig.baseURL + "handler/InsertSignatureandThumbDetails_V1_RC.ashx";
    public static final String InsertAudioImages = BuildConfig.baseURL + "handler/InsertAudioImages.ashx";
    public static final String InsertLFTImages = BuildConfig.baseURL + "handler/LFTFileSaveHandler.ashx";
    public static final String InsertAudioImages_New = BuildConfig.baseURL + "handler/InsertAudioImages_NEW.ashx";
    public static final String InsertAudioImages_version = BuildConfig.baseURL + "handler/InsertAudioImages_NEW_VersionNo.ashx";
    public static final String InsertConstructionWorkDetails_Handler = "handler/InsertConstructionWorkDetails.ashx";
    public static final String InsertCWCampPatientInfo_Handler = "handler/InsertCWCampPatientInfo.ashx";
    public static final String CWPatientSignhandler_Handler = "handler/CWPatientSignhandler.ashx";
    public static final String InsertFingerPrintDetails_Handler = "handler/InsertFingerPrintDetails.ashx";
    public static final String FingerPrint = "handler/FingerPrint.ashx";
    public static final String InsertAdvertiesmentImages_Handler = "handler/InsertAdvertiesmentImages.ashx";
    public static final String InsertAudioandLungImageDetails_Handler = "handler/InsertAudioandLungImageDetails.ashx";
    public static final String CW_UplaodDoctorDetails_Handler = BuildConfig.baseURL + "handler/CW_UplaodDoctorDetails.ashx";
    public static final String CW_UploadDoctorDetailsNew = BuildConfig.baseURL + "handler/CW_UploadDoctorDetailsNew.ashx";
    public static final String CW_UploadDoctorDetailsNewV1 = BuildConfig.baseURL + "handler/CW_UploadDoctorDetailsNew_V1.ashx";
    public static final String PostCampAcknowlegedment = BuildConfig.baseURL + "handler/PostCampAcknowlegedment.ashx";
    public static final String CW_CampCreationForCSCEgov = BuildConfig.baseURL + "handler/CW_CampCreationForCSCEgov.ashx";


//    public static final String webservice_d2d = BuildConfig.baseURL + "D2D_V1.asmx/";
    public static final String webservice_d2d = BuildConfig.baseURL + "D2D_V2.asmx/";
    public static final String webservice_disha = BuildConfig.baseURL + "D2D_V2.asmx/";


    public static final String webservice_php = "https://reports.myhindlab.com/ReportsMCWLive/API/Liverscan/";
    public static final String webservice_campcreation = BuildConfig.baseURL + "CampCreation.asmx/";

    //  public static final String webservice_d2d = "http://mcwwb.tvetonline.in/webservices/D2D_V1.asmx/";

    public static final String webservice_forcallist = BuildConfig.baseURL + "BeneficiaryCalling.asmx/";
    public static final String webservice_twentyFourBySeven =  "https://app.office24by7.com/v1/communication/API/";

    public static final String webservice_twentyFourBySevenNew = "https://app.office24by7.com/v1/common/API/";

    public static final String webservice_mmuCamp =  "http://5.178.98.212:9999/disha-t2t-Apis/api/access/master/mmuDetails/";


    ///Beta
//    public static final String webservice_countSuper =  "http://5.178.98.212:9999/disha-t2t-Apis/api/access/master/countdata/";
//    public static final String webservice_countSuperForDownload =  "http://5.178.98.212:8080/disha-t2t-Apis/api/access/master/countdata/";


      ///Live
    public static final String webservice_countSuper =  "http://103.251.94.57:8080/disha-t2t-Apis/api/access/master/countdata/";
    public static final String webservice_countSuperForDownload =  "http://103.251.94.57:8080/disha-t2t-Apis/api/access/master/countdata/";


}