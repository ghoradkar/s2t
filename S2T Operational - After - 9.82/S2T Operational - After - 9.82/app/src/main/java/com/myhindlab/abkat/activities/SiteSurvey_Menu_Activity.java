package com.myhindlab.abkat.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.model.AppUpdateType;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.AttendanceDetailsActivity;
import com.myhindlab.abkat.activities.attendance_details.AttendanceDetailsForCCActivity;
import com.myhindlab.abkat.activities.calling_dashboard.CallingDashboardActivity;
import com.myhindlab.abkat.activities.campApproval.CampSelectionForCampUpdate_Activity;
import com.myhindlab.abkat.activities.campApproval.CampStatusDashboardActivity;
import com.myhindlab.abkat.activities.campredinessnew.CampredinessNewActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.CTAndedicineCommonBeneficiaryListActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestAssignTeamActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestAssignTeamEmergencyPatientActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestNewActivity;
import com.myhindlab.abkat.activities.couriermodule.AcceptSamplesInLabActivity;
import com.myhindlab.abkat.activities.couriermodule.CentriFugeConfirmationActivity;
import com.myhindlab.abkat.activities.couriermodule.Courier_Activity;
import com.myhindlab.abkat.activities.doortodoor.CallToDoctorRequestActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dPhysicalExaminationDetailsActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.activities.doortodoor.ExpectectedBeneficiaryForTeamsActivity;
import com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.CampSelectionForD2DActivity_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2DAvailabilityActivity;
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.DashboardPatientAppNew;
import com.myhindlab.abkat.activities.medicine_return.MedicineDeliveryMenuActivity;
import com.myhindlab.abkat.activities.medicine_return.MedicineDeliveryMenuNewActivity;
import com.myhindlab.abkat.activities.medicine_return.MedicineReturnActivity;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceForDoctorActivity;
import com.myhindlab.abkat.activities.re_registration.DailyWorkDashboardActivity;
import com.myhindlab.abkat.activities.re_registration.DailyWorkDashboardForAdminActivity;
import com.myhindlab.abkat.activities.reallocation.PacketReallocationActivity;
import com.myhindlab.abkat.activities.regularcampcreation.CampCreationActivityV4;
import com.myhindlab.abkat.adapters.MenuListAdapter;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.expense_module.activities.ExpenseClaimDashboardActivity;
import com.myhindlab.abkat.models.AutoLogoutResponse;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.GetDocListD2D;
import com.myhindlab.abkat.models.MainScreenCountsModel;
import com.myhindlab.abkat.models.SuperAdminCountModel;
import com.myhindlab.abkat.pojos.MenuListPojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.services.LogoutService;
import com.myhindlab.abkat.services.LogoutWorkManager;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.DeviceIdUtil;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteSurvey_Menu_Activity extends AppCompatActivity {

    private String TAG = getClass().getName();
    private Context context;
    private ConstantData constantData;
    private DrawerLayout drawer_main_drawerlist;
    private UserSessionManager session;
    private ProgressDialog pd;
    String tokenIDString;
    String versionName, otpnumber;


    private ArrayList<GetApprovedCampListDetailsForAppList> campLists;
    private ArrayList<GetApprovedCampListDetailsForAppList> searchCampList;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout ll_counts;
    private CardView llcount_todays_patient, llcount_conducted_camp;
    private TextView tvTodaysUpdatedTime, tvdate_time, tvdate_time_today, tv_date_time, tv_designation, tv_username, tv_camp_conducted,
            tv_bene_screened, tv_row22_today, tv_row_total_r2_today,
            tvRegularConductedCamp, tvD2dConductedCamp, tvRegularBeneScreened, tvD2dBeneScreened, tv_financial_year,
            tvRegularReferredPatients, tv_regular_HLL_Life_conducted_treatment_today,
            tvD2dReferredPatients, tvRegularTodaysPatient, tvD2dTodaysPatient, tv_critical_patients,
            tv_referred_patients, tv_todays_patient, tv_row_1_today,
            tv_total_HLL_Life, total_d2d, tv_d2d_HSCC_HLL_Life, total_regular, tv_regular_HSCC_HLL_Life,
            tv_total_HSCC_Life_HLL_conducted_treatment_today, tv_conducted_finacial_year,
            tv_total_HSCC_Life_HLL_conducted, tv_total_HSCC__HLLLife, tv_regular_HLL_Life,
            tv_d2d_HLL_Life, tv_total_HSCC_Life_conducted_tratment_today, tv_r4_total_today,
            tv_total_HSCC_Life, tv_regular_HSCC_Life, tv_d2d_HSCC_Life, tv_total_HLL_Life_conducted,
            tv_r51, tv_r52, tv_r53_total, tv_r1_today, tv_r42_today, tv_r51_today,
            tv_regular_HLL_Life_conducted, tv_d2d_HLL_Life_conducted, tv_total_HSCC_Life_conducted,
            tv_r42, tv_r4_total, tv_r32_today, tv_r3_total_today, tv_r52_today,
            tv_d2d_HSCC_Life_conducted, tv_regular_HSCC_Life_conducted,
            tv_total_HSCC_Life_conducted_tratment, tv_r1, tv_r32, tv_r3_total, tv_r41, tv_r41_today, tv_r53_total_today,
            tv_regular_HLL_Life_conducted_treatment, tv_row22, tv_row_total_r2, tv_total_HSCC_Life_HLL_conducted_treatment, tv_row_1;
    private LinearLayout ll_camp_conducted, ll_bene_screened, ll_critical_patients, ll_referred_patients, todays_treatment_count_LL, ll_todays_patient, conducted_Tratment_count_LL;
    private ArrayList<MenuListPojo> menuList;
    private String name = "", Designation = "", DISTLGDCODE, TodayDate, mobileNo, TodayDateNew, allowMenu = "0", userId;

    private boolean isFixedPay;

    int DESGID;
    private int mYear, mMonth, mDay;
    private ArrayList<CampCalendarModel.OutputBean> campList;
    private ArrayList<CampCalendarModel.OutputBean> totalTodaysCamp;
    private ArrayList<CampCalendarModel.OutputBean> d2dTodaysCamp;
    private ArrayList<CampCalendarModel.OutputBean> cscd2dTodaysCamp;
    private ArrayList<CampCalendarModel.OutputBean> cscRegularTodaysCamp;
    private RadioGroup radioGroup;
    private RadioButton rbOnline, rbOffline, rbAbkat, rbDoorToDoor;
    private ArrayList<GetDocListD2D.Output> getDocListD2DArrayList;
    private String campType = "0";
    private ProgressBar progressBar;
    private RadioGroup rgLogin;
    private ApiInterface D2DApiInterface;
    private ApiInterface apiInterface;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitesurvey_menu);

//        checkupdate();
        init();
        setDefaults();
        setEventHandlers();
        setToolBar();
    }


