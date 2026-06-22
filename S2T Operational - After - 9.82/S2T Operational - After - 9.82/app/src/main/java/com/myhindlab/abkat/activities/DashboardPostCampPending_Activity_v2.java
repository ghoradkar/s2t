package com.myhindlab.abkat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.PostCampReadinessModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardPostCampPending_Activity_v2 extends AppCompatActivity {

    private Context mContext;
    private TextInputEditText tiedt_district, tiedt_month_year;
    private TextView tv_selectyear, tv_selectmonth;
    private TextView tv_sample_accepted, tv_sample_completed, tv_sample_authenticated, tv_internal_doctor, tv_sample_accepted_pending;

    private static String districtId, monthName;
    private int mYear, mMonth, selectedMonthId, selectedYearId;
    private Calendar calMonth;

    private static List<PostCampReadinessModel.OutputBean> campList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_post_camp_pending_v2);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
        getSessionData();
    }

    private void init() {
        mContext = DashboardPostCampPending_Activity_v2.this;

        tiedt_district = findViewById(R.id.tiedt_district);
        tiedt_month_year = findViewById(R.id.tiedt_month_year);
        tv_sample_accepted = findViewById(R.id.tv_sample_accepted);
        tv_sample_completed = findViewById(R.id.tv_sample_completed);
        tv_sample_authenticated = findViewById(R.id.tv_sample_authenticated);
        tv_internal_doctor = findViewById(R.id.tv_internal_doctor);
        tv_sample_accepted_pending = findViewById(R.id.tv_sample_accepted_pending);

        campList = new ArrayList<>();
    }

    private void setDefaults() {
        districtId = getIntent().getStringExtra("districtId");
        tiedt_district.setText(getIntent().getStringExtra("districtName"));
        calMonth = (Calendar) getIntent().getSerializableExtra("calMonth");

        tiedt_month_year.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);

        monthName = new DateFormatSymbols().getMonths()[mMonth];
        selectedMonthId = mMonth;
        selectedYearId = mYear;

        if (Utilities.isNetworkAvailable(mContext))
            new InvoiceAndPostCampDashboard(
                    mContext, tv_sample_accepted, tv_sample_completed, tv_sample_authenticated, tv_internal_doctor, tv_sample_accepted_pending)
                    .execute(
                            districtId,
                            String.valueOf(selectedMonthId + 1),
                            String.valueOf(selectedYearId),
                            "3"
                    );
        else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);

    }

    private void getSessionData() {

    }

    private void setEventHandler() {
        tiedt_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(mContext)) {
                    new GetDistrictList(mContext, tiedt_district, tiedt_month_year).execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);
                }
            }
        });

        tiedt_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiedt_district.getText().toString().trim().isEmpty()) {
                    tiedt_district.setError("Please select district");
                    return;
                }

                selectYearMonthAlert();
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Camp Readiness");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private static class GetDistrictList extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        WeakReference<Context> context;
        WeakReference<TextInputEditText> tiedt_district, tiedt_month_year;

        GetDistrictList(Context context, TextInputEditText tiedt_district, TextInputEditText tiedt_month_year) {
            this.context = new WeakReference<>(context);
            this.tiedt_district = new WeakReference<>(tiedt_district);
            this.tiedt_month_year = new WeakReference<>(tiedt_month_year);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context.get());
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type, message;
            try {
                if (!result.equals("")) {
                    ArrayList<DistrictList_Model> districtList;
                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0) {
                            showDistrictListDialog(districtList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context.get(), false);
                        }
                    } else {
                        Utilities.showAlertDialog(context.get(), "Fail", message, false);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context.get(), "Please Try Again", "Server Not Responding", false);
            }
        }

        private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
            districtList.add(0, new DistrictList_Model("0", "All"));

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context.get());
            builderSingle.setTitle("Select District");
            builderSingle.setCancelable(false);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context.get(), R.layout.list_row);

            for (int i = 0; i < districtList.size(); i++) {
                arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
            }

            builderSingle.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                tiedt_district.get().setText(districtList.get(which).getDISTNAME());
                districtId = districtList.get(which).getDISTLGDCODE();
                tiedt_month_year.get().setText("");
            });
            builderSingle.show();
        }
    }

    private static class InvoiceAndPostCampDashboard extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        WeakReference<Context> context;
        WeakReference<TextView> tv_sample_accepted, tv_sample_completed, tv_sample_authenticated, tv_internal_doctor, tv_sample_accepted_pending;

        InvoiceAndPostCampDashboard(Context context, TextView... textViews) {
            this.context = new WeakReference<>(context);
            this.tv_sample_accepted = new WeakReference<>(textViews[0]);
            this.tv_sample_completed = new WeakReference<>(textViews[1]);
            this.tv_sample_authenticated = new WeakReference<>(textViews[2]);
            this.tv_internal_doctor = new WeakReference<>(textViews[3]);
            this.tv_sample_accepted_pending = new WeakReference<>(textViews[4]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context.get());
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("DISTLGDCODE", params[0]));
            param.add(new ParamsPojo("MonthID", params[1]));
            param.add(new ParamsPojo("Year", params[2]));
            param.add(new ParamsPojo("TYPE", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.InvoiceAndPostCampDashboard, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type, message;
            try {
                if (!result.equals("")) {
                    PostCampReadinessModel pojoDetails = new Gson().fromJson(result, PostCampReadinessModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        if (campList.size() > 0) {
                            campCalculations(campList);
                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context.get(), false);
                        }
                    } else {
                        Utilities.showAlertDialog(context.get(), "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context.get(), "Please Try Again", "Server Not Responding", false);
            }
        }

        private void campCalculations(final List<PostCampReadinessModel.OutputBean> invoiceList) {
            int sampleAcceptPending = 0, sampleAccepted = 0, sampleCompleted = 0, sampleAuthenticated = 0, internalDoctor = 0;

            for (PostCampReadinessModel.OutputBean outputBean : invoiceList) {
                switch (outputBean.getReadinessStatusId()) {
                    case "0":
                        sampleAcceptPending = sampleAcceptPending + 1;
                        break;

                    case "1":
                        sampleAccepted = sampleAccepted + 1;
                        break;

                    case "2":
                        sampleCompleted = sampleCompleted + 1;
                        break;

                    case "3":
                        sampleAuthenticated = sampleAuthenticated + 1;
                        break;

                    case "4":
                        internalDoctor = internalDoctor + 1;
                        break;
                }
            }

            tv_sample_accepted_pending.get().setText(String.valueOf(sampleAcceptPending));
            tv_sample_accepted.get().setText(String.valueOf(sampleAccepted));
            tv_sample_completed.get().setText(String.valueOf(sampleCompleted));
            tv_sample_authenticated.get().setText(String.valueOf(sampleAuthenticated));
            tv_internal_doctor.get().setText(String.valueOf(internalDoctor));
        }
    }

    private void selectYearMonthAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.prompt_yearmonthfilter, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
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
                        tiedt_month_year.setText(android.text.format.DateFormat.format("MMMM, yyyy", calMonth));

                        if (Utilities.isNetworkAvailable(mContext)) {
                            new InvoiceAndPostCampDashboard(
                                    mContext, tv_sample_accepted, tv_sample_completed, tv_sample_authenticated, tv_internal_doctor, tv_sample_accepted_pending)
                                    .execute(
                                            districtId,
                                            String.valueOf(selectedMonthId + 1),
                                            String.valueOf(selectedYearId),
                                            "3"
                                    );
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);
                        }

                    } else {
                        Utilities.showAlertDialog(mContext, "Alert", "Please select all field.", false);
                    }
                })
                .setNegativeButton("Cancel", (dialod, id) -> {

                });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void listYearDialogCreater() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Year");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                mContext,
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
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        builderSingle.setTitle("Select Month");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.list_row);

