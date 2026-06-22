package com.myhindlab.abkat.activities.confirmatoryTest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.SearchBeneficiaryDetailsActivity;
import com.myhindlab.abkat.adapters.Dependent_Adapter_For_Search_Beneficiary;
import com.myhindlab.abkat.adapters.Dependent_Adapter_For_Search_Beneficiary_Confirmatory;
import com.myhindlab.abkat.adapters.doortodoor.ConfirmatoryTestAdapter;
import com.myhindlab.abkat.expense_module.activities.BillUploadActivity;
import com.myhindlab.abkat.expense_module.models.PostCampFileListModel;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;

import com.myhindlab.abkat.models.Remark.CtRemarkResponse;
import com.myhindlab.abkat.models.Remark.OutputItem;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.models.doortodoor.TeamNumberModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppointmentConfirmationConfirmatoryActivity extends AppCompatActivity implements Dependent_Adapter_For_Search_Beneficiary_Confirmatory.DepedentDetailsConfimatory {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private EditText edt_Address, edt_area, edt_district;
    private TextView tv_block, tv_from_date, tv_total, tvDate, tv_teamNumber, tvCamp, tvCampType, tv_District, tv_filterremark, tv_to_date_new, tv_from_date_new;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;

    private RadioGroup rg_selection;

    private Button btnConfirm, btncall;

    private List<DependentForSearchBeneficiaryModel> dependentList;

    private RadioButton rb_regId, rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_dependent;
    ConfirmatoryTestAdapter confirmatoryTestAdapter;

    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber, mainllCampId, mainLLDate, campDate, mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;
    String fromDate, toDate, Remark_Id = "3";

    private String campId = "0", callType, isAdmin, UserId, campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0", status = "0",
            district, TALLGDCODE, regNo, beneficiary, mobileNumber, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private List<TeamCallingModel.Output> campBeneficiaryList;
    private LinearLayout lin_apdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmatory_test);

        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }

    private void initView() {
        context = AppointmentConfirmationConfirmatoryActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        tv_District = findViewById(R.id.tv_District);
        rv_dependent = findViewById(R.id.rv_dependent);
        rv_dependent.setLayoutManager(new LinearLayoutManager(context));
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_total = findViewById(R.id.tv_total);
        llTeamNumber = findViewById(R.id.llTeamNumber);
        llDistrict = findViewById(R.id.llDistrict);
        imvSearch = findViewById(R.id.imvSearch);
        rg_selection = findViewById(R.id.rg_selection);
        edt_area = findViewById(R.id.edt_area);
        rb_regId = findViewById(R.id.rb_regId);
        rb_campId = findViewById(R.id.rb_campId);
        edt_Address = findViewById(R.id.edt_Address);
        edt_district = findViewById(R.id.edt_district);
        btnConfirm = findViewById(R.id.btnConfirm);
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
        btncall = findViewById(R.id.btncall);
        tv_filterremark = findViewById(R.id.tv_filterremark);
        tv_from_date_new = findViewById(R.id.tv_from_date_new);
        tv_to_date_new = findViewById(R.id.tv_to_date_new);
        lin_apdate = findViewById(R.id.lin_apdate);

        Intent intent = getIntent();


        beneficiary = intent.getStringExtra("beneficiaryName");
        regNo = intent.getStringExtra("regNo");
        mobileNumber = intent.getStringExtra("mobile");
        status = intent.getStringExtra("status");

//        divisioId = intent.getStringExtra("DivisionId");

//        beneficiary = (TeamCallingModel.Output) getIntent().getSerializableExtra("beneficiaryDetails");

        if (status.equalsIgnoreCase("4")) {
            tv_filterremark.setEnabled(false);
            btnConfirm.setEnabled(false);
            tv_from_date.setEnabled(false);
            Utilities.showAlertDialog(context, "Alert", "You are not allowed to change the status as the beneficiary is not interested in CT.", false);
        }


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar todayCal = Calendar.getInstance();
        todayCal.add(Calendar.DAY_OF_MONTH, -22);

        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -7);
        tv_to_date_new.setText(Utilities.dfDate4.format(new Date()));
        tv_from_date_new.setText(Utilities.dfDate4.format(toCal.getTime()));

//        tv_from_date.setText(Utilities.dfDate6.format(todayCal.getTime()));
//        CampDate = Utilities.dfDate6.format(new Date());
//        CampDateToDate = Utilities.dfDate6.format(new Date());
        CampDate = Utilities.dfDate6.format(new Date());
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