//    private void checkupdate() {
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    // This example applies an immediate update. To apply a flexible update
//                    // instead, pass in AppUpdateType.FLEXIBLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                // Request the update.
//
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                            appUpdateInfo,
//                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                            AppUpdateType.IMMEDIATE,
//                            // The current activity making the update request.
//                            this,
//                            // Include a request code to later monitor this update request.
//                            1010);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Utilities.showToastMessage("Version is updated", context, true);
//
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010) {
            if (resultCode != RESULT_OK) {
                Log.d("Update flow failed!", String.valueOf(resultCode));
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

    private void init() {
        context = SiteSurvey_Menu_Activity.this;
        session = new UserSessionManager(context);
        tv_username = findViewById(R.id.tv_username);
        pd = new ProgressDialog(context);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


//        drawerLayout.addDrawerListener();

        tv_designation = findViewById(R.id.tv_designation);
        tv_camp_conducted = findViewById(R.id.tv_camp_conducted);
        tv_bene_screened = findViewById(R.id.tv_bene_screened);
        tv_critical_patients = findViewById(R.id.tv_critical_patients);
        tv_referred_patients = findViewById(R.id.tv_referred_patients);
        tv_todays_patient = findViewById(R.id.tv_todays_patient);
        ll_camp_conducted = findViewById(R.id.ll_camp_conducted);
        ll_bene_screened = findViewById(R.id.ll_bene_screened);
        ll_critical_patients = findViewById(R.id.ll_critical_patients);
        ll_referred_patients = findViewById(R.id.ll_referred_patients);
        ll_todays_patient = findViewById(R.id.ll_todays_patient);
        conducted_Tratment_count_LL = findViewById(R.id.conducted_Tratment_count_LL);
        todays_treatment_count_LL = findViewById(R.id.todays_treatment_count_LL);
        mRecyclerView = findViewById(R.id.recyclerview_sitesurveymenu);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_counts = findViewById(R.id.ll_counts);
//        mainLL_fibroscanner = findViewById(R.id.mainLL_fibroscanner);
        llcount_todays_patient = findViewById(R.id.llcount_todays_patient);
        llcount_conducted_camp = findViewById(R.id.llcount_conducted_camp);
        progressBar = findViewById(R.id.progressBar);
        tvTodaysUpdatedTime = findViewById(R.id.tvTodaysUpdatedTime);
        tv_date_time = findViewById(R.id.tv_date_time);

        String dateTime = Utilities.dateTimeFormat.format(new Date());

        TodayDate = Utilities.dfDate.format(new Date());
        TodayDateNew = Utilities.dfDate10.format(new Date());

        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);
        tv_date_time.setText("*Data as of:- " + dateTime);

        tvRegularConductedCamp = findViewById(R.id.tvRegularConductedCamp);
        tvRegularBeneScreened = findViewById(R.id.tvRegularBeneScreened);
        tvRegularReferredPatients = findViewById(R.id.tvRegularReferredPatients);
        tvRegularTodaysPatient = findViewById(R.id.tvRegularTodaysPatient);

        tvD2dBeneScreened = findViewById(R.id.tvD2dBeneScreened);
        tv_financial_year = findViewById(R.id.tv_financial_year);
        tvD2dConductedCamp = findViewById(R.id.tvD2dConductedCamp);
        tvD2dReferredPatients = findViewById(R.id.tvD2dReferredPatients);
        tvD2dTodaysPatient = findViewById(R.id.tvD2dTodaysPatient);
        tv_total_HLL_Life = findViewById(R.id.tv_total_HLL_Life);
        tv_regular_HLL_Life = findViewById(R.id.tv_regular_HLL_Life);
        tv_d2d_HLL_Life = findViewById(R.id.tv_d2d_HLL_Life);
        tv_total_HSCC_Life = findViewById(R.id.tv_total_HSCC_Life);
        tv_regular_HSCC_Life = findViewById(R.id.tv_regular_HSCC_Life);
        tv_d2d_HSCC_Life = findViewById(R.id.tv_d2d_HSCC_Life);
        tv_total_HLL_Life_conducted = findViewById(R.id.tv_total_HLL_Life_conducted);
        tv_regular_HLL_Life_conducted = findViewById(R.id.tv_regular_HLL_Life_conducted);
        tv_d2d_HLL_Life_conducted = findViewById(R.id.tv_d2d_HLL_Life_conducted);
        tv_total_HSCC_Life_conducted = findViewById(R.id.tv_total_HSCC_Life_conducted);
        tv_regular_HSCC_Life_conducted = findViewById(R.id.tv_regular_HSCC_Life_conducted);
        tv_d2d_HSCC_Life_conducted = findViewById(R.id.tv_d2d_HSCC_Life_conducted);
        tv_total_HSCC__HLLLife = findViewById(R.id.tv_total_HSCC__HLLLife);
        tv_total_HSCC_Life_HLL_conducted = findViewById(R.id.tv_total_HSCC_Life_HLL_conducted);
        tv_regular_HSCC_HLL_Life = findViewById(R.id.tv_regular_HSCC_HLL_Life);
        tv_d2d_HSCC_HLL_Life = findViewById(R.id.tv_d2d_HSCC_HLL_Life);
        total_regular = findViewById(R.id.total_regular);
        total_d2d = findViewById(R.id.total_d2d);
        tvdate_time = findViewById(R.id.tvdate_time);
        tv_total_HSCC_Life_conducted_tratment = findViewById(R.id.tv_total_HSCC_Life_conducted_tratment);
        tv_total_HSCC_Life_HLL_conducted_treatment = findViewById(R.id.tv_total_HSCC_Life_HLL_conducted_treatment);
        tv_row_1 = findViewById(R.id.tv_row_1);
        tv_regular_HLL_Life_conducted_treatment = findViewById(R.id.tv_regular_HLL_Life_conducted_treatment);
        tv_row22 = findViewById(R.id.tv_row22);
        tv_row_total_r2 = findViewById(R.id.tv_row_total_r2);
        tv_r1 = findViewById(R.id.tv_r1);
        tv_r32 = findViewById(R.id.tv_r32);
        tv_r3_total = findViewById(R.id.tv_r3_total);
        tv_r41 = findViewById(R.id.tv_r41);
        tv_r42 = findViewById(R.id.tv_r42);
        tv_r4_total = findViewById(R.id.tv_r4_total);
        tv_r51 = findViewById(R.id.tv_r51);
        tv_r52 = findViewById(R.id.tv_r52);
        tv_r53_total = findViewById(R.id.tv_r53_total);
        tv_total_HSCC_Life_conducted_tratment_today = findViewById(R.id.tv_total_HSCC_Life_conducted_tratment_today);
        tv_total_HSCC_Life_HLL_conducted_treatment_today = findViewById(R.id.tv_total_HSCC_Life_HLL_conducted_treatment_today);
        tv_conducted_finacial_year = findViewById(R.id.tv_conducted_finacial_year);
        tv_row_1_today = findViewById(R.id.tv_row_1_today);
        tv_regular_HLL_Life_conducted_treatment_today = findViewById(R.id.tv_regular_HLL_Life_conducted_treatment_today);
        tv_row22_today = findViewById(R.id.tv_row22_today);
        tv_row_total_r2_today = findViewById(R.id.tv_row_total_r2_today);
        tv_r1_today = findViewById(R.id.tv_r1_today);
        tv_r32_today = findViewById(R.id.tv_r32_today);
        tv_r3_total_today = findViewById(R.id.tv_r3_total_today);
        tv_r41_today = findViewById(R.id.tv_r41_today);
        tv_r42_today = findViewById(R.id.tv_r42_today);
        tv_r4_total_today = findViewById(R.id.tv_r4_total_today);
        tv_r51_today = findViewById(R.id.tv_r51_today);
        tv_r52_today = findViewById(R.id.tv_r52_today);
        tv_r53_total_today = findViewById(R.id.tv_r53_total_today);
        tvdate_time_today = findViewById(R.id.tvdate_time_today);


        rbAbkat = findViewById(R.id.rbAbkat);
        rbDoorToDoor = findViewById(R.id.rbDoorToDoor);
        rgLogin = findViewById(R.id.rgLogin);

        D2DApiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


//        tvdate_time.setText("Total Patient Count *FY(2024-25)"+ dateTime);
//        tvdate_time_today.setText("Todays Patient count Data as of:-" + dateTime);


        if (BuildConfig.isBeta) {
            Utilities.showAlertDialog(context, "Alert", "This is beta version", false);
        }
    }


    void getCountsFromServer() {
        if (Utilities.isNetworkAvailable(context)) {
//            new GetHomePageCountDetails().execute();
            getLandingPageCountsDisplayforFinancialYeart();
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    void getLandingPageCountsDisplayforFinancialYeart() {

        progressBar.setVisibility(View.VISIBLE);
        ll_counts.setVisibility(View.GONE);

        D2DApiInterface.getLandingPageCountsDisplayforFinancialYear().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("output");
                            tvRegularConductedCamp.setText(String.valueOf(jsonArray.getJSONObject(0).getInt("Regular Camp")));
                            tvD2dConductedCamp.setText(String.valueOf(jsonArray.getJSONObject(0).getInt("D2D Camp")));
                            tv_camp_conducted.setText(String.valueOf(jsonArray.getJSONObject(0).getInt("Total Camp")));

                            if (BuildConfig.isBeta) {
                                tv_financial_year.setText(jsonArray.getJSONObject(0).getString("FinancialYear"));
                            } else {
//                                tv_financial_year.setText("*FY(2025-26)");
                                tv_financial_year.setText(jsonArray.getJSONObject(0).getString("FinancialYear"));
                            }
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

//                new GetCampDetailsTodayCount().execute();
//                getCampDetailsTodayCountForOS();
                getCampDetailsTodayCountForOSNew();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                getCampDetailsTodayCountForOS();
                getCampDetailsTodayCountForOSNew();


            }
        });

    }


    private void getSuperAdminaData() {
        final ProgressDialog progressDialog = new ProgressDialog(SiteSurvey_Menu_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.super_admin_call().create(ApiInterface.class);
        Call<SuperAdminCountModel> call = apiService.getTodaysCount("", "");
        call.enqueue(new Callback<SuperAdminCountModel>() {
            @Override
            public void onResponse(Call<SuperAdminCountModel> call, Response<SuperAdminCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String dateTime = response.body().getDateTime();

                    if (status.equalsIgnoreCase("1")) {
//                        List<SuperAdminCountModel.Details> invoiceList = (List<SuperAdminCountModel.Details>) response.body().getDetails();


                        SuperAdminCountModel.Details details = response.body().getDetails();


                        if (details != null) {


                            int b = 0;
                            int c = 0;
                            int d = 0;

                            int e = 0;
                            int f = 0;
                            int g = 0;


                            int h = 0;
                            int i = 0;
                            int j = 0;

                            int l = 0;
                            int m = 0;
                            int n = 0;

                            int z = 0;
                            int p = 0;
                            int k = 0;


                            b = b + details.getTreatmentGivenHLL();
                            c = c + details.getTreatmentGivenHSCC();
                            d = d + details.getTreatmentGivenTotal();


                            e = e + details.getIpdRegisteredHLL();
                            f = f + details.getIpdRegisteredHSCC();
                            g = g + details.getIpdRegisteredTotal();

                            h = h + details.getDischargePatientHLL();
                            i = i + details.getDischargePatientHSCC();
                            j = j + details.getDischargePatientTotal();


                            l = l + details.getPrescriptionGivenHLL();
                            m = m + details.getPrescriptionGivenHSCC();
                            n = n + details.getPrescriptionGivenTotal();


                            z = z + details.getPrescriptionIssuedHLL();
                            p = p + details.getPrescriptionIssuedHSCC();
                            k = k + details.getPrescriptionIssuedTotal();

                            tv_total_HSCC_Life_conducted_tratment.setText("" + b);
                            tv_total_HSCC_Life_HLL_conducted_treatment.setText("" + c);
                            tv_row_1.setText("" + d);

                            tv_regular_HLL_Life_conducted_treatment.setText("" + e);
                            tv_row22.setText("" + f);
                            tv_row_total_r2.setText("" + g);

                            tv_r1.setText("" + h);
                            tv_r32.setText("" + i);
                            tv_r3_total.setText("" + j);


                            tv_r41.setText("" + l);
                            tv_r42.setText("" + m);
                            tv_r4_total.setText("" + n);

                            tv_r51.setText("" + z);
                            tv_r52.setText("" + p);
                            tv_r53_total.setText("" + k);


                            tvdate_time.setText("Total Data as of" + " " + dateTime);


//                                if (o.getSubOrgId().equalsIgnoreCase("2")) {
//                                    tv_total_HLL_Life_conducted.setText("" + o.getTotalConducted());
//                                    tv_regular_HLL_Life_conducted.setText("" + o.getRegularConductedCamp());
//                                    tv_d2d_HLL_Life_conducted.setText("" + o.getD2dConductedCamp());
//                                } else if (o.getSubOrgId().equalsIgnoreCase("3")) {
//                                    tv_total_HSCC_Life_conducted.setText("" + o.getTotalConducted());
//                                    tv_regular_HSCC_Life_conducted.setText("" + o.getRegularConductedCamp());
//                                    tv_d2d_HSCC_Life_conducted.setText("" + o.getD2dConductedCamp());
//                                }
//                            tv_todays_patient.setText(o.getTotal());
//                            tvRegularTodaysPatient.setText(o.getRegularCamp());
//                            tvD2dTodaysPatient.setText(o.getD2DCamp());


                            // Show error message if no match was found
//                            if (!isMatchFound) {
//                                Utilities.showAlertDialog(context, "Alert", "No camp scheduled for this camp date", false);
////                                edt_camp_date.setText("");
////                                edt_post_camp_date.setText("");
////                                edt_camp_address.setText("");
//                                Log.d("DEBUG", "No matching date found for the selected camp date.");
//                            }
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                        tv_total_HSCC_Life_conducted_tratment.setText("0");
                        tv_total_HSCC_Life_HLL_conducted_treatment.setText("0");
                        tv_row_1.setText("0");

                        tv_regular_HLL_Life_conducted_treatment.setText("0");
                        tv_row22.setText("0");
                        tv_row_total_r2.setText("0");

                        tv_r1.setText("0");
                        tv_r32.setText("0");
                        tv_r3_total.setText("0");


                        tv_r41.setText("0");
                        tv_r42.setText("0");
                        tv_r4_total.setText("0");

                        tv_r51.setText("0");
                        tv_r52.setText("0");
                        tv_r53_total.setText("0");


                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", " Details Not Found", false);
                    tv_total_HSCC_Life_conducted_tratment.setText("0");
                    tv_total_HSCC_Life_HLL_conducted_treatment.setText("0");
                    tv_row_1.setText("0");

                    tv_regular_HLL_Life_conducted_treatment.setText("0");
                    tv_row22.setText("0");
                    tv_row_total_r2.setText("0");

                    tv_r1.setText("0");
                    tv_r32.setText("0");
                    tv_r3_total.setText("0");


                    tv_r41.setText("0");
                    tv_r42.setText("0");
                    tv_r4_total.setText("0");

                    tv_r51.setText("0");
                    tv_r52.setText("0");
                    tv_r53_total.setText("0");
                }
            }

            @Override
            public void onFailure(Call<SuperAdminCountModel> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();
            }
        });
    }

    private void getSuperadminTodaysCamp() {
        final ProgressDialog progressDialog = new ProgressDialog(SiteSurvey_Menu_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.super_admin_call().create(ApiInterface.class);
        Call<SuperAdminCountModel> call = apiService.getTodaysCount(TodayDateNew, TodayDateNew);
        call.enqueue(new Callback<SuperAdminCountModel>() {
            @Override
            public void onResponse(Call<SuperAdminCountModel> call, Response<SuperAdminCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String dateTime = response.body().getDateTime();

                    if (status.equalsIgnoreCase("1")) {

//                        List<SuperAdminCountModel.Details> invoiceList = new ArrayList<>();

                        SuperAdminCountModel.Details details = response.body().getDetails();


//                        SuperAdminCountModel.Details details = (SuperAdminCountModel.Details) response.body().getDetails();


//                        if (details != null) {
//                            invoiceList.add(details); // Wrap single object into a list
//                        }


                        if (details != null) {


                            int b = 0;
                            int c = 0;
                            int d = 0;

                            int e = 0;
                            int f = 0;
                            int g = 0;


                            int h = 0;
                            int i = 0;
                            int j = 0;

                            int l = 0;
                            int m = 0;
                            int n = 0;

                            int z = 0;
                            int p = 0;
                            int k = 0;


                            b = b + details.getTreatmentGivenHLL();
                            c = c + details.getTreatmentGivenHSCC();
                            d = d + details.getTreatmentGivenTotal();


                            e = e + details.getIpdRegisteredHLL();
                            f = f + details.getIpdRegisteredHSCC();
                            g = g + details.getIpdRegisteredTotal();

                            h = h + details.getDischargePatientHLL();
                            i = i + details.getDischargePatientHSCC();
                            j = j + details.getDischargePatientTotal();


                            l = l + details.getPrescriptionGivenHLL();
                            m = m + details.getPrescriptionGivenHSCC();
                            n = n + details.getPrescriptionGivenTotal();


                            z = z + details.getPrescriptionIssuedHLL();
                            p = p + details.getPrescriptionIssuedHSCC();
                            k = k + details.getPrescriptionIssuedTotal();

                            tv_total_HSCC_Life_conducted_tratment_today.setText("" + b);
                            tv_total_HSCC_Life_HLL_conducted_treatment_today.setText("" + c);
                            tv_row_1_today.setText("" + d);

                            tv_regular_HLL_Life_conducted_treatment_today.setText("" + e);
                            tv_row22_today.setText("" + f);
                            tv_row_total_r2_today.setText("" + g);

                            tv_r1_today.setText("" + h);
                            tv_r32_today.setText("" + i);
                            tv_r3_total_today.setText("" + j);


                            tv_r41_today.setText("" + l);
                            tv_r42_today.setText("" + m);
                            tv_r4_total_today.setText("" + n);

                            tv_r51_today.setText("" + z);
                            tv_r52_today.setText("" + p);
                            tv_r53_total_today.setText("" + k);


                            tvdate_time_today.setText("Todays Data as of" + " " + dateTime);


//                                if (o.getSubOrgId().equalsIgnoreCase("2")) {
//                                    tv_total_HLL_Life_conducted.setText("" + o.getTotalConducted());
//                                    tv_regular_HLL_Life_conducted.setText("" + o.getRegularConductedCamp());
//                                    tv_d2d_HLL_Life_conducted.setText("" + o.getD2dConductedCamp());
//                                } else if (o.getSubOrgId().equalsIgnoreCase("3")) {
//                                    tv_total_HSCC_Life_conducted.setText("" + o.getTotalConducted());
//                                    tv_regular_HSCC_Life_conducted.setText("" + o.getRegularConductedCamp());
//                                    tv_d2d_HSCC_Life_conducted.setText("" + o.getD2dConductedCamp());
//                                }
//                            tv_todays_patient.setText(o.getTotal());
//                            tvRegularTodaysPatient.setText(o.getRegularCamp());
//                            tvD2dTodaysPatient.setText(o.getD2DCamp());


                            // Show error message if no match was found
//                            if (!isMatchFound) {
//                                Utilities.showAlertDialog(context, "Alert", "No camp scheduled for this camp date", false);
////                                edt_camp_date.setText("");
////                                edt_post_camp_date.setText("");
////                                edt_camp_address.setText("");
//                                Log.d("DEBUG", "No matching date found for the selected camp date.");
//                            }
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);


                        tv_total_HSCC_Life_conducted_tratment_today.setText("0");
                        tv_total_HSCC_Life_HLL_conducted_treatment_today.setText("0");
                        tv_row_1_today.setText("0");

                        tv_regular_HLL_Life_conducted_treatment_today.setText("0");
                        tv_row22_today.setText("0");
                        tv_row_total_r2_today.setText("0");

                        tv_r1_today.setText("0");
                        tv_r32_today.setText("0");
                        tv_r3_total_today.setText("0");


                        tv_r41_today.setText("0");
                        tv_r42_today.setText("0");
                        tv_r4_total_today.setText("0");

                        tv_r51_today.setText("0");
                        tv_r52_today.setText("0");
                        tv_r53_total_today.setText("0");
                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                    tv_total_HSCC_Life_conducted_tratment_today.setText("0");
                    tv_total_HSCC_Life_HLL_conducted_treatment_today.setText("0");
                    tv_row_1_today.setText("0");

                    tv_regular_HLL_Life_conducted_treatment_today.setText("0");
                    tv_row22_today.setText("0");
                    tv_row_total_r2_today.setText("0");

                    tv_r1_today.setText("0");
                    tv_r32_today.setText("0");
                    tv_r3_total_today.setText("0");


                    tv_r41_today.setText("0");
                    tv_r42_today.setText("0");
                    tv_r4_total_today.setText("0");

                    tv_r51_today.setText("0");
                    tv_r52_today.setText("0");
                    tv_r53_total_today.setText("0");
                }
            }

            @Override
            public void onFailure(Call<SuperAdminCountModel> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();
            }
        });
    }


    void getCampDetailsTodayCount() {

        progressBar.setVisibility(View.VISIBLE);
        ll_counts.setVisibility(View.GONE);

        apiInterface.getMonthlySurveySiteRequest(mMonth + 1, mYear, 0).enqueue(new Callback<CampCalendarModel>() {
            @Override
            public void onResponse(Call<CampCalendarModel> call, Response<CampCalendarModel> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        campList = new ArrayList<>();
                        totalTodaysCamp = new ArrayList<>();

                        campList = response.body().getOutput();
                        totalTodaysCamp = response.body().getOutput();
                        groupCampDetails(campList);


                    }
                }

                getCampDetailsTodayCountForOS();
                getCampDetailsTodayCountForOSNew();

            }

            @Override
            public void onFailure(Call<CampCalendarModel> call, Throwable t) {
                getCampDetailsTodayCountForOS();
                getCampDetailsTodayCountForOSNew();

            }
        });

    }

    void getCampDetailsTodayCountForOS() {

        progressBar.setVisibility(View.VISIBLE);
        ll_counts.setVisibility(View.GONE);

        apiInterface.getMonthlySurveySiteRequestForOS(mMonth + 1, mYear, 0, Integer.parseInt(campType)).enqueue(new Callback<CampCalendarModel>() {
            @Override
            public void onResponse(Call<CampCalendarModel> call, Response<CampCalendarModel> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ll_counts.setVisibility(View.VISIBLE);


                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        campList = new ArrayList<>();
                        totalTodaysCamp = new ArrayList<>();

                        campList = response.body().getOutput();
                        totalTodaysCamp = response.body().getOutput();
                        groupCampDetails(campList);
                        String dateTime = Utilities.dateTimeFormat.format(new Date());
                        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);
                    }
                }


//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);

            }

            @Override
            public void onFailure(Call<CampCalendarModel> call, Throwable t) {
//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);

            }
        });

    }

    void getCampDetailsTodayCountForOSNew() {

        progressBar.setVisibility(View.VISIBLE);
        ll_counts.setVisibility(View.GONE);

        D2DApiInterface.getMonthlySurveySiteRequestForOSNew(TodayDate).enqueue(new Callback<CampCalendarModel>() {
            @Override
            public void onResponse(Call<CampCalendarModel> call, Response<CampCalendarModel> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ll_counts.setVisibility(View.VISIBLE);


                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        campList = new ArrayList<>();
                        totalTodaysCamp = new ArrayList<>();

//                        campList = response.body().getOutput();
//                        totalTodaysCamp = response.body().getOutput();
//                        groupCampDetails(campList);

                        List<CampCalendarModel.OutputBean> todaysDetailsNew = response.body().getOutput();

                        for (CampCalendarModel.OutputBean o :
                                todaysDetailsNew) {

                            tv_todays_patient.setText("" + o.getTotal());
                            tvRegularTodaysPatient.setText(o.getRegularCamp());
                            tvD2dTodaysPatient.setText(o.getD2DCamp());

                        }


                        String dateTime = Utilities.dateTimeFormat.format(new Date());
                        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);
                    }

                }


//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);

            }

            @Override
            public void onFailure(Call<CampCalendarModel> call, Throwable t) {
//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);


            }
        });

    }

    void getCampDetailsTodaysPatientCount() {

        progressBar.setVisibility(View.VISIBLE);
//        ll_counts.setVisibility(View.GONE);

        D2DApiInterface.getMonthlyForTodaysPatient(TodayDate).enqueue(new Callback<CampCalendarModel>() {
            @Override
            public void onResponse(Call<CampCalendarModel> call, Response<CampCalendarModel> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
//                    ll_counts.setVisibility(View.GONE);


                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        campList = new ArrayList<>();
                        totalTodaysCamp = new ArrayList<>();

//                        campList = response.body().getOutput();
//                        totalTodaysCamp = response.body().getOutput();
//                        groupCampDetails(campList);

                        List<CampCalendarModel.OutputBean> todaysDetailsNew = response.body().getOutput();

                        int a = 0;
                        int b = 0;
                        int c = 0;
                        for (CampCalendarModel.OutputBean o :
                                todaysDetailsNew) {

                            a = a + o.getTotal();
                            b = b + o.getRegular();
                            c = c + o.getD2D();
                            tv_total_HSCC__HLLLife.setText("" + a);
                            tv_regular_HSCC_HLL_Life.setText("" + b);
                            tv_d2d_HSCC_HLL_Life.setText("" + c);


                            if (o.getSubOrgId().equalsIgnoreCase("2")) {
                                tv_total_HLL_Life.setText("" + o.getTotal());
                                tv_regular_HLL_Life.setText("" + o.getRegular());
                                tv_d2d_HLL_Life.setText("" + o.getD2D());


                            } else if (o.getSubOrgId().equalsIgnoreCase("3")) {
                                tv_total_HSCC_Life.setText("" + o.getTotal());
                                tv_regular_HSCC_Life.setText("" + o.getRegular());
                                tv_d2d_HSCC_Life.setText("" + o.getD2D());
                            }
//                            tv_todays_patient.setText(o.getTotal());
//                            tvRegularTodaysPatient.setText(o.getRegularCamp());
//                            tvD2dTodaysPatient.setText(o.getD2DCamp());

                        }


                        String dateTime = Utilities.dateTimeFormat.format(new Date());
                        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);
                    }

                }


//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);

            }

            @Override
            public void onFailure(Call<CampCalendarModel> call, Throwable t) {
//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);
                tv_total_HLL_Life.setText("");
                tv_regular_HLL_Life.setText("");
                tv_d2d_HLL_Life.setText("");
                tv_total_HSCC_Life.setText("");
                tv_regular_HSCC_Life.setText("");
                tv_d2d_HSCC_Life.setText("");

            }
        });

    }

    void getCampDetailsConductedCampCount() {

        progressBar.setVisibility(View.VISIBLE);
        ll_counts.setVisibility(View.GONE);

        D2DApiInterface.getMonthlyForTodaysPatient().enqueue(new Callback<CampCalendarModel>() {
            @Override
            public void onResponse(Call<CampCalendarModel> call, Response<CampCalendarModel> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    ll_counts.setVisibility(View.GONE);


                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        campList = new ArrayList<>();
                        totalTodaysCamp = new ArrayList<>();

//                        campList = response.body().getOutput();
//                        totalTodaysCamp = response.body().getOutput();
//                        groupCampDetails(campList);

                        List<CampCalendarModel.OutputBean> todaysDetailsNew = response.body().getOutput();


                        int b = 0;
                        int c = 0;
                        int d = 0;
                        for (CampCalendarModel.OutputBean o :
                                todaysDetailsNew) {

                            b = b + o.getTotalConducted();
                            c = c + o.getRegularConductedCamp();
                            d = d + o.getD2dConductedCamp();

                            tv_total_HSCC_Life_HLL_conducted.setText("" + b);
                            total_regular.setText("" + c);
                            total_d2d.setText("" + d);


                            if (o.getSubOrgId().equalsIgnoreCase("2")) {
                                tv_total_HLL_Life_conducted.setText("" + o.getTotalConducted());
                                tv_regular_HLL_Life_conducted.setText("" + o.getRegularConductedCamp());
                                tv_d2d_HLL_Life_conducted.setText("" + o.getD2dConductedCamp());
//                                tv_conducted_finacial_year.setText(o.getFinancialYear());
                            } else if (o.getSubOrgId().equalsIgnoreCase("3")) {
                                tv_total_HSCC_Life_conducted.setText("" + o.getTotalConducted());
                                tv_regular_HSCC_Life_conducted.setText("" + o.getRegularConductedCamp());
                                tv_d2d_HSCC_Life_conducted.setText("" + o.getD2dConductedCamp());
                            }
//                            tv_todays_patient.setText(o.getTotal());
//                            tvRegularTodaysPatient.setText(o.getRegularCamp());
//                            tvD2dTodaysPatient.setText(o.getD2DCamp());

                        }


                        String dateTime = Utilities.dateTimeFormat.format(new Date());
                        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);
                    }

                }


//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);

            }

            @Override
            public void onFailure(Call<CampCalendarModel> call, Throwable t) {
//                campType = "1";
//                tvRegularTodaysPatient.setText("");
//                new GetCampDetailsTodayCountForOS().execute(campType);
                tv_total_HLL_Life_conducted.setText("");
                tv_regular_HLL_Life_conducted.setText("");
                tv_d2d_HLL_Life_conducted.setText("");
                tv_total_HSCC_Life_conducted.setText("");
                tv_regular_HSCC_Life_conducted.setText("");
                tv_d2d_HSCC_Life_conducted.setText("");

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LogoutUser().execute(userId, BuildConfig.VERSION_NAME);
//        getAutoLogout();

    }


    private void verificationRemark(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_change_password, null);
        alertBuilder.setView(alertLayout);
        EditText tv_userName = alertLayout.findViewById(R.id.tv_userName);
        EditText tv_password = alertLayout.findViewById(R.id.tv_password);
        EditText tv_otp = alertLayout.findViewById(R.id.tv_otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);

        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();


        resendOtpBtn.setOnClickListener(view -> {
            //verify api call


//            if (edt_Otp.getText().toString().trim().length() != 10) {
//                edt_Otp.setError("Please enter valid otp");
//                return;
//            }


//            if (!edt_Otp.getText().toString().trim().equals(otpnumber)) {
//                edt_Otp.setError("Entered OTP is not matched");
//                return;
//            }


        });

        verifyOtpBtn.setOnClickListener(view -> {


            if (tv_password.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(context, "Alert", "Please enter password", false);
                return;
            }
            if (tv_password.getText().toString().length() < 6) {
                Utilities.showAlertDialog(context, "Alert", "Password should be 6 digit", false);
                return;
            }

            if (tv_otp.getText().toString().trim().matches("")) {
                Utilities.showAlertDialog(context, "Alert", "Please enter otp", false);
                return;
            }

            if (!tv_otp.getText().toString().trim().equals(otpnumber)) {
                tv_otp.setError("Entered OTP is not matched");
                return;
            }


//            new InsertOTPForUpdatePassword(2).execute( mobileNo);

            new VerifyOtp(alertDialog).execute(userId, tv_password.getText().toString().trim(), mobileNo, tv_otp.getText().toString().trim());


//            Utilities.showAlertDialog(context, "Alert", "OTP resend successfully", true);

//          new GetOtp(2).execute(edt_mobile_no.getText().toString(), otpnumber, RegId, UserId, UserId);

//            tv.setText("");

        });

    }


    private void setDefaults() {

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = pinfo.versionName;

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                name = json.getString("name");
                Designation = json.getString("Designation");
                DESGID = json.getInt("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                userId = String.valueOf(json.getInt("EmpCode"));
                mobileNo = json.getString("BMobile");

//                if (BuildConfig.isBeta){
                isFixedPay = json.getBoolean("IsFixedPay");

//                }
//                mobileNo = "9764568835";


                tv_username.setText(name);
                tv_designation.setText("(" + Designation + ")");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (BuildConfig.isBeta) {
//            if (!isFixedPay) {
//                allowMenu = "0";
//            } else {
//                allowMenu = "1";
//            }
//        }

        if (!isFixedPay) {
            allowMenu = "0";
        } else {
            allowMenu = "1";
        }

//        allowMenu = "0";


        if (DESGID == 30 || DESGID == 127) {
            rgLogin.setVisibility(View.GONE);
            rbAbkat.setVisibility(View.GONE);
            rbDoorToDoor.setVisibility(View.GONE);
        }


        if (DESGID == 51 || DESGID == 63 || DESGID == 101 || DESGID == 102 || DESGID == 47 || DESGID == 103 || DESGID == 170 || DESGID == 171 || DESGID == 202) {
            tv_username.setVisibility(View.GONE);
            tv_designation.setVisibility(View.GONE);
            rgLogin.setVisibility(View.GONE);
            ll_counts.setVisibility(View.VISIBLE);
//            mainLL_fibroscanner.setVisibility(View.VISIBLE);

            getCountsFromServer();

        } else {
            tv_username.setVisibility(View.VISIBLE);
            tv_designation.setVisibility(View.VISIBLE);
            ll_counts.setVisibility(View.GONE);
            rgLogin.setVisibility(View.VISIBLE);
        }
        if (DESGID == 182 || DESGID == 183 || DESGID == 71 || DESGID == 26 || DESGID == 196 || DESGID == 198) {
            tv_username.setVisibility(View.VISIBLE);
            tv_designation.setVisibility(View.VISIBLE);
            rgLogin.setVisibility(View.GONE);
        }

        if (DESGID == 201) {
            tv_username.setVisibility(View.GONE);
            tv_designation.setVisibility(View.GONE);
            rgLogin.setVisibility(View.GONE);
        }

        if (DESGID == 166) {
            llcount_todays_patient.setVisibility(View.VISIBLE);
            llcount_conducted_camp.setVisibility(View.VISIBLE);
            conducted_Tratment_count_LL.setVisibility(View.VISIBLE);
            todays_treatment_count_LL.setVisibility(View.VISIBLE);
            tv_username.setVisibility(View.GONE);
            ll_counts.setVisibility(View.GONE);
            tv_designation.setVisibility(View.GONE);
            rgLogin.setVisibility(View.GONE);


            getCampDetailsTodaysPatientCount();
            getCampDetailsConductedCampCount();
            getSuperAdminaData();
            getSuperadminTodaysCamp();


        } else {
            llcount_todays_patient.setVisibility(View.GONE);
            llcount_conducted_camp.setVisibility(View.GONE);
            conducted_Tratment_count_LL.setVisibility(View.GONE);
            todays_treatment_count_LL.setVisibility(View.GONE);
        }

        if (  DESGID == 168 || DESGID == 83) {
            tv_username.setVisibility(View.VISIBLE);
            tv_designation.setVisibility(View.VISIBLE);
            rgLogin.setVisibility(View.GONE);
        }

        new LogoutUser().execute(userId, versionName);

// remove or comment out this line if the Service calls the same API
// startService(new Intent(SiteSurvey_Menu_Activity.this, LogoutService.class).putExtra("userId", userId));

        WorkManager workManager = WorkManager.getInstance(context);

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                LogoutWorkManager.class,
                1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        workManager.enqueueUniquePeriodicWork(
                "LogoutChecker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
        );

        String imei = DeviceIdUtil.getDeviceId(context);

        tokenIDString = imei;


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        View headerView = navigationView.getHeaderView(0);

        ImageView navHome = headerView.findViewById(R.id.menu_leftbtn);
        TextView tv_change_pass = headerView.findViewById(R.id.tv_change_pass);
        TextView tv_userName = headerView.findViewById(R.id.tv_userName);
        TextView tv_designation = headerView.findViewById(R.id.tv_designation);
        TextView tv_vsersion_number = headerView.findViewById(R.id.tv_vsersion_number);

        tv_userName.setText(name);
        if (DESGID == 201) {
            tv_designation.setText("");
        } else {
            tv_designation.setText(Designation);
        }

        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new InsertOTPForUpdatePassword(1).execute(mobileNo);
            }
        });

        tv_vsersion_number.setText("App Version" + ":-" + " " + versionName);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (session.isHllUser()) {
            rbAbkat.setChecked(false);
            rbDoorToDoor.setChecked(true);
        } else {
            rbAbkat.setChecked(true);
            rbDoorToDoor.setChecked(false);
        }
        setupMenu();
    }

    void setupMenu() {
        menuList = new ArrayList<MenuListPojo>();

        if (BuildConfig.isBeta) {
            if (session.isHllUser()) {
                switch (DESGID) {
                    case 23:    // --- Survey Coordinator
                        menuList.add(new MenuListPojo("Site Survey Request List", R.drawable.icon_campcreation, SiteSurveyRequestList_Actvity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 156:   //Service Delivery Coordinator

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));

/// New Change
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 157:///Delivery Executive
                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
                        break;

                    case 92: //D2D Camp Coordinator
                    case 162: //D2d Camp Coordinator


                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));



                        /// new change
//                        menuList.add(new MenuListPojo("Medicine Return", R.drawable.icon_medicinedelivery, MedicineReturnActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));





//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Common Beneficiary List", R.drawable.icon_medicinedelivery, CTAndedicineCommonBeneficiaryListActivity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));

                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamEmergencyPatientActivity.class));

                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));

                        break;


                    case 160: //Maha Arogya Saarathi
                    case 108: //Lab Coordinator
                    case 136: //Mmu T2t Camp Coordinator
                    case 139: //Mmu T2t Arogasathi

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));

                        /// new change
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));


