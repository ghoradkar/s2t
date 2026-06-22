package com.myhindlab.abkat.activities.re_registration;

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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.MedicineDeliveryAcknowledgement_Activity;
import com.myhindlab.abkat.activities.confirmatoryTest.AssignTeamConfirmatoryTestActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestAssignTeamActivity;
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryListModel;
import com.myhindlab.abkat.activities.re_registration.model.CountsForDailyWorkDashboardModel;
import com.myhindlab.abkat.activities.re_registration.model.RemarkForReReGistrationModel;
import com.myhindlab.abkat.activities.regularcampcreation.ResourceAllocationFragment;
import com.myhindlab.abkat.activities.regularcampcreation.model.ResourceListForMappingModel;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.TeamsAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedForReRegistrationAdapter;
import com.myhindlab.abkat.adapters.doortodoor.AdminActiveInactiveTeamAdapter;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.appointment_confirmation.adapters.CallListAdaptor;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.databinding.ActivityDailyWorkDashboardBinding;
import com.myhindlab.abkat.databinding.ActivityRejectedListDashboardBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RejectedBeneficiaryListActivity extends AppCompatActivity {
    ActivityRejectedListDashboardBinding binding;
    private UserSessionManager sessionManager;

    private ConstantData constantData;

    private List<AppointmentListModel.Output> districtList_models;
    private List<BeneficiaryListModel.Output> countList;

    private BeneficiaryListAdapter adapter;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String fromDate, CampDate, toDate;
    private ResourceListForMappingModel itemList;

//    private List<Item> itemList;

    private ArrayList<GetTeamsModel.OutputBean> teamList;

    private List<BeneficiaryListModel.Output> adminList;


    private ArrayList<TeamsDetailsModel.OutputBean> assignteamlist;


    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private String call_statusid = "1", teamId = "0", remarkId, relationWithWorker, pincodeArray, searchFilterType = "0", mobileNumber, EmpCode,
            referenceId, size, taluka, STATELGDCODE = "2", areaArray, DISTLGDCODE = "0", oganizationId = "0", divisionId = "0",campType ="0",
            agentId, DESGID, AssignedId = "0", member1, rejCampId, member2, isDataSuccess = "0", district, TALLGDCODE = "0", searchId = "5", landingLabId = "0", statusType = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRejectedListDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        setUpToolbar();
        getSessionData();
        setdefault();
        eventListener();

    }


    void init() {
        mContext = RejectedBeneficiaryListActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


//        RecyclerView recyclerView = findViewById(R.id.rv_beneficiaryDetails);
//
//// Check if data is available
//        if (resourceListModel != null && resourceListModel.getOutput() != null) {
//            // Initialize the adapter
//            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter();
//
//            // Set layout manager and adapter
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(adapter);
//
//            // Notify adapter if the data changes
//            adapter.notifyDataSetChanged();
//        } else {
//            Log.d("RecyclerView", "Data is empty or null");
//        }

        binding.rvBeneficiaryDetails.setLayoutManager(new LinearLayoutManager(this));


        BeneficiaryListAdapter beneficiaryListAdapter = new BeneficiaryListAdapter(mContext, adminList);
        binding.rvBeneficiaryDetails.setAdapter(beneficiaryListAdapter);


//        adapter = new BeneficiaryListAdapter(new ArrayList<>());
//        binding.rvBeneficiaryDetails.setAdapter(adapter);
//


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

//
////        Calendar todayCal = Calendar.getInstance();
////        todayCal.add(Calendar.DAY_OF_MONTH, -22);
////
//        Calendar toCal = Calendar.getInstance();
//        toCal.add(Calendar.DAY_OF_MONTH, -3);
//
//
//        fromDate = Utilities.dfDate4.format(toCal.getTime());
//        CampDateToDate = Utilities.dfDate4.format(new Date());

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
//                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                agentId = json.getString("AgentID");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setdefault() {


        binding.searchView.setIconifiedByDefault(true);
        if (getIntent() != null) {
            fromDate = getIntent().getStringExtra("FromDate");
            toDate = getIntent().getStringExtra("ToDate");
            oganizationId = getIntent().getStringExtra("organizationId");
            divisionId = getIntent().getStringExtra("divisionId");
            DISTLGDCODE = getIntent().getStringExtra("distLgd");
            TALLGDCODE = getIntent().getStringExtra("talukaId");
            landingLabId = getIntent().getStringExtra("labcode");
            areaArray = getIntent().getStringExtra("areaArray");
            pincodeArray = getIntent().getStringExtra("pincodeArray");
            searchFilterType = getIntent().getStringExtra("filterType");


            campType = getIntent().getStringExtra("campType");


            searchFilterType ="0";
//            ConstantData constantData = ConstantData.getInstance();
//            constantData.setCampType(campTypeId);


//            areaArray="[{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":400083,\"Area\":\"Pimpri\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Devale\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Hadsar\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junnar\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junner\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junnr\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Katede\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Padali\"}]";
//
//            pincodeArray = "[{\"SubOrgId\":0,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":\"400083\"},{\"SubOrgId\":0,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":\"410502\"}]";


            if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {
//                new GetCountDataForTeam().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "0", "0", EmpCode, "2");

                statusType = "0";

                if (searchFilterType.equalsIgnoreCase("0")) {
                    getCountForPageloadForTeam(
                            areaArray,
                            pincodeArray
                    );

                } else {

                    getCountForTeam(
                            areaArray,
                            pincodeArray
                    );

                }

                binding.status.setText("All");


            } else {
//                new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "2", "0", EmpCode, "2");


                statusType = "2";


                if (searchFilterType.equalsIgnoreCase("0")) {

                    getCountForPageload(
                            areaArray,
                            pincodeArray
                    );

                } else {
                    getCount(
                            areaArray,
                            pincodeArray
                    );

                }

                binding.status.setText("Assignment Pending");

            }


        }

    }

    private void eventListener() {


//        binding.llMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<BeneficiaryListModel.Output> searchPatientList = new ArrayList<>();

                if (adminList != null) {
                    if (adminList.size() > 0) {
                        for (BeneficiaryListModel.Output pojo : adminList) {


                            String siteDetails = pojo.getBeneficiaryName();
                            String siteDetailsNew = pojo.getPincode();
                            String siteDetailsTaluka = pojo.getTaluka();
                            String siteDetailsArea = pojo.getArea();
                            String siteDetailsBeneficiaryNumber = pojo.getBeneficiaryNumber();

                            if (searchId.equals("1")) {

                                if (siteDetails != null && siteDetails.toLowerCase().startsWith(query.toLowerCase())) {
                                    searchPatientList.add(pojo);

                                }
                            } else if (searchId.equals("2")) {
                                if (siteDetailsNew != null && siteDetailsNew.startsWith(query)) {
                                    searchPatientList.add(pojo);

                                }
                            } else if (searchId.equals("3")) {
                                if (siteDetailsTaluka != null && siteDetailsTaluka.startsWith(query)) {
                                    searchPatientList.add(pojo);
                                }
                            } else if (searchId.equals("4")) {
                                if (siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(query.toLowerCase())) {
                                    searchPatientList.add(pojo);
                                }
                            } else if (searchId.equals("5")) {
                                if (siteDetailsBeneficiaryNumber != null && siteDetailsBeneficiaryNumber.startsWith(query)) {
                                    searchPatientList.add(pojo);
                                }
                            }

//                            if ((siteDetails != null && siteDetails.toLowerCase().startsWith(query.toLowerCase())
//                            ) || siteDetailsNew != null && siteDetailsNew.startsWith(query) || siteDetailsBeneficiaryNumber != null && siteDetailsBeneficiaryNumber.startsWith(query) || siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(query.toLowerCase())
//
//                            ) {
//                                searchPatientList.add(pojo);
//                            }
                        }

                        if (searchPatientList.size() == 0) {
                            Utilities.showAlertDialog(RejectedBeneficiaryListActivity.this, "Alert", "No record found.", false);
                            searchPatientList.addAll(adminList);
                            searchPatientList.clear();
                            BeneficiaryListAdapter callListAdaptor = new BeneficiaryListAdapter(RejectedBeneficiaryListActivity.this, searchPatientList);
                            binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);

                            //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        } else {
                            BeneficiaryListAdapter callListAdaptor = new BeneficiaryListAdapter(RejectedBeneficiaryListActivity.this, searchPatientList);
                            binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
                            //  binding.rvBeneficiaryDetails.setAdapter(new PostCampBeneficiaryAdapter(RejectedBeneficiaryListActivity.this, searchPatientList, isAdmin, callType));
                        }
                    }
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                List<BeneficiaryListModel.Output> searchPatientList = new ArrayList<>();

                if (adminList != null) {
                    if (newText.equals("")) {
                        searchPatientList.addAll(adminList);
                        BeneficiaryListAdapter callListAdaptor = new BeneficiaryListAdapter(RejectedBeneficiaryListActivity.this, searchPatientList);
                        binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
//                        binding.rvBeneficiaryDetails.setAdapter(new PostCampBeneficiaryAdapter(RejectedBeneficiaryListActivity.this, searchPatientList,false));
                    } else {
                        if (adminList.size() > 0) {
                            for (BeneficiaryListModel.Output pojo : adminList) {
                                String siteDetails = pojo.getBeneficiaryName();
                                String siteDetailsNew = pojo.getPincode();
                                String siteDetailsArea = pojo.getArea();
                                String siteDetailsTaluka = pojo.getTaluka();
                                String siteDetailsBeneficiaryNumber = pojo.getBeneficiaryNumber();


                                if (searchId.equals("1")) {

                                    if (siteDetails != null && siteDetails.toLowerCase().startsWith(newText.toLowerCase())) {
                                        searchPatientList.add(pojo);

                                    }
                                } else if (searchId.equals("2")) {
                                    if (siteDetailsNew != null && siteDetailsNew.startsWith(newText)) {
                                        searchPatientList.add(pojo);

                                    }
                                } else if (searchId.equals("3")) {
                                    if (siteDetailsTaluka != null && siteDetailsTaluka.startsWith(newText)) {
                                        searchPatientList.add(pojo);

                                    }
                                } else if (searchId.equals("4")) {

                                    if (siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(newText.toLowerCase())) {
                                        searchPatientList.add(pojo);

                                    }
                                } else if (searchId.equals("5")) {
                                    if (siteDetailsBeneficiaryNumber != null && siteDetailsBeneficiaryNumber.startsWith(newText)) {
                                        searchPatientList.add(pojo);

                                    }
                                }


//                                if (siteDetails != null && siteDetails.toUpperCase().startsWith(newText.toUpperCase())
//                                        || siteDetailsNew != null && siteDetailsNew.startsWith(newText) || siteDetailsBeneficiaryNumber != null && siteDetailsBeneficiaryNumber.startsWith(newText) || siteDetailsArea != null && siteDetailsArea.toUpperCase().startsWith(newText.toUpperCase())) {
//                                    searchPatientList.add(pojo);
//                                }
                            }

                            if (searchPatientList.size() == 0) {
                                searchPatientList.addAll(adminList);
                                BeneficiaryListAdapter callListAdaptor = new BeneficiaryListAdapter(RejectedBeneficiaryListActivity.this, searchPatientList);
                                binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
                                //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            } else {
                                BeneficiaryListAdapter callListAdaptor = new BeneficiaryListAdapter(RejectedBeneficiaryListActivity.this, searchPatientList);
                                binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
                                // rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            }
                        }
                    }
                }
                return true;
            }
        });


        binding.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {
                    new GetStatusList().execute("3");

                } else {
                    new GetStatusList().execute("0");
                }

            }
        });

        binding.tvSearchBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("Beneficiary Name", 1));
                campTypeModelArrayList.add(new CampTypeModel("Pincode", 2));
                campTypeModelArrayList.add(new CampTypeModel("Taluka", 3));
                campTypeModelArrayList.add(new CampTypeModel("Area", 4));
                campTypeModelArrayList.add(new CampTypeModel("Beneficiary Number", 5));
                //                campTypeModelArrayList.add(new CampTypeModel("CSC", 2));

                showSeaschType(campTypeModelArrayList);

            }
        });


