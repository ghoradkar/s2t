package com.myhindlab.abkat.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.adapters.CampListAdapter;
import com.myhindlab.abkat.models.GetApprovedCampListDetailsForAppList;
import com.myhindlab.abkat.models.UserAttendanceModel;
import com.myhindlab.abkat.pojos.GetApprovedCampListDetailsForApp_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.NetworkProgressDialog;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.myhindlab.abkat.utilities.network_monitor.ProgressListener;
import com.myhindlab.abkat.utilities.network_monitor.TrafficSpeedMonitor;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampSelectionForHealthScreeing_Activity extends AppCompatActivity {
    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private String userId, userName, campDate = "", distLgdCode = "0", designName, designId, DesgLevelId, selTaluka, selGp, labCode, SiteDetailId = "", campId, subOrgId = "0", districtName;

    private MaterialEditText edt_campdate;
    private LinearLayout ll_campdetailslist;
    private SearchView searchview_campname;
    private RecyclerView recyclerview_campList;

    private ArrayList<GetApprovedCampListDetailsForAppList> campList;
    private ArrayList<GetApprovedCampListDetailsForAppList> searchCampList;
    private String userID = "", suborganization = "0";
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campselection_forhealthscreeing);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
        getABKATSessionDetails();

    }

    private void init() {
        edt_campdate = findViewById(R.id.edt_campdate);
        ll_campdetailslist = findViewById(R.id.ll_campdetailslist);
        searchview_campname = findViewById(R.id.searchview_campname);
        recyclerview_campList = findViewById(R.id.recyclerview_campList);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Camp");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        context = CampSelectionForHealthScreeing_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

        ll_campdetailslist.setVisibility(View.GONE);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
                subOrgId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getABKATSessionDetails() {
        UserSessionManager session = new UserSessionManager(context);
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                designId = json.getString("DESGID");
                labCode = json.getString("LabCode");
                subOrgId = json.getString("SubOrgId");
                labCode = "0";
//                distLgdCode = "472";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandler() {
        edt_campdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_campdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                if (Utilities.isNetworkAvailable(context)) {
//                                    new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID);

                                    getApprovedCampListDetails(
                                            edt_campdate.getText().toString().trim(), subOrgId, "0", "0", userID, designId
                                    );

                                } else {
                                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                }
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        searchview_campname.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (campList.size() > 0) {
                        for (GetApprovedCampListDetailsForAppList pojo : campList) {
                            String siteDetails = pojo.getCampId();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchCampList.add(pojo);
                            }
                        }

                        if (searchCampList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchCampList.addAll(campList);
                            showSiteListDialog(searchCampList);
                        } else {
                            showSiteListDialog(searchCampList);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCampList = new ArrayList<>();

                if (campList != null) {
                    if (newText.equals("")) {
                        searchCampList.addAll(campList);
                        showSiteListDialog(searchCampList);
                    } else {
                        if (campList.size() > 0) {
                            for (GetApprovedCampListDetailsForAppList pojo : campList) {
                                String siteDetails = pojo.getCampId();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchCampList.add(pojo);
                                }
                            }

                            if (searchCampList.size() == 0) {
                                searchCampList.addAll(campList);
                                showSiteListDialog(searchCampList);
                            } else {
                                showSiteListDialog(searchCampList);
                            }
                        }
                    }
                }
                return true;
            }
        });

        recyclerview_campList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                ConstantData constantData = ConstantData.getInstance();


                                GetApprovedCampListDetailsForAppList campDetails = campList.get(position);
                                constantData.setCalendarCampId(String.valueOf(campDetails.getCampId()));


                                if (designId.equalsIgnoreCase("29") || (designId.equalsIgnoreCase("92") || (designId.equalsIgnoreCase("104") || (designId.equalsIgnoreCase("160") || (designId.equalsIgnoreCase("105") || designId.equalsIgnoreCase("77") || designId.equalsIgnoreCase("84") || designId.equalsIgnoreCase("30") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details") || designId.equalsIgnoreCase("108") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details") || designId.equalsIgnoreCase("147") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details") || designId.equalsIgnoreCase("130") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details")))))) {
//                    if (campDetails.getIsCampClosed().equalsIgnoreCase("1")){
//                        Utilities.showToastMessage("This Camp is closed", context, false);
//                        return;
//                    }
                                    startActivity(new Intent(context, HealthScreening_Activity.class)
                                            .putExtra("campId", campDetails.getCampId().toString())
                                            .putExtra("campDetails", campDetails)
                                            .putExtra("Date", edt_campdate.getText().toString().trim())
                                            .putExtra("DISTLGDCODE", campDetails.getDISTLGDCODE().toString())
                                            .putExtra("CampType", campDetails.getCampType()));
                                } else {

                                    new GetUserCampMappingAndAttendanceStatus(campDetails, constantData).execute(edt_campdate.getText().toString(), userId, String.valueOf(campList.get(position).getDISTLGDCODE()), campList.get(position).getCampType(), String.valueOf(campList.get(position).getCampId()), "1");

                                }

