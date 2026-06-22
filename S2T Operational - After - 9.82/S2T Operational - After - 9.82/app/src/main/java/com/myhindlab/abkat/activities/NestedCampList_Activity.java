package com.myhindlab.abkat.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.adapters.CampCalendarListAdapter;
import com.myhindlab.abkat.adapters.NestedCampListAdapter;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampNestedListModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.CampTypeResponseModel;
import com.myhindlab.abkat.models.DistrictList_Model;
import com.myhindlab.abkat.models.DivisionWiseTargetListModel;
import com.myhindlab.abkat.models.NestedCampListModel;
import com.myhindlab.abkat.pojos.DistrictList_Pojo;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NestedCampList_Activity extends AppCompatActivity implements NestedCampListAdapter.NestedListEvent {

    private Context context;
    private RecyclerView rv_campList;
    private EditText edtSelCampType;
    private ProgressDialog pd;
    private ApiInterface apiInterface;

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
    NestedCampListAdapter campCalendarListAdapter;
    private Button btnExport;
    private ProgressBar exportProgress;
    File exportFolder;
    private TextView tvTotalRegWorker;
    private ArrayList<DistrictList_Model> districtList_models;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestedcamplist);

        init();
        getSessionData();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = NestedCampList_Activity.this;
        apiInterface = ApiClient.getD2DClient().create(ApiInterface.class);

        pd = new ProgressDialog(context);
        rv_campList = findViewById(R.id.rv_campList);
        rv_campList.setHasFixedSize(true);
        rv_campList.setLayoutManager(new LinearLayoutManager(context));
        edtSelCampType = findViewById(R.id.edtSelCampType);
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
                if (Utilities.isNetworkAvailable(context)) {

                    getCampTypes();
                }

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
                        Collections.sort(campList, new CampCountComparatorReverse().reversed());
                        tvRegWorker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);

                    }
                    rv_campList.scrollToPosition(0);
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

    private void setDefaults() {
        totalTodaysCamp = new ArrayList<>();

        if (getIntent() != null) {
            TYPE = getIntent().getStringExtra("TYPE");
            selectedMonthId = getIntent().getIntExtra("selectedMonth", 0);
            selectedYearId = getIntent().getIntExtra("selectedYear", 0);
            DISTLGDCODE = getIntent().getStringExtra("selectedDist");
            campTypeId = getIntent().getIntExtra("campType", 0);
            ConstantData constantData = ConstantData.getInstance();
            constantData.setCampType(campTypeId);


            try {
//                campList = (ArrayList<CampCalendarModel.OutputBean>) getIntent().getSerializableExtra("campList");
//                totalTodaysCamp = (ArrayList<CampCalendarModel.OutputBean>) getIntent().getSerializableExtra("campList");
            } catch (Exception e) {
                campList = new ArrayList<>();
                totalTodaysCamp = new ArrayList<>();
            }
            getDistrictList();


        }
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
                new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));


            }
        });
        builderSingle.show();
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

            res = WebServiceCall.APICall(ApplicationConstants.GetMonthlySurveySiteRequestForOS, ApplicationConstants.webservice, param);
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
                    NestedCampListModel pojoDetails = new Gson().fromJson(result, NestedCampListModel.class);
                    String status = pojoDetails.getStatus();
                    String message = pojoDetails.getMessage();


                    if (status.equalsIgnoreCase("success")) {

                        ArrayList<CampNestedListModel> campNestedListModels = new ArrayList<>();

                        List<NestedCampListModel.Output> campList = new ArrayList<>();


                        for (int j = 0; j < districtList_models.size(); j++) {

                            campList = new ArrayList<>();
                            Long total = Long.valueOf(0);
                            for (int k = 0; k < pojoDetails.getOutput().size(); k++) {


                                if (pojoDetails.getOutput().get(k).getDistname().equalsIgnoreCase(districtList_models.get(j).getDISTNAME())) {
                                    NestedCampListModel.Output output = pojoDetails.getOutput().get(k);
                                    total = total + output.getRegisterworkers();
                                    campList.add(output);

                                }
                            }
                            campNestedListModels.add(new CampNestedListModel(districtList_models.get(j).getDISTNAME(), "2023/05/05", total, campList, false));

                        }


                        Log.d("TAG", "onPostExecute: " + campNestedListModels.size());

//                        Collections.sort(campList, new CampCountComparatorReverse());
                        campCalendarListAdapter = new NestedCampListAdapter(context, campNestedListModels, NestedCampList_Activity.this::onToggle);
                        rv_campList.setAdapter(campCalendarListAdapter);

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

    @Override
    public void onToggle(CampNestedListModel campNestedListModel, int position) {
//        campCalendarListAdapter.notifyDataSetChanged();


    }

    boolean isDistrictExist(ArrayList<String> distList, String dist) {
        boolean isExist = false;
        for (String s : distList
        ) {
            if (s.equalsIgnoreCase(dist)) {
                isExist = true;
            }

        }

        return isExist;
    }

    void getDistrictList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getAllDistrictList(2).enqueue(new Callback<DistrictList_Pojo>() {
            @Override
            public void onResponse(Call<DistrictList_Pojo> call, Response<DistrictList_Pojo> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        districtList_models = response.body().getOutput();
                        new GetMonthlySurveySiteRequestForOS().execute(String.valueOf(selectedMonthId), String.valueOf(selectedYearId), DISTLGDCODE, String.valueOf(campTypeId));

                    }
                }
            }

            @Override
            public void onFailure(Call<DistrictList_Pojo> call, Throwable t) {

            }
        });
    }


    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(campTypeId == 0 ? "All Camp List" : campTypeId == 1 ? "Regular Camp List" : "D2D Camp List");
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
                exportToExcel();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

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
