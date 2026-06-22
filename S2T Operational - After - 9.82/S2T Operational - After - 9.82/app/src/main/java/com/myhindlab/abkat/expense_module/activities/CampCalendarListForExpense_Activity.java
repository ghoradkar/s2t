package com.myhindlab.abkat.expense_module.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.expense_module.adapters.CampCalendarListForExpenseAdapter;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

public class CampCalendarListForExpense_Activity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv_campList;
    private EditText edtSelCampType;

    private String TYPE, date, CAMPTYPR;
    private ArrayList<CampCalendarModel.OutputBean> campList;
    private ArrayList<CampCalendarModel.OutputBean> totalTodaysCamp;

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat format2 = new SimpleDateFormat("dd");
    private int campTypeId = 0;
    private int mYear, mMonth, mDay;
    private int selectedMonthId, selectedYearId;
    private String DESGID, DISTLGDCODE, district, TALLGDCODE, taluka;
    private TextView tvRegWorker, tvCampDate;
    private boolean isReversed = false;
    CampCalendarListForExpenseAdapter campCalendarListAdapter;
    private Button btnExport;
    private ProgressBar exportProgress;
    File exportFolder;

    TextView tvTotalRegWorker;

    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campcalendar_list_for_expense);

        init();
        getSessionData();
        setDefaults();
        setUpToolBar();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                new GetCampDetailsTodayCountForOS1().execute(String.valueOf(intent.getStringExtra("campTypeId")));
            }
        }
    };

    private void init() {
        context = CampCalendarListForExpense_Activity.this;
        rv_campList = findViewById(R.id.rv_campList);
        rv_campList.setLayoutManager(new LinearLayoutManager(context));
        edtSelCampType = findViewById(R.id.edtSelCampType);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter("refreshCampList");
        localBroadcastManager.registerReceiver(broadcastReceiver, filter);


        tvTotalRegWorker = findViewById(R.id.tvTotalRegWorker);
        tvRegWorker = findViewById(R.id.tvRegWorker);
        tvCampDate = findViewById(R.id.tvCampDate);
        btnExport = findViewById(R.id.btnExport);
        exportProgress = findViewById(R.id.exportProgress);


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(campList, new CampCountComparatorReverse().reversed());
                        }
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                    }
                    rv_campList.scrollToPosition(0);
                    campCalendarListAdapter.notifyDataSetChanged();
                }


            }
        });
        tvRegWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campCalendarListAdapter != null) {

                    if (isReversed) {
                        isReversed = false;
                        Collections.sort(campList, new CampCountComparatorDate());
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                    } else {
                        isReversed = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(campList, new CampCountComparatorDate().reversed());
                        }
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                    }
                    rv_campList.scrollToPosition(0);
                    campCalendarListAdapter.notifyDataSetChanged();
                }


            }
        });


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campList != null && campList.size() > 0) {
                    exportProgress.setVisibility(View.VISIBLE);
                    btnExport.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        exportFolder = getExternalCacheDir();
                    } else {

                        exportFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Export/");
                        if (!exportFolder.exists())
                            exportFolder.mkdirs();
                    }
                    String csvFile = "CampList.xls";

                    Uri fileUri = Uri.fromFile(exportFolder);

                    try {
                        File file = new File(exportFolder, csvFile);

                        WorkbookSettings wbSettings = new WorkbookSettings();
                        wbSettings.setLocale(new Locale("en", "EN"));
                        WritableWorkbook workbook;
                        workbook = Workbook.createWorkbook(file, wbSettings);
                        //Excel sheet name. 0 represents first sheet
                        WritableSheet sheet = workbook.createSheet("CampList", 0);

                        // Create cell font and format
                        WritableFont cellFont = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD);
                        cellFont.setColour(Colour.BLACK);


                        // column and row
                        sheet.addCell(new Label(0, 0, " Camp Id "));
                        sheet.addCell(new Label(1, 0, " District "));
                        sheet.addCell(new Label(2, 0, " Date "));
                        sheet.addCell(new Label(3, 0, " Registered Workers "));
                        sheet.addCell(new Label(4, 0, " Camp Status "));


                        for (int i = 0; i < campList.size(); i++) {
                            int pos = i + 1;

                            CellView cell = sheet.getColumnView(i);
                            cell.setAutosize(true);
                            sheet.setColumnView(i, cell);

                            CellView cellView = sheet.getColumnView(pos);
                            cellView.setAutosize(true);
                            sheet.setColumnView(pos, cellView);

                            sheet.addCell(new Label(0, pos, campList.get(i).getCampId()));
                            sheet.addCell(new Label(1, pos, String.valueOf(campList.get(i).getDISTNAME())));
                            sheet.addCell(new Label(2, pos, String.valueOf(campList.get(i).getCampDate())));
                            sheet.addCell(new Label(3, pos, String.valueOf(campList.get(i).getREGISTERWORKERS())));
                            sheet.addCell(new Label(4, pos, String.valueOf(campList.get(i).getCampStatus())));

                        }
                        workbook.write();
                        workbook.close();
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            exportProgress.setVisibility(View.GONE);
                            btnExport.setVisibility(View.VISIBLE);

                            startActivity(intent);
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(file.getAbsolutePath()), "application/pdf");
                            intent = Intent.createChooser(intent, "Open File");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            exportProgress.setVisibility(View.GONE);
                            btnExport.setVisibility(View.VISIBLE);

                            startActivity(intent);
                        }