//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));

                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamEmergencyPatientActivity.class));
                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 84: //District Coordinator
                    case 77: //Health Camp Screening Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        break;


                    case 165: //Cw Lab Assistant
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;
                    case 54:    // --- Surveyor Executive
                        menuList.add(new MenuListPojo("Construction Work (RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_RERASiteList_Activity.class));
                        menuList.add(new MenuListPojo("Construction Work (NON RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Demolition", R.drawable.icon_building_demolition, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Maintenance", R.drawable.icon_building_maintenance, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Bridge Construction", R.drawable.icon_bridge_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Road Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Metro Construction", R.drawable.icon_metro_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Toll Plaza Construction", R.drawable.icon_toll_plaza, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Other Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;

                    case 29:    // --- Camp Coordinator or Post Camp Coordinator

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));

                        /// new change
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));



//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.ic_packetassignment, PacketAssignmentActivity.class));
                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));

//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));


//                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));

//                    menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CampRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        // menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
//                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        //  menuList.add(new MenuListPojo("Team-Camp Mapping Details", R.drawable.ic_group_outlined, TeamCampMappingdetailsActivity.class));
                        // menuList.add(new MenuListPojo("IBE Screening Closing", R.drawable.icon_healthscreening, CampSelectionForIbeScreeningClosing_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        // menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        //  menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
