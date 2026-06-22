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
import android.view.inputmethod.EditorInfo;
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
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campredinessnew.CampredinessNewActivity;
import com.myhindlab.abkat.adapters.PayoutDetailsAdapterMonthWiseNew;
import com.myhindlab.abkat.adapters.PayoutDetailsAdapterMonthWiseNew;
import com.myhindlab.abkat.adapters.ReportDeliveryCampIdAdapter;
import com.myhindlab.abkat.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.models.CampListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.GetTeamsModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PayoutDetailsModel;
import com.myhindlab.abkat.models.PostCampBeneficiaryListModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PayoutDetailsMonthWise extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;


    private TextView tv_block,tv_penaulty,tv_total_wcount,tv_worker_Count,tv_screening_pending,tv_rejected_BeneFi,tv_register_beneficiary, tv_from_date, tv_to_date, tv_teamNumber;
    private ArrayList<GetTeamsModel.OutputBean> teamList;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private SearchView searchview_campname;
//    private androidx.appcompat.widget.SearchView ;

    private RadioGroup rg_selection;

    private RadioButton rb_regId, rb_campId;
    private AlertDialog teamDialog;
    private List<CampListModel.OutputBean> assignteamlist;
    private RecyclerView rv_patient_list;
    PayoutDetailsAdapterMonthWiseNew postCampBeneficiaryAdapter;

    private ArrayList<DistrictList_Model> districtList;
    private ArrayList<TeamNumberModel.Output> teamlist;
    private LinearLayout ll_buttons, llTeamNumber, mainllCampId, mainLLDate, campDate, mainllCampType, llDistrict;
    private ImageView imvSearch;
    private CardView cardviewSearch;

    private PayoutDetailsModel.OutputBean beneficiaryDetails;

    String fromDate, toDate;


    private String campId = "0", callType, landinglabId = "0", isAdmin, UserId, campTypeId, EmpCode, STATELGDCODE = "2", DESGID, teamId = "0",paymentsheetId,
            district, TALLGDCODE, DISTLGDCODE, taluka, LabCode, CampDate, CampDateToDate, AssignedId;

    private LocalBroadcastManager localBroadcastManager;
    private ArrayList<PayoutDetailsModel.OutputBean> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_details_month_wise);

        initView();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void initView() {
        context = PayoutDetailsMonthWise.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);
        searchview_campname = findViewById(R.id.searchview_campname);
        tv_block = findViewById(R.id.tv_block);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_teamNumber = findViewById(R.id.tv_teamNumber);

        beneficiaryDetails = (PayoutDetailsModel.OutputBean) getIntent().getSerializableExtra("beneficiaryDetails");

        searchview_campname.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
        mainLLDate = findViewById(R.id.mainLLDate);
        cardviewSearch = findViewById(R.id.cardviewSearch);
        rb_campId = findViewById(R.id.rb_campId);
        rb_regId = findViewById(R.id.rb_regId);
        campDate = findViewById(R.id.campDate);
        mainllCampId = findViewById(R.id.mainllCampId);
        mainllCampType = findViewById(R.id.mainllCampType);
        tv_penaulty = findViewById(R.id.tv_penaulty);
        tv_total_wcount = findViewById(R.id.tv_total_wcount);
        tv_worker_Count = findViewById(R.id.tv_worker_Count);
        tv_screening_pending = findViewById(R.id.tv_screening_pending);
        tv_rejected_BeneFi = findViewById(R.id.tv_rejected_BeneFi);
        tv_register_beneficiary = findViewById(R.id.tv_register_beneficiary);


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -30);


//        tv_from_date.setText(Utilities.dfDate4.format(new Date()));
        tv_to_date.setText(Utilities.dfDate4.format(new Date()));
        tv_from_date.setText(Utilities.dfDate4.format(toCal.getTime()));

        fromDate = Utilities.dfDate4.format(toCal.getTime());
        CampDateToDate = Utilities.dfDate4.format(new Date());

//        tvDate.setText(Utilities.dfDate4.format(new Date()));
        CampDate = Utilities.dfDate4.format(new Date());


        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyy");
//        tv_from_date.setText(Utilities.getPreviousDate());
//        tv_to_date.setText(mdformat.format(calendar.getTime()));

        campBeneficiaryList = new ArrayList<>();


        teamId = beneficiaryDetails.getTeamID();
        paymentsheetId = beneficiaryDetails.getPaymentSheetID();

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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Performance & Payout Details");

