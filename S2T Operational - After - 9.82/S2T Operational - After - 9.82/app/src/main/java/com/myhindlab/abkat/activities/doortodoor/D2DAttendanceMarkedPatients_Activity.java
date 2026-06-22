package com.myhindlab.abkat.activities.doortodoor;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.AttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.CloseBreastScreeningCamp;
import com.myhindlab.abkat.adapters.BreastScreeningPresentPatient_Adapter;
import com.myhindlab.abkat.adapters.PresentPatient_Adapter_PE;
import com.myhindlab.abkat.appointment_confirmation.pojo.CallingApiVirtualNumberModel;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TeamsDetailsModel;
import com.myhindlab.abkat.models.UserAttendanceForPhysicalExamModel.Output;
import com.myhindlab.abkat.models.UserAttendanceForPhysicalExamModel;
import com.myhindlab.abkat.models.doortodoor.CallRequest;
import com.myhindlab.abkat.models.doortodoor.CallRequestNew;
import com.myhindlab.abkat.pojos.PresentPatientList_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.NetworkProgressDialog;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.TrafficSpeedMonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class D2DAttendanceMarkedPatients_Activity extends AppCompatActivity implements PresentPatient_Adapter_PE.OnImvClickPE {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog pd;
    private UserSessionManager session;
    private RecyclerView rv_patientlist;
    private Button btn_attendance;

    private String apiKeyForMyOperator = "",
            companyID = "",
            rider_company_id = "",
            rider_public_ivr_id = "",
            secrateToken = "",
            public_IVR_ID = "",
            typeForMyOperator = "";
    private String campId, healthScreentype, breastScreeningDeviceId, breastScreeningDeviceName,Is24By7IsAccountCreated = "0",
            referenceId,agentId,executivemobileNumber,myOperator_UserID,mobileNumber,apiKey,doctorMobile, apiKeyNew, virtualNumber, virtualNumberNew, regId;
    private ArrayList<UserAttendanceForPhysicalExamModel.Output> patientList;
    private SearchView searchView;
    private GetApprovedCampListDetailsForAppList selectedCamp;

    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastManager localBroadcastManager1;
    private String userID = "",teamId="",oganizationId = "",DESGID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attenmarked_patients_pe);

        init();
        setUpToolbar();
        getSessionData();
        setDefault();
        setEventHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
        searchView.setQuery("", false);

    }

    private void init() {
        context = D2DAttendanceMarkedPatients_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rv_patientlist = findViewById(R.id.rv_patientlist);
        rv_patientlist.setLayoutManager(new LinearLayoutManager(context));
        searchView = findViewById(R.id.searchView);
        btn_attendance = findViewById(R.id.btn_attendance);
        patientList = new ArrayList<>();

    }

    private void setDefault() {
        campId = getIntent().getStringExtra("campId");
        healthScreentype = getIntent().getStringExtra("healthScreentype");
        breastScreeningDeviceId = getIntent().getStringExtra("breastScreeningDeviceId");
        breastScreeningDeviceName = getIntent().getStringExtra("breastScreeningDeviceName");
        selectedCamp = (GetApprovedCampListDetailsForAppList) getIntent().getSerializableExtra("selectedCamp");

        if (Utilities.isNetworkAvailable(context)) {
            new GetTeamId().execute();
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter = new IntentFilter("D2DAttendanceMarkedPatients_Activity");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        localBroadcastManager1 = LocalBroadcastManager.getInstance(context);
        IntentFilter intentFilter1 = new IntentFilter("D2DAttendanceMarkedPatients_ActivityFinish");
        localBroadcastManager1.registerReceiver(broadcastReceiver1, intentFilter1);

//        if (healthScreentype.equals("13")) {
//            btn_attendance.setVisibility(View.VISIBLE);
//        } else {
        btn_attendance.setVisibility(View.GONE);
//        }


        new GetUserCreatedBy24By7().execute(userID);

        new GetOrganizationNew().execute(userID, DESGID);
    }

    private void setEventHandler() {
        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(context, PatientAttendance_Activity.class).putExtra("campId", campId));
                startActivity(new Intent(context, CloseBreastScreeningCamp.class)
                        .putExtra("selectedCamp", selectedCamp)
                        .putExtra("breastScreeningDeviceId", breastScreeningDeviceId)
                        .putExtra("breastScreeningDeviceName", breastScreeningDeviceName));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (!query.equals("")) {
                    ArrayList<UserAttendanceForPhysicalExamModel.Output> patientSearchedList = new ArrayList<>();
                    for (UserAttendanceForPhysicalExamModel.Output patientDetails : patientList) {
                        String patientToBeSearched = patientDetails.getEnglishName().toLowerCase() + String.valueOf(patientDetails.getRegdNo()).toLowerCase();
                        if (patientToBeSearched.contains(query.toLowerCase())) {
                            patientSearchedList.add(patientDetails);
                        }
                    }
                    rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientSearchedList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                } else {
                    rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    ArrayList<UserAttendanceForPhysicalExamModel.Output> patientSearchedList = new ArrayList<>();
                    for (UserAttendanceForPhysicalExamModel.Output patientDetails : patientList) {
                        String patientToBeSearched = patientDetails.getEnglishName().toLowerCase() + String.valueOf(patientDetails.getRegdNo()).toLowerCase();
                        if (patientToBeSearched.contains(newText.toLowerCase())) {
                            patientSearchedList.add(patientDetails);
                        }
                    }
                    rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientSearchedList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                } else if (newText.equals("")) {
                    rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                }
                return true;
            }
        });

    }

    private void getSessionData() {
        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                DESGID = json.getString("DESGID");
                agentId = json.getString("AgentID");
                executivemobileNumber = json.getString("BMobile");
                myOperator_UserID = json.getString("MyOperator_UserID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImvClickPE(UserAttendanceForPhysicalExamModel.Output item) {

        regId = String.valueOf(item.getRegdId());
        mobileNumber = item.getMobileNo();
        doctorMobile  = item.getDoctorMobile();

        if (Is24By7IsAccountCreated.equalsIgnoreCase("0")){
            new GetCallingStatus().execute(String.valueOf(item.getRegdId()),healthScreentype,userID);

        }else {
            new GetCallingStatusNew().execute(String.valueOf(item.getRegdId()),healthScreentype,userID);
        }


//        new GetCallingStatus().execute(String.valueOf(item.getRegdId()),healthScreentype,userID);

    }


    private class GetUserAttendancesUsingSitedetailsID extends AsyncTask<String, Void, String> {

        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();


            netDialog = new NetworkProgressDialog((Activity) context);
            netDialog.setMessage("Contacting server...");
            netDialog.show();

            // start system-wide speed monitor
            // start system-wide or app-only speed monitor (prefer app-only)
            speedMonitor = new TrafficSpeedMonitor((sessionTx, sessionRx, upBps, downBps) -> {
                final String up = humanReadableSpeed(upBps);
                final String down = humanReadableSpeed(downBps);
                // sessionTx/sessionRx are baseline-subtracted totals for this session
                ((Activity) context).runOnUiThread(() -> {
                    netDialog.updateUploadSpeed(up + ""); // per second
                    netDialog.updateDownloadSpeed(down + "");
                    netDialog.updateSentBytes(sessionTx);    // session total sent (B)
                    netDialog.updateReceivedBytes(sessionRx); // session total received (B)
                });
            }, true); // <-- pass true to try per-UID (app-only)
            speedMonitor.start();


        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();

            // progress listener for per-request progress
            ProgressListener pl = new ProgressListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength) {
                    lastReqBytes = bytesWritten;
                    // you can publish progress or update UI via runOnUiThread if needed
                }

                @Override
                public void onResponseProgress(long bytesRead, long contentLength) {
                    lastRespBytes = bytesRead;
                    // update netDialog from background via runOnUiThread
                    ((Activity) context).runOnUiThread(() -> {
                        netDialog.updateReceivedBytes(bytesRead);
                        // if contentLength > 0 you can show percent
                        if (contentLength > 0) {
                            // optional: netDialog.setProgress((int)((bytesRead*100)/contentLength));
                        }
                    });
                }
            };