//                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivityForCC.class));
//                        menuList.add(new MenuListPojo("Call To Doctor", R.drawable.icon_campcreation, CallToDoctorRequestActivity.class));
                        // menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("D2D Camp Activity", R.drawable.icon_campcreation, CampSelectionForD2DActivity_Activity.class));


                        break;


                    case 170: //Regional Manager
                    case 171:    //Vice President
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

                        break;


                    case 166: //Logistic Coordinator
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        break;

                    case 115: //Lab Logistic Executive

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));

                        ///new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketReallocationActivity.class));


//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 131: //Report Delivery Executive


                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;

                    case 167: //Divisional Manager
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        break;


                    case 34:    // --- Doctor
                    case 130:    // --- Doctor
                    case 147:  //Flexi Camp Doctor
                    case 141:  //Mmu T2t Medical Officer

                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        // menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("D2d Physical Examination Details", R.drawable.ic_physical_details, D2dPhysicalExaminationDetailsActivity.class));

                        if (allowMenu.equals("0")) {
                            menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceForDoctorActivity.class));
                        }

                        //menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
//                        menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
//                        menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        //  menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        //  menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        menuList.add(new MenuListPojo("D2D Availability Screening", R.drawable.icon_userattendance, D2DAvailabilityActivity.class));
                        menuList.add(new MenuListPojo("D2D Health Screening", R.drawable.icon_physical_examination, D2DSelectCampActivity.class));

                        break;

                    case 135:///Delivery Executive

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));



                        /// new change
//                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 58:    // --- LFT (Lung) Technician LungFunction
                    case 41:    // --- AVT (Audio) Technician visual tech
                    case 57:    // --- VST (Visual) Technician Audio Technician
                    case 105:    // --- Plhebo
                    case 4:    // --- HLL Plhebo
                    case 35:
                    case 86: //Data Entry Operator
                    case 64: ///data entry operator
                    case 129: ///Flexi
                    case 146:///Flexi



                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));
                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));
                        menuList.add(new MenuListPojo("Common Beneficiary List", R.drawable.icon_medicinedelivery, CTAndedicineCommonBeneficiaryListActivity.class));

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));

                        /// new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceActivity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;


                    case 138:///Mmu T2t Phlebotomist
                    case 169:///Ert Mmu
                    case 177:///Ert Mmu
                    case 137:///Nurse
                    case 176:///Nurse Mmu

                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));


                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        ///new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 173: //CT Manager
                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuNewActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

                        break;

                    case 30:
                    case 127:
                        menuList.add(new MenuListPojo("Calling List", R.drawable.icon_userattendance, Expected_BeneficiariesActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));

                        //   menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        break;


                    case 32:    // --- Camp Admin
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("CAMP Closing", R.drawable.icon_closing, CampSelectionForCampClosingList_Activity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));

                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //  menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        // menuList.add(new MenuListPojo("Inventory Item Consumption", R.drawable.icon_itemconsuption, CampSelectionForItemconsumption_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 37:    // --- pre camp planner coordinator
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //   menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 52:    // --- GLO
                        menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 53:    // --- Site Coordinator
                        menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
//                    menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

                    case 24:    // --- Nominated Officer
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 26:    // --- Hll Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 51:    // --- Construction Worker Admin
                    case 202:    // --- Project Incharge
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));

                        break;
                    case 83:    // --- Division Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

