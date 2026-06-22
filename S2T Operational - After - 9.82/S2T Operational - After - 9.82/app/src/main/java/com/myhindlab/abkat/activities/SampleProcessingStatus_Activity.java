package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.SampleProcessingLabModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SampleProcessingStatus_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private CardView ll_completed, ll_pending, ll_rejected;
    private TextView tv_completed, tv_pending, tv_rejected;
    private Calendar calMonth;
    private List<SampleProcessingLabModel.OutputBean> labList;
    private EditText edt_selectdistrict, edt_year_month;
    private TextView tv_selectyear, tv_selectmonth;
    private String user_id, year, month, monthName, districtCode = "0";
    private int mYear, mMonth, mDay, selectedMonthId, selectedYearId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_processing_status);

        init();
        setDefaults();
        setEventHandlers();
        setUpToolbar();
    }

    private void init() {
        context = SampleProcessingStatus_Activity.this;
        pd = new ProgressDialog(context);
        ll_completed = findViewById(R.id.ll_completed);
        ll_pending = findViewById(R.id.ll_pending);
        ll_rejected = findViewById(R.id.ll_rejected);
        tv_completed = findViewById(R.id.tv_completed);
        tv_pending = findViewById(R.id.tv_pending);
        tv_rejected = findViewById(R.id.tv_rejected);
        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        edt_year_month = findViewById(R.id.edt_year_month);
        calMonth = Calendar.getInstance();
        labList = new ArrayList<>();
    }

    private void setDefaults() {
        districtCode = "0";
        edt_year_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        monthName = new DateFormatSymbols().getMonths()[mMonth];
        selectedMonthId = mMonth + 1;
        selectedYearId = mYear;

        String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", calMonth)).toString().split(" ");
        month = monthYear[0];
        year = monthYear[1];

        if (Utilities.isNetworkAvailable(context)) {
            new GetLabWiseTestStatus().execute(districtCode, String.valueOf(selectedMonthId), String.valueOf(selectedYearId), "0", "0", "0");
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {
        edt_selectdistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });

        edt_year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYearMonthAlert();
            }
        });

        ll_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_completed.getText().toString().trim().equals("0")) {
                    Utilities.showAlertDialog(context, "Alert", "No data available", false);
                    return;
                }

                startActivity(new Intent(context, SampleProcessingStatusLabwise_Activity.class)
                        .putParcelableArrayListExtra("labList", (ArrayList<? extends Parcelable>) labList)
                        .putExtra("type", 1)
                        .putExtra("selectedMonthId", selectedMonthId)
                        .putExtra("selectedYearId", selectedYearId)
                        .putExtra("districtCode", districtCode));

            }
        });

        ll_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_pending.getText().toString().trim().equals("0")) {
                    Utilities.showAlertDialog(context, "Alert", "No data available", false);
                    return;
                }

                startActivity(new Intent(context, SampleProcessingStatusLabwise_Activity.class)
                        .putParcelableArrayListExtra("labList", (ArrayList<? extends Parcelable>) labList)
                        .putExtra("type", 2)
                        .putExtra("selectedMonthId", selectedMonthId)
                        .putExtra("selectedYearId", selectedYearId)
                        .putExtra("districtCode", districtCode));


            }
        });

        ll_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_rejected.getText().toString().trim().equals("0")) {
                    Utilities.showAlertDialog(context, "Alert", "No data available", false);
                    return;
                }

                startActivity(new Intent(context, SampleProcessingStatusLabwise_Activity.class)
                        .putParcelableArrayListExtra("labList", (ArrayList<? extends Parcelable>) labList)
                        .putExtra("type", 3)
                        .putExtra("selectedMonthId", selectedMonthId)
                        .putExtra("selectedYearId", selectedYearId)
                        .putExtra("districtCode", districtCode));

            }
        });
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

                        if (Utilities.isNetworkAvailable(context)) {
                            new GetLabWiseTestStatus().execute(districtCode, String.valueOf(selectedMonthId), String.valueOf(selectedYearId), "0", "0", "0");
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }
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
            arrayAdapter.add(monthList.get(i).toString().trim());

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

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edt_selectdistrict.setText(districtList.get(which).getDISTNAME());
                districtCode = districtList.get(which).getDISTLGDCODE();

                if (Utilities.isNetworkAvailable(context)) {
                    new GetLabWiseTestStatus().execute(districtCode, String.valueOf(selectedMonthId), String.valueOf(selectedYearId), "0", "0", "0");
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
        builderSingle.show();
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
                    SampleProcessingLabModel pojoDetails = new Gson().fromJson(result, SampleProcessingLabModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();
                    if (status.equalsIgnoreCase("success")) {
                        labList = pojoDetails.getOutput();
                        if (labList.size() > 0) {

                            int COMPLETED = 0, PENDING = 0, REJECTED = 0;

                            for (int i = 0; i < labList.size(); i++) {

                                COMPLETED = COMPLETED + labList.get(i).getCOMPLETED();
                                PENDING = PENDING + labList.get(i).getPENDING();
                                REJECTED = REJECTED + labList.get(i).getREJECTED();
                            }
                            tv_completed.setText("" + COMPLETED);
                            tv_pending.setText("" + PENDING);
                            tv_rejected.setText("" + REJECTED);
                        }
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
