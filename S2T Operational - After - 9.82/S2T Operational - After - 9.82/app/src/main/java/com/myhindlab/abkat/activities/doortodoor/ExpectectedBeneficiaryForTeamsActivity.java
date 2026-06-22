package com.myhindlab.abkat.activities.doortodoor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.doortodoor.CallingNumberAdapter;
import com.myhindlab.abkat.appointment_confirmation.AppoinmentConfirmationActivity;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.appointment_confirmation.adapters.CallListAdaptor;
import com.myhindlab.abkat.appointment_confirmation.adapters.CallListTeamAdaptor;
import com.myhindlab.abkat.appointment_confirmation.adapters.TeamsDetailsAssignedReportDeliveryAdapter;
import com.myhindlab.abkat.appointment_confirmation.callstatus.CallstatusResponse;
import com.myhindlab.abkat.appointment_confirmation.pojo.OutputItem;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.doortodoor.TeamCallingModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.NetworkProgressDialog;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.TrafficSpeedMonitor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpectectedBeneficiaryForTeamsActivity extends AppCompatActivity implements CallListTeamAdaptor.OnImvClick  {
    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;

    private AlertDialog teamDialog;

    private SearchView searchView;

    private TextView tvCallStatus,tvDate,tv_sr_no,tv_select_team;
    private ImageView iv_infoTeam;
    private String DESGID, EmpCode,call_statusid="0",assigncallId = "", LabCode, DISTLGDCODE = "0",teamId ="0",mobileNumber="", district, TALLGDCODE, taluka, STATELGDCODE = "2",size,
            divisioId = "0", camptypeId = "0", labId = "0",channekPartnerId,
            selectedLabID = "", resorceId, selectedLabName = "", exernalPhleboId, TeamID, CampId, CampDate, AssignedId = "", IsTeam;
    private RecyclerView rv_BeneficiaryList;
    private LinearLayout ll_select_team;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private CallListTeamAdaptor callListTeamAdaptor;
    private List<OutputItem> adminlist;
    private int flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expected_beneficiary_team);

        initView();
        getSessionData();
        setDefault();
        setEventHandlers();
        setUpToolbar();

    }



    private void initView() {

        context = ExpectectedBeneficiaryForTeamsActivity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        adminlist = new ArrayList<>();


        rv_BeneficiaryList = findViewById(R.id.rv_BeneficiaryList);
        tvDate = findViewById(R.id.tvDate);
        tv_sr_no = findViewById(R.id.tv_sr_no);
        tv_select_team = findViewById(R.id.tv_select_team);
        ll_select_team = findViewById(R.id.ll_select_team);
        iv_infoTeam = findViewById(R.id.iv_infoTeam);
        tvCallStatus = findViewById(R.id.tvCallStatus);
        searchView = findViewById(R.id.searchView);

        rv_BeneficiaryList.setLayoutManager(new LinearLayoutManager(context));



        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        tvDate.setText(Utilities.dfDate6.format(new Date()));
        CampDate = Utilities.dfDate6.format(new Date());


    }


    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
             //   DISTLGDCODE = json.getString("DISTLGDCODE");
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

    private void setEventHandlers() {

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year));
                              //  fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);

                               // tv_to_date.setText("");


                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate6, dayOfMonth, monthOfYear + 1, year);







                                if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("136")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("108")){
                                    new GetBeneficiaryListForMasJas().execute(EmpCode,CampDate);

                                }else {
                                    new GetBeneficiaryList().execute(EmpCode,CampDate);

                                }

                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);


                try {
                   //
                     dpd1.getDatePicker().setCalendarViewShown(false);



//                    dpd1.getDatePicker().setCalendarViewShown(false);
                  //  dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());




                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        tv_select_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchTeamData();
            }
        });


        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<OutputItem> searchPatientList = new ArrayList<>();

                if (adminlist != null) {
                    if (adminlist.size() > 0) {
                        for (OutputItem pojo : adminlist) {
                            String siteDetails = pojo.getBeneficiaryName();
                            String siteDetailsNew = pojo.getMobile();
                            String siteDetailsArea = pojo.getArea();
                            String siteDetailsBeneficiaryNo = pojo.getBeneficiaryNo();
                            String siteDetailsPincode = String.valueOf(pojo.getPincode());
                            if ((siteDetails != null && siteDetails.toLowerCase().startsWith(query.toLowerCase())
                            ) || siteDetailsNew != null && siteDetailsNew.startsWith(query)
                                    ||  siteDetailsBeneficiaryNo != null && siteDetailsBeneficiaryNo.startsWith(query)
                                    || siteDetailsArea != null && siteDetailsArea.toLowerCase().startsWith(query.toLowerCase())
                                    || siteDetailsPincode != null && siteDetailsPincode.toLowerCase().startsWith(query)

                            ) {
                                searchPatientList.add(pojo);
                            }
                        }

                        if (searchPatientList.size() == 0) {
                            Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Alert", "No record found.", false);
                            searchPatientList.addAll(adminlist);
                            searchPatientList.clear();
//                            rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, new ArrayList<>(),ExpectectedBeneficiaryForTeamsActivity.this));
//                            rv_BeneficiaryList.setAdapter(callListAdaptor);

                            rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, new ArrayList<>(),ExpectectedBeneficiaryForTeamsActivity.this));


                            //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                        } else {
//                            CallListAdaptor callListAdaptor = new CallListAdaptor(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList, ExpectectedBeneficiaryForTeamsActivity.this);
//                            rv_BeneficiaryList.setAdapter(callListAdaptor);

                            rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, searchPatientList,ExpectectedBeneficiaryForTeamsActivity.this));

                            //  rv_BeneficiaryList.setAdapter(new PostCampBeneficiaryAdapter(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList, isAdmin, callType));
                        }
                    }
                }
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                List<OutputItem> searchPatientList = new ArrayList<>();

                if (adminlist != null) {
                    if (newText.equals("")) {
                        searchPatientList.addAll(adminlist);
//                        CallListAdaptor callListAdaptor = new CallListAdaptor(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList, ExpectectedBeneficiaryForTeamsActivity.this);
//                        rv_BeneficiaryList.setAdapter(callListAdaptor);

                        rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, searchPatientList,ExpectectedBeneficiaryForTeamsActivity.this));

