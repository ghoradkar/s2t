package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.PendingCountActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.ProcessingLabCountActivity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter.LiverCountDistrictWiseAdapter;
import com.myhindlab.abkat.activities.payout.PendingProcessingCountModel;
import com.myhindlab.abkat.adapters.CampCalendarAdapter;
import com.myhindlab.abkat.adapters.WeekAdapter;
import com.myhindlab.abkat.adapters.doortodoor.AdminActiveInactiveTeamAdapter;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampCalenderDatesModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.CampTypeResponseModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;
import com.myhindlab.abkat.models.ProcessingLabCountModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaModel;
import com.myhindlab.abkat.models.doortodoor.AdminActiveInactiveModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampCalendar_Activity extends AppCompatActivity {

    private Context context;
    private ProgressDialog pd;
    private TextView tv_title, tv_home_lab,tv_data_as_of, tv_hub_lab, tv_Month_year;
    private List<AdminActiveInactiveModel.Output> adminlist;
    private EditText edt_selectdistrict, edt_district, edt_organization, edt_division, edtSelCampType;
    private ImageView imv_previous, imv_next;
    private GridView gdv_week, gdv_cal;
    private CardView cv_campstatuscount;
    private TextView tvCampStatusHeading, tvTodaysUpdatedTime, tv_total,tvPendingCount_hublab,tvProcessingCount,tvProcessingCount_hublab,tvPendingCount, tv_do, tv_nodal, tv_pending, tv_completed, tvZeroCampCount, tv_rejected, tv_upcoming, tv_months_beneficiary, tv_todays_beneficiary;
    private LinearLayout ll_total,ll_processing_count, ll_do, ll_nodal,ll_pending_count,ll_pending_count_hublab, ll_pending, ll_completed, ll_rejected, ll_upcoming;

    private WeekAdapter weekAdapter;
    private CampCalendarAdapter campCalAdapter;

    private String[] weekday = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private Calendar calMonth;
    private ArrayList<CampCalendarModel.OutputBean> campList;
    private ArrayList<CampCalenderDatesModel> campDateCountList;
    private TextView tv_selectyear, tv_selectmonth;
    private String year, month,clusterWiseFlag = "1", monthName;
    private int mYear, mMonth, mDay, selectedMonthId, selectedYearId;
    private Handler handler;
    private String DESGID, organizationId = "0", divisionId = "0", DISTLGDCODE, district, empcode, TALLGDCODE, taluka;
    private UserSessionManager session;
    private int campTypeId = 0;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_calendar);

        init();
        getSessionData();
        setDefaults();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = CampCalendar_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);
        edt_selectdistrict = findViewById(R.id.edt_selectdistrict);
        edt_district = findViewById(R.id.edt_district);
        edt_organization = findViewById(R.id.edt_organization);
        edt_division = findViewById(R.id.edt_division);
        tv_title = findViewById(R.id.tv_title);
        tv_home_lab = findViewById(R.id.tv_home_lab);
        tv_data_as_of = findViewById(R.id.tv_data_as_of);
        tv_hub_lab = findViewById(R.id.tv_hub_lab);
        imv_previous = findViewById(R.id.imv_previous);
        tv_Month_year = findViewById(R.id.tv_Month_year);
        imv_next = findViewById(R.id.imv_next);
        gdv_week = findViewById(R.id.gdv_week);
        gdv_cal = findViewById(R.id.gdv_cal);
        cv_campstatuscount = findViewById(R.id.cv_campstatuscount);
        edtSelCampType = findViewById(R.id.edtSelCampType);
        tvTodaysUpdatedTime = findViewById(R.id.tvTodaysUpdatedTime);

        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        tv_total = findViewById(R.id.tv_total);
        tvProcessingCount = findViewById(R.id.tvProcessingCount);
        tvProcessingCount_hublab = findViewById(R.id.tvProcessingCount_hublab);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvPendingCount_hublab = findViewById(R.id.tvPendingCount_hublab);
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
        ll_processing_count = findViewById(R.id.ll_processing_count);
        ll_do = findViewById(R.id.ll_do);
        ll_nodal = findViewById(R.id.ll_nodal);
        ll_pending_count_hublab = findViewById(R.id.ll_pending_count_hublab);
        ll_pending_count = findViewById(R.id.ll_pending_count);
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
                empcode = json.getString("EmpCode");
                //  edt_selectdistrict.setText(json.getString("district"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void setDefaults() {

        if (DESGID.equals("24")) {
            edt_district.setVisibility(View.VISIBLE);
            edt_selectdistrict.setVisibility(View.GONE);
            edt_district.setText(district);
        } else if (DESGID.equals("51") || DESGID.equals("63")
                || DESGID.equals("201") || DESGID.equals("103")
                || DESGID.equals("47") || DESGID.equals("102")
                || DESGID.equals("151") || DESGID.equals("108")
                || DESGID.equals("148") || DESGID.equals("166")
                || DESGID.equals("83") || DESGID.equals("168")
                || DESGID.equals("173") || DESGID.equals("171")
                || DESGID.equals("170") || DESGID.equals("183")
                || DESGID.equals("182") || DESGID.equals("26")
                || DESGID.equals("196") || DESGID.equals("198")
                || DESGID.equals("75") || DESGID.equals("202")

        ) {
            DISTLGDCODE = "0";
            edt_district.setVisibility(View.GONE);
            edt_selectdistrict.setVisibility(View.VISIBLE);
        }

