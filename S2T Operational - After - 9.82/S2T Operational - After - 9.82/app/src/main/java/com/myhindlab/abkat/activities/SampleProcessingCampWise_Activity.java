package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.myhindlab.abkat.adapters.SampleProcessingCampWiseAdapter;
import com.myhindlab.abkat.models.SampleProcessingCampModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class SampleProcessingCampWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private RecyclerView rv_camplist;
    private TextView tv_title;

    private List<SampleProcessingCampModel.OutputBean> campList;
    private int type, selectedMonthId, selectedYearId, labCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleprocessing_campwise);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = SampleProcessingCampWise_Activity.this;
        pd = new ProgressDialog(context);
        rv_camplist = findViewById(R.id.rv_camplist);
        tv_title = findViewById(R.id.tv_title);

        campList = new ArrayList<>();
    }

    private void setDefaults() {
        rv_camplist.setLayoutManager(new LinearLayoutManager(context));

        type = getIntent().getIntExtra("type", 0);
        selectedMonthId = getIntent().getIntExtra("selectedMonthId", 0);
        selectedYearId = getIntent().getIntExtra("selectedYearId", 0);
        labCode = getIntent().getIntExtra("labCode", 0);
        String districtCode = getIntent().getStringExtra("districtCode");

        switch (type) {
            case 1:
                tv_title.setText("Completed");
                break;
            case 2:
                tv_title.setText("Pending");
                break;
            case 3:
                tv_title.setText("Rejected");
                break;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new GetLabWiseTestStatus().execute(
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
        rv_camplist.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SampleProcessingCampModel.OutputBean labDetails = campList.get(position);

                startActivity(new Intent(context, SampleProcessingPatientWise_Activity.class)
                        .putExtra("campId", labDetails.getCampId())
                        .putExtra("type", type)
                        .putExtra("selectedMonthId", selectedMonthId)
                        .putExtra("selectedYearId", selectedYearId)
                        .putExtra("labCode", labCode));
            }
        }));

    }

    private class GetLabWiseTestStatus extends AsyncTask<String, Void, String> {

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
            res = WebServiceCall.APICall(ApplicationConstants.GetLabWiseTestStatus, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    SampleProcessingCampModel pojoDetails = new Gson().fromJson(result, SampleProcessingCampModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            rv_camplist.setAdapter(new SampleProcessingCampWiseAdapter(context, campList, type));
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
        getSupportActionBar().setTitle("Sample Processing");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
