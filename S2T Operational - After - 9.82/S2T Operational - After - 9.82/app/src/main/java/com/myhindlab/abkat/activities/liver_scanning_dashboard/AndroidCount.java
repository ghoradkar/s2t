package com.myhindlab.abkat.activities.liver_scanning_dashboard;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.attendance_details.AttendanceDetailsActivity;
import com.myhindlab.abkat.activities.payout.InvoiceGenModel;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.adapter.AndroidAndIosCountAdapter;
import com.myhindlab.abkat.activities.payout.adapter.AndroidIosMobileCountAdapter;
import com.myhindlab.abkat.activities.payout.adapter.InvoiceAdapter;
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
import com.myhindlab.abkat.databinding.ActivityAndroidIosCountBinding;
import com.myhindlab.abkat.databinding.ActivityFibroScaningDataBinding;
import com.myhindlab.abkat.models.AndroidIosCountModel;
import com.myhindlab.abkat.models.AndroidMobileCountModel;
import com.myhindlab.abkat.models.AreaListModel;
import com.myhindlab.abkat.models.DistrictOrgModel;
import com.myhindlab.abkat.models.LandingLabModel;
import com.myhindlab.abkat.models.PincodeListModel;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AndroidCount extends AppCompatActivity {
    ActivityAndroidIosCountBinding binding;
    private UserSessionManager sessionManager;

    private List<AppointmentListModel.Output> districtList_models;

    private List<InvoiceMonthWiseDetailsnModel.Output> invoiceDetailsList;

    private List<BeneficiaryCountForPageLoadModel.Output> adminlist;

    private int mYear, mMonth, mDay, mYear1, mMonth1, mDay1;

    private String CampDate, CampDateToDate, fromDate, toDate;

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
    private String call_statusid = "1", monthId = "0", Type = "0", isSendForVerification, isInvoiceApproved, remarkId, otpnumber, yearId = "0", campTypeId = "0", teamId = "0", userInvoiceID = "0", landingLabId = "0", mobileNumber, EmpCode, divisionId = "0", pincodeArray, referenceId, size, areaArray, taluka, STATELGDCODE = "2", DISTLGDCODE = "0", agentId, oganizationId = "0", DESGID, district, TALLGDCODE = "0", LabCode;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionManager session;
    private SearchView searchView;

    private static final int REQUEST_PERMISSION_CODE = 1001;
    private final String downloadUrl = "http://5.178.98.212:9999/disha-t2t-Apis/api/access/master/countdata/downloadPatientDataExcel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAndroidIosCountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        init();
        getSessionData();
        setDefault();
        eventListener();
        setUpToolbar();
    }


    void init() {
        mContext = AndroidCount.this;
        sessionManager = new UserSessionManager(mContext);
        pd = new ProgressDialog(mContext);

        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter("refresh_exp_benf_list");
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);


        binding.rvPatientList.setLayoutManager(new LinearLayoutManager(mContext));


    }


    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(sessionManager.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                DESGID = json.getString("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                district = json.getString("district");
                TALLGDCODE = json.getString("TALLGDCODE");
                taluka = json.getString("taluka");
                EmpCode = json.getString("EmpCode");
                mobileNumber = json.getString("BMobile");
                LabCode = json.getString("LabCode");
                oganizationId = json.getString("SubOrgId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDefault() {
        if (getIntent() != null) {
            Type = getIntent().getStringExtra("Type");

        }

        getAndroidIosCount();

    }

    private void eventListener() {


//        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                getAndroidIosCount();
//
//            }
//        });


//        binding.btnSendForVerification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                mobileNumber = "8888658717";
//                new GetOtp(3).execute("3", EmpCode, userInvoiceID, EmpCode, mobileNumber);
//
////                verificationRemark("11", otpnumber);
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
        ImageButton imageButton = findViewById(R.id.btn_save_accordian);
        ImageButton imageButtonForRefresh = findViewById(R.id.btn_refresh);





        // Now set the title

//        if (Type.equalsIgnoreCase("1")) {
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("Android Total Patient");
//            }
//
//        } else if (Type.equalsIgnoreCase("2")) {
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("iOS Total Patient");
//            }
//
//        }


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleTextView = new TextView(this);

// Set the text, size, color, and other properties

        if (Type.equalsIgnoreCase("1")){
            titleTextView.setText("Android Total Patient");

        }else if (Type.equalsIgnoreCase("2")){
            titleTextView.setText("iOS Total Patient");

        }

        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);  // Set size in SP
        titleTextView.setTextColor(Color.WHITE);                    // Set text color
//        titleTextView.setTypeface(Typeface.DEFAULT_BOLD);            // Optional, bold text

        toolbar.addView(titleTextView);



        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());

        imageButtonForRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAndroidIosCount();

            }
        });

        imageButton.setVisibility(View.VISIBLE);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadExcelFile();