//        if (DESGID.equals("51") || DESGID.equals("166") || DESGID.equals("47")) {
//            edt_selectdistrict.setEnabled(true);
//        } else {
////            edt_selectdistrict.setEnabled(false);
//        }


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


        if (BuildConfig.isBeta){
//            getClusterWiseFlag();
        }
//
        if (Utilities.isNetworkAvailable(context)) {
//            new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE);
//            new GetMonthlySurveySiteRequestForOS().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));


            if (DESGID.equals("51") || DESGID.equals("166")|| DESGID.equals("201") || DESGID.equals("47") || DESGID.equals("83") || DESGID.equals("202")) {
                new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), "0", "0", empcode, DESGID);
//                new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), "0", "0", empcode, DESGID);



                getTotalCampAndBeneficiaryData(
                        month,
                        String.valueOf(selectedYearId),
                        DISTLGDCODE,
                        String.valueOf(campTypeId),
                        "0",
                        "0",
                        empcode,
                        DESGID

                );



                getProcessingCountData();
            } else {
//            edt_selectdistrict.setEnabled(false);

                new GetOrganizationNew().execute(empcode, DESGID);


            }
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
    }


    private void setEventHandlers() {

        edtSelCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utilities.isNetworkAvailable(context)) {

                    getCampTypes();
                }
//                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
//                campTypeModelArrayList.add(new CampTypeModel("All Camp", 0));
//                campTypeModelArrayList.add(new CampTypeModel("Regular Camp", 1));
//                campTypeModelArrayList.add(new CampTypeModel("CSC Regular Camp", 2));
//                campTypeModelArrayList.add(new CampTypeModel("D2D Camp", 3));
//                campTypeModelArrayList.add(new CampTypeModel("CSC D2D Camp", 4));
//
//                showCampType(campTypeModelArrayList);
            }
        });


        tv_hub_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(context, ProcessingLabCountActivity.class)
                        .putExtra("hubLab", "1")
                        .putExtra("districtCode", DISTLGDCODE)
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("monthId", month)
                        .putExtra("yearId", String.valueOf(selectedYearId))

                );

            }
        });
        ll_pending_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(context, PendingCountActivity.class)
                        .putExtra("hubLab", "1")
                        .putExtra("districtCode", DISTLGDCODE)
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("campType", String.valueOf(campTypeId))
                        .putExtra("monthId", month)
                        .putExtra("yearId", String.valueOf(selectedYearId))

                );

            }
        });
        ll_pending_count_hublab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                startActivity(new Intent(context, PendingCountActivity.class)
                        .putExtra("hubLab", "2")
                        .putExtra("districtCode", DISTLGDCODE)
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("campType", String.valueOf(campTypeId))
                        .putExtra("monthId", month)
                        .putExtra("yearId", String.valueOf(selectedYearId))

                );

            }
        });
        tv_home_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(context, ProcessingLabCountActivity.class)
                        .putExtra("hubLab", "2")
                        .putExtra("districtCode", DISTLGDCODE)
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("monthId", month)
                        .putExtra("yearId", String.valueOf(selectedYearId))
                        );
            }
        });

        gdv_cal.setOnItemClickListener((parent, view, position, id) -> {
            TextView date = view.findViewById(R.id.date);

            if (date != null && !date.getText().equals("")) {

//                String stringDate = date.getText().toString().trim();
//                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
//                DateFormat format2 = new SimpleDateFormat("dd");
//                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();
//
//                for (int i = 0; i < campList.size(); i++) {
//                    try {
//                        if (Integer.parseInt(format2.format(format1.parse(campList.get(i).getCampDate()))) == Integer.parseInt(stringDate)) {
//                            filteredCampList.add(campList.get(i));
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }

                startActivity(new Intent(context, CampCalendarList_Activity.class)
                        .putExtra("TYPE", "1")
                        .putExtra("date", date.getText().toString().trim())
                        .putExtra("campList", "")
                        .putExtra("selectedMonth", selectedMonthId)
                        .putExtra("selectedYear", selectedYearId)
                        .putExtra("selectedDist", DISTLGDCODE)
                        .putExtra("campType", campTypeId)
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("clusterId", clusterWiseFlag)

                );
//                startActivity(new Intent(context, NestedCampList_Activity.class)
//                        .putExtra("TYPE", "1")
//                        .putExtra("date", date.getText().toString().trim())
//                        .putExtra("campList", "")
//                        .putExtra("selectedMonth", selectedMonthId)
//                        .putExtra("selectedYear", selectedYearId)
//                        .putExtra("selectedDist", DISTLGDCODE)
//                        .putExtra("campType", campTypeId));
            }
        });

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYearMonthAlert();
            }
        });
        edt_organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrganization().execute(empcode, DESGID);
            }
        });
        edt_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_organization.getText().toString().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Please select sub organization", false);
                    return;
                }
                new GetDivision().execute(organizationId, empcode, DESGID);
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
                        .putExtra("organization", organizationId)
                        .putExtra("division", divisionId)
                        .putExtra("campType", campTypeId)
                        .putExtra("clusterId", clusterWiseFlag)

                );

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


                if (DESGID.equals("51") || DESGID.equals("166")|| DESGID.equals("201") || DESGID.equals("47") || DESGID.equals("83") ||DESGID.equals("202")) {

                    if (Utilities.isNetworkAvailable(context)) {
                        new GetDistrictListFor().execute(organizationId, empcode, DESGID, divisionId, "0");
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                } else {

                    if (edt_organization.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select sub organization", false);
                        return;
                    }
                    if (edt_division.getText().toString().isEmpty()) {
                        Utilities.showAlertDialog(context, "Alert", "Please select division", false);
                        return;
                    }

                    if (Utilities.isNetworkAvailable(context)) {
                        new GetDistrictListFor().execute(organizationId, empcode, DESGID, divisionId, "0");
                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }


            }
        });

    }

    private void showCampType(final ArrayList<CampTypeResponseModel.Output> campTypeModelsList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Camp Type");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < campTypeModelsList.size(); i++) {
            arrayAdapter.add(campTypeModelsList.get(i).getCampTypeDescription());
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
                edtSelCampType.setText(campTypeModelsList.get(which).getCampTypeDescription());
                campTypeId = campTypeModelsList.get(which).getCamptype();

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


        clearCampTextViews();

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
//            new GetMonthlySurveySiteRequestForOS().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));
//            new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));
//            new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

            if (Utilities.isNetworkAvailable(context)) {
//            new GetMonthlySurveySiteRequest().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE);
//            new GetMonthlySurveySiteRequestForOS().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));


                if (clusterWiseFlag.equalsIgnoreCase("1")){


                    if (DESGID.equals("51") || DESGID.equals("201")|| DESGID.equals("166") || DESGID.equals("47") || DESGID.equals("83") || DESGID.equals("202")) {
                        new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
                        getProcessingCountData();
//                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);

                        getTotalCampAndBeneficiaryData(
                                month,
                                String.valueOf(selectedYearId),
                                DISTLGDCODE,
                                String.valueOf(campTypeId),
                                organizationId,
                                divisionId,
                                empcode,
                                DESGID

                        );

                    } else {

                        new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);

                        getTotalCampAndBeneficiaryData(
                                month,
                                String.valueOf(selectedYearId),
                                DISTLGDCODE,
                                String.valueOf(campTypeId),
                                organizationId,
                                divisionId,
                                empcode,
                                DESGID

                        );


                    }

                } else if (clusterWiseFlag.equalsIgnoreCase("2")) {


                    if (DESGID.equals("51") || DESGID.equals("201")|| DESGID.equals("166") || DESGID.equals("47") || DESGID.equals("83")||DESGID.equals("202")) {
                        new GetMonthlySurveySiteRequestForOSNewClusterWise().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                        getProcessingCountData();
//                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);

                        getTotalCampAndBeneficiaryDataClusterWise(
                                month,
                                String.valueOf(selectedYearId),
                                DISTLGDCODE,
                                String.valueOf(campTypeId),
                                organizationId,
                                divisionId,
                                empcode,
                                DESGID

                        );

                    } else {

                        new GetMonthlySurveySiteRequestForOSNewClusterWise().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);

                        getTotalCampAndBeneficiaryDataClusterWise(
                                month,
                                String.valueOf(selectedYearId),
                                DISTLGDCODE,
                                String.valueOf(campTypeId),
                                organizationId,
                                divisionId,
                                empcode,
                                DESGID

                        );


                    }



                }


            } else {
                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
            }


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