//            if (!healthScreentype.equals("13")) {
//                param.add(new ParamsPojo("EmpCode", params[0]));
//                param.add(new ParamsPojo("DistrictId", "0"));
//                param.add(new ParamsPojo("TestId", params[1]));
//                res = WebServiceCall.APICall(ApplicationConstants.GetUserAttendancesUsingSitedetailsID_Anti, ApplicationConstants.webservice, param);
//            } else {
            param.add(new ParamsPojo("SiteDetailId", params[0]));
            param.add(new ParamsPojo("DistrictId", "0"));
            param.add(new ParamsPojo("TestId", params[1]));
            param.add(new ParamsPojo("TeamId", teamId));
            param.add(new ParamsPojo("UserId", userID));
            res = WebServiceCall.APICallWithProgress(ApplicationConstants.GetuserAttendanceForSitedetailsID_PhysicalExam, ApplicationConstants.webservice_d2d, param,pl);
//            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("AttendancePatient", result);
//            pd.dismiss();

            if (speedMonitor != null) speedMonitor.stop();
            if (netDialog != null && netDialog.isShowing()) netDialog.dismiss();

            swipeRefreshLayout.setRefreshing(false);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    patientList = new ArrayList<>();
                    UserAttendanceForPhysicalExamModel pojoDetails = new Gson().fromJson(result, UserAttendanceForPhysicalExamModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        patientList.addAll(pojoDetails.getOutput());
                        if (patientList.size() > 0) {

                            if (healthScreentype.equals("13")) {

                                UserAttendanceForPhysicalExamModel.Output presentPatientDetails = patientList.get(0);
//                                if (presentPatientDetails.getScreeningDoneCnt() >= 50) {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                    builder.setTitle("Alert");
//                                    builder.setCancelable(false);
//                                    builder.setMessage("You have done 50 screenings for today");
//                                    builder.setNegativeButton("GO BACK", (dialog, which) -> finish());
//                                    if (selectedCamp.getFLAG().equals("0")) {
//                                        builder.setPositiveButton("Close Screening", (dialog, which) -> {
//                                            startActivity(new Intent(context, CloseBreastScreeningCamp.class)
//                                                    .putExtra("selectedCamp", selectedCamp)
//                                                    .putExtra("breastScreeningDeviceId", breastScreeningDeviceId)
//                                                    .putExtra("breastScreeningDeviceName", breastScreeningDeviceName));
//                                            finish();
//                                        });
//                                    }
//                                    AlertDialog alertD = builder.create();
//                                    alertD.show();
//                                    return;
//                                }

                                ArrayList<UserAttendanceForPhysicalExamModel.Output> femalePatientList = new ArrayList<>();

                                for (UserAttendanceForPhysicalExamModel.Output patientDetails : patientList) {
                                    if (patientDetails.getGender().equalsIgnoreCase("F")) {
                                        femalePatientList.add(patientDetails);
                                    }
                                }

                                patientList.clear();
                                patientList.addAll(femalePatientList);

//                                rv_patientlist.setAdapter(new BreastScreeningPresentPatient_Adapter(context,
//                                        patientList,
//                                        campId,
//                                        healthScreentype,
//                                        breastScreeningDeviceId,
//                                        breastScreeningDeviceName));
                            } else {
                                rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                            }
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                        Utilities.showAlertDialog(context, "Fail", "Patient List Not Found", false);
                    }
                }
                searchView.clearFocus();
            } catch (Exception e) {
                e.printStackTrace();
                rv_patientlist.setAdapter(new PresentPatient_Adapter_PE(context, patientList, campId, healthScreentype,D2DAttendanceMarkedPatients_Activity.this::onImvClickPE));
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }


        private String humanReadableSpeed(double bytesPerSec) {
            if (bytesPerSec < 1024) return String.format(Locale.getDefault(), "%.0f B/s", bytesPerSec);
            double kb = bytesPerSec / 1024.0;
            if (kb < 1024) return String.format(Locale.getDefault(), "%.2f KB/s", kb);
            return String.format(Locale.getDefault(), "%.2f MB/s", kb / 1024.0);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient List");
        ImageView iv_info = findViewById(R.id.iv_info);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(D2DAttendanceMarkedPatients_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.colour_info_dialoge_for_physical_examination);
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


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);
        }
    };

    private BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        localBroadcastManager1.unregisterReceiver(broadcastReceiver1);
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
            param.add(new ParamsPojo("campid", campId));
            param.add(new ParamsPojo("UserID", String.valueOf(session.getUserDetailsJson().getEmpCode())));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }

                    } else {
                        Utilities.showAlertDialog(context, "Error", "Unable to get team id", false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }
    private class GetCallStatus extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("RegdId", campId));
            param.add(new ParamsPojo("TestID", healthScreentype));
            param.add(new ParamsPojo("CreatedBy", String.valueOf(session.getUserDetailsJson().getEmpCode())));
            res = WebServiceCall.APICall(ApplicationConstants.GetTeamNumberByCampIdAndUSerId, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("output");

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        teamId = jsonObject1.getString("TeamNumber");
                        if (Utilities.isNetworkAvailable(context)) {
                            new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }


                    } else {
                        Utilities.showAlertDialog(context, "Error", "Unable to get team id", false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
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
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("TestID", params[1]));
            param.add(new ParamsPojo("CreatedBy", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertCallDetails, ApplicationConstants.webservice_d2d, param);
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
                        intent.setData(Uri.parse("tel:" + doctorMobile));
                        startActivity(intent);


                        if (Utilities.isNetworkAvailable(context)) {
                            new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }

                    }else {
                        Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Fail", message, false);

                    }


                } else {
                    Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Fail", "Server not responding", false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


    public class GetUserCreatedBy24By7 extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetIs24By7IsAccountCreatedFlag, ApplicationConstants.webservice_d2d, param);
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
                    // selectedCampList = new ArrayList<>();
                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);



                            Is24By7IsAccountCreated = output.getIs24By7IsAccountCreated();






                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Data not available", false);
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


