package com.myhindlab.abkat.activities.confirmatoryTest;

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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
//import com.myhindlab.abkat.activities.confirmatoryTest.Model.CTMedicineDetailsModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CTMedicineDetailsModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryListModel;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.CommonBeneficiaryStatusModel;
import com.myhindlab.abkat.activities.confirmatoryTest.adapter.CTMedicineDetailsAdapter;
import com.myhindlab.abkat.activities.confirmatoryTest.adapter.CommonBeneficiaryListAdapter;
import com.myhindlab.abkat.activities.doortodoor.ExpectectedBeneficiaryForTeamsActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.FibroScanningCountForDistrictActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter.LiverCountDistrictWiseAdapter;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.model.MonthModel;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.adapters.doortodoor.TeamCallingAdapter;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.databinding.ActivityCtAndMedicineBeneficiaryListBinding;
import com.myhindlab.abkat.databinding.ActivityFibroScaningDataBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CTAndedicineCommonBeneficiaryListActivity extends AppCompatActivity implements CommonBeneficiaryListAdapter.onTouchListner {
    ActivityCtAndMedicineBeneficiaryListBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<CommonBeneficiaryStatusModel.Output> invoiceDetailsList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String statusId = "0",CampDate, CampDateToDate, type, toDate;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;

    private List<CommonBeneficiaryListModel.Output> invoiceList;


    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", monthId = "0", isSendForVerification,isInvoiceApproved, remarkId, otpnumber, yearId = "0", campTypeId = "0", teamId = "0", userInvoiceID = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2",
            DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCtAndMedicineBeneficiaryListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = CTAndedicineCommonBeneficiaryListActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));



        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -15);

        binding.Todate.setText(Utilities.dfDate.format(new Date()));
        binding.fromDate.setText(Utilities.dfDate.format(toCal.getTime()));


        CampDate = Utilities.dfDate.format(toCal.getTime());
        CampDateToDate = Utilities.dfDate.format(new Date());


    }


    private void verificationRemark(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_remark, null);
        alertBuilder.setView(alertLayout);
        EditText tv_remark = alertLayout.findViewById(R.id.tv_remark);
        EditText tv_otp = alertLayout.findViewById(R.id.tv_otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        EditText tv_remark_description = alertLayout.findViewById(R.id.tv_remark_description);
        LinearLayout ll_main_others = alertLayout.findViewById(R.id.ll_main_others);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

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
            monthId = getIntent().getStringExtra("month");
            yearId = getIntent().getStringExtra("year");
            userInvoiceID = getIntent().getStringExtra("userInvoice");
            isSendForVerification = getIntent().getStringExtra("isSendForVerification");
            isInvoiceApproved = getIntent().getStringExtra("raisedStatus");

        }



        if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("162")||
                DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("136")
                ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("172")
        ){

            getListData();

        }else {
            getListDataForTeam();

        }

//        getListData();
        binding.tvStatus.setText("ALL");

    }

    private void eventListener() {


        binding.Todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.fromDate.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", mContext);
                else {

                    DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    binding.Todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));

                                    CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);



                                    if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("162")||
                                            DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("136")
                                            ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("172")
                                    ){

                                        getListData();

                                    }else {
                                        getListDataForTeam();

                                    }

