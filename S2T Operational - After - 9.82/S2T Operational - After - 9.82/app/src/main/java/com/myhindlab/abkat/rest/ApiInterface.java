package com.myhindlab.abkat.rest;

import com.google.gson.JsonObject;
import com.myhindlab.abkat.abha.models.ABHASessionModel;
import com.myhindlab.abkat.abha.models.CreateHealthIdModel;
import com.myhindlab.abkat.abha.models.SearchHealthIDRequestModel;
import com.myhindlab.abkat.abha.models.SendOTPRequestModel;
import com.myhindlab.abkat.abha.models.abha_creation_response_via_aadhaar_demo_auth.EnrollByAadhaarViaDemoAuthCreationResponseModel;
import com.myhindlab.abkat.abha.models.add_context.AddContextRequestModel;
import com.myhindlab.abkat.abha.models.add_context.CareContextLinkingRequestModel;
import com.myhindlab.abkat.abha.models.confirm_auth.ConfirmAuthRequestModel;
import com.myhindlab.abkat.abha.models.context_notify.CareContextNotifyRequestModel;
import com.myhindlab.abkat.abha.models.district_list.DistrictOnStateListResponseModel;
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar.EnrolByAadhaarRequestModel;
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.EnrollByAadhaarViaDemoAuth;
import com.myhindlab.abkat.abha.models.fetch_auth_modes.FetchAuthRequestModel;
import com.myhindlab.abkat.abha.models.init_auth.InitAuthRequestModel;
import com.myhindlab.abkat.abha.models.sms_notify.SMSNotifyRequestModel;
import com.myhindlab.abkat.abha.models.state_list.StateListResponseModel;
import com.myhindlab.abkat.abha.models.verify_mobile_otp.VerifyMobileOTPRequestModel;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceDetailsModel;
import com.myhindlab.abkat.activities.attendance_details.model.AttendanceImageModel;
import com.myhindlab.abkat.activities.attendance_details.model.BeneficiaryConsentModel;
import com.myhindlab.abkat.activities.attendance_details.model.BeneficiaryStatusModel;
import com.myhindlab.abkat.activities.attendance_details.model.CampConfirmationModel;
import com.myhindlab.abkat.activities.calling_dashboard.model.CallingDashboardModel;
import com.myhindlab.abkat.activities.campApproval.models.AllocatedResourceListModel;
import com.myhindlab.abkat.activities.campApproval.models.CampDetailsModel;
import com.myhindlab.abkat.activities.campApproval.models.CampStatusCountModel;
import com.myhindlab.abkat.activities.campApproval.models.CampStatusListModel;
import com.myhindlab.abkat.activities.campApproval.models.ResourceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.campredinessnew.CampRedinessDataResponse;
import com.myhindlab.abkat.activities.campredinessnew.InsertCampResponse;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CTMedicineDetailsModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryListModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryStatusModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.DepartMentTypeModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.GetMobileNumberModel;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.ProcessingLabCountActivity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.PendingProcessingCountModel;
import com.myhindlab.abkat.activities.payout.model.CompanyListModel;
import com.myhindlab.abkat.activities.payout.model.InvoiceMonthWiseDetailsnModel;
import com.myhindlab.abkat.activities.payout.model.MonthModel;
import com.myhindlab.abkat.activities.payout.model.PaymentDetailsModel;
import com.myhindlab.abkat.activities.payout.model.VerificationRemarkModel;
import com.myhindlab.abkat.activities.payout.model.YearModel;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryListModel;
import com.myhindlab.abkat.activities.re_registration.model.CountsForDailyWorkDashboardModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.AllocatedSubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.CustomerTestResponseModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.GetLabOnDistrictListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ProductStockListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ResourceListForMappingModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SiteListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.TestListForResourceMappingModel;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallingRemarkModel;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallstatusResponse;
import com.myhindlab.abkat.appointment_confirmation.insertapi.ResponseModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.DateTypesModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.DependentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.appointment_confirmation.pojo.VodaphoneCallModel;
import com.myhindlab.abkat.expense_module.models.advance_detail_response.AdvanceDetailsResponseModel;
import com.myhindlab.abkat.expense_module.models.expense_head.ExpenseHeadListModel;
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.SubExpenseHeadResponseModel;
import com.myhindlab.abkat.expense_module.models.see_requested_advance_detail.SeeAdvanceRequestResponseModel;
import com.myhindlab.abkat.models.AndroidIosCountModel;
import com.myhindlab.abkat.models.AndroidMobileCountModel;
import com.myhindlab.abkat.models.AutoLogoutResponse;
import com.myhindlab.abkat.models.AvailabilityTestModel;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampLocationModel;
import com.myhindlab.abkat.models.CampTypeResponseModel;
import com.myhindlab.abkat.models.DependentModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.FaceDetectionModel;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;
import com.myhindlab.abkat.models.GetDependentListModel;
import com.myhindlab.abkat.models.GetFingerPrintPathResponce;
import com.myhindlab.abkat.models.GetInitiatedByListForCampModel;
import com.myhindlab.abkat.models.GetListOfLungandAudioImageDetails_Responce;
import com.myhindlab.abkat.models.GpModel;
import com.myhindlab.abkat.models.InsertBasicHealthInfoResponce;
import com.myhindlab.abkat.models.MachineDataStatusModel;
import com.myhindlab.abkat.models.MahabocwInsertApiResponse;
import com.myhindlab.abkat.models.MiniCampQuestionsModel;
import com.myhindlab.abkat.models.MmuSchedulesDataModel;
import com.myhindlab.abkat.models.PendingCountModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.ProcessingLabCountModel;
import com.myhindlab.abkat.models.Responce;
import com.myhindlab.abkat.models.ScreenedDependentModel;
import com.myhindlab.abkat.models.SuperAdminCountModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.UniteIdModel;
import com.myhindlab.abkat.models.WorkerDependentModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.models.doortodoor.AssignedPhysicalExamModel;
import com.myhindlab.abkat.models.doortodoor.CallRequest;
import com.myhindlab.abkat.models.doortodoor.CallRequestForVodaphone;
import com.myhindlab.abkat.models.doortodoor.CallRequestNew;
import com.myhindlab.abkat.models.doortodoor.CheckListResponseModel;
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails;
import com.myhindlab.abkat.models.doortodoor.GetCampIDWiseTeamDetailsResponseModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.models.doortodoor.d2d_camp_activity.GetLocationDetailsByLabCodeResponseModel;
import com.myhindlab.abkat.models.doortodoor.d2d_camp_activity.TeamDetailsByCampIDResponseModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.pojos.GetApprovedCampListDetailsForApp_Pojo;
import com.myhindlab.abkat.pojos.InsertAdvertiesmentImagesResponse;
import com.myhindlab.abkat.pojos.InsertAudioandLungImageDetailsResponse;
import com.myhindlab.abkat.pojos.InsertCWCampPatientInfoResponse;
import com.myhindlab.abkat.pojos.InsertConstructionWorkDetailsResponse;
import com.myhindlab.abkat.pojos.InsertFingerPrintDetailsResponse;
import com.myhindlab.abkat.pojos.doortodoor.RelationPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiInterface {
//    @GET(ApplicationConstant.UserLogin)
//    Call<UserLoginResponse> UserLogin(@Query("UserName") String userName, @Query("Password") String password);

    /*
     * @FormUrlEncoded : Point out this method will construct a form submit action.
     * @POST : Point out the form submit will use post method, the form action url is the parameter of @POST annotation.
     * @Field("form_field_name") : Indicate the form filed name, the filed value will be assigned to input parameter userNameValue.
     * */
    @Multipart
    @POST(ApplicationConstants.InsertConstructionWorkDetails_Handler)
    Call<InsertConstructionWorkDetailsResponse> InsertConstructionWorkDetails(@Query("ReraId") String ReraId,
                                                                              @Query("SiteDetailId") String SiteDetailId,
                                                                              @Query("Userid") String Userid,
                                                                              @Query("FileType") String FileType,
                                                                              @Query("BuilderDetailID") String BuilderDetailID,
                                                                              @Part MultipartBody.Part file);

    @Multipart
    @POST(ApplicationConstants.InsertAudioandLungImageDetails_Handler)
    Call<InsertAudioandLungImageDetailsResponse> InsertAudioandLungImageDetails(@Query("RegdId") String RegdId,
                                                                                @Query("ImageName") String ImageName,
                                                                                @Query("CreatedBy") String CreatedBy,
                                                                                @Part MultipartBody.Part file);

//    @Multipart
//    @POST(ApplicationConstants.InsertConstructionWorkDetails_Handler)
//    Call<InsertConstructionWorkDetailsResponse> InsertConstructionWorkDetails(@Part("ReraId") RequestBody ReraId,
//                                                                              @Part("SiteDetailId") RequestBody SiteDetailId,
//                                                                              @Part("Userid") RequestBody Userid,
//                                                                              @Part("FileType") RequestBody FileType,
//                                                                              @Part("BuilderDetailID") RequestBody BuilderDetailID,
//                                                                              @Part MultipartBody.Part file);

    @Multipart
    @POST(ApplicationConstants.FingerPrint)
    Call<InsertFingerPrintDetailsResponse> InsertFingerPrintDetails(@Query("RegType") String RegType,
                                                                    @Query("RegId") String RegId,
                                                                    @Query("Filetype") String Filetype,
                                                                    @Query("CreatedBy") String CreatedBy,
                                                                    @Part MultipartBody.Part file);


//    @Headers({
//            "Content-Type: application/json",
//            "x-api-key: oomfKA3I2K6TCJYistHyb7sDf0l0F6c8AZro5DJh"
//    })
//    @POST("obd-api-v1")
//    Call<ResponseBody> makeOBDCall(@Body CallRequest request);


    @Headers("Content-Type: application/json")
    @POST("obd-api-v1")
    Call<ResponseBody> makeOBDCall(
            @Header("x-api-key") String apiKey,
            @Body CallRequest request
    );


    @Headers("Content-Type: application/json")
    @POST("obd-api-v1")
    Call<ResponseBody> makeOBDCallNew(
            @Header("x-api-key") String apiKey,
            @Body CallRequestNew request
    );


    @Headers("Content-Type: application/json")
    @POST("initiate-call")
    Call<ResponseBody> vodaphoneCall(
            @Header("Authorization") String bearerToken,
            @Body CallRequestForVodaphone request
    );


    @Headers("Content-Type: application/json")
    @POST("AuthToken")
    Call<ResponseBody> getToken(@Body JsonObject body);


    @POST(ApplicationConstants.GetAPIDishaToken)
    @FormUrlEncoded
    Call<ResponseBody> getDishaAPIToken(@Field("username") String username,
                                @Field("password") String password);


    @POST(ApplicationConstants.InsertBasicInfoMale_V1)
    @FormUrlEncoded
    Call<ResponseBody> insertBasicInfoMale_V1(@Field("RegdId") int RegdId,
                                              @Field("CampId") int CampId,
                                              @Field("LungClear") int LungClear,
                                              @Field("LungComment") String LungComment,
                                              @Field("AbnormalSound") int AbnormalSound,
                                              @Field("AbnormalComment") String AbnormalComment,
                                              @Field("OtherAbnormality") int OtherAbnormality,
                                              @Field("SIS2Normal") int SIS2Normal,
                                              @Field("AnyMurmurs") int AnyMurmurs,
                                              @Field("CVSComment") String CVSComment,
                                              @Field("Palpationabdomen") int Palpationabdomen,
                                              @Field("Anyhernia") int Anyhernia,
                                              @Field("MassperAbdomen") int MassperAbdomen,
                                              @Field("GITComment") String GITComment,
                                              @Field("PatwellOriented") int PatwellOriented,
                                              @Field("Sensory") int Sensory,
                                              @Field("Motor") int Motor,
                                              @Field("SkinInfection") String SkinInfection,
                                              @Field("CNSComment") String CNSComment,
                                              @Field("FinalRemark") int FinalRemark,
                                              @Field("FinalComment") String FinalComment,
                                              @Field("Createdby") int Createdby,
                                              @Field("jsonstring") String jsonstring,
                                              @Field("IsCovid") int IsCovid,
                                              @Field("IsDose1") int IsDose1,
                                              @Field("IsDose2") int IsDose2,
                                              @Field("IsBoosterDose") int IsBoosterDose,
                                              @Field("jsonstringFamily") String jsonstringFamily

    );




//    @POST(ApplicationConstants.InsertDisha)
//    @FormUrlEncoded
//    Call<ResponseBody> InsertDishaAPI(@Header("Authorization") String bearerToken,
//                                      @Field("patientId") int patientId,
//                                              @Field("title") String title,
//                                              @Field("fname") String fname,
//                                              @Field("mname") String mname,
//                                              @Field("lname") String lname,
//                                              @Field("gender") String gender,
//                                              @Field("mobile") String mobile,
//                                              @Field("ageYears") String ageYears,
//                                              @Field("ageMonth") String ageMonth,
//                                              @Field("ageDay") String ageDay,
//                                              @Field("address") String address,
//                                              @Field("email") String email,
//                                              @Field("unitId") String unitId,
//                                              @Field("createdBy") String createdBy,
//                                              @Field("weight") String weight,
//                                              @Field("height") String height,
//                                              @Field("collectedDate") String collectedDate,
//                                              @Field("collectedTime") String collectedTime,
//                                              @Field("customerId") String customerId,
//                                              @Field("visitCode") String visitCode,
//                                              @Field("campId") String campId,
//                                              @Field("hmisPatientId") String hmisPatientId,
//                                              @Field("totalAmount") String totalAmount,
//                                              @Field("listTestDetails") String listTestDetails
//
//    );

    @POST(ApplicationConstants.InsertDisha)
    Call<ResponseBody> InsertDishaAPI(
            @Header("Authorization") String token,
            @Body JsonObject body
    );


    @POST(ApplicationConstants.InsertBasicInfoMale_VersionNoNew)
    @FormUrlEncoded
    Call<ResponseBody> insertBasicInfoMale_V1VersionNumber(@Field("RegdId") int RegdId,
                                                           @Field("CampId") int CampId,
                                                           @Field("LungClear") int LungClear,
                                                           @Field("LungComment") String LungComment,
                                                           @Field("AbnormalSound") int AbnormalSound,
                                                           @Field("AbnormalComment") String AbnormalComment,
                                                           @Field("OtherAbnormality") int OtherAbnormality,
                                                           @Field("SIS2Normal") int SIS2Normal,
                                                           @Field("AnyMurmurs") int AnyMurmurs,
                                                           @Field("CVSComment") String CVSComment,
                                                           @Field("Palpationabdomen") int Palpationabdomen,
                                                           @Field("Anyhernia") int Anyhernia,
                                                           @Field("MassperAbdomen") int MassperAbdomen,
                                                           @Field("GITComment") String GITComment,
                                                           @Field("PatwellOriented") int PatwellOriented,
                                                           @Field("Sensory") int Sensory,
                                                           @Field("Motor") int Motor,
                                                           @Field("SkinInfection") String SkinInfection,
                                                           @Field("CNSComment") String CNSComment,
                                                           @Field("FinalRemark") int FinalRemark,
                                                           @Field("FinalComment") String FinalComment,
                                                           @Field("Createdby") int Createdby,
                                                           @Field("jsonstring") String jsonstring,
                                                           @Field("IsCovid") int IsCovid,
                                                           @Field("IsDose1") int IsDose1,
                                                           @Field("IsDose2") int IsDose2,
                                                           @Field("IsBoosterDose") int IsBoosterDose,
                                                           @Field("jsonstringFamily") String jsonstringFamily,
                                                           @Field("VersionNo") String VersionNo


    );


//    @POST(ApplicationConstants.InsertPhysicalExaminationForHSCC)
//    @FormUrlEncoded
//    Call<ResponseBody> insertBasicInfoMale_V1New
//            (@Field("Createdby") int Createdby,
//             @Field("T_PhysicalexaminationAndDescription") String T_PhysicalexaminationAndDescription);


    @POST(ApplicationConstants.InsertPhysicalExaminationForHSCC_V1)
    @FormUrlEncoded
    Call<ResponseBody> insertBasicInfoMale_V1New
            (@Field("Createdby") int Createdby,
             @Field("T_PhysicalexaminationAndDescription") String T_PhysicalexaminationAndDescription,
             @Field("IsAbnormal") String IsAbnormal,
             @Field("Camptype") String Camptype

            );

    @POST(ApplicationConstants.InsertPhysicalExaminationForHSCC_VersionNo_V2)
    @FormUrlEncoded
    Call<ResponseBody> insertBasicInfoMale_V1NewVersionNumber
            (@Field("Createdby") int Createdby,
             @Field("T_PhysicalexaminationAndDescription") String T_PhysicalexaminationAndDescription,
             @Field("IsAbnormal") String IsAbnormal,
             @Field("Camptype") String Camptype,
             @Field("VersionNo") String VersionNo

            );

    @POST(ApplicationConstants.SaveAndroidToken_V1)
    @FormUrlEncoded
    Call<ResponseBody> insertSaveToken
            (@Field("USERID") int USERID,
             @Field("ANDROIEDTOKEN") String ANDROIEDTOKEN,
             @Field("ActiveStatus") String ActiveStatus

            );

    @POST(ApplicationConstants.UpdateVodafoneToken)
    @FormUrlEncoded
    Call<ResponseBody> updateToken
            (@Field("Token") String Token


            );

    @POST(ApplicationConstants.UpdateLogOutDetails)
    @FormUrlEncoded
    Call<ResponseBody> insertLogout
            (@Field("MobileNo") String MobileNo);

    @POST(ApplicationConstants.UpdateNewUserPassword)
    @FormUrlEncoded
    Call<ResponseBody> insertForgotPassword
            (@Field("Pwd") String Pwd, @Field("MobNo") String MobNo, @Field("Otp") String Otp);


    @POST(ApplicationConstants.InsertUserInOutAttendance)
    @FormUrlEncoded
    Call<ResponseBody> insertAttendance
            (@Field("Userid") String Userid, @Field("LATITUDE") String LATITUDE, @Field("LONGITUDE") String LONGITUDE, @Field("ATTENDANCEDATE") String ATTENDANCEDATE);

    @POST(ApplicationConstants.VerifyCTOTP)
    @FormUrlEncoded
    Call<ResponseBody> insertVerifyOtp
            (@Field("MobNo") String MobNo, @Field("Otp") String Otp);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampListForApprove)
    Call<CampStatusListModel> getCampListForApprove(
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("IsInternalApproval") int IsInternalApproval);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampListForApproveForPartner)
    Call<CampStatusListModel> getCampListForApproveForPartner(
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("IsInternalApproval") int IsInternalApproval);

    @POST("GetApprovedCampListDetailsForApp_FlexiCamp_V1")
    @FormUrlEncoded
    Call<GetApprovedCampListDetailsForApp_Pojo> getApprovedCampListDetails(
            @Field("CampDATE") String campDate,
            @Field("SubOrgId") String subOrgId,
            @Field("Divison") String division,
            @Field("DISTLGDCODE") String distCode,
            @Field("USERID") String userId,
            @Field("DesgId") String desgId
    );


