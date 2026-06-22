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
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.BreastScreeningCampWiseAdapter;
import com.myhindlab.abkat.models.BreastScreeningCampWiseModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class DashboardBreastScreeningCampWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;

    private RecyclerView rv_camp;
    private TextView tv_count_name;
    private String fromdate, toDate, districtCode, statusType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_breast_screening_camp_wise);

        init();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = DashboardBreastScreeningCampWise_Activity.this;

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);

        rv_camp = findViewById(R.id.rv_camp);
        tv_count_name = findViewById(R.id.tv_count_name);
        rv_camp.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setDefaults() {
        fromdate = getIntent().getStringExtra("fromdate");
        toDate = getIntent().getStringExtra("toDate");
        districtCode = getIntent().getStringExtra("districtCode");
        statusType = getIntent().getStringExtra("statusType");

        switch (statusType) {
            case "1":
                tv_count_name.setText("Positive Count");
                break;
            case "2":
                tv_count_name.setText("Negative Count");
                break;
            case "3":
                tv_count_name.setText("No Status Count");
                break;
        }


        if (Utilities.isNetworkAvailable(context))
            new BreastScreeningDetails().execute(
                    fromdate,
                    toDate,
                    "1", districtCode, "0", statusType
            );
        else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    private class BreastScreeningDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("FromDate", params[0]));
            param.add(new ParamsPojo("ToDate", params[1]));
            param.add(new ParamsPojo("TYPE", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("CAMPID", params[4]));
            param.add(new ParamsPojo("STATUSTYPE", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.BreastScreeningDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type, message;
            try {
                if (!result.equals("")) {
                    BreastScreeningCampWiseModel pojoDetails = new Gson().fromJson(result, BreastScreeningCampWiseModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        List<BreastScreeningCampWiseModel.OutputBean> campList = pojoDetails.getOutput();
                        if (campList.size() > 0)
                            rv_camp.setAdapter(new BreastScreeningCampWiseAdapter(fromdate, toDate, context, campList, statusType, districtCode));
                        else
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                    } else
                        Utilities.showAlertDialog(context, "Fail", message, false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Breast Screening Dashboard");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
