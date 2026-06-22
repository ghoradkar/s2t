package com.myhindlab.abkat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodaysPatientCountDistWise_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private RecyclerView rv_districtlist;
    int mYear, mMonth, mDay;
    private ArrayList<CampCalendarModel.OutputBean> campList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todayspatient_countdistwise);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = TodaysPatientCountDistWise_Activity.this;
        pd = new ProgressDialog(context);
        rv_districtlist = findViewById(R.id.rv_districtlist);
    }

    private void setDefaults() {
        rv_districtlist.setLayoutManager(new LinearLayoutManager(context));

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        if (Utilities.isNetworkAvailable(context)) {
//            new GetDateWiseCount().execute(
//                    "0",
//                    1900 + "/" + 1 + "/" + 1,
//                    mYear + "/" + (mMonth + 1) + "/" + mDay
//            );
            new GetMonthlySurveySiteRequest().execute(String.valueOf(mMonth+1), String.valueOf(mYear), "0");

        } else {
            Utilities.showToastMessage("Please check your internet connection", context, false);
        }
    }

    private void setEventHandlers() {

    }


    private class GetMonthlySurveySiteRequest extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Month", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("DistCode", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequest, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {

                        campList = pojoDetails.getOutput();

                        startActivity(new Intent(context, CampCalendarList_Activity.class)
                                .putExtra("TYPE", "2")
                                .putExtra("CAMPTYPR", "TOTAL")
                                .putExtra("campList", campList));
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_warning);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("Camps are not available for selected month and year.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
            }
            setEventHandlers();
        }
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today's Patient Count");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