//  @FormUrlEncoded
//  @POST("InsertChangePasswordRequest")
//  Call<ResponseBody> sendOtp(
//          @Field("Mobileno") String mobileNo,
//          @Field("OTP") String otp
//  );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetLabProductStockListByLab)
    Call<ProductStockListModel> getLabProductStockListByLab(@Field("LabId") String LabId);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampConsumptionDetails_ForAppForIntApproval)
    Call<ConsumableDetailsForApprovalModel> getCampConsumptionDetails_ForAppForIntApproval(@Field("campid") int campId);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetApproveSubDeviceListForCampAllo)
    Call<AllocatedSubDeviceListModel> getApproveSubDeviceListForCampAllo(@Field("DevicesId") int devicesId, @Field("Campdate") String campDate, @Field("Campid") String Campid);


    @FormUrlEncoded
    @POST("InsertCampReadinessFormDetails")
    Call<InsertCampResponse> SentInsertCampredinessdetails(@Field("CampID") String CampID, @Field("CampType") String CampType, @Field("Createdby") String Createdby, @Field("TeamId") String TeamId, @Field("Type_CampReadinessForm") String Type_CampReadinessForm);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampResourceDetails_ForAppForIntApproval)
    Call<ResourceDetailsForApprovalModel> getCampResourceDetails_ForAppForIntApproval(@Field("campid") int campId);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampDetails_ForAppForIntApproval)
    Call<CampDetailsModel> getCampDetails_ForAppForIntApproval(@Field("campid") int campId);

    @FormUrlEncoded
    @POST(ApplicationConstants.UpdateCampMappingResources)
    Call<ResponseBody> updateCampMappingResources(@Field("ResourceUserId") int resourceUserId, @Field("CampId") int campId, @Field("TestId") int testId, @Field("UserId") int userId);

    @FormUrlEncoded
    @POST(ApplicationConstants.RemoveCampMappingResources)
    Call<ResponseBody> removeCampMappingResources(@Field("ResourceUserId") int resourceUserId, @Field("CampId") int campId, @Field("TestId") int testId, @Field("UserId") int userId);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampDeviceDetails_ForAppForIntApproval)
    Call<DeviceDetailsForApprovalModel> getCampDeviceDetails_ForAppForIntApproval(@Field("campid") int campId);

    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationWithoutApprovalForPartner)
    Call<ResponseBody> InserCampCreationWithoutApprovalForPartner(
            @Field("UserId") String UserId,
            @Field("SiteMappingJSON") String SiteMappingJSON,
            @Field("CampDetailsJson") String CampDetailsJson,
            @Field("DeviceMappingJson") String DeviceMappingJson,
            @Field("ConsumableMappingJson") String ConsumableMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson);


    @GET(ApplicationConstants.GetCampReadinessFormItems)
    Call<CampRedinessDataResponse> GetCampReadinessFormItemsnew(@Query("CampID") String CampID, @Query("TeamId") String TeamId);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetApproveResourcelstForUpdate)
    Call<AllocatedResourceListModel> getApproveResourcelstForUpdate(@Field("TestId") int TestId, @Field("Campdate") String campDate, @Field("Campid") String Campid, @Field("LabCode") String LabCode, @Field("DISTLGDCODE") int DISTLGDCODE);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampSiteDetails_ForAppForIntApproval)
    Call<SiteListModel> getCampSiteDetails_ForAppForIntApproval(@Field("campid") int campId);


    @FormUrlEncoded
    @POST("GetTeamListCC")
    Call<TeamsDetailsModel> getTeamDataByUserId(
            @Field("UserID") String userId
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampCountApproveHolePendingCount)
    Call<CampStatusCountModel> getCampCountApproveHolePendingCount(
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_RemovePincode_V1)
    Call<CountsForDailyWorkDashboardModel> getCountForReregistration(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryDashboardForMob)
    Call<CountsForDailyWorkDashboardModel> getCountForAdmin(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Arid") String Arid,
            @Field("UserId") String UserId,
            @Field("DESGID") String DESGID,
            @Field("Type") String Type

    );

    //    @FormUrlEncoded
