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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampSampleProcessingAdapter;
import com.myhindlab.abkat.models.CampSampleProcessingModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class CampSampleProcessing_Activity extends AppCompatActivity {

    private Context context;
    private LinearLayout ll_nothingtoshow, ll_lables;
    private RecyclerView rv_sample_process;
    private TextView tv_message;
    private static ArrayList<CampSampleProcessingModel.OutputBean> campSampleProcessList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campsample_processing);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = CampSampleProcessing_Activity.this;
        ll_nothingtoshow = findViewById(R.id.ll_nothingtoshow);
        tv_message = findViewById(R.id.tv_message);
        ll_lables = findViewById(R.id.ll_labels);
        rv_sample_process = findViewById(R.id.rv_sample_process);
        rv_sample_process.setLayoutManager(new LinearLayoutManager(context));
        campSampleProcessList = new ArrayList<>();
    }

    private void setDefaults() {
        campSampleProcessList = (ArrayList<CampSampleProcessingModel.OutputBean>)
                getIntent().getSerializableExtra("filteredCampSampleProcessList");

        if (campSampleProcessList == null) {
            if (Utilities.isNetworkAvailable(context)) {
                new SampleProcessingReport().execute(getIntent().getStringExtra("campId"), "0", "0");
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        } else {
            rv_sample_process.setAdapter(new CampSampleProcessingAdapter(context, campSampleProcessList));
        }
    }

    private void setEventHandlers() {
    }

    private class SampleProcessingReport extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampId", params[0]));
            param.add(new ParamsPojo("RegdId", params[1]));
            param.add(new ParamsPojo("TestType", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.SampleProcessingReport, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {
                    CampSampleProcessingModel pojoDetails = new Gson().fromJson(result, CampSampleProcessingModel.class);
                    String status = pojoDetails.getStatus();
                    if (status.equalsIgnoreCase("success")) {
                        campSampleProcessList = pojoDetails.getOutput();
                        if (campSampleProcessList.size() > 0) {
                            rv_sample_process.setVisibility(View.VISIBLE);
                            ll_lables.setVisibility(View.VISIBLE);
                            ll_nothingtoshow.setVisibility(View.GONE);
                            rv_sample_process.setAdapter(new CampSampleProcessingAdapter(context, campSampleProcessList));
                        } else {
                            rv_sample_process.setVisibility(View.GONE);
                            ll_lables.setVisibility(View.GONE);
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            tv_message.setText("Sample processing details not available");
                        }
                    } else {
                        rv_sample_process.setVisibility(View.GONE);
                        ll_lables.setVisibility(View.GONE);
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        tv_message.setText("Sample processing details not available");
                    }
                } else {
                    rv_sample_process.setVisibility(View.GONE);
                    ll_lables.setVisibility(View.GONE);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    tv_message.setText("Sample processing details not available");
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
