package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.SiteSurveyRequestListAdapter;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_OutPut_Pojo;
import com.myhindlab.abkat.pojos.SiteSurveyRequestList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SiteSurveyRequestList_Actvity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private UserSessionManager sessionManager;
    private static EditText edt_distrcit, edt_fromdate, edt_todate;
    private Button btn_search;
    private static String distlgdcode = "0", userId;
    private String name = "", Designation = "", DESGID = "0", distrcit;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private static RecyclerView rv_sitesurveyrquestlist;
    private static ProgressDialog pd;
    private static ArrayList<SiteSurveyRequestList_OutPut_Pojo> sitesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_survey_request_list);
        init();
        setUpToolBar();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = SiteSurveyRequestList_Actvity.this;
        sessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_distrcit = findViewById(R.id.edt_distrcit);
        edt_fromdate = findViewById(R.id.edt_fromdate);
        edt_todate = findViewById(R.id.edt_todate);
        rv_sitesurveyrquestlist = findViewById(R.id.rv_sitesurveyrquestlist);
        rv_sitesurveyrquestlist.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Site Survey Request List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) - 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        edt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay1, mMonth1 + 1, mYear1));

        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                name = json.getString("name");
                Designation = json.getString("Designation");
                DESGID = json.getString("DESGID");
                distrcit = json.getString("district");
                distlgdcode = json.getString("DISTLGDCODE");
                userId = json.getString("EmpCode");

                edt_distrcit.setText(distrcit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GetSiteSurveyRequestList().execute();
    }


    private void setEventHandler() {
        edt_fromdate.setOnClickListener(this);
        edt_todate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_fromdate:
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_fromdate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                Calendar c = Calendar.getInstance();
                                mYear1 = c.get(Calendar.YEAR);
                                mMonth1 = c.get(Calendar.MONTH);
                                mDay1 = c.get(Calendar.DAY_OF_MONTH);

                                edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay1, mMonth1 + 1, mYear1));

                                new GetSiteSurveyRequestList().execute();
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
                break;

            case R.id.edt_todate:
                if (edt_fromdate.getText().toString().equals("")) {
                    Utilities.showToastMessage("Please Select From Date", context, false);
                    return;
                } else {
                    DatePickerDialog dpd = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    edt_todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                    mYear1 = year;
                                    mMonth1 = monthOfYear;
                                    mDay1 = dayOfMonth;
                                    new GetSiteSurveyRequestList().execute();
                                }
                            }, mYear1, mMonth1, mDay1);
                    Calendar c = Calendar.getInstance();
                    c.set(mYear, mMonth, mDay);
                    try {
                        dpd.getDatePicker().setCalendarViewShown(false);
                        dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                        dpd.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd.show();
                }
                break;
        }
    }


    public static class GetSiteSurveyRequestList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Districtcode", distlgdcode));
            param.add(new ParamsPojo("fromdate", edt_fromdate.getText().toString().trim()));
            param.add(new ParamsPojo("ToDate", edt_todate.getText().toString().trim()));
            param.add(new ParamsPojo("Level", "1"));
            res = WebServiceCall.APICall(ApplicationConstants.GetSiteSurveyRequest, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    sitesList = new ArrayList<>();
                    SiteSurveyRequestList_Pojo pojoDetails = new Gson().fromJson(result, SiteSurveyRequestList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        sitesList = pojoDetails.getOutput();
                        if (sitesList.size() > 0) {
//                         setUpRecycelerview(sitesList);
                            rv_sitesurveyrquestlist.setAdapter(new SiteSurveyRequestListAdapter(context, sitesList, userId));
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, "Site Survey details not available.", false);
                        rv_sitesurveyrquestlist.setAdapter(new SiteSurveyRequestListAdapter(context, sitesList, userId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }


    }

//    private void setUpRecycelerview(ArrayList<SiteSurveyRequestList_OutPut_Pojo> s_list) {
//
//    }
}
