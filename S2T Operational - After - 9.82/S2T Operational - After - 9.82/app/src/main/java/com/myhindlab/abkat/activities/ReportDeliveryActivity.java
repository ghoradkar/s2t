package com.myhindlab.abkat.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.couriermodule.AcceptSamplesInLabActivity;
import com.myhindlab.abkat.activities.doortodoor.CallToDoctorRequestActivity;
import com.myhindlab.abkat.adapters.PostCampBeneficiaryAdapterNew;
import com.myhindlab.abkat.adapters.ReportDeliveryCampIdAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedAdapterNew;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.TeamsDetailsModelNew;
import com.myhindlab.abkat.models.doortodoor.DivisionModel;
import com.myhindlab.abkat.models.doortodoor.TeamNumberModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportDeliveryActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;


    private TextView tv_block, tv_from_date, tv_to_date,tvDate, tv_teamNumber,tvCamp,tvCampType, tv_district;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;

    private RadioGroup rg_selection;

    private RadioButton rb_regId,rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_patient_list;
    PostCampBeneficiaryAdapterNew postCampBeneficiaryAdapter;

    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber,mainllCampId,mainLLDate,campDate,mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;
    String fromDate,toDate;



    private String campId = "0", callType, isAdmin, UserId,campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",
            district, TALLGDCODE, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private ArrayList<PostCampBeneficiaryListModel.OutputBean> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_delivery);

        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }

    private void initView() {
        context = ReportDeliveryActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        tv_district = findViewById(R.id.tv_district);
        rv_patient_list = findViewById(R.id.rv_patient_list);
        rv_patient_list.setLayoutManager(new LinearLayoutManager(context));
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        llTeamNumber = findViewById(R.id.llTeamNumber);
        llDistrict = findViewById(R.id.llDistrict);
        imvSearch = findViewById(R.id.imvSearch);
        rg_selection = findViewById(R.id.rg_selection);
        rb_regId = findViewById(R.id.rb_regId);
        rb_campId = findViewById(R.id.rb_campId);
        tvDate = findViewById(R.id.tvDate);
        tvCamp = findViewById(R.id.tvCamp);
        mainLLDate = findViewById(R.id.mainLLDate);
        cardviewSearch = findViewById(R.id.cardviewSearch);
        rb_campId = findViewById(R.id.rb_campId);
        rb_regId = findViewById(R.id.rb_regId);
        campDate = findViewById(R.id.campDate);
        mainllCampId = findViewById(R.id.mainllCampId);
        tvCampType = findViewById(R.id.tvCampType);
        mainllCampType = findViewById(R.id.mainllCampType);


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar todayCal = Calendar.getInstance();
        todayCal.add(Calendar.DAY_OF_MONTH, -22);

        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -7);


        rb_regId.setChecked(true);

        campDate.setVisibility(View.GONE);
        mainllCampId.setVisibility(View.GONE);
        mainllCampType.setVisibility(View.GONE);
        mainLLDate.setVisibility(View.VISIBLE);
        cardviewSearch.setVisibility(View.VISIBLE);


        tv_from_date.setText(Utilities.dfDate4.format(todayCal.getTime()));
        tv_to_date.setText(Utilities.dfDate4.format(toCal.getTime()));
        CampDate = Utilities.dfDate4.format(new Date());
        CampDateToDate = Utilities.dfDate4.format(new Date());

        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyy");
//        tv_from_date.setText(Utilities.getPreviousDate());
//        tv_to_date.setText(mdformat.format(calendar.getTime()));

        campBeneficiaryList = new ArrayList<>();

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
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
            }

            tv_district.setText(district);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            apiCall();
            String regId = intent.getStringExtra("regId");
            String campId = intent.getStringExtra("campId");
            Log.d("BroadcastReceiver", "onReceive: " + regId);

            try {

                if (rb_campId.isChecked()){

                    new GetPostCampDetails().execute(
                            DISTLGDCODE,
                            "0",
                            "0",
                            "3",
                            "0",
                            campId,
                            "0",
                            "0",
                            "0000",
                            "2018/04/10",
                            "2026/04/10",
                            teamId
                    );

                } else if (rb_regId.isChecked()) {
                    new GetPostCampDetails().execute(
                            DISTLGDCODE,
                            "0",
                            "0",
                            "5",
                            "0",
                            "0",
                            "1",
                            "0",
                            regId.substring(regId.length() - 4),
                            CampDate,
                            CampDateToDate,
                            teamId
                    );

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//            new GetPostCampDetails().execute(
//                    DISTLGDCODE,
//                    "0",
//                    "0",
//                    "3",
//                    "0",
//                    "0",
//                    callType,
//                    "0",
//                    regId.substring(regId.length() - 4)
//
//
//            );

        }
    };

    private void apiCall() {
        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    DISTLGDCODE,
                    "0",
                    "0",
                    "3",
                    "0",
                    campId,
                    callType,
                    "0"


            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }


    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        callType = getIntent().getStringExtra("callType");
        isAdmin = getIntent().getStringExtra("isAdmin");

