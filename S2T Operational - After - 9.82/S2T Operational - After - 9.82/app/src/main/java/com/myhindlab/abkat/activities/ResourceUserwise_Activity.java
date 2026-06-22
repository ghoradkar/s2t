package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.UserListAdapter;
import com.myhindlab.abkat.pojos.GetAttandanceReportUserwiseOutPut_Pojo;
import com.myhindlab.abkat.pojos.GetAttandanceReportUserwise_Pojo;
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

public class ResourceUserwise_Activity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    ProgressDialog pd;
    private UserSessionManager session;
    private RecyclerView rv_userlist;
    String EmpCode, divId, distlgdcode, desgid;
    String DESIGID, DesigName, fromDate;
    private EditText edt_date;
    private TextView tv_desg_name;
    private ArrayList<GetAttandanceReportUserwiseOutPut_Pojo> desigList;
    private int mYear, mMonth, mDay;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_userwise);
        init();
        setDefaults();
        getSessionData();
        setToolBar();
        setEventHandlers();
    }

    private void init() {
        context = ResourceUserwise_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        rv_userlist = findViewById(R.id.rv_userlist);
        rv_userlist.setLayoutManager(new LinearLayoutManager(context));
        edt_date = findViewById(R.id.edt_date);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

    }

    private void setDefaults() {
        DESIGID = getIntent().getStringExtra("DESIGID");
        DesigName = getIntent().getStringExtra("DesigName");

        Calendar calendar = Calendar.getInstance();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        edt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));

        if (Utilities.isNetworkAvailable(context)) {
            new GetUserList().execute(DESIGID, "0", edt_date.getText().toString().trim());
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
        edt_date.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetUserList().execute(DESIGID, "0", edt_date.getText().toString().trim());
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                EmpCode = json.getString("EmpCode");
//                divId = json.getString("EmpCode");
                divId = "0";
                desgid = json.getString("DESGID");
                distlgdcode = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(DesigName);
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
            case R.id.edt_date:

                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                fromDate = Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                //TodayAttendanceReport_LABWISEStatical().execute("0", fromDate,DESIGID);
                                new GetUserList().execute(DESIGID, "0", edt_date.getText().toString().trim());
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
        }
    }

    public class GetUserList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DESGID", params[0]));
            param.add(new ParamsPojo("DISTLGDCODE", params[1]));
            param.add(new ParamsPojo("ATTENDANCEDATE", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.GetAttandanceReportUserwise, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    desigList = new ArrayList<>();
                    GetAttandanceReportUserwise_Pojo pojoDetails = new Gson().fromJson(result, GetAttandanceReportUserwise_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        desigList = pojoDetails.getOutput();
                        if (desigList.size() > 0) {
                            rv_userlist.setAdapter(new UserListAdapter(context, desigList));
                        }
                    } else {
                        Utilities.showAlertDialog(context, type, message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
            // searchView.clearFocus();

        }
    }
}
