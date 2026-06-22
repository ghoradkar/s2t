package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.TodaysAvailabilityReportDistrictWiseAdapter;
import com.myhindlab.abkat.pojos.AttendanceReportUserList_OutPut_Pojo;
import com.myhindlab.abkat.pojos.AttendanceReportUserList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardAvailabilityReportUserList_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private int mYear, mMonth, mDay;
    private TextView txt_filterdate, txt_filterdate1, tv_labforadmin, tv_totalpresent, tv_totalabsent;
    private String fromDate, DISTLGDCODE, totalattendane, absent, date, DISTNAME;
    private RecyclerView recyclerview_list;
    private ArrayList<AttendanceReportUserList_OutPut_Pojo> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_availability_report_user_list);

        init();
        setToolBar();
        setDefaults();
        setEventHandlers();
    }

    private void init() {
        context = DashboardAvailabilityReportUserList_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        txt_filterdate = findViewById(R.id.txt_filterdate);
        tv_labforadmin = findViewById(R.id.tv_labforadmin);
        tv_totalpresent = findViewById(R.id.tv_totalpresent);
        tv_totalabsent = findViewById(R.id.tv_totalabsent);
        recyclerview_list = findViewById(R.id.recyclerview_list);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaults() {
        DISTLGDCODE = getIntent().getStringExtra("DISTLGDCODE");
        DISTNAME = getIntent().getStringExtra("DISTNAME");
        fromDate = getIntent().getStringExtra("Date");
        totalattendane = getIntent().getStringExtra("Total Attendance");
        absent = getIntent().getStringExtra("Absent");

        tv_labforadmin.setText(DISTNAME);
        tv_totalpresent.setText(totalattendane);
        tv_totalabsent.setText(absent);
        txt_filterdate.setText(fromDate);

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        txt_filterdate.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear);
        new TodayAttendanceReport_LABWISEStatical().execute(fromDate, "0", DISTLGDCODE, "0");
    }

    private void setEventHandlers() {

        txt_filterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txt_filterdate.setText("Date - " + Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                new TodayAttendanceReport_LABWISEStatical().execute(fromDate, "0", DISTLGDCODE, "0");
                            }
                        }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });
    }

    public class TodayAttendanceReport_LABWISEStatical extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String res = "[]";

            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Date", params[0]));
            param.add(new ParamsPojo("DesgId", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAttendanceReportDistrictWise, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {

                    Gson gson = new Gson();
                    AttendanceReportUserList_Pojo output = gson.fromJson(result,
                            AttendanceReportUserList_Pojo.class);

                    if (output.getStatus().equalsIgnoreCase("Success")) {

                        listDetails = new ArrayList<>();
                        listDetails = output.getOutput();

                        if (listDetails.size() > 0) {

                            setRecyclerView(listDetails);

                        } else
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);

                    } else
                        Utilities.showAlertDialog(context, output.getStatus(), output.getMessage(), false);
                } else
                    Utilities.showAlertDialog(context,
                            "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    private void setRecyclerView(ArrayList<AttendanceReportUserList_OutPut_Pojo> attList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerview_list.setLayoutManager(layoutManager);

        recyclerview_list.setAdapter(new TodaysAvailabilityReportDistrictWiseAdapter(context, attList));
    }
}
