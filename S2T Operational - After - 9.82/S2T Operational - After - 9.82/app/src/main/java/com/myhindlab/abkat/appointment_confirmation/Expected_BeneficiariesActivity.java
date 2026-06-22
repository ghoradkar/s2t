package com.myhindlab.abkat.appointment_confirmation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.activities.doortodoor.D2DAttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.adapters.doortodoor.CallingNumberAdapter;
import com.myhindlab.abkat.adapters.doortodoor.TeamCallingAdapter;
import com.myhindlab.abkat.appointment_confirmation.adapters.CallListAdaptor;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.CallingApiVirtualNumberModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.DateTypesModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.appointment_confirmation.pojo.VodaphoneCallModel;
import com.myhindlab.abkat.databinding.ActivityExpectedBeneficiariesBinding;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.CallRequest;
import com.myhindlab.abkat.models.doortodoor.CallRequestForVodaphone;
import com.myhindlab.abkat.models.doortodoor.CallRequestNew;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Expected_BeneficiariesActivity extends AppCompatActivity implements CallListAdaptor.Onclicklisner {
    private static final Logger log = LoggerFactory.getLogger(Expected_BeneficiariesActivity.class);
    ActivityExpectedBeneficiariesBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;
    private List<DateTypesModel.Output> DateTypesModel;

    private List<OutputItem> campBeneficiaryList;
    private List<TeamCallingModel.Output> numberList;

    private String apiKeyForMyOperator = "",
            companyID = "",
            public_IVR_ID = "",
            secrateToken = "",
            typeForMyOperator = "";
    private ProgressDialog pd;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private AlertDialog teamDialog;
    private Context mContext;

    String selectedApptDate;

    private String TAG = Expected_BeneficiariesActivity.class.getSimpleName();


    private String call_statusid = "1", fromDateTypeDta = "0", dateTypeId = "0", oganizationId = "0", teamId = "0", apiKeyNew, virtualNumberForVodaphone, apiToken, channelflag, dtmfflag, recordingflag, modifiedOn,
            virtualNumberNew, mobileNumber, EmpCode, referenceId, size, taluka, STATELGDCODE = "2", Is24By7IsAccountCreated, executivemobileNumber, myOperator_UserID,
            DISTLGDCODE = "0", agentId, DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_expected_beneficiaries);
        setUpToolBar();
        binding.rvReportlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvReportlist.setHasFixedSize(true);
        binding.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(Expected_BeneficiariesActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.colour_info_dialoge);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        init();
        getSessionData();
        setDefault();
        eventListener();

    }

    void init() {
        mContext = Expected_BeneficiariesActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        binding.edtCallstatus.setText("Calling Pending");
        call_statusid = String.valueOf(1);
        binding.edtTeamNumber.setText("All");
        teamId = String.valueOf(0);
        // searchView.setQueryHint("Search Beneficiary By Name or Mobile Number");


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

//
//        binding.etDate.setText(Utilities.dfDate6.format(new Date()));
//        selectedApptDate = Utilities.dfDate6.format(new Date());
//


        getReportlist();

//        getVodaphoneAPIDetails();


//        getReportlistNew();


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
                LabCode = json.getString("LabCode");
                agentId = json.getString("AgentID");
                executivemobileNumber = json.getString("BMobile");
                myOperator_UserID = json.getString("MyOperator_UserID");





//                Is24By7IsAccountCreated = json.getString("Is24By7IsAccountCreated");

//                executivemobileNumber = "8668258532";


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefault() {

        new GetOrganizationNew().execute(EmpCode, DESGID);

        new GetUserCreatedBy24By7().execute(EmpCode);

    }

    private void eventListener() {
        binding.edtCallstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getapicall();
                dateTypeId = "0";
                getStatusList();
            }
        });

        binding.edtDateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getapicall();
                getDateTypeList();
            }
        });

        binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (binding.edtCallstatus.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Select Call Status", "Please select call status", false);
//                    return;
//                }

                if (binding.edtDateType.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Alert", "Please select date type", false);
                    return;
                }

                showDatePicker(Integer.parseInt(dateTypeId));

            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<OutputItem> searchPatientList = new ArrayList<>();

                if (campBeneficiaryList != null) {
                    if (campBeneficiaryList.size() > 0) {
                        for (OutputItem pojo : campBeneficiaryList) {
                            String siteDetails = pojo.getBeneficiaryName();
                            String siteDetailsNew = pojo.getMobile();
                            String siteDetailsArea = pojo.getArea();
                            if ((siteDetails != null && siteDetails.toLowerCase().startsWith(query.toLowerCase())
                            ) || siteDetailsNew != null && siteDetailsNew.startsWith(query) || siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(query.toLowerCase())

                            ) {
                                searchPatientList.add(pojo);
                            }
                        }

                        if (searchPatientList.size() == 0) {
                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Alert", "No record found.", false);
                            searchPatientList.addAll(campBeneficiaryList);
                            searchPatientList.clear();
                            CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, searchPatientList, Expected_BeneficiariesActivity.this);
                            binding.rvReportlist.setAdapter(callListAdaptor);

                            //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        } else {
                            CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, searchPatientList, Expected_BeneficiariesActivity.this);
                            binding.rvReportlist.setAdapter(callListAdaptor);
                            //  binding.rvReportlist.setAdapter(new PostCampBeneficiaryAdapter(Expected_BeneficiariesActivity.this, searchPatientList, isAdmin, callType));
                        }
                    }
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                List<OutputItem> searchPatientList = new ArrayList<>();

                if (campBeneficiaryList != null) {
                    if (newText.equals("")) {
                        searchPatientList.addAll(campBeneficiaryList);
                        CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, searchPatientList, Expected_BeneficiariesActivity.this);
                        binding.rvReportlist.setAdapter(callListAdaptor);
//                        binding.rvReportlist.setAdapter(new PostCampBeneficiaryAdapter(Expected_BeneficiariesActivity.this, searchPatientList,false));
                    } else {
                        if (campBeneficiaryList.size() > 0) {
                            for (OutputItem pojo : campBeneficiaryList) {
                                String siteDetails = pojo.getBeneficiaryName();
                                String siteDetailsNew = pojo.getMobile();
                                String siteDetailsArea = pojo.getArea();

                                if (siteDetails != null && siteDetails.toUpperCase().startsWith(newText.toUpperCase())
                                        || siteDetailsNew != null && siteDetailsNew.startsWith(newText) || siteDetailsArea != null && siteDetailsArea.toUpperCase().startsWith(newText.toUpperCase())) {
                                    searchPatientList.add(pojo);
                                }
                            }
                            if (searchPatientList.size() == 0) {
                                searchPatientList.addAll(campBeneficiaryList);
                                CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, searchPatientList, Expected_BeneficiariesActivity.this);
                                binding.rvReportlist.setAdapter(callListAdaptor);
                                //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            } else {
                                CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, searchPatientList, Expected_BeneficiariesActivity.this);
                                binding.rvReportlist.setAdapter(callListAdaptor);
                                // rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            }
                        }
                    }
                }
                return true;
            }
        });


        binding.edtTeamNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(Expected_BeneficiariesActivity.this)) {
                    dateTypeId = "0";

                    new GetTeamId().execute();
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", Expected_BeneficiariesActivity.this, false);
                }
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (fromDateTypeDta.equalsIgnoreCase("0")) {
                getReportlist();

            } else if (fromDateTypeDta.equalsIgnoreCase("1")) {
                getReportlistNew();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expected Beneficiaries");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.inputLayoutCallStatus.setVisibility(binding.inputLayoutCallStatus.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

//                if ( binding.edtCallstatus.getVisibility() == View.VISIBLE) {
//                    binding.edtCallstatus.setVisibility(View.GONE);
//                } else {
//                    binding.edtCallstatus.setVisibility(View.VISIBLE);
//
//                }

                binding.inputLayoutTeamNumber.setVisibility(binding.inputLayoutTeamNumber.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                binding.inputLayoutStatusType.setVisibility(binding.inputLayoutStatusType.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                binding.inputLayoutApptDate.setVisibility(binding.inputLayoutApptDate.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


            }
        });
    }

    private void getReportlist() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ReportlistResponse> call = apiService.REPORTLIST_RESPONSE_CALL(sessionManager.getUserDetailsJson().getEmpCode(), "0", teamId, call_statusid);
        call.enqueue(new Callback<ReportlistResponse>() {
            @Override
            public void onResponse(Call<ReportlistResponse> call, Response<ReportlistResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        fromDateTypeDta = "0";

                        campBeneficiaryList = response.body().getOutput();
                        List<OutputItem> reportdatalist = response.body().getOutput();
                        CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, reportdatalist, Expected_BeneficiariesActivity.this);
                        binding.rvReportlist.setAdapter(callListAdaptor);
                        // size = String.valueOf(reportdatalist.size());
                        //   binding.tvSerialNo.setText(si);

                    } else {
                        CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, new ArrayList<>(), Expected_BeneficiariesActivity.this);
                        binding.rvReportlist.setAdapter(callListAdaptor);
                        Utilities.showAlertDialog(mContext, "Alert", "Beneficiary data not found", false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportlistResponse> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    private void getReportlistNew() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ReportlistResponse> call = apiService.REPORTLIST_RESPONSE_CALL_NEW(sessionManager.getUserDetailsJson().getEmpCode(), "0", teamId, call_statusid, selectedApptDate, dateTypeId);
        call.enqueue(new Callback<ReportlistResponse>() {
            @Override
            public void onResponse(Call<ReportlistResponse> call, Response<ReportlistResponse> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {

                        fromDateTypeDta = "1";
                        campBeneficiaryList = response.body().getOutput();
                        List<OutputItem> reportdatalist = response.body().getOutput();
                        CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, reportdatalist, Expected_BeneficiariesActivity.this);
                        binding.rvReportlist.setAdapter(callListAdaptor);
                        // size = String.valueOf(reportdatalist.size());
                        //   binding.tvSerialNo.setText(si);

                    } else {
                        CallListAdaptor callListAdaptor = new CallListAdaptor(Expected_BeneficiariesActivity.this, new ArrayList<>(), Expected_BeneficiariesActivity.this);
                        binding.rvReportlist.setAdapter(callListAdaptor);
                        Utilities.showAlertDialog(mContext, "Alert", "Beneficiary data not found", false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportlistResponse> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getStatusList() {

        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.web_forcalllist().create(ApiInterface.class);
        apiInterface.Appointment_List().enqueue(new Callback<AppointmentListModel>() {
            @Override
            public void onResponse(Call<AppointmentListModel> call, Response<AppointmentListModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        districtList_models = response.body().getOutput();
                        List<AppointmentListModel.Output> districtListModels = new ArrayList<>();

                        districtListModels.add(new AppointmentListModel().new Output("All", 0));
                        districtListModels.addAll(response.body().getOutput());

                        if (districtListModels.size() > 0) {

                            for (AppointmentListModel.Output o :
                                    districtListModels) {
//                                if (o.getAssignStatusID() == 1) {


//                                    outputItems.remove(o);
//                                    break;
//                                }
                            }
                            showTrenchListDialog(districtListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<AppointmentListModel> call, Throwable t) {

            }
        });

    }

    private void getStatusListNew() {

        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.web_forcalllist().create(ApiInterface.class);
        apiInterface.Appointment_List().enqueue(new Callback<AppointmentListModel>() {
            @Override
            public void onResponse(Call<AppointmentListModel> call, Response<AppointmentListModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        districtList_models = response.body().getOutput();
                        List<AppointmentListModel.Output> districtListModels = new ArrayList<>();

                        districtListModels.add(new AppointmentListModel().new Output("All", 0));
                        districtListModels.addAll(response.body().getOutput());

                        if (districtListModels.size() > 0) {

                            for (AppointmentListModel.Output o :
                                    districtListModels) {
//                                if (o.getAssignStatusID() == 1) {


//                                    outputItems.remove(o);
//                                    break;
//                                }
                            }
                            showTrenchListDialog(districtListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<AppointmentListModel> call, Throwable t) {

            }
        });
    }

    private void getDateTypeList() {

        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.web_forcalllist().create(ApiInterface.class);
        apiInterface.getDateType_List().enqueue(new Callback<DateTypesModel>() {
            @Override
            public void onResponse(Call<DateTypesModel> call, Response<DateTypesModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<DateTypesModel.Output> outputItems = response.body().getOutput();

                        if (outputItems.size() > 0) {
                            showDateTypeListDialog(outputItems);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DateTypesModel> call, Throwable t) {

            }
        });
    }


    private void getInitiateCall() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.twentyFourBySeven_call().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ResponseBody> call = apiService.TwentyFourBySeven_CALL(apiKeyNew, mobileNumber, virtualNumberNew, "json", agentId, referenceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("statusCode");
                        String msg = jsonObject.getString("statusMessage");
                        String data = jsonObject.getString("data");

                        if (msg.equalsIgnoreCase("success")) {
            
                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", data, true);


                        } else {

                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Fail", data, false, "Try Again!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    getInitiateCall();

                                }
                            });

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void getInitiateCallNew() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.twentyFourBySeven_callNew().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ResponseBody> call = apiService.TwentyFourBySeven_CALLNew(apiKeyNew, executivemobileNumber, mobileNumber, virtualNumberNew, referenceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("statusCode");
                        String msg = jsonObject.getString("statusMessage");
//                        String data = jsonObject.getString("data");

                        if (status.equalsIgnoreCase("200")) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + virtualNumberNew));
                            startActivity(intent);

                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", msg, true);


                        } else {

                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Fail", msg, false, "Try Again!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    getInitiateCallNew();

                                }
                            });

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }
//    private void getMyOperatorCall() {
//        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        ApiInterface api = ApiClient.getMyOperator().create(ApiInterface.class);
//
//        CallRequest request = new CallRequest(
//                "6824641618cec397",
//                "8fa9864aeb38e1e1e28ef21a291b009e165de1e86203016d26f0bcac076922fa",
//                "1",
//                "682c5bc7097e9831",
//                "+918668258532",
//                "682db4f4af0bb164",
//                referenceId
//        );
//
//        Call<ResponseBody> call = api.makeOBDCall(request);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.isSuccessful()) {
//                        Log.d("API_SUCCESS", response.body().string());
//                    } else {
//                        Log.e("API_ERROR", response.errorBody().string());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("API_FAILURE", t.getMessage(), t);
//            }
//        });
//    }

//
//    private void getMyOperatorCall() {
//        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        ApiInterface api = ApiClient.getMyOperator().create(ApiInterface.class);
//
//        CallRequest request = new CallRequest(
//                companyID,
//                secrateToken,
//                typeForMyOperator,
//                myOperator_UserID,
//                "+91" + mobileNumber,
//                public_IVR_ID,
//                referenceId
//        );
//
//        Call<ResponseBody> call = api.makeOBDCall(apiKeyForMyOperator, request);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String responseString = response.body().string();
//                        JSONObject jsonObject = new JSONObject(responseString);
//
//                        String status = jsonObject.optString("status");
//                        String message = jsonObject.optString("details");
//
//                        if (status.equalsIgnoreCase("success")) {
////                            Toast.makeText(Expected_BeneficiariesActivity.this, "Success: " + message, Toast.LENGTH_SHORT).show();
//
//                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", message, true);
//
//                        } else {
////                            Toast.makeText(Expected_BeneficiariesActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
//                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Failed", message, true);
//
//                        }
//
//                        Log.d("API_SUCCESS", responseString);
//                    } else {
//                        String errorString = response.errorBody().string();
//                        JSONObject errorJson = new JSONObject(errorString);
//                        String errorMessage = errorJson.optString("details", "Unknown error");
//                        Toast.makeText(Expected_BeneficiariesActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
//                        Log.e("API_ERROR", errorString);
//                    }
//                } catch (Exception e) {
//                    progressDialog.dismiss();
//                    e.printStackTrace();
//                    Toast.makeText(Expected_BeneficiariesActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(Expected_BeneficiariesActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_FAILURE", t.getMessage(), t);
//            }
//        });
//    }



    private void getMyOperatorCall() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface api = ApiClient.getMyOperator().create(ApiInterface.class);

        CallRequestNew request = new CallRequestNew(
                companyID,
                secrateToken,
                typeForMyOperator,
                "+91"+executivemobileNumber,
                "+91" + mobileNumber,
                public_IVR_ID,
                referenceId,
                0,
                "",
                "",
                "",
                false
        );

        Call<ResponseBody> call = api.makeOBDCallNew(apiKeyForMyOperator, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("details");


                        if (status.equalsIgnoreCase("success")) {
//                            Toast.makeText(Expected_BeneficiariesActivity.this, "Success: " + message, Toast.LENGTH_SHORT).show();

                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", "Request accepted successfully", true);

                        } else {
//                            Toast.makeText(Expected_BeneficiariesActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Failed", message, true);

                        }
                        Log.d("API_SUCCESS", responseString);
                    } else {
                        String errorString = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorString);
                        String errorMessage = errorJson.optString("details", "Unknown error");
                        Toast.makeText(Expected_BeneficiariesActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", errorString);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(Expected_BeneficiariesActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Expected_BeneficiariesActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }

    private void getVodaphoneCall() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface api = ApiClient.getVodaphoneOperator().create(ApiInterface.class);

        CallRequestForVodaphone request = new CallRequestForVodaphone(
                virtualNumberForVodaphone,
                executivemobileNumber,
                mobileNumber,
                referenceId,
                channelflag,
                dtmfflag,
                recordingflag
        );

        String token = apiToken;
        String bearerToken = "Bearer " + token;

        Call<ResponseBody> call = api.vodaphoneCall(bearerToken, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        int status = jsonObject.optInt("status");
                        JSONObject messageObj = jsonObject.optJSONObject("message");

                        if (messageObj != null) {
                            String responseMsg = messageObj.optString("Response");
                            int callId = messageObj.optInt("callid");
                            int reqId = messageObj.optInt("ReqId");

                            if ("success".equalsIgnoreCase(responseMsg)) {
//                                String msg = "Call ID: " + callId + "\nReq ID: " + reqId;
                                Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", "Please wait for call", true);
                            } else {
                                Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Failed", responseMsg, true);
                            }
                        }

                        Log.d("API_SUCCESS", responseString);
                    } else {
                        String errorString = response.errorBody() != null ? response.errorBody().string() : "";
                        JSONObject errorJson = !errorString.isEmpty() ? new JSONObject(errorString) : new JSONObject();
                        String errorMessage = errorJson.optString("details", "Unknown error");

                        Toast.makeText(Expected_BeneficiariesActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", errorString);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(Expected_BeneficiariesActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Expected_BeneficiariesActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }


//    private void getapicall() {
//        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
//        Call<AppointmentListModel> call = apiService.Appointment_List();
//        call.enqueue(new Callback<AppointmentListModel>() {
//            @Override
//            public void onResponse(Call<AppointmentListModel> call, Response<AppointmentListModel> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    String status = response.body().getStatus();
//                    String message = response.body().getMessage();
//                    if (status.equalsIgnoreCase("Success")) {
//                        List<AppointmentListModel.Output> Output = new ArrayList<>();
//
//                        Output.add(0,"All");
//                        Output.addAll( response.body().getOutput());
//                        if (Output.size() > 0) {
//
//
//                            for (AppointmentListModel.Output o :
//                                    Output) {
//
//
////                                if (o.getAssignStatusID() == 1) {
////                                    outputItems.remove(o);
////                                    break;
////                                }
//
//
//                            }
//                            showTrenchListDialog(outputItems);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AppointmentListModel> call, Throwable t) {
//                progressDialog.dismiss();
//                t.getLocalizedMessage();
//
//            }
//        });
//    }


    private void showTrenchListDialog(final List<AppointmentListModel.Output> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(Expected_BeneficiariesActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Expected_BeneficiariesActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getAppointmentStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtCallstatus.setText(trenchList.get(which).getAppointmentStatus());
                call_statusid = String.valueOf(trenchList.get(which).getGroupID());
                binding.inputLayoutCallStatus.setErrorEnabled(false);


                binding.edtDateType.setText("");
                binding.etDate.setText("");

                getReportlist();
//                getReportlistNew();

            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showDateTypeListDialog(final List<DateTypesModel.Output> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(Expected_BeneficiariesActivity.this);
        builderSingle.setTitle("Select Date Type");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Expected_BeneficiariesActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getCallingDateType());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.edtDateType.setText(trenchList.get(which).getCallingDateType());
                dateTypeId = String.valueOf(trenchList.get(which).getCallingDateID());
                binding.inputLayoutCallStatus.setErrorEnabled(false);


                binding.inputLayoutApptDate.setVisibility(View.VISIBLE);
                binding.etDate.setText("");


//                getReportlistNew();

            }
        });

        AlertDialog alertDialog = builderSingle.create();
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }


    private class GetTeamId extends AsyncTask<String, Void, String> {

        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
            progressDialog.setMessage("Please wait . . . ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("UserID", EmpCode));
            //  res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamDataByUserId, ApplicationConstants.webservice_forcallist, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //   final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
            progressDialog.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // teamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

//                       assignteamlist.add( new TeamsDetailsModel.OutputBean("0","0","All"));
//
                        assignteamlist.addAll(teamsDetailsModel.getOutput());

                        if (assignteamlist.size() > 0) {

                            showAssignedTeamListDialog(assignteamlist);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, Expected_BeneficiariesActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();


                Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(Expected_BeneficiariesActivity.this);
            builderSingle.setTitle("Select Team");
            builderSingle.setCancelable(false);
            View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
            builderSingle.setView(dialogueView);

            RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
            EditText edt_search = dialogueView.findViewById(R.id.edt_search);
            rvList.setHasFixedSize(true);
            rvList.setLayoutManager(new LinearLayoutManager(Expected_BeneficiariesActivity.this));

            rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

            rvList.addOnItemTouchListener(
                    new RecyclerItemClickListener(Expected_BeneficiariesActivity.this,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, final int position) {
                                    TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                    teamId = assignteamlist.get(position).getTeamid();

                                    binding.edtTeamNumber.setText(assignteamlist.get(position).getTeamName());
                                    teamDialog.dismiss();

                                    binding.edtDateType.setText("");
                                    binding.etDate.setText("");

                                    getReportlist();
//                                    getReportlistNew();
                                }
                            }));

            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence query, int start, int before, int count) {

                    if (query.toString().isEmpty()) {
                        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
                        return;
                    }

                    if (assignteamlist.size() == 0) {
                        rvList.setVisibility(View.GONE);
                        return;
                    }

                    if (!query.toString().equals("")) {
                        ArrayList<TeamsDetailsModel.OutputBean> searchedTestList = new ArrayList<>();
                        for (TeamsDetailsModel.OutputBean clientDetails : assignteamlist) {

                            String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                            String member2 = clientDetails.getMember2().toLowerCase();

                            if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                    member2.contains(query.toString().toLowerCase())) {
                                searchedTestList.add(clientDetails);
                            }
                        }
                        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(searchedTestList));
                    } else {
                        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            teamDialog = builderSingle.create();
            teamDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    int numOfTeams = 0;
                    for (TeamsDetailsModel.OutputBean team : assignteamlist
                    ) {
                        if (team.isChecked()) {
                            numOfTeams += 1;
                        }
                    }
                    Log.d("TAG", "onClick: " + numOfTeams);

                }
            });


            teamDialog.show();

        }


//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            try {
//                if (!result.equals("")) {
//
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                        teamId = jsonObject1.getString("TeamNumber");
//                     //   new CallToDoctorRequestActivity.GetDoctorList().execute();
//                    } else {
//                        Utilities.showAlertDialog(context, "Error", "Unable to get team id", false);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
    }


    public class GetCallingStatus extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("AssignCallID", params[0]));
            param.add(new ParamsPojo("CreatedBy", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertBeneficiaryCallingLog, ApplicationConstants.webservice_forcallist, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        pd.dismiss();


                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileNumber));
                        startActivity(intent);


