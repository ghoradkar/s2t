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
import com.myhindlab.abkat.adapters.SampleProcessingTestWiseAdapter;
import com.myhindlab.abkat.models.PendingTestModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class SampleProcessingTestWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private RecyclerView rv_lablist;

    private List<PendingTestModel.OutputBean> campList;
    private int type, selectedMonthId, selectedYearId, labCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleprocessing_testwise);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = SampleProcessingTestWise_Activity.this;
        pd = new ProgressDialog(context);
        rv_lablist = findViewById(R.id.rv_lablist);

        campList = new ArrayList<>();
    }

    private void setDefaults() {
        rv_lablist.setLayoutManager(new LinearLayoutManager(context));

        type = getIntent().getIntExtra("type", 0);
        selectedMonthId = getIntent().getIntExtra("selectedMonthId", 0);
        selectedYearId = getIntent().getIntExtra("selectedYearId", 0);
        labCode = getIntent().getIntExtra("labCode", 0);
        String districtCode = getIntent().getStringExtra("districtCode");

        if (Utilities.isNetworkAvailable(context)) {
            new GetLabWiseTestStatusWithService().execute(
                    districtCode,
                    String.valueOf(selectedMonthId),
                    String.valueOf(selectedYearId),
                    String.valueOf(labCode),
                    "0",
                    "0");
        } else {
            Utilities.showToastMessage("Please check your internet connection", context, false);
        }
    }

    private void setEventHandlers() {

    }

    private class GetLabWiseTestStatusWithService extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Month", params[1]));
            param.add(new ParamsPojo("Year", params[2]));
            param.add(new ParamsPojo("LabCode", params[3]));
            param.add(new ParamsPojo("CampId", params[4]));
            param.add(new ParamsPojo("TestType", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.GetLabWiseTestStatusWithService, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    PendingTestModel pojoDetails = new Gson().fromJson(result, PendingTestModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            rv_lablist.setAdapter(new SampleProcessingTestWiseAdapter(context, campList));
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pending Tests");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
