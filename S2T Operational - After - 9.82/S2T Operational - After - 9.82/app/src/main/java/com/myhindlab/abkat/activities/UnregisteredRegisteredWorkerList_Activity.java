package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.UnregisteredRegisteredWorkerAdapter;
import com.myhindlab.abkat.models.UnregisteredWorkersModel;
import com.myhindlab.abkat.pojos.UnregisteredWorkersPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class UnregisteredRegisteredWorkerList_Activity extends AppCompatActivity {

    private static Context context;
    private UserSessionManager session;
    private static RecyclerView rv_patientlist;
    private static ProgressDialog pd;

    private static String SiteDetailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unregisteredregistered_workerlist);

        init();
        setUpToolbar();
        setDefaults();
        getSessionData();
        setEventHandler();
    }

    private void init() {
        context = UnregisteredRegisteredWorkerList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        rv_patientlist = findViewById(R.id.rv_patientlist);
        rv_patientlist.setLayoutManager(new LinearLayoutManager(context));

    }

    private void setDefaults() {
        SiteDetailId = getIntent().getStringExtra("SiteDetailId");
        if (Utilities.isNetworkAvailable(context)) {
            new GetUnRegisterUserDetails().execute(SiteDetailId);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void getSessionData() {

    }

    private void setEventHandler() {

    }

    public static class GetUnRegisterUserDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("SiteId", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetUnRegisterUserDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<UnregisteredWorkersModel> workerList = new ArrayList<>();
                    UnregisteredWorkersPojo pojoDetails = new Gson().fromJson(result, UnregisteredWorkersPojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        workerList = pojoDetails.getOutput();
                        if (workerList.size() > 0) {
                            rv_patientlist.setAdapter(new UnregisteredRegisteredWorkerAdapter(context, workerList, SiteDetailId));
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", "No patients are pending for assigining registration number", false);
                        rv_patientlist.setAdapter(new UnregisteredRegisteredWorkerAdapter(context, workerList, SiteDetailId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registered Worker List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
