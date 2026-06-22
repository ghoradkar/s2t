package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.DashboarsPowerBIActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.FibroScanningCountActivity;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.databinding.PatientAppDashboardBinding;
import com.myhindlab.abkat.databinding.PowerBiDashboardBinding;
import com.myhindlab.abkat.models.AndroidIosCountModel;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PowerBIDashboard extends AppCompatActivity {
    PowerBiDashboardBinding binding;

    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<OutputItem> campBeneficiaryList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;
    private Calendar calMonth;

    private WebView WebView;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, otpnumber, CampDateToDate, type, toDate, yearid, monthId;

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
    private String call_statusid = "1", teamId = "0", userInvoiceId, isPaymentNotReceived, isPaymentReceived, searchFilterType = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PowerBiDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();

    }


    void init() {
        mContext = PowerBIDashboard.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        calMonth = Calendar.getInstance();

//        WebView webView = findViewById(R.id.webview);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.setWebViewClient(new WebViewClient());
//
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);
//
//        webView.loadUrl("https://app.powerbi.com/view?r=eyJrIjoiMTQ4ODk1MDgtYjBkNC00YTcyLThlMWItOGZhYTQzNmFiZGYzIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9");
//        https://app.powerbi.com/view?r=eyJrIjoiMTQ4ODk1MDgtYjBkNC00YTcyLThlMWItOGZhYTQzNmFiZGYzIiwidCI6IjM5NjNhN2VlLWRhMjYtNDZjNS1iN2Q5LTExMjZjY2M4MTUyYSJ9



//        Calendar cal = Calendar.getInstance();
//        mYear = cal.get(Calendar.YEAR);
//        mMonth = cal.get(Calendar.MONTH);
//        mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//
////        Calendar todayCal = Calendar.getInstance();
////        todayCal.add(Calendar.DAY_OF_MONTH, -22);
////
//        Calendar toCal = Calendar.getInstance();
//        toCal.add(Calendar.DAY_OF_MONTH, -10);
//
//
//        CampDate = Utilities.dfDate4.format(toCal.getTime());
//        toDate = Utilities.dfDate4.format(new Date());

    }


    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {

//        getCount();
//        getAndroidIosCount();

    }

    private void eventListener() {

        binding.cvScreeningMhSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DashboarsPowerBIActivity.class)
                        .putExtra("Type", "1")

                );
            }
        });
        binding.cvScreeningMhTAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DashboarsPowerBIActivity.class)
                        .putExtra("Type", "2")

                );
            }
        });
        binding.cvScreeningMhPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DashboarsPowerBIActivity.class)
                        .putExtra("Type", "3")

                );
            }
        });
        binding.cvScreeningMhDeployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DashboarsPowerBIActivity.class)
                        .putExtra("Type", "4")

                );
            }
        });
        binding.cvScreeningMhRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DashboarsPowerBIActivity.class)
                        .putExtra("Type", "5")

                );
            }
        });



//        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                getCount();
//                getAndroidIosCount();
//
//            }
//        });


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
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);
        getSupportActionBar().setTitle("Analytical Dashboards");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());

//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


    }
//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(v -> finish());


//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog dialog;
//                dialog = new Dialog(DashboardPatientApp.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.invoice_info_dialoge);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });


    private void getCount() {

        final ProgressDialog progressDialog = new ProgressDialog(PowerBIDashboard.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.GetDashboardCountForLiverScanning().create(ApiInterface.class);
        apiInterface.getLiverDashboardCount().enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.i("TAG", "onResponse: " + responseString);


                        JSONObject jsonObject = new JSONObject(responseString);


                        String tillDateScanningCount = jsonObject.optString("TillDateScanningCount", "0");
                        String todayScanningCount = jsonObject.optString("TodayScanningCount", "0");
                        String machineInstalledCount = jsonObject.optString("MachineInstalledCount", "0");

//
//                        binding.tvTillDateTotal.setText(tillDateScanningCount);
//                        binding.tvTodaysScanning.setText(todayScanningCount);
//                        binding.tvTillDate.setText(machineInstalledCount);

//                        binding.swipeRefreshLayout.setRefreshing(false);


                    } else {
                        Log.e("TAG", "Response error: " + response.code() + " - " + response.message());
//                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "API call failed: " + t.getMessage());
            }

        });
    }


    private void getAndroidIosCount() {
        final ProgressDialog progressDialog = new ProgressDialog(PowerBIDashboard.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.super_admin_call().create(ApiInterface.class);
        Call<AndroidIosCountModel> call = apiService.getAndroidIosCount();
        call.enqueue(new Callback<AndroidIosCountModel>() {
            @Override
            public void onResponse(Call<AndroidIosCountModel> call, Response<AndroidIosCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String dateTime = response.body().getDateTime();

                    if (status.equalsIgnoreCase("1")) {
                        AndroidIosCountModel.Details details = response.body().getDetails();

                        if (details != null && details.getCount() != null && !details.getCount().isEmpty()) {
                            AndroidIosCountModel.Count count = details.getCount().get(0);

                            if (count != null) {
                                binding.tvTillDateAndroid.setText(String.valueOf(count.getAndroid()));
                                binding.tvTillDateIOS.setText(String.valueOf(count.getiOS()));
                            }

//                            binding.swipeRefreshLayout.setRefreshing(false);
                        }
                    } else {

                        binding.tvTillDateAndroid.setText(0);
                        binding.tvTillDateIOS.setText(0);

//                        binding.swipeRefreshLayout.setRefreshing(false);


                    }
                } else {

                    binding.tvTillDateAndroid.setText(0);
                    binding.tvTillDateIOS.setText(0);

//                    binding.swipeRefreshLayout.setRefreshing(false);

//
                }
            }

            @Override
            public void onFailure(Call<AndroidIosCountModel> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();
            }
        });
    }


}