//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            apiCall();
            String regId = intent.getStringExtra("regId");
            String campId = intent.getStringExtra("campId");
            Log.d("BroadcastReceiver", "onReceive: " + regId);


            if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131"))))) {

                if (Utilities.isNetworkAvailable(context)) {
                }

            } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
                llTeamNumber.setVisibility(View.VISIBLE);


                if (Utilities.isNetworkAvailable(context)) {
                }
            }

//

        }
    };


    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        callType = getIntent().getStringExtra("callType");
        isAdmin = getIntent().getStringExtra("isAdmin");


//        if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131"))))) {
//
//
//        } else {
//
//        }





        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    teamId,
                    EmpCode,
                    paymentsheetId
            );
        }


        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("MedicineDeliveryActivity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131"))))) {
            llTeamNumber.setVisibility(View.GONE);
//            llDistrict.setVisibility(View.VISIBLE);
            //  teamId = "0";

        } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
            llTeamNumber.setVisibility(View.VISIBLE);
//            llDistrict.setVisibility(View.GONE);
            // teamId = "";
        }

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

                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);


                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);


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




                                    if (DESGID.equalsIgnoreCase("92") || (DESGID.equalsIgnoreCase("29") || (DESGID.equalsIgnoreCase("104") || (DESGID.equalsIgnoreCase("162") || DESGID.equalsIgnoreCase("78") || DESGID.equalsIgnoreCase("77") || DESGID.equalsIgnoreCase("128") || DESGID.equalsIgnoreCase("131"))))) {

                                        if (Utilities.isNetworkAvailable(context)) {
                                        }

                                    } else if (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("35") || DESGID.equalsIgnoreCase("86"))) {
                                        llTeamNumber.setVisibility(View.VISIBLE);


                                        if (Utilities.isNetworkAvailable(context)) {
                                        }
                                    }


                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();


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

                if (tv_from_date.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select from date", context, false);
                    return;

                }
                if (tv_to_date.getText().toString().isEmpty()) {
                    Utilities.showToastMessage("Please select to date", context, false);
                    return;
                }


                if (Utilities.isNetworkAvailable(context)) {
                    new GetTeamId().execute();
                } else {
                    Utilities.showToastMessage("Please Check Your Connection", context, false);
                }
            }
        });


        imvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DESGID.equalsIgnoreCase("35") || (DESGID.equalsIgnoreCase("64") || (DESGID.equalsIgnoreCase("86")))) {
//                    if (tv_teamNumber.getText().toString().trim().isEmpty()) {
//                        Utilities.showToastMessage("Please Select Team Number", context, false);
//                        return;
//                    }

                } else {

                }


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
            param.add(new ParamsPojo("TeamID", params  [0]));
            param.add(new ParamsPojo("LoginUserID", params[1]));
            param.add(new ParamsPojo("PaymentSheetID", params[2]));


            Log.d("GetPostCampDetails", "doInBackground: " + new Gson().toJson(param));

            res = WebServiceCall.APICall(ApplicationConstants.GetUserPayoutCampDetails, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("TAG", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    PayoutDetailsModel pojoDetails = new Gson().fromJson(result, PayoutDetailsModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {

                            postCampBeneficiaryAdapter = new PayoutDetailsAdapterMonthWiseNew(context, campBeneficiaryList, "0", "0");
                            rv_patient_list.setAdapter(postCampBeneficiaryAdapter);



                            double t = 0;
                            int p = 0;
                            int q = 0;
                            int r = 0;
                            int s = 0;
                            int u = 0;

                            for (PayoutDetailsModel.OutputBean output :
                                    campBeneficiaryList) {

                                t =  t + (output.getPenaltyAmount());
                                p = p + (output.getBillableDependent());
                                q = q + (output.getBillableWorkers());
                                r = r + (output.getScreeningPending());
                                s = s + (output.getRejection());
                                u = u + (output.getTotalRegistartions());

                            }

                            tv_penaulty.setText( ""+t);
                            tv_total_wcount.setText( ""+p);
                            tv_worker_Count.setText( ""+q);
                            tv_screening_pending.setText( ""+r);
                            tv_rejected_BeneFi.setText( ""+s);
                            tv_register_beneficiary.setText( ""+u);



                        }
                    }else {
                        Utilities.showMessageString("Beneficiary List Not Found",context);

                        postCampBeneficiaryAdapter = new PayoutDetailsAdapterMonthWiseNew(context, new ArrayList<>(), "0", "0");
                        rv_patient_list.setAdapter(postCampBeneficiaryAdapter);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


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
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByUserId_MedicineDelivery, ApplicationConstants.webservice_d2d, param);

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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
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

                                if (Utilities.isNetworkAvailable(context)) {
                                }


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


}