//            tv_District.setText(district);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Appointment Confirmation");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


//            new Handler(Looper.getMainLooper()).post(() -> {
//
//                pd.setMessage("Please wait . . . ");
//                pd.setCancelable(false);
//                pd.show();
//
//                new Handler().postDelayed(() -> {
//
//                    pd.show();
//
//
//                }, 3000);
//            });


//            apiCall();
//            String regId = intent.getStringExtra("regId");
            String regNo = intent.getStringExtra("regNo");
//            Log.d("BroadcastReceiver", "onReceive: " + regId);

            new GetDependentInfo().execute(EmpCode, "0", "0", "3", regNo, "0");

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


//    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//
//            String regNo = intent.getStringExtra("regNo");
//
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                new GetDependentInfo().execute(EmpCode, "0", "0", "3", regNo, "0");
//            }, 3000); // Delay of 3 seconds
//        }
//    };



    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        callType = getIntent().getStringExtra("callType");
        isAdmin = getIntent().getStringExtra("isAdmin");

//        if (callType.equals("2"))
//            ll_buttons.setVisibility(View.GONE);

//        if (isAdmin.equals("1"))
//            ll_buttons.setVisibility(View.GONE);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("AppointmentConfirmationConfirmatoryActivity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        new GetDependentInfo().execute(EmpCode, "0", "0", "3", regNo, "0");


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
                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

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


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });


        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobileNumber));
                startActivity(intent);
            }
        });

        tv_filterremark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetRemarklist().execute(STATELGDCODE);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });

        tv_from_date_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date_new.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                //  fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);

                                tv_to_date_new.setText("");

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
        tv_to_date_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_from_date_new.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", context);
                else {

                    DatePickerDialog dpd1 = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    tv_to_date_new.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));


                                    //  CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);


//                                    tv_teamNumber.setText("");
//                                    tv_district.setText("");
//                                    tv_lab.setText("");

                                    if (Utilities.isNetworkAvailable(context)) {

//                                        new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE,TALLGDCODE,"411025", "1");

                                    }

