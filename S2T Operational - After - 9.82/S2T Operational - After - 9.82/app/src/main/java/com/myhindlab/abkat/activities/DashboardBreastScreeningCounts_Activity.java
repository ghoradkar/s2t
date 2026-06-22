package com.myhindlab.abkat.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.BreastScreeningDistrictWiseModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardBreastScreeningCounts_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private MaterialEditText edt_from_date, edt_to_date;
    private CardView cv_positive, cv_negative, cv_no_status;
    private TextView tv_positive, tv_negative, tv_no_status;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;
    private List<BreastScreeningDistrictWiseModel.OutputBean> districtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_breast_screeing);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = DashboardBreastScreeningCounts_Activity.this;

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait . . . ");
        pd.setCancelable(false);

        edt_from_date = findViewById(R.id.edt_from_date);
        edt_to_date = findViewById(R.id.edt_to_date);
        cv_positive = findViewById(R.id.cv_positive);
        cv_negative = findViewById(R.id.cv_negative);
        cv_no_status = findViewById(R.id.cv_no_status);
        tv_positive = findViewById(R.id.tv_positive);
        tv_negative = findViewById(R.id.tv_negative);
        tv_no_status = findViewById(R.id.tv_no_status);

        districtList = new ArrayList<>();
    }

    private void setDefaults() {

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = 1;

        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        edt_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear));
        edt_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, mDay1, mMonth1 + 1, mYear1));

        if (Utilities.isNetworkAvailable(context))
            new BreastScreeningDetails().execute(
                    edt_from_date.getText().toString().trim(),
                    edt_to_date.getText().toString().trim(),
                    "0", "0", "0", "0"
            );
        else
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
    }

    private void setEventHandlers() {
        edt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edt_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                        edt_to_date.setText("");

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);
                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });

        edt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_from_date.getText().toString().equals("")) {
                    Utilities.showToastMessage("Please Select From Date", context, false);
                } else {
                    DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            edt_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                            mYear1 = year;
                            mMonth1 = monthOfYear;
                            mDay1 = dayOfMonth;

                            if (Utilities.isNetworkAvailable(context))
                                new BreastScreeningDetails().execute(
                                        edt_from_date.getText().toString().trim(),
                                        edt_to_date.getText().toString().trim(),
                                        "0", "0", "0", "0"
                                );
                            else
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
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
            }
        });

        cv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DashboardBreastScreeningDistrictWise_Activity.class)
                        .putExtra("fromdate", edt_from_date.getText().toString().trim())
                        .putExtra("toDate", edt_to_date.getText().toString().trim())
                        .putExtra("statusType", "1")
                        .putExtra("districtList", (Serializable) districtList));
            }
        });

        cv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DashboardBreastScreeningDistrictWise_Activity.class)
                        .putExtra("fromdate", edt_from_date.getText().toString().trim())
                        .putExtra("toDate", edt_to_date.getText().toString().trim())
                        .putExtra("statusType", "2")
                        .putExtra("districtList", (Serializable) districtList));
            }
        });

//        cv_no_status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(context, DashboardBreastScreeningDistrictWise_Activity.class)
//                        .putExtra("fromdate", edt_from_date.getText().toString().trim())
//                        .putExtra("toDate", edt_to_date.getText().toString().trim())
//                        .putExtra("statusType", "3")
//                        .putExtra("districtList", (Serializable) districtList));
//            }
//        });

    }

    private class BreastScreeningDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            List<ParamsPojo> param = new ArrayList<>();
            param.add(new ParamsPojo("FromDate", params[0]));
            param.add(new ParamsPojo("ToDate", params[1]));
            param.add(new ParamsPojo("TYPE", params[2]));
            param.add(new ParamsPojo("DISTLGDCODE", params[3]));
            param.add(new ParamsPojo("CAMPID", params[4]));
            param.add(new ParamsPojo("STATUSTYPE", params[5]));
            res = WebServiceCall.APICall(ApplicationConstants.BreastScreeningDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type, message;
            try {
                if (!result.equals("")) {
                    BreastScreeningDistrictWiseModel pojoDetails = new Gson().fromJson(result, BreastScreeningDistrictWiseModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        districtList = pojoDetails.getOutput();
                        if (districtList.size() > 0)
                            populateValues();
                        else
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                    } else
                        Utilities.showAlertDialog(context, "Fail", message, false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    private void populateValues() {
        int positive = 0, negative = 0, noStatus = 0;

        for (BreastScreeningDistrictWiseModel.OutputBean outputBean : districtList) {
            positive = positive + outputBean.getPOSITIVE_PATIENTS();
            negative = negative + outputBean.getNEGATIVE_PATIENTS();
            noStatus = noStatus + outputBean.getPOSITIVE_PATIENTS() + outputBean.getNEGATIVE_PATIENTS();
        }

        tv_positive.setText(String.valueOf(positive));
        tv_negative.setText(String.valueOf(negative));
        tv_no_status.setText(String.valueOf(noStatus));
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Breast Screening Dashboard");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
