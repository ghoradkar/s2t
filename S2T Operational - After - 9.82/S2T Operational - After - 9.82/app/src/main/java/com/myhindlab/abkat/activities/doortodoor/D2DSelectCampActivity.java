package com.myhindlab.abkat.activities.doortodoor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.AttendanceMarkedPatients_Activity;
import com.myhindlab.abkat.activities.CampClosingActivity;
import com.myhindlab.abkat.activities.HealthScreening_Activity;
import com.myhindlab.abkat.activities.UploadHealthCardPhoto_Activity;
import com.myhindlab.abkat.adapters.doortodoor.D2DCampListAdapter;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.UserAttendanceModel;
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class D2DSelectCampActivity extends AppCompatActivity {

    private Context context;
    private UserSessionManager sessionManager;
    private TextView tvAddress, tvToday, tvCampId;
    private MaterialEditText edt_district, edtCampDate;
    private String userId, userName, campDate = "",beneficiaryNo,Type,relation,rej_RegId,beneficiarName,rejCampId,
            distLgdCode = "0", designName, designId, DesgLevelId, selTaluka, selGp,
            labCode, SiteDetailId = "", campId,subOrgId ="0", districtName;
    private ProgressDialog pd;
    private int flag = 1;
    private ArrayList<D2DCampDetails.Output> campList;
    private RecyclerView recyclerView;
    String cDate = null;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hllselect_camp);


        initView();
        setUpToolbar();
//        if (new UserSessionManager(context).isHllUser()) {
//            getSessionDetails();
//        } else {
        getABKATSessionDetails();
        setDefault();
