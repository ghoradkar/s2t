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
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.AttendanceDetailsActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.Model.DepartMentTypeModel;
import com.myhindlab.abkat.activities.confirmatoryTest.adapter.ConfirmatoryTestTeamAssignNewAdapter;
import com.myhindlab.abkat.models.CampBeneficiaryListModel;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.DependentForSearchBeneficiaryModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.Remark.CtRemarkResponse;
import com.myhindlab.abkat.models.Remark.OutputItem;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.doortodoor.TeamNumberModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmatoryTestAssignTeamEmergencyPatientActivity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private RecyclerView rv_d2dTeams;


    private List<DependentForSearchBeneficiaryModel> dependentList;

    private int pageNo = 1;
    private int pageSize = 10;


    private TextView tv_block, tv_from_date, tv_to_date, tv_total, tv_taluka, tv_prescribed_from, tv_filterremark, tvDate, tv_teamNumber, tvCamp, tvCampType, tv_District;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;
    private MaterialEditText searchview_campname_pincode;
    private ImageButton imvSearchPincode;

    private RadioGroup rg_selection;

    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_patient_list;
    ConfirmatoryTestTeamAssignNewAdapter confirmatoryTestTeamAssignAdapter;

    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber, mainllCampId, mainLLDate, campDate, mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;
    String fromDate, toDate;

    private Button btnSearch;


    private String presciptioId = "0";
    private String campId = "0", callType, isAdmin, UserId, talukaId = "0", Remark_Id = "2", campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",
            district, TALLGDCODE, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private List<CampBeneficiaryListModel.OutputBean> campBeneficiaryList;

    private LinearLayout llFilter;
    private ImageButton btnFilter;
    private boolean isAllLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmatory_test_team_assign_new);
        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();

    }

    private void initView() {
        context = ConfirmatoryTestAssignTeamEmergencyPatientActivity.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        imvSearchPincode = findViewById(R.id.imvSearchPincode);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);
        searchview_campname_pincode = findViewById(R.id.searchview_campname_pincode);
        tv_District = findViewById(R.id.tv_District);
        rv_patient_list = findViewById(R.id.rv_patient_list);
        btnSearch = findViewById(R.id.btnSearch);
        llFilter = findViewById(R.id.llFilter);
        searchview_campname.setImeOptions(EditorInfo.IME_ACTION_DONE);
        rv_patient_list.setLayoutManager(new LinearLayoutManager(context));
        rv_patient_list.setHasFixedSize(true);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_taluka = findViewById(R.id.tv_taluka);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_total = findViewById(R.id.tv_total);
        llTeamNumber = findViewById(R.id.llTeamNumber);
        llDistrict = findViewById(R.id.llDistrict);
        imvSearch = findViewById(R.id.imvSearch);
        rg_selection = findViewById(R.id.rg_selection);
        tvDate = findViewById(R.id.tvDate);
        tvCamp = findViewById(R.id.tvCamp);
        mainLLDate = findViewById(R.id.mainLLDate);
        cardviewSearch = findViewById(R.id.cardviewSearch);
        campDate = findViewById(R.id.campDate);
        mainllCampId = findViewById(R.id.mainllCampId);
        tvCampType = findViewById(R.id.tvCampType);
        mainllCampType = findViewById(R.id.mainllCampType);
        tv_filterremark = findViewById(R.id.tv_filterremark);
        tv_prescribed_from = findViewById(R.id.tv_prescribed_from);
//        tv_District.setEnabled(false);
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
//        Calendar toCal = Calendar.getInstance();
//        toCal.add(Calendar.DAY_OF_MONTH, -7);
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -3);
        tv_to_date.setText(Utilities.dfDate4.format(new Date()));
        tv_from_date.setText(Utilities.dfDate4.format(toCal.getTime()));

        fromDate = Utilities.dfDate4.format(toCal.getTime());
        CampDateToDate = Utilities.dfDate4.format(new Date());