//                        openExcel(fileUri.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void getSessionData() {

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

                if (campTypeId != 0) {
                    new GetCampDetailsTodayCountForOS().execute(String.valueOf(campTypeId));
                } else {

                    date = getIntent().getStringExtra("date");
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat format2 = new SimpleDateFormat("dd");
                    ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                    for (int i = 0; i < totalTodaysCamp.size(); i++) {
                        try {
                            if (Integer.parseInt(format2.format(format1.parse(totalTodaysCamp.get(i).getCampDate()))) == Integer.parseInt(date)) {
                                filteredCampList.add(totalTodaysCamp.get(i));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (filteredCampList.size() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setIcon(R.drawable.icon_warning);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("Camps are not available for selected date.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    } else {

                        Collections.sort(campList, new CampCountComparatorReverse());
                        campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, campList, campTypeId);
                        rv_campList.setAdapter(campCalendarListAdapter);
//                        rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));
                    }
                }


            }
        });
        builderSingle.show();
    }

    private class GetCampDetailsTodayCount extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequest, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();


                    }
                    new GetCampDetailsTodayCountForOS().execute(String.valueOf(campTypeId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class GetCampDetailsTodayCountForOS extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));
            param.add(new ParamsPojo("CampType", params[0]));
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("FromDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
//            param.add(new ParamsPojo("ToDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOSV1New, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        date = getIntent().getStringExtra("date");
                        ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                        Calendar c1, c2;
                        c1 = Calendar.getInstance();
                        c2 = Calendar.getInstance();

                        for (CampCalendarModel.OutputBean o :
                                campList) {
                            c1.setTime(Utilities.dfDate.parse(o.getCampDate()));
                            if (c1.before(c2)) {
                                filteredCampList.add(o);
                            }
                        }


                        if (filteredCampList.size() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("Camps are not available for selected date.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {

                            Collections.sort(campList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    private class GetCampDetailsTodayCountForOS1 extends AsyncTask<String, Void, String> {

//        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("Month", String.valueOf(mMonth + 1)));
            param.add(new ParamsPojo("Year", String.valueOf(mYear)));
            param.add(new ParamsPojo("DistCode", "0"));
            param.add(new ParamsPojo("CampType", params[0]));
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//            param.add(new ParamsPojo("FromDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
//            param.add(new ParamsPojo("ToDate", Utilities.ConvertDateFormat(Utilities.dfDate4, mDay, mMonth + 1, mYear)));
            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOSV1New, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();

                    if (status.equalsIgnoreCase("success")) {
                        campList = pojoDetails.getOutput();
                        date = getIntent().getStringExtra("date");
                        ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                        Calendar c1, c2;
                        c1 = Calendar.getInstance();
                        c2 = Calendar.getInstance();

                        for (CampCalendarModel.OutputBean o :
                                campList) {
                            c1.setTime(Utilities.dfDate.parse(o.getCampDate()));
                            if (c1.before(c2)) {
                                filteredCampList.add(o);
                            }
                        }


                        if (filteredCampList.size() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("Camps are not available for selected date.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {

                            Collections.sort(campList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class GetMonthlySurveySiteRequestForOS extends AsyncTask<String, Void, String> {

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
            param.add(new ParamsPojo("Month", params[0]));
            param.add(new ParamsPojo("Year", params[1]));
            param.add(new ParamsPojo("DistCode", params[2]));
            param.add(new ParamsPojo("CampType", params[3]));

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOSV1New, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            Log.d("camp", "onPostExecute: " + result);
            try {
                if (!result.equals("")) {

                    campList = new ArrayList<>();
                    CampCalendarModel pojoDetails = new Gson().fromJson(result, CampCalendarModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();


                    if (status.equalsIgnoreCase("success")) {

                        campList = pojoDetails.getOutput();
                        if (campList.size() == 0) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
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

                            if (TYPE.equals("1")) {


                                int total = 0;
                                int ExpTotal = 0;

                                date = getIntent().getStringExtra("date");
                                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                                Calendar c1, c2;
                                c1 = Calendar.getInstance();
                                c2 = Calendar.getInstance();

                                for (CampCalendarModel.OutputBean o :
                                        campList) {
                                    c1.setTime(Utilities.dfDate.parse(o.getCampDate()));
                                    if (c1.before(c2)) {
                                        filteredCampList.add(o);
                                        total = total + Integer.valueOf(o.getREGISTERWORKERS());
                                        ExpTotal = ExpTotal + Integer.valueOf(o.getExpectedbeneficiarycount());

                                    }
                                }

                                tvTotalRegWorker.setText(total + "/" + ExpTotal);

                                if (filteredCampList.size() == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                                    builder.setIcon(R.drawable.icon_warning);
                                    builder.setTitle("Alert");
                                    builder.setCancelable(false);
                                    builder.setMessage("Camps are not available for selected date.");
                                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.show();
                                } else {
                                    Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                    campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                                    rv_campList.setAdapter(campCalendarListAdapter);

                                    tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (campCalendarListAdapter != null) {

                                                if (isReversed) {
                                                    isReversed = false;
                                                    Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                                    tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                                } else {
                                                    isReversed = true;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                        Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                                    }
                                                    tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                                }
                                                campCalendarListAdapter.notifyDataSetChanged();
                                            }


                                        }
                                    });

                                }
                            } else {

                                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                                Calendar c1, c2;
                                c1 = Calendar.getInstance();
                                c2 = Calendar.getInstance();

                                int total = 0;
                                int ExpTotal = 0;

                                for (CampCalendarModel.OutputBean o :
                                        campList) {
                                    c1.setTime(Utilities.dfDate.parse(o.getCampDate()));
                                    if (c1.before(c2)) {
                                        filteredCampList.add(o);
                                        total = total + Integer.valueOf(o.getREGISTERWORKERS());
                                        ExpTotal = ExpTotal + Integer.valueOf(o.getExpectedbeneficiarycount());

                                    }
                                }

                                tvTotalRegWorker.setText(total + "/" + ExpTotal);
                                Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                                rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, campList));

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
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    Collections.sort(campList, new CampCountComparatorReverse().reversed());
                                                }
                                                tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                            }
                                            campCalendarListAdapter.notifyDataSetChanged();
                                        }


                                    }
                                });

                            }

                        }
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

                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_emptyresponse, context, false);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Alert", e.getMessage(), false);

            }
        }
    }


    private void setDefaults() {
        totalTodaysCamp = new ArrayList<>();

        if (getIntent() != null) {
            TYPE = getIntent().getStringExtra("TYPE");
            selectedMonthId = getIntent().getIntExtra("selectedMonth", 0);
            selectedYearId = getIntent().getIntExtra("selectedYear", 0);
            DISTLGDCODE = getIntent().getStringExtra("selectedDist");
            campTypeId = getIntent().getIntExtra("campType", 0);

            try {
                campList = (ArrayList<CampCalendarModel.OutputBean>) getIntent().getSerializableExtra("campList");
                totalTodaysCamp = (ArrayList<CampCalendarModel.OutputBean>) getIntent().getSerializableExtra("campList");
            } catch (Exception e) {
                campList = new ArrayList<>();
                totalTodaysCamp = new ArrayList<>();
            }


            if (TYPE.equals("1")) {

                if (campList != null && campList.size() > 0) {


                    date = getIntent().getStringExtra("date");
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat format2 = new SimpleDateFormat("dd");
                    ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();

                    int total = 0;
                    int ExpTotal = 0;
                    for (int i = 0; i < campList.size(); i++) {
                        try {
                            if (Integer.parseInt(format2.format(format1.parse(campList.get(i).getCampDate()))) == Integer.parseInt(date)) {
                                filteredCampList.add(campList.get(i));
                                total = total + Integer.valueOf(campList.get(i).getREGISTERWORKERS());
                                ExpTotal = ExpTotal + Integer.valueOf(campList.get(i).getExpectedbeneficiarycount());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    tvTotalRegWorker.setText(total + "/" + ExpTotal);

                    if (filteredCampList.size() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setIcon(R.drawable.icon_warning);
                        builder.setTitle("Alert");
                        builder.setCancelable(false);
                        builder.setMessage("Camps are not available for selected date.");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        Collections.sort(filteredCampList, new CampCountComparatorReverse());
                        campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                        rv_campList.setAdapter(campCalendarListAdapter);
//                    rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                        tvRegWorker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (campCalendarListAdapter != null) {

                                    if (isReversed) {
                                        isReversed = false;
                                        Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                    } else {
                                        isReversed = true;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                        }
                                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                    }
                                    campCalendarListAdapter.notifyDataSetChanged();
                                }


                            }
                        });

                    }
                } else {
                    if (Utilities.isNetworkAvailable(context)) {
                        new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    } else {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                    }
                }
            } else if (TYPE.equals("2")) {
                ArrayList<CampCalendarModel.OutputBean> filteredCampList = new ArrayList<>();
                CAMPTYPR = getIntent().getStringExtra("CAMPTYPR");

                switch (CAMPTYPR) {
                    case "TOTAL":
                        if (campList != null && campList.size() > 0) {

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
                                    filteredCampList.add(o);
                                }
                            }
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);

                        } else {
                            if (Utilities.isNetworkAvailable(context)) {
                                new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                            } else {
                                Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                            }
                        }

                        break;

                    case "BYDO":
                        for (int i = 0; i < campList.size(); i++) {
                            if (campList.get(i).getStatus().equalsIgnoreCase("D")) {
                                filteredCampList.add(campList.get(i));
                            }
                        }


                        if (filteredCampList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no camp requests pending for DO approval.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                    case "NODAL":
                        for (int i = 0; i < campList.size(); i++) {
                            if (campList.get(i).getStatus().equalsIgnoreCase("E")) {
                                filteredCampList.add(campList.get(i));
                            }
                        }

                        if (filteredCampList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no camp requests pending for Nominated Officer approval.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                    case "PENDING":
                        for (int i = 0; i < campList.size(); i++) {
                            if (campList.get(i).getStatus().equalsIgnoreCase("P")) {
                                filteredCampList.add(campList.get(i));
                            }
                        }
                        if (filteredCampList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no camp approved by Nominated Officer.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                    case "COMPLETED":
                        for (int i = 0; i < campList.size(); i++) {
                            if (campList.get(i).getStatus().equalsIgnoreCase("W")) {
                                filteredCampList.add(campList.get(i));
                            }
                        }
                        if (filteredCampList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no completed camps.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                    case "REJECTED":
                        for (int i = 0; i < campList.size(); i++) {
                            if (campList.get(i).getStatus().equalsIgnoreCase("R")) {
                                filteredCampList.add(campList.get(i));
                            }
                        }
                        if (filteredCampList.size() == 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no rejected camps.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                    case "UPCOMING":
                        for (int i = 0; i < campList.size(); i++) {
                            try {
                                if (Integer.parseInt(format2.format(format1.parse(campList.get(i).getCampDate()))) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                    filteredCampList.add(campList.get(i));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (filteredCampList.size() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                            builder.setIcon(R.drawable.icon_warning);
                            builder.setTitle("Alert");
                            builder.setCancelable(false);
                            builder.setMessage("There are no upcoming camps.");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                            campCalendarListAdapter = new CampCalendarListForExpenseAdapter(context, filteredCampList, campTypeId);
                            rv_campList.setAdapter(campCalendarListAdapter);
//                            rv_campList.setAdapter(new CampCalendarListForExpenseAdapter(context, filteredCampList));

                            tvRegWorker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (campCalendarListAdapter != null) {

                                        if (isReversed) {
                                            isReversed = false;
                                            Collections.sort(filteredCampList, new CampCountComparatorReverse());
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);

                                        } else {
                                            isReversed = true;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                Collections.sort(filteredCampList, new CampCountComparatorReverse().reversed());
                                            }
                                            tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                                        }
                                        campCalendarListAdapter.notifyDataSetChanged();
                                    }


                                }
                            });

                        }
                        break;

                }

            }
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp List");
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//
//        getMenuInflater().inflate(R.menu.export_menu, menu);
//
//        return super.onCreateOptionsMenu(menu);
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.menuExport:
//                exportToExcel();
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//
//    }

    void exportToExcel() {
        if (campList != null && campList.size() > 0) {
            exportProgress.setVisibility(View.VISIBLE);
            exportProgress.setIndeterminate(true);
//            btnExport.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                exportFolder = getExternalCacheDir();
            } else {

                exportFolder = new File(Environment.getExternalStorageDirectory() + "/Health Checkup/" + "/Export/");
                if (!exportFolder.exists())
                    exportFolder.mkdirs();
            }
            String csvFile = "CampList.xls";

            Uri fileUri = Uri.fromFile(exportFolder);

            try {
                File file = new File(exportFolder, csvFile);

                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook;
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("CampList", 0);

                // Create cell font and format
                WritableFont cellFont = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD);
                cellFont.setColour(Colour.BLACK);


                // column and row
                sheet.addCell(new Label(0, 0, " Camp Id "));
                sheet.addCell(new Label(1, 0, " District "));
                sheet.addCell(new Label(2, 0, " Date "));
                sheet.addCell(new Label(3, 0, " Registered Workers "));
                sheet.addCell(new Label(4, 0, " Camp Status "));


                for (int i = 0; i < campList.size(); i++) {
                    int pos = i + 1;

                    CellView cell = sheet.getColumnView(i);
                    cell.setAutosize(true);
                    sheet.setColumnView(i, cell);

                    CellView cellView = sheet.getColumnView(pos);
                    cellView.setAutosize(true);
                    sheet.setColumnView(pos, cellView);

                    sheet.addCell(new Label(0, pos, campList.get(i).getCampId()));
                    sheet.addCell(new Label(1, pos, String.valueOf(campList.get(i).getDISTNAME())));
                    sheet.addCell(new Label(2, pos, String.valueOf(campList.get(i).getCampDate())));
                    sheet.addCell(new Label(3, pos, String.valueOf(campList.get(i).getREGISTERWORKERS())));
                    sheet.addCell(new Label(4, pos, String.valueOf(campList.get(i).getCampStatus())));

                }
                workbook.write();
                workbook.close();
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    exportProgress.setVisibility(View.GONE);
//                    btnExport.setVisibility(View.VISIBLE);

                    startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(file.getAbsolutePath()), "application/pdf");
                    intent = Intent.createChooser(intent, "Open File");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    exportProgress.setVisibility(View.GONE);
//                    btnExport.setVisibility(View.VISIBLE);

                    startActivity(intent);
                }
//                        openExcel(fileUri.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