//        }
    }

    void initView() {
        context = D2DSelectCampActivity.this;
        pd = new ProgressDialog(this);
//        tvCampId = findViewById(R.id.tvCampId);
//        tvAddress = findViewById(R.id.tvAddress);
//        tvToday = findViewById(R.id.tvToday);
        edt_district = findViewById(R.id.edt_district);
        edtCampDate = findViewById(R.id.edtCampDate);
        recyclerView = findViewById(R.id.rvCampList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Intent intent = getIntent();
//        flag = intent.getIntExtra("flag", 1);





        if (getIntent() != null) {
            beneficiaryNo = getIntent().getStringExtra("beneficiaryNumber");
            relation = getIntent().getStringExtra("relation");
            rej_RegId = getIntent().getStringExtra("regId");
            rejCampId = getIntent().getStringExtra("rejCampId");
            beneficiarName = getIntent().getStringExtra("beneficiaryName");
            Type = getIntent().getStringExtra("Type");

            if (Type!=null){
                if (Type.equalsIgnoreCase("5")){

                    ConstantData constantData = ConstantData.getInstance();
                    constantData.setSetSitetypeName("Patient Registration");

                }
            }


            if (Type!=null){
                if (Type.equalsIgnoreCase("6")||Type.equalsIgnoreCase("7")){

                    ConstantData constantData = ConstantData.getInstance();
                    constantData.setSetSitetypeName("Patient Registration");

                }
            }
        }




        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


//        edt_district.setEnabled(false);

        campDate = Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear);
        edtCampDate.setText(campDate);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ConstantData constantData = ConstantData.getInstance();

                D2DCampDetails.Output campDetails = campList.get(position);
                constantData.setCalendarCampId(String.valueOf(campDetails.getCampId()));

                if (designId.equalsIgnoreCase("34") || designId.equalsIgnoreCase("29")|| designId.equalsIgnoreCase("141")
                        || (designId.equalsIgnoreCase("92") || (designId.equalsIgnoreCase("104")
                        || (designId.equalsIgnoreCase("160") || (designId.equalsIgnoreCase("105")
                        || designId.equalsIgnoreCase("77") || designId.equalsIgnoreCase("84") ||
                        designId.equalsIgnoreCase("30")||designId.equalsIgnoreCase("136")
                        ||designId.equalsIgnoreCase("139") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details")
                        || designId.equalsIgnoreCase("108") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details")
                        || designId.equalsIgnoreCase("147") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details")
                        || designId.equalsIgnoreCase("130") && constantData.getSetSitetypeName().equalsIgnoreCase("Health Screening Details")))))) {

//                    if (campDetails.getIsCampClosed().equalsIgnoreCase("1")){
//                        Utilities.showToastMessage("This Camp is closed", context, false);
//                        return;
//                    }

                    startActivity(new Intent(context, HealthScreening_Activity.class)
                            .putExtra("campId", campDetails.getCampId().toString())
                            .putExtra("campDetails", campDetails)
                            .putExtra("Date", cDate)
                            .putExtra("DISTLGDCODE", campDetails.getDistlgdcode().toString())
                            .putExtra("CampType", "3"));

                } else {
                    switch (constantData.getSetSitetypeName()) {
                        case "Acknowledgement":
                            startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                                    .putExtra("healthScreentype", "9")
                                    .putExtra("campId", campDetails.getCampId().toString())
                            );
                            break;
                        case "Health Card Upload":
                            startActivity(new Intent(context, UploadHealthCardPhoto_Activity.class)
                                    .putExtra("campId", campDetails.getCampId().toString())
                                    .putExtra("siteId", campDetails.getSiteDetailId().toString())
                            );
                            break;

//                    case "Patient Registration":
//                        startActivity(new Intent(context, D2DPatientRegistration_Activity.class)
//                                .putExtra("campId", campDetails.getCampId().toString())
//                                .putExtra("district", districtName)
//                                .putExtra("siteId", campDetails.getSiteDetailId().toString()));
//                        break;

//                    case "D2D Health Screening":
//                        startActivity(new Intent(context, HealthScreening_Activity.class)
//                                .putExtra("campId", campDetails.getCampId())
//                                .putExtra("Date", edtCampDate.getText().toString().trim())
//                                .putExtra("DISTLGDCODE", campDetails.getDistlgdcode())
//                                .putExtra("CampType", "3"));
//
//                        break;
                        case "D2d Camp Closing":
                            if (Integer.parseInt(designId) == 92) {
                                context.startActivity(new Intent(context, CampClosingActivity.class)
                                        .putExtra("Date", edtCampDate.getText().toString())
                                        .putExtra("campId", String.valueOf(campDetails.getCampId()))
                                        .putExtra("DISTLGDCODE", String.valueOf(campDetails.getDistlgdcode()))
                                        .putExtra("Type", "3"));
                            }

                            break;

//                        case "Heath Screening Staus":
//                            if (Integer.parseInt(designId) == 92){
//                                context.startActivity(new Intent(context, CampDetails_Activity.class)
//                                        .putExtra("Date", edtCampDate.getText().toString())
//                                        .putExtra("DISTLGDCODE", String.valueOf(campDetails.getDistlgdcode()))
//                                        .putExtra("campDetails", campDetails)
//                                        .putExtra("Type", "1"));
//                            }

//                            break;

                        default:
                            new GetUserCampMappingAndAttendanceStatus(campDetails, constantData).execute(edtCampDate.getText().toString(), userId, String.valueOf(campList.get(position).getDistlgdcode()), "3", String.valueOf(campList.get(position).getCampId()));
                            break;
                    }

                }

            }


        }));



        cDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        edt_district.setOnClickListener(v ->
        {
//            if (edt_district.getText().toString().trim().isEmpty()) {
//                edt_district.setError("Select district");
//                return;
//            }
//            if (!designId.equalsIgnoreCase("29") || !designId.equalsIgnoreCase("35") || !designId.equalsIgnoreCase("65") || !designId.equalsIgnoreCase("41") || !designId.equalsIgnoreCase("57") || !designId.equalsIgnoreCase("42") || !designId.equalsIgnoreCase("43") || !designId.equalsIgnoreCase("58") || !designId.equalsIgnoreCase("31")) {
//                new GetDistrictList().execute();
//            }

            new GetDistrictList().execute("2",userId);

        });

        edtCampDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        campDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, month + 1, year);
                        edtCampDate.setText(campDate);
                        if (Utilities.isNetworkAvailable(context)) {
//                            new GetCampDetails().execute(labCode, distLgdCode, userId);

                            getCampDetails
                                    (campDate,labCode,subOrgId,userId,designId);

                        } else {
                            Utilities.showToastMessage("Not Internet Connected", context, false);
                        }
                    }
                }, mYear, mMonth, mDay);
                try {
                    dialog.getDatePicker().setCalendarViewShown(false);
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.show();
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
//                    districtList = new ArrayList<>();
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            //  districtList.add(0, new DistrictList_Model("0", "All"));


//                            if (pojoDetails.getOutput().size() > 0) {
//                                DistrictList_Model output = pojoDetails.getOutput().get(0);
//
//                                designId = output.getDISTLGDCODE();
//                                edt_district.setText(output.getDISTNAME());
//
//
//                            }


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
                edt_district.setText(districtList.get(which).getDISTNAME());
                distLgdCode = districtList.get(which).getDISTLGDCODE();
                if (Utilities.isNetworkAvailable(context)) {
//                    new GetCampDetails().execute(labCode, distLgdCode, userId);

                    getCampDetails
                            (campDate,labCode,subOrgId,userId,designId);

                } else {

                }
            }
        });
        builderSingle.show();
    }


    private void getSessionDetails() {
        try {
            JSONArray user_info = new JSONArray(new UserSessionManager(context).getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userId = json.getString("EmpCode");
                distLgdCode = json.getString("DISTLGDCODE");
                districtName = json.getString("DISTNAME");
                DesgLevelId = json.getString("DESGLEVELID");
                designId = json.getString("DESGID");
                designName = json.getString("Designation");
                userName = json.getString("name");
                labCode = json.getString("Labcode");
//                edt_district.setText(json.getString("district"));

//                labCode = "28";
//                distLgdCode = "490";
                if (Utilities.isNetworkAvailable(context)) {
//                    new GetCampDetails().execute(labCode, distLgdCode, userId);

                    getCampDetails
                            (campDate,labCode,subOrgId,userId,designId);

                } else {

                }

                if (designId.equalsIgnoreCase("4") || designId.equalsIgnoreCase("105")) {
                    edt_district.setVisibility(View.GONE);
                } else {
                    edt_district.setVisibility(View.VISIBLE);

                }
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
                subOrgId = json.getString("SubOrgId");

                edt_district.setText(json.getString("district"));
                labCode = json.getString("LabCode");
                labCode = "0";
//                distLgdCode = "472";
                if (Utilities.isNetworkAvailable(context)) {
//                    new GetCampDetails().execute(labCode, distLgdCode, userId);

                    getCampDetails
                            (campDate,labCode,subOrgId,userId,designId);

                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDefault(){

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

    private class GetCampDetails extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();


            paramsPojos.add(new ParamsPojo("CampDate", campDate));
            paramsPojos.add(new ParamsPojo("LabCode", params[0] != null ? params[0] : "0"));
            paramsPojos.add(new ParamsPojo("DISTLGDCODE", params[1]));
            paramsPojos.add(new ParamsPojo("UserID", userId));

            //  res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsonLabForDoorToDoor, ApplicationConstants.webservice, paramsPojos);
            res = WebServiceCall.APICall(ApplicationConstants.GetCampDetailsonLabForDoorToDoor_V1, ApplicationConstants.webservice_d2d, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute RESULT: " + result);
            pd.dismiss();

            if (!result.equals("") || !result.equals("[]")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    D2DCampDetails d2DCampDetails = new Gson().fromJson(result, D2DCampDetails.class);

                    if (status.equalsIgnoreCase("fail")) {
                        Utilities.showToastMessage(msg, context, false);
                    } else {
                        campList = new ArrayList<>();
                        campList = d2DCampDetails.getOutput();
//                        campId = d2DCampDetails.getOutput().get(0).getCampId().toString();
//                        SiteDetailId = d2DCampDetails.getOutput().get(0).getSiteDetailId().toString();
//                        tvAddress.setText(d2DCampDetails.getOutput().get(0).getCampLocation());
//                        tvCampId.setText(d2DCampDetails.getOutput().get(0).getCampId().toString());

                        recyclerView.setAdapter(new D2DCampListAdapter(campList));

                    }
                } catch (JsonIOException | JSONException e) {
                    Log.e("", "onPostExecute: " + e.getMessage());
                }
            }


        }

    }



    private NetworkProgressDialog netDialog;
    private TrafficSpeedMonitor speedMonitor;
    // track per-request totals if you want
    private volatile long lastRespBytes = 0L;
    private volatile long lastReqBytes = 0L;


    private void getCampDetails(String campDate, String labCode, String subOrgId, String userId, String designId) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

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

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<D2DCampDetails> call = apiService.getCampDetailsOnLab(
                campDate,
                labCode != null ? labCode : "0",
                subOrgId,
                "0",
                distLgdCode,
                userId,
                designId
        );

        call.enqueue(new Callback<D2DCampDetails>() {
            @Override
            public void onResponse(Call<D2DCampDetails> call, Response<D2DCampDetails> response) {
                pd.dismiss();

//                netDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    D2DCampDetails d2DCampDetails = response.body();
                    String status = d2DCampDetails.getStatus();
                    String msg = d2DCampDetails.getMessage();

                    if (status.equalsIgnoreCase("fail")) {
                        Utilities.showToastMessage(msg, context, false);
                        recyclerView.setAdapter(new D2DCampListAdapter(new ArrayList<>()));
                    } else {
                        campList = new ArrayList<>(d2DCampDetails.getOutput());

                        // Update RecyclerView
                        recyclerView.setAdapter(new D2DCampListAdapter(campList));

                        // Optional: Uncomment below lines if you want to display address or campId
                        // tvAddress.setText(campList.get(0).getCampLocation());
                        // tvCampId.setText(String.valueOf(campList.get(0).getCampId()));
                    }
                } else {
                    Log.e("API", "Response unsuccessful or empty");
                }
            }

            @Override
            public void onFailure(Call<D2DCampDetails> call, Throwable t) {
                pd.dismiss();
                Log.e("API Error", "onFailure: " + t.getMessage());
                Utilities.showToastMessage("Something went wrong", context, false);
            }
        });
    }


    private class GetApprovedCampListDetailsForAppD2D extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";

            List<ParamsPojo> paramsPojos = new ArrayList<>();

            paramsPojos.add(new ParamsPojo("CampDate", campDate));
            paramsPojos.add(new ParamsPojo("UserId", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.GetApprovedCampListDetailsForAppD2D, ApplicationConstants.webservice, paramsPojos);

            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GETCSCPreCampInfo", "onPostExecute RESULT: " + result);
            pd.dismiss();

            if (!result.equals("") || !result.equals("[]")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    D2DCampDetails d2DCampDetails = new Gson().fromJson(result, D2DCampDetails.class);

                    if (status.equalsIgnoreCase("fail")) {
                        Utilities.showToastMessage(msg, context, false);
                    } else {
                        campList = new ArrayList<>();
                        campList = d2DCampDetails.getOutput();
//                        campId = d2DCampDetails.getOutput().get(0).getCampId().toString();
//                        SiteDetailId = d2DCampDetails.getOutput().get(0).getSiteDetailId().toString();
//                        tvAddress.setText(d2DCampDetails.getOutput().get(0).getCampLocation());
//                        tvCampId.setText(d2DCampDetails.getOutput().get(0).getCampId().toString());

                        recyclerView.setAdapter(new D2DCampListAdapter(campList));

                    }
                } catch (JsonIOException | JSONException e) {
                    Log.e("", "onPostExecute: " + e.getMessage());
                }
            }


        }

    }

    class GetUserCampMappingAndAttendanceStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;
        private D2DCampDetails.Output campDetails;
        private ConstantData constantData;

        private NetworkProgressDialog netDialog;
        private TrafficSpeedMonitor speedMonitor;
        // track per-request totals if you want
        private volatile long lastRespBytes = 0L;
        private volatile long lastReqBytes = 0L;

        public GetUserCampMappingAndAttendanceStatus(D2DCampDetails.Output campDetails, ConstantData constantData) {
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


//            res = WebServiceCall.APICallWithProgress(ApplicationConstants.GetUserCampMappingAndAttendanceStatus_Readiness, ApplicationConstants.webservice_d2d, param,pl);

            if (BuildConfig.isBeta){
                res = WebServiceCall.APICall(ApplicationConstants.GetUserCampMappingAndAttendanceStatus_Readiness_CampClose_V1, ApplicationConstants.webservice_d2d, param);

            }else {

                res = WebServiceCall.APICall(ApplicationConstants.GetUserCampMappingAndAttendanceStatus_Readiness_CampClose_V1, ApplicationConstants.webservice_d2d, param);

            }
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




                        ///new changes

                        if (userAttendanceModel.getOutput().get(0).getIsOldCampClosed() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "मागील दिवसाचा कॅम्प अजूनही  सुरू आहे. त्यामुळे नवीन patient registration करता येणार नाही.", false);
                            return;
                        }




                        if (userAttendanceModel.getOutput().get(0).getIsCampClosed() == 1) {
                            Utilities.showAlertDialog(context, "Alert", "This camp is closed", false);
                            return;
                        }


                        if (userAttendanceModel.getOutput().get(0).getIsReadinessFormFilled() == 0){
                            Utilities.showAlertDialog(context, "Alert", "Please fill camp readiness form", false);
                            return;
                        }

                        if (userAttendanceModel.getOutput().get(0).getCampFlag() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "This camp not mapped to you", false);
                            return;

                        }
                        else if (userAttendanceModel.getOutput().get(0).getAttendanceFlag() == 0) {
                            Utilities.showAlertDialog(context, "Attendance not marked", "Please mark attendance first ", false);
                            return;
                        }


                        if (userAttendanceModel.getOutput().get(0).getTeamMemberAttendance() == 1) {
                            Utilities.showAlertDialog(context, "Alert", "हा कॅम्प तुम्ही सुरू करू शकत नाही कारण \n" +
                                    "1. सर्व टीम सदस्यांची उपस्थिती नोंदवलेली नाही, किंवा \n" +
                                    "2. टीमचा फोटो अपलोड केलेला नाही, किंवा \n" +
                                    "3. टीम फोटो मॅनेजरने Approve केलेला नाही.", false);
                            return;
                        } else {
//                            if (designId.equalsIgnoreCase("") || designId.equalsIgnoreCase("")) {
                            ConstantData constantData = ConstantData.getInstance();
                            constantData.setCalendarCampId(String.valueOf(campDetails.getCampId()));
                            switch (constantData.getSetSitetypeName()) {
                                case "Health Screening Details":
                                    startActivity(new Intent(context, HealthScreening_Activity.class)
                                            .putExtra("campId", campDetails.getCampId().toString())
                                            .putExtra("Date", cDate)
                                            .putExtra("DISTLGDCODE", campDetails.getDistlgdcode().toString())
                                            .putExtra("CampType", "3"));
                                    break;
                                case "Acknowledgement":
                                    startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
                                            .putExtra("healthScreentype", "9")
                                            .putExtra("campId", campDetails.getCampId().toString())
                                    );
                                    break;
                                case "Health Card Upload":
                                    startActivity(new Intent(context, UploadHealthCardPhoto_Activity.class)
                                            .putExtra("campId", campDetails.getCampId().toString())
                                            .putExtra("siteId", campDetails.getSiteDetailId().toString())
                                    );
                                    break;
                                case "Patient Registration":
                                    startActivity(new Intent(context, D2DPatientRegistration_Activity.class)
                                            .putExtra("campId", campDetails.getCampId().toString())
                                            .putExtra("district", districtName)
                                            .putExtra("siteId", campDetails.getSiteDetailId().toString())
                                            .putExtra("beneficiaryNo", beneficiaryNo)
                                            .putExtra("relation", relation)
                                            .putExtra("regId", rej_RegId)
                                            .putExtra("rejCampId", rejCampId)
                                            .putExtra("beneficiarName", beneficiarName)
                                            .putExtra("Type", Type)

                                    );
                                    break;

                                case "D2D Health Screening":
                                    startActivity(new Intent(context, HealthScreening_Activity.class)
                                            .putExtra("campId", campDetails.getCampId())
                                            .putExtra("Date", edtCampDate.getText().toString().trim())
                                            .putExtra("DISTLGDCODE", campDetails.getDistlgdcode())
                                            .putExtra("CampType", "3"));

                                    break;
                                case "D2d Camp Closing":
                                    context.startActivity(new Intent(context, CampClosingActivity.class)
                                            .putExtra("Date", edtCampDate.getText().toString())
                                            .putExtra("campId", campId)
                                            .putExtra("DISTLGDCODE", getIntent().getStringExtra("DISTLGDCODE"))
                                            .putExtra("Type", "3"));
                                    break;

                                default:
                                    Utilities.showAlertDialog(context, "Alert", "Unknown Action", false);
                                    break;
                            }
//                            }

//                            else {
//                                startActivity(new Intent(context, AttendanceMarkedPatients_Activity.class)
//                                        .putExtra("healthScreentype", "16")
//                                        .putExtra("campId", campDetails.getCampId().toString()));
//
//                            }
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