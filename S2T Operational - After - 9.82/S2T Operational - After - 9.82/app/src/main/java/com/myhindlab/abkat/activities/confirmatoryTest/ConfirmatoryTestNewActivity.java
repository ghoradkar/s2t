package com.myhindlab.abkat.activities.confirmatoryTest;

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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.myhindlab.abkat.adapters.doortodoor.ConfirmatoryTestNewAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfirmatoryTestNewActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private RecyclerView rv_d2dTeams;
    private List<DependentForSearchBeneficiaryModel> dependentList;


    private TextView tv_block, tv_from_date, tv_total, tvDate, tv_teamNumber, tvCamp, tvCampType, tv_filterremark,tv_District,tv_from_date_new,tv_to_date_new;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;

    private RadioGroup rg_selection;

    private RadioButton rb_regId, rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_patient_list;
    ConfirmatoryTestNewAdapter confirmatoryTestAdapter;

    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber, mainllCampId, mainLLDate, campDate, mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;
    String fromDate, toDate,Remark_Id="2";


    private String campId = "0", callType, isAdmin, UserId, campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",
            district, TALLGDCODE, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private List<TeamCallingModel.Output> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmatory_test_new);
        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }
    private void initView() {
        context = ConfirmatoryTestNewActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);

        ll_buttons = findViewById(R.id.ll_buttons);
        tv_from_date_new = findViewById(R.id.tv_from_date_new);
        tv_to_date_new = findViewById(R.id.tv_to_date_new);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        tv_District = findViewById(R.id.tv_District);
        tv_filterremark = findViewById(R.id.tv_filterremark);
        rv_patient_list = findViewById(R.id.rv_patient_list);
        rv_patient_list.setLayoutManager(new LinearLayoutManager(context));
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_total = findViewById(R.id.tv_total);
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

//        tv_District.setEnabled(false);
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar todayCal = Calendar.getInstance();
        todayCal.add(Calendar.DAY_OF_MONTH, -22);

        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -7);


        tv_from_date.setText(Utilities.dfDate6.format(todayCal.getTime()));
        CampDate = Utilities.dfDate6.format(new Date());
        CampDateToDate = Utilities.dfDate6.format(new Date());

        CampDate = Utilities.dfDate6.format(new Date());

        tv_to_date_new.setText(Utilities.dfDate4.format(new Date()));
        tv_from_date_new.setText(Utilities.dfDate4.format(toCal.getTime()));

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
            tv_District.setText(district);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirmatory Tests Screening");

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

//            try {
//
//                if (rb_campId.isChecked()) {
//
//
//                } else if (rb_regId.isChecked()) {
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
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

        if (rb_campId.isChecked()){
            new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");
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
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);
                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);
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


        rb_regId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_regId.isChecked()) {


                    new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","1","0","0");



//                    confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, new ArrayList<>());
//                    rv_patient_list.setAdapter(confirmatoryTestAdapter);
                }

            }
        });


        rb_campId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_campId.isChecked()) {


                    new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");



//                    confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, new ArrayList<>());
//                    rv_patient_list.setAdapter(confirmatoryTestAdapter);
                }

            }
        });


        tv_District.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute(STATELGDCODE, EmpCode);
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
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

        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchview_campname.clearFocus();
                ArrayList<TeamCallingModel.Output> facilitySearchedList = new ArrayList<>();
                for (TeamCallingModel.Output facility : campBeneficiaryList) {
                    if (facility.getBeneficiaryName() != null &&
                            facility.getBeneficiaryName().toLowerCase().startsWith(query.toLowerCase())
                            || facility.getRegdNo()!=null && facility.getRegdNo().toLowerCase().startsWith(query.toLowerCase())) {
                        facilitySearchedList.add(facility);
                    }
                }
                if (facilitySearchedList.size() == 0) {
                    Utilities.showToastMessage("No Data Found", context, false);
                    facilitySearchedList.clear();

                    confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, facilitySearchedList,DISTLGDCODE);
                    rv_patient_list.setAdapter(confirmatoryTestAdapter);
                } else {
                    confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, facilitySearchedList,DISTLGDCODE);
                    rv_patient_list.setAdapter(confirmatoryTestAdapter);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<TeamCallingModel.Output> facilitySearchedList = new ArrayList<>();
                if (newText.equals("")) {
                    //searchView.clearFocus();
                    facilitySearchedList.clear();

                    confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, campBeneficiaryList,DISTLGDCODE);
                    rv_patient_list.setAdapter(confirmatoryTestAdapter);

//                    setRecyclerView(sampleCollectionModelList);
                    Log.e("TAG", "onQueryTextChange: ");
                } else {
                    for (TeamCallingModel.Output facility : campBeneficiaryList) {
                        if (facility.getBeneficiaryName() != null &&
                                facility.getBeneficiaryName().toLowerCase().startsWith(newText.toLowerCase())
                                || facility.getRegdNo()!=null && facility.getRegdNo().toLowerCase().startsWith(newText.toLowerCase())) {



                            facilitySearchedList.add(facility);
                        }
                    }

                    if (facilitySearchedList.size() == 0) {
                        Utilities.showToastMessage("N0 Data Found", context, false);
                        facilitySearchedList.clear();


                        confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, facilitySearchedList,DISTLGDCODE);
                        rv_patient_list.setAdapter(confirmatoryTestAdapter);
//                        setRecyclerView(facilitySearchedList);
                    } else {

                        confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, facilitySearchedList,DISTLGDCODE);
                        rv_patient_list.setAdapter(confirmatoryTestAdapter);