//    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetailsCount_V1)
//    Call<CountsForDailyWorkDashboardModel> getCountForPageload(
//            @Field("Fromdate") String Fromdate,
//            @Field("Todate") String Todate,
//            @Field("SubOrgId") String SubOrgId,
//            @Field("DIVID") String DIVID,
//            @Field("DISTLGDCODE") String DISTLGDCODE,
//            @Field("TALLGDCODE") String TALLGDCODE,
//            @Field("Labcode") String Labcode,
//            @Field("Arid") String Arid,
//            @Field("BeneficiaryNumber") String BeneficiaryNumber,
//            @Field("UserId") String UserId,
//            @Field("Type") String Type,
//            @Field("CampType") String CampType
//
//
//    );
    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_Count_V1)
    Call<CountsForDailyWorkDashboardModel> getCountForPageload(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("DesgID") String DesgID,
            @Field("CampType") String CampType


    );

//    @FormUrlEncoded
//    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetailsCount_Team_V1)
//    Call<CountsForDailyWorkDashboardModel> getCountForPageloadForTeam(
//            @Field("Fromdate") String Fromdate,
//            @Field("Todate") String Todate,
//            @Field("SubOrgId") String SubOrgId,
//            @Field("DIVID") String DIVID,
//            @Field("DISTLGDCODE") String DISTLGDCODE,
//            @Field("TALLGDCODE") String TALLGDCODE,
//            @Field("Labcode") String Labcode,
//            @Field("Arid") String Arid,
//            @Field("BeneficiaryNumber") String BeneficiaryNumber,
//            @Field("UserId") String UserId,
//            @Field("Type") String Type,
//            @Field("CampType") String CampType
//
//    );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_Count_Team_V1)
    Call<CountsForDailyWorkDashboardModel> getCountForPageloadForTeam(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );

    //    @FormUrlEncoded
//    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetailsCount_V1)
//    Call<BeneficiaryListModel> getCountForPageloadBeneficiary(
//            @Field("Fromdate") String Fromdate,
//            @Field("Todate") String Todate,
//            @Field("SubOrgId") String SubOrgId,
//            @Field("DIVID") String DIVID,
//            @Field("DISTLGDCODE") String DISTLGDCODE,
//            @Field("TALLGDCODE") String TALLGDCODE,
//            @Field("Labcode") String Labcode,
//            @Field("Arid") String Arid,
//            @Field("BeneficiaryNumber") String BeneficiaryNumber,
//            @Field("UserId") String UserId,
//            @Field("Type") String Type,
//            @Field("CampType") String CampType
//
//    );
    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_Count_V1)
    Call<BeneficiaryListModel> getCountForPageloadBeneficiary(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("DesgID") String DesgID,
            @Field("CampType") String CampType

    );

    //    @FormUrlEncoded
//    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetailsCount_Team_V1)
//    Call<BeneficiaryListModel> getCountForPageloadBeneficiaryForTeam(
//            @Field("Fromdate") String Fromdate,
//            @Field("Todate") String Todate,
//            @Field("SubOrgId") String SubOrgId,
//            @Field("DIVID") String DIVID,
//            @Field("DISTLGDCODE") String DISTLGDCODE,
//            @Field("TALLGDCODE") String TALLGDCODE,
//            @Field("Labcode") String Labcode,
//            @Field("Arid") String Arid,
//            @Field("BeneficiaryNumber") String BeneficiaryNumber,
//            @Field("UserId") String UserId,
//            @Field("Type") String Type,
//            @Field("CampType") String CampType
//
//    );
    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_Count_Team_V1)
    Call<BeneficiaryListModel> getCountForPageloadBeneficiaryForTeam(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );


    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryToTeam_RemovePincode_V1)
    Call<CountsForDailyWorkDashboardModel> getCountForTeam(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );


    @FormUrlEncoded
    @POST("GetCampDetailsonLabForDoorToDoor_V2")
    Call<D2DCampDetails> getCampDetailsOnLab(
            @Field("CampDate") String campDate,
            @Field("LabCode") String labCode,
            @Field("SubOrgId") String subOrgId,
            @Field("Divison") String division,
            @Field("DISTLGDCODE") String distCode,
            @Field("USERID") String userId,
            @Field("DesgId") String desgId
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.GetPincode)
    Call<PincodeListModel> getPincodeList(
            @Field("T_AreaofPincode") String T_AreaofPincode


    );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_RemovePincode_V1)
    Call<BeneficiaryListModel> getCountForReregistrationForCC(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );


    //    @FormUrlEncoded
//    @POST(ApplicationConstants.GetTotalcampAndTotalBeneficiarywithZeroCamp_Org)
//    Call<AdminActiveInactiveModel> getTotalCampAndBeneficiaryData(
//            @Field("MonthId") String monthId,
//            @Field("Year") String year,
//            @Field("Distcode") String distCode,
//            @Field("CampType") String campType,
//            @Field("SubOrgId") String subOrgId,
//            @Field("DIVID") String divId,
//            @Field("UserId") String userId,
//            @Field("DESGID") String desgId
//    );
    @FormUrlEncoded
    @POST(ApplicationConstants.GetTotalAndTodaysBeneficiaryCountWithProcessCount)
    Call<PendingProcessingCountModel> getTotalCampAndBeneficiaryData(
            @Field("MonthId") String monthId,
            @Field("Year") String year,
            @Field("Distcode") String distCode,
            @Field("CampType") String campType,
            @Field("SubOrgId") String subOrgId,
            @Field("DIVID") String divId,
            @Field("UserId") String userId,
            @Field("DESGID") String desgId
    );

    @FormUrlEncoded
    @POST("GetTotalcampAndTotalBeneficiarywithZeroCamp_Org_Cluster")
    Call<AdminActiveInactiveModel> getTotalCampAndBeneficiaryDataClusterWise(
            @Field("MonthId") String monthId,
            @Field("Year") String year,
            @Field("Distcode") String distCode,
            @Field("CampType") String campType,
            @Field("SubOrgId") String subOrgId,
            @Field("DIVID") String divId,
            @Field("UserId") String userId,
            @Field("DESGID") String desgId
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryToTeam_RemovePincode_V1)
    Call<BeneficiaryListModel> getCountForReregistrationForTeam(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails_RemovePincode_V1)
    Call<BeneficiaryListModel> getBeneficiaryData(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );

    @FormUrlEncoded
    @POST(ApplicationConstants.GetRecollectionBeneficiaryToTeam_RemovePincode_V1)
    Call<BeneficiaryListModel> getBeneficiaryDataForTeam(
            @Field("Fromdate") String Fromdate,
            @Field("Todate") String Todate,
            @Field("SubOrgId") String SubOrgId,
            @Field("DIVID") String DIVID,
            @Field("DISTLGDCODE") String DISTLGDCODE,
            @Field("TALLGDCODE") String TALLGDCODE,
            @Field("Labcode") String Labcode,
            @Field("Arid") String Arid,
            @Field("BeneficiaryNumber") String BeneficiaryNumber,
            @Field("UserId") String UserId,
            @Field("Type") String Type,
            @Field("CampType") String CampType

    );


    @POST(ApplicationConstants.GetDistrictListWithGloMapping)
    @FormUrlEncoded
    Call<ResponseBody> getDistrictListWithGloMapping(@Field("UserId") int UserId);


    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationD2DWithoutApprovalNew)
    Call<ResponseBody> inserCampCreationD2DWithoutApprovalNew(
            @Field("UserId") String UserId,
            @Field("ISD2DCamp") String ISD2DCamp,
            @Field("SiteMappingJSON") String SiteMappingJSON,
            @Field("CampDetailsJson") String CampDetailsJson,
            @Field("DeviceMappingJson") String DeviceMappingJson,
            @Field("ConsumableMappingJson") String ConsumableMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson,
            @Field("PartnerIDForCampCreate") int PartnerIDForCampCreate
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationD2DWithoutApproval)
    Call<ResponseBody> inserCampCreationD2DWithoutApproval(
            @Field("DeviceMappingJson") String DeviceMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson,
            @Field("UserId") String UserId,
            @Field("CampId") String CampId
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.InsertInternalCampApproval)
    Call<ResponseBody> insertInternalCampApproval(
            @Field("DevicApproval") String UserId,
            @Field("ResourcesAppro") String CampDetailsJson);