//                showDownloadReportDialog();


//                Dialog dialog;
//                dialog = new Dialog(mContext);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.filter_patient_count_dialoge);
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


    private void showDownloadReportDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.filter_patient_count_dialoge);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width = (int) (dm.widthPixels * 0.95); // 90% of screen
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }




        FrameLayout root = dialog.findViewById(android.R.id.content);
        View parent = ((ViewGroup) root.getChildAt(0)).getRootView();
        parent.setBackgroundResource(R.drawable.bg_dialog_rounded);

        TextView tvFromDate = dialog.findViewById(R.id.tvFromDate);
        TextView tvToDate = dialog.findViewById(R.id.tvToDate);
        ImageView ivFromCalendar = dialog.findViewById(R.id.ivFromCalendar);
        ImageView ivToCalendar = dialog.findViewById(R.id.ivToCalendar);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnApply = dialog.findViewById(R.id.btnApply);
        RelativeLayout rl_from_date = dialog.findViewById(R.id.rl_from_date);
        RelativeLayout rl_to_date = dialog.findViewById(R.id.rl_to_date);
        RadioGroup radioGroupType = dialog.findViewById(R.id.radioGroupType);
        RadioButton rbMonthly = dialog.findViewById(R.id.rbMonthly);
        RadioButton rbQuarterly = dialog.findViewById(R.id.rbQuarterly);



        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);




        tvFromDate.setText(Utilities.dfDate8.format(new Date()));
        fromDate = Utilities.dfDate8.format(new Date());


        tvToDate.setText(Utilities.dfDate8.format(new Date()));
        toDate = Utilities.dfDate8.format(new Date());




        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rbMonthly) {
                rbMonthly.setTypeface(null, Typeface.BOLD);
                rbQuarterly.setTypeface(null, Typeface.NORMAL);
                rbMonthly.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                rbQuarterly.setTextColor(ContextCompat.getColor(mContext,R.color.rb_color));
            } else if (checkedId == R.id.rbQuarterly) {
                rbQuarterly.setTypeface(null, Typeface.BOLD);
                rbMonthly.setTypeface(null, Typeface.NORMAL);
                rbQuarterly.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                rbMonthly.setTextColor(ContextCompat.getColor(mContext,R.color.rb_color));

            }

        });




        rl_from_date.setOnClickListener(v -> showDatePicker(tvFromDate,tvToDate,rbMonthly,rbQuarterly));
        rl_to_date.setOnClickListener(v -> showDatePicker(tvFromDate,tvToDate,rbMonthly,rbQuarterly));

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnApply.setOnClickListener(v -> {
             fromDate = tvFromDate.getText().toString();
             toDate = tvToDate.getText().toString();

            Toast.makeText(this, "From: " + fromDate + "\nTo: " + toDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }


    private void showDatePicker(TextView targetTextView,TextView toDateNew,RadioButton rbMonthly,RadioButton rbQuarterly) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    String formattedFromDate = dayOfMonth + " " +
                            new DateFormatSymbols().getMonths()[month] + " " + year;

                    targetTextView.setText(formattedFromDate);

                    // Auto calculate To Date
                    Calendar toDate = (Calendar) selectedDate.clone();

                    if (rbMonthly.isChecked()) {
                        toDate.add(Calendar.DAY_OF_MONTH, 30);
                    } else if (rbQuarterly.isChecked()) {
                        toDate.add(Calendar.DAY_OF_MONTH, 90);
                    }

                    String formattedToDate =
                            toDate.get(Calendar.DAY_OF_MONTH) + " " +
                                    new DateFormatSymbols().getMonths()[toDate.get(Calendar.MONTH)] + " " +
                                    toDate.get(Calendar.YEAR);

                    toDateNew.setText(formattedToDate);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


//    private void downloadExcelFile() {
//        OkHttpClient client = new OkHttpClient();
//
//        // If the API requires a body, customize it here.
//        RequestBody requestBody = RequestBody.create("", MediaType.parse("application/json"));
//
//        Request request = new Request.Builder()
//                .url(downloadUrl)
//                .post(requestBody) // <-- POST instead of GET
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                runOnUiThread(() ->
//                        Toast.makeText(AndroidCount.this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
//                );
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                if (!response.isSuccessful()) {
//                    runOnUiThread(() ->
//                            Toast.makeText(AndroidCount.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show()
//                    );
//                    return;
//                }
//
//                try {
//                    InputStream inputStream = response.body().byteStream();
//                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                    File file = new File(downloadDir, "PatientData.xlsx");
//                    FileOutputStream outputStream = new FileOutputStream(file);
//
//                    byte[] buffer = new byte[4096];
//                    int bytesRead;
//
//                    while ((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//
//                    outputStream.close();
//                    inputStream.close();
//
//                    runOnUiThread(() ->
//                            Toast.makeText(AndroidCount.this, "File saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show()
//                    );
//                } catch (Exception e) {
//                    runOnUiThread(() ->
//                            Toast.makeText(AndroidCount.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
//                    );
//                }
//            }
//        });
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                downloadExcelFile();
//            } else {
//                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void getAndroidIosCount() {
        final ProgressDialog progressDialog = new ProgressDialog(AndroidCount.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.super_admin_call().create(ApiInterface.class);
        Call<AndroidMobileCountModel> call = apiService.getAndroidIosMoblieCount();

        call.enqueue(new Callback<AndroidMobileCountModel>() {
            @Override
            public void onResponse(Call<AndroidMobileCountModel> call, Response<AndroidMobileCountModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    AndroidMobileCountModel model = response.body();
                    if (model != null && "1".equalsIgnoreCase(model.getStatus())) {
                        AndroidMobileCountModel.Details details = model.getDetails();
                        if (details != null && details.getCount() != null && !details.getCount().isEmpty()) {
                            List<AndroidMobileCountModel.Count> countList = details.getCount();
                            if (Type.equalsIgnoreCase("1")) {
                                binding.tvTotal.setText("" + countList.get(countList.size() - 1).getAndroid());
                            } else if (Type.equalsIgnoreCase("2")) {
                                binding.tvTotal.setText("" + countList.get(countList.size() - 1).getiOS_Count());
                            }

                            countList.remove(countList.size() - 1);


                            if (Type != null) {
                                if (Type.equalsIgnoreCase("1")) {
                                    AndroidAndIosCountAdapter androidAndIosCountAdapter = new AndroidAndIosCountAdapter(AndroidCount.this, countList);
                                    binding.rvPatientList.setAdapter(androidAndIosCountAdapter);

                                } else if (Type.equalsIgnoreCase("2")) {


                                    AndroidIosMobileCountAdapter andIosCountAdapter = new AndroidIosMobileCountAdapter(AndroidCount.this, countList);
                                    binding.rvPatientList.setAdapter(andIosCountAdapter);

                                }
                            }


//                            binding.swipeRefreshLayout.setRefreshing(false);


                        } else {
                            Toast.makeText(AndroidCount.this, "No data found", Toast.LENGTH_SHORT).show();
//                            binding.swipeRefreshLayout.setRefreshing(false);

                        }
                    } else {
                        Toast.makeText(AndroidCount.this, "Failed to get data", Toast.LENGTH_SHORT).show();
//                        binding.swipeRefreshLayout.setRefreshing(false);

                    }
                } else {
                    Toast.makeText(AndroidCount.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AndroidMobileCountModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("API Error", "Failure: " + t.getLocalizedMessage());
                Toast.makeText(AndroidCount.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadExcelFile() {
        final ProgressDialog progressDialog = new ProgressDialog(AndroidCount.this);
        progressDialog.setMessage("Please wait . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiService = ApiClient.super_admin_call_download_excel().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.downloadExcelFile(new Object());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = writeResponseBodyToDisk(response.body());
                    Toast.makeText(AndroidCount.this, success ? "Download complete" : "Download failed", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AndroidCount.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AndroidCount.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "PatientData_" + timeStamp + ".xlsx";

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);


//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PatientData.xlsx");

            Utilities.showFileDownloadedNotification(mContext, file);

            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) break;
                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();
                return true;

            } catch (Exception e) {
                return false;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }

        } catch (Exception e) {
            return false;
        }
    }


//    public void showNotification(boolean success) {
//        String channelId = "download_channel";
//        String channelName = "Download Notifications";
//
//        // 1. Create notification channel (once)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    channelName,
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        // 2. Build the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(android.R.drawable.stat_sys_download_done)
//                .setContentTitle("Download Status")
//                .setContentText(success ? "Download complete" : "Download failed")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        // 3. Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(1, builder.build());
//    }


}