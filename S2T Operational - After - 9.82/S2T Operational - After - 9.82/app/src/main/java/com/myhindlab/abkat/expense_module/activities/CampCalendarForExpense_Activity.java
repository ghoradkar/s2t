package com.myhindlab.abkat.expense_module.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.CampCalendarList_Activity;
import com.myhindlab.abkat.adapters.CampCalendarAdapter;
import com.myhindlab.abkat.adapters.WeekAdapter;
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
import java.util.Date;
import java.util.List;

public class CampCalendarForExpense_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private TextView tv_title;
    private EditText edt_selectdistrict, edt_district, edtSelCampType;
    private ImageView imv_previous, imv_next;
    private GridView gdv_week, gdv_cal;
    private CardView cv_campstatuscount;

    private TextView tvCampStatusHeading, tv_total, tv_do, tv_nodal, tv_pending, tv_completed, tvZeroCampCount, tv_rejected, tv_upcoming, tv_months_beneficiary, tv_todays_beneficiary;
    private LinearLayout ll_total, ll_do, ll_nodal, ll_pending, ll_completed, ll_rejected, ll_upcoming;

    private WeekAdapter weekAdapter;
    private CampCalendarAdapter campCalAdapter;

    private String[] weekday = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private Calendar calMonth;
    private ArrayList<CampCalendarModel.OutputBean> campList;
    private ArrayList<CampCalenderDatesModel> campDateCountList;
    private TextView tv_selectyear, tv_selectmonth;
    private String user_id, year, month, monthName;
    private int mYear, mMonth, mDay, selectedMonthId, selectedYearId;
    private Handler handler;
    private String DESGID, DISTLGDCODE, district, TALLGDCODE, taluka;
    private UserSessionManager session;
    private int campTypeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_calendar_for_expense);

        init();
        getSessionData();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = CampCalendarForExpense_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        edt_district = findViewById(R.id.edt_district);
        tv_title = findViewById(R.id.tv_title);
        imv_previous = findViewById(R.id.imv_previous);
        imv_next = findViewById(R.id.imv_next);
        gdv_week = findViewById(R.id.gdv_week);
        gdv_cal = findViewById(R.id.gdv_cal);
        cv_campstatuscount = findViewById(R.id.cv_campstatuscount);
        edtSelCampType = findViewById(R.id.edtSelCampType);


        tv_total = findViewById(R.id.tv_total);
        tv_do = findViewById(R.id.tv_do);
        tv_pending = findViewById(R.id.tv_pending);
        tv_completed = findViewById(R.id.tv_completed);
        tv_nodal = findViewById(R.id.tv_nodal);
        tv_rejected = findViewById(R.id.tv_rejected);
        tv_upcoming = findViewById(R.id.tv_upcoming);
        tv_months_beneficiary = findViewById(R.id.tv_months_beneficiary);
        tv_todays_beneficiary = findViewById(R.id.tv_todays_beneficiary);
        tvZeroCampCount = findViewById(R.id.tvZeroCampCount);

        ll_total = findViewById(R.id.ll_total);
        ll_do = findViewById(R.id.ll_do);
        ll_nodal = findViewById(R.id.ll_nodal);
        ll_pending = findViewById(R.id.ll_pending);
        ll_completed = findViewById(R.id.ll_completed);
        ll_rejected = findViewById(R.id.ll_rejected);
        ll_upcoming = findViewById(R.id.ll_upcoming);

        tvCampStatusHeading = findViewById(R.id.tvCampStatusHeading);

        calMonth = Calendar.getInstance();
        campDateCountList = new ArrayList<>();
        campList = new ArrayList<>();
        campCalAdapter = new CampCalendarAdapter(context, calMonth, campDateCountList);

        edtSelCampType.setText("All Camp");


    }

    private void getSessionData() {

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");

                edt_selectdistrict.setText(district);
                if (DESGID.equals("84")) {
                    edt_selectdistrict.setEnabled(true);

                } else {
                    edt_selectdistrict.setEnabled(false);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setDefaults() {

        if (DESGID.equals("24") || DESGID.equals("26")) {
            edt_district.setVisibility(View.VISIBLE);
            edt_selectdistrict.setVisibility(View.GONE);
            edt_district.setText(district);
        } else if (DESGID.equals("51") || DESGID.equals("63")) {
            DISTLGDCODE = "0";
            edt_district.setVisibility(View.GONE);
            edt_selectdistrict.setVisibility(View.VISIBLE);
        } else {
            edt_district.setVisibility(View.GONE);
            edt_selectdistrict.setVisibility(View.VISIBLE);
        }


        weekAdapter = new WeekAdapter(context, weekday);
        gdv_week.setAdapter(weekAdapter);

        tv_title.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));

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
//            new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE);
            new GetMonthlySurveySiteRequestForOS().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }


    private void setEventHandlers() {

        edtSelCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("All Camp", 0));
                campTypeModelArrayList.add(new CampTypeModel("Regular Camp", 1));
                campTypeModelArrayList.add(new CampTypeModel("D2D Camp", 3));

                showCampType(campTypeModelArrayList);
            }
        });


        gdv_cal.setOnItemClickListener((parent, view, position, id) -> {
            TextView date = view.findViewById(R.id.date);

            if (date != null && !date.getText().equals("")) {

                String stringDate = date.getText().toString().trim();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat format2 = new SimpleDateFormat("dd");
                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                for (int i = 0; i < campList.size(); i++) {
                    try {
                        if (Integer.parseInt(format2.format(format1.parse(campList.get(i).getCampDate()))) == Integer.parseInt(stringDate)) {
                            filteredCampList.add(campList.get(i));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (filteredCampList.size() > 0) {
                    startActivity(new Intent(context, CampCalendarListForExpense_Activity.class)
                            .putExtra("TYPE", "1")
                            .putExtra("date", date.getText().toString().trim())
                            .putExtra("campList", filteredCampList)
                            .putExtra("selectedMonth", selectedMonthId)
                            .putExtra("selectedYear", selectedYearId)
                            .putExtra("selectedDist", DISTLGDCODE)
                            .putExtra("campType", campTypeId));
                }
            }
        });

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYearMonthAlert();
            }
        });

        imv_previous.setOnClickListener(v -> {
            if (calMonth.get(Calendar.MONTH) == calMonth.getActualMinimum(Calendar.MONTH)) {
                calMonth.set((calMonth.get(Calendar.YEAR) - 1), calMonth.getActualMaximum(Calendar.MONTH), 1);

            } else {
                calMonth.set(Calendar.MONTH, calMonth.get(Calendar.MONTH) - 1);
            }
            refreshCalendar();
        });

        imv_next.setOnClickListener(v -> {
            if (calMonth.get(Calendar.MONTH) == calMonth.getActualMaximum(Calendar.MONTH)) {
                calMonth.set((calMonth.get(Calendar.YEAR) + 1), calMonth.getActualMinimum(Calendar.MONTH), 1);
            } else {
                calMonth.set(Calendar.MONTH, calMonth.get(Calendar.MONTH) + 1);
            }
            refreshCalendar();
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
                        .putExtra("selectedDist", DISTLGDCODE)
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
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
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

                refreshCalendar();


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
                        refreshCalendar();
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

    public void refreshCalendar() {
        campCalAdapter.refreshDays();
        String[] monthYear = (android.text.format.DateFormat.format("MM yyyy", calMonth)).toString().split(" ");
        month = monthYear[0];
        year = monthYear[1];
        selectedYearId = Integer.parseInt(year);
        selectedMonthId = Integer.parseInt(month);

        if (Utilities.isNetworkAvailable(context)) {
//            if (campTypeId == 0) {
//                new GetMonthlySurveySiteRequest().execute(month, String.valueOf(selectedYearId), DISTLGDCODE);
//            } else
            new GetMonthlySurveySiteRequestForOS().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }

        campCalAdapter.notifyDataSetChanged();
        handler = new Handler();
        handler.post(calendarUpdater);
        tv_title.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));
    }

    public Runnable calendarUpdater = new Runnable() {
        @Override
        public void run() {
            campCalAdapter.setItems(campDateCountList);
            campCalAdapter.notifyDataSetChanged();
        }
    };

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

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequest, param);
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
                        gdv_week.setVisibility(View.VISIBLE);
                        gdv_cal.setVisibility(View.VISIBLE);