//                                    if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29")||(DESGID.equalsIgnoreCase("104")||(DESGID.equalsIgnoreCase("162")||DESGID.equalsIgnoreCase("78")||DESGID.equalsIgnoreCase("77")||DESGID.equalsIgnoreCase("128")||DESGID.equalsIgnoreCase("131"))))) {
//
//
//                                    } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35")||DESGID.equalsIgnoreCase("86"))) {
//                                        llTeamNumber.setVisibility(View.VISIBLE);
//                                    }


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

    }


    private void submitData() {


        if (Remark_Id.equals("3") && tv_from_date.getText().toString().trim().isEmpty()) {
            Utilities.showToastMessage("Please Select Appointment Date", context, false);
            return;
        }

        if (dependentList == null) {
            Utilities.showToastMessage("Dependent details not found", context, false);
            return;
        }

        String date = "0";
        if (Remark_Id.equals("3")) {
            date = tv_from_date.getText().toString();
        } else {
            date = tv_from_date.getText().toString();
        }

        JsonArray campArr = new JsonArray();
        if (dependentList != null) {
            for (DependentForSearchBeneficiaryModel o : dependentList) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Regdid", Integer.valueOf(o.getRegdid()));
                jsonObject.addProperty("Regdno", o.getRegdNo());
                jsonObject.addProperty("AppointmentDate", date);
                campArr.add(jsonObject);
            }
        }


        if (Utilities.isNetworkAvailable(context)) {
            new InsertBillDetails().execute(campArr.toString());
        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }

    }

    @Override
    public void onBeneficiaryClick(DependentForSearchBeneficiaryModel team) {

    }


    public class GetDependentInfo extends AsyncTask<String, Void, String> {

        String fromdate_new = tv_from_date_new.getText().toString();
        String todate_new = tv_to_date_new.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("AREA", params[2]));
            param.add(new ParamsPojo("Type", params[3]));
            param.add(new ParamsPojo("REDNO", params[4]));
            param.add(new ParamsPojo("Regdid", params[5]));
            param.add(new ParamsPojo("FROMDATE", "2024/01/01"));
            param.add(new ParamsPojo("TODATE", "2027/07/24"));

            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));
            res = WebServiceCall.APICall(ApplicationConstants.GetConfirmatoryTestsScreeningAppointmentDetails_V1, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String type = "", message = "";
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    DependentForSearchBeneficiaryModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, DependentForSearchBeneficiaryModel.class);
//                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);


                    type = dependentForSearchBeneficiaryModel.getStatus();
                    message = dependentForSearchBeneficiaryModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
                        if (dependentList != null && dependentList.size() > 0) {


                            if (dependentForSearchBeneficiaryModel.getOutput().size() > 0) {
                                DependentForSearchBeneficiaryModel output = dependentForSearchBeneficiaryModel.getOutput().get(0);
                                edt_Address.setText(String.valueOf(output.getAddress()));
                                edt_area.setText(String.valueOf(output.getArea()));
                                edt_district.setText(String.valueOf(output.getdISTNAME()));
                                tv_from_date.setText(output.getAppointmentDate());


//                                if (output.getIsAppointmentDone().equalsIgnoreCase("Y")){
//                                    btnConfirm.setVisibility(View.GONE);
//
//                                }

                            }

//                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());

                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, dependentList, AppointmentConfirmationConfirmatoryActivity.this::onBeneficiaryClick));

                            int t = 0;

                            for (DependentForSearchBeneficiaryModel output :
                                    dependentList) {

//                                 t = t + dependentList.;

                                if (output.getSampleCollection() != null) {

                                    if (output.getSampleCollection().equalsIgnoreCase("Y")) {
//                                    finish();
                                        t = t + 1;

                                        if (t == dependentList.size()) {
                                            finish();
                                        }
                                    }

                                }
                            }

                            //   tvTotalCount.setText("" + t);


                        } else {
                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AppointmentConfirmationConfirmatoryActivity.this::onBeneficiaryClick));

                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AppointmentConfirmationConfirmatoryActivity.this::onBeneficiaryClick));

                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    public class GetRemarklist extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Type", "3"));
            res = WebServiceCall.APICall(ApplicationConstants.Get_T2T_CT_AssignmentRemarks, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    CtRemarkResponse ctRemarkResponse = new Gson().fromJson(result, CtRemarkResponse.class);
                    type = ctRemarkResponse.getStatus();
                    message = ctRemarkResponse.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<OutputItem> camptypelist = ctRemarkResponse.getOutput();
                        if (camptypelist.size() > 0) {


                            // camptypelist.add(0, new TalukaModel().new Output("ALL", 0));


                            showTalukaDialogue(camptypelist);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
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

        private void showTalukaDialogue(final List<OutputItem> talukalist) {
            androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
            builderSingle.setTitle("Select Remark");
            builderSingle.setCancelable(false);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
            for (int i = 0; i < talukalist.size(); i++) {
                arrayAdapter.add(String.valueOf(talukalist.get(i).getAssignmentRemarks()));
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
                    tv_filterremark.setText(talukalist.get(which).getAssignmentRemarks());
                    Remark_Id = String.valueOf(talukalist.get(which).getArId());



                    if (Remark_Id.equals("4")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_alertred);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("This beneficiary will not be available for CT screening.\n" +
                                "Please verify before proceeding with the submission.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");

                                dialog.dismiss();



                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    }
                    if (Remark_Id.equals("6")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_alertred);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("This beneficiary can be re-attempted for CT screening.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");

                                dialog.dismiss();



                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


                        });
                        builder.show();

                    }



                    if (Remark_Id.equals("3")) {
                        lin_apdate.setVisibility(View.VISIBLE);
                        // btnConfirm.setVisibility(View.VISIBLE);
                    } else {
                        lin_apdate.setVisibility(View.GONE);
                        // btnConfirm.setVisibility(View.GONE);
                    }
//                    new ConfirmatoryTestActivity.GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");
                    //  refreshCalendar();
                }
            });
            builderSingle.show();
        }
    }

    private class InsertBillDetails extends AsyncTask<String, Void, String> {
        String type_data = "1";

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
            param.add(new ParamsPojo("AppointmentDateDT", params[0]));
            param.add(new ParamsPojo("ArId", Remark_Id));
//            if (Remark_Id.equals("3")){
//                type_data="1";
//            }else {
//                type_data="2";
//            }
//            param.add(new ParamsPojo("Type", type_data));
            res = WebServiceCall.APICall(ApplicationConstants.UpdateT2T_CT_AppointmentDate, ApplicationConstants.webservice_d2d, param);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage("Appointment updated successfully");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                new UploadFile().execute();

//                                new GetDependentInfo().execute("0", "0", "0", "3", regNo, "0");


                                finish();


                            }

//                                finish();

                            //  ll_assigned.setVisibility(View.VISIBLE);


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
        }
    }


}