//        if (callType.equals("2"))
//            ll_buttons.setVisibility(View.GONE);

//        if (isAdmin.equals("1"))
//            ll_buttons.setVisibility(View.GONE);




        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("ReportDeliveryActivity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")||(DESGID.equalsIgnoreCase("104")||(DESGID.equalsIgnoreCase("162")||DESGID.equalsIgnoreCase("78")||DESGID.equalsIgnoreCase("77")||DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("160"))))) {
            llTeamNumber.setVisibility(View.GONE);
            llDistrict.setVisibility(View.VISIBLE);
          //  teamId = "0";

        } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35")||DESGID.equalsIgnoreCase("86"))) {
            llTeamNumber.setVisibility(View.VISIBLE);
            llDistrict.setVisibility(View.GONE);
           // teamId = "";
        }

        // apiCall();
    }

    private void setEventHandler() {
        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                                tv_to_date.setText("");

                                //

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


                                //                                if (Utilities.isNetworkAvailable(context)) {
                                //                                } else {
                                //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                //                                }
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

        rb_campId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_campId.isChecked()){
                    mainLLDate.setVisibility(View.GONE);
                    cardviewSearch.setVisibility(View.GONE);
                    campDate.setVisibility(View.VISIBLE);
                    mainllCampId.setVisibility(View.VISIBLE);
                    mainllCampType.setVisibility(View.VISIBLE);


                    postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, new ArrayList<>(), "0", "0");
                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                }

            }
        });

        rb_regId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_regId.isChecked()){
                    campDate.setVisibility(View.GONE);
                    mainllCampId.setVisibility(View.GONE);
                    mainllCampType.setVisibility(View.GONE);
                    mainLLDate.setVisibility(View.VISIBLE);
                    cardviewSearch.setVisibility(View.VISIBLE);


                    postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, new ArrayList<>(), "0", "0");
                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                }

            }
        });



        tvCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvCampType.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Select camp type", "Please select camp type", false);
                    return;
                }

                new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, campTypeId, "0");

            }
        });


        tvCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("REGULAR", 1));
//                campTypeModelArrayList.add(new CampTypeModel("CSC REGULAR CAMP", 2));
                campTypeModelArrayList.add(new CampTypeModel("DOOR TO DOOR", 3));
//                campTypeModelArrayList.add(new CampTypeModel("CSC D2D", 4));
                campTypeModelArrayList.add(new CampTypeModel("FLEXI CAMP", 5));

                showCampType(campTypeModelArrayList);

            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                              //  new GetCampList().execute(CampDate, EmpCode, DISTLGDCODE, String.valueOf(AssignedId), "0");

                            }

                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tv_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_from_date.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {


                    DatePickerDialog dpd1 = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    tv_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                    CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


                                    //                                if (Utilities.isNetworkAvailable(context)) {
                                    //                                } else {
                                    //                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                    //                                }
                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();

                    try {
                        c.setTime(Utilities.dfDate4.parse(fromDate));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    c.add(Calendar.DAY_OF_MONTH, 14);

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


        tv_teamNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetTeamId().execute();
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


//        tv_district.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetDistrictList().execute(STATELGDCODE,"0",EmpCode);
//                } else {
//                    Utilities.showToastMessage("Please Check Your Connection", context, false);
//                }
//            }
//        });


        imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DESGID.equalsIgnoreCase("35")||(DESGID.equalsIgnoreCase("64")||(DESGID.equalsIgnoreCase("86")))){
//                    if (tv_teamNumber.getText().toString().trim().isEmpty()) {
//                        Utilities.showToastMessage("Please Select Team Number", context, false);
//                        return;
//                    }

                }else {

                }

                if (searchview_campname.getQuery().toString().length() > 3) {
                    new GetPostCampDetails().execute(
                            DISTLGDCODE,
                            "0",
                            "0",
                            "5",
                            "0",
                            "0",
                            "1",
                            "0",
                            searchview_campname.getQuery().toString(),
                            CampDate,
                            CampDateToDate,
                            teamId
                    );
                } else {
                    Utilities.showToastMessage("Enter at least 4 digit of Reg. No", context, false);
                }

            }
        });


        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<PostCampBeneficiaryListModel.OutputBean> searchPatientList = new ArrayList<>();
//                if(query.length()==4){
//                    new GetPostCampDetails().execute(
//                            "0",
//                            "0",
//                            "0",
//                            "5",
//                            "0",
//                            "0",
//                            "1",
//                            "0"
//
//
//                    );
//
//                }


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<PostCampBeneficiaryListModel.OutputBean> searchPatientList = new ArrayList<>();