//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        String phoneNumber = b_mobile;
//                        intent.setData(Uri.parse("tel:" + phoneNumber));
//                        startActivity(intent);

                    }


                } else {
                    Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


//    private void showDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//        // Create a calendar for 7 days back
//        Calendar minDateCalendar = Calendar.getInstance();
//        minDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(Expected_BeneficiariesActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        Calendar selectedCalendar = Calendar.getInstance();
//                        selectedCalendar.set(year, month, dayOfMonth);
//                        if (selectedCalendar.before(minDateCalendar)) {
//                            binding.etDate.setError("Please select a date after 7 days ago.");
//                        } else {
//                            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
//                            String dt = "";
//                            try {
//                                dt = Utilities.dfDate6.format(Utilities.dfDate2.parse(selectedDate));
//                                selectedApptDate = dt;
//
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            binding.etDate.setText(dt);
//
//                            getReportlistNew();
//
//                        }
//                    }
//                }, currentYear, currentMonth, currentDay);
//
//        // Set the minimum date to 7 days ago
//        datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());
//
//        // Set the maximum date to the current date
//        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//
//        datePickerDialog.show();
//    }


    private void showDatePicker(int dateTypeId) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Expected_BeneficiariesActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        // Apply validation based on dateTypeId
                        if (dateTypeId == 1) {
                            Calendar minDateCalendar = Calendar.getInstance();
                            minDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
                            if (selectedCalendar.before(minDateCalendar)) {
                                binding.etDate.setError("Please select a date after 7 days ago.");
                                return;
                            }
                        }

                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        String dt = "";
                        try {
                            dt = Utilities.dfDate6.format(Utilities.dfDate2.parse(selectedDate));
                            selectedApptDate = dt;

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        binding.etDate.setText(dt);
                        getReportlistNew();

//                        binding.edtCallstatus.setText("");
                    }
                }, currentYear, currentMonth, currentDay);

        if (dateTypeId == 1) {
            // Set minimum date to 7 days ago and max date to today for dateTypeId 1
            Calendar minDateCalendar = Calendar.getInstance();
            minDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
            datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else if (dateTypeId == 2) {
            datePickerDialog.show();

        } else if (dateTypeId == 3) {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        }

        datePickerDialog.show();
    }

    public class GetCallingStatusNew extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("AssignCallID", params[0]));
            param.add(new ParamsPojo("CreatedBy", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertBeneficiaryCallingLog_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    referenceId = teamsDetailsModel.getID();

                    if (type.equalsIgnoreCase("success")) {
                        pd.dismiss();


//                        getVodaphoneCall();


                        if (Is24By7IsAccountCreated.equalsIgnoreCase("1")) {
                            if (agentId.equalsIgnoreCase("0")) {
                                getInitiateCallNew();
                            } else {
                                getInitiateCall();
                            }
                        } else if (Is24By7IsAccountCreated.equalsIgnoreCase("2")) {
                            getMyOperatorCall();

                        } else if (Is24By7IsAccountCreated.equalsIgnoreCase("3")) {
                            getVodaphoneCall();
                        }


//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        intent.setData(Uri.parse("tel:"+ mobileNumber));
//                        startActivity(intent);


//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        String phoneNumber = b_mobile;
//                        intent.setData(Uri.parse("tel:" + phoneNumber));
//                        startActivity(intent);

                    }


                } else {
                    Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


    @Override
    public void onclick(OutputItem item) {
        int assigncall_id = item.getAssignCallID();
        Intent intent = new Intent(Expected_BeneficiariesActivity.this, AppoinmentConfirmationActivity.class);
        intent.putExtra("beneficiary", item);
//        intent.putExtra("b_address", item.getRegAddress());
//        intent.putExtra("b_mobileno", item.getMobile());
//        intent.putExtra("AssignCallID", String.valueOf(assigncall_id));
        startActivity(intent);
    }


//    public class GetApiKey extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("OrgID", params[0]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetT2TCallingAPIDetails, ApplicationConstants.webservice_d2d, param);
//            return res;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    // campList = new ArrayList<>();
//                    // selectedCampList = new ArrayList<>();
//                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();
//
//                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
//                    type = callingApiVirtualNumberModel.getStatus();
//                    message = callingApiVirtualNumberModel.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//
//                        //  campList = HomeLabHublabDetailsModel.getOutput();
//                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
//                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);
//
//
//                            apiKey = output.getAPIKey();
//                            virtualNumber = output.getServieNumber();

//
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(mContext, "Fail", message, false);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(mContext, "Please Try Again", "Data not available", false);
//            }
//        }
//    }

    public class GetUserCreatedBy24By7 extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserID", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetIs24By7IsAccountCreatedFlag_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    // selectedCampList = new ArrayList<>();
                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);


//                            apiKey = output.getAPIKey();
//                            virtualNumber = output.getServieNumber();
                            Is24By7IsAccountCreated = output.getIs24By7IsAccountCreated();




                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Data not available", false);
            }
        }
    }


    public class GetOrganizationNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubOrganizationModel.Output> organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            if (pojoDetails.getOutput().size() > 0) {
                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);

                                oganizationId = String.valueOf(output.getSubOrgId());