//                        menuList.add(new MenuListPojo("Breast Screening Dashboard", R.drawable.icon_female_1, DashboardBreastScreeningCounts_Activity.class));
                        break;

                    case 128:

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        break;


                    case 69:    // --- IBE
                        menuList.add(new MenuListPojo("Breast Screening", R.drawable.icon_female_1, CampSelectionForBreastScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 79:    // --- Post Camp Administrator
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 61:    // --- Project Head
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 60:    // --- CEO
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));


                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    default:
                        Utilities.showAlertDialog(context, "Alert",
                                "You are not authorised user to use this application, Please contact to support team.",
                                false, "Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.logoutUser();
                                        finish();
                                    }
                                });
                        break;
                }

            } else {
                switch (DESGID) {
                    case 23:    // --- Survey Coordinator
                        menuList.add(new MenuListPojo("Site Survey Request List", R.drawable.icon_campcreation, SiteSurveyRequestList_Actvity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;
                    case 54:    // --- Surveyor Executive
                        menuList.add(new MenuListPojo("Construction Work (RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_RERASiteList_Activity.class));
                        menuList.add(new MenuListPojo("Construction Work (NON RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Demolition", R.drawable.icon_building_demolition, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Maintenance", R.drawable.icon_building_maintenance, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Bridge Construction", R.drawable.icon_bridge_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Road Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Metro Construction", R.drawable.icon_metro_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Toll Plaza Construction", R.drawable.icon_toll_plaza, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Other Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

                    case 157:///Delivery Executive
                        break;


                    case 156:   //Service Delivery Coordinator

//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 135:///Delivery Executive
                        break;

                    case 162: //D2d Camp Coordinator
                    case 160: //Maha Arogya Saarathi
                    case 92: //D2D Camp Coordinator
                    case 108: //Lab Coordinator
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Device & Resource Mapping", R.drawable.ic_group_outlined, CampCreationActivityV4.class));
                        menuList.add(new MenuListPojo("Resource Re-Mapping", R.drawable.ic_post_camp, CampSelectionForCampUpdate_Activity.class));
                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Camp Approval", R.drawable.icon_campcreation, CampStatusDashboardActivity.class));
                        break;
                    case 146: //Flexi
//                    case 130: //Flexi Doctor
                    case 136: //Mmu T2t Camp Coordinator
                    case 139: //Mmu T2t Arogasathi
                    case 141:  //Mmu T2t Medical Officer
                    case 138:///Mmu T2t Phlebotomist
                    case 169:///ERT MMU
                    case 177:///ERT MMU
                    case 137:///Nurse
                    case 176:///Nurse
                    case 31:///Nurse

                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));



                        break;

                    case 115: //Lab Logistic Executive
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.icon_medicinedelivery, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 178: //Lab Logistic Executive
                    case 85: //Lab Logistic Executive
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));


                        break;


                    case 131: //Report Delivery Executive
//                        menuList.add(new MenuListPojo("Pickup Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 84: //District Coordinator
                    case 77: //Health Camp Screening Coordinator
//                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;

                    case 29:    // --- Camp Coordinator
                        menuList.add(new MenuListPojo("Device & Resource Mapping", R.drawable.ic_group_outlined, CampCreationActivityV4.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));

                        menuList.add(new MenuListPojo("Resource Re-Mapping", R.drawable.ic_post_camp, CampSelectionForCampUpdate_Activity.class));
//                        menuList.add(new MenuListPojo("Camp Approval", R.drawable.icon_campcreation, CampStatusDashboardActivity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));

//                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamActivity.class));
//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));


                        // menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        //  menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_patient, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
//                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        // menuList.add(new MenuListPojo("Update Process Lab", R.drawable.icon_campcreation, UpdateProcessLabActivity.class));
//                   menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CampRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        // menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Team-Camp Mapping Details", R.drawable.ic_group_outlined, TeamCampMappingdetailsActivity.class));

                        //  menuList.add(new MenuListPojo("IBE Screening Closing", R.drawable.icon_healthscreening, CampSelectionForIbeScreeningClosing_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivityForCC.class));
                        // menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));

                        break;


                    case 170: //Regional Manager
                    case 171:    //Vice President
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
//                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));


                        break;

//                    case 173: //Lab Technician
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//
//                        break;
                    case 165:  //Cw Lab Assistant
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    case 166: //Super Admin
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        break;

                        

                    case 167: //Divisional Manager
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        break;

                    case 34:// --- Doctor
                    case 130: //Flexi Doctor
                    case 147://Flexi Camp Doctor
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));

                        if (allowMenu.equals("0")) {
//                            menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceForDoctorActivity.class));

                        }
//                        menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceActivity.class));

                        //   menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        //  menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        //  menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("D2D Availability Screening", R.drawable.icon_userattendance, D2DAvailabilityActivity.class));
                        menuList.add(new MenuListPojo("D2D Health Screening", R.drawable.icon_physical_examination, D2DSelectCampActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;
                    case 58:    // --- LFT (Lung) Technician LungFunction
                    case 41:    // --- AVT (Audio) Technician visual tech
                    case 57:    // --- VST (Visual) Technician Audio Technician
                    case 35:    // --- Plhebo
                    case 129: //Flexi
                    case 86: //Data Entry Operator/executive
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));


//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
//                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        //menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 128:
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));


                        //   menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 32:    // --- Camp Admin
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("CAMP Closing", R.drawable.icon_closing, CampSelectionForCampClosingList_Activity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));

                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //  menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        // menuList.add(new MenuListPojo("Inventory Item Consumption", R.drawable.icon_itemconsuption, CampSelectionForItemconsumption_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 37:    // --- pre camp planner coordinator
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //   menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        // menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 52:    // --- GLO
                        // menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 53:    // --- Site Coordinator
                        //    menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //    menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
//                    menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

                    case 24:    // --- Nominated Officer
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

//                    case 26:    // --- Hll Coordinator
//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//
//                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        finish();
//                        break;

                    case 30:
                    case 127:
                        menuList.add(new MenuListPojo("Calling List", R.drawable.icon_userattendance, Expected_BeneficiariesActivity.class));
                        menuList.add(new MenuListPojo("Calling Dashboard", R.drawable.icon_userattendance, CallingDashboardActivity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));

                        // menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));

                        break;


                    case 51:    // --- Construction Worker Admin
                    case 202:    // --- Project Incharge

                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("Analytical Dashboards", R.drawable.icon_power_bi_main, PowerBIDashboard.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));


//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));

                        break;




                    case 173: //President/Ceo login
                    case 168:
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;


                    case 83:    // --- Division Manager
                    case 47://Project Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
//                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
//                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));

//                        menuList.add(new MenuListPojo("Breast Screening Dashboard", R.drawable.icon_female_1, DashboardBreastScreeningCounts_Activity.class));
                        break;

                    case 69:    // --- IBE
                        menuList.add(new MenuListPojo("Breast Screening", R.drawable.icon_female_1, CampSelectionForBreastScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 79:    // --- Post Camp Administrator
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 61:    // --- Project Head
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 60:    // --- CEO
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 64:
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    default:
                        Utilities.showAlertDialog(context, "Alert",
                                "You are not authorised user to use this application, Please contact to support team.",
                                false, "Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.logoutUser();
                                        finish();
                                    }
                                });
                        break;
                }
            }
        } else {
            if (session.isHllUser()) {
                switch (DESGID) {
                    case 23:    // --- Survey Coordinator
                        menuList.add(new MenuListPojo("Site Survey Request List", R.drawable.icon_campcreation, SiteSurveyRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    case 165: //Cw Lab Assistant
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    case 54:    // --- Surveyor Executive
                        menuList.add(new MenuListPojo("Construction Work (RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_RERASiteList_Activity.class));
                        menuList.add(new MenuListPojo("Construction Work (NON RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Demolition", R.drawable.icon_building_demolition, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Maintenance", R.drawable.icon_building_maintenance, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Bridge Construction", R.drawable.icon_bridge_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Road Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Metro Construction", R.drawable.icon_metro_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Toll Plaza Construction", R.drawable.icon_toll_plaza, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Other Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;


                    case 148://Central Post Camp Coordinator
                    case 178://Senior Lab Technician
                    case 174://Accession Executive
                    case 75://Account Login
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

                        break;


                    case 157:///Delivery Executive
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//
                        break;

                    case 30:
                    case 130:
                        menuList.add(new MenuListPojo("Calling List", R.drawable.icon_userattendance, Expected_BeneficiariesActivity.class));

                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));


                        break;


                    case 131: //Report Delivery Executive

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        break;

                    case 156:   //Service Delivery Coordinator

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));

                        break;

                    case 115: //Lab Logistic Executive // Lab Logistic Coordinator
                    case 190: //Logistic Executive
                    case 85: //Logistic Executive

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));


                        break;


                    case 29:    // --- Camp Coordinator
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));



                        ///new change
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));


                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamEmergencyPatientActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));

//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("Update Process Lab", R.drawable.icon_campcreation, UpdateProcessLabActivity.class));
                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));

//                    menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CampRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        // menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("IBE Screening Closing", R.drawable.icon_healthscreening, CampSelectionForIbeScreeningClosing_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        //  menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        //   menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivityForCC.class));

//                        menuList.add(new MenuListPojo("Call To Doctor", R.drawable.icon_campcreation, CallToDoctorRequestActivity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));

                        break;


//                    case 178: //Senio
//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//
//
//                        break;


                    case 182: // Regional Manager
                    case 183: // Vice President
                    case 26: // Sr.Project Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));

                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

                        break;


//                    case 30:
//                        menuList.add(new MenuListPojo("Calling List", R.drawable.icon_userattendance, PostCampCounts_Activity.class));
//                       // menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        break;

                    case 34:    // --- Doctor
                    case 147:    // --- Doctor
                    case 141:  //Mmu T2t Medical Officer
                    case 181:  //Medical Officer - MMU


                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        if (allowMenu.equals("0")) {
                            menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceForDoctorActivity.class));
                        }


                        //menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        // menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
//                        menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
//                        menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_road_construction, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));

                        menuList.add(new MenuListPojo("D2d Physical Examination Details", R.drawable.ic_physical_details, D2dPhysicalExaminationDetailsActivity.class));

                        //   menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        //   menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("D2D Availability Screening", R.drawable.icon_userattendance, D2DAvailabilityActivity.class));
//                        menuList.add(new MenuListPojo("D2D Health Screening", R.drawable.icon_physical_examination, D2DSelectCampActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;
                    case 58:    // --- LFT (Lung) Technician LungFunction
                    case 41:    // --- AVT (Audio) Technician visual tech
                    case 57:    // --- VST (Visual) Technician Audio Technician
                        // case 105:    // --- HLL Plhebo
                    case 4:    // --- HLL Plhebo
                    case 35:
                    case 86: //Data Entry Operator
                    case 146: //Flexi phebo


                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));

                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));
//                        menuList.add(new MenuListPojo("Common Beneficiary List", R.drawable.icon_medicinedelivery, CTAndedicineCommonBeneficiaryListActivity.class));

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuNewActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceActivity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));

                        break;


                    case 138:///Mmu T2t Phlebotomist
                    case 169:///Ert Mmu
                    case 177:///Ert Mmu
                    case 137:///Nurse
                    case 31:///Nurse
                    case 176:///Nurse


                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Payment & Invoice", R.drawable.icon_payment, PayoutInvoiceActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Appointments & Sample Collection Of CT", R.drawable.icon_userattendance, ConfirmatoryTestNewActivity.class));
                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));


                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));

                        break;

                    case 104: //District Manager
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));

                        break;
                    case 84: //District Coordinator
                    case 77: //Health Camp Screening Coordinator
                    case 105: //Block Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 32:    // --- Camp Admin
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("CAMP Closing", R.drawable.icon_closing, CampSelectionForCampClosingList_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 37:    // --- pre camp planner coordinator
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 52:    // --- GLO
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                    case 166: //Logistic Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));

                        break;
                    case 167: //Divisional Manager
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));


                        break;

                    case 78:    // --- Post Camp Administrator

                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        break;


                    case 53:    // --- Site Coordinator

                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

                    case 24:    // --- Nominated Officer
                    case 151:    // --- Senior Phlebotomist Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));



                        break;

                    case 128:

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));

                        /// new change
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        break;


//                    case 26:    // --- Hll Coordinator
//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
////                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//
//                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        finish();
//                        break;
                    case 51:    // --- Construction Worker Admin
                    case 202:    // --- Project Incharge

                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));

                        break;
                    case 101:// Project Head
                    case 102://Project Incharge Operations
                    case 103://  Assistant Project Manager
                    case 47://Project Manager
                    case 83:    // --- Division Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

                        break;

                    case 69:    // --- IBE
                        menuList.add(new MenuListPojo("Breast Screening", R.drawable.icon_female_1, CampSelectionForBreastScreeing_Activity.class));

                        break;

                    case 79:    // --- Post Camp Administrator
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;

                    case 70: //---Post Camp Executive
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 92: //---Dd Camp Coordinator


//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));
                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));


                        /// new change
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Return", R.drawable.icon_medicinedelivery, MedicineReturnActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));

                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamEmergencyPatientActivity.class));
                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));

                        break;


                    case 160: //Maha Arogya Saarathi
                    case 108: //Lab Coordinator
                    case 136: //Mmu T2t Camp Coordinator
                    case 139: //Mmu T2t Arogasathi


                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));
                        /// new change


//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));

//
//                        if (DESGID == 160) {
//                            menuList.add(new MenuListPojo("Medicine Return", R.drawable.icon_medicinedelivery, MedicineReturnActivity.class));
//
//                        }

                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamEmergencyPatientActivity.class));
                        menuList.add(new MenuListPojo("D2d Team", R.drawable.icon_resources, D2dTeamsNotWorkingTeamActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        menuList.add(new MenuListPojo("Acknowledgement", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));

                        break;


                    case 172:
                    case 186:

                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuNewActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamActivity.class));
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
                        break;


//                    case 172:
//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//

//
//                        break;

                    case 113: // Lab Incharge
                    case 123: // Mis Executive
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

//                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        finish();
                        break;


                    case 61:    // --- Project Head
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    case 60:    // --- CEO
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        startActivity(new Intent(context, CampCalendar_Activity.class));
                        finish();
                        break;

                    default:
