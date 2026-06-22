package com.myhindlab.abkat.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampTargetAdapter;
import com.myhindlab.abkat.adapters.CampTargetDivisionWiseAdapter;
import com.myhindlab.abkat.models.CampTargetListModel;
import com.myhindlab.abkat.models.CampTargetModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DivisionWiseTargetListModel;
import com.myhindlab.abkat.models.TargetDivisionWiseListModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SetPostCampTarget_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private MaterialEditText edt_select_district, edt_year_month, edt_from_date, edt_to_date, edt_target, edtSelCampType;
    private TextView tv_selectyear, tv_selectmonth, tvTotalMonthlyTarget, tvTotalMonthlyTargetAchieved, tvTotalDailyTarget, tvTotalDailyTargetAchieved, targetRemain;
    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1, selectedMonthId, selectedYearId;
    private Button btn_submit, btnExport;
    private RecyclerView rvTargetList;
    File exportFolder;
    private RadioGroup rgSwitch;
    private RadioButton rbDistrict, rbDivision;
    private LinearLayoutCompat llHeader;

    private String userID, districtId, monthName;
    private Calendar calMonth;
    private int campTypeId = 0, totalMonthlyTarget = 0, totalMonthlyTargetAchieved = 0, totalDailyTarget = 0, totalDailyTargetAchieved = 0;

    private ArrayList<CampTargetListModel.Output> targetList;
    private ArrayList<TargetDivisionWiseListModel.Output> targetListDivisionWise;
    private ArrayList<DivisionWiseTargetListModel> divisionWiseTargetListModels;
    private ArrayList<DivisionWiseTargetListModel.TargetListItem> targetListItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpost_camptarget);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = SetPostCampTarget_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

        edt_select_district = findViewById(R.id.edt_select_district);
        edt_from_date = findViewById(R.id.edt_from_date);
        edt_to_date = findViewById(R.id.edt_to_date);
        edt_year_month = findViewById(R.id.edt_year_month);
        edt_target = findViewById(R.id.edt_target);
        edtSelCampType = findViewById(R.id.edtSelCampType);
        tvTotalMonthlyTarget = findViewById(R.id.tvTotalMonthlyTarget);
        tvTotalMonthlyTargetAchieved = findViewById(R.id.tvTotalMonthlyTargetAchieved);
        tvTotalDailyTarget = findViewById(R.id.tvTotalDailyTarget);
        tvTotalDailyTargetAchieved = findViewById(R.id.tvTotalDailyTargetAchieved);
        rvTargetList = findViewById(R.id.rvTargetList);
        btn_submit = findViewById(R.id.btn_submit);
        btnExport = findViewById(R.id.btnExport);
        llHeader = findViewById(R.id.llHeader);

        rgSwitch = findViewById(R.id.rgSwitch);
        rbDistrict = findViewById(R.id.rbDistrict);
        rbDivision = findViewById(R.id.rbDivision);
        targetRemain = findViewById(R.id.targetRemain);


        rvTargetList.setHasFixedSize(true);
        rvTargetList.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        calMonth = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat.format(c.getTimeInMillis());
        edt_from_date.setText(simpleDateFormat.format(c.getTimeInMillis()));
        edtSelCampType.setText("Regular Camp");
        try {
            targetRemain.setText("Daily Target Achieved\n" + Utilities.dfDate5.format(new SimpleDateFormat("yyyy/MM/dd").parse(edt_from_date.getText().toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetCampTargetList().execute(edt_from_date.getText().toString(), String.valueOf(campTypeId));
//        new GetCampTargetListDivisionWise().execute(edt_from_date.getText().toString(), "0", "0", String.valueOf(campTypeId));


        monthName = new DateFormatSymbols().getMonths()[mMonth];
        selectedMonthId = mMonth + 1;
        selectedYearId = mYear;

    }

    private void setEventHandler() {
        rgSwitch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == rbDistrict.getId()) {
                    llHeader.setVisibility(View.VISIBLE);
                    rvTargetList.setVisibility(View.GONE);

                    new GetCampTargetList().execute(edt_from_date.getText().toString(), String.valueOf(campTypeId));
                } else {
                    llHeader.setVisibility(View.GONE);
                    rvTargetList.setVisibility(View.GONE);
                    new GetCampTargetListDivisionWise().execute(edt_from_date.getText().toString(), "0", "0", String.valueOf(campTypeId));
                }
            }
        });
        edt_select_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetDistrictList().execute();
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        });
        edtSelCampType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CampTypeModel> campTypeModelArrayList = new ArrayList<>();
                campTypeModelArrayList.add(new CampTypeModel("Regular Camp", 1));
                campTypeModelArrayList.add(new CampTypeModel("D2D Camp", 3));

                showCampType(campTypeModelArrayList);
            }
        });

        edt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_from_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));
                                try {
                                    targetRemain.setText("Daily Target Achieved\n" + Utilities.dfDate5.format(new SimpleDateFormat("yyyy/MM/dd").parse(edt_from_date.getText().toString())));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                Calendar c = Calendar.getInstance();
                                mYear1 = c.get(Calendar.YEAR);
                                mMonth1 = c.get(Calendar.MONTH);
                                mDay1 = c.get(Calendar.DAY_OF_MONTH);
//                                edt_to_date.setText("");
                                if (rbDistrict.isChecked()) {
                                    new GetCampTargetList().execute(edt_from_date.getText().toString(), String.valueOf(campTypeId));
                                } else {
                                    new GetCampTargetListDivisionWise().execute(edt_from_date.getText().toString(), "0", "0", String.valueOf(campTypeId));
                                }
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

        edt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_from_date.getText().toString().equals("")) {
                    Utilities.showToastMessage("Please Select From Date", context, false);
                    return;
                }

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_to_date.setText(Utilities.ConvertDateFormat(Utilities.dfDate4, dayOfMonth, monthOfYear + 1, year));

                                mYear1 = year;
                                mMonth1 = monthOfYear;
                                mDay1 = dayOfMonth;
                            }
                        }, mYear1, mMonth1, mDay1);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                try {
                    dpd.getDatePicker().setCalendarViewShown(false);
                    dpd.getDatePicker().setMinDate(c.getTimeInMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd.show();
            }
        });

        edt_year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_select_district.getText().toString().trim().isEmpty()) {
                    edt_select_district.setError("Please select district");
                    return;
                }

                selectYearMonthAlert();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                submitData();