//    private class GetTotalcampAndTotalBeneficiarywithZeroCamp extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("MonthId", params[0]));
//            param.add(new ParamsPojo("Year", params[1]));
//            param.add(new ParamsPojo("Distcode", params[2]));
//            param.add(new ParamsPojo("CampType", params[3]));
//            param.add(new ParamsPojo("SubOrgId", params[4]));
//            param.add(new ParamsPojo("DIVID", params[5]));
//            param.add(new ParamsPojo("UserId", params[6]));
//            param.add(new ParamsPojo("DESGID", params[7]));
//
////            res = WebServiceCall.APICall(ApplicationConstants.GetTotalcampAndTotalBeneficiarywithZeroCamp, ApplicationConstants.webservice_d2d, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetTotalcampAndTotalBeneficiarywithZeroCamp_Org, ApplicationConstants.webservice_d2d, param);
//            return res;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
//
//                    AdminActiveInactiveModel adminActiveInactiveModel = new Gson().fromJson(result, AdminActiveInactiveModel.class);
//                    type = adminActiveInactiveModel.getStatus();
//                    message = adminActiveInactiveModel.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        adminlist = adminActiveInactiveModel.getOutput();
//                        if (adminlist != null && adminlist.size() > 0) {
//
////                            rv_d2dTeams.setAdapter(new AdminActiveInactiveTeamAdapter(context, adminlist));
//
//
//                            if (adminActiveInactiveModel.getOutput().size() > 0) {
//                                AdminActiveInactiveModel.Output output = adminActiveInactiveModel.getOutput().get(0);
//                                tv_total.setText(String.valueOf(output.getTotalCamps()));
//                                tv_months_beneficiary.setText(String.valueOf(output.getTotalMonthsBeneficiaryCount()));
//                                tv_todays_beneficiary.setText(String.valueOf(output.getTodayBeneficiaryCount()));
//                                tvZeroCampCount.setText(String.valueOf(output.getZeroCountCamp()));
//
//                            }
//
//                        } else {
//
//                            tv_total.setText("");
//                            tv_months_beneficiary.setText("");
//                            tv_todays_beneficiary.setText("");
//                            tvZeroCampCount.setText("");
//
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//
//                        tv_total.setText("");
//                        tv_months_beneficiary.setText("");
//                        tv_todays_beneficiary.setText("");
//                        tvZeroCampCount.setText("");
//
//
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
//            }
//        }
//    }