//    @Field("ConsumApproval") String SiteMappingJSON,


    @GET(ApplicationConstants.GetTestListForMapResourceDesgForD2d)
    Call<TestListForResourceMappingModel> getTestListForMapResourceDesgForD2d();

    @GET(ApplicationConstants.GetTestListForMapResourceDesg)
    Call<TestListForResourceMappingModel> getTestListForMapResourceDesg();


    @GET(ApplicationConstants.GetTestListDisha)
    Call<CustomerTestResponseModel> getTestListDisha(
            @Header("Authorization") String bearerToken,
            @Query("customerCode") String customerCode
    );

    @POST(ApplicationConstants.CheckFlagForCampCalender)
    @FormUrlEncoded
    Call<AdminActiveInactiveModel> getClusterWiseFlag(@Field("DESGID") int DESGID);


    @GET(ApplicationConstants.CheckFlagForTeamCampMapping)
    Call<AdminActiveInactiveModel> getClusterWiseFlagForMapping();

    @FormUrlEncoded
    @POST(ApplicationConstants.GetTestidFromResourcelistNewForPatnerResorces)
    Call<ResourceListForMappingModel> getTestidFromResourcelistNewForPatnerResorces(@Field("TestId") int TestId, @Field("Campdate") String campDate, @Field("DISTLGDCODE") int DISTLGDCODE, @Field("PartnerID") int PartnerID, @Field("LabCode") int LabCode);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetApproveResourcelistNew)
    Call<AllocatedResourceListModel> getApproveResourcelist(@Field("TestId") int TestId, @Field("Campdate") String campDate, @Field("Campid") String Campid, @Field("DISTLGDCODE") int DISTLGDCODE);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetTestidFromResourcelistNew)
    Call<ResourceListForMappingModel> getTestidFromResourcelist(@Field("TestId") int TestId, @Field("Campdate") String campDate, @Field("DISTLGDCODE") int DISTLGDCODE);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetSUBDeviceListNew)
    Call<SubDeviceListModel> getSubDeviceListForCamp(@Field("DevicesId") int devicesId, @Field("Campdate") String campDate, @Field("DISTLGDCODE") int DISTLGDCODE, @Field("LabCode") int LabCode);


    @FormUrlEncoded
    @POST(ApplicationConstants.InsertInternalCampApprovalForPartner)
    Call<ResponseBody> insertInternalCampApprovalForPartner(
            @Field("DevicApproval") String UserId,
            @Field("ConsumApproval") String SiteMappingJSON,
            @Field("ResourcesAppro") String CampDetailsJson);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetDeviceListForCamp)
    Call<DeviceListModel> getDeviceListForCamp(@Field("BeneficairyCount") int BeneficairyCount);


    @Multipart
    @POST(ApplicationConstants.FingerPrint)
    Call<InsertFingerPrintDetailsResponse> InsertFingerPrint(@Query("RegType") String RegType,
                                                             @Query("RegId") String RegId,
                                                             @Query("Filetype") String Filetype,
                                                             @Query("CreatedBy") String CreatedBy,
                                                             @Part MultipartBody.Part file);

    @Multipart
    @POST(ApplicationConstants.InsertAdvertiesmentImages_Handler)
    Call<InsertAdvertiesmentImagesResponse> InsertAdvertiesmentImages(@Query("CampId") String CampId,
                                                                      @Query("AdvTypeId") String AdvTypeId,
                                                                      @Query("AdvertisementBy") String AdvertisementBy,
                                                                      @Query("IsActive") String IsActive,
                                                                      @Query("CreatedBy") String CreatedBy,
                                                                      @Part MultipartBody.Part file);

    @GET(ApplicationConstants.GetListOfLungandAudioImageDetails)
    Call<GetListOfLungandAudioImageDetails_Responce> GetListOfLungandAudioImageDetails(@Query("Id") String Id);


    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationWithoutApproval)
    Call<ResponseBody> insertCampCreationWithoutApproval(
            @Field("UserId") String UserId,
            @Field("SiteMappingJSON") String SiteMappingJSON,
            @Field("CampDetailsJson") String CampDetailsJson,
            @Field("DeviceMappingJson") String DeviceMappingJson,
            @Field("ConsumableMappingJson") String ConsumableMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson
    );

    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationWithoutApprovalNew)
    Call<ResponseBody> inserCampCreationWithoutApprovalNew(
            @Field("UserId") String UserId,
            @Field("SiteMappingJSON") String SiteMappingJSON,
            @Field("CampDetailsJson") String CampDetailsJson,
            @Field("DeviceMappingJson") String DeviceMappingJson,
            @Field("ConsumableMappingJson") String ConsumableMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson,
            @Field("PartnerIDForCampCreate") int PartnerIDForCampCreate
    );


    @FormUrlEncoded
    @POST(ApplicationConstants.InserCampCreationD2DWithoutApprovalForPartner)
    Call<ResponseBody> inserCampCreationD2DWithoutApprovalForPartner(
            @Field("UserId") String UserId,
            @Field("ISD2DCamp") String ISD2DCamp,
            @Field("SiteMappingJSON") String SiteMappingJSON,
            @Field("CampDetailsJson") String CampDetailsJson,
//            @Field("DeviceMappingJson") String DeviceMappingJson,
//            @Field("ConsumableMappingJson") String ConsumableMappingJson,
            @Field("ResourceMappingJson") String ResourceMappingJson);


    @GET(ApplicationConstants.InsertMachineHearingTest_New)
    Call<Responce> InsertMachineHearingTest(@Query("CreatedBy") String CreatedBy,
                                            @Query("JsonString") String JsonString);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetLabOnDistrict)
    Call<GetLabOnDistrictListModel> getLabOnDistrict(@Field("DISTLGDCODE") int DISTLGDCODE);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetConsumableListForExpectedBenificiary)
    Call<ConsumableListModel> getConsumableListForExpectedBeneficiary(@Field("ExpectedBenificiaryCount") int expectedBenificiaryCount);


    @GET(ApplicationConstants.GetFingerPrintPath)
    Call<GetFingerPrintPathResponce> GetFingerPrintPath(@Query("Regid") String Regid);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    @POST(ApplicationConstants.bocwRegistration)
    Call<MahabocwInsertApiResponse> InsertMahabocwInsertApiResponse(
            @Header("x-auth") String xAuth,
            @Query("RegdNo") String RegdNo,
            @Query("RegistrationDate") String RegistrationDate,
            @Query("EnglishName") String EnglishName,
            @Query("MobileNo") String MobileNo,
            @Query("Aadhar") String Aadhar,
            @Query("Dob") String Dob,
            @Query("Age") int Age,
            @Query("Gender") String Gender,
            @Query("DueRenewalDate") String DueRenewalDate,
            @Query("DISTNAME") String DISTNAME,
            @Query("Taluka") String Taluka,
            @Query("ResidentialAddress") String ResidentialAddress,
            @Query("PermanentAddress") String PermanentAddress);

    @POST(ApplicationConstants.InsertBasicHealthInfo)
    Call<InsertBasicHealthInfoResponce> InsertBasicHealthInfo(@Query("RegdId") String RegdId,
                                                              @Query("CampId") String CampId,
                                                              @Query("Height_CMs") String Height_CMs,
                                                              @Query("Weight_KGs") String Weight_KGs,
                                                              @Query("BloodPressure") String BloodPressure,
                                                              @Query("BloodSugar_F") String BloodSugar_F,
                                                              @Query("BloodSugar_PP") String BloodSugar_PP,
                                                              @Query("BloodSugar_R") String BloodSugar_R,
                                                              @Query("BMI") String BMI,
                                                              @Query("BMIStatus") String BMIStatus,
                                                              @Query("CreatedBy") String CreatedBy);


    @FormUrlEncoded
    @POST("GetCTandMDCommonBeneficiaryDetails")
    Call<CTMedicineDetailsModel> getCTMedicineDetails(@Field("RegdId") String RegdId);


    //    @GET(ApplicationConstant.UserLogin)