//                        rv_BeneficiaryList.setAdapter(new PostCampBeneficiaryAdapter(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList,false));
                    } else {
                        if (adminlist.size() > 0) {
                            for (OutputItem pojo : adminlist) {
                                String siteDetails = pojo.getBeneficiaryName();
                                String siteDetailsNew = pojo.getMobile();
                                String siteDetailsArea = pojo.getArea();
                                String siteDetailsBeneficiaryNo = pojo.getBeneficiaryNo();
                                String siteDetailsPincode = String.valueOf(pojo.getPincode());

                                if (siteDetails != null && siteDetails.toUpperCase().startsWith(newText.toUpperCase())
                                        || siteDetailsNew != null && siteDetailsNew.startsWith(newText)
                                        || siteDetailsBeneficiaryNo != null && siteDetailsBeneficiaryNo.startsWith(newText)
                                        || siteDetailsPincode != null && siteDetailsPincode.startsWith(newText)
                                        || siteDetailsArea != null && siteDetailsArea.toUpperCase().startsWith(newText.toUpperCase()
                                )) {
                                    searchPatientList.add(pojo);
                                }
                            }

                            if (searchPatientList.size() == 0) {
                                searchPatientList.addAll(adminlist);
//                                CallListAdaptor callListAdaptor = new CallListAdaptor(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList, ExpectectedBeneficiaryForTeamsActivity.this);
//                                rv_BeneficiaryList.setAdapter(callListAdaptor);

                                rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, searchPatientList,ExpectectedBeneficiaryForTeamsActivity.this));

                                //  rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            } else {
//                                CallListAdaptor callListAdaptor = new CallListAdaptor(ExpectectedBeneficiaryForTeamsActivity.this, searchPatientList, ExpectectedBeneficiaryForTeamsActivity.this);
//                                rv_BeneficiaryList.setAdapter(callListAdaptor);

                                rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, searchPatientList,ExpectectedBeneficiaryForTeamsActivity.this));

                                // rv_patient_list.setAdapter(new PostCampBeneficiaryAdapter(context, searchPatientList, isAdmin, callType));
                            }
                        }
                    }
                }
                return true;
            }
        });


        tvCallStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getapicall();
            }
        });

        iv_infoTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(ExpectectedBeneficiaryForTeamsActivity.this);
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


    }



    private void setDefault() {

        call_statusid = "2";

        tvCallStatus.setText("Confirmed");

        if (Utilities.isNetworkAvailable(context)) {


//            new GetBeneficiaryList().execute(EmpCode,CampDate);


            if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("136")||DESGID.equalsIgnoreCase("139")||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("108")){
                new GetBeneficiaryListForMasJas().execute(EmpCode,CampDate);

                ll_select_team.setVisibility(View.VISIBLE);
            }else {
                new GetBeneficiaryList().execute(EmpCode,CampDate);

            }
        }


        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);