//                        Utilities.showAlertDialog(context, "Alert",
//                                "You are not authorised user to use this application, Please contact to support team.",
//                                false, "Logout", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        session.logoutUser();
//                                        finish();
//                                    }
//                                });
                        //  menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                }
                // menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

            } else {
                switch (DESGID) {
                    case 28:    // --- Survey Coordinator
                        menuList.add(new MenuListPojo("Site Survey Request List", R.drawable.icon_campcreation, SiteSurveyRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;
                    case 23:    // --- Surveyor Coordinator
                        menuList.add(new MenuListPojo("Site Survey Request List", R.drawable.icon_campcreation, SiteSurveyRequestList_Actvity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


//                    case 172:
//                    case 186:
//
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.packetreceive, MedicineDeliveryMenuNewActivity.class));
//
////                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
////                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamActivity.class));
////                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
////                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
////                        menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
////                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
////                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        break;

                    case 54:    // --- Surveyor Executive
                        menuList.add(new MenuListPojo("Construction Work (RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_RERASiteList_Activity.class));
                        menuList.add(new MenuListPojo("Construction Work (NON RERA Reg. Site)", R.drawable.icon_construction_working, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Construction", R.drawable.icon_building_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Demolition", R.drawable.icon_building_demolition, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Building Maintenance", R.drawable.icon_building_maintenance, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Bridge Construction", R.drawable.icon_bridge_const, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Road Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Metro Construction", R.drawable.icon_metro_construction, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Toll Plaza Construction", R.drawable.icon_toll_plaza, SiteSurvey_BuildingConstDetails_Activity.class));
                        menuList.add(new MenuListPojo("Other Construction", R.drawable.icon_road_construction, SiteSurvey_BuildingConstDetails_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

//                    case 173: //Lab Technician
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
////                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//
//                        break;
                    case 165: //Cw Lab Assistant
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    case 166: //Super Admin
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));

//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));


                        break;

                    case 167: //Divisional Manager
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

                        break;

                    case 148://Central Post Camp Coordinator
                    case 71://Assistance Commissioner of Labour
                    case 178://Senior Lab Technician
                    case 174://Accession Executive
                    case 75://Account Login
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        break;


//                    case 176: //Nurse
//                    case 31:    // --- Nurse
                    case 58:    // --- LFT (Lung) Technician LungFunction`
                    case 43:    // --- LFT (Lung) Technician LungFunction
                    case 42:    // --- AVT (Audio) Technician visual tech
                    case 57:    // --- AVT (Audio) Technician visual tech
                    case 41:    // --- VST (Visual) Technician Audio Technician
                    case 65:    // --- VST (Visual) Technician Audio Technician
                    case 35:    // --- Phlebo
                    case 86: //Data Entry Operator
                    case 146: //Flexi

//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));

//                       menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
//                        menuList.add(new MenuListPojo("Appointment Confirmed List", R.drawable.icon_userattendance, ExpectectedBeneficiaryForTeamsActivity.class));
                        //   menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //   menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        // menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("D2D Availability Screening", R.drawable.icon_userattendance, D2DAvailabilityActivity.class));
//                        menuList.add(new MenuListPojo("D2D Health Screening", R.drawable.icon_physical_examination, D2DSelectCampActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
//                        menuList.add(new MenuListPojo("Call To Doctor", R.drawable.icon_campcreation, CallToDoctorRequestActivity.class));

                        break;


                    case 128:
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report, ReportDeliveryActivity.class));

                        //   menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;
                    case 156:   //Service Delivery Coordinator

//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;


                    case 30:
                    case 130:
                        menuList.add(new MenuListPojo("Calling List", R.drawable.icon_userattendance, Expected_BeneficiariesActivity.class));
                        menuList.add(new MenuListPojo("Calling Dashboard", R.drawable.icon_userattendance, CallingDashboardActivity.class));

//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));

                        //  menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        //   menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;


                    case 115: //Lab Logistic Executive
//                        menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                    case 131: //Report Delivery Executive
//                        menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
//                        menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 29:    // --- Camp Coordinator
                    case 59:    // --- Camp Administrator Reg Desk

//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));

                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsActivity.class));

                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("Device & Resource Mapping", R.drawable.ic_group_outlined, CampCreationActivityV4.class));
                        menuList.add(new MenuListPojo("Resource Re-Mapping", R.drawable.ic_post_camp, CampSelectionForCampUpdate_Activity.class));
//                        menuList.add(new MenuListPojo("Check Beneficiary Details", R.drawable.ic_baseline_person_search_24, SearchBeneficiaryDetailsActivity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
//                        menuList.add(new MenuListPojo("Accept Samples", R.drawable.icon_healthscreening, AcceptSamplesInLabActivity.class));
//                        menuList.add(new MenuListPojo("Centrifuge Confirmation", R.drawable.icon_healthscreening, CentriFugeConfirmationActivity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("Update Process Lab", R.drawable.icon_campcreation, UpdateProcessLabActivity.class));
//                   menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CampRequestList_Actvity.class));
                        // menuList.add(new MenuListPojo("Inventory Item Consumption", R.drawable.icon_itemconsuption, CampSelectionForItemconsumption_Activity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //  menuList.add(new MenuListPojo("IBE Screening Closing", R.drawable.icon_healthscreening, CampSelectionForIbeScreeningClosing_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //   menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Team-Camp Mapping", R.drawable.ic_group_outlined, TeamCampMappingActivity.class));
                        //  menuList.add(new MenuListPojo("Team-Camp Mapping Details", R.drawable.ic_group_outlined, TeamCampMappingdetailsActivity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        // menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        //  menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("CSC Camp Request", R.drawable.icon_campcreation, CampRequestActivityForCC.class));

                        break;

//                case 7:     // --- Doctor

                    case 182: //Regional Manager
                    case 183:  //Vice President
                    case 26: // Sr.Project Manager
                    case 196: // Operation Executive
                    case 198: // Sr Regional Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
                        break;

                    case 34:    // --- Doctor
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
//                        menuList.add(new MenuListPojo("D2D Availability Screening", R.drawable.icon_userattendance, D2DAvailabilityActivity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
//                        menuList.add(new MenuListPojo("D2D Health Screening", R.drawable.icon_physical_examination, D2DSelectCampActivity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //   menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //   menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        //  menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        //  menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
                        //  menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
                        break;
                    case 104: //District Manager
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;
                    case 84: //District Coordinator
                    case 77: //Health Camp Screening Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));

//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 32:    // --- Camp Admin
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
                        menuList.add(new MenuListPojo("CAMP Closing", R.drawable.icon_closing, CampSelectionForCampClosingList_Activity.class));
                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));

                        // menuList.add(new MenuListPojo("Inventory Item Consumption", R.drawable.icon_itemconsuption, CampSelectionForItemconsumption_Activity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
                        //  menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
                        //  menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 37:    // --- pre camp planner coordinator
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Patient Registration", R.drawable.icon_patient, CampSelectionForInstantPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Registration and Attendance", R.drawable.icon_patient, CampSelectionForPatientRegistration_Activity.class));
//                    menuList.add(new MenuListPojo("Patient Attendance", R.drawable.icon_attandance, CampSelectionForPatientAttendance_Activity.class));
//                    menuList.add(new MenuListPojo("Instant Registration", R.drawable.icon_instant_reg, InstantRegCampSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        //  menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
                        menuList.add(new MenuListPojo("Fingerprint Upload", R.drawable.icon_fingerprint, UploadFingerPrint_Activity.class));
//                        menuList.add(new MenuListPojo("Health Card Upload", R.drawable.icon_unregno, CampSelectForHealthCardUpload_Activity.class));
//                        menuList.add(new MenuListPojo("Acknowledgment", R.drawable.icon_acknowledgement_1, CampSelectForAcknowledgement_Activity.class));
                        menuList.add(new MenuListPojo("ELearning", R.drawable.icon_elearning, ELearning.class));
                        break;

                    case 157:///Delivery Executive
                        break;

                    case 52:    // --- GLO
                        //  menuList.add(new MenuListPojo("Registration Number for Un-Reg. Workers", R.drawable.icon_unregno, UnregWorkerSiteForWorkerNo_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        break;

                    case 53:    // --- Site Coordinator
                        //   menuList.add(new MenuListPojo("Registration of Un-Reg. Workers", R.drawable.icon_unreg, UnregisteredWorkerSiteSelection_Activity.class));
                        //   menuList.add(new MenuListPojo("Worker-Site Mapping", R.drawable.icon_worker_site, WorkerSiteMappingSiteSelection_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
                        menuList.add(new MenuListPojo("Camp Awareness Activity", R.drawable.icon_campawareness, CampAwarenessCampList_Activity.class));
                        break;

                    case 24:    // --- Nominated Officer
                    case 113: // Lab Incharge
                    case 123: // Mis Executive
                    case 151: // Senior Phlebotomist Coordinator
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));

//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

//                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        finish();
                        break;

//                    case 26:    // --- Hll Coordinator
//                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
////                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//
//                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        finish();
//                        break;

                    case 69:    // --- IBE
                        menuList.add(new MenuListPojo("Breast Screening", R.drawable.icon_female_1, CampSelectionForBreastScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;

                    case 78:    // --- Post Camp Administrator
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        break;
                    case 70: //---Post Camp Executive
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));

                        break;

                    case 51:    // --- Construction Worker Admin
                    case 202:    // --- Project Incharge

                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));


//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
//                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
//                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
//                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        break;


                    case 201:    // CTO
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

//                        menuList.add(new MenuListPojo("Liver Scanning", R.drawable.heart_rate_monitor, DashboardPatientApp.class));
//                        menuList.add(new MenuListPojo("S2T Patient App", R.drawable.ios_android, DashboardPatientAppNew.class));