//    Call<UserLoginResponse> UserLogin(@Query("UserName") String userName, @Query("Password") String password);

    @Multipart
    @POST(ApplicationConstants.InsertCWCampPatientInfo_Handler)
    Call<InsertCWCampPatientInfoResponse> InsertCWCampPatientInfo_Handler(
            @Query("SiteDetailId") String siteDetailId,
            @Query("WorkerRegNo") String workerregno,
            @Query("FNAME") String fname,
            @Query("MNAME") String mname,
            @Query("LNAME") String lname,
            @Query("DOB") String dob,
            @Query("AGE") String age,
            @Query("GENDER") String genderId,
            @Query("Height") String height,
            @Query("Weight") String weight,
            @Query("BMI") String bmi,
            @Query("MOBILE") String moblieno,
            @Query("Email") String empty,
            @Query("Address") String address,
            @Query("PostalCode") String pincode,
            @Query("InsertedBy") String USERID,
            @Query("Latitude") String Latitude,
            @Query("Longitude") String Longitude,
            @Query("MARITALSTATUSID") String MARITALSTATUSID,
            @Query("SpouseName") String nameofspouse,
            @Query("NoOfChildren") String noofchildren,
            @Query("AdharNumber") String aadhaarno,
            @Part MultipartBody.Part file);

    @GET(ApplicationConstants.GetQuestionnaire)
    Call<MiniCampQuestionsModel> getMiniCampQuestions();

    @FormUrlEncoded
    @POST(ApplicationConstants.GetBeneficiaryListByRegID)
    Call<WorkerDependentModel> getBeneficiaryListByRegID(@Field("Regdid") int regdId);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetAssignedBeneficiaryAndDoctorList)
    Call<AssignedPhysicalExamModel> getAssignedBeneficiaryAndDoctorList(@Field("Campid") String campId, @Field("userid") int userId);

    @FormUrlEncoded
    @POST(ApplicationConstants.InsertBeneficiaryDoctorMapping)
    Call<ResponseBody> insertBeneficiaryDoctorMapping(@Field("MapData") String regdId);

    @GET(ApplicationConstants.BindD2DCheckListData)
    Call<CheckListResponseModel> bindD2DCheckListData();

    @FormUrlEncoded
    @POST(ApplicationConstants.GetTeamDetailsByCampID)
    Call<TeamDetailsByCampIDResponseModel> getTeamDetailsByCampID(@Field("campid") int campId);

    @FormUrlEncoded
    @POST(ApplicationConstants.Insert_D2D_CampReadiness)
    Call<ResponseBody> insert_D2D_CampReadiness(@Field("CampCheckList") String CampCheckList, @Field("CreatedBy") int CreatedBy, @Field("CampID") int CampID);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetLocationDetailsByLabCode)
    Call<GetLocationDetailsByLabCodeResponseModel> getLocationDetailsByLabCode(@Field("LabCode") int LabCode, @Field("USERID") int USERID, @Field("Type") int Type);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetCampIDWiseTeamDetails)
    Call<GetCampIDWiseTeamDetailsResponseModel> getCampIDWiseTeamDetails(@Field("CampId") int CampId);


    @GET(ApplicationConstants.GetCampTypeAndCatagory)
    Call<CampTypeResponseModel> getCampTypeAndCategory();

    //    @GET(ApplicationConstants.GetLandingPageCountsDisplayforFinancialYear)
    @GET(ApplicationConstants.GetLandingPageCountsDisplayforFinancialYearForAllSubOrg)
    Call<ResponseBody> getLandingPageCountsDisplayforFinancialYear();

    //    @POST(ApplicationConstants.GetLandingPageCountsDisplayforFinancialYear_V1)
    @POST(ApplicationConstants.GetLandingPageCountsDisplayforFinancialYear_V1_WithSubOrg)
    Call<CampCalendarModel> getMonthlyForTodaysPatient();

    @FormUrlEncoded
    @POST(ApplicationConstants.GetAllDistrictList)
    Call<DistrictList_Pojo> getAllDistrictList(@Field("STATELGDCODE") int STATELGDCODE);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetMonthlySurveySiteRequest)
    Call<CampCalendarModel> getMonthlySurveySiteRequest(@Field("Month") int Month, @Field("Year") int Year, @Field("DistCode") int DistCode);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetMonthlySurveySiteRequestForOS)
    Call<CampCalendarModel> getMonthlySurveySiteRequestForOS(@Field("Month") int Month, @Field("Year") int Year, @Field("DistCode") int DistCode, @Field("CampType") int CampType);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetTodaysPatientCount)
    Call<CampCalendarModel> getMonthlySurveySiteRequestForOSNew(@Field("Date") String Date);

    @FormUrlEncoded


    @POST(ApplicationConstants.GetTodaysPatientCount_V1)
    Call<CampCalendarModel> getMonthlyForTodaysPatient(@Field("Date") String Date);


    @POST("GetAssignedExpectedBefForCallingMobileNew")
    @FormUrlEncoded
    Call<ReportlistResponse> REPORTLIST_RESPONSE_CALL(@Field("Userid") int Userid, @Field("CallStatusID") String CallStatusID, @Field("TeamID") String TeamID, @Field("GroupID") String GroupID);

    @POST("GetAssignedExpectedBefForCallingMobileNew_V1")
    @FormUrlEncoded
    Call<ReportlistResponse> REPORTLIST_RESPONSE_CALL_NEW(@Field("Userid") int Userid, @Field("CallStatusID") String CallStatusID, @Field("TeamID") String TeamID, @Field("GroupID") String GroupID, @Field("AssignDate") String AssignDate, @Field("CallingDateID") String CallingDateID);

    @POST("GetMonthWiseInvoiceStatus")
    @FormUrlEncoded
    Call<InvoiceGenModel> getInvoice(@Field("Year") int Year, @Field("UserID") String UserID, @Field("LoginUserID") String LoginUserID);

    @POST("GetMonthWiseInvoiceStatus_V1")
    @FormUrlEncoded
    Call<InvoiceGenModel> getInvoice(@Field("Year") int Year, @Field("UserID") String UserID, @Field("LoginUserID") String LoginUserID, @Field("DValueAv") String DValueAv);

    @POST("Get_Payout_GetMonthWiseDoctorInvoiceStatus")
    @FormUrlEncoded
    Call<InvoiceGenModel> getInvoiceForDoctor(@Field("Year") int Year, @Field("UserID") String UserID, @Field("LoginUserID") String LoginUserID);

    @POST("GetUserInvoicePaymentDetails")
    @FormUrlEncoded
    Call<PaymentDetailsModel> getPaymentDetails(@Field("InvoiceYear") int InvoiceYear, @Field("InvoiceMonth") String InvoiceMonth, @Field("UserID") String UserID, @Field("LoginUserID") String LoginUserID);

    @POST("Get_Payout_GetUserDoctorInvoicepaymentDetails")
    @FormUrlEncoded
    Call<PaymentDetailsModel> getPaymentDetailsForDoctor(@Field("InvoiceYear") int InvoiceYear, @Field("InvoiceMonth") String InvoiceMonth, @Field("UserID") String UserID, @Field("LoginUserID") String LoginUserID, @Field("PaidByCompanyID") String PaidByCompanyID);


    @POST("GetCampWiseInvoiceDetails")
    @FormUrlEncoded
    Call<InvoiceMonthWiseDetailsnModel> getInvoiceDetails(@Field("UserInviceID") String UserInviceID, @Field("InvoiceYear") String InvoiceYear, @Field("InvoiceMonth") String InvoiceMonth, @Field("UserID") String UserID, @Field("CampType") String CampType, @Field("LoginUserID") String LoginUserID);

    @POST(ApplicationConstants.GetTeamMembersAttendanceDetailsCampIDWise)
    @FormUrlEncoded
    Call<AttendanceDetailsModel> getAttendanceDetails(@Field("CampDATE") String CampDATE, @Field("TeamID") String TeamID, @Field("CampID") String CampID, @Field("StatusID") String StatusID);


    @POST(ApplicationConstants.GetDailyCallingReportForCallerLogin)
    @FormUrlEncoded
    Call<CallingDashboardModel> getCallingDashboardCount(@Field("FromDate") String FromDate, @Field("ToDate") String ToDate, @Field("CallingExcUserID") String CallingExcUserID, @Field("TeamId") String TeamId);


    @POST(ApplicationConstants.GetCampAttendanceImages_V1)
    @FormUrlEncoded
    Call<AttendanceImageModel> getCampImages(@Field("CampID") String CampID);


    @POST("GetCampWiseDoctorInvoiceDetails")
    @FormUrlEncoded
    Call<InvoiceMonthWiseDetailsnModel> getInvoiceDetailsForDoctor(@Field("UserInviceID") String UserInviceID, @Field("InvoiceYear") String InvoiceYear, @Field("InvoiceMonth") String InvoiceMonth, @Field("UserID") String UserID, @Field("CampType") String CampType, @Field("LoginUserID") String LoginUserID);

    @GET("counts.php")
    Call<ResponseBody> getLiverDashboardCount();


    @GET("GetStatusCTandMDForMasJas")
    Call<CommonBeneficiaryStatusModel> getStatus();

    @GET("GetStatusCTandMDForTeam")
    Call<CommonBeneficiaryStatusModel> getStatusForTeam();

    @GET("GetCallingTokenDetails")
    Call<VodaphoneCallModel> getVodaphoneAPIDetails();


    @POST("district-wise-count.php")
    @FormUrlEncoded
    Call<FibroscanDistrictWiseCountModel> getDistrictwiseDataLiverScan(@Field("fromdate") String fromdate, @Field("todate") String todate, @Field("userid") String userid, @Field("desgid") String desgid, @Field("suborgid") String suborgid, @Field("distlgdcode") String distlgdcode);

    @POST("GetCTandMDCommonBeneficiaryList")
    @FormUrlEncoded
    Call<CommonBeneficiaryListModel> getCommonBeneficiaryList(@Field("FromDate") String FromDate, @Field("Todate") String Todate, @Field("DIVID") String DIVID, @Field("SubOrgId") String SubOrgId, @Field("DISTLGDCODE") String DISTLGDCODE, @Field("USERID") String USERID, @Field("TeamId") String TeamId, @Field("Type") String Type);

    @POST("GetCTandMDCommonBeneficiaryListForTeam")
    @FormUrlEncoded
    Call<CommonBeneficiaryListModel> getCommonBeneficiaryListForTeam(@Field("FromDate") String FromDate, @Field("Todate") String Todate, @Field("DIVID") String DIVID, @Field("SubOrgId") String SubOrgId, @Field("DISTLGDCODE") String DISTLGDCODE, @Field("USERID") String USERID, @Field("TeamId") String TeamId, @Field("Type") String Type);

    @POST("GetHubandHomelabDashboard")
    @FormUrlEncoded
    Call<ProcessingLabCountModel> getProcessingLabCount(@Field("MonthID") String MonthID, @Field("Year") String Year, @Field("DIVID") String DIVID, @Field("SubOrgId") String SubOrgId, @Field("DISTLGDCODE") String DISTLGDCODE, @Field("USERID") String USERID, @Field("DesgId") String DesgId);

    @GET("GetMachineAvailabilityFlag_V1")
    Call<MachineDataStatusModel> getMachineStatus(@Query("USERID") String USERID);


    @GET("CheckCampTestStatusOfBeneficiaries")
    Call<AvailabilityTestModel> getTestCompleteFlag(@Query("UserId") String UserId);


    @GET(ApplicationConstants.GetCampLocationDetails)
    Call<CampLocationModel> getCampLocation(@Query("UserId") String UsUserIderId);

    @GET(ApplicationConstants.GetCampClosingConfirmationStatus_V1)
    Call<CampConfirmationModel> getConfirmationFlag(@Query("CampId") String CampId);


    @POST(ApplicationConstants.CheckDependentRegistrationStatus)
    @FormUrlEncoded
    Call<BeneficiaryStatusModel> getDependentRegistrationStatus(@Field("RegdNo") String RegdNo,@Field("DependentName") String DependentName);

    @POST(ApplicationConstants.VerifyDependentDetails)
    @FormUrlEncoded
    Call<BeneficiaryStatusModel> verifyBeneficiaryDetails(@Field("RegdNo") String RegdNo,@Field("AdharNo") String AdharNo,@Field("DependentName") String DependentName,@Field("RelationID") String RelationID);
    @POST(ApplicationConstants.VerifyDependentDetails_V2)
    @FormUrlEncoded
    Call<BeneficiaryStatusModel> verifyBeneficiaryDetailsNew(@Field("RegdNo") String RegdNo,@Field("AdharNo") String AdharNo,@Field("DependentName") String DependentName,@Field("RelationID") String RelationID,@Field("Pincode") String Pincode,@Field("DOB") String DOB,@Field("RationCardNo") String RationCardNo);
    @POST(ApplicationConstants.GetBeneficiaryConsentDetails)
    @FormUrlEncoded
    Call<BeneficiaryConsentModel> getConsent(@Field("BOCWRegNO") String BOCWRegNO, @Field("BeneficiaryName") String BeneficiaryName, @Field("ReleationID") String ReleationID);


    @POST("GetScreenedDependentCount")
    @FormUrlEncoded
    Call<ScreenedDependentModel> getScreenedDependentCount(@Field("AssignCallID") String AssignCallID, @Field("Type") String Type);


    @POST("GetHubandHomeLabProcessingCountWithLabName")
    @FormUrlEncoded
    Call<ProcessingLabCountModel> getProcessingLabCountDetails(@Field("MonthID") String MonthID, @Field("Year") String Year, @Field("DIVID") String DIVID, @Field("SubOrgId") String SubOrgId, @Field("DISTLGDCODE") String DISTLGDCODE, @Field("USERID") String USERID, @Field("DesgId") String DesgId, @Field("Type") String Type);

    @POST("GetSampleProcessingDashboard")
    @FormUrlEncoded
    Call<PendingCountModel> getPendingCountDetails(@Field("MonthId") String MonthId, @Field("Year") String Year, @Field("SubOrgId") String SubOrgId, @Field("DIVID") String DIVID, @Field("Distcode") String Distcode, @Field("UserId") String UserId, @Field("DESGID") String DESGID, @Field("CampType") String CampType);

    @POST("district-wise-patient-details.php")
    @FormUrlEncoded
    Call<FibroscanDistrictWiseCountModel> getDistrictDataLiverScan(@Field("fromdate") String fromdate, @Field("todate") String todate, @Field("distlgdcode") String distlgdcode);