//                        setRecyclerView(facilitySearchedList);
                    }
                }
                return true;
            }
        });

        tv_to_date_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_from_date.getText().toString().equals(""))
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
                                        new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");
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
            param.add(new ParamsPojo("Type", "2"));
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
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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
                    new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");

                    //  refreshCalendar();
                }
            });
            builderSingle.show();

        }

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
            param.add(new ParamsPojo("USERID", params[1]));


            //  res = WebServiceCall.APICall(ApplicationConstants.GetAllDistrictList, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictByUserID, ApplicationConstants.webservice_d2d, param);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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
                tv_District.setText(districtList.get(which).getDISTNAME());
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                //  refreshCalendar();


                if (rb_campId.isChecked()){
                    new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");

                } else if (rb_regId.isChecked()) {
                    new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","1","0","0");

                }
            }
        });
        builderSingle.show();

    }





    private class GetPostCampDetails extends AsyncTask<String, Void, String> {
        String fromdate_new=tv_from_date_new.getText().toString();
        String todate_new=tv_to_date_new.getText().toString();
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
            param.add(new ParamsPojo("USERID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("AREA", params[2]));
            param.add(new ParamsPojo("Type", Remark_Id));
            param.add(new ParamsPojo("REDNO", params[4]));
            param.add(new ParamsPojo("FROMDATE", fromdate_new));
            param.add(new ParamsPojo("TODATE", todate_new));
            param.add(new ParamsPojo("T2T_Order_Id", "0"));
            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));

//            Log.d("Confirmatory","GetDataCT" + new Gson().toJson(param));

            res = WebServiceCall.APICall(ApplicationConstants.GetConfirmatoryTestsScreeningAppointmentDetails_V2, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    TeamCallingModel pojoDetails = new Gson().fromJson(result, TeamCallingModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {


                            int t = 0;

                            for (TeamCallingModel.Output  outut :
                                    campBeneficiaryList) {


                                 t = t + Integer.valueOf(outut.getMemberCount());


                                tv_total.setText(""+t);


                            }


                            confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, campBeneficiaryList,DISTLGDCODE);
                            rv_patient_list.setAdapter(confirmatoryTestAdapter);
                        }
                    } else {
                        Utilities.showMessageString("Beneficiary List Not Found", context);

                        tv_total.setText("");

                        confirmatoryTestAdapter = new ConfirmatoryTestNewAdapter(context, new ArrayList<>(),DISTLGDCODE);
                        rv_patient_list.setAdapter(confirmatoryTestAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


//    public class GetDependentInfo extends AsyncTask<String, Void, String> {
//
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
//            param.add(new ParamsPojo("SearchType", params[0]));
//            param.add(new ParamsPojo("RegdNo", params[1]));
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryInformationByRegdNo, ApplicationConstants.webservice_d2d, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//
//                    DependentForSearchBeneficiaryModel dependentForSearchBeneficiaryModel = new Gson().fromJson(result, DependentForSearchBeneficiaryModel.class);
////                    d2dWorkingTeamModel = new Gson().fromJson(result, D2dWorkingTeamModel.class);
//
//
//                    type = dependentForSearchBeneficiaryModel.getStatus();
//                    message = dependentForSearchBeneficiaryModel.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        dependentList = dependentForSearchBeneficiaryModel.getOutput();
//                        if (dependentList != null && dependentList.size() > 0) {
//
//
////                            campCoordinatorNotWorkingTeamAdapter = new CampCoordinatorNotWorkingTeamAdapter(d2dWorkingTeamModel.getOutput());
//
//                            rv_patient_list.setAdapter(new ConfirmatoryTestNewAdapter(context, dependentList));
//                            int t = 0;
//
//                            for (DependentForSearchBeneficiaryModel  output :
//                                    dependentList) {
//
//                                // t = t + Integer.valueOf(output.getaGE());
//
//
//
//
//                            }
//
//                            //   tvTotalCount.setText("" + t);
//
//
//////                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//////                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//////                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//////                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//////                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
////
////                            }
//
//                        } else {
//                            rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AppointmentConfirmationConfirmatoryActivity.this::onBeneficiaryClick));
//
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        rv_dependent.setAdapter(new Dependent_Adapter_For_Search_Beneficiary_Confirmatory(context, new ArrayList<>(), AppointmentConfirmationConfirmatoryActivity.this::onBeneficiaryClick));
//
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
//            }
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();


        if (rb_campId.isChecked()){

            new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","2","0","0");

        } else if (rb_regId.isChecked()) {
            new GetPostCampDetails().execute(EmpCode, DISTLGDCODE, "0","1","0","0");

        }

    }


}