//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
//                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
//                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
//                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
                        break;


                    case 173: //President/Ceo login
                    case 168:
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));
                        startActivity(new Intent(context, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
                        break;

                    case 162: //D2d Camp Coordinator
                    case 160: //Maha Arogya Saarathi
                    case 92: //D2D Camp Coordinator
                    case 108: //Lab Coordinator
                        menuList.add(new MenuListPojo("Camp Creation", R.drawable.icon_campcreation, CreateCamp_Activity_v3.class));
                        menuList.add(new MenuListPojo("Team Photos", R.drawable.icon_userattendance, AttendanceDetailsForCCActivity.class));
                        menuList.add(new MenuListPojo("Device & Resource Mapping", R.drawable.ic_group_outlined, CampCreationActivityV4.class));
                        menuList.add(new MenuListPojo("Resource Re-Mapping", R.drawable.ic_post_camp, CampSelectionForCampUpdate_Activity.class));
                        menuList.add(new MenuListPojo("Camp Readiness Form", R.drawable.icon_campcreation, CampredinessNewActivity.class));
                        menuList.add(new MenuListPojo("Expense/Claim", R.drawable.icon_camp_calendar, ExpenseClaimDashboardActivity.class));
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Health Screening Details", R.drawable.icon_healthscreening, CampSelectionForHealthScreeing_Activity.class));
//                        menuList.add(new MenuListPojo("Camp Approval", R.drawable.icon_campcreation, CampStatusDashboardActivity.class));
                        break;

                    case 101:// Project Head
                    case 102://Project Incharge Operations
                    case 103://  Assistant Project Manager
                    case 47://Project Manager

                    case 83:    // --- Division Manager
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
                        menuList.add(new MenuListPojo("Rescreening Dashboard", R.drawable.icon_dailyworkdashboard, DailyWorkDashboardForAdminActivity.class));

//                        menuList.add(new MenuListPojo("Target Tracking", R.drawable.icon_postcamp_target, SetPostCampTarget_Activity.class));
//                        menuList.add(new MenuListPojo("Resource List", R.drawable.icon_resources, ResourceList_Activity.class));
//                        menuList.add(new MenuListPojo("Post Camp", R.drawable.icon_patient, PostCampCounts_Activity.class));
//                        menuList.add(new MenuListPojo("D2d Teams", R.drawable.icon_resources, D2dTeamsActivity.class));
//                        menuList.add(new MenuListPojo("Sample Processing", R.drawable.icon_sampleprocessing, SampleProcessingStatus_Activity.class));
//                        menuList.add(new MenuListPojo("Availability Dashboard", R.drawable.icon_userattendance, DashboardAvailabilityReportDistrict_Activty.class));
//                        menuList.add(new MenuListPojo("Post Camp Readiness", R.drawable.icon_readiness, DashboardInvoice_Activity.class));
//                        menuList.add(new MenuListPojo("Courier Activity", R.drawable.icon_acknowledgement_1, Courier_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

//                        menuList.add(new MenuListPojo("Breast Screening Dashboard", R.drawable.icon_female_1, DashboardBreastScreeningCounts_Activity.class));

//                    startActivity(new Intent(context, CampCalendar_Activity.class));
                        break;

//                case 61:  // --- Project Head
//                    menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                    startActivity(new Intent(context, CampCalendar_Activity.class));
//                    break;
//
//                case 60:    // --- CEO
//                    menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                    startActivity(new Intent(context, CampCalendar_Activity.class));
//                    break;

                    case 63:    // --- Dashboard User
                        menuList.add(new MenuListPojo("Camp Calendar", R.drawable.icon_camp_calendar, CampCalendar_Activity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

//                    startActivity(new Intent(context, CampCalendar_Activity.class));
//                    finish();
                        break;

                    default:

//                        Utilities.showAlertDialog(context, "Alert",
//                                "You are not authorised user to use this application, Please contact to support team.",
//                                false, "Logout", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        session.logoutUser();
//                                        finish();
//                                    }
//                                });
                        // menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));

                        break;
                }
            }

            if (DESGID == 51 || DESGID == 166 || DESGID == 173 || DESGID == 182 || DESGID == 183 || DESGID == 196 || DESGID == 198 || DESGID == 202) {

            } else {
                menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, AvailabilityNew_Activity.class));
            }


        }

        if (menuList != null) {
            setMenuRecyclerView();
        }

    }

    private void setEventHandlers() {

        rgLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbAbkat.isChecked()) {
                    session.createHllUserSession(false);
                    setupMenu();
                } else {
                    session.createHllUserSession(true);
                    setupMenu();
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MenuListPojo element = menuList.get(position);
                        if (element.getClassName() != null) {
                            if (session.isHllUser()) {
                                switch (element.getMenuName()) {
                                    case "ELearning":
                                        startActivity(new Intent(context, ELearning.class));
                                        break;
                                    case "Fingerprint Upload":
                                        startActivity(new Intent(context, UploadFingerPrint_Activity.class));
                                        break;
                                    case "User Attendance":
                                        startActivity(new Intent(context, AvailabilityNew_Activity.class));
                                        break;

                                    case "Team-Camp Mapping":
                                        startActivity(new Intent(context, TeamCampMappingActivity.class));
                                        break;

                                    case "D2D Availability Screening":
                                        startActivity(new Intent(context, D2DAvailabilityActivity.class));
                                        break;

                                    case "Camp Calendar":
                                        startActivity(new Intent(context, CampCalendar_Activity.class));
                                        break;

                                    case "Target Tracking":
                                        startActivity(new Intent(context, SetPostCampTarget_Activity.class));
                                        break;

                                    case "Resource List":
                                        startActivity(new Intent(context, ResourceList_Activity.class));
                                        break;

                                    case "Report Delivery":
                                        startActivity(new Intent(context, ReportDeliveryActivity.class));
                                        break;

//                                    case "Medicine Delivery":
//                                        startActivity(new Intent(context, MedicineDeliveryActivity.class));
//                                        break;


                                    case "Packet Allocation":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Medicine Return":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Payout Details":
                                        startActivity(new Intent(context, PayoutDetails.class));
                                        break;

                                    case "Packet Collection":
                                        startActivity(new Intent(context, PacketCollectionActivity.class));
                                        break;

                                    case "Post Camp":
                                        startActivity(new Intent(context, PostCampCounts_Activity.class));
                                        break;

                                    case "Sample Processing":
                                        startActivity(new Intent(context, SampleProcessingStatus_Activity.class));
                                        break;

                                    case "Expense/Claim":
                                        startActivity(new Intent(context, ExpenseClaimDashboardActivity.class));
                                        break;

                                    case "D2d Teams":
                                        startActivity(new Intent(context, D2dTeamsActivity.class));
                                        break;

                                    case "Acknowledgment":
                                        startActivity(new Intent(context, CampSelectForAcknowledgement_Activity.class));
                                        break;

                                    case "D2d Team":
                                        startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class));
                                        break;

                                    case "Accept Samples":
                                        startActivity(new Intent(context, AcceptSamplesInLabActivity.class));
                                        break;

                                    case "Packet Receive":
                                        startActivity(new Intent(context, PacketReceiveActivity.class));
                                        break;

                                    case "Medicine Delivery":
                                        startActivity(new Intent(context, MedicineDeliveryMenuNewActivity.class));
                                        break;
                                   case "Team Photos":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Pickup Medicine Packet":
                                        startActivity(new Intent(context, AcceptMedicinePacketActivity.class));
                                        break;

                                    case "Device & Resource Mapping":
                                        startActivity(new Intent(context, CampCreationActivityV4.class));
                                        break;

                                    case "Centrifuge Confirmation":
                                        startActivity(new Intent(context, CentriFugeConfirmationActivity.class));
                                        break;

                                    case "Courier Activity":
                                        startActivity(new Intent(context, Courier_Activity.class));
                                        break;

                                    case "CSC Camp Request":
                                        startActivity(new Intent(context, CampRequestActivity.class));
                                        break;


                                    case "Call To Doctor":
                                        startActivity(new Intent(context, CallToDoctorRequestActivity.class));
                                        break;

                                    case "Availability Dashboard":
                                        startActivity(new Intent(context, DashboardAvailabilityReportDistrict_Activty.class));
                                        break;

                                    case "Heath Screening Staus":
                                        startActivity(new Intent(context, CampDetails_Activity.class));
                                        break;

                                    case "Camp Creation":
                                        startActivity(new Intent(context, CreateCamp_Activity_v3.class));
                                        break;

                                    case "Update Process Lab":
                                        startActivity(new Intent(context, UpdateProcessLabActivity.class));
                                        break;

                                    case "Post Camp Readiness":
                                        startActivity(new Intent(context, DashboardInvoice_Activity.class));
                                        break;

                                    case "Check Beneficiary Details":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Appointment Confirmed List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Common Beneficiary List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Payment & Invoice":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Calling List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;


                                    case "Patient Registration":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Dashboard Patient app":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;


                                    case "D2d Physical Examination Details":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Appointments & Sample Collection Of CT":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Camp Readiness Form":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "CT Assignment":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

//                                    case "Packet Allocation":
//                                        startActivity(new Intent(context, element.getClassName()));
//                                        break;
                                    case "Rescreening Dashboard":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Pick-Up Medicine Packet":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "D2D Camp Activity":
                                        constantData = ConstantData.getInstance();
                                        constantData.setSetSitetypeid(String.valueOf(position + 1));
                                        constantData.setSetSitetypeName(element.getMenuName());
                                        constantData.setClassName(element.getClassName());
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    default:
                                        constantData = ConstantData.getInstance();
                                        constantData.setSetSitetypeid(String.valueOf(position + 1));
                                        constantData.setSetSitetypeName(element.getMenuName());
                                        constantData.setClassName(element.getClassName());
                                        startActivity(new Intent(context, D2DSelectCampActivity.class));
                                        break;
                                }
                            }

//                            else if (element.getClassName().equals(ResourceList_Activity.class)) {
//                                Utilities.showToastMessage("Coming Soon...", context, false);
//
//                            }
                            else {
                                constantData = ConstantData.getInstance();
                                constantData.setSetSitetypeid(String.valueOf(position + 1));
                                constantData.setSetSitetypeName(element.getMenuName());
                                startActivity(new Intent(context, element.getClassName()));
                            }
                        } else {
                            Utilities.showToastMessage("Coming Soon...", context, false);
                        }
                    }
                })
        );


        ll_camp_conducted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampsConducted_Activity.class));
            }
        });

        ll_bene_screened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ll_critical_patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CriticalReferredPatientList_Activity.class)
                        .putExtra("DrillType", "3"));

            }
        });

//        ll_referred_patients.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(context, CriticalReferredPatientList_Activity.class)
//                        .putExtra("DrillType", "2"));
//
//            }
//        });

        ll_todays_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(context, TodaysPatientCountDistWise_Activity.class));

//                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//                DateFormat format2 = new SimpleDateFormat("dd");
//                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();
//                int totalRegWorker = 0;
//
//                try {
//                    String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
//
//                    for (int i = 0; i < filteredCampList.size(); i++) {
//
//                        if (filteredCampList.get(i).getCampDate().equals(todaysDate)) {
//                            filteredCampList.add(campList.get(i));
//                            totalRegWorker = totalRegWorker + Integer.parseInt(filteredCampList.get(i).getREGISTERWORKERS());
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                startActivity(new Intent(context, TodaysCampList_Activity.class)
                        .putExtra("TYPE", "1")
                        .putExtra("date", mDay + "")
//                        .putExtra("campList", filteredCampList)
                        .putExtra("selectedMonth", mMonth + 1)
                        .putExtra("selectedYear", mYear)
                        .putExtra("selectedDist", "0")
                        .putExtra("campType", 0));

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ll_counts.getVisibility() == View.VISIBLE) {
                    getCountsFromServer();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (llcount_todays_patient.getVisibility() == View.VISIBLE || llcount_conducted_camp.getVisibility() == View.VISIBLE
                        || todays_treatment_count_LL.getVisibility() == View.VISIBLE || conducted_Tratment_count_LL.getVisibility() == View.VISIBLE) {

                    getCampDetailsTodaysPatientCount();
                    getCampDetailsConductedCampCount();
                    getSuperAdminaData();
                    getSuperadminTodaysCamp();

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });


    }

    private void setMenuRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new MenuListAdapter(menuList));
    }

//    private class GetHomePageCountDetails extends AsyncTask<String, Void, String> {
//
//        private ProgressDialog pd;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            pd = new ProgressDialog(context);
////            pd.setMessage("Please wait . . . ");
////            pd.setCancelable(false);
////            pd.show();
//            progressBar.setVisibility(View.VISIBLE);
//            ll_counts.setVisibility(View.GONE);
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("DrillType", "0"));
//            res = WebServiceCall.APICall(ApplicationConstants.GetHomePageCountDetailsByFinancialYear, ApplicationConstants.webservice, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

    /// /            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    MainScreenCountsModel pojoDetails = new Gson().fromJson(result, MainScreenCountsModel.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (type.equalsIgnoreCase("success")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//                        List<MainScreenCountsModel.OutputBean> screeningTestList = pojoDetails.getOutput();
//                        if (screeningTestList.size() > 0) {
//                            tv_camp_conducted.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getConductedCamps()));
//                            tv_bene_screened.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getFacilitatedWorkers()));
//                            tv_critical_patients.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getCritical()));
//                            tv_referred_patients.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getRefeeredPatient()));
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                    campType = "1";
//                    new GetHomePageCountDetailsForOS().execute(campType);
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//
//            campType = "1";
//            new GetHomePageCountDetailsForOS().execute(campType);
//        }
//    }

    private class GetHomePageCountDetailsForOS extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampType", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetHomePageCountDetailsForOSByFinancialYear, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    MainScreenCountsModel pojoDetails = new Gson().fromJson(result, MainScreenCountsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    if (type.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");
                        List<MainScreenCountsModel.OutputBean> screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            if (campType.equalsIgnoreCase("1")) {
//                                int regularConductedCamp = 0;
//                                int regularBeneScreened = 0;
//                                int regularTodaysPatient = 0;
//                                int regularReferredPatients = 0;
//                                for (MainScreenCountsModel.OutputBean o : screeningTestList) {
//                                    regularConductedCamp += o.getConductedCamps();
//                                    regularBeneScreened += o.getFacilitatedWorkers();
//                                    regularTodaysPatient += o.getCritical();
//                                    regularReferredPatients += o.getRefeeredPatient();
//                                }
//                                tvRegularConductedCamp.setText("" + new DecimalFormat("##,##,##0").format(regularConductedCamp));
//                                tvRegularBeneScreened.setText("" + new DecimalFormat("##,##,##0").format(regularBeneScreened));
//                                tvRegularTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(regularTodaysPatient));
//                                tvRegularReferredPatients.setText("" + new DecimalFormat("##,##,##0").format(regularReferredPatients));
                                tvRegularConductedCamp.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getConductedCamps()));
                                tvRegularBeneScreened.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getFacilitatedWorkers()));
                                tvRegularTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getCritical()));
                                tvRegularReferredPatients.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getRefeeredPatient()));

                            }


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }

            campType = "3";
            new GetHomePageCountDetailsForOSD2D().execute(campType);

        }
    }

    private class GetHomePageCountDetailsForOSD2D extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampType", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetHomePageCountDetailsForOSByFinancialYear, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    MainScreenCountsModel pojoDetails = new Gson().fromJson(result, MainScreenCountsModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    if (type.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");
                        List<MainScreenCountsModel.OutputBean> screeningTestList = pojoDetails.getOutput();
                        if (screeningTestList.size() > 0) {
                            if (campType.equalsIgnoreCase("3")) {
//                                int regularConductedCamp = 0;
//                                int regularBeneScreened = 0;
//                                int regularTodaysPatient = 0;
//                                int regularReferredPatients = 0;
//                                for (MainScreenCountsModel.OutputBean o : screeningTestList) {
//                                    regularConductedCamp += o.getConductedCamps();
//                                    regularBeneScreened += o.getFacilitatedWorkers();
//                                    regularTodaysPatient += o.getCritical();
//                                    regularReferredPatients += o.getRefeeredPatient();
//                                }
//                                tvD2dConductedCamp.setText("" + new DecimalFormat("##,##,##0").format(regularConductedCamp));
//                                tvD2dBeneScreened.setText("" + new DecimalFormat("##,##,##0").format(regularBeneScreened));
//                                tvD2dTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(regularTodaysPatient));
//                                tvD2dReferredPatients.setText("" + new DecimalFormat("##,##,##0").format(regularReferredPatients));

                                tvD2dConductedCamp.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getConductedCamps()));
                                tvD2dBeneScreened.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getFacilitatedWorkers()));
                                tvD2dTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getCritical()));
                                tvD2dReferredPatients.setText("" + new DecimalFormat("##,##,##0").format(screeningTestList.get(0).getRefeeredPatient()));

                            }


                            tv_camp_conducted.setText("" + new DecimalFormat("##,##,##0").format(Long.valueOf(tvRegularConductedCamp.getText().toString().replaceAll(",", "")) + Long.valueOf(tvD2dConductedCamp.getText().toString().replaceAll(",", ""))));


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }


        }
    }

    private class LogoutUser extends AsyncTask<String, Void, String> {

//        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Userid", params[0]));
            param.add(new ParamsPojo("MobVersion", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetUserForceLogout, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String status = "", message = "";
            Log.d(TAG, "GetUserForceLogout: " + result);
            try {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        message = jsonObject.getString("message");
                        JSONArray jsonArray = jsonObject.getJSONArray("output");
                        JSONObject j = jsonArray.getJSONObject(0);
                        int isLogOutFlag = j.getInt("IsLogOutFlag");
                        if (isLogOutFlag == 1) {
                            new UpdateLogoutUser().execute(userId, BuildConfig.VERSION_NAME, "0");
                        }
                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }


        }
    }

    private class UpdateLogoutUser extends AsyncTask<String, Void, String> {

//        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Userid", params[0]));
            param.add(new ParamsPojo("MobVersion", params[1]));
            param.add(new ParamsPojo("IsLogOutFlag", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.UpdateLogoutUser, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            Log.d(TAG, "UpdateLogoutUser: " + result);

            String status = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        message = jsonObject.getString("message");
                        session.logoutUser();
                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }


        }
    }

    private class GetCampDetailsTodayCount extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("FromDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
//            param.add(new ParamsPojo("ToDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequest, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    totalTodaysCamp = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        totalTodaysCamp = pojoDetails.getOutput();
                        groupCampDetails(campList);

                    }
//                    JSONObject jsonObject = new JSONObject(result);
//                    type = jsonObject.getString("status");
//                    message = jsonObject.getString("message");
//                    if (type.equalsIgnoreCase("success")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//                        JSONObject jsonObj = jsonArray.getJSONObject(0);
//                        tv_todays_patient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("REGISTERWORKERS"))));
//                        campType = "1";
//                        new GetCampDetailsTodayCountForOS().execute(campType);
//                    }

                    campType = "1";
                    tvRegularTodaysPatient.setText("");
                    new GetCampDetailsTodayCountForOS().execute(campType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class GetCampDetailsTodayCountForOS extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));
            param.add(new ParamsPojo("CampType", params[0]));
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("FromDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
//            param.add(new ParamsPojo("ToDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        groupCampDetailsForOS(campList);


                    }

