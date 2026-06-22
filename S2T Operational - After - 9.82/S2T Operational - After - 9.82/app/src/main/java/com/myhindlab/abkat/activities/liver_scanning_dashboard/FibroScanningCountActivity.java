package com.myhindlab.abkat.activities.liver_scanning_dashboard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.doortodoor.D2DPatientRegistration_Activity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.adapter.LiverCountDistrictWiseAdapter;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.adapter.AndroidAndIosCountAdapter;
import com.myhindlab.abkat.activities.payout.adapter.AndroidIosMobileCountAdapter;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceDetailsMonthWiseAdapter;
import com.myhindlab.abkat.activities.payout.model.InvoiceMonthWiseDetailsnModel;
import com.myhindlab.abkat.activities.payout.model.VerificationRemarkModel;
import com.myhindlab.abkat.activities.re_registration.adapter.RejectDetailsAdminAdapter;
import com.myhindlab.abkat.activities.re_registration.model.BeneficiaryCountForPageLoadModel;
import com.myhindlab.abkat.adapters.AreaListSelectAdapter;
import com.myhindlab.abkat.adapters.DistrictListSelectAdapter;
import com.myhindlab.abkat.adapters.DivisionListSelectAdapter;
import com.myhindlab.abkat.adapters.LabListListSelectAdapter;
import com.myhindlab.abkat.adapters.OrganizationListSelectAdapter;
import com.myhindlab.abkat.adapters.PincodeListSelectAdapter;
import com.myhindlab.abkat.adapters.TalukaListSelectAdapter;
import com.myhindlab.abkat.appointment_confirmation.pojo.AppointmentListModel;
import com.myhindlab.abkat.appointment_confirmation.pojo.ReportlistResponse;
import com.myhindlab.abkat.databinding.ActivityFibroScaningDataBinding;
import com.myhindlab.abkat.databinding.ActivityRaiseRequestBinding;
import com.myhindlab.abkat.models.AndroidMobileCountModel;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.CampCalendarModel;
import com.myhindlab.abkat.models.CampTypeModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.FibroscanDistrictWiseCountModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
import com.myhindlab.abkat.models.ScreenedDependentModel;
import com.myhindlab.abkat.models.SubDivisionModel;
import com.myhindlab.abkat.models.SubOrganizationModel;
import com.myhindlab.abkat.models.TalukaListForFilterModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FibroScanningCountActivity extends AppCompatActivity implements LiverCountDistrictWiseAdapter.onTouchListner {
    ActivityFibroScaningDataBinding binding;
    private UserSessionManager sessionManager;

//    private ProgressBar exportProgress;

    private ProgressDialog progressDialog;


    File exportFolder;


    private List<AppointmentListModel.Output> districtList_models;

    private List<FibroscanDistrictWiseCountModel.Output> invoiceDetailsList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private List<FibroscanDistrictWiseCountModel.Output> districtWiseDataList;


    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, CampDateToDate, type, toDate;

    private ProgressDialog pd;
    private AlertDialog teamDialog;
    private Context mContext;
    private List<DistrictOrgModel.Output> districtList;
    private List<AreaListModel.Output> areaList;
    private List<PincodeListModel.Output> pincodeList;
    private List<SubDivisionModel.Output> divisionList;

    private List<SubOrganizationModel.Output> organizationList;
    private List<TalukaListForFilterModel.Output> talukaList;
    private List<LandingLabModel.Output> landingLabList;
    private String call_statusid = "1", monthId = "0", isSendForVerification, isInvoiceApproved, remarkId, otpnumber, yearId = "0", campTypeId = "0", teamId = "0", userInvoiceID = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2",
            DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFibroScaningDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = FibroScanningCountActivity.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));


        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


//        Calendar todayCal = Calendar.getInstance();
//        todayCal.add(Calendar.DAY_OF_MONTH, -22);
//
        Calendar toCal = Calendar.getInstance();
        toCal.add(Calendar.DAY_OF_MONTH, -10);

        binding.Todate.setText(Utilities.dfDate.format(new Date()));
        binding.fromDate.setText(Utilities.dfDate.format(toCal.getTime()));


        CampDate = Utilities.dfDate.format(toCal.getTime());
        CampDateToDate = Utilities.dfDate.format(new Date());


    }


    private void verificationRemark(String mno, String otp) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.prompot_verify_remark, null);
        alertBuilder.setView(alertLayout);
        EditText tv_remark = alertLayout.findViewById(R.id.tv_remark);
        EditText tv_otp = alertLayout.findViewById(R.id.tv_otp);
        Button verifyOtpBtn = alertLayout.findViewById(R.id.verifyOtpBtn);
        Button resendOtpBtn = alertLayout.findViewById(R.id.resendOtpBtn);
        EditText tv_remark_description = alertLayout.findViewById(R.id.tv_remark_description);
        LinearLayout ll_main_others = alertLayout.findViewById(R.id.ll_main_others);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertBuilder.setPositiveButton("ok", (dialog, which) -> finish());
            }
        });


        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");
