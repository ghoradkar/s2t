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
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CampCalendar_Activity;
import com.myhindlab.abkat.activities.re_registration.adapter.RejectDetailsAdminAdapter;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.activities.re_registration.model.CountsForDailyWorkDashboardModel;
import com.myhindlab.abkat.adapters.AreaListSelectAdapter;
import com.myhindlab.abkat.adapters.DistrictListSelectAdapter;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.LabListListSelectAdapter;
import com.myhindlab.abkat.adapters.OrganizationListSelectAdapter;
import com.myhindlab.abkat.adapters.PincodeListSelectAdapter;
import com.myhindlab.abkat.adapters.TalukaListSelectAdapter;
import com.myhindlab.abkat.adapters.doortodoor.AdminActiveInactiveTeamAdapter;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.databinding.ActivityRejectedListDashboardBinding;
import com.myhindlab.abkat.databinding.ActivityRejectedListDashboardTrackingBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RejectedBeneficiaryTrackingActivity extends AppCompatActivity  {
    ActivityRejectedListDashboardTrackingBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<OutputItem> campBeneficiaryList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

   private String CampDate,CampDateToDate,type, toDate;

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
    private String call_statusid = "1", teamId = "0",searchFilterType="0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0",pincodeArray, referenceId, size,areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRejectedListDashboardTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }




    void init() {
        mContext = RejectedBeneficiaryTrackingActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);



        binding.rvDependent.setLayoutManager(new LinearLayoutManager(mContext));


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -10);

        binding.Todate.setText(Utilities.dfDate4.format(new Date()));
        binding.fromDate.setText(Utilities.dfDate4.format(toCal.getTime()));


        CampDate = Utilities.dfDate4.format(toCal.getTime());
        toDate = Utilities.dfDate4.format(new Date());

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
                agentId = json.getString("AgentID");
                LabCode = json.getString("LabCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault(){
        if (getIntent() != null) {
            CampDate = getIntent().getStringExtra("FromDate");
            toDate = getIntent().getStringExtra("ToDate");
            oganizationId = getIntent().getStringExtra("organizationId");
            divisionId = getIntent().getStringExtra("divisionId");
            DISTLGDCODE = getIntent().getStringExtra("distLgd");
            TALLGDCODE = getIntent().getStringExtra("talukaId");
            landingLabId = getIntent().getStringExtra("labcode");
            areaArray = getIntent().getStringExtra("areaArray");
            pincodeArray = getIntent().getStringExtra("pincodeArray");
            searchFilterType = getIntent().getStringExtra("filterType");


            binding.fromDate.setText(CampDate);
            binding.Todate.setText(toDate);



//            if (DESGID.equals("60")){
//
//            }
//
//
//
//            new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");
//


            if (DESGID.equals("51") || DESGID.equals("166") || DESGID.equals("47")||DESGID.equals("83")) {
                new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");

            } else {
//            edt_selectdistrict.setEnabled(false);

                new GetOrganizationNew().execute(EmpCode, DESGID);


            }





//            ConstantData constantData = ConstantData.getInstance();
//            constantData.setCampType(campTypeId);


//            areaArray="[{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":400083,\"Area\":\"Pimpri\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Devale\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Hadsar\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junnar\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junner\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Junnr\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Katede\"},{\"SubOrgId\":3,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":410502,\"Area\":\"Padali\"}]";
//
//            pincodeArray = "[{\"SubOrgId\":0,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":\"400083\"},{\"SubOrgId\":0,\"DIVID\":2,\"DISTLGDCODE\":490,\"TALLGDCODE\":4187,\"Pincode\":\"410502\"}]";


//            if (DESGID.equals("86") || DESGID.equals("64") || DESGID.equals("35") || DESGID.equals("129") || DESGID.equals("146")) {
////                new GetCountDataForTeam().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "0", "0", EmpCode, "2");
//
//                type = "0";
//                getCountForTeam(
//                        areaArray,
//                        pincodeArray
//                );
//
//                binding.status.setText("All");
//
//
//            } else {
////                new GetCountData().execute(fromDate, toDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, areaArray, pincodeArray, "2", "0", EmpCode, "2");
//
//
//                type = "2";
//                getCount(
//                        areaArray,
//                        pincodeArray
//                );
//
//                binding.status.setText("Assignment Pending");
//
//            }


        }

    }
    private void eventListener() {


//        binding.llMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });



        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.fromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                                binding.Todate.setText("");

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
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
                                    binding.Todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                    toDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


                                    new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");



                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate4.parse(CampDate));
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
        Toolbar toolbar = findViewById(R.id.toolbar);
//        Toolbar toolbar = binding.toolbar; // Assuming your layout has a Toolbar with id 'toolbar'
        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);


        // Now set the title
        setSupportActionBar(toolbar);

// Disable the default toolbar title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

// Create a custom TextView for the title
        TextView titleTextView = new TextView(this);

// Set the text, size, color, and other properties
        titleTextView.setText("Rejected Beneficiary Tracking");
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);  // Set size in SP
        titleTextView.setTextColor(Color.WHITE);                    // Set text color
//        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);            // Optional, bold text

// Set layout params to ensure it displays properly
//        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(
//                Toolbar.LayoutParams.WRAP_CONTENT,
//                Toolbar.LayoutParams.WRAP_CONTENT,
////                Gravity.CENTER_HORIZONTAL);  // Align the title in the center
//        titleTextView.setLayoutParams(layoutParams);

// Add the custom TextView to the Toolbar
        toolbar.addView(titleTextView);
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RejectedBeneficiaryTrackingActivity.this);
                bottomSheetDialog.setContentView(R.layout.list_row_filter);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// You can set the layout parameters for the bottom sheet dialog here
                bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView imv_CloseDialog = bottomSheetDialog.findViewById(R.id.imv_CloseDialog);
                RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
                TextView tvDate = bottomSheetDialog.findViewById(R.id.tvDate);
                TextView tv_division = bottomSheetDialog.findViewById(R.id.tv_division);
                TextView tv_district = bottomSheetDialog.findViewById(R.id.tv_district);
                TextView tv_taluka = bottomSheetDialog.findViewById(R.id.tv_taluka);
                TextView tv_lab = bottomSheetDialog.findViewById(R.id.tv_lab);
                TextView fromDate = bottomSheetDialog.findViewById(R.id.fromDate);
                TextView tv_area = bottomSheetDialog.findViewById(R.id.tv_area);
                TextView ToDate = bottomSheetDialog.findViewById(R.id.ToDate);
                TextView tv_pincode = bottomSheetDialog.findViewById(R.id.tv_pincode);
                TextView tv_orgnization = bottomSheetDialog.findViewById(R.id.tv_orgnization);
                LinearLayout mainLLDate = bottomSheetDialog.findViewById(R.id.mainLLDate);
                LinearLayout mainLLToDate = bottomSheetDialog.findViewById(R.id.mainLLToDate);
                LinearLayout llmain_fromDate = bottomSheetDialog.findViewById(R.id.llmain_fromDate);
                LinearLayout llmain_toDate = bottomSheetDialog.findViewById(R.id.llmain_toDate);
                Button btnApply = bottomSheetDialog.findViewById(R.id.btnApply);
                Button btnClearAll = bottomSheetDialog.findViewById(R.id.btnClearAll);
                CheckBox selectAllCheckbox = bottomSheetDialog.findViewById(R.id.selectAllCheckbox);

                tv_area.setVisibility(View.GONE);
                tv_pincode.setVisibility(View.GONE);
                tv_lab.setVisibility(View.GONE);
                tv_taluka.setVisibility(View.GONE);
                llmain_fromDate.setVisibility(View.GONE);
                llmain_toDate.setVisibility(View.GONE);
                tvDate.setVisibility(View.GONE);

                selectAllCheckbox.setVisibility(View.GONE);

                fromDate.setText(CampDate);
                ToDate.setText(toDate);

                tvDate.setBackgroundColor(Color.WHITE); // Set the background color to red

//                Calendar cal = Calendar.getInstance();
//                mYear = cal.get(Calendar.YEAR);
//                mMonth = cal.get(Calendar.MONTH);
//                mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//
////        Calendar todayCal = Calendar.getInstance();
////        todayCal.add(Calendar.DAY_OF_MONTH, -22);
////
//                Calendar toCal = Calendar.getInstance();
//                toCal.add(Calendar.DAY_OF_MONTH, -3);
//
//                fromDate.setText(Utilities.dfDate4.format(new Date()));
//                ToDate.setText(Utilities.dfDate4.format(toCal.getTime()));
//
//
//                CampDate = Utilities.dfDate4.format(toCal.getTime());
//                CampDateToDate = Utilities.dfDate4.format(new Date());
//

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


                btnApply.setBackgroundColor(Color.parseColor("#002D62"));
                btnClearAll.setBackgroundColor(Color.parseColor("#C44240"));


                btnClearAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (divisionList!=null){
                            divisionList.clear();

                        }

                        if (organizationList!=null){
                            organizationList.clear();

                        }
                        if (districtList!=null){
                            districtList.clear();

                        }

                        if (talukaList!=null){
                            talukaList.clear();

                        }

                        if (landingLabList!=null){
                            landingLabList.clear();

                        }
                        if (areaList!=null){
                            areaList.clear();

                        }

                        if (pincodeList!=null){
                            pincodeList.clear();

                        }

                        divisionId = "0";
                        oganizationId = "0";
                        DISTLGDCODE = "0";
                        TALLGDCODE = "0";
                        landingLabId = "0";

                        searchFilterType = "0";


                        fromDate.setText("");
                        ToDate.setText("");
                        CampDate = "";
                        CampDateToDate = "";



//                        if (phleboDetailsModel.getOutput() != null) {
//                            phleboDetailsModel.getOutput().clear();
//                            phleboListDetailsAdapter.notifyDataSetChanged();
//
//                        }

                        recyclerView.setVisibility(View.GONE);
                        selectAllCheckbox.setVisibility(View.GONE);
                        PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(pincodeListSelectAdapter);

                        DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(divisionListSelectAdapter);

                        OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(organizationListSelectAdapter);

                        DivisionListSelectAdapter divisionListSelectAdapter1 = new DivisionListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(divisionListSelectAdapter1);

                        TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(talukaListSelectAdapter);

                        LabListAdapter labListAdapter = new LabListAdapter(new ArrayList<>());
                        recyclerView.setAdapter(labListAdapter);


                        AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(new ArrayList<>());
                        recyclerView.setAdapter(areaListSelectAdapter);







                    }


                });

                imv_CloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });


                tv_district.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        JsonArray organizationArray = new JsonArray();

                        if (organizationList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                            for (SubOrganizationModel.Output t :
                                    organizationList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("organization", t.getSubOrgId());

                                    oganizationId = String.valueOf(t.getSubOrgId());
                                    organizationArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }


                        JsonArray divisionArray = new JsonArray();

                        if (divisionList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (SubDivisionModel.Output t :
                                    divisionList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("division", t.getDivid());

                                    divisionId = String.valueOf(t.getDivid());
                                    divisionArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }

                        new GetDistrictList(recyclerView).execute(oganizationId, EmpCode, DESGID, divisionId, "0");

                        if (districtList == null || districtList.isEmpty()) {


//                            if (divisionId.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
//                                return;
//                            }

                            new GetDistrictList(recyclerView).execute(oganizationId, EmpCode, DESGID, divisionId, "0");

                        } else {

                            DistrictListSelectAdapter districtListSelectAdapter1 = new DistrictListSelectAdapter(districtList);
                            recyclerView.setAdapter(districtListSelectAdapter1);
                        }


//                        new GetDistrictList(recyclerView).execute(STATELGDCODE, "0");
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);

                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red+
                        selectAllCheckbox.setVisibility(View.GONE);


                    }
                });
                tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.VISIBLE);
                        mainLLToDate.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                        tvDate.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);


                    }
                });


                tv_orgnization.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        if (organizationList == null || organizationList.isEmpty()) {
                            new GetOrganization(recyclerView).execute(EmpCode, DESGID);

                        } else {

                            OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(organizationList);
                            recyclerView.setAdapter(organizationListSelectAdapter);
                        }


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.WHITE); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);

                    }
                });

                tv_division.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        JsonArray organizationArray = new JsonArray();

                        if (organizationList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                            for (SubOrganizationModel.Output t :
                                    organizationList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("organization", t.getSubOrgId());

                                    oganizationId = String.valueOf(t.getSubOrgId());
                                    organizationArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }

                        if (divisionList == null || divisionList.isEmpty()) {

//                            if (oganizationId.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select organization", false);
//                                return;
//                            }

                            new GetDivision(recyclerView).execute(oganizationId, EmpCode, DESGID);

                        } else {

                            DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(divisionList);
                            recyclerView.setAdapter(divisionListSelectAdapter);
                        }


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);


                    }
                });

                tv_lab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        JsonArray districtJsonArray = new JsonArray();

                        if (districtList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                            for (DistrictOrgModel.Output t :
                                    districtList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("district", t.getDistlgdcode());

                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
                                    districtJsonArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }

                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        if (landingLabList == null || landingLabList.isEmpty()) {
                            new GetLandingLab(recyclerView).execute(DISTLGDCODE);

                        } else {

                            LabListListSelectAdapter labListListSelectAdapter = new LabListListSelectAdapter(landingLabList);
                            recyclerView.setAdapter(labListListSelectAdapter);

                        }


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);

                    }
                });
                tv_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);


                        if (areaList == null || areaList.isEmpty()) {


                            JsonArray districtJsonArray = new JsonArray();

                            if (districtList != null) {
                                //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                                for (DistrictOrgModel.Output t :
                                        districtList) {
                                    if (t.isChecked()) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("district", t.getDistlgdcode());

                                        DISTLGDCODE = String.valueOf(t.getDistlgdcode());
                                        districtJsonArray.add(jsonObject);
                                        //  doctorSelected += 1;
                                    }
                                }
                            }

                            JsonArray divisionArray = new JsonArray();

                            if (divisionList != null) {
                                //  Utilities.showToastMessage("Please select Doctor", context, false);

                                for (SubDivisionModel.Output t :
                                        divisionList) {
                                    if (t.isChecked()) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("division", t.getDivid());

                                        divisionId = String.valueOf(t.getDivid());
                                        divisionArray.add(jsonObject);
                                        //  doctorSelected += 1;
                                    }
                                }
                            }


                            JsonArray organizationArray = new JsonArray();

                            if (organizationList != null) {
                                //  Utilities.showToastMessage("Please select Doctor", context, false);

                                for (SubOrganizationModel.Output t :
                                        organizationList) {
                                    if (t.isChecked()) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("organization", t.getSubOrgId());

                                        oganizationId = String.valueOf(t.getSubOrgId());
                                        organizationArray.add(jsonObject);
                                        //  doctorSelected += 1;
                                    }
                                }
                            }


                            JsonArray talukaArray = new JsonArray();

                            if (talukaList != null) {
                                //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                                for (TalukaListForFilterModel.Output t :
                                        talukaList) {
                                    if (t.isChecked()) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("talika", t.getTallgdcode());

                                        TALLGDCODE = String.valueOf(t.getTallgdcode());
                                        talukaArray.add(jsonObject);
                                        //  doctorSelected += 1;

                                    }
                                }
                            }


//                            if (TALLGDCODE.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select taluka", false);
//                                return;
//                            }

//                            if (divisionId.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
//                                return;
//                            }

                            new GetAreaList(recyclerView).execute(oganizationId, divisionId, DISTLGDCODE, TALLGDCODE);

                        } else {

                            AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(areaList);
                            recyclerView.setAdapter(areaListSelectAdapter);
                        }

                    }
                });

                tv_taluka.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        JsonArray districtJsonArray = new JsonArray();

                        if (districtList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                            for (DistrictOrgModel.Output t :
                                    districtList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("district", t.getDistlgdcode());

                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
                                    districtJsonArray.add(jsonObject);
                                    //  doctorSelected += 1;


                                }
                            }

                        }

                        if (talukaList == null || talukaList.isEmpty()) {
                            new GetTaluka(recyclerView).execute(EmpCode, DISTLGDCODE);

                        } else {

                            TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(talukaList);
                            recyclerView.setAdapter(talukaListSelectAdapter);
                        }


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.GONE);


                    }
                });

                selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {


                        if (selectAllCheckbox.isChecked()) {
                            if (pincodeList != null) {

                                for (PincodeListModel.Output output :
                                        pincodeList) {
                                    output.setChecked(true);

                                }

                                PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
                                recyclerView.setAdapter(pincodeListSelectAdapter);


                            }

                        } else {

                            if (pincodeList != null) {

                                for (PincodeListModel.Output output :
                                        pincodeList) {
                                    output.setChecked(false);

                                }

                                PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
                                recyclerView.setAdapter(pincodeListSelectAdapter);



                            }

                        }


                    }
                });
                tv_pincode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainLLDate.setVisibility(View.GONE);
                        mainLLToDate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);


                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_pincode.setBackgroundColor(Color.WHITE); // Set the background color to red
                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
                        selectAllCheckbox.setVisibility(View.VISIBLE);



                        JsonArray divisionArray = new JsonArray();

                        if (divisionList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (SubDivisionModel.Output t :
                                    divisionList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("division", t.getDivid());

                                    divisionId = String.valueOf(t.getDivid());
                                    divisionArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }


                        JsonArray areaArray = new JsonArray();

                        if (areaList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (AreaListModel.Output t :
                                    areaList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("DIVID", t.getDividop());
                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
                                    jsonObject.addProperty("Pincode", t.getPincode());
                                    jsonObject.addProperty("Area", t.getArea());

                                    areaArray.add(jsonObject);
                                    //  doctorSelected += 1;


                                }
                            }

//                            new GetPincodeList(recyclerView).execute(areaArray.toString());

                            getPincode(areaArray.toString(), recyclerView);

                        }

                        if (pincodeList == null || pincodeList.isEmpty()) {

                            if (divisionId.equals("0")) {
                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
                                return;
                            }

                            if (areaArray.toString().isEmpty()) {
                                Utilities.showAlertDialog(mContext, "Alert", "Please select area", false);
                                return;
                            }

//                            new GetPincodeList(recyclerView).execute(areaArray.toString());

                            getPincode(areaArray.toString(), recyclerView);


                        } else {

                            PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
                            recyclerView.setAdapter(pincodeListSelectAdapter);
                        }

                    }
                });


                fromDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        fromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                        CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


//                                    if (Utilities.isNetworkAvailable(context)) {
//                                        new PacketCollectionActivity.GetPostCampDetails().execute(
//                                                DISTLGDCODE,
//                                                fromDate,
//                                                CampDateToDate,
//                                                landinglabId,
//                                                EmpCode
//                                        );
//                                    }


//                                    tv_teamNumber.setText("");
//                                    tv_district.setText("");
//                                    tv_lab.setText("");


                                    }

                                }, mYear, mMonth, mDay);

                        Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate4.parse(CampDate));
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

                });


                ToDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (fromDate.getText().toString().equals(""))
                            Utilities.showMessageString("Please Select From Date", mContext);
                        else {

                            DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            ToDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                            CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


//                                    if (Utilities.isNetworkAvailable(context)) {
//                                        new PacketCollectionActivity.GetPostCampDetails().execute(
//                                                DISTLGDCODE,
//                                                fromDate,
//                                                CampDateToDate,
//                                                landinglabId,
//                                                EmpCode
//                                        );
//                                    }


//                                    tv_teamNumber.setText("");
//                                    tv_district.setText("");
//                                    tv_lab.setText("");


                                        }

                                    }, mYear, mMonth, mDay);

                            Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate4.parse(CampDate));
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

                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bottomSheetDialog.dismiss();

                        JsonArray districtJsonArray = new JsonArray();

                        if (districtList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            int districtSelected = 0;
                            for (DistrictOrgModel.Output t :
                                    districtList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("district", t.getDistlgdcode());

                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
                                    districtJsonArray.add(jsonObject);
                                    districtSelected += 1;
                                }
                            }
//                            if (districtSelected == 0) {
//                                Utilities.showToastMessage("Please select district", mContext, false);
//                                return;
//                            }

                        }


                        JsonArray labJsonArray = new JsonArray();

                        if (landingLabList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                            for (LandingLabModel.Output t :
                                    landingLabList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("lab", t.getLabCode());

                                    landingLabId = String.valueOf(t.getLabCode());
                                    labJsonArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }

                        JsonArray divisionArray = new JsonArray();

                        if (divisionList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (SubDivisionModel.Output t :
                                    divisionList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("division", t.getDivid());

                                    divisionId = String.valueOf(t.getDivid());
                                    divisionArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }


                        JsonArray organizationArray = new JsonArray();

                        if (organizationList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (SubOrganizationModel.Output t :
                                    organizationList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("organization", t.getSubOrgId());

                                    oganizationId = String.valueOf(t.getSubOrgId());
                                    organizationArray.add(jsonObject);
                                    //  doctorSelected += 1;
                                }
                            }
                        }


                        JsonArray talukaArray = new JsonArray();

                        if (talukaList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            for (TalukaListForFilterModel.Output t :
                                    talukaList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("talUka", t.getTallgdcode());

                                    TALLGDCODE = String.valueOf(t.getTallgdcode());
                                    talukaArray.add(jsonObject);

                                }
                            }
                        }


                        JsonArray pincodeArray = new JsonArray();

                        if (pincodeList != null) {

                            int pincodeSelected = 0;
                            for (PincodeListModel.Output t :
                                    pincodeList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("SubOrgId", t.getSubOrgId());
                                    jsonObject.addProperty("DIVID", t.getDivid());
                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
                                    jsonObject.addProperty("Pincode", t.getPincode());

                                    pincodeArray.add(jsonObject);
                                    pincodeSelected += 1;

                                }
                            }
                            if (pincodeSelected == 0) {
                                Utilities.showToastMessage("Please select pincode", mContext, false);
                                return;

                            }

                        }


                        JsonArray areaArray = new JsonArray();

                        if (areaList != null) {
                            //  Utilities.showToastMessage("Please select Doctor", context, false);

                            int areaSelected = 0;
                            for (AreaListModel.Output t :
                                    areaList) {
                                if (t.isChecked()) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("SubOrgId", t.getSubOrgId());
                                    jsonObject.addProperty("DIVID", t.getDividop());
                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
                                    jsonObject.addProperty("Pincode", t.getPincode());
                                    jsonObject.addProperty("Area", t.getArea());

                                    areaArray.add(jsonObject);
                                    areaSelected += 1;


                                }
                            }

                            if (areaSelected == 0) {
                                Utilities.showToastMessage("Please select Area", mContext, false);
                                return;

                            }

                        }




//                        getCount(
//                                areaArray.toString(),
//                                pincodeArray.toString()
//                        );

                        new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");


                    }
                });


            }
        });

    }

    private void getReportlist() {
        final ProgressDialog progressDialog = new ProgressDialog(RejectedBeneficiaryTrackingActivity.this);
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
//                        campBeneficiaryList = response.body().getOutput();
//                        List<OutputItem> reportdatalist = response.body().getOutput();
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, reportdatalist, RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
//
//                    } else {
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(RejectedBeneficiaryListActivity.this, new ArrayList<>(), RejectedBeneficiaryListActivity.this);
////                        binding.rvReportlist.setAdapter(callListAdaptor);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(RejectedBeneficiaryTrackingActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RejectedBeneficiaryTrackingActivity.this, R.layout.list_row);
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







//    private class BeneficiaryListAdapter extends RecyclerView.Adapter<BeneficiaryListAdapter.MyViewHolder> {
//
//        @NonNull
//        @Override
//        public BeneficiaryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.list_row_reject_list, parent, false);
//            return new BeneficiaryListAdapter.MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull BeneficiaryListAdapter.MyViewHolder holder, int pos) {
//            final int position = holder.getAdapterPosition();
//
//            holder.cb_select.setText(resourceListModel.getOutput().get(position).getResourceName());
//
//            if (resourceListModel.getOutput().get(position).isChecked()) {
//                holder.cb_select.setChecked(true);
//            }
//
//            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                resourceListModel.getOutput().get(position).setChecked(isChecked);
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return resourceListModel.getOutput().size();
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//
//            private CheckBox cb_select;
//            private TextView benName,tv_relation_with_worker,tv_pincode,tv_area,tv_address,member1,tv_member2,tv_select_team,
//                    tv_campId,campType,campDate,tv_receivedAt,tv_rejected_date,Reject_Reason;
//            private Button btn_submit;
//
//            public MyViewHolder(@NonNull View view) {
//                super(view);
//                cb_select = view.findViewById(R.id.cb_select);
//                benName = view.findViewById(R.id.benName);
//                tv_relation_with_worker = view.findViewById(R.id.tv_relation_with_worker);
//                tv_pincode = view.findViewById(R.id.tv_pincode);
//                tv_area = view.findViewById(R.id.tv_area);
//                tv_address = view.findViewById(R.id.tv_address);
//                tv_campId = view.findViewById(R.id.tv_campId);
//                campType = view.findViewById(R.id.campType);
//                campDate = view.findViewById(R.id.campDate);
//                tv_receivedAt = view.findViewById(R.id.tv_receivedAt);
//                tv_rejected_date = view.findViewById(R.id.tv_rejected_date);
//                Reject_Reason = view.findViewById(R.id.Reject_Reason);
//                member1 = view.findViewById(R.id.member1);
//                tv_member2 = view.findViewById(R.id.tv_member2);
//                tv_select_team = view.findViewById(R.id.tv_select_team);
//                btn_submit = view.findViewById(R.id.btn_submit);
//            }
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return position;
//        }
//    }



    public class GetAdminData extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("Arid", params[5]));
            param.add(new ParamsPojo("UserId", params[6]));
            param.add(new ParamsPojo("DESGID", params[7]));
            param.add(new ParamsPojo("Type", params[8]));
            res = WebServiceCall.APICall(ApplicationConstants.GetRecollectionBeneficiaryDashboardForMob, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    BeneficiaryCountForPageLoadModel adminActiveInactiveModel = new Gson().fromJson(result, BeneficiaryCountForPageLoadModel.class);
                    type = adminActiveInactiveModel.getStatus();
                    message = adminActiveInactiveModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = adminActiveInactiveModel.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, adminlist));




//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }

                        } else {

                           binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {

                        binding.rvDependent.setAdapter(new RejectDetailsAdminAdapter(mContext, new ArrayList<>()));


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


    public class GetDistrictList extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetDistrictList(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDistrict, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    districtList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

//                            showDistrictListDialog(districtList);


                            DistrictListSelectAdapter districtListSelectAdapter1 = new DistrictListSelectAdapter(districtList);
                            recyclerView.setAdapter(districtListSelectAdapter1);

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
//                                edt_organization.setText(output.getSubOrgName());


                                new GetAdminData().execute(CampDate,toDate,oganizationId,divisionId,DISTLGDCODE,"0",EmpCode,DESGID,"1");

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



    private void getPincode(String area, RecyclerView recyclerView) {
        // Get the API interface from the ApiClient
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        // Make the API call asynchronously
        apiInterface.getPincodeList(area).enqueue(new Callback<PincodeListModel>() {
            @Override
            public void onResponse(Call<PincodeListModel> call, Response<PincodeListModel> response) {
                // Check if the response is successful
                if (response.isSuccessful() && response.body() != null) {
                    // Assign the response to pojoDetails
                    PincodeListModel pojoDetails = response.body();

                    // Check if the response status is "success"
                    if (pojoDetails.getStatus().equalsIgnoreCase("success")) {
                        // Update the global pincodeList variable
                        pincodeList = pojoDetails.getOutput();

//                        PincodeListModel.Output selectAll = new PincodeListModel.Output();
//                        selectAll.setPincode("Select All");
//                        selectAll.setChecked(false); // Initially, "Select All" is not selected
//                        pincodeList.add(0, selectAll); // Add at the beginning


                        // Check if pincodeList has data
                        if (pincodeList != null && pincodeList.size() > 0) {
                            // Update the RecyclerView adapter with the new data



                            PincodeListSelectAdapter adapter = new PincodeListSelectAdapter(pincodeList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            // Handle empty list case
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        // Handle non-success response status
                        Utilities.showAlertDialog(mContext, "Fail", pojoDetails.getMessage(), false);
                    }
                } else {
                    // Handle unsuccessful response or null body
                    Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
                }
            }

            @Override
            public void onFailure(Call<PincodeListModel> call, Throwable t) {
                // Handle failure of the API call
                t.printStackTrace(); // Log the error
                Utilities.showAlertDialog(mContext, "Error", t.getLocalizedMessage(), false);
            }
        });
    }


    public class GetLandingLab extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetLandingLab(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));

            res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            //  res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    landingLabList = new ArrayList<>();

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        landingLabList = landingLabModel.getOutput();

                        if (landingLabList.size() > 0) {

                            LabListListSelectAdapter labListListSelectAdapter = new LabListListSelectAdapter(landingLabList);
                            recyclerView.setAdapter(labListListSelectAdapter);

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

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server not responding", false);
            }
        }

//        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
//            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//            builderSingle.setTitle("Select Lab");
//            builderSingle.setCancelable(false);
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//            for (int i = 0; i < landinglablist.size(); i++) {
//                arrayAdapter.add(String.valueOf(landinglablist.get(i).getLabName()));
//            }
//
//            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    tv_lab.setText(landinglablist.get(which).getLabName());
//                    landinglabId = String.valueOf(landinglablist.get(which).getLabCode());
//
//
//                    if (Utilities.isNetworkAvailable(context)) {
//                        new PacketCollectionActivity.GetPostCampDetails().execute(
//                                DISTLGDCODE,
//                                fromDate,
//                                CampDateToDate,
//                                landinglabId,
//                                EmpCode
//
//                        );
//                    }
//
//
//
//
//                    tv_teamNumber.setText("");
//
//
////                    new GetCamName().execute();
//                }
//            });
//            builderSingle.show();
//
//        }
    }


    public class GetOrganization extends AsyncTask<String, Void, String> {


        RecyclerView recyclerView;

        public GetOrganization(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
                    organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {


                            OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(organizationList);
                            recyclerView.setAdapter(organizationListSelectAdapter);


//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

//                            if (pojoDetails.getOutput().size() > 0) {
//                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);
//
//                                organizationId = String.valueOf(output.getSubOrgId());
//
//
//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }
//
//
//                            }


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

    public class GetDivision extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetDivision(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDivision, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

//                            showDivisionListDialog(divisionList);

                            DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(divisionList);
                            recyclerView.setAdapter(divisionListSelectAdapter);
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

    public class GetTaluka extends AsyncTask<String, Void, String> {


        RecyclerView recyclerView;

        public GetTaluka(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserMappedTaluka, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    talukaList = new ArrayList<>();
                    TalukaListForFilterModel talukaModel = new Gson().fromJson(result, TalukaListForFilterModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        talukaList = talukaModel.getOutput();
                        if (talukaList.size() > 0) {
//

                            TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(talukaList);
                            recyclerView.setAdapter(talukaListSelectAdapter);

//                            showTalukaDialogue(camptypelist);
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

//        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
//            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
//            builderSingle.setTitle("Select Taluka");
//            builderSingle.setCancelable(false);
//
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);
//
//            for (int i = 0; i < talukalist.size(); i++) {
//                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
//            }
//
//            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    tv_taluka.setText(talukalist.get(which).gettALNAME());
//                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());
//
//                    new AcceptMedicinePacketActivity.GetPostCampDetails().execute(
//                            fromDate,
//                            CampDateToDate,
//                            landinglabId,
//                            talukaId,
//                            EmpCode
//                    );
//
//                    //  refreshCalendar();
//                }
//            });
//            builderSingle.show();
//
//        }

    }


    public class GetAreaList extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public GetAreaList(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
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
            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("DIVID", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("TALLGDCODE", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetArea, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    areaList = new ArrayList<>();
                    AreaListModel pojoDetails = new Gson().fromJson(result, AreaListModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        areaList = pojoDetails.getOutput();
                        if (areaList.size() > 0) {
//                              districtList.add(0, new DistrictList_Model("0", "All"));

//                            showDistrictListDialog(districtList);


                            AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(areaList);
                            recyclerView.setAdapter(areaListSelectAdapter);

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



}