// Adjust the hint size
        searchAutoComplete.setHintTextColor(Color.GRAY); // Optional: Set hint text color
        searchAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10); // Set text size in SP

    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Appointments Confirmed List");

        ImageView btn_registration = findViewById(R.id.iv_info);



        if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("136")||DESGID.equalsIgnoreCase("139")
                ||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("108")){
            btn_registration.setVisibility(View.GONE);
        }

        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, D2DSelectCampActivity.class)
                        .putExtra("Type", "6")

                );

            }
        });

//        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//
//        });

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
                                new android.app.AlertDialog.Builder(context);

                        View v = LayoutInflater.from(context)
                                .inflate(R.layout.calling_number, null, false);

                        builder.setView(v);
                        builder.setTitle("Call To Beneficiary");

                        RecyclerView recyclerView = v.findViewById(R.id.rv_d2dTeams);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setHasFixedSize(false);

                        // ✅ Create dialog ONLY ONCE
                        android.app.AlertDialog alertDialog = builder.create();

                        CallingNumberAdapter callingNumberAdapter =
                                new CallingNumberAdapter(context, teamCallingModel.getOutput(),
                                        new CallingNumberAdapter.OnCallClickListener() {
                                            @Override
                                            public void onCallClick(TeamCallingModel.Output data) {

                                                // ✅ Dismiss dialog
                                                alertDialog.dismiss();

                                                // ✅ Your logic
                                                String mobileNo = data.getMobileNo();
                                                mobileNumber = mobileNo;

                                                if (mobileNumber.equalsIgnoreCase("NA")) {
                                                    Utilities.showAlertDialog(context, "Alert", "Mobile number not available", false);
                                                    return;
                                                }


                                                new GetCallingStatus().execute(String.valueOf(assignCallID),EmpCode);


                                            }
                                        });

                        recyclerView.setAdapter(callingNumberAdapter);

                        // ✅ Close button
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                        // ✅ Show dialog
                        alertDialog.show();
                    }
                } else {
                    Utilities.showAlertDialog(context, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }

    @Override
    public void onImvclick(OutputItem item) {



        mobileNumber = item.getMobile();



        new GetCallingNumber(item.getAssignCallID()).execute(item.getBeneficiaryNo());

//        new GetCallingStatus().execute(String.valueOf(item.getAssignCallID()),EmpCode);



    }


    public class GetBeneficiaryList extends AsyncTask<String, Void, String> {


        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();


//            netDialog = new NetworkProgressDialog((Activity) context);
//            netDialog.setMessage("Contacting server...");
//            netDialog.show();
//
//            // start system-wide speed monitor
//            // start system-wide or app-only speed monitor (prefer app-only)
//            speedMonitor = new TrafficSpeedMonitor((sessionTx, sessionRx, upBps, downBps) -> {
//                final String up = humanReadableSpeed(upBps);
//                final String down = humanReadableSpeed(downBps);
//                // sessionTx/sessionRx are baseline-subtracted totals for this session
//                ((Activity) context).runOnUiThread(() -> {
//                    netDialog.updateUploadSpeed(up + ""); // per second
//                    netDialog.updateDownloadSpeed(down + "");
//                    netDialog.updateSentBytes(sessionTx);    // session total sent (B)
//                    netDialog.updateReceivedBytes(sessionRx); // session total received (B)
//                });
//            }, true); // <-- pass true to try per-UID (app-only)
//            speedMonitor.start();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("UserID", params[0]));
            param.add(new ParamsPojo("AppoinmentDate", params[1]));
            param.add(new ParamsPojo("AssignStatusID", call_statusid));

//            ProgressListener pl = new ProgressListener() {
//                @Override
//                public void onRequestProgress(long bytesWritten, long contentLength) {
//                    lastReqBytes = bytesWritten;
//                    // you can publish progress or update UI via runOnUiThread if needed
//                }
//
//                @Override
//                public void onResponseProgress(long bytesRead, long contentLength) {
//                    lastRespBytes = bytesRead;
//                    // update netDialog from background via runOnUiThread
//                    ((Activity) context).runOnUiThread(() -> {
//                        netDialog.updateReceivedBytes(bytesRead);
//                        // if contentLength > 0 you can show percent
//                        if (contentLength > 0) {
//                            // optional: netDialog.setProgress((int)((bytesRead*100)/contentLength));
//                        }
//                    });
//                }
//            };




            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryAppoinmentDetails_V1, ApplicationConstants.webservice_forcallist, param);
//            res = WebServiceCall.APICallWithProgress(ApplicationConstants.GetBeneficiaryAppoinmentDetails_V1, ApplicationConstants.webservice_forcallist, param,pl);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


//            if (speedMonitor != null) speedMonitor.stop();
//            if (netDialog != null && netDialog.isShowing()) netDialog.dismiss();

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    ReportlistResponse reportlistResponse = new Gson().fromJson(result, ReportlistResponse.class);
                    type = reportlistResponse.getStatus();
                    message = reportlistResponse.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = reportlistResponse.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, adminlist,ExpectectedBeneficiaryForTeamsActivity.this));


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }
//                            if (reportlistResponse.getOutput().size() > 0){
//                                OutputItem outputItem = reportlistResponse.getOutput().get(0);
//                                outputItem
//
//                            }
                            size = String.valueOf(adminlist.size());
                            tv_sr_no.setText(size);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, new ArrayList<>(),ExpectectedBeneficiaryForTeamsActivity.this));
                        tv_sr_no.setText("0");