//                DISTLGDCODE = json.getString("DISTLGDCODE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
        if (getIntent() != null) {
            monthId = getIntent().getStringExtra("month");
            yearId = getIntent().getStringExtra("year");
            userInvoiceID = getIntent().getStringExtra("userInvoice");
            isSendForVerification = getIntent().getStringExtra("isSendForVerification");
            isInvoiceApproved = getIntent().getStringExtra("raisedStatus");


        }

        getDistrictwiseData();


    }

    private void eventListener() {


        binding.Todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.fromDate.getText().toString().equals(""))
                    Utilities.showMessageString("Please Select From Date", mContext);
                else {

                    DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    binding.Todate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));

                                    CampDateToDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);


                                    getDistrictwiseData();
                                }

                            }, mYear, mMonth, mDay);

                    Calendar c = Calendar.getInstance();
//                    try {
//                        c.setTime(Utilities.dfDate.parse(CampDate));
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    c.add(Calendar.DAY_OF_MONTH, 14);

                    try {
                        dpd1.getDatePicker().setCalendarViewShown(false);
                        dpd1.getDatePicker().setMaxDate(c.getTimeInMillis());

                        // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dpd1.show();
                }
            }
        });


        binding.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd1 = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.fromDate.setText(Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year));
                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);

                                binding.Todate.setText("");

//                                CampDate = Utilities.ConvertDateFormat(Utilities.dfDate, dayOfMonth, monthOfYear + 1, year);
                            }

                        }, mYear, mMonth, mDay);
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                //  c.add(Calendar.DAY_OF_MONTH, 15);

                try {
                    dpd1.getDatePicker().setCalendarViewShown(false);

//                    dpd1.getDatePicker().setCalendarViewShown(false);
//                    dpd1.getDatePicker().setMinDate(c.getTimeInMillis());
//                    dpd1.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

                    // dpd1.getDatePicker().setMinDate(System.currentTimeMillis());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpd1.show();
            }
        });


        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToExcel();
            }
        });


//        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                getDistrictwiseData();
//
//            }
//        });


    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar; // Assuming your layout has a Toolbar with id 'toolbar'
        setSupportActionBar(toolbar);
        ImageButton imageButton = findViewById(R.id.btn_refresh);


        // Now set the title
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("FibroScanning Patient Data");
//        }


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleTextView = new TextView(this);

// Set the text, size, color, and other properties
        titleTextView.setText("FibroScanning Patient Data");
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);  // Set size in SP
        titleTextView.setTextColor(Color.WHITE);                    // Set text color
//        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);            // Optional, bold text

        toolbar.addView(titleTextView);


        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDistrictwiseData();