//    private void getTotalCampAndBeneficiaryData(String monthId, String year, String distCode, String campType, String subOrgId, String divId, String userId, String desgId) {
//        final ProgressDialog progressDialog = new ProgressDialog(CampCalendar_Activity.this);
//        progressDialog.setMessage("Please wait . . . ");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
//
//
//        Call<AdminActiveInactiveModel> call = apiService.getTotalCampAndBeneficiaryData(
//                monthId, year, distCode, campType, subOrgId, divId, userId, desgId
//        );
//
//        call.enqueue(new Callback<AdminActiveInactiveModel>() {
//            @Override
//            public void onResponse(Call<AdminActiveInactiveModel> call, Response<AdminActiveInactiveModel> response) {
//                progressDialog.dismiss();
//
//                if (response.isSuccessful() && response.body() != null) {
//                    AdminActiveInactiveModel model = response.body();
//                    String status = model.getStatus();
//                    String message = model.getMessage();
//
//                    if (status.equalsIgnoreCase("success")) {
//                        List<AdminActiveInactiveModel.Output> adminList = model.getOutput();
//
//                        if (adminList != null && !adminList.isEmpty()) {
//                            AdminActiveInactiveModel.Output output = adminList.get(0);
//
//                            tv_total.setText(String.valueOf(output.getTotalCamps()));
//                            tv_months_beneficiary.setText(String.valueOf(output.getTotalMonthsBeneficiaryCount()));
//                            tv_todays_beneficiary.setText(String.valueOf(output.getTodayBeneficiaryCount()));
//                            tvZeroCampCount.setText(String.valueOf(output.getZeroCountCamp()));
//                        } else {
//                            clearCampTextViews();
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//
////                            tv_total.setText("NA");
////                            tv_months_beneficiary.setText("NA");
////                            tv_todays_beneficiary.setText("NA");
////                            tvZeroCampCount.setText("NA");
////
//
//
//                        }
//                    } else {
//                        clearCampTextViews();
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//                    }
//                } else {
//                    clearCampTextViews();
//                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AdminActiveInactiveModel> call, Throwable t) {
//                progressDialog.dismiss();
//                clearCampTextViews();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
//            }
//        });
//    }
    private void getTotalCampAndBeneficiaryData(String monthId, String year, String distCode, String campType, String subOrgId, String divId, String userId, String desgId) {
        final ProgressDialog progressDialog = new ProgressDialog(CampCalendar_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<PendingProcessingCountModel> call = apiService.getTotalCampAndBeneficiaryData(
                monthId, year, distCode, campType, subOrgId, divId, userId, desgId
        );

        call.enqueue(new Callback<PendingProcessingCountModel>() {
            @Override
            public void onResponse(Call<PendingProcessingCountModel> call, Response<PendingProcessingCountModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    PendingProcessingCountModel model = response.body();
                    String status = model.getStatus();
                    String message = model.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        List<PendingProcessingCountModel.Output> adminList = model.getOutput();

                        if (adminList != null && !adminList.isEmpty()) {
                            PendingProcessingCountModel.Output output = adminList.get(0);

//                            tv_total.setText(String.valueOf(output.getTotalCamps()));
//                            tvZeroCampCount.setText(String.valueOf(output.getZeroCountCamp()));


                            tv_months_beneficiary.setText(String.valueOf(output.getTotalMonthsBeneficiaryCount()));
                            tv_todays_beneficiary.setText(String.valueOf(output.getTodaysBeneficiaryCount()));
                            tvProcessingCount.setText(String.valueOf(output.getHomeLabProcessedCount()));
                            tvProcessingCount_hublab.setText(String.valueOf(output.getHubLabProcessedCount()));
                            tvPendingCount.setText(String.valueOf(output.getHomeLabProcessedPendingCount()));
                            tvPendingCount_hublab.setText(String.valueOf(output.getHubLabProcessedPendingCount()));



                        } else {
                            clearCampTextViews();
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);

//                            tv_total.setText("NA");
//                            tv_months_beneficiary.setText("NA");
//                            tv_todays_beneficiary.setText("NA");
//                            tvZeroCampCount.setText("NA");
//


                        }
                    } else {
                        clearCampTextViews();
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    clearCampTextViews();
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<PendingProcessingCountModel> call, Throwable t) {
                progressDialog.dismiss();
                clearCampTextViews();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        });
    }


    private void getTotalCampAndBeneficiaryDataClusterWise(String monthId, String year, String distCode, String campType, String subOrgId, String divId, String userId, String desgId) {
        final ProgressDialog progressDialog = new ProgressDialog(CampCalendar_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<AdminActiveInactiveModel> call = apiService.getTotalCampAndBeneficiaryDataClusterWise(
                monthId, year, distCode, campType, subOrgId, divId, userId, desgId
        );

        call.enqueue(new Callback<AdminActiveInactiveModel>() {
            @Override
            public void onResponse(Call<AdminActiveInactiveModel> call, Response<AdminActiveInactiveModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    AdminActiveInactiveModel model = response.body();
                    String status = model.getStatus();
                    String message = model.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        List<AdminActiveInactiveModel.Output> adminList = model.getOutput();

                        if (adminList != null && !adminList.isEmpty()) {
                            AdminActiveInactiveModel.Output output = adminList.get(0);

                            tv_total.setText(String.valueOf(output.getTotalCamps()));
                            tv_months_beneficiary.setText(String.valueOf(output.getTotalMonthsBeneficiaryCount()));
                            tv_todays_beneficiary.setText(String.valueOf(output.getTodayBeneficiaryCount()));
                            tvZeroCampCount.setText(String.valueOf(output.getZeroCountCamp()));
                        } else {
                            clearCampTextViews();
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);

//                            tv_total.setText("NA");
//                            tv_months_beneficiary.setText("NA");
//                            tv_todays_beneficiary.setText("NA");
//                            tvZeroCampCount.setText("NA");
//


                        }
                    } else {
                        clearCampTextViews();
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    clearCampTextViews();
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<AdminActiveInactiveModel> call, Throwable t) {
                progressDialog.dismiss();
                clearCampTextViews();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        });
    }


    private void getClusterWiseFlag() {
        final ProgressDialog progressDialog = new ProgressDialog(CampCalendar_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);

        Call<AdminActiveInactiveModel> call = apiService.getClusterWiseFlag(Integer.parseInt(DESGID));

        call.enqueue(new Callback<AdminActiveInactiveModel>() {
            @Override
            public void onResponse(Call<AdminActiveInactiveModel> call, Response<AdminActiveInactiveModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    AdminActiveInactiveModel model = response.body();
                    String status = model.getStatus();
                    String message = model.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        List<AdminActiveInactiveModel.Output> adminList = model.getOutput();

                        if (adminList != null && !adminList.isEmpty()) {
                            AdminActiveInactiveModel.Output output = adminList.get(0);

                            clusterWiseFlag = output.getDataFlag();
//                            clusterWiseFlag = "2";


                            if (clusterWiseFlag!=null){
                                if (clusterWiseFlag.equalsIgnoreCase("1")){


                                    if (Utilities.isNetworkAvailable(context)) {


                                        if (DESGID.equals("51") || DESGID.equals("201")|| DESGID.equals("166") || DESGID.equals("47") || DESGID.equals("83")||DESGID.equals("202")) {
                                            new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), "0", "0", empcode, DESGID);


                                            getTotalCampAndBeneficiaryData(
                                                    month,
                                                    String.valueOf(selectedYearId),
                                                    DISTLGDCODE,
                                                    String.valueOf(campTypeId),
                                                    "0",
                                                    "0",
                                                    empcode,
                                                    DESGID
                                            );


                                            getProcessingCountData();

                                        } else {

                                            new GetOrganizationNew().execute(empcode, DESGID);


                                        }
                                    } else {
                                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                                    }

                                }else if (clusterWiseFlag.equalsIgnoreCase("2")){

                                    if (DESGID.equals("51") || DESGID.equals("201")|| DESGID.equals("166") || DESGID.equals("47") || DESGID.equals("83")) {
                                        new GetMonthlySurveySiteRequestForOSNewClusterWise().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), "0", "0", empcode, DESGID);


                                        getTotalCampAndBeneficiaryDataClusterWise(
                                                month,
                                                String.valueOf(selectedYearId),
                                                DISTLGDCODE,
                                                String.valueOf(campTypeId),
                                                "0",
                                                "0",
                                                empcode,
                                                DESGID
                                        );


//                                        getProcessingCountData();

                                    }else {
                                        new GetOrganizationNew().execute(empcode, DESGID);

                                    }





                                }

                            }else {
                                Utilities.showAlertDialog(context,"Alert","Cluster wise data flag getting null",false);
                            }


                        } else {
//                            clearCampTextViews();
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);

//                            tv_total.setText("NA");
//                            tv_months_beneficiary.setText("NA");
//                            tv_todays_beneficiary.setText("NA");
//                            tvZeroCampCount.setText("NA");
//


                        }
                    } else {
                        clearCampTextViews();
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    clearCampTextViews();
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            }

            @Override
            public void onFailure(Call<AdminActiveInactiveModel> call, Throwable t) {
                progressDialog.dismiss();
                clearCampTextViews();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        });
    }

    private void clearCampTextViews() {
//        tv_total.setText("");
//        tv_months_beneficiary.setText("");
//        tv_todays_beneficiary.setText("");
//        tvZeroCampCount.setText("");


        tv_total.setText("NA");
        tv_months_beneficiary.setText("NA");
        tv_todays_beneficiary.setText("NA");
        tvZeroCampCount.setText("NA");



        tv_home_lab.setText("NA");
        tv_hub_lab.setText("NA");
        tv_Month_year.setText("NA");
        tv_data_as_of.setText("NA");
        tvProcessingCount.setText("NA");
        tvProcessingCount_hublab.setText("NA");
        tvPendingCount.setText("NA");
        tvPendingCount_hublab.setText("NA");

    }

    private class GetMonthlySurveySiteRequestForOSNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("MonthId", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Distcode", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("SubOrgId", params[4]));
            param.add(new ParamsPojo("DIVID", params[5]));
            param.add(new ParamsPojo("UserId", params[6]));
            param.add(new ParamsPojo("DESGID", params[7]));

            // res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetCampCountWithDayAndMonthWiseWithCampType, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCampCountWithDayAndMonthWiseWithCampType_Org, ApplicationConstants.webservice_d2d, param);
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
                        cv_campstatuscount.setVisibility(View.VISIBLE);

                        campList = pojoDetails.getOutput();
                        groupCampDetailsNew(campList);


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_warning);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage(message);
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
//            setEventHandlers();
        }
    }
    private class GetMonthlySurveySiteRequestForOSNewClusterWise extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("MonthId", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("Distcode", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            param.add(new ParamsPojo("SubOrgId", params[4]));
            param.add(new ParamsPojo("DIVID", params[5]));
            param.add(new ParamsPojo("UserId", params[6]));
            param.add(new ParamsPojo("DESGID", params[7]));

            // res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
//            res = WebServiceCall.APICall(ApplicationConstants.GetCampCountWithDayAndMonthWiseWithCampType, ApplicationConstants.webservice_d2d, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCampCountWithDayAndMonthWiseWithCampType_Org_Cluster, ApplicationConstants.webservice_d2d, param);
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
                        cv_campstatuscount.setVisibility(View.VISIBLE);

                        campList = pojoDetails.getOutput();
                        groupCampDetailsNew(campList);

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
//            setEventHandlers();
        }
    }

    private void groupCampDetails(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 0) {
            tvCampStatusHeading.setText("Total Camp Status Dashboard");
        } else if (campTypeId == 1) {
            tvCampStatusHeading.setText("Normal Camp Status Dashboard");
        } else {
            tvCampStatusHeading.setText("D2D Camp Status Dashboard");
        }
        String dateTime = Utilities.dateTimeFormat.format(new Date());

        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);

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