//                        Utilities.showAlertDialog(context, "Fail", "No Beneficiary Available For Screening", false);

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }

        private String humanReadableSpeed(double bytesPerSec) {
            if (bytesPerSec < 1024) return String.format(Locale.getDefault(), "%.0f B/s", bytesPerSec);
            double kb = bytesPerSec / 1024.0;
            if (kb < 1024) return String.format(Locale.getDefault(), "%.2f KB/s", kb);
            return String.format(Locale.getDefault(), "%.2f MB/s", kb / 1024.0);
        }
    }
    public class GetBeneficiaryListForMasJas extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("AppoinmentDate", params[1]));
            param.add(new ParamsPojo("AssignStatusID", call_statusid));
            param.add(new ParamsPojo("TeamId", teamId));
//            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryAppoinmentDetails, ApplicationConstants.webservice_forcallist, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetBeneficiaryAppoinmentDetails_V1_MAS, ApplicationConstants.webservice_forcallist, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    ReportlistResponse reportlistResponse = new Gson().fromJson(result, ReportlistResponse.class);
                    type = reportlistResponse.getStatus();
                    message = reportlistResponse.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        adminlist = reportlistResponse.getOutput();
                        if (adminlist != null && adminlist.size() > 0) {

                            rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, adminlist,ExpectectedBeneficiaryForTeamsActivity.this));


//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                WorkingTeamCount.setText(String.valueOf(output.getWorkingTeamCount()));
//                                NotWorkingTeamCount.setText(String.valueOf(output.getNonWorkingTeamCount()));
//                                TotalTeamCount.setText(String.valueOf(output.getTotalTeamCount()));
//
//                            }
//                            if (reportlistResponse.getOutput().size() > 0){
//                                OutputItem outputItem = reportlistResponse.getOutput().get(0);
//                                outputItem
//
//                            }
                            size = String.valueOf(adminlist.size());
                            tv_sr_no.setText(size);

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_BeneficiaryList.setAdapter(new CallListTeamAdaptor(context, new ArrayList<>(),ExpectectedBeneficiaryForTeamsActivity.this));
                        tv_sr_no.setText("0");

