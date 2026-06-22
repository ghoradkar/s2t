package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampCalendarListAdapter;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampCalenderDatesModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CampsConducted_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private EditText edt_selectdistrict, edt_year_month, edtSelCampType;
    private String TYPE, date, CAMPTYPR = "TOTAL";
    CampCalendarListAdapter campCalendarListAdapter;

    private TextView tvCampStatusHeading, tv_total, tv_do, tv_nodal, tv_pending, tv_completed, tv_rejected, tv_upcoming, tvRegularOrD2dTotal;
    private LinearLayout ll_total, ll_do, ll_nodal, ll_pending, ll_completed, ll_rejected, ll_upcoming;

    private Calendar calMonth;
    private ArrayList<CampCalendarModel.OutputBean> campList;
    private ArrayList<CampCalenderDatesModel> campDateCountList;
    private TextView tv_selectyear, tv_selectmonth, tvRegWorker, tvCampDate;
    private String user_id, year, month, monthName, districtCode;
    private int mYear, mMonth, mDay, selectedMonthId, selectedYearId;
    private String DESGID;
    private UserSessionManager session;
    private int campTypeId = 0;
    private RecyclerView rv_campList;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat format2 = new SimpleDateFormat("dd");

    private boolean isReversed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camps_conducted);

        init();
        getSessionData();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = CampsConducted_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        edt_year_month = findViewById(R.id.edt_year_month);


        tv_total = findViewById(R.id.tv_total);
        tv_do = findViewById(R.id.tv_do);
        tv_pending = findViewById(R.id.tv_pending);
        tv_completed = findViewById(R.id.tv_completed);
        tv_nodal = findViewById(R.id.tv_nodal);
        tv_rejected = findViewById(R.id.tv_rejected);
        tv_upcoming = findViewById(R.id.tv_upcoming);

        ll_total = findViewById(R.id.ll_total);
        ll_do = findViewById(R.id.ll_do);
        ll_nodal = findViewById(R.id.ll_nodal);
        ll_pending = findViewById(R.id.ll_pending);
        ll_completed = findViewById(R.id.ll_completed);
        ll_rejected = findViewById(R.id.ll_rejected);
        ll_upcoming = findViewById(R.id.ll_upcoming);

        tvRegWorker = findViewById(R.id.tvRegWorker);
        tvCampDate = findViewById(R.id.tvCampDate);

        calMonth = Calendar.getInstance();
        campDateCountList = new ArrayList<>();
        campList = new ArrayList<>();

        edtSelCampType = findViewById(R.id.edtSelCampType);
        tvCampStatusHeading = findViewById(R.id.tvCampStatusHeading);
        tvRegularOrD2dTotal = findViewById(R.id.tvRegularOrD2dTotal);
        rv_campList = findViewById(R.id.rv_campList);
        rv_campList.setLayoutManager(new LinearLayoutManager(context));

    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

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


