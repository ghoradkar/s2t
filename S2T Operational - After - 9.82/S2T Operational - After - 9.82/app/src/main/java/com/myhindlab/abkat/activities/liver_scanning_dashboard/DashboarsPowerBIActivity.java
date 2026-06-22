package com.myhindlab.abkat.activities.liver_scanning_dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter.LiverCountDistrictWiseAdapter;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.databinding.ActivityDashboardPowerBiDataBinding;
import com.myhindlab.abkat.databinding.ActivityFibroScaningDataBinding;
import com.myhindlab.abkat.databinding.PowerBiDashboardBindingImpl;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboarsPowerBIActivity extends AppCompatActivity implements LiverCountDistrictWiseAdapter.onTouchListner {
    ActivityDashboardPowerBiDataBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<FibroscanDistrictWiseCountModel.Output> invoiceDetailsList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, CampDateToDate, type, toDate;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", monthId = "0", isSendForVerification,isInvoiceApproved, remarkId, otpnumber, yearId = "0", campTypeId = "0", teamId = "0", userInvoiceID = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2",
            DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode,Type = "0";
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardPowerBiDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
//        setUpToolbar();
    }


    void init() {
        mContext = DashboarsPowerBIActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);






        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -10);








    }



    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");
//                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
        if (getIntent() != null) {
            Type = getIntent().getStringExtra("Type");







            WebView webView = findViewById(R.id.webview);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient());

            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(true);

            if (Type.equalsIgnoreCase("1")){
                webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiNjc2ZTEwOTAtYjYxYS00NTJlLWE0N2QtNzBhZGM3NTM0Mzg1IiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");

            }else if (Type.equalsIgnoreCase("2")){
                webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiMzllNDFmMGYtNGQyMC00NTEzLTg4MTEtM2ZjYmM0OTA4YzFkIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");

            }else if (Type.equalsIgnoreCase("3")){
                webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiNzUxNzVmNTAtNWQyZi00NzQxLWFkMzYtMGMyNDRiOWJhMDVkIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");

            }else if (Type.equalsIgnoreCase("4")){
                webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiMzM3NzhmMDAtNzAyMy00MDcwLWEyZTMtOTlhY2JjYTE1YTA2IiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");

            }else if (Type.equalsIgnoreCase("5")){
                webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiNjYwMGM3ZjMtNTU1Yi00MTA0LWFlZTMtMTE4ZTNjMWJkODllIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");

            }

        }




    }



    private void eventListener() {







//        binding.btnSendForVerification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                mobileNumber = "8888658717";
//                new GetOtp(3).execute("3", EmpCode, userInvoiceID, EmpCode, mobileNumber);
//
////                verificationRemark("11", otpnumber);
//            }
//        });\




    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolbar() {
//        Toolbar toolbar = binding.toolbar; // Assuming your layout has a Toolbar with id 'toolbar'
//        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);


        // Now set the title
        if (getSupportActionBar() != null) {

            if (Type.equalsIgnoreCase("1")){
                getSupportActionBar().setTitle("Screening MH Summary");

            }else if (Type.equalsIgnoreCase("2")){
                getSupportActionBar().setTitle("Screening MH TAT");

            }else if (Type.equalsIgnoreCase("3")){
                getSupportActionBar().setTitle("Screening MH Pending");

            }else if (Type.equalsIgnoreCase("4")){
                getSupportActionBar().setTitle("Patient App Deployment");

            }else if (Type.equalsIgnoreCase("5")){
                getSupportActionBar().setTitle("Screening MH Rejection");

            }
        }
//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(v -> finish());
//



    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Do nothing — prevents activity recreation and WebView reload
    }

    @Override
    public void onDataClick(FibroscanDistrictWiseCountModel.Output item) {

        startActivity(new Intent(mContext,FibroScanningCountForDistrictActivity.class)
                .putExtra("fromDate",CampDate)
                .putExtra("toDate",CampDateToDate)
                .putExtra("districtCode",String.valueOf(item.getDistlgdcode())));

    }
}