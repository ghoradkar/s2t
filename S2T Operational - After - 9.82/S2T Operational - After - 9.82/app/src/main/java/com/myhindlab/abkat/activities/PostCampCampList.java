package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.PostCampAdapter;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.PostCampListModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostCampCampList extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private Calendar calMonth;

    private EditText edt_selectdistrict, edt_year_month;
    private TextView tv_selectyear, tv_selectmonth;
    private RecyclerView rv_camp_list;
    private String user_id, monthName, districtCode = "0", IsComplete,EmpCode;
    private int mYear, mMonth, mDay, selectedMonthId, selectedYearId;

    private String DESGID;
    private boolean isDateChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_camp_camp_list);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = PostCampCampList.this;
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        edt_year_month = findViewById(R.id.edt_year_month);
        rv_camp_list = findViewById(R.id.rv_camp_list);
        rv_camp_list.setLayoutManager(new LinearLayoutManager(context));

        calMonth = Calendar.getInstance();

    }

    private void setDefaults() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                districtCode = json.getString("DISTLGDCODE");
                EmpCode = json.getString("EmpCode");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DESGID.equals("51")||DESGID.equals("70")) {
            districtCode = "0";
            edt_selectdistrict.setVisibility(View.VISIBLE);
        } else {
            edt_selectdistrict.setVisibility(View.GONE);
        }

        IsComplete = getIntent().getStringExtra("IsComplete");
//        edt_year_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));
        edt_year_month.setText("All");

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        monthName = new DateFormatSymbols().getMonths()[mMonth];
        selectedMonthId = mMonth + 1;
        selectedYearId = mYear;

        apiCall(0, 0);
    }

    private void setEventHandlers() {
        edt_selectdistrict.setOnClickListener(v -> {
            if (Utilities.isNetworkAvailable(context)) {
                new GetDistrictList().execute();
            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }
        });

        edt_year_month.setOnClickListener(v -> selectYearMonthAlert());
    }

    private void selectYearMonthAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.prompt_yearmonthfilter, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(context);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Edit Filter");
        alertDialogBuilder.setCancelable(false);

        tv_selectyear = promptView.findViewById(R.id.tv_selectyear);
        tv_selectmonth = promptView.findViewById(R.id.tv_selectmonthduration);

        tv_selectyear.setText(String.valueOf(android.text.format.DateFormat.format("yyyy", calMonth)));
        tv_selectmonth.setText(android.text.format.DateFormat.format("MMMM", calMonth));

        tv_selectyear.setOnClickListener(v -> listYearDialogCreater());

        tv_selectmonth.setOnClickListener(v -> listMonthDialogCreater());

        alertDialogBuilder
                .setPositiveButton("Ok", (dialod, id) -> {
                    if (!tv_selectyear.getText().toString().equalsIgnoreCase("")
                            && !tv_selectmonth.getText().toString().equalsIgnoreCase("")) {
                        monthName = tv_selectmonth.getText().toString().trim();
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("MMMM").parse(monthName);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        selectedMonthId = cal.get(Calendar.MONTH);

                        calMonth.set(selectedYearId, selectedMonthId, 1);
                        selectedMonthId = selectedMonthId + 1;
                        edt_year_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));

                        isDateChanged = true;

                        apiCall(selectedYearId, selectedMonthId);
                    } else {
                        selectYearMonthAlert();
                        Utilities.showAlertDialog(context, "Alert", "Please select all field.", false);
                    }
                })
                .setNegativeButton("Cancel", (dialod, id) -> {

                });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void listYearDialogCreater() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Year");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                R.layout.list_row);

        Calendar year1 = Calendar.getInstance();
        List<String> yearList = new ArrayList();
        int startYear = 2019;
        int endYear = year1.get(Calendar.YEAR);

        yearList.add(String.valueOf(startYear));

        for (int i = startYear; i < endYear; i++) {
            startYear = startYear + 1;
            yearList.add(String.valueOf(startYear));
        }


        for (int i = 0; i < yearList.size(); i++)
            arrayAdapter.add(String.valueOf(yearList.get(i)));

        builderSingle.setNegativeButton(
                "Cancel",
                (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(
                arrayAdapter,
                (dialog, which) -> {
                    selectedYearId = Integer.parseInt(arrayAdapter.getItem(which));
                    tv_selectyear.setText(arrayAdapter.getItem(which));

                    selectedMonthId = 0;
                    monthName = "";
                    tv_selectmonth.setText("");
                });
        builderSingle.show();
    }

    public void listMonthDialogCreater() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select Month");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        List<String> monthList = new ArrayList<String>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            String month = months[i];
            System.out.println("month = " + month);
            monthList.add(months[i]);
        }

        for (int i = 0; i < monthList.size(); i++)
            arrayAdapter.add(monthList.get(i).trim());

        builderSingle.setNegativeButton(
                "Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            monthName = arrayAdapter.getItem(which);
            tv_selectmonth.setText(arrayAdapter.getItem(which));
        });
        builderSingle.show();
    }

    public class GetDistrictList extends AsyncTask<String, Void, String> {

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

            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            districtList.add(0, new DistrictList_Model("0", "All"));

                            showDistrictListDialog(districtList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < districtList.size(); i++) {
            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
        }

        builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            edt_selectdistrict.setText(districtList.get(which).getDISTNAME());
            districtCode = districtList.get(which).getDISTLGDCODE();

            if (isDateChanged)
                apiCall(selectedYearId, selectedMonthId);
            else
                apiCall(0, 0);
        });
        builderSingle.show();
    }

    private void apiCall(int selectedYearId, int selectedMonthId) {
        if (Utilities.isNetworkAvailable(context)) {
            new GetPostCampDetails().execute(
                    districtCode,
                    String.valueOf(selectedYearId),
                    String.valueOf(selectedMonthId),
                    "1",
                    IsComplete,
                    "0",
                    "0",
                    "0"
            );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private class GetPostCampDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Month", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            param.add(new ParamsPojo("IsComplete", params[4]));
            param.add(new ParamsPojo("CAMPID", params[5]));
            param.add(new ParamsPojo("IsReferred", params[6]));
            param.add(new ParamsPojo("RegdID", params[7]));
            param.add(new ParamsPojo("UserID", EmpCode));
            param.add(new ParamsPojo("SearchRegdID", "0"));
         //   res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails, ApplicationConstants.webservice, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetPostCampDetails_V1, ApplicationConstants.webservice_d2d, param);

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                if (!result.equals("")) {

                    PostCampListModel pojoDetails = new Gson().fromJson(result, PostCampListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        ArrayList<PostCampListModel.OutputBean> campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            rv_camp_list.setAdapter(new PostCampAdapter(context, campList));
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                        ArrayList<PostCampListModel.OutputBean> campList = new ArrayList<>();
                        rv_camp_list.setAdapter(new PostCampAdapter(context, campList));
                    }
                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail", "Server not responding", false);
                e.printStackTrace();
            }
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp List");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}