//                        cv_campstatuscount.setVisibility(View.VISIBLE);

                        campList = pojoDetails.getOutput();
                        groupCampDetails(campList);
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
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

                        gdv_week.setVisibility(View.GONE);
                        gdv_cal.setVisibility(View.GONE);
                        cv_campstatuscount.setVisibility(View.GONE);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
                    gdv_week.setVisibility(View.GONE);
                    gdv_cal.setVisibility(View.GONE);
                    cv_campstatuscount.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
                gdv_week.setVisibility(View.GONE);
                gdv_cal.setVisibility(View.GONE);
                cv_campstatuscount.setVisibility(View.GONE);
            }
            setEventHandlers();
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

         //   res = WebServiceCall.APICall(ApplicationConstants.GetMonthlyCalenderForAdvancesTaken, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlyCalenderForAdvancesTaken_Close, ApplicationConstants.webservice_d2d, param);

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
                        gdv_week.setVisibility(View.VISIBLE);
                        gdv_cal.setVisibility(View.VISIBLE);
//                        cv_campstatuscount.setVisibility(View.VISIBLE);

                        campList = pojoDetails.getOutput();
                        groupCampDetails(campList);
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
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

                        gdv_week.setVisibility(View.GONE);
                        gdv_cal.setVisibility(View.GONE);
                        cv_campstatuscount.setVisibility(View.GONE);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);
                    gdv_week.setVisibility(View.GONE);
                    gdv_cal.setVisibility(View.GONE);
                    cv_campstatuscount.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);
                gdv_week.setVisibility(View.GONE);
                gdv_cal.setVisibility(View.GONE);
                cv_campstatuscount.setVisibility(View.GONE);
            }
            setEventHandlers();
        }
    }

    private void groupCampDetails(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 0) {
            tvCampStatusHeading.setText("Total Camp Status Dashboard");
        } else if (campTypeId == 1) {
            tvCampStatusHeading.setText("Regular Camp Status Dashboard");
        } else {
            tvCampStatusHeading.setText("D2D Camp Status Dashboard");
        }
        String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
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

        setAdapter(campDateCountList);

        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0, monthsCount = 0,
                todaysCount = 0, zeroCountCamp = 0;

        Calendar c1, c2;
        c1 = Calendar.getInstance();
        c2 = Calendar.getInstance();

        for (CampCalendarModel.OutputBean o :
                campList) {
            try {
                c1.setTime(Utilities.dfDate.parse(o.getCampDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (c1.before(c2)) {
                if (o.getREGISTERWORKERS().equalsIgnoreCase("0")) {
                    zeroCountCamp = zeroCountCamp + 1;
                }
            }
        }
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

            monthsCount = monthsCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());

            if (campList.get(i).getCampDate().equals(todaysDate)) {
                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
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
        tv_months_beneficiary.setText("" + monthsCount);
        tv_todays_beneficiary.setText("" + todaysCount);
        tvZeroCampCount.setText("" + zeroCountCamp);
    }

    private void setAdapter(ArrayList<CampCalenderDatesModel> campDateCountList) {
        campCalAdapter = new CampCalendarAdapter(context, calMonth, campDateCountList);
        weekAdapter = new WeekAdapter(context, weekday);
        gdv_week.setAdapter(weekAdapter);
        gdv_cal.setAdapter(campCalAdapter);
        tv_title.setText(android.text.format.DateFormat.format("MMMM yyyy", calMonth));
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

          //  res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, ApplicationConstants.webservice_d2d, param);

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
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
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
                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
                refreshCalendar();
            }
        });
        builderSingle.show();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Calender");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (DESGID.equals("63") || DESGID.equals("26") || DESGID.equals("24")) {
            ImageView logout = findViewById(R.id.img_logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
                    builder.setMessage("Are you sure you want to log out?");
                    builder.setTitle("Alert");
                    builder.setIcon(R.drawable.icon_alertred);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            session.logoutUser();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    androidx.appcompat.app.AlertDialog alertD = builder.create();
                    alertD.show();
                }
            });
        } else {
            ImageView logout = findViewById(R.id.img_logout);
            logout.setVisibility(View.GONE);
        }
    }
}