//        CampDate = Utilities.dfDate6.format(new Date());
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
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                LabCode = json.getString("LabCode");
//                TALLGDCODE = json.getString("TALLGDCODE");
            }

            tv_District.setText(district);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assign Team For CT");
        btnFilter = toolbar.findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFilter();
            }
        });


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    void toggleFilter() {
        llFilter.setVisibility(llFilter.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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


//        new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE, talukaId, searchview_campname_pincode.getText().toString(), "1");


        // apiCall();
    }

    private void loadMoreData() {
        ArrayList<CampBeneficiaryListModel.OutputBean> filteredList = new ArrayList<>();
        // Add more data to the list
        int start = pageNo * pageSize;
        try {
            for (int i = start; i < start + pageSize; i++) {
                filteredList.add(campBeneficiaryList.get(i));
            }
            pageNo += 1;
        } catch (Exception e) {
            e.printStackTrace();
            isAllLoaded = true;
        }

        confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, filteredList);
        rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
    }

    private void loadMoreDataReverse() {
        ArrayList<CampBeneficiaryListModel.OutputBean> filteredList = new ArrayList<>();
        // Add more data to the list
        int start = pageNo * pageSize;
        try {
            for (int i = start; i < start + pageSize; i++) {
                filteredList.add(campBeneficiaryList.get(i));
            }
            pageNo -= 1;
        } catch (Exception e) {
            e.printStackTrace();
            isAllLoaded = true;
        }

        confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, filteredList);
        rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
    }

    private void setEventHandler() {
        // Add scroll listener
        rv_patient_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1)) {
                    if (searchview_campname.getQuery().toString().isEmpty() && !isAllLoaded) {
                        //     loadMoreData();
                    }


                } else {
//                    if (!recyclerView.canScrollVertically(-1)) {
//                        if (searchview_campname.getQuery().toString().isEmpty() && isAllLoaded) {
//                            pageNo -= 1;
//                            loadMoreDataReverse();
//                        }
//
//                    }

                }

            }
        });


        tv_prescribed_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrescription();
            }
        });

        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tv_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);

                                tv_to_date.setText("");

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


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (tv_taluka.getText().toString().isEmpty()) {
//                    Utilities.showToastMessage("Please select taluka", context, false);
//                    return;
//                }

//                if (campBeneficiaryList!=null){
//                    campBeneficiaryList.clear();
//                }

                if (!searchview_campname_pincode.getText().toString().isEmpty()) {
                    if (searchview_campname_pincode.getText().toString().length() < 6) {
                        Utilities.showToastMessage("Please 6 Digit Pin code", context, false);
                        return;
                    }
                }

//                if (searchview_campname_pincode.getText().toString().isEmpty()) {
//                    searchview_campname_pincode.setText("0");
//                }


                pageNo = 1;
                new GetPostCampDetails().execute(
                        fromDate,
                        CampDateToDate,
                        EmpCode,
                        DISTLGDCODE,
                        talukaId,
                        searchview_campname_pincode.getText().toString().isEmpty() ? "0" : searchview_campname_pincode.getText().toString(),
                        Remark_Id,
                        presciptioId);
            }
        });


//        searchview_campname_pincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!searchview_campname_pincode.getText().toString().trim().equals("")) {
//                    if (searchview_campname_pincode.getText().toString().trim().length() == 6) {
//
//                        if (Utilities.isNetworkAvailable(context)) {
//                            new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE, talukaId, searchview_campname_pincode.getText().toString(), "1");
////                                getWorkerInfo();
////                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
//                            searchview_campname_pincode.clearFocus();
//
//                            //       new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                        } else {
//
//                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                        }
//
//
//                    } else {
//                        searchview_campname_pincode.setError("Enter 6 digit pincode");
//                    }
//                }
//            }
//        });