//                if (newText.length() == 4) {
//
//                    new GetPostCampDetails().execute(
//                            DISTLGDCODE,
//                            "0",
//                            "0",
//                            "5",
//                            "0",
//                            "0",
//                            "1",
//                            "0",
//                            newText
//
//
//                    );
//
//                } else {
//
//                    if (campBeneficiaryList != null) {
//                        campBeneficiaryList.clear();
//
//                    }
//
//                    postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, campBeneficiaryList, "0", "0");
//                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
//
//
//                }

                if (newText.isEmpty()) {
                    if (campBeneficiaryList != null) {
                        campBeneficiaryList.clear();

                    }

                    postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, campBeneficiaryList, "0", "0");
                    rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                }
                return true;
            }
        });

    }

    private class GetPostCampDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            if (!((Activity) context).isFinishing()) {
                pd.show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", params  [0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Month", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            param.add(new ParamsPojo("IsComplete", params[4]));
            param.add(new ParamsPojo("CAMPID", params[5]));
            param.add(new ParamsPojo("IsReferred", params[6]));
            param.add(new ParamsPojo("RegdID", params[7]));
            param.add(new ParamsPojo("UserID", "0"));
            param.add(new ParamsPojo("SearchRegdID", params[8]));
            param.add(new ParamsPojo("FromDate", params[9]));
            param.add(new ParamsPojo("ToDate", params[10]));
            param.add(new ParamsPojo("TeamNumber", params[11]));
            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));
            // res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails, ApplicationConstants.webservice, param);
          //  res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails_V1, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails_V2, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    PostCampBeneficiaryListModel pojoDetails = new Gson().fromJson(result, PostCampBeneficiaryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {

                            postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, campBeneficiaryList, "0", "0");
                            rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                        }
                    }else {
                        Utilities.showMessageString("Beneficiary List Not Found",context);

                        postCampBeneficiaryAdapter = new PostCampBeneficiaryAdapterNew(context, new ArrayList<>(), "0", "0");
                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    private void callApi() {
//        if (Utilities.isNetworkAvailable(context)) {
//            new GetPostCampDetails().execute(
//                    DISTLGDCODE,
//                    "0",
//                    "0",
//                    "3",
//                    "0",
//                    campId,
//                    callType,
//                    "0"
//
//
//            );
//
//        } else
//            Utilities.showMessageString(getResources().getString(R.string.msgt_nointernetconnection), context);
//    }

    private class GetTeamId extends AsyncTask<String, Void, String> {

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
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("UserID", EmpCode));
            //  res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByUserId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    teamlist = new ArrayList<>();
                    ArrayList<TeamsDetailsModel.OutputBean> assignteamlist = new ArrayList<>();
                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        assignteamlist = teamsDetailsModel.getOutput();
                        if (assignteamlist.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showAssignedTeamListDialog(assignteamlist);
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

    private void showTeamListListDialog(final List<TeamNumberModel.Output> teamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team Number");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < teamlist.size(); i++) {
            arrayAdapter.add(String.valueOf(teamlist.get(i).getTeamName()));
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
                tv_teamNumber.setText(teamlist.get(which).getTeamName());
                teamId = String.valueOf(teamlist.get(which).getTeamNumber());
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamNumber();

                                tv_teamNumber.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();

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


    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

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
                tvCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = String.valueOf(campTypeModelsList.get(which).getCampTypeId());




                //  refreshCalendar();


            }
        });
        builderSingle.show();
    }




    public class GetCampList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("LABCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.GetCampList, ApplicationConstants.webservice_d2d, param);
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
                    List<CampListModel.OutputBean> campList = new ArrayList<>();

                    //  Log.d("result ",result.toString());

                    CampListModel campListModel = new Gson().fromJson(result, CampListModel.class);
                    type = campListModel.getStatus();
                    message = campListModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        campList = campListModel.getOutput();

                        if (campList.size() > 0) {
                            // districtList.add(0, new DistrictList_Model("0", "All"));

                            showCampListDialog(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Camp I'd not available", false);
            }
        }
    }


    private void showCampListDialog(final List<CampListModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select CampId");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(context));

        rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                CampListModel.OutputBean team = assignteamlist.get(position);
                                AssignedId = assignteamlist.get(position).getCampId();

                                tvCamp.setText(assignteamlist.get(position).getCampId());
                                teamDialog.dismiss();


                                new GetPostCampDetails().execute(
                                        DISTLGDCODE,
                                        "0",
                                        "0",
                                        "3",
                                        "0",
                                        AssignedId,
                                        "0",
                                        "0",
                                        "0000",
                                        "2018/04/10",
                                        "2025/04/10",
                                        teamId
                                );

                            }
                        }));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));
                    return;
                }

                if (teamList.size() == 0) {
                    rvList.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    List<CampListModel.OutputBean> searchedTestList = new ArrayList<>();
                    for (CampListModel.OutputBean clientDetails : assignteamlist) {

                        String countryToBeSearched = clientDetails.getMember1().toLowerCase();
                        String member2 = clientDetails.getMember2().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase()) ||
                                member2.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(searchedTestList));
                } else {
                    rvList.setAdapter(new ReportDeliveryCampIdAdapter(assignteamlist));
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
                for (CampListModel.OutputBean team : assignteamlist
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





    public class GetDistrictList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DivId", params[1]));
            param.add(new ParamsPojo("USERID", params[2]));


          //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDivisionWiseDistrictAndUserID, ApplicationConstants.webservice_d2d, param);
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
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList);
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

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
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
                tv_district.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();
            }
        });
        builderSingle.show();

    }


}