//        List monthList = new ArrayList();
//        Calendar cal = Calendar.getInstance();
//        for (int i = 1; i < 12; i++) {
//            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
//            cal.set(Calendar.MONTH, i);
//            String month_name = month_date.format(cal.getTime());
//            monthList.add(month_name);
//        }

        List<String> monthList = new ArrayList<String>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            String month = months[i];
            System.out.println("month = " + month);
            monthList.add(months[i]);
        }

        for (int i = 0; i < monthList.size(); i++)
            arrayAdapter.add(monthList.get(i).toString().trim());

        builderSingle.setNegativeButton(
                "Cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            monthName = arrayAdapter.getItem(which);
            tv_selectmonth.setText(arrayAdapter.getItem(which));
        });
        builderSingle.show();
    }

    public void open25CampWiseList(View view) {
        List<PostCampReadinessModel.OutputBean> filteredList = new ArrayList<>();

        for (PostCampReadinessModel.OutputBean outputBeans : campList) {
            if (outputBeans.getReadinessStatusId().equals("1"))
                filteredList.add(outputBeans);
        }

        startActivity(new Intent(mContext, DashboardPostCampReadiness_Activity.class)
                .putExtra("invoiceList", (Serializable) filteredList)
                .putExtra("districtId", districtId)
                .putExtra("selectedMonthId", selectedMonthId)
                .putExtra("selectedYearId", selectedYearId));
    }

    public void open50CampWiseList(View view) {
        List<PostCampReadinessModel.OutputBean> filteredList = new ArrayList<>();

        for (PostCampReadinessModel.OutputBean outputBeans : campList) {
            if (outputBeans.getReadinessStatusId().equals("2"))
                filteredList.add(outputBeans);
        }

        startActivity(new Intent(mContext, DashboardPostCampReadiness_Activity.class)
                .putExtra("invoiceList", (Serializable) filteredList)
                .putExtra("districtId", districtId)
                .putExtra("selectedMonthId", selectedMonthId)
                .putExtra("selectedYearId", selectedYearId));
    }

    public void open75CampWiseList(View view) {
        List<PostCampReadinessModel.OutputBean> filteredList = new ArrayList<>();

        for (PostCampReadinessModel.OutputBean outputBeans : campList) {
            if (outputBeans.getReadinessStatusId().equals("3"))
                filteredList.add(outputBeans);
        }

        startActivity(new Intent(mContext, DashboardPostCampReadiness_Activity.class)
                .putExtra("invoiceList", (Serializable) filteredList)
                .putExtra("districtId", districtId)
                .putExtra("selectedMonthId", selectedMonthId)
                .putExtra("selectedYearId", selectedYearId));
    }

    public void open100CampWiseList(View view) {
        List<PostCampReadinessModel.OutputBean> filteredList = new ArrayList<>();

        for (PostCampReadinessModel.OutputBean outputBeans : campList) {
            if (outputBeans.getReadinessStatusId().equals("4"))
                filteredList.add(outputBeans);
        }

        startActivity(new Intent(mContext, DashboardPostCampReadiness_Activity.class)
                .putExtra("invoiceList", (Serializable) filteredList)
                .putExtra("districtId", districtId)
                .putExtra("selectedMonthId", selectedMonthId)
                .putExtra("selectedYearId", selectedYearId));
    }

    public void open0CampWiseList(View view) {
        List<PostCampReadinessModel.OutputBean> filteredList = new ArrayList<>();

        for (PostCampReadinessModel.OutputBean outputBeans : campList) {
            if (outputBeans.getReadinessStatusId().equals("0"))
                filteredList.add(outputBeans);
        }

        startActivity(new Intent(mContext, DashboardPostCampReadiness_Activity.class)
                .putExtra("invoiceList", (Serializable) filteredList)
                .putExtra("districtId", districtId)
                .putExtra("selectedMonthId", selectedMonthId)
                .putExtra("selectedYearId", selectedYearId));
    }

}