//        if (Utilities.isNetworkAvailable(context)) {
//            new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode);
//        } else {
//            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//        }

        if (Utilities.isNetworkAvailable(context)) {

            new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode, String.valueOf(campTypeId));
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }

    private void setEventHandlers() {

        tvRegWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campCalendarListAdapter != null) {

                    if (isReversed) {
                        isReversed = false;
                        Collections.sort(campList, new CampCountComparatorReverse());
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                    } else {
                        isReversed = true;
                        Collections.sort(campList, new CampCountComparatorReverse().reversed());
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                    }
                    campCalendarListAdapter.notifyDataSetChanged();
                }


            }
        });
        tvCampDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campCalendarListAdapter != null) {

                    if (isReversed) {
                        isReversed = false;
                        Collections.sort(campList, new CampCountComparatorDate());
                        tvCampDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                    } else {
                        isReversed = true;
                        Collections.sort(campList, new CampCountComparatorDate().reversed());
                        tvCampDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                    }
                    campCalendarListAdapter.notifyDataSetChanged();
                }


            }
        });

        edtSelCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("All Camp", 0));
                campTypeModelArrayList.add(new CampTypeModel("Normal Camp", 1));
                campTypeModelArrayList.add(new CampTypeModel("D2D Camp", 3));

                showCampType(campTypeModelArrayList);
            }
        });


        edt_year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYearMonthAlert();
            }
        });

        ll_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "TOTAL")
                        .putExtra("campList", "")
                        .putExtra("selectedMonth", selectedMonthId)
                        .putExtra("selectedYear", selectedYearId)
                        .putExtra("selectedDist", districtCode)
                        .putExtra("campType", campTypeId));
            }
        });

        ll_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "BYDO")
                        .putExtra("campList", campList));

            }
        });

        ll_nodal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "NODAL")
                        .putExtra("campList", campList));
            }
        });

        ll_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "PENDING")
                        .putExtra("campList", campList));

            }
        });

        ll_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "COMPLETED")
                        .putExtra("campList", campList));
            }
        });

        ll_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "REJECTED")
                        .putExtra("campList", campList));
            }
        });

        ll_upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "2")
                        .putExtra("CAMPTYPR", "UPCOMING")
                        .putExtra("campList", campList));
            }
        });

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

    }

    private void showCampType(final ArrayList<CampTypeModel> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(String.valueOf(campTypeModelsList.get(i).getCampTypeName()));
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
                edtSelCampType.setText(campTypeModelsList.get(which).getCampTypeName());
                campTypeId = campTypeModelsList.get(which).getCampTypeId();

                if (Utilities.isNetworkAvailable(context)) {
//                    if (campTypeId == 0) {
//                        new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode);
//                    } else
                    new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode, String.valueOf(campTypeId));
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }

            }
        });
        builderSingle.show();
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
                        edt_year_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));

                        if (Utilities.isNetworkAvailable(context)) {
                            new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId + 1), String.valueOf(selectedYearId), districtCode);
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
                        groupCampDetails(campList);

                        ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();


                        if (campList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("Camps are not available.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(campList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListAdapter(context, campList);
                            rv_campList.setAdapter(campCalendarListAdapter);
                        }


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

//            if (Utilities.isNetworkAvailable(context)) {
//                campTypeId = 1;
//                new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode, String.valueOf(campTypeId));
//
//            } else {
//                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//            }
        }


    }


    private class GetMonthlySurveySiteRequestForOS extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("CampType", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
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
                        groupCampDetailsForOS(campList);


                        ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();


                        if (campList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("Camps are not available.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(campList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListAdapter(context, campList);
                            rv_campList.setAdapter(campCalendarListAdapter);
                        }


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

    private void groupCampDetails(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 1) {
            tvCampStatusHeading.setText("Normal Camp Status");
        } else if (campTypeId == 3) {
            tvCampStatusHeading.setText("D2D Camp Status");
        } else {
            tvCampStatusHeading.setText("Total Camp Status");

        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        campDateCountList = new ArrayList<>();
        try {
            for (int i = 0; i < campList.size(); i++) {
                CampCalendarModel.OutputBean dt = campList.get(i);

                if (campDateCountList.size() <= 0) {
                    CampCalenderDatesModel fdt = new CampCalenderDatesModel();
                    fdt.setDate(dt.getCampDate());
                    fdt.setCampCount(1);
                    fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
                    fdt.setMonth(dt.getCampDate());
                    fdt.setYear(dt.getCampDate());

                    campDateCountList.add(fdt);

                } else {

                    ArrayList<String> date = new ArrayList<>();
                    for (int j = 0; j < campDateCountList.size(); j++) {
                        date.add(campDateCountList.get(j).getDate());
                    }

                    if (date.contains(dt.getCampDate())) {
                        for (int j = 0; j < campDateCountList.size(); j++) {
                            CampCalenderDatesModel fdt1 = campDateCountList.get(j);

                            if (fdt1.getDate().equalsIgnoreCase(dt.getCampDate())) {
                                fdt1.setCampCount(fdt1.getCampCount() + 1);
                            }
                        }
                    } else {
                        CampCalenderDatesModel fdt = new CampCalenderDatesModel();

                        fdt.setDate(dt.getCampDate());
                        fdt.setCampCount(1);
                        fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
                        fdt.setMonth(dt.getCampDate());
                        fdt.setYear(dt.getCampDate());
                        campDateCountList.add(fdt);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0;

        for (int i = 0; i < campList.size(); i++) {
            total = total + 1;

            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
                bydo = bydo + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
                nodal = nodal + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
                pending = pending + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
                completed = completed + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
                rejected = rejected + 1;
            }

            try {
                if (new SimpleDateFormat("yyyy-MM-dd").parse(campList.get(i).getCampDate()).after(Calendar.getInstance().getTime())) {
                    upcoming = upcoming + 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tv_total.setText("" + total);
        tv_do.setText("" + bydo);
        tv_nodal.setText("" + nodal);
        tv_pending.setText("" + pending);
        tv_completed.setText("" + completed);
        tv_rejected.setText("" + rejected);
        tv_upcoming.setText("" + upcoming);
    }

    private void groupCampDetailsForOS(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 1) {
            tvCampStatusHeading.setText("Normal Camp Status Dashboard");
        } else if (campTypeId == 3) {
            tvCampStatusHeading.setText("D2D Camp Status Dashboard");
        }
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        campDateCountList = new ArrayList<>();
        try {
            for (int i = 0; i < campList.size(); i++) {
                CampCalendarModel.OutputBean dt = campList.get(i);

                if (campDateCountList.size() <= 0) {
                    CampCalenderDatesModel fdt = new CampCalenderDatesModel();
                    fdt.setDate(dt.getCampDate());
                    fdt.setCampCount(1);
                    fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
                    fdt.setMonth(dt.getCampDate());
                    fdt.setYear(dt.getCampDate());

                    campDateCountList.add(fdt);

                } else {

                    ArrayList<String> date = new ArrayList<>();
                    for (int j = 0; j < campDateCountList.size(); j++) {
                        date.add(campDateCountList.get(j).getDate());
                    }

                    if (date.contains(dt.getCampDate())) {
                        for (int j = 0; j < campDateCountList.size(); j++) {
                            CampCalenderDatesModel fdt1 = campDateCountList.get(j);

                            if (fdt1.getDate().equalsIgnoreCase(dt.getCampDate())) {
                                fdt1.setCampCount(fdt1.getCampCount() + 1);
                            }
                        }
                    } else {
                        CampCalenderDatesModel fdt = new CampCalenderDatesModel();

                        fdt.setDate(dt.getCampDate());
                        fdt.setCampCount(1);
                        fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
                        fdt.setMonth(dt.getCampDate());
                        fdt.setYear(dt.getCampDate());
                        campDateCountList.add(fdt);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0;

        for (int i = 0; i < campList.size(); i++) {
            total = total + 1;

            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
                bydo = bydo + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
                nodal = nodal + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
                pending = pending + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
                completed = completed + 1;
            }

            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
                rejected = rejected + 1;
            }

            try {
                if (new SimpleDateFormat("yyyy-MM-dd").parse(campList.get(i).getCampDate()).after(Calendar.getInstance().getTime())) {
                    upcoming = upcoming + 1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        if (campTypeId == 1 || campTypeId == 3) {
//            tvRegularOrD2dTotal.setText(String.valueOf(total));
//        }
        tv_total.setText("" + total);
        tv_do.setText("" + bydo);
        tv_nodal.setText("" + nodal);
        tv_pending.setText("" + pending);
        tv_completed.setText("" + completed);
        tv_rejected.setText("" + rejected);
        tv_upcoming.setText("" + upcoming);
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
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
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
//                    if (campTypeId == 0) {
//                        new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode);
//                    } else
                    new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), districtCode, String.valueOf(campTypeId));
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
        builderSingle.show();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Conducted Camp");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // creates the comparator for comparing name
    class CampCountComparator implements Comparator<CampCalendarModel.OutputBean> {

        // override the compare() method
        public int compare(CampCalendarModel.OutputBean s1, CampCalendarModel.OutputBean s2) {
            return Integer.valueOf(s1.getREGISTERWORKERS()) > Integer.valueOf(s2.getREGISTERWORKERS()) ? 1 : 0;
        }
    }// creates the comparator for comparing name

    class CampCountComparatorReverse implements Comparator<CampCalendarModel.OutputBean> {

        // override the compare() method
        public int compare(CampCalendarModel.OutputBean s1, CampCalendarModel.OutputBean s2) {
            if (Integer.valueOf(s1.getREGISTERWORKERS()) == Integer.valueOf(s2.getREGISTERWORKERS()))
                return 0;
            else if (Integer.valueOf(s1.getREGISTERWORKERS()) > Integer.valueOf(s2.getREGISTERWORKERS()))
                return 1;
            else
                return -1;
        }
    }

    class CampCountComparatorDate implements Comparator<CampCalendarModel.OutputBean> {

        // override the compare() method
        public int compare(CampCalendarModel.OutputBean s1, CampCalendarModel.OutputBean s2) {
            Date s1Date = null;
            Date s2Date = null;
            try {
                s1Date = new SimpleDateFormat("yyyy-MM-dd").parse(s1.getCampDate());
                s2Date = new SimpleDateFormat("yyyy-MM-dd").parse(s2.getCampDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            if (Integer.valueOf(s1.getREGISTERWORKERS()) != 0 &&
//                    Integer.valueOf(s2.getREGISTERWORKERS()) != 0) {
//                if (s1Date.equals(s2Date))
//                    return 0;
//                else if (s1Date.after(s2Date))
//                    return 1;
//                else
//                    return -1;
//            } else {
            if (s1Date.equals(s2Date))
                return 0;
            else if (s1Date.after(s2Date))
                return 1;
            else
                return -1;

//            }

        }
    }

}
