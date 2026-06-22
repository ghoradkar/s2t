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
import android.widget.SearchView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.SampleProcessingPatientListAdapter;
import com.myhindlab.abkat.models.SampleProcessingPatientModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.List;

public class SampleProcessingPatientWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private SearchView sv_name;
    private RecyclerView rv_patientlist;

    private List<SampleProcessingPatientModel.OutputBean> patientList, searchPatientList;
    private int type, selectedMonthId, selectedYearId, campId, labCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleprocessing_patientwise);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = SampleProcessingPatientWise_Activity.this;
        pd = new ProgressDialog(context);
        sv_name = findViewById(R.id.sv_name);
        rv_patientlist = findViewById(R.id.rv_patientlist);

        searchPatientList = new ArrayList<>();
        patientList = new ArrayList<>();
    }

    private void setDefaults() {
        rv_patientlist.setLayoutManager(new LinearLayoutManager(context));
        type = getIntent().getIntExtra("type", 0);
        selectedMonthId = getIntent().getIntExtra("selectedMonthId", 0);
        selectedYearId = getIntent().getIntExtra("selectedYearId", 0);
        campId = getIntent().getIntExtra("campId", 0);
        labCode = getIntent().getIntExtra("labCode", 0);

        if (Utilities.isNetworkAvailable(context)) {
            new GetLabWiseTestStatus().execute(
                    "0",
                    String.valueOf(selectedMonthId),
                    String.valueOf(selectedYearId),
                    String.valueOf(labCode),
                    String.valueOf(campId),
                    String.valueOf(type));
        } else {
            Utilities.showToastMessage("Please check your internet connection", context, false);
        }
    }

    private void setEventHandlers() {
        sv_name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPatientList = new ArrayList<>();

                if (!query.equals("")) {
                    if (patientList != null) {
                        if (patientList.size() > 0) {
                            for (SampleProcessingPatientModel.OutputBean pojo : patientList) {
                                String searchDetails = pojo.getEnglishName() + pojo.getOrderId();
                                if (searchDetails.toLowerCase().contains(query.toLowerCase())) {
                                    searchPatientList.add(pojo);
                                }
                            }
                            rv_patientlist.setAdapter(new SampleProcessingPatientListAdapter(context, searchPatientList, type));
                        }
                    }
                } else {
                    rv_patientlist.setAdapter(new SampleProcessingPatientListAdapter(context, patientList, type));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPatientList = new ArrayList<>();

                if (!newText.equals("")) {
                    if (patientList != null) {
                        if (patientList.size() > 0) {
                            for (SampleProcessingPatientModel.OutputBean pojo : patientList) {
                                String searchDetails = pojo.getEnglishName() + pojo.getOrderId();
                                if (searchDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchPatientList.add(pojo);
                                }
                            }
                            rv_patientlist.setAdapter(new SampleProcessingPatientListAdapter(context, searchPatientList, type));
                        }
                    }
                } else {
                    rv_patientlist.setAdapter(new SampleProcessingPatientListAdapter(context, patientList, type));
                }
                return true;
            }
        });

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
                    SampleProcessingPatientModel pojoDetails = new Gson().fromJson(result, SampleProcessingPatientModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        patientList = pojoDetails.getOutput();
                        if (patientList.size() > 0) {
                            rv_patientlist.setAdapter(new SampleProcessingPatientListAdapter(context, patientList, type));
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            sv_name.clearFocus();
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