//        searchview_campname_pincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!searchview_campname_pincode.getText().toString().trim().equals("")) {
//                    confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, new ArrayList<>());
//                    rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
//                    if (searchview_campname_pincode.getText().toString().trim().length() == 6) {
//
//
//                        if (Utilities.isNetworkAvailable(context)) {
//                            new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE, talukaId, searchview_campname_pincode.getText().toString(), "1");
////                                getWorkerInfo();
////                                new GetWorkerInfroRe_Registration().execute(edt_workerregno.getText().toString().trim());
////                            searchview_campname_pincode.clearFocus();
//
//                            //       new GetWorkerInfo().execute("MH" + edt_workerregno.getText().toString().trim());
//
//                        } else {
//
//                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                        }
//
//
//                    } else {
//                        searchview_campname_pincode.setError("Enter 6 digit pincode");
//                    }
//                }
//            }
//        });


//                for (int i = 0; i < 5; i++) {
//                    Log.d("ThreadExample", "Running on a separate thread: " + i);
//                    try {
//                        Thread.sleep(1000); // Sleep for 1 second
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }


// Create a new thread with the runnable object
//        Thread thread = new Thread(runnable);

// Start the thread
//        thread.start();

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


        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchview_campname.clearFocus();
                ArrayList<CampBeneficiaryListModel.OutputBean> facilitySearchedList = new ArrayList<>();
                for (CampBeneficiaryListModel.OutputBean facility : campBeneficiaryList) {
                    if (facility.getBeneficiaryName() != null &&
                            facility.getBeneficiaryName().toLowerCase().startsWith(query.toLowerCase())
                            || facility.getPinCode() != null && facility.getPinCode().startsWith(query)) {
                        facilitySearchedList.add(facility);
                    }
                }
                if (facilitySearchedList.size() == 0) {
                    Utilities.showToastMessage("No Such Customer Name Found", context, false);
                    facilitySearchedList.clear();

                    confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, facilitySearchedList);
                    rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
//                    setRecyclerView(facilitySearchedList);
                } else {

                    confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, facilitySearchedList);
                    rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
//                    setRecyclerView(facilitySearchedList);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<CampBeneficiaryListModel.OutputBean> facilitySearchedList = new ArrayList<>();
                if (newText.equals("")) {
                    //searchView.clearFocus();
                    facilitySearchedList.clear();

                    confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, campBeneficiaryList);
                    rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);

//                    setRecyclerView(sampleCollectionModelList);
                    Log.e("TAG", "onQueryTextChange: ");
                } else {
                    for (CampBeneficiaryListModel.OutputBean facility : campBeneficiaryList) {
                        if (facility.getBeneficiaryName() != null &&
                                facility.getBeneficiaryName().toLowerCase().startsWith(newText.toLowerCase())
                                || facility.getPinCode() != null && facility.getPinCode().startsWith(newText)) {


                            facilitySearchedList.add(facility);
                        }
                    }

                    if (facilitySearchedList.size() == 0) {
                        Utilities.showToastMessage("No Such Customer Name Found", context, false);
                        facilitySearchedList.clear();
                        confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, facilitySearchedList);
                        rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
//                        setRecyclerView(facilitySearchedList);
                    } else {
                        confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, facilitySearchedList);
                        rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
//                        setRecyclerView(facilitySearchedList);
                    }
                }
                return true;
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


        tv_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetTaluka().execute(STATELGDCODE, DISTLGDCODE);
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