//                                new GetApiKey().execute(oganizationId);

                                new GetApiKeyNew().execute(oganizationId, EmpCode);

                                new GetApiKeyForMyOperator().execute(oganizationId, EmpCode);


//                                edt_organization.setText(output.getSubOrgName());


//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new CampCalendar_Activity.GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }


                            }


//                            showOrganizationListDialog(organizationList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }
    }


    public class GetApiKeyNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("OrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetOrganisationWiseAPIKey, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);


                            apiKeyNew = output.getAPIKey();
                            virtualNumberNew = output.getServieNumber();


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", message, false);
            }
        }
    }


    private void getVodaphoneAPIDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<VodaphoneCallModel> call = apiService.getVodaphoneAPIDetails();
        call.enqueue(new Callback<VodaphoneCallModel>() {
            @Override
            public void onResponse(Call<VodaphoneCallModel> call, Response<VodaphoneCallModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<VodaphoneCallModel.Output> apiDetailsList = response.body().getOutput();


                        if (apiDetailsList != null && !apiDetailsList.isEmpty()) {
                            VodaphoneCallModel.Output output = apiDetailsList.get(0);

                            virtualNumberForVodaphone = output.getVirtualNumber();
                            apiToken = output.getToken();
                            channelflag = output.getChannelflag();
                            dtmfflag = output.getDtmfflag();
                            recordingflag = output.getRecordingflag();
                            modifiedOn = output.getModifiedOn();

//                            modifiedOn = null;

                            if (modifiedOn != null && !modifiedOn.isEmpty()) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    Date modifiedDate = sdf.parse(modifiedOn);
                                    Date currentDate = new Date();

                                    long diffInMillis = currentDate.getTime() - modifiedDate.getTime();
                                    long hoursDiff = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                                    if (hoursDiff >= 24) {
                                        // Generate new token and save
                                        getAPIToken(); // Call your token generation function
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // If modifiedOn is null, also generate new token
                                getAPIToken();
                            }
                        }


                    } else {


                    }
                } else {


                    Utilities.showAlertDialog(mContext,"Alert",response.body().getMessage(),false);

                }
            }

            @Override
            public void onFailure(Call<VodaphoneCallModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });
    }

    public class GetApiKeyForMyOperator extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("OrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetOrganisationWiseAPIKey_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    // campList = new ArrayList<>();
                    // selectedCampList = new ArrayList<>();
                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);


                            apiKeyForMyOperator = output.getAPIKey();
                            companyID = output.getCompanyID();
                            public_IVR_ID = output.getPublicId();
                            secrateToken = output.getSecrateToken();
                            typeForMyOperator = output.getType();


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please Try Again", message, false);
            }
        }
    }


    private void getAPIToken() {
        final ProgressDialog progressDialog = new ProgressDialog(Expected_BeneficiariesActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface api = ApiClient.getVodaphoneOperator().create(ApiInterface.class);

        // Create JSON body
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("username", "c2c_tn");
        jsonBody.addProperty("password", "c2c_tn");

        // Call API
        Call<ResponseBody> call = api.getToken(jsonBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        String idToken = jsonObject.optString("idToken", "");
                        int expiresIn = jsonObject.optInt("expiresIn", 0);

                        if (!idToken.isEmpty()) {
                            Log.d("API_TOKEN", idToken);
                            insertToken(idToken);
//                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Success", "Token received", true);
                        } else {
                            Utilities.showAlertDialog(Expected_BeneficiariesActivity.this, "Failed", "Token not found", true);
                        }

                        Log.d("API_SUCCESS", responseString);
                    } else {
                        String errorString = response.errorBody() != null ? response.errorBody().string() : "";
                        JSONObject errorJson = !errorString.isEmpty() ? new JSONObject(errorString) : new JSONObject();
                        String errorMessage = errorJson.optString("details", "Unknown error");

                        Toast.makeText(Expected_BeneficiariesActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", errorString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Expected_BeneficiariesActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Expected_BeneficiariesActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }


    void insertToken(String idToken) {
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);


        apiInterface.updateToken(idToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
//                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
//                                builder.setIcon(R.drawable.icon_success);
//                                builder.setTitle("Success");
//                                builder.setCancelable(false);
//                                builder.setMessage(message);
//                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        finish();
//                                    }
//                                });
//                                builder.show();

                            } else {
                                Utilities.showAlertDialog(mContext, status, message, false);
                            }
                        } else
                            Utilities.showAlertDialog(mContext, "Please try again", "Server not responding.", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Utilities.showAlertDialog(mContext, "Failure", response.errorBody().string(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Utilities.showAlertDialog(mContext, "Failure", e.getMessage(), false);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Utilities.showAlertDialog(mContext, "Failure", t.getMessage(), false);

            }
        });
    }


    public class GetCallingNumber extends AsyncTask<String, Void, String> {

        private int assignCallID;

        public GetCallingNumber(int assignCallID) {
            this.assignCallID = assignCallID;
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
            param.add(new ParamsPojo("RegdNo", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRegdWiseListOfMobileNosForAppointments, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamCallingModel teamCallingModel = new Gson().fromJson(result, TeamCallingModel.class);
                    type = teamCallingModel.getStatus();
                    message = teamCallingModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        android.app.AlertDialog.Builder builder =
                                new android.app.AlertDialog.Builder(mContext);

                        View v = LayoutInflater.from(mContext)
                                .inflate(R.layout.calling_number, null, false);

                        builder.setView(v);
                        builder.setTitle("Call To Beneficiary");

                        RecyclerView recyclerView = v.findViewById(R.id.rv_d2dTeams);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setHasFixedSize(false);


                        android.app.AlertDialog alertDialog = builder.create();

                        CallingNumberAdapter callingNumberAdapter =
                                new CallingNumberAdapter(mContext, teamCallingModel.getOutput(),
                                        new CallingNumberAdapter.OnCallClickListener() {
                                            @Override
                                            public void onCallClick(TeamCallingModel.Output data) {


                                                alertDialog.dismiss();


                                                String mobileNo = data.getMobileNo();
                                                mobileNumber = mobileNo;

                                                if (mobileNumber.equalsIgnoreCase("NA")) {
                                                    Utilities.showAlertDialog(mContext, "Alert", "Mobile number not available", false);
                                                    return;
                                                }

                                                Log.d(TAG, "Mobile No " + mobileNumber);


                                                if (Is24By7IsAccountCreated.equalsIgnoreCase("0")) {
                                                    new GetCallingStatus()
                                                            .execute(String.valueOf(assignCallID), EmpCode);
                                                } else {
                                                    new GetCallingStatusNew()
                                                            .execute(String.valueOf(assignCallID), EmpCode);
                                                }
                                            }
                                        });

                        recyclerView.setAdapter(callingNumberAdapter);


                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });


                        alertDialog.show();
                    }
                } else {
                    Utilities.showAlertDialog(mContext, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }


    @Override
    public void onImvClick(OutputItem item) {

    new GetCallingNumber(item.getAssignCallID()).execute(item.getBeneficiaryNo());

//        if (Is24By7IsAccountCreated.equalsIgnoreCase("0")) {
//            new GetCallingStatus().execute(String.valueOf(item.getAssignCallID()), EmpCode);
//        } else if (Is24By7IsAccountCreated.equalsIgnoreCase("1")) {
//            new GetCallingStatusNew().execute(String.valueOf(item.getAssignCallID()), EmpCode);
//        } else if (Is24By7IsAccountCreated.equalsIgnoreCase("2")) {
//            new GetCallingStatusNew().execute(String.valueOf(item.getAssignCallID()), EmpCode);
//        } else if (Is24By7IsAccountCreated.equalsIgnoreCase("3")) {
//            new GetCallingStatusNew().execute(String.valueOf(item.getAssignCallID()), EmpCode);
//        }
//
//        mobileNumber = item.getMobile();

    }

}