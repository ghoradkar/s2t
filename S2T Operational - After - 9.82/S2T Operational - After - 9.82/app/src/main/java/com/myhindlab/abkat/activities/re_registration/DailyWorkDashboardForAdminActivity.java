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
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.re_registration.model.CountsForDailyWorkDashboardModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.FilterDataModel;
import com.myhindlab.abkat.adapters.AreaListSelectAdapter;
import com.myhindlab.abkat.adapters.DistrictListSelectAdapter;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.LabListAdapter;
import com.myhindlab.abkat.adapters.LabListListSelectAdapter;
import com.myhindlab.abkat.adapters.OrganizationListSelectAdapter;
import com.myhindlab.abkat.adapters.PincodeListSelectAdapter;
import com.myhindlab.abkat.adapters.TalukaListSelectAdapter;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.appointment_confirmation.adapters.CallListAdaptor;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.databinding.ActivityDailyWorkDashboardBinding;
import com.myhindlab.abkat.databinding.ActivityDailyWorkDashboardForAdminBinding;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
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


public class DailyWorkDashboardForAdminActivity extends AppCompatActivity implements CallListAdaptor.Onclicklisner {
    ActivityDailyWorkDashboardForAdminBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private FilterDataModel filterList;

    private List<OutputItem> campBeneficiaryList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;

    private DistrictListSelectAdapter districtListSelectAdapter;

    private List<CountsForDailyWorkDashboardModel.Output> countList;


    private String fromDate, fromFilterDate, areaArray, pincodeArray, CampDateFilterToDate, CampDateToDate, CampDate, toDate;


    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private String call_statusid = "1",searchFilterType="0", teamId = "0", landingLabId = "0",
            mobileNumber, EmpCode, divisionId = "0", referenceId, size, taluka, STATELGDCODE = "2",campType="0",
            DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyWorkDashboardForAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = DailyWorkDashboardForAdminActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);



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
        CampDateToDate = Utilities.dfDate4.format(new Date());

    }


    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                district = json.getString("district");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                agentId = json.getString("AgentID");
                LabCode = json.getString("LabCode");
//                TALLGDCODE = json.getString("TALLGDCODE");
//                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {


        if (binding.rbAbkat.isChecked()) {
            campType = "1";
        } else if (binding.rbDoorToDoor.isChecked()) {
            campType = "3";
        }


        if (DESGID.equalsIgnoreCase("170") || (DESGID.equalsIgnoreCase("171") || (DESGID.equalsIgnoreCase("182")
                || (DESGID.equalsIgnoreCase("183"))))){
            binding.edtLab.setVisibility(View.VISIBLE);

        }


        if (DESGID.equalsIgnoreCase("173")){
                    new GetOrganizationNew().execute(EmpCode, DESGID);

        }else {




            getCountForPageload(
                    "0",
                    "0"
            );

        }
    }

    private void eventListener() {



        binding.edtOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrganizationNewFilter().execute(EmpCode, DESGID);
            }
        });


        binding.edtDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtDivision.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(mContext, "Alert", "Please select sub organization", false);
                    return;
                }
                new GetDivisionNew().execute(oganizationId, EmpCode, DESGID);
            }
        });


//        binding.edtTaluka.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (binding.edtSelectdistrict.getText().toString().isEmpty()) {
//                    Utilities.showAlertDialog(mContext, "Alert", "Please select district", false);
//                    return;
//                }
//
//                if (DISTLGDCODE.equals("0")){
//                    Utilities.showAlertDialog(mContext, "Alert", "Please select district", false);
//                    return;
//                }
//
//
//                new GetTalukaNew().execute("2", DISTLGDCODE);
//
//            }
//        });


        binding.edtLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DISTLGDCODE.equals("0")){
                    Utilities.showAlertDialog(mContext,"Alert","Please select district",false);
                    return;
                }


                if (Utilities.isNetworkAvailable(mContext))
                    new GetLandingLabNew().execute(DISTLGDCODE);
                else {
                    Utilities.showToastMessage("Please Check your internet connection", mContext, false);
                }
            }
        });


        binding.edtSelectdistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (DESGID.equals("51") || DESGID.equals("166") || DESGID.equals("47")||DESGID.equals("83")) {

                    if (Utilities.isNetworkAvailable(mContext)) {
                        new GetDistrictListFor().execute(oganizationId,EmpCode,DESGID,divisionId,"0");
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);
                    }
                } else {

                    if (binding.edtOrganization.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select sub organization", false);
                        return;
                    }
                    if (binding.edtDivision.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
                        return;
                    }

                    if (Utilities.isNetworkAvailable(mContext)) {
                        new GetDistrictListFor().execute(oganizationId,EmpCode,DESGID,divisionId,"0");
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);
                    }
                }


            }
        });


        binding.rbAbkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campType = "1";

                if (DESGID.equalsIgnoreCase("173")){
                    new GetOrganizationNew().execute(EmpCode, DESGID);

                }else {

                    binding.tvAssigned.setText("0");
                    binding.interestedInScreening.setText("0");
                    binding.tvNotScreening.setText("0");
                    binding.tvDeniedForScreening.setText("0");
                    binding.reScreenedBeneficiary.setText("0");
                    binding.reScreenedPendingCard.setText("0");
                    binding.assignedBene.setText("0");
                    binding.tvUnassigned.setText("0");
                    binding.rejectedBeneficiary.setText("0");
                    binding.tvDeniedFor.setText("0");


                    getCountForPageload(
                            "0",
                            "0"
                    );

                }


            }
        });

        binding.rbDoorToDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                campType = "3";

                if (DESGID.equalsIgnoreCase("173")){
                    new GetOrganizationNew().execute(EmpCode, DESGID);

                }else {



                    binding.tvAssigned.setText("0");
                    binding.interestedInScreening.setText("0");
                    binding.tvNotScreening.setText("0");
                    binding.tvDeniedForScreening.setText("0");
                    binding.reScreenedBeneficiary.setText("0");
                    binding.reScreenedPendingCard.setText("0");
                    binding.assignedBene.setText("0");
                    binding.tvUnassigned.setText("0");
                    binding.rejectedBeneficiary.setText("0");
                    binding.tvDeniedFor.setText("0");

                    getCountForPageload(
                            "0",
                            "0"
                    );
                }
            }
        });

        binding.rejectedBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JsonArray areaArray = new JsonArray();

                if (areaList != null) {
                    //  Utilities.showToastMessage("Please select Doctor", context, false);

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
                            //  doctorSelected += 1;


                        }
                    }