//                new GetCampTargetList().execute(edt_from_date.getText().toString(), String.valueOf(campTypeId));
                new GetCampTargetListDivisionWise().execute(edt_from_date.getText().toString(), "0", "0", String.valueOf(campTypeId));

            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void openExcel(String filepath) {
        File file = new File(filepath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void selectYearMonthAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.prompt_yearmonthfilter, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
                        edt_year_month.setText(android.text.format.DateFormat.format("yyyy MMMM", calMonth));

                        if (Utilities.isNetworkAvailable(context)) {
                            new GetCampTargetDetails().execute(
                                    districtId,
                                    String.valueOf(selectedMonthId + 1),
                                    String.valueOf(selectedYearId));
                        } else {
                            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        }

                    } else {
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
        int startYear = year1.get(Calendar.YEAR);

        yearList.add(String.valueOf(startYear));

//        for (int i = startYear; i < endYear; i++) {
//            startYear = startYear + 1;
//            yearList.add(String.valueOf(startYear));
//        }

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

            if (i >= mMonth) {
                String month = months[i];
                System.out.println("month = " + month);
                monthList.add(months[i]);
            }
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

    private void submitData() {
        if (edt_select_district.getText().toString().trim().isEmpty()) {
            edt_select_district.setError("Please select district");
            return;
        }

        if (edt_year_month.getText().toString().trim().isEmpty()) {
            edt_year_month.setError("Please select year and month");
            return;
        }

//        if (edt_from_date.getText().toString().trim().isEmpty()) {
//            edt_from_date.setError("Please from date");
//            return;
//        }
//
//        if (edt_to_date.getText().toString().trim().isEmpty()) {
//            edt_to_date.setError("Please to date district");
//            return;
//        }

        if (edt_target.getText().toString().trim().isEmpty()) {
            edt_target.setError("Please enter target");
            return;
        }

        if (Utilities.isNetworkAvailable(context)) {
            new InsertCampTargetDetails().execute(
                    districtId,
                    edt_target.getText().toString().trim(),
                    String.valueOf(selectedMonthId + 1),
                    String.valueOf(selectedYearId),
                    userID);
        } else {
            Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
        }
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

    public class GetCampTargetList extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("FDate", params[0]));
            param.add(new ParamsPojo("CampType", params[1]));
            res = WebServiceCall.APICall(ApplicationConstants.BeneficairyTargetTracking, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("BeneficairyTarget", "onPostExecute" + result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    CampTargetListModel campTargetModel = new Gson().fromJson(result, CampTargetListModel.class);
                    type = campTargetModel.getStatus();
                    message = campTargetModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        ArrayList<CampTargetListModel.Output> arrayList = new ArrayList<CampTargetListModel.Output>();
                        arrayList.addAll(campTargetModel.getOutput());
                        targetList = new ArrayList<>();
                        targetList.addAll(campTargetModel.getOutput());

                        totalDailyTarget = 0;
                        totalMonthlyTarget = 0;
                        totalDailyTargetAchieved = 0;
                        totalMonthlyTargetAchieved = 0;

                        for (CampTargetListModel.Output output : arrayList
                        ) {
                            totalDailyTarget += output.getDailyTarget();
                            totalMonthlyTargetAchieved += output.getTotalMonthlyTargetAchived();
                            totalDailyTargetAchieved += output.getCurrentDateTargetAchived();
                            totalMonthlyTarget += output.getMonthlyTarget();
                        }
                        tvTotalDailyTarget.setText(String.valueOf(totalDailyTarget));
                        tvTotalMonthlyTargetAchieved.setText(String.valueOf(totalMonthlyTargetAchieved));
                        tvTotalDailyTargetAchieved.setText(String.valueOf(totalDailyTargetAchieved));
                        tvTotalMonthlyTarget.setText(String.valueOf(totalMonthlyTarget));

                        rvTargetList.setVisibility(View.VISIBLE);
                        rvTargetList.setAdapter(new CampTargetAdapter(context, arrayList));

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

    public class GetCampTargetListDivisionWise extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Fromdate", params[0]));
            param.add(new ParamsPojo("Type", params[1]));
            param.add(new ParamsPojo("DISTLGDCODE", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));
            res = WebServiceCall.APICall(ApplicationConstants.GetTargetDivision, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("GetCampTargetListDivisionWise", "onPostExecute" + result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    TargetDivisionWiseListModel campTargetModel = new Gson().fromJson(result, TargetDivisionWiseListModel.class);
                    type = campTargetModel.getStatus();
                    message = campTargetModel.getMessage();
                    if (type.equalsIgnoreCase("success")) {

                        targetListDivisionWise = new ArrayList<>();
                        targetListDivisionWise.addAll(campTargetModel.getOutput());
                        divisionWiseTargetListModels = new ArrayList<>();

                        ArrayList<String> divArray = new ArrayList<>();
                        String divName = targetListDivisionWise.get(0).getDivname();

                        for (int i = 0; i < targetListDivisionWise.size(); i++) {
                            if (!divArray.contains(divName)) {
                                divArray.add(targetListDivisionWise.get(i).getDivname());
                            } else {
                                divName = targetListDivisionWise.get(i).getDivname();
                            }
                        }
                        Log.d("TAG", "DivArr: " + new Gson().toJson(divArray));
                        for (int j = 0; j < divArray.size(); j++) {

                            targetListItemArrayList = new ArrayList<>();
                            for (int k = 0; k < targetListDivisionWise.size(); k++) {

                                if (targetListDivisionWise.get(k).getDivname().equalsIgnoreCase(divArray.get(j))) {
                                    DivisionWiseTargetListModel.TargetListItem targetListItem = new DivisionWiseTargetListModel.TargetListItem(targetListDivisionWise.get(k).getDivname(), targetListDivisionWise.get(k).getDistlgdcode(), targetListDivisionWise.get(k).getDistrictName(), targetListDivisionWise.get(k).getTotalBeneficiary(), targetListDivisionWise.get(k).getMonthlyTarget(), targetListDivisionWise.get(k).getThreeMonthsTarget(), targetListDivisionWise.get(k).getDailyTarget(), targetListDivisionWise.get(k).getTotalMonthlyTargetAchived(), targetListDivisionWise.get(k).getCurrentDateTargetAchived(), targetListDivisionWise.get(k).getPercentage());
                                    targetListItemArrayList.add(targetListItem);
                                }
                            }
                            DivisionWiseTargetListModel divisionWiseTargetListModel = new DivisionWiseTargetListModel(divArray.get(j), targetListItemArrayList);
                            divisionWiseTargetListModels.add(divisionWiseTargetListModel);
                        }

                        Log.d("TAG", "AllDivArr: " + new Gson().toJson(divisionWiseTargetListModels));
                        rvTargetList.setVisibility(View.VISIBLE);
                        rvTargetList.setAdapter(new CampTargetDivisionWiseAdapter(context, divisionWiseTargetListModels, edt_from_date.getText().toString()));
                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);

                    }
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    public class GetCampTargetDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("MonthID", params[1]));
            param.add(new ParamsPojo("Year", params[2]));
            res = WebServiceCall.APICall(ApplicationConstants.GetCampTargetDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "";
            try {
                if (!result.equals("")) {
                    List<CampTargetModel.OutputBean> targetList = new ArrayList<>();
                    CampTargetModel pojoDetails = new Gson().fromJson(result, CampTargetModel.class);
                    type = pojoDetails.getStatus();
                    if (type.equalsIgnoreCase("success")) {
                        targetList = pojoDetails.getOutput();
                        if (targetList.size() > 0) {

                            CampTargetModel.OutputBean outputBean = targetList.get(0);

                            if (outputBean.getTargetCount() != 0) {
                                edt_target.setText(outputBean.getTargetCount() + "");
                                btn_submit.setText("Update");
                            } else {
                                edt_target.setText("");
                                btn_submit.setText("Save");
                            }

                        } else {
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
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
                edt_select_district.setText(districtList.get(which).getDISTNAME());
                districtId = districtList.get(which).getDISTLGDCODE();
                edt_year_month.setText("");
            }
        });
        builderSingle.show();
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

                new GetCampTargetList().execute(edt_from_date.getText().toString(), String.valueOf(campTypeId));


            }
        });
        builderSingle.show();
    }

    public class InsertCampTargetDetails extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("DistrictId", params[0]));
            param.add(new ParamsPojo("TargetCount", params[1]));
            param.add(new ParamsPojo("MonthId", params[2]));
            param.add(new ParamsPojo("YearId", params[3]));
            param.add(new ParamsPojo("CreatedBy", params[4]));
            res = WebServiceCall.APICall(ApplicationConstants.InsertCampTargetDetails, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();

                if (result != null && result.length() > 0 && !result.equalsIgnoreCase("[]")) {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("Success")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setMessage(message);
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();

                    } else {
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else
                    Utilities.showAlertDialog(context, "Please try again", "Server not responding.", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Target Tracking");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.export_menu, menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuExport:
                if (rbDistrict.isChecked()) {
                    exportToExcel();
                } else {
//                    Utilities.showToastMessage("Export Only Work in District Wise List, For Now..", context, false);
                    Utilities.showAlertDialog(context, "Coming Soon...", "Export Only For District Wise List", false);
                }
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    void exportToExcel() {
        if (targetList != null && targetList.size() > 0) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                exportFolder = getExternalCacheDir();
            } else {

                exportFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Export/");
                if (!exportFolder.exists())
                    exportFolder.mkdirs();
            }
            String csvFile = "CampTargetDetails.xls";

            Uri fileUri = Uri.fromFile(exportFolder);

            try {
                File file = new File(exportFolder, csvFile);

                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook;
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("CampTargetDetails", 0);

                // Create cell font and format
                WritableFont cellFont = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD);
                cellFont.setColour(Colour.BLACK);

                // column and row
                sheet.addCell(new Label(0, 0, " District "));
                sheet.addCell(new Label(1, 0, " Monthly Target "));
                sheet.addCell(new Label(2, 0, " Monthly Target Achieved "));
                sheet.addCell(new Label(3, 0, " Daily Target "));
                sheet.addCell(new Label(4, 0, " Daily Target Achieved "));


                for (int i = 0; i < targetList.size(); i++) {
                    int pos = i + 1;

                    CellView cell = sheet.getColumnView(i);
                    cell.setAutosize(true);
                    sheet.setColumnView(i, cell);

                    CellView cellView = sheet.getColumnView(pos);
                    cellView.setAutosize(true);
                    sheet.setColumnView(pos, cellView);

                    sheet.addCell(new Label(0, pos, targetList.get(i).getDistrictName()));
                    sheet.addCell(new Label(1, pos, String.valueOf(targetList.get(i).getMonthlyTarget())));
                    sheet.addCell(new Label(2, pos, String.valueOf(targetList.get(i).getTotalMonthlyTargetAchived())));
                    sheet.addCell(new Label(3, pos, String.valueOf(targetList.get(i).getDailyTarget())));
                    sheet.addCell(new Label(4, pos, String.valueOf(targetList.get(i).getCurrentDateTargetAchived())));

                }
                workbook.write();
                workbook.close();
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(file.getAbsolutePath()), "application/pdf");
                    intent = Intent.createChooser(intent, "Open File");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
//                        openExcel(fileUri.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