//                                startActivity(new Intent(context, HealthScreening_Activity.class)
//                                        .putExtra("campId", campDetails.getCampId())
//                                        .putExtra("Date", edt_campdate.getText().toString().trim())
//                                        .putExtra("DISTLGDCODE", campDetails.getDISTLGDCODE())
//                                        .putExtra("CampType", campDetails.getCampType()));


                            }
                        }));
    }

    private class GetApprovedCampListDetailsForAppAPICall extends AsyncTask<String, Void, String> {

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

            //    res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetailsForApp, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetailsForApp_FlexiCamp, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Camp List", result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = new Gson().fromJson(result, GetApprovedCampListDetailsForApp_Pojo.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        searchview_campname.clearFocus();

                        campList = new ArrayList<>();
                        campList = pojoDetails.getOutput();

                        searchCampList = new ArrayList<>();
                        searchCampList.addAll(campList);

                        if (searchCampList.size() > 0) {
                            showSiteListDialog(searchCampList);
                        } else {
                            ll_campdetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_campdetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    ll_campdetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_campdetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
        }


    }

    private NetworkProgressDialog netDialog;
    private TrafficSpeedMonitor speedMonitor;
    // track per-request totals if you want
    private volatile long lastRespBytes = 0L;
    private volatile long lastReqBytes = 0L;

    private void getApprovedCampListDetails(String campDate, String subOrgId, String division, String distCode, String userId, String desgId) {
        final ProgressDialog progressDialog = new ProgressDialog(CampSelectionForHealthScreeing_Activity.this); // Change to actual activity name
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Track Network Speed
//        netDialog = new NetworkProgressDialog((Activity) context);
//        netDialog.setMessage("Contacting server...");
//        netDialog.show();
//
//        speedMonitor = new TrafficSpeedMonitor((sessionTx, sessionRx, upBps, downBps) -> {
//            final String up = humanReadableSpeed(upBps);
//            final String down = humanReadableSpeed(downBps);
//            ((Activity) context).runOnUiThread(() -> {
//                netDialog.updateUploadSpeed(up + "");
//                netDialog.updateDownloadSpeed(down + "");
//                netDialog.updateSentBytes(sessionTx);
//                netDialog.updateReceivedBytes(sessionRx);
//            });
//        }, true);
//        speedMonitor.start();

//        ProgressListener pl = new ProgressListener() {
//            @Override
//            public void onRequestProgress(long bytesWritten, long contentLength) {
//                lastReqBytes = bytesWritten;
//            }
//
//            @Override
//            public void onResponseProgress(long bytesRead, long contentLength) {
//                lastRespBytes = bytesRead;
//                ((Activity) context).runOnUiThread(() -> {
//                    netDialog.updateReceivedBytes(bytesRead);
//                    if (contentLength > 0) {
//                    }
//                });
//            }
//        };
        //End

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<GetApprovedCampListDetailsForApp_Pojo> call = apiService.getApprovedCampListDetails(
                campDate, subOrgId, division, distCode, userId, desgId
        );

        call.enqueue(new Callback<GetApprovedCampListDetailsForApp_Pojo>() {
            @Override
            public void onResponse(Call<GetApprovedCampListDetailsForApp_Pojo> call, Response<GetApprovedCampListDetailsForApp_Pojo> response) {
                progressDialog.dismiss();
//                netDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    GetApprovedCampListDetailsForApp_Pojo pojoDetails = response.body();
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        searchview_campname.clearFocus();

                        campList = new ArrayList<>(pojoDetails.getOutput());
                        searchCampList = new ArrayList<>(campList);

                        if (searchCampList.size() > 0) {
                            showSiteListDialog(searchCampList);
                        } else {
                            ll_campdetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_campdetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else {
                    ll_campdetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<GetApprovedCampListDetailsForApp_Pojo> call, Throwable t) {
                progressDialog.dismiss();
//                netDialog.dismiss();

                ll_campdetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Alert", t.getLocalizedMessage(), false);
            }
        });
    }


    private void showSiteListDialog(ArrayList<GetApprovedCampListDetailsForAppList> camplist) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview_campList.setLayoutManager(layoutManager);
        ll_campdetailslist.setVisibility(View.VISIBLE);
        recyclerview_campList.setAdapter(new CampListAdapter(camplist));

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchview_campname.clearFocus();

        if (Utilities.isNetworkAvailable(context)) {
//            new GetApprovedCampListDetailsForAppAPICall().execute(edt_campdate.getText().toString().trim(), userID);


            getApprovedCampListDetails(
                    edt_campdate.getText().toString().trim(), subOrgId, "0", "0", userID, designId
            );

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }


    }

    private class GetUserCampMappingAndAttendanceStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;
        private GetApprovedCampListDetailsForAppList campDetails;
        private ConstantData constantData;


        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;


        public GetUserCampMappingAndAttendanceStatus(GetApprovedCampListDetailsForAppList campDetails, ConstantData constantData) {
            this.campDetails = campDetails;
            this.constantData = constantData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
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


            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CampDATE", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("CampID", params[4]));
            param.add(new ParamsPojo("TestId", params[5]));
//            res = WebServiceCall.APICallWithProgress(ApplicationConstants.GetUserCampMappingAndAttendanceStatusForRegularCamp, ApplicationConstants.webservice_d2d, param, pl);
            res = WebServiceCall.APICall(ApplicationConstants.GetUserCampMappingAndAttendanceStatusForRegularCamp, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "GetUserAttendanceStatus: " + result);

//
//            if (speedMonitor != null) speedMonitor.stop();
//            if (netDialog != null && netDialog.isShowing()) netDialog.dismiss();

            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    UserAttendanceModel userAttendanceModel = new Gson().fromJson(result, UserAttendanceModel.class);

                    if (userAttendanceModel.getStatus().equalsIgnoreCase("success")) {

                        if (userAttendanceModel.getOutput().get(0).getIsCampClosed() == 1) {
                            Utilities.showAlertDialog(context, "Alert", "This camp is closed", false);
                            return;
                        }

                        if (userAttendanceModel.getOutput().get(0).getCampFlag() == 0) {
                            Utilities.showAlertDialog(context, "This camp not belongs to you", "This camp not mapped to you", false);
                            return;
                        } else if (userAttendanceModel.getOutput().get(0).getAttendanceFlag() == 0) {
                            Utilities.showAlertDialog(context, "Attendance not marked", "Please mark attendance first", false);
                            return;
                        } else {
                            startActivity(new Intent(context, HealthScreening_Activity.class)
                                    .putExtra("campId", campDetails.getCampId().toString())
                                    .putExtra("campDetails", campDetails)
                                    .putExtra("Date", edt_campdate.getText().toString().trim())
                                    .putExtra("DISTLGDCODE", campDetails.getDISTLGDCODE().toString())
                                    .putExtra("CampType", campDetails.getCampType()));

                        }


//                        JSONArray jsonArray = jsonObject.getJSONArray("output");
//
//                        if (type.equalsIgnoreCase("1")) {
//                            rbOnline.setChecked(true);
//                            Utilities.showAlertDialog(context, "Online", "You are online now", true);
//
//                        } else {
//                            rbOffline.setChecked(true);
//                            Utilities.showAlertDialog(context, "Offline", "You are offline now", true);
//
//                        }

//                        new GetD2dDoctors().execute(userId);

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

    private String humanReadableSpeed(double bytesPerSec) {
        if (bytesPerSec < 1024) return String.format(Locale.getDefault(), "%.0f B/s", bytesPerSec);
        double kb = bytesPerSec / 1024.0;
        if (kb < 1024) return String.format(Locale.getDefault(), "%.2f KB/s", kb);
        return String.format(Locale.getDefault(), "%.2f MB/s", kb / 1024.0);
    }

}