//    @GET(ApplicationConstants.GetYear)
//    Call<YearModel> GET_YEAR_RESPONSE_CALL();

    @POST(ApplicationConstants.GetYear_UserIDWise)
    @FormUrlEncoded
    Call<YearModel> GET_YEAR_RESPONSE_CALL(@Field("UserID") String UserID);


    @POST(ApplicationConstants.GetPaidByCompanyList)
    @FormUrlEncoded
    Call<CompanyListModel> getComanylist(@Field("UserID") String UserID, @Field("Year") String Year, @Field("Month") String Month);

    @GET("GetPaymentVerificationRemark")
    Call<VerificationRemarkModel> getRemark();

//    @POST(ApplicationConstants.GetMonth)
//    @FormUrlEncoded
//    Call<MonthModel> GET_MONTH_RESPONSE_CALL(@Field("YearID") String YearID);

    @POST(ApplicationConstants.GetMonth_User)
    @FormUrlEncoded
    Call<MonthModel> GET_MONTH_RESPONSE_CALL(@Field("YearID") String YearID, @Field("UserID") String UserID);


    @POST(ApplicationConstants.GetUserWiseCampList)
    @FormUrlEncoded
    Call<CampListModel> getCampId(@Field("UserId") String YearID, @Field("CampDate") String CampDate, @Field("CampType") String CampType);


    @POST(ApplicationConstants.GetTeamDataByUserId)
    @FormUrlEncoded
    Call<TeamsDetailsModel> getTeamDataForCCE(@Field("UserID") String UserID);


    @GET(ApplicationConstants.GetDepartmentType)
    Call<DepartMentTypeModel> getPrescription();

    @POST(ApplicationConstants.GetMobileNoCTandMDOTP)
    @FormUrlEncoded
    Call<GetMobileNumberModel> getSelectNumber(@Field("RegdNo") String RegdNo);


    @POST(ApplicationConstants.GetDependentDetailsFromBoardData)
    @FormUrlEncoded
    Call<GetDependentListModel> getDependentList(@Field("RegdNo") String RegdNo);



    @POST(ApplicationConstants.GetDependentDetailsForRescreening)
    @FormUrlEncoded
    Call<GetDependentListModel> getDependentRescreeningData(@Field("RegdId") String RegdId);


    @POST(ApplicationConstants.GetDependentDetailsFromBoardData_V1)
    @FormUrlEncoded
    Call<GetDependentListModel> getDependentListNew(@Field("RegdNo") String RegdNo,@Field("WorkerAge") String WorkerAge,@Field("WorkerGender") String WorkerGender,@Field("WorkerMaritalStatus") String WorkerMaritalStatus);


    @POST(ApplicationConstants.GetGPListTalukaWise)
    @FormUrlEncoded
    Call<GpModel> getGramPanchayat(@Field("TALLGDCODE") String TALLGDCODE);


    @POST(ApplicationConstants.InsertForgotPasswordRequest)
    @FormUrlEncoded
    Call<ResponseBody> getOtp(@Field("Mobileno") String Mobileno, @Field("otp") String otp);


    @POST(ApplicationConstants.UpdateAttendanceImageApproval)
    @FormUrlEncoded
    Call<ResponseBody> insertApproveReject(@Field("StatusID") String StatusID, @Field("CampId") String CampId, @Field("ApprovalStatusID") String ApprovalStatusID, @Field("CreatedBy") String CreatedBy);


    @POST(ApplicationConstants.InsertOTPForCTSampleCollection_Option)
    @FormUrlEncoded
    Call<ResponseBody> getOtpForSampleCollection(@Field("MOBNO") String MOBNO, @Field("OTP") String OTP, @Field("RegdId") String RegdId, @Field("CreatedBy") String CreatedBy, @Field("MsgID") String MsgID, @Field("SubOrgID") String SubOrgID, @Field("Option") String Option);

    @POST("clickToCall")
    @FormUrlEncoded
    Call<ResponseBody> TwentyFourBySeven_CALL(@Field("apiKey") String apiKey, @Field("customernumber") String customernumber, @Field("servienumber") String servienumber, @Field("format") String format, @Field("agentloginid") String agentloginid, @Field("referencestate") String referencestate);

    @POST("GetAndroidIDActiveStatus")
    @FormUrlEncoded
    Call<AutoLogoutResponse> getAutoLogout(@Field("AndroidID") String AndroidID);


    @POST("addcalldata")
    @FormUrlEncoded
    Call<ResponseBody> TwentyFourBySeven_CALLNew(@Field("apiKey") String apiKey, @Field("customer_number") String customer_number, @Field("user_number") String user_number, @Field("caller_id") String caller_id, @Field("reference_id") String reference_id);

    @POST("getMMUScheduleDetails")
    @FormUrlEncoded
    Call<MmuSchedulesDataModel> getMmuCamp_CALL(@Field("unitId") String unitId);

    @POST("GetDistrictWiseUnit")
    @FormUrlEncoded
    Call<UniteIdModel> getUniteId(@Field("DISTLGDCODE") String DISTLGDCODE);