//                Dialog dialog;
//                dialog = new Dialog(FibroScanningCountActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.raise_info_dialoge);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                ImageView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
//                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

            }
        });


    }


    private void getCount() {

        final ProgressDialog progressDialog = new ProgressDialog(FibroScanningCountActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.GetDashboardCountForLiverScanning().create(ApiInterface.class);
        apiInterface.getLiverDashboardCount().enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        Log.i("TAG", "onResponse: " + responseString);

                        JSONArray jsonArray = new JSONArray(responseString);

                        if (jsonArray.length() > 0) {

                            String fName = jsonArray.getJSONObject(0).getString("TillDateScanningCount") == "null" ? "" : jsonArray.getJSONObject(0).getString("TillDateScanningCount");
                            String mName = jsonArray.getJSONObject(0).getString("TodayScanningCount") == "null" ? "" : jsonArray.getJSONObject(0).getString("TodayScanningCount");
                            String lName = jsonArray.getJSONObject(0).getString("MachineInstalledCount") == "null" ? "" : jsonArray.getJSONObject(0).getString("MachineInstalledCount");


                            // Process the JSON array
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                // Extract necessary fields from jsonObject
//
//
//                            }
                        } else {
                            Log.w("TAG", "No data found in the response.");
                        }

                    } else {
                        Log.e("TAG", "Response error: " + response.code() + " - " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Exception: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG", "API call failed: " + t.getMessage());
            }
        });
    }


    private void getDistrictwiseData() {
        final ProgressDialog progressDialog = new ProgressDialog(FibroScanningCountActivity.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiService = ApiClient.GetDashboardCountForLiverScanning().create(ApiInterface.class);
        Log.d("User Sessions", "getReportlist: " + new Gson().toJson(sessionManager.getUserDetailsJson()));
        Call<FibroscanDistrictWiseCountModel> call = apiService.getDistrictwiseDataLiverScan(CampDate, CampDateToDate, EmpCode, DESGID, oganizationId, DISTLGDCODE);
        call.enqueue(new Callback<FibroscanDistrictWiseCountModel>() {
            @Override
            public void onResponse(Call<FibroscanDistrictWiseCountModel> call, Response<FibroscanDistrictWiseCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    if (status.equalsIgnoreCase("Success")) {
                        districtWiseDataList = response.body().getOutput();

//                        districtWiseDataList = respons.body().getOutput();
                        LiverCountDistrictWiseAdapter liverCountDistrictWiseAdapter = new LiverCountDistrictWiseAdapter(FibroScanningCountActivity.this, districtWiseDataList, FibroScanningCountActivity.this);
                        binding.rvPatientList.setAdapter(liverCountDistrictWiseAdapter);


                        int t = 0;
                        int s = 0;
                        int u = 0;
                        int v = 0;
                        int w = 0;
                        for (FibroscanDistrictWiseCountModel.Output output :
                                districtWiseDataList) {

                            t = t + (output.getTotalShots());
                            s = s + (output.getPatientCount());
                            u = u + (output.getAbnormalPatientCount());
                            v = v + (output.getSuccessfullShots());
                            w = w + (output.getModerateSevereCount());


                        }

                        binding.tvTotalCount.setText("" + t);
                        binding.tvPatientTotal.setText("" + s);
                        binding.tvAbnormalTotal.setText("" + u);
                        binding.tvTotalSuccessfull.setText("" + v);
                        binding.tvWarrantedTotal.setText(""+w);

//                        binding.tvWarrantedTotal.setText("1234567890");


//                        binding.swipeRefreshLayout.setRefreshing(false);

                    } else {
                        LiverCountDistrictWiseAdapter liverCountDistrictWiseAdapter = new LiverCountDistrictWiseAdapter(FibroScanningCountActivity.this, new ArrayList<>(), FibroScanningCountActivity.this);
                        binding.rvPatientList.setAdapter(liverCountDistrictWiseAdapter);
                        Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);

//                        binding.swipeRefreshLayout.setRefreshing(false);

                    }
                } else {
                    Utilities.showAlertDialog(mContext, "Alert", "Details Not Found", false);
//                    binding.swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onFailure(Call<FibroscanDistrictWiseCountModel> call, Throwable t) {
                t.getLocalizedMessage();
                progressDialog.dismiss();

            }
        });

    }


    @Override
    public void onDataClick(FibroscanDistrictWiseCountModel.Output item) {

        startActivity(new Intent(mContext, FibroScanningCountForDistrictActivity.class)
                .putExtra("fromDate", CampDate)
                .putExtra("toDate", CampDateToDate)
                .putExtra("districtCode", String.valueOf(item.getDistlgdcode()))
                .putExtra("districtName", item.getDistrict())


        );

    }


    void exportToExcel() {
        if (districtWiseDataList != null && districtWiseDataList.size() > 0) {

            // Show progress
            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Exporting data, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(() -> {
                try {
                    File exportFolder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Android 10 and above - Use public Downloads folder
                        exportFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    } else {
                        // Android 9 and below - manually create Downloads folder path
                        exportFolder = new File(Environment.getExternalStorageDirectory() + "/Download/");
                        if (!exportFolder.exists()) {
                            exportFolder.mkdirs();
                        }
                    }

                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
                    String fileName = "LiverScanningDistrictWiseData_" + timeStamp + ".xls";
                    File file = new File(exportFolder, fileName);

                    if (file.exists()) file.delete();

                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
                    WritableSheet sheet = workbook.createSheet("Liver Scanning District Data", 0);


                    WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
                    WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
                    cellFormat.setAlignment(Alignment.CENTRE); // Horizontal center
                    cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // Optional: vertical center
                    cellFormat.setWrap(true); // Optional: wrap text

                    sheet.addCell(new Label(0, 0, " District ", cellFormat));
                    sheet.addCell(new Label(1, 0, " Patient Count ", cellFormat));
                    sheet.addCell(new Label(2, 0, " Abnormal Patient ", cellFormat));
                    sheet.addCell(new Label(3, 0, " Successful Shot ", cellFormat));
                    sheet.addCell(new Label(4, 0, " Total Shots ", cellFormat));
                    sheet.addCell(new Label(5, 0, " Referral warranted (Moderate-Severe) ", cellFormat));

                    for (int i = 0; i < districtWiseDataList.size(); i++) {
                        int row = i + 1;
                        sheet.addCell(new Label(0, row, districtWiseDataList.get(i).getDistrict(), cellFormat));
                        sheet.addCell(new Label(1, row, String.valueOf(districtWiseDataList.get(i).getPatientCount()), cellFormat));
                        sheet.addCell(new Label(2, row, String.valueOf(districtWiseDataList.get(i).getAbnormalPatientCount()), cellFormat));
                        sheet.addCell(new Label(3, row, String.valueOf(districtWiseDataList.get(i).getSuccessfullShots()), cellFormat));
                        sheet.addCell(new Label(4, row, String.valueOf(districtWiseDataList.get(i).getTotalShots()), cellFormat));
                        sheet.addCell(new Label(5, row, String.valueOf(districtWiseDataList.get(i).getModerateSevereCount()), cellFormat));
                    }

                    // Auto size columns
                    for (int col = 0; col <= 4; col++) {
                        CellView cell = sheet.getColumnView(col);
                        cell.setAutosize(true);
                        sheet.setColumnView(col, cell);
                    }

                    workbook.write();
                    workbook.close();

                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "File exported to Downloads", Toast.LENGTH_SHORT).show();

                        try {
                            Intent intent;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri uri = FileProvider.getUriForFile(mContext,
                                        mContext.getPackageName() + ".provider", file);
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            mContext.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "File saved, but no app to open it.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }).start();
        }
    }

}