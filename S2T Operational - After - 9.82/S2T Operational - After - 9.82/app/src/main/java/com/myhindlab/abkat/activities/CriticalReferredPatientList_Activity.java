package com.myhindlab.abkat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CriticalReferredAdapter;
import com.myhindlab.abkat.models.CriticalReferredListModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class CriticalReferredPatientList_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private RecyclerView rv_beneficiary;
    private String DrillType;
    private ArrayList<CriticalReferredListModel.OutputBean> campBeneficiaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criticalreferred_patientlist);

        init();
        setDefault();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = CriticalReferredPatientList_Activity.this;
        session = new UserSessionManager(context);
        rv_beneficiary = findViewById(R.id.rv_beneficiary);
        rv_beneficiary.setLayoutManager(new LinearLayoutManager(context));
        campBeneficiaryList = new ArrayList<>();
    }

    private void setDefault() {
        DrillType = getIntent().getStringExtra("DrillType");

        if (Utilities.isNetworkAvailable(context)) {
            new GetHomePageCountDetails().execute(DrillType);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
    }

    private class GetHomePageCountDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DrillType", DrillType));

            res = WebServiceCall.APICall(ApplicationConstants.GetHomePageCountDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    CriticalReferredListModel pojoDetails = new Gson().fromJson(result, CriticalReferredListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campBeneficiaryList = pojoDetails.getOutput();
                        if (campBeneficiaryList.size() > 0) {
                            rv_beneficiary.setAdapter(new CriticalReferredAdapter(context, campBeneficiaryList));
                        }
                    } else {
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_warning);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("Patient list not available");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (DrillType.equals("2"))
            getSupportActionBar().setTitle("Referred Patients");
        else if (DrillType.equals("3"))
            getSupportActionBar().setTitle("Critical Patients");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