//    @POST("GetAssignedExpectedBefForCallingBySearch")
//    @FormUrlEncoded
//    Call<ReportlistResponse> REPORTLIST_RESPONSE_CALL_NEW(@Field("Userid") int Userid);


    @POST(ApplicationConstants.GetAdvadetailsNewVersion_V2)
    @FormUrlEncoded
    Call<AdvanceDetailsResponseModel> getAdvanceDetails(@Field("FromReqDate") String FromReqDate, @Field("ToReqDate") String ToReqDate, @Field("distlgdcode") int distlgdcode, @Field("USERID") int USERID);


    @GET(ApplicationConstants.GetDistrictList)
    Call<ResponseBody> getDistrict();

    @POST("count")
    Call<SuperAdminCountModel> getSuperadminCount();


    @POST("count")
    @FormUrlEncoded
    Call<SuperAdminCountModel> getTodaysCount(@Field("fromDate") String fromDate, @Field("toDate") String toDate);


    @POST("patientAppCount")
    Call<AndroidIosCountModel> getAndroidIosCount();

    @POST("IosCount")
    Call<AndroidMobileCountModel> getAndroidIosMoblieCount();


    @POST("downloadPatientDataExcel")
    Call<ResponseBody> downloadExcelFile(@Body Object emptyBody);

    @Multipart
    @POST(ApplicationConstants.ExpenseBillDetailsHandler)
    Call<ResponseBody> uploadBills(@Part MultipartBody.Part Billid, @Part MultipartBody.Part createdBy, @Part MultipartBody.Part ExpenseHead, @Part MultipartBody.Part FileName);

    @Multipart
    @POST(ApplicationConstants.CW_AttendanceInout)
    Call<ResponseBody> insertAttendance(@Part MultipartBody.Part CampID, @Part MultipartBody.Part InImagePath, @Part MultipartBody.Part OutImagePath, @Part MultipartBody.Part UserId, @Part MultipartBody.Part StatusID);

    @Multipart
    @POST(ApplicationConstants.CW_AttendanceInoutNew)
    Call<ResponseBody> insertAttendanceNew(@Part MultipartBody.Part CampID, @Part MultipartBody.Part ImagePath, @Part MultipartBody.Part UserId, @Part MultipartBody.Part StatusID);


    @Multipart
    @POST(ApplicationConstants.ProofOfPermissionHandler)
    Call<ResponseBody> uploadProofOfPermission(@Part MultipartBody.Part RequestID, @Part MultipartBody.Part createdBy, @Part MultipartBody.Part AttachementProof);

    @POST(ApplicationConstants.InsertAdvancesRequestNewChanges_New)
    @FormUrlEncoded
    Call<ResponseBody> insertAdvancesRequestNewChanges(@Field("Campid") int campId, @Field("SubExpenseID") int SubExpenseID, @Field("ExpenseAmount") int ExpenseAmmount, @Field("CreatedBy") int CreatedBy, @Field("ProcessID") int ProcessID, @Field("ApprovalStatus") int ApprovalStatus, @Field("RequestType") int RequestType, @Field("expeDescription") String expeDescription, @Field("NoOfItems") int NoOfItems, @Field("PerItemPrice") String PerItemPrice);

    @POST(ApplicationConstants.InsertAdvancesRequestNewChanges_V3)
    @FormUrlEncoded
    Call<ResponseBody> insertAdvancesRequestNewChangesNew(@Field("Campid") int campId, @Field("RequestType") int RequestType, @Field("SubExpenseID") int SubExpenseID, @Field("ExpenseAmount") int ExpenseAmmount, @Field("ProcessID") int ProcessID, @Field("CreatedBy") int CreatedBy, @Field("ApprovalStatus") int ApprovalStatus, @Field("expeDescription") String expeDescription, @Field("NoOfItems") int NoOfItems, @Field("PerItemPrice") String PerItemPrice, @Field("OrganisedBy") String OrganisedBy);


    @POST(ApplicationConstants.GetSubExpensesMasterDataForMgehaCampChanges)
    @FormUrlEncoded
    Call<SubExpenseHeadResponseModel> getSubExpensesMasterData(@Field("ExpenseHead") int ExpenseHead, @Field("Campid") int Campid, @Field("RegisterCount") int RegisterCount);

    @POST(ApplicationConstants.GetSubExpensesMasterData_V1)
    @FormUrlEncoded
    Call<SubExpenseHeadResponseModel> getSubExpensesMasterDataNew(@Field("ExpenseHead") int ExpenseHead, @Field("OrganisedBy") int RegisterCount, @Field("CampType") int CampType);


    @POST("GetFaceDetectionFlag")
    @FormUrlEncoded
    Call<FaceDetectionModel> getFaceDetectionData(@Field("UserId") String UserId);


    @FormUrlEncoded
    @POST(ApplicationConstants.GetAdvaDemandeddetailsShow)
    Call<SeeAdvanceRequestResponseModel> getAdvaDemandeddetailsShow(@Field("campid") String campID);

    @GET(ApplicationConstants.GetExpensesMasterData)
    Call<ExpenseHeadListModel> getExpensesMasterData();

    @GET(ApplicationConstants.GetInitiatedByListForCamp)
    Call<GetInitiatedByListForCampModel> getOrganizedBy();


    @GET(ApplicationConstants.GetWorkerInfo + "{workerReg}")
    Call<ResponseBody> getWorkerInfo(@Path("workerReg") String workerReg);

    @FormUrlEncoded
    @POST(ApplicationConstants.GetBillSubmitdetailsShow)
    Call<SeeAdvanceRequestResponseModel> getBillSubmitdetailsShow(@Field("campid") String campID);


    @POST(ApplicationConstants.InsertAdvancesRequest)
    @FormUrlEncoded
    Call<ResponseBody> insertAdvancesRequest(@Field("Campid") int Campid, @Field("RefreshmentCwCount") int RefreshmentCwCount, @Field("RefreshmentAmount") int RefreshmentAmount, @Field("RefreshmentTotal") int RefreshmentTotal, @Field("RefreshmentDescription") String RefreshmentDescription, @Field("CourierCwCount") int CourierCwCount, @Field("CourierAmount") int CourierAmount, @Field("CourierTotal") int CourierTotal, @Field("CourierDescription") String CourierDescription, @Field("TransportCwCount") int TransportCwCount, @Field("TransportAmount") int TransportAmount, @Field("TransportTotal") int TransportTotal, @Field("TransportDescription") String TransportDescription, @Field("MISCwCount") int MISCwCount, @Field("MISAmount") int MISAmount, @Field("MISTotal") int MISTotal, @Field("MISDescription") String MISDescription, @Field("FinalTotal") int FinalTotal, @Field("CreatedBy") int CreatedBy);


    @POST("GetBeneficiaryDependantDetails")
    @FormUrlEncoded
    Call<DependentModel> DEPENDENTLIS_RESPONSE_CALL(@Field("AssignCallID") String AssignCallID);

    @POST("GetBeneficiaryAppoinmentDetails")
    @FormUrlEncoded
    Call<ReportlistResponse> REPORTLIST_RESPONSE_CALL_TEAM(@Field("Userid") int Userid);


    @FormUrlEncoded
    @POST("InsertBeneficiaryCallingAppointmentDetails_Mob")
    Call<ResponseModel> InsertBeneficiaryCallingAppointmentDetails(@Field("AssignCallID") String AssignCallID,
                                                                   @Field("CallStatusID") String CallStatusID,
                                                                   @Field("RegisteredAddress") String RegisteredAddress,
                                                                   @Field("CurrentAddress") String CurrentAddress,
                                                                   @Field("IsCurrentSameAsRegd") String IsCurrentSameAsRegd,
                                                                   @Field("RegMobileNo") String RegMobileNo,
                                                                   @Field("AltMobileNo") String AltMobileNo,
                                                                   @Field("Pincode") String Pincode,
                                                                   @Field("Landmark") String Landmark,
                                                                   @Field("WorkersGender") String WorkersGender,
                                                                   @Field("WorkersMaritalStatus") String WorkersMaritalStatus,
                                                                   @Field("NoOfDependants") String NoOfDependants,
                                                                   @Field("DependantScreeningPending") String DependantScreeningPending,
                                                                   @Field("AppoinmentDate") String AppoinmentDate,
                                                                   @Field("AppoinmentTime") String AppoinmentTime,
                                                                   @Field("Remark") String Remark,
                                                                   @Field("DISTLGDCODE") int DISTLGDCODE,
                                                                   @Field("TALLGDCODE") int TALLGDCODE,
                                                                   @Field("HouseNo") String HouseNo,
                                                                   @Field("Road") String Road,
                                                                   @Field("Area") String Area,
                                                                   @Field("DependantDetails") String DependantDetails,
                                                                   @Field("CReatedBy") String CReatedBy,
                                                                   @Field("WorkerScreeningStatus") String WorkerScreeningStatus
    );

    @FormUrlEncoded
    @POST("InsertBeneficiaryCallingAppointmentDetails_Mob_New")
    Call<ResponseModel> InsertBeneficiaryCallingAppointmentDetailsNew(@Field("AssignCallID") String AssignCallID,
                                                                      @Field("CallStatusID") String CallStatusID,
                                                                      @Field("RegisteredAddress") String RegisteredAddress,
                                                                      @Field("CurrentAddress") String CurrentAddress,
                                                                      @Field("IsCurrentSameAsRegd") String IsCurrentSameAsRegd,
                                                                      @Field("RegMobileNo") String RegMobileNo,
                                                                      @Field("AltMobileNo") String AltMobileNo,
                                                                      @Field("Pincode") String Pincode,
                                                                      @Field("Landmark") String Landmark,
                                                                      @Field("WorkersGender") String WorkersGender,
                                                                      @Field("WorkersMaritalStatus") String WorkersMaritalStatus,
                                                                      @Field("NoOfDependants") String NoOfDependants,
                                                                      @Field("DependantScreeningPending") String DependantScreeningPending,
                                                                      @Field("AppoinmentDate") String AppoinmentDate,
                                                                      @Field("AppoinmentTime") String AppoinmentTime,
                                                                      @Field("Remark") String Remark,
                                                                      @Field("DISTLGDCODE") int DISTLGDCODE,
                                                                      @Field("TALLGDCODE") int TALLGDCODE,
                                                                      @Field("HouseNo") String HouseNo,
                                                                      @Field("Road") String Road,
                                                                      @Field("Area") String Area,
                                                                      @Field("DependantDetails") String DependantDetails,
                                                                      @Field("CReatedBy") String CReatedBy,
                                                                      @Field("WorkerScreeningStatus") String WorkerScreeningStatus,
                                                                      @Field("RemarkID") String RemarkID

    );

    @FormUrlEncoded
    @POST("UpdateAppointmentDetails_Mob")
    Call<ResponseModel> InsertBeneficiaryCallingAppointmentDetailsNewForUpdate(@Field("AssignCallID") String AssignCallID,
                                                                               @Field("CallStatusID") String CallStatusID,
                                                                               @Field("AppoinmentDate") String AppoinmentDate,
                                                                               @Field("AppoinmentTime") String AppoinmentTime,
                                                                               @Field("PhleboRemark") String PhleboRemark,
                                                                               @Field("PhleboRemarkID") String PhleboRemarkID,
                                                                               @Field("CReatedBy") String CReatedBy

    );

    @GET("GetCallStatusList")
    Call<CallstatusResponse> CALLSTATUS_RESPONSE_CALL();

    @FormUrlEncoded
    @POST("GetAppoinmentCallStatusList")
    Call<CallstatusResponse> CALLSTATUS_RESPONSE_CALL_New(@Field("AssignCallID") String AssignCallID);

    @FormUrlEncoded
    @POST("GetPhleboCallStatusList")
    Call<CallstatusResponse> CALLSTATUS_RESPONSE_CALL_For_Phlebo(@Field("AssignCallID") String AssignCallID);


    @FormUrlEncoded
    @POST("GetAppointmentStatusList")
    Call<CallstatusResponse> CALLSTATUS_RESPONSE_CALL_New_For_Team(@Field("UserID") String UserID);

    @GET("GetCallingRemark")
    Call<CallingRemarkModel> RemarkL();


    @POST("GetCallingRemark_V1")
    @FormUrlEncoded
    Call<CallingRemarkModel> RemarkLNew(@Field("CallStatusID") String CallStatusID);

    @GET("GetAppoinmentStatusList")
    Call<AppointmentListModel> Appointment_List();

    @GET("GetCallingDate_Type")
    Call<DateTypesModel> getDateType_List();

    @POST(ApplicationConstants.GetAllTalukaList)
    @FormUrlEncoded
    Call<TalukaModel> getTaluka(@Field("STATELGDCODE") int STATELGDCODE, @Field("DISTLGDCODE") int DISTLGDCODE);


    @GET(ApplicationConstants.GetRelation)
    Call<RelationPojo> getRelation();


    /**
     * ABHA
     */

    @POST(ApplicationConstants.sessions)
    Call<ResponseBody> createAbhaSession(@Body ABHASessionModel abhaSessionModel, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-CM-ID") String xCMId);

    @POST(ApplicationConstants.sendOTP)
    Call<ResponseBody> generateOtp(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body SendOTPRequestModel sendOTPRequestModel);

    @GET(ApplicationConstants.getPublicCertificate)
    Call<ResponseBody> getPublicCertificate(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID);

    @POST(ApplicationConstants.generateMobileOtp)
    Call<ResponseBody> generateMobileOtp(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body JsonObject jsonObject);

    @POST(ApplicationConstants.verifyOTP)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> verifyOTP(@Header("Authorization") String token, @Body JsonObject verifyOTP);

    @POST(ApplicationConstants.enrollByAadhaar)
    Call<ResponseBody> enrollByAAdhaar(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body EnrolByAadhaarRequestModel enrolByAadhaarRequestModel);

    @POST(ApplicationConstants.checkAndGenerateMobileOTP)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> checkAndGenerateMobileOTP(@Header("Authorization") String token, @Body JsonObject verifyOTP);


    @POST(ApplicationConstants.sendMobileOTP)
    Call<ResponseBody> sendMobileOTP(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body SendOTPRequestModel sendOTPRequestModel);

    @POST(ApplicationConstants.verifyMobileOTPABDM)
    Call<ResponseBody> verifyMobileOTPABDM(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body VerifyMobileOTPRequestModel verifyMobileOTPRequestModel);

    @POST(ApplicationConstants.verifyMobileOTP)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> verifyMobileOTP(@Header("Authorization") String token, @Body JsonObject verifyOTP);

    @POST(ApplicationConstants.searchABHA)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> searchABHA(@Header("Authorization") String token, @Body SearchHealthIDRequestModel searchPayload);

    @POST(ApplicationConstants.createHealthIdWithAadhaarOtp)
    Call<ResponseBody> createHealthIdWithAadhaarOtp(@Header("Authorization") String token, @Body CreateHealthIdModel createHealthIdModel);

    @POST(ApplicationConstants.createHealthIdWithPreVerified)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createHealthIdWithPreVerified(@Header("Authorization") String token, @Body JsonObject payload);

    @POST(ApplicationConstants.initAuth)
    Call<ResponseBody> initAuth(@Header("Authorization") String token, @Header("X-CM-ID") String cmId, @Body InitAuthRequestModel initAuthRequestModel);

    @POST(ApplicationConstants.hidInitAuth)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> hidInitAuth(@Header("Authorization") String token, @Body JsonObject payload);

    @POST(ApplicationConstants.fetchModes)
    Call<ResponseBody> fetchModes(@Header("Authorization") String token, @Header("X-CM-ID") String cmId, @Body FetchAuthRequestModel fetchAuthRequestModel);

    @POST(ApplicationConstants.confirmAuth)
    Call<ResponseBody> confirmAuth(@Header("Authorization") String token, @Header("X-CM-ID") String cmId, @Body ConfirmAuthRequestModel confirmAuthRequestModel);

    @POST(ApplicationConstants.confirmWithAadhaarOtp)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> confirmWithAadhaarOtp(@Header("Authorization") String token, @Body JsonObject payload);

    @POST(ApplicationConstants.confirmWithMobileOtp)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> confirmWithMobileOtp(@Header("Authorization") String token, @Body JsonObject payload);

    @POST(ApplicationConstants.addContext)
    Call<ResponseBody> addContext(@Header("Authorization") String token, @Header("X-CM-ID") String cmId, @Body AddContextRequestModel addContextRequestModel);


    @GET(ApplicationConstants.phrSuggestions)
    Call<ResponseBody> getPHRSuggestions(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("Transaction_Id") String TransactionId);

    @GET(ApplicationConstants.phrISExists)
    Call<ResponseBody> phrISExists(@Header("Authorization") String token, @Query("phrAddress") String phrAddress);

    @GET(ApplicationConstants.getPngCard)
    Call<ResponseBody> getPngCard(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-Token") String xToken);

    @GET(ApplicationConstants.getPngCardPHR)
    Call<ResponseBody> getPngCardPHR(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-Token") String xToken);

    @GET(ApplicationConstants.accountProfile)
    Call<ResponseBody> accountProfile(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-Token") String xToken);

    @POST(ApplicationConstants.createPHRAddress)
    Call<ResponseBody> createPHRAddress(@Header("Authorization") String token, @Header("REQUEST-ID") String REQUESTID, @Header("TIMESTAMP") String TIMESTAMP, @Body JsonObject payload);

    @POST(ApplicationConstants.linkProfileDetails)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> linkProfileDetails(@Header("Authorization") String token, @Body JsonObject payload);

    @POST(ApplicationConstants.InsertAbhaRegistration)
    @FormUrlEncoded
    Call<ResponseBody> insertAbhaRegistration(@Field("AbhaRegistrationJSON") String payload);

    @POST(ApplicationConstants.loginByMobileSendOTP)
    Call<ResponseBody> loginByMobileSendOTP(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body SendOTPRequestModel sendOTPRequestModel);

    @POST(ApplicationConstants.loginByMobileVerifyOTP)
    Call<ResponseBody> loginByMobileVerifyOTP(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body VerifyMobileOTPRequestModel verifyMobileOTPRequestModel);

    @POST(ApplicationConstants.verifyLoginProfileUser)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> verifyLoginProfileUser(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("T-token") String tToken, @Body JsonObject body);

    @POST(ApplicationConstants.searchABHAAddress)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> searchABHAAddress(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body JsonObject body);

    @POST(ApplicationConstants.sendOTPABHAAddress)
    Call<ResponseBody> sendOTPABHAAddress(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body SendOTPRequestModel body);

    @POST(ApplicationConstants.verifyOTPABHAAddress)
    Call<ResponseBody> verifyOTPABHAAddress(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Body VerifyMobileOTPRequestModel body);

    @GET(ApplicationConstants.getABHAProfileAddress)
    Call<ResponseBody> getABHAProfileAddress(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-token") String xToken);

    @POST(ApplicationConstants.getPatientQueue)
    @FormUrlEncoded
    Call<ResponseBody> getPatientQueue(@Field("Campid") Long Campid);


    @POST(ApplicationConstants.generateLinkToken)
    Call<ResponseBody> generateLinkToken(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-HIP-ID") String xHIPId, @Header("X-CM-ID") String xCMId, @Body JsonObject payload);

    @POST(ApplicationConstants.getTokenFromAdhar)
    @FormUrlEncoded
    Call<ResponseBody> getLinkTokenCallback(@Field("abhaAddress") String abhaAddress);

    @POST(ApplicationConstants.linkCareContext)
    Call<ResponseBody> linkCareContext(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-HIP-ID") String xHIPId, @Header("X-CM-ID") String xCMId, @Header("X-LINK-TOKEN") String linkToken, @Body CareContextLinkingRequestModel payload);

    @POST(ApplicationConstants.contextNotify)
    Call<ResponseBody> contextNotify(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-HIP-ID") String xHIPId, @Header("X-CM-ID") String xCMId, @Body CareContextNotifyRequestModel payload);

    @POST(ApplicationConstants.contextNotify)
    Call<ResponseBody> saveDataToOurServer(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-HIP-ID") String xHIPId, @Header("X-CM-ID") String xCMId, @Body CareContextNotifyRequestModel payload);

    @POST(ApplicationConstants.smsNotify)
    Call<ResponseBody> smsNotify(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("X-HIP-ID") String xHIPId, @Header("X-CM-ID") String xCMId, @Body SMSNotifyRequestModel payload);

    @POST(ApplicationConstants.UpdateQueFlag)
    @FormUrlEncoded
    Call<ResponseBody> updateQueFlag(@Field("healthIdNumber") String healthIdNumber, @Field("Campid") Integer Campid, @Field("IsRegistered") Integer IsRegistered);

    @POST(ApplicationConstants.findABHA)
    Call<ResponseBody> findABHA(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("BENEFIT_NAME") String BENEFIT_NAME, @Body JsonObject payload);

    @POST(ApplicationConstants.UserLoginApp)
    @FormUrlEncoded
    Call<ResponseBody> userLoginApp(@Header("Strict-Transport-Security") String sts, @Header("Content-Security-Policy") String csp, @Header("X-XSS-Protection") String xssp, @Header("Cache-Control") String cc, @Header("Pragma") String pragma, @Header("Access-Control-Allow-Origin") String acao, @Header("X-Frame-Options") String cfo, @Field("UserName") String UserName, @Field("Password") String Password);

    @GET(ApplicationConstants.GetState)
    Call<StateListResponseModel> getState();

    @POST(ApplicationConstants.GetDistrictsOnState)
    @FormUrlEncoded
    Call<DistrictOnStateListResponseModel> getDistrictsOnState(@Field("StateLGDCode") int stateLGDCode);

    @POST(ApplicationConstants.enrollByAadhaar)
    Call<ResponseBody> enrollByAAdhaarViaDemoAuth(@Header("Authorization") String token, @Header("TIMESTAMP") String TIMESTAMP, @Header("REQUEST-ID") String REQUESTID, @Header("BENEFIT-NAME") String BENEFIT_NAME, @Body EnrollByAadhaarViaDemoAuth enrollByAadhaarViaDemoAuth);

    @POST("save-abhadetails-v3.php")
    Call<ResponseBody> saveAbhaDetails(@Body EnrollByAadhaarViaDemoAuthCreationResponseModel payload);


}