//                                    getListData();
                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate.parse(CampDate));
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    c.add(Calendar.DAY_OF_MONTH, 14);

                    try {
                        dpd1.getDatePicker().setCalendarViewShown(false);
                        dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());

                        // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd1.show();
                }
            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<CommonBeneficiaryListModel.Output> searchPatientList = new ArrayList<>();

                if (invoiceList != null) {
                    if (invoiceList.size() > 0) {
                        for (CommonBeneficiaryListModel.Output pojo : invoiceList) {
                            String siteDetails = pojo.getPatientName();
                            String siteDetailsNew = pojo.getPincode();
                            String siteDetailsArea = pojo.getArea();
                            if ((siteDetails != null && siteDetails.toLowerCase().startsWith(query.toLowerCase())
                            ) || siteDetailsNew != null && siteDetailsNew.startsWith(query) || siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(query.toLowerCase())

                            ) {
                                searchPatientList.add(pojo);
                            }
                        }

                        if (searchPatientList.size() == 0) {
                            Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Alert", "No record found.", false);
                            searchPatientList.addAll(invoiceList);
                            searchPatientList.clear();
                            CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, CTAndedicineCommonBeneficiaryListActivity.this);
                            binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);

                            //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        } else {
                            CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, CTAndedicineCommonBeneficiaryListActivity.this);
                            binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
                            //  binding.rvPatientList.setAdapter(new PostCampBeneficiaryAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, isAdmin, callType));
                        }
                    }
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                List<CommonBeneficiaryListModel.Output> searchPatientList = new ArrayList<>();

                if (invoiceList != null) {
                    if (newText.equals("")) {
                        searchPatientList.addAll(invoiceList);
                        CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, CTAndedicineCommonBeneficiaryListActivity.this);
                        binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
//                        binding.rvPatientList.setAdapter(new PostCampBeneficiaryAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList,false));
                    } else {
                        if (invoiceList.size() > 0) {
                            for (CommonBeneficiaryListModel.Output pojo : invoiceList) {
                                String siteDetails = pojo.getPatientName();
                                String siteDetailsNew = pojo.getPincode();
                                String siteDetailsArea = pojo.getArea();

                                if (siteDetails != null && siteDetails.toUpperCase().startsWith(newText.toUpperCase())
                                        || siteDetailsNew != null && siteDetailsNew.startsWith(newText) || siteDetailsArea != null && siteDetailsArea.toUpperCase().startsWith(newText.toUpperCase())) {
                                    searchPatientList.add(pojo);
                                }
                            }
                            if (searchPatientList.size() == 0) {
                                searchPatientList.addAll(invoiceList);
                                CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, CTAndedicineCommonBeneficiaryListActivity.this);
                                binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
                                //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            } else {
                                CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, searchPatientList, CTAndedicineCommonBeneficiaryListActivity.this);
                                binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
                                // rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            }
                        }
                    }
                }
                return true;
            }
        });

        binding.tvSelectTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchTeamData();
            }
        });





        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.fromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                                binding.Todate.setText("");

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);

                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(mContext)) {

                    if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("162")||
                            DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("136")
                            ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("172")
                    ){

                        getStatus();

                    }else {
                        getStatusForTeam();

                    }

                }else {
                    Utilities.showAlertDialog(mContext,"Alert","Please check internet connection",false);
                }
            }
        });



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
        Toolbar toolbar = binding.toolbar; // Assuming your layout has a Toolbar with id 'toolbar'
        setSupportActionBar(toolbar);
//        ImageButton imageButton = findViewById(R.id.btn_save_accordian);


        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFilter();
            }
        });



        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Common Beneficiary List");
        }
//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(v -> finish());


        binding.imvEye.setVisibility(View.VISIBLE);

        binding.imvEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog;
                dialog = new Dialog(CTAndedicineCommonBeneficiaryListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.raise_info_dialoge);
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


    }




    private void fetchTeamData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait . . .");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<TeamsDetailsModel> call = apiService.getTeamDataByUserId(EmpCode);

        call.enqueue(new Callback<TeamsDetailsModel>() {
            @Override
            public void onResponse(Call<TeamsDetailsModel> call, Response<TeamsDetailsModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    TeamsDetailsModel data = response.body();
                    if (data.getStatus().equalsIgnoreCase("success")) {
                        ArrayList<TeamsDetailsModel.OutputBean> teamList = new ArrayList<>(data.getOutput());
                        if (!teamList.isEmpty()) {
                            showAssignedTeamListDialog(teamList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, CTAndedicineCommonBeneficiaryListActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Fail", data.getMessage(), false);
                    }
                } else {
                    Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Error", "Unexpected server response", false);
                }
            }

            @Override
            public void onFailure(Call<TeamsDetailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Please Try Again", "Server Not Responding", false);
            }
        });
    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(CTAndedicineCommonBeneficiaryListActivity.this);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(CTAndedicineCommonBeneficiaryListActivity.this));

        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(CTAndedicineCommonBeneficiaryListActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamid();

                                binding.tvSelectTeam.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();



                                if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("162")||
                                        DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("136")
                                        ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("172")
                                ){

                                    getListData();

                                }else {
                                    getListDataForTeam();

                                }


//                                getListData();


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


    private void getListData() {
        final ProgressDialog progressDialog = new ProgressDialog(CTAndedicineCommonBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<CommonBeneficiaryListModel> call = apiService.getCommonBeneficiaryList(CampDate,CampDateToDate,"0","0",DISTLGDCODE, EmpCode,teamId,statusId);
        call.enqueue(new Callback<CommonBeneficiaryListModel>() {
            @Override
            public void onResponse(Call<CommonBeneficiaryListModel> call, Response<CommonBeneficiaryListModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        invoiceList = response.body().getOutput();
                        List<CommonBeneficiaryListModel.Output> invoiceList = response.body().getOutput();
                        CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, invoiceList, CTAndedicineCommonBeneficiaryListActivity.this);
                        binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);

                        toggleFilter();

                    } else {
                        CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, new ArrayList<>(), CTAndedicineCommonBeneficiaryListActivity.this);
                        binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
                        Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);
                    }
                }else {
                    Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);
                }
            }

            @Override
            public void onFailure(Call<CommonBeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }
    private void getListDataForTeam() {
        final ProgressDialog progressDialog = new ProgressDialog(CTAndedicineCommonBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<CommonBeneficiaryListModel> call = apiService.getCommonBeneficiaryListForTeam(CampDate,CampDateToDate,"0","0",DISTLGDCODE, EmpCode,teamId,statusId);
        call.enqueue(new Callback<CommonBeneficiaryListModel>() {
            @Override
            public void onResponse(Call<CommonBeneficiaryListModel> call, Response<CommonBeneficiaryListModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        invoiceList = response.body().getOutput();
                        List<CommonBeneficiaryListModel.Output> invoiceList = response.body().getOutput();
                        CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, invoiceList, CTAndedicineCommonBeneficiaryListActivity.this);
                        binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);

                        toggleFilter();

                    } else {
                        CommonBeneficiaryListAdapter commonBeneficiaryListAdapter = new CommonBeneficiaryListAdapter(CTAndedicineCommonBeneficiaryListActivity.this, new ArrayList<>(), CTAndedicineCommonBeneficiaryListActivity.this);
                        binding.rvPatientList.setAdapter(commonBeneficiaryListAdapter);
                        Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);
                    }
                }else {
                    Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);
                }
            }

            @Override
            public void onFailure(Call<CommonBeneficiaryListModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }
    private void getStatus() {
        final ProgressDialog progressDialog = new ProgressDialog(CTAndedicineCommonBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<CommonBeneficiaryStatusModel> call = apiService.getStatus();
        call.enqueue(new Callback<CommonBeneficiaryStatusModel>() {
            @Override
            public void onResponse(Call<CommonBeneficiaryStatusModel> call, Response<CommonBeneficiaryStatusModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        invoiceList = response.body().getOutput();
                        List<CommonBeneficiaryStatusModel.Output> invoiceList = response.body().getOutput();


                        if (invoiceList.size() > 0) {
                            showMonthTrenchListDialog(invoiceList);
                        }


                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);

                    }
                }else {
                    Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<CommonBeneficiaryStatusModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }
    private void getStatusForTeam() {
        final ProgressDialog progressDialog = new ProgressDialog(CTAndedicineCommonBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<CommonBeneficiaryStatusModel> call = apiService.getStatusForTeam();
        call.enqueue(new Callback<CommonBeneficiaryStatusModel>() {
            @Override
            public void onResponse(Call<CommonBeneficiaryStatusModel> call, Response<CommonBeneficiaryStatusModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
//                        invoiceList = response.body().getOutput();
                        List<CommonBeneficiaryStatusModel.Output> invoiceList = response.body().getOutput();


                        if (invoiceList.size() > 0) {
                            showMonthTrenchListDialog(invoiceList);
                        }


                    } else {

                        Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);

                    }
                }else {
                    Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);

                }
            }

            @Override
            public void onFailure(Call<CommonBeneficiaryStatusModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    private void getMedicineCTDetails(String regno) {
        ProgressDialog progressDialog = new ProgressDialog(CTAndedicineCommonBeneficiaryListActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<CTMedicineDetailsModel> call = apiService.getCTMedicineDetails(regno);

        call.enqueue(new Callback<CTMedicineDetailsModel>() {
            @Override
            public void onResponse(Call<CTMedicineDetailsModel> call, Response<CTMedicineDetailsModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if ("success".equalsIgnoreCase(status)) {
                        List<CTMedicineDetailsModel.Output> ctmedecineList = response.body().getOutput();

                        AlertDialog.Builder builder = new AlertDialog.Builder(CTAndedicineCommonBeneficiaryListActivity.this);
                        View dialogView = LayoutInflater.from(CTAndedicineCommonBeneficiaryListActivity.this)
                                .inflate(R.layout.medicine_and_ct_details, null, false);
                        builder.setView(dialogView);
                        TextView tv_ct_prescription_date = dialogView.findViewById(R.id.tv_ct_prescription_date);
                        TextView tv_medicine_prescription_date = dialogView.findViewById(R.id.tv_medicine_prescription_date);
                        TextView tv_ct_assignment_team = dialogView.findViewById(R.id.tv_ct_assignment_team);
                        TextView tv_assigned_team_text = dialogView.findViewById(R.id.tv_assigned_team_text);
                        TextView tv_medicine_assignment_team = dialogView.findViewById(R.id.tv_medicine_assignment_team);
                        TextView tv_ct_status = dialogView.findViewById(R.id.tv_ct_status);
                        TextView tv_medicine_status = dialogView.findViewById(R.id.tv_medicine_status);
                        TextView tv_address = dialogView.findViewById(R.id.tv_address);

                        if (ctmedecineList != null && !ctmedecineList.isEmpty()) {
                            CTMedicineDetailsModel.Output output = ctmedecineList.get(0);
                            tv_ct_prescription_date.setText(output.getCTPrescribedDate() != null ? output.getCTPrescribedDate() : "N/A");
                            tv_medicine_prescription_date.setText(output.getMDPrescribedDate() != null ? output.getMDPrescribedDate() : "N/A");
                            tv_ct_assignment_team.setText(output.getCTTeamName() != null ? output.getCTTeamName() : "N/A");

                            if (!output.getmDDeliveryExecutiveName().equals("")){
                                tv_medicine_assignment_team.setText(output.getmDDeliveryExecutiveName() != null ? output.getmDDeliveryExecutiveName() : "N/A");
                                tv_assigned_team_text.setText("Delivery Executive");
                            }else {
                                tv_medicine_assignment_team.setText(output.getMDTeamName() != null ? output.getMDTeamName() : "N/A");
                            }


                            tv_ct_status.setText(output.getCTStatus() != null ? output.getCTStatus() : "N/A");
                            tv_medicine_status.setText(output.getMDStatus() != null ? output.getMDStatus() : "N/A");
                            tv_address.setText(output.getAddress() != null ? output.getAddress() : "N/A");
                        }

                        AlertDialog alertDialog = builder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    } else {
                        Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Fail", message, false);
                    }
                } else {
                    Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Fail", "Details Not Found", false);
                }
            }

            @Override
            public void onFailure(Call<CTMedicineDetailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Utilities.showAlertDialog(CTAndedicineCommonBeneficiaryListActivity.this, "Please Try Again", "Server not responding", false);
            }
        });
    }


    private void showMonthTrenchListDialog(final List<CommonBeneficiaryStatusModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(CTAndedicineCommonBeneficiaryListActivity.this);
        builderSingle.setTitle("Select Month");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CTAndedicineCommonBeneficiaryListActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                binding.tvStatus.setText(trenchList.get(which).getStatus());
                statusId = String.valueOf(trenchList.get(which).getType());



                if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("162")||
                        DESGID.equalsIgnoreCase("160")||DESGID.equalsIgnoreCase("136")
                        ||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("172")
                ){
                    getListData();
                }else {
                    getListDataForTeam();
                }

//                getListData();


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


    void toggleFilter() {
        binding.llFilter.setVisibility(binding.llFilter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }



    @Override
    public void onDataClick(CommonBeneficiaryListModel.Output item) {

        getMedicineCTDetails(String.valueOf(item.getRegdid()));


    }
}