//                        Utilities.showAlertDialog(context, "Fail", "No Beneficiary Available For Screening", false);

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

    public class GetCallingStatus extends AsyncTask<String, Void, String> {
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


                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ mobileNumber));
                        context.startActivity(intent);

                    }


                } else {
                    Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


    private void getapicall() {
        final ProgressDialog progressDialog = new ProgressDialog(ExpectectedBeneficiaryForTeamsActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.web_forcalllist().create(ApiInterface.class);
        Call<CallstatusResponse> call = apiService.CALLSTATUS_RESPONSE_CALL_New_For_Team(EmpCode);
        call.enqueue(new Callback<CallstatusResponse>() {
            @Override
            public void onResponse(Call<CallstatusResponse> call, Response<CallstatusResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<com.myhindlab.abkat.appointment_confirmation.callstatus.OutputItem> outputItems = response.body().getOutput();
                        if (outputItems.size() > 0) {
                            for (com.myhindlab.abkat.appointment_confirmation.callstatus.OutputItem o :
                                    outputItems) {
//                                if (o.getAssignStatusID() == 1) {
//                                    outputItems.remove(o);
//                                    break;
//                                }
                            }
                            showTrenchListDialog(outputItems);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CallstatusResponse> call, Throwable t) {
                progressDialog.dismiss();
                t.getLocalizedMessage();

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
                            Utilities.showToastMessage(R.string.msgt_emptylist, ExpectectedBeneficiaryForTeamsActivity.this, false);
                        }
                    } else {
                        Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Fail", data.getMessage(), false);
                    }
                } else {
                    Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Error", "Unexpected server response", false);
                }
            }

            @Override
            public void onFailure(Call<TeamsDetailsModel> call, Throwable t) {
                progressDialog.dismiss();
                Utilities.showAlertDialog(ExpectectedBeneficiaryForTeamsActivity.this, "Please Try Again", "Server Not Responding", false);
            }
        });
    }



    private void showAssignedTeamListDialog(final ArrayList<TeamsDetailsModel.OutputBean> assignteamlist) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(ExpectectedBeneficiaryForTeamsActivity.this);
        builderSingle.setTitle("Select Team");
        builderSingle.setCancelable(false);
        View dialogueView = getLayoutInflater().inflate(R.layout.recyclerview_dialogue_for_calling, null);
        builderSingle.setView(dialogueView);

        RecyclerView rvList = dialogueView.findViewById(R.id.rvList);
        EditText edt_search = dialogueView.findViewById(R.id.edt_search);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(ExpectectedBeneficiaryForTeamsActivity.this));

        rvList.setAdapter(new TeamsDetailsAssignedReportDeliveryAdapter(assignteamlist));

        rvList.addOnItemTouchListener(
                new RecyclerItemClickListener(ExpectectedBeneficiaryForTeamsActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                TeamsDetailsModel.OutputBean team = assignteamlist.get(position);
                                teamId = assignteamlist.get(position).getTeamid();

                                tv_select_team.setText(assignteamlist.get(position).getTeamName());
                                teamDialog.dismiss();


                                new GetBeneficiaryListForMasJas().execute(EmpCode,CampDate);



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



    private void showTrenchListDialog(final List<com.myhindlab.abkat.appointment_confirmation.callstatus.OutputItem> trenchList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(ExpectectedBeneficiaryForTeamsActivity.this);
        builderSingle.setTitle("Select Call Status");
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ExpectectedBeneficiaryForTeamsActivity.this, R.layout.list_row);
        for (int i = 0; i < trenchList.size(); i++) {
            arrayAdapter.add(trenchList.get(i).getAppointmentStatus());
        }
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvCallStatus.setText(trenchList.get(which).getAppointmentStatus());
                call_statusid = String.valueOf(trenchList.get(which).getAssignStatusID());



                if (call_statusid.equalsIgnoreCase("0")||call_statusid.equalsIgnoreCase("10")){
                    CampDate = "";
                    tvDate.setText("");
                }

//                new GetBeneficiaryList().execute(EmpCode,CampDate);


                if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("136")||DESGID.equalsIgnoreCase("139")
                        ||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("108")){
                    new GetBeneficiaryListForMasJas().execute(EmpCode,CampDate);

                }else {
                    new GetBeneficiaryList().execute(EmpCode,CampDate);

                }


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
    public void onResume() {
        super.onResume();

        if (Utilities.isNetworkAvailable(context)) {
//            new GetBeneficiaryList().execute(EmpCode,CampDate);


            if (DESGID.equalsIgnoreCase("92")||DESGID.equalsIgnoreCase("136")||DESGID.equalsIgnoreCase("139")
                    ||DESGID.equalsIgnoreCase("29")||DESGID.equalsIgnoreCase("108")){
                new GetBeneficiaryListForMasJas().execute(EmpCode,CampDate);

            }else {
                new GetBeneficiaryList().execute(EmpCode,CampDate);

            }

        } else {
            Utilities.showMessage(R.string.msgt_nointernetconnection, context);
        }

    }
}