//                    if (campType.equalsIgnoreCase("1")) {
//                        campType = "3";
//                        tvD2dTodaysPatient.setText("");
//                        new GetCampDetailsTodayCountForOS().execute(campType);
//                    }
//                    JSONObject jsonObject = new JSONObject(result);
//                    type = jsonObject.getString("status");
//                    message = jsonObject.getString("message");
//                    if (type.equalsIgnoreCase("success")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//                        JSONObject jsonObj = jsonArray.getJSONObject(0);
////                        tv_todays_patient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("TODAFACILITEDWORKER"))));
//                        if (campType.equalsIgnoreCase("1")) {
//                            tvRegularTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("REGISTERWORKERS"))));
//                            campType = "3";
//                            new GetCampDetailsTodayCountForOS().execute(campType);
//                        } else if (campType.equalsIgnoreCase("3")) {
//
//                            tvD2dTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("REGISTERWORKERS"))));
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//            if (campType.equalsIgnoreCase("1")) {
            campType = "3";
            new GetCampDetailsTodayCountForOSD2D().execute(campType);
//            }

        }


    }

    private class GetCampDetailsTodayCountForOSD2D extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));
            param.add(new ParamsPojo("CampType", params[0]));
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("FromDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
//            param.add(new ParamsPojo("ToDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            progressBar.setVisibility(View.GONE);

            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        groupCampDetailsForOSD2D(campList);


                    }

//                    if (campType.equalsIgnoreCase("1")) {
//                        campType = "3";
//                        tvD2dTodaysPatient.setText("");
//                        new GetCampDetailsTodayCountForOS().execute(campType);
//                    }
//                    JSONObject jsonObject = new JSONObject(result);
//                    type = jsonObject.getString("status");
//                    message = jsonObject.getString("message");
//                    if (type.equalsIgnoreCase("success")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//                        JSONObject jsonObj = jsonArray.getJSONObject(0);
////                        tv_todays_patient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("TODAFACILITEDWORKER"))));
//                        if (campType.equalsIgnoreCase("1")) {
//                            tvRegularTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("REGISTERWORKERS"))));
//                            campType = "3";
//                            new GetCampDetailsTodayCountForOS().execute(campType);
//                        } else if (campType.equalsIgnoreCase("3")) {
//
//                            tvD2dTodaysPatient.setText("" + new DecimalFormat("##,##,##0").format(Integer.parseInt(jsonObj.getString("REGISTERWORKERS"))));
//                        }
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void groupCampDetails(ArrayList<CampCalendarModel.OutputBean> campList) {
        String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0, monthsCount = 0,
                todaysCount = 0, d2dTodaysCount = 0, regularTodaysCount = 0;

        for (int i = 0; i < campList.size(); i++) {

            if (campList.get(i).getCampDate().equals(todaysDate)) {
//                totalTodaysCamp.add(campList.get(i));
                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
                if (campList.get(i).getCampTypeDescription().equalsIgnoreCase("Regular") || campList.get(i).getCampTypeDescription().equalsIgnoreCase("CSC CAMP")) {
                    regularTodaysCount = regularTodaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
                }
                if (campList.get(i).getCampTypeDescription().equalsIgnoreCase("DOOR TO DOOR") || campList.get(i).getCampTypeDescription().equalsIgnoreCase("CSC D2D") || campList.get(i).getCampTypeDescription().equalsIgnoreCase("FLEXI CAMP")) {
                    d2dTodaysCount = d2dTodaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
                }

            }

            tv_todays_patient.setText("" + todaysCount);
            tvD2dTodaysPatient.setText("" + d2dTodaysCount);
            tvRegularTodaysPatient.setText("" + regularTodaysCount);
        }
    }

    private void groupCampDetailsForOS(ArrayList<CampCalendarModel.OutputBean> campList) {
        String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0, monthsCount = 0,
                todaysCount = 0;

        for (int i = 0; i < campList.size(); i++) {

            if (campList.get(i).getCampDate().equals(todaysDate)) {
                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
            }

        }

        if (campType.equalsIgnoreCase("1")) {
            tvRegularTodaysPatient.setText("" + todaysCount);
        } else {
            tvD2dTodaysPatient.setText("" + todaysCount);
        }

        long tvTotal = Long.valueOf(tvRegularTodaysPatient.getText().toString()) + Long.valueOf(tvD2dTodaysPatient.getText().toString());

        tv_todays_patient.setText("" + tvTotal);
    }

    private void groupCampDetailsForOSD2D(ArrayList<CampCalendarModel.OutputBean> campList) {
        String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0, monthsCount = 0,
                todaysCount = 0;

        for (int i = 0; i < campList.size(); i++) {

            if (campList.get(i).getCampDate().equals(todaysDate)) {
                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
            }

        }

        if (campType.equalsIgnoreCase("1")) {
            tvRegularTodaysPatient.setText("" + todaysCount);
        } else {
            tvD2dTodaysPatient.setText("" + todaysCount);
            ll_counts.setVisibility(View.GONE);

        }

        long tvTotal = Long.valueOf(tvRegularTodaysPatient.getText().toString()) + Long.valueOf(tvD2dTodaysPatient.getText().toString());

        tv_todays_patient.setText("" + tvTotal);
    }


    void insertLogout() {
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.insertLogout(mobileNo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    try {
                        pd.dismiss();
                        String result = response.body().string();
                        Log.i("TAG", "onPostExecute: " + result);

                        if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("Success")) {
//
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.icon_success);
                                builder.setTitle("Success");
                                builder.setCancelable(false);
                                builder.setMessage("User logout successfully");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        session.logoutUser();

                                    }
                                });
                                builder.show();


                            } else {
                                Utilities.showAlertDialog(context, status, message, false);
                            }
                        } else
                            Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Utilities.showAlertDialog(context, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(context, "Failure", e.getMessage(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();

                Utilities.showAlertDialog(context, "Failure", t.getMessage(), false);

            }
        });
    }


    public class InsertOTPForUpdatePassword extends AsyncTask<String, Void, String> {
        //ProgressDialog pd;

        int type;

        public InsertOTPForUpdatePassword(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            param.add(new ParamsPojo("Mobileno", params[0]));
            Random r = new Random(System.currentTimeMillis());
            otpnumber = String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));
            param.add(new ParamsPojo("OTP", otpnumber));


            res = WebServiceCall.APICall(ApplicationConstants.InsertChangePasswordRequest, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {
//                        ArrayList<ResourcesListModel.OutputBean> resourceList = resourcesListModel.getOutput();
//                        if (resourceList.size() > 0) {

                        Utilities.showToastMessage("OTP sent successfully on" + " " + mobileNo, context, true);

                        if (type == 1) {

                            verificationRemark("1", otpnumber);

                        } else if (type == 3) {
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class VerifyOtp extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        // String otp;

        public VerifyOtp(AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            //  this.otp = otp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("Pwd", params[1]));
            param.add(new ParamsPojo("MobNo", params[2]));
            param.add(new ParamsPojo("Otp", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.UpdateUserPassword, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            pd.dismiss();
            String status = "", message = "";
            try {
                if (!result.equals("")) {

//                    ResourcesListModel resourcesListModel = new Gson().fromJson(result, ResourcesListModel.class);
//                    type = resourcesListModel.getStatus();
//                    message = resourcesListModel.getMessage();
                    JSONObject jsonObject = new JSONObject(result);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")) {


                        Utilities.showAlertDialog(context, "Success", message, false, "ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        session.logoutUser();

                                        alertDialog.dismiss();

                                    }
                                });


//                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView logout = findViewById(R.id.img_logout);
//        ImageView drawer = findViewById(R.id.img_drawer);
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);


        toolbar.setNavigationIcon(R.drawable.drawer_icon);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + ":" + versionName);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to logout?");
                builder.setTitle("Alert");
                builder.setIcon(R.drawable.icon_alertred);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


//                        mobileNo = "8668258532";
//                        mobileNo = "9764568835";


                        if (BuildConfig.isBeta) {
                            session.logoutUser();
                        } else {
                            insertLogout();
//                            session.logoutUser();
                        }

//                        insertLogout();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertD = builder.create();
                alertD.show();
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                } else {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }

                toggle = new ActionBarDrawerToggle(SiteSurvey_Menu_Activity.this, drawerLayout, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
            }


        });


//        drawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
////                    drawerLayout.openDrawer(GravityCompat.START);
////                } else {
////                    drawerLayout.closeDrawer(GravityCompat.START);
////                }
//
//                toggle = new ActionBarDrawerToggle(SiteSurvey_Menu_Activity.this, drawerLayout, toolbar,
//                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//                drawerLayout.addDrawerListener(toggle);
//                toggle.syncState();
//            }
//        });

    }

    private void getAutoLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(SiteSurvey_Menu_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<AutoLogoutResponse> call = apiService.getAutoLogout(tokenIDString);

        call.enqueue(new Callback<AutoLogoutResponse>() {
            @Override
            public void onResponse(Call<AutoLogoutResponse> call, Response<AutoLogoutResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    AutoLogoutResponse result = response.body();

                    if (result.getStatus().equalsIgnoreCase("Success")) {
                        AutoLogoutResponse.Output output = result.getOutput().get(0); // Assuming only one item
                        String deviceStatus = output.getDeviceStatus();
                        boolean isActive = output.isIsActive();

//                        Utilities.showAlertDialog(
//                                SiteSurvey_Menu_Activity.this,
//                                "Success",
//                                "Device Status: " + deviceStatus + "\nActive: " + isActive,
//                                true
//                        );
                        if (deviceStatus.equalsIgnoreCase("InActive")) {
                            session.logoutUser();
                        }

                    } else {
                        Utilities.showAlertDialog(
                                SiteSurvey_Menu_Activity.this,
                                "Fail",
                                result.getMessage(),
                                false
                        );
                    }
                } else {
                    Utilities.showToastMessage("Unexpected response from server.", SiteSurvey_Menu_Activity.this, false);
                }
            }

            @Override
            public void onFailure(Call<AutoLogoutResponse> call, Throwable t) {
                progressDialog.dismiss();
                Utilities.showToastMessage("Something went wrong", SiteSurvey_Menu_Activity.this, false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to exit ?");
        builder.setIcon(R.drawable.icon_alertred);
        builder.setTitle("Alert");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertD = builder.create();
        alertD.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