//        tv_from_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
//                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
//
//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
//
//                            }
//
//                        }, mYear, mMonth, mDay);
//                Calendar c = Calendar.getInstance();
//                c.set(mYear, mMonth, mDay);
//                //  c.add(Calendar.DAY_OF_MONTH, 15);
//
//
//                try {
////                    dpd1.getDatePicker().setCalendarViewShown(false);
//
////                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
////                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
//
//                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dpd1.show();
//            }
//        });


//        binding.edtTeamNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(DailyWorkDashboardActivity.this)) {
//                    new GetTeamId().execute();
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", DailyWorkDashboardActivity.this, false);
//                }
//            }
//        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getReportlist();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar; // Assuming your layout has a Toolbar with id 'toolbar'
        setSupportActionBar(toolbar);

        // Now set the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rejected Beneficiary List");
        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void getReportlist() {
        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
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
//                    if (status.equalsIgnoreCase("Success")) {
//                        adminList = response.body().getOutput();
//                        List<OutputItem> reportdatalist = response.body().getOutput();
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, reportdatalist, RejectedBeneficiaryListActivity.this);
////                        binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
//
//                    } else {
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, new ArrayList<>(), RejectedBeneficiaryListActivity.this);
////                        binding.rvBeneficiaryDetails.setAdapter(callListAdaptor);
//                    }
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

        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
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


    private void getInitiateCall() {
        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.twentyFourBySeven_call().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<ResponseBody> call = apiService.TwentyFourBySeven_CALL("6cff0d3e-f16a-4c13-9c31-7f338942be7e", "9665253245", "918035038367", "json", agentId, referenceId);
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

                            Utilities.showAlertDialog(RejectedBeneficiaryListActivity.this, "Success", data, true);


                        } else {

                            Utilities.showAlertDialog(RejectedBeneficiaryListActivity.this, "Fail", data, false, "Try Again!", new DialogInterface.OnClickListener() {
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


//    private void getapicall() {
//        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RejectedBeneficiaryListActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RejectedBeneficiaryListActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getAppointmentStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                binding.edtCallstatus.setText(trenchList.get(which).getAppointmentStatus());
                call_statusid = String.valueOf(trenchList.get(which).getGroupID());
//                binding.inputLayoutCallStatus.setErrorEnabled(false);

                getReportlist();

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

        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
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
            //   final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryListActivity.this);
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
                            Utilities.showToastMessage(R.string.msgt_emptylist, RejectedBeneficiaryListActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(RejectedBeneficiaryListActivity.this, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();


                Utilities.showAlertDialog(RejectedBeneficiaryListActivity.this, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(RejectedBeneficiaryListActivity.this);
            builderSingle.setTitle("Select Team");
            builderSingle.setCancelable(false);
            View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
            builderSingle.setView(dialogueView);

            RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
            EditText edt_search = dialogueView.findViewById(R.id.edt_search);
            rvList.setHasFixedSize(true);
            rvList.setLayoutManager(new LinearLayoutManager(RejectedBeneficiaryListActivity.this));

            rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

            rvList.addOnItemTouchListener(
                    new RecyclerItemClickListener(RejectedBeneficiaryListActivity.this,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, final int position) {
                                    TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                    teamId = assignteamlist.get(position).getTeamid();

//                                    binding.edtTeamNumber.setText(assignteamlist.get(position).getTeamName());
                                    teamDialog.dismiss();

                                    getReportlist();
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


    private void showSeaschType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Search By");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
        }

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.tvSearchBy.setText(campTypeModelsList.get(which).getCampTypeName());
                searchId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());


                binding.searchView.setQuery("", false); // Clear existing query
                binding.searchView.setQueryHint("Search");
                binding.searchView.clearFocus(); // Clear focus to show hint

                // Ensure this runs on the UI thread
                binding.searchView.post(() -> {
                    binding.searchView.setQueryHint("Search");
                });

                if (searchId.equalsIgnoreCase("1") || searchId.equalsIgnoreCase("3") || searchId.equalsIgnoreCase("4")) {
                    EditText searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);

                    if (searchEditText != null) {
                        Log.d("SearchView", "Setting filter for letters only");
                        InputFilter filter = new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                for (int i = start; i < end; i++) {
                                    if (!Character.isLetter(source.charAt(i))) {
                                        return "";
                                    }
                                }
                                return null;
                            }
                        };
                        searchEditText.setFilters(new InputFilter[]{filter});
                    } else {
                        Log.e("SearchView", "Search EditText not found");
                    }

                } else if (searchId.equalsIgnoreCase("2") || searchId.equalsIgnoreCase("5")) {
                    EditText searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);

                    if (searchEditText != null) {
                        Log.d("SearchView", "Setting filter for digits only");
                        InputFilter filter = new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                for (int i = start; i < end; i++) {
                                    if (!Character.isDigit(source.charAt(i))) {
                                        return "";
                                    }
                                }
                                return null;
                            }
                        };
                        searchEditText.setFilters(new InputFilter[]{filter});
                    } else {
                        Log.e("SearchView", "Search EditText not found");
                    }
                }

                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }


    public class GetStatusList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Type", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionAssignmentRemarks, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    RemarkForReReGistrationModel remarkForReReGistrationModel = new Gson().fromJson(result, RemarkForReReGistrationModel.class);
                    type = remarkForReReGistrationModel.getStatus();
                    message = remarkForReReGistrationModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<RemarkForReReGistrationModel.Output> statusList = remarkForReReGistrationModel.getOutput();
                        if (statusList.size() > 0) {
                            // camptypelist.add(0, new TalukaModel().new Output("ALL", 0));
                            showStatusDialogue(statusList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showStatusDialogue(final List<RemarkForReReGistrationModel.Output> statusList) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
            builderSingle.setTitle("Select Status");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

            for (int i = 0; i < statusList.size(); i++) {
                arrayAdapter.add(String.valueOf(statusList.get(i).getAssignmentRemarks()));
            }

            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    binding.status.setText(statusList.get(which).getAssignmentRemarks());
                    statusType = String.valueOf(statusList.get(which).getArId());

                    //  refreshCalendar();

                    binding.searchView.setQuery("", false); // Clear existing query
                    binding.searchView.setQueryHint("Search");
                    binding.searchView.clearFocus(); // Clear focus to show hint

                    // Ensure this runs on the UI thread
                    binding.searchView.post(() -> {
                        binding.searchView.setQueryHint("Search");
                    });
                    if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {
//                        new GetCountDataForTeam().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, type, "0", EmpCode, "2");


                        if (searchFilterType.equalsIgnoreCase("0")) {
                            getCountForPageloadForTeam(
                                    areaArray,
                                    pincodeArray
                            );
                        } else {
                            getCountForTeam(
                                    areaArray,
                                    pincodeArray
                            );

                        }


//                        binding.status.setText("All");


                    } else {
//                        new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, type, "0", EmpCode, "2");


                        if (searchFilterType.equalsIgnoreCase("0")) {
                            getCountForPageload(
                                    areaArray,
                                    pincodeArray
                            );
                        } else {
                            getCount(
                                    areaArray,
                                    pincodeArray
                            );

                        }

//                        binding.status.setText("Assignment Pending");

                    }


//                    new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, type, "0", EmpCode, "2");


                }
            });
            builderSingle.show();

        }

    }

    public class GetStatusListForTeam extends AsyncTask<String, Void, String> {

        TextView tv_remark_for_team;
        LinearLayout ll_appt_date_team;
        TextView tv_appt_date;
        Button btn_reassign;

        public GetStatusListForTeam(TextView tv_remark_for_team, LinearLayout ll_appt_date_team, TextView tv_appt_date, Button btn_reassign) {
            this.tv_remark_for_team = tv_remark_for_team;
            this.ll_appt_date_team = ll_appt_date_team;
            this.tv_appt_date = tv_appt_date;
            this.btn_reassign = btn_reassign;
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
            param.add(new ParamsPojo("Type", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionAssignmentRemarks, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    RemarkForReReGistrationModel remarkForReReGistrationModel = new Gson().fromJson(result, RemarkForReReGistrationModel.class);
                    type = remarkForReReGistrationModel.getStatus();
                    message = remarkForReReGistrationModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<RemarkForReReGistrationModel.Output> statusList = remarkForReReGistrationModel.getOutput();
                        if (statusList.size() > 0) {
                            // camptypelist.add(0, new TalukaModel().new Output("ALL", 0));
                            showStatusDialogueForTeam(statusList, tv_remark_for_team, ll_appt_date_team, tv_appt_date, btn_reassign);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showStatusDialogueForTeam(final List<RemarkForReReGistrationModel.Output> statusList, TextView tv_remark_for_team, LinearLayout ll_appt_date_team, TextView tv_appt_date, Button btn_reassign) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
            builderSingle.setTitle("Select Remark");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

            for (int i = 0; i < statusList.size(); i++) {
                arrayAdapter.add(String.valueOf(statusList.get(i).getAssignmentRemarks()));
            }

            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tv_remark_for_team.setText(statusList.get(which).getAssignmentRemarks());
                    remarkId = String.valueOf(statusList.get(which).getArId());

                    if (remarkId.equals("3")) {
                        ll_appt_date_team.setVisibility(View.VISIBLE);

                    } else {
                        ll_appt_date_team.setVisibility(View.GONE);
                    }
                    tv_appt_date.setText("");
                    //  refreshCalendar();

//                    new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, type, "0", EmpCode, "2");


                }
            });
            builderSingle.show();

        }

    }


    private class BeneficiaryListAdapter extends RecyclerView.Adapter<BeneficiaryListAdapter.MyViewHolder> {

        int selIndex = -1;
        boolean showUserdata = false;
        private List<BeneficiaryListModel.Output> adminList;
        private Context context;


        public BeneficiaryListAdapter(Context context, List<BeneficiaryListModel.Output> adminList) {
            this.context = context;
            this.adminList = adminList;


        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_reject_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            BeneficiaryListModel.Output data = adminList.get(position);

//            holder.cb_select.setText(resourceListModel.getOutput().get(position).ge());

            holder.tv_sr_no.setText("" + (position + 1));
            holder.tv_patientname.setText(data.getBeneficiaryName());
            holder.benName.setText(data.getBeneficiaryName());
            holder.tv_type.setText(data.getArea());
            holder.tv_cardno.setText(data.getPincode());

            holder.benName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + data.getMobileNo()));
                    startActivity(intent);
                }
            });

            if (DESGID.equals("29") || DESGID.equals("160") || DESGID.equals("162") || DESGID.equals("92") || DESGID.equals("108")) {

                holder.ll_remark_team.setVisibility(View.GONE);
                holder.ll_appt_date_team.setVisibility(View.GONE);
                holder.btn_reassign.setVisibility(View.GONE);

                if (statusType.equals("7")) {

                }


            } else {
                holder.btn_submit.setText("Save");

                holder.ll_remark_team.setVisibility(View.VISIBLE);
                holder.ll_appt_date_team.setVisibility(View.VISIBLE);
//                holder.btn_reassign.setVisibility(View.VISIBLE);
                holder.mainLLDate.setVisibility(View.GONE);

            }


            holder.btn_reassign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(mContext, D2DSelectCampActivity.class)
                            .putExtra("beneficiaryNumber", data.getBeneficiaryNumber())
                            .putExtra("beneficiaryName", data.getBeneficiaryName())
                            .putExtra("relation", relationWithWorker)
                            .putExtra("regId", data.getRej_Regdid())
                            .putExtra("rejCampId", rejCampId)
                            .putExtra("Type", "5")

                    );
                }
            });

            if (selIndex == holder.getAbsoluteAdapterPosition()) {
                holder.mainLL_Team.setVisibility(View.VISIBLE);
//                holder.mainLL_Team.setVisibility(holder.mainLL_Team.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            } else {
                holder.mainLL_Team.setVisibility(View.GONE);
            }

            holder.tv_patientname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUserdata = !showUserdata;

                    if (holder.mainLL_Team.getVisibility() == View.VISIBLE) {
                        holder.mainLL_Team.setVisibility(View.GONE);
                        return;
                    }


                    if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {


//                        new GetBeneficiaryDataForTeamData(holder, holder.getAbsoluteAdapterPosition()).execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "0", data.getBeneficiaryNumber(), EmpCode, "3");

                        getBeneficiaryDataForTeam(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE,
                                landingLabId, areaArray, pincodeArray, "0", data.getRej_Regdid(),
                                EmpCode, "3", holder);


                    } else {




//                        new GetBeneficiaryData(holder, holder.getAbsoluteAdapterPosition()).execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "0", data.getBeneficiaryNumber(), EmpCode, "3");

                        getBeneficiaryData(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE,
                                landingLabId, areaArray, pincodeArray, "0", data.getRej_Regdid(),
                                EmpCode, "3", holder);


                    }