//                                GetApiKey().execute(oganizationId);

                              new  GetApiKeyNew().execute(oganizationId, userID);
                                new GetApiKeyForMyOperator().execute(oganizationId, userID);



//                                edt_organization.setText(output.getSubOrgName());


//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new CampCalendar_Activity.GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }


                            }


//                            showOrganizationListDialog(organizationList);
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


    public class GetApiKeyForMyOperator extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("OrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetOrganisationWiseAPIKey_V1, ApplicationConstants.webservice_d2d, param);
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
                    // selectedCampList = new ArrayList<>();
                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        //  campList = HomeLabHublabDetailsModel.getOutput();
                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);


                            apiKeyForMyOperator = output.getAPIKey();
                            companyID = output.getCompanyID();
                            public_IVR_ID = output.getPublicId();
                            secrateToken = output.getSecrateToken();
                            typeForMyOperator = output.getType();
                            rider_company_id = output.getRiderCompanyId();
                            rider_public_ivr_id = output.getRiderPublicIvrId();


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", message, false);
            }
        }
    }



    public class GetCallingStatusNew extends AsyncTask<String, Void, String> {


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
            param.add(new ParamsPojo("RegdId", params[0]));
            param.add(new ParamsPojo("TestID", params[1]));
            param.add(new ParamsPojo("CreatedBy", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertBeneficiaryCallingLog_V2, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            pd.dismiss();
            String type = "", message = "";
            try {
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    TeamsDetailsModel teamsDetailsModel = new Gson().fromJson(result, TeamsDetailsModel.class);
                    type = teamsDetailsModel.getStatus();
                    message = teamsDetailsModel.getMessage();
                    referenceId = teamsDetailsModel.getID();

                    if (type.equalsIgnoreCase("success")) {
                        if (Is24By7IsAccountCreated.equalsIgnoreCase("1")) {
                            if (agentId.equalsIgnoreCase("0")) {
                                getInitiateCallNew();
                            } else {
                                getInitiateCall();
                            }
                        }else if (Is24By7IsAccountCreated.equalsIgnoreCase("2")){
                            getMyOperatorCall();

                        }else {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + doctorMobile));
                            startActivity(intent);
                        }


//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        intent.setData(Uri.parse("tel:"+ mobileNumber));
//                        startActivity(intent);


//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        String phoneNumber = b_mobile;
//                        intent.setData(Uri.parse("tel:" + phoneNumber));
//                        startActivity(intent);

                    }


                } else {
                    Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Fail", message, false);
                }

            } catch (Exception e) {
                e.printStackTrace();

                Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Please Try Again", "Server not responding", false);
            }
        }
    }


    private void getInitiateCallNew() {
        final ProgressDialog progressDialog = new ProgressDialog(D2DAttendanceMarkedPatients_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.twentyFourBySeven_callNew().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.TwentyFourBySeven_CALLNew(apiKeyNew, executivemobileNumber, mobileNumber, virtualNumberNew, referenceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("statusCode");
                        String msg = jsonObject.getString("statusMessage");
//                        String data = jsonObject.getString("data");

                        if (status.equalsIgnoreCase("200")) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + virtualNumberNew));
                            startActivity(intent);

                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Success", msg, true);


                        } else {

                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Fail", msg, false, "Try Again!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    getInitiateCallNew();

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



    private void getInitiateCall() {
        final ProgressDialog progressDialog = new ProgressDialog(D2DAttendanceMarkedPatients_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.twentyFourBySeven_call().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.TwentyFourBySeven_CALL(apiKey, mobileNumber, virtualNumber, "json", agentId, referenceId);
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

                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Success", data, true);


                        } else {

                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Fail", data, false, "Try Again!", new DialogInterface.OnClickListener() {
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



    private void getMyOperatorCall() {
        final ProgressDialog progressDialog = new ProgressDialog(D2DAttendanceMarkedPatients_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface api = ApiClient.getMyOperator().create(ApiInterface.class);

        CallRequestNew request = new CallRequestNew(
                rider_company_id,
                secrateToken,
                typeForMyOperator,
                "+91"+executivemobileNumber,
                "+91" +doctorMobile,
                rider_public_ivr_id,
                referenceId,
                0,
                "",
                "",
                "",
                false
        );

        Call<ResponseBody> call = api.makeOBDCallNew(apiKeyForMyOperator, request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("details");

                        if (status.equalsIgnoreCase("success")) {
//                            Toast.makeText(Expected_BeneficiariesActivity.this, "Success: " + message, Toast.LENGTH_SHORT).show();

//                            if (userSessionManager.isHllUser() && healthScreentype.equalsIgnoreCase("16")) {
//                               GetTeamId().execute(campId, healthScreentype);
//                            }

                            new GetUserAttendancesUsingSitedetailsID().execute(campId, healthScreentype);



                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Success", "Request accepted successfully", true);

                        } else {
//                            Toast.makeText(Expected_BeneficiariesActivity.this, "Failed: " + message, Toast.LENGTH_SHORT).show();
                            Utilities.showAlertDialog(D2DAttendanceMarkedPatients_Activity.this, "Failed", message, true);

                        }

                        Log.d("API_SUCCESS", responseString);
                    } else {
                        String errorString = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorString);
                        String errorMessage = errorJson.optString("details", "Unknown error");
                        Toast.makeText(D2DAttendanceMarkedPatients_Activity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", errorString);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(D2DAttendanceMarkedPatients_Activity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(D2DAttendanceMarkedPatients_Activity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", t.getMessage(), t);
            }
        });
    }



    public class GetApiKeyNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("OrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.GetOrganisationWiseAPIKey, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    List<CallingApiVirtualNumberModel.Output> campList = new ArrayList<>();

                    CallingApiVirtualNumberModel callingApiVirtualNumberModel = new Gson().fromJson(result, CallingApiVirtualNumberModel.class);
                    type = callingApiVirtualNumberModel.getStatus();
                    message = callingApiVirtualNumberModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        if (callingApiVirtualNumberModel.getOutput().size() > 0) {
                            CallingApiVirtualNumberModel.Output output = callingApiVirtualNumberModel.getOutput().get(0);


                            apiKeyNew = output.getAPIKey();
                            virtualNumberNew = output.getServieNumber();


                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", message, false);
            }
        }
    }


}