//        for (int i = 0; i < campList.size(); i++) {
//            total = total + 1;
//
//            if (campList.get(i).getREGISTERWORKERS().equalsIgnoreCase("0")) {
//                zeroCountCamp = zeroCountCamp + 1;
//            }
//            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
//                bydo = bydo + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
//                nodal = nodal + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
//                pending = pending + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
//                completed = completed + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
//                rejected = rejected + 1;
//            }
//
//            monthsCount = monthsCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//
//            if (campList.get(i).getCampDate().equals(todaysDate)) {
//                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//            }
//
//            try {
//                if (new SimpleDateFormat("yyyy-MM-dd").parse(campList.get(i).getCampDate()).after(Calendar.getInstance().getTime())) {
//                    upcoming = upcoming + 1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

//        tv_total.setText("" + total);
//        tv_do.setText("" + bydo);
//        tv_nodal.setText("" + nodal);
//        tv_pending.setText("" + pending);
//        tv_completed.setText("" + completed);
//        tv_rejected.setText("" + rejected);
//        tv_upcoming.setText("" + upcoming);
//        tv_months_beneficiary.setText("" + monthsCount);
//        tv_todays_beneficiary.setText("" + todaysCount);
//        tvZeroCampCount.setText("" + zeroCountCamp);
    }

    private void groupCampDetailsNew(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 0) {
            tvCampStatusHeading.setText("Total Camp Status Dashboard");
        } else if (campTypeId == 1) {
            tvCampStatusHeading.setText("Normal Camp Status Dashboard");
        } else {
            tvCampStatusHeading.setText("D2D Camp Status Dashboard");
        }
        String dateTime = Utilities.dateTimeFormat.format(new Date());

        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);

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
                    fdt.setCampCount(Integer.parseInt(dt.getCampCount()));
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
                        fdt.setCampCount(Integer.parseInt(dt.getCampCount()));
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

