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
import com.myhindlab.abkat.models.InvoiceDashboardModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardInvoice_Activity extends AppCompatActivity {

    private Context mContext;
    private TextInputEditText tiedt_district, tiedt_month_year;
    private TextView tv_selectyear, tv_selectmonth;
    private TextView tv_camp_target, tv_post_camps_completed, tv_post_camps_pending, tv_total_camps_completed, tv_beneficiaries_screened, tv_applicable_for_billing;

    private  String districtId, monthName;
    private int mYear, mMonth, selectedMonthId, selectedYearId;
    private Calendar calMonth;
    private  List<InvoiceDashboardModel.OutputBean> invoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_invoice);

        init();
        setUpToolbar();
        setDefaults();
        setEventHandler();
        getSessionData();
    }

    private void init() {
        mContext = DashboardInvoice_Activity.this;

        tiedt_district = findViewById(R.id.tiedt_district);
        tiedt_month_year = findViewById(R.id.tiedt_month_year);
        tv_camp_target = findViewById(R.id.tv_camp_target);
        tv_post_camps_completed = findViewById(R.id.tv_post_camps_completed);
        tv_post_camps_pending = findViewById(R.id.tv_post_camps_pending);
        tv_total_camps_completed = findViewById(R.id.tv_total_camps_completed);
        tv_beneficiaries_screened = findViewById(R.id.tv_beneficiaries_screened);
        tv_applicable_for_billing = findViewById(R.id.tv_applicable_for_billing);
    }

    private void setDefaults() {
        calMonth = Calendar.getInstance();
        tiedt_month_year.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);

        monthName = new DateFormatSymbols().getMonths()[mMonth];
        selectedMonthId = mMonth;
        selectedYearId = mYear;

        districtId = "0";

        if (Utilities.isNetworkAvailable(mContext)) {
            new InvoiceAndPostCampDashboard(
                    mContext,
                    tv_camp_target,
                    tv_post_camps_completed,
                    tv_post_camps_pending,
                    tv_total_camps_completed,
                    tv_beneficiaries_screened,
                    tv_applicable_for_billing)
                    .execute(
                            districtId,
                            String.valueOf(selectedMonthId + 1),
                            String.valueOf(selectedYearId),
                            "1"
                    );
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, mContext, false);
        }
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

    private  class GetDistrictList extends AsyncTask<String, Void, String> {
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
            pd = new ProgressDialog(mContext);
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

    private  class InvoiceAndPostCampDashboard extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        WeakReference<Context> context;
        WeakReference<TextView> tv_camp_target, tv_post_camps_completed, tv_post_camps_pending, tv_total_camps_completed, tv_beneficiaries_screened, tv_applicable_for_billing;

        InvoiceAndPostCampDashboard(Context context, TextView... textViews) {
            this.context = new WeakReference<>(context);
            this.tv_camp_target = new WeakReference<>(textViews[0]);
            this.tv_post_camps_completed = new WeakReference<>(textViews[1]);
            this.tv_post_camps_pending = new WeakReference<>(textViews[2]);
            this.tv_total_camps_completed = new WeakReference<>(textViews[3]);
            this.tv_beneficiaries_screened = new WeakReference<>(textViews[4]);
            this.tv_applicable_for_billing = new WeakReference<>(textViews[5]);
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
                    InvoiceDashboardModel pojoDetails = new Gson().fromJson(result, InvoiceDashboardModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        invoiceList = pojoDetails.getOutput();
                        if (invoiceList.size() > 0) {
                            showDistrictListDialog(invoiceList);
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

        private void showDistrictListDialog(final List<InvoiceDashboardModel.OutputBean> invoiceList) {
            int campTarget = 0, postCampsCompleted = 0, postCampsPending = 0, totalCampsCompleted = 0, beneficiariesScreened = 0, applicableForBilling = 0;

            for (InvoiceDashboardModel.OutputBean outputBean : invoiceList) {
                campTarget = campTarget + outputBean.getCampTarget();
                postCampsCompleted = postCampsCompleted + outputBean.getPostCampsCompleted();
                postCampsPending = postCampsPending + outputBean.getPostCampsPending();
                totalCampsCompleted = totalCampsCompleted + outputBean.getTotalCamps();
                beneficiariesScreened = beneficiariesScreened + outputBean.getActualWorkers();
                applicableForBilling = applicableForBilling + outputBean.getApplicableForBilling();
            }

            tv_camp_target.get().setText(String.valueOf(campTarget));
            tv_post_camps_completed.get().setText(String.valueOf(postCampsCompleted));
            tv_post_camps_pending.get().setText(String.valueOf(postCampsPending));
            tv_total_camps_completed.get().setText(String.valueOf(totalCampsCompleted));
            tv_beneficiaries_screened.get().setText(String.valueOf(beneficiariesScreened));
            tv_applicable_for_billing.get().setText(String.valueOf(applicableForBilling));
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
                                    mContext,
                                    tv_camp_target,
                                    tv_post_camps_completed,
                                    tv_post_camps_pending,
                                    tv_total_camps_completed,
                                    tv_beneficiaries_screened,
                                    tv_applicable_for_billing)
                                    .execute(
                                            districtId,
                                            String.valueOf(selectedMonthId + 1),
                                            String.valueOf(selectedYearId),
                                            "1"
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

    public void showPostCampPending(View view) {
        startActivity(new Intent(mContext, DashboardPostCampPending_Activity_v2.class)
                .putExtra("districtId", districtId)
                .putExtra("districtName", tiedt_district.getText().toString().trim())
                .putExtra("calMonth", calMonth));
    }

}
