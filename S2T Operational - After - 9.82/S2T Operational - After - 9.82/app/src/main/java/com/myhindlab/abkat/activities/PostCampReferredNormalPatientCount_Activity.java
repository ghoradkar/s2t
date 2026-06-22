package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostCampReferredNormalPatientCount_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private CardView ll_referred_patients, ll_non_referred_patient;
    private TextView tv_referred_patient, tv_non_referred_patient;

    private String campId, isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_ref_non_ref_patient_count);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = PostCampReferredNormalPatientCount_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        ll_referred_patients = findViewById(R.id.ll_referred_patients);
        ll_non_referred_patient = findViewById(R.id.ll_non_referred_patient);
        tv_referred_patient = findViewById(R.id.tv_referred_patient);
        tv_non_referred_patient = findViewById(R.id.tv_non_referred_patient);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Count");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setDefaults() {
        campId = getIntent().getStringExtra("campId");
        isAdmin = getIntent().getStringExtra("isAdmin");

        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    "0",
                    "0",
                    "0",
                    "2",
                    "0",
                    campId,
                    "0",
                    "0"
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class GetPostCampDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Month", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            param.add(new ParamsPojo("IsComplete", params[4]));
            param.add(new ParamsPojo("CAMPID", params[5]));
            param.add(new ParamsPojo("IsReferred", params[6]));
            param.add(new ParamsPojo("RegdID", params[7]));
            res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray array = obj.getJSONArray("output");
                        JSONObject jsonObject = array.getJSONObject(0);

                        String Referred = jsonObject.getString("Referred");
                        String Normal = jsonObject.getString("Normal");
                        tv_referred_patient.setText(Referred);
                        tv_non_referred_patient.setText(Normal);
                    }
//                    else {
//                        Utilities.showAlertDialog(context, status, message, false);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setEventHandler() {
        ll_referred_patients.setOnClickListener(v -> {
            startActivity(new Intent(context, PostCampPatientList_Activity.class)
                    .putExtra("campId", campId)
                    .putExtra("callType", "1")
                    .putExtra("isAdmin", isAdmin));
        });

        ll_non_referred_patient.setOnClickListener(v -> {
            startActivity(new Intent(context, PostCampPatientList_Activity.class)
                    .putExtra("campId", campId)
                    .putExtra("callType", "2")
                    .putExtra("isAdmin", isAdmin));
        });
    }
}