//        for (int i = 0; i < campList.size(); i++) {
//            total = total + 1;
//
//            if (campList.get(i).getREGISTERWORKERS().equalsIgnoreCase("0")) {
//                zeroCountCamp = zeroCountCamp + 1;
//            }
//            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
//                bydo = bydo + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
//                nodal = nodal + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
//                pending = pending + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
//                completed = completed + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
//                rejected = rejected + 1;
//            }
//
//            monthsCount = monthsCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//
//            if (campList.get(i).getCampDate().equals(todaysDate)) {
//                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//            }
//
//            try {
//                if (new SimpleDateFormat("yyyy-MM-dd").parse(campList.get(i).getCampDate()).after(Calendar.getInstance().getTime())) {
//                    upcoming = upcoming + 1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

//        tv_total.setText("" + total);
//        tv_do.setText("" + bydo);
//        tv_nodal.setText("" + nodal);
//        tv_pending.setText("" + pending);
//        tv_completed.setText("" + completed);
//        tv_rejected.setText("" + rejected);
//        tv_upcoming.setText("" + upcoming);
//        tv_months_beneficiary.setText("" + monthsCount);
//        tv_todays_beneficiary.setText("" + todaysCount);
//        tvZeroCampCount.setText("" + zeroCountCamp);
    }

    private void groupCampDetailsCount(ArrayList<CampCalendarModel.OutputBean> campList) {
        if (campTypeId == 0) {
            tvCampStatusHeading.setText("Total Camp Status Dashboard");
        } else if (campTypeId == 1) {
            tvCampStatusHeading.setText("Normal Camp Status Dashboard");
        } else {
            tvCampStatusHeading.setText("D2D Camp Status Dashboard");
        }
        String dateTime = Utilities.dateTimeFormat.format(new Date());

        tvTodaysUpdatedTime.setText("*Data as of:- " + dateTime);

        String todaysDate = Utilities.ConvertDateFormat(Utilities.dfDate, mDay, mMonth + 1, mYear);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("dd");
        campDateCountList = new ArrayList<>();
//        try {
//            for (int i = 0; i < campList.size(); i++) {
//                CampCalendarModel.OutputBean dt = campList.get(i);
//
//                if (campDateCountList.size() <= 0) {
//                    CampCalenderDatesModel fdt = new CampCalenderDatesModel();
//                    fdt.setDate(dt.getCampDate());
//                    fdt.setCampCount(1);
//                    fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
//                    fdt.setMonth(dt.getCampDate());
//                    fdt.setYear(dt.getCampDate());
//
//                    campDateCountList.add(fdt);
//
//                } else {
//
//                    ArrayList<String> date = new ArrayList<>();
//                    for (int j = 0; j < campDateCountList.size(); j++) {
//                        date.add(campDateCountList.get(j).getDate());
//                    }
//
//                    if (date.contains(dt.getCampDate())) {
//                        for (int j = 0; j < campDateCountList.size(); j++) {
//                            CampCalenderDatesModel fdt1 = campDateCountList.get(j);
//
//                            if (fdt1.getDate().equalsIgnoreCase(dt.getCampDate())) {
//                                fdt1.setCampCount(fdt1.getCampCount() + 1);
//                            }
//                        }
//                    } else {
//                        CampCalenderDatesModel fdt = new CampCalenderDatesModel();
//
//                        fdt.setDate(dt.getCampDate());
//                        fdt.setCampCount(1);
//                        fdt.setDay(format2.format(format1.parse(dt.getCampDate())));
//                        fdt.setMonth(dt.getCampDate());
//                        fdt.setYear(dt.getCampDate());
//                        campDateCountList.add(fdt);
//                    }
//
//
//                }
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        setAdapter(campDateCountList);

        int total = 0, bydo = 0, pending = 0, hll = 0, completed = 0, nodal = 0, rejected = 0, upcoming = 0, monthsCount = 0,
                todaysCount = 0, zeroCountCamp = 0;

//        for (int i = 0; i < campList.size(); i++) {
//            total = total + 1;
//
//            if (campList.get(i).getREGISTERWORKERS().equalsIgnoreCase("0")) {
//                zeroCountCamp = zeroCountCamp + 1;
//            }
//            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
//                bydo = bydo + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
//                nodal = nodal + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
//                pending = pending + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
//                completed = completed + 1;
//            }
//
//            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
//                rejected = rejected + 1;
//            }
//
//            monthsCount = monthsCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//
//            if (campList.get(i).getCampDate().equals(todaysDate)) {
//                todaysCount = todaysCount + Integer.parseInt(campList.get(i).getREGISTERWORKERS());
//            }
//
//            try {
//                if (new SimpleDateFormat("yyyy-MM-dd").parse(campList.get(i).getCampDate()).after(Calendar.getInstance().getTime())) {
//                    upcoming = upcoming + 1;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

//        tv_total.setText("" + campList.get());
//        tv_do.setText("" + bydo);
//        tv_nodal.setText("" + nodal);
//        tv_pending.setText("" + pending);
//        tv_completed.setText("" + completed);
//        tv_rejected.setText("" + rejected);
//        tv_upcoming.setText("" + upcoming);
//        tv_months_beneficiary.setText("" + monthsCount);
//        tv_todays_beneficiary.setText("" + todaysCount);
//        tvZeroCampCount.setText("" + zeroCountCamp);
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

    public class GetOrganization extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubOrganizationModel.Output> organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

//                            if (pojoDetails.getOutput().size() > 0) {
//                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);
//
//                                organizationId = String.valueOf(output.getSubOrgId());
//
//
//                                if (organizationId!=null){
//                                    new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId,divisionId, empcode, DESGID);
//
//                                }
//
//
//                            }


                            showOrganizationListDialog(organizationList);
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

    public class GetOrganizationNew extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("UserId", params[0]));
            param.add(new ParamsPojo("DESGID", params[1]));

            res = WebServiceCall.APICall(ApplicationConstants.BindOrg, ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubOrganizationModel.Output> organizationList = new ArrayList<>();
                    SubOrganizationModel pojoDetails = new Gson().fromJson(result, SubOrganizationModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            if (pojoDetails.getOutput().size() > 0) {
                                SubOrganizationModel.Output output = pojoDetails.getOutput().get(0);

                                organizationId = String.valueOf(output.getSubOrgId());
                                edt_organization.setText(output.getSubOrgName());


                                if (clusterWiseFlag.equalsIgnoreCase("1")){

                                    if (organizationId != null) {
                                        new GetMonthlySurveySiteRequestForOSNew().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
                                        getProcessingCountData();
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
                                        getTotalCampAndBeneficiaryData(
                                                month,
                                                String.valueOf(selectedYearId),
                                                DISTLGDCODE,
                                                String.valueOf(campTypeId),
                                                organizationId,
                                                divisionId,
                                                empcode,
                                                DESGID

                                        );


                                    }

                                }else if (clusterWiseFlag.equalsIgnoreCase("2")){

                                    if (organizationId != null) {
                                        new GetMonthlySurveySiteRequestForOSNewClusterWise().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
//                                        getProcessingCountData();
//                                    new GetTotalcampAndTotalBeneficiarywithZeroCamp().execute(month, String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId), organizationId, divisionId, empcode, DESGID);
                                        getTotalCampAndBeneficiaryDataClusterWise(
                                                month,
                                                String.valueOf(selectedYearId),
                                                DISTLGDCODE,
                                                String.valueOf(campTypeId),
                                                organizationId,
                                                divisionId,
                                                empcode,
                                                DESGID

                                        );


                                    }


                                }


                            }


//                            showOrganizationListDialog(organizationList);
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

    public class GetDistrictListFor extends AsyncTask<String, Void, String> {

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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDistrict, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DistrictOrgModel.Output> organizationList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDistrictForListDialog(organizationList);
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
    public class GetDistrictListForClusterWise extends AsyncTask<String, Void, String> {

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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserID", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));
            param.add(new ParamsPojo("DIVID", params[3]));
            param.add(new ParamsPojo("DISTLGDCODE", params[4]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDistrict_Cluster, ApplicationConstants.webservice_d2d, param);
            return res;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<DistrictOrgModel.Output> organizationList = new ArrayList<>();
                    DistrictOrgModel pojoDetails = new Gson().fromJson(result, DistrictOrgModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        organizationList = pojoDetails.getOutput();
                        if (organizationList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDistrictForListDialog(organizationList);
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

    public class GetDivision extends AsyncTask<String, Void, String> {

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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDivision, ApplicationConstants.webservice_d2d, param);
            return res;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubDivisionModel.Output> divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDivisionListDialog(divisionList);
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
    public class GetDivisionCluster extends AsyncTask<String, Void, String> {

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

            param.add(new ParamsPojo("SubOrgId", params[0]));
            param.add(new ParamsPojo("UserId", params[1]));
            param.add(new ParamsPojo("DESGID", params[2]));

            res = WebServiceCall.APICall(ApplicationConstants.BindDivision_Cluster, ApplicationConstants.webservice_d2d, param);
            return res;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    List<SubDivisionModel.Output> divisionList = new ArrayList<>();
                    SubDivisionModel pojoDetails = new Gson().fromJson(result, SubDivisionModel.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        divisionList = pojoDetails.getOutput();
                        if (divisionList.size() > 0) {
//                            organizationList.add(0, new SubOrganizationModel("0", "All"));

                            showDivisionListDialog(divisionList);
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

    private void showOrganizationListDialog(final List<SubOrganizationModel.Output> organizationList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Organization");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getSubOrgName()));
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
                edt_organization.setText(organizationList.get(which).getSubOrgName());
                organizationId = String.valueOf(organizationList.get(which).getSubOrgId());
                divisionId = "0";
                DISTLGDCODE = "0";
                refreshCalendar();

                edt_division.setText("All");
                edt_selectdistrict.setText("All");
            }
        });
        builderSingle.show();
    }


    private void showDistrictForListDialog(final List<DistrictOrgModel.Output> organizationList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select District");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < organizationList.size(); i++) {
            arrayAdapter.add(String.valueOf(organizationList.get(i).getDistname()));
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
                edt_selectdistrict.setText(organizationList.get(which).getDistname());
                DISTLGDCODE = String.valueOf(organizationList.get(which).getDistlgdcode());
                refreshCalendar();
            }
        });
        builderSingle.show();
    }

    private void showDivisionListDialog(final List<SubDivisionModel.Output> divisionList) {
        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(context);
        builderSingle.setTitle("Select Division");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);

        for (int i = 0; i < divisionList.size(); i++) {
            arrayAdapter.add(String.valueOf(divisionList.get(i).getDivname()));
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
                edt_division.setText(divisionList.get(which).getDivname());
                divisionId = String.valueOf(divisionList.get(which).getDivid());

                edt_selectdistrict.setText("All");
                DISTLGDCODE = "0";
                refreshCalendar();
            }
        });
        builderSingle.show();
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
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
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


    private void getProcessingCountData() {
        final ProgressDialog progressDialog = new ProgressDialog(CampCalendar_Activity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        Call<ProcessingLabCountModel> call = apiService.getProcessingLabCount(month, String.valueOf(selectedYearId),divisionId, organizationId, DISTLGDCODE, empcode, DESGID);
        call.enqueue(new Callback<ProcessingLabCountModel>() {
            @Override
            public void onResponse(Call<ProcessingLabCountModel> call, Response<ProcessingLabCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        List<ProcessingLabCountModel.Output> invoiceList = response.body().getOutput();


                        if (invoiceList != null && !invoiceList.isEmpty()) {
                            ProcessingLabCountModel.Output output = invoiceList.get(0);
                            tv_Month_year.setText(output.getMonthYear());
                            tv_home_lab.setText(String.valueOf(output.getHomeLab()));
                            tv_hub_lab.setText(String.valueOf(output.getHubLab()));
                            tv_data_as_of.setText("*Data as of :- "+output.getLastUpdatedDate());
                        }


                    } else {

//                        Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);

                        tv_Month_year.setText("NA");
                        tv_home_lab.setText("0");
                        tv_hub_lab.setText("0");
                        tv_data_as_of.setText("NA");

                    }
                } else {
//                    Utilities.showAlertDialog(context, "Alert", "Details Not Found", false);
                    tv_Month_year.setText("NA");
                    tv_home_lab.setText("0");
                    tv_hub_lab.setText("0");
                    tv_data_as_of.setText("NA");
                }
            }

            @Override
            public void onFailure(Call<ProcessingLabCountModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }

    void getCampTypes() {
        pd.setMessage("Getting Camp Types..");
        pd.setCancelable(false);
        pd.show();
        apiInterface.getCampTypeAndCategory().enqueue(new Callback<CampTypeResponseModel>() {
            @Override
            public void onResponse(Call<CampTypeResponseModel> call, Response<CampTypeResponseModel> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        ArrayList<CampTypeResponseModel.Output> campTypeModelsList = new ArrayList<>();
                        CampTypeResponseModel campTypeResponseModel = new CampTypeResponseModel();
                        campTypeModelsList.add(campTypeResponseModel.new Output(0, "All Camps", 0, "All Camp"));
                        campTypeModelsList.addAll(response.body().getOutput());
                        showCampType(campTypeModelsList);
                    } else {
                        Utilities.showToastMessage(response.body().getMessage(), context, false);
                    }
                } else {
                    Utilities.showToastMessage(response.message(), context, false);
                }
            }

            @Override
            public void onFailure(Call<CampTypeResponseModel> call, Throwable t) {
                pd.dismiss();
                Utilities.showToastMessage(t.getMessage(), context, false);

            }
        });
    }
}
