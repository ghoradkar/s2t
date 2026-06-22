package com.myhindlab.abkat.activities.doortodoor;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.GetDocListD2D;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class D2DAvailabilityActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton rbOnline, rbOffline;
    private ArrayList<GetDocListD2D.Output> getDocListD2DArrayList;
    private Context context;
    private ConstantData constantData;
    private UserSessionManager session;
    private String name = "", Designation = "", DISTLGDCODE, userId;
    int DESGID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d2d_availability);


        setUpToolbar();
        init();
        setDefaults();
    }

    void init() {
        context = D2DAvailabilityActivity.this;
        session = new UserSessionManager(context);

        rbOnline = findViewById(R.id.rbOnline);
        rbOffline = findViewById(R.id.rbOffline);
        radioGroup = findViewById(R.id.rgOnline);


//        rbOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)
//                    new ChangeAvailabilityStatus().execute(userId, "1");
//
//            }
//        });

        rbOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbOnline.isChecked()) {
                    new ChangeAvailabilityStatus().execute(userId, "1");

                }
            }
        });
        rbOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbOffline.isChecked()) {
                    new ChangeAvailabilityStatus().execute(userId, "0");

                }
            }
        });


//        rbOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b)
//                    new ChangeAvailabilityStatus().execute(userId, "0");
//
//            }
//        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D2D Availability");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                name = json.getString("name");
                Designation = json.getString("Designation");
                DESGID = json.getInt("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                userId = String.valueOf(json.getInt("EmpCode"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GetD2dDoctors().execute();


    }


    class ChangeAvailabilityStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DocUserid", params[0]));
            param.add(new ParamsPojo("DocStatus", params[1]));
            param.add(new ParamsPojo("Createdby", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertCscDoctorAvailaibilityStatus, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "ChangeAvailabilityStatus: " + result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
//                    MainScreenCountsModel pojoDetails = new Gson().fromJson(result, MainScreenCountsModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    type = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (type.equalsIgnoreCase("success")) {
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

    private class GetD2dDoctors extends AsyncTask<String, Void, String> {

        ProgressDialog pd = new ProgressDialog(context);

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
            param.add(new ParamsPojo("DocUserid", userId));
            res = WebServiceCall.APICall(ApplicationConstants.GetDoctorListCSCCampAvailaible, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "GetD2dDoctors: " + result);
            try {
                pd.dismiss();
                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    GetDocListD2D pojoDetails = new Gson().fromJson(result, GetDocListD2D.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        getDocListD2DArrayList = pojoDetails.getOutput();
                        if (getDocListD2DArrayList.size() > 0) {

                            int docStatus = 0;
                            for (GetDocListD2D.Output output : getDocListD2DArrayList
                            ) {
                                if (output.getUserId() == Integer.parseInt(userId)) {
                                    docStatus = output.getDocStatus();
                                    break;
                                }

                            }
                            if (docStatus == 1) {
                                rbOnline.setChecked(true);
                            } else {
                                rbOffline.setChecked(true);
                            }
                        }
                    } else {
                        rbOffline.setChecked(true);

//                        Utilities.showAlertDialog(context, status, message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetUserCampMappingAndAttendanceStatus extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("CampType", params[0]));
            param.add(new ParamsPojo("CampID", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetUserCampMappingAndAttendanceStatus, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", "ChangeAvailabilityStatus: " + result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
//                    MainScreenCountsModel pojoDetails = new Gson().fromJson(result, MainScreenCountsModel.class);
                    JSONObject jsonObject = new JSONObject(result);
                    type = jsonObject.getString("status");
                    message = jsonObject.getString("message");
                    if (type.equalsIgnoreCase("success")) {
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

}

