package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class CampPlanningRequest_Activity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private UserSessionManager sessionManager;
    private static EditText edt_distrcit, edt_fromdate, edt_todate;
    private static String distlgdcode = "0", userId;
    private String name = "", Designation = "", DESGID = "0", distrcit;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private static RecyclerView rv_campplanrequestlist;
    private static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_planning_request);
        init();
        SetDefaults();
        setUpToolBar();
    }

    private void init() {
        context = CampPlanningRequest_Activity.this;
        sessionManager = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_distrcit = findViewById(R.id.edt_distrcit);
        edt_fromdate = findViewById(R.id.edt_fromdate);
        edt_todate = findViewById(R.id.edt_todate);

        rv_campplanrequestlist = findViewById(R.id.rv_campplanrequestlist);
        rv_campplanrequestlist.setLayoutManager(new LinearLayoutManager(context));
    }

    private void SetDefaults() {

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

        //new GetSiteSurveyRequestList().execute();
    }

    private void setEventHandler() {
        edt_fromdate.setOnClickListener(this);
        edt_todate.setOnClickListener(this);

    }


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Planning Request");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

//                                new CampRequestList_Actvity.GetSiteSurveyRequestList().execute();
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
                    Utilities.showMessageString("Please Select From Date", context);
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
//                                    new CampRequestList_Actvity.GetSiteSurveyRequestList().execute();
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

}