//                    holder.mainLL_Team.setVisibility(holder.mainLL_Team.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


//                    if (isDataSuccess.equals("1")){
//
//                       adapter.notifyDataSetChanged();
//
//                        holder.tv_relation_with_worker.setText(data.getBeneficiaryName());
//                        holder.tv_pincode.setText(data.getPincode());
//                        holder.tv_area.setText(data.getArea());
//                        holder.tv_address.setText(data.getAddress());
//                        holder.campType.setText(data.getCampType());
//                        holder.tv_receivedAt.setText(data.getIsRejectedFrom());
//                        holder.tv_rejected_date.setText(data.getRejectionDate());
//                        holder.Reject_Reason.setText(data.getRejectedReason());
//                        holder.member1.setText(data.getUsernamem1());
//                        holder.tv_member2.setText(data.getUsernamem2());
//
////                        holder.tv_campId.setText(data.getcamId());
////                        holder.campDate.setText(data.getca());
//                    }


//                    if (rv_teams.getVisibility() == View.VISIBLE) {
//                        tvTeamHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24), null);
//                    } else {
//                        tvTeamHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), null);
//
//                    }

                    selIndex = holder.getAbsoluteAdapterPosition();
                    if (selIndex == holder.getAbsoluteAdapterPosition()) {
                        holder.mainLL_Team.setVisibility(View.VISIBLE);
                    } else {


                        holder.mainLL_Team.setVisibility(View.GONE);

                    }

                    notifyDataSetChanged();
                }
            });


            holder.tv_select_team.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetTeamDetailsListForAssign(holder.tv_select_team, holder.tv_active_or_inactive).execute("0", EmpCode);
                }
            });

            holder.tv_remark_for_team.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new GetStatusListForTeam(holder.tv_remark_for_team, holder.ll_appt_date_team, holder.tv_appt_date, holder.btn_reassign).execute("1");

                }
            });

            holder.tv_appt_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.tv_remark_for_team.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select remark", false);
                        return;
                    }

                    DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    holder.tv_appt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
