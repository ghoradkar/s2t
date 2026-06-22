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

public class PostCampCounts_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;

    private CardView ll_completed, ll_pending;
    private TextView tv_post_camps_completed, tv_post_camps_pending;

    private String DISTLGDCODE, DESGID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_counts);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = PostCampCounts_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        ll_completed = findViewById(R.id.ll_completed);
        ll_pending = findViewById(R.id.ll_pending);
        tv_post_camps_completed = findViewById(R.id.tv_post_camps_completed);
        tv_post_camps_pending = findViewById(R.id.tv_post_camps_pending);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Count");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setDefaults() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DESGID.equals("51")) {
            DISTLGDCODE = "0";
        }


        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    DISTLGDCODE,
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
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

                        String PostCampsCompleted = jsonObject.getString("PostCampsCompleted");
                        String PostCampsPending = jsonObject.getString("PostCampsPending");
                        tv_post_camps_completed.setText(PostCampsCompleted);
                        tv_post_camps_pending.setText(PostCampsPending);
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
        ll_pending.setOnClickListener(v -> {
            startActivity(new Intent(context, PostCampCampList.class)
                    .putExtra("IsComplete", "0"));
        });

        ll_completed.setOnClickListener(v -> {
            startActivity(new Intent(context, PostCampCampList.class)
                    .putExtra("IsComplete", "1"));
        });
    }
}