//                new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE,TALLGDCODE,"411025", "1");


            }
        });
        builderSingle.show();

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
            param.add(new ParamsPojo("FROMDATE", params[0]));
            param.add(new ParamsPojo("TODATE", params[1]));
            param.add(new ParamsPojo("USERID", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("TALLGDCODE", params[4]));
            param.add(new ParamsPojo("PINCODE", params[5]));
            param.add(new ParamsPojo("TYPE", params[6]));
            param.add(new ParamsPojo("DeptTypeId", params[7]));
            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));

            res = WebServiceCall.APICall(ApplicationConstants.GetT2T_CT_BeneficiaryDetailsforDistCoordinator_V4, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    CampBeneficiaryListModel pojoDetails = new Gson().fromJson(result, CampBeneficiaryListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {
                            int t = 0;
//                            for (CampBeneficiaryListModel.OutputBean  outut :
//                                    campBeneficiaryList) {
//
//
//                                 t = t + Integer.valueOf(outut.getMemberCount());
//
//
//                                tv_total.setText(""+t);
//
//
//                            }
                            // Initialize data list

//                            for (int i = 0; i < pageSize; i++) {
//
//                                filteredList.add(campBeneficiaryList.get(i));
//                            }
//
//                            confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, filteredList);
//                            rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);

                            ArrayList<CampBeneficiaryListModel.OutputBean> filteredList = new ArrayList<>();

                            for (int i = 0; i < pageSize; i++) {
                                try {
                                    filteredList.add(campBeneficiaryList.get(i));
                                } catch (Exception e) {
                                }
                            }
                            confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, campBeneficiaryList);
                            rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
                            confirmatoryTestTeamAssignAdapter.notifyDataSetChanged();
                            //   toggleFilter();
                        }
                    } else {
                        Utilities.showMessageString("Beneficiary List Not Found", context);
                        tv_total.setText("");
                        confirmatoryTestTeamAssignAdapter = new ConfirmatoryTestTeamAssignNewAdapter(context, new ArrayList<>());
                        rv_patient_list.setAdapter(confirmatoryTestTeamAssignAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class GetTaluka extends AsyncTask<String, Void, String> {

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


                            camptypelist.add(0, new TalukaModel().new Output("ALL", 0));


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

        private void showTalukaDialogue(final List<TalukaModel.Output> talukalist) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Select Taluka");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

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
                    tv_taluka.setText(talukalist.get(which).gettALNAME());
                    talukaId = String.valueOf(talukalist.get(which).gettLLGDCODE());
                    //  refreshCalendar();
                }
            });
            builderSingle.show();

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
            param.add(new ParamsPojo("Type", "1"));
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

                    //  refreshCalendar();
                }
            });
            builderSingle.show();

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
//                            rv_patient_list.setAdapter(new ConfirmatoryTestAdapter(context, dependentList));
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

    /// ///                            if (adminActiveInactiveModel.getOutput().size() > 0) {
    /// ///                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
    /// ///                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
    /// ///                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
    /// ///                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
    /// /
    /// /                            }
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
    private void getPrescription() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<DepartMentTypeModel> call = apiService.getPrescription();
        call.enqueue(new Callback<DepartMentTypeModel>() {
            @Override
            public void onResponse(Call<DepartMentTypeModel> call, Response<DepartMentTypeModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<DepartMentTypeModel.Output> outputItems = response.body().getOutput();

                        if (outputItems != null && !outputItems.isEmpty()) {

                            // Create "All" option
                            DepartMentTypeModel.Output allItem =
                                    response.body().new Output();
                            allItem.setDeptTypeId(0);
                            allItem.setDepartmentType("All");

                            // Add at first position
                            outputItems.add(0, allItem);

                            showPrescriptionList(outputItems);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Alert", message, false);
                    }
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
            public void onFailure(Call<DepartMentTypeModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    private void showPrescriptionList(final List<DepartMentTypeModel.Output> trenchList) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Deparment Type");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getDepartmentType());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv_prescribed_from.setText(trenchList.get(which).getDepartmentType());
                presciptioId = String.valueOf(trenchList.get(which).getDeptTypeId());


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

    @Override
    protected void onResume() {
        super.onResume();

        if (searchview_campname_pincode.getText().toString().isEmpty()) {

            new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE, talukaId, "0", Remark_Id,presciptioId);

        } else {

            new GetPostCampDetails().execute(fromDate, CampDateToDate, EmpCode, DISTLGDCODE, talukaId, searchview_campname_pincode.getText().toString(), Remark_Id,presciptioId);
        }
    }
}