//                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                                    CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                                }

                            }, mYear, mMonth, mDay);
                    Calendar c = Calendar.getInstance();
                    c.set(mYear, mMonth, mDay);
                    //  c.add(Calendar.DAY_OF_MONTH, 15);


                    try {

//                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
                        dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                        // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd1.show();
                }
            });


            holder.btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {
                        if (holder.tv_remark_for_team.getText().toString().isEmpty()) {
                            Utilities.showAlertDialog(mContext, "Alert", "Please Select remark", false);
                            return;
                        }

                        if (remarkId.equals("3")) {
                            if (holder.tv_appt_date.getText().toString().isEmpty()) {
                                Utilities.showAlertDialog(mContext, "Alert", "Please Select appointment date", false);
                                return;
                            }

                        }

                        JsonArray appointmentJsonArray = new JsonArray();

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Regdid", data.getRej_Regdid());
                        jsonObject.addProperty("Regdno", data.getBeneficiaryNumber());
                        jsonObject.addProperty("AppointmentDate", holder.tv_appt_date.getText().toString().trim());
                        appointmentJsonArray.add(jsonObject);


                        Dialog dialog;
                        dialog = new Dialog(RejectedBeneficiaryListActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alert_popup_dialoge);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                        Button btnYes = dialog.findViewById(R.id.btnYes);
                        Button btnNo = dialog.findViewById(R.id.btnNo);
                        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                new InsertAppointmentDetails().execute(appointmentJsonArray.toString(), remarkId, EmpCode);

                            }
                        });
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });


//                        appointmentJsonArray.toString();


                    } else {

                        if (holder.tv_select_team.getText().toString().isEmpty()) {
                            Utilities.showAlertDialog(mContext, "Alert", "Please Select Team", false);
                            return;
                        }
                        if (holder.tv_select_team.getText().toString().matches("NA")) {
                            Utilities.showAlertDialog(mContext, "Alert", "Please Select Team", false);
                            return;
                        }




                        JsonArray teamUserJsonArray = new JsonArray();

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("USERID", member1);
                        jsonObject.addProperty("Teamid", AssignedId);
                        teamUserJsonArray.add(jsonObject);

                        JsonObject jsonObject1 = new JsonObject();
                        jsonObject1.addProperty("USERID", member2);
                        jsonObject1.addProperty("Teamid", AssignedId);
                        teamUserJsonArray.add(jsonObject1);


                        Dialog dialog;
                        dialog = new Dialog(RejectedBeneficiaryListActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.alert_popup_dialoge);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
                        Button btnYes = dialog.findViewById(R.id.btnYes);
                        Button btnNo = dialog.findViewById(R.id.btnNo);
                        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                new InsertTeamDetailsDetails().execute(data.getRej_Regdid(), data.getRejCampID(), teamUserJsonArray.toString(), EmpCode);

                            }
                        });
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });


                    }

                }
            });


//            if (resourceListModel.getOutput().get(position).isChecked()) {
//                holder.cb_select.setChecked(true);
//            }

//            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                resourceListModel.getOutput().get(position).setChecked(isChecked);
//            });
        }

        @Override
        public int getItemCount() {
            return adminList != null ? adminList.size() : 0;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;
            private LinearLayout mainLL_Team, mainLLDate, ll_remark_team, ll_appt_date_team;
            private TextView benName, benNumber, tv_next_renewalDate, tv_active_or_inactive, tv_remark_for_team, tv_appt_date, tv_type, tv_cardno, tv_sr_no, tv_patientname, tv_relation_with_worker, tv_pincode, tv_area, tv_address, member1, tv_member2, tv_select_team,
                    tv_campId, campType, campDate, tv_receivedAt, tv_rejected_date, Reject_Reason,Reject_lab;
            private Button btn_reassign, btn_submit;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
                tv_patientname = view.findViewById(R.id.tv_patientname);
                mainLL_Team = view.findViewById(R.id.mainLL_Team);
                tv_cardno = view.findViewById(R.id.tv_cardno);
                Reject_lab = view.findViewById(R.id.Reject_lab);
                benName = view.findViewById(R.id.benName);
                tv_type = view.findViewById(R.id.tv_type);
                tv_relation_with_worker = view.findViewById(R.id.tv_relation_with_worker);
                tv_pincode = view.findViewById(R.id.tv_pincode);
                tv_area = view.findViewById(R.id.tv_area);
                tv_address = view.findViewById(R.id.tv_address);
                tv_campId = view.findViewById(R.id.tv_campId);
                campType = view.findViewById(R.id.campType);
                campDate = view.findViewById(R.id.campDate);
                tv_receivedAt = view.findViewById(R.id.tv_receivedAt);
                tv_rejected_date = view.findViewById(R.id.tv_rejected_date);
                Reject_Reason = view.findViewById(R.id.Reject_Reason);
                member1 = view.findViewById(R.id.member1);
                tv_member2 = view.findViewById(R.id.tv_member2);
                tv_select_team = view.findViewById(R.id.tv_select_team);
                btn_submit = view.findViewById(R.id.btn_submit);
                ll_appt_date_team = view.findViewById(R.id.ll_appt_date_team);
                ll_remark_team = view.findViewById(R.id.ll_remark_team);
                btn_reassign = view.findViewById(R.id.btn_reassign);
                mainLLDate = view.findViewById(R.id.mainLLDate);
                tv_remark_for_team = view.findViewById(R.id.tv_remark_for_team);
                tv_appt_date = view.findViewById(R.id.tv_appt_date);
                tv_active_or_inactive = view.findViewById(R.id.tv_active_or_inactive);
                tv_sr_no = view.findViewById(R.id.tv_sr_no);
                benNumber = view.findViewById(R.id.benNumber);
                tv_next_renewalDate = view.findViewById(R.id.tv_next_renewalDate);

            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    public class GetTeamDetailsListForAssign extends AsyncTask<String, Void, String> {

        TextView tv_select_team;
        TextView tv_active_or_inactive;

        public GetTeamDetailsListForAssign(TextView tv_select_team, TextView tv_active_or_inactive) {
            this.tv_select_team = tv_select_team;
            this.tv_active_or_inactive = tv_active_or_inactive;
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
            param.add(new ParamsPojo("Pincode", params[0]));
            param.add(new ParamsPojo("USERID", params[1]));


            // res = WebServiceCall.APICall(ApplicationConstants.GetAssignedTeamDetails, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionTeamDetials, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    assignteamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModel.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist, tv_select_team, tv_active_or_inactive);

                            // ll_assigned.setVisibility(View.VISIBLE);


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", "Team not assigned for selected campId, assign team first", false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(mContext, "Please select CampId", "You have to select campId", false);
            }
        }
    }

    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist, TextView tv_select_team, TextView tv_active_or_inactive) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        ImageView ImageView = dialogueView.findViewById(R.id.ImageView);
        ImageView.setVisibility(View.GONE);
        edt_search.setVisibility(View.GONE);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));

        rvList.setAdapter(new TeamsDetailsAssignedForReRegistrationAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getTeamID();
                                member1 = assignteamlist.get(position).getMemberUserID1();
                                member2 = assignteamlist.get(position).getMemberUserID2();

                                tv_select_team.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

                                tv_active_or_inactive.setText(assignteamlist.get(position).getTeamName() + " " + "(Active)");

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new TeamsDetailsAssignedForReRegistrationAdapter(assignteamlist));
                    return;
                }

                if (teamList.size() == 0) {
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
                    rvList.setAdapter(new TeamsDetailsAssignedForReRegistrationAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new TeamsDetailsAssignedForReRegistrationAdapter(assignteamlist));
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

    public class GetCountData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Todate", params[1]));
            param.add(new ParamsPojo("SubOrgId", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));
            param.add(new ParamsPojo("TALLGDCODE", params[5]));
            param.add(new ParamsPojo("Labcode", params[6]));
            param.add(new ParamsPojo("T_AreaofPincode", params[7]));
            param.add(new ParamsPojo("T_PincodeofArea", params[8]));
            param.add(new ParamsPojo("Arid", params[9]));
            param.add(new ParamsPojo("BeneficiaryNumber", params[10]));
            param.add(new ParamsPojo("UserId", params[11]));
            param.add(new ParamsPojo("Type", params[12]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridDataWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryListModel beneficiaryListModel = new Gson().fromJson(result, BeneficiaryListModel.class);
                    type = beneficiaryListModel.getStatus();
                    message = beneficiaryListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && adminList.size() > 0) {

                            binding.rvBeneficiaryDetails.setAdapter(new BeneficiaryListAdapter(mContext, adminList));


                            int t = 0;
                            int p = 0;


//                            tv_NotWorkingCount.setText("" + p);
//                            tv_Working_TeamCount.setText("" + t);


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

//                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {


//                        rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(mContext, new ArrayList<>()));


                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetCountDataForTeam extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Todate", params[1]));
            param.add(new ParamsPojo("SubOrgId", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));
            param.add(new ParamsPojo("TALLGDCODE", params[5]));
            param.add(new ParamsPojo("Labcode", params[6]));
            param.add(new ParamsPojo("T_AreaofPincode", params[7]));
            param.add(new ParamsPojo("T_PincodeofArea", params[8]));
            param.add(new ParamsPojo("Arid", params[9]));
            param.add(new ParamsPojo("BeneficiaryNumber", params[10]));
            param.add(new ParamsPojo("UserId", params[11]));
            param.add(new ParamsPojo("Type", params[12]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryToTeam, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridDataWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryListModel beneficiaryListModel = new Gson().fromJson(result, BeneficiaryListModel.class);
                    type = beneficiaryListModel.getStatus();
                    message = beneficiaryListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && adminList.size() > 0) {

                            binding.rvBeneficiaryDetails.setAdapter(new BeneficiaryListAdapter(mContext, adminList));


                            int t = 0;
                            int p = 0;


//                            tv_NotWorkingCount.setText("" + p);
//                            tv_Working_TeamCount.setText("" + t);


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

//                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {


//                        rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(mContext, new ArrayList<>()));


                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetBeneficiaryData extends AsyncTask<String, Void, String> {


        BeneficiaryListAdapter.MyViewHolder holder;
        int position;

        public GetBeneficiaryData(BeneficiaryListAdapter.MyViewHolder holder, int absoluteAdapterPosition) {

            this.holder = holder;
            position = absoluteAdapterPosition;
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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Todate", params[1]));
            param.add(new ParamsPojo("SubOrgId", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));
            param.add(new ParamsPojo("TALLGDCODE", params[5]));
            param.add(new ParamsPojo("Labcode", params[6]));
            param.add(new ParamsPojo("T_AreaofPincode", params[7]));
            param.add(new ParamsPojo("T_PincodeofArea", params[8]));
            param.add(new ParamsPojo("Arid", params[9]));
            param.add(new ParamsPojo("BeneficiaryNumber", params[10]));
            param.add(new ParamsPojo("UserId", params[11]));
            param.add(new ParamsPojo("Type", params[12]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryStatusandDetails, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridDataWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryListModel beneficiaryListModel = new Gson().fromJson(result, BeneficiaryListModel.class);
                    type = beneficiaryListModel.getStatus();
                    message = beneficiaryListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && adminList.size() > 0) {

//                            binding.rvBeneficiaryDetails.setAdapter(new BeneficiaryListAdapter(mContext,adminList));


                            isDataSuccess = "1";

//                            int t = 0;
//                            int p = 0;
//
//
////                            tv_NotWorkingCount.setText("" + p);
////                            tv_Working_TeamCount.setText("" + t);
//

                            if (beneficiaryListModel.getOutput().size() > 0) {
                                BeneficiaryListModel.Output data = beneficiaryListModel.getOutput().get(0);

                                holder.tv_relation_with_worker.setText(data.getRelationWithWorker());
                                holder.tv_pincode.setText(data.getPincode());
                                holder.tv_area.setText(data.getArea());
                                holder.tv_address.setText(data.getAddress());
                                holder.campType.setText(data.getCampType());
                                holder.tv_receivedAt.setText(data.getRejectedFrom());
                                holder.tv_rejected_date.setText(data.getRejectionDate());
                                holder.Reject_Reason.setText(data.getRejectedReason());
                                holder.member1.setText(data.getUsernamem1());
                                holder.tv_member2.setText(data.getUsernamem2());
                                holder.tv_campId.setText(data.getRejCampID());
                                holder.campDate.setText(data.getCampDate());
                                AssignedId = String.valueOf(data.getTeamID());

                                member1 = data.getMemberUserID1();
                                member2 = data.getMemberUserID2();

                                holder.tv_select_team.setText("" + data.getTeamName());
                                if (data.getIsTeamMapped().equalsIgnoreCase("1")) {
                                    holder.btn_submit.setVisibility(View.GONE);
                                }


                            }

                        } else {

//                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {


//                        rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(mContext, new ArrayList<>()));


                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetBeneficiaryDataForTeamData extends AsyncTask<String, Void, String> {


        BeneficiaryListAdapter.MyViewHolder holder;
        int position;

        public GetBeneficiaryDataForTeamData(BeneficiaryListAdapter.MyViewHolder holder, int absoluteAdapterPosition) {

            this.holder = holder;
            position = absoluteAdapterPosition;
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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Todate", params[1]));
            param.add(new ParamsPojo("SubOrgId", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));
            param.add(new ParamsPojo("TALLGDCODE", params[5]));
            param.add(new ParamsPojo("Labcode", params[6]));
            param.add(new ParamsPojo("T_AreaofPincode", params[7]));
            param.add(new ParamsPojo("T_PincodeofArea", params[8]));
            param.add(new ParamsPojo("Arid", params[9]));
            param.add(new ParamsPojo("BeneficiaryNumber", params[10]));
            param.add(new ParamsPojo("UserId", params[11]));
            param.add(new ParamsPojo("Type", params[12]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryToTeam, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetActiveInactiveD2DTeamsGridDataWithCatagoryID, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryListModel beneficiaryListModel = new Gson().fromJson(result, BeneficiaryListModel.class);
                    type = beneficiaryListModel.getStatus();
                    message = beneficiaryListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && adminList.size() > 0) {

//                            binding.rvBeneficiaryDetails.setAdapter(new BeneficiaryListAdapter(mContext,adminList));


                            isDataSuccess = "1";

//                            int t = 0;
//                            int p = 0;
//
//
////                            tv_NotWorkingCount.setText("" + p);
////                            tv_Working_TeamCount.setText("" + t);
//

                            if (beneficiaryListModel.getOutput().size() > 0) {
                                BeneficiaryListModel.Output data = beneficiaryListModel.getOutput().get(0);

                                holder.tv_relation_with_worker.setText(data.getRelationWithWorker());
                                holder.tv_pincode.setText(data.getPincode());
                                holder.tv_area.setText(data.getArea());
                                holder.tv_address.setText(data.getAddress());
                                holder.campType.setText(data.getCampType());
                                holder.tv_receivedAt.setText(data.getRejectedFrom());
                                holder.tv_rejected_date.setText(data.getRejectionDate());
                                holder.Reject_Reason.setText(data.getRejectedReason());
                                holder.member1.setText(data.getUsernamem1());
                                holder.tv_member2.setText(data.getUsernamem2());
                                holder.tv_campId.setText(data.getRejCampID());
                                holder.campDate.setText(data.getCampDate());
                                AssignedId = String.valueOf(data.getTeamID());
                                member1 = data.getMemberUserID1();
                                member2 = data.getMemberUserID2();

                                holder.btn_submit.setText("Save");

                                holder.tv_select_team.setText("" + data.getTeamName());
//                                if (data.getIsTeamMapped().equalsIgnoreCase("1")){
//                                    holder.btn_submit.setVisibility(View.GONE);
//                                }


                            }

                        } else {

//                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, new ArrayList<>()));

                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {


//                        rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(mContext, new ArrayList<>()));


                        Utilities.showAlertDialog(mContext, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }
    }


    private class InsertTeamDetailsDetails extends AsyncTask<String, Void, String> {


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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Regdid", params[0]));
            param.add(new ParamsPojo("Campid", params[1]));
            param.add(new ParamsPojo("RecollectionTeamandBeneficiaryMapping", params[2]));
            param.add(new ParamsPojo("AssignedBy", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.Insert_RecollectionTeamandBeneficiaryMapping, ApplicationConstants.webservice_d2d, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Team Assigned successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");


//                                finish();


//                                new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "2", "0", EmpCode, "2");

                                if (searchFilterType.equalsIgnoreCase("0")) {

                                    getCountForPageload(
                                            areaArray,
                                            pincodeArray
                                    );

                                } else {
                                    getCount(
                                            areaArray,
                                            pincodeArray
                                    );

                                }


                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(mContext, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(mContext, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class InsertAppointmentDetails extends AsyncTask<String, Void, String> {


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
            Log.d("InsertSampleAccept", "doInBackground: " + Arrays.toString(params));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("RecollectionAppointmentDateType", params[0]));
            param.add(new ParamsPojo("ArId", params[1]));
            param.add(new ParamsPojo("Userid", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.UpdaterecollectionAppointmentDate, ApplicationConstants.webservice_d2d, param);
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("InsertSampleAccept", "onPostExecute: " + result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");

                    if (status.equalsIgnoreCase("Success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);

                        if (remarkId.equals("3")) {
                            builder.setMessage("Appointment Booked successfully");
                        } else {
                            builder.setMessage("Data Saved successfully");
                        }

                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");


//                                finish();


//                                new GetCountDataForTeam().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "0", "0", EmpCode, "2");


                                if (searchFilterType.equalsIgnoreCase("0")) {


                                    if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {

                                        getCountForPageloadForTeam(
                                                areaArray,
                                                pincodeArray
                                        );

                                    } else {

                                    }

//                                    getCountForPageload(
//                                            areaArray,
//                                            pincodeArray
//                                    );

                                } else {
                                    getCountForTeam(
                                            areaArray,
                                            pincodeArray
                                    );

                                }


                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(mContext, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(mContext, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void getCount(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForReregistrationForCC(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, statusType, "0", EmpCode, "2",campType).enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        adminList = new ArrayList<>();

                        // Populating adminList with data
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (BeneficiaryListModel.Output output : countsForDailyWorkDashboardModels) {
                                adminList.add(output); // Add each output to adminList
                            }

                            // Set adapter to RecyclerView
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, adminList);
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                            // Notify adapter about data changes
                            adapter.notifyDataSetChanged();
                        } else {
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                        }
                    } else {
                        BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                        binding.rvBeneficiaryDetails.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    private void getCountForTeam(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForReregistrationForTeam(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, statusType, "0", EmpCode, "2",campType).enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        adminList = new ArrayList<>();

                        // Populating adminList with data
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (BeneficiaryListModel.Output output : countsForDailyWorkDashboardModels) {
                                adminList.add(output); // Add each output to adminList
                            }

                            // Set adapter to RecyclerView
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, adminList);
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                            // Notify adapter about data changes
                            adapter.notifyDataSetChanged();
                        } else {
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                            binding.rvBeneficiaryDetails.setAdapter(adapter);
                        }
                    } else {
                        BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                        binding.rvBeneficiaryDetails.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    private void getBeneficiaryData(String fromDate, String toDate, String subOrgId, String divid,
                                    String distlgdCode, String tallgdCode, String labCode,
                                    String areaOfPincode, String pincodeOfArea, String arid,
                                    String beneficiaryNumber, String userId, String type,
                                    BeneficiaryListAdapter.MyViewHolder holder) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<BeneficiaryListModel> call = apiInterface.getCountForPageloadBeneficiary(
                fromDate, toDate, subOrgId, divid, distlgdCode, labCode, arid, beneficiaryNumber, userId, type,DESGID,campType);

        call.enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BeneficiaryListModel beneficiaryListModel = response.body();
                    if (beneficiaryListModel.getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && !adminList.isEmpty()) {
                            // Set data to UI elements in holder
                            BeneficiaryListModel.Output data = adminList.get(0);
                            holder.tv_relation_with_worker.setText(data.getRelationWithWorker());
                            holder.tv_pincode.setText(data.getPincode());
                            holder.tv_area.setText(data.getArea());
                            holder.tv_address.setText(data.getAddress());
                            holder.campType.setText(data.getCampTypeDescription());
                            holder.tv_receivedAt.setText(data.getRejectedFrom());
                            holder.tv_rejected_date.setText(data.getRejectionDate());
                            holder.Reject_Reason.setText(data.getRejectedReason().replace("\\n", "\n"));

                            holder.tv_campId.setText(data.getRejCampID());
                            holder.campDate.setText(data.getCampDate());
                            holder.tv_select_team.setText(data.getTeamName());
                            holder.tv_patientname.setText(data.getBeneficiaryName());
                            holder.benName.setText(data.getBeneficiaryName());
                            holder.tv_next_renewalDate.setText(data.getNextRenewalDate());
                            holder.tv_type.setText(data.getArea());
                            holder.benNumber.setText(data.getRegdNo());
                            holder.tv_cardno.setText(data.getPincode());
                            AssignedId = String.valueOf(data.getTeamID());

                            if (data.getRejectedLabName()!=null){
                                holder.Reject_lab.setText(data.getRejectedLabName());
                            }

                            member1 = data.getMemberUserID1();
                            member2 = data.getMemberUserID2();

                            if (data.getIsAppointmentConfirm().equalsIgnoreCase("1")) {
                                holder.btn_submit.setVisibility(View.GONE);
                            }

                            if (statusType.equalsIgnoreCase("1") || statusType.equalsIgnoreCase("8") || statusType.equalsIgnoreCase("0")||statusType.equalsIgnoreCase("9")) {
                                holder.btn_submit.setVisibility(View.GONE);
                            }


                            if (data.getIsTeamActive().equals("1")) {
                                holder.tv_active_or_inactive.setText(data.getTeamName() + " " + "(Active)");
                            } else {
                                holder.tv_active_or_inactive.setText(data.getTeamName() + " " + "(InActive)");
                            }

                            if (data.getIsTeamMapped().equalsIgnoreCase("1")) {
                                Utilities.showAlertDialog(mContext, "Alert", "Team assigned for this beneficiary", true);
                            }

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", beneficiaryListModel.getMessage(), false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        });
    }

    private void getBeneficiaryDataForTeam(String fromDate, String toDate, String subOrgId, String divid,
                                           String distlgdCode, String tallgdCode, String labCode,
                                           String areaOfPincode, String pincodeOfArea, String arid,
                                           String beneficiaryNumber, String userId, String type,
                                           BeneficiaryListAdapter.MyViewHolder holder) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<BeneficiaryListModel> call = apiInterface.getCountForPageloadBeneficiaryForTeam(
                fromDate, toDate,
                arid, beneficiaryNumber, userId, type,campType);

        call.enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BeneficiaryListModel beneficiaryListModel = response.body();
                    if (beneficiaryListModel.getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> adminList = beneficiaryListModel.getOutput();
                        if (adminList != null && !adminList.isEmpty()) {
                            // Set data to UI elements in holder
                            BeneficiaryListModel.Output data = adminList.get(0);
                            holder.tv_relation_with_worker.setText(data.getRelationWithWorker());
                            holder.tv_pincode.setText(data.getPincode());
                            holder.tv_area.setText(data.getArea());
                            holder.tv_address.setText(data.getAddress());
                            holder.campType.setText(data.getCampTypeDescription());
                            holder.tv_receivedAt.setText(data.getRejectedFrom());
                            holder.tv_rejected_date.setText(data.getRejectionDate());
                            holder.Reject_Reason.setText(data.getRejectedReason());
//                            holder.member1.setText(data.getUsernamem1());
//                            holder.tv_member2.setText(data.getUsernamem2());
                            holder.tv_campId.setText(data.getRejCampID());
                            holder.campDate.setText(data.getCampDate());
                            holder.tv_select_team.setText(data.getTeamName());

                            holder.tv_patientname.setText(data.getBeneficiaryName());
                            holder.benName.setText(data.getBeneficiaryName());
                            holder.tv_type.setText(data.getArea());
                            holder.tv_cardno.setText(data.getPincode());
                            holder.tv_remark_for_team.setText(data.getRemarks());
                            holder.benNumber.setText(data.getRegdNo());
                            holder.tv_next_renewalDate.setText(data.getNextRenewalDate());

                            if (data.getRejectedLabName()!=null){
                                holder.Reject_lab.setText(data.getRejectedLabName());
                            }


                            remarkId = String.valueOf(data.getArid());

                            if (remarkId != null) {
                                if (remarkId.equals("3")) {
                                    holder.ll_appt_date_team.setVisibility(View.VISIBLE);

                                } else {
                                    holder.ll_appt_date_team.setVisibility(View.GONE);
                                }

                            }

                            AssignedId = String.valueOf(data.getTeamID());

                            member1 = data.getMemberUserID1();
                            member2 = data.getMemberUserID2();

                            rejCampId = data.getRejCampID();

//                            member1 = data.getUsernamem1();
//                            member2 = data.getUsernamem2();


                            if (data.getIsTeamActive().equals("1")) {
                                holder.tv_active_or_inactive.setText("Assigned Team" + " - " + data.getTeamName());
                            } else {
                                holder.tv_active_or_inactive.setText("Assigned Team" + " - " + data.getTeamName());
                            }


                            if (data.getIsAppointmentConfirm().equalsIgnoreCase("1")) {
                                holder.tv_appt_date.setText(data.getAppointmentDate());

                                holder.btn_reassign.setVisibility(View.VISIBLE);
                            } else {
                                holder.btn_reassign.setVisibility(View.GONE);
                            }

                            if (statusType.equalsIgnoreCase("1") || statusType.equalsIgnoreCase("8") || statusType.equalsIgnoreCase("0")||statusType.equalsIgnoreCase("9")) {
                                holder.btn_submit.setVisibility(View.GONE);
                                holder.btn_reassign.setVisibility(View.GONE);
                                holder.tv_remark_for_team.setEnabled(false);

                            }

                            if (statusType.equalsIgnoreCase("6")){
                                holder.btn_reassign.setVisibility(View.GONE);

                            }


                            relationWithWorker = data.getRelationWithWorker();


//                            if (data.getIsTeamMapped().equalsIgnoreCase("1")) {
//                                holder.btn_submit.setVisibility(View.GONE);
//                            }
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", beneficiaryListModel.getMessage(), false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        });
    }


    private void getCountForPageload(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForPageloadBeneficiary(fromDate, toDate, oganizationId, "0", DISTLGDCODE, "0", statusType, "0",EmpCode, "2",DESGID,campType).enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        adminList = new ArrayList<>();

                        // Populating adminList with data
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (BeneficiaryListModel.Output output : countsForDailyWorkDashboardModels) {
                                adminList.add(output); // Add each output to adminList
                            }

                            // Set adapter to RecyclerView
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, adminList);
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                            // Notify adapter about data changes
                            adapter.notifyDataSetChanged();
                        } else {
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                        }
                    } else {
                        BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                        binding.rvBeneficiaryDetails.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    private void getCountForPageloadForTeam(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForPageloadBeneficiaryForTeam(fromDate, toDate , statusType, "0", EmpCode, "2","0").enqueue(new Callback<BeneficiaryListModel>() {
            @Override
            public void onResponse(Call<BeneficiaryListModel> call, Response<BeneficiaryListModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<BeneficiaryListModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        adminList = new ArrayList<>();

                        // Populating adminList with data
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (BeneficiaryListModel.Output output : countsForDailyWorkDashboardModels) {
                                adminList.add(output); // Add each output to adminList
                            }

                            // Set adapter to RecyclerView
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, adminList);
                            binding.rvBeneficiaryDetails.setAdapter(adapter);

                            // Notify adapter about data changes
                            adapter.notifyDataSetChanged();
                        } else {
                            BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                            binding.rvBeneficiaryDetails.setAdapter(adapter);
                        }
                    } else {
                        BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(mContext, new ArrayList<>());
                        binding.rvBeneficiaryDetails.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {

            if (searchFilterType.equalsIgnoreCase("0")) {

                getCountForPageloadForTeam(
                        areaArray,
                        pincodeArray
                );

            } else {

                getCountForTeam(
                        areaArray,
                        pincodeArray
                );


            }


        } else {
            if (searchFilterType.equalsIgnoreCase("0")) {

                getCountForPageload(
                        areaArray,
                        pincodeArray
                );

            } else {

                getCount(
                        areaArray,
                        pincodeArray
                );

            }
        }
    }


}