//                    new GetPincodeList(recyclerView).execute(areaArray.toString());

                }


                JsonArray pincodeArray = new JsonArray();

                if (pincodeList != null) {
                    //  Utilities.showToastMessage("Please select Doctor", context, false);

//                    int doctorSelected = 0;
                    for (PincodeListModel.Output t :
                            pincodeList) {
                        if (t.isChecked()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("SubOrgId", t.getSubOrgId());
                            jsonObject.addProperty("DIVID", t.getDivid());
                            jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
                            jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
                            jsonObject.addProperty("Pincode", t.getPincode());

//                                    TALLGDCODE = String.valueOf(t.getTallgdcode());
                            pincodeArray.add(jsonObject);
                            //  doctorSelected += 1;

                        }
                    }
                }

                startActivity(new Intent(mContext, RejectedBeneficiaryTrackingActivity.class)
                        .putExtra("FromDate", CampDate)
                        .putExtra("ToDate", CampDateToDate)
                        .putExtra("organizationId", oganizationId)
                        .putExtra("divisionId", divisionId)
                        .putExtra("distLgd", DISTLGDCODE)
                        .putExtra("talukaId", TALLGDCODE)
                        .putExtra("labcode", landingLabId)
                        .putExtra("areaArray", areaArray.toString())
                        .putExtra("pincodeArray", pincodeArray.toString())
                        .putExtra("filterType", searchFilterType)

                );

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

                                    CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);





                                    binding.tvAssigned.setText("0");
                                    binding.interestedInScreening.setText("0");
                                    binding.tvNotScreening.setText("0");
                                    binding.tvDeniedForScreening.setText("0");
                                    binding.reScreenedBeneficiary.setText("0");
                                    binding.reScreenedPendingCard.setText("0");
                                    binding.assignedBene.setText("0");
                                    binding.tvUnassigned.setText("0");
                                    binding.rejectedBeneficiary.setText("0");
                                    binding.tvDeniedFor.setText("0");

//                                    DISTLGDCODE ="0";
//                                    landingLabId ="0";
//                                    TALLGDCODE ="0";
//                                    divisionId ="0";
                                    getCountForPageload(
                                            "0",
                                            "0"
                                    );


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
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);


        // Now set the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daily Work Dashboard");
        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainLlFilter.setVisibility(binding.mainLlFilter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });

//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DailyWorkDashboardForAdminActivity.this);
//                bottomSheetDialog.setContentView(R.layout.list_row_filter);
//                bottomSheetDialog.setCanceledOnTouchOutside(true);
//                bottomSheetDialog.show();
//                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//// You can set the layout parameters for the bottom sheet dialog here
//                bottomSheetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                ImageView imv_CloseDialog = bottomSheetDialog.findViewById(R.id.imv_CloseDialog);
//                RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.recyclerView);
//                TextView tvDate = bottomSheetDialog.findViewById(R.id.tvDate);
//                TextView tv_division = bottomSheetDialog.findViewById(R.id.tv_division);
//                TextView tv_district = bottomSheetDialog.findViewById(R.id.tv_district);
//                TextView tv_taluka = bottomSheetDialog.findViewById(R.id.tv_taluka);
//                TextView tv_lab = bottomSheetDialog.findViewById(R.id.tv_lab);
//                TextView fromDate = bottomSheetDialog.findViewById(R.id.fromDate);
//                TextView tv_area = bottomSheetDialog.findViewById(R.id.tv_area);
//                TextView ToDate = bottomSheetDialog.findViewById(R.id.ToDate);
//                TextView tv_pincode = bottomSheetDialog.findViewById(R.id.tv_pincode);
//                TextView tv_orgnization = bottomSheetDialog.findViewById(R.id.tv_orgnization);
//                LinearLayout mainLLDate = bottomSheetDialog.findViewById(R.id.mainLLDate);
//                LinearLayout mainLLToDate = bottomSheetDialog.findViewById(R.id.mainLLToDate);
//                Button btnApply = bottomSheetDialog.findViewById(R.id.btnApply);
//                Button btnClearAll = bottomSheetDialog.findViewById(R.id.btnClearAll);
//                CheckBox selectAllCheckbox = bottomSheetDialog.findViewById(R.id.selectAllCheckbox);
//
//                selectAllCheckbox.setVisibility(View.GONE);
//
//                fromDate.setText(CampDate);
//                ToDate.setText(CampDateToDate);
//
//                tvDate.setBackgroundColor(Color.WHITE); // Set the background color to red
//
////                Calendar cal = Calendar.getInstance();
////                mYear = cal.get(Calendar.YEAR);
////                mMonth = cal.get(Calendar.MONTH);
////                mDay = cal.get(Calendar.DAY_OF_MONTH);
////
////
//////        Calendar todayCal = Calendar.getInstance();
//////        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//////
////                Calendar toCal = Calendar.getInstance();
////                toCal.add(Calendar.DAY_OF_MONTH, -3);
////
////                fromDate.setText(Utilities.dfDate4.format(new Date()));
////                ToDate.setText(Utilities.dfDate4.format(toCal.getTime()));
////
////
////                CampDate = Utilities.dfDate4.format(toCal.getTime());
////                CampDateToDate = Utilities.dfDate4.format(new Date());
////
//
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//
//
//                btnApply.setBackgroundColor(Color.parseColor("#002D62"));
//                btnClearAll.setBackgroundColor(Color.parseColor("#C44240"));
//
//
//                btnClearAll.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (divisionList!=null){
//                            divisionList.clear();
//
//                        }
//
//                        if (organizationList!=null){
//                            organizationList.clear();
//
//                        }
//                        if (districtList!=null){
//                            districtList.clear();
//
//                        }
//
//                        if (talukaList!=null){
//                            talukaList.clear();
//
//                        }
//
//                        if (landingLabList!=null){
//                            landingLabList.clear();
//
//                        }
//                        if (areaList!=null){
//                            areaList.clear();
//
//                        }
//
//                        if (pincodeList!=null){
//                            pincodeList.clear();
//
//                        }
//
//                        divisionId = "0";
//                        oganizationId = "0";
//                        DISTLGDCODE = "0";
//                        TALLGDCODE = "0";
//                        landingLabId = "0";
//
//                        searchFilterType = "0";
//
//
//                        fromDate.setText("");
//                        ToDate.setText("");
//                        CampDate = "";
//                        CampDateToDate = "";
//
//
//                        binding.tvAssigned.setText("0");
//                        binding.interestedInScreening.setText("0");
//                        binding.tvNotScreening.setText("0");
//                        binding.tvDeniedForScreening.setText("0");
//                        binding.reScreenedBeneficiary.setText("0");
//                        binding.reScreenedPendingCard.setText("0");
//                        binding.assignedBene.setText("0");
//                        binding.tvUnassigned.setText("0");
//                        binding.rejectedBeneficiary.setText("0");
//                        binding.tvDeniedFor.setText("0");
//
////                        if (phleboDetailsModel.getOutput() != null) {
////                            phleboDetailsModel.getOutput().clear();
////                            phleboListDetailsAdapter.notifyDataSetChanged();
////
////                        }
//
//                        recyclerView.setVisibility(View.GONE);
//                        selectAllCheckbox.setVisibility(View.GONE);
//                        PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(pincodeListSelectAdapter);
//
//                        DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(divisionListSelectAdapter);
//
//                        OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(organizationListSelectAdapter);
//
//                        DivisionListSelectAdapter divisionListSelectAdapter1 = new DivisionListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(divisionListSelectAdapter1);
//
//                        TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(talukaListSelectAdapter);
//
//                        LabListAdapter labListAdapter = new LabListAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(labListAdapter);
//
//
//                        AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(new ArrayList<>());
//                        recyclerView.setAdapter(areaListSelectAdapter);
//
//
//
//
//
//
//
//                    }
//
//
//                });
//
//                imv_CloseDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//
//
//                tv_district.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        JsonArray organizationArray = new JsonArray();
//
//                        if (organizationList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                            for (SubOrganizationModel.Output t :
//                                    organizationList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("organization", t.getSubOrgId());
//
//                                    oganizationId = String.valueOf(t.getSubOrgId());
//                                    organizationArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//
//                        JsonArray divisionArray = new JsonArray();
//
//                        if (divisionList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (SubDivisionModel.Output t :
//                                    divisionList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("division", t.getDivid());
//
//                                    divisionId = String.valueOf(t.getDivid());
//                                    divisionArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//                        new GetDistrictList(recyclerView).execute(oganizationId, EmpCode, DESGID, divisionId, "0");
//
//                        if (districtList == null || districtList.isEmpty()) {
//
//
//                            if (divisionId.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
//                                return;
//                            }
//
//                            new GetDistrictList(recyclerView).execute(oganizationId, EmpCode, DESGID, divisionId, "0");
//
//                        } else {
//
//                            DistrictListSelectAdapter districtListSelectAdapter1 = new DistrictListSelectAdapter(districtList);
//                            recyclerView.setAdapter(districtListSelectAdapter1);
//                        }
//
//
////                        new GetDistrictList(recyclerView).execute(STATELGDCODE, "0");
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//
//                        recyclerView.setVisibility(View.VISIBLE);
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red+
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//
//                    }
//                });
//                tvDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.VISIBLE);
//                        mainLLToDate.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//
//                        tvDate.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//
//                    }
//                });
//
//
//                tv_orgnization.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        if (organizationList == null || organizationList.isEmpty()) {
//                            new GetOrganization(recyclerView).execute(EmpCode, DESGID);
//
//                        } else {
//
//                            OrganizationListSelectAdapter organizationListSelectAdapter = new OrganizationListSelectAdapter(organizationList);
//                            recyclerView.setAdapter(organizationListSelectAdapter);
//                        }
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//                    }
//                });
//
//                tv_division.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        JsonArray organizationArray = new JsonArray();
//
//                        if (organizationList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                            for (SubOrganizationModel.Output t :
//                                    organizationList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("organization", t.getSubOrgId());
//
//                                    oganizationId = String.valueOf(t.getSubOrgId());
//                                    organizationArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//                        if (divisionList == null || divisionList.isEmpty()) {
//
////                            if (oganizationId.equals("0")) {
////                                Utilities.showAlertDialog(mContext, "Alert", "Please select organization", false);
////                                return;
////                            }
//
//                            new GetDivision(recyclerView).execute(oganizationId, EmpCode, DESGID);
//
//                        } else {
//
//                            DivisionListSelectAdapter divisionListSelectAdapter = new DivisionListSelectAdapter(divisionList);
//                            recyclerView.setAdapter(divisionListSelectAdapter);
//                        }
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//
//                    }
//                });
//
//                tv_lab.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        JsonArray districtJsonArray = new JsonArray();
//
//                        if (districtList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                            for (DistrictOrgModel.Output t :
//                                    districtList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("district", t.getDistlgdcode());
//
//                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
//                                    districtJsonArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        if (landingLabList == null || landingLabList.isEmpty()) {
//                            new GetLandingLab(recyclerView).execute(DISTLGDCODE);
//
//                        } else {
//
//                            LabListListSelectAdapter labListListSelectAdapter = new LabListListSelectAdapter(landingLabList);
//                            recyclerView.setAdapter(labListListSelectAdapter);
//
//                        }
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//                    }
//                });
//                tv_area.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//
//                        if (areaList == null || areaList.isEmpty()) {
//
//
//                            JsonArray districtJsonArray = new JsonArray();
//
//                            if (districtList != null) {
//                                //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                                for (DistrictOrgModel.Output t :
//                                        districtList) {
//                                    if (t.isChecked()) {
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("district", t.getDistlgdcode());
//
//                                        DISTLGDCODE = String.valueOf(t.getDistlgdcode());
//                                        districtJsonArray.add(jsonObject);
//                                        //  doctorSelected += 1;
//                                    }
//                                }
//                            }
//
//                            JsonArray divisionArray = new JsonArray();
//
//                            if (divisionList != null) {
//                                //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                                for (SubDivisionModel.Output t :
//                                        divisionList) {
//                                    if (t.isChecked()) {
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("division", t.getDivid());
//
//                                        divisionId = String.valueOf(t.getDivid());
//                                        divisionArray.add(jsonObject);
//                                        //  doctorSelected += 1;
//                                    }
//                                }
//                            }
//
//
//                            JsonArray organizationArray = new JsonArray();
//
//                            if (organizationList != null) {
//                                //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                                for (SubOrganizationModel.Output t :
//                                        organizationList) {
//                                    if (t.isChecked()) {
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("organization", t.getSubOrgId());
//
//                                        oganizationId = String.valueOf(t.getSubOrgId());
//                                        organizationArray.add(jsonObject);
//                                        //  doctorSelected += 1;
//                                    }
//                                }
//                            }
//
//
//                            JsonArray talukaArray = new JsonArray();
//
//                            if (talukaList != null) {
//                                //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                                for (TalukaListForFilterModel.Output t :
//                                        talukaList) {
//                                    if (t.isChecked()) {
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("talika", t.getTallgdcode());
//
//                                        TALLGDCODE = String.valueOf(t.getTallgdcode());
//                                        talukaArray.add(jsonObject);
//                                        //  doctorSelected += 1;
//
//                                    }
//                                }
//                            }
//
//
////                            if (TALLGDCODE.equals("0")) {
////                                Utilities.showAlertDialog(mContext, "Alert", "Please select taluka", false);
////                                return;
////                            }
//
////                            if (divisionId.equals("0")) {
////                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
////                                return;
////                            }
//
//                            new GetAreaList(recyclerView).execute(oganizationId, divisionId, DISTLGDCODE, TALLGDCODE);
//
//                        } else {
//
//                            AreaListSelectAdapter areaListSelectAdapter = new AreaListSelectAdapter(areaList);
//                            recyclerView.setAdapter(areaListSelectAdapter);
//                        }
//
//                    }
//                });
//
//                tv_taluka.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        JsonArray districtJsonArray = new JsonArray();
//
//                        if (districtList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                            for (DistrictOrgModel.Output t :
//                                    districtList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("district", t.getDistlgdcode());
//
//                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
//                                    districtJsonArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//
//
//                                }
//                            }
//
//                        }
//
//                        if (talukaList == null || talukaList.isEmpty()) {
//                            new GetTaluka(recyclerView).execute(EmpCode, DISTLGDCODE);
//
//                        } else {
//
//                            TalukaListSelectAdapter talukaListSelectAdapter = new TalukaListSelectAdapter(talukaList);
//                            recyclerView.setAdapter(talukaListSelectAdapter);
//                        }
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.GONE);
//
//
//                    }
//                });
//
//                selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//
//
//                        if (selectAllCheckbox.isChecked()) {
//                            if (pincodeList != null) {
//
//                                for (PincodeListModel.Output output :
//                                        pincodeList) {
//                                    output.setChecked(true);
//
//                                }
//
//                                PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
//                                recyclerView.setAdapter(pincodeListSelectAdapter);
//
//
//                            }
//
//                        } else {
//
//                            if (pincodeList != null) {
//
//                                for (PincodeListModel.Output output :
//                                        pincodeList) {
//                                    output.setChecked(false);
//
//                                }
//
//                                PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
//                                recyclerView.setAdapter(pincodeListSelectAdapter);
//
//
//
//                            }
//
//                        }
//
//
//                    }
//                });
//                tv_pincode.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mainLLDate.setVisibility(View.GONE);
//                        mainLLToDate.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//
//
//                        tvDate.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_division.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_district.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_taluka.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_area.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_pincode.setBackgroundColor(Color.WHITE); // Set the background color to red
//                        tv_lab.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        tv_orgnization.setBackgroundColor(Color.parseColor("#F3F2F9")); // Set the background color to red
//                        selectAllCheckbox.setVisibility(View.VISIBLE);
//
//
//
//                        JsonArray divisionArray = new JsonArray();
//
//                        if (divisionList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (SubDivisionModel.Output t :
//                                    divisionList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("division", t.getDivid());
//
//                                    divisionId = String.valueOf(t.getDivid());
//                                    divisionArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//
//                        JsonArray areaArray = new JsonArray();
//
//                        if (areaList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (AreaListModel.Output t :
//                                    areaList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("DIVID", t.getDividop());
//                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
//                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
//                                    jsonObject.addProperty("Pincode", t.getPincode());
//                                    jsonObject.addProperty("Area", t.getArea());
//
//                                    areaArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//
//
//                                }
//                            }
//
////                            new GetPincodeList(recyclerView).execute(areaArray.toString());
//
//                            getPincode(areaArray.toString(), recyclerView);
//
//                        }
//
//                        if (pincodeList == null || pincodeList.isEmpty()) {
//
//                            if (divisionId.equals("0")) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select division", false);
//                                return;
//                            }
//
//                            if (areaArray.toString().isEmpty()) {
//                                Utilities.showAlertDialog(mContext, "Alert", "Please select area", false);
//                                return;
//                            }
//
////                            new GetPincodeList(recyclerView).execute(areaArray.toString());
//
//                            getPincode(areaArray.toString(), recyclerView);
//
//
//                        } else {
//
//                            PincodeListSelectAdapter pincodeListSelectAdapter = new PincodeListSelectAdapter(pincodeList);
//                            recyclerView.setAdapter(pincodeListSelectAdapter);
//                        }
//
//                    }
//                });
//
//
//                fromDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        DatePickerDialog dpd1 = new DatePickerDialog(mContext,
//                                new DatePickerDialog.OnDateSetListener() {
//                                    @Override
//                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                        fromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
//
//                                        CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
//
//
////                                    if (Utilities.isNetworkAvailable(context)) {
////                                        new PacketCollectionActivity.GetPostCampDetails().execute(
////                                                DISTLGDCODE,
////                                                fromDate,
////                                                CampDateToDate,
////                                                landinglabId,
////                                                EmpCode
////                                        );
////                                    }
//
//
////                                    tv_teamNumber.setText("");
////                                    tv_district.setText("");
////                                    tv_lab.setText("");
//
//
//                                    }
//
//                                }, mYear, mMonth, mDay);
//
//                        Calendar c = Calendar.getInstance();
////                    try {
////                        c.setTime(Utilities.dfDate4.parse(CampDate));
////                    } catch (ParseException e) {
////                        throw new RuntimeException(e);
////                    }
////                    c.add(Calendar.DAY_OF_MONTH, 14);
//
//                        try {
//                            dpd1.getDatePicker().setCalendarViewShown(false);
//                            dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());
//
//                            // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        dpd1.show();
//                    }
//
//                });
//
//
//                ToDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (fromDate.getText().toString().equals(""))
//                            Utilities.showMessageString("Please Select From Date", mContext);
//                        else {
//
//                            DatePickerDialog dpd1 = new DatePickerDialog(mContext,
//                                    new DatePickerDialog.OnDateSetListener() {
//                                        @Override
//                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                            ToDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
//
//                                            CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
//
//
////                                    if (Utilities.isNetworkAvailable(context)) {
////                                        new PacketCollectionActivity.GetPostCampDetails().execute(
////                                                DISTLGDCODE,
////                                                fromDate,
////                                                CampDateToDate,
////                                                landinglabId,
////                                                EmpCode
////                                        );
////                                    }
//
//
////                                    tv_teamNumber.setText("");
////                                    tv_district.setText("");
////                                    tv_lab.setText("");
//
//
//                                        }
//
//                                    }, mYear, mMonth, mDay);
//
//                            Calendar c = Calendar.getInstance();
////                    try {
////                        c.setTime(Utilities.dfDate4.parse(CampDate));
////                    } catch (ParseException e) {
////                        throw new RuntimeException(e);
////                    }
////                    c.add(Calendar.DAY_OF_MONTH, 14);
//
//                            try {
//                                dpd1.getDatePicker().setCalendarViewShown(false);
//                                dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());
//
//                                // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            dpd1.show();
//                        }
//                    }
//                });
//
//                btnApply.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        bottomSheetDialog.dismiss();
//
//                        JsonArray districtJsonArray = new JsonArray();
//
//                        if (districtList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            int districtSelected = 0;
//                            for (DistrictOrgModel.Output t :
//                                    districtList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("district", t.getDistlgdcode());
//
//                                    DISTLGDCODE = String.valueOf(t.getDistlgdcode());
//                                    districtJsonArray.add(jsonObject);
//                                    districtSelected += 1;
//                                }
//                            }
////                            if (districtSelected == 0) {
////                                Utilities.showToastMessage("Please select district", mContext, false);
////                                return;
////                            }
//
//                        }
//
//
//                        JsonArray labJsonArray = new JsonArray();
//
//                        if (landingLabList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
////                    int doctorSelected = 0;
//                            for (LandingLabModel.Output t :
//                                    landingLabList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("lab", t.getLabCode());
//
//                                    landingLabId = String.valueOf(t.getLabCode());
//                                    labJsonArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//                        JsonArray divisionArray = new JsonArray();
//
//                        if (divisionList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (SubDivisionModel.Output t :
//                                    divisionList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("division", t.getDivid());
//
//                                    divisionId = String.valueOf(t.getDivid());
//                                    divisionArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//
//                        JsonArray organizationArray = new JsonArray();
//
//                        if (organizationList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (SubOrganizationModel.Output t :
//                                    organizationList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("organization", t.getSubOrgId());
//
//                                    oganizationId = String.valueOf(t.getSubOrgId());
//                                    organizationArray.add(jsonObject);
//                                    //  doctorSelected += 1;
//                                }
//                            }
//                        }
//
//
//                        JsonArray talukaArray = new JsonArray();
//
//                        if (talukaList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            for (TalukaListForFilterModel.Output t :
//                                    talukaList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("talUka", t.getTallgdcode());
//
//                                    TALLGDCODE = String.valueOf(t.getTallgdcode());
//                                    talukaArray.add(jsonObject);
//
//                                }
//                            }
//                        }
//
//
//                        JsonArray pincodeArray = new JsonArray();
//
//                        if (pincodeList != null) {
//
//                            int pincodeSelected = 0;
//                            for (PincodeListModel.Output t :
//                                    pincodeList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("SubOrgId", t.getSubOrgId());
//                                    jsonObject.addProperty("DIVID", t.getDivid());
//                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
//                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
//                                    jsonObject.addProperty("Pincode", t.getPincode());
//
//                                    pincodeArray.add(jsonObject);
//                                    pincodeSelected += 1;
//
//                                }
//                            }
//                            if (pincodeSelected == 0) {
//                                Utilities.showToastMessage("Please select pincode", mContext, false);
//                                return;
//
//                            }
//
//                        }
//
//
//                        JsonArray areaArray = new JsonArray();
//
//                        if (areaList != null) {
//                            //  Utilities.showToastMessage("Please select Doctor", context, false);
//
//                            int areaSelected = 0;
//                            for (AreaListModel.Output t :
//                                    areaList) {
//                                if (t.isChecked()) {
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("SubOrgId", t.getSubOrgId());
//                                    jsonObject.addProperty("DIVID", t.getDividop());
//                                    jsonObject.addProperty("DISTLGDCODE", t.getDistlgdcode());
//                                    jsonObject.addProperty("TALLGDCODE", t.getTallgdcode());
//                                    jsonObject.addProperty("Pincode", t.getPincode());
//                                    jsonObject.addProperty("Area", t.getArea());
//
//                                    areaArray.add(jsonObject);
//                                    areaSelected += 1;
//
//
//                                }
//                            }
//
//                            if (areaSelected == 0) {
//                                Utilities.showToastMessage("Please select Area", mContext, false);
//                                return;
//
//                            }
//
//                        }
//
//
//                        binding.rejectedBeneficiary.setText("0");
//                        binding.tvDeniedFor.setText("0");
//                        binding.interestedInScreening.setText("0");
//                        binding.tvNotScreening.setText("0");
//                        binding.tvDeniedForScreening.setText("0");
//                        binding.reScreenedBeneficiary.setText("0");
//                        binding.reScreenedPendingCard.setText("0");
//                        binding.tvAssigned.setText("0");
//                        binding.tvUnassigned.setText("0");
//
//
//                        getCount(
//                                areaArray.toString(),
//                                pincodeArray.toString()
//                        );
//
//
//                    }
//                });
//
//
//            }
//        });

    }

    private void getReportlist() {
        final ProgressDialog progressDialog = new ProgressDialog(DailyWorkDashboardForAdminActivity.this);
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
                        campBeneficiaryList = response.body().getOutput();
                        List<OutputItem> reportdatalist = response.body().getOutput();
                        CallListAdaptor callListAdaptor = new CallListAdaptor(DailyWorkDashboardForAdminActivity.this, reportdatalist, DailyWorkDashboardForAdminActivity.this);
//                        binding.rvReportlist.setAdapter(callListAdaptor);

                    } else {
                        CallListAdaptor callListAdaptor = new CallListAdaptor(DailyWorkDashboardForAdminActivity.this, new ArrayList<>(), DailyWorkDashboardForAdminActivity.this);
//                        binding.rvReportlist.setAdapter(callListAdaptor);
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

        final ProgressDialog progressDialog = new ProgressDialog(DailyWorkDashboardForAdminActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(DailyWorkDashboardForAdminActivity.this);
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

                            Utilities.showAlertDialog(DailyWorkDashboardForAdminActivity.this, "Success", data, true);


                        } else {

                            Utilities.showAlertDialog(DailyWorkDashboardForAdminActivity.this, "Fail", data, false, "Try Again!", new DialogInterface.OnClickListener() {
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(DailyWorkDashboardForAdminActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(DailyWorkDashboardForAdminActivity.this, R.layout.list_row);
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

        final ProgressDialog progressDialog = new ProgressDialog(DailyWorkDashboardForAdminActivity.this);


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
                            Utilities.showToastMessage(R.string.msgt_emptylist, DailyWorkDashboardForAdminActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(DailyWorkDashboardForAdminActivity.this, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();


                Utilities.showAlertDialog(DailyWorkDashboardForAdminActivity.this, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(DailyWorkDashboardForAdminActivity.this);
            builderSingle.setTitle("Select Team");
            builderSingle.setCancelable(false);
            View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
            builderSingle.setView(dialogueView);

            RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
            EditText edt_search = dialogueView.findViewById(R.id.edt_search);
            rvList.setHasFixedSize(true);
            rvList.setLayoutManager(new LinearLayoutManager(DailyWorkDashboardForAdminActivity.this));

            rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

            rvList.addOnItemTouchListener(
                    new RecyclerItemClickListener(DailyWorkDashboardForAdminActivity.this,
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





    @Override
    public void onclick(OutputItem item) {
        int assigncall_id = item.getAssignCallID();
        Intent intent = new Intent(DailyWorkDashboardForAdminActivity.this, AppoinmentConfirmationActivity.class);
        intent.putExtra("beneficiary", item);
//        intent.putExtra("b_address", item.getRegAddress());
//        intent.putExtra("b_mobileno", item.getMobile());
//        intent.putExtra("AssignCallID", String.valueOf(assigncall_id));
        startActivity(intent);
    }

    @Override
    public void onImvClick(OutputItem item) {

//        new GetCallingStatus().execute(String.valueOf(item.getAssignCallID()), EmpCode);
//        new GetCallingStatusNew().execute(String.valueOf(item.getAssignCallID()), EmpCode);
        mobileNumber = item.getMobile();
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


                                getCountForPageload(
                                        "0",
                                        "0"
                                );


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


    private void getCount(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForReregistration(CampDate, CampDateToDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, "0", "0", EmpCode, "1",campType).enqueue(new Callback<CountsForDailyWorkDashboardModel>() {
            @Override
            public void onResponse(Call<CountsForDailyWorkDashboardModel> call, Response<CountsForDailyWorkDashboardModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<CountsForDailyWorkDashboardModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        countList = new ArrayList<>();
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (CountsForDailyWorkDashboardModel.Output o :
                                    countsForDailyWorkDashboardModels) {
//

                                searchFilterType = "1";


                                if (o.getSequenceNo() == 7) {

                                    binding.tvAssigned.setText(""+ o.getPatientCount());

                                } else if (o.getSequenceNo() == 2) {

                                    binding.interestedInScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 3) {

                                    binding.tvNotScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 4) {

                                    binding.tvDeniedForScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 5) {

                                    binding.reScreenedBeneficiary.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 6) {

                                    binding.reScreenedPendingCard.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 1) {

                                    binding.assignedBene.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 8) {

                                    binding.tvUnassigned.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 0) {
                                    binding.rejectedBeneficiary.setText("" + o.getPatientCount());

                                }else if (o.getSequenceNo() == 9){
                                    binding.tvDeniedFor.setText("" + o.getPatientCount());

                                }

                            }


                            //  showTalukaListDialog(countListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<CountsForDailyWorkDashboardModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }


    private void getCountForPageload(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForPageload(CampDate, CampDateToDate, oganizationId, divisionId, DISTLGDCODE,landingLabId,"0","0",EmpCode,"1",DESGID,campType).enqueue(new Callback<CountsForDailyWorkDashboardModel>() {
            @Override
            public void onResponse(Call<CountsForDailyWorkDashboardModel> call, Response<CountsForDailyWorkDashboardModel> response) {

                if (response.isSuccessful()) {

                   String msg = response.body().getMessage();
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<CountsForDailyWorkDashboardModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        countList = new ArrayList<>();
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (CountsForDailyWorkDashboardModel.Output o :
                                    countsForDailyWorkDashboardModels) {

                                if (o.getSequenceNo() == 7) {

                                    binding.tvAssigned.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 2) {

                                    binding.interestedInScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 3) {

                                    binding.tvNotScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 4) {

                                    binding.tvDeniedForScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 5) {

                                    binding.reScreenedBeneficiary.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 6) {

                                    binding.reScreenedPendingCard.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 1) {

                                    binding.assignedBene.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 8) {

                                    binding.tvUnassigned.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 0) {
                                    binding.rejectedBeneficiary.setText("" + o.getPatientCount());

                                }else if (o.getSequenceNo() == 9){
                                    binding.tvDeniedFor.setText("" + o.getPatientCount());
                                }

                            }

                            //  showTalukaListDialog(countListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }else {
                        Utilities.showAlertDialog(mContext, "Fail",msg, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CountsForDailyWorkDashboardModel> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
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

    private void getCountForTeam(String areaArray, String pincodeArray) {
        ApiInterface apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);
        apiInterface.getCountForTeam(CampDate, CampDateToDate, oganizationId, divisionId, DISTLGDCODE, TALLGDCODE, landingLabId, "0", "0", EmpCode, "1",campType).enqueue(new Callback<CountsForDailyWorkDashboardModel>() {
            @Override
            public void onResponse(Call<CountsForDailyWorkDashboardModel> call, Response<CountsForDailyWorkDashboardModel> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        List<CountsForDailyWorkDashboardModel.Output> countsForDailyWorkDashboardModels = response.body().getOutput();
                        countList = new ArrayList<>();
                        if (countsForDailyWorkDashboardModels.size() > 0) {
                            for (CountsForDailyWorkDashboardModel.Output o :
                                    countsForDailyWorkDashboardModels) {
//


                                if (o.getSequenceNo() == 7) {

                                    binding.tvAssigned.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 2) {

                                    binding.interestedInScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 3) {

                                    binding.tvNotScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 4) {

                                    binding.tvDeniedForScreening.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 5) {

                                    binding.reScreenedBeneficiary.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 6) {

                                    binding.reScreenedPendingCard.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 1) {

                                    binding.assignedBene.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 8) {

                                    binding.tvUnassigned.setText("" + o.getPatientCount());

                                } else if (o.getSequenceNo() == 0) {
                                    binding.rejectedBeneficiary.setText("" + o.getPatientCount());

                                }

                            }


                            //  showTalukaListDialog(countListModels);
                        }
                        //   new NestedCampList_Activity.GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<CountsForDailyWorkDashboardModel> call, Throwable t) {
                t.getLocalizedMessage();
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


    public class GetOrganizationNewFilter extends AsyncTask<String, Void, String> {

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


                            showOrganizationListDialog(organizationList);
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


    private void showOrganizationListDialog(final List<SubOrganizationModel.Output> organizationList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Organization");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getSubOrgName()));
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
                binding.edtOrganization.setText(organizationList.get(which).getSubOrgName());
                oganizationId = String.valueOf(organizationList.get(which).getSubOrgId());
                divisionId = "0";
                DISTLGDCODE = "0";




                binding.edtDivision.setText("All");
                binding.edtSelectdistrict.setText("All");



                binding.tvAssigned.setText("0");
                binding.interestedInScreening.setText("0");
                binding.tvNotScreening.setText("0");
                binding.tvDeniedForScreening.setText("0");
                binding.reScreenedBeneficiary.setText("0");
                binding.reScreenedPendingCard.setText("0");
                binding.assignedBene.setText("0");
                binding.tvUnassigned.setText("0");
                binding.rejectedBeneficiary.setText("0");
                binding.tvDeniedFor.setText("0");



                getCountForPageload(
                        "0",
                        "0"
                );



            }
        });
        builderSingle.show();
    }




    public class GetDivisionNew extends AsyncTask<String, Void, String> {

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
                    List<SubDivisionModel.Output> divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDivisionListDialog(divisionList);
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


    private void showDivisionListDialog(final List<SubDivisionModel.Output> divisionList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Division");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getDivname()));
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
                binding.edtDivision.setText(divisionList.get(which).getDivname());
                divisionId = String.valueOf(divisionList.get(which).getDivid());

                binding.edtSelectdistrict.setText("All");
//                binding.edtTaluka.setText("All");
                binding.edtLab.setText("All");
                taluka = "0";
                landingLabId = "0";
                DISTLGDCODE ="0";


                binding.tvAssigned.setText("0");
                binding.interestedInScreening.setText("0");
                binding.tvNotScreening.setText("0");
                binding.tvDeniedForScreening.setText("0");
                binding.reScreenedBeneficiary.setText("0");
                binding.reScreenedPendingCard.setText("0");
                binding.assignedBene.setText("0");
                binding.tvUnassigned.setText("0");
                binding.rejectedBeneficiary.setText("0");
                binding.tvDeniedFor.setText("0");


                getCountForPageload(
                        "0",
                        "0"
                );





            }
        });
        builderSingle.show();
    }


    public class GetTalukaNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("STATELGDCODE", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAllTalukaList, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    TalukaModel talukaModel = new Gson().fromJson(result, TalukaModel.class);
                    type = talukaModel.getStatus();
                    message = talukaModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<TalukaModel.Output> camptypelist = talukaModel.getOutput();
                        if (camptypelist.size() > 0) {
                            showTalukaDialogue(camptypelist);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                        }
                    } else {
                        Utilities.showAlertDialog(mContext, "Fail", "Taluka not found", false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, mContext, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(mContext, "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
            builderSingle.setTitle("Select Taluka");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).gettALNAME()));
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
//                    binding.edtTaluka.setText(talukalist.get(which).gettALNAME());
                    taluka = String.valueOf(talukalist.get(which).gettLLGDCODE());
                    //  refreshCalendar();

                    binding.edtLab.setText("All");
                    landingLabId = "0";


                    binding.tvAssigned.setText("0");
                    binding.interestedInScreening.setText("0");
                    binding.tvNotScreening.setText("0");
                    binding.tvDeniedForScreening.setText("0");
                    binding.reScreenedBeneficiary.setText("0");
                    binding.reScreenedPendingCard.setText("0");
                    binding.assignedBene.setText("0");
                    binding.tvUnassigned.setText("0");
                    binding.rejectedBeneficiary.setText("0");
                    binding.tvDeniedFor.setText("0");


                    getCountForPageload(
                            "0",
                            "0"
                    );

                }
            });
            builderSingle.show();

        }

    }


    public class GetLandingLabNew extends AsyncTask<String, Void, String> {

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
            if (BuildConfig.isBeta) {
                res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            } else {
                res = WebServiceCall.APICall(ApplicationConstants.GetLabDistrictWise_V1, ApplicationConstants.webservice_d2d, param);
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    LandingLabModel landingLabModel = new Gson().fromJson(result, LandingLabModel.class);
                    type = landingLabModel.getStatus();
                    message = landingLabModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<LandingLabModel.Output> landinglablist = landingLabModel.getOutput();
                        if (landinglablist.size() > 0) {
                            showLadingLabDialogue(landinglablist);
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

        private void showLadingLabDialogue(final List<LandingLabModel.Output> landinglablist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
            builderSingle.setTitle("Select Lab");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

            for (int i = 0; i < landinglablist.size(); i++) {
                arrayAdapter.add(String.valueOf(landinglablist.get(i).getLabName()));
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
                    binding.edtLab.setText(landinglablist.get(which).getLabName());
                    landingLabId = String.valueOf(landinglablist.get(which).getLabCode());



                    binding.tvAssigned.setText("0");
                    binding.interestedInScreening.setText("0");
                    binding.tvNotScreening.setText("0");
                    binding.tvDeniedForScreening.setText("0");
                    binding.reScreenedBeneficiary.setText("0");
                    binding.reScreenedPendingCard.setText("0");
                    binding.assignedBene.setText("0");
                    binding.tvUnassigned.setText("0");
                    binding.rejectedBeneficiary.setText("0");
                    binding.tvDeniedFor.setText("0");


                    getCountForPageload(
                            "0",
                            "0"
                    );



                }
            });
            builderSingle.show();

        }
    }


    public class GetDistrictListFor extends AsyncTask<String, Void, String> {

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
                    List<DistrictOrgModel.Output> organizationList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDistrictForListDialog(organizationList);
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


    private void showDistrictForListDialog(final List<DistrictOrgModel.Output> organizationList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getDistname()));
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
                binding.edtSelectdistrict.setText(organizationList.get(which).getDistname());
                DISTLGDCODE = String.valueOf(organizationList.get(which).getDistlgdcode());
//                refreshCalendar();


//                binding.edtTaluka.setText("All");
                binding.edtLab.setText("All");
                taluka = "0";
                landingLabId = "0";



                binding.tvAssigned.setText("0");
                binding.interestedInScreening.setText("0");
                binding.tvNotScreening.setText("0");
                binding.tvDeniedForScreening.setText("0");
                binding.reScreenedBeneficiary.setText("0");
                binding.reScreenedPendingCard.setText("0");
                binding.assignedBene.setText("0");
                binding.tvUnassigned.setText("0");
                binding.rejectedBeneficiary.setText("0");
                binding.tvDeniedFor.setText("0");


                getCountForPageload(
                        "0",
                        "0"
                );




            }
        });
        